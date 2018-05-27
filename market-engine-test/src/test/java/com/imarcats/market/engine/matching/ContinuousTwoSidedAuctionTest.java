package com.imarcats.market.engine.matching;

import com.imarcats.interfaces.client.v100.exception.ExceptionLanguageKeys;
import com.imarcats.interfaces.client.v100.exception.MarketRuntimeException;
import com.imarcats.interfaces.client.v100.i18n.MarketSystemMessageLanguageKeys;
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
import com.imarcats.model.types.MarketState;
import com.imarcats.model.types.OrderExpirationInstruction;
import com.imarcats.model.types.OrderSide;
import com.imarcats.model.types.OrderState;
import com.imarcats.model.types.OrderType;
import com.imarcats.model.types.QuoteType;

/**
 * Test continuous two-sided auction 
 * 
 * See Order Matching and Order Management � Business Requirement Specification document
 * Order Matching and Trade Pricing section
 * @author Adam
 *
 */
public class ContinuousTwoSidedAuctionTest extends OrderCompareTestCaseBase {

	
	public void testOpen() throws Exception {
		openTest(QuoteType.Price);
		openTest(QuoteType.Yield);
	}
	
	private void openTest(QuoteType quoteType_) throws InterruptedException {
		
		
		MockDatastores datastore = new MockDatastores();
		Market marketModel = getMarket(datastore);
		
		marketModel.setState(MarketState.Closed);
		
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
			MatchedTrade[] matchedTrades = ContinuousTwoSidedAuction.INSTANCE.matchOnSubmit(new OrderImpl(order, datastore), market, orderManagementContext);			
			assertEquals(0, matchedTrades.length);
		}
		
		assertEqualsOrders(gen.getExpectedBuyOrders(), bookToOrders(marketModel.getBuyBook(), datastore));
		assertEqualsOrders(gen.getExpectedSellOrders(), bookToOrders(marketModel.getSellBook(), datastore));
		
		MatchedTrade[] matchOnMarketOpen = ContinuousTwoSidedAuction.INSTANCE.matchOnMarketOpen(market, orderManagementContext);
		
		assertEquals(4, matchOnMarketOpen.length);
		
