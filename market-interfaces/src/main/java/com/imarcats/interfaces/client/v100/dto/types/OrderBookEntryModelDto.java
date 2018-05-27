package com.imarcats.interfaces.client.v100.dto.types;

import java.util.Date;

import com.imarcats.interfaces.client.v100.dto.MarketModelObjectDto;

/**
 * One Entry in the Order Book, Order Book Entries are 
 * always sorted by Price Preference. Orders in the Order Book Entry 
 * are sorted by the Secondary Order Precedence Rules. 
 * @author Adam
 */
public interface OrderBookEntryModelDto extends MarketModelObjectDto {

	public QuoteDto getLimitQuote();

	public Integer getAggregateSize();

	public Boolean getHasHiddenOrders();

	public OrderType getOrderType();

	public OrderSide getOrderSide();
	
	/**
	 * @return Return the Date and Time, when the Object was Last Updated
	 */
	public Date getLastUpdateTimestamp();
}
