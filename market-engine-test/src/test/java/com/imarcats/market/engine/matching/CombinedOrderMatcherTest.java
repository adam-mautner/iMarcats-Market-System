package com.imarcats.market.engine.matching;

import com.imarcats.internal.server.interfaces.order.OrderManagementContext;
import com.imarcats.market.engine.market.MarketImpl;
import com.imarcats.market.engine.order.OrderImpl;
import com.imarcats.market.engine.testutils.MockDatastores;
import com.imarcats.market.engine.testutils.OrderCompareTestCaseBase;
import com.imarcats.model.Market;
import com.imarcats.model.MatchedTrade;
import com.imarcats.model.Order;
import com.imarcats.model.test.testutils.TestOrderFlowGenerator;
import com.imarcats.model.types.MarketState;
import com.imarcats.model.types.QuoteType;

public class CombinedOrderMatcherTest extends OrderCompareTestCaseBase {

	
	public void testClosedMarketTest() throws Exception {
		closedMarketTest(QuoteType.Price);
		closedMarketTest(QuoteType.Yield);
	}
	
	private void closedMarketTest(QuoteType quoteType_) throws InterruptedException {
		
		
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
			MatchedTrade[] matchedTrades = CombinedOrderMatcher.INSTANCE.matchOnSubmit(new OrderImpl(order, datastore), market, orderManagementContext);			
			assertEquals(0, matchedTrades.length);
		}
		
		assertEqualsOrders(gen.getExpectedBuyOrders(), bookToOrders(marketModel.getBuyBook(), datastore));
		assertEqualsOrders(gen.getExpectedSellOrders(), bookToOrders(marketModel.getSellBook(), datastore));
	}
	
	public void testOrderMatchingWhenMarketIsOpen() throws Exception {
		orderMatchingWhenMarketIsOpen(QuoteType.Price);
		orderMatchingWhenMarketIsOpen(QuoteType.Yield);
	}
	
	private void orderMatchingWhenMarketIsOpen(QuoteType quoteType_) throws InterruptedException {
		
		
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
		
		MatchedTrade[] matchedTrades = CombinedOrderMatcher.INSTANCE.matchOnSubmit(new OrderImpl(orders[0], datastore), market, orderManagementContext);
		
		assertEquals(0, matchedTrades.length);
		
		checkQuoteAndSize(market.getBestBidSystem(), getExpectedQuote(20.0, quoteType_), 3, true);
		assertEquals(null, market.getBestAskSystem());
		
		matchedTrades = CombinedOrderMatcher.INSTANCE.matchOnSubmit(new OrderImpl(orders[1], datastore), market, orderManagementContext);
		
		assertEquals(0, matchedTrades.length);

		checkQuoteAndSize(market.getBestBidSystem(), getExpectedQuote(20.0, quoteType_), 3, true);
		checkQuoteAndSize(market.getBestAskSystem(), getExpectedQuote(20.1, quoteType_), 2, true);
		
		matchedTrades = CombinedOrderMatcher.INSTANCE.matchOnSubmit(new OrderImpl(orders[2], datastore), market, orderManagementContext);
		
		assertEquals(0, matchedTrades.length);

		checkQuoteAndSize(market.getBestBidSystem(), getExpectedQuote(20.0, quoteType_), 5, true);
		checkQuoteAndSize(market.getBestAskSystem(), getExpectedQuote(20.1, quoteType_), 2, true);	
		
		matchedTrades = CombinedOrderMatcher.INSTANCE.matchOnSubmit(new OrderImpl(orders[3], datastore), market, orderManagementContext);
		
		assertEquals(1, matchedTrades.length);
		checkPair(matchedTrades[0], 1, "Bea", "Sol");

		checkQuoteAndSize(market.getBestBidSystem(), getExpectedQuote(20.0, quoteType_), 4, true);
		checkQuoteAndSize(market.getBestAskSystem(), getExpectedQuote(20.1, quoteType_), 2, true);	
		
		matchedTrades = CombinedOrderMatcher.INSTANCE.matchOnSubmit(new OrderImpl(orders[4], datastore), market, orderManagementContext);
		
		assertEquals(0, matchedTrades.length);

		checkQuoteAndSize(market.getBestBidSystem(), getExpectedQuote(20.0, quoteType_), 4, true);
		checkQuoteAndSize(market.getBestAskSystem(), getExpectedQuote(20.1, quoteType_), 2, true);	
		
		matchedTrades = CombinedOrderMatcher.INSTANCE.matchOnSubmit(new OrderImpl(orders[5], datastore), market, orderManagementContext);
		
		assertEquals(2, matchedTrades.length);
		checkPair(matchedTrades[0], 2, "Bif", "Sam");
		checkPair(matchedTrades[1], 2, "Bif", "Stu");

	}
	
	public void testOrderMatchingOnOpen() throws Exception {
		orderMatchingOnOpenTest(QuoteType.Price, true);
		orderMatchingOnOpenTest(QuoteType.Yield, true);
		orderMatchingOnOpenTest(QuoteType.Price, false);
		orderMatchingOnOpenTest(QuoteType.Yield, false);
	}

	private void orderMatchingOnOpenTest(QuoteType quoteType_, boolean reopen_) throws InterruptedException {
		
		
		OrderManagementContext orderManagementContext = createOrderManagerContext();
		
		MockDatastores datastore = new MockDatastores();
		Market marketModel = getMarket(datastore);
		
		if(reopen_) {
			marketModel.setState(MarketState.ReOpening);			
		} else {
			marketModel.setState(MarketState.Opening);
		}
		TestOrderFlowGenerator gen = new TestOrderFlowGenerator(quoteType_, marketModel);
		Order[] orders = gen.getOrders();
		
		// create order to place it to the data source
		for (Order order : orders) {
			datastore.createOrder(order);
		}
		
		marketModel.setQuoteType(quoteType_);
		MarketImpl market = new MarketImpl(marketModel, null, datastore, datastore, datastore, datastore);
		
		for (Order order : orders) {
			CombinedOrderMatcher.INSTANCE.matchOnSubmit(new OrderImpl(order, datastore), market, orderManagementContext);
		}
		
		MatchedTrade[] matchedPairs = null;
		if(reopen_) {
			matchedPairs = CombinedOrderMatcher.INSTANCE.matchOnMarketReOpen(market, orderManagementContext);
		} else {
			matchedPairs = CombinedOrderMatcher.INSTANCE.matchOnMarketOpen(market, orderManagementContext);
		}
		
		assertEquals(4, matchedPairs.length);
		
		checkPair(matchedPairs[0], 1, "Bif", "Sol");		
		checkPair(matchedPairs[1], 3, "Bif", "Sue");		
		checkPair(matchedPairs[2], 2, "Bob", "Sue");	
		checkPair(matchedPairs[3], 1, "Bea", "Sue");
	}
	
}
