package com.imarcats.market.engine.order;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.imarcats.interfaces.client.v100.dto.types.PropertyChangeDto;
import com.imarcats.interfaces.client.v100.exception.MarketRuntimeException;
import com.imarcats.interfaces.client.v100.exception.MarketSecurityException;
import com.imarcats.interfaces.client.v100.i18n.MarketSystemMessageLanguageKeys;
import com.imarcats.interfaces.client.v100.messages.response.CurrentPositionListResponse;
import com.imarcats.interfaces.client.v100.messages.response.CurrentPositionResponse;
import com.imarcats.interfaces.client.v100.notification.ChangeOrigin;
import com.imarcats.interfaces.client.v100.notification.ObjectVersion;
import com.imarcats.interfaces.client.v100.util.DataUtils;
import com.imarcats.interfaces.server.v100.dto.mapping.PropertyDtoMapping;
import com.imarcats.interfaces.server.v100.util.QuoteRounding;
import com.imarcats.interfaces.server.v100.validation.OrderValidator;
import com.imarcats.internal.server.infrastructure.datastore.MarketDatastore;
import com.imarcats.internal.server.infrastructure.datastore.MatchedTradeDatastore;
import com.imarcats.internal.server.infrastructure.datastore.OrderDatastore;
import com.imarcats.internal.server.interfaces.market.MarketInternal;
import com.imarcats.internal.server.interfaces.order.OrderManagementContext;
import com.imarcats.internal.server.interfaces.order.OrderInternal;
import com.imarcats.model.Instrument;
import com.imarcats.model.Order;
import com.imarcats.model.OrderPropertyNames;
import com.imarcats.model.TradeSide;
import com.imarcats.model.mutators.ClientOrderMutator;
import com.imarcats.model.mutators.DuplicatePropertyException;
import com.imarcats.model.mutators.PropertyChange;
import com.imarcats.model.mutators.PropertyValueChange;
import com.imarcats.model.mutators.UnsupportedPropertyChangeException;
import com.imarcats.model.mutators.UnsupportedPropertyException;
import com.imarcats.model.mutators.UnsupportedPropertyValueException;
import com.imarcats.model.types.AuditInformation;
import com.imarcats.model.types.DatastoreKey;
import com.imarcats.model.types.DoubleProperty;
import com.imarcats.model.types.ExpirationProperties;
import com.imarcats.model.types.OrderSide;
import com.imarcats.model.types.OrderState;
import com.imarcats.model.types.OrderType;
import com.imarcats.model.types.PagedMatchedTradeSideList;
import com.imarcats.model.types.PagedOrderList;
import com.imarcats.model.types.PropertyHolder;
import com.imarcats.model.types.QuoteType;
import com.imarcats.model.types.TriggerProperties;

/**
 * Manages Orders on the Markets
 * @author Adam
 * 
 * TODO: Rework permission checks, not to return orders and markets
 */
public class OrderManagementSystem { 

	private final MarketDatastore _marketDatastore;
	private final OrderDatastore _orderDatastore;
	private final MatchedTradeDatastore _tradeDatastore;
	private final OrderSubmitActionRequestor _orderSubmitExecutor;
	private final OrderCancelActionRequestor _orderCancelExecutor;

	public OrderManagementSystem(
			MarketDatastore marketDatastore_, 
			OrderDatastore orderDatastore_,
			MatchedTradeDatastore tradeDatastore_,
			OrderSubmitActionRequestor orderSubmitExecutor_, 
			OrderCancelActionRequestor orderCancelExecutor_) {
		super();
		_marketDatastore = marketDatastore_;
		_orderDatastore = orderDatastore_;
		_tradeDatastore = tradeDatastore_;	
		_orderSubmitExecutor = orderSubmitExecutor_;
		_orderCancelExecutor = orderCancelExecutor_;
	}

