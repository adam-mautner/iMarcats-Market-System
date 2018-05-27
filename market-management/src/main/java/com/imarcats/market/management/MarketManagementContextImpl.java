package com.imarcats.market.management;

import com.imarcats.internal.server.infrastructure.marketdata.MarketDataSession;
import com.imarcats.internal.server.infrastructure.notification.properties.PropertyChangeSession;
import com.imarcats.internal.server.infrastructure.notification.trades.TradeNotificationSession;

public class MarketManagementContextImpl implements MarketManagementContext {

	private final MarketDataSession _marketDataSession; 
	private final PropertyChangeSession _propertyChangeSession;
	private final TradeNotificationSession _tradeNotificationSession;

	public MarketManagementContextImpl(
			MarketDataSession marketDataSession_, 
			PropertyChangeSession propertyChangeSession_,
			TradeNotificationSession tradeNotificationSession_) {
		super();
		_marketDataSession = marketDataSession_;
		_propertyChangeSession = propertyChangeSession_;
		_tradeNotificationSession = tradeNotificationSession_;
	}

	@Override
	public PropertyChangeSession getPropertyChangeSession() {
		return _propertyChangeSession;
	}

	@Override
	public MarketDataSession getMarketDataSession() {
		return _marketDataSession;
	}

	@Override
	public TradeNotificationSession getTradeNotificationSession() {
		return _tradeNotificationSession;
	}
}
