package com.imarcats.market.engine.execution;

import com.imarcats.internal.server.interfaces.order.OrderInternal;
import com.imarcats.internal.server.interfaces.order.OrderManagementContext;


/**
 * Defines an Order Execution System for the Market
 * @author Adam
 */
public interface MarketExecutionSystem {

	/**
	 * Handles Market Open 
	 * @param orderManagementContext_ Management Context for Order Changes
	 */
	public void handleMarketOpen(OrderManagementContext orderManagementContext_);

	/**
	 * Handles Market Re-Open
	 * @param orderManagementContext_ Management Context for Order Changes
	 */
	public void handleMarketReOpen(OrderManagementContext orderManagementContext_);
	
	/**
	 * Handles Market Close
	 * @param orderManagementContext_ Management Context for Order Changes
	 */
	public void handleMarketClose(OrderManagementContext orderManagementContext_);
	
	/**
	 * Handles an Order Execution 
	 * @param order_ Order being Executed
	 * @param orderManagementContext_ Management Context for Order Changes
	 */
	public void handleSubmit(OrderInternal order_, OrderManagementContext orderManagementContext_);
	
	/**
	 * Handles Market Call
	 * @param orderManagementContext_ Management Context for Order Changes
	 */
	public void handleMarketCall(OrderManagementContext orderManagementContext_);
	
}
