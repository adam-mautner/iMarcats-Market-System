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
@Table(name="PREVIOUS_OPENING_QUOTE")
public class PreviousOpeningQuote implements MarketModelObject {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * Primary Key of the PreviousOpeningQuote
	 * Note: No need for accessing this field directly
	 */	
	@Id
	@GeneratedValue 
	@Column(name="ID")
	private Long _id;
    
	@Embedded
	private Quote _previousOpeningQuote;

	public PreviousOpeningQuote() {
		super();
	}

	public PreviousOpeningQuote(Quote previousOpeningQuote_) {
		super();
		_previousOpeningQuote = previousOpeningQuote_;
	}

	public Quote getPreviousOpeningQuote() {
		return _previousOpeningQuote;
	}

	public void setPreviousOpeningQuote(Quote openingQuote_) {
		_previousOpeningQuote = openingQuote_;
	}

	@Override
	public String toString() {
		return "PreviousOpeningQuote [_previousOpeningQuote=" + _previousOpeningQuote + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((_id == null) ? 0 : _id.hashCode());
		result = prime
				* result
				+ ((_previousOpeningQuote == null) ? 0 : _previousOpeningQuote
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
		PreviousOpeningQuote other = (PreviousOpeningQuote) obj;
		if (_id == null) {
			if (other._id != null)
				return false;
		} else if (!_id.equals(other._id))
			return false;
		if (_previousOpeningQuote == null) {
			if (other._previousOpeningQuote != null)
				return false;
		} else if (!_previousOpeningQuote.equals(other._previousOpeningQuote))
			return false;
		return true;
	}
	
}
