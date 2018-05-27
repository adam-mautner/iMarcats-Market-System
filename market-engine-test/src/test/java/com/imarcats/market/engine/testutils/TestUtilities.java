package com.imarcats.market.engine.testutils;

import com.imarcats.infrastructure.server.trigger.MockMarketDataSource;
import com.imarcats.infrastructure.server.trigger.MockPropertyChangeBroker;
import com.imarcats.infrastructure.server.trigger.MockTradeNotificationBroker;
import com.imarcats.internal.server.infrastructure.datastore.MarketDatastore;
import com.imarcats.internal.server.infrastructure.marketdata.MarketDataSessionImpl;
import com.imarcats.internal.server.infrastructure.notification.properties.PropertyChangeSessionImpl;
import com.imarcats.internal.server.infrastructure.notification.trades.TradeNotificationSessionImpl;
import com.imarcats.internal.server.infrastructure.testutils.MockDatastoresBase;
import com.imarcats.internal.server.interfaces.order.OrderInternal;
import com.imarcats.internal.server.interfaces.order.OrderManagementContext;
import com.imarcats.internal.server.interfaces.order.OrderManagementContextImpl;
import com.imarcats.market.engine.order.OrderImpl;
import com.imarcats.model.Order;

public class TestUtilities {

	private TestUtilities() { /* static utility class */ }

	public static OrderManagementContext createOrderManagerContext() {
		MockDatastoresBase datastores = new MockDatastoresBase();
		MockMarketDataSource marketDataSource = new MockMarketDataSource(datastores); 
		MockPropertyChangeBroker propertyChangeBroker = new MockPropertyChangeBroker(datastores);
		return createOrderManagerContext(marketDataSource, propertyChangeBroker);
	}
	
	public static OrderManagementContextImpl createOrderManagerContext(
			MockMarketDataSource marketDataSource, 
			MockPropertyChangeBroker propertyChangeBroker_) {
		MarketDataSessionImpl marketDataSession = new MockMarketDataSessionImpl(marketDataSource); 
		PropertyChangeSessionImpl propertyChangeSession = new MockPropertyChangeSessionImpl(propertyChangeBroker_);
		TradeNotificationSessionImpl tradeNotificationSessionImpl = new MockTradeNotificationSessionImpl(new MockTradeNotificationBroker());
		
		OrderManagementContextImpl context = 
			new OrderManagementContextImpl(marketDataSession, propertyChangeSession, tradeNotificationSessionImpl);
		return context;
	}
	
	public static OrderInternal wrapOrder(Order order_, MarketDatastore marketDatastore_) {
		order_.updateLastUpdateTimestamp();
		return new OrderImpl(order_, marketDatastore_);
	}
}
