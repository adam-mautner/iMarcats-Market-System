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
@Table(name="LAST_TRADE")
public class LastTrade implements MarketModelObject {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * Primary Key of the LastTrade
	 * Note: No need for accessing this field directly
	 */	
	@Id
	@GeneratedValue 
	@Column(name = "ID")
    private Long _id;

    @Embedded
	private QuoteAndSize _lastTrade;

	public LastTrade() {
		super();
	}

	public LastTrade(QuoteAndSize lastTrade_) {
		super();
		_lastTrade = lastTrade_;
	}

	public void setLastTrade(QuoteAndSize lastTrade) {
		_lastTrade = lastTrade;
	}

	public QuoteAndSize getLastTrade() {
		return _lastTrade;
	}

	@Override
	public String toString() {
		return "LastTrade [_lastTrade=" + _lastTrade + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((_id == null) ? 0 : _id.hashCode());
		result = prime * result
				+ ((_lastTrade == null) ? 0 : _lastTrade.hashCode());
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
		LastTrade other = (LastTrade) obj;
		if (_id == null) {
			if (other._id != null)
				return false;
		} else if (!_id.equals(other._id))
			return false;
		if (_lastTrade == null) {
			if (other._lastTrade != null)
				return false;
		} else if (!_lastTrade.equals(other._lastTrade))
			return false;
		return true;
	}
	
}