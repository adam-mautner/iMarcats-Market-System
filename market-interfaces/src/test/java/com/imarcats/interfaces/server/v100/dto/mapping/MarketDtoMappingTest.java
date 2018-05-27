package com.imarcats.interfaces.server.v100.dto.mapping;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.imarcats.interfaces.client.v100.dto.MarketDto;
import com.imarcats.interfaces.client.v100.dto.types.BuyBookDto;
import com.imarcats.interfaces.client.v100.dto.types.PagedMarketListDto;
import com.imarcats.interfaces.client.v100.dto.types.QuoteAndSizeDto;
import com.imarcats.interfaces.client.v100.dto.types.QuoteDto;
import com.imarcats.interfaces.client.v100.dto.types.SellBookDto;
import com.imarcats.interfaces.client.v100.dto.types.TimeOfDayDto;
import com.imarcats.model.BuyBook;
import com.imarcats.model.OrderBookEntryModel;
import com.imarcats.model.OrderBookModel;
import com.imarcats.model.Market;
import com.imarcats.model.SellBook;
import com.imarcats.model.mutators.PropertyListValueChange;
import com.imarcats.model.mutators.helpers.ActivationStatusWrapper;
import com.imarcats.model.mutators.helpers.BooleanWrapper;
import com.imarcats.model.mutators.helpers.ExecutionSystemWrapper;
import com.imarcats.model.mutators.helpers.MarketStateWrapper;
import com.imarcats.model.mutators.helpers.QuoteTypeWrapper;
import com.imarcats.model.mutators.helpers.RecurringActionDetailWrapper;
import com.imarcats.model.mutators.helpers.SecondaryOrderPrecedenceRuleTypeWrapper;
import com.imarcats.model.mutators.helpers.TradingSessionWrapper;
import com.imarcats.model.types.ActivationStatus;
import com.imarcats.model.types.ExecutionSystem;
import com.imarcats.model.types.Property;
import com.imarcats.model.types.MarketState;
import com.imarcats.model.types.OrderSide;
import com.imarcats.model.types.PagedMarketList;
import com.imarcats.model.types.Quote;
import com.imarcats.model.types.QuoteAndSize;
import com.imarcats.model.types.QuoteType;
import com.imarcats.model.types.RecurringActionDetail;
import com.imarcats.model.types.SecondaryOrderPrecedenceRuleType;
import com.imarcats.model.types.TimeOfDay;
import com.imarcats.model.types.TradingSession;
import com.imarcats.model.utils.PropertyUtils;

public class MarketDtoMappingTest extends PropertyMappingTestBase {
	public void testRoundTripMappingMarket() throws Exception {
		Market market = createMarketForDto("TestMarket");
		
		MarketDto marketDto = MarketDtoMapping.INSTANCE.toDto(market);
		Market marketMapped = MarketDtoMapping.INSTANCE.fromDto(marketDto); 
		
		checkMarket(market, marketMapped);
		
		TimeOfDay timeOfDay = createTimePeriod().getStartTime();
		
		TimeOfDayDto timeOfDayDto = MarketDtoMapping.INSTANCE.toDto(timeOfDay);
		TimeOfDay timeOfDayMapped = MarketDtoMapping.INSTANCE.fromDto(timeOfDayDto); 
		
		assertEquals(timeOfDay, timeOfDayMapped);
	}

	public void testRoundTripMappingMarketPrecedenceRulesIssue() throws Exception {
		Market market = createMarketForDto("TestMarket");
		
		List<SecondaryOrderPrecedenceRuleType> list = new ArrayList<SecondaryOrderPrecedenceRuleType>();

		list.add(SecondaryOrderPrecedenceRuleType.UnrestrictedOrderPrecedence);
		list.add(SecondaryOrderPrecedenceRuleType.TimePrecedence);
		
		market.setSecondaryOrderPrecedenceRules(list); 
		
		MarketDto marketDto = MarketDtoMapping.INSTANCE.toDto(market);
		Market marketMapped = MarketDtoMapping.INSTANCE.fromDto(marketDto); 
		
		checkMarket(market, marketMapped);
		
		TimeOfDay timeOfDay = createTimePeriod().getStartTime();
		
		TimeOfDayDto timeOfDayDto = MarketDtoMapping.INSTANCE.toDto(timeOfDay);
		TimeOfDay timeOfDayMapped = MarketDtoMapping.INSTANCE.fromDto(timeOfDayDto); 
		
		assertEquals(timeOfDay, timeOfDayMapped);
	}
	
