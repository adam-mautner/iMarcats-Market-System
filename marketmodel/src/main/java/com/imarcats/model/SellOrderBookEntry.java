package com.imarcats.model;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.imarcats.model.types.OrderSide;

/**
 * Sell Orders for the same Price are held in this Sell side Order Book Entry, 
 * before they are executed
 * @author Adam
 */
@Entity
@Table(name="SELL_ORDER_BOOK_ENTRY")
public class SellOrderBookEntry extends BookEntryBase {

	private static final long serialVersionUID = 1L;

	public OrderSide getOrderSide() {
		return OrderSide.Sell;
	}
}