		checkPair(matchOnMarketOpen[0], 1, "Bif", "Sol");		
		checkPair(matchOnMarketOpen[1], 3, "Bif", "Sue");		
		checkPair(matchOnMarketOpen[2], 2, "Bob", "Sue");	
		checkPair(matchOnMarketOpen[3], 1, "Bea", "Sue");
	}
	
	public void testOrderMatching() throws Exception {
		orderMatchingTest(QuoteType.Price, true);
		orderMatchingTest(QuoteType.Yield, true);
		
		// matching using hidden orders
		orderMatchingTest(QuoteType.Price, false);
		orderMatchingTest(QuoteType.Yield, false);
	}
	
	private void orderMatchingTest(QuoteType quoteType_, boolean displayOrder_) throws InterruptedException {
		
		
		MockDatastores datastore = new MockDatastores();
		Market marketModel = getMarket(datastore);
		
		marketModel.setState(MarketState.Open);
		
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
		
		// 1.  
		MatchedTrade[] matchedTrades = ContinuousTwoSidedAuction.INSTANCE.matchOnSubmit(new OrderImpl(orders[0], datastore), market, orderManagementContext);
		
		assertEquals(0, matchedTrades.length);
		
		checkQuoteAndSize(market.getBestBidSystem(), getExpectedQuote(20.0, quoteType_), 3, true);
		if(!displayOrder_) {
			checkQuoteAndSize(market.getBestBid(), getExpectedQuote(20.0, quoteType_), 0, true);
		}
		assertEquals(null, market.getBestAskSystem());
		
		// 2.  
		matchedTrades = ContinuousTwoSidedAuction.INSTANCE.matchOnSubmit(new OrderImpl(orders[1], datastore), market, orderManagementContext);
		
		assertEquals(0, matchedTrades.length);

		checkQuoteAndSize(market.getBestBidSystem(), getExpectedQuote(20.0, quoteType_), 3, true);
		checkQuoteAndSize(market.getBestAskSystem(), getExpectedQuote(20.1, quoteType_), 2, true);
		if(!displayOrder_) {
			checkQuoteAndSize(market.getBestAsk(), getExpectedQuote(20.1, quoteType_), 0, true);
		}
		
		// 3.  
		matchedTrades = ContinuousTwoSidedAuction.INSTANCE.matchOnSubmit(new OrderImpl(orders[2], datastore), market, orderManagementContext);
		
		assertEquals(0, matchedTrades.length);

		checkQuoteAndSize(market.getBestBidSystem(), getExpectedQuote(20.0, quoteType_), 5, true);
		if(!displayOrder_) {
			checkQuoteAndSize(market.getBestBid(), getExpectedQuote(20.0, quoteType_), 2, true);
		}
		checkQuoteAndSize(market.getBestAskSystem(), getExpectedQuote(20.1, quoteType_), 2, true);	
		
		// 4.  
		matchedTrades = ContinuousTwoSidedAuction.INSTANCE.matchOnSubmit(new OrderImpl(orders[3], datastore), market, orderManagementContext);
		
		assertEquals(1, matchedTrades.length);
		checkPair(matchedTrades[0], 1, "Bea", "Sol");

		checkQuoteAndSize(market.getBestBidSystem(), getExpectedQuote(20.0, quoteType_), 4, true);
		checkQuoteAndSize(market.getBestAskSystem(), getExpectedQuote(20.1, quoteType_), 2, true);	
		
		// 5.  
		matchedTrades = ContinuousTwoSidedAuction.INSTANCE.matchOnSubmit(new OrderImpl(orders[4], datastore), market, orderManagementContext);
		
		assertEquals(0, matchedTrades.length);

		checkQuoteAndSize(market.getBestBidSystem(), getExpectedQuote(20.0, quoteType_), 4, true);
		checkQuoteAndSize(market.getBestAskSystem(), getExpectedQuote(20.1, quoteType_), 2, true);	
		
		// 6.  
		matchedTrades = ContinuousTwoSidedAuction.INSTANCE.matchOnSubmit(new OrderImpl(orders[5], datastore), market, orderManagementContext);
		
		assertEquals(2, matchedTrades.length);
		checkPair(matchedTrades[0], 2, "Bif", "Sam");
		checkPair(matchedTrades[1], 2, "Bif", "Stu");
		
		checkQuoteAndSize(market.getBestBidSystem(), getExpectedQuote(20.0, quoteType_), 4, true);
		checkQuoteAndSize(market.getBestAskSystem(), getExpectedQuote(20.2, quoteType_), 3, true);	
		
		// 7.  
		matchedTrades = ContinuousTwoSidedAuction.INSTANCE.matchOnSubmit(new OrderImpl(orders[6], datastore), market, orderManagementContext);
		
		assertEquals(0, matchedTrades.length);
		
		checkQuoteAndSize(market.getBestBidSystem(), getExpectedQuote(20.1, quoteType_), 2, true);
		checkQuoteAndSize(market.getBestAskSystem(), getExpectedQuote(20.2, quoteType_), 3, true);	
		
		// 8.  
		matchedTrades = ContinuousTwoSidedAuction.INSTANCE.matchOnSubmit(new OrderImpl(orders[7], datastore), market, orderManagementContext);
		
		assertEquals(3, matchedTrades.length);
		checkPair(matchedTrades[0], 2, "Bob", "Sue");
		checkPair(matchedTrades[1], 2, "Bea", "Sue");
		checkPair(matchedTrades[2], 2, "Ben", "Sue");
		
		assertEquals(null, market.getBestBidSystem());
		checkQuoteAndSize(market.getBestAskSystem(), getExpectedQuote(20.2, quoteType_), 3, true);	
		
		// 9.  
		matchedTrades = ContinuousTwoSidedAuction.INSTANCE.matchOnSubmit(new OrderImpl(orders[8], datastore), market, orderManagementContext);
		
		assertEquals(0, matchedTrades.length);
		
		checkQuoteAndSize(market.getBestBidSystem(), getExpectedQuote(19.8, quoteType_), 7, true);
		checkQuoteAndSize(market.getBestAskSystem(), getExpectedQuote(20.2, quoteType_), 3, true);
		
		OrderBookModel buyBook = marketModel.getBuyBook();
		OrderBookModel sellBook = marketModel.getSellBook();
		
		// check book at the end
		checkEntry(buyBook, 0, 0, "Bud", 7, getExpectedQuote(19.8, quoteType_), OrderSide.Buy, datastore);

		checkEntry(sellBook, 0, 0, "Stu", 3, getExpectedQuote(20.2, quoteType_), OrderSide.Sell, datastore);		
	}
	
	public void testInbalancedBookMatch() throws Exception {
		inbalancedBookMatchTest(QuoteType.Price);
		inbalancedBookMatchTest(QuoteType.Yield);
	}

	private void inbalancedBookMatchTest(QuoteType quoteType_) throws InterruptedException {
		
		
		MockDatastores datastore = new MockDatastores();
		Market marketModel = getMarket(datastore);
		
		marketModel.setState(MarketState.Open);
		
		TestOrderFlowGenerator gen = new TestOrderFlowGenerator(quoteType_, marketModel);
		Order[] orders = gen.getOrders();
		
		// create order to place it to the data source
		for (Order order : orders) {
			datastore.createOrder(order);
		}
		
		marketModel.setQuoteType(quoteType_);
		MarketImpl market = new MarketImpl(marketModel, null, datastore, datastore, datastore, datastore);
		
		OrderManagementContext orderManagementContext = createOrderManagerContext();
		
		MatchedTrade[] matchedPairs = null;
		for (Order order : orders) {
			if(order.getSide() == OrderSide.Buy) {
				matchedPairs = ContinuousTwoSidedAuction.INSTANCE.matchOnSubmit(new OrderImpl(order, datastore), market, orderManagementContext);
			}
		}
	
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
		
		marketModel.setState(MarketState.Open);
		
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
		MatchedTrade[] matchedPairs = ContinuousTwoSidedAuction.INSTANCE.matchOnSubmit(new OrderImpl(orders[0], datastore), market, orderManagementContext);
		assertEquals(0, matchedPairs.length);

		matchedPairs = ContinuousTwoSidedAuction.INSTANCE.matchOnSubmit(new OrderImpl(orders[1], datastore), market, orderManagementContext);
	
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
	
	public void testImmediateOrCancelOrders() throws Exception {
		immediateOrCancelOrdersTest(QuoteType.Price);
		immediateOrCancelOrdersTest(QuoteType.Yield);
	}
	
	private void immediateOrCancelOrdersTest(QuoteType quoteType_) throws InterruptedException {
		
		
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
		
		orders[0].setExpirationInstruction(OrderExpirationInstruction.ImmediateOrCancel);
		try {
			ContinuousTwoSidedAuction.INSTANCE.matchOnSubmit(new OrderImpl(orders[0], datastore), market, orderManagementContext);
			fail();
		} catch (MarketRuntimeException e) {
			// expected
			assertEquals(ExceptionLanguageKeys.IMMEDIATE_OR_CANCEL_ORDER_IS_NOT_SUPPORTED_WHEN_THE_MARKET_IS_NOT_OPEN, e.getLanguageKey());
		}
		orders[0].setExpirationInstruction(OrderExpirationInstruction.GoodTillCancel);
		
		marketModel.setState(MarketState.Open);
		
		MatchedTrade[] matchedTrades = ContinuousTwoSidedAuction.INSTANCE.matchOnSubmit(new OrderImpl(orders[0], datastore), market, orderManagementContext);
		
		assertEquals(0, matchedTrades.length);
		
		checkQuoteAndSize(market.getBestBidSystem(), getExpectedQuote(20.0, quoteType_), 3, true);
		assertEquals(null, market.getBestAskSystem());
		
		matchedTrades = ContinuousTwoSidedAuction.INSTANCE.matchOnSubmit(new OrderImpl(orders[1], datastore), market, orderManagementContext);
		
		assertEquals(0, matchedTrades.length);

		checkQuoteAndSize(market.getBestBidSystem(), getExpectedQuote(20.0, quoteType_), 3, true);
		checkQuoteAndSize(market.getBestAskSystem(), getExpectedQuote(20.1, quoteType_), 2, true);
		
		matchedTrades = ContinuousTwoSidedAuction.INSTANCE.matchOnSubmit(new OrderImpl(orders[2], datastore), market, orderManagementContext);
		
		assertEquals(0, matchedTrades.length);

		checkQuoteAndSize(market.getBestBidSystem(), getExpectedQuote(20.0, quoteType_), 5, true);
		checkQuoteAndSize(market.getBestAskSystem(), getExpectedQuote(20.1, quoteType_), 2, true);	
		
		// Set ImmediateOrCancel
		orders[3].setSize(7);
		orders[3].setExpirationInstruction(OrderExpirationInstruction.ImmediateOrCancel);
		assertEquals(OrderState.Created, orders[3].getState());
		matchedTrades = ContinuousTwoSidedAuction.INSTANCE.matchOnSubmit(new OrderImpl(orders[3], datastore), market, orderManagementContext);
		assertEquals(OrderState.Canceled, orders[3].getState());
		assertEquals(MarketSystemMessageLanguageKeys.ORDER_CANCELED_IT_WAS_IMMEDIATE_OR_CANCEL_ORDER, orders[3].getCancellationCommentLanguageKey());
		OrderInternal order3 = new OrderImpl(orders[3], datastore);
		assertEquals(2, order3.getRemainingSize());
		assertEquals(5, order3.getExecutedSize());
		
		assertEquals(2, matchedTrades.length);
		checkPair(matchedTrades[0], 3, "Bea", "Sol");
		checkPair(matchedTrades[1], 2, "Ben", "Sol");   

		assertEquals(null, market.getBestBidSystem());
		checkQuoteAndSize(market.getBestAskSystem(), getExpectedQuote(20.1, quoteType_), 2, true);
		
		OrderBookModel sellBook = marketModel.getSellBook();
		checkEntry(sellBook, 0, 0, "Sam", 2, getExpectedQuote(20.1, quoteType_), OrderSide.Sell, datastore);		
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
		
		orders[0].setExecuteEntireOrderAtOnce(true); // Bea
		

		orders[5].setExecuteEntireOrderAtOnce(true); // Bif - Mkt Order
		orders[5].setExpirationInstruction(OrderExpirationInstruction.ImmediateOrCancel); // Bif - Mkt Order
		
		orders[6].setExecuteEntireOrderAtOnce(true); // Bob
		
		marketModel.setQuoteType(quoteType_);
		MarketImpl market = new MarketImpl(marketModel, null, datastore, datastore, datastore, datastore);
		
		OrderManagementContext orderManagementContext = createOrderManagerContext();
		
		try {
			for (Order order : orders) {
				ContinuousTwoSidedAuction.INSTANCE.matchOnSubmit(new OrderImpl(order, datastore), market, orderManagementContext);
			}
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.ENTIRE_ORDER_AT_ONCE_NOT_SUPPORTED_WHEN_THE_MARKET_IS_NOT_OPEN, e.getLanguageKey());
		}
		
		market.getMarketModel().setState(MarketState.Open);
		
		checkOrderConstraints(quoteType_, datastore, orders, market,
				orderManagementContext);
	}

	private void checkOrderConstraints(QuoteType quoteType_,
			MockDatastores datastore, Order[] orders, MarketImpl market,
			OrderManagementContext orderManagementContext) {
		MatchedTrade[] matchedTrades = ContinuousTwoSidedAuction.INSTANCE.matchOnSubmit(new OrderImpl(orders[0], datastore), market, orderManagementContext);
		
		assertEquals(0, matchedTrades.length);
		
		checkQuoteAndSize(market.getBestBidSystem(), getExpectedQuote(20.0, quoteType_), 3, true);
		assertEquals(null, market.getBestAskSystem());
		
		matchedTrades = ContinuousTwoSidedAuction.INSTANCE.matchOnSubmit(new OrderImpl(orders[1], datastore), market, orderManagementContext);
		
		assertEquals(0, matchedTrades.length);

		checkQuoteAndSize(market.getBestBidSystem(), getExpectedQuote(20.0, quoteType_), 3, true);
		checkQuoteAndSize(market.getBestAskSystem(), getExpectedQuote(20.1, quoteType_), 2, true);
		
		matchedTrades = ContinuousTwoSidedAuction.INSTANCE.matchOnSubmit(new OrderImpl(orders[2], datastore), market, orderManagementContext);
		
		assertEquals(0, matchedTrades.length);

		checkQuoteAndSize(market.getBestBidSystem(), getExpectedQuote(20.0, quoteType_), 5, true);
		checkQuoteAndSize(market.getBestAskSystem(), getExpectedQuote(20.1, quoteType_), 2, true);	
		
		matchedTrades = ContinuousTwoSidedAuction.INSTANCE.matchOnSubmit(new OrderImpl(orders[3], datastore), market, orderManagementContext);
		
		assertEquals(0, matchedTrades.length);
		// This could not be matched because of Entire-Order-at-Once or Minimum Execution Size
		// checkPair(matchedTrades[0], 1, "Bea", "Sol");

		checkQuoteAndSize(market.getBestBidSystem(), getExpectedQuote(20.0, quoteType_), 5, true);
		checkQuoteAndSize(market.getBestAskSystem(), getExpectedQuote(19.8, quoteType_), 1, true);	
		
		matchedTrades = ContinuousTwoSidedAuction.INSTANCE.matchOnSubmit(new OrderImpl(orders[4], datastore), market, orderManagementContext);
		
		assertEquals(0, matchedTrades.length);

		checkQuoteAndSize(market.getBestBidSystem(), getExpectedQuote(20.0, quoteType_), 5, true);
		checkQuoteAndSize(market.getBestAskSystem(), getExpectedQuote(19.8, quoteType_), 1, true);	
		
		matchedTrades = ContinuousTwoSidedAuction.INSTANCE.matchOnSubmit(new OrderImpl(orders[5], datastore), market, orderManagementContext);
		
		assertEquals(3, matchedTrades.length);
		checkPair(matchedTrades[0], 1, "Bif", "Sol");
		checkPair(matchedTrades[1], 2, "Bif", "Sam");
		checkPair(matchedTrades[2], 1, "Bif", "Stu");
		
		checkQuoteAndSize(market.getBestBidSystem(), getExpectedQuote(20.0, quoteType_), 5, true);
		checkQuoteAndSize(market.getBestAskSystem(), getExpectedQuote(20.2, quoteType_), 4, true);	
		
		matchedTrades = ContinuousTwoSidedAuction.INSTANCE.matchOnSubmit(new OrderImpl(orders[6], datastore), market, orderManagementContext);
		
		assertEquals(0, matchedTrades.length);
		
		checkQuoteAndSize(market.getBestBidSystem(), getExpectedQuote(20.1, quoteType_), 2, true);
		checkQuoteAndSize(market.getBestAskSystem(), getExpectedQuote(20.2, quoteType_), 4, true);	
		
		matchedTrades = ContinuousTwoSidedAuction.INSTANCE.matchOnSubmit(new OrderImpl(orders[7], datastore), market, orderManagementContext);
		
		assertEquals(3, matchedTrades.length);
		checkPair(matchedTrades[0], 2, "Bob", "Sue");
		checkPair(matchedTrades[1], 3, "Bea", "Sue");
		checkPair(matchedTrades[2], 1, "Ben", "Sue");
		
		checkQuoteAndSize(market.getBestBidSystem(), getExpectedQuote(20.0, quoteType_), 1, true);	
		checkQuoteAndSize(market.getBestAskSystem(), getExpectedQuote(20.2, quoteType_), 4, true);	
		
		matchedTrades = ContinuousTwoSidedAuction.INSTANCE.matchOnSubmit(new OrderImpl(orders[8], datastore), market, orderManagementContext);
		
		assertEquals(0, matchedTrades.length);
		
		checkQuoteAndSize(market.getBestBidSystem(), getExpectedQuote(20.0, quoteType_), 1, true);
		checkQuoteAndSize(market.getBestAskSystem(), getExpectedQuote(20.2, quoteType_), 4, true);
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
		
		orders[0].setMinimumSizeOfExecution(2); // Bea
		

		orders[5].setExecuteEntireOrderAtOnce(true); // Bif - Mkt Order
		orders[5].setExpirationInstruction(OrderExpirationInstruction.ImmediateOrCancel); // Bif - Mkt Order
		
		marketModel.setQuoteType(quoteType_);
		MarketImpl market = new MarketImpl(marketModel, null, datastore, datastore, datastore, datastore);
		
		OrderManagementContext orderManagementContext = createOrderManagerContext();
		
		try {
			for (Order order : orders) {
				ContinuousTwoSidedAuction.INSTANCE.matchOnSubmit(new OrderImpl(order, datastore), market, orderManagementContext);
			}
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.MINIMUM_EXECUTION_SIZE_NOT_SUPPORTED_WHEN_THE_MARKET_IS_NOT_OPEN, e.getLanguageKey());
		}
		
		market.getMarketModel().setState(MarketState.Open);
		
		checkOrderConstraints(quoteType_, datastore, orders, market,
				orderManagementContext);
	}
}
