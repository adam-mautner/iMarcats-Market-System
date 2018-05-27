package com.imarcats.internal.server.interfaces.order;

import com.imarcats.interfaces.client.v100.notification.ChangeOrigin;
import com.imarcats.internal.server.infrastructure.marketdata.MarketDataSession;
import com.imarcats.internal.server.infrastructure.notification.properties.PropertyChangeSession;
import com.imarcats.internal.server.infrastructure.notification.trades.TradeNotificationSession;
import com.imarcats.model.Order;
import com.imarcats.model.mutators.PropertyChangeListener;
import com.imarcats.model.types.Property;
import com.imarcats.model.types.OrderState;

/**
 * Context for Order Management, like change, submit and execute Orders  
 * @author Adam
 */
public interface OrderManagementContext extends PropertyChangeListener {
	
	/**
	 * @return the Trade Notification Session for Order Management
	 */
	public TradeNotificationSession getTradeNotificationSession();
	
	/**
	 * @return the Market Data Session for Order Management
	 */
	public MarketDataSession getMarketDataSession();
	
	/**
	 * @return the Property Change Session for Order Management
	 */
	public PropertyChangeSession getPropertyChangeSession();

	/**
	 * Notifies about Order State Change 
	 * @param state_ New State 
	 * @param order_ Order
	 * @param changeOrigin_ Origin of the Change 
	 */
	public void notifyOrderStateChange(OrderState state_, Order order_, ChangeOrigin changeOrigin_);

	/**
	 * Notifies about Order Property Change 
	 * @param property_ New Property
	 * @param order_ Order
	 */
	public void notifyAboutOrderPropertyChange(Property property_, Order order_);
}
