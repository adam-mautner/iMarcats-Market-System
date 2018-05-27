package com.imarcats.interfaces.client.v100.messages.request;

import com.imarcats.interfaces.client.v100.messages.MessageBase;

public abstract class MarketCodeRequestBase extends MessageBase {

	private static final long serialVersionUID = 1L;

	private String _marketCode;

	public void setMarketCode(String marketCode) {
		_marketCode = marketCode;
	}

	public String getMarketCode() {
		return _marketCode;
	}
}
