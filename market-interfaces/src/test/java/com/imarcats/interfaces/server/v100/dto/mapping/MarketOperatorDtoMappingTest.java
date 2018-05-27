package com.imarcats.interfaces.server.v100.dto.mapping;

import com.imarcats.interfaces.client.v100.dto.MarketOperatorDto;
import com.imarcats.interfaces.client.v100.dto.types.PagedMarketOperatorListDto;
import com.imarcats.interfaces.server.v100.dto.mapping.MarketOperatorDtoMapping;
import com.imarcats.model.MarketOperator;
import com.imarcats.model.test.testutils.MarketObjectTestBase;
import com.imarcats.model.types.PagedMarketOperatorList;

public class MarketOperatorDtoMappingTest extends MarketObjectTestBase {

	public void testRoundTripMapping() throws Exception {
		MarketOperator marketOperator = createMarketOperator("TestMarketOperator");
		marketOperator.setVersionNumber(10L);
		
		MarketOperatorDto marketOperatorDto = MarketOperatorDtoMapping.INSTANCE.toDto(marketOperator);
		MarketOperator marketOperatorMapped = MarketOperatorDtoMapping.INSTANCE.fromDto(marketOperatorDto); 
		
		checkMarketOperator(marketOperator, marketOperatorMapped);
	}

	private void checkMarketOperator(MarketOperator marketOperator, MarketOperator marketOperatorMapped) {
		assertEqualsMarketOperator(marketOperator, marketOperatorMapped);
		assertEquals(marketOperator.getLastUpdateTimestamp(), marketOperatorMapped.getLastUpdateTimestamp());
		assertEquals(marketOperator.getVersionNumber(), marketOperatorMapped.getVersionNumber());
	}
	
	public void testRoundTripListMapping() throws Exception {
		MarketOperator marketOperator = createMarketOperator("Test1");
		MarketOperator marketOperator2 = createMarketOperator("Test2");
		
		PagedMarketOperatorList list = new PagedMarketOperatorList();
		list.setMarketOperators(new MarketOperator[] {marketOperator, marketOperator2});
		list.setCursorString("TestCursor");
		list.setMaxNumberOfMarketOperatorsOnPage(2); 
		
		PagedMarketOperatorListDto listDto = MarketOperatorDtoMapping.INSTANCE.toDto(list);
		PagedMarketOperatorList listMapped = MarketOperatorDtoMapping.INSTANCE.fromDto(listDto); 
		
		assertEquals(list.getCursorString(), listMapped.getCursorString());
		assertEquals(list.getMaxNumberOfMarketOperatorsOnPage(), listMapped.getMaxNumberOfMarketOperatorsOnPage());
		assertEquals(list.getMarketOperators().length, listMapped.getMarketOperators().length);
		checkMarketOperator(list.getMarketOperators()[0], listMapped.getMarketOperators()[0]);		
		checkMarketOperator(list.getMarketOperators()[1], listMapped.getMarketOperators()[1]);		
	}
}
