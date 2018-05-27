package com.imarcats.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;

import com.imarcats.model.types.OrderSide;

/**
 * Sell Orders are held in this Sell side Order Book on the Market before they are executed
 * @author Adam
 */
@Entity
@Table(name="SELL_BOOK")
public class SellBook implements OrderBookModel {
	
	private static final long serialVersionUID = 1L;

	/**
	 * Primary Key of the SellBook
	 * Note: No need for accessing this field directly
	 */	
	@Id
	@GeneratedValue 
	@Column(name="ID")
    private Long _id;
	
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
	// TODO: We need to keep the original order of the entries here, as sorting them requires a complex logic
	@Column(name="ORDER_BOOK_ENTRIES")
	@OneToMany(cascade=CascadeType.ALL, orphanRemoval=true) 
    private List<SellOrderBookEntry> _orderBookEntries = new ArrayList<SellOrderBookEntry>();
	
	/**
	 * Version of the object in the datastore 
	 */
	@Version
	@Column(name="VERSION")
	private Long _versionNumber;
	
	public List<SellOrderBookEntry> getOrderBookEntries() {
		return _orderBookEntries;
	}

	public void setOrderBookEntries(List<SellOrderBookEntry> orders_) {
		_orderBookEntries = orders_;
	}
	
	@Override
	public OrderSide getSide() {
		return OrderSide.Sell;
	}
	
	@Override
	public OrderBookEntryModel get(int index_) {
		return _orderBookEntries.get(index_);
	}

	@Override
	public boolean isEmpty() {
		return _orderBookEntries == null || 
			   _orderBookEntries.isEmpty();
	}

	@Override
	public int binarySearch(OrderBookEntryModel newEntry_,
			Comparator<OrderBookEntryModel> comparator_) {
		return Collections.binarySearch(_orderBookEntries, newEntry_, comparator_);
	}

	@Override
	public void add(OrderBookEntryModel entry_) {
		_orderBookEntries.add((SellOrderBookEntry) entry_);
	}

	@Override
	public void remove(OrderBookEntryModel entry_) {
		_orderBookEntries.remove(entry_);
	}

	@Override
	public int size() {
		return _orderBookEntries.size();
	}

	@Override
	public void add(int index_, OrderBookEntryModel entry_) {
		_orderBookEntries.add(index_, (SellOrderBookEntry) entry_);
	}
	
	@Override
	public Iterator<OrderBookEntryModel> getEntryIterator() {
		return new BookIterator(_orderBookEntries.iterator());
	}

	@Override
	public Long getVersionNumber() {
		return _versionNumber;
	}

	@Override
	public void setVersionNumber(Long version_) {
		_versionNumber = version_;
	}

	@Override
	public String toString() {
		return "SellBook [_id=" + _id + ", _orderBookEntries="
				+ _orderBookEntries + ", _versionNumber=" + _versionNumber
				+ "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((_id == null) ? 0 : _id.hashCode());
		result = prime
				* result
				+ ((_orderBookEntries == null) ? 0 : _orderBookEntries
						.hashCode());
		result = prime * result
				+ ((_versionNumber == null) ? 0 : _versionNumber.hashCode());
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
		SellBook other = (SellBook) obj;
		if (_id == null) {
			if (other._id != null)
				return false;
		} else if (!_id.equals(other._id))
			return false;
		if (_orderBookEntries == null) {
			if (other._orderBookEntries != null)
				return false;
		} else if (!_orderBookEntries.equals(other._orderBookEntries))
			return false;
		if (_versionNumber == null) {
			if (other._versionNumber != null)
				return false;
		} else if (!_versionNumber.equals(other._versionNumber))
			return false;
		return true;
	}




	private static class BookIterator implements Iterator<OrderBookEntryModel> {
	
		private final Iterator<SellOrderBookEntry> _iterator;

		public BookIterator(Iterator<SellOrderBookEntry> iterator_) {
			super();
			_iterator = iterator_;
		}

		@Override
		public boolean hasNext() {
			return _iterator.hasNext();
		}

		@Override
		public OrderBookEntryModel next() {
			return _iterator.next();
		}

		@Override
		public void remove() {
			new UnsupportedOperationException("Remove is not supported on Book Iterator");
		}
	}
}
