package com.imarcats.market.engine.order;

import com.imarcats.model.Order;


/**
 * Unrestricted Order Precedence, orders which have no restriction on the size needs to be executed has precedence
 * @author Adam
 */
public class UnrestrictedOrderPrecedenceRule implements SecondaryOrderPrecedenceRule {

	public static final UnrestrictedOrderPrecedenceRule INSTANCE = new UnrestrictedOrderPrecedenceRule();
	
	private UnrestrictedOrderPrecedenceRule() { /* singleton */ }
	
	/**
	 * @return a negative integer, zero, or a positive integer 
	 * as the first order has greater precedence, equal to, 
	 * or less precedence than the second.
	 */
	@Override
	public int compare(Order order1_, Order order2_) {
		int precedence = 0;
		if(isRestricted(order1_) == isRestricted(order2_)) {
			precedence = 0;
		} else if(isRestricted(order1_)) {
			precedence = +1;
		} else if(isRestricted(order2_)) {
			precedence = -1;
		}
		
		return precedence;
	}
	
	private boolean isRestricted(Order order_) {
		return order_.getMinimumSizeOfExecution() > 0 || order_.getExecuteEntireOrderAtOnce();
	}
}
