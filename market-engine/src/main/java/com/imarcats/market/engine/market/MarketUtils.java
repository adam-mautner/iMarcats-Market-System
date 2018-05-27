package com.imarcats.market.engine.market;

import com.imarcats.internal.server.interfaces.market.MarketInternal;
import com.imarcats.internal.server.interfaces.order.OrderBookInternal;
import com.imarcats.internal.server.interfaces.order.OrderInternal;
import com.imarcats.model.MatchedTrade;
import com.imarcats.model.types.OrderSide;


/**
 * Utility Class for Markets
 * @author Adam
 */
public class MarketUtils {

	private MarketUtils() { /* Static Utils Class */ }

	/**
	 * Gets the Book for a given Order
	 * @param order_ Order
	 * @param market_ Market, from which we want the Book 
	 * @return Order Book 
	 */
	public static OrderBookInternal getBook(OrderInternal order_, MarketInternal market_) {
		return getBook(order_.getSide(), market_);
	}
	
	/**
	 * Gets the Book for a given Side
	 * @param side_ Order Side (Value from OrderSide)
	 * @param market_ Market, from which we want the Book 
	 * @return Order Book 
	 */
	public static OrderBookInternal getBook(OrderSide side_, MarketInternal market_) {
		OrderBookInternal book = null;
		if(side_ == OrderSide.Buy) {
			book = market_.getBuyBook();
		} else if(side_ == OrderSide.Sell) {
			book = market_.getSellBook();
		} else {
			throw new IllegalArgumentException("Order side cannot be identified.");
		}
		
		return book;
	}

	/**
	 * Gets the Opposite Side Book for a given Side
	 * @param side_ Order Side (Value from OrderSide)
	 * @param market_ Market, from which we want the Book 
	 * @return Order Book 
	 */
	public static OrderBookInternal getOppositeSideBook(OrderSide side_, MarketInternal market_) {
		return getBook(getOppositeSide(side_), market_);
	}
	
	/**
	 * Gets the Opposite Side for a given Side
	 * @param side_ Order Side (Value from OrderSide)
	 * @return Order Book 
	 */
	public static OrderSide getOppositeSide(OrderSide side_) {
		return side_ == OrderSide.Buy ? OrderSide.Sell : OrderSide.Buy;
	}
	
	/**
	 * Gets the Opposite Side Book for a given Order
	 * @param order_ Order
	 * @param market_ Market, from which we want the Book 
	 * @return Order Book 
	 */
	public static OrderBookInternal getOppositeSideBook(OrderInternal order_, MarketInternal market_) {
		return getOppositeSideBook(order_.getSide(), market_);
	}
	
	/**
	 * Tells, if a Trade has valid Quote 
	 * @param matchedTrade_ Trade
	 * @return if Trade has valid Quote 
	 */
	public static boolean hasValidTradeQuote(MatchedTrade matchedTrade_) {
		return matchedTrade_.getTradeQuote() != null && matchedTrade_.getTradeQuote().getValidQuote();
	}
}
