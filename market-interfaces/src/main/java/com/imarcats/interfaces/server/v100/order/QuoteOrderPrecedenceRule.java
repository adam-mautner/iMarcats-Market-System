package com.imarcats.interfaces.server.v100.order;

import java.util.Comparator;

import com.imarcats.interfaces.server.v100.util.QuoteRounding;
import com.imarcats.model.OrderBookEntryModel;
import com.imarcats.model.types.OrderSide;
import com.imarcats.model.types.OrderType;
import com.imarcats.model.types.QuoteType;

/**
 * Defines a Time Precedence between OrderBookEntry objects (Based on Quote)
 * 
 * - Order with bigger price has greater precedence on the Buy Side. 
 * - Orders with smaller price has precedence on the Sell Side. 
 * - With Yield precedence is the reverse compared to the price preference
 * - Market Orders have always precedence over Limit Orders
 * 
 * @author Adam
 */
public class QuoteOrderPrecedenceRule implements Comparator<OrderBookEntryModel> {

	/**
	 * Value from OrderSide
	 */
	private final OrderSide _orderSide;
	
	/**
	 * Minimum Price change that needs to be made on the Market
	 */
	private final double _minimumPriceIncrement;
	
	/**
	 * Value from OrderType
	 */
	private final QuoteType _quoteType;

	public QuoteOrderPrecedenceRule(OrderSide orderSide_, double minimumPriceIncrement_, QuoteType quoteType_) {
		super();
		_orderSide = orderSide_;
		_minimumPriceIncrement = minimumPriceIncrement_;
		_quoteType = quoteType_;
	}
	
	/**
	 * @return a negative integer, zero, or a positive integer 
	 * as the first order has greater precedence, equal to, 
	 * or less precedence than the second.
	 */
	@Override
	public int compare(OrderBookEntryModel orderBookEntry1_, OrderBookEntryModel orderBookEntry2_) {
		int compare = 0;
		if(orderBookEntry1_.getOrderType() == orderBookEntry2_.getOrderType() && orderBookEntry2_.getOrderType() == OrderType.Market) {
			// Two Market Order have the same precedence 
			// let the Secondary Precedence Rule decide the actual Precedence
			compare = 0;
		} else if(orderBookEntry1_.getOrderType() == OrderType.Market) {
			compare = -1;
		} else if (orderBookEntry2_.getOrderType() == OrderType.Market) {
			compare = +1;
		} else {
			compare = compareQuotes(orderBookEntry1_.getLimitQuote().getQuoteValue(), 
					orderBookEntry2_.getLimitQuote().getQuoteValue());
		}
		
		return compare;
	}

	/**
	 * Compares Prices on the Sell Side (Orders with smaller price has precedence on the Sell Side.)
	 * @param sellPrice1_
	 * @param sellPrice2_
	 * @return
	 */
	private int compareSellPrice(double sellPrice1_, double sellPrice2_) {
		int compare = Double.compare(sellPrice1_, sellPrice2_);
		// if the two quote has a difference smaller than 1 / 100 * min price increment, 
		// they are considered to be equal
		if(Math.abs(sellPrice1_ - sellPrice2_) < QuoteRounding.getQuoteRoundingThreshold(_minimumPriceIncrement)) {
			compare = 0;
		}
		
		return compare;
	}
	
	/**
	 * Compares Prices on the Buy Side (Order with bigger price has greater precedence on the Buy Side.)
	 * @param buyPrice1_
	 * @param buyPrice2_
	 * @return Reverse of the Sell Side 
	 */
	private int compareBuyPrice(double buyPrice1_, double buyPrice2_) {
		return -compareSellPrice(buyPrice1_, buyPrice2_);
	}

	private int compareQuotes(double quote1_, double quote2_) {
		int compare = comparePrice(quote1_, quote2_);
		if(_quoteType == QuoteType.Yield) {
			compare = compareYield(quote1_, quote2_);
		}
		
		return compare;
	}
	
	/**
	 * Compares Yields of the Orders (With Yield precedence is the reverse compared to the price preference)
	 * @param yield1_
	 * @param yield2_
	 * @return
	 */
	private int compareYield(double yield1_, double yield2_) {
		return -comparePrice(yield1_, yield2_);
	}	
	
	/**
	 * Compares Prices of the Orders
	 * @param price1_
	 * @param price2_
	 * @return
	 */
	private int comparePrice(double price1_, double price2_) {
		int compare = compareBuyPrice(price1_, price2_);
		if(_orderSide == OrderSide.Sell) {
			compare = compareSellPrice(price1_, price2_);
		}
		
		return compare;
	}
}

