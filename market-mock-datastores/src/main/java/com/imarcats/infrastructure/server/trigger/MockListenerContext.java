package com.imarcats.infrastructure.server.trigger;

import com.imarcats.internal.server.infrastructure.datastore.InstrumentDatastore;
import com.imarcats.internal.server.infrastructure.datastore.MarketDatastore;
import com.imarcats.internal.server.infrastructure.datastore.OrderDatastore;
import com.imarcats.internal.server.infrastructure.datastore.ProductDatastore;
import com.imarcats.internal.server.infrastructure.marketdata.MarketDataSession;
import com.imarcats.internal.server.infrastructure.notification.ListenerCallContext;
import com.imarcats.internal.server.infrastructure.notification.properties.PropertyChangeSession;
import com.imarcats.internal.server.infrastructure.notification.trades.TradeNotificationSession;
import com.imarcats.internal.server.infrastructure.testutils.MockDatastoresBase;
import com.imarcats.internal.server.infrastructure.timer.MarketTimer;
import com.imarcats.internal.server.interfaces.order.OrderManagementContext;
import com.imarcats.internal.server.interfaces.order.OrderManagementContextImpl;

public class MockListenerContext implements ListenerCallContext {

	private final MockDatastoresBase _datastores;
	private final MarketDataSession _marketDataSession;
	private final PropertyChangeSession _propertyChangeSession;
	private OrderManagementContextImpl _orderManagementContext;
	private final MockTimeTrigger _timeTrigger;
	private final TradeNotificationSession _tradeNotificationSession; 
	 
	public MockListenerContext(MockDatastoresBase datastores_, 
			MarketDataSession marketDataSession_, 
			PropertyChangeSession propertyChangeSession_,
			TradeNotificationSession tradeNotificationSession_) {
		super();
		_datastores = datastores_;
		_tradeNotificationSession = tradeNotificationSession_; 
		_timeTrigger = new MockTimeTrigger(_datastores); 
		_marketDataSession = marketDataSession_;
		_propertyChangeSession = propertyChangeSession_;
		_orderManagementContext = 
			new OrderManagementContextImpl(
					getMarketDataSession(), 
					getPropertyChangeSession(), 
					getTradeNotificationSession());
	}

	@Override
	public InstrumentDatastore getInstrumentDatastore() {
		return _datastores;
	}

	@Override
	public MarketDatastore getMarketDatastore() {
		return _datastores;
	}

	@Override
	public OrderDatastore getOrderDatastore() {
		return (MockDatastoresBase) _datastores;
	}

	@Override
	public ProductDatastore getProductDatastore() {
		return null;
	}

	@Override
	public MarketDataSession getMarketDataSession() {
		return _marketDataSession;
	}

	@Override
	public OrderManagementContext getOrderManagementContext() {
		return _orderManagementContext;
	}

	@Override
	public PropertyChangeSession getPropertyChangeSession() {
		return _propertyChangeSession;
	}

	@Override
	public MarketTimer getMarketTimer() {
		return _timeTrigger;
	}

	@Override
	public TradeNotificationSession getTradeNotificationSession() {
		return _tradeNotificationSession;
	}
}
