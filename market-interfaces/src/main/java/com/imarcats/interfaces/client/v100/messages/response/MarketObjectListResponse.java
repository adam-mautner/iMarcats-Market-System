package com.imarcats.interfaces.client.v100.messages.response;

import com.imarcats.interfaces.client.v100.dto.ActivatableMarketObjectDto;
import com.imarcats.interfaces.client.v100.messages.MessageBase;

/**
 * Market Object List Response
 * @author Adam
 *
 */
public abstract class MarketObjectListResponse extends MessageBase {

	private static final long serialVersionUID = 1L;
	
	public abstract ActivatableMarketObjectDto[] getMarketObjects();
	
	public abstract String getCursorString();
}
