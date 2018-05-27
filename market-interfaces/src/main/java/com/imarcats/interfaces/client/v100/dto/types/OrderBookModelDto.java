package com.imarcats.interfaces.client.v100.dto.types;

import java.util.Date;

import com.imarcats.interfaces.client.v100.dto.MarketModelObjectDto;

/**
 * Orders are held in an Order Book on the Market before they are executed
 * @author Adam
 */
public interface OrderBookModelDto extends MarketModelObjectDto {
	
	/**
	 * Get an Order Book Entry from the Book. Order Book Entries are 
	 * always sorted by Price Preference. Orders in the OrderBookEntry Objects 
	 * are sorted by the Secondary Order Precedence Rule 
	 * @return Book Entries
	 */
	public OrderBookEntryModelDto get(int index_);
	
	public boolean isEmpty();

	/**
	 * Sell or Buy Side Order (Value from OrderSide)
	 * @return Side
	 */
	public OrderSide getSide();
	
	/**
	 * @return Return the Date and Time, when the Object was Last Updated
	 */
	public Date getLastUpdateTimestamp();
}
