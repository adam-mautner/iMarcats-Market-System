package com.imarcats.model.types;

import java.io.Serializable;
import java.util.Arrays;

import com.imarcats.model.MarketOperator;

/**
 * Paged list of Market Operators 
 * @author Adam
 */
public class PagedMarketOperatorList implements Serializable {

	private static final long serialVersionUID = 1L;

	private MarketOperator[] _marketOperators;
	private String _cursorString;
	private int _maxNumberOfMarketOperatorsOnPage;
	
	public MarketOperator[] getMarketOperators() {
		return _marketOperators;
	}
	public void setMarketOperators(MarketOperator[] marketOperators_) {
		_marketOperators = marketOperators_;
	}
	public String getCursorString() {
		return _cursorString;
	}
	public void setCursorString(String cursorString_) {
		_cursorString = cursorString_;
	}
	public int getMaxNumberOfMarketOperatorsOnPage() {
		return _maxNumberOfMarketOperatorsOnPage;
	}
	public void setMaxNumberOfMarketOperatorsOnPage(
			int maxNumberOfMarketOperatorsOnPage_) {
		_maxNumberOfMarketOperatorsOnPage = maxNumberOfMarketOperatorsOnPage_;
	}
	@Override
	public String toString() {
		return "PagedMarketOperatorList [_cursorString=" + _cursorString
				+ ", _marketOperators=" + Arrays.toString(_marketOperators)
				+ ", _maxNumberOfMarketOperatorsOnPage="
				+ _maxNumberOfMarketOperatorsOnPage + "]";
	}
	
}
