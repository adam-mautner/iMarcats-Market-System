package com.imarcats.interfaces.client.v100.messages.response;

import com.imarcats.interfaces.client.v100.dto.MatchedTradeDto;
import com.imarcats.interfaces.client.v100.messages.MessageBase;

/**
 * Response with Matched Trade 
 * @author Adam
 *
 */
public class MatchedTradeResponse extends MessageBase {

	private static final long serialVersionUID = 1L;

	private MatchedTradeDto _matchedTrade;

	public void setMatchedTrade(MatchedTradeDto matchedTrade) {
		_matchedTrade = matchedTrade;
	}

	public MatchedTradeDto getMatchedTrade() {
		return _matchedTrade;
	}
}