	/**
	 * Gets Orders for User on the given Market 
	 * @param marketCode_ ID of the Market
	 * @param userId_ User
	 * @param cursorString_ Cursor String for continuing a Paged Query or Null for a new Paged Query
	 * @param maxNumberOfOrdersOnPage_ Max Number of Orders on a Page  
	 * 
	 * 
	 * @return Orders for the User 
	 */
	public PagedOrderList getOrdersFor(String marketCode_, String userId_, String cursorString_, int maxNumberOfOrdersOnPage_) {
		// TODO: This might cause performance problems 
		checkMarket(marketCode_);
		
		return _orderDatastore.findOrdersFromCursorBy(userId_, marketCode_, cursorString_, maxNumberOfOrdersOnPage_);
	}

	/**
	 * Gets All Orders for User 
	 * 
	 * @param userID_ User
	 * @param cursorString_ Cursor String for continuing a Paged Query or Null for a new Paged Query
	 * @param maxNumberOfOrdersOnPage_ Max Number of Orders on a Page  
	 * 
	 * 
	 * @return Orders for the User 
	 */
	public PagedOrderList getOrdersFor(String userID_, String cursorString_, int maxNumberOfOrdersOnPage_) {		
		return _orderDatastore.findOrdersFromCursorBy(userID_, cursorString_, maxNumberOfOrdersOnPage_);
	}
	
	/**
	 * Gets All Active (Submitted or WaitingSubmitor or PendingSubmit) Orders for User 
	 * 
	 * @param userId_ orders for this user ID
	 * @param cursorString_ Cursor String for continuing a Paged Query or Null for a new Paged Query
	 * @param maxNumberOfOrdersOnPage_ Max Number of Orders on a Page  
	 * 
	 * 
	 * @return Active Orders for the User 
	 */
	public PagedOrderList getActiveOrdersFor(String userId_, String cursorString_, int maxNumberOfOrdersOnPage_) {		
		return _orderDatastore.findActiveOrdersFromCursorBy(userId_, cursorString_, maxNumberOfOrdersOnPage_);
	}

	
	/**
	 * Gets Matched Trade Sides for User on the given Market 
	 * @param marketCode_ ID of the Market
	 * @param userID_ User
	 * @param cursorString_ Cursor String for continuing a Paged Query or Null for a new Paged Query
	 * @param maxNumberOfTradesOnPage_ Max Number of Trades on a Page  
	 * 
	 * 
	 * @return Trades for the User 
	 */
	public PagedMatchedTradeSideList getMatchedTradesFor(String marketCode_, String userID_, String cursorString_, int maxNumberOfTradesOnPage_) {
		// TODO: This might cause performance problems 
		checkMarket(marketCode_);
		
		return _tradeDatastore.findMatchedTradeByUserAndMarket(userID_, marketCode_, cursorString_, maxNumberOfTradesOnPage_);
	}
	
	/**
	 * Gets Position for User on the given Market 
	 * TODO: Rework to use the internal account from settlement and payment system
	 * 		 Fully test this method 
	 * @param marketCode_ ID of the Market
	 * @param userID_ User 
	 * 
	 * 
	 * @return Position for the User 
	 */
	public CurrentPositionResponse getPositionFor(String marketCode_, String userID_) {
		// TODO: This might cause performance problems 
		MarketInternal market = checkMarket(marketCode_); 
		
		TradeSide[] tradeSides = _tradeDatastore.findMatchedTradeByUserAndMarketInternal(userID_, marketCode_); 
		
		return aggregateToPosition(tradeSides, market, new CurrentPositionResponse());
	}

	/**
	 * Gets All Positions for User 
	 * TODO: Rework to use the internal account from settlement and payment system
	 * 		 Fully test this method 
	 * @param userSession_ User Session
	 * 
	 * 
	 * @return Position for the User 
	 */
	public CurrentPositionListResponse getPositionFor(String userID_) {		
		
		// TODO: Filter trades that are NOT fully settled (settled size != trade size)
		TradeSide[] tradeSides = _tradeDatastore.findMatchedTradeByUserInternal(userID_);
		
		return extractPositionByMarket(tradeSides);
	}

