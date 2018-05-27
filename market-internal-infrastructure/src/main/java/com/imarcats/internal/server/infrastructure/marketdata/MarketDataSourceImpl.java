package com.imarcats.internal.server.infrastructure.marketdata;

import com.imarcats.interfaces.client.v100.dto.types.MarketDataType;
import com.imarcats.interfaces.client.v100.notification.MarketDataChange;
import com.imarcats.interfaces.client.v100.notification.NotificationType;
import com.imarcats.internal.server.infrastructure.notification.NotificationBroker;
import com.imarcats.model.Market;
import com.imarcats.model.types.DatastoreKey;

/**
 * Implementation for Market Data Source 
 * @author Adam
 */
public class MarketDataSourceImpl implements MarketDataSource {
	
	private final NotificationBroker _broker;

	public MarketDataSourceImpl(NotificationBroker broker_) {
		super();
		_broker = broker_;
	}


	@Override
	public Long addMarketDataChangeListener(String marketCode_,
			MarketDataType marketDataType_,
			PersistedMarketDataChangeListener listener_) {
		return _broker.addListener(
				new DatastoreKey(marketCode_), 
				Market.class, 
				NotificationType.MarketDataChange,
				getMarketDataType(marketDataType_), 
				listener_);
	}

	private String getMarketDataType(MarketDataType marketDataType_) {
		return marketDataType_ != null 
			? marketDataType_.name()
			: "";
	}

	@Override
	public void removeMarketDataChangeListener(Long listenerID_) {
		_broker.removeListener(listenerID_);
	}

	/**
	 * TODO: Consider using a Synchronous Listener Call here (this mainly 
	 * 		 Triggers Stop Orders and sends Market Data to Clients)  
	 */
	public void notifyListeners(MarketDataChange[] marketDataChanges_) {
		for (MarketDataChange marketDataChange : marketDataChanges_) {
			_broker.notifyListeners(new DatastoreKey(marketDataChange.getMarketCode()), 
					Market.class, 
					NotificationType.MarketDataChange,
					marketDataChange.getChangeType().name(), 
					marketDataChange);	
		}
	}

	@Override
	public NotificationBroker getNotificationBroker() {
		return _broker;
	}
}
