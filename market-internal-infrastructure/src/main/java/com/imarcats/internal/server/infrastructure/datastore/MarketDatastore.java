package com.imarcats.internal.server.infrastructure.datastore;

import com.imarcats.internal.server.interfaces.market.MarketInternal;
import com.imarcats.model.Market;
import com.imarcats.model.types.ActivationStatus;
import com.imarcats.model.types.PagedMarketList;

/**
 * Data Access Object (DAO), which stores all the Markets in the System. 
 * Implementation of this interface can be found in the project specific to
 * the platform where the Market Engine is hosted. 
 * 
 * @author Adam
 */
public interface MarketDatastore {

	/**
	 * Creates a Market 
	 * @param market_ Market to be created
	 *
	 * @return Market Code
	 */
	public String createMarket(Market market_);
	
	/**
	 * Finds the Market by its Code (Primary Key)
	 * @param orderKey_ Code (Primary Key) of the Market 
	 *
	 * @return Market 
	 */
	public MarketInternal findMarketBy(String marketCode_);
	
	/**
	 * Finds Market Models by Activation Status 
	 * @param activationStatus_ Activation Status
	 * @param cursorString_ Cursor String for continuing a Paged Query or Null for a new Paged Query
	 * @param maxNumberOfMarketsOnPage_ Max Number of Markets on a Page   
	 *
	 * @return Paged Market List
	 */
	public PagedMarketList findMarketModelsFromCursorByActivationStatus(
			ActivationStatus activationStatus_, String cursorString_,
			int maxNumberOfMarketsOnPage_);

	/**
	 * Finds Market Models by Instrument 
	 * @param instrumentCode_ Code of the Instrument
	 * @param cursorString_ Cursor String for continuing a Paged Query or Null for a new Paged Query
	 * @param maxNumberOfMarketsOnPage_ Max Number of Markets on a Page   
	 *
	 * @return Paged Market List
	 */
	public PagedMarketList findMarketModelsFromCursorByInstrument(String instrumentCode_, String cursorString_, int maxNumberOfMarketsOnPage_);
		
	/**
	 * Finds Market Models by Instrument 
	 * @param instrumentCode_ Code of the Instrument
	 *
	 * @return Market List
	 */
	public Market[] findMarketModelsByInstrument(String instrumentCode_);

	/**
	 * Finds Market Models by Market Operator
	 * @param marketOperatorCode_ Code of the Market Operator
	 * @param cursorString_ Cursor String for continuing a Paged Query or Null for a new Paged Query
	 * @param maxNumberOfMarketsOnPage_ Max Number of Markets on a Page   
	 *
	 * @return Paged Market List
	 */
	public PagedMarketList findMarketModelsFromCursorByMarketOperator(String marketOperatorCode_, String cursorString_, int maxNumberOfMarketsOnPage_);
	
	/**
	 * Finds Market Models by Market Operator 
	 * @param marketOperatorCode_ Code of the Market Operator
	 *
	 * @return Market List
	 */
	public Market[] findMarketModelsByMarketOperator(String marketOperatorCode_);
	
	/**
	 * Finds Market Models by Business Entity  
	 * @param businessEntityCode_ Code of the Business Entity
	 *
	 * @return Market List
	 */
	public Market[] findMarketModelsByBusinessEntity(String businessEntityCode_);
	
	/**
	 * Deletes a Market 
	 * @param marketCode_ Code of the Market to be deleted 
	 *
	 */
	public void deleteMarket(String marketCode_);

	/**
	 * Explicitly updates Market in the Datastore with instance provided 
	 * @param changedMarketModel_ Market to be updated 
	 *
	 */
	public Market updateMarket(Market changedMarketModel_);
}
