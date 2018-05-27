package com.imarcats.market.engine.market;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.imarcats.infrastructure.server.trigger.MockTimeTrigger;
import com.imarcats.interfaces.client.v100.dto.MatchedTradeDto;
import com.imarcats.interfaces.client.v100.dto.types.IntPropertyDto;
import com.imarcats.interfaces.client.v100.dto.types.MarketDataType;
import com.imarcats.interfaces.client.v100.dto.types.ObjectPropertyDto;
import com.imarcats.interfaces.client.v100.dto.types.PropertyValueChangeDto;
import com.imarcats.interfaces.client.v100.exception.ExceptionLanguageKeys;
import com.imarcats.interfaces.client.v100.exception.MarketRuntimeException;
import com.imarcats.interfaces.client.v100.i18n.MarketSystemMessageLanguageKeys;
import com.imarcats.interfaces.client.v100.notification.ChangeOrigin;
import com.imarcats.interfaces.client.v100.notification.MarketDataChange;
import com.imarcats.interfaces.client.v100.notification.PropertyChanges;
import com.imarcats.interfaces.server.v100.dto.mapping.MatchedTradeDtoMapping;
import com.imarcats.internal.server.infrastructure.notification.ListenerCallContext;
import com.imarcats.internal.server.infrastructure.notification.ListenerCallParameters;
import com.imarcats.internal.server.infrastructure.timer.MarketTimer;
import com.imarcats.internal.server.infrastructure.timer.TimerAction;
import com.imarcats.internal.server.interfaces.market.MarketInternal;
import com.imarcats.internal.server.interfaces.order.OrderInternal;
import com.imarcats.internal.server.interfaces.order.OrderManagementContext;
import com.imarcats.market.engine.testutils.MockDatastores;
import com.imarcats.market.engine.testutils.MockMarketDataSessionImpl;
import com.imarcats.market.engine.testutils.MockPropertyChangeSessionImpl;
import com.imarcats.market.engine.testutils.MockTradeNotificationSessionImpl;
import com.imarcats.market.engine.testutils.OrderCompareTestCaseBase;
import com.imarcats.market.i18n.client.language.MarketExceptionLanguageConstants;
import com.imarcats.model.CircuitBreaker;
import com.imarcats.model.HaltRule;
import com.imarcats.model.Market;
import com.imarcats.model.MarketPropertyNames;
import com.imarcats.model.Order;
import com.imarcats.model.OrderPropertyNames;
import com.imarcats.model.test.testutils.TestOrderFlowGenerator;
import com.imarcats.model.types.ActivationStatus;
import com.imarcats.model.types.BusinessCalendar;
import com.imarcats.model.types.ExecutionSystem;
import com.imarcats.model.types.MarketState;
import com.imarcats.model.types.OrderExpirationInstruction;
import com.imarcats.model.types.OrderSide;
import com.imarcats.model.types.OrderState;
import com.imarcats.model.types.OrderTriggerInstruction;
import com.imarcats.model.types.OrderType;
import com.imarcats.model.types.Quote;
import com.imarcats.model.types.QuoteAndSize;
import com.imarcats.model.types.QuoteType;
import com.imarcats.model.types.RecurringActionDetail;
import com.imarcats.model.types.TimeOfDay;
import com.imarcats.model.utils.PropertyUtils;

public class MarketImplTest extends OrderCompareTestCaseBase {
	
	public void testMatketMaintenance() throws Exception {
		
		MockDatastores datastore = new MockDatastores();
		Market market = getMarket(datastore);
		
		OrderManagementContext orderManagementContext = createOrderManagerContext();
		
		market.setQuoteType(QuoteType.Price);
		market.setExecutionSystem(ExecutionSystem.CallMarketWithSingleSideAuction);
		market.setActivationStatus(ActivationStatus.Activated);
		
		TestOrderFlowGenerator gen = new TestOrderFlowGenerator(QuoteType.Price, market);
		Order[] orders = gen.getOrders();
		orders[0].setState(OrderState.Created);
		orders[1].setState(OrderState.Created);
		orders[2].setState(OrderState.Created);
		orders[3].setState(OrderState.Created);
		
		datastore.createOrder(orders[0]);
		datastore.createOrder(orders[1]);
		datastore.createOrder(orders[2]);
		datastore.createOrder(orders[3]);
		
		MarketImpl marketImpl = new MarketImpl(market, null, datastore, datastore, datastore, datastore);

		marketImpl.submit(wrapOrder(orders[0], datastore), orderManagementContext);
		marketImpl.submit(wrapOrder(orders[1], datastore), orderManagementContext);
		
		// call market 		
		market.setQuoteType(QuoteType.Price);
		market.setExecutionSystem(ExecutionSystem.CallMarketWithSingleSideAuction);
		market.setActivationStatus(ActivationStatus.Activated);
		orderManagementContext = createOrderManagerContext();
		
		marketImpl.executeCallMarketMaintenance(orderManagementContext);
		
		assertEquals(OrderState.Canceled, orders[0].getState());
		assertEquals(OrderState.Canceled, orders[1].getState());
		assertEquals(OrderState.Created, orders[2].getState());
		assertEquals(OrderState.Created, orders[3].getState());
		
		// check property changes 
		assertEquals(4, ((MockPropertyChangeSessionImpl) ((MockPropertyChangeSessionImpl) orderManagementContext.getPropertyChangeSession())).getPropertyChanges().length);
		// check the first 3 properties only
		checkStateChangeWithCommentAndSubmissionDate(orderManagementContext, OrderState.Canceled, MarketSystemMessageLanguageKeys.ORDER_CANCELED_AFTER_MARKET_CALL, null, ChangeOrigin.System);

		
		// regular market
		market.setExecutionSystem(ExecutionSystem.ContinuousTwoSidedAuction);
		
		marketImpl.submit(wrapOrder(orders[2], datastore), orderManagementContext);
		
		orderManagementContext = createOrderManagerContext();
		
		marketImpl.executeRegularMarketMaintenance(orderManagementContext);
		
		assertEquals(OrderState.Submitted, orders[2].getState());
		
		assertEquals(null, datastore.findOrderBy(orders[0].getKey())); 
		assertEquals(null, datastore.findOrderBy(orders[1].getKey())); 
		assertEquals(null, datastore.findOrderBy(orders[3].getKey())); 
		
		// check property changes 
		assertEquals(3, ((MockPropertyChangeSessionImpl) ((MockPropertyChangeSessionImpl) orderManagementContext.getPropertyChangeSession())).getPropertyChanges().length);
		// check the first 2 properties only
		checkStateChange(orderManagementContext, OrderState.Deleted, ChangeOrigin.System);
		
		// TODO: Check Market Data Impact of Market Maintenance
	}
	
	public void testCheckOrderErrors() throws Exception {
		checkOrderErrorsTest(QuoteType.Price);
		checkOrderErrorsTest(QuoteType.Yield);
	}
	
