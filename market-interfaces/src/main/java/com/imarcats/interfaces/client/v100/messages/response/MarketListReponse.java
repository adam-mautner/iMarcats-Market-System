package com.imarcats.interfaces.client.v100.messages.response;

import com.imarcats.interfaces.client.v100.dto.ActivatableMarketObjectDto;
import com.imarcats.interfaces.client.v100.dto.types.PagedMarketListDto;

public class MarketListReponse extends MarketObjectListResponse {

	private static final long serialVersionUID = 1L;
	
	private PagedMarketListDto _marketList;
	
	@Override
	public ActivatableMarketObjectDto[] getMarketObjects() {
		ActivatableMarketObjectDto[] list = new ActivatableMarketObjectDto[0];
		
		if(_marketList != null && _marketList.getMarkets() != null) {
			list = new ActivatableMarketObjectDto[_marketList.getMarkets().length];
			for (int i = 0; i < list.length; i++) {
				list[i] = _marketList.getMarkets()[i];
			}
		}
		
		return list;
	}

	@Override
	public String getCursorString() {
		return _marketList.getCursorString();
	}

	public void setMarketList(PagedMarketListDto marketList) {
		_marketList = marketList;
	}

	public PagedMarketListDto getMarketList() {
		return _marketList;
	}
	
}
