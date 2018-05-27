package com.imarcats.interfaces.server.v100.dto.mapping;

import org.dozer.classmap.RelationshipType;
import org.dozer.loader.api.BeanMappingBuilder;
import org.dozer.loader.api.FieldsMappingOptions;

import com.imarcats.interfaces.client.v100.dto.MarketDto;
import com.imarcats.interfaces.client.v100.dto.types.BusinessCalendarDto;
import com.imarcats.interfaces.client.v100.dto.types.BuyBookDto;
import com.imarcats.interfaces.client.v100.dto.types.OrderBookModelDto;
import com.imarcats.interfaces.client.v100.dto.types.PagedMarketListDto;
import com.imarcats.interfaces.client.v100.dto.types.QuoteAndSizeDto;
import com.imarcats.interfaces.client.v100.dto.types.QuoteDto;
import com.imarcats.interfaces.client.v100.dto.types.SellBookDto;
import com.imarcats.interfaces.client.v100.dto.types.TimeOfDayDto;
import com.imarcats.model.BuyBook;
import com.imarcats.model.OrderBookModel;
import com.imarcats.model.Market;
import com.imarcats.model.SellBook;
import com.imarcats.model.types.BusinessCalendar;
import com.imarcats.model.types.PagedMarketList;
import com.imarcats.model.types.Quote;
import com.imarcats.model.types.QuoteAndSize;
import com.imarcats.model.types.TimeOfDay;

public class MarketDtoMapping extends DtoMappingBase {
	public static MarketDtoMapping INSTANCE = new MarketDtoMapping();
	
	private MarketDtoMapping() {
		super();
		_mapper.addMapping(createListMappingBuilder());
	}
	
	public static BeanMappingBuilder createListMappingBuilder() {
		BeanMappingBuilder builder = new BeanMappingBuilder() {

			@Override
			protected void configure() {
				mapping(Market.class, MarketDto.class).fields("secondaryOrderPrecedenceRules", "secondaryOrderPrecedenceRules", 
						FieldsMappingOptions.collectionStrategy(true, 
                        RelationshipType.NON_CUMULATIVE));
			}
			
		};
		
		return builder;
	}
	
	public BusinessCalendarDto toDto(BusinessCalendar calendar_) {
		return _mapper.map(calendar_, BusinessCalendarDto.class);
	}
		
	public BusinessCalendar fromDto(BusinessCalendarDto calendar_) {
		return _mapper.map(calendar_, BusinessCalendar.class);
	}
	
	public MarketDto toDto(Market market_) {
		return _mapper.map(market_, MarketDto.class);
	}
	
	public Market fromDto(MarketDto market_) {
		return _mapper.map(market_, Market.class);
	}

	public QuoteDto toDto(Quote quote_) {
		return _mapper.map(quote_, QuoteDto.class);
	}
	
	public Quote fromDto(QuoteDto quote_) {
		return _mapper.map(quote_, Quote.class);
	}
	
	public QuoteAndSizeDto toDto(QuoteAndSize quote_) {
		return _mapper.map(quote_, QuoteAndSizeDto.class);
	}
	
	public QuoteAndSize fromDto(QuoteAndSizeDto quote_) {
		return _mapper.map(quote_, QuoteAndSize.class);
	}
	
	
	public OrderBookModelDto toDto(OrderBookModel book_) {
		if(book_ instanceof BuyBook) {
			return toDto((BuyBook)book_);
		} else if(book_ instanceof SellBook) {
			return toDto((SellBook)book_);
		} else {
			throw new IllegalArgumentException("Unsupported order book implementation");
		}
	}

	public OrderBookModel fromDto(OrderBookModelDto book_) {
		if(book_ instanceof BuyBookDto) {
			return fromDto((BuyBookDto)book_);
		} else if(book_ instanceof SellBookDto) {
			return fromDto((SellBookDto)book_);
		} else {
			throw new IllegalArgumentException("Unsupported order book implementation");
		}
	}
	
	public BuyBookDto toDto(BuyBook book_) {
		return _mapper.map(book_, BuyBookDto.class);
	}
	
	public BuyBook fromDto(BuyBookDto book_) {
		return _mapper.map(book_, BuyBook.class);
	}
	
	public SellBookDto toDto(SellBook book_) {
		return _mapper.map(book_, SellBookDto.class);
	}
	
	public SellBook fromDto(SellBookDto book_) {
		return _mapper.map(book_, SellBook.class);
	}
	
	public PagedMarketListDto toDto(PagedMarketList marketList_) {
		return _mapper.map(marketList_, PagedMarketListDto.class);
	}
	
	public PagedMarketList fromDto(PagedMarketListDto marketList_) {
		return _mapper.map(marketList_, PagedMarketList.class);
	}
	
	public TimeOfDayDto toDto(TimeOfDay time_) {
		return _mapper.map(time_, TimeOfDayDto.class);
	}
	
	public TimeOfDay fromDto(TimeOfDayDto time_) {
		return _mapper.map(time_, TimeOfDay.class);
	}
}
