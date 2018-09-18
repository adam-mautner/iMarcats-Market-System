package com.imarcats.internal.server.infrastructure.datastore;

import com.imarcats.model.MarketOperator;
import com.imarcats.model.types.ActivationStatus;
import com.imarcats.model.types.PagedMarketOperatorList;

/**
 * Data Access Object (DAO), which stores all the Market Operators in the System. 
 * Implementation of this interface can be found in the project specific to
 * the platform where the Market Engine is hosted. 
 * 
 * @author Adam
 */
public interface MarketOperatorDatastore {

	/**
	 * Creates a Market Operator
	 * @param marketOperator_ Market Operator to be created
	 *
	 * @return Market Operator Code
	 */
	public String createMarketOperator(MarketOperator marketOperator_);
	
	/**
	 * Finds the Market Operator by its Code (Primary Key)
	 * @param marketOperatorCode_ Code (Primary Key) of the Market Operator
	 *
	 * @return Market Operator
	 */
	public MarketOperator findMarketOperatorByCode(String marketOperatorCode_);
	
	/**
	 * Finds Market Operator by Activation Status
	 * @param activationStatus_ Activation Status
	 * @param cursorString_ Cursor String for continuing a Paged Query or Null for a new Paged Query
	 * @param maxNumberOfMarketOperatorsOnPage_ Max Number of Market Operators on a Page   
	 *
	 * @return Paged Market Operator List
	 */
	public PagedMarketOperatorList findMarketOperatorsFromCursorByActivationStatus(ActivationStatus activationStatus_, String cursorString_, 
			int maxNumberOfMarketOperatorsOnPage_);

	/**
	 * Finds all Market Operators
	 * @param cursorString_ Cursor String for continuing a Paged Query or Null for a new Paged Query
	 * @param maxNumberOfMarketOperatorsOnPage_ Max Number of Market Operators on a Page   
	 *
	 * @return Paged Market Operator List
	 */
	public PagedMarketOperatorList findAllMarketOperatorsFromCursor(String cursorString_, 
			int maxNumberOfMarketOperatorsOnPage_);
	
	
	/**
	 * Finds Market Operator by Business Entity
	 * @param businessEntityCode_ Code of the Business Entity 
	 *
	 * @return Market Operator List
	 */
	public MarketOperator[] findMarketOperatorsByBusinessEntity(String businessEntityCode_);	
	
	/**
	 * Finds Market Operator by User
	 * @param userCode_ Code of the User
	 * @param cursorString_ Cursor String for continuing a Paged Query or Null for a new Paged Query
	 * @param maxNumberOfMarketOperatorsOnPage_ Max Number of Market Operators on a Page   
	 *
	 * @return Paged Market Operator List
	 */
	public PagedMarketOperatorList findMarketOperatorsFromCursorByUser(String userCode_, String cursorString_, int maxNumberOfMarketOperatorsOnPage_);	
	
	/**
	 * Finds Market Operator by User
	 * @param userCode_ Code of the User
	 *
	 * @return Market Operator List
	 */
	public MarketOperator[] findMarketOperatorsByUser(String userCode_);	
	
	
	/**
	 * Deletes a Market Operator
	 * @param marketOperatorCode_ Code of the Market Operator to be deleted 
	 *
	 */
	public void deleteMarketOperator(String marketOperatorCode_);

	/**
	 * Explicitly updates Market Operator in the Datastore with instance provided 
	 * @param changedMarketOperatorModel_ Market Operator to be updated 
	 *
	 * @return the updated object 
	 */
	public MarketOperator updateMarketOperator(MarketOperator changedMarketOperatorModel_);
}
