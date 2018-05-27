package com.imarcats.market.engine.matching;

import java.util.ArrayList;
import java.util.List;

import com.imarcats.interfaces.client.v100.exception.MarketRuntimeException;
import com.imarcats.internal.server.interfaces.market.MarketInternal;
import com.imarcats.internal.server.interfaces.order.OrderInternal;
import com.imarcats.internal.server.interfaces.order.OrderManagementContext;
import com.imarcats.market.engine.market.MarketUtils;
import com.imarcats.model.MatchedTrade;
import com.imarcats.model.types.OrderExpirationInstruction;
import com.imarcats.model.types.OrderSide;
import com.imarcats.model.types.OrderType;

/**
 * Implements a Single Price Auction order matching. 
 * @author Adam
 */
public class SinglePriceAuction extends OrderMatcherBase implements OrderMatcher {

	public static final SinglePriceAuction INSTANCE = new SinglePriceAuction();
	
	private SinglePriceAuction() { /* Singleton */ }
	
	/**
	 * Single Price Auction matches orders on Market Call, 
	 * it removes all the matching Order from the Book and 
	 * returns them as Order Pairs
	 */
	@Override
	public MatchedTrade[] matchOnMarketCall(MarketInternal market_, OrderManagementContext orderManagementContext_) {
		List<MatchedTrade> pairs = new ArrayList<MatchedTrade>();
		MatchedOrders pair = matchOrders(market_);
		while (pair != null) {
			MarketUtils.getBook(OrderSide.Buy, market_).recordExecution(pair.getBuyOrder(), pair.getTrade().getMatchedSize(), orderManagementContext_);
			MarketUtils.getBook(OrderSide.Sell, market_).recordExecution(pair.getSellOrder(), pair.getTrade().getMatchedSize(), orderManagementContext_);
			
			pairs.add(pair.getTrade());
			pair = matchOrders(market_);
		}
		
		return pairs.toArray(new MatchedTrade[pairs.size()]);
	}
	
	/**
	 * Single Price Auction does not match on Submit, 
	 * it just puts the Order to the Book on the corresponding side.
	 */
	@Override
	public MatchedTrade[] matchOnSubmit(OrderInternal order_, MarketInternal market_, OrderManagementContext orderManagementContext_) {
		if(order_.getExpirationInstruction() == OrderExpirationInstruction.ImmediateOrCancel) {
			throw MarketRuntimeException.createExceptionWithDetails(
					MarketRuntimeException.IMMEDIATE_OR_CANCEL_ORDER_IS_NOT_SUPPORTED_ON_CALL_MARKET, null, 
					new Object[]{ order_, market_ });
			
		}
		if(order_.getMinimumSizeOfExecution() > 0) {
			throw MarketRuntimeException.createExceptionWithDetails(
					MarketRuntimeException.MINIMUM_EXECUTION_SIZE_NOT_SUPPORTED_ON_CALL_MARKET, null, 
					new Object[]{ order_, market_ });
			
		}
		if(order_.getExecuteEntireOrderAtOnce()) {
			throw MarketRuntimeException.createExceptionWithDetails(
					MarketRuntimeException.ENTIRE_ORDER_AT_ONCE_NOT_SUPPORTED_ON_CALL_MARKET, null, 
					new Object[]{ order_, market_ });
			
		}
		if(order_.getType() == OrderType.Market) {
			int newSize = order_.getRemainingSize();
			int marketOrderSize = MarketUtils.getBook(order_, market_).aggregatedSizeOfMarketOrders();
			int marketOrderSizeOtherSide = MarketUtils.getOppositeSideBook(order_, market_).aggregatedSizeOfMarketOrders();
			
			if((newSize + marketOrderSize) == marketOrderSizeOtherSide) {
				throw MarketRuntimeException.createExceptionWithDetails(
						MarketRuntimeException.MARKET_ORDER_SUBMITTED_CANNOT_BE_FILLED_ON_CALL_MARKET, null, 
						new Object[]{ order_, market_ });
			}
		}
		
		MarketUtils.getBook(order_.getSide(), market_).recordSubmit(order_, orderManagementContext_);
		
		return new MatchedTrade[0];
	}
	
	private MatchedOrders matchOrders(MarketInternal market_) {
		OrderInternal bestBuyOrder = market_.getBuyBook().getBestOrder();
		OrderInternal bestSellOrder = market_.getSellBook().getBestOrder();
		
		return matchOrders(market_, bestBuyOrder, bestSellOrder);
 	}

	/**
	 * No Matching happens on Market Open
	 */
	@Override
	public MatchedTrade[] matchOnMarketOpen(MarketInternal market_, OrderManagementContext orderManagementContext_) {
		return new MatchedTrade[0];
	}

	/**
	 * No Matching happens on Market Re-Open
	 */
	@Override
	public MatchedTrade[] matchOnMarketReOpen(MarketInternal market_, OrderManagementContext orderManagementContext_) {
		return new MatchedTrade[0];
	}
}