	public void testObjectPropertyChangeRoundTripMapping() throws Exception {
		List<Property> properties = new ArrayList<Property>();
		properties.add(PropertyUtils.createObjectProperty("ActivationStatus", new ActivationStatusWrapper(ActivationStatus.Activated)));
		properties.add(PropertyUtils.createObjectProperty("Bool", new BooleanWrapper(true)));
		properties.add(PropertyUtils.createObjectProperty("ExecutionSystem", new ExecutionSystemWrapper(ExecutionSystem.CallMarketWithSingleSideAuction)));
		properties.add(PropertyUtils.createObjectProperty("OrderExpirationInstruction", new MarketStateWrapper(MarketState.Called)));
		properties.add(PropertyUtils.createObjectProperty("QuoteType", new QuoteTypeWrapper(QuoteType.Price)));
		properties.add(PropertyUtils.createObjectProperty("RecurringActionDetail", new RecurringActionDetailWrapper(RecurringActionDetail.Daily)));
		properties.add(PropertyUtils.createObjectProperty("SecondaryOrderPrecedenceRuleType", new SecondaryOrderPrecedenceRuleTypeWrapper(SecondaryOrderPrecedenceRuleType.DisplayPrecedence)));
		properties.add(PropertyUtils.createObjectProperty("TradingSession", new TradingSessionWrapper(TradingSession.Continuous)));

		properties.add(PropertyUtils.createObjectProperty("Audit", createAudit()));
		properties.add(PropertyUtils.createObjectProperty("BusinessCalendar", createBusinessCalendar()));
		properties.add(PropertyUtils.createObjectProperty("CircuitBreaker", createCircuitBreaker()));
		properties.add(PropertyUtils.createObjectProperty("TimeOfDay", createTimePeriod().getStartTime()));
		properties.add(PropertyUtils.createObjectProperty("TimePeriod", createTimePeriod()));
						
		for (Property property : properties) {			
			testProperty(property);
		}
	}

	private void testProperty(Property property) {
		PropertyListValueChange listChange = createListValueChange(property);
		
		testObjectProperties(property, listChange);
	}
	
	public void testPropertyChangeRoundTripMapping() throws Exception {
		List<Property> properties = createPropertyList();
		
		for (Property property : properties) {			
			testProperty(property);
		}
	}
	
	public void testRoundTripListMapping() throws Exception {
		Market market = createMarketForDto("Test1");
		Market market2 = createMarketForDto("Test2");
		
		PagedMarketList list = new PagedMarketList();
		list.setMarkets(new Market[] {market, market2});
		list.setCursorString("TestCursor");
		list.setMaxNumberOfMarketsOnPage(2); 
		
		PagedMarketListDto listDto = MarketDtoMapping.INSTANCE.toDto(list);
		PagedMarketList listMapped = MarketDtoMapping.INSTANCE.fromDto(listDto); 
		
		assertEquals(list.getCursorString(), listMapped.getCursorString());
		assertEquals(list.getMaxNumberOfMarketsOnPage(), listMapped.getMaxNumberOfMarketsOnPage());
		assertEquals(list.getMarkets().length, listMapped.getMarkets().length);
		checkMarket(list.getMarkets()[0], listMapped.getMarkets()[0]);		
		checkMarket(list.getMarkets()[1], listMapped.getMarkets()[1]);		
	}
	
	private Market createMarketForDto(String code_) throws Exception {
		Market market = createMarket(code_);
		market.updateLastUpdateTimestampAndVersion();
		market.setVersionNumber(10L);
		return market;
	}

