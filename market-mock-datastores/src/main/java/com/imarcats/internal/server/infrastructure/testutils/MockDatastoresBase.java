package com.imarcats.internal.server.infrastructure.testutils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.imarcats.interfaces.client.v100.exception.MarketRuntimeException;
import com.imarcats.internal.server.infrastructure.datastore.AssetClassDatastore;
import com.imarcats.internal.server.infrastructure.datastore.AuditTrailEntryDatastore;
import com.imarcats.internal.server.infrastructure.datastore.InstrumentDatastore;
import com.imarcats.internal.server.infrastructure.datastore.MarketDatastore;
import com.imarcats.internal.server.infrastructure.datastore.MarketOperatorDatastore;
import com.imarcats.internal.server.infrastructure.datastore.MatchedTradeDatastore;
import com.imarcats.internal.server.infrastructure.datastore.OrderDatastore;
import com.imarcats.internal.server.infrastructure.datastore.ProductDatastore;
import com.imarcats.internal.server.infrastructure.marketdata.MarketDataSession;
import com.imarcats.internal.server.infrastructure.timer.MarketTimer;
import com.imarcats.internal.server.interfaces.market.MarketInternal;
import com.imarcats.internal.server.interfaces.order.OrderBookInternal;
import com.imarcats.internal.server.interfaces.order.OrderInternal;
import com.imarcats.internal.server.interfaces.order.OrderManagementContext;
import com.imarcats.model.AssetClass;
import com.imarcats.model.AuditTrailEntry;
import com.imarcats.model.CircuitBreaker;
import com.imarcats.model.HaltRule;
import com.imarcats.model.Instrument;
import com.imarcats.model.Market;
import com.imarcats.model.MarketOperator;
import com.imarcats.model.MatchedTrade;
import com.imarcats.model.Order;
import com.imarcats.model.Product;
import com.imarcats.model.test.testutils.MockIdentityGenerator;
import com.imarcats.model.types.ActivationStatus;
import com.imarcats.model.types.AuditInformation;
import com.imarcats.model.types.BusinessCalendar;
import com.imarcats.model.types.ExecutionSystem;
import com.imarcats.model.types.MarketState;
import com.imarcats.model.types.OrderExpirationInstruction;
import com.imarcats.model.types.OrderSide;
import com.imarcats.model.types.OrderState;
import com.imarcats.model.types.OrderTriggerInstruction;
import com.imarcats.model.types.OrderType;
import com.imarcats.model.types.PagedAssetClassList;
import com.imarcats.model.types.PagedAuditTrailEntryList;
import com.imarcats.model.types.PagedInstrumentList;
import com.imarcats.model.types.PagedMarketList;
import com.imarcats.model.types.PagedMarketOperatorList;
import com.imarcats.model.types.PagedMatchedTradeSideList;
import com.imarcats.model.types.PagedOrderList;
import com.imarcats.model.types.PagedProductList;
import com.imarcats.model.types.Property;
import com.imarcats.model.types.Quote;
import com.imarcats.model.types.QuoteAndSize;
import com.imarcats.model.types.QuoteType;
import com.imarcats.model.types.SecondaryOrderPrecedenceRuleType;
import com.imarcats.model.types.TimeOfDay;
import com.imarcats.model.types.TimePeriod;
import com.imarcats.model.types.TradeSide;
import com.imarcats.model.types.TradingSession;
import com.imarcats.model.types.UnderlyingType;

