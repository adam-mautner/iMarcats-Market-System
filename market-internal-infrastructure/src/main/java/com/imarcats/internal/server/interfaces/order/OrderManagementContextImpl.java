package com.imarcats.internal.server.interfaces.order;

import java.util.Date;

import com.imarcats.interfaces.client.v100.notification.ChangeOrigin;
import com.imarcats.interfaces.client.v100.notification.ObjectVersion;
import com.imarcats.internal.server.infrastructure.marketdata.MarketDataSession;
import com.imarcats.internal.server.infrastructure.notification.properties.PropertyChangeSession;
import com.imarcats.internal.server.infrastructure.notification.trades.TradeNotificationSession;
import com.imarcats.model.Order;
import com.imarcats.model.OrderPropertyNames;
import com.imarcats.model.mutators.PropertyChange;
import com.imarcats.model.mutators.PropertyValueChange;
import com.imarcats.model.mutators.helpers.OrderStateWrapper;
import com.imarcats.model.types.DatastoreKey;
import com.imarcats.model.types.Property;
import com.imarcats.model.types.ObjectProperty;
import com.imarcats.model.types.OrderState;
import com.imarcats.model.utils.PropertyUtils;

/**
 * Implementation for Order Management Context 
 * @author Adam
 */
public class OrderManagementContextImpl implements OrderManagementContext {

	private final MarketDataSession _marketDataSession; 
	private final PropertyChangeSession _propertyChangeSession;
	private final TradeNotificationSession _tradeNotificationSession;

	public OrderManagementContextImpl(
			MarketDataSession marketDataSession_,
			PropertyChangeSession propertyChangeSession_,
			TradeNotificationSession tradeNotificationSession_) {
		super();
		_marketDataSession = marketDataSession_;
		_propertyChangeSession = propertyChangeSession_;
		_tradeNotificationSession = tradeNotificationSession_; 
	}

	@Override
	public MarketDataSession getMarketDataSession() {
		return _marketDataSession;
	}
	
	@Override
	public PropertyChangeSession getPropertyChangeSession() {
		return _propertyChangeSession;
	}
	
	@Override
	public void notifyAboutOrderPropertyChange(Property property_, Order order_) {
		PropertyChange[] propertyChanges = PropertyUtils.createPropertyChangeList(property_); 
		getPropertyChangeSession().recordPropertyChanges(
				new DatastoreKey(order_.getKey()), 
				Order.class,
				new DatastoreKey(order_.getTargetMarketCode()),
				propertyChanges, 
				order_.getLastUpdateTimestamp(), 
				getVersion(order_),
				order_.getSubmitterID());
	}

	@Override
	public void propertyChanged(Object changedObject_,
			PropertyChange propertyChange_) {
		Order order = (Order) changedObject_; 
		getPropertyChangeSession().recordPropertyChanges(
				new DatastoreKey(order.getKey()), 
				Order.class,
				new DatastoreKey(order.getTargetMarketCode()),
				new PropertyChange[] { propertyChange_ }, 
				order.getLastUpdateTimestamp(), 
				getVersion(order),
				order.getSubmitterID());
	}
	
	@Override
	public void notifyOrderStateChange(OrderState state_, Order order_, ChangeOrigin changeOrigin_) {
		DatastoreKey orderKey = new DatastoreKey(order_.getKey());
		
		ObjectProperty property = new ObjectProperty();
		property.setName(OrderPropertyNames.STATE_PROPERTY);
		property.setValue(new OrderStateWrapper(state_));
		
		PropertyValueChange propertyChange = new PropertyValueChange();
		propertyChange.setProperty(property);
		
		PropertyChange[] propertyChanges = { propertyChange }; 
		
		// for deleted orders, we send the current time 
		// because, the order itself will not be updated 
		// so last update time stamp will be old
		Date lastUpdateTimestamp = 
			state_ != OrderState.Deleted 
				? order_.getLastUpdateTimestamp()
				: new Date();
		
		getPropertyChangeSession().recordPropertyChanges(
				orderKey, Order.class, 				
				new DatastoreKey(order_.getTargetMarketCode()),
				propertyChanges, 
				lastUpdateTimestamp, 
				getVersion(order_),
				order_.getSubmitterID(),
				changeOrigin_);
		
	}

	@Override
	public TradeNotificationSession getTradeNotificationSession() {
		return _tradeNotificationSession;
	}
	
	private ObjectVersion getVersion(Order order_) {
		ObjectVersion version = new ObjectVersion();
		version.setVersion(order_.getVersionNumber());
		
		return version;
	}
}
