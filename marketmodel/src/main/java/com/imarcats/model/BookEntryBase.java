package com.imarcats.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.imarcats.model.types.OrderType;
import com.imarcats.model.types.Quote;

/**
 * Common Base Class for Book Entries 
 * @author Adam
 * TODO: Remove all @Transient fields, when DTOs are created
 */
@Entity
@Table(name="BOOK_ENTRY_BASE")
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
public abstract class BookEntryBase implements OrderBookEntryModel {

	private static final long serialVersionUID = 1L;

	/**
	 * Primary Key of the BookEntry
	 * Note: No need for accessing this field directly
	 */	
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	@Column(name="ID")
    private Long _id;
	
	/**
	 * Keys for the Orders in this Order Book Entry, Orders are sorted by 
	 * the Secondary Order Precedence Rule 
	 * Required
	 * 
	 * Note: This should be using explicit ordering clause, but it is not possible here, 
	 * 		 because collection is ordered by a comparator, which is hard to reproduce 
	 * 		 as an ordering clause.
	 */
	// We need to keep the original order of the entries here, as sorting them requires a complex logic
	// TODO: Possibly convert into a list normal entities or enforce order to make it more efficient 
	@ElementCollection(targetClass=Long.class)
    @CollectionTable(name="BOOK_ENTRY_ORDER_IDS")
	@Column(name="ORDER_IDS")
	private List<Long> _orderIDs = new ArrayList<Long>();

	/**
	 * Limit Quote of the Order Book Entry or Null for Market Orders 
	 */
	@Embedded
	private Quote _limitQuote;
	
	/**
	 * Aggregate Size of this Order Book Entry or Null, if it is not calculated
	 */
	@Transient
	private Integer _aggregateSize;
	
	/**
	 * Tells if this Order Book Entry has Hidden Orders or Null, if it is not calculated
	 */
	@Transient
	private Boolean _hasHiddenOrders;
	
	/**
	 * Type of the Orders on this Book Entry
	 */
	@Column(name="ORDER_TYPE")
	@Enumerated(EnumType.STRING) 
	private OrderType _orderType;

	/**
     * Date and Time, when the Object was Last Updated
     * Required
     * TODO: Do we need to keep this entry? Do we need check it in the GUI? 
     */ 
	@Column(name="LAST_UPDATE_TIMESTAMP", nullable=false)
	private Timestamp _lastUpdateTimestamp;
	
	// TODO: Add version
	
	public Quote getLimitQuote() {
		return _limitQuote;
	}

	public void setLimitQuote(Quote limitQuote_) {
		_limitQuote = Quote.createQuote(limitQuote_); // Cloning Quote is needed, because it might be copied from an other persisted Object
	}

	public Integer getAggregateSize() {
		return _aggregateSize;
	}

	public void setAggregateSize(Integer aggregateSize_) {
		_aggregateSize = aggregateSize_;
	}

	public OrderType getOrderType() {
		return _orderType;
	}

	public void setOrderType(OrderType orderType_) {
		_orderType = orderType_;
	}
	
	public void updateLastUpdateTimestamp() {
		_lastUpdateTimestamp = new Timestamp(System.currentTimeMillis());
	}

	@Override
	public Date getLastUpdateTimestamp() {
		return _lastUpdateTimestamp;
	}

	public void setLastUpdateTimestamp(Date lastUpdateTimestamp_) {
		_lastUpdateTimestamp = lastUpdateTimestamp_ != null ? new Timestamp(lastUpdateTimestamp_.getTime()) : null;
	}

	public void setHasHiddenOrders(Boolean hasHiddenOrders) {
		_hasHiddenOrders = hasHiddenOrders;
	}

	public Boolean getHasHiddenOrders() {
		return _hasHiddenOrders;
	}

	public void setOrderKeys(List<Long> orderKeys_) {
		_orderIDs = orderKeys_;
	}

	@Override
	public void addOrderKey(int index_, Long key_, OrderBookModel book_) {
		_orderIDs.add(index_, key_);
		updateLastUpdateTimestamp();
	}

	@Override
	public void addOrderKey(Long key_, OrderBookModel book_) {
		_orderIDs.add(key_);
		updateLastUpdateTimestamp();
	}

	@Override
	public Long getOrderKey(int index_) {
		return _orderIDs.get(index_);
	}

	@Override
	public Iterator<Long> getOrderKeyIterator() {
		return _orderIDs.iterator();
	}

	@Override
	public boolean isOrderKeyListEmpty() {
		return _orderIDs.isEmpty();
	}

	@Override
	public int orderKeyCount() {
		return _orderIDs.size();
	}

	@Override
	public void removeOrderKey(Long key_, OrderBookModel book_) {
		_orderIDs.remove(key_);
		updateLastUpdateTimestamp();
	}

	@Override
	public String toString() {
		return "BookEntryBase [_id=" + _id + ", _orderIDs=" + _orderIDs
				+ ", _limitQuote=" + _limitQuote + ", _aggregateSize="
				+ _aggregateSize + ", _hasHiddenOrders=" + _hasHiddenOrders
				+ ", _orderType=" + _orderType + ", _lastUpdateTimestamp="
				+ _lastUpdateTimestamp + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((_aggregateSize == null) ? 0 : _aggregateSize.hashCode());
		result = prime
				* result
				+ ((_hasHiddenOrders == null) ? 0 : _hasHiddenOrders.hashCode());
		result = prime * result + ((_id == null) ? 0 : _id.hashCode());
		result = prime
				* result
				+ ((_lastUpdateTimestamp == null) ? 0 : _lastUpdateTimestamp
						.hashCode());
		result = prime * result
				+ ((_limitQuote == null) ? 0 : _limitQuote.hashCode());
		result = prime * result
				+ ((_orderIDs == null) ? 0 : _orderIDs.hashCode());
		result = prime * result
				+ ((_orderType == null) ? 0 : _orderType.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BookEntryBase other = (BookEntryBase) obj;
		if (_aggregateSize == null) {
			if (other._aggregateSize != null)
				return false;
		} else if (!_aggregateSize.equals(other._aggregateSize))
			return false;
		if (_hasHiddenOrders == null) {
			if (other._hasHiddenOrders != null)
				return false;
		} else if (!_hasHiddenOrders.equals(other._hasHiddenOrders))
			return false;
		if (_id == null) {
			if (other._id != null)
				return false;
		} else if (!_id.equals(other._id))
			return false;
		if (_lastUpdateTimestamp == null) {
			if (other._lastUpdateTimestamp != null)
				return false;
		} else if (!_lastUpdateTimestamp.equals(other._lastUpdateTimestamp))
			return false;
		if (_limitQuote == null) {
			if (other._limitQuote != null)
				return false;
		} else if (!_limitQuote.equals(other._limitQuote))
			return false;
		if (_orderIDs == null) {
			if (other._orderIDs != null)
				return false;
		} else if (!_orderIDs.equals(other._orderIDs))
			return false;
		if (_orderType != other._orderType)
			return false;
		return true;
	}

	
}
