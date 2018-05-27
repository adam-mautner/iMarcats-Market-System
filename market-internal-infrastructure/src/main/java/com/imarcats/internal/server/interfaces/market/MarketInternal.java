package com.imarcats.internal.server.interfaces.market;

import com.imarcats.interfaces.server.v100.market.MarketInterface;
import com.imarcats.internal.server.infrastructure.marketdata.MarketDataSession;
import com.imarcats.internal.server.infrastructure.timer.MarketTimer;
import com.imarcats.internal.server.interfaces.order.OrderBookInternal;
import com.imarcats.internal.server.interfaces.order.OrderInternal;
import com.imarcats.internal.server.interfaces.order.OrderManagementContext;
import com.imarcats.model.HaltRule;
import com.imarcats.model.Market;
import com.imarcats.model.types.Quote;
import com.imarcats.model.types.QuoteAndSize;

public interface MarketInternal extends MarketInterface {
	
	/**
	 * @return Best Bid on the Market for the System 
	 */
	public QuoteAndSize getBestBidSystem();
	
	/**
	 * @return Best Ask (Offer) on the Market for the System 
	 */
	public QuoteAndSize getBestAskSystem();
	
	/**
	 * Submits an Order to the Market
	 * @param order_ Order to be Submitted
	 * @param orderManagementContext_ Management Context for Order Changes
	 */
	public void submit(OrderInternal order_, OrderManagementContext orderManagementContext_);
	
	/**
	 * Cancels an Order on the Market
	 * @param order_ Order to be Canceled 
	 * @param cancellationCommentLanguageKey_ Language Key for Cancellation Comment
	 * @param orderManagementContext_ Management Context for Order Changes
	 */
	public void cancel(OrderInternal order_, String cancellationCommentLanguageKey_, OrderManagementContext orderManagementContext_);
		
	/**
	 * @return Buy Order Book of the Market
	 */
	public OrderBookInternal getBuyBook();

	/**
	 * @return Sell Order Book of the Market
	 */
	public OrderBookInternal getSellBook();
	
	/**
	 * Records Last Trade on the Market
	 * @param lastTrade_ Last Trade
	 * @param marketDataSession_ Market Data Session
	 */
	public void recordLastTrade(QuoteAndSize lastTrade_, MarketDataSession marketDataSession_);
	
	/**
	 * Records the Opening Quote 
	 * @param openingQuote_ opening quote 
	 * @param marketDataSession_ Market Data Session
	 */
	public void recordOpeningQuote(Quote openingQuote_, MarketDataSession marketDataSession_);
	
	/**
	 * Records the Closing Quote 
	 * @param closingQuote_ closing quote 
	 * @param marketDataSession_ Market Data Session
	 */
	public void recordClosingQuote(Quote closingQuote_, MarketDataSession marketDataSession_);
	
	/**
	 * Opens the Market
	 * @param orderManagementContext_ Management Context for Order Changes
	 */
	public void openMarket(OrderManagementContext orderManagementContext_);
	
	/**
	 * Re-Opens the Market
	 * @param orderManagementContext_ Management Context for Order Changes
	 */
	public void reOpenMarket(OrderManagementContext orderManagementContext_);

	/**
	 * Halts Trading on the Market
	 * @param orderManagementContext_ Management Context for Order Changes
	 */
	public void haltMarket(OrderManagementContext orderManagementContext_);
	
	/**
	 * Closes the Market
	 * @param orderManagementContext_ Management Context for Order Changes
	 */
	public void closeMarket(OrderManagementContext orderManagementContext_);
	
	/**
	 * Issue a Market Call to the Market
	 * @param orderManagementContext_ Management Context for Order Changes
	 */
	public void callMarket(OrderManagementContext orderManagementContext_);
	
	/**
	 * Executes Maintenance usually at End of the Trading Day
	 * @param orderManagementContext_ Management Context for Order Changes
	 */
	public void executeRegularMarketMaintenance(OrderManagementContext orderManagementContext_);
	/**
	 * Executes Maintenance usually after Market Call
	 * @param orderManagementContext_ Management Context for Order Changes
	 */
	public void executeCallMarketMaintenance(OrderManagementContext orderManagementContext_);
	
	/**
	 * @return the Actual Halt Rule of the Market
	 */
	public HaltRule getActualHaltRule();
	
	/**
	 * @return the Order model object of this Order
	 */
	public Market getMarketModel();	
	
	/**
	 * @return Time Trigger 
	 */
	public MarketTimer getTimeTrigger();
}
