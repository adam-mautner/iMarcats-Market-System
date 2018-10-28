package com.imarcats.interfaces.server.v100.dto.mapping;

import com.imarcats.interfaces.client.v100.dto.MatchedTradeDto;
import com.imarcats.interfaces.client.v100.dto.types.PagedMatchedTradeSideListDto;
import com.imarcats.interfaces.client.v100.dto.types.TradeSideDto;
import com.imarcats.model.MatchedTrade;
import com.imarcats.model.TradeSide;
import com.imarcats.model.types.PagedMatchedTradeSideList;

public class MatchedTradeDtoMapping extends DtoMappingBase {
	public static MatchedTradeDtoMapping INSTANCE = new MatchedTradeDtoMapping();
	
	public MatchedTradeDto toDto(MatchedTrade trade_) {
		return _mapper.map(trade_, MatchedTradeDto.class);
	}
	
	public MatchedTrade fromDto(MatchedTradeDto trade_) {
		return _mapper.map(trade_, MatchedTrade.class);
	}
	
	public TradeSideDto toDto(TradeSide tradeSide_) {
		return _mapper.map(tradeSide_, TradeSideDto.class);
	}
	
	public TradeSide fromDto(TradeSideDto tradeSide_) {
		return _mapper.map(tradeSide_, TradeSide.class);
	}
	
	public PagedMatchedTradeSideListDto toDto(PagedMatchedTradeSideList tradeList_) {
		return _mapper.map(tradeList_, PagedMatchedTradeSideListDto.class);
	}
	
	public PagedMatchedTradeSideList fromDto(PagedMatchedTradeSideListDto tradeList_) {
		return _mapper.map(tradeList_, PagedMatchedTradeSideList.class);
	}
}
