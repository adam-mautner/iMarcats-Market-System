package com.imarcats.market.engine.service;

import com.imarcats.interfaces.client.v100.dto.MarketDto;
import com.imarcats.interfaces.client.v100.dto.OrderDto;
import com.imarcats.interfaces.client.v100.dto.types.OrderBookModelDto;
import com.imarcats.interfaces.client.v100.dto.types.OrderSide;
import com.imarcats.interfaces.client.v100.dto.types.PagedMatchedTradeSideListDto;
import com.imarcats.interfaces.client.v100.dto.types.PagedOrderListDto;
import com.imarcats.interfaces.client.v100.dto.types.PropertyChangeDto;
import com.imarcats.interfaces.client.v100.exception.MarketRuntimeException;
import com.imarcats.interfaces.client.v100.exception.MarketSecurityException;
import com.imarcats.interfaces.client.v100.messages.response.CurrentPositionListResponse;
import com.imarcats.interfaces.client.v100.messages.response.CurrentPositionResponse;
import com.imarcats.internal.server.interfaces.order.OrderManagementContext;


/**
 * Provides Internal Facade for Market Functionality. The Functionalities provided to the client are 
 * implemented using this API.
 * 
 * Note: This is a Low-Level API direct usage of it is discouraged.  
 * @author Adam
 */
public interface MarketServiceInternalFacade {
	
	public static final int MAX_NUMBER_OF_ORDER_BOOK_ENTRIES_RETURNED = 200;
	
	/**
	 * Creates Order on a given Market. Order will be in 
	 * Created state until it is submitted. 
	 * 
	 * @param marketCode_ Code of the Target Market
	 * @param orderDto_ Order to be Created 
	 * @param orderManagementContext_ Context for Order Management
	 * @param user_ User
	 * @return the Key for the newly created Order 
	 * @throws MarketRuntimeException, if the Order cannot be created.
	 * @throws MarketSecurityException if the this method call results security violation.
	 */
	public Long createOrder(String marketCode_, OrderDto orderDto_, OrderManagementContext orderManagementContext_, String userID_);

	/**
	 * Deletes Order 
	 * @param orderKey_ Order to be Cleared
	 * @param orderManagementContext_ Context for Order Management
	 * @param userID_ User 
	 * @throws MarketRuntimeException, if the Order cannot be deleted.
	 * @throws MarketSecurityException, if the user who Queries the Orders is not a Supervisor of the Submitter and the user does not have trade permission on Market
	 */
	public void deleteOrder(Long orderKey_, OrderManagementContext orderManagementContext_, String userID_);
	
	/**
	 * Changes Properties of an Order, if the Order has been submitted, cancels the Order first. 
	 * If the Order is in Executed state the method call results an MarketRuntimeException.
	 * 
	 * If the Property is unknown it will Result a MarketRuntimeException.
	 * 
	 * @param orderKey_ Order to be changed
	 * @param propertyChangesDto_ Property Changes
	 * @param orderManagementContext_ Order Management Context
	 * @param userID_ User
	 * @throws MarketRuntimeException if the properties cannot be changed
	 * @throws MarketSecurityException if the this method call results security violation.
	 */
	public void changeOrderProperties(Long orderKey_, PropertyChangeDto[] propertyChangesDto_, OrderManagementContext orderManagementContext_, String userID_);
	
	/**
	 * Submits the Order to the Market.
	 *  
	 * @param orderKey_ Order to be submitted 
	 * @param orderManagementContext_ Order Management Context
	 * @param user_ User 
	 * @throws MarketRuntimeException if the Order cannot be Submitted.
	 * @throws MarketSecurityException if the this method call results security violation.
	 */
	public void submitOrder(Long orderKey_, OrderManagementContext orderManagementContext_, String userID_);

