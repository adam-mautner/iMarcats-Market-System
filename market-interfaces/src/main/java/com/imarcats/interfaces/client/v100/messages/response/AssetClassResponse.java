package com.imarcats.interfaces.client.v100.messages.response;

import com.imarcats.interfaces.client.v100.dto.AssetClassDto;
import com.imarcats.interfaces.client.v100.dto.ActivatableMarketObjectDto;

/**
 * Response with Asset Class 
 * @author Adam
 *
 */
public class AssetClassResponse extends MarketObjectResponse {

	private static final long serialVersionUID = 1L;

	private AssetClassDto _assetClass;

	public void setAssetClass(AssetClassDto assetClass) {
		_assetClass = assetClass;
	}

	public AssetClassDto getAssetClass() {
		return _assetClass;
	}

	@Override
	public ActivatableMarketObjectDto getMarketObject() {
		return getAssetClass();
	}
	
	
}
