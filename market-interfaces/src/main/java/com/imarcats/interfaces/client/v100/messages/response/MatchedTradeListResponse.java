package com.imarcats.interfaces.client.v100.messages.response;

import com.imarcats.interfaces.client.v100.dto.types.PagedMatchedTradeSideListDto;
import com.imarcats.interfaces.client.v100.messages.MessageBase;

/**
 * Response with an Matched Trade List
 * @author Adam
 *
 */
public class MatchedTradeListResponse extends MessageBase {

	private static final long serialVersionUID = 1L;

	private PagedMatchedTradeSideListDto _matchedTradeList;

	public void setMatchedTradeList(PagedMatchedTradeSideListDto matchedTradeList) {
		_matchedTradeList = matchedTradeList;
	}

	public PagedMatchedTradeSideListDto getMatchedTradeList() {
		return _matchedTradeList;
	}
	
}
