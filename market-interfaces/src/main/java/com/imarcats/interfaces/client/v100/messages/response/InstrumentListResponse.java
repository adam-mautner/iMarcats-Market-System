package com.imarcats.interfaces.client.v100.messages.response;

import com.imarcats.interfaces.client.v100.dto.ActivatableMarketObjectDto;
import com.imarcats.interfaces.client.v100.dto.types.PagedInstrumentListDto;

/**
 * Response with Instrument List
 * @author Adam
 *
 */
public class InstrumentListResponse extends MarketObjectListResponse {

	private static final long serialVersionUID = 1L;
	
	private PagedInstrumentListDto _instrumentList;

	public void setInstrumentList(PagedInstrumentListDto instrumentList) {
		_instrumentList = instrumentList;
	}

	public PagedInstrumentListDto getInstrumentList() {
		return _instrumentList;
	}
	
	@Override
	public ActivatableMarketObjectDto[] getMarketObjects() {
		ActivatableMarketObjectDto[] list = new ActivatableMarketObjectDto[0];
		
		if(_instrumentList != null && _instrumentList.getInstruments() != null) {
			list = new ActivatableMarketObjectDto[_instrumentList.getInstruments().length];
			for (int i = 0; i < list.length; i++) {
				list[i] = _instrumentList.getInstruments()[i];
			}
		}
		
		return list;
	}

	@Override
	public String getCursorString() {
		return _instrumentList.getCursorString();
	}
}
