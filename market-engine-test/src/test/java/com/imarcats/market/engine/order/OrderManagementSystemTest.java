package com.imarcats.market.engine.order;

import com.imarcats.infrastructure.server.trigger.MockMarketDataSource;
import com.imarcats.infrastructure.server.trigger.MockPropertyChangeBroker;
import com.imarcats.infrastructure.server.trigger.MockTimeTrigger;
import com.imarcats.interfaces.client.v100.dto.types.DatePropertyDto;
import com.imarcats.interfaces.client.v100.dto.types.MarketDataType;
import com.imarcats.interfaces.client.v100.dto.types.ObjectPropertyDto;
import com.imarcats.interfaces.client.v100.dto.types.PropertyChangeDto;
import com.imarcats.interfaces.client.v100.dto.types.PropertyValueChangeDto;
import com.imarcats.interfaces.client.v100.exception.ExceptionLanguageKeys;
import com.imarcats.interfaces.client.v100.exception.MarketRuntimeException;
import com.imarcats.interfaces.client.v100.exception.MarketSecurityException;
import com.imarcats.interfaces.client.v100.i18n.MarketSystemMessageLanguageKeys;
import com.imarcats.interfaces.client.v100.notification.ChangeOrigin;
import com.imarcats.interfaces.client.v100.notification.PropertyChanges;
import com.imarcats.interfaces.server.v100.dto.mapping.PropertyDtoMapping;
import com.imarcats.internal.server.interfaces.market.MarketInternal;
import com.imarcats.internal.server.interfaces.order.OrderManagementContext;
import com.imarcats.internal.server.interfaces.order.OrderInternal;
import com.imarcats.internal.server.interfaces.order.OrderManagementContextImpl;
import com.imarcats.market.engine.testutils.MockDatastores;
import com.imarcats.market.engine.testutils.MockPropertyChangeSessionImpl;
import com.imarcats.market.engine.testutils.OrderCompareTestCaseBase;
import com.imarcats.model.Market;
import com.imarcats.model.MatchedTrade;
import com.imarcats.model.Order;
import com.imarcats.model.OrderPropertyNames;
import com.imarcats.model.TradeSide;
import com.imarcats.model.mutators.PropertyChange;
import com.imarcats.model.mutators.PropertyListValueChange;
import com.imarcats.model.mutators.PropertyValueChange;
import com.imarcats.model.test.testutils.TestOrderFlowGenerator;
import com.imarcats.model.types.ActivationStatus;
import com.imarcats.model.types.DoubleProperty;
import com.imarcats.model.types.ExecutionSystem;
import com.imarcats.model.types.MarketState;
import com.imarcats.model.types.OrderExpirationInstruction;
import com.imarcats.model.types.OrderSide;
import com.imarcats.model.types.OrderState;
import com.imarcats.model.types.OrderTriggerInstruction;
import com.imarcats.model.types.OrderType;
import com.imarcats.model.types.PagedMatchedTradeSideList;
import com.imarcats.model.types.PagedOrderList;
import com.imarcats.model.types.Quote;
import com.imarcats.model.types.QuoteAndSize;
import com.imarcats.model.types.QuoteType;

/**
 * Test functionality of Order Management System 
 * Requirements for these tests can be found in 
 *  - Order Matching and Order Management � Business Requirement Specification: Order Management section 
 * @author Adam
 */
public class OrderManagementSystemTest extends OrderCompareTestCaseBase {
	public static final int MAX_NUMBER_OF_ORDERS_ON_PAGE = 2;
	private static final String BENS_SESSION = "Ben";
	private static final String SUES_SESSION = "Sue";

	protected boolean generateKeys() {
		return true;
	}
	
	public void testGetTrades() throws Exception {
		// initialize test data 
		
		
		MockDatastores datastore = createDatastores();
		
		MockOrderActionRequestor mockOrderActionExecutor = new MockOrderActionRequestor(datastore, datastore);
		final OrderManagementSystem orderManagementSystem = new OrderManagementSystem(datastore, datastore, datastore, mockOrderActionExecutor, mockOrderActionExecutor);
		
		MatchedTrade matchedTrade1 = createMatchedTrade(MockDatastores.MARKET_ID); 
		TradeSide bensTradeSide1 = matchedTrade1.getBuySide();
		bensTradeSide1.setTraderID(BENS_SESSION);
		TradeSide suesTradeSide1 = matchedTrade1.getSellSide();
		suesTradeSide1.setTraderID(SUES_SESSION);
		
		// to make sure trades are apart in time
		Thread.sleep(1000);

		MatchedTrade matchedTrade2 = createMatchedTrade(MockDatastores.MARKET_ID_2); 
		TradeSide suesTradeSide2 = matchedTrade2.getBuySide();
		suesTradeSide2.setTraderID(SUES_SESSION);
		TradeSide bensTradeSide2 = matchedTrade2.getSellSide();
		bensTradeSide2.setTraderID(BENS_SESSION);

		// reverse order to model, that real datastore will sort trades by reverse trade time
		openTransaction();
		matchedTrade2.setTransactionID(null);
		datastore.createMatchedTrade(matchedTrade2);
		matchedTrade1.setTransactionID(null);
		datastore.createMatchedTrade(matchedTrade1);
		commitTransaction();
		
		// end of initialize test data 
				
		// error - get trades for user on market
		try{
			orderManagementSystem.getMatchedTradesFor(null, BENS_SESSION, null, MAX_NUMBER_OF_ORDERS_ON_PAGE);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.MARKET_MUST_HAVE_CODE, e.getLanguageKey());
		}
		
