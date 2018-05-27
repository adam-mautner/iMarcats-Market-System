package com.imarcats.model.types;

import java.io.Serializable;
import java.util.Arrays;

import com.imarcats.model.Market;

/**
 * Paged list of Markets 
 * @author Adam
 */
public class PagedMarketList implements Serializable {

	private static final long serialVersionUID = 1L;

	private Market[] _markets;
	private String _cursorString;
	private int _maxNumberOfMarketsOnPage;
	
	public Market[] getMarkets() {
		return _markets;
	}
	public void setMarkets(Market[] markets_) {
		_markets = markets_;
	}
	public String getCursorString() {
		return _cursorString;
	}
	public void setCursorString(String cursorString_) {
		_cursorString = cursorString_;
	}
	public int getMaxNumberOfMarketsOnPage() {
		return _maxNumberOfMarketsOnPage;
	}
	public void setMaxNumberOfMarketsOnPage(int maxNumberOfMarketsOnPage_) {
		_maxNumberOfMarketsOnPage = maxNumberOfMarketsOnPage_;
	}
	@Override
	public String toString() {
		return "PagedMarketList [_cursorString=" + _cursorString
				+ ", _markets=" + Arrays.toString(_markets)
				+ ", _maxNumberOfMarketsOnPage=" + _maxNumberOfMarketsOnPage
				+ "]";
	}
	
	
	
}
