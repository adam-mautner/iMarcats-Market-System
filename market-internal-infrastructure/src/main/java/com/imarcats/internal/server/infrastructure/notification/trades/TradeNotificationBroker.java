package com.imarcats.internal.server.infrastructure.notification.trades;

import com.imarcats.interfaces.client.v100.dto.MatchedTradeDto;
import com.imarcats.internal.server.infrastructure.notification.NotificationBroker;

/**
 * Brokers Trade Notifications between Components
 * @author Adam
 *
 */
public interface TradeNotificationBroker {

	/**
	 * Notifies all the Listeners about Matched Trades
	 * @param matchedTrades_ Matched Trades
	 */
	public void notifyListeners(MatchedTradeDto[] matchedTrades_);
	
	/**
	 * @return Notification Broker
	 */
	public NotificationBroker getNotificationBroker();
}
