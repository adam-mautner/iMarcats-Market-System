package com.imarcats.market.engine.matching;

import com.imarcats.interfaces.client.v100.exception.ExceptionLanguageKeys;
import com.imarcats.interfaces.client.v100.exception.MarketRuntimeException;
import com.imarcats.internal.server.interfaces.order.OrderInternal;
import com.imarcats.internal.server.interfaces.order.OrderManagementContext;
import com.imarcats.market.engine.market.MarketImpl;
import com.imarcats.market.engine.order.OrderImpl;
import com.imarcats.market.engine.testutils.MockDatastores;
import com.imarcats.market.engine.testutils.OrderCompareTestCaseBase;
import com.imarcats.model.OrderBookModel;
import com.imarcats.model.Market;
import com.imarcats.model.MatchedTrade;
import com.imarcats.model.Order;
import com.imarcats.model.test.testutils.TestOrderFlowGenerator;
import com.imarcats.model.types.ActivationStatus;
import com.imarcats.model.types.MarketState;
import com.imarcats.model.types.OrderExpirationInstruction;
import com.imarcats.model.types.OrderSide;
import com.imarcats.model.types.OrderType;
import com.imarcats.model.types.QuoteType;

/**
 * Test single price auction 
 * 
 * See Order Matching and Order Management � Business Requirement Specification document
 * Order Matching and Trade Pricing section
 * @author Adam
 *
 */
public class SinglePriceAuctionTest extends OrderCompareTestCaseBase {
	
	public void testOrderMatching() throws Exception {
		orderMatchTest(QuoteType.Price, true);
		orderMatchTest(QuoteType.Yield, true);

		orderMatchTest(QuoteType.Price, false);
		orderMatchTest(QuoteType.Yield, false);
	}

	private void orderMatchTest(QuoteType quoteType_, boolean displayOrder_) throws InterruptedException {
				
		MockDatastores datastore = new MockDatastores();
		Market marketModel = getMarket(datastore);
		
		TestOrderFlowGenerator gen = new TestOrderFlowGenerator(quoteType_, marketModel);
		Order[] orders = gen.getOrders();
		
		// set orders to be hidden, to make sure matching works for hidden orders 
		orders[0].setDisplayOrder(displayOrder_);
		orders[1].setDisplayOrder(displayOrder_);
		
		// create order to place it to the data source
		for (Order order : orders) {
			datastore.createOrder(order);
		}
		
		marketModel.setQuoteType(quoteType_);
		MarketImpl market = new MarketImpl(marketModel, null, datastore, datastore, datastore, datastore);
		
		OrderManagementContext orderManagementContext = createOrderManagerContext();
		
		for (Order order : orders) {
			MatchedTrade[] matchOnSubmit = SinglePriceAuction.INSTANCE.matchOnSubmit(new OrderImpl(order, datastore), market, orderManagementContext);
			assertEquals(0, matchOnSubmit.length);
		}
		
		MatchedTrade[] matchedPairs = SinglePriceAuction.INSTANCE.matchOnMarketCall(market, orderManagementContext);
	
		assertEquals(4, matchedPairs.length);
		
		checkPair(matchedPairs[0], 1, "Bif", "Sol");		
		checkPair(matchedPairs[1], 3, "Bif", "Sue");		
		checkPair(matchedPairs[2], 2, "Bob", "Sue");	
		checkPair(matchedPairs[3], 1, "Bea", "Sue");
		
		OrderBookModel buyBook = marketModel.getBuyBook();
		OrderBookModel sellBook = marketModel.getSellBook();

		checkEntry(buyBook, 0, 0, "Bea", 2, getExpectedQuote(20.0, quoteType_), OrderSide.Buy, datastore);
		checkEntry(buyBook, 0, 1, "Ben", 2, getExpectedQuote(20.0, quoteType_), OrderSide.Buy, datastore);		
		checkEntry(buyBook, 1, 0, "Bud", 7, getExpectedQuote(19.8, quoteType_), OrderSide.Buy, datastore);
		
		checkEntry(sellBook, 0, 0, "Sam", 2, getExpectedQuote(20.1, quoteType_), OrderSide.Sell, datastore);
		checkEntry(sellBook, 1, 0, "Stu", 5, getExpectedQuote(20.2, quoteType_), OrderSide.Sell, datastore);
	}
	
	public void testEmptyBookMatch() throws Exception {
		emptyBookMatchTest(QuoteType.Price);
		emptyBookMatchTest(QuoteType.Yield);
	}

	private void emptyBookMatchTest(QuoteType quoteType_) {
				
		MockDatastores datastore = new MockDatastores();
		Market marketModel = getMarket(datastore);
		
		marketModel.setQuoteType(quoteType_);
		MarketImpl market = new MarketImpl(marketModel, null, datastore, datastore, datastore, datastore);
		
		OrderManagementContext orderManagementContext = createOrderManagerContext();
		
		MatchedTrade[] matchedPairs = SinglePriceAuction.INSTANCE.matchOnMarketCall(market, orderManagementContext);
	
		assertEquals(0, matchedPairs.length);
	}
	
