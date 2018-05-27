package com.imarcats.model.types;

import java.io.Serializable;
import java.util.Arrays;


/**
 * Paged List of Matched Trades Sides
 * @author Adam
 */
public class PagedMatchedTradeSideList implements Serializable {

	private static final long serialVersionUID = 1L;

	private TradeSide[] _matchedTradeSides;
	private String _cursorString;
	private int _maxNumberOfMatchedTradeSidesOnPage;
	
	public TradeSide[] getMatchedTradeSides() {
		return _matchedTradeSides;
	}
	public void setMatchedTradeSides(TradeSide[] matchedTradeSides_) {
		_matchedTradeSides = matchedTradeSides_;
	}
	public String getCursorString() {
		return _cursorString;
	}
	public void setCursorString(String cursorString_) {
		_cursorString = cursorString_;
	}
	public int getMaxNumberOfMatchedTradeSidesOnPage() {
		return _maxNumberOfMatchedTradeSidesOnPage;
	}
	public void setMaxNumberOfMatchedTradeSidesOnPage(
			int maxNumberOfMatchedTradeSidesOnPage_) {
		_maxNumberOfMatchedTradeSidesOnPage = maxNumberOfMatchedTradeSidesOnPage_;
	}
	@Override
	public String toString() {
		return "PagedMatchedTradeSideList [_cursorString=" + _cursorString
				+ ", _matchedTradeSides=" + Arrays.toString(_matchedTradeSides)
				+ ", _maxNumberOfMatchedTradeSidesOnPage="
				+ _maxNumberOfMatchedTradeSidesOnPage + "]";
	}
	
	
}