	private CurrentPositionListResponse extractPositionByMarket(
			TradeSide[] tradeSides_) {
		Map<String, List<TradeSide>> marketCodeToTradeSide = new HashMap<String, List<TradeSide>>();
		for (TradeSide tradeSide : tradeSides_) {
			List<TradeSide> tradeList = marketCodeToTradeSide.get(tradeSide.getMarketOfTheTrade());
			if(tradeList == null) {
				// TODO: Rework to use linked list - to reduce memory footprint 
				tradeList = new ArrayList<TradeSide>();
				marketCodeToTradeSide.put(tradeSide.getMarketOfTheTrade(), tradeList);
			}
			
			tradeList.add(tradeSide);
		}
		
		// TODO: Rework to use linked list - to reduce memory footprint 
		List<CurrentPositionResponse> currentPositionList = new ArrayList<CurrentPositionResponse>();
		
		for (String marketCode : marketCodeToTradeSide.keySet()) {
			// TODO: This might cause performance problems 
			MarketInternal market = null;
			try {
				market = _marketDatastore.findMarketBy(marketCode);
			} catch (MarketRuntimeException e) {
				// error loading market
				// TODO: Log 
			}
			// its very unlikely, that market does not exist anymore, but there is still unsettled position for that market 
			// it still may be possible, in this case we ignore the position
			if(market == null) {
				continue;
			}
			List<TradeSide> tradeSideList = marketCodeToTradeSide.get(marketCode);
			CurrentPositionResponse position = aggregateToPosition(tradeSideList.toArray(new TradeSide[tradeSideList.size()]), market, new CurrentPositionResponse());
			
			currentPositionList.add(position);
		}
		
		CurrentPositionListResponse listReponse = new CurrentPositionListResponse();
		listReponse.setPositions(currentPositionList.toArray(new CurrentPositionResponse[currentPositionList.size()]));
		
		return listReponse;
	}
	
	private CurrentPositionResponse aggregateToPosition(TradeSide[] tradeSides_, MarketInternal market_, CurrentPositionResponse position_) {
		if(tradeSides_.length > 0) {
			position_.setTransactionIdOfLatestTrade(tradeSides_[0].getTransactionID());
			position_.setTimeOfRequest(new Date());
			Instrument instrument = market_.getInstrument();
			position_.setInstrumentCode(instrument.getCode());
			position_.setMarketCode(tradeSides_[0].getMarketOfTheTrade());
			position_.setContractSize(instrument.getContractSize());
			position_.setContractSizeUnit(instrument.getContractSizeUnit());
			position_.setCurrency(instrument.getDenominationCurrency());
			
			int aggregateSize = 0;
			double totalCost = 0;
			for (TradeSide tradeSide : tradeSides_) {
				// price for yield quoted markets will be calculated as 100 - yield
				double price = tradeSide.getTradeQuote().getQuoteValue();
				if(market_.getQuoteType() == QuoteType.Yield) {
					price = 100 - tradeSide.getTradeQuote().getQuoteValue();
				}
				
				// count buy trades as cost and sell trades as negative cost (income)
				// buy trades increase the total size sell trades decrease
				
				// TODO: Use open size instead of matched size (trade size � settled size)
				int openSize = tradeSide.getMatchedSize();
				
				double contractSize = tradeSide.getContractSize();
				
				double cost = openSize * price * contractSize;
				
				if(tradeSide.getSide() == OrderSide.Buy) {
					aggregateSize += openSize;
					totalCost += cost;
				} else {
					aggregateSize -= openSize;
					totalCost -= cost;
				}
				
				double commission = tradeSide.getCommission();
				if(!tradeSide.getCommissionCurrency().equals(instrument.getDenominationCurrency())) 
				{
					// TODO: Change commission to market currency using the currency exchange rates from  
					// 	     the payment agent of the market
				}
				
				totalCost += commission;
			}
			
			position_.setCurrentPosition(aggregateSize);
			position_.setTotalCost(totalCost);
		}
		
		return position_;
	}
	
