package com.imarcats.market.engine.order;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.imarcats.interfaces.client.v100.dto.types.MarketDataType;
import com.imarcats.interfaces.client.v100.i18n.MarketSystemMessageLanguageKeys;
import com.imarcats.interfaces.client.v100.notification.MarketDataChange;
import com.imarcats.internal.server.infrastructure.datastore.OrderDatastore;
import com.imarcats.internal.server.infrastructure.marketdata.MarketDataSession;
import com.imarcats.internal.server.infrastructure.marketdata.MarketDataSessionImpl;
import com.imarcats.internal.server.infrastructure.notification.properties.PropertyChangeSessionImpl;
import com.imarcats.internal.server.interfaces.order.OrderInternal;
import com.imarcats.internal.server.interfaces.order.OrderManagementContext;
import com.imarcats.internal.server.interfaces.order.OrderManagementContextImpl;
import com.imarcats.market.engine.market.MarketImpl;
import com.imarcats.market.engine.market.MockMarketBase;
import com.imarcats.market.engine.testutils.MockDatastores;
import com.imarcats.market.engine.testutils.MockMarketDataSessionImpl;
import com.imarcats.market.engine.testutils.MockPropertyChangeSessionImpl;
import com.imarcats.market.engine.testutils.OrderCompareTestCaseBase;
import com.imarcats.model.BuyBook;
import com.imarcats.model.OrderBookEntryModel;
import com.imarcats.model.OrderBookModel;
import com.imarcats.model.Market;
import com.imarcats.model.Order;
import com.imarcats.model.OrderBookFactory;
import com.imarcats.model.test.testutils.MockIdentityGenerator;
import com.imarcats.model.test.testutils.TestOrderFlowGenerator;
import com.imarcats.model.types.OrderSide;
import com.imarcats.model.types.OrderState;
import com.imarcats.model.types.OrderType;
import com.imarcats.model.types.Quote;
import com.imarcats.model.types.QuoteType;
import com.imarcats.model.types.SecondaryOrderPrecedenceRuleType;

public class OrderBookImplTest extends OrderCompareTestCaseBase {

