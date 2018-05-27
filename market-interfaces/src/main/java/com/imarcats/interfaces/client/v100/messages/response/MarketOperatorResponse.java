package com.imarcats.interfaces.client.v100.messages.response;

import com.imarcats.interfaces.client.v100.dto.ActivatableMarketObjectDto;
import com.imarcats.interfaces.client.v100.dto.MarketOperatorDto;

/**
 * Response with a Market Operator 
 * @author Adam
 *
 */
public class MarketOperatorResponse extends MarketObjectResponse {

	private static final long serialVersionUID = 1L;
	
	private MarketOperatorDto _marketOperator;

	public void setMarketOperator(MarketOperatorDto marketOperator) {
		_marketOperator = marketOperator;
	}

	public MarketOperatorDto getMarketOperator() {
		return _marketOperator;
	}

	@Override
	public ActivatableMarketObjectDto getMarketObject() {
		return getMarketOperator();
	}

}
