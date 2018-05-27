package com.imarcats.internal.server.infrastructure.notification.trades;

import com.imarcats.model.MatchedTrade;

/**
 * Collects all the Matched Trade Notifications into a Session, so they can be 
 * committed to the Broker 
 * @author Adam
 */
public interface TradeNotificationSession {
	
	/**
	 * Records Matched Trades 
	 * @param trades_ Matched Trades
	 */
	public void recordMatchedTrades(MatchedTrade[] trades_);
}
