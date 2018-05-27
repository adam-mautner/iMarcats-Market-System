package com.imarcats.internal.server.infrastructure.notification.trades;

import com.imarcats.interfaces.client.v100.dto.MatchedTradeDto;
import com.imarcats.interfaces.client.v100.notification.NotificationType;
import com.imarcats.internal.server.infrastructure.notification.NotificationBroker;
import com.imarcats.model.MatchedTrade;
import com.imarcats.model.types.DatastoreKey;

/**
 * Implementation for Trade Notification Broker 
 * @author Adam
 */
public class TradeNotificationBrokerImpl implements TradeNotificationBroker {
	
	private final NotificationBroker _broker;

	public TradeNotificationBrokerImpl(NotificationBroker broker_) {
		super();
		_broker = broker_;
	}

	@Override
	public NotificationBroker getNotificationBroker() {
		return _broker;
	}

	@Override
	public void notifyListeners(MatchedTradeDto[] matchedTrades_) {
		for (MatchedTradeDto matchedTrade : matchedTrades_) {
			_broker.notifyListeners(new DatastoreKey(matchedTrade.getTransactionID()), 
					MatchedTrade.class, 
					NotificationType.Trade,
					null, 
					new TradeNotification(matchedTrade));
		}
	}

}
