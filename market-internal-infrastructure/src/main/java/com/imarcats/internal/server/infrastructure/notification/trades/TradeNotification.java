package com.imarcats.internal.server.infrastructure.notification.trades;

import com.imarcats.interfaces.client.v100.dto.MatchedTradeDto;
import com.imarcats.interfaces.client.v100.notification.ListenerCallUserParameters;

/**
 * Information about Trade Notification
 * @author Adam
 */
public class TradeNotification extends ListenerCallUserParameters {

	private static final long serialVersionUID = 1L;
	
	private MatchedTradeDto _trade;

	public TradeNotification(MatchedTradeDto trade_) {
		super();
		_trade = trade_;
	}

	public MatchedTradeDto getTrade() {
		return _trade;
	}
}
