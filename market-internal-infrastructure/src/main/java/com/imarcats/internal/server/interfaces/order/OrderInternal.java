package com.imarcats.internal.server.interfaces.order;

import java.util.Date;

import com.imarcats.interfaces.server.v100.order.OrderInterface;
import com.imarcats.internal.server.interfaces.market.MarketInternal;
import com.imarcats.model.Order;
import com.imarcats.model.types.Quote;
import com.imarcats.model.types.TradeSide;

/**
 * Internal Functionality of the Order
 * @author Adam
 */
public interface OrderInternal extends OrderInterface {
	
	/**
	 * Current Stop Quote of the Stop Loss Order (Static for Non-Trailing Stop Loss Order)
	 */
	public Quote getCurrentStopQuote();
	
	/**
	 * @return Market, where the Order was submitted
	 * TODO: Decomission ? 
	 */
	public MarketInternal getTargetMarket();
	
	/**
	 * @return Submitter ID of the Order
	 */
	public String getSubmitterID();
	
	/**
	 * @return the Order model object of this Order
	 */
	public Order getOrderModel();
	
	/**
	 * Sets Quote Change Trigger that will submit the Order
	 * @param quoteChangeTriggerKey_ Key of the Trigger that will submit the Order
	 * @param orderManagementContext_ Management Context for Order Changes 
	 */
	public void setQuoteChangeTriggerKey(Long quoteChangeTriggerKey_);
	
	/**
	 * @return Key of the Quote Change Trigger that will submit the Order
	 */
	public Long getQuoteChangeTriggerKey();

	/**
	 * Records that the Order is Waiting to be Submitted to the Market
	 * @param orderManagementContext_ Management Context for Order Changes
	 */
	public void recordSubmitWaiting(OrderManagementContext orderManagementContext_);
	
	/**
	 * Records that the Order has been Submitted to the Market
	 * @param orderManagementContext_ Management Context for Order Changes
	 */
	public void recordSubmit(OrderManagementContext orderManagementContext_);

	/**
	 * Records that the Order is in pending submit state
	 * @param orderManagementContext_ Management Context for Order Changes
	 */
	public void recordPendingSubmit(OrderManagementContext orderManagementContext_);
	
	/**
	 * Records that the Order has been Canceled on the Market
	 * @param cancellationCommentLanguageKey_ Language Key for Cancellation Comment
	 * @param orderManagementContext_ Management Context for Order Changes
	 */
	public void recordCancel(String cancellationCommentLanguageKey_, OrderManagementContext orderManagementContext_);
	
	/**
	 * Records that the certain portion of the Order has been Executed on the Market. 
	 * @param market_ Target Market  
	 * @param executedSize_ Size has been executed
	 * @param orderManagementContext_ Management Context for Order Changes
	 */
	public void recordExecution(MarketInternal targetMarket_, int executedSize_, OrderManagementContext orderManagementContext_);
	
	/**
	 * @param tradeDateTime_ Date Time of the Trade
	 * @return One side of a Trade based on this Order 
	 * 
	 * TODO: Add Trade Date and rename tradeDateTime_ to executionTimestamp_
	 */
	public TradeSide getTradeSide(Date tradeDateTime_);
	
	/**
	 * @return Key of the Expiration Time Trigger Action for the Order, 
	 * 		   this Trigger Action will be called, when the Order expires
	 */
	public Long getExpirationTriggerActionKey();
	
	/**
	 * Sets Key of the Expiration Time Trigger Action for the Order, 
	 * this Trigger Action will be called, when the Order expires
	 * @param timeTriggerAction_ Cancel Time Trigger Action for the Order
	 */
	public void setExpirationTriggerActionKey(Long timeTriggerActionKey_);
	
	/**
	 * For Trailing Stop Orders, records the change in the Stop Quote as the Market moves
	 * @param new Stop Quote
	 */
	public void recordNewStopQuote(Quote newStopQuote_);
	
	/**
	 * Adds Expiration Trigger to the Order on a Market
	 * @param market_ Target Market  
	 * @param orderManagementContext_ Order Management Context
	 * @return Order affected 
	 */
	public OrderInternal addExpirationTrigger(MarketInternal market_,
			OrderManagementContext orderManagementContext_);
	
	/**
	 * Removes Expiration Trigger from the Order on a Market
	 * @param market_ Target Market  
	 * @param orderManagementContext_ Order Management Context
	 * @return Order affected 
	 */
	public OrderInternal removeExpirationTrigger(MarketInternal targetMarket_, 
			OrderManagementContext context_);
	
	/**
	 * Adds Quote Change Trigger to the Order on a Market
	 * @param market_ Target Market 
	 * @param orderManagementContext_ Order Management Context
	 * @return Order affected 
	 */
	public OrderInternal addQuoteChangeTrigger(MarketInternal market_,
			OrderManagementContext orderManagementContext_);
	
	/**
	 * Removes Quote Change Trigger from the Order on a Market
	 * @param market_ Target Market 
	 * @param orderManagementContext_ Order Management Context
	 * @return Order affected 
	 */
	public OrderInternal removeQuoteChangeTrigger(MarketInternal targetMarket_,
			OrderManagementContext orderManagementContext_);
}
