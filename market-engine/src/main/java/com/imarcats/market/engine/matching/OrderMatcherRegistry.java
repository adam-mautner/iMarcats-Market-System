package com.imarcats.market.engine.matching;

import java.util.HashMap;
import java.util.Map;

import com.imarcats.interfaces.client.v100.exception.MarketRuntimeException;
import com.imarcats.internal.server.interfaces.market.MarketInternal;
import com.imarcats.model.types.ExecutionSystem;

/**
 * Registry has the set of implemented Order Matchers 
 * @author Adam
 */
public class OrderMatcherRegistry {

	private static final Map<ExecutionSystem, OrderMatcher> MAP_TYPE_TO_MATCHER = new HashMap<ExecutionSystem, OrderMatcher>();
	
	static{
		MAP_TYPE_TO_MATCHER.put(ExecutionSystem.CallMarketWithSingleSideAuction, SinglePriceAuction.INSTANCE);
		MAP_TYPE_TO_MATCHER.put(ExecutionSystem.ContinuousTwoSidedAuction, ContinuousTwoSidedAuction.INSTANCE);		
		MAP_TYPE_TO_MATCHER.put(ExecutionSystem.Combined, CombinedOrderMatcher.INSTANCE);
	}
	
	private OrderMatcherRegistry() { /* Static Class */ }
	
	/**
	 * Gets the Order Matcher for the Market
	 * @param market_ Market
	 * @return Order Matcher
	 */
	public static OrderMatcher getOrderMatcher(MarketInternal market_) {
		OrderMatcher orderMatcher = MAP_TYPE_TO_MATCHER.get(market_.getExecutionSystem());
		
		if(orderMatcher == null) {
			MarketRuntimeException.createExceptionWithDetails(
					MarketRuntimeException.UNSUPPORTED_EXECUTION_SYSTEM_ORDER_MATCHING, null, 
					new Object[]{ "Execution System=" + market_.getExecutionSystem(), market_ });
		}
		
		return orderMatcher;
	}
	
}
