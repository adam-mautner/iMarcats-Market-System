package com.imarcats.internal.server.interfaces.order;

import com.imarcats.interfaces.server.v100.order.OrderBookEntry;

/**
 * Internal Interface for Order Book
 * @author Adam
 *
 */
public interface OrderBookEntryInternal extends OrderBookEntry {
	
	/**
	 * @param forClient_ Get Size for Client Not for the System 
	 * @return Aggregate Size of the Orders at this Price for the System 
	 * Note: For Client Hidden Orders (DisplayOrder = False) will not be added to the Calculation
	 */
	public OrderBookEntrySize getSize(boolean forClient_);
}
