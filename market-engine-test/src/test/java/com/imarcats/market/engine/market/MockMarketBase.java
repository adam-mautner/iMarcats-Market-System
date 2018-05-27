package com.imarcats.market.engine.market;

import com.imarcats.internal.server.infrastructure.marketdata.MarketDataSession;
import com.imarcats.internal.server.infrastructure.timer.MarketTimer;
import com.imarcats.internal.server.interfaces.market.MarketInternal;
import com.imarcats.internal.server.interfaces.order.OrderBookInternal;
import com.imarcats.internal.server.interfaces.order.OrderInternal;
import com.imarcats.internal.server.interfaces.order.OrderManagementContext;
import com.imarcats.model.CircuitBreaker;
import com.imarcats.model.HaltRule;
import com.imarcats.model.Instrument;
import com.imarcats.model.Market;
import com.imarcats.model.types.ActivationStatus;
import com.imarcats.model.types.BusinessCalendar;
import com.imarcats.model.types.ExecutionSystem;
import com.imarcats.model.types.MarketState;
import com.imarcats.model.types.Quote;
import com.imarcats.model.types.QuoteAndSize;
import com.imarcats.model.types.QuoteType;
import com.imarcats.model.types.SecondaryOrderPrecedenceRuleType;
import com.imarcats.model.types.TimeOfDay;
import com.imarcats.model.types.TimePeriod;
import com.imarcats.model.types.TradingSession;

public class MockMarketBase implements MarketInternal {

	@Override
	public ActivationStatus getActivationStatus() {
		 
		return ActivationStatus.Activated;
	}

	@Override
	public QuoteAndSize getBestAskSystem() {
		 
		return null;
	}

	@Override
	public QuoteAndSize getBestBidSystem() {
		 
		return null;
	}

//	@Override
//	public BusinessEntity getBusinessEntity() {
//		 
//		return null;
//	}

	@Override
	public OrderBookInternal getBuyBook() {
		 
		return null;
	}

	@Override
	public double getCommission() {
		 
		return 0;
	}

	@Override
	public String getDescription() {
		 
		return null;
	}

	@Override
	public ExecutionSystem getExecutionSystem() {
		 
		return ExecutionSystem.Combined;
	}

	@Override
	public CircuitBreaker getCircuitBreaker() {
		 
		return null;
	}

	@Override
	public Instrument getInstrument() {
		return null;
	}

	@Override
	public QuoteAndSize getLastTrade() {
		return null;
	}

	@Override
	public String getMarketCode() {
		return null;
	}

	@Override
	public String getMarketOperationContract() {
		return null;
	}

	@Override
	public int getMaximumContractsTraded() {
		return 0;
	}

	@Override
	public int getMinimumContractsTraded() {
		return 0;
	}

	@Override
	public double getMinimumQuoteIncrement() {
		return 0;
	}

	@Override
	public String getName() {
		return null;
	}

	@Override
	public QuoteType getQuoteType() {
		return QuoteType.Price;
	}

	@Override
	public SecondaryOrderPrecedenceRuleType[] getSecondaryOrderPrecedenceRules() {
		return new SecondaryOrderPrecedenceRuleType[]{ SecondaryOrderPrecedenceRuleType.TimePrecedence };
	}

	@Override
	public OrderBookInternal getSellBook() {
		return null;
	}

	@Override
	public MarketState getState() {
		return MarketState.Open;
	}

	@Override
	public TimeOfDay getTradingDayEnd() {
		return null;
	}

	@Override
	public TimePeriod getTradingHours() {
		return null;
	}

	@Override
	public TradingSession getTradingSession() {
		return TradingSession.Continuous;
	}

	@Override
	public void cancel(OrderInternal order_, String cancellationCommentLanguageKey_, OrderManagementContext orderManagementContext_) {
		
		
	}

	@Override
	public void submit(OrderInternal order_, OrderManagementContext orderManagementContext_) {
		
		
	}

	@Override
	public void recordLastTrade(QuoteAndSize lastTrade_, MarketDataSession marketDataSession_) {
		
		
	}

	@Override
	public void closeMarket(OrderManagementContext orderManagementContext_) {
		
		
	}

	@Override
	public void openMarket(OrderManagementContext orderManagementContext_) {
		
		
	}

	@Override
	public void recordClosingQuote(Quote closingQuote_, MarketDataSession marketDataSession_) {
		
		
	}

	@Override
	public void recordOpeningQuote(Quote openingQuote_, MarketDataSession marketDataSession_) {
		
		
	}

	@Override
	public Quote getClosingQuote() {
		
		return null;
	}

	@Override
	public Quote getOpeningQuote() {
		
		return null;
	}

	@Override
	public void haltMarket(OrderManagementContext orderManagementContext_) {
		
		
	}

	@Override
	public void reOpenMarket(OrderManagementContext orderManagementContext_) {
		
		
	}

	@Override
	public void callMarket(OrderManagementContext orderManagementContext_) {
		
		
	}

	@Override
	public HaltRule getActualHaltRule() {
		
		return null;
	}

	@Override
	public Market getMarketModel() {
		
		return null;
	}

	@Override
	public BusinessCalendar getBusinessCalendar() {
		
		return null;
	}

	@Override
	public boolean getAllowHiddenOrders() {
		return false;
	}

	@Override
	public QuoteAndSize getBestAsk() {
		return null;
	}

	@Override
	public QuoteAndSize getBestBid() {
		return null;
	}

	@Override
	public String getMarketTimeZoneID() {
		return null;
	}

	@Override
	public String getCommissionCurrency() {
		return null;
	}

	@Override
	public String getClearingBank() {
		return null;
	}

	@Override
	public MarketTimer getTimeTrigger() {
		return null;
	}

	@Override
	public QuoteAndSize getPreviousLastTrade() {
		return null;
	}

	@Override
	public Quote getPreviousClosingQuote() {
		return null;
	}

	@Override
	public Quote getPreviousOpeningQuote() {
		return null;
	}

	@Override
	public boolean getAllowSizeRestrictionOnOrders() {
		return false;
	}

	@Override
	public void executeCallMarketMaintenance(
			OrderManagementContext orderManagementContext_) {
		// Does nothing
	}

	@Override
	public void executeRegularMarketMaintenance(
			OrderManagementContext orderManagementContext_) {
		// Does nothing
	}
	
}