	public void testInbalancedBookMatch() throws Exception {
		inbalancedBookMatchTest(QuoteType.Price);
		inbalancedBookMatchTest(QuoteType.Yield);
	}

	private void inbalancedBookMatchTest(QuoteType quoteType_) throws InterruptedException {
				
		MockDatastores datastore = new MockDatastores();
		Market marketModel = getMarket(datastore);
		
		TestOrderFlowGenerator gen = new TestOrderFlowGenerator(quoteType_, marketModel);
		Order[] orders = gen.getOrders();
		
		// create order to place it to the data source
		for (Order order : orders) {
			datastore.createOrder(order);
		}
		
		marketModel.setQuoteType(quoteType_);
		MarketImpl market = new MarketImpl(marketModel, null, datastore, datastore, datastore, datastore);
		
		OrderManagementContext orderManagementContext = createOrderManagerContext();
		
		for (Order order : orders) {
			if(order.getSide() == OrderSide.Buy) {
				SinglePriceAuction.INSTANCE.matchOnSubmit(new OrderImpl(order, datastore), market, orderManagementContext);
			}
		}
		
		MatchedTrade[] matchedPairs = SinglePriceAuction.INSTANCE.matchOnMarketCall(market, orderManagementContext);
	
		assertEquals(0, matchedPairs.length);
		
		assertEquals(0, marketModel.getSellBook().getOrderBookEntries().size());
		
		Order[] buyOrders = bookToOrders(marketModel.getBuyBook(), datastore);
		
		assertEquals(gen.getExpectedBuyOrders().length, buyOrders.length);
		
		for (int i = 0; i < buyOrders.length; i++) {
			assertEqualsOrder(gen.getExpectedBuyOrders()[i], buyOrders[i]);
		}
	}
	
	public void testMarketOrderMatch() throws Exception {
		marketOrderMatchTest(QuoteType.Price);
		marketOrderMatchTest(QuoteType.Yield);
	}

	private void marketOrderMatchTest(QuoteType quoteType_) throws InterruptedException {
				
		MockDatastores datastore = new MockDatastores();
		Market marketModel = getMarket(datastore);
		
		TestOrderFlowGenerator gen = new TestOrderFlowGenerator(quoteType_, marketModel);
		Order[] orders = gen.getOrders();
		
		// create order to place it to the data source
		for (Order order : orders) {
			datastore.createOrder(order);
		}
		
		marketModel.setQuoteType(quoteType_);
		MarketImpl market = new MarketImpl(marketModel, null, datastore, datastore, datastore, datastore);
		
		orders[0].setType(OrderType.Market);
		orders[1].setType(OrderType.Market);
		
		OrderManagementContext orderManagementContext = createOrderManagerContext();
		
		// This should NEVER really the case, Market should NOT allow submission of 
		// Market Orders to an empty book
		SinglePriceAuction.INSTANCE.matchOnSubmit(new OrderImpl(orders[0], datastore), market, orderManagementContext);
		SinglePriceAuction.INSTANCE.matchOnSubmit(new OrderImpl(orders[1], datastore), market, orderManagementContext);
		
		MatchedTrade[] matchedPairs = SinglePriceAuction.INSTANCE.matchOnMarketCall(market, orderManagementContext);
	
		// This Matching does not make sense, because it cannot be priced
		assertEquals(1, matchedPairs.length);

		assertEquals((Integer)2, matchedPairs[0].getMatchedSize());
		
		assertEquals(0, marketModel.getSellBook().getOrderBookEntries().size());
		assertEquals(1, marketModel.getBuyBook().getOrderBookEntries().size());
		
		Order[] buyOrders = bookToOrders(marketModel.getBuyBook(), datastore);
		// check the remaining size 
		OrderInternal buyOrder0 = new OrderImpl(buyOrders[0], datastore);
		assertEquals(1, buyOrder0.getRemainingSize());
	}
	
	public void testFillEntireOrderAtOnce() throws Exception {
		fillEntireOrderAtOnceTest(QuoteType.Price);
		fillEntireOrderAtOnceTest(QuoteType.Yield);
	}
	
