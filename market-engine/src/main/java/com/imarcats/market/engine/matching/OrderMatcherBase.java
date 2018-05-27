package com.imarcats.market.engine.matching;

import java.util.Date;

import com.imarcats.internal.server.interfaces.market.MarketInternal;
import com.imarcats.internal.server.interfaces.order.OrderInternal;
import com.imarcats.model.MatchedTrade;
import com.imarcats.model.types.OrderSide;
import com.imarcats.model.types.OrderType;
import com.imarcats.model.types.Quote;
import com.imarcats.model.types.QuoteType;

/**
 * Common Base Class for Order Matcher implementations
 * @author Adam
 */
public abstract class OrderMatcherBase {

	/**
	 * Matches two Orders
	 * @param market_ Market, where the matching happens 
	 * @param buyOrder_ Buy Order
	 * @param sellOrder_ Sell Order
	 * @return Matched Order pair
	 */
	protected MatchedOrders matchOrders(MarketInternal market_,
			OrderInternal buyOrder_, OrderInternal sellOrder_) {
		// Order Side is not relevant for this Matched Pair 
		return matchOrders(market_, buyOrder_, sellOrder_, null);
	}
	
	/**
	 * Matches two Orders
	 * @param market_ Market, where the matching happens 
	 * @param buyOrder_ Buy Order
	 * @param sellOrder_ Sell Order
	 * @param standingOrderSide_ Defines, which side of the Matched Orders was a Standing Order in the Book. This is only relevant for Discriminatory Pricing Rule and it does not make sense for Uniform Pricing Rule (as all Matched Orders were in the Book). - Value from OrderSide
	 * @return Matched Order pair
	 */
	protected MatchedOrders matchOrders(MarketInternal market_,
			OrderInternal buyOrder_, OrderInternal sellOrder_, OrderSide standingOrderSide_) {
		MatchedOrders matchedPair = null;
		Date tradeDateTime = new Date();
		if(buyOrder_ != null && sellOrder_ != null) {
			int matchedSize = getMatchedSize(buyOrder_, sellOrder_);
			if(checkMinimumSizeOnStandingOrder(buyOrder_, sellOrder_, matchedSize, standingOrderSide_) ) {
			
				if(buyOrder_.getType() == OrderType.Market || 
				   sellOrder_.getType() == OrderType.Market) {
					MatchedTrade matchedTrade = 
						new MatchedTrade(matchedSize, 
								buyOrder_.getTradeSide(tradeDateTime), 
								sellOrder_.getTradeSide(tradeDateTime), 
								market_.getMarketCode(), 
								standingOrderSide_);
					matchedPair = new MatchedOrders(buyOrder_,
							sellOrder_, matchedTrade);
				} else if(areQuotesMatching(market_, buyOrder_.getLimitQuoteValue(), sellOrder_.getLimitQuoteValue())) {
					MatchedTrade matchedTrade =
						new MatchedTrade(matchedSize, 
								buyOrder_.getTradeSide(tradeDateTime), 
								sellOrder_.getTradeSide(tradeDateTime), 
								market_.getMarketCode(), 
								standingOrderSide_);
					matchedPair = new MatchedOrders(buyOrder_,
							sellOrder_, matchedTrade);
				}
			}
		}
		
		if(matchedPair != null && matchedPair.getTrade().getMatchedSize() == 0) {
			matchedPair = null;
		}
		
		return matchedPair;
	}

	private boolean checkMinimumSizeOnStandingOrder(OrderInternal buyOrder_,
			OrderInternal sellOrder_, int matchedSize_, OrderSide standingOrderSide_) {
		boolean matchedSizeOK = true;
		if(buyOrder_.getSide() == standingOrderSide_) {
			matchedSizeOK = matchedSize_ >= getMinimumRequiredExecutionSize(buyOrder_);
		} else if(sellOrder_.getSide() == standingOrderSide_) {
			matchedSizeOK = matchedSize_ >= getMinimumRequiredExecutionSize(sellOrder_);
		}
		
		return matchedSizeOK;
	}

	/**
	 * Gets the Minimum Size required to be Executed on the Order
	 * @param order_
	 * @return
	 */
	public static int getMinimumRequiredExecutionSize(OrderInternal order_) {
		int minimumSize = order_.getMinimumSizeOfExecution();
		if(order_.getExecuteEntireOrderAtOnce()) {
			minimumSize = order_.getRemainingSize();
		} 
		
		return minimumSize;
	}
	
	/**
	 * Calculates the Matched Size of the Order
	 * @param buyOrder_ Buy Order
	 * @param sellOrder_ Sell Order
	 * @return Matched Size of the Order
	 */
	private int getMatchedSize(OrderInternal buyOrder_,
			OrderInternal sellOrder_) {
		int buySize = buyOrder_.getRemainingSize();
		int sellSize = sellOrder_.getRemainingSize();
		return Math.min(buySize, sellSize);
	}


	/**
	 * Finds out, if two Orders can be matched 
	 * @param market_ Market, where the matching happens 
	 * @param buyOrderQuote_ Buy Order
	 * @param sellOrderQuote_ Sell Order
	 * @return if, the Orders can be matched
	 */
	public static boolean areQuotesMatching(MarketInternal market_, Quote buyOrderQuote_, Quote sellOrderQuote_) {
		boolean matching = false;
		if(market_.getQuoteType() == QuoteType.Price) {
			matching = buyOrderQuote_.getQuoteValue() >= sellOrderQuote_.getQuoteValue(); 
		} else if(market_.getQuoteType() == QuoteType.Yield) {
			matching = buyOrderQuote_.getQuoteValue() <= sellOrderQuote_.getQuoteValue();
		}

		
		return matching;
	}
	
	/**
	 * Holds a Matched Order pair, which makes up a Trade
	 * @author Adam
	 */
	protected static class MatchedOrders {
		private final OrderInternal _buyOrder;
		private final OrderInternal _sellOrder;
		private final MatchedTrade _trade;
		
		public MatchedOrders(OrderInternal buyOrder_,
				OrderInternal sellOrder_, MatchedTrade trade_) {
			super();
			_buyOrder = buyOrder_;
			_sellOrder = sellOrder_;
			_trade = trade_;
		}
		public OrderInternal getBuyOrder() {
			return _buyOrder;
		}
		public OrderInternal getSellOrder() {
			return _sellOrder;
		}
		public MatchedTrade getTrade() {
			return _trade;
		}
	}	
}
