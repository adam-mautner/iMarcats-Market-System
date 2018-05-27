package com.imarcats.interfaces.client.v100.notification;

import com.imarcats.interfaces.client.v100.dto.types.TradeSideDto;

/**
 * Information about Trade Notification for a User
 * @author Adam
 */
public class UserTradeNotification {

	private static final long serialVersionUID = 1L;
	
	private TradeSideDto _trade;

	public UserTradeNotification(TradeSideDto trade_) {
		super();
		_trade = trade_;
	}

	public TradeSideDto getTrade() {
		return _trade;
	}

	@Override
	public String toString() {
		return "UserTradeNotification [_trade=" + _trade + "]";
	}
	
}