	private void checkOrderErrorsTest(QuoteType quoteType_) throws InterruptedException {
		
		MockDatastores datastore = new MockDatastores();
		Market market = getMarket(datastore);
		
		OrderManagementContext orderManagementContext = createOrderManagerContext();
		
		market.setQuoteType(quoteType_);
		market.setExecutionSystem(ExecutionSystem.ContinuousTwoSidedAuction);
		
		TestOrderFlowGenerator gen = new TestOrderFlowGenerator(quoteType_, market);
		Order[] orders = gen.getOrders();
		
		// create order to place it to the data source
		for (Order order : orders) {
			datastore.createOrder(order);
		}
		
		MarketInternal marketImpl = new MarketImpl(market, null, datastore, datastore, datastore, datastore);
		
		// Submit Tests
		assertEquals(OrderState.Created, orders[0].getState());
		try {
			marketImpl.submit(wrapOrder(orders[0], datastore), orderManagementContext);
			fail();
		} catch (MarketRuntimeException e) {
			// expected
			assertEquals(ExceptionLanguageKeys.ORDER_CANNOT_BE_SUBMITTED_TO_NON_ACTIVE_MARKET, e.getLanguageKey());
		}
		assertEquals(OrderState.Created, orders[0].getState());
		
		market.setActivationStatus(ActivationStatus.Activated);
		assertEquals(OrderState.Created, orders[0].getState());
		try {
			marketImpl.submit(wrapOrder(orders[0], datastore), orderManagementContext);
		} catch (Exception e) {
			fail();
		}
		assertEquals(OrderState.Submitted, orders[0].getState());
		
		try {
			marketImpl.submit(wrapOrder(orders[0], datastore), orderManagementContext);
			fail();
		} catch (MarketRuntimeException e) {
			// expected
			assertEquals(ExceptionLanguageKeys.ORDER_ALREADY_SUBMITTED, e.getLanguageKey());
		}
		assertEquals(OrderState.Submitted, orders[0].getState());
		
		orders[0].setState(OrderState.Executed);
		try {
			marketImpl.submit(wrapOrder(orders[0], datastore), orderManagementContext);
			fail();
		} catch (MarketRuntimeException e) {
			// expected
			assertEquals(ExceptionLanguageKeys.EXECUTED_ORDER_CANNOT_BE_RESUBMITTED, e.getLanguageKey());
		}
		assertEquals(OrderState.Executed, orders[0].getState());
		
		orders[0].setState(OrderState.Submitted);
		
		market.setMinimumContractsTraded(10);
		market.setMaximumContractsTraded(100);
		
		try {
			marketImpl.submit(wrapOrder(orders[1], datastore), orderManagementContext);
			fail();
		} catch (MarketRuntimeException e) {
			// expected
			assertEquals(ExceptionLanguageKeys.ORDER_IS_TOO_SMALL_FOR_THE_MARKET, e.getLanguageKey());
		}
		assertEquals(OrderState.Created, orders[1].getState());
		
		orders[1].setSize(10);
		try {
			marketImpl.submit(wrapOrder(orders[1], datastore), orderManagementContext);
		} catch (Exception e) {
			fail();
		}
		assertEquals(OrderState.Submitted, orders[1].getState());
		
		orders[2].setSize(100);
		try {
			marketImpl.submit(wrapOrder(orders[2], datastore), orderManagementContext);
		} catch (Exception e) {
			fail();
		}
		assertEquals(OrderState.Submitted, orders[2].getState());
		
		orders[3].setSize(101);
		try {
			marketImpl.submit(wrapOrder(orders[3], datastore), orderManagementContext);
		} catch (MarketRuntimeException e) {
			// expected
			assertEquals(ExceptionLanguageKeys.ORDER_IS_TOO_BIG_FOR_THE_MARKET, e.getLanguageKey());
		}
		assertEquals(OrderState.Created, orders[3].getState());

		orders[3].setSize(10);
		orders[3].setMinimumSizeOfExecution(15);
		market.setState(MarketState.Open);
		try {
			marketImpl.submit(wrapOrder(orders[3], datastore), orderManagementContext);
		} catch (MarketRuntimeException e) {
			// expected
			assertEquals(ExceptionLanguageKeys.ORDER_SIZE_IS_SMALLER_THAN_MINIMUM_SIZE_OF_EXECUTION, e.getLanguageKey());
		}
		assertEquals(OrderState.Created, orders[3].getState());
		market.setState(MarketState.Closed);
		
		market.setMinimumQuoteIncrement(0.5);
		orders[4].setSize(100);
		orders[4].setLimitQuoteValue(wrapQuote(20.55));
		try {
			marketImpl.submit(wrapOrder(orders[4], datastore), orderManagementContext);
		} catch (MarketRuntimeException e) {
			// expected
			assertEquals(ExceptionLanguageKeys.ORDER_MUST_BE_SUBMITTED_WITH_A_VALID_MARKET_QUOTE, e.getLanguageKey());
		}
		assertEquals(OrderState.Created, orders[4].getState());
		
		// Cancel Test
		
		assertEquals(OrderState.Submitted, orders[0].getState());
		try {
			marketImpl.cancel(wrapOrder(orders[0], datastore), MarketSystemMessageLanguageKeys.ORDER_CANCELED_BY_USER, orderManagementContext);
		} catch (Exception e) {
			fail();
		}
		assertEquals(OrderState.Canceled, orders[0].getState());
		
		orders[0].setState(OrderState.Executed);
		try {
			marketImpl.cancel(wrapOrder(orders[0], datastore), MarketSystemMessageLanguageKeys.ORDER_CANCELED_BY_USER, orderManagementContext);
			fail();
		} catch (MarketRuntimeException e) {
			// expected
			assertEquals(ExceptionLanguageKeys.EXECUTED_ORDER_CANNOT_BE_CANCELED, e.getLanguageKey());
		}
		assertEquals(OrderState.Executed, orders[0].getState());
		
		orders[4].setType(OrderType.Market);
		market.setState(MarketState.Closed);
		try {
			marketImpl.submit(wrapOrder(orders[4], datastore), orderManagementContext);
		} catch (MarketRuntimeException e) {
			// expected
			assertEquals(ExceptionLanguageKeys.MARKET_ORDER_IS_NOT_SUPPORTED_WHEN_THE_MARKET_IS_NOT_OPEN, e.getLanguageKey());
		}
		assertEquals(OrderState.Created, orders[4].getState());
		
		market.setState(MarketState.Open);
		market.setExecutionSystem(ExecutionSystem.CallMarketWithSingleSideAuction);
		try {
			marketImpl.submit(wrapOrder(orders[4], datastore), orderManagementContext);
		} catch (MarketRuntimeException e) {
			// expected
			assertEquals(ExceptionLanguageKeys.MARKET_ORDER_IS_NOT_SUPPORTED_ON_CALL_MARKET, e.getLanguageKey());
		}
		assertEquals(OrderState.Created, orders[4].getState());
	}
	
