package com.imarcats.interfaces.server.v100.service;

import com.imarcats.interfaces.client.v100.exception.MarketRuntimeException;
import com.imarcats.interfaces.client.v100.exception.MarketSecurityException;
import com.imarcats.interfaces.client.v100.notification.NotificationType;
import com.imarcats.model.OrderBookModel;
import com.imarcats.model.Market;
import com.imarcats.model.Order;
import com.imarcats.model.mutators.PropertyChange;
import com.imarcats.model.types.DatastoreKey;
import com.imarcats.model.types.OrderSide;
import com.imarcats.model.types.PagedMatchedTradeSideList;
import com.imarcats.model.types.PagedOrderList;

/**
 * Provides Facade for Market Functionality. The Functionalities provided to the client are 
 * implemented using this API.
 * 
 * Note: This is a Low-Level API direct usage of it is discouraged.  
 * @author Adam
 */
public interface MarketService {
	
	/**
	 * Creates Order on a given Market. Order will be in 
	 * Created state until it is submitted. 
	 * 
	 * @param marketCode_ Code of the Target Market
	 * @param order_ Order to be Created 
	 * @return the Key for the newly created Order 
	 * @throws MarketRuntimeException, if the Order cannot be created.
	 * @throws MarketSecurityException if the this method call results security violation.
	 */
	public Long createOrder(String marketCode_, Order order_) throws ConnectionException;

	/**
	 * Deletes Order 
	 * @param orderKey_ Order to be Cleared
	 * @throws MarketRuntimeException, if the Order cannot be deleted.
	 * @throws MarketSecurityException if the this method call results security violation.
	 */
	public void deleteOrder(Long orderKey_) throws ConnectionException;
	
	/**
	 * Changes Properties of an Order, if the Order has been submitted, cancels the Order first. 
	 * If the Order is in Executed state the method call results an MarketRuntimeException.
	 * 
	 * If the Property is unknown it will Result a MarketRuntimeException.
	 * 
	 * @param orderKey_ Order to be changed
	 * @param propertyChanges_ Property Changes
	 * @throws MarketRuntimeException if the properties cannot be changed
	 * @throws MarketSecurityException if the this method call results security violation.
	 */
	public void changeOrderProperties(Long orderKey_, PropertyChange[] propertyChanges_) throws ConnectionException;
	
	/**
	 * Submits the Order to the Market.
	 *  
	 * @param orderKey_ Order to be submitted 
	 * @throws MarketRuntimeException if the Order cannot be Submitted.
	 * @throws MarketSecurityException if the this method call results security violation.
	 */
	public void submitOrder(Long orderKey_) throws ConnectionException;

	/**
	 * Cancels the Order on the Market.
	 *  
	 * @param orderKey_ Order to be canceled 
	 * @throws MarketRuntimeException if the Order cannot be Canceled.
	 * @throws MarketSecurityException if the this method call results security violation.
	 */
	public void cancelOrder(Long orderKey_) throws ConnectionException;
	
	/**
	 * Gets Orders for a given User on the given Market 
	 * @param marketCode_ Code of the Market
	 * @param cursorString_ Cursor String for continuing a Paged Query or Null for a new Paged Query
	 * 
	 * @return Orders for the User on the given Market 
	 */
	public PagedOrderList getOrdersForUserAndMarket(String marketCode_, String cursorString_) throws ConnectionException;
	
	/**
	 * Gets All Orders for a given User 
	 * @param cursorString_ Cursor String for continuing a Paged Query or Null for a new Paged Query
	 * 
	 * @return Orders for the User 
	 */
	public PagedOrderList getOrdersForUser(String cursorString_) throws ConnectionException;
	
	/**
	 * Gets Orders for Other User on the given Market 
	 * @param marketCode_ Code of the Market
	 * @param userID_ User 
	 * @param cursorString_ Cursor String for continuing a Paged Query or Null for a new Paged Query
	 * 
	 * @return Orders for the User on the given Market 
	 * @throws MarketSecurityException, if the user who Queries the Orders is not a Supervisor of the User
	 */
	public PagedOrderList getOrdersForOtherUserAndMarket(String marketCode_, String userID_, String cursorString_) throws ConnectionException;
	
