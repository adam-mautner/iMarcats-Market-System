package com.imarcats.interfaces.server.v100.market;

import com.imarcats.interfaces.client.v100.dto.types.MarketDataType;
import com.imarcats.model.types.QuoteAndSize;

/**
 * Notifies about Change of the Market Data
 */
public interface MarketDataChangeListener {

	/**
	 * Called, when Market Data is changed on the Market
	 * @param market_ Market, where market data changed 
	 * @param marketDataType_ Type of the Changed Market Data (Int value from MarketDataType)
	 * @param value_ Changed Quote and Size value 
	 */
	public void marketDataChanged(MarketInterface market_, MarketDataType marketDataType_, QuoteAndSize value_);
	
}