	public void testBasicAddDelete() throws Exception {
		final 
		
		// Data Source to Store the Actual Orders 
		MockDatastores datastore = new MockDatastores();
		
		OrderBookImpl book = createBook(OrderSide.Sell, QuoteType.Price, datastore);
		OrderBookModel bookModel = book._orderBookModel;
		
		Order limitOrder1 = createLimitOrder1(datastore);
		
		Order limitOrder1a = createLimitOrder1(datastore);
		
		Order limitOrder2 = createLimitOrder2(datastore);

		Order limitOrder3 = createLimitOrder3(datastore);
		
		// We need at least 10 millis time delay for the Time Precedence to work
		Thread.sleep(10);		

		Order limitOrder3a = createLimitOrder3(datastore);
		
		MarketDataSession marketDataSession = new MockMarketDataSessionImpl(null); 
		OrderManagementContext orderManagementContext = new OrderManagementContextImpl(marketDataSession, null, null); 
		
		addOrdersToBook(orderManagementContext, book, limitOrder1, limitOrder1a, limitOrder2,
				limitOrder3, limitOrder3a, datastore);
		
		assertEqualsOrder(getOrder(0, 0, bookModel, datastore), limitOrder1);		
		assertEqualsOrder(getOrder(0, 1, bookModel, datastore), limitOrder1a);	
		assertEqualsOrder(getOrder(1, 0, bookModel, datastore), limitOrder2);
		assertEqualsOrder(getOrder(2, 0, bookModel, datastore), limitOrder3);
		assertEqualsOrder(getOrder(2, 1, bookModel, datastore), limitOrder3a);
		
		assertEquals(3, bookModel.size());
		
		// check level ii market data 
		MarketDataChange[] marketDataChanges = ((MockMarketDataSessionImpl) marketDataSession).getMarketDataChanges();
		
		assertEquals(5, marketDataChanges.length);
		
		checkLevelIIMktData(MockDatastores.MARKET_ID, marketDataChanges[0], MarketDataType.LevelIIAsk, 100, true, 10, false);
		checkLevelIIMktData(MockDatastores.MARKET_ID, marketDataChanges[1], MarketDataType.LevelIIAsk, 100, true, 20, false);
		checkLevelIIMktData(MockDatastores.MARKET_ID, marketDataChanges[2], MarketDataType.LevelIIAsk, 110, true, 10, false);
		checkLevelIIMktData(MockDatastores.MARKET_ID, marketDataChanges[3], MarketDataType.LevelIIAsk, 120, true, 10, false);
		checkLevelIIMktData(MockDatastores.MARKET_ID, marketDataChanges[4], MarketDataType.LevelIIAsk, 120, true, 20, false);
		
		marketDataSession = new MockMarketDataSessionImpl(null); 
		orderManagementContext = new OrderManagementContextImpl(marketDataSession, null, null); 
		
		book.deleteOrder(wrapOrder(limitOrder2, datastore), orderManagementContext);
		
		assertEqualsOrder(getOrder(0, 0, bookModel, datastore), limitOrder1);		
		assertEqualsOrder(getOrder(0, 1, bookModel, datastore), limitOrder1a);
		assertEqualsOrder(getOrder(1, 0, bookModel, datastore), limitOrder3);
		assertEqualsOrder(getOrder(1, 1, bookModel, datastore), limitOrder3a);
		
		assertEquals(2, bookModel.size());
		
		// check level ii market data 
		marketDataChanges = ((MockMarketDataSessionImpl) marketDataSession).getMarketDataChanges();
		
		assertEquals(1, marketDataChanges.length);
		
		checkLevelIIMktData(MockDatastores.MARKET_ID, marketDataChanges[0], MarketDataType.LevelIIAsk, 110, false, 0, null);
		
		marketDataSession = new MockMarketDataSessionImpl(null); 
		orderManagementContext = new OrderManagementContextImpl(marketDataSession, null, null); 

		book.deleteOrder(wrapOrder(limitOrder1, datastore), orderManagementContext);
		
		assertEqualsOrder(getOrder(0, 0, bookModel, datastore), limitOrder1a);
		assertEqualsOrder(getOrder(1, 0, bookModel, datastore), limitOrder3);
		assertEqualsOrder(getOrder(1, 1, bookModel, datastore), limitOrder3a);
		
		assertEquals(2, bookModel.size());
		
		// check level ii market data 
		marketDataChanges = ((MockMarketDataSessionImpl) marketDataSession).getMarketDataChanges();
		
		assertEquals(1, marketDataChanges.length);
		
		checkLevelIIMktData(MockDatastores.MARKET_ID, marketDataChanges[0], MarketDataType.LevelIIAsk, 100, true, 10, false);
		
		marketDataSession = new MockMarketDataSessionImpl(null); 
		orderManagementContext = new OrderManagementContextImpl(marketDataSession, null, null); 
		
		book.deleteOrder(wrapOrder(limitOrder1a, datastore), orderManagementContext);
		
		assertEqualsOrder(getOrder(0, 0, bookModel, datastore), limitOrder3);
		assertEqualsOrder(getOrder(0, 1, bookModel, datastore), limitOrder3a);
		
		assertEquals(1, bookModel.size());
		
		// check level ii market data 
		marketDataChanges = ((MockMarketDataSessionImpl) marketDataSession).getMarketDataChanges();
		
		assertEquals(1, marketDataChanges.length);
		
		checkLevelIIMktData(MockDatastores.MARKET_ID, marketDataChanges[0], MarketDataType.LevelIIAsk, 100, false, 0, null);
	
	}

