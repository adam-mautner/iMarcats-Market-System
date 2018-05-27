package com.imarcats.interfaces.server.v100.dto.mapping;

import com.imarcats.interfaces.client.v100.dto.AssetClassDto;
import com.imarcats.interfaces.client.v100.dto.types.PagedAssetClassListDto;
import com.imarcats.model.AssetClass;
import com.imarcats.model.types.PagedAssetClassList;

public class AssetClassDtoMapping extends DtoMappingBase {
	public static AssetClassDtoMapping INSTANCE = new AssetClassDtoMapping();
	
	public AssetClassDto toDto(AssetClass assetClass_) {
		return _mapper.map(assetClass_, AssetClassDto.class);
	}
	
	public AssetClass fromDto(AssetClassDto assetClass_) {
		return _mapper.map(assetClass_, AssetClass.class);
	}
	
	public PagedAssetClassListDto toDto(PagedAssetClassList assetClassList_) {
		return _mapper.map(assetClassList_, PagedAssetClassListDto.class);
	}
	
	public PagedAssetClassList fromDto(PagedAssetClassListDto assetClassList_) {
		return _mapper.map(assetClassList_, PagedAssetClassList.class);
	}
}
