package com.imarcats.market.engine.pricing;

import com.imarcats.interfaces.client.v100.dto.types.MarketDataType;
import com.imarcats.interfaces.client.v100.notification.MarketDataChange;
import com.imarcats.internal.server.infrastructure.marketdata.MarketDataSession;
import com.imarcats.internal.server.interfaces.order.OrderManagementContext;
import com.imarcats.market.engine.market.MarketImpl;
import com.imarcats.market.engine.matching.ContinuousTwoSidedAuction;
import com.imarcats.market.engine.order.OrderImpl;
import com.imarcats.market.engine.testutils.MockDatastores;
import com.imarcats.market.engine.testutils.MockMarketDataSessionImpl;
import com.imarcats.market.engine.testutils.OrderCompareTestCaseBase;
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
 * Test continuous two-sided auction with discriminatory pricing rule 
 * 
 * See Order Matching and Order Management � Business Requirement Specification document
 * Order Matching and Trade Pricing section
 * @author Adam
 *
 */
public class ContinuousTwoSidedAuctionDiscriminatoryPricingRuleTest extends OrderCompareTestCaseBase {
    
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

		DiscriminatoryPricingRule.INSTANCE.priceOrders(matchOnMarketOpen, market, orderManagementContext);
		
		assertEquals(4, matchOnMarketOpen.length);
		
		checkPair(matchOnMarketOpen[0], 1, "Bif", "Sol", getExpectedQuote(19.8, quoteType_));		
		checkPair(matchOnMarketOpen[1], 3, "Bif", "Sue", getExpectedQuote(20.0, quoteType_));		
		checkPair(matchOnMarketOpen[2], 2, "Bob", "Sue", getExpectedQuote(20.1, quoteType_));	
		checkPair(matchOnMarketOpen[3], 1, "Bea", "Sue", getExpectedQuote(20.0, quoteType_));
		
		// check market data
		MarketDataSession marketDataSession = orderManagementContext.getMarketDataSession();
		MarketDataChange[] marketDataChanges = filterMarketData(((MockMarketDataSessionImpl) marketDataSession).getMarketDataChanges(), MarketDataType.Last);
		
		assertEquals(4, marketDataChanges.length);
		