	/**
	 * Gets All Matched Trade Sides for User 
	 * 
	 * @param userID_ User 
	 * @param cursorString_ Cursor String for continuing a Paged Query or Null for a new Paged Query
	 * @param maxNumberOfTradesOnPage_ Max Number of Trade on a Page  
	 * 
	 * 
	 * @return Orders for the User 
	 */
	public PagedMatchedTradeSideList getMatchedTradesFor(String userID_, String cursorString_, int maxNumberOfTradesOnPage_) {		
		return _tradeDatastore.findMatchedTradeByUser(userID_, cursorString_, maxNumberOfTradesOnPage_);
	}
	
	/**
	 * Creates Order on a given Market. Order will be in 
	 * Created state until it is submitted. 
	 * 
	 * @param marketCode_ Code of the Target Market
	 * @param order_ Order to be Created 
	 * @param userID_ User
	 * @param orderManagementContext_ Context for Order Management
	 * @return the ID for the newly created Order 
	 * @throws MarketRuntimeException, if the Order cannot be created.
	 * @throws MarketSecurityException if the this method call results security violation.
	 */
	public Long createOrder(String marketCode_, Order order_, String userID_, OrderManagementContext orderManagementContext_) {		
		if(order_ == null) {
			throw MarketRuntimeException.NULL_ORDER_CANNOT_BE_CREATED;
		}
		
		MarketInternal targetMarket = checkMarket(marketCode_);
				
		// set up default fields, if not set
		// TODO: Consider moving this code to the Order it self
		if(order_.getTriggerPropertiesObject() == null) {
			order_.setTriggerPropertiesObject(new TriggerProperties(new PropertyHolder())); 
		}
		if(order_.getExpirationPropertiesObject() == null) {
			order_.setExpirationPropertiesObject(new ExpirationProperties(new PropertyHolder())); 
		}
		
		// validate order 
		OrderValidator.validateNewOrder(order_, targetMarket.getMarketModel());
		
		// round Order Quote
		checkAndRoundOrderLimitQuote(order_, targetMarket);
		
		// make sure order target market is set correctly 
		order_.setTargetMarketCode(targetMarket.getMarketModel().getMarketCode());
		
		
		// set creation audit to the order 
		AuditInformation creationAudit = new AuditInformation();
		creationAudit.setDateTime(new Date());
		creationAudit.setUserID(userID_);
		order_.setCreationAudit(creationAudit);
		
		// set submitter 
		order_.setSubmitterID(userID_);
		order_.setState(OrderState.Created);
		order_.updateLastUpdateTimestamp();
		
		// create order 
		Long orderKey = _orderDatastore.createOrder(order_);
		order_.setKey(orderKey);
		
		// send notification on order creation
		orderManagementContext_.notifyOrderStateChange(OrderState.Created, order_, ChangeOrigin.User);
		
		return orderKey;
	}

	private ObjectVersion getUpdatedVersion(Order order_) {
		ObjectVersion version = new ObjectVersion();
		// JPA has the current version of the object until it is committed to the datastore, 
		// but in the change notification messages version after update is needed (which is current version + 1) 
		if(order_.getVersionNumber() != null) {			
			version.setVersion(order_.getVersionNumber() + 1);
		} else {
			version.setVersion(0L);
		}
		
		return version;
	}
	
	private void checkAccountForOrder(Order order_) {
		// TODO: Connect Settlement and Payment System's Account Check Process before Trade flow
		
	}

	/**
	 * Deletes Order 
	 * @param orderKey_ Order to be Cleared
	 * @param userID_ User
	 * @param orderManagementContext_ Context for Order Management
	 * @throws MarketRuntimeException, if the Order cannot be deleted.
	 * @throws MarketSecurityException if the this method call results security violation.
	 */
	public void deleteOrder(Long orderKey_, String userID_, OrderManagementContext orderManagementContext_) {
		OrderInternal orderInternal = getOrder(orderKey_);
		
		MarketInternal targetMarket = checkTargetMarketOnOrder(orderInternal);
		
		OrderValidator.validateOrderForDelete(orderInternal.getOrderModel(), targetMarket.getMarketModel());

		// TODO: Resolve that the Deleted OrderState Change Notification will not be sent to
		// 		 the listeners, because listeners will be deleted in the next line and
		//		 notification happens asynchronously (using task queues)
		// 		 Explicit delete of listeners needed, because anyway they may stay around and 
		// 		 will never be deleted.
		orderManagementContext_.notifyOrderStateChange(OrderState.Deleted, orderInternal.getOrderModel(), ChangeOrigin.User);
		
		_orderDatastore.deleteOrder(orderKey_);
		
		orderManagementContext_.getPropertyChangeSession().getPropertyChangeBroker().getNotificationBroker().removeAllListeners(
				new DatastoreKey(orderKey_), Order.class);
		
	}
	
