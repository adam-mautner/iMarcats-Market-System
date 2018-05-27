package com.imarcats.interfaces.server.v100.order;

import com.imarcats.model.mutators.ChangeAction;

/**
 * Notifies about Sub-Order Addition/Deletion/Clear on the Order
 */
public interface SubOrderChangeListener {

	/**
	 * Called on Sub-Order Addition/Deletion/Clear
	 * @param action_ Sub-Order change Action, Addition/Deletion/Clear
	 * @param order_ Order added or removed, null when called on clear
	 */
	public void subOrderChange(ChangeAction action_, OrderInterface order_);
	
}
