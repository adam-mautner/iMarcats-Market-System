package com.imarcats;

import com.imarcats.infrastructure.server.trigger.OrderExpirationTest;
import com.imarcats.infrastructure.server.trigger.StopLossTriggerTest;
import com.imarcats.infrastructure.server.trigger.TrailingStopTriggerTest;
import com.imarcats.internal.server.infrastructure.util.BusinessCalendarTest;
import com.imarcats.internal.server.interfaces.market.MarketPropertyChangeExecutorTest;
import com.imarcats.market.engine.market.CircuitBreakerTest;
import com.imarcats.market.engine.market.MarketImplTest;
import com.imarcats.market.engine.matching.CombinedOrderMatcherTest;
import com.imarcats.market.engine.matching.ContinuousTwoSidedAuctionTest;
import com.imarcats.market.engine.matching.SinglePriceAuctionTest;
import com.imarcats.market.engine.order.OrderBookImplTest;
import com.imarcats.market.engine.order.OrderManagementSystemTest;
import com.imarcats.market.engine.order.OrderPrecedenceRuleTest;
import com.imarcats.market.engine.pricing.CombinedOrderMatcherCombinedOrderPricingRuleTest;
import com.imarcats.market.engine.pricing.ContinuousTwoSidedAuctionDiscriminatoryPricingRuleTest;
import com.imarcats.market.engine.pricing.SinglePriceAuctionUniformPricingRuleTest;
import com.imarcats.market.engine.service.MarketServiceTest;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for com.imarcats.market.engine");
		//$JUnit-BEGIN$
		suite.addTestSuite(MarketPropertyChangeExecutorTest.class);	
		suite.addTestSuite(BusinessCalendarTest.class);
		suite.addTestSuite(CircuitBreakerTest.class);
		suite.addTestSuite(MarketImplTest.class);
		suite.addTestSuite(CombinedOrderMatcherTest.class);
		suite.addTestSuite(ContinuousTwoSidedAuctionTest.class);
		suite.addTestSuite(SinglePriceAuctionTest.class);
		suite.addTestSuite(OrderBookImplTest.class);
		suite.addTestSuite(OrderPrecedenceRuleTest.class);
		suite.addTestSuite(OrderManagementSystemTest.class);		
		suite.addTestSuite(CombinedOrderMatcherCombinedOrderPricingRuleTest.class);
		suite.addTestSuite(SinglePriceAuctionUniformPricingRuleTest.class);
		suite.addTestSuite(ContinuousTwoSidedAuctionDiscriminatoryPricingRuleTest.class);
		suite.addTestSuite(MarketServiceTest.class);
		suite.addTestSuite(OrderExpirationTest.class);
		suite.addTestSuite(StopLossTriggerTest.class);
		suite.addTestSuite(TrailingStopTriggerTest.class);
		//$JUnit-END$
		return suite;
	}

}
