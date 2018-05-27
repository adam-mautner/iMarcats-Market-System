package com.imarcats.market.engine.pricing;

import java.util.Date;

import com.imarcats.internal.server.interfaces.market.MarketInternal;
import com.imarcats.internal.server.interfaces.order.OrderManagementContext;
import com.imarcats.model.MatchedTrade;
import com.imarcats.model.types.OrderType;
import com.imarcats.model.types.Quote;
import com.imarcats.model.types.QuoteAndSize;
import com.imarcats.model.types.QuoteType;

/**
 * All Trades take place at same market clearing price 
 * @author Adam
 */
public class UniformPricingRule implements OrderPricingRule {

	public static final UniformPricingRule INSTANCE = new UniformPricingRule();
	
	private UniformPricingRule() { /* Singleton */ }
	
	/**
	 * Prices all Matched Orders Pairs to the same Market Clearing Price, 
	 * Records the Last Trading price to the Market. 
	 * 
	 * Market Clearing Quote is last the Quote from the last feasible trade, 
	 * where the Bid/Ask spread is the narrowest. The Market Orders define 
	 * the widest spreads. 
	 */
	@Override
	public void priceOrders(MatchedTrade[] trades_, MarketInternal market_, OrderManagementContext orderManagementContext_) {
		MatchedTrade bestPair = null;
		double minBidAskSpread = Double.POSITIVE_INFINITY;
		for (MatchedTrade matchedOrderPair : trades_) {
			double currentSpread = getBidAskSpread(matchedOrderPair, market_);
			if(!Double.isInfinite(currentSpread) && 
			   currentSpread <= minBidAskSpread) {
				minBidAskSpread = currentSpread;
				bestPair = matchedOrderPair;
			}
			
		}
		
		if(bestPair != null) {
			// Clearing Quote is the Average of the Bid and Ask (Mid)
			double clearingQuote = 
				(bestPair.getSellSide().getOrderQuote().getQuoteValue() + bestPair.getBuySide().getOrderQuote().getQuoteValue()) / 2;
			
			// set Clearing Quote to all Trades
			int sumSize = 0;
			for (MatchedTrade matchedTrade : trades_) {
				sumSize += matchedTrade.getMatchedSize();
				matchedTrade.setTradeQuote(Quote.createQuote(clearingQuote));
			}
			
			// record last trade to market
			QuoteAndSize lastTrade = new QuoteAndSize();
			lastTrade.setSize(sumSize);
			lastTrade.getQuote().setQuoteValue(clearingQuote);
			lastTrade.getQuote().setDateOfQuote(new Date());
			lastTrade.getQuote().setValidQuote(true);
			market_.recordLastTrade(lastTrade, orderManagementContext_.getMarketDataSession());
		}
	}
	
	/**
	 * Get the Bid/Ask Spread of the Trade
	 * @param matchedOrderPair_
	 * @param market_
	 * @return
	 */
	private double getBidAskSpread(MatchedTrade matchedOrderPair_, MarketInternal market_) {
		double spread = Double.POSITIVE_INFINITY;
		if(matchedOrderPair_.getBuySide().getOrderType() == OrderType.Limit && 
		   matchedOrderPair_.getSellSide().getOrderType() == OrderType.Limit) {
			if(market_.getQuoteType() == QuoteType.Price) {
				spread = matchedOrderPair_.getBuySide().getOrderQuote().getQuoteValue() - 
					matchedOrderPair_.getSellSide().getOrderQuote().getQuoteValue();
			} else {
				spread = matchedOrderPair_.getSellSide().getOrderQuote().getQuoteValue() -
					matchedOrderPair_.getBuySide().getOrderQuote().getQuoteValue();				
			}
		}
		
		return spread;
	}
}
