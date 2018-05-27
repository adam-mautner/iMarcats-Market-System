package com.imarcats.interfaces.server.v100.order;

import com.imarcats.model.mutators.ChangeAction;

/**
 * Notifies about an Order Added/Removed in the Book
 */
public interface OrderBookChangeListener {
	
	/**
	 * Called on Order Addition/Deletion to the Book
	 * @param action_ Change Action, Addition/Deletion/Clear
	 * @param order_ Order Entry added or removed
	 */
	public void orderBookChange(ChangeAction action_, OrderBookEntry orderBookEntry_);
}