	/**
	 * Submits the Order to the Market.
	 *  
	 * @param orderKey_ Order to be submitted 
	 * @param userID_ User
	 * @param orderManagementContext_ Context for Order Management
	 * @throws MarketRuntimeException if the Order cannot be Submitted.
	 * @throws MarketSecurityException if the this method call results security violation.
	 */
	public void submitOrder(Long orderKey_, String userID_, OrderManagementContext orderManagementContext_) {
		OrderInternal orderInternal = getOrder(orderKey_);
		MarketInternal targetMarket = checkTargetMarketOnOrder(orderInternal);
		checkAccountForOrder(orderInternal.getOrderModel());
		
		submitOrder(orderInternal, targetMarket, orderManagementContext_);
	}

	private void submitOrder(final OrderInternal orderInternal, final MarketInternal targetMarket, OrderManagementContext orderManagementContext_) {
		orderInternal.recordPendingSubmit(orderManagementContext_); 
		_orderSubmitExecutor.submitOrder(orderInternal, orderManagementContext_);
	} 
	
	/**
	 * Cancels all Orders for a User on a Market
	 * @param userId_ User 
	 * @param marketCode_ Market
	 * @param cancellationCommentLanguageKey_ Language Key for Cancellation Comment
	 * @param orderManagementContext_ Management Context for Order Changes
	 */
	public void cancelAllOrdersForUserOnMarket(String userId_, String marketCode_, OrderManagementContext orderManagementContext_) {
		checkMarket(marketCode_);
		OrderInternal[] orders = _orderDatastore.findOrdersBy(userId_, marketCode_);
		cancelActiveOrders(userId_, orders, orderManagementContext_);
	}

	/**
	 * Cancels all Orders for a User 
	 * @param userId_ User 
	 * @param cancellationCommentLanguageKey_ Language Key for Cancellation Comment
	 * @param orderManagementContext_ Management Context for Order Changes
	 */
	public void cancelAllOrdersForUser(String userId_, OrderManagementContext orderManagementContext_) {
		OrderInternal[] orders = _orderDatastore.findOrdersBy(userId_);
		cancelActiveOrders(userId_, orders, orderManagementContext_);
	}
	
	private void cancelActiveOrders(String userId_, OrderInternal[] orders, OrderManagementContext orderManagementContext_) {		
		// TODO: Consider, this query may retrieve a lot of Orders, which may cause timeouts and performance problems		
		for (OrderInternal order : orders) {
			if(order.getState() == OrderState.Submitted || order.getState() == OrderState.WaitingSubmit || order.getState() == OrderState.PendingSubmit) {
				String cancellationCommentLanguageKey = 
					getCancellationCommentLanguageKey(order);
				// get market for the order, if orders are taken from different markets 
				MarketInternal targetMarket = order.getTargetMarket();
				if(targetMarket == null) {
					targetMarket = checkTargetMarketOnOrder(order);
				}
				_orderCancelExecutor.cancelOrder(order, cancellationCommentLanguageKey, orderManagementContext_);
			}
		}
	}
	
	/**
	 * Cancels the Order on the Market.
	 *  
	 * @param orderKey_ Order to be canceled 
	 * @param userID_ User
	 * @param orderManagementContext_ Context for Order Management
	 * @throws MarketRuntimeException if the Order cannot be Canceled.
	 * @throws MarketSecurityException if the this method call results security violation.
	 */
	public void cancelOrder(Long orderKey_, String userId_, OrderManagementContext orderManagementContext_) {
		OrderInternal orderInternal = getOrder(orderKey_);
		
		String cancellationCommentLanguageKey = getCancellationCommentLanguageKey(
				orderInternal);
		
		_orderCancelExecutor.cancelOrder(orderInternal, cancellationCommentLanguageKey, orderManagementContext_);
	}