	public void testMarketData() throws Exception {
		QuoteType quoteType = QuoteType.Price;
		
		
		
		MockDatastores datastore = new MockDatastores();
		Market marketModel = createMarket(quoteType, datastore);
		
		OrderManagementContext orderManagementContext = createOrderManagerContext();
		
		MarketImpl market = new MarketImpl(marketModel, null, datastore, datastore, datastore, datastore);
				
		Order[] orders = createAndSubmitOrders(quoteType, datastore, marketModel,
				orderManagementContext, market); 
	
		// check market data
		MockMarketDataSessionImpl marketDataSession = (MockMarketDataSessionImpl) orderManagementContext.getMarketDataSession();
		MarketDataChange[] marketDataChanges = marketDataSession.getMarketDataChanges();
		
		// check last trades
		MarketDataChange[] lastTradesMarketData = filterMarketData(marketDataChanges, MarketDataType.Last);
		
		assertEquals(6, lastTradesMarketData.length);
		
		checkMarketDataChange(quoteType, market.getMarketCode(), lastTradesMarketData[0], MarketDataType.Last, 20.0, 1, null);
		checkMarketDataChange(quoteType, market.getMarketCode(), lastTradesMarketData[1], MarketDataType.Last, 20.1, 2, null);
		checkMarketDataChange(quoteType, market.getMarketCode(), lastTradesMarketData[2], MarketDataType.Last, 20.2, 2, null);
		checkMarketDataChange(quoteType, market.getMarketCode(), lastTradesMarketData[3], MarketDataType.Last, 20.1, 2, null);
		checkMarketDataChange(quoteType, market.getMarketCode(), lastTradesMarketData[4], MarketDataType.Last, 20.0, 2, null);
		checkMarketDataChange(quoteType, market.getMarketCode(), lastTradesMarketData[5], MarketDataType.Last, 20.0, 2, null);

		// check bid
		MarketDataChange[] bidTradesMarketData = filterMarketData(marketDataChanges, MarketDataType.Bid);
		
		assertEquals(6, bidTradesMarketData.length);
		
		checkMarketDataChange(quoteType, market.getMarketCode(), bidTradesMarketData[0], MarketDataType.Bid, 20.0, 3, false);
		checkMarketDataChange(quoteType, market.getMarketCode(), bidTradesMarketData[1], MarketDataType.Bid, 20.0, 5, false);
		checkMarketDataChange(quoteType, market.getMarketCode(), bidTradesMarketData[2], MarketDataType.Bid, 20.0, 4, false);
		checkMarketDataChange(quoteType, market.getMarketCode(), bidTradesMarketData[3], MarketDataType.Bid, 20.1, 2, false);
		
		assertEquals(false, bidTradesMarketData[4].getNewQuoteValid());
		assertEquals(0.0, bidTradesMarketData[4].getNewQuoteValue());
		assertEquals(0, bidTradesMarketData[4].getNewQuoteSize());
		
		checkMarketDataChange(quoteType, market.getMarketCode(), bidTradesMarketData[5], MarketDataType.Bid, 19.8, 7, false);
			
		// check previous bid
		assertEquals(null, market.getMarketModel().getPreviousBestBid());
		
		// check ask
		MarketDataChange[] askTradesMarketData = filterMarketData(marketDataChanges, MarketDataType.Ask);
		
		assertEquals(2, askTradesMarketData.length);
		
		checkMarketDataChange(quoteType, market.getMarketCode(), askTradesMarketData[0], MarketDataType.Ask, 20.1, 2, false);
		checkMarketDataChange(quoteType, market.getMarketCode(), askTradesMarketData[1], MarketDataType.Ask, 20.2, 3, false);
		
		// check previous ask
		assertEquals(20.1, market.getMarketModel().getPreviousBestAsk().getQuote().getQuoteValue());
		assertEquals(true, market.getMarketModel().getPreviousBestAsk().getQuote().getValidQuote());
		assertEquals(2, market.getMarketModel().getPreviousBestAsk().getSize());
		
		// test cancel 
		orderManagementContext = createOrderManagerContext();
		market.cancel(wrapOrder(orders[4], datastore), MarketSystemMessageLanguageKeys.ORDER_CANCELED_BY_USER, orderManagementContext);
		
		marketDataSession = (MockMarketDataSessionImpl) orderManagementContext.getMarketDataSession();
		marketDataChanges = marketDataSession.getMarketDataChanges();
		
		// check bid
		bidTradesMarketData = filterMarketData(marketDataChanges, MarketDataType.Bid);
		
		assertEquals(0, bidTradesMarketData.length);

		// check ask
		askTradesMarketData = filterMarketData(marketDataChanges, MarketDataType.Ask);
		
		assertEquals(1, askTradesMarketData.length);
		
		assertEquals(false, askTradesMarketData[0].getNewQuoteValid()); // invalid quote for empty book 
		
		// create and submit the first order - to move the bid
		TestOrderFlowGenerator gen = new TestOrderFlowGenerator(quoteType, marketModel);
		orders = gen.getOrders();
		
		datastore.createOrder(orders[0]);
		market.submit(datastore.findOrderBy(orders[0].getKey()), orderManagementContext);
		
		// check previous bid
		assertEquals(19.8, market.getMarketModel().getPreviousBestBid().getQuote().getQuoteValue());
		assertEquals(true, market.getMarketModel().getPreviousBestBid().getQuote().getValidQuote());
		assertEquals(7, market.getMarketModel().getPreviousBestBid().getSize());
		
		// submit an sell order to move the ask
		datastore.createOrder(orders[1]);
		market.submit(datastore.findOrderBy(orders[1].getKey()), orderManagementContext);
		
		// check previous ask
		assertEquals(null, market.getMarketModel().getPreviousBestAsk());
	}

	public void testShouldNotifyAboutMarketDataChange() throws Exception {
		assertEquals(false, MarketImpl.shouldNotifiy((Quote)null));
		assertEquals(false, MarketImpl.shouldNotifiy((QuoteAndSize)null));
		
		Quote invalidQuote = new Quote();
		Quote validQuote = Quote.createQuote(10);
		
		QuoteAndSize invalidQuoteAndSize = new QuoteAndSize();
		QuoteAndSize validQuoteAndSize = new QuoteAndSize();
		validQuoteAndSize.setQuote(Quote.createQuote(10));
		validQuoteAndSize.setSize(10);
		
		QuoteAndSize validQuoteAndSize2 = new QuoteAndSize();
		validQuoteAndSize2.setQuote(Quote.createQuote(11));
		validQuoteAndSize2.setSize(10);
		
		QuoteAndSize validQuoteAndSize3 = new QuoteAndSize();
		validQuoteAndSize3.setQuote(Quote.createQuote(10));
		validQuoteAndSize3.setSize(11);
		
		assertEquals(false, MarketImpl.shouldNotifiy(invalidQuote));
		assertEquals(false, MarketImpl.shouldNotifiy(invalidQuoteAndSize));
		
		assertEquals(true, MarketImpl.shouldNotifiy(validQuote));
		assertEquals(true, MarketImpl.shouldNotifiy(validQuoteAndSize));

		assertEquals(false, MarketImpl.shouldNotifiy(null, null));
		assertEquals(false, MarketImpl.shouldNotifiy(invalidQuoteAndSize, invalidQuoteAndSize));
		assertEquals(false, MarketImpl.shouldNotifiy(validQuoteAndSize, validQuoteAndSize));
		
		assertEquals(true, MarketImpl.shouldNotifiy(null, invalidQuoteAndSize));
		assertEquals(true, MarketImpl.shouldNotifiy(null, validQuoteAndSize));
		assertEquals(true, MarketImpl.shouldNotifiy(invalidQuoteAndSize, null));
		assertEquals(true, MarketImpl.shouldNotifiy(validQuoteAndSize, null));
		assertEquals(true, MarketImpl.shouldNotifiy(invalidQuoteAndSize, validQuoteAndSize));
		assertEquals(true, MarketImpl.shouldNotifiy(validQuoteAndSize, validQuoteAndSize2));
		assertEquals(true, MarketImpl.shouldNotifiy(validQuoteAndSize, validQuoteAndSize3));
	}
	
	private Order[] createAndSubmitOrders(QuoteType quoteType,
			MockDatastores datastore,
			Market marketModel, OrderManagementContext orderManagementContext,
			MarketImpl market) throws InterruptedException {
		TestOrderFlowGenerator gen = new TestOrderFlowGenerator(quoteType, marketModel);
		Order[] orders = gen.getOrders();

		// create order to place it to the data source
		for (Order order : orders) {
			if(order.getType() == OrderType.Market) {
				order.setExpirationInstruction(OrderExpirationInstruction.ImmediateOrCancel);
			}
			datastore.createOrder(order);
		}
		
		// submit all orders
		for (Order order : orders) {
			market.submit(datastore.findOrderBy(order.getKey()), orderManagementContext);
		}
		
		return orders;
	}