		// test correct behavior 
		// get trades for user 
		PagedMatchedTradeSideList tradeSidesForBen = orderManagementSystem.getMatchedTradesFor(BENS_SESSION, null, MAX_NUMBER_OF_ORDERS_ON_PAGE);
		assertEqualsTradeSides(new TradeSide[] { bensTradeSide2, bensTradeSide1 }, tradeSidesForBen.getMatchedTradeSides());
		// get trades for user on market 
		orderManagementSystem.getMatchedTradesFor(MockDatastores.MARKET_ID, BENS_SESSION, null, MAX_NUMBER_OF_ORDERS_ON_PAGE);
		
	}

	public void testGetOrders() throws Exception {
		// initialize test data 
		
		
		MockDatastores datastore = createDatastores();
		MockOrderActionRequestor mockOrderActionExecutor = new MockOrderActionRequestor(datastore, datastore);
		final OrderManagementSystem orderManagementSystem = new OrderManagementSystem(datastore, datastore, datastore, mockOrderActionExecutor, mockOrderActionExecutor);
		
		Market marketModel = getMarket(datastore);
		
		TestOrderFlowGenerator generator = new TestOrderFlowGenerator(QuoteType.Price, marketModel, generateKeys());
		Order[] orders = generator.getOrders();
		final Order order = orders[0];
		
		order.setKey(null);
		order.setSubmitterID(null);
		order.setSubmissionDate(null);
		order.setTargetMarketCode(null);
		
		final Order order2 = orders[1];		
		
		order2.setKey(null);
		order2.setSubmitterID(null);
		order2.setSubmissionDate(null);
		order2.setTargetMarketCode(null);

		setupUserAndMarketForTest(datastore);
		
		setMarket2Activated(datastore);
		
		OrderManagementContext context = createOrderManagerContext();  
		
		testCreateOrderForBen(order, orderManagementSystem, context);

		// create order on market 2
		openTransaction();
		orderManagementSystem.createOrder(MockDatastores.MARKET_ID_2, order2, BENS_SESSION, context);
		commitTransaction();
		// end of initialize test data 
		
		// error - get orders for user on market
		try{
			orderManagementSystem.getOrdersFor(null, BENS_SESSION, null, MAX_NUMBER_OF_ORDERS_ON_PAGE);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.MARKET_MUST_HAVE_CODE, e.getLanguageKey());
		}
		
		// test correct behavior 
		
		// get orders for user 
		PagedOrderList ordersForBen = orderManagementSystem.getOrdersFor(BENS_SESSION, null, MAX_NUMBER_OF_ORDERS_ON_PAGE);
		assertEqualsOrders(new Order[] { order, order2 }, ordersForBen.getOrders());
		// get orders for user on market 
		PagedOrderList ordersForMarket = orderManagementSystem.getOrdersFor(MockDatastores.MARKET_ID, BENS_SESSION, null, MAX_NUMBER_OF_ORDERS_ON_PAGE);
		assertEquals(1, ordersForMarket.getOrders().length);
		assertEqualsOrder(order, ordersForMarket.getOrders()[0]);					
	}

	private void setMarket2Activated(
			MockDatastores datastore) {
		openTransaction();
		datastore.findMarketBy(MockDatastores.MARKET_ID_2).getMarketModel().setActivationStatus(ActivationStatus.Activated);
		commitTransaction();	
	}

	protected MockDatastores createDatastores() {
		return new MockDatastores(true);
	}
	
	protected void openTransaction() {
		
	}

	protected void commitTransaction() {
	}
	
	public void testCreateOrder() throws Exception {
		// initialize test data 
		
		
		MockDatastores datastore = createDatastores();
		MockOrderActionRequestor mockOrderActionExecutor = new MockOrderActionRequestor(datastore, datastore);
		final OrderManagementSystem orderManagementSystem = new OrderManagementSystem(datastore, datastore, datastore, mockOrderActionExecutor, mockOrderActionExecutor);

		OrderManagementContext context = createOrderManagerContext();
		
		Market marketModel = getMarket(datastore);
		
		TestOrderFlowGenerator generator = new TestOrderFlowGenerator(QuoteType.Price, marketModel, generateKeys());
		Order[] orders = generator.getOrders();
		final Order order = orders[0];
		
		order.setKey(null);
		order.setSubmitterID(null);
		order.setSubmissionDate(null);
		order.setTargetMarketCode(null);
		// end initialize test data  
		
		// test create errors 

		context = createOrderManagerContext();
		
		// test invalid market 
		try {
			orderManagementSystem.createOrder(MockDatastores.MARKET_ID, null, BENS_SESSION, context);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.NULL_ORDER_CANNOT_BE_CREATED, e.getLanguageKey());
			checkIfNoPropertyChange(context);
		}

		try {
			orderManagementSystem.createOrder(null, order, BENS_SESSION, context);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.MARKET_MUST_HAVE_CODE, e.getLanguageKey());
			checkIfNoPropertyChange(context);
		}
		
		try {
			orderManagementSystem.createOrder(MockDatastores.MARKET_ID + "1", order, BENS_SESSION, context);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.NON_EXISTENT_MARKET, e.getLanguageKey());
			checkIfNoPropertyChange(context);
		}

		// test error on non-active market 
		try {
			testCreateOrderForBen(order, orderManagementSystem, context);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.ORDER_CANNOT_BE_CREATED_NON_ACTIVE_MARKET, e.getLanguageKey());
			checkIfNoPropertyChange(context);
		} finally {
			commitTransaction();
		}
		
		// test create order
		setMarketActivated(datastore);
		
		context = createOrderManagerContext();
		
		// set up market order correctly 
		setMarketToCall(datastore); 
		
		order.setType(OrderType.Market);
		order.setExpirationInstruction(OrderExpirationInstruction.ImmediateOrCancel);
		order.setExecuteEntireOrderAtOnce(true);
		
		context = createOrderManagerContext();
		
		// test market order on call market
		try {
			testCreateOrderForBen(order, orderManagementSystem, context);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.MARKET_ORDER_IS_NOT_SUPPORTED_ON_CALL_MARKET, e.getLanguageKey());
		} finally {
			commitTransaction();
		}
		
		setMarketCombined(datastore); 
		
		context = createOrderManagerContext();
		
		// test successful creation 
		testCreateOrderForBen(order, orderManagementSystem, context);
		
		// check data on created order 
		assertTrue(order.getSubmitterID() != null);
		assertEquals(BENS_SESSION, order.getSubmitterID());
		
		assertTrue(order.getCreationAudit() != null);
		assertTrue(order.getCreationAudit().getDateTime() != null);		
		assertEquals(BENS_SESSION, order.getCreationAudit().getUserID());

		assertTrue(order.getKey() != null);
		assertTrue(datastore.findOrderBy(order.getKey()) != null); 
		assertEqualsOrder(order, datastore.findOrderBy(order.getKey()).getOrderModel());
		
		assertEquals(order.getState(), OrderState.Created);
		assertEquals(MockDatastores.MARKET_ID, order.getTargetMarketCode());
		
		// check property change notification
		checkStateChange(context, OrderState.Created, ChangeOrigin.User);	
	}

	private void testCreateOrderForBen(final Order order,
			final OrderManagementSystem orderManagementSystem,
			OrderManagementContext context) {
		openTransaction();
		orderManagementSystem.createOrder(MockDatastores.MARKET_ID, order, BENS_SESSION, context);
		commitTransaction();
	}

	private void setMarketCombined(
			MockDatastores datastore) {
		openTransaction();
		datastore.findMarketBy(MockDatastores.MARKET_ID).getMarketModel().setExecutionSystem(ExecutionSystem.Combined);
		commitTransaction();
	}

	private void setMarketToCall(MockDatastores datastore) {
		openTransaction();
		datastore.findMarketBy(MockDatastores.MARKET_ID).getMarketModel().setExecutionSystem(ExecutionSystem.CallMarketWithSingleSideAuction);
		commitTransaction();
	}

	private void setMarketActivated(MockDatastores datastore) {
		openTransaction();
		datastore.findMarketBy(MockDatastores.MARKET_ID).getMarketModel().setActivationStatus(ActivationStatus.Activated);
		commitTransaction();		
	}
	
	public void testDeleteOrder() throws Exception {
		
		OrderManagementContext context = createOrderManagerContext();
		
		MockDatastores datastore = createDatastores(); 
		MockOrderActionRequestor mockOrderActionExecutor = new MockOrderActionRequestor(datastore, datastore);
		final OrderManagementSystem orderManagementSystem = new OrderManagementSystem(datastore, datastore, datastore, mockOrderActionExecutor, mockOrderActionExecutor);

		Market marketModel = getMarket(datastore);
		
		Order order = createOrder(marketModel);
		
		
		setupUserAndMarketForTest(datastore);
		
		context = createOrderManagerContext();
		
		testCreateOrderForBen(order, orderManagementSystem, context);
		
		context = createOrderManagerContext();
		
		// test non-existent order
		try {
			orderManagementSystem.deleteOrder(null, BENS_SESSION, context);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.NON_EXISTENT_ORDER, e.getLanguageKey());
			checkIfNoPropertyChange(context);
		}
		
		try {
			orderManagementSystem.deleteOrder(order.getKey().longValue() + 1, BENS_SESSION, context);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.NON_EXISTENT_ORDER, e.getLanguageKey());
			checkIfNoPropertyChange(context);
		}
		
		// reload order
		openTransaction();
		order = datastore.findOrderBy(order.getKey()).getOrderModel();
		
		// test delete submitted order
		order.setState(OrderState.WaitingSubmit);
		
		commitTransaction();
		
		context = createOrderManagerContext();
		
		try {
			testDeleteOrderByBen(order, context, orderManagementSystem);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.SUBMITTED_ORDER_CANNOT_BE_DELETED_CANCEL_FIRST, e.getLanguageKey());
			checkIfNoPropertyChange(context);
		} finally {
			commitTransaction();
		}	

		// reload order
		openTransaction();
		order = datastore.findOrderBy(order.getKey()).getOrderModel();
		
		// test delete submitted order
		order.setState(OrderState.Submitted);

		commitTransaction();
		
		context = createOrderManagerContext();
		
		try {
			testDeleteOrderByBen(order, context, orderManagementSystem);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.SUBMITTED_ORDER_CANNOT_BE_DELETED_CANCEL_FIRST, e.getLanguageKey());
			checkIfNoPropertyChange(context);
		} finally {
			commitTransaction();
		}		

		// reload order
		openTransaction();
		order = datastore.findOrderBy(order.getKey()).getOrderModel();
		
		// test delete executed order
		order.setState(OrderState.Executed);
		
		commitTransaction();
		
		context = createOrderManagerContext();
		
		testDeleteOrderByBen(order, context, orderManagementSystem);
		
		// check property change notification
		checkStateChange(context, OrderState.Deleted, ChangeOrigin.User);
		
		// check order
		assertEquals(null, datastore.findOrderBy(order.getKey()));
		
		// check delete by other user
		order = createOrder(marketModel);
		
		context = createOrderManagerContext();
		
		testCreateOrderForBen(order, orderManagementSystem, context);
		
		context = createOrderManagerContext();
		
		// commit changes in markets
		context = createOrderManagerContext();
		
		testDeleteOrderByBen(order, context, orderManagementSystem);
		
		// commit changes in order
		context = createOrderManagerContext();
		
		assertEquals(null, datastore.findOrderBy(order.getKey()));
	}

	private void testDeleteOrderByBen(Order order,
			OrderManagementContext context,
			final OrderManagementSystem orderManagementSystem) {
		openTransaction();
		orderManagementSystem.deleteOrder(order.getKey(), BENS_SESSION, context);
		commitTransaction();
	}

	private Order createOrder(Market marketModel) throws InterruptedException {
		TestOrderFlowGenerator generator = new TestOrderFlowGenerator(QuoteType.Price, marketModel, generateKeys());
		Order[] orders = generator.getOrders();
		Order order = orders[0];
		
		order.setKey(null);
		order.setSubmitterID(null);
		order.setSubmissionDate(null);
		order.setTargetMarketCode(null);
		
		order.setKey(null);
		return order;
	}
	
	public void testSubmit() throws Exception {
		
		
		MockDatastores datastore = createDatastores();
		MockTimeTrigger trigger = new MockTimeTrigger(datastore);
		MockMarketDataSource marketDataSource = new MockMarketDataSource(datastore);
		MockPropertyChangeBroker propertyChangeBroker = new MockPropertyChangeBroker(datastore);
		MockOrderActionRequestor mockOrderActionExecutor = new MockOrderActionRequestor(datastore, datastore);
		OrderManagementSystem orderManagementSystem = new OrderManagementSystem(datastore, datastore, datastore, mockOrderActionExecutor, mockOrderActionExecutor);
		
		Market marketModel = getMarket(datastore);
		
		TestOrderFlowGenerator generator = new TestOrderFlowGenerator(QuoteType.Price, marketModel, generateKeys());
		Order[] orders = generator.getOrders();
		Order order = orders[0];
		order.setKey(null);
		order.setSide(OrderSide.Buy);
		order.setType(OrderType.Limit);
		order.setLimitQuoteValue(wrapQuote(21));
		order.setSubmissionDate(null);
		order.setSubmitterID(null);
		order.setTargetMarketCode(null);
		order.setState(OrderState.Created);
		order.setCreationAudit(null);
		
		setupUserAndMarketForTest(datastore);

		OrderManagementContextImpl context = createOrderManagerContext(marketDataSource, propertyChangeBroker);
		
		// test order submit
		testCreateOrderByBen(order, orderManagementSystem, context);
		
		// commit changes in order
		context = createOrderManagerContext(marketDataSource, propertyChangeBroker);
		
		assertTrue(order.getSubmissionDate() == null);
		context = createOrderManagerContext(marketDataSource, propertyChangeBroker);
		
		// commit changes in order
		context = createOrderManagerContext(marketDataSource, propertyChangeBroker);
		
		testSubmitOrderByBen(order, orderManagementSystem, context);
		
		// re-load order 
		order = datastore.findOrderBy(order.getKey()).getOrderModel();
		
		// check property change notification
		checkStrictStateChangeWithCommentAndSubmissionDate(context, OrderState.Submitted, null, order.getSubmissionDate(), ChangeOrigin.User);
		
		assertEquals(OrderState.Submitted, order.getState());
		assertEquals(null, order.getCancellationCommentLanguageKey());
		
		assertTrue(order.getSubmissionDate() != null);
		
		Long key = trigger.getActionKey();
		assertEquals(datastore.findOrderBy(order.getKey()).getExpirationTriggerActionKey(), key);
		
		MarketInternal marketInternal = datastore.findMarketBy(MockDatastores.MARKET_ID);
		
		assertTrue(marketInternal.getBestAskSystem() == null);
		assertTrue(marketInternal.getBestBidSystem() != null);
		
		assertEquals(order.getSize(), marketInternal.getBestBidSystem().getSize());
		assertTrue(marketInternal.getBestBidSystem().getQuote() != null);
		assertEquals(order.getLimitQuoteValue().getQuoteValue(), marketInternal.getBestBidSystem().getQuote().getQuoteValue());
		assertTrue(marketInternal.getBestBidSystem().getQuote().getValidQuote());	
		
		
		// test stop order triggered submit
		generator = new TestOrderFlowGenerator(QuoteType.Price, marketModel, generateKeys());
		orders = generator.getOrders();
		Order order1 = orders[0];
		setOrderDefaults(order1);
		
		order1.setTriggerInstruction(OrderTriggerInstruction.StopLoss);
		DoubleProperty property = new DoubleProperty();
		property.setName(OrderPropertyNames.STOP_QUOTE_PROPERTY_NAME);
		property.setValue(20.0);
		
		order1.addTriggerProperty(property);

		context = createOrderManagerContext(marketDataSource, propertyChangeBroker);
		
		testCreateOrderByBen(order1, orderManagementSystem, context);
		
		assertFalse(marketDataSource.hasMarketDataChangeListeners());

		assertTrue(order1.getSubmissionDate() == null);
		
		context = createOrderManagerContext(marketDataSource, propertyChangeBroker);
		
		testSubmitOrderByBen(order1, orderManagementSystem, context);
		
		
		
		// re-load order 
		order1 = datastore.findOrderBy(order1.getKey()).getOrderModel();
		
		OrderState expectedState = OrderState.WaitingSubmit;
		// check property change notification
		assertEquals(3, ((MockPropertyChangeSessionImpl)((MockPropertyChangeSessionImpl)context.getPropertyChangeSession())).getPropertyChanges().length);
		assertEquals(1, ((MockPropertyChangeSessionImpl)context.getPropertyChangeSession()).getPropertyChanges()[0].getChanges().length);
		assertEquals(1, ((MockPropertyChangeSessionImpl)context.getPropertyChangeSession()).getPropertyChanges()[1].getChanges().length);

		assertEquals(OrderState.PendingSubmit.toString(), ((ObjectPropertyDto)getPropertyChange(0, 0, context).getProperty()).getValue().getObjectValue().toString());
		assertEquals(order1.getSubmissionDate(), ((DatePropertyDto)getPropertyChange(1, 0, context).getProperty()).getValue());		
		assertEquals(expectedState.toString(), ((ObjectPropertyDto)getPropertyChange(2, 0, context).getProperty()).getValue().getObjectValue().toString());
		
		assertEquals(expectedState, order1.getState());

		assertTrue(order1.getSubmissionDate() != null);
		assertTrue(order1.getQuoteChangeTriggerKey() != null);
		
		assertTrue(marketDataSource.hasMarketDataChangeListeners());
		
		Long expirationTriggerAction1 = datastore.findOrderBy(order1.getKey()).getExpirationTriggerActionKey();
		key = trigger.getActionKey();
		assertEquals(expirationTriggerAction1, key);
		
		assertTrue(marketInternal.getBestAskSystem() == null);
		assertTrue(marketInternal.getBestBidSystem() != null);
		
		QuoteAndSize lastTrade = new QuoteAndSize();
		lastTrade.setSize(10);
		Quote lastQuote = new Quote();
		lastQuote.setQuoteValue(20);
		lastQuote.setValidQuote(true);
		lastTrade.setQuote(lastQuote);

		openTransaction();
		marketInternal = datastore.findMarketBy(MockDatastores.MARKET_ID);
		marketInternal.recordLastTrade(lastTrade, context.getMarketDataSession());
		commitTransaction();

		context = createOrderManagerContext(marketDataSource, propertyChangeBroker);

		openTransaction();
		marketDataSource.marketDataUpdated(marketInternal.getMarketCode(), MarketDataType.Last, lastTrade, null, context);
		commitTransaction();
		
		// re-load order 
		order1 = datastore.findOrderBy(order1.getKey()).getOrderModel();
		
		// re-load market
		marketInternal = datastore.findMarketBy(marketInternal.getMarketCode());
		
		// check property change notification
		checkStrictStateChangeWithCommentAndSubmissionDate(context, OrderState.Submitted, null, null, ChangeOrigin.System);
		
		assertEquals(OrderState.Submitted, order1.getState());
		assertEquals(null, order.getCancellationCommentLanguageKey());

		assertFalse(marketDataSource.hasMarketDataChangeListeners());
		
		assertEquals(expirationTriggerAction1, trigger.getAction());
		
		assertTrue(marketInternal.getBestAskSystem() != null);
		assertTrue(marketInternal.getBestBidSystem() != null);
		
		assertEquals(order.getSize(), marketInternal.getBestBidSystem().getSize());
		assertTrue(marketInternal.getBestBidSystem().getQuote() != null);
		assertEquals(order.getLimitQuoteValue().getQuoteValue(), marketInternal.getBestBidSystem().getQuote().getQuoteValue());
		assertTrue(marketInternal.getBestBidSystem().getQuote().getValidQuote());	
		
		assertEquals(order1.getSize(), marketInternal.getBestAskSystem().getSize());
		assertTrue(marketInternal.getBestAskSystem().getQuote() != null);
		assertEquals(order1.getLimitQuoteValue().getQuoteValue(), marketInternal.getBestAskSystem().getQuote().getQuoteValue());
		assertTrue(marketInternal.getBestAskSystem().getQuote().getValidQuote());	
	}

	private void testSubmitOrderByBen(Order order,
			OrderManagementSystem orderManagementSystem,
			OrderManagementContextImpl context) {
		openTransaction();
		orderManagementSystem.submitOrder(order.getKey(), BENS_SESSION, context);
		commitTransaction();
	}

	private void testCreateOrderByBen(Order order,
			OrderManagementSystem orderManagementSystem,
			OrderManagementContextImpl context) {
		openTransaction();
		orderManagementSystem.createOrder(MockDatastores.MARKET_ID, order, BENS_SESSION, context);
		commitTransaction();
	}
	
	public void testCancelAll() throws Exception {
		
		
		MockDatastores datastore = createDatastores();
		MockMarketDataSource marketDataSource = new MockMarketDataSource(datastore);
		MockPropertyChangeBroker propertyChangeBroker = new MockPropertyChangeBroker(datastore);

		MockOrderActionRequestor mockOrderActionExecutor = new MockOrderActionRequestor(datastore, datastore);
		OrderManagementSystem orderManagementSystem = new OrderManagementSystem(datastore, datastore, datastore, mockOrderActionExecutor, mockOrderActionExecutor);

		Market marketModel = getMarket(datastore);
		marketModel.setTradingDayEnd(createTimePeriod().getStartTime());
		
		TestOrderFlowGenerator generator = new TestOrderFlowGenerator(QuoteType.Price, marketModel, generateKeys());
		Order[] orders = generator.getOrders();
		Order order1 = orders[0];	
		setOrderDefaults(order1);
		Order order2 = orders[1];	
		setOrderDefaults(order2);
		Order order3 = orders[2];	
		setOrderDefaults(order3);
		Order order4 = orders[3];	
		setOrderDefaults(order4);
		Order order5 = orders[4];	
		setOrderDefaults(order5);
		
		setupUserAndMarketForTest(datastore);
		
		OrderManagementContextImpl context = createOrderManagerContext(marketDataSource, propertyChangeBroker);

		// create orders 
		testCreateOrderByBen(order1, orderManagementSystem, context);		
		testCreateOrderOnMarket2ByBen(order2, orderManagementSystem, context);	
		testCreateOrderByBen(order3, orderManagementSystem, context);
		testCreateOrderBySue(order4, orderManagementSystem, context);
		testCreateOrderOnMarket2BySue(order5, orderManagementSystem, context);
		
		// commit changes in order
		context = createOrderManagerContext(marketDataSource, propertyChangeBroker);
		
		// submit orders 
		testSubmitOrder(order2, orderManagementSystem, context, BENS_SESSION);
		
		context = createOrderManagerContext(marketDataSource, propertyChangeBroker);
		
		testSubmitOrder(order3, orderManagementSystem, context, BENS_SESSION);
		
		context = createOrderManagerContext(marketDataSource, propertyChangeBroker);
		
		testSubmitOrder(order4, orderManagementSystem, context, SUES_SESSION);
		
		context = createOrderManagerContext(marketDataSource, propertyChangeBroker);
		
		testSubmitOrder(order5, orderManagementSystem, context, SUES_SESSION);
		
		context = createOrderManagerContext(marketDataSource, propertyChangeBroker);
		
		// cancel all Ben's orders on market1 by Ben
		testCancelAllOrdersForBenBySelf(orderManagementSystem, context);
		
		context = createOrderManagerContext(marketDataSource, propertyChangeBroker);
		
		// check orders
		checkState(order1.getKey(), OrderState.Created, datastore);
		checkState(order2.getKey(), OrderState.Canceled, datastore);
		checkState(order3.getKey(), OrderState.Canceled, datastore);
		checkState(order4.getKey(), OrderState.Submitted, datastore);
		checkState(order5.getKey(), OrderState.Submitted, datastore);
		
		context = createOrderManagerContext(marketDataSource, propertyChangeBroker);
		
		// cancel all Ben's orders on market1 by Sue
		testCancelAllOrdersForSueBySelf(orderManagementSystem, context);
		
		context = createOrderManagerContext(marketDataSource, propertyChangeBroker);
		
		// check orders
		checkState(order1.getKey(), OrderState.Created, datastore);
		checkState(order2.getKey(), OrderState.Canceled, datastore);
		checkState(order3.getKey(), OrderState.Canceled, datastore);
		checkState(order4.getKey(), OrderState.Canceled, datastore);
		checkState(order5.getKey(), OrderState.Canceled, datastore);
	}

	private void testCreateOrderOnMarket2BySue(
			Order order, OrderManagementSystem orderManagementSystem,
			OrderManagementContextImpl context) {
		openTransaction();
		orderManagementSystem.createOrder(MockDatastores.MARKET_ID_2, order, SUES_SESSION, context);
		commitTransaction();
	}

	private void testCreateOrderOnMarket2ByBen(Order order,
			OrderManagementSystem orderManagementSystem,
			OrderManagementContextImpl context) {
		openTransaction();
		orderManagementSystem.createOrder(MockDatastores.MARKET_ID_2, order, BENS_SESSION, context);
		commitTransaction();
	}

	private void testCreateOrderBySue(Order order,
			OrderManagementSystem orderManagementSystem,
			OrderManagementContextImpl context) {
		openTransaction();
		orderManagementSystem.createOrder(MockDatastores.MARKET_ID, order, SUES_SESSION, context);
		commitTransaction();
	}

	private void testSubmitOrder(Order order,
			OrderManagementSystem orderManagementSystem,
			OrderManagementContextImpl context, String user) {
		openTransaction();
		orderManagementSystem.submitOrder(order.getKey(), user, context);
		commitTransaction();
	}

	private void checkState(Long orderKey_, OrderState state_, MockDatastores datastore_) {
		Order order = datastore_.findOrderBy(orderKey_).getOrderModel();
		assertEquals(state_, order.getState());
	}
	
	public void testCancelAndOtherActions() throws Exception {
		
		
		MockDatastores datastore = createDatastores();
		MockTimeTrigger trigger = datastore.getTrigger();
		MockMarketDataSource marketDataSource = new MockMarketDataSource(datastore);
		MockPropertyChangeBroker propertyChangeBroker = new MockPropertyChangeBroker(datastore);

		MockOrderActionRequestor mockOrderActionExecutor = new MockOrderActionRequestor(datastore, datastore);
		OrderManagementSystem orderManagementSystem = new OrderManagementSystem(datastore, datastore, datastore, mockOrderActionExecutor, mockOrderActionExecutor);

		Market marketModel = getMarket(datastore);
		marketModel.setTradingDayEnd(createTimePeriod().getStartTime());
		
		TestOrderFlowGenerator generator = new TestOrderFlowGenerator(QuoteType.Price, marketModel, generateKeys());
		Order[] orders = generator.getOrders();
		Order order = orders[0];
		setOrderDefaults(order);
		
		setupUserAndMarketForTest(datastore);
		
		OrderManagementContextImpl context = createOrderManagerContext(marketDataSource, propertyChangeBroker);

		testCreateOrderByBen(order, orderManagementSystem, context);
		
		context = createOrderManagerContext(marketDataSource, propertyChangeBroker);
		
		testSubmitOrderByBen(order, orderManagementSystem, context);
		
		context = createOrderManagerContext(marketDataSource, propertyChangeBroker);

		// re-load order 
		order = datastore.findOrderBy(order.getKey()).getOrderModel();
		
		assertEquals(OrderState.Submitted, order.getState());
		
		assertTrue(order.getExpirationTriggerActionKey() == null);
		assertTrue(trigger.getAction() == null);
		
		testCancelOrderByBen(order, orderManagementSystem, context);
		
		// check property change notification
		checkStrictStateChangeWithCommentAndSubmissionDate(context, OrderState.Canceled, MarketSystemMessageLanguageKeys.ORDER_CANCELED_BY_USER, null, ChangeOrigin.User);
		
		context = createOrderManagerContext(marketDataSource, propertyChangeBroker);

		assertEquals(null, datastore.findOrderBy(order.getKey()).getExpirationTriggerActionKey());
		assertEquals(null, trigger.getAction());
		
		assertTrue(order.getExpirationTriggerActionKey() == null);

		// re-load order 
		order = datastore.findOrderBy(order.getKey()).getOrderModel();
		
		assertEquals(OrderState.Canceled, order.getState());
		assertEquals(MarketSystemMessageLanguageKeys.ORDER_CANCELED_BY_USER, order.getCancellationCommentLanguageKey());
		
		MarketInternal marketInternal = datastore.findMarketBy(MockDatastores.MARKET_ID);
		
		assertTrue(marketInternal.getBestAskSystem() == null);
		assertTrue(marketInternal.getBestBidSystem() == null);
		
		// test cancel triggered order
		generator = new TestOrderFlowGenerator(QuoteType.Price, marketModel, generateKeys());
		orders = generator.getOrders();
		Order order1 = orders[0];
		setOrderDefaults(order1);
		
		order1.setTriggerInstruction(OrderTriggerInstruction.StopLoss);
		DoubleProperty property = new DoubleProperty();
		property.setName(OrderPropertyNames.STOP_QUOTE_PROPERTY_NAME);
		property.setValue(20.0);
		
		order1.addTriggerProperty(property);
		order1.setExpirationInstruction(OrderExpirationInstruction.DayOrder);

		testCreateOrderByBen(order1, orderManagementSystem, context);
		
		context = createOrderManagerContext(marketDataSource, propertyChangeBroker);
		
		testSubmitOrderByBen(order1, orderManagementSystem, context);
		
			
		context = createOrderManagerContext(marketDataSource, propertyChangeBroker);

		// re-load order 
		order1 = datastore.findOrderBy(order1.getKey()).getOrderModel();
		
		assertEquals(OrderState.WaitingSubmit, order1.getState());
		assertTrue(order1.getQuoteChangeTriggerKey() != null);
		assertTrue(order1.getExpirationTriggerActionKey() != null);
		
		assertTrue(marketDataSource.hasMarketDataChangeListeners());
		assertTrue(trigger.getAction() != null);
		
		Long expirationTriggerAction1 = datastore.findOrderBy(order1.getKey()).getExpirationTriggerActionKey();
		Long key = trigger.getActionKey();
		assertEquals(expirationTriggerAction1, key);
		
		assertTrue(marketInternal.getBestAskSystem() == null);
		assertTrue(marketInternal.getBestBidSystem() == null);
		
		context = createOrderManagerContext(marketDataSource, propertyChangeBroker);

		testCancelOrderByBen(order1, orderManagementSystem, context);
		
			
		context = createOrderManagerContext(marketDataSource, propertyChangeBroker);

		// re-load order 
		order1 = datastore.findOrderBy(order1.getKey()).getOrderModel();
		
		assertEquals(OrderState.Canceled, order1.getState());
		assertTrue(order1.getQuoteChangeTriggerKey() == null);
		assertTrue(order1.getExpirationTriggerActionKey() == null);
		
		assertFalse(marketDataSource.hasMarketDataChangeListeners());
		
		assertEquals(null, trigger.getAction());
		
		// test cancel non-submitted order 
		generator = new TestOrderFlowGenerator(QuoteType.Price, marketModel, generateKeys());
		orders = generator.getOrders();
		Order order2 = orders[0];
		setOrderDefaults(order2);
		
		testCreateOrderByBen(order2, orderManagementSystem, context);
		
		context = createOrderManagerContext(marketDataSource, propertyChangeBroker);

		testCancelOrderByBen(order2, orderManagementSystem, context);
		
		// re-load order 
		order2 = datastore.findOrderBy(order2.getKey()).getOrderModel();
		
		assertEquals(OrderState.Created, order2.getState());
		
		context = createOrderManagerContext(marketDataSource, propertyChangeBroker);
		
		testSubmitOrderByBen(order2, orderManagementSystem, context);
		
		context = createOrderManagerContext(marketDataSource, propertyChangeBroker);

		assertEquals(OrderState.Canceled, datastore.findOrderBy(order.getKey()).getState());
		
		context = createOrderManagerContext(marketDataSource, propertyChangeBroker);
		
		openTransaction();
		orderManagementSystem.cancelOrder(order2.getKey(), SUES_SESSION, context);
		commitTransaction();
		
		// check property change notification
		assertEquals(2, ((MockPropertyChangeSessionImpl)context.getPropertyChangeSession()).getPropertyChanges().length);
		checkStrictStateChangeWithCommentAndSubmissionDate(context, OrderState.Canceled, MarketSystemMessageLanguageKeys.ORDER_CANCELED_BY_USER, null, ChangeOrigin.User);
	
		context = createOrderManagerContext(marketDataSource, propertyChangeBroker);
		
		assertEquals(OrderState.Canceled, datastore.findOrderBy(order2.getKey()).getState());
		assertEquals(MarketSystemMessageLanguageKeys.ORDER_CANCELED_BY_USER, datastore.findOrderBy(order2.getKey()).getCancellationCommentLanguageKey());
		
		// test cancel for pending submit
		openTransaction();		
		order2 = datastore.findOrderBy(order2.getKey()).getOrderModel();
		order2.setState(OrderState.PendingSubmit); 
		commitTransaction();
		
		context = createOrderManagerContext(marketDataSource, propertyChangeBroker);

		testCancelOrderByBen(order2, orderManagementSystem, context);
		
		// re-load order 
		order2 = datastore.findOrderBy(order2.getKey()).getOrderModel();
		
		assertEquals(OrderState.Canceled, order2.getState());
		
		// set order back to created 
		openTransaction();		
		order2 = datastore.findOrderBy(order2.getKey()).getOrderModel();
		order2.setState(OrderState.Created); 
		commitTransaction();
	}

	private void testCancelOrderByBen(Order order,
			OrderManagementSystem orderManagementSystem,
			OrderManagementContextImpl context) {
		openTransaction();
		orderManagementSystem.cancelOrder(order.getKey(), BENS_SESSION, context);
		commitTransaction();
	}

	private void setOrderDefaults(Order order_) {
		order_.setKey(null);
		order_.setSide(OrderSide.Sell);
		order_.setType(OrderType.Limit);
		order_.setLimitQuoteValue(wrapQuote(21));
		order_.setSubmissionDate(null);
		order_.setSubmitterID(null);
		order_.setTargetMarketCode(null);
		order_.setState(OrderState.Created);
		order_.setCreationAudit(null);
	}
	
	public void testSubmitErrors() throws Exception {		
		MockDatastores datastore = createDatastores();
		MockMarketDataSource marketDataSource = new MockMarketDataSource(datastore);		
		MockPropertyChangeBroker propertyChangeBroker = new MockPropertyChangeBroker(datastore);

		MockOrderActionRequestor mockOrderActionExecutor = new MockOrderActionRequestor(datastore, datastore);
		final OrderManagementSystem orderManagementSystem = new OrderManagementSystem(datastore, datastore, datastore, mockOrderActionExecutor, mockOrderActionExecutor);

		Market marketModel = getMarket(datastore);
		
		TestOrderFlowGenerator generator = new TestOrderFlowGenerator(QuoteType.Price, marketModel, generateKeys());
		Order[] orders = generator.getOrders();
		final Order order = orders[0];
		final Order order2 = orders[1];
		setupOrderForTest(order);
		setupOrderForTest(order2);
		
		// Order 2 is market order
		order2.setExecuteEntireOrderAtOnce(true);
		order2.setExpirationInstruction(OrderExpirationInstruction.ImmediateOrCancel);
		order2.setType(OrderType.Market);
		
		setupUserAndMarketForTest(datastore);
		
		OrderManagementContextImpl context = createOrderManagerContext(marketDataSource, propertyChangeBroker);

		testCreateOrderByBen(order, orderManagementSystem, context);
		testCreateOrderByBen(order2, orderManagementSystem, context);
		
		// test user session
		final OrderManagementContextImpl contextFinal = createOrderManagerContext(marketDataSource, propertyChangeBroker);
		
		// test submit non-existent order 
		try {
			orderManagementSystem.submitOrder(null, BENS_SESSION, contextFinal);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.NON_EXISTENT_ORDER, e.getLanguageKey());
		}
		
		try {
			orderManagementSystem.submitOrder(order.getKey().longValue() + 100, BENS_SESSION, contextFinal);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.NON_EXISTENT_ORDER, e.getLanguageKey());
		}	
		
		MarketInternal marketLoaded = datastore.findMarketBy(MockDatastores.MARKET_ID);
		marketLoaded.getMarketModel().setExecutionSystem(ExecutionSystem.CallMarketWithSingleSideAuction);
		marketLoaded.getMarketModel().setState(MarketState.Open);
		
		context = createOrderManagerContext(marketDataSource, propertyChangeBroker);

		try {
			testSubmitOrderByBen(order2, orderManagementSystem, contextFinal);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.MARKET_ORDER_IS_NOT_SUPPORTED_ON_CALL_MARKET, e.getLanguageKey());
		}	
		
	}
	
	public void testCancelErrors() throws Exception {
		
		
		MockDatastores datastore = createDatastores();
		MockMarketDataSource marketDataSource = new MockMarketDataSource(datastore);		
		MockPropertyChangeBroker propertyChangeBroker = new MockPropertyChangeBroker(datastore);

		MockOrderActionRequestor mockOrderActionExecutor = new MockOrderActionRequestor(datastore, datastore);
		final OrderManagementSystem orderManagementSystem = new OrderManagementSystem(datastore, datastore, datastore, mockOrderActionExecutor, mockOrderActionExecutor);

		Market marketModel = getMarket(datastore);
		
		TestOrderFlowGenerator generator = new TestOrderFlowGenerator(QuoteType.Price, marketModel, generateKeys());
		Order[] orders = generator.getOrders();
		final Order order = orders[0];
		setupOrderForTest(order);
		
		setupUserAndMarketForTest(datastore);
		
		OrderManagementContextImpl context = createOrderManagerContext(marketDataSource, propertyChangeBroker);
		
		testCreateOrderByBen(order, orderManagementSystem, context);

		context = createOrderManagerContext(marketDataSource, propertyChangeBroker);
		
		testSubmitOrderByBen(order, orderManagementSystem, context);
		
		// test user session		
		// test cancel non-existent order 
		try {
			orderManagementSystem.cancelOrder(null, BENS_SESSION, context);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.NON_EXISTENT_ORDER, e.getLanguageKey());
		}
		
		try {
			orderManagementSystem.cancelOrder(order.getKey().longValue() + 1, BENS_SESSION, context);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.NON_EXISTENT_ORDER, e.getLanguageKey());
		}
		
		context = createOrderManagerContext(marketDataSource, propertyChangeBroker);
		
		openTransaction();
		datastore.findOrderBy(order.getKey()).getOrderModel().setState(OrderState.Executed);
		commitTransaction();
		
		context = createOrderManagerContext(marketDataSource, propertyChangeBroker);
		
		try {
			testCancelOrderByBen(order, orderManagementSystem, context);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.EXECUTED_ORDER_CANNOT_BE_CANCELED, e.getLanguageKey());
		} finally {
			commitTransaction();
		}
		
	}

	private void setupUserAndMarketForTest(MockDatastores datastore) {
		openTransaction();
		commitTransaction();
		setMarketActivated(datastore);
		setMarket2Activated(datastore);
	}

	private void setupOrderForTest(final Order order) {
		order.setSubmissionDate(null);
		order.setSubmitterID(null);
		order.setTargetMarketCode(null);
		order.setState(OrderState.Created);
		order.setCreationAudit(null);		
		order.setKey(null);
	}
	
	public void testGetOrder() throws Exception {
		// initialize test data 
		
		
		MockDatastores datastore = createDatastores();
		MockMarketDataSource marketDataSource = new MockMarketDataSource(datastore);	
		MockPropertyChangeBroker propertyChangeBroker = new MockPropertyChangeBroker(datastore);

		MockOrderActionRequestor mockOrderActionExecutor = new MockOrderActionRequestor(datastore, datastore);
		final OrderManagementSystem orderManagementSystem = new OrderManagementSystem(datastore, datastore, datastore, mockOrderActionExecutor, mockOrderActionExecutor);
		
		Market marketModel = getMarket(datastore);
		
		TestOrderFlowGenerator generator = new TestOrderFlowGenerator(QuoteType.Price, marketModel, generateKeys());
		Order[] orders = generator.getOrders();
		final Order order = orders[0];
		setupOrderForTest(order);
		
		setupUserAndMarketForTest(datastore);
		
		OrderManagementContextImpl context = createOrderManagerContext(marketDataSource, propertyChangeBroker);
		 
		testCreateOrderByBen(order, orderManagementSystem, context);

		context = createOrderManagerContext(marketDataSource, propertyChangeBroker);
		// end of initialize test data 
		
		// test cancel non-existent order 
		try {
			orderManagementSystem.getOrder(null);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.NON_EXISTENT_ORDER, e.getLanguageKey());
		}
		
		try {
			orderManagementSystem.getOrder(order.getKey().longValue() + 1);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.NON_EXISTENT_ORDER, e.getLanguageKey());
		}
		
		// get order
		OrderInternal orderLoaded = orderManagementSystem.getOrder(order.getKey());
		
		// check order
		assertTrue(orderLoaded != null);
		assertEquals(order.getKey(), orderLoaded.getOrderModel().getKey());
		assertEquals(order.getSize(), orderLoaded.getSize());
		
		orderLoaded = orderManagementSystem.getOrder(order.getKey());
		
		// check order
		assertTrue(orderLoaded != null);
		assertEquals(order.getKey(), orderLoaded.getOrderModel().getKey());
		assertEquals(order.getSize(), orderLoaded.getSize());
	}
	
	public void testChangeProperty() throws Exception {
		
		
		MockDatastores datastore = createDatastores();
		MockMarketDataSource marketDataSource = new MockMarketDataSource(datastore);		
		MockPropertyChangeBroker propertyChangeBroker = new MockPropertyChangeBroker(datastore);

		MockOrderActionRequestor mockOrderActionExecutor = new MockOrderActionRequestor(datastore, datastore);
		final OrderManagementSystem orderManagementSystem = new OrderManagementSystem(datastore, datastore, datastore, mockOrderActionExecutor, mockOrderActionExecutor);
		
		TestOrderFlowGenerator generator = new TestOrderFlowGenerator(QuoteType.Price, datastore.findMarketBy(MockDatastores.MARKET_ID).getMarketModel(), generateKeys());
		Order[] orders = generator.getOrders();
		Order order = orders[0];
		
		setupOrderForTest(order);

		setupUserAndMarketForTest(datastore);
		
		OrderManagementContextImpl context = createOrderManagerContext(marketDataSource, propertyChangeBroker);

		context = createOrderManagerContext(marketDataSource, propertyChangeBroker);
		
		testCreateOrderByBen(order, orderManagementSystem, context);

		context = createOrderManagerContext(marketDataSource, propertyChangeBroker);
		
		testSubmitOrderByBen(order, orderManagementSystem, context);

		context = createOrderManagerContext(marketDataSource, propertyChangeBroker);
		 
		// test change non-existent order
		try {
			orderManagementSystem.changeOrderProperties(null, propertyChangesToDto(new PropertyChange[] { getPropertyChange(getIntProperty(OrderPropertyNames.SIZE_PROPERTY, 100)) }), BENS_SESSION, context);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.NON_EXISTENT_ORDER, e.getLanguageKey());
		}

		try {
			orderManagementSystem.changeOrderProperties(order.getKey().longValue() + 1, propertyChangesToDto(new PropertyChange[] { getPropertyChange(getIntProperty(OrderPropertyNames.SIZE_PROPERTY, 100)) }), BENS_SESSION, context);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.NON_EXISTENT_ORDER, e.getLanguageKey());
		}
		
		context = createOrderManagerContext(marketDataSource, propertyChangeBroker);
		 
		// test invalid property
		try {
			orderManagementSystem.changeOrderProperties(order.getKey(), null, BENS_SESSION, context);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.UNSUPPORTED_ORDER_PROPERTY_CHANGE, e.getLanguageKey());
			// expected
		}
		
		context = createOrderManagerContext(marketDataSource, propertyChangeBroker);
		 		
		try {
			orderManagementSystem.changeOrderProperties(order.getKey(), propertyChangesToDto(new PropertyChange[] { null }), BENS_SESSION, context);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.UNSUPPORTED_ORDER_PROPERTY_CHANGE, e.getLanguageKey());
			// expected
		}	
		
		context = createOrderManagerContext(marketDataSource, propertyChangeBroker);
		
		try {
			orderManagementSystem.changeOrderProperties(order.getKey(), propertyChangesToDto(new PropertyChange[] { new PropertyValueChange() }), BENS_SESSION, context);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.UNSUPPORTED_ORDER_PROPERTY_VALUE, e.getLanguageKey());
			// expected
		}
		
		context = createOrderManagerContext(marketDataSource, propertyChangeBroker);
		 
		try {
			orderManagementSystem.changeOrderProperties(order.getKey(), propertyChangesToDto(new PropertyChange[] { new PropertyListValueChange() }), BENS_SESSION, context);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.UNSUPPORTED_ORDER_PROPERTY_VALUE, e.getLanguageKey());
			// expected
		}	
		
		context = createOrderManagerContext(marketDataSource, propertyChangeBroker);
		 
		try {
			orderManagementSystem.changeOrderProperties(order.getKey(), propertyChangesToDto(new PropertyChange[] { getPropertyChange(getIntProperty(OrderPropertyNames.KEY_PROPERTY, 100)) }), BENS_SESSION, context);
			fail();
		} catch (MarketSecurityException e) {
			assertEquals(ExceptionLanguageKeys.USER_TRIED_TO_SET_A_SYSTEM_CONTROLLED_PROPERTY, e.getLanguageKey());
			// expected
		}		
		
		context = createOrderManagerContext(marketDataSource, propertyChangeBroker);
		 
		// re-load order 
		order = datastore.findOrderBy(order.getKey()).getOrderModel();
		
		assertEquals(OrderState.Canceled, order.getState());		
		assertEquals(MarketSystemMessageLanguageKeys.ORDER_CANCELED_BY_USER, order.getCancellationCommentLanguageKey());
		
		// test change order
		openTransaction();
		orderManagementSystem.changeOrderProperties(order.getKey(), propertyChangesToDto(new PropertyChange[] { getPropertyChange(getIntProperty(OrderPropertyNames.SIZE_PROPERTY, 100)) }), BENS_SESSION, context);
		commitTransaction();
		
		// check property change notification
		checkSingleIntProperty(context, OrderPropertyNames.SIZE_PROPERTY, 100);
		
		((MockPropertyChangeSessionImpl)context.getPropertyChangeSession()).rollback();
		
		context = createOrderManagerContext(marketDataSource, propertyChangeBroker);
		 
		// re-load order 
		order = datastore.findOrderBy(order.getKey()).getOrderModel();
		
		assertEquals(OrderState.Canceled, order.getState());
		assertEquals(100, order.getSize());
		
		// test change just created order
		order.setState(OrderState.Created);
		
		context = createOrderManagerContext(marketDataSource, propertyChangeBroker);
		 
		// re-load order 
		order = datastore.findOrderBy(order.getKey()).getOrderModel();
		
		openTransaction();
		orderManagementSystem.changeOrderProperties(order.getKey(), propertyChangesToDto(new PropertyChange[] { getPropertyChange(getIntProperty(OrderPropertyNames.SIZE_PROPERTY, 100)) }), BENS_SESSION, context);
		commitTransaction();
		
		((MockPropertyChangeSessionImpl)context.getPropertyChangeSession()).rollback();
		
		// commit changes in order
		context = createOrderManagerContext(marketDataSource, propertyChangeBroker);

		// re-load order 
		order = datastore.findOrderBy(order.getKey()).getOrderModel();
		
		assertEquals(OrderState.Created, order.getState());
		
		// check quote rounding on property change 
		openTransaction();
		datastore.findMarketBy(order.getTargetMarketCode()).getMarketModel().setMinimumQuoteIncrement(0.05);
		commitTransaction();
		
		context = createOrderManagerContext(marketDataSource, propertyChangeBroker);
		
		openTransaction();
		orderManagementSystem.changeOrderProperties(order.getKey(),
				propertyChangesToDto(
					new PropertyChange[] { 
						getPropertyChange(
								getDoubleProperty(
										OrderPropertyNames.LIMIT_QUOTE_VALUE_PROPERTY, 
										10.19956)) }), BENS_SESSION, context);
		commitTransaction();
		
		// check property change notification
		MockPropertyChangeSessionImpl propertyChangeSession = ((MockPropertyChangeSessionImpl)context.getPropertyChangeSession());
		PropertyChanges[] propertyChanges = propertyChangeSession.getPropertyChanges();
		assertEquals(2, propertyChanges.length);
		assertEquals(1, propertyChanges[0].getChanges().length);
		assertEquals(1, propertyChanges[1].getChanges().length);
		assertTrue(propertyChanges[0].getChanges()[0] instanceof PropertyValueChangeDto);
		assertTrue(propertyChanges[1].getChanges()[0] instanceof PropertyValueChangeDto);
		PropertyValueChangeDto propertyValueChange1 = (PropertyValueChangeDto) propertyChanges[0].getChanges()[0];
		PropertyValueChangeDto propertyValueChange2 = (PropertyValueChangeDto) propertyChanges[1].getChanges()[0];
		
		checkDoubleProperty(OrderPropertyNames.LIMIT_QUOTE_VALUE_PROPERTY, 10.19956, propertyValueChange1);
		checkDoubleProperty(OrderPropertyNames.LIMIT_QUOTE_VALUE_PROPERTY, 10.20, propertyValueChange2);
		
		((MockPropertyChangeSessionImpl)context.getPropertyChangeSession()).rollback();
		
		context = createOrderManagerContext(marketDataSource, propertyChangeBroker);
		
		// re-load order 
		order = datastore.findOrderBy(order.getKey()).getOrderModel();
		
		assertEquals(10.20, order.getLimitQuoteValue().getQuoteValue(), 0.000001);
		
		// check quote not being rounding on property change 
		openTransaction();
		orderManagementSystem.changeOrderProperties(order.getKey(), 
				propertyChangesToDto(
					new PropertyChange[] { 
						getPropertyChange(
								getDoubleProperty(
										OrderPropertyNames.LIMIT_QUOTE_VALUE_PROPERTY, 
										10.15)) }), BENS_SESSION, context);
		
		commitTransaction();
		
		// re-load order 
		order = datastore.findOrderBy(order.getKey()).getOrderModel();
		
		assertEquals(10.15, order.getLimitQuoteValue().getQuoteValue(), 0.000001);
		
		// check property change notification
		checkSingleDoubleProperty(context, OrderPropertyNames.LIMIT_QUOTE_VALUE_PROPERTY, 10.15);
	}


	private void testCancelAllOrdersForBenBySelf(
			OrderManagementSystem orderManagementSystem,
			OrderManagementContextImpl context) {
		openTransaction();
		orderManagementSystem.cancelAllOrdersForUser(BENS_SESSION, context);
		commitTransaction();
	}

	private void testCancelAllOrdersForSueBySelf(
			OrderManagementSystem orderManagementSystem,
			OrderManagementContextImpl context) {
		openTransaction();
		orderManagementSystem.cancelAllOrdersForUser(SUES_SESSION, context);
		commitTransaction();
	}
	
	private PropertyChangeDto[] propertyChangesToDto(PropertyChange[] change_) {
		return PropertyDtoMapping.INSTANCE.toDto(change_);
	}
}
