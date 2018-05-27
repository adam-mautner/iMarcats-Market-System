package com.imarcats.market.engine.order;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.imarcats.interfaces.client.v100.exception.MarketRuntimeException;
import com.imarcats.interfaces.client.v100.notification.ObjectVersion;
import com.imarcats.interfaces.server.v100.order.QuoteOrderPrecedenceRule;
import com.imarcats.internal.server.infrastructure.datastore.MarketDatastore;
import com.imarcats.internal.server.infrastructure.datastore.OrderDatastore;
import com.imarcats.internal.server.interfaces.market.MarketInternal;
import com.imarcats.internal.server.interfaces.order.OrderBookEntryInternal;
import com.imarcats.internal.server.interfaces.order.OrderBookInternal;
import com.imarcats.internal.server.interfaces.order.OrderInternal;
import com.imarcats.internal.server.interfaces.order.OrderManagementContext;
import com.imarcats.internal.server.interfaces.order.OrderBookEntrySize;
import com.imarcats.market.engine.matching.OrderMatcherBase;
import com.imarcats.market.engine.service.MarketServiceInternalFacade;
import com.imarcats.model.OrderBookEntryModel;
import com.imarcats.model.OrderBookModel;
import com.imarcats.model.Order;
import com.imarcats.model.OrderBookEntryFactory;
import com.imarcats.model.OrderBookFactory;
import com.imarcats.model.types.OrderSide;
import com.imarcats.model.types.OrderState;
import com.imarcats.model.types.OrderType;
import com.imarcats.model.types.Quote;
import com.imarcats.model.types.QuoteAndSize;
import com.imarcats.model.types.SecondaryOrderPrecedenceRuleType;

/**
 * Implementation for the Order Book Internal functionality. 
 * 
 * Note: Non-Synchronized Class requires external synchronization.
 * 
 * @author Adam
 */
public class OrderBookImpl implements OrderBookInternal {
	
	protected final MarketInternal _market;
	protected final OrderBookModel _orderBookModel;
	protected final QuoteOrderPrecedenceRule _primaryOrderPrecedenceRule;
	protected final SecondaryOrderPrecedenceRule _compositeSecondaryOrderPrecedenceRule;
	private final MarketDatastore _marketDatastore;
	private final OrderDatastore _orderDatastore;

	/**
	 * Caches the Orders, because Order Book only have a list of Order Key, 
	 * the current Datastore Implementation does not show changes in the recent
	 * Transaction, if the Order object is reloaded, so we need to cache the current Order objects. 
	 * TODO: Revise this, when we switch to a different Datastore Implementation.  
	 */
	private final Map<Long, OrderInternal> _orderCache = new HashMap<Long, OrderInternal>();
	
	public OrderBookImpl(OrderBookModel orderBookModel_, 
			MarketInternal market_, 
			OrderDatastore orderDatastore_,
			MarketDatastore marketDatastore_) {
		super();
		_market = market_;
		_orderBookModel = orderBookModel_;
		_orderDatastore = orderDatastore_;
		_marketDatastore = marketDatastore_;
		
		_primaryOrderPrecedenceRule = 
			new QuoteOrderPrecedenceRule(orderBookModel_.getSide(), 
					market_.getMinimumQuoteIncrement(), market_.getQuoteType());
		
		SecondaryOrderPrecedenceRule[] rules = 
			new SecondaryOrderPrecedenceRule[market_.getSecondaryOrderPrecedenceRules().length];
		
		for (int i = 0; i < rules.length; i++) {
			rules[i] = 
				SecondaryOrderPrecedenceRuleRegistry.getSecondaryPrecedence(market_.getSecondaryOrderPrecedenceRules()[i]);
		}
		
		_compositeSecondaryOrderPrecedenceRule = new CompositePrecedenceRule(rules);		
	}
	
	@Override
	public int aggregatedSizeOfMarketOrders() {
		int aggregatedSizeOfMarketOrders = 0;
		if(!isEmpty()) {
			OrderBookEntryModel entry = _orderBookModel.get(0);
			
			for (Iterator<Long> iterator = entry.getOrderKeyIterator(); iterator.hasNext();) {
				OrderInternal order = getOrderByKey(iterator.next());
				if(order.getType() == OrderType.Limit) {
					break;
				}
				aggregatedSizeOfMarketOrders += order.getRemainingSize();
			}
		}
		return aggregatedSizeOfMarketOrders;
	}

