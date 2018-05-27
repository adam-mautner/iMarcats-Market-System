package com.imarcats.internal.server.infrastructure.datastore;

import com.imarcats.internal.server.interfaces.order.OrderInternal;
import com.imarcats.model.Order;
import com.imarcats.model.types.PagedOrderList;

/**
 * Data Access Object (DAO), which stores all the Orders in the System. 
 * Implementation of this interface can be found in the project specific to
 * the platform where the Market Engine is hosted. 
 * 
 * @author Adam
 */
public interface OrderDatastore {

	/**
	 * Creates an Order and saves it to the Datastore
	 * @param orderModel_ Order to be created
	 *
	 * @return Primary Key of the Order 
	 */
	public Long createOrder(Order orderModel_);
	
	/**
	 * Deletes an Order from the Datastore
	 * @param orderKey_ Primary Key of the Order to be Deleted
	 *
	 */
	public void deleteOrder(Long orderKey_);
	
	/**
	 * Deletes all Non-Active (Created, Canceled and Executed) Order from a Market 
	 * @param marketCode_ Market, where the Orders are
	 *
	 * 
	 * Note: This is a System Maintenance function, it must not be offered to the Clients 
	 */
	public void deleteNonActiveOrdersOnMarket(String marketCode_);

	/**
	 * Find all Non-Active (Created, Canceled and Executed) Orders for a Market 
	 * @param marketCode_ Market, where the Orders are
	 *
	 * @return Non-Active Orders 
	 * 
	 * Note: This is a System Maintenance function, it must not be offered to the Clients 
	 */
	public OrderInternal[] findNonActiveOrdersOnMarket(String marketCode_);

	/**
	 * Find all Active (Submitted or WaitingSubmit) Orders for a Market 
	 * @param marketCode_ Market, where the Orders are
	 *
	 * @return Active Orders 
	 * 
	 * Note: This is a System Maintenance function, it must not be offered to the Clients 
	 */
	public OrderInternal[] findActiveOrdersOnMarket(String marketCode_);
	
	/**
	 * Finds the Order by its Primary Key 
	 * @param orderKey_ Primary Key of the Order 
	 *
	 * @return Order 
	 */
	public OrderInternal findOrderBy(Long orderKey_);
	
	/**
	 * Finds the Order by its External Reference (unique with user ID and market code) 
	 * @param externalReference_
	 * @param userID_
	 * @param marketCode_
	 * @return Order
	 */
	public OrderInternal findOrderBy(String externalReference_, String userID_, String marketCode_);

	/**
	 * Finds the Order by its User (sorted by Last Update Time, in reverse order)
	 * @param userID_ User, who created the Order 
	 *
	 * @return Orders 
	 */
	public OrderInternal[] findOrdersBy(String userID_);
	
	/**
	 * Finds the Orders by its User (sorted by Last Update Time, in reverse order)
	 * @param userID_ User, who created the Order 
	 * @param cursorString_ Cursor String for continuing a Paged Query or Null for a new Paged Query
	 * @param maxNumberOfOrderOnPage_ Max Number of Order on a Page   
	 *
	 * @return Paged Order List
	 */
	public PagedOrderList findOrdersFromCursorBy(String userID_, String cursorString_, int maxNumberOfOrderOnPage_);

	/**
	 * Finds the Active (Submitted or WaitingSubmit) Orders by its User 
	 * @param userID_ User, who created the Order 
	 * @param cursorString_ Cursor String for continuing a Paged Query or Null for a new Paged Query
	 * @param maxNumberOfOrderOnPage_ Max Number of Order on a Page   
	 *
	 * @return Paged Order List
	 */
	public PagedOrderList findActiveOrdersFromCursorBy(String userID_, String cursorString_, int maxNumberOfOrderOnPage_);
	
	/**
	 * Finds the Order by its User and Market (sorted by Last Update Time, in reverse order)
	 * @param userID_ User, who created the Order 
	 * @param marketCode_ Market, where the Order was created 
	 *
	 * @return Orders 
	 */
	public OrderInternal[] findOrdersBy(String userID_, String marketCode_);	
	
	/**
	 * Finds the Order by its User and Market (sorted by Last Update Time, in reverse order)
	 * @param userID_ User, who created the Order 
	 * @param marketCode_ Market, where the Order was created 
	 * @param cursorString_ Cursor String for continuing a Paged Query or Null for a new Paged Query
	 * @param maxNumberOfOrderOnPage_ Max Number of Order on a Page    
	 *
	 * @return Paged Order List
	 */
	public PagedOrderList findOrdersFromCursorBy(String userID_, String marketCode_, String cursorString_, int maxNumberOfOrderOnPage_);	
}
