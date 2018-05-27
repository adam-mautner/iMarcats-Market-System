package com.imarcats.internal.server.interfaces.order;

/**
 * Internal Object to Enclose Order Book Entry Size and the fact it has or not Hidden Orders 
 * @author Adam
 *
 */
public class OrderBookEntrySize {

	/**
	 * Aggregate Size of this Order Book Entry or Null, if it is not calculated
	 */
	private final Integer _aggregateSize;
	
	/**
	 * Tells if this Order Book Entry has Hidden Orders or Null, if it is not calculated
	 */
	private final Boolean _hasHiddenOrders;

	public OrderBookEntrySize(Integer aggregateSize_, Boolean hasHiddenOrders_) {
		super();
		_aggregateSize = aggregateSize_;
		_hasHiddenOrders = hasHiddenOrders_;
	}

	public Integer getAggregateSize() {
		return _aggregateSize;
	}

	public Boolean getHasHiddenOrders() {
		return _hasHiddenOrders;
	}
}