public class MockDatastoresBase implements 
									ProductDatastore, 
									InstrumentDatastore,
									AssetClassDatastore, 
									MarketOperatorDatastore,
									MarketDatastore, 
									AuditTrailEntryDatastore,
									MatchedTradeDatastore, 
									OrderDatastore {

	private final Map<String, String> _mapUserNameToPassword = new HashMap<String, String>();

	private final Map<String, AssetClass> _mapAssetClassNameToAssetClass = new HashMap<String, AssetClass>();

	private final Map<String, Product> _mapCodeToProduct = new HashMap<String, Product>();
	
	// instrument
	private final Map<String, Instrument> _mapCodeToInstrument = new HashMap<String, Instrument>();

	// market
	private final Map<String, Market> _mapCodeToMarket = new HashMap<String, Market>();
	
	// market operator 
	private final Map<String, MarketOperator> _mapCodeToMarketOperator = new HashMap<String, MarketOperator>();
	
	// audits
	private final List<AuditTrailEntry> _auditTrails = new ArrayList<AuditTrailEntry>();
	
	// trades - LinkedHashMap to keep insertion order
	private final Map<Long, MatchedTrade> _mapIdToTrade = new LinkedHashMap<Long, MatchedTrade>();
	
	// order
	private final Map<Long, OrderInternal> _mapIdToOrder = new HashMap<Long, OrderInternal>();
	
	public MockDatastoresBase() {
		initData();
	}

	private void initData() {
	}
	
	// instrument
	@Override
	public String createInstrument(Instrument instrument_) {
		_mapCodeToInstrument.put(instrument_.getCode(), instrument_);
		return instrument_.getCode();
	}

	@Override
	public Instrument updateInstrument(Instrument changedInstrumentModel_) {
		createInstrument(changedInstrumentModel_);
		
		return changedInstrumentModel_;
	}	
	
	@Override
	public void deleteInstrument(String instrumentCode_) {
		_mapCodeToInstrument.remove(instrumentCode_);
	}
	
	@Override
	public Instrument findInstrumentByCode(String instrumentCode_) {		
		return _mapCodeToInstrument.get(instrumentCode_);
	}

	@Override
	public PagedInstrumentList findAllInstrumentsFromCursor(
			String cursorString_, int maxNumberOfProductsOnPage_) {
		return createInstrList(_mapCodeToInstrument.values());
	}

	@Override
	public PagedInstrumentList findInstrumentsFromCursorByActivationStatus(
			final ActivationStatus activationStatus_, String cursorString_,
			int maxNumberOfInstrumentsOnPage_) {
		return findAllInstrumentsToPagedList( instrument_ -> activationStatus_.equals(instrument_.getActivationStatus()));
	}

	@Override
	public PagedInstrumentList findInstrumentsFromCursorByAssetClass(
			final String assetClassName_, String cursorString_,
			int maxNumberOfProductsOnPage_) {
		return findAllInstrumentsToPagedList( instrument_ -> assetClassName_.equals(instrument_.getAssetClassName()));
	}
	
	@Override
	public PagedInstrumentList findInstrumentsFromCursorByUnderlying(
			final String underlyingCode_, final UnderlyingType underlyingType_, String cursorString_,
			int maxNumberOfProductsOnPage_) {
		return findAllInstrumentsToPagedList( instrument_ -> 
					underlyingCode_.equals(instrument_.getUnderlyingCode()) &&
					underlyingType_.equals(instrument_.getUnderlyingType()));
	}
	

	@Override
	public Instrument[] findInstrumentsByUnderlying(final String underlyingCode_,
			final UnderlyingType underlyingType_) {
		return findAllInstrumentsToPagedList( instrument_ -> 
				 	underlyingCode_.equals(instrument_.getUnderlyingCode()) &&
				 	underlyingType_.equals(instrument_.getUnderlyingType())).getInstruments();
	}
	
	private interface IInstrumentFilter {
		public boolean accept(Instrument instrument_);
	} 
	
	private PagedInstrumentList findAllInstrumentsToPagedList(IInstrumentFilter filter_) {
		return createInstrList(findAllInstruments(filter_));
	}
	
	private List<Instrument> findAllInstruments(IInstrumentFilter filter_) {
		return _mapCodeToInstrument.values().stream().filter(instrument -> filter_.accept(instrument)).collect(Collectors.toList());
	}
	
	private PagedInstrumentList createInstrList(Collection<Instrument> instruments_) {
		PagedInstrumentList list = new PagedInstrumentList();
		
		list.setInstruments(instruments_.toArray(new Instrument[instruments_.size()]));
		
		return list;
	}

	// Asset Class
	@Override
	public String createAssetClass(AssetClass assetClass_) {
		_mapAssetClassNameToAssetClass.put(assetClass_.getName(), assetClass_);
		return assetClass_.getName();
	}


	@Override
	public AssetClass updateAssetClass(AssetClass changedAssetClassModel_) {
		createAssetClass(changedAssetClassModel_);
		
		return changedAssetClassModel_;
	}
	
	@Override
	public void deleteAssetClass(String name_) {
		_mapAssetClassNameToAssetClass.remove(name_);
	}

	@Override
	public PagedAssetClassList findAllAssetClassesFromCursor(
			String cursorString_, int maxNumberOfAssetClassesOnPage_) {
		AssetClass[] assetClasses = _mapAssetClassNameToAssetClass.values().toArray(new AssetClass[_mapAssetClassNameToAssetClass.values().size()]);
		
		PagedAssetClassList list = new PagedAssetClassList();
		list.setAssetClasses(assetClasses);
		list.setCursorString(cursorString_);
		list.setMaxNumberOfAssetClassesOnPage(maxNumberOfAssetClassesOnPage_);
		
		return list;
	}
	

	@Override
	public PagedAssetClassList findAllTopLevelAssetClassesFromCursor(
			String cursorString_, int maxNumberOfAssetClassesOnPage_) {
		List<AssetClass> assetClasses = _mapAssetClassNameToAssetClass.values().stream().filter(
				assetClass -> assetClass.getParentName() == null).collect(Collectors.toList());
		
		AssetClass[] assetClassesArray = assetClasses.toArray(new AssetClass[assetClasses.size()]);
		
		PagedAssetClassList list = new PagedAssetClassList();
		list.setAssetClasses(assetClassesArray);
		list.setCursorString(cursorString_);
		list.setMaxNumberOfAssetClassesOnPage(maxNumberOfAssetClassesOnPage_);
		
		return list;
	}

	@Override
	public PagedAssetClassList findAssetClassesFromCursorByParent(
			String parentAssetClassName_, String cursorString_,
			int maxNumberOfAssetClassesOnPage_) {
		List<AssetClass> assetClasses = _mapAssetClassNameToAssetClass.values().stream().filter(
				assetClass -> parentAssetClassName_.equals(assetClass.getParentName())).collect(Collectors.toList());
		
		AssetClass[] assetClassesArray = assetClasses.toArray(new AssetClass[assetClasses.size()]);
		
		PagedAssetClassList list = new PagedAssetClassList();
		list.setAssetClasses(assetClassesArray);
		list.setCursorString(cursorString_);
		list.setMaxNumberOfAssetClassesOnPage(maxNumberOfAssetClassesOnPage_);
		
		return list;
	}

	@Override
	public AssetClass findAssetClassByName(String name_) {
		return _mapAssetClassNameToAssetClass.get(name_);
	}

	@Override
	public String createProduct(Product product_) {
		_mapCodeToProduct.put(product_.getCode(), product_);
		return product_.getCode();
	}

	@Override
	public Product updateProduct(Product changedProductModel_) {
		createProduct(changedProductModel_);
		
		return changedProductModel_;
	}
	
	@Override
	public void deleteProduct(String productCode_) {
		_mapCodeToProduct.remove(productCode_);
	}

	@Override
	public PagedProductList findAllProductsFromCursor(String cursorString_,
			int maxNumberOfProductsOnPage_) {
		return createList(_mapCodeToProduct.values());
	}

	@Override
	public Product findProductByCode(String productCode_) {
		return _mapCodeToProduct.get(productCode_);
	}

	@Override
	public PagedProductList findProductsFromCursorByActivationStatus(
			final ActivationStatus activationStatus_, String cursorString_,
			int maxNumberOfProductsOnPage_) {
		return findAllProductsToPagedList(product_ -> activationStatus_.equals(product_.getActivationStatus()));
	}
	
	private PagedProductList findAllProductsToPagedList(IProductFilter filter_) {
		return createList(findAllProducts(filter_));
	}
	
	private interface IProductFilter {
		public boolean accept(Product product_);
	} 
	
	private List<Product> findAllProducts(IProductFilter filter_) {
		return _mapCodeToProduct.values().stream().filter(product -> filter_.accept(product)).collect(Collectors.toList());
		
	}
	
	private PagedProductList createList(Collection<Product> products_) {
		PagedProductList list = new PagedProductList();
		
		list.setProducts(products_.toArray(new Product[products_.size()]));
		
		return list;
	}

	@Override
	public String createMarket(Market market_) {
		_mapCodeToMarket.put(market_.getMarketCode(), market_);
		return market_.getMarketCode();
	}

	@Override
	public Market updateMarket(Market changedMarketModel_) {
		createMarket(changedMarketModel_);
		
		return changedMarketModel_;
	}
	
	@Override
	public void deleteMarket(String marketCode_) {
		_mapCodeToMarket.remove(marketCode_);
	}

	@Override
	public MarketInternal findMarketBy(String marketCode_) {
		Market market = _mapCodeToMarket.get(marketCode_);
		return market != null 
					? new MockMarket(market) 
					: null;
	}

	@Override
	public Market[] findMarketModelsByInstrument(String instrumentCode_) {
		List<Market> markets = _mapCodeToMarket.values().stream().filter(market -> instrumentCode_.equals(market.getInstrumentCode())).collect(Collectors.toList());
		
		return markets.toArray(new Market[markets.size()]);
	}

	@Override
	public PagedMarketList findMarketModelsFromCursorByInstrument(
			String instrumentCode_, String cursorString_,
			int maxNumberOfMarketsOnPage_) {
		Market[] markets = findMarketModelsByInstrument(instrumentCode_);
		
		PagedMarketList marketList = new PagedMarketList();
		marketList.setMarkets(markets);
		
		return marketList;
	}

	@Override
	public Market[] findMarketModelsByBusinessEntity(
			String businessEntityCode_) {
		List<Market> markets = _mapCodeToMarket.values().stream().filter(market -> businessEntityCode_.equals(market.getBusinessEntityCode())).collect(Collectors.toList());
		return markets.toArray(new Market[markets.size()]);
	}
	
	@Override
	public PagedMarketList findMarketModelsFromCursorByActivationStatus(
			ActivationStatus activationStatus_, String cursorString_,
			int maxNumberOfMarketsOnPage_) {
		List<Market> markets = _mapCodeToMarket.values().stream().filter(market -> activationStatus_.equals(market.getActivationStatus())).collect(Collectors.toList());
		
		PagedMarketList marketList = new PagedMarketList();
		marketList.setMarkets(markets.toArray(new Market[markets.size()]));
		
		return marketList;
	}

	@Override
	public Market[] findMarketModelsByMarketOperator(
			String marketOperatorCode_) {
		List<Market> markets = _mapCodeToMarket.values().stream().filter(market -> marketOperatorCode_.equals(market.getMarketOperatorCode())).collect(Collectors.toList());
		
		return markets.toArray(new Market[markets.size()]);
	}

	@Override
	public PagedMarketList findMarketModelsFromCursorByMarketOperator(
			String marketOperatorCode_, String cursorString_,
			int maxNumberOfMarketsOnPage_) {
		Market[] markets = findMarketModelsByMarketOperator(marketOperatorCode_);
		
		PagedMarketList marketList = new PagedMarketList();
		marketList.setMarkets(markets);
		
		return marketList;
	}
	
	@Override
	public String createMarketOperator(MarketOperator marketOperator_) {
		_mapCodeToMarketOperator.put(marketOperator_.getCode(), marketOperator_);
		return marketOperator_.getCode();
	}

	@Override
	public MarketOperator updateMarketOperator(MarketOperator changedMarketOperator_) {
		createMarketOperator(changedMarketOperator_);
		
		return changedMarketOperator_;
	}	
	
	@Override
	public void deleteMarketOperator(String marketOperatorCode_) {
		_mapCodeToMarketOperator.remove(marketOperatorCode_);
	}

	@Override
	public MarketOperator findMarketOperatorByCode(String marketOperatorCode_) {
		return _mapCodeToMarketOperator.get(marketOperatorCode_);
	}

	@Override
	public PagedMarketOperatorList findMarketOperatorsFromCursorByUser(
			String userCode_, String cursorString_,
			int maxNumberOfMarketOperatorsOnPage_) {
		
		List<MarketOperator> marketOperatorsFound = _mapCodeToMarketOperator.values().stream().filter(marketOperator -> marketOperator.getOwnerUserID().equals(userCode_)).collect(Collectors.toList());
		PagedMarketOperatorList list = createPagedMarketOperatorList(
				cursorString_, maxNumberOfMarketOperatorsOnPage_,
				marketOperatorsFound);
		
		return list;
	}

	@Override
	public MarketOperator[] findMarketOperatorsByUser(String userCode_) {
		return findMarketOperatorsFromCursorByUser(
				userCode_, null,
				10).getMarketOperators();
	}

	@Override
	public MarketOperator[] findMarketOperatorsByBusinessEntity(
			String businessEntityCode_) {
		List<MarketOperator> found = _mapCodeToMarketOperator.values().stream().filter(marketOperator -> marketOperator.getBusinessEntityCode().equals(businessEntityCode_)).collect(Collectors.toList());
		
		return found.toArray(new MarketOperator[found.size()]);
	}
	
	private PagedMarketOperatorList createPagedMarketOperatorList(
			String cursorString_, int maxNumberOfMarketOperatorsOnPage_,
			List<MarketOperator> marketOperatorsFound) {
		PagedMarketOperatorList list = new PagedMarketOperatorList();
		list.setCursorString(cursorString_);
		list.setMaxNumberOfMarketOperatorsOnPage(maxNumberOfMarketOperatorsOnPage_);
		list.setMarketOperators(marketOperatorsFound.toArray(new MarketOperator[marketOperatorsFound.size()]));
		return list;
	}
	

	@Override
	public PagedMarketOperatorList findMarketOperatorsFromCursorByActivationStatus(
			ActivationStatus activationStatus_, String cursorString_,
			int maxNumberOfMarketOperatorsOnPage_) {
		List<MarketOperator> marketOperatorsFound = _mapCodeToMarketOperator.values().stream().filter(marketOperator -> marketOperator.getActivationStatus().equals(activationStatus_)).collect(Collectors.toList());
		
		PagedMarketOperatorList list = createPagedMarketOperatorList(
				cursorString_, maxNumberOfMarketOperatorsOnPage_,
				marketOperatorsFound);
		
		return list;
	}
	
	// Order
	@Override
	public Long createOrder(Order orderModel_) {
		orderModel_.setKey(MockIdentityGenerator.getId());
		
		_mapIdToOrder.put(orderModel_.getKey(), wrapOrder(orderModel_, this));
		
		return orderModel_.getKey();
	}

	protected OrderInternal wrapOrder(Order orderModel_,
			MockDatastoresBase mockDatastoresBase_) {
		return new MockOrder(orderModel_);
	}

	@Override
	public void deleteOrder(Long orderID_) {
		_mapIdToOrder.remove(orderID_);
	}

	@Override
	public OrderInternal findOrderBy(Long orderID_) {
		return _mapIdToOrder.get(orderID_);
	}

	@Override
	public OrderInternal[] findOrdersBy(String userID_) {
		return findOrdersByUser(userID_);
	}
	@Override
	public PagedOrderList findOrdersFromCursorBy(String userID_, String marketCode_, String cursorString_, int maxNumberOfOrderOnPage_) {
		return toPagedOrderList(findOrdersByUserAndMarket(userID_, marketCode_));
	}

	@Override
	public OrderInternal[] findOrdersBy(String userID_, String marketCode_) {
		return findOrdersByUserAndMarket(userID_, marketCode_);
	}


	@Override
	public PagedOrderList findActiveOrdersFromCursorBy(String userID_, String cursorString_,
			int maxNumberOfOrderOnPage_) {
		OrderInternal[] userOrders = findOrdersBy(userID_);

		List<OrderInternal> ordersForUser = Arrays.asList(userOrders).stream().filter(orderInternal -> orderInternal.getSubmitterID().equals(
					userID_) && (orderInternal.getState() == OrderState.Submitted || 
					orderInternal.getState() == OrderState.PendingSubmit || 
					orderInternal.getState() == OrderState.WaitingSubmit)).collect(Collectors.toList());
		
		return toPagedOrderList(ordersForUser.toArray(new OrderInternal[ordersForUser.size()]));
	}
	
	private OrderInternal[] findOrdersByUserAndMarket(String userID_,
			String marketCode_) {
		OrderInternal[] userOrders = findOrdersBy(userID_);

		List<OrderInternal> ordersForMarket = Arrays.asList(userOrders).stream().filter(
				orderInternal -> orderInternal.getTargetMarket().getMarketModel().getMarketCode().equals(
				marketCode_)).collect(Collectors.toList());
		return ordersForMarket.toArray(new OrderInternal[ordersForMarket.size()]);
	}

	@Override
	public OrderInternal findOrderBy(String externalReference_,
			String userID_, String marketCode_) {
		return findOrderByRef(externalReference_, userID_, marketCode_);
	}
	
	private OrderInternal findOrderByRef(String externalReference_,
			String userID_, String marketCode_) {
		OrderInternal[] userOrders = findOrdersBy(userID_);
		List<OrderInternal> ordersForMarket = Arrays.asList(userOrders).stream().filter(
				orderInternal -> orderInternal.getTargetMarket().getMarketModel().getMarketCode().equals(
						marketCode_) && orderInternal.getOrderModel().getExternalOrderReference().equals(
								externalReference_)).collect(Collectors.toList());
		
		return !ordersForMarket.isEmpty() ? ordersForMarket.get(0) : null;
	}
	
	@Override
	public void deleteNonActiveOrdersOnMarket(String marketCode_) {
		OrderInternal[] ordersToBeDeleted = findNonActiveOrdersOnMarket(marketCode_);
		Arrays.asList(ordersToBeDeleted).stream().map(order -> order.getKey()).forEach(this::deleteOrder);
	}
	
	@Override
	public OrderInternal[] findNonActiveOrdersOnMarket(String marketCode_) {
		List<OrderInternal> ordersToBeDeleted = 
			_mapIdToOrder.values().stream().filter(order -> order.getState() == OrderState.Created || 
			   order.getState() == OrderState.Canceled || 
			   order.getState() == OrderState.Executed).collect(Collectors.toList());
		return ordersToBeDeleted.toArray(new OrderInternal[ordersToBeDeleted.size()]);
	}
	

	@Override
	public OrderInternal[] findActiveOrdersOnMarket(String marketCode_) {
		List<OrderInternal> ordersToBeReturned = 
				_mapIdToOrder.values().stream().filter(order -> order.getState() == OrderState.Submitted || order.getState() == OrderState.WaitingSubmit).collect(Collectors.toList());
		return ordersToBeReturned.toArray(new OrderInternal[ordersToBeReturned.size()]);
	}
	
	private OrderInternal[] findOrdersByUser(String userID_) {
		List<OrderInternal> userOrders = _mapIdToOrder.values().stream().filter(order -> order.getSubmitterID().equals(userID_)).collect(Collectors.toList());
		return userOrders.toArray(new OrderInternal[userOrders.size()]);
	}
	@Override
	public PagedOrderList findOrdersFromCursorBy(String userID_, String cursorString_, int maxNumberOfOrderOnPage_) {
		return toPagedOrderList(findOrdersByUser(userID_));
	}

	private PagedOrderList toPagedOrderList(OrderInternal[] ordersInternal) {
		PagedOrderList list = new PagedOrderList();
		list.setCursorString(null);
		list.setMaxNumberOfOrdersOnPage(ordersInternal.length);
		
		List<Order> orders = Arrays.asList(ordersInternal).stream().map(order -> order.getOrderModel()).collect(Collectors.toList());
		
		list.setOrders(orders.toArray(new Order[orders.size()]));
		return list;
	}
	// End Order
	
	private static class MockOrder implements OrderInternal {

		private final Order _model;
		
		public MockOrder(Order model_) {
			super();
			_model = model_;
		}
		
		@Override
		public Long getKey() {
			return _model.getKey();
		}

		@Override
		public OrderSide getSide() {
			return _model.getSide();
		}

		@Override
		public OrderType getType() {
			return _model.getType();
		}

		@Override
		public Quote getLimitQuoteValue() {
			return _model.getLimitQuoteValue();
		}

		@Override
		public int getSize() {
			return _model.getSize();
		}

		@Override
		public int getMinimumSizeOfExecution() {
			return _model.getMinimumSizeOfExecution();
		}

		@Override
		public boolean getExecuteEntireOrderAtOnce() {
			return _model.getExecuteEntireOrderAtOnce();
		}

		@Override
		public boolean getDisplayOrder() {
			return _model.getDisplayOrder();
		}

		@Override
		public int getRemainingSize() {
			return _model.getSize() - _model.getExecutedSize();
		}

		@Override
		public int getExecutedSize() {
			return _model.getExecutedSize();
		}

		@Override
		public OrderState getState() {
			return _model.getState();
		}

		@Override
		public OrderTriggerInstruction getTriggerInstruction() {
			return _model.getTriggerInstruction();
		}

		@Override
		public Property[] getTriggerProperties() {
			return _model.getTriggerProperties();
		}

		@Override
		public OrderExpirationInstruction getExpirationInstruction() {
			return _model.getExpirationInstruction();
		}

		@Override
		public Property[] getExpirationProperties() {
			return _model.getExpirationProperties();
		}

		@Override
		public Date getSubmissionDate() {
			return _model.getSubmissionDate();
		}

		@Override
		public String getCancellationCommentLanguageKey() {
			return _model.getCancellationCommentLanguageKey();
		}

		@Override
		public AuditInformation getCreationAudit() {
			return _model.getCreationAudit();
		}

		@Override
		public Quote getCurrentStopQuote() {
			return _model.getCurrentStopQuote();
		}

		@Override
		public MarketInternal getTargetMarket() {
			// Does nothing
			return null;
		}

		@Override
		public String getSubmitterID() {
			return _model.getSubmitterID();
		}

		@Override
		public Order getOrderModel() {
			return _model;
		}

		@Override
		public void setQuoteChangeTriggerKey(Long quoteChangeTriggerKey_) {
			// Does nothing
		}

		@Override
		public Long getQuoteChangeTriggerKey() {
			return _model.getQuoteChangeTriggerKey();
		}

		@Override
		public void recordSubmitWaiting(
				OrderManagementContext orderManagementContext_) {
			// Does nothing
		}

		@Override
		public void recordSubmit(OrderManagementContext orderManagementContext_) {
			// Does nothing
		}

		@Override
		public void recordCancel(String cancellationCommentLanguageKey_,
				OrderManagementContext orderManagementContext_) {
			// Does nothing
			
		}

		@Override
		public void recordExecution(MarketInternal targetMarket_,
				int executedSize_,
				OrderManagementContext orderManagementContext_) {
			// Does nothing
			
		}

		@Override
		public TradeSide getTradeSide(Date tradeDateTime_) {
			// Does nothing
			return null;
		}

		@Override
		public Long getExpirationTriggerActionKey() {
			return _model.getExpirationTriggerActionKey();
		}

		@Override
		public void setExpirationTriggerActionKey(Long timeTriggerActionKey_) {
			// Does nothing
		}

		@Override
		public void recordNewStopQuote(Quote newStopQuote_) {
			// Does nothing
		}

		@Override
		public OrderInternal addExpirationTrigger(MarketInternal market_,
				OrderManagementContext orderManagementContext_) {
			// Does nothing 
			return this;
		}

		@Override
		public OrderInternal removeExpirationTrigger(
				MarketInternal targetMarket_, OrderManagementContext context_) {
			// Does nothing 
			return this;
		}

		@Override
		public OrderInternal addQuoteChangeTrigger(MarketInternal market_,
				OrderManagementContext orderManagementContext_) {
			// Does nothing 
			return this;
		}

		@Override
		public OrderInternal removeQuoteChangeTrigger(
				MarketInternal targetMarket_,
				OrderManagementContext orderManagementContext_) {
			// Does nothing 
			return this;
		}

		@Override
		public void recordPendingSubmit(
				OrderManagementContext orderManagementContext_) {
			// Does nothing	
		}
		
	}
	
	private static class MockMarket implements MarketInternal {

		private final Market _model;

		public MockMarket(Market model_) {
			super();
			_model = model_;
		}

		@Override
		public Market getMarketModel() {
			return _model;
		}

		@Override
		public void callMarket(OrderManagementContext orderManagementContext_) {
			// Does nothing
			
		}

		@Override
		public void cancel(OrderInternal order_, String cancellationCommentLanguageKey_,
				OrderManagementContext orderManagementContext_) {
			// Does nothing
			
		}

		@Override
		public void closeMarket(OrderManagementContext orderManagementContext_) {
			_model.setState(MarketState.Closed);
		}

		@Override
		public HaltRule getActualHaltRule() {
			// Does nothing
			return null;
		}

		@Override
		public OrderBookInternal getBuyBook() {
			// Does nothing
			return null;
		}

		@Override
		public OrderBookInternal getSellBook() {
			// Does nothing
			return null;
		}

		@Override
		public void haltMarket(OrderManagementContext orderManagementContext_) {
			// Does nothing
			
		}

		@Override
		public void openMarket(OrderManagementContext orderManagementContext_) {
			checkMarketForOpen();
			_model.setState(MarketState.Open);
		}

		private void checkMarketForOpen() {
			if(getActivationStatus() != ActivationStatus.Activated) {
				throw MarketRuntimeException.createExceptionWithDetails(
						MarketRuntimeException.NON_ACTIVE_MARKET_CANNOT_BE_OPENED, null, 
						new Object[]{ this });
			}
			if(getState() != MarketState.Closed) {
				throw MarketRuntimeException.createExceptionWithDetails(
						MarketRuntimeException.NON_CLOSED_MARKET_CANNOT_BE_OPENED, null, 
						new Object[]{ this });
			}
		}

		@Override
		public void reOpenMarket(OrderManagementContext orderManagementContext_) {
			// Does nothing
			
		}

		@Override
		public void recordClosingQuote(Quote closingQuote_,
				MarketDataSession marketDataSession_) {
			// Does nothing
			
		}

		@Override
		public void recordLastTrade(QuoteAndSize lastTrade_,
				MarketDataSession marketDataSession_) {
			// Does nothing
			
		}

		@Override
		public void recordOpeningQuote(Quote openingQuote_,
				MarketDataSession marketDataSession_) {
			// Does nothing
			
		}

		@Override
		public void submit(OrderInternal order_,
				OrderManagementContext orderManagementContext_) {
			// Does nothing
			
		}

		@Override
		public ActivationStatus getActivationStatus() {
			return _model.getActivationStatus();
		}

		@Override
		public QuoteAndSize getBestAsk() {
			// Does nothing
			return null;
		}

		@Override
		public QuoteAndSize getBestBid() {
			// Does nothing
			return null;
		}

		@Override
		public BusinessCalendar getBusinessCalendar() {
			// Does nothing
			return null;
		}

//		@Override
//		public BusinessEntity getBusinessEntity() {
//			// Does nothing
//			return null;
//		}

		@Override
		public CircuitBreaker getCircuitBreaker() {
			// Does nothing
			return null;
		}

		@Override
		public Quote getClosingQuote() {
			// Does nothing
			return null;
		}

		@Override
		public double getCommission() {
			// Does nothing
			return 0;
		}
		
		@Override
		public String getDescription() {
			// Does nothing
			return null;
		}

		@Override
		public ExecutionSystem getExecutionSystem() {
			// Does nothing
			return null;
		}

		@Override
		public Instrument getInstrument() {
			// Does nothing
			return null;
		}

		@Override
		public QuoteAndSize getLastTrade() {
			// Does nothing
			return null;
		}

		@Override
		public String getMarketCode() {
			// Does nothing
			return null;
		}

		@Override
		public String getMarketOperationContract() {
			return _model.getMarketOperationContract();
		}

		@Override
		public int getMaximumContractsTraded() {
			// Does nothing
			return 0;
		}

		@Override
		public int getMinimumContractsTraded() {
			// Does nothing
			return 0;
		}

		@Override
		public double getMinimumQuoteIncrement() {
			// Does nothing
			return 0;
		}

		@Override
		public String getName() {
			// Does nothing
			return null;
		}

		@Override
		public Quote getOpeningQuote() {
			// Does nothing
			return null;
		}

		@Override
		public QuoteType getQuoteType() {
			return _model.getQuoteType();
		}

		@Override
		public SecondaryOrderPrecedenceRuleType[] getSecondaryOrderPrecedenceRules() {
			// Does nothing
			return null;
		}

		@Override
		public MarketState getState() {
			return _model.getState();
		}

		@Override
		public TimeOfDay getTradingDayEnd() {
			// Does nothing
			return null;
		}

		@Override
		public TimePeriod getTradingHours() {
			// Does nothing
			return null;
		}

		@Override
		public TradingSession getTradingSession() {
			// Does nothing
			return null;
		}

		@Override
		public boolean getAllowHiddenOrders() {
			return false;
		}

		@Override
		public QuoteAndSize getBestAskSystem() {
			return null;
		}

		@Override
		public QuoteAndSize getBestBidSystem() {
			return null;
		}

		@Override
		public String getMarketTimeZoneID() {
			return null;
		}

		@Override
		public String getCommissionCurrency() {
			return null;
		}

		@Override
		public String getClearingBank() {
			return null;
		}

		@Override
		public MarketTimer getTimeTrigger() {
			return null;
		}

		@Override
		public QuoteAndSize getPreviousLastTrade() {
			return null;
		}

		@Override
		public Quote getPreviousClosingQuote() {
			return null;
		}

		@Override
		public Quote getPreviousOpeningQuote() {
			return null;
		}

		@Override
		public boolean getAllowSizeRestrictionOnOrders() {
			return false;
		}

		@Override
		public void executeCallMarketMaintenance(
				OrderManagementContext orderManagementContext_) {
			// Does nothing
		}

		@Override
		public void executeRegularMarketMaintenance(
				OrderManagementContext orderManagementContext_) {
			// Does nothing
		}
	}

	@Override
	public void createAuditTrailEntry(AuditTrailEntry auditTrailEntry_) {
		_auditTrails.add(0, auditTrailEntry_);
	}

	@Override
	public PagedAuditTrailEntryList findAllAuditTrailEntriesFromCursor(
			String cursorString_, int maxNumberOfAuditTrailEntryOnPage_) {
		PagedAuditTrailEntryList list = new PagedAuditTrailEntryList();
		list.setAuditTrailEntries(_auditTrails.toArray(new AuditTrailEntry[_auditTrails.size()]));
		return list;
	}
	
	public Long createMatchedTrade(MatchedTrade matchedTrade_) {
		if(matchedTrade_.getTransactionID() == null) {			
			matchedTrade_.setTransactionID((MockIdentityGenerator.getId()));
		}
		
		_mapIdToTrade.put(matchedTrade_.getTransactionID(), matchedTrade_);
		
		return matchedTrade_.getTransactionID();
	}

	public MatchedTrade[] findAllMatchedTradeByMarket(String marketCode_) {
		List<MatchedTrade> tradesForMarket = _mapIdToTrade.values().stream().filter(trade -> trade.getMarketOfTheTrade().equals(
					marketCode_)).collect(Collectors.toList());
		
		return tradesForMarket.toArray(new MatchedTrade[tradesForMarket.size()]);
	}
	
	public MatchedTrade findMatchedTradeByTransactionId(Long transactionId_) {
		List<MatchedTrade> trades = _mapIdToTrade.values().stream().filter(matchedTrade -> matchedTrade.getTransactionID().longValue() == transactionId_).collect(Collectors.toList());
		
		return !trades.isEmpty() ? trades.get(0) : null;
	}

	public PagedMatchedTradeSideList findMatchedTradeByUser(String userID_,
			String cursorString_, int maxNumberOfMatchedTradeSidesOnPage_) {
		List<TradeSide> list = new ArrayList<TradeSide>();
		
		for (MatchedTrade trade : _mapIdToTrade.values()) {
			if(userID_.equals(trade.getBuySide().getTraderID())) {
				list.add(trade.getBuySide());
			} else if(userID_.equals(trade.getSellSide().getTraderID())) {
				list.add(trade.getSellSide());
			}
		}
		
		PagedMatchedTradeSideList pagedList = new PagedMatchedTradeSideList();
		pagedList.setMatchedTradeSides(list.toArray(new TradeSide[list.size()]));
		pagedList.setCursorString(cursorString_);
		pagedList.setMaxNumberOfMatchedTradeSidesOnPage(maxNumberOfMatchedTradeSidesOnPage_);
		
		return pagedList;
	}

	public PagedMatchedTradeSideList findMatchedTradeByUserAndMarket(
			String userID_, String marketCode_, String cursorString_,
			int maxNumberOfMatchedTradeSidesOnPage_) {
		List<TradeSide> list = new ArrayList<TradeSide>();
		
		for (MatchedTrade trade : _mapIdToTrade.values()) {
			if(marketCode_.equals(trade.getMarketOfTheTrade()) && 
					(userID_.equals(trade.getBuySide().getTraderID()))) {
				list.add(trade.getBuySide());
			} else if(marketCode_.equals(trade.getMarketOfTheTrade()) && 
					(userID_.equals(trade.getSellSide().getTraderID()))) {
				list.add(trade.getSellSide());
			}
		}
		
		PagedMatchedTradeSideList pagedList = new PagedMatchedTradeSideList();
		pagedList.setMatchedTradeSides(list.toArray(new TradeSide[list.size()]));
		pagedList.setCursorString(cursorString_);
		pagedList.setMaxNumberOfMatchedTradeSidesOnPage(maxNumberOfMatchedTradeSidesOnPage_);
		
		return pagedList;
	}
	
	@Override
	public TradeSide[] findMatchedTradeByUserAndMarketInternal(String userID_,
			String marketCode_) {
		return findMatchedTradeByUserAndMarket(
				userID_, marketCode_, null,
				1).getMatchedTradeSides();
	}
	

	@Override
	public TradeSide[] findMatchedTradeByUserInternal(String userID_) {
		List<TradeSide> list = _mapIdToTrade.values().stream().map(trade -> trade.getSellSide()).collect(Collectors.toList());
		
		return list.toArray(new TradeSide[list.size()]);
	}
	


	@Override
	public TradeSide findMatchedTradeByReferenceUserAndMarket(
			String externalReference_, String userID_, String marketCode_) {
		TradeSide tradeFound = null;
		
		for (MatchedTrade trade : _mapIdToTrade.values()) {
			if(marketCode_.equals(trade.getMarketOfTheTrade()) && 
					(userID_.equals(trade.getBuySide().getTraderID())) && 
					(externalReference_.equals(trade.getBuySide().getExternalOrderReference()))) {
				 tradeFound = (trade.getBuySide());
			} else if(marketCode_.equals(trade.getMarketOfTheTrade()) && 
					(userID_.equals(trade.getSellSide().getTraderID()))&& 
					(externalReference_.equals(trade.getSellSide().getExternalOrderReference()))) {
				 tradeFound = (trade.getSellSide());
			}
		};
		
		return tradeFound;
	}
}
