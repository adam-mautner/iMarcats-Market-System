package com.imarcats.market.engine.pricing;

import com.imarcats.interfaces.client.v100.dto.types.MarketDataType;
import com.imarcats.interfaces.client.v100.notification.MarketDataChange;
import com.imarcats.internal.server.infrastructure.marketdata.MarketDataSession;
import com.imarcats.internal.server.interfaces.order.OrderManagementContext;
import com.imarcats.market.engine.market.MarketImpl;
import com.imarcats.market.engine.matching.SinglePriceAuction;
import com.imarcats.market.engine.order.OrderImpl;
import com.imarcats.market.engine.testutils.MockDatastores;
import com.imarcats.market.engine.testutils.MockMarketDataSessionImpl;
import com.imarcats.market.engine.testutils.OrderCompareTestCaseBase;
import com.imarcats.model.Market;
import com.imarcats.model.MatchedTrade;
import com.imarcats.model.Order;
import com.imarcats.model.test.testutils.TestOrderFlowGenerator;
import com.imarcats.model.types.OrderType;
import com.imarcats.model.types.QuoteType;

/**
 * Test single price auction with uniform pricing rule
 * 
 * See Order Matching and Order Management � Business Requirement Specification document
 * Order Matching and Trade Pricing section
 * @author Adam
 *
 */
public class SinglePriceAuctionUniformPricingRuleTest extends OrderCompareTestCaseBase {

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
		
		
		TestOrderFlowGenerator gen = new TestOrderFlowGenerator(quoteType_, marketModel);
		Order[] orders = gen.getOrders();
		
		// create order to place it to the data source
		for (Order order : orders) {
			// set orders to be hidden, to make sure matching works for hidden orders 
			order.setDisplayOrder(displayOrder_);
			
			datastore.createOrder(order);
		}
		
		marketModel.setQuoteType(quoteType_);
		MarketImpl market = new MarketImpl(marketModel, null, datastore, datastore, datastore, datastore);
		
		for (Order order : orders) {
			SinglePriceAuction.INSTANCE.matchOnSubmit(new OrderImpl(order, datastore), market, orderManagementContext);
		}
		
		MatchedTrade[] matchedPairs = SinglePriceAuction.INSTANCE.matchOnMarketCall(market, orderManagementContext);
	
		UniformPricingRule.INSTANCE.priceOrders(matchedPairs, market, orderManagementContext);
		
		assertEquals(4, matchedPairs.length);
		
		checkPair(matchedPairs[0], 1, "Bif", "Sol", getExpectedQuote(20.0, quoteType_));
		checkCommission(market, matchedPairs[0], market.getCommission(), market.getCommission());
		
		checkPair(matchedPairs[1], 3, "Bif", "Sue", getExpectedQuote(20.0, quoteType_));	
		checkCommission(market, matchedPairs[1], 0, market.getCommission());
		
		checkPair(matchedPairs[2], 2, "Bob", "Sue", getExpectedQuote(20.0, quoteType_));	
		checkCommission(market, matchedPairs[2], market.getCommission(), 0);
		
		checkPair(matchedPairs[3], 1, "Bea", "Sue", getExpectedQuote(20.0, quoteType_));	
		checkCommission(market, matchedPairs[3], market.getCommission(), 0);
		
		assertEquals(getExpectedQuote(20.0, quoteType_).getQuoteValue(), market.getLastTrade().getQuote().getQuoteValue());
		assertEquals(7, market.getLastTrade().getSize());
		assertEquals(true, market.getLastTrade().getQuote().getValidQuote());
		
		// check market data
		MarketDataSession marketDataSession = orderManagementContext.getMarketDataSession();
		MarketDataChange[] marketDataChanges = filterMarketData(((MockMarketDataSessionImpl) marketDataSession).getMarketDataChanges(), MarketDataType.Last);
		
		assertEquals(1, marketDataChanges.length);
		
		// check last trade
		MarketDataChange lastTradeMarketData = marketDataChanges[0];
		
		checkMarketDataChange(quoteType_, market.getMarketCode(), lastTradeMarketData, MarketDataType.Last, 20.0, 7, null);
	}

	private void checkCommission(MarketImpl market_,
			MatchedTrade matchedTrade_, double buyCommission_,
			double sellCommission_) {
		assertEquals(buyCommission_, matchedTrade_.getBuySide().getCommission());
		assertEquals(market_.getCommissionCurrency(), matchedTrade_.getBuySide().getCommissionCurrency());
		assertEquals(sellCommission_, matchedTrade_.getSellSide().getCommission());
		assertEquals(market_.getCommissionCurrency(), matchedTrade_.getSellSide().getCommissionCurrency());
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
	
		UniformPricingRule.INSTANCE.priceOrders(matchedPairs, market, orderManagementContext);
		
		assertEquals(0, matchedPairs.length);
		
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
		
		TestOrderFlowGenerator gen = new TestOrderFlowGenerator(quoteType_, marketModel);
		Order[] orders = gen.getOrders();
		
		// create order to place it to the data source
		for (Order order : orders) {
			datastore.createOrder(order);
		}
		
		marketModel.setQuoteType(quoteType_);
		MarketImpl market = new MarketImpl(marketModel, null, datastore, datastore, datastore, datastore);
		
		// This should NEVER really the case, Market should NOT allow submission of 
		// Market Orders to an empty book
		orders[0].setType(OrderType.Market);
		orders[1].setType(OrderType.Market);
		
		SinglePriceAuction.INSTANCE.matchOnSubmit(new OrderImpl(orders[0], datastore), market, orderManagementContext);
		SinglePriceAuction.INSTANCE.matchOnSubmit(new OrderImpl(orders[1], datastore), market, orderManagementContext);
		
		MatchedTrade[] matchedPairs = SinglePriceAuction.INSTANCE.matchOnMarketCall(market, orderManagementContext);

		// This Matching does not make sense, because it cannot be priced
		UniformPricingRule.INSTANCE.priceOrders(matchedPairs, market, orderManagementContext);
		
		assertEquals(1, matchedPairs.length);
		assertEquals((Integer)2, matchedPairs[0].getMatchedSize());
		assertEquals(null, matchedPairs[0].getTradeQuote());
		
		assertEquals(null, market.getLastTrade());
	}
}