	private Market createMarket(QuoteType quoteType,
			MockDatastores datastore) {
		Market marketModel = getMarket(datastore);
		marketModel.setState(MarketState.Open);
		marketModel.setQuoteType(quoteType);
		marketModel.setActivationStatus(ActivationStatus.Activated);
		return marketModel;
	}
	
	public void testPropertyChangeOnExecution() throws Exception {
		QuoteType quoteType = QuoteType.Price;
		
		
		
		MockDatastores datastore = new MockDatastores();
		Market marketModel = createMarket(quoteType, datastore);
		
		OrderManagementContext orderManagementContext = createOrderManagerContext();
		
		MarketImpl market = new MarketImpl(marketModel, null, datastore, datastore, datastore, datastore);
				
		// generate test orders
		TestOrderFlowGenerator gen = new TestOrderFlowGenerator(quoteType, marketModel);
		Order[] orders = gen.getOrders();

		// create order to place it to the data source
		datastore.createOrder(orders[0]);
		
		// submit order
		market.submit(datastore.findOrderBy(orders[0].getKey()), orderManagementContext);
		
		OrderInternal orderInternal = datastore.findOrderBy(orders[0].getKey());
		
		orderManagementContext = createOrderManagerContext();
		
		// record execution on order
		orderInternal.recordExecution(market, orderInternal.getSize() - 1, orderManagementContext);
		
		// check resulted property change 
		checkSingleIntProperty(orderManagementContext,
				OrderPropertyNames.EXECUTED_SIZE_PROPERTY, orderInternal.getSize() - 1);
	
		orderManagementContext = createOrderManagerContext();
		
		// record execution with minimum execution size reset
		orderInternal.getOrderModel().setExecutedSize(0); // clear execution
		orderInternal.getOrderModel().setMinimumSizeOfExecution(1);
		
		orderInternal.recordExecution(market, orderInternal.getSize() - 1, orderManagementContext);
		
		assertEquals(2, ((MockPropertyChangeSessionImpl) orderManagementContext.getPropertyChangeSession()).getPropertyChanges().length);
		
		// check resulted property changes 
		PropertyValueChangeDto propertyChange1 = getPropertyChange(0, 0, orderManagementContext);
		PropertyValueChangeDto propertyChange2 = getPropertyChange(1, 0, orderManagementContext);
		
		assertEquals(OrderPropertyNames.EXECUTED_SIZE_PROPERTY, propertyChange1.getProperty().getName());
		assertEquals(orderInternal.getExecutedSize(), ((IntPropertyDto) propertyChange1.getProperty()).getValue());
		
		assertEquals(OrderPropertyNames.MINIMUM_SIZE_OF_EXECUTION_PROPERTY, propertyChange2.getProperty().getName());
		assertEquals(0, ((IntPropertyDto) propertyChange2.getProperty()).getValue());
		
		// check minimum execution size reset
		assertEquals(0, orderInternal.getMinimumSizeOfExecution());
		
		orderManagementContext = createOrderManagerContext();
		
		// record execution on order
		orderInternal.recordExecution(market, 1, orderManagementContext);
		
		assertEquals(2, ((MockPropertyChangeSessionImpl) orderManagementContext.getPropertyChangeSession()).getPropertyChanges().length);
		
		// check resulted property changes 
		propertyChange1 = getPropertyChange(0, 0, orderManagementContext);
		propertyChange2 = getPropertyChange(1, 0, orderManagementContext);
		
		assertEquals(OrderPropertyNames.EXECUTED_SIZE_PROPERTY, propertyChange1.getProperty().getName());
		assertEquals(orderInternal.getSize(), ((IntPropertyDto) propertyChange1.getProperty()).getValue());
		
		assertEquals(OrderPropertyNames.STATE_PROPERTY, propertyChange2.getProperty().getName());
		assertEquals(com.imarcats.interfaces.client.v100.dto.types.OrderState.Executed, ((ObjectPropertyDto) propertyChange2.getProperty()).getValue().getObjectValue());
	}
	
	public void testTriggerRemoveOnExecution() throws Exception {
		QuoteType quoteType = QuoteType.Price;
		
		
		
		MockDatastores datastore = new MockDatastores();
		Market marketModel = createMarket(quoteType, datastore);
		
		OrderManagementContext orderManagementContext = createOrderManagerContext();
		
		MockTimeTrigger trigger = new MockTimeTrigger(datastore);
		MarketImpl market = new MarketImpl(marketModel, trigger, datastore, datastore, datastore, datastore);
				
		// generate test orders
		TestOrderFlowGenerator gen = new TestOrderFlowGenerator(quoteType, marketModel);
		Order[] orders = gen.getOrders();

		// create order to place it to the data source
		datastore.createOrder(orders[0]);
		
		// submit order
		market.submit(datastore.findOrderBy(orders[0].getKey()), orderManagementContext);

		OrderInternal orderInternal = datastore.findOrderBy(orders[0].getKey());
		
		// test, if execution cancels triggers
		orderInternal.setExpirationTriggerActionKey(10L);
		orderInternal.setQuoteChangeTriggerKey(11L);
		trigger.addAction(new TimerAction() {
			
			@Override
			public void execute(Date actualCallTime_, Date scheduledCallTime_,
					ListenerCallParameters listenerParameters_,
					ListenerCallContext listenerContext_) {
				// does nothing
				
			}
		});
		orderInternal.getOrderModel().setTriggerInstruction(OrderTriggerInstruction.StopLoss);
		orderInternal.getOrderModel().addTriggerProperty(PropertyUtils.createDoubleProperty(OrderPropertyNames.STOP_QUOTE_PROPERTY_NAME, 10));
		orderInternal.addQuoteChangeTrigger(market, orderManagementContext);
		orderInternal.getOrderModel().setExpirationInstruction(OrderExpirationInstruction.DayOrder);
		
		orderInternal.recordExecution(market, orderInternal.getSize(), orderManagementContext);
		
		assertEquals(null, orderInternal.getQuoteChangeTriggerKey());
		assertEquals(null, orderInternal.getExpirationTriggerActionKey());
		
		assertEquals(null, trigger.getAction());
	}
	
	public void testMarketOrderSubmit() throws Exception {
		marketOrderSubmitTest(QuoteType.Price);
		marketOrderSubmitTest(QuoteType.Yield);
	}
	
