package com.imarcats.internal.server.infrastructure.marketdata;

import com.imarcats.interfaces.client.v100.dto.types.MarketDataType;
import com.imarcats.interfaces.client.v100.notification.MarketDataChange;
import com.imarcats.internal.server.infrastructure.notification.NotificationBroker;

/**
 * Brokers Market Data Changes between Components
 * @author Adam
 */
public interface MarketDataSource {
	
	/**
	 * Adds Market Data Listener to the Market Data Source
	 * @param marketCode_ Code of the Market to be Observed
	 * @param marketDataType_ Type of the Market Data to be Observed, of null for all
	 * @param listener_ Listener
	 * 
	 * @return listener key
	 */
	public Long addMarketDataChangeListener(String marketCode_, 
			MarketDataType marketDataType_, 
			PersistedMarketDataChangeListener listener_);

	/**
	 * Remove a Listener from the Market Data Source
	 * @param listenerID_ Key of the Listener to be removed 
	 */
	public void removeMarketDataChangeListener(Long listenerID_);
	
	/**
	 * Notifies all the Listeners in Ascending Order by Creation Time about Market Data Changes
	 * @param marketDataChanges_ Market Changes
	 */
	public void notifyListeners(MarketDataChange[] marketDataChanges_);

	/**
	 * @return Notification Broker
	 */
	public NotificationBroker getNotificationBroker();
}
