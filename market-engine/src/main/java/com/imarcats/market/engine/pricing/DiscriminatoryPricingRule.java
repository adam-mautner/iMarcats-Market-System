package com.imarcats.market.engine.pricing;

import java.util.Date;

import com.imarcats.internal.server.interfaces.market.MarketInternal;
import com.imarcats.internal.server.interfaces.order.OrderManagementContext;
import com.imarcats.market.engine.market.MarketUtils;
import com.imarcats.model.MatchedTrade;
import com.imarcats.model.TradeSide;
import com.imarcats.model.types.OrderSide;
import com.imarcats.model.types.OrderType;
import com.imarcats.model.types.Quote;
import com.imarcats.model.types.QuoteAndSize;

/**
 * All Trades take place at Price of the Standing Order
 * @author Adam
 */
public class DiscriminatoryPricingRule implements OrderPricingRule {

	public static final DiscriminatoryPricingRule INSTANCE = new DiscriminatoryPricingRule();
	
	private DiscriminatoryPricingRule() { /* Singleton */ }
	
	/**
	 * Prices All Matched Trade based on the Standing Order Side of the Trade, 
	 * if the Standing Side was a Market Order, it will use the other side for 
	 * Pricing. If two Market Orders are matched and their are other matched 
	 * trader, the price is taken from the next matched trade (if possible). 
	 * In case there are no more Trades, where the pricing was possible, 
	 * the trade will not have a valid price. 
	 * 
	 * Records all the matched trades with valid price as 
	 * the Last Trading price to the Market. 
	 * 
	 * Note: Orders have to be sorted as they were matched on the Market
	 */
	@Override
	public void priceOrders(MatchedTrade[] trades_, MarketInternal market_, OrderManagementContext orderManagementContext_) {
		// try pricing the matched orders
		for (MatchedTrade trade : trades_) {
			priceOrder(trade, market_);
		}
		
		// try taking the price for non-priced orders from the next order
		Quote quote = null;
		for (int i = trades_.length - 1; i >= 0; i--) {
			MatchedTrade order = trades_[i];
			if(order.getTradeQuote() == null || 
			   !order.getTradeQuote().getValidQuote()) {
				order.setTradeQuote(quote);
			}
			
			quote = order.getTradeQuote();
		}
		
		// record all the matched trades with valid price as 
		// the Last Trading price to the Market. 
		for (MatchedTrade trade : trades_) {
			if(MarketUtils.hasValidTradeQuote(trade)) {
				// record last trade to market
				QuoteAndSize lastTrade = new QuoteAndSize();
				lastTrade.setSize(trade.getMatchedSize());
				lastTrade.setQuote(trade.getTradeQuote());
				lastTrade.getQuote().setDateOfQuote(new Date());
				market_.recordLastTrade(lastTrade, orderManagementContext_.getMarketDataSession());
			}
		}
	}

	private void priceOrder(MatchedTrade trade_, MarketInternal market_) {
		TradeSide standingSide = getStandingSide(trade_);
		TradeSide nonStandingSide = getNonStandingSide(trade_);
		
		if(standingSide.getOrderType() == OrderType.Limit) {
			trade_.setTradeQuote(standingSide.getOrderQuote());
		} else if(nonStandingSide.getOrderType() == OrderType.Limit) {
			trade_.setTradeQuote(nonStandingSide.getOrderQuote());		
		}
	}

	private TradeSide getNonStandingSide(MatchedTrade trade_) {
		return trade_.getStandingOrderSide() == OrderSide.Buy
				? trade_.getSellSide()
				: trade_.getBuySide();
	}
	
	private TradeSide getStandingSide(MatchedTrade trade_) {
		return trade_.getStandingOrderSide() == OrderSide.Buy
				? trade_.getBuySide()
				: trade_.getSellSide();
	}
}
