package com.imarcats.model;

import java.util.Date;
import java.util.Iterator;

import com.imarcats.model.types.OrderSide;
import com.imarcats.model.types.OrderType;
import com.imarcats.model.types.Quote;

/**
 * One Entry in the Order Book, Order Book Entries are 
 * always sorted by Price Preference. Orders in the Order Book Entry 
 * are sorted by the Secondary Order Precedence Rules. 
 * @author Adam
 */
public interface OrderBookEntryModel extends MarketModelObject {

	public void addOrderKey(Long key_, OrderBookModel book_);
	
	public void addOrderKey(int index_, Long key_, OrderBookModel book_);
	
	public int orderKeyCount();
	
	public void removeOrderKey(Long key_, OrderBookModel book_);
	
	public Iterator<Long> getOrderKeyIterator();
	
	public Long getOrderKey(int index_);
	
	public boolean isOrderKeyListEmpty();
	
	public void setLimitQuote(Quote limitQuote);

	public Quote getLimitQuote();

	public void setAggregateSize(Integer aggregateSize);

	public Integer getAggregateSize();
	
	public void setHasHiddenOrders(Boolean hasHiddenOrders);

	public Boolean getHasHiddenOrders();

	public void setOrderType(OrderType orderType);

	public OrderType getOrderType();

	public OrderSide getOrderSide();
	
	/**
	 * @return Return the Date and Time, when the Object was Last Updated
	 */
	public Date getLastUpdateTimestamp();
	
	public void updateLastUpdateTimestamp();
}
