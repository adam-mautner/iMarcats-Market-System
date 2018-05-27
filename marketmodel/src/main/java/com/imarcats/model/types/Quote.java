package com.imarcats.model.types;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import com.imarcats.model.MarketModelObject;

/**
 * Defines a Quote on the Market
 * @author Adam
 */
@Embeddable
public class Quote implements MarketModelObject, TransferableObject {

	private static final long serialVersionUID = 1L;

	/**
	 * Creates a valid Quote object form double quote value
	 * @param quoteValue_ Quote value
	 * @return Quote object
	 */
	public static Quote createQuote(double quoteValue_) {
		Quote quote = new Quote();
		quote.setQuoteValue(quoteValue_);
		quote.setValidQuote(true);
		
		return quote;
	}
	
	/**
	 * Creates a Quote object form a different Quote Object, copies it
	 * @param quoteValue_ Quote 
	 * @return Quote object
	 */	
	public static Quote createQuote(Quote quote_) {
		Quote quote = null;
		if(quote_ != null) {
			quote = new Quote();
			quote.setQuoteValue(quote_.getQuoteValue());
			quote.setValidQuote(quote_.getValidQuote());
		}
		return quote;		
	}
	
	/**
	 * Quote
	 * Required
	 */
	@Column(name="QUOTE", nullable=false)
	private double _quote;
	
	/**
	 * Date of Quote (Datetime?)
	 * Optional
	 */
	@Column(name="DATE_OF_QUOTE")
	private Timestamp _dateOfQuote;
	
	/**
	 * Tells, if the Quote is Valid
	 * Required
	 */
	@Column(name="VALID_QUOTE", nullable=false)
	private boolean _validQuote;

	public double getQuoteValue() {
		return _quote;
	}

	public void setQuoteValue(double quote_) {
		_quote = quote_;
	}

	public Date getDateOfQuote() {
		return _dateOfQuote;
	}

	public void setDateOfQuote(Date dateOfQuote_) {
		_dateOfQuote = dateOfQuote_ != null ? new Timestamp(dateOfQuote_.getTime()) : null;
	}

	public boolean getValidQuote() {
		return _validQuote;
	}

	public void setValidQuote(boolean validQuote_) {
		_validQuote = validQuote_;
	}

	@Override
	public Object getObjectValue() {
		return this;
	}
	
	/**
	 * @return String used in Debug and Exceptions 
	 */
	@Override
	public String toString() {
		return "Quote=[ " + getQuoteValue() + ", " + getValidQuote() + ", " + 
			   getDateOfQuote() + " ]"; 
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((_dateOfQuote == null) ? 0 : _dateOfQuote.hashCode());
		long temp;
		temp = Double.doubleToLongBits(_quote);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + (_validQuote ? 1231 : 1237);
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
		Quote other = (Quote) obj;
		if (_dateOfQuote == null) {
			if (other._dateOfQuote != null)
				return false;
		} else if (!_dateOfQuote.equals(other._dateOfQuote))
			return false;
		if (Double.doubleToLongBits(_quote) != Double
				.doubleToLongBits(other._quote))
			return false;
		if (_validQuote != other._validQuote)
			return false;
		return true;
	}
	
}
