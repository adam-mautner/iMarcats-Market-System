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
@Table(name="STOP_QUOTE")
public class StopQuote  implements MarketModelObject {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	@Column(name="ID")
	private Long _id;
	
	@Embedded
	private Quote _quote;

	public StopQuote() {
		/* for Instantiation */
	}
	
	public StopQuote(Quote quote_) {
		super();
		_quote = quote_;
	}

	public void setQuote(Quote quote) {
		_quote = quote;
	}

	public Quote getQuote() {
		return _quote;
	}

	@Override
	public String toString() {
		return "StopQuote [_id=" + _id + ", _quote=" + _quote + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((_id == null) ? 0 : _id.hashCode());
		result = prime * result + ((_quote == null) ? 0 : _quote.hashCode());
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
		StopQuote other = (StopQuote) obj;
		if (_id == null) {
			if (other._id != null)
				return false;
		} else if (!_id.equals(other._id))
			return false;
		if (_quote == null) {
			if (other._quote != null)
				return false;
		} else if (!_quote.equals(other._quote))
			return false;
		return true;
	}
}