	public void testSubmitCancelExecution() throws Exception {
		final 
		
		// Data Source to Store the Actual Orders 
		MockDatastores datastore = new MockDatastores();
		
		OrderBookImpl book = createBook(OrderSide.Sell, QuoteType.Price, datastore);
		OrderBookModel bookModel = book._orderBookModel;
		
		Order limitOrder1 = createLimitOrder1(datastore);
		
		Order limitOrder1a = createLimitOrder1(datastore);
		
		Order limitOrder2 = createLimitOrder2(datastore);
		
		PropertyChangeSessionImpl propertyChangeSession = new MockPropertyChangeSessionImpl(null);
		
		// test submit
		MarketDataSession marketDataSession = new MockMarketDataSessionImpl(null); 
		OrderManagementContext orderManagementContext = new OrderManagementContextImpl(marketDataSession, propertyChangeSession, null); 
		
		book.recordSubmit(wrapOrder(limitOrder1, datastore), orderManagementContext);
		book.recordSubmit(wrapOrder(limitOrder1a, datastore), orderManagementContext);
		book.recordSubmit(wrapOrder(limitOrder2, datastore), orderManagementContext);
		
		assertEqualsOrder(getOrder(0, 0, bookModel, datastore), limitOrder1);		
		assertEqualsOrder(getOrder(0, 1, bookModel, datastore), limitOrder1a);	
		assertEqualsOrder(getOrder(1, 0, bookModel, datastore), limitOrder2);
		
		assertEquals(2, bookModel.size());
		
		// check level ii market data 
		MarketDataChange[] marketDataChanges = ((MockMarketDataSessionImpl) marketDataSession).getMarketDataChanges();
		
		assertEquals(3, marketDataChanges.length);
		
		checkLevelIIMktData(MockDatastores.MARKET_ID, marketDataChanges[0], MarketDataType.LevelIIAsk, 100, true, 10, false);
		checkLevelIIMktData(MockDatastores.MARKET_ID, marketDataChanges[1], MarketDataType.LevelIIAsk, 100, true, 20, false);
		checkLevelIIMktData(MockDatastores.MARKET_ID, marketDataChanges[2], MarketDataType.LevelIIAsk, 110, true, 10, false);
	
		// test cancel
		
		// cheating a bit here: setting commission charged flag, to see cancel deletes it 
		limitOrder2.setCommissionCharged(true);
		
		marketDataSession = new MockMarketDataSessionImpl(null); 
		orderManagementContext = new OrderManagementContextImpl(marketDataSession, propertyChangeSession, null); 

		book.recordCancel(wrapOrder(limitOrder2, datastore), MarketSystemMessageLanguageKeys.ORDER_CANCELED_BY_USER, orderManagementContext);
		
		assertEquals(MarketSystemMessageLanguageKeys.ORDER_CANCELED_BY_USER, limitOrder2.getCancellationCommentLanguageKey());
		assertEquals(OrderState.Canceled, limitOrder2.getState());
		assertEquals(false, limitOrder2.getCommissionCharged().booleanValue());
		
		assertEqualsOrder(getOrder(0, 0, bookModel, datastore), limitOrder1);		
		assertEqualsOrder(getOrder(0, 1, bookModel, datastore), limitOrder1a);	
		
		assertEquals(1, bookModel.size());
		
		// check level ii market data 
		marketDataChanges = ((MockMarketDataSessionImpl) marketDataSession).getMarketDataChanges();
		
		assertEquals(1, marketDataChanges.length);
		
		checkLevelIIMktData(MockDatastores.MARKET_ID, marketDataChanges[0], MarketDataType.LevelIIAsk, 110, false, 0, null);
	
		// test execution
		marketDataSession = new MockMarketDataSessionImpl(null); 
		orderManagementContext = new OrderManagementContextImpl(marketDataSession, propertyChangeSession, null); 

		book.recordExecution(wrapOrder(limitOrder1, datastore), 5, orderManagementContext);
		
		assertEqualsOrder(getOrder(0, 0, bookModel, datastore), limitOrder1);		
		assertEqualsOrder(getOrder(0, 1, bookModel, datastore), limitOrder1a);	
		
		assertEquals(1, bookModel.size());
		assertEquals(5, limitOrder1.getExecutedSize());
		assertEquals(5, limitOrder1.calculateRemainingSize());
		
		// check level ii market data 
		marketDataChanges = ((MockMarketDataSessionImpl) marketDataSession).getMarketDataChanges();
		
		assertEquals(1, marketDataChanges.length);
		
		checkLevelIIMktData(MockDatastores.MARKET_ID, marketDataChanges[0], MarketDataType.LevelIIAsk, 100, true, 15, false);
	
		marketDataSession = new MockMarketDataSessionImpl(null); 
		orderManagementContext = new OrderManagementContextImpl(marketDataSession, propertyChangeSession, null); 

		book.recordExecution(wrapOrder(limitOrder1, datastore), 5, orderManagementContext);
				
		assertEqualsOrder(getOrder(0, 0, bookModel, datastore), limitOrder1a);	
		
		assertEquals(1, bookModel.size());
		
		// check level ii market data 
		marketDataChanges = ((MockMarketDataSessionImpl) marketDataSession).getMarketDataChanges();
		
		assertEquals(1, marketDataChanges.length);
		
		checkLevelIIMktData(MockDatastores.MARKET_ID, marketDataChanges[0], MarketDataType.LevelIIAsk, 100, true, 10, false);
	
		marketDataSession = new MockMarketDataSessionImpl(null); 
		orderManagementContext = new OrderManagementContextImpl(marketDataSession, propertyChangeSession, null); 

		book.recordExecution(wrapOrder(limitOrder1a, datastore), 10, orderManagementContext);

		assertEquals(0, bookModel.size());
		
		// check level ii market data 
		marketDataChanges = ((MockMarketDataSessionImpl) marketDataSession).getMarketDataChanges();
		
		assertEquals(1, marketDataChanges.length);
		
		checkLevelIIMktData(MockDatastores.MARKET_ID, marketDataChanges[0], MarketDataType.LevelIIAsk, 100, false, 0, null);
	
		
	}
	
