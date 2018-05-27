package com.imarcats.interfaces.client.v100.dto.types;

import com.imarcats.interfaces.client.v100.dto.MarketModelObjectDto;



/**
 * Defines a Quote with a Size on the Market
 * @author Adam
 */
public class QuoteAndSizeDto implements MarketModelObjectDto {

	/**
	 * Quote
	 * Required
	 */
	private QuoteDto _quote;
	
	/**
	 * Size with the Quote 
	 * Required
	 */
	private Integer _size;
	
	public QuoteDto getQuote() {
		return _quote;
	}

	public void setQuote(QuoteDto quote_) {
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
