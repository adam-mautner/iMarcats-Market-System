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
@Table(name="OPENING_QUOTE")
public class OpeningQuote implements MarketModelObject {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * Primary Key of the OpeningQuote
	 * Note: No need for accessing this field directly
	 */	
	@Id
	@GeneratedValue 
	@Column(name="ID")
    private Long _id;

    @Embedded
	private Quote _openingQuote;

	public OpeningQuote() {
		super();
	}

	public OpeningQuote(Quote openingQuote_) {
		super();
		_openingQuote = openingQuote_;
	}

	public Quote getOpeningQuote() {
		return _openingQuote;
	}

	public void setOpeningQuote(Quote openingQuote_) {
		_openingQuote = openingQuote_;
	}

	@Override
	public String toString() {
		return "OpeningQuote [_openingQuote=" + _openingQuote + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((_id == null) ? 0 : _id.hashCode());
		result = prime * result
				+ ((_openingQuote == null) ? 0 : _openingQuote.hashCode());
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
		OpeningQuote other = (OpeningQuote) obj;
		if (_id == null) {
			if (other._id != null)
				return false;
		} else if (!_id.equals(other._id))
			return false;
		if (_openingQuote == null) {
			if (other._openingQuote != null)
				return false;
		} else if (!_openingQuote.equals(other._openingQuote))
			return false;
		return true;
	}
	
}