	private void marketOrderSubmitTest(QuoteType quoteType_) throws InterruptedException {
			
		MockDatastores datastore = new MockDatastores();
		Market market = getMarket(datastore);
		
		OrderManagementContext orderManagementContext = createOrderManagerContext();
		
		market.setQuoteType(quoteType_);
		market.setExecutionSystem(ExecutionSystem.ContinuousTwoSidedAuction);
		market.setActivationStatus(ActivationStatus.Activated);
		market.setState(MarketState.Open);
		
		TestOrderFlowGenerator gen = new TestOrderFlowGenerator(quoteType_, market);
		Order[] orders = gen.getOrders();
		
		// create order to place it to the data source
		for (Order order : orders) {
			datastore.createOrder(order);
		}
		
		MarketInternal marketImpl = new MarketImpl(market, null, datastore, datastore, datastore, datastore);
		
		orders[0].setType(OrderType.Market);
		orders[0].setSide(OrderSide.Buy);
		orders[0].setSize(9);
		orders[0].setLimitQuoteValue(wrapQuote(0));
		orders[0].setExpirationInstruction(OrderExpirationInstruction.ImmediateOrCancel);
		
		try {
			marketImpl.submit(wrapOrder(orders[0], datastore), orderManagementContext);
			fail();
		} catch (MarketRuntimeException e) {
			// expected
			assertEquals(ExceptionLanguageKeys.MARKET_ORDER_CANNOT_BE_SUBMITTED_IF_THE_OTHER_SIDE_BOOK_IS_EMPTY, e.getLanguageKey());
		}
		assertEquals(OrderState.Created, orders[0].getState());
		

		orders[1].setType(OrderType.Limit);
		orders[1].setSide(OrderSide.Buy);
		orders[1].setLimitQuoteValue(wrapQuote(10));

		marketImpl.submit(wrapOrder(orders[1], datastore), orderManagementContext);
		assertEquals(OrderState.Submitted, orders[1].getState());
		
		orders[2].setType(OrderType.Market);
		orders[2].setSide(OrderSide.Sell);
		orders[2].setSize(5);
		orders[2].setLimitQuoteValue(wrapQuote(0));
		orders[2].setExpirationInstruction(OrderExpirationInstruction.ImmediateOrCancel);

		marketImpl.submit(wrapOrder(orders[2], datastore), orderManagementContext);
		assertEquals(OrderState.Canceled, orders[2].getState());
		
		// This cannot be tested because Market Order is not allowed on Non-Open Market, 
		// but, if it is submitted to an Open Market, it will be executed right after 
		// so we cannot create a book with only Market Orders + Market Orders are Immediate-or-Cancel, 
		// we will not have standing Market Orders 
//		orders[3].setType(OrderType.Market);
//		orders[3].setSide(OrderSide.Sell);
//		orders[3].setSize(5);
//		orders[3].setLimitQuoteValue(wrapQuote(0));
//
//		marketImpl.submit(wrapOrder(orders[3], datastore), orderManagementContext);
//		assertEquals(OrderState.Submitted, orders[3].getState());
//
//		try {
//			marketImpl.submit(wrapOrder(orders[0], datastore), orderManagementContext);
//			fail();
//		} catch (MarketRuntimeException e) {
//			// expected
//			assertEquals(ExceptionLanguageKeys.MARKET_ORDER_CANNOT_BE_SUBMITTED_IF_THE_OTHER_SIDE_BOOK_HAS_ONLY_MARKET_ORDERS, e.getLanguageKey());
//		}
//		assertEquals(OrderState.Created, orders[0].getState());
		
		orders[4].setType(OrderType.Limit);
		orders[4].setSide(OrderSide.Sell);
		orders[4].setLimitQuoteValue(wrapQuote(10));

		marketImpl.submit(wrapOrder(orders[4], datastore), orderManagementContext);
		assertEquals(OrderState.Submitted, orders[4].getState());
		
		marketImpl.submit(wrapOrder(orders[0], datastore), orderManagementContext);
		
		market.setState(MarketState.Closed);
		try {
			marketImpl.submit(wrapOrder(orders[5], datastore), orderManagementContext);
			fail();
		} catch (MarketRuntimeException e) {
			// expected
			assertEquals(ExceptionLanguageKeys.MARKET_ORDER_IS_NOT_SUPPORTED_WHEN_THE_MARKET_IS_NOT_OPEN, e.getLanguageKey());
		}

		market.setExecutionSystem(ExecutionSystem.CallMarketWithSingleSideAuction);
		market.setState(MarketState.Open);
		
		try {
			marketImpl.submit(wrapOrder(orders[5], datastore), orderManagementContext);
			fail();
		} catch (MarketRuntimeException e) {
			// expected
			assertEquals(ExceptionLanguageKeys.MARKET_ORDER_IS_NOT_SUPPORTED_ON_CALL_MARKET, e.getLanguageKey());
		}
		
		// This cannot be tested because some other rules stop Market Order from being Submitted
//		orders[5].setType(OrderType.Market);
//		orders[5].setSize(1);
//		orders[5].setSide(OrderSide.Buy);
//		orders[5].setLimitQuoteValue(wrapQuote(0));
//
//		try {
//			marketImpl.submit(wrapOrder(orders[5], datastore), orderManagementContext);
//			fail();
//		} catch (MarketRuntimeException e) {
//			// expected
//			assertEquals(ExceptionLanguageKeys.MARKET_ORDER_SUBMITTED_CANNOT_BE_FILLED_ON_CALL_MARKET, e.getLanguageKey());
//		}
	}
	
	public void testCancelNonSubmittedOrder() throws Exception {
		
		MockDatastores datastore = new MockDatastores();
		Market market = getMarket(datastore);
		
		OrderManagementContext orderManagementContext = createOrderManagerContext();
		
		market.setQuoteType(QuoteType.Price);
		market.setExecutionSystem(ExecutionSystem.CallMarketWithSingleSideAuction);
		market.setActivationStatus(ActivationStatus.Activated);
		
		TestOrderFlowGenerator gen = new TestOrderFlowGenerator(QuoteType.Price, market);
		Order[] orders = gen.getOrders();
		
		// test created 
		orders[0].setState(OrderState.Created);
		
		MarketInternal marketImpl = new MarketImpl(market, null, datastore, datastore, datastore, datastore);
		marketImpl.cancel(wrapOrder(orders[0], datastore), MarketSystemMessageLanguageKeys.ORDER_CANCELED_BY_USER, orderManagementContext);
		
		assertEquals(OrderState.Canceled, orders[0].getState());
		assertEquals(MarketSystemMessageLanguageKeys.ORDER_CANCELED_BY_USER, orders[0].getCancellationCommentLanguageKey());
	
		// test pending submit 
		orders[0].setState(OrderState.PendingSubmit);
		
		marketImpl = new MarketImpl(market, null, datastore, datastore, datastore, datastore);
		marketImpl.cancel(wrapOrder(orders[0], datastore), MarketSystemMessageLanguageKeys.ORDER_CANCELED_BY_USER, orderManagementContext);
		
		assertEquals(OrderState.Canceled, orders[0].getState());
		assertEquals(MarketSystemMessageLanguageKeys.ORDER_CANCELED_BY_USER, orders[0].getCancellationCommentLanguageKey());
		
		// test pending submit 
		orders[0].setState(OrderState.WaitingSubmit);
		
		marketImpl = new MarketImpl(market, null, datastore, datastore, datastore, datastore);
		marketImpl.cancel(wrapOrder(orders[0], datastore), MarketSystemMessageLanguageKeys.ORDER_CANCELED_BY_USER, orderManagementContext);
		
		assertEquals(OrderState.Canceled, orders[0].getState());
		assertEquals(MarketSystemMessageLanguageKeys.ORDER_CANCELED_BY_USER, orders[0].getCancellationCommentLanguageKey());
		
		// test executed 
		orders[0].setState(OrderState.Executed);
		
		marketImpl = new MarketImpl(market, null, datastore, datastore, datastore, datastore);
		try {
			marketImpl.cancel(wrapOrder(orders[0], datastore), MarketSystemMessageLanguageKeys.ORDER_CANCELED_BY_USER, orderManagementContext);
			fail();
		} catch(MarketRuntimeException e) {
			assertEquals(MarketExceptionLanguageConstants.EXECUTED_ORDER_CANNOT_BE_CANCELED, e.getLanguageKey());
		}
		
		
		// test cancelled 
		orders[0].setState(OrderState.Canceled);
		
		marketImpl = new MarketImpl(market, null, datastore, datastore, datastore, datastore);
		try {
			marketImpl.cancel(wrapOrder(orders[0], datastore), MarketSystemMessageLanguageKeys.ORDER_CANCELED_BY_USER, orderManagementContext);
			fail();
		} catch(MarketRuntimeException e) {
			assertEquals(MarketExceptionLanguageConstants.ORDER_HAS_ALREADY_BEEN_CANCELED, e.getLanguageKey());
		}
		
		// test deleted 
		orders[0].setState(OrderState.Deleted);
		
		marketImpl = new MarketImpl(market, null, datastore, datastore, datastore, datastore);
		marketImpl.cancel(wrapOrder(orders[0], datastore), MarketSystemMessageLanguageKeys.ORDER_CANCELED_BY_USER, orderManagementContext);
		
		assertEquals(OrderState.Deleted, orders[0].getState());
	}
	
