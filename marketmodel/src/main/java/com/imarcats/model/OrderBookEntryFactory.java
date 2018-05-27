package com.imarcats.model;

import com.imarcats.model.types.OrderSide;
import com.imarcats.model.types.OrderType;
import com.imarcats.model.types.Quote;

/**
 * Creates an IOrderBookEntryModel for Orders
 * @author Adam
 */
public class OrderBookEntryFactory {

	private OrderBookEntryFactory() { /* static factory */ }
	
	/**
	 * Creates an OrderBookEntry from an Order 
	 * @param order_ Order
	 * @param book_ Order Book
	 * @return Book Entry
	 */
	public static OrderBookEntryModel createEntry(Order order_, OrderBookModel book_) {
		OrderBookEntryModel entry = createEntry(order_.getSide());
		entry.addOrderKey(order_.getKey(), book_);
		entry.setLimitQuote(getLimitQuote(order_));
		entry.setOrderType(order_.getType());
		
		return entry;
	}
	
	/**
	 * @param order_
	 * @return Copy of Limit Quote or Null for Market Orders 
	 */
	private static Quote getLimitQuote(Order order_) {
		return order_.getType() == OrderType.Limit 
					? order_.getLimitQuoteValue()
					: null;
	}
	
	public static OrderBookEntryModel createEntry(OrderSide side_) {
		return side_ == OrderSide.Buy 
				? (OrderBookEntryModel) new BuyOrderBookEntry() 
				: (OrderBookEntryModel) new SellOrderBookEntry();
	}
}