	private void addOrdersToBook(final OrderManagementContext orderManagementContext_,
			OrderBookImpl book, Order limitOrder1, Order limitOrder1a,
			Order limitOrder2, Order limitOrder3, Order limitOrder3a, MockDatastores datastore_) {
		book.addOrder(wrapOrder(limitOrder1, datastore_), orderManagementContext_);
		book.addOrder(wrapOrder(limitOrder1a, datastore_), orderManagementContext_);
		book.addOrder(wrapOrder(limitOrder2, datastore_), orderManagementContext_);
		book.addOrder(wrapOrder(limitOrder3, datastore_), orderManagementContext_);
		book.addOrder(wrapOrder(limitOrder3a, datastore_), orderManagementContext_);
	}

	private Order createLimitOrder3(MockDatastores datastore) {
		Order limitOrder3 = new Order();
		limitOrder3.setTargetMarketCode(MockDatastores.MARKET_ID);
		limitOrder3.setType(OrderType.Limit);	
		limitOrder3.setSize(10);
		limitOrder3.setLimitQuoteValue(wrapQuote(120));
		limitOrder3.setSubmissionDate(new Date());
		limitOrder3.setKey(MockIdentityGenerator.getId());
		datastore.createOrder(limitOrder3);
		return limitOrder3;
	}

	private Order createLimitOrder2(MockDatastores datastore) {
		Order limitOrder2 = new Order();
		limitOrder2.setTargetMarketCode(MockDatastores.MARKET_ID);
		limitOrder2.setType(OrderType.Limit);			
		limitOrder2.setLimitQuoteValue(wrapQuote(110));
		limitOrder2.setSize(10);
		limitOrder2.setSubmissionDate(new Date());
		limitOrder2.setKey(MockIdentityGenerator.getId());
		datastore.createOrder(limitOrder2);
		return limitOrder2;
	}

	private Order createLimitOrder1(MockDatastores datastore) {
		Order limitOrder1 = new Order();
		limitOrder1.setTargetMarketCode(MockDatastores.MARKET_ID);
		limitOrder1.setType(OrderType.Limit);	
		limitOrder1.setLimitQuoteValue(wrapQuote(100));
		limitOrder1.setSize(10);
		limitOrder1.setSubmissionDate(new Date());
		limitOrder1.setKey(MockIdentityGenerator.getId());
		datastore.createOrder(limitOrder1);
		return limitOrder1;
	}

