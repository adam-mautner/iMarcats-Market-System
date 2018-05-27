package com.imarcats.market.engine.order;

import com.imarcats.model.Order;


public class DisplayPrecedenceRule implements SecondaryOrderPrecedenceRule {

	public static final DisplayPrecedenceRule INSTANCE = new DisplayPrecedenceRule();
	
	private DisplayPrecedenceRule() { /* singleton */ }
	
	/**
	 * @return a negative integer, zero, or a positive integer 
	 * as the first order has greater precedence, equal to, 
	 * or less precedence than the second.
	 */
	@Override
	public int compare(Order order1_, Order order2_) {
		int precedence = 0;
		if(order1_.getDisplayOrder() == order2_.getDisplayOrder()) {
			precedence = 0;
		} else if(order2_.getDisplayOrder()) {
			precedence = +1;
		} else if(order1_.getDisplayOrder()) {
			precedence = -1;
		}
		
		return precedence;
	}
}