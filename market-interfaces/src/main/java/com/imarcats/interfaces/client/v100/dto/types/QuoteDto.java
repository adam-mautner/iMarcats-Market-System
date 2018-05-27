package com.imarcats.interfaces.client.v100.dto.types;

import java.sql.Timestamp;
import java.util.Date;

/**
 * Defines a Quote on the Market
 * @author Adam
 */
public class QuoteDto implements TransferableObjectDto {
	
	/**
	 * Quote
	 * Required
	 */
	private double _quote;
	
	/**
	 * Date of Quote (Datetime?)
	 * Optional
	 */
	private Timestamp _dateOfQuote;
	
	/**
	 * Tells, if the Quote is Valid
	 * Required
	 */
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
	
}