	@Override
	public boolean hasOnlyMarketOrders() {
		boolean hasOnlyMarketOrders = true;
		if(!isEmpty()) {
			for (Iterator<OrderBookEntryModel> iterator = _orderBookModel.getEntryIterator(); iterator.hasNext();) {
				OrderBookEntryModel entry = iterator.next();
				if(entry.getOrderType() == OrderType.Limit) {
					hasOnlyMarketOrders = false;
					break;
				}
			}
		}
		
		return hasOnlyMarketOrders;
	}

	@Override
	public boolean canOrderBeMatched(OrderInternal orderInternal_, MarketInternal market_) {
		MatchTester matchTester = new MatchTester(orderInternal_);
		return matchTester.canOrderBeMatched(market_);
	}
	
	@Override
	public boolean isEmpty() {
		return _orderBookModel.isEmpty();
	}
	
	@Override
	public OrderInternal getBestOrder() {
		Order order = getBestOrderModel();
		OrderImpl orderImpl = null;
		if(order != null) {
			orderImpl = new OrderImpl(order, _marketDatastore);
		}
		return orderImpl;
	}

	@Override
	public OrderBookModel getOrderBookForClient() {
		OrderBookModel book = OrderBookFactory.createBook(getSide());
		
		for (Iterator<OrderBookEntryModel> iterator = _orderBookModel.getEntryIterator(); iterator.hasNext();) {
			OrderBookEntryModel entry = iterator.next();
			OrderBookEntryModel bookEntry = getOrderBookEntryForClient(entry); 
			
			if(book.size() == MarketServiceInternalFacade.MAX_NUMBER_OF_ORDER_BOOK_ENTRIES_RETURNED) {
				break;
			}
			
			if(!entry.isOrderKeyListEmpty()) {
				book.add(bookEntry);
			}
		}
		
		return book;
	}
	
	private OrderBookEntryModel getOrderBookEntryForClient(OrderBookEntryModel entry_) {
		OrderBookEntryModel entry = OrderBookEntryFactory.createEntry(entry_.getOrderSide());
		
		if(!entry_.isOrderKeyListEmpty()) {

			entry.setOrderType(entry_.getOrderType());
			entry.setLimitQuote(entry_.getLimitQuote());
			
			OrderBookEntrySize sizeObject = OrderBookEntryImpl.getSize(true, entry_, this);
			
			entry.setAggregateSize(sizeObject.getAggregateSize());
			entry.setHasHiddenOrders(sizeObject.getHasHiddenOrders());
			
		}
		
		return entry;
	}
	
	protected Order getBestOrderModel() {
		Order order = null;
		
		OrderBookEntryModel entry = getBestOrderBookEntryModel();
		
		if(entry != null && entry.orderKeyCount() != 0) {
			order = getOrderByKey(entry.getOrderKey(0)).getOrderModel();
		}
		return order;
	}

	protected OrderInternal getOrderByKey(Long orderKey_) {
		OrderInternal orderInternal = _orderCache.get(orderKey_);
		if(orderInternal == null) {
			orderInternal = getOrderByKeyUncached(orderKey_, _orderDatastore);
			// only add to the cache, if persistence manager not null
			// order loaded using null persistence manager cannot be saved back 
			// TODO: Revise this, once new datastore implementation is used 
//			if(pm_ != null) {				
				_orderCache.put(orderKey_, orderInternal);
//			}
		}
		return orderInternal;
	}

	private static OrderInternal getOrderByKeyUncached(Long orderKey_, OrderDatastore orderDatastore_) {
		OrderInternal orderInternal = getOrder(orderKey_, orderDatastore_);
		if(orderInternal == null) {
			throw MarketRuntimeException.createExceptionWithDetails(MarketRuntimeException.NON_EXISTENT_ORDER, 
					null, new Object[] { "Order Key=" + orderKey_ });
		}
		return orderInternal;
	}
	
	private static OrderInternal getOrder(Long orderKey_, OrderDatastore orderDatastore_) {
		return orderDatastore_.findOrderBy(orderKey_);
	}

	private OrderBookEntryModel getBestOrderBookEntryModel() {
		OrderBookEntryModel entry = null;
		if(!_orderBookModel.isEmpty()) {
			entry = _orderBookModel.get(0);
		}
		return entry;
	}
	
	@Override
	public void recordSubmit(OrderInternal order_, OrderManagementContext orderManagementContext_) {
		addOrder(order_, orderManagementContext_);
		order_.recordSubmit(orderManagementContext_);
	}
	
