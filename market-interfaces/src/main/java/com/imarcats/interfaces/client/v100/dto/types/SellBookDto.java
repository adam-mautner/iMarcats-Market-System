package com.imarcats.interfaces.client.v100.dto.types;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Sell Orders are held in this Sell side Order Book on the Market before they are executed
 * @author Adam
 */
public class SellBookDto implements OrderBookModelDto {

	/**
	 * Orders in the Book stored as OrderBookEntry Objects. Order Book Entries are 
	 * always sorted by Price Preference. Orders in the OrderBookEntry Objects 
	 * are sorted by the Secondary Order Precedence Rule 
	 * Required
	 * 
	 * Note: This should be using explicit ordering clause, but it is not possible here, 
	 * 		 because collection is ordered by a comparator, which is hard to reproduce 
	 * 		 as an ordering clause.
	 */
    private List<SellOrderBookEntryDto> _orderBookEntries = new ArrayList<SellOrderBookEntryDto>();
	
	/**
     * Date and Time, when the Object was Last Updated
     * Required
     */
	private Date _lastUpdateTimestamp;
	
	/**
	 * Version number of the Order Book, updated every time any Order Book field is updated 
	 * If overflown is true version just restarted from 0
	 * Required
	 */
	private Long _versionNumber;

	public List<SellOrderBookEntryDto> getOrderBookEntries() {
		return _orderBookEntries;
	}

	public void setOrderBookEntries(List<SellOrderBookEntryDto> orderBookEntries_) {
		_orderBookEntries = orderBookEntries_;
	}

	public Date getLastUpdateTimestamp() {
		return _lastUpdateTimestamp;
	}

	public void setLastUpdateTimestamp(Date lastUpdateTimestamp_) {
		_lastUpdateTimestamp = lastUpdateTimestamp_;
	}

	public Long getVersionNumber() {
		return _versionNumber;
	}

	public void setVersionNumber(Long version_) {
		_versionNumber = version_;
	}

	@Override
	public OrderBookEntryModelDto get(int index_) {
		return _orderBookEntries.get(index_);
	}

	@Override
	public boolean isEmpty() {
		return _orderBookEntries.isEmpty();
	}

	@Override
	public OrderSide getSide() {
		return OrderSide.Buy;
	}
	
}
