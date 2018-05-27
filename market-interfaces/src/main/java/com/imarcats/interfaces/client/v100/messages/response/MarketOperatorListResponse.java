package com.imarcats.interfaces.client.v100.messages.response;

import com.imarcats.interfaces.client.v100.dto.ActivatableMarketObjectDto;
import com.imarcats.interfaces.client.v100.dto.types.PagedMarketOperatorListDto;

/**
 * Response with Market Operator List
 * @author Adam
 *
 */
public class MarketOperatorListResponse extends MarketObjectListResponse {

	private static final long serialVersionUID = 1L;
	
	private PagedMarketOperatorListDto _marketOperatorList;

	
	public PagedMarketOperatorListDto getMarketOperatorList() {
		return _marketOperatorList;
	}

	public void setMarketOperatorList(PagedMarketOperatorListDto marketOperatorList_) {
		_marketOperatorList = marketOperatorList_;
	}

	@Override
	public ActivatableMarketObjectDto[] getMarketObjects() {
		ActivatableMarketObjectDto[] list = new ActivatableMarketObjectDto[0];
		
		if(_marketOperatorList != null && _marketOperatorList.getMarketOperators() != null) {
			list = new ActivatableMarketObjectDto[_marketOperatorList.getMarketOperators().length];
			for (int i = 0; i < list.length; i++) {
				list[i] = _marketOperatorList.getMarketOperators()[i];
			}
		}
		
		return list;
	}

	@Override
	public String getCursorString() {
		return _marketOperatorList.getCursorString();
	}
}
