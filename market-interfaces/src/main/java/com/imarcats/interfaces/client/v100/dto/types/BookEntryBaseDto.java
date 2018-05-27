package com.imarcats.interfaces.client.v100.dto.types;

import java.sql.Timestamp;
import java.util.Date;

/**
 * Common Base Class for Book Entries 
 * @author Adam
 * TODO: Remove all @Transient fields, when DTOs are created
 */
public abstract class BookEntryBaseDto implements OrderBookEntryModelDto {

	/**
	 * Limit Quote of the Order Book Entry or Null for Market Orders 
	 */
	private QuoteDto _limitQuote;
	
	/**
	 * Aggregate Size of this Order Book Entry or Null, if it is not calculated
	 */
	private Integer _aggregateSize;
	
	/**
	 * Tells if this Order Book Entry has Hidden Orders or Null, if it is not calculated
	 */
	private Boolean _hasHiddenOrders;
	
	/**
	 * Type of the Orders on this Book Entry
	 */
	private OrderType _orderType;

	/**
     * Date and Time, when the Object was Last Updated
     * Required
     */ 
    // TODO: Change to timestamp
	private Date _lastUpdateTimestamp;
	
	public QuoteDto getLimitQuote() {
		return _limitQuote;
	}

	public void setLimitQuote(QuoteDto limitQuote_) {
		_limitQuote = limitQuote_;
		
		updateLastUpdateTimestamp();
	}

	public Integer getAggregateSize() {
		return _aggregateSize;
	}

	public void setAggregateSize(Integer aggregateSize_) {
		_aggregateSize = aggregateSize_;
		
		updateLastUpdateTimestamp();
	}

	public OrderType getOrderType() {
		return _orderType;
	}

	public void setOrderType(OrderType orderType_) {
		_orderType = orderType_;
		
		updateLastUpdateTimestamp();
	}
	
	private void updateLastUpdateTimestamp() {
		_lastUpdateTimestamp = new Timestamp(System.currentTimeMillis());
	}

	public Date getLastUpdateTimestamp() {
		return _lastUpdateTimestamp;
	}

	public void setHasHiddenOrders(Boolean hasHiddenOrders) {
		_hasHiddenOrders = hasHiddenOrders;
		
		updateLastUpdateTimestamp();
	}

	public Boolean getHasHiddenOrders() {
		return _hasHiddenOrders;
	}

	public void setLastUpdateTimestamp(Date lastUpdateTimestamp_) {
		_lastUpdateTimestamp = lastUpdateTimestamp_;
	}
}
