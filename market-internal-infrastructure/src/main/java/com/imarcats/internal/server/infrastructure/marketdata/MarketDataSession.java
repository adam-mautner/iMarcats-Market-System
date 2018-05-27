package com.imarcats.internal.server.infrastructure.marketdata;

import com.imarcats.interfaces.client.v100.dto.types.MarketDataType;
import com.imarcats.interfaces.client.v100.notification.MarketDataChange;
import com.imarcats.interfaces.client.v100.notification.ObjectVersion;
import com.imarcats.model.types.Quote;
import com.imarcats.model.types.QuoteAndSize;

/**
 * Collects all the Market Data Changes into a Session, so they can be 
 * committed to the Market Data Source 
 * @author Adam
 */
public interface MarketDataSession {
	
	/**
	 * Records New Best Bid on the Market
	 * @param marketCode_ Code of the Market 
	 * @param newBestBid_ New Best Bid with Size, null means Market Order, 0 Size means All Orders are Hidden
	 * @param hasHiddenOrders_ Tells,  if there are Hidden Orders
	 */
	public void recordNewBestBid(String marketCode_, QuoteAndSize newBestBid_, Boolean hasHiddenOrders_, ObjectVersion version_);
	
	/**
	 * Records Level II Bid on the Market (Absolute Quote, NOT Delta Quote Value)
	 * @param marketCode_ Code of the Market 
	 * @param newLevelIIBid_ Level II Bid with Size, 
	 * 					  	 Invalid Quote means Quote has been deleted from the Book, 
	 * 					  	 0 Size means All Orders are Hidden, 
	 * 						 Null Quote means Market Order
	 * @param hasHiddenOrders_ Tells,  if there are Hidden Orders
	 */
	public void recordNewLevelIIBid(String marketCode_, QuoteAndSize newLevelIIBid_, Boolean hasHiddenOrders_, ObjectVersion version_);

	/**
	 * Records New Best Ask on the Market
	 * @param marketCode_ Code of the Market 
	 * @param newBestBid_ New Best Ask with Size, null means Market Order, 0 Size means All Orders are Hidden
	 * @param hasHiddenOrders_ Tells,  if there are Hidden Orders
	 */
	public void recordNewBestAsk(String marketCode_, QuoteAndSize newBestAsk_, Boolean hasHiddenOrders_, ObjectVersion version_);
	
	/**
	 * Records Level II Ask on the Market (Absolute Quote, NOT Delta Quote Value)
	 * @param marketCode_ Code of the Market 
	 * @param newLevelIIAsk_ Level II Ask with Size, 
	 * 					  	 Invalid Quote means Quote has been deleted from the Book, 
	 * 					  	 0 Size means All Orders are Hidden, 
	 * 						 Null Quote means Market Order
	 * @param hasHiddenOrders_ Tells,  if there are Hidden Orders
	 */
	public void recordNewLevelIIAsk(String marketCode_, QuoteAndSize newLevelIIAsk_, Boolean hasHiddenOrders_, ObjectVersion version_);
	
	/**
	 * Records New Last Trade on the Market
	 * @param marketCode_ Code of the Market 
	 * @param newLastTrade_ Last Trade
	 */
	public void recordNewLastTrade(String marketCode_, QuoteAndSize newLastTrade_, ObjectVersion version_);
	
	/**
	 * Records the Opening Quote 
	 * @param marketCode_ Code of the Market 
	 * @param openingQuote_ opening quote 
	 */
	public void recordOpeningQuote(String marketCode_, Quote openingQuote_, ObjectVersion version_);
	
	/**
	 * Records the Closing Quote 
	 * @param marketCode_ Code of the Market 
	 * @param closingQuote_ closing quote 
	 */
	public void recordClosingQuote(String marketCode_, Quote closingQuote_, ObjectVersion version_);

	/**
	 * Adds Market Data Listener to the Market Data Source
	 * @param marketCode_ Code of the Market to be Observed
	 * @param marketDataType_ Type of the Market Data to be Observed, of null for all
	 * @param listener_ Listener
	 * @return the Datastore Key for the Listener
	 */
	public Long addMarketDataChangeListener(String marketCode_, 
			MarketDataType marketDataType_, 
			PersistedMarketDataChangeListener listener_);
	
	/**
	 * Remove a Listener from the Market Data Source
	 * @param listenerID_ Key of the Listener to be removed 
	 */
	public void removeMarketDataChangeListener(Long listenerID_);
	
	/**
	 * @return Market Data Source 
	 */
	public MarketDataSource getMarketDataSource();
}