	private void checkMarket(Market market, Market marketMapped) {
		// add internal fields - they are not copied to DTO 
		marketMapped.setBuyBook(market.getBuyBook());
		marketMapped.setSellBook(market.getSellBook());
		marketMapped.setHaltLevel(market.getHaltLevel()); 
		marketMapped.setMarketCallActionKey(market.getMarketCallActionKey());
		marketMapped.setMarketOpenActionKey(market.getMarketOpenActionKey());
		marketMapped.setMarketCloseActionKey(market.getMarketCloseActionKey());
		marketMapped.setMarketReOpenActionKey(market.getMarketReOpenActionKey());
		marketMapped.setMarketMaintenanceActionKey(market.getMarketMaintenanceActionKey());
		marketMapped.setCallMarketMaintenanceActionKey(market.getCallMarketMaintenanceActionKey());
	
		assertEqualsMarket(market, marketMapped);
		assertEquals(market.getLastUpdateTimestamp(), marketMapped.getLastUpdateTimestamp());
		assertEquals(market.getVersionNumber(), marketMapped.getVersionNumber());
	}
	
	public void testRoundTripMappingBuyBook() throws Exception {
		BuyBook buyBook = (BuyBook)createBook(OrderSide.Buy);
		buyBook.setVersionNumber(10L); 
		
		BuyBookDto buyBookDto = MarketDtoMapping.INSTANCE.toDto(buyBook);
		BuyBook buyBookMapped = MarketDtoMapping.INSTANCE.fromDto(buyBookDto); 
	
		// add internal fields - they are not copied to DTO 
		copyOrderKeys(buyBook, buyBookMapped);
		
		assertEqualsBook(buyBook, buyBookMapped);
		assertEquals(buyBook.getVersionNumber(), buyBookMapped.getVersionNumber());	
	}

	public void testRoundTripQuotes() throws Exception {
		Quote quote = Quote.createQuote(100);
		
		QuoteDto quoteDto = MarketDtoMapping.INSTANCE.toDto(quote);
		Quote quoteMapped = MarketDtoMapping.INSTANCE.fromDto(quoteDto); 

		assertEqualsQuote(quote, quoteMapped);
		
		QuoteAndSize quoteAndSize = new QuoteAndSize();
		quoteAndSize.setQuote(Quote.createQuote(100));
		quoteAndSize.setSize(10); 
		
		QuoteAndSizeDto quoteAndSizeDto = MarketDtoMapping.INSTANCE.toDto(quoteAndSize);
		QuoteAndSize quoteAndSizeMapped = MarketDtoMapping.INSTANCE.fromDto(quoteAndSizeDto); 

		assertEqualsQuoteAndSize(quoteAndSize, quoteAndSizeMapped);
	}
	
	public void testRoundTripMappingSellBook() throws Exception {
		SellBook sellBook = (SellBook)createBook(OrderSide.Sell);
		sellBook.setVersionNumber(10L);
		
		SellBookDto sellBookDto = MarketDtoMapping.INSTANCE.toDto(sellBook);
		SellBook sellBookMapped = MarketDtoMapping.INSTANCE.fromDto(sellBookDto); 

		// add internal fields - they are not copied to DTO 
		copyOrderKeys(sellBook, sellBookMapped);
		
		assertEqualsBook(sellBook, sellBookMapped);
		assertEquals(sellBook.getVersionNumber(), sellBookMapped.getVersionNumber());
	}
	
	private void copyOrderKeys(OrderBookModel buyBook, OrderBookModel buyBookMapped) {
		for (int i = 0; i < buyBook.size(); i++) {
			OrderBookEntryModel orderBookEntryModel = buyBook.get(i);
			OrderBookEntryModel orderBookEntryModel2 = buyBookMapped.get(i);
			
			for (Iterator<Long> iterator = orderBookEntryModel.getOrderKeyIterator(); iterator.hasNext();) {
				orderBookEntryModel2.addOrderKey(iterator.next(), buyBookMapped);
			}
		}
	}
}
