package com.imarcats.model.types;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.imarcats.model.MarketModelObject;

/**
 * This class is needed to have a separate database table for this class
 * @author Adam
 */
@Entity
@Table(name="BID")
public class Bid implements MarketModelObject {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * Primary Key of the Bid
	 * Note: No need for accessing this field directly
	 */	
	@Id
	@GeneratedValue 
	@Column(name="ID")
    private Long _id;
    
    @Embedded
	private QuoteAndSize _bid;

	public Bid() {
		super();
	}

	public Bid(QuoteAndSize bid_) {
		super();
		_bid = bid_;
	}

	public void setBid(QuoteAndSize bid_) {
		_bid = bid_;
	}

	public QuoteAndSize getBid() {
		return _bid;
	}

	@Override
	public String toString() {
		return "Bid [_bid=" + _bid + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((_bid == null) ? 0 : _bid.hashCode());
		result = prime * result + ((_id == null) ? 0 : _id.hashCode());
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
		Bid other = (Bid) obj;
		if (_bid == null) {
			if (other._bid != null)
				return false;
		} else if (!_bid.equals(other._bid))
			return false;
		if (_id == null) {
			if (other._id != null)
				return false;
		} else if (!_id.equals(other._id))
			return false;
		return true;
	}
	
}