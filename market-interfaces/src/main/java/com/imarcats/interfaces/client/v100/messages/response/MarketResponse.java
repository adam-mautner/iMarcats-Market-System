package com.imarcats.interfaces.client.v100.messages.response;

import com.imarcats.interfaces.client.v100.dto.ActivatableMarketObjectDto;
import com.imarcats.interfaces.client.v100.dto.MarketDto;

/**
 * Response with a Market 
 * @author Adam
 *
 */
public class MarketResponse extends MarketObjectResponse {

	private static final long serialVersionUID = 1L;

	private MarketDto _market;

	public void setMarket(MarketDto market) {
		_market = market;
	}

	public MarketDto getMarket() {
		return _market;
	}

	@Override
	public ActivatableMarketObjectDto getMarketObject() {
		return getMarket();
	}
	
}
