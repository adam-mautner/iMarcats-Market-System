package com.imarcats.market.engine.order;

import com.imarcats.model.Order;


/**
 * This Composite Precedence Rule applies its precedences in a given order, 
 * if the first Precedence Rule decides that the Orders have the same precedence, 
 * it will use the next one. Applies Precedence Rules until precedence can be 
 * decided or it have no more rules.
 * @author Adam
 */
public class CompositePrecedenceRule implements SecondaryOrderPrecedenceRule {

	private final SecondaryOrderPrecedenceRule[] _rules;

	public CompositePrecedenceRule(SecondaryOrderPrecedenceRule[] rules_) {
		super();
		_rules = rules_;
	}
 	
	
	/**
	 * @return a negative integer, zero, or a positive integer 
	 * as the first order has greater precedence, equal to, 
	 * or less precedence than the second.
	 */
	@Override
	public int compare(Order order1_, Order order2_) {
		int precedence = 0;
		
		for (SecondaryOrderPrecedenceRule rule : _rules) {
			precedence = rule.compare(order1_, order2_);
			if(precedence != 0) {
				break;
			}
		}
		
		return precedence;
	}
}