	private String getCancellationCommentLanguageKey(
			OrderInternal orderInternal_) {
		String cancellationCommentLanguageKey = MarketSystemMessageLanguageKeys.ORDER_CANCELED_BY_USER;
		return cancellationCommentLanguageKey;
	}

	/**
	 * Changes a Property on the Order. 
	 * Cancels the Order on the Market first.
	 *  
	 * @param orderKey_ Order to be changed
	 * 
	 * @param userID_ User
	 * @param propertyChangesDto_ Changed Property
	 * @param orderManagementContext_ Context for Order Management
	 * @throws MarketRuntimeException if the Order cannot be Canceled or Changed.
	 * @throws MarketSecurityException if the this method call results security violation.
	 */
	public void changeOrderProperties(Long orderKey_, PropertyChangeDto[] propertyChangesDto_, String userID_, OrderManagementContext orderManagementContext_) {
		PropertyChange[] propertyChanges = PropertyDtoMapping.INSTANCE.fromDto(propertyChangesDto_);
		
		OrderInternal orderInternal = prepareOrderForChange(orderKey_,
				userID_, orderManagementContext_);
	
		boolean limitQuoteWasRounded = false;
		Order orderModel = orderInternal.getOrderModel();
		
		try {
			
			// validate order change, to check that it does not change system properties
			OrderValidator.validateOrderChange(propertyChanges);

			// set up default fields, if not set - for orders not setup correctly
			// TODO: Consider removing it, if order model problems fixed
			if(orderModel.getTriggerPropertiesObject() == null) {
				orderModel.setTriggerPropertiesObject(new TriggerProperties(new PropertyHolder())); 
			}
			if(orderModel.getExpirationPropertiesObject() == null) {
				orderModel.setExpirationPropertiesObject(new ExpirationProperties(new PropertyHolder())); 
			}
			
			// change the actual order 
			// this sends notification about property changes
			ClientOrderMutator.INSTANCE.executePropertyChanges(orderModel, propertyChanges, orderManagementContext_);
			orderModel.updateLastUpdateTimestamp();
			
			// round Order Quote
			limitQuoteWasRounded = checkAndRoundOrderLimitQuote(orderModel, orderInternal.getTargetMarket());
			
		} catch (UnsupportedPropertyException e) {
			throw MarketRuntimeException.createExceptionWithDetails(MarketRuntimeException.UNSUPPORTED_ORDER_PROPERTY, 
					e, new Object[] { orderInternal, propertyChangesDto_ });
		} catch (UnsupportedPropertyValueException e) {
			throw MarketRuntimeException.createExceptionWithDetails(MarketRuntimeException.UNSUPPORTED_ORDER_PROPERTY_VALUE, 
					e, new Object[] { orderInternal, propertyChangesDto_ });
		} catch (DuplicatePropertyException e) {
			throw MarketRuntimeException.createExceptionWithDetails(MarketRuntimeException.DUPLICATE_ORDER_PROPERTY, 
					e, new Object[] { orderInternal, propertyChangesDto_ });
		} catch (UnsupportedPropertyChangeException e) {
			throw MarketRuntimeException.createExceptionWithDetails(MarketRuntimeException.UNSUPPORTED_ORDER_PROPERTY_CHANGE, 
					e, new Object[] { orderInternal, propertyChangesDto_ });
		}
		
		// validate the order to make sure it is still valid after the change 
		OrderValidator.validateExistingOrder(orderModel, orderInternal.getTargetMarket().getMarketModel());
		
		DatastoreKey marketCodeKey = new DatastoreKey(orderModel.getTargetMarketCode());

		// send notification limit quote change if it was rounded 
		DatastoreKey orderKey = new DatastoreKey(orderModel.getKey());
		
		if(limitQuoteWasRounded) {
			PropertyValueChange propertyChange = new PropertyValueChange();
			DoubleProperty limitQuoteProperty = new DoubleProperty();
			limitQuoteProperty.setName(OrderPropertyNames.LIMIT_QUOTE_VALUE_PROPERTY);
			limitQuoteProperty.setValue(orderInternal.getLimitQuoteValue().getQuoteValue());
			propertyChange.setProperty(limitQuoteProperty);
			
			orderManagementContext_.getPropertyChangeSession().recordPropertyChanges(
					orderKey, Order.class, marketCodeKey, 
					new PropertyChange[] { propertyChange }, 
					orderModel.getLastUpdateTimestamp(), getUpdatedVersion(orderModel),
					orderModel.getSubmitterID());
		}
	}
	