	public void testCanOrderBeMatched() throws Exception {
		// set up system for test 
		final 
		
		// Data Source to Store the Actual Orders 
		MockDatastores datastore = new MockDatastores();
		
		// create standing orders
		OrderBookImpl book = createBook(OrderSide.Sell, QuoteType.Price, datastore);

		Order limitOrder1 = createLimitOrder1(datastore);
		
		Order limitOrder1a = createLimitOrder1(datastore);
		
		Order limitOrder2 = createLimitOrder2(datastore);

		Order limitOrder3 = createLimitOrder3(datastore);
		
		// We need at least 10 millis time delay for the Time Precedence to work
		Thread.sleep(10);		

		Order limitOrder3a = createLimitOrder3(datastore);
		
		
		MarketDataSessionImpl marketDataSession = new MockMarketDataSessionImpl(null); 
		OrderManagementContextImpl orderManagementContext = new OrderManagementContextImpl(marketDataSession, null, null); 
		
		// add orders to book 
		addOrdersToBook(orderManagementContext, book, limitOrder1, limitOrder1a, limitOrder2,
				limitOrder3, limitOrder3a, datastore);
		// end of set up system for test
		
		// test limit order with no restriction 
		Order lmtOrderNoRestriction = createLimitOrder1(datastore);
		lmtOrderNoRestriction.setSide(OrderSide.Buy);
		lmtOrderNoRestriction.setSize(10);
		
		assertEquals(true, book.canOrderBeMatched(wrapOrder(lmtOrderNoRestriction, datastore), new MockMarketBase()));

		// test huge limit order with no restriction 
		Order hugeLmtOrderNoRestriction = createLimitOrder1(datastore);
		hugeLmtOrderNoRestriction.setSide(OrderSide.Buy);
		hugeLmtOrderNoRestriction.setSize(10000);
		
		assertEquals(true, book.canOrderBeMatched(wrapOrder(hugeLmtOrderNoRestriction, datastore), new MockMarketBase()));
		
		// test huge limit order with entire-order-at-once 
		Order hugeLmtOrderEntireAtOnce = createLimitOrder1(datastore);
		hugeLmtOrderEntireAtOnce.setSide(OrderSide.Buy);
		hugeLmtOrderEntireAtOnce.setSize(10000);
		hugeLmtOrderEntireAtOnce.setExecuteEntireOrderAtOnce(true);
		
		assertEquals(false, book.canOrderBeMatched(wrapOrder(hugeLmtOrderEntireAtOnce, datastore), new MockMarketBase()));

		// test huge limit order with minimum size of execution 
		Order hugeLmtOrderMinExec = hugeLmtOrderEntireAtOnce;
		hugeLmtOrderMinExec.setMinimumSizeOfExecution(100);
		hugeLmtOrderMinExec.setExecuteEntireOrderAtOnce(false);
		
		assertEquals(false, book.canOrderBeMatched(wrapOrder(hugeLmtOrderMinExec, datastore), new MockMarketBase()));
	
		// change size to be smaller
		hugeLmtOrderMinExec.setMinimumSizeOfExecution(10);
		
		assertEquals(true, book.canOrderBeMatched(wrapOrder(hugeLmtOrderMinExec, datastore), new MockMarketBase()));

		// test limit order with non-marketable price 
		Order lmtOrderNonFeasiblePrice = lmtOrderNoRestriction;
		lmtOrderNonFeasiblePrice.setLimitQuoteValue(Quote.createQuote(99));
		
		assertEquals(false, book.canOrderBeMatched(wrapOrder(lmtOrderNonFeasiblePrice, datastore), new MockMarketBase()));
		
		// change it to market order 
		Order mktOrder = lmtOrderNoRestriction;
		mktOrder.setType(OrderType.Market);
		
		assertEquals(true, book.canOrderBeMatched(wrapOrder(mktOrder, datastore), new MockMarketBase()));

		// test huge market order
		Order hugeMktOrder = lmtOrderNoRestriction;
		hugeMktOrder.setType(OrderType.Market);
		hugeMktOrder.setSize(10000);
		
		assertEquals(true, book.canOrderBeMatched(wrapOrder(hugeMktOrder, datastore), new MockMarketBase()));
		
		// test huge market order with entire-order-at-once 
		Order hugeMktOrderWithRestriction = lmtOrderNoRestriction;
		hugeMktOrderWithRestriction.setType(OrderType.Market);
		hugeMktOrderWithRestriction.setSize(10000);
		hugeMktOrderWithRestriction.setExecuteEntireOrderAtOnce(true);
		
		assertEquals(false, book.canOrderBeMatched(wrapOrder(hugeMktOrderWithRestriction, datastore), new MockMarketBase()));

		// test huge market order with minimum size of execution 
		hugeMktOrderWithRestriction.setType(OrderType.Market);
		hugeMktOrderWithRestriction.setSize(10000);
		hugeMktOrderWithRestriction.setMinimumSizeOfExecution(1000);
		hugeMktOrderWithRestriction.setExecuteEntireOrderAtOnce(false);
		
		assertEquals(false, book.canOrderBeMatched(wrapOrder(hugeMktOrderWithRestriction, datastore), new MockMarketBase()));

		// test huge market order with minimum size of execution
		// restriction on size is applied on the remaining size not on the matched size 
		hugeMktOrderWithRestriction.setType(OrderType.Market);
		hugeMktOrderWithRestriction.setSize(10000);
		hugeMktOrderWithRestriction.setMinimumSizeOfExecution(500);
		hugeMktOrderWithRestriction.setExecutedSize(500); 
		hugeMktOrderWithRestriction.setExecuteEntireOrderAtOnce(false);
		
		assertEquals(false, book.canOrderBeMatched(wrapOrder(hugeMktOrderWithRestriction, datastore), new MockMarketBase()));
		
		// test huge limit order with minimum size of execution 
		// 40 can be matched with standing orders in the book 
		hugeMktOrderWithRestriction.setType(OrderType.Market);
		hugeMktOrderWithRestriction.setSize(10000);
		hugeMktOrderWithRestriction.setMinimumSizeOfExecution(40);
		
		assertEquals(true, book.canOrderBeMatched(wrapOrder(hugeMktOrderWithRestriction, datastore), new MockMarketBase()));
		
		// test limit order with entire-order-at-once 
		// standing order's size restriction make matching impossible 
		Order lmtOrderRestriction = createLimitOrder1(datastore);
		lmtOrderRestriction.setSide(OrderSide.Buy);
		lmtOrderRestriction.setSize(15);
		lmtOrderRestriction.setExecuteEntireOrderAtOnce(true);
		
		// set restriction on other orders 
		limitOrder1.setExecuteEntireOrderAtOnce(true);
		limitOrder1a.setExecuteEntireOrderAtOnce(true);
		
		assertEquals(false, book.canOrderBeMatched(wrapOrder(lmtOrderRestriction, datastore), new MockMarketBase()));
		
		// test again, clear restriction on other orders 
		limitOrder1a.setExecuteEntireOrderAtOnce(false);
		
		assertEquals(true, book.canOrderBeMatched(wrapOrder(lmtOrderRestriction, datastore), new MockMarketBase()));
		
		// test again, clear restriction on order 
		lmtOrderRestriction.setExecuteEntireOrderAtOnce(false);
		
		// set restriction on other orders 
		limitOrder1.setExecuteEntireOrderAtOnce(true);
		limitOrder1a.setExecuteEntireOrderAtOnce(true);
		
		assertEquals(true, book.canOrderBeMatched(wrapOrder(lmtOrderRestriction, datastore), new MockMarketBase()));
		
		// test again, clear restriction on other orders 
		limitOrder1a.setExecuteEntireOrderAtOnce(false);
		
		assertEquals(true, book.canOrderBeMatched(wrapOrder(lmtOrderRestriction, datastore), new MockMarketBase()));
		
		// test with restriction on other orders 
		Order lmtOrderNoRestriction2 = createLimitOrder1(datastore);
		lmtOrderNoRestriction2.setSide(OrderSide.Buy);
		lmtOrderNoRestriction2.setSize(5);
		
		// set restriction on other orders 
		limitOrder1.setExecuteEntireOrderAtOnce(true);
		limitOrder1a.setExecuteEntireOrderAtOnce(false);
		
		assertEquals(false, book.canOrderBeMatched(wrapOrder(lmtOrderNoRestriction2, datastore), new MockMarketBase()));
		
	}
	
