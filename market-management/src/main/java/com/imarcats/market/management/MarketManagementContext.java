package com.imarcats.market.management;

import com.imarcats.internal.server.infrastructure.marketdata.MarketDataSession;
import com.imarcats.internal.server.infrastructure.notification.properties.PropertyChangeSession;
import com.imarcats.internal.server.infrastructure.notification.trades.TradeNotificationSession;

/**
 * Context for Market Management 
 * @author Adam
 */
public interface MarketManagementContext {

	/**
	 * @return the Market Data Session for Market Management
	 */
	public MarketDataSession getMarketDataSession();
	
	/**
	 * @return the Property Change Session for Market Management
	 */
	public PropertyChangeSession getPropertyChangeSession();

	/**
	 * @return the Trade Notification Session for Order Management
	 */
	public TradeNotificationSession getTradeNotificationSession();
}
