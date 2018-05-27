package com.imarcats.market.engine.pricing;

import com.imarcats.internal.server.interfaces.market.MarketInternal;
import com.imarcats.internal.server.interfaces.order.OrderManagementContext;
import com.imarcats.model.MatchedTrade;


/**
 * Applies a Pricing Rule on the given Orders
 * @author Adam
 */
public interface OrderPricingRule {

	/**
	 * Prices Orders with a Pricing Rule
	 * @param trades_ Trades to be Priced
	 * @param market_ Market where the Order is Executed
	 * @param orderManagementContext_ Management Context for Order Changes
	 */
	public void priceOrders(MatchedTrade[] trades_, MarketInternal market_, OrderManagementContext orderManagementContext_);
	
}