	public void testCompositePrecedence() throws Exception {
		final 
		
		// Data Source to Store the Actual Orders 
		MockDatastores datastore = new MockDatastores();
		
		OrderBookModel bookModel = new BuyBook();
		MarketImpl mkt = new MarketImpl(new Market(), null, datastore, datastore, datastore, datastore);
		Market marketModel = mkt.getMarketModel();
		List<SecondaryOrderPrecedenceRuleType> secondaryOrderPrecedenceRules = new ArrayList<SecondaryOrderPrecedenceRuleType>();
		secondaryOrderPrecedenceRules.add(SecondaryOrderPrecedenceRuleType.TimePrecedence);
		secondaryOrderPrecedenceRules.add(SecondaryOrderPrecedenceRuleType.DisplayPrecedence);
		secondaryOrderPrecedenceRules.add(SecondaryOrderPrecedenceRuleType.UnrestrictedOrderPrecedence);
		marketModel.setSecondaryOrderPrecedenceRules(
				secondaryOrderPrecedenceRules);
		
		OrderBookImpl book = new OrderBookImpl(bookModel, mkt, datastore, datastore);
		
		Date now = new Date();
		
		Order limitOrder1 = new Order();
		limitOrder1.setTargetMarketCode(MockDatastores.MARKET_ID);
		limitOrder1.setType(OrderType.Limit);			
		limitOrder1.setLimitQuoteValue(wrapQuote(100));
		limitOrder1.setSubmissionDate(now);
		limitOrder1.setKey(MockIdentityGenerator.getId());
		limitOrder1.setSize(1);
		datastore.createOrder(limitOrder1);
		
		Order limitOrder1a = new Order();
		limitOrder1a.setTargetMarketCode(MockDatastores.MARKET_ID);
		limitOrder1a.setType(OrderType.Limit);			
		limitOrder1a.setLimitQuoteValue(wrapQuote(100));
		limitOrder1a.setSubmissionDate(now);
		limitOrder1a.setKey(MockIdentityGenerator.getId());
		limitOrder1a.setExecuteEntireOrderAtOnce(true);
		limitOrder1a.setSize(2);
		datastore.createOrder(limitOrder1a);
		
		Order limitOrder1b = new Order();
		limitOrder1b.setTargetMarketCode(MockDatastores.MARKET_ID);
		limitOrder1b.setType(OrderType.Limit);			
		limitOrder1b.setLimitQuoteValue(wrapQuote(100));
		limitOrder1b.setSubmissionDate(now);
		limitOrder1b.setDisplayOrder(false);
		limitOrder1b.setKey(MockIdentityGenerator.getId());
		limitOrder1b.setSize(3);
		datastore.createOrder(limitOrder1b);
		
		// We need at least 10 millis time delay for the Time Precedence to work
		Thread.sleep(10);	
		
		Order limitOrder2= new Order();
		limitOrder2.setSide(OrderSide.Buy);
		limitOrder2.setTargetMarketCode(MockDatastores.MARKET_ID);
		limitOrder2.setType(OrderType.Limit);			
		limitOrder2.setLimitQuoteValue(wrapQuote(100));
		limitOrder2.setSubmissionDate(new Date());
		limitOrder2.setKey(MockIdentityGenerator.getId());
		limitOrder2.setSize(4);
		datastore.createOrder(limitOrder2);
		
		MarketDataSession marketDataSession = new MockMarketDataSessionImpl(null); 
		OrderManagementContext orderManagementContext = new OrderManagementContextImpl(marketDataSession, null, null); 
		
		book.addOrder(wrapOrder(limitOrder2, datastore), orderManagementContext);
		book.addOrder(wrapOrder(limitOrder1b, datastore), orderManagementContext);
		book.addOrder(wrapOrder(limitOrder1a, datastore), orderManagementContext);
		book.addOrder(wrapOrder(limitOrder1, datastore), orderManagementContext);
		
		assertEqualsOrder(getOrder(0, 0, bookModel, datastore), limitOrder1);		
		assertEqualsOrder(getOrder(0, 1, bookModel, datastore), limitOrder1a);
		assertEqualsOrder(getOrder(0, 2, bookModel, datastore), limitOrder1b);
		assertEqualsOrder(getOrder(0, 3, bookModel, datastore), limitOrder2);
		
		MarketDataChange[] marketDataChanges = ((MockMarketDataSessionImpl) marketDataSession).getMarketDataChanges();
		
		// check level ii market data 
		assertEquals(4, marketDataChanges.length);
		
		checkLevelIIMktData(MockDatastores.MARKET_ID, marketDataChanges[0], MarketDataType.LevelIIBid, 100, true, 4, false);
		// hidden order
		checkLevelIIMktData(MockDatastores.MARKET_ID, marketDataChanges[1], MarketDataType.LevelIIBid, 100, true, 4, true);
		checkLevelIIMktData(MockDatastores.MARKET_ID, marketDataChanges[2], MarketDataType.LevelIIBid, 100, true, 6, true);
		checkLevelIIMktData(MockDatastores.MARKET_ID, marketDataChanges[3], MarketDataType.LevelIIBid, 100, true, 7, true);
	}

