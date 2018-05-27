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
@Table(name="PREVIOUS_CLOSING_QUOTE")
public class PreviousClosingQuote implements MarketModelObject {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * Primary Key of the PreviousClosingQuote
	 * Note: No need for accessing this field directly
	 */	
	@Id
	@GeneratedValue 
	@Column(name="ID")
    private Long _id;
   
	@Embedded
	private Quote _previousClosingQuote;

	public PreviousClosingQuote() {
		super();
	}

	public PreviousClosingQuote(Quote previousClosingQuote_) { 
		super();
		_previousClosingQuote = previousClosingQuote_;
	}

	public void setPreviousClosingQuote(Quote previousClosingQuote) {
		_previousClosingQuote = previousClosingQuote;
	}

	public Quote getPreviousClosingQuote() {
		return _previousClosingQuote;
	}

	@Override
	public String toString() {
		return "PreviousClosingQuote [_previousClosingQuote=" + _previousClosingQuote + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((_id == null) ? 0 : _id.hashCode());
		result = prime
				* result
				+ ((_previousClosingQuote == null) ? 0 : _previousClosingQuote
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
		PreviousClosingQuote other = (PreviousClosingQuote) obj;
		if (_id == null) {
			if (other._id != null)
				return false;
		} else if (!_id.equals(other._id))
			return false;
		if (_previousClosingQuote == null) {
			if (other._previousClosingQuote != null)
				return false;
		} else if (!_previousClosingQuote.equals(other._previousClosingQuote))
			return false;
		return true;
	}
	
}