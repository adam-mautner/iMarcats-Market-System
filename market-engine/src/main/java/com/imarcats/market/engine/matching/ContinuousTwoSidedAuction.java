package com.imarcats.market.engine.matching;

import java.util.ArrayList;
import java.util.List;

import com.imarcats.interfaces.client.v100.exception.MarketRuntimeException;
import com.imarcats.interfaces.client.v100.i18n.MarketSystemMessageLanguageKeys;
import com.imarcats.internal.server.interfaces.market.MarketInternal;
import com.imarcats.internal.server.interfaces.order.OrderBookInternal;
import com.imarcats.internal.server.interfaces.order.OrderInternal;
import com.imarcats.internal.server.interfaces.order.OrderManagementContext;
import com.imarcats.market.engine.market.MarketUtils;
import com.imarcats.model.MatchedTrade;
import com.imarcats.model.types.MarketState;
import com.imarcats.model.types.OrderExpirationInstruction;
import com.imarcats.model.types.OrderSide;

/**
 * Implements a Continuous Two Sided Auction order matching.
 * @author Adam
 *
 */
public class ContinuousTwoSidedAuction extends OrderMatcherBase implements OrderMatcher {

	public static final ContinuousTwoSidedAuction INSTANCE = new ContinuousTwoSidedAuction();
	
	private ContinuousTwoSidedAuction() { /* Singleton */ }
	
	/**
	 * Market Call is not supported on Continuous Markets 
	 */
	@Override
	public MatchedTrade[] matchOnMarketCall(MarketInternal market_, OrderManagementContext orderManagementContext_) {
		throw new UnsupportedOperationException("Market call is not supported in ContinuousTwoSidedAuction, and it should never be called.");
	}

	/**
	 * Matches the submitted Order, with standing Orders from the other side Order Book. 
	 * If the Order cannot be matched with the Orders in the Book, it will be submitted to
	 * the Book of the Order side defined on the Order. 
	 * 
	 * Note: If the Market is not Open, places Order in to the Order Book 
	 */
	@Override
	public MatchedTrade[] matchOnSubmit(OrderInternal order_, MarketInternal market_, OrderManagementContext orderManagementContext_) {
		if(market_.getState() != MarketState.Open) {
			if(order_.getExpirationInstruction() == OrderExpirationInstruction.ImmediateOrCancel) {
				throw MarketRuntimeException.createExceptionWithDetails(
						MarketRuntimeException.IMMEDIATE_OR_CANCEL_ORDER_IS_NOT_SUPPORTED_WHEN_THE_MARKET_IS_NOT_OPEN, null, 
						new Object[]{ order_, market_ });
			}
			if(order_.getMinimumSizeOfExecution() > 0) {
				throw MarketRuntimeException.createExceptionWithDetails(
						MarketRuntimeException.MINIMUM_EXECUTION_SIZE_NOT_SUPPORTED_WHEN_THE_MARKET_IS_NOT_OPEN, null, 
						new Object[]{ order_, market_ });
				
			}
			if(order_.getExecuteEntireOrderAtOnce()) {
				throw MarketRuntimeException.createExceptionWithDetails(
						MarketRuntimeException.ENTIRE_ORDER_AT_ONCE_NOT_SUPPORTED_WHEN_THE_MARKET_IS_NOT_OPEN, null, 
						new Object[]{ order_, market_ });
				
			}
		}
		MatchedTrade[] matches = new MatchedTrade[0];
		if(market_.getState() == MarketState.Open) {
			// if market is open matches orders on the market
			matches = matchOrderOnMarket(order_, false, market_, orderManagementContext_);
		} else {
			// if market is not open submits order to the book, unless it is ImmediateOrCancel Order
			if(order_.getExpirationInstruction() != OrderExpirationInstruction.ImmediateOrCancel) {
				MarketUtils.getBook(order_.getSide(), market_).recordSubmit(order_, orderManagementContext_);
			}
		}
		return matches;
	}

	/**
	 * Matches Order on the Market (using the Opposite Side Book), 
	 * - If the Order is  not in the Book yet, it will submit it with the remaining size
	 * Order to the Book (unless it is ImmediateOrCancel Order).
	 * - If the Order is in the Book it will remove the Order, when it is fully executed. 
	 * @param order_ Order to be Matched
	 * @param orderIsInBookAlready_ Tells, if the Order is Already in the Book 
	 * @param market_ Market where the matching happens 
	 * @param orderManagementContext_ Management Context for Order Changes
	 * @return Matched Pairs
	 */
	private MatchedTrade[] matchOrderOnMarket(OrderInternal order_, boolean orderIsInBookAlready_,
			MarketInternal market_, OrderManagementContext orderManagementContext_) {
		
		OrderBookInternal bookForOrder = MarketUtils.getBook(order_.getSide(), market_);
		OrderBookInternal oppositeSideBook = MarketUtils.getOppositeSideBook(order_, market_);
		
		if(doesOrderHaveSizeRestriction(order_) &&
		   !oppositeSideBook.canOrderBeMatched(order_, market_)) {
			submitOrderToBook(order_, bookForOrder, orderIsInBookAlready_,
					orderManagementContext_);
			return new MatchedTrade[0];
		}
		OrderInternal oppositeSideBestOrder = oppositeSideBook.getBestOrder();
		
		List<MatchedTrade> pairs = new ArrayList<MatchedTrade>();
		MatchedOrders pair = 
			matchOrders(market_, 
					getOrderForSide(OrderSide.Buy, order_, oppositeSideBestOrder), 
					getOrderForSide(OrderSide.Sell, order_, oppositeSideBestOrder), 
					oppositeSideBestOrder != null 
						? oppositeSideBestOrder.getSide()
						: null); 
		while (pair != null) {
			
			oppositeSideBook.recordExecution(oppositeSideBestOrder, pair.getTrade().getMatchedSize(), orderManagementContext_);			
			if(orderIsInBookAlready_) {
				bookForOrder.recordExecution(order_, pair.getTrade().getMatchedSize(), orderManagementContext_);
			} else {
				order_.recordExecution(market_, pair.getTrade().getMatchedSize(), orderManagementContext_);
			}
			
			pairs.add(pair.getTrade());
			
			oppositeSideBestOrder = oppositeSideBook.getBestOrder();
			pair = 
				matchOrders(market_, 
						getOrderForSide(OrderSide.Buy, order_, oppositeSideBestOrder), 
						getOrderForSide(OrderSide.Sell, order_, oppositeSideBestOrder), 
						oppositeSideBestOrder != null 
							? oppositeSideBestOrder.getSide()
							: null); 
		}
		
		submitOrderToBook(order_, bookForOrder, orderIsInBookAlready_,
				orderManagementContext_);
		return pairs.toArray(new MatchedTrade[pairs.size()]);
	}