	private void checkLevelIIMktData(String marketCode_,
			MarketDataChange marketDataChange_, MarketDataType marketDataType_, double quote_, boolean validQuote_, int size_, Boolean hasHiddenOrders_) { 
		assertEquals(marketCode_, marketDataChange_.getMarketCode());
		assertEquals(validQuote_, marketDataChange_.getNewQuoteValid());
		assertEquals(validQuote_, marketDataChange_.getNewQuote().getValidQuote());
		assertEquals(quote_, marketDataChange_.getNewQuoteAndSize().getQuote().getQuoteValue());
		assertEquals(size_, marketDataChange_.getNewQuoteAndSize().getSize());
		assertEquals(marketDataType_, marketDataChange_.getChangeType());
		assertEquals(hasHiddenOrders_, marketDataChange_.getHasHiddenOrders());
	}
	
	public void testBuySellBook() throws Exception {
		buySellBookTest(QuoteType.Price);
		buySellBookTest(QuoteType.Yield);
	}

	private void buySellBookTest(QuoteType quoteType_) throws InterruptedException {
		// Data Source to Store the Actual Orders 
		MockDatastores datastore = new MockDatastores();
		
		
		OrderBookImpl sellBook = createBook(OrderSide.Sell, quoteType_, datastore);
		OrderBookImpl buyBook = createBook(OrderSide.Buy, quoteType_, datastore);
		
		Market marketModel = getMarket(datastore);
		
		TestOrderFlowGenerator gen = new TestOrderFlowGenerator(quoteType_, marketModel);
		submitOrders(gen.getOrders(), sellBook, buyBook, datastore);
		
		assertEqualsOrders(gen.getExpectedBuyOrders(), bookToOrders(buyBook._orderBookModel, datastore));
		assertEqualsOrders(gen.getExpectedSellOrders(), bookToOrders(sellBook._orderBookModel, datastore));
	}

	
	private void submitOrders(Order[] orders_, OrderBookImpl sellBook_, OrderBookImpl buyBook_, MockDatastores datastore_) {
		
		
		for (int i = 0; i < orders_.length; i++) {
			if(datastore_.findOrderBy(orders_[i].getKey()) == null) {
				datastore_.createOrder(orders_[i]);
			}
			submitOrder(orders_[i], sellBook_, buyBook_, datastore_);
		}
	}
	
