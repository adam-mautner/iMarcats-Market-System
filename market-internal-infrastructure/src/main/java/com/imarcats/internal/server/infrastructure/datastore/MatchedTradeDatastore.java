package com.imarcats.internal.server.infrastructure.datastore;

import com.imarcats.model.MatchedTrade;
import com.imarcats.model.TradeSide;
import com.imarcats.model.types.PagedMatchedTradeSideList;

/**
 * Data Access Object (DAO), which stores all the Matched Trades in the System. 
 * Implementation of this interface can be found in the project specific to
 * the platform where the Market Engine is hosted. 
 * 
 * @author Adam
 * 
 * TODO: Secure this datastore 
 */
public interface MatchedTradeDatastore {

	/**
	 * Creates Matched Trade and saves them to the Datastore
	 * @param matchedTrade_ Matched Trade
	 *
	 * @return Primary Key of the Matched Trade 
	 */
	public Long createMatchedTrade(MatchedTrade matchedTrade_);
	
//	/**
//	 * Deletes an Matched Trade from the Datastore 
//	 * @param matchedTradeKey_ Primary Key of the Matched Trade to be Deleted
//	 *
//	 * 
//	 * Note: This is a System functionality, should be only called after, 
//	 * 		 the Matched Trades are safely stored in the Settlement System. 
//	 * 		 If it is called any other cases, it will result Serious Loss of 
//	 * 		 Client Trades corrupting the operation of Markets 
//	 * 
//	 * TODO: Consider deleting this method and Purge Trades after long time (based on Data Purge Policy)
//	 * 		 
//	 */
//	public void deleteMatchedTrade(Long matchedTradeKey_);
	
	/**
	 * Returns the Matched Trades for a User on a given Market 
	 * Note: This method should be used only internally in the system 
	 * @param userID_
	 * @param marketCode_
	 * @param cursorString_
	 * @param maxNumberOfMatchedTradeSidesOnPage_
	 * @param pm_
	 * @return
	 */
	public TradeSide[] findMatchedTradeByUserAndMarketInternal(String userID_, String marketCode_);	
	
	/**
	 * Returns the Matched Trades for a User
	 * Note: This method should be used only internally in the system 
	 * @param userID_
	 * @param cursorString_
	 * @param maxNumberOfMatchedTradeSidesOnPage_
	 * @param pm_
	 * @return
	 */
	public TradeSide[] findMatchedTradeByUserInternal(String userID_);	
	
	
	
	
//	/**
//	 * Finds the Matched Trade by its Primary Key 
//	 * @param matchedTradeKey_ Primary Key of the Matched Trade
//	 *
//	 * @return Matched Trade 
//	 */
//	public MatchedTrade findMatchedTradeByKey(Long matchedTradeKey_);
	
	/**
	 * Finds the Matched Trade by its Transaction ID 
	 * @param transactionId_ Transaction ID of the Matched Trade
	 *
	 * @return Matched Trade 
	 */
	public MatchedTrade findMatchedTradeByTransactionId(Long transactionId_);
	
	
	/**
	 * Finds the Matched Trade Sides by its User (sorted by Trade Time, in reverse order)
	 * @param userID_ User, who is involved to this Trade	
	 * @param cursorString_ Cursor String for continuing a Paged Query or Null for a new Paged Query
	 * @param maxNumberOfMatchedTradesOnPage_ Max Number of Matched Trades on a Page   
	 *
	 * @return Paged List of Matched Trade Sides
	 */
	public PagedMatchedTradeSideList findMatchedTradeByUser(String userID_, String cursorString_, int maxNumberOfMatchedTradeSidesOnPage_);

	/**
	 * Finds the Matched Trade Sides by its User and Market (sorted by Trade Time, in reverse order)
	 * @param userID_ User, who is involved to this Trade
	 * @param marketCode_ Market, where the Matched Trade was created 
	 * @param cursorString_ Cursor String for continuing a Paged Query or Null for a new Paged Query
	 * @param maxNumberOfMatchedTradesOnPage_ Max Number of Matched Trades on a Page   
	 *
	 * @return Paged List of Matched Trade Sides  
	 */
	public PagedMatchedTradeSideList findMatchedTradeByUserAndMarket(String userID_, String marketCode_, String cursorString_, int maxNumberOfMatchedTradeSidesOnPage_);	
	
	/**
	 * Finds the Matched Trade Side by its Reference, User and Market (sorted by Trade Time, in reverse order)
	 * @param externalReference_ External reference defined by the User
	 * @param userID_ User, who is involved to this Trade
	 * @param marketCode_ Market, where the Matched Trade was created 
	 *
	 * @return Matched Trade Sides  
	 */
	public TradeSide findMatchedTradeByReferenceUserAndMarket(String externalReference_, String userID_, String marketCode_);

	/**
	 * Finds the Matched Trade by its Market
	 * @param marketCode_ Market, where the Matched Trade was created 
	 *
	 * @return Matched Trade  
	 */
	public MatchedTrade[] findAllMatchedTradeByMarket(String marketCode_);	
		
}
