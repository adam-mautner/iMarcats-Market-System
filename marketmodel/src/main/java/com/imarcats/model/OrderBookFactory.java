package com.imarcats.model;

import com.imarcats.model.types.OrderSide;

/**
 * Creates an OrderBook 
 * @author Adam
 */
public class OrderBookFactory {

	public OrderBookFactory() {
		/* static factory class */
	}

	/**
	 * Creates an Order Book for an Order Side
	 * @param side_ Side of the Order
	 * @return OrderBook Implementation
	 */
	public static OrderBookModel createBook(OrderSide side_) {
		return side_ == OrderSide.Buy ? new BuyBook() : new SellBook();
	}
	
}
