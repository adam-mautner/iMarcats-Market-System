package com.imarcats.model.types;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;

import com.imarcats.model.MarketModelObject;


/**
 * Defines a Quote with a Size on the Market
 * @author Adam
 */
@Embeddable
public class QuoteAndSize implements MarketModelObject {

	private static final long serialVersionUID = 1L;
    
	/**
	 * Quote
	 * Required
	 */
	@Embedded
	private Quote _quote = new Quote();
	
	/**
	 * Size with the Quote 
	 * Required
	 */
	@Column(name="SIZE", nullable=false)
	private Integer _size;

	/**
	 * Creates a Quote and Size object form a different Quote and Size Object, copies it
	 * @param quoteValue_ Quote and Size 
	 * @return Quote and Size object 
	 */	
	public static QuoteAndSize createQuoteAndSize(QuoteAndSize quoteAndSize_) {
		QuoteAndSize newQuoteAndSize = null;
		if(quoteAndSize_ != null) {
			newQuoteAndSize = new QuoteAndSize();
		
			newQuoteAndSize.setQuote(Quote.createQuote(quoteAndSize_.getQuote()));
			newQuoteAndSize.setSize(quoteAndSize_.getSize());
		}
		return newQuoteAndSize;
	}
	
	public Quote getQuote() {
		return _quote;
	}

	public void setQuote(Quote quote_) {
		_quote = quote_;
	}

	public int getSize() {
		return _size != null ? _size : 0;
	}

	public void setSize(int size_) {
		_size = size_;
	}

	@Override
	public String toString() {
		return "QuoteAndSize [_quote=" + _quote + ", _size=" + _size + "]";
	}
}