		// check last trades
		checkMarketDataChange(quoteType_, market.getMarketCode(), marketDataChanges[0], MarketDataType.Last, 19.8, 1, null);
		checkMarketDataChange(quoteType_, market.getMarketCode(), marketDataChanges[1], MarketDataType.Last, 20.0, 3, null);
		checkMarketDataChange(quoteType_, market.getMarketCode(), marketDataChanges[2], MarketDataType.Last, 20.1, 2, null);
		checkMarketDataChange(quoteType_, market.getMarketCode(), marketDataChanges[3], MarketDataType.Last, 20.0, 1, null);
	}
	
	public void testOrderMatchingPricing() throws Exception {
		orderMatchingPricingTest(QuoteType.Price, true);
		orderMatchingPricingTest(QuoteType.Yield, true);
		
		orderMatchingPricingTest(QuoteType.Price, false);
		orderMatchingPricingTest(QuoteType.Yield, false);
	}
	
	private void orderMatchingPricingTest(QuoteType quoteType_, boolean displayOrder_) throws InterruptedException {
		
		
		MockDatastores datastore = new MockDatastores();
		Market marketModel = getMarket(datastore);
		
		OrderManagementContext orderManagementContext = createOrderManagerContext();
		
		marketModel.setState(MarketState.Open);
		
		TestOrderFlowGenerator gen = new TestOrderFlowGenerator(quoteType_, marketModel);
		Order[] orders = gen.getOrders();
		
		// create order to place it to the data source
		for (Order order : orders) {
			// setup Market Orders correctly to test, if Valid Market Orders (Entire-Order-at-Once and Immediate-or-Cancel) are matched 
			if(order.getType() == OrderType.Market) {
				order.setExecuteEntireOrderAtOnce(true);
				order.setExpirationInstruction(OrderExpirationInstruction.ImmediateOrCancel);
			}
			// set orders to be hidden, to make sure matching works for hidden orders 
			order.setDisplayOrder(displayOrder_);
			
			datastore.createOrder(order);
		}
		
		marketModel.setQuoteType(quoteType_);
		MarketImpl market = new MarketImpl(marketModel, null, datastore, datastore, datastore, datastore);
		
		// 1.
		MatchedTrade[] matchedTrades = ContinuousTwoSidedAuction.INSTANCE.matchOnSubmit(new OrderImpl(orders[0], datastore), market, orderManagementContext);
		DiscriminatoryPricingRule.INSTANCE.priceOrders(matchedTrades, market, orderManagementContext);
		
		assertEquals(0, matchedTrades.length);
		assertEquals(null, market.getLastTrade());
		
		// 2.
		matchedTrades = ContinuousTwoSidedAuction.INSTANCE.matchOnSubmit(new OrderImpl(orders[1], datastore), market, orderManagementContext);
		DiscriminatoryPricingRule.INSTANCE.priceOrders(matchedTrades, market, orderManagementContext);
		
		assertEquals(0, matchedTrades.length);
		assertEquals(null, market.getLastTrade());

		// 3.
		matchedTrades = ContinuousTwoSidedAuction.INSTANCE.matchOnSubmit(new OrderImpl(orders[2], datastore), market, orderManagementContext);
		DiscriminatoryPricingRule.INSTANCE.priceOrders(matchedTrades, market, orderManagementContext);
		
		assertEquals(0, matchedTrades.length);
		assertEquals(null, market.getLastTrade());
		
		assertEquals(0, matchedTrades.length);

		// 4.
		matchedTrades = ContinuousTwoSidedAuction.INSTANCE.matchOnSubmit(new OrderImpl(orders[3], datastore), market, orderManagementContext);
		DiscriminatoryPricingRule.INSTANCE.priceOrders(matchedTrades, market, orderManagementContext);
		
		assertEquals(1, matchedTrades.length);
		checkPair(matchedTrades[0], 1, "Bea", "Sol", getExpectedQuote(20.0, quoteType_));
		checkQuoteAndSize(market.getLastTrade(), getExpectedQuote(20.0, quoteType_), 1, true);
		
		// 5.
		matchedTrades = ContinuousTwoSidedAuction.INSTANCE.matchOnSubmit(new OrderImpl(orders[4], datastore), market, orderManagementContext);
		DiscriminatoryPricingRule.INSTANCE.priceOrders(matchedTrades, market, orderManagementContext);
		
		assertEquals(0, matchedTrades.length);
		checkQuoteAndSize(market.getLastTrade(), getExpectedQuote(20.0, quoteType_), 1, true);
		
		// 6.
		matchedTrades = ContinuousTwoSidedAuction.INSTANCE.matchOnSubmit(new OrderImpl(orders[5], datastore), market, orderManagementContext);
		DiscriminatoryPricingRule.INSTANCE.priceOrders(matchedTrades, market, orderManagementContext);
		
		assertEquals(2, matchedTrades.length);
		checkPair(matchedTrades[0], 2, "Bif", "Sam", getExpectedQuote(20.1, quoteType_));
		checkPair(matchedTrades[1], 2, "Bif", "Stu", getExpectedQuote(20.2, quoteType_));
		
		checkQuoteAndSize(market.getLastTrade(), getExpectedQuote(20.2, quoteType_), 2, true);
		
		// 7.
		matchedTrades = ContinuousTwoSidedAuction.INSTANCE.matchOnSubmit(new OrderImpl(orders[6], datastore), market, orderManagementContext);
		DiscriminatoryPricingRule.INSTANCE.priceOrders(matchedTrades, market, orderManagementContext);
		
		assertEquals(0, matchedTrades.length);
		
		checkQuoteAndSize(market.getLastTrade(), getExpectedQuote(20.2, quoteType_), 2, true);
		
		// 8.
		matchedTrades = ContinuousTwoSidedAuction.INSTANCE.matchOnSubmit(new OrderImpl(orders[7], datastore), market, orderManagementContext);
		DiscriminatoryPricingRule.INSTANCE.priceOrders(matchedTrades, market, orderManagementContext);
		
		assertEquals(3, matchedTrades.length);
		checkPair(matchedTrades[0], 2, "Bob", "Sue", getExpectedQuote(20.1, quoteType_));
		checkPair(matchedTrades[1], 2, "Bea", "Sue", getExpectedQuote(20.0, quoteType_));
		checkPair(matchedTrades[2], 2, "Ben", "Sue", getExpectedQuote(20.0, quoteType_));
		
		checkQuoteAndSize(market.getLastTrade(), getExpectedQuote(20.0, quoteType_), 2, true);
		
		// 9.
		matchedTrades = ContinuousTwoSidedAuction.INSTANCE.matchOnSubmit(new OrderImpl(orders[8], datastore), market, orderManagementContext);
		DiscriminatoryPricingRule.INSTANCE.priceOrders(matchedTrades, market, orderManagementContext);
		
		assertEquals(0, matchedTrades.length);
		
		checkQuoteAndSize(market.getLastTrade(), getExpectedQuote(20.0, quoteType_), 2, true);		
	}
	
	public void testInbalancedBookMatch() throws Exception {
		inbalancedBookMatchTest(QuoteType.Price);
		inbalancedBookMatchTest(QuoteType.Yield);
	}

	private void inbalancedBookMatchTest(QuoteType quoteType_) throws InterruptedException {
		
		
		MockDatastores datastore = new MockDatastores();
		Market marketModel = getMarket(datastore);
		
		OrderManagementContext orderManagementContext = createOrderManagerContext();
		
		marketModel.setState(MarketState.Open);
		
		TestOrderFlowGenerator gen = new TestOrderFlowGenerator(quoteType_, marketModel);
		Order[] orders = gen.getOrders();
		
		// create order to place it to the data source
		for (Order order : orders) {
			datastore.createOrder(order);
		}
		
		marketModel.setQuoteType(quoteType_);
		MarketImpl market = new MarketImpl(marketModel, null, datastore, datastore, datastore, datastore);
		
		MatchedTrade[] matchedPairs = null;
		for (Order order : orders) {
			if(order.getSide() == OrderSide.Buy) {
				matchedPairs = ContinuousTwoSidedAuction.INSTANCE.matchOnSubmit(new OrderImpl(order, datastore), market, orderManagementContext);
				DiscriminatoryPricingRule.INSTANCE.priceOrders(matchedPairs, market, orderManagementContext);
			}
		}
		
		assertEquals(null, market.getLastTrade());
	}
	
	public void testMarketOrderMatch() throws Exception {
		marketOrderMatchTest(QuoteType.Price);
		marketOrderMatchTest(QuoteType.Yield);
	}

	private void marketOrderMatchTest(QuoteType quoteType_) throws InterruptedException {
		
		
		MockDatastores datastore = new MockDatastores();
		Market marketModel = getMarket(datastore);
		
		OrderManagementContext orderManagementContext = createOrderManagerContext();
		
		marketModel.setState(MarketState.Open);
		
		TestOrderFlowGenerator gen = new TestOrderFlowGenerator(quoteType_, marketModel);
		Order[] orders = gen.getOrders();
		
		// create order to place it to the data source
		for (Order order : orders) {
			datastore.createOrder(order);
		}
		
		marketModel.setQuoteType(quoteType_);
		MarketImpl market = new MarketImpl(marketModel, null, datastore, datastore, datastore, datastore);
		
		// This should NEVER really the case, Market should NOT allow submission of Market Orders to an empty book
		
		// setting market orders to the required settings 
		orders[0].setType(OrderType.Market);
		orders[0].setSize(10);
		orders[0].setExecuteEntireOrderAtOnce(true);
		orders[0].setExpirationInstruction(OrderExpirationInstruction.ImmediateOrCancel);
		
		orders[1].setType(OrderType.Market);
		orders[1].setSize(10);
		orders[1].setExecuteEntireOrderAtOnce(true);
		orders[1].setExpirationInstruction(OrderExpirationInstruction.ImmediateOrCancel);
		
		MatchedTrade[] matchedPairs = ContinuousTwoSidedAuction.INSTANCE.matchOnSubmit(new OrderImpl(orders[0], datastore), market, orderManagementContext);
		DiscriminatoryPricingRule.INSTANCE.priceOrders(matchedPairs, market, orderManagementContext);
		
		assertEquals(0, matchedPairs.length);
		assertEquals(null, market.getLastTrade());
		assertEquals(null, market.getBestBidSystem());
		assertEquals(null, market.getBestAskSystem());
		
		matchedPairs = ContinuousTwoSidedAuction.INSTANCE.matchOnSubmit(new OrderImpl(orders[1], datastore), market, orderManagementContext);
		
		// This Matching does not make sense, because it cannot be priced
		DiscriminatoryPricingRule.INSTANCE.priceOrders(matchedPairs, market, orderManagementContext);
		
		assertEquals(0, matchedPairs.length);
		assertEquals(null, market.getLastTrade());
		assertEquals(null, market.getBestBidSystem());
		assertEquals(null, market.getBestAskSystem());
		
		assertEquals(OrderState.Canceled, orders[0].getState());
		assertEquals(OrderState.Canceled, orders[1].getState());
	} 
}