	@Override
	public void recordCancel(OrderInternal order_, String cancellationCommentLanguageKey_, OrderManagementContext orderManagementContext_) {
		deleteOrder(order_, orderManagementContext_);
		order_.recordCancel(cancellationCommentLanguageKey_, orderManagementContext_);
	}
	
	@Override
	public void recordExecution(OrderInternal order_, int executedSize_, OrderManagementContext orderManagementContext_) {
		order_.recordExecution(_market, executedSize_, orderManagementContext_);
		if(order_.getState() == OrderState.Executed) {
			// remove the fully executed Orders from Book
			deleteOrder(order_, orderManagementContext_);
		} else {
			int orderEntryIndex = 
				_orderBookModel.binarySearch(OrderBookEntryFactory.createEntry(order_.getOrderModel(), _orderBookModel), _primaryOrderPrecedenceRule);
				
			if(orderEntryIndex >= 0) {
				OrderBookEntryModel entry = 
					(OrderBookEntryModel) _orderBookModel.get(orderEntryIndex);
				
				OrderBookEntrySize sizeObject = OrderBookEntryImpl.getSize(true, entry, this);
				QuoteAndSize newLevelIIQuote = createLevelIIQuote(order_.getOrderModel(), true, sizeObject.getAggregateSize());
				recordLevelIIQuote(order_.getOrderModel().getTargetMarketCode(), newLevelIIQuote, sizeObject.getHasHiddenOrders(), orderManagementContext_);
			}
		}
	}

	@Override
	public OrderBookEntryInternal getBestOrderBookEntry() {
		OrderBookEntryModel entry = getBestOrderBookEntryModel();
		OrderBookEntryImpl orderBookEntryImpl = null;
		if(entry != null && entry.orderKeyCount() != 0) {
			orderBookEntryImpl = new OrderBookEntryImpl(entry, this);
		}
		return orderBookEntryImpl;
	}
	
	// TODO: Move to the User Implementation
	/*
	@Override
	public IOrderBookEntry[] getOrderBookEntries() {
		List<IOrderBookEntry> entries = new ArrayList<IOrderBookEntry>();
		for (Iterator entryIt = _orderBookModel.getOrderBookEntries().iterator(); entryIt.hasNext();) {
			OrderBookEntry entryModel = (OrderBookEntry) entryIt.next();
			entries.add(new OrderBookEntryImpl(entryModel));
		}
		return entries.toArray(new IOrderBookEntry[entries.size()]);
	}
    */
	// End TODO
	
	@Override
	public SecondaryOrderPrecedenceRuleType[] getSecondaryOrderPrecedenceRules() {
		return _market.getSecondaryOrderPrecedenceRules();
	}


	@Override
	public OrderSide getSide() {
		return _orderBookModel.getSide();
	}
	
	protected void addOrder(OrderInternal order_, OrderManagementContext orderManagementContext_) {
		_orderCache.put(order_.getKey(), order_);
		Order orderModel = order_.getOrderModel();
		
		OrderBookEntryModel newEntry = OrderBookEntryFactory.createEntry(orderModel, _orderBookModel);
		
		int orderEntryIndex = 
			_orderBookModel.binarySearch(newEntry, _primaryOrderPrecedenceRule);
			
		QuoteAndSize newLevelIIQuote = null;
		Boolean hasHiddenOrders = null;
		if(orderEntryIndex >= 0) {
			OrderBookEntryModel existingEntry = 
				(OrderBookEntryModel) _orderBookModel.get(orderEntryIndex);
			
			List<Order> orderList = getOrdersByKey(existingEntry, _orderDatastore);
			
			int orderIndex = Collections.binarySearch(orderList, orderModel, _compositeSecondaryOrderPrecedenceRule);
			if(orderIndex >= 0) {
				// inserts after the existing one
				existingEntry.addOrderKey(orderIndex + 1, orderModel.getKey(), _orderBookModel);
			} else {
				addNewItem(existingEntry, orderModel.getKey(), orderIndex);
				
				System.out.println("Order ID added: " + order_.getKey());
			}
			
			OrderBookEntrySize sizeObject = OrderBookEntryImpl.getSize(true, existingEntry, this);
			newLevelIIQuote = createLevelIIQuote(orderModel, true, sizeObject.getAggregateSize());
			hasHiddenOrders = sizeObject.getHasHiddenOrders();
		} else {
			addNewItem(newEntry, orderEntryIndex);		
			
			OrderBookEntrySize sizeObject = OrderBookEntryImpl.getSize(true, newEntry, this);
			newLevelIIQuote = createLevelIIQuote(orderModel, true, sizeObject.getAggregateSize());
			hasHiddenOrders = sizeObject.getHasHiddenOrders();
		}

//		_marketDatastore.updateMarket(_market.getMarketModel());
		
		recordLevelIIQuote(orderModel.getTargetMarketCode(), newLevelIIQuote, hasHiddenOrders, orderManagementContext_);
	}
	
