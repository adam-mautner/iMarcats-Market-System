package com.imarcats.interfaces.client.v100.dto.types;

import java.io.Serializable;

import com.imarcats.interfaces.client.v100.dto.MarketOperatorDto;

/**
 * Paged list of Market Operators 
 * @author Adam
 */
public class PagedMarketOperatorListDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private MarketOperatorDto[] _marketOperators;
	private String _cursorString;
	private int _maxNumberOfMarketOperatorsOnPage;
	
	public MarketOperatorDto[] getMarketOperators() {
		return _marketOperators;
	}
	public void setMarketOperators(MarketOperatorDto[] marketOperators_) {
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
}