	/**
	 * Gets All Orders for Other User 
	 * @param userID_ User 
	 * @param cursorString_ Cursor String for continuing a Paged Query or Null for a new Paged Query
	 * 
	 * @return Orders for the User 
	 * @throws MarketSecurityException, if the user who Queries the Orders is not a Supervisor of the User
	 */
	public PagedOrderList getOrdersForOtherUser(String userID_, String cursorString_) throws ConnectionException;
	
	/**
	 * Gets Order for a given Key
	 * 
	 * @param orderKey_ Key of the Order 
	 * @return Order for the Key
	 * @throws MarketSecurityException, if the user who Queries the Orders is not a Supervisor of the User
	 */	
	public Order getOrder(Long orderKey_) throws ConnectionException; 
	
	/**
	 * Gets Matched Trade Sides for a given User on the given Market 
	 * @param marketCode_ ID of the Market
	 * @param cursorString_ Cursor String for continuing a Paged Query or Null for a new Paged Query
	 * 
	 * @return Trades for the User 
	 */
	public PagedMatchedTradeSideList getMatchedTradesForUserAndMarket(String marketCode_, String cursorString_) throws ConnectionException;	
	
	/**
	 * Gets All Matched Trade Sides for a given User 
	 * @param cursorString_ Cursor String for continuing a Paged Query or Null for a new Paged Query
	 * 
	 * @return Trades for the User 
	 */
	public PagedMatchedTradeSideList getMatchedTradesForUser(String cursorString_) throws ConnectionException;	
	
	
	/**
	 * Gets Matched Trade Sides for Other User on the given Market 
	 * @param marketCode_ ID of the Market
	 * @param userID_ User
	 * @param cursorString_ Cursor String for continuing a Paged Query or Null for a new Paged Query
	 * 
	 * @return Trades for the User 
	 * @throws MarketSecurityException, if the user who Queries the Orders is not a Supervisor of the User
	 */
	public PagedMatchedTradeSideList getMatchedTradesForOtherUserAndMarket(String marketCode_, String userID_, String cursorString_) throws ConnectionException;	
	
	/**
	 * Gets All Matched Trade Sides for Other User 
	 * @param userID_ User 
	 * @param cursorString_ Cursor String for continuing a Paged Query or Null for a new Paged Query
	 * 
	 * @return Orders for the User 
	 * @throws MarketSecurityException, if the user who Queries the Orders is not a Supervisor of the User
	 */
	public PagedMatchedTradeSideList getMatchedTradesForOtherUser(String userID_, String cursorString_) throws ConnectionException;	
	
	/**
	 * Finds a Market by Code. 
	 * @param marketCode_ Code of the Market
	 * @return Market or null, if it cannot be found
	 */
	public Market findMarketByCode(String marketCode_) throws ConnectionException;
	
	/**
	 * Returns a the Order Book of the Market for the given side.
	 * 
	 * Note: Book returned will have less Entries then MAX_NUMBER_OF_ORDER_BOOK_ENTRIES_RETURNED.
	 * 
	 * @param marketCode_ Code of the Market
	 * @param orderSide_ Side of the Book 
	 * @return Order Book of the Market 
	 */
	public OrderBookModel getOrderBookFor(String marketCode_, OrderSide orderSide_) throws ConnectionException;

	/**
	 * Subscribes the Client to observe changes of an Object
	 * @param datastoreKey_ Key of the Object to be Observed
	 * @param observedClass_ Class of the Object to be Observed
	 * @param notificationType_ Type of the Notification
	 * @param filterString_ Filter String
	 * @param callbackUrl_ Call back URL (provided by the client), Messages will be sent here 
	 * @return Key of the Listener 
	 */
	@SuppressWarnings("unchecked")
	public Long subscribe(DatastoreKey datastoreKey_, 
			Class observedClass_, NotificationType notificationType_, String filterString_, 
			String callbackUrl_) throws ConnectionException;
	
	/**
	 * Unsubscribes Client's Listener
	 * @param listenerId_ Listener to be removed
	 */
	public void unsubscribe(Long listenerId_) throws ConnectionException;
}