	private void fillEntireOrderAtOnceTest(QuoteType quoteType_) throws InterruptedException {
		
		
		MockDatastores datastore = new MockDatastores();
		Market marketModel = getMarket(datastore);
		
		TestOrderFlowGenerator gen = new TestOrderFlowGenerator(quoteType_, marketModel);
		Order[] orders = gen.getOrders();

		// create order to place it to the data source
		for (Order order : orders) {
			datastore.createOrder(order);
		}
		
		orders[0].setExecuteEntireOrderAtOnce(true); // Bob
		orders[6].setExecuteEntireOrderAtOnce(true); // Bea
		
		marketModel.setQuoteType(quoteType_);
		MarketImpl market = new MarketImpl(marketModel, null, datastore, datastore, datastore, datastore);
		
		OrderManagementContext orderManagementContext = createOrderManagerContext();
		
		try {
			for (Order order : orders) {
				SinglePriceAuction.INSTANCE.matchOnSubmit(new OrderImpl(order, datastore), market, orderManagementContext);
			}
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.ENTIRE_ORDER_AT_ONCE_NOT_SUPPORTED_ON_CALL_MARKET, e.getLanguageKey());
		}
	}
	
	public void testMinimumExecutionSize() throws Exception {
		minimumExecutionSizeTest(QuoteType.Price);
		minimumExecutionSizeTest(QuoteType.Yield);
	}
	
	private void minimumExecutionSizeTest(QuoteType quoteType_) throws InterruptedException {
		
		
		MockDatastores datastore = new MockDatastores();
		Market marketModel = getMarket(datastore);
		
		TestOrderFlowGenerator gen = new TestOrderFlowGenerator(quoteType_, marketModel);
		Order[] orders = gen.getOrders();

		// create order to place it to the data source
		for (Order order : orders) {
			datastore.createOrder(order);
		}
		
		orders[5].setMinimumSizeOfExecution(1); // Bif 
		orders[7].setMinimumSizeOfExecution(3); // Sue
		
		marketModel.setQuoteType(quoteType_);
		MarketImpl market = new MarketImpl(marketModel, null, datastore, datastore, datastore, datastore);
		
		OrderManagementContext orderManagementContext = createOrderManagerContext();
		try{
			for (Order order : orders) {
				SinglePriceAuction.INSTANCE.matchOnSubmit(new OrderImpl(order, datastore), market, orderManagementContext);
			}
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.MINIMUM_EXECUTION_SIZE_NOT_SUPPORTED_ON_CALL_MARKET, e.getLanguageKey());
		}
	}
	
	public void testMarketOrdersCannotBeSubmitted() throws Exception {
		
		
		MockDatastores datastore = new MockDatastores();
		Market marketModel = getMarket(datastore);
		
		TestOrderFlowGenerator gen = new TestOrderFlowGenerator(QuoteType.Price, marketModel);
		Order[] orders = gen.getOrders();

		// create order to place it to the data source
		for (Order order : orders) {
			datastore.createOrder(order);
		}

		// set orders to market orders
		orders[0].setSize(10);
		orders[0].setType(OrderType.Market);
		orders[0].setSide(OrderSide.Sell);
		
		orders[1].setSize(10);
		orders[1].setType(OrderType.Market);
		orders[1].setSide(OrderSide.Buy);
		
		marketModel.setQuoteType(QuoteType.Price);
		MarketImpl market = new MarketImpl(marketModel, null, datastore, datastore, datastore, datastore);
		market.getMarketModel().setActivationStatus(ActivationStatus.Activated);
		market.getMarketModel().setState(MarketState.Open);
		
		OrderManagementContext orderManagementContext = createOrderManagerContext();
		
		SinglePriceAuction.INSTANCE.matchOnSubmit(new OrderImpl(orders[1], datastore), market, orderManagementContext);
		
		try{
			SinglePriceAuction.INSTANCE.matchOnSubmit(new OrderImpl(orders[0], datastore), market, orderManagementContext);
			fail();
		} catch (MarketRuntimeException e) {
			// expected
			assertEquals(ExceptionLanguageKeys.MARKET_ORDER_SUBMITTED_CANNOT_BE_FILLED_ON_CALL_MARKET, e.getLanguageKey());
		}
	}
	
	public void testImmediateOrCancelOrders() throws Exception {
		
		
		MockDatastores datastore = new MockDatastores();
		Market marketModel = getMarket(datastore);
		
		TestOrderFlowGenerator gen = new TestOrderFlowGenerator(QuoteType.Price, marketModel);
		Order[] orders = gen.getOrders();

		// create order to place it to the data source
		for (Order order : orders) {
			datastore.createOrder(order);
		}
		
		orders[0].setExpirationInstruction(OrderExpirationInstruction.ImmediateOrCancel);
		
		marketModel.setQuoteType(QuoteType.Price);
		MarketImpl market = new MarketImpl(marketModel, null, datastore, datastore, datastore, datastore);
		
		OrderManagementContext orderManagementContext = createOrderManagerContext();
		
		try{
			SinglePriceAuction.INSTANCE.matchOnSubmit(new OrderImpl(orders[0], datastore), market, orderManagementContext);
			fail();
		} catch (MarketRuntimeException e) {
			// expected
			assertEquals(ExceptionLanguageKeys.IMMEDIATE_OR_CANCEL_ORDER_IS_NOT_SUPPORTED_ON_CALL_MARKET, e.getLanguageKey());
		}
	}
}
