package com.imarcats.market.engine.order;

import com.imarcats.model.Order;


/**
 * Defines a Time Precedence between Orders (Based on Submission Time)
 * 
 * Note: At least 10 milliseconds time delay between Orders is needed for 
 * the Strict Time Precedence to work
 * 
 * @author Adam
 */
public class TimeOrderPrecedenceRule implements SecondaryOrderPrecedenceRule {

	public static final TimeOrderPrecedenceRule INSTANCE = new TimeOrderPrecedenceRule();
	
	private TimeOrderPrecedenceRule() { /* singleton */ }
	
	/**
	 * @return a negative integer, zero, or a positive integer 
	 * as the first order has greater precedence, equal to, 
	 * or less precedence than the second.
	 */
	@Override
	public int compare(Order order1_, Order order2_) {
		return order1_.getSubmissionDate().compareTo(order2_.getSubmissionDate());
	}
}
