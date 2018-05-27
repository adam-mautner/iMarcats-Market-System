package com.imarcats.internal.server.interfaces.order;

import com.imarcats.interfaces.server.v100.order.OrderBook;
import com.imarcats.internal.server.interfaces.market.MarketInternal;
import com.imarcats.model.OrderBookModel;

/**
 * Internal Functionality of the Order Book 
 * @author Adam
 *
 */
public interface OrderBookInternal extends OrderBook {

	/**
	 * @return if the Book is empty
	 */
	public boolean isEmpty(); 
	
	/**
	 * @return if the Book has only Market Orders
	 */
	public boolean hasOnlyMarketOrders();
	
	/**
	 * Checks, if the Order can be executed in the Book (Does NOT actually execute the Order)
	 * @param orderInternal_ Order to be checked 
	 * @param market_ Market where the Order Execution happens
	 * @return If the Order can be executed in the Book
	 */
	public boolean canOrderBeMatched(OrderInternal orderInternal_, MarketInternal market_);
	
	/**
	 * @return Aggregated size of Market Orders in the Book
	 */
	public int aggregatedSizeOfMarketOrders();
	
	/**
	 * @return Copy of OrderBook to be returned to the Client
	 * Note: The Only the first IMarketServiceInternalFacade.MAX_NUMBER_OF_ORDER_BOOK_ENTRIES_RETURNED entries will be returned and 
	 * 		 the Hidden Orders (DisplayOrder = False) will not be Included 
	 */
	public OrderBookModel getOrderBookForClient();
	
	/**
	 * @return Best Order Book Entry in the Book, the Order which is the first one to Execute
	 */
	public OrderBookEntryInternal getBestOrderBookEntry();
	
	/**
	 * @return Best Order in the Book, or null if the Book is empty
	 */
	public OrderInternal getBestOrder();
	
	/**
	 * Records Submit of an Order to the Book
	 * @param order_
	 * @param orderManagementContext_ Order Management Context 
	 */
	public void recordSubmit(OrderInternal order_, OrderManagementContext orderManagementContext_);
	
	/**
	 * Records Cancel of an Order in the Book
	 * @param order_ Order to be Canceled 
	 * @param cancellationCommentLanguageKey_ Language Key for Cancellation Comment
	 * @param orderManagementContext_ Order Management Context 
	 */
	public void recordCancel(OrderInternal order_, String cancellationCommentLanguageKey_, OrderManagementContext orderManagementContext_);

	/**
	 * Records that the certain portion of the Order has been Executed on the Market
	 * @param order_ Order being Executed
	 * @param executedSize_ Size has been executed
	 * @param orderManagementContext_ Order Management Context 
	 */
	public void recordExecution(OrderInternal order_, int executedSize_, OrderManagementContext orderManagementContext_);
}