	public void testMarketActions() throws Exception {
		
		
		OrderManagementContext orderManagementContext = createOrderManagerContext();
		
		Market market = new Market();
		market.setMarketCode("TestCode");
		market.setState(MarketState.Closed);
		
		CircuitBreaker circuitBreaker = createCircuitBreakerForMarket();
		market.setCircuitBreaker(circuitBreaker);
		
		MarketInternal marketImpl = new MarketImpl(market, createMockMarketTimer(), null, null, null, null);
		
		// test opening non-active market
		assertEquals(MarketState.Closed, market.getState());
		try {
			marketImpl.openMarket(orderManagementContext);
			fail();
		} catch (MarketRuntimeException e) {
			// expected
			assertEquals(ExceptionLanguageKeys.NON_ACTIVE_MARKET_CANNOT_BE_OPENED, e.getLanguageKey());
			
		}
		assertEquals(MarketState.Closed, market.getState());
		
		// test closing non-active market
		try {
			marketImpl.closeMarket(orderManagementContext);
			fail();
		} catch (MarketRuntimeException e) {
			// expected
			assertEquals(ExceptionLanguageKeys.NON_ACTIVE_MARKET_CANNOT_BE_CLOSED, e.getLanguageKey());
			
		}
		assertEquals(MarketState.Closed, market.getState());

		// test halting non-active market
		try {
			marketImpl.haltMarket(orderManagementContext);
			fail();
		} catch (MarketRuntimeException e) {
			// expected
			assertEquals(ExceptionLanguageKeys.NON_ACTIVE_MARKET_CANNOT_BE_HALTED, e.getLanguageKey());
			
		}
		assertEquals(MarketState.Closed, market.getState());
		

		// test opening 
		market.setActivationStatus(ActivationStatus.Activated);
		// setting up a fake last trade to see that it is being deleted
		QuoteAndSize lastTrade = new QuoteAndSize();
		lastTrade.setQuote(Quote.createQuote(10));
		lastTrade.setSize(10);
		market.setLastTrade(lastTrade);
		// setting up a fake opening quote to see that it is being deleted
		market.setOpeningQuote(lastTrade.getQuote());
		
		marketImpl.openMarket(orderManagementContext);
		
		assertEquals(MarketState.Open, market.getState());
		assertEquals(null, market.getLastTrade());
		assertEquals(null, market.getOpeningQuote());
		
		// test property changes 
		check2StateChange(orderManagementContext, MarketState.Opening, MarketState.Open);
		((MockPropertyChangeSessionImpl) orderManagementContext.getPropertyChangeSession()).rollback();
		
		// test opening an open market 
		try {
			marketImpl.openMarket(orderManagementContext);
			fail();
		} catch (MarketRuntimeException e) {
			// expected
			assertEquals(ExceptionLanguageKeys.NON_CLOSED_MARKET_CANNOT_BE_OPENED, e.getLanguageKey());
			
		}
		
		// test halting 
		marketImpl.haltMarket(orderManagementContext);
		assertEquals(MarketState.Halted, market.getState());
		
		// test property changes 
		assertEquals(1, ((MockPropertyChangeSessionImpl) orderManagementContext.getPropertyChangeSession()).getPropertyChanges().length);
		
		// check resulted property changes 
		PropertyValueChangeDto propertyChange1 = getPropertyChange(0, 0, orderManagementContext);
		
		assertEquals(MarketPropertyNames.STATE_PROPERTY, propertyChange1.getProperty().getName());
		assertEquals(com.imarcats.interfaces.client.v100.dto.types.MarketState.Halted, ((ObjectPropertyDto) propertyChange1.getProperty()).getValue().getObjectValue());
	
		((MockPropertyChangeSessionImpl) orderManagementContext.getPropertyChangeSession()).rollback();
		
		// test halting halted market 
		try {
			marketImpl.haltMarket(orderManagementContext);
			fail();
		} catch (MarketRuntimeException e) {
			// expected
			assertEquals(ExceptionLanguageKeys.NON_OPEN_MARKET_CANNOT_BE_HALTED, e.getLanguageKey());
			
		}
		
		// test opening halted market
		try {
			marketImpl.openMarket(orderManagementContext);
			fail();
		} catch (MarketRuntimeException e) {
			// expected
			assertEquals(ExceptionLanguageKeys.NON_CLOSED_MARKET_CANNOT_BE_OPENED, e.getLanguageKey());
		}
		assertEquals(MarketState.Halted, market.getState());
		
		// test re-opening
		marketImpl.reOpenMarket(orderManagementContext);
		assertEquals(MarketState.Open, market.getState());
		
		// test property changes 
		check2StateChange(orderManagementContext, MarketState.ReOpening, MarketState.Open);
		((MockPropertyChangeSessionImpl) orderManagementContext.getPropertyChangeSession()).rollback();
		
		
		// test re-opening non-halted market 
		try {
			marketImpl.reOpenMarket(orderManagementContext);
			fail();
		} catch (MarketRuntimeException e) {
			// expected
			assertEquals(ExceptionLanguageKeys.NON_HALTED_MARKET_CANNOT_BE_OPENED, e.getLanguageKey());
		}
		assertEquals(MarketState.Open, market.getState());
		
		// test closing market
		marketImpl.closeMarket(orderManagementContext);
		assertEquals(MarketState.Closed, market.getState());		
		
		// test property changes 
		assertEquals(3, ((MockPropertyChangeSessionImpl) orderManagementContext.getPropertyChangeSession()).getPropertyChanges().length);
		
		// check resulted property changes 
		propertyChange1 = getPropertyChange(0, 0, orderManagementContext);
		PropertyValueChangeDto propertyChange2 = getPropertyChange(1, 0, orderManagementContext);
		PropertyValueChangeDto propertyChange3 = getPropertyChange(2, 0, orderManagementContext);
		
		assertEquals(MarketPropertyNames.STATE_PROPERTY, propertyChange1.getProperty().getName());
		assertEquals(com.imarcats.interfaces.client.v100.dto.types.MarketState.Closing, ((ObjectPropertyDto) propertyChange1.getProperty()).getValue().getObjectValue());
		
		assertEquals(MarketPropertyNames.HALT_LEVEL_PROPERTY, propertyChange2.getProperty().getName());
		assertEquals(-1, ((IntPropertyDto) propertyChange2.getProperty()).getValue());
		
		assertEquals(MarketPropertyNames.STATE_PROPERTY, propertyChange3.getProperty().getName());
		assertEquals(com.imarcats.interfaces.client.v100.dto.types.MarketState.Closed, ((ObjectPropertyDto) propertyChange3.getProperty()).getValue().getObjectValue());
		((MockPropertyChangeSessionImpl) orderManagementContext.getPropertyChangeSession()).rollback();
		((MockPropertyChangeSessionImpl) orderManagementContext.getPropertyChangeSession()).rollback();
		
		// test closing closed market
		try {
			marketImpl.closeMarket(orderManagementContext);
			fail();
		} catch (MarketRuntimeException e) {
			// expected
			assertEquals(ExceptionLanguageKeys.NON_OPEN_MARKET_CANNOT_BE_CLOSED, e.getLanguageKey());
		}
		assertEquals(MarketState.Closed, market.getState());
		
		// test halting closed market
		try {
			marketImpl.haltMarket(orderManagementContext);
			fail();
		} catch (MarketRuntimeException e) {
			// expected
			assertEquals(ExceptionLanguageKeys.NON_OPEN_MARKET_CANNOT_BE_HALTED, e.getLanguageKey());
		}
		assertEquals(MarketState.Closed, market.getState());
		
		// test re-opening closed market - nothing happens
		marketImpl.reOpenMarket(orderManagementContext);
		assertEquals(MarketState.Closed, market.getState());
		
		// open market for further testing
		marketImpl.openMarket(orderManagementContext);
		
		// test property changes 
		check2StateChange(orderManagementContext, MarketState.Opening, MarketState.Open);
		((MockPropertyChangeSessionImpl) orderManagementContext.getPropertyChangeSession()).rollback();
		
		// test closing halted market
		marketImpl.haltMarket(orderManagementContext);
		assertEquals(MarketState.Halted, market.getState());
		((MockPropertyChangeSessionImpl) orderManagementContext.getPropertyChangeSession()).rollback();
		
		// setting up a fake opening quote to see that it is being deleted
		market.setClosingQuote(lastTrade.getQuote());
		
		marketImpl.closeMarket(orderManagementContext);
		assertEquals(MarketState.Closed, market.getState());		
		((MockPropertyChangeSessionImpl) orderManagementContext.getPropertyChangeSession()).rollback();
		
		assertEquals(null, market.getClosingQuote());
	}

