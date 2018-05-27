package com.imarcats.interfaces.client.v100.messages.request;

import com.imarcats.interfaces.client.v100.messages.MessageBase;

/**
 * Get Matched Trades for Other User
 * @author Adam
 *
 */
public class GetMatchedTradesForOtherUserAndMarketRequest extends MessageBase {

	private static final long serialVersionUID = 1L;

	private String _userID;
	private String _marketCode;
	private String _cursorString;

	public void setMarketCode(String marketCode) {
		_marketCode = marketCode;
	}

	public String getMarketCode() {
		return _marketCode;
	}

	public void setCursorString(String cursorString) {
		_cursorString = cursorString;
	}

	public String getCursorString() {
		return _cursorString;
	}

	public void setUserID(String userID) {
		_userID = userID;
	}

	public String getUserID() {
		return _userID;
	}
	
}