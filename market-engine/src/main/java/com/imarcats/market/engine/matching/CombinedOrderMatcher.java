package com.imarcats.market.engine.matching;

import com.imarcats.internal.server.interfaces.market.MarketInternal;
import com.imarcats.internal.server.interfaces.order.OrderInternal;
import com.imarcats.internal.server.interfaces.order.OrderManagementContext;
import com.imarcats.model.MatchedTrade;


/**
 * Combined Order Matcher uses ContinuousTwoSidedAuction when the Market is Open, 
 * places Orders to the Order Book when the Market is Closed. It uses SinglePriceAuction
 * on Open and Re-Open. Market Call is not supported on this kind of Markets.
 * @author Adam
 *
 */
public class CombinedOrderMatcher implements OrderMatcher {

	public static final CombinedOrderMatcher INSTANCE = new CombinedOrderMatcher();
	
	private CombinedOrderMatcher() { /* Singleton */ }
	
	/**
	 * Market Call is NOT supported on this Market
	 */
	@Override
	public MatchedTrade[] matchOnMarketCall(MarketInternal market_, OrderManagementContext orderManagementContext_) {
		return ContinuousTwoSidedAuction.INSTANCE.matchOnMarketCall(market_, orderManagementContext_);
	}

	/**
	 * Matches Order on Open using Single Price Auction
	 */
	@Override
	public MatchedTrade[] matchOnMarketOpen(MarketInternal market_, OrderManagementContext orderManagementContext_) {
		return SinglePriceAuction.INSTANCE.matchOnMarketCall(market_, orderManagementContext_);
	}

	/**
	 * Matches Order on Re-Open using Single Price Auction
	 */
	@Override
	public MatchedTrade[] matchOnMarketReOpen(MarketInternal market_, OrderManagementContext orderManagementContext_) {
		return SinglePriceAuction.INSTANCE.matchOnMarketCall(market_, orderManagementContext_);
	}
	
	/**
	 * Matches Incoming Order on using Continuous Two Sided Auction when the Market is Open, 
	 * or places it into the Order Book, if Market is Closed.
	 */
	@Override
	public MatchedTrade[] matchOnSubmit(OrderInternal order_, MarketInternal market_, OrderManagementContext orderManagementContext_) {
		return ContinuousTwoSidedAuction.INSTANCE.matchOnSubmit(order_, market_, orderManagementContext_);
	}

}
