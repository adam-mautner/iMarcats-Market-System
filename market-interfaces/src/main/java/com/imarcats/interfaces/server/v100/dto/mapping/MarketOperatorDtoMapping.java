package com.imarcats.interfaces.server.v100.dto.mapping;

import com.imarcats.interfaces.client.v100.dto.MarketOperatorDto;
import com.imarcats.interfaces.client.v100.dto.types.PagedMarketOperatorListDto;
import com.imarcats.model.MarketOperator;
import com.imarcats.model.types.PagedMarketOperatorList;

public class MarketOperatorDtoMapping extends DtoMappingBase {
	public static MarketOperatorDtoMapping INSTANCE = new MarketOperatorDtoMapping();
	
	public MarketOperatorDto toDto(MarketOperator marketOperator_) {
		return _mapper.map(marketOperator_, MarketOperatorDto.class);
	}
	
	public MarketOperator fromDto(MarketOperatorDto marketOperator_) {
		return _mapper.map(marketOperator_, MarketOperator.class);
	}
	
	public PagedMarketOperatorListDto toDto(PagedMarketOperatorList productList_) {
		return _mapper.map(productList_, PagedMarketOperatorListDto.class);
	}
	
	public PagedMarketOperatorList fromDto(PagedMarketOperatorListDto productList_) {
		return _mapper.map(productList_, PagedMarketOperatorList.class);
	}
}
