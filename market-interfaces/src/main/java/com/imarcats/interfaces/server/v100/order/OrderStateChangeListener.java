package com.imarcats.interfaces.server.v100.order;

import com.imarcats.model.types.OrderState;


/**
 * Listener for a Order State Change 
 * @author Adam
 */
public interface OrderStateChangeListener {

	/**
	 * Called on State Change of the Order 
	 * @param orderChanged_ Order being Changed
	 * @param newState_ New Order State 
	 */
	public void stateChanged(OrderInterface orderChanged_, OrderState newState_);
	
}