	private void submitOrderToBook(OrderInternal order_,
			OrderBookInternal bookForOrder_, boolean orderIsInBookAlready_,
			OrderManagementContext orderManagementContext_) {
		if(!orderIsInBookAlready_) {
			// submit the the order with the remaining size to the book, unless it is ImmediateOrCancel Order
			if(order_.getRemainingSize() > 0) {
				if(order_.getExpirationInstruction() != OrderExpirationInstruction.ImmediateOrCancel) {
					bookForOrder_.recordSubmit(order_, orderManagementContext_);
				} else {
					
					order_.recordCancel(MarketSystemMessageLanguageKeys.ORDER_CANCELED_IT_WAS_IMMEDIATE_OR_CANCEL_ORDER, orderManagementContext_);
				}
			}
		}
	}

	private boolean doesOrderHaveSizeRestriction(OrderInternal order_) {
		return order_.getExecuteEntireOrderAtOnce() || order_.getMinimumSizeOfExecution() > 0;
	}
	
	/**
	 * Get the Order on the given Side
	 * @param side_ 
	 * @param order1_ 
	 * @param order2_
	 * @return Order on the Specified Side
	 */
	private OrderInternal getOrderForSide(OrderSide side_, OrderInternal order1_, OrderInternal order2_) {
		OrderInternal order = null;
		if(order1_ != null && order1_.getSide() == side_) {
			order = order1_;
		} else if(order2_ != null && order2_.getSide() == side_) {
			order = order2_;
		}
		
		return order;
	}

	/**
	 * Matches Orders on Market Open, takes the Best Sell and Buy Orders and 
	 * start the matching process with earlier one
	 */
	@Override
	public MatchedTrade[] matchOnMarketOpen(MarketInternal market_, OrderManagementContext orderManagementContext_) {
		return matchOnOpenReOpen(market_, orderManagementContext_);
	}

	/**
	 * Matches Orders on Market Re-Open, takes the Best Sell and Buy Orders and 
	 * start the matching process with earlier one
	 */
	@Override
	public MatchedTrade[] matchOnMarketReOpen(MarketInternal market_, OrderManagementContext orderManagementContext_) {
		return matchOnOpenReOpen(market_, orderManagementContext_);
	}
	
	private MatchedTrade[] matchOnOpenReOpen(MarketInternal market_, OrderManagementContext orderManagementContext_) {
		OrderInternal bestBuyOrder = market_.getBuyBook().getBestOrder();
		OrderInternal bestSellOrder = market_.getSellBook().getBestOrder();
		
		List<MatchedTrade> allMatches = new ArrayList<MatchedTrade>();
		
		MatchedTrade[] currentMatches = 
			matchBestOrders(market_, bestBuyOrder, bestSellOrder, orderManagementContext_);
		
		while(currentMatches.length > 0) {
			for (int i = 0; i < currentMatches.length; i++) {
				allMatches.add(currentMatches[i]);	
			}
			
			bestBuyOrder = market_.getBuyBook().getBestOrder();
			bestSellOrder = market_.getSellBook().getBestOrder();
			
			currentMatches = matchBestOrders(market_, bestBuyOrder, bestSellOrder, orderManagementContext_);
		} 
		
		return allMatches.toArray(new MatchedTrade[allMatches.size()]);
	}

	private MatchedTrade[] matchBestOrders(MarketInternal market_,
			OrderInternal bestBuyOrder, OrderInternal bestSellOrder, 
			OrderManagementContext orderManagementContext_) {
		MatchedTrade[] matches = new MatchedTrade[0];
		if(bestBuyOrder != null && bestSellOrder != null) {
			// the later order has the will discriminate among the earlier orders
			// just as it happens when the market is open
			if(bestBuyOrder.getSubmissionDate().after(bestSellOrder.getSubmissionDate())) {
				matches = matchOrderOnMarket(bestBuyOrder, true, market_, orderManagementContext_);
			} else {
				matches = matchOrderOnMarket(bestSellOrder, true, market_, orderManagementContext_);
			}
		}
		return matches;
	}
}