	private MarketTimer createMockMarketTimer() {
		return new MarketTimer() {

			@Override
			public void cancel(Long action_) {
				// does nothing
				
			}
			@Override
			public Long scheduleAbsolute(Date scheduledTime_, boolean moveToNextDayIfInPast_, 
					TimerAction action_) {
				// does nothing
				return null;
			}
			@Override 
			public Long scheduleRelative(long relativeTimePeriodMilliseconds_,
					TimerAction action_) {
				// does nothing
				return null;
			}
			@Override
			public Long scheduleToTime(Date date_, TimeOfDay time_, 
					RecurringActionDetail recurringActionDetail_, BusinessCalendar calendar_, boolean moveToNextDayIfInPast_, 
					TimerAction action_) {
				// does nothing
				return null;
			}
			@Override
			public Long rescheduleToTime(Long scheduledTimerID_, Date date_, TimeOfDay time_, boolean moveToNextDayIfInPast_, Long actionKey_) {
				// does nothing
				return null;
			}
			@Override
			public Long rescheduleAbsolute(
					Long scheduledTimerID_, 
					Date scheduledTime_,
					boolean moveToNextDayIfInPast_, 
					Long actionKey_) {
				// does nothing
				return null;
			}
			@Override
			public Long rescheduleRelative(
					Long scheduledTimerID_, 
					long relativeTimePeriodMilliseconds_,
					Long actionKey_) {
				// does nothing
				return null;
			}
		};
	}

	private CircuitBreaker createCircuitBreakerForMarket() {
		HaltRule haltRule = new HaltRule();
		haltRule.setHaltPeriod(10);
		haltRule.setQuoteChangeAmount(100);
		CircuitBreaker circuitBreaker = new CircuitBreaker();
		List<HaltRule> haltRules = new ArrayList<HaltRule>();
		haltRules.add(haltRule);
		circuitBreaker.setHaltRules(haltRules);
		return circuitBreaker;
	}
	
	public void testTestOpenClosePrice() throws Exception {
		
		MockDatastores datastore = new MockDatastores();
		Market market = getMarket(datastore);
		market.setState(MarketState.Closed);
		market.setCircuitBreaker(createCircuitBreakerForMarket());
		
		OrderManagementContext orderManagementContext = createOrderManagerContext();
		
		MarketImpl marketImpl = new MarketImpl(market, createMockMarketTimer(), datastore, datastore, datastore, datastore);
		
		market.setActivationStatus(ActivationStatus.Activated);
		
		QuoteType quoteType = QuoteType.Price;
		TestOrderFlowGenerator gen = new TestOrderFlowGenerator(quoteType, market);
		Order[] orders = gen.getOrders();
		
		// create order to place it to the data source
		for (Order order : orders) {
			datastore.createOrder(order);
		}
		
		for (Order order : orders) {
			// Exclude Market Orders
			if(order.getType() != OrderType.Market) {
				marketImpl.submit(wrapOrder(order, datastore), orderManagementContext);
			}
		}
		
		orderManagementContext = createOrderManagerContext();
		marketImpl.openMarket(orderManagementContext);
		
		assertEquals(20.0, marketImpl.getOpeningQuote().getQuoteValue());
		assertEquals(true, marketImpl.getOpeningQuote().getValidQuote());

		// check market data
		MockMarketDataSessionImpl marketDataSession = (MockMarketDataSessionImpl) orderManagementContext.getMarketDataSession();
		MarketDataChange[] marketDataChanges = ((MockMarketDataSessionImpl) marketDataSession).getMarketDataChanges();
		
		MarketDataChange[] filteredMarketDataChanges = filterMarketData(marketDataChanges, MarketDataType.Open);
		
		assertEquals(1, filteredMarketDataChanges.length);
		checkMarketDataChange(quoteType, marketImpl.getMarketCode(),
				filteredMarketDataChanges[0], MarketDataType.Open, 20.0, 0, null);
		
		// check bid/ask 
		MarketDataChange[] bidTradesMarketData = filterMarketData(marketDataChanges, MarketDataType.Bid);
		assertEquals(1, bidTradesMarketData.length);
		checkMarketDataChange(quoteType, market.getMarketCode(), bidTradesMarketData[0], MarketDataType.Bid, 19.8, 7, false);
		
		MarketDataChange[] askTradesMarketData = filterMarketData(marketDataChanges, MarketDataType.Ask);
		assertEquals(1, askTradesMarketData.length);
		checkMarketDataChange(quoteType, market.getMarketCode(), askTradesMarketData[0], MarketDataType.Ask, 20.1, 2, false);
		
		assertEquals(null, marketImpl.getClosingQuote());	
		
		// test halt market data 
		orderManagementContext = createOrderManagerContext();
		marketImpl.haltMarket(orderManagementContext);
		
		// halt itself will not trigger bid/ask changes - so nothing to test
		marketDataSession = (MockMarketDataSessionImpl)orderManagementContext.getMarketDataSession();
		marketDataChanges = marketDataSession.getMarketDataChanges();
		assertEquals(0, filterMarketData(marketDataChanges, MarketDataType.Bid).length);
		assertEquals(0, filterMarketData(marketDataChanges, MarketDataType.Ask).length);
		
		// test reopen 
		createAndSubmitMatchingOrder(datastore, quoteType, market, marketImpl, orderManagementContext);
		
		orderManagementContext = createOrderManagerContext();
		marketImpl.reOpenMarket(orderManagementContext);
		
		// check bid/ask 
		marketDataSession = (MockMarketDataSessionImpl)orderManagementContext.getMarketDataSession();
		marketDataChanges = marketDataSession.getMarketDataChanges();
		bidTradesMarketData = filterMarketData(marketDataChanges, MarketDataType.Bid);
		assertEquals(1, bidTradesMarketData.length);
		checkMarketDataChange(quoteType, market.getMarketCode(), bidTradesMarketData[0], MarketDataType.Bid, 20.0, 2, false);
		
		askTradesMarketData = filterMarketData(marketDataChanges, MarketDataType.Ask);
		assertEquals(1, askTradesMarketData.length);
		checkMarketDataChange(quoteType, market.getMarketCode(), askTradesMarketData[0], MarketDataType.Ask, 20.1, 4, false);
		

		orderManagementContext = createOrderManagerContext();
		marketImpl.closeMarket(orderManagementContext);

		assertEquals(19.9, marketImpl.getClosingQuote().getQuoteValue());
		assertEquals(true, marketImpl.getClosingQuote().getValidQuote());
		
		// check market data
		marketDataSession = (MockMarketDataSessionImpl)orderManagementContext.getMarketDataSession();
		marketDataChanges = marketDataSession.getMarketDataChanges();
		
		filteredMarketDataChanges = filterMarketData(marketDataChanges, MarketDataType.Close);
		
		assertEquals(1, filteredMarketDataChanges.length);
		checkMarketDataChange(quoteType, marketImpl.getMarketCode(),
				filteredMarketDataChanges[0], MarketDataType.Close, 19.9, 0, null);
		
		// check bid/ask
		
		// No change on Bid/Ask is triggered here - so nothing to test
		marketDataSession = (MockMarketDataSessionImpl)orderManagementContext.getMarketDataSession();
		marketDataChanges = marketDataSession.getMarketDataChanges();
		assertEquals(0, filterMarketData(marketDataChanges, MarketDataType.Bid).length);
		assertEquals(0, filterMarketData(marketDataChanges, MarketDataType.Ask).length);
	}
	
	public void testCallMarkets() throws Exception {
		
		
		MockDatastores datastore = new MockDatastores();
		QuoteType quoteType = QuoteType.Price; 
		
		Market market = createMarket(quoteType, datastore);
		
		MarketImpl marketImpl = new MarketImpl(market, null, null, null, null, null);
		
		OrderManagementContext orderManagementContext = createOrderManagerContext();
		
		market.setActivationStatus(ActivationStatus.Activated);
		
		try {
			marketImpl.callMarket(orderManagementContext);
			fail();
		} catch (MarketRuntimeException e) {
			// expected
			assertEquals(ExceptionLanguageKeys.NON_CALL_MARKET_CANNOT_BE_CALLED, e.getLanguageKey());
		}
		
		market.setExecutionSystem(ExecutionSystem.CallMarketWithSingleSideAuction);
		market.setState(MarketState.Called);

		marketImpl = new MarketImpl(market, null, null, null, null, null);

		try {
			marketImpl.callMarket(orderManagementContext);
			fail();
		} catch (MarketRuntimeException e) {
			// expected
			assertEquals(ExceptionLanguageKeys.OPEN_MARKET_CAN_ONLY_BE_CALLED, e.getLanguageKey());
		}
		
		market.setState(MarketState.Open);
		market.setActivationStatus(ActivationStatus.Deactivated);
		marketImpl = new MarketImpl(market, null, null, null, null, null);		
		
		try {
			marketImpl.callMarket(orderManagementContext);
			fail();
		} catch (MarketRuntimeException e) {
			// expected
			assertEquals(ExceptionLanguageKeys.NON_ACTIVE_MARKET_CANNOT_BE_CALLED, e.getLanguageKey());
		}
		market.setActivationStatus(ActivationStatus.Activated);
		
		marketImpl = new MarketImpl(market, null, datastore, datastore, datastore, datastore);
		
		
		orderManagementContext = createOrderManagerContext();
		marketImpl.callMarket(orderManagementContext);

		check2StateChange(orderManagementContext, MarketState.Called, MarketState.Open);
		
		assertEquals(null, marketImpl.getOpeningQuote());	
		assertEquals(null, marketImpl.getClosingQuote());	
		
		// test bid/ask on call
		createAndSubmitMatchingOrder(datastore, quoteType, market,
				marketImpl, orderManagementContext);
		
		orderManagementContext = createOrderManagerContext();
		marketImpl.callMarket(orderManagementContext);
		
		MarketDataChange[] marketDataChanges = ((MockMarketDataSessionImpl)orderManagementContext.getMarketDataSession()).getMarketDataChanges();
		MarketDataChange[] lastTradesMarketData = filterMarketData(marketDataChanges, MarketDataType.Last);
		
		assertEquals(1, lastTradesMarketData.length);
		checkMarketDataChange(quoteType, market.getMarketCode(), lastTradesMarketData[0], MarketDataType.Last, 19.9, 1, null);
		
		MarketDataChange[] bidTradesMarketData = filterMarketData(marketDataChanges, MarketDataType.Bid);
		
		assertEquals(1, bidTradesMarketData.length);
		
		checkMarketDataChange(quoteType, market.getMarketCode(), bidTradesMarketData[0], MarketDataType.Bid, 20.0, 2, false);
		
		MarketDataChange[] askTradesMarketData = filterMarketData(marketDataChanges, MarketDataType.Ask);
		
		assertEquals(1, askTradesMarketData.length);
		
		checkMarketDataChange(quoteType, market.getMarketCode(), askTradesMarketData[0], MarketDataType.Ask, 20.1, 2, false);
		
		assertNotNull(marketImpl.getLastTrade());

		MatchedTradeDto[] matchedTrades = ((MockTradeNotificationSessionImpl) orderManagementContext.getTradeNotificationSession()).getTrades(); 
		
		assertEquals(1, matchedTrades.length);
		checkPair(MatchedTradeDtoMapping.INSTANCE.fromDto(matchedTrades[0]), 1, "Bea", "Sol", getExpectedQuote(19.9, quoteType));
	}

	private void createAndSubmitMatchingOrder(
			MockDatastores datastore,
			QuoteType quoteType, Market market, MarketImpl marketImpl,
			OrderManagementContext orderManagementContext)
			throws InterruptedException {
		TestOrderFlowGenerator gen = new TestOrderFlowGenerator(quoteType, market);
		Order[] orders = gen.getOrders();
		
		datastore.createOrder(orders[0]);
		marketImpl.submit(wrapOrder(orders[0], datastore), orderManagementContext);
		datastore.createOrder(orders[1]);
		marketImpl.submit(wrapOrder(orders[1], datastore), orderManagementContext);
		datastore.createOrder(orders[3]);
		marketImpl.submit(wrapOrder(orders[3], datastore), orderManagementContext);
	}

	private void check2StateChange(
			OrderManagementContext orderManagementContext, MarketState state1,
			MarketState state2) {
		PropertyChanges[] propertyChanges = ((MockPropertyChangeSessionImpl) orderManagementContext.getPropertyChangeSession()).getPropertyChanges();
		assertEquals(2, propertyChanges.length);
		
		// check resulted property changes 
		PropertyValueChangeDto propertyChange1 = getPropertyChange(0, 0, orderManagementContext);
		PropertyValueChangeDto propertyChange2 = getPropertyChange(1, 0, orderManagementContext);
		
		assertEquals(MarketPropertyNames.STATE_PROPERTY, propertyChange1.getProperty().getName());

		assertEquals(state1.toString(), ((ObjectPropertyDto) propertyChange1.getProperty()).getValue().getObjectValue().toString());
		
		assertEquals(MarketPropertyNames.STATE_PROPERTY, propertyChange2.getProperty().getName());
		assertEquals(state2.toString(), ((ObjectPropertyDto) propertyChange2.getProperty()).getValue().getObjectValue().toString());
	}
}
