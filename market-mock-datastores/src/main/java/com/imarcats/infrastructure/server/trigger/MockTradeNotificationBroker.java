package com.imarcats.infrastructure.server.trigger;

import com.imarcats.interfaces.client.v100.dto.MatchedTradeDto;
import com.imarcats.internal.server.infrastructure.notification.NotificationBroker;
import com.imarcats.internal.server.infrastructure.notification.trades.TradeNotificationBroker;
import com.imarcats.model.MatchedTrade;

public class MockTradeNotificationBroker implements TradeNotificationBroker {

	@Override
	public NotificationBroker getNotificationBroker() {
		return null;
	}

	@Override
	public void notifyListeners(MatchedTradeDto[] matchedTrades_) {

	}

}
