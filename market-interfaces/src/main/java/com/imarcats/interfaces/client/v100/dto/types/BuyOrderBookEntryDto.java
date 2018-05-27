package com.imarcats.interfaces.client.v100.dto.types;

/**
 * Buy Orders for the same Price are held in this Buy side Order Book Entry, 
 * before they are executed
 * @author Adam
 */
public class BuyOrderBookEntryDto extends BookEntryBaseDto {

	public OrderSide getOrderSide() {
		return OrderSide.Buy;
	}
}