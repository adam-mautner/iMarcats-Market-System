package com.imarcats.market.engine.pricing;

import java.util.HashMap;
import java.util.Map;

import com.imarcats.interfaces.client.v100.exception.MarketRuntimeException;
import com.imarcats.internal.server.interfaces.market.MarketInternal;
import com.imarcats.model.types.ExecutionSystem;

/**
 * Registry has the set of implemented Order Pricing Rules
 * @author Adam
 */
public class OrderPricingRuleRegistry {
	
	private static final Map<ExecutionSystem, OrderPricingRule> MAP_TYPE_TO_PRICING_RULE = new HashMap<ExecutionSystem, OrderPricingRule>();
	
	static{
		MAP_TYPE_TO_PRICING_RULE.put(ExecutionSystem.CallMarketWithSingleSideAuction, UniformPricingRule.INSTANCE);
		MAP_TYPE_TO_PRICING_RULE.put(ExecutionSystem.ContinuousTwoSidedAuction, DiscriminatoryPricingRule.INSTANCE);
		MAP_TYPE_TO_PRICING_RULE.put(ExecutionSystem.Combined, CombinedOrderPricingRule.INSTANCE);
	}
	
	private OrderPricingRuleRegistry() { /* Static Class */ }
	
	/**
	 * Gets the Order Pricing Rule for the Market
	 * @param market_ Market
	 * @return Order Pricing Rule
	 */
	public static OrderPricingRule getOrderPricingRule(MarketInternal market_) {
		OrderPricingRule orderPricingRule = MAP_TYPE_TO_PRICING_RULE.get(market_.getExecutionSystem());
		
		if(orderPricingRule == null) {
			MarketRuntimeException.createExceptionWithDetails(
					MarketRuntimeException.UNSUPPORTED_EXECUTION_SYSTEM_PRICING_RULE, null, 
					new Object[]{ "Execution System=" + market_.getExecutionSystem(), market_ });
		}
		
		return orderPricingRule;
	}
}
