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
@Table(name="PREVIOUS_LAST_QUOTE")
public class PreviousLastTrade implements MarketModelObject {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * Primary Key of the PreviousLastTrade
	 * Note: No need for accessing this field directly
	 */	
	@Id
	@GeneratedValue 
	@Column(name="ID")
	private Long _id;
    
	@Embedded
	private QuoteAndSize _previousLastTrade;

	public PreviousLastTrade() {
		super();
	}

	public PreviousLastTrade(QuoteAndSize previousLastTrade_) {
		super();
		_previousLastTrade = previousLastTrade_;
	}

	public void setPreviousLastTrade(QuoteAndSize previousLastTrade) {
		_previousLastTrade = previousLastTrade;
	}

	public QuoteAndSize getPreviousLastTrade() {
		return _previousLastTrade;
	}

	@Override
	public String toString() {
		return "PreviousLastTrade [_previousLastTrade=" + _previousLastTrade + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((_id == null) ? 0 : _id.hashCode());
		result = prime
				* result
				+ ((_previousLastTrade == null) ? 0 : _previousLastTrade
						.hashCode());
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
		PreviousLastTrade other = (PreviousLastTrade) obj;
		if (_id == null) {
			if (other._id != null)
				return false;
		} else if (!_id.equals(other._id))
			return false;
		if (_previousLastTrade == null) {
			if (other._previousLastTrade != null)
				return false;
		} else if (!_previousLastTrade.equals(other._previousLastTrade))
			return false;
		return true;
	}
	
}
