package com.imarcats.interfaces.server.v100.market;

import com.imarcats.model.types.MarketState;

/**
 * Notifies about Change of State of the Market
 * @author Adam
 */
public interface MarketStateChangeListener {

	/**
	 * Called when, the State of the Market is Changed
	 * @param market_ Market, which changed its activation status 
	 * @param state_ Opening State (Value from MarketState)
	 */
	public void marketStateChanged(MarketInterface market_, MarketState state_); 
}
