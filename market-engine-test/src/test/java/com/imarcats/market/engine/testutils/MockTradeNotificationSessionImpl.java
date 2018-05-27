package com.imarcats.market.engine.testutils;

import java.util.ArrayList;
import java.util.List;

import com.imarcats.interfaces.client.v100.dto.MatchedTradeDto;
import com.imarcats.internal.server.infrastructure.notification.trades.TradeNotificationBroker;
import com.imarcats.internal.server.infrastructure.notification.trades.TradeNotificationSessionImpl;

public class MockTradeNotificationSessionImpl extends TradeNotificationSessionImpl {
	private final List<MatchedTradeDto> _matchedTrades = new ArrayList<MatchedTradeDto>();
	
	public MockTradeNotificationSessionImpl(
			TradeNotificationBroker tradeNotificationBroker_) {
		super(tradeNotificationBroker_);
	}

	protected void notify(MatchedTradeDto clonedMatchedTrade) {
		_matchedTrades.add(clonedMatchedTrade);
	}

	public MatchedTradeDto[] getTrades() {
		return _matchedTrades.toArray(new MatchedTradeDto[_matchedTrades.size()]);
	}
	
	public void commit() {
		// clear changes already sent to prevent duplications
		_matchedTrades.clear();
	}
	
	public void rollback() {
		// TODO Auto-generated method stub	
	}
}