	/**
	 * Cancels the Order on the Market.
	 *  
	 * @param orderKey_ Order to be canceled 
	 * @param orderManagementContext_ Order Management Context
	 * @param userID_ User 
	 * @throws MarketRuntimeException if the Order cannot be Canceled.
	 * @throws MarketSecurityException, if the user who Queries the Orders is not a Supervisor of the Submitter and the user does not have trade permission on Market
	 */
	public void cancelOrder(Long orderKey_, OrderManagementContext orderManagementContext_, String userID_);
	
	/**
	 * Gets Orders for a given User on the given Market 
	 * @param marketCode_ Code of the Market
	 * @param cursorString_ Cursor String for continuing a Paged Query or Null for a new Paged Query
	 * @param maxNumberOfOrderOnPage_ Max Number of Order on a Page
	 * 
	 * @param userID_ User 
	 * 
	 * @return Orders for the User on the given Market 
	 */
	public PagedOrderListDto getOrdersForUserAndMarket(String marketCode_, String cursorString_, int maxNumberOfOrderOnPage_, String userID_);
	
	/**
	 * Gets All Orders for a given User 
	 * @param cursorString_ Cursor String for continuing a Paged Query or Null for a new Paged Query
	 * @param maxNumberOfOrderOnPage_ Max Number of Order on a Page
	 * @param pm_Persistence Manager 
	 * @param userID_ User 
	 * 
	 * @return Orders for the User 
	 */
	public PagedOrderListDto getOrdersForUser(String cursorString_, int maxNumberOfOrderOnPage_, String userID_);
	
	/**
	 * Gets Order for a given Key
	 * 
	 * @param orderKey_ Key of the Order 
	 * @return Order for the Key
	 */	
	public OrderDto getOrder(Long orderKey_); 
	
	/**
	 * Gets Matched Trade Sides for a given User on the given Market 
	 * @param marketCode_ ID of the Market
	 * @param cursorString_ Cursor String for continuing a Paged Query or Null for a new Paged Query
	 * @param maxNumberOfTradesOnPage_ Max Number of Trades on a Page  
	 * @param user_ User 
	 * 
	 * @return Trades for the User 
	 */
	public PagedMatchedTradeSideListDto getMatchedTradesForUserAndMarket(String marketCode_, String cursorString_, int maxNumberOfTradesOnPage_, String userID_);	
	
	/**
	 * Gets All Matched Trade Sides for a given User 
	 * 
	 * @param cursorString_ Cursor String for continuing a Paged Query or Null for a new Paged Query
	 * @param maxNumberOfTradesOnPage_ Max Number of Trade on a Page  
	 * @param userID_ User 
	 * 
	 * @return Orders for the User 
	 */
	public PagedMatchedTradeSideListDto getMatchedTradesForUser(String cursorString_, int maxNumberOfTradesOnPage_, String userID_);	
	
	/**
	 * Gets Position for User on the given Market 
	 * @param marketCode_ ID of the Market
	 * @param userID_ User 
	 * @return Position for the User 
	 */
	public CurrentPositionResponse getPosistionForUserAndMarket(String marketCode_, String userID_);
	
	/**
	 * Gets Position for User
	 * 
	 * @param userID_ User 
	 * @return Position for the User 
	 */
	public CurrentPositionListResponse getPosistionForUser(String userID_);
	
	
	/**
	 * Finds a Market by Code. 
	 * @param marketCode_ Code of the Market
	 * @return Market or null, if it cannot be found
	 */
	public MarketDto findMarketByCode(String marketCode_);
	
	/**
	 * Returns a the Order Book of the Market for the given side.
	 * 
	 * Note: Book returned will have less Entries then MAX_NUMBER_OF_ORDER_BOOK_ENTRIES_RETURNED.
	 * 
	 * @param marketCode_ Code of the Market
	 * @param orderSide_ Side of the Book 
	 * @return Order Book of the Market 
	 */
	public OrderBookModelDto getOrderBookFor(String marketCode_, OrderSide orderSide_);

}