	/**
	 * Get Order 
	 * @param orderKey_ Key for the Order 
	 * @return Order Internal
	 */
	public OrderInternal getOrder(Long orderKey_) {
		if(orderKey_ == null) {
			throw MarketRuntimeException.createExceptionWithDetails(MarketRuntimeException.NON_EXISTENT_ORDER, 
					null, new Object[] { "Order ID=" + orderKey_ });
		}
		OrderInternal orderInternal = _orderDatastore.findOrderBy(orderKey_);
		if(orderInternal == null) {
			throw MarketRuntimeException.createExceptionWithDetails(MarketRuntimeException.NON_EXISTENT_ORDER, 
					null, new Object[] { "Order ID=" + orderKey_ });
		}
		return orderInternal;
	}
	
	/**
	 * Checks and Rounds the Limit Quote of the Order
	 * @param order_ Order to be submitted
	 * @param market_ Market, where the Order will be submitted
	 * @return if the Quote was changed
	 * @throws MarketRuntimeException, if the Order is not suitable for the Market
	 */
	private static boolean checkAndRoundOrderLimitQuote(Order order_, MarketInternal market_) {
		boolean limitQuoteChanged = false;
		if(order_.getType() == OrderType.Limit) {
			double oldQuoteValue = order_.getLimitQuoteValue().getQuoteValue();
			double roundedQuote = 
				QuoteRounding.roundToValidQuote(
						order_.getLimitQuoteValue().getQuoteValue(), 
						market_.getMinimumQuoteIncrement());
			order_.getLimitQuoteValue().setQuoteValue(roundedQuote);
			limitQuoteChanged = (oldQuoteValue != roundedQuote);
		}
		
		return limitQuoteChanged;
	}

	private OrderInternal prepareOrderForChange(Long orderKey_,
			String userId_, OrderManagementContext orderManagementContext_) {
		OrderInternal orderInternal = getOrder(orderKey_);
		
		// TODO: Check, if order is canceled
		// 		 If not throw an exception instead of trying to cancel the order, 
		// 		 since cancel is asynchronous operation in Production system 
		// 		 it cannot be called synchronously 
		if(orderInternal.getState() == OrderState.WaitingSubmit ||
		   orderInternal.getState() == OrderState.Submitted) {
			_orderCancelExecutor.cancelOrder(orderInternal, MarketSystemMessageLanguageKeys.ORDER_CANCELED_BY_USER, orderManagementContext_);			
		}
		return orderInternal;
	}
	
	private MarketInternal checkTargetMarketOnOrder(OrderInternal orderInternal) {
		return checkMarket(orderInternal.getOrderModel().getTargetMarketCode());
	}

	private MarketInternal checkMarket(String marketCode_) {
		checkMarketCode(marketCode_);
		MarketInternal targetMarket = _marketDatastore.findMarketBy(marketCode_);
		if(targetMarket == null) {
			throw MarketRuntimeException.createExceptionWithDetails(MarketRuntimeException.NON_EXISTENT_MARKET, 
					null, new Object[] { "Market ID=" + marketCode_ });
		}
		return targetMarket;
	}

	private void checkMarketCode(String marketCode_) {
		if(!DataUtils.isValidMarketIdString(marketCode_)) {
			throw MarketRuntimeException.createExceptionWithDetails(MarketRuntimeException.MARKET_MUST_HAVE_CODE, null, new Object[] { DataUtils.VALID_MARKET_ID });
		}
	}
}
