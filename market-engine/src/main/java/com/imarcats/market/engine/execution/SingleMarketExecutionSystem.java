package com.imarcats.market.engine.execution;

import java.util.Date;

import com.imarcats.internal.server.infrastructure.datastore.MatchedTradeDatastore;
import com.imarcats.internal.server.interfaces.market.MarketInternal;
import com.imarcats.internal.server.interfaces.order.OrderInternal;
import com.imarcats.internal.server.interfaces.order.OrderManagementContext;
import com.imarcats.market.engine.market.MarketUtils;
import com.imarcats.market.engine.matching.OrderMatcher;
import com.imarcats.market.engine.matching.OrderMatcherRegistry;
import com.imarcats.market.engine.pricing.OrderPricingRule;
import com.imarcats.market.engine.pricing.OrderPricingRuleRegistry;
import com.imarcats.model.HaltRule;
import com.imarcats.model.MatchedTrade;
import com.imarcats.model.types.ChangeType;
import com.imarcats.model.types.ExecutionSystem;
import com.imarcats.model.types.Quote;

/**
 * Implements a Single Market Order Execution System
 * @author Adam
 */
public class SingleMarketExecutionSystem implements MarketExecutionSystem {

	private final MarketInternal _market;
	private final OrderMatcher _orderMatcher;
	private final OrderPricingRule _orderPricingRule;
	private final MatchedTradeDatastore _tradeDatastore;
	
	public SingleMarketExecutionSystem(MarketInternal market_, MatchedTradeDatastore tradeDatastore_) {
		super();
		_market = market_;
		_tradeDatastore = tradeDatastore_;
		_orderMatcher = OrderMatcherRegistry.getOrderMatcher(_market);
		_orderPricingRule = OrderPricingRuleRegistry.getOrderPricingRule(_market);
	}

	@Override
	public void handleMarketCall(OrderManagementContext orderManagementContext_) {
		MatchedTrade[] matchedTrades = _orderMatcher.matchOnMarketCall(_market, 
				orderManagementContext_);
		handleMatchedTrades(matchedTrades, orderManagementContext_);
	}

	@Override
	public void handleSubmit(OrderInternal order_, OrderManagementContext orderManagementContext_) {
		MatchedTrade[] matchedTrades = _orderMatcher.matchOnSubmit(order_, _market, 
				orderManagementContext_);
		handleMatchedTrades(matchedTrades, orderManagementContext_);
	}

	@Override
	public void handleMarketOpen(OrderManagementContext orderManagementContext_) {
		// Last Trade is deleted before Open to avoid confusion on which trading session the Last Trade is from. 
		_market.recordLastTrade(null, orderManagementContext_.getMarketDataSession());
		// delete previous Opening Quote to avoid confusion
		_market.recordOpeningQuote(null, orderManagementContext_.getMarketDataSession());
		
		MatchedTrade[] matchedTrades = _orderMatcher.matchOnMarketOpen(_market, 
				orderManagementContext_);
		handleMatchedTrades(matchedTrades, orderManagementContext_);

		// open/close price is not meaningful for Call Markets
		if(_market.getExecutionSystem() != ExecutionSystem.CallMarketWithSingleSideAuction) {
			if(matchedTrades.length > 0) {
	
				Quote firstTradeQuote = Quote.createQuote(matchedTrades[0].getTradeQuote());
				if(firstTradeQuote != null) {
					firstTradeQuote.setDateOfQuote(new Date());
				}
				
				_market.recordOpeningQuote(firstTradeQuote, 
						orderManagementContext_.getMarketDataSession());
			} 
		} else {
			// delete last closing quote from market - as it is not meaningful for Call Markets
			_market.recordClosingQuote(null, orderManagementContext_.getMarketDataSession());
		}
	}

	@Override
	public void handleMarketReOpen(OrderManagementContext orderManagementContext_) {
		MatchedTrade[] matchedTrades = _orderMatcher.matchOnMarketReOpen(_market, 
				orderManagementContext_);
		handleMatchedTrades(matchedTrades, orderManagementContext_);
	}
	
	@Override
	public void handleMarketClose(OrderManagementContext orderManagementContext_) {
		// delete previous Closing Quote to avoid confusion
		_market.recordClosingQuote(null, orderManagementContext_.getMarketDataSession());
		
		// open/close price is not meaningful for Call Markets
		if(_market.getExecutionSystem() != ExecutionSystem.CallMarketWithSingleSideAuction) {
			if(_market.getLastTrade() != null) {
				_market.recordClosingQuote(_market.getLastTrade().getQuote(), 
						orderManagementContext_.getMarketDataSession());
			} 
		}
	}
	
	private void handleMatchedTrades(MatchedTrade[] matchedTrades_, OrderManagementContext orderManagementContext_) {
		_orderPricingRule.priceOrders(matchedTrades_, _market, orderManagementContext_);
		
		// save all the matched trades to the datastore 
		for (MatchedTrade matchedTrade : matchedTrades_) {
			if(!MarketUtils.hasValidTradeQuote(matchedTrade)) {
				// throw exception on trades with no valid quote, this should never happen
				throw new IllegalArgumentException("Matched Trade without valid Quote");
			}

			_tradeDatastore.createMatchedTrade(matchedTrade);
		}
		
		// notifies external systems about the matched trades 
		// send matched trades to Settlement and Payment System
		orderManagementContext_.getTradeNotificationSession().recordMatchedTrades(matchedTrades_);
		
		// call market cannot be halted 
		if(_market.getExecutionSystem() != ExecutionSystem.CallMarketWithSingleSideAuction) {
			// check, if the market should be halted
			if(_market.getActualHaltRule() != null && 
			   _market.getOpeningQuote() != null &&
			   _market.getOpeningQuote().getValidQuote()) {
				boolean halt = false;
				
				for (MatchedTrade matchedTrade : matchedTrades_) {
					if(shouldHalt(_market.getActualHaltRule(), matchedTrade)) {
						halt = true;
						break;
					}
				}
				
				if(halt) {
					_market.haltMarket(orderManagementContext_);	
				}
			}
		}
	}

	private boolean shouldHalt(HaltRule haltRule_, MatchedTrade matchedTrade_) {
		boolean halt = false;
		
		if(_market.getOpeningQuote() != null && matchedTrade_.getTradeQuote() != null) {
			double openingValue = _market.getOpeningQuote().getQuoteValue();
			double distance = Math.abs(openingValue - matchedTrade_.getTradeQuote().getQuoteValue());
			
			if(haltRule_.getChangeType() == ChangeType.Absolute) {
				// absolute amount
				halt = (distance >= haltRule_.getQuoteChangeAmount());
			} else {
				// percentage
				halt = distance >= openingValue * haltRule_.getQuoteChangeAmount();
			}
		}
		return halt;
	}
}
