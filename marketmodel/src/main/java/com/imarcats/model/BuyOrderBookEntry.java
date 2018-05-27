package com.imarcats.model;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.imarcats.model.types.OrderSide;

/**
 * Buy Orders for the same Price are held in this Buy side Order Book Entry, 
 * before they are executed
 * @author Adam
 */
@Entity
@Table(name="BUY_ORDER_BOOK_ENTRY")
public class BuyOrderBookEntry extends BookEntryBase {

	private static final long serialVersionUID = 1L;

	public OrderSide getOrderSide() {
		return OrderSide.Buy;
	}
}