	private void submitOrder(Order order_, OrderBookImpl sellBook_, OrderBookImpl buyBook_, MockDatastores datastore_) {		
		OrderManagementContext orderManagementContext = createOrderManagerContext();
		
		OrderInternal orderImpl = new OrderImpl(order_, datastore_);		
		if(orderImpl.getSide() == OrderSide.Buy) {
			buyBook_.recordSubmit(orderImpl, orderManagementContext);
		} else {
			sellBook_.recordSubmit(orderImpl, orderManagementContext);	
		}
	}
	
	private OrderBookImpl createBook(OrderSide side_, final QuoteType quoteType_, MockDatastores datastore_) {
		OrderBookModel bookModel = OrderBookFactory.createBook(side_);
		
		return new OrderBookImpl(bookModel, new MockMarketBase() {
			@Override
			public QuoteType getQuoteType() {
				return quoteType_;
			}

			@Override
			public SecondaryOrderPrecedenceRuleType[] getSecondaryOrderPrecedenceRules() {
				return new SecondaryOrderPrecedenceRuleType[] { SecondaryOrderPrecedenceRuleType.TimePrecedence };
			}
		}, datastore_, datastore_);
	}
	
	private Order getOrder(int entryIndex_, int orderIndex_, OrderBookModel bookModel_, OrderDatastore orderDatastore_) {
		OrderBookEntryModel entry = (OrderBookEntryModel) bookModel_.get(entryIndex_);
		
		
		return orderDatastore_.findOrderBy(entry.getOrderKey(orderIndex_)).getOrderModel();
	}
}
