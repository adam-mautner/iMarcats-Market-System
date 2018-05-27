package com.imarcats.interfaces.server.v100.order;

import com.imarcats.model.types.OrderSide;
import com.imarcats.model.types.SecondaryOrderPrecedenceRuleType;


/**
 * Defines the set of Operations on the Order Book.
 */
public interface OrderBook {


	/**
	 * @return Side of the Order Book (Value from OrderSide)
	 */
	public OrderSide getSide();
	
	/**
	 * @return how the Orders will be organized in, if they have the same price 
	 * 		   the Book (Value from SecondaryOrderPrecedenceRuleType)
	 */
	public SecondaryOrderPrecedenceRuleType[] getSecondaryOrderPrecedenceRules();

}
