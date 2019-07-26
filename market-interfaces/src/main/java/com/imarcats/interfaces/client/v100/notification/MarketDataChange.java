package com.imarcats.interfaces.client.v100.notification;

import java.util.Date;

import com.imarcats.interfaces.client.v100.dto.types.MarketDataType;
import com.imarcats.interfaces.client.v100.dto.types.QuoteAndSizeDto;
import com.imarcats.interfaces.client.v100.dto.types.QuoteDto;

/**
 * Information for a Market Data Change 
 * @author Adam
 */
public class MarketDataChange extends ListenerCallUserParameters {

	private static final long serialVersionUID = 1L;

	private String _marketCode;
	private MarketDataType _changeType;
	private double _newQuoteValue;
	private boolean _newQuoteValid;
	private int _newQuoteSize;	
	private Boolean _hasHiddenOrders;
	
	private long _quoteChangeDate;
	private ObjectVersion _version;
		
	public MarketDataChange() {
		super();
	}

	public MarketDataChange(String marketCode_, MarketDataType changeType_, QuoteDto newQuote_, Date changeDate_, ObjectVersion version_) {
		_marketCode = marketCode_;
		_changeType = changeType_;
		if(newQuote_ != null) {
			_newQuoteValue = newQuote_.getQuoteValue();
			_newQuoteValid = newQuote_.getValidQuote();
		}
		if(changeDate_ != null) {
			_quoteChangeDate = changeDate_.getTime();
		}
		_version = version_;
	}

	public MarketDataChange(String marketCode_, MarketDataType changeType_, QuoteAndSizeDto newQuote_, Boolean hasHiddenOrders_, Date changeDate_, ObjectVersion version_) {
		this(marketCode_, changeType_, newQuote_.getQuote(), changeDate_, version_);
		if(newQuote_ != null) {
			_newQuoteSize = newQuote_.getSize();
		}
		_hasHiddenOrders = hasHiddenOrders_;
	}
	
	public double getNewQuoteValue() {
		return _newQuoteValue;
	}
	public boolean getNewQuoteValid() {
		return _newQuoteValid;
	}
	public int getNewQuoteSize() {
		return _newQuoteSize;
	}
	public Date getQuoteChangeDate() {
		return new Date(_quoteChangeDate);
	}

	public QuoteAndSizeDto getNewQuoteAndSize() {
		QuoteAndSizeDto quote = new QuoteAndSizeDto();
		quote.setQuote(getNewQuote());
		quote.setSize(_newQuoteSize);
		
		return quote;
	}
	
	public QuoteDto getNewQuote() {
		QuoteDto quote = new QuoteDto();
		quote.setQuoteValue(_newQuoteValue);
		quote.setValidQuote(_newQuoteValid);
		
		return quote;
	}

	public void setNewQuoteAndSize(QuoteAndSizeDto quote) {
		if (quote != null) {			
			setNewQuote(quote.getQuote());
			_newQuoteSize = quote.getSize();
		} else {
			setNewQuote(null);
			_newQuoteSize = 0;
		}
	}
	
	public void setNewQuote(QuoteDto quote) {
		if (quote != null) {			
			_newQuoteValid = quote.getValidQuote();
			_newQuoteValue = quote.getQuoteValue();
		} else {
			_newQuoteValid = false;
			_newQuoteValue = 0;
		}
	}
	
	public MarketDataType getChangeType() {
		return _changeType;
	}

	public String getMarketCode() {
		return _marketCode;
	}

	public Boolean getHasHiddenOrders() {
		return _hasHiddenOrders;
	}

	public ObjectVersion getVersion() {
		return _version;
	}

	// setters for Blaze DS
	public void setMarketCode(String marketCode_) {
		_marketCode = marketCode_;
	}

	public void setChangeType(MarketDataType changeType_) {
		_changeType = changeType_;
	}

	public void setNewQuoteValue(double newQuoteValue_) {
		_newQuoteValue = newQuoteValue_;
	}

	public void setNewQuoteValid(boolean newQuoteValid_) {
		_newQuoteValid = newQuoteValid_;
	}

	public void setNewQuoteSize(int newQuoteSize_) {
		_newQuoteSize = newQuoteSize_;
	}

	public void setHasHiddenOrders(Boolean hasHiddenOrders_) {
		_hasHiddenOrders = hasHiddenOrders_;
	}

	public void setQuoteChangeDate(long quoteChangeDate_) {
		_quoteChangeDate = quoteChangeDate_;
	}

	public void setVersion(ObjectVersion version_) {
		_version = version_;
	}
}
