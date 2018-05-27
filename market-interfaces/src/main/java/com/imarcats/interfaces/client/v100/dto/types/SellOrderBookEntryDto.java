package com.imarcats.interfaces.client.v100.dto.types;

/**
 * Sell Orders for the same Price are held in this Sell side Order Book Entry, 
 * before they are executed
 * @author Adam
 */
public class SellOrderBookEntryDto extends BookEntryBaseDto {

	public OrderSide getOrderSide() {
		return OrderSide.Sell;
	}
}
