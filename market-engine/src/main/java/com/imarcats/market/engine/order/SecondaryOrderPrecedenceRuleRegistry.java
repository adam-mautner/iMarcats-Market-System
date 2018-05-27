package com.imarcats.market.engine.order;

import java.util.HashMap;
import java.util.Map;

import com.imarcats.model.types.SecondaryOrderPrecedenceRuleType;

/**
 * List of Available Secondary Order Precedence Rules 
 * @author Adam
 */
public class SecondaryOrderPrecedenceRuleRegistry {

	private static final Map<SecondaryOrderPrecedenceRuleType, SecondaryOrderPrecedenceRule> MAP_TYPE_TO_RULE = 
		new HashMap<SecondaryOrderPrecedenceRuleType, SecondaryOrderPrecedenceRule>();
	
	static {
		MAP_TYPE_TO_RULE.put(SecondaryOrderPrecedenceRuleType.TimePrecedence, TimeOrderPrecedenceRule.INSTANCE);
		MAP_TYPE_TO_RULE.put(SecondaryOrderPrecedenceRuleType.UnrestrictedOrderPrecedence, UnrestrictedOrderPrecedenceRule.INSTANCE);
		MAP_TYPE_TO_RULE.put(SecondaryOrderPrecedenceRuleType.DisplayPrecedence, DisplayPrecedenceRule.INSTANCE);		
	}
	
	private SecondaryOrderPrecedenceRuleRegistry() { /* static class */ }
	
	/**
	 * 
	 * @param secondaryOrderPrecedenceRuleType_ value from SecondaryOrderPrecedenceRuleType
	 * @return
	 */
	public static SecondaryOrderPrecedenceRule getSecondaryPrecedence(SecondaryOrderPrecedenceRuleType secondaryOrderPrecedenceRuleType_) {
		SecondaryOrderPrecedenceRule rule = MAP_TYPE_TO_RULE.get(secondaryOrderPrecedenceRuleType_);
		if(rule == null) {
			throw new IllegalArgumentException("No rule can be found for " + secondaryOrderPrecedenceRuleType_ + " rule type");
		}
		
		return rule;
	}
	
}
