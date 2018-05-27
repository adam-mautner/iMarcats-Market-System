package com.imarcats.market.engine.matching;

import com.imarcats.internal.server.interfaces.market.MarketInternal;
import com.imarcats.internal.server.interfaces.order.OrderInternal;
import com.imarcats.internal.server.interfaces.order.OrderManagementContext;
import com.imarcats.model.MatchedTrade;


/**
 * Matches Orders on the Market
 * @author Adam
 */
public interface OrderMatcher {

	/**
	 * Matches Orders in the Order Book of the Market, when the Market opens
	 * @param market_ Market that Opens
	 * @param orderManagementContext_ Order Management Context 
	 * @return the matched Orders or Empty Array
	 */
	public MatchedTrade[] matchOnMarketOpen(MarketInternal market_, OrderManagementContext orderManagementContext_);
	
	/**
	 * Matches Orders in the Order Book of the Market, when the Market re-opens
	 * @param market_ Market that Re-Opens
	 * @param orderManagementContext_ Order Management Context 
	 * @return the matched Orders or Empty Array
	 */
	public MatchedTrade[] matchOnMarketReOpen(MarketInternal market_, OrderManagementContext orderManagementContext_);
	
	/**
	 * Matches the Submitted Order with the Orders in the Order Book of the Market
	 * @param order_ Order being Submitted
	 * @param market_ Target Market of the Order
	 * @param orderManagementContext_ Order Management Context 
	 * @return the matched Orders or Empty Array
	 */
	public MatchedTrade[] matchOnSubmit(OrderInternal order_, MarketInternal market_, OrderManagementContext orderManagementContext_);
	
	/**
	 * Matches Orders when the Market is Called
	 * @param market_ Market being Called
	 * @param orderManagementContext_ Order Management Context 
	 * @return the matched Orders or Empty Array
	 */
	public MatchedTrade[] matchOnMarketCall(MarketInternal market_, OrderManagementContext orderManagementContext_);
}
