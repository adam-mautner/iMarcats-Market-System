package com.imarcats.interfaces.client.v100.messages.response;

import com.imarcats.interfaces.client.v100.dto.ActivatableMarketObjectDto;
import com.imarcats.interfaces.client.v100.dto.types.PagedAssetClassListDto;
/**
 * Response with Asset Class List
 * @author Adam
 *
 */
public class AssetClassListResponse extends MarketObjectListResponse {

	private static final long serialVersionUID = 1L;

	private PagedAssetClassListDto _assetClassList;

	public void setAssetClassList(PagedAssetClassListDto assetClassList) {
		_assetClassList = assetClassList;
	}

	public PagedAssetClassListDto getAssetClassList() {
		return _assetClassList;
	}

	@Override
	public ActivatableMarketObjectDto[] getMarketObjects() {
		ActivatableMarketObjectDto[] list = new ActivatableMarketObjectDto[0];
		
		if(_assetClassList != null && _assetClassList.getAssetClasses() != null) {
			list = new ActivatableMarketObjectDto[_assetClassList.getAssetClasses().length];
			for (int i = 0; i < list.length; i++) {
				list[i] = _assetClassList.getAssetClasses()[i];
			}
		}
		
		return list;
	}

	@Override
	public String getCursorString() {
		return _assetClassList.getCursorString();
	}
	
}
