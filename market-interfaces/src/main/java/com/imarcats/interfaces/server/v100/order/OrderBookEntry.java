package com.imarcats.interfaces.server.v100.order;

import com.imarcats.model.types.OrderSide;
import com.imarcats.model.types.OrderType;
import com.imarcats.model.types.Quote;

/**
 * Defines a limited set of Operations on an Order, 
 * these Operations are offered to the Public 
 * when they are browsing an Order Book.
 */
public interface OrderBookEntry {

	/**
	 * @return Side of the Order (Value from OrderSide)
	 */
	public OrderSide getSide();
	
	/**
	 * @return Type of the Order (Value from OrderType)
	 */
	public OrderType getType();
	
	/**
	 * @return Price or Yield Value Defined on the Order - Only for Limit Orders
	 */
	public Quote getLimitQuote();

}
