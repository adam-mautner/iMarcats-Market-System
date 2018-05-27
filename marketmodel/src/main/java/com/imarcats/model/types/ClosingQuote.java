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
@Table(name="CLOSING_QUOTE")
public class ClosingQuote implements MarketModelObject {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * Primary Key of the ClosingQuote
	 * Note: No need for accessing this field directly
	 */	
	@Id
	@GeneratedValue 
	@Column(name="ID")
    private Long _id;
    		
    @Embedded
	private Quote _closingQuote;

	public ClosingQuote() {
		super();
	}

	public ClosingQuote(Quote closingQuote_) {
		super();
		_closingQuote = closingQuote_;
	}

	public void setClosingQuote(Quote closingQuote) {
		_closingQuote = closingQuote;
	}

	public Quote getClosingQuote() {
		return _closingQuote;
	}

	@Override
	public String toString() {
		return "ClosingQuote [_closingQuote=" + _closingQuote + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((_closingQuote == null) ? 0 : _closingQuote.hashCode());
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
		ClosingQuote other = (ClosingQuote) obj;
		if (_closingQuote == null) {
			if (other._closingQuote != null)
				return false;
		} else if (!_closingQuote.equals(other._closingQuote))
			return false;
		if (_id == null) {
			if (other._id != null)
				return false;
		} else if (!_id.equals(other._id))
			return false;
		return true;
	}
	
}
