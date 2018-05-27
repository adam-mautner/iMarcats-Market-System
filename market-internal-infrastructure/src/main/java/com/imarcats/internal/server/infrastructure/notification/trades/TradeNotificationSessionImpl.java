package com.imarcats.internal.server.infrastructure.notification.trades;

import com.imarcats.interfaces.client.v100.dto.MatchedTradeDto;
import com.imarcats.interfaces.server.v100.dto.mapping.MatchedTradeDtoMapping;
import com.imarcats.model.MatchedTrade;

/**
 * Implementation of Trade Notification Session
 * @author Adam
 *
 */
public class TradeNotificationSessionImpl implements TradeNotificationSession {

	private final TradeNotificationBroker _tradeNotificationBroker;
	
	public TradeNotificationSessionImpl(
			TradeNotificationBroker tradeNotificationBroker_) {
		super();
		_tradeNotificationBroker = tradeNotificationBroker_;
	}

	@Override
	public void recordMatchedTrades(MatchedTrade[] trades_) {
		for (MatchedTrade matchedTrade : trades_) {
			MatchedTradeDto clonedMatchedTrade = cloneMatchedTrade(matchedTrade);
			notify(clonedMatchedTrade);
		}
	}

	protected void notify(MatchedTradeDto clonedMatchedTrade) {
		_tradeNotificationBroker.notifyListeners(new MatchedTradeDto[] { clonedMatchedTrade });
	}

	private MatchedTradeDto cloneMatchedTrade(MatchedTrade matchedTrade_) {
		return MatchedTradeDtoMapping.INSTANCE.toDto(matchedTrade_); 
	}
}
