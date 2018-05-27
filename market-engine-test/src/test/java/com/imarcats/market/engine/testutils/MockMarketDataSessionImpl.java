package com.imarcats.market.engine.testutils;

import java.util.ArrayList;
import java.util.List;

import com.imarcats.interfaces.client.v100.notification.MarketDataChange;
import com.imarcats.internal.server.infrastructure.marketdata.MarketDataSource;
import com.imarcats.internal.server.infrastructure.marketdata.MarketDataSessionImpl;

public class MockMarketDataSessionImpl extends MarketDataSessionImpl {
	private final List<MarketDataChange> _dataChanges = new ArrayList<MarketDataChange>();

	public MockMarketDataSessionImpl(MarketDataSource marketDataSource_) {
		super(marketDataSource_);
	}
	
	protected void notify(MarketDataChange change) {
		_dataChanges.add(change);
	}
	
	public void commit() {
		// clear the market data changes already sent to prevent duplications
		_dataChanges.clear();
	}
	
	public void rollback() {
		_dataChanges.clear();
	}

	public MarketDataChange[] getMarketDataChanges() {
		return _dataChanges.toArray(new MarketDataChange[_dataChanges.size()]);
	}
}
