package com.imarcats.interfaces.client.v100.messages.request;

import com.imarcats.interfaces.client.v100.messages.MessageBase;

/**
 * Get Orders for a User
 * @author Adam
 *
 */
public class GetOrdersForUserRequest extends MessageBase {

	private static final long serialVersionUID = 1L;

	private String _cursorString;

	public void setCursorString(String cursorString) {
		_cursorString = cursorString;
	}

	public String getCursorString() {
		return _cursorString;
	}
}