	protected static List<Order> getOrdersByKey(OrderBookEntryModel entry_, OrderDatastore orderRegistry_) {
		List<Order> orders = new ArrayList<Order>();
		for (Iterator<Long> iterator = entry_.getOrderKeyIterator(); iterator.hasNext();) {
			Long key = (Long) iterator.next();
			orders.add(getOrderByKeyUncached(key, orderRegistry_).getOrderModel());
		}
		
		return orders;
	}
	
	protected void deleteOrder(OrderInternal order_, OrderManagementContext orderManagementContext_) {
		Order orderModel = order_.getOrderModel();
		
		OrderBookEntryModel deletedEntry = OrderBookEntryFactory.createEntry(orderModel, _orderBookModel);
		
		int orderEntryIndex = 
			_orderBookModel.binarySearch(deletedEntry, _primaryOrderPrecedenceRule);
			
		if(orderEntryIndex < 0) {
			throw new IllegalArgumentException("Order being deleted cannot be found in the Book");
		}
		
		// TODO: Use binary search here if possible 
		OrderBookEntryModel entryFound = _orderBookModel.get(orderEntryIndex);
		Long found = null;
		for (Iterator<Long> iterator = entryFound.getOrderKeyIterator(); iterator.hasNext();) {
			Long orderKey = (Long) iterator.next();
			if(orderModel.getKey().equals(orderKey)) {
				found = orderKey;
				break;
			}
		}
		
		if(found == null) {
			throw new IllegalArgumentException("Order being deleted cannot be found in the Book");
		}
		
		entryFound.removeOrderKey(found, _orderBookModel);
		_orderCache.remove(orderModel.getKey());
		
		System.out.println("Order ID removed: " + order_.getKey());
		
		QuoteAndSize newLevelIIQuote = null;
		Boolean hasHiddenOrders = null;
		if(entryFound.isOrderKeyListEmpty()) {
			_orderBookModel.remove(entryFound);
			
			newLevelIIQuote = createLevelIIQuote(orderModel, false, 0);
			
		} else {
			OrderBookEntrySize sizeObject = OrderBookEntryImpl.getSize(true, entryFound, this);
			newLevelIIQuote = createLevelIIQuote(orderModel, true, sizeObject.getAggregateSize());
			hasHiddenOrders = sizeObject.getHasHiddenOrders();
		}
		
//		_marketDatastore.updateMarket(_market.getMarketModel());
		
		recordLevelIIQuote(orderModel.getTargetMarketCode(), newLevelIIQuote, hasHiddenOrders, orderManagementContext_); 
	}

	private QuoteAndSize createLevelIIQuote(Order orderAffected_, boolean validQuote_, int bookEntrySize_) {
		QuoteAndSize newLevelIIQuote = new QuoteAndSize();
		
		newLevelIIQuote.setSize(bookEntrySize_);
		
		Quote quote = null;
		if(orderAffected_.getLimitQuoteValue() != null ) {
			quote = new Quote();
			quote.setQuoteValue(orderAffected_.getLimitQuoteValue().getQuoteValue());
			quote.setValidQuote(validQuote_);
		}
		
		newLevelIIQuote.setQuote(quote);
		
		return newLevelIIQuote;
	}

	/**
	 * Inserts a New Entry to the Book
	 * @param entry_
	 * @param indexFromBinarySearch_ (-(insertion point) - 1)
	 * 
	 * Note: Type safety is not enforced here
	 */
	protected void addNewItem(OrderBookEntryModel entry_, int indexFromBinarySearch_) {
		int index = getInsertionIndex(indexFromBinarySearch_);
		_orderBookModel.add(index, entry_);
	}

	private int getInsertionIndex(int indexFromBinarySearch_) {
		int index = -(indexFromBinarySearch_ + 1);
		return index;
	}
	
	/**
	 * Inserts a New Key to a List
	 * @param entry_
	 * @param key_
	 * @param indexFromBinarySearch_ (-(insertion point) - 1)
	 * 
	 * Note: Type safety is not enforced here
	 */
	protected void addNewItem(OrderBookEntryModel entry_, Long key_, int indexFromBinarySearch_) {
		int index = getInsertionIndex(indexFromBinarySearch_);
		entry_.addOrderKey(index, key_, _orderBookModel);
	}
	
	private void recordLevelIIQuote(String marketCode_, QuoteAndSize newLevelIIQuote_, Boolean hasHiddenOrders_, OrderManagementContext orderManagementContext_) {
		if(getSide() == OrderSide.Buy) {
			orderManagementContext_.getMarketDataSession().recordNewLevelIIBid(marketCode_, newLevelIIQuote_, hasHiddenOrders_, getUpdatedVersion());
		} else {
			orderManagementContext_.getMarketDataSession().recordNewLevelIIAsk(marketCode_, newLevelIIQuote_, hasHiddenOrders_, getUpdatedVersion());
		}
	}
	
	private ObjectVersion getUpdatedVersion() {
		ObjectVersion version = new ObjectVersion();
		// JPA has the current version of the object until it is committed to the datastore, 
		// but in the change notification messages version after update is needed (which is current version + 1) 
		if(_orderBookModel.getVersionNumber() != null) {			
			version.setVersion(_orderBookModel.getVersionNumber() + 1);
		} else {
			version.setVersion(0L);
		}
		
		return version;
	}
	
	// classes 
	class MatchTester {
		private final OrderInternal _orderInternal;
		private final int _minimumSize;
		private int _sizeMatched = 0;
		private boolean _cannotBeMatched;
		
		private MatchTester(OrderInternal orderInternal_) {
			super();
			_orderInternal = orderInternal_;
			_minimumSize = OrderMatcherBase.getMinimumRequiredExecutionSize(orderInternal_); ;
		}

		public boolean canOrderBeMatched(MarketInternal market_) {
			if(!isEmpty()) {
				for (Iterator<OrderBookEntryModel> iterator = _orderBookModel.getEntryIterator(); iterator.hasNext();) {
					OrderBookEntryModel entry = iterator.next();
					
					if(entry.getOrderType() == OrderType.Limit && _orderInternal.getType() == OrderType.Limit)  {
						Quote buyOrderQuote;
						Quote sellOrderQuote;
						if(_orderInternal.getSide() == OrderSide.Buy) {
							buyOrderQuote = _orderInternal.getLimitQuoteValue();
							sellOrderQuote = entry.getLimitQuote();
						} else {
							buyOrderQuote = entry.getLimitQuote();
							sellOrderQuote = _orderInternal.getLimitQuoteValue();
						}
						
						if(OrderMatcherBase.areQuotesMatching(market_, buyOrderQuote, sellOrderQuote)) {
							OrderBookEntryImpl.canOrderBeMatched(this, entry, OrderBookImpl.this); 
						} else {
							break;
						}
					} else {
						OrderBookEntryImpl.canOrderBeMatched(this, entry, OrderBookImpl.this); 
					}
					
					if(getStopMatching()) {
						break;
					}
				}
			}
			
			return getCanOrderBeMatched();
		}
		
		public void testOrder(OrderInternal orderInternal_) {
			if(isOrderRestrictionsCompatible(orderInternal_)) {
				_sizeMatched += orderInternal_.getRemainingSize();
			} else {				
				_cannotBeMatched = true;
			}
			
		}
		
		private boolean getRestrictedOrder() {
			return _minimumSize > 0;
		}
		
		private boolean isOrderRestrictionsCompatible(OrderInternal orderInternal_) {
			int minimumExecutionSize = OrderMatcherBase.getMinimumRequiredExecutionSize(orderInternal_);
			return getRemainingSize() >= minimumExecutionSize;
		}

		private int getRemainingSize() {
			return _orderInternal.getSize() - _sizeMatched;
		}
		
		public boolean getStopMatching() {
			return _cannotBeMatched || 
				getRemainingSize() <= 0 || 
				getRestrictedOrder() && _sizeMatched >= _minimumSize;
		}
		
		private boolean getCanOrderBeMatched() {
			return (_sizeMatched >= _minimumSize && !_cannotBeMatched && getRestrictedOrder()) || 
				   (_sizeMatched > 0 && !getRestrictedOrder());
		}
	}
}
