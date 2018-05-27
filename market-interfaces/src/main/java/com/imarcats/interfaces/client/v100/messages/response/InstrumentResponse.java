package com.imarcats.interfaces.client.v100.messages.response;

import com.imarcats.interfaces.client.v100.dto.ActivatableMarketObjectDto;
import com.imarcats.interfaces.client.v100.dto.InstrumentDto;
/**
 * Response with a Instrument
 * @author Adam
 *
 */
public class InstrumentResponse extends MarketObjectResponse {

	private static final long serialVersionUID = 1L;

	private InstrumentDto _instrument;

	public void setInstrument(InstrumentDto instrument) {
		_instrument = instrument;
	}

	public InstrumentDto getInstrument() {
		return _instrument;
	}

	@Override
	public ActivatableMarketObjectDto getMarketObject() {
		return getInstrument();
	}
}
