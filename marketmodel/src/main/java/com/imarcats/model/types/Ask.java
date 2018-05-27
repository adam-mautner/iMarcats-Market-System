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
@Table(name="ASK")
public class Ask implements MarketModelObject {
	
	private static final long serialVersionUID = 1L;

	/**
	 * Primary Key of the Ask
	 * Note: No need for accessing this field directly
	 */	
    @Id
	@GeneratedValue 
	@Column(name="ID")
    private Long _id;
    
    @Embedded
	private QuoteAndSize _ask;

	public Ask() {
		super();
	}

	public Ask(QuoteAndSize ask_) {
		super();
		_ask = ask_;
	}

	public void setAsk(QuoteAndSize ask_) {
		_ask = ask_;
	}

	public QuoteAndSize getAsk() {
		return _ask;
	}

	@Override
	public String toString() {
		return "Ask [_ask=" + _ask + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((_ask == null) ? 0 : _ask.hashCode());
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
		Ask other = (Ask) obj;
		if (_ask == null) {
			if (other._ask != null)
				return false;
		} else if (!_ask.equals(other._ask))
			return false;
		if (_id == null) {
			if (other._id != null)
				return false;
		} else if (!_id.equals(other._id))
			return false;
		return true;
	}
}
