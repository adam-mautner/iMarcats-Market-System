package com.imarcats.interfaces.server.v100.dto.mapping;

import com.imarcats.interfaces.client.v100.dto.MatchedTradeDto;
import com.imarcats.interfaces.client.v100.dto.types.PagedMatchedTradeSideListDto;
import com.imarcats.interfaces.client.v100.dto.types.TradeSideDto;
import com.imarcats.interfaces.server.v100.dto.mapping.MatchedTradeDtoMapping;
import com.imarcats.model.MatchedTrade;
import com.imarcats.model.TradeSide;
import com.imarcats.model.test.testutils.MarketObjectTestBase;
import com.imarcats.model.types.PagedMatchedTradeSideList;
import com.imarcats.model.types.Quote;

public class MatchedTradeDtoMappingTest extends MarketObjectTestBase {
	public void testRoundTripMappingTradeSide() throws Exception {
		String userName = "TestUser";
		TradeSide tradeSide = createTradeSide(userName); 
		
		TradeSideDto tradeSideDto = MatchedTradeDtoMapping.INSTANCE.toDto(tradeSide);
		TradeSide tradeSideMapped = MatchedTradeDtoMapping.INSTANCE.fromDto(tradeSideDto); 
		
		assertEqualsTradeSide(tradeSide, tradeSideMapped);
		assertEquals(tradeSide.getTransactionID(), tradeSideDto.getTransactionID());
	}

	private TradeSide createTradeSide(String userName) {
		TradeSide tradeSide = createTradeSide(userName, "TestMarket");
		tradeSide.setTradeQuote(Quote.createQuote(1000));
		tradeSide.setMatchedSize(100); 
		tradeSide.setMatchedTrade(createMatchedTrade("TestMarket", tradeSide,
				tradeSide));
		return tradeSide;
	}
	
	public void testRoundTripListMapping() throws Exception {
		TradeSide trade = createTradeSide("Test1");
		TradeSide trade2 = createTradeSide("Test2");
		
		PagedMatchedTradeSideList list = new PagedMatchedTradeSideList();
		list.setMatchedTradeSides(new TradeSide[] {trade, trade2});
		list.setCursorString("TestCursor");
		list.setMaxNumberOfMatchedTradeSidesOnPage(2); 
		
		PagedMatchedTradeSideListDto listDto = MatchedTradeDtoMapping.INSTANCE.toDto(list);
		PagedMatchedTradeSideList listMapped = MatchedTradeDtoMapping.INSTANCE.fromDto(listDto); 
		
		assertEquals(list.getCursorString(), listMapped.getCursorString());
		assertEquals(list.getMaxNumberOfMatchedTradeSidesOnPage(), listMapped.getMaxNumberOfMatchedTradeSidesOnPage());
		assertEquals(list.getMatchedTradeSides().length, listMapped.getMatchedTradeSides().length);
		assertEqualsTradeSide(list.getMatchedTradeSides()[0], listMapped.getMatchedTradeSides()[0]);		
		assertEqualsTradeSide(list.getMatchedTradeSides()[1], listMapped.getMatchedTradeSides()[1]);		
	}
	
	public void testRoundTripMappingMatchedTrade() throws Exception {
		TradeSide buySide = createTradeSide("TestUser", "TestMarket");
		buySide.setTradeQuote(Quote.createQuote(1000));
		TradeSide sellSide = createTradeSide("TestUser2", "TestMarket");
		sellSide.setTradeQuote(Quote.createQuote(1000));
		MatchedTrade matchedTrade = createMatchedTrade("TestMarket", buySide, sellSide);
		
		MatchedTradeDto matchedTradeDto = MatchedTradeDtoMapping.INSTANCE.toDto(matchedTrade);
		MatchedTrade matchedTradeMapped = MatchedTradeDtoMapping.INSTANCE.fromDto(matchedTradeDto); 
		
		assertEqualsMatchedTrade(matchedTrade, matchedTradeMapped);
		
	}
}
