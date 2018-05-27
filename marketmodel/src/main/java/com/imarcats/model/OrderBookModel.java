package com.imarcats.model;

import java.util.Comparator;
import java.util.Iterator;

import com.imarcats.model.types.OrderSide;

/**
 * Orders are held in an Order Book on the Market before they are executed
 * @author Adam
 */
public interface OrderBookModel extends MarketModelObject, VersionedMarketObject {
	
	/**
	 * Get an Order Book Entry from the Book. Order Book Entries are 
	 * always sorted by Price Preference. Orders in the OrderBookEntry Objects 
	 * are sorted by the Secondary Order Precedence Rule 
	 * @return Book Entries
	 */
	public OrderBookEntryModel get(int index_);
	
	public boolean isEmpty();
	
	/**
	 * Searches the place of the given Entry in the Book by using the given comparator. 
	 * @param newEntry
	 * @param comparator_
	 * @return (-(insertion point) - 1)
	 */
	public int binarySearch(OrderBookEntryModel newEntry, Comparator<OrderBookEntryModel> comparator_);
	
	public int size();
	
	public void add(OrderBookEntryModel entry_);

	public void add(int index_, OrderBookEntryModel entry_);
	
	public void remove(OrderBookEntryModel entry_);

	public Iterator<OrderBookEntryModel> getEntryIterator();
	
	/**
	 * Sell or Buy Side Order (Value from OrderSide)
	 * @return Side
	 */
	public OrderSide getSide();
}
