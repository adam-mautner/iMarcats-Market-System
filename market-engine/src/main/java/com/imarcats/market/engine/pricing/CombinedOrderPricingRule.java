package com.imarcats.market.engine.pricing;

import com.imarcats.internal.server.interfaces.market.MarketInternal;
import com.imarcats.internal.server.interfaces.order.OrderManagementContext;
import com.imarcats.model.MatchedTrade;
import com.imarcats.model.types.MarketState;


/**
 * Combined Pricing uses DiscriminatoryPricingRule when the Market is Open and uses 
 * UniformPricingRule on Open and Re-Open. 
 * @author Adam
 *
 */
public class CombinedOrderPricingRule implements OrderPricingRule {

	public static final CombinedOrderPricingRule INSTANCE = new CombinedOrderPricingRule();
	
	private CombinedOrderPricingRule() { /* Singleton */ }
	
	/**
	 * Prices the orders with DiscriminatoryPricingRule Market State is Open and
	 * rices the orders with UniformPricingRule Market State is Opening or Re-Opening
	 */
	@Override
	public void priceOrders(MatchedTrade[] trades_, MarketInternal market_, OrderManagementContext orderManagementContext_) {
		if(market_.getState() == MarketState.Open) {
			DiscriminatoryPricingRule.INSTANCE.priceOrders(trades_, market_, orderManagementContext_);
		} else if(market_.getState() == MarketState.Opening || 
				market_.getState() == MarketState.ReOpening) {
			UniformPricingRule.INSTANCE.priceOrders(trades_, market_, orderManagementContext_);			
		}
	}

}
