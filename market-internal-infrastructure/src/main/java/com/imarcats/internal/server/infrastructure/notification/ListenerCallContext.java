package com.imarcats.internal.server.infrastructure.notification;

import com.imarcats.internal.server.infrastructure.datastore.InstrumentDatastore;
import com.imarcats.internal.server.infrastructure.datastore.MarketDatastore;
import com.imarcats.internal.server.infrastructure.datastore.OrderDatastore;
import com.imarcats.internal.server.infrastructure.datastore.ProductDatastore;
import com.imarcats.internal.server.infrastructure.marketdata.MarketDataSession;
import com.imarcats.internal.server.infrastructure.notification.properties.PropertyChangeSession;
import com.imarcats.internal.server.infrastructure.notification.trades.TradeNotificationSession;
import com.imarcats.internal.server.infrastructure.timer.MarketTimer;
import com.imarcats.internal.server.interfaces.order.OrderManagementContext;

/**
 * Context for the Listener calls, provides all the necessary components for 
 * completing the listener call.
 * @author Adam
 */
public interface ListenerCallContext {

	/**
	 * @return Instrument Datastore
	 */
	public InstrumentDatastore getInstrumentDatastore();

	/**
	 * @return Market Datastore
	 */
	public MarketDatastore getMarketDatastore();

	/**
	 * @return Order Datastore
	 */
	public OrderDatastore getOrderDatastore();

	/**
	 * @return Product Datastore
	 */
	public ProductDatastore getProductDatastore();
	
	/**
	 * @return Market Data Session
	 */
	public MarketDataSession getMarketDataSession();
	
	/**
	 * @return the Property Change Session for Order Management
	 */
	public PropertyChangeSession getPropertyChangeSession();
	
	/**
	 * @return the Trade Notification Session for Order Management
	 */
	public TradeNotificationSession getTradeNotificationSession();
	
	/**
	 * @return Order Management Context
	 */
	public OrderManagementContext getOrderManagementContext();
	
	/**
	 * @return Market Timer 
	 */
	public MarketTimer getMarketTimer();
}
