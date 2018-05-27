package com.imarcats.market.engine.market;

import java.util.List;

import com.imarcats.interfaces.client.v100.exception.MarketRuntimeException;
import com.imarcats.interfaces.client.v100.i18n.MarketSystemMessageLanguageKeys;
import com.imarcats.interfaces.client.v100.notification.ChangeOrigin;
import com.imarcats.interfaces.client.v100.notification.ObjectVersion;
import com.imarcats.interfaces.server.v100.validation.OrderValidator;
import com.imarcats.internal.server.infrastructure.datastore.InstrumentDatastore;
import com.imarcats.internal.server.infrastructure.datastore.MarketDatastore;
import com.imarcats.internal.server.infrastructure.datastore.MatchedTradeDatastore;
import com.imarcats.internal.server.infrastructure.datastore.OrderDatastore;
import com.imarcats.internal.server.infrastructure.marketdata.MarketDataSession;
import com.imarcats.internal.server.infrastructure.notification.properties.PropertyChangeSession;
import com.imarcats.internal.server.infrastructure.timer.MarketTimer;
import com.imarcats.internal.server.infrastructure.trigger.ReOpenMarketAction;
import com.imarcats.internal.server.interfaces.market.MarketInternal;
import com.imarcats.internal.server.interfaces.market.MarketPropertyChangeExecutor;
import com.imarcats.internal.server.interfaces.order.OrderBookEntryInternal;
import com.imarcats.internal.server.interfaces.order.OrderBookInternal;
import com.imarcats.internal.server.interfaces.order.OrderInternal;
import com.imarcats.internal.server.interfaces.order.OrderManagementContext;
import com.imarcats.internal.server.interfaces.order.OrderBookEntrySize;
import com.imarcats.market.engine.execution.MarketExecutionSystem;
import com.imarcats.market.engine.execution.SingleMarketExecutionSystem;
import com.imarcats.market.engine.order.OrderBookImpl;
import com.imarcats.model.BuyBook;
import com.imarcats.model.CircuitBreaker;
import com.imarcats.model.HaltRule;
import com.imarcats.model.OrderBookModel;
import com.imarcats.model.Instrument;
import com.imarcats.model.Market;
import com.imarcats.model.Order;
import com.imarcats.model.SellBook;
import com.imarcats.model.types.ActivationStatus;
import com.imarcats.model.types.BusinessCalendar;
import com.imarcats.model.types.DatastoreKey;
import com.imarcats.model.types.ExecutionSystem;
import com.imarcats.model.types.MarketState;
import com.imarcats.model.types.OrderState;
import com.imarcats.model.types.OrderType;
import com.imarcats.model.types.Quote;
import com.imarcats.model.types.QuoteAndSize;
import com.imarcats.model.types.QuoteType;
import com.imarcats.model.types.SecondaryOrderPrecedenceRuleType;
import com.imarcats.model.types.TimeOfDay;
import com.imarcats.model.types.TimePeriod;
import com.imarcats.model.types.TradingSession;

public class MarketImpl implements MarketInternal {

	private final Market _marketModel;
	private final MarketPropertyChangeExecutor _marketPropertyChangeExecutor;
	private final MarketDatastore _marketDatastore;
	private final InstrumentDatastore _instrumentDatastore;
	private final MarketExecutionSystem _executionSystem;
	protected final MarketTimer _timeTrigger;
	
	private OrderBookInternal _sellBook;
	private OrderBookInternal _buyBook;
	private final OrderDatastore _orderDatastore;
	
	public MarketImpl(Market marketModel_, MarketTimer timeTrigger_, 
			OrderDatastore orderDatastore_,
			MarketDatastore marketDatastore_,
			InstrumentDatastore instrumentDatastore_, 
			MatchedTradeDatastore matchedTradeDatastore_) {
		super();
		_marketModel = marketModel_;
		_marketPropertyChangeExecutor = new MarketPropertyChangeExecutor(_marketModel);
		_timeTrigger = timeTrigger_;
		_orderDatastore = orderDatastore_;
		_marketDatastore = marketDatastore_;
		_instrumentDatastore = instrumentDatastore_;
		_executionSystem = 
			new SingleMarketExecutionSystem(this, matchedTradeDatastore_);
	}
	
	@Override
	public Market getMarketModel() {
		return _marketModel;
	}
	
	private static void checkOrderTypeAndQuote(OrderInternal order_,
			MarketInternal market_) {
		if(order_.getType() == OrderType.Market) {
			OrderBookInternal otherSideOrderBook = MarketUtils.getOppositeSideBook(order_, market_);
			if(otherSideOrderBook.isEmpty()) {
				throw MarketRuntimeException.createExceptionWithDetails(
						MarketRuntimeException.MARKET_ORDER_CANNOT_BE_SUBMITTED_IF_THE_OTHER_SIDE_BOOK_IS_EMPTY, null, 
						new Object[]{ order_, market_ });
			}
			if(otherSideOrderBook.hasOnlyMarketOrders()) {
				throw MarketRuntimeException.createExceptionWithDetails(
						MarketRuntimeException.MARKET_ORDER_CANNOT_BE_SUBMITTED_IF_THE_OTHER_SIDE_BOOK_HAS_ONLY_MARKET_ORDERS, null, 
						new Object[]{ order_, market_ });				
			}
		} 
	}
	
	// TODO: When moved to Event Queue processing, catch and log Market Runtime and Market Security 
	// 		 so transaction is not rolled back (but rolled back for other exceptions)
	@Override
	public void submit(final OrderInternal order_, final OrderManagementContext orderManagementContext_) {
		QuoteAndSize previousBestBid = getBestBid();
		QuoteAndSize previousBestAsk = getBestAsk();
		
		// Note: This validation needs to happen right before the actual submit, even if we have an
		// 		 Event Queue, Market may have been changed, while the Submit Message is in the Queue 
		OrderValidator.validateOrderOnSubmit(order_.getOrderModel(), this.getMarketModel());
		checkOrderTypeAndQuote(order_, this);
		
		_executionSystem.handleSubmit(order_, orderManagementContext_);

		notifyAboutBidAskUpdate(orderManagementContext_.getMarketDataSession(),
				previousBestBid, previousBestAsk);
	
		order_.addExpirationTrigger(this, orderManagementContext_);
	}

	// TODO: When moved to Event Queue processing, catch and log Market Runtime and Market Security 
	// 		 so transaction is not rolled back (but rolled back for other exceptions)
	@Override
	public void cancel(OrderInternal order_, String cancellationCommentLanguageKey_, OrderManagementContext orderManagementContext_) {
		QuoteAndSize previousBestBid = getBestBid();
		QuoteAndSize previousBestAsk = getBestAsk();
		
		OrderValidator.validateOrderForCancel(order_.getOrderModel(), this.getMarketModel());
		if(order_.getState() != OrderState.Canceled && order_.getState() != OrderState.Deleted) {
			if(order_.getState() == OrderState.Submitted) {
				MarketUtils.getBook(order_, this).recordCancel(
						order_, cancellationCommentLanguageKey_, orderManagementContext_);

				order_.removeExpirationTrigger(this, orderManagementContext_);
			} else {
				order_.recordCancel(cancellationCommentLanguageKey_, orderManagementContext_);
			}
		}

		notifyAboutBidAskUpdate(orderManagementContext_.getMarketDataSession(),
				previousBestBid, previousBestAsk);
	}

	private void notifyAboutBidAskUpdate(MarketDataSession marketDataSession_, 
			QuoteAndSize previousBestBid_, QuoteAndSize previousBestAsk_) {
		QuoteAndSizeInfo bestBid = getBestBid(true);
		QuoteAndSize bestBidToNotify = bestBid.getQuoteAndSize();
		Boolean bidHasHiddenOrders = bestBid.getHasHiddenOrders();
		if(shouldNotifiy(bestBidToNotify, previousBestBid_)) {
			if(bestBidToNotify == null) {
				bestBidToNotify = createEmptyQuote();
			}
			// since the best bid comes from the book, this is the only way to force increment the object version,
			// object version is used by some client to decide, if the message is current 
			// so we need to execute set previous bid before notification (to increment version)
			getMarketModel().setPreviousBestBid(previousBestBid_);
			marketDataSession_.recordNewBestBid(getMarketCode(), bestBidToNotify, bidHasHiddenOrders, getUpdatedVersion());
		}
		
		QuoteAndSizeInfo bestAsk = getBestAsk(true);
		QuoteAndSize bestAskToNotify = bestAsk.getQuoteAndSize();
		Boolean askHasHiddenOrders = bestAsk.getHasHiddenOrders();
		if(shouldNotifiy(bestAskToNotify, previousBestAsk_)) {
			if(bestAskToNotify == null) {
				bestAskToNotify = createEmptyQuote();
			}
			// since the best ask comes from the book, this is the only way to force increment the object version,
			// object version is used by some client to decide, if the message is current 
			// so we need to execute set previous ask before notification (to increment version)
			getMarketModel().setPreviousBestAsk(previousBestAsk_);
			marketDataSession_.recordNewBestAsk(getMarketCode(), bestAskToNotify, askHasHiddenOrders, getUpdatedVersion());
		}
	}

	private QuoteAndSize createEmptyQuote() {
		QuoteAndSize empty = new QuoteAndSize();
		Quote quote = new Quote();
		empty.setSize(0);
		empty.setQuote(quote);
		
		return empty;
	}
	
	protected static boolean shouldNotifiy(QuoteAndSize quoteAndSize_, QuoteAndSize quoteAndSizePrevious_) {
		return (quoteAndSize_ == null) != (quoteAndSizePrevious_ == null) || 
				quoteAndSize_ != null && quoteAndSizePrevious_ != null &&
				   ((quoteAndSize_.getQuote() == null) != (quoteAndSizePrevious_.getQuote() == null) ||
				   quoteAndSize_.getQuote().getValidQuote() != quoteAndSizePrevious_.getQuote().getValidQuote() ||
				   quoteAndSize_.getQuote().getQuoteValue() != quoteAndSizePrevious_.getQuote().getQuoteValue() || 
				   quoteAndSize_.getSize() != quoteAndSizePrevious_.getSize());
	}
	
	protected static boolean shouldNotifiy(QuoteAndSize quoteAndSize_) {
		return quoteAndSize_ != null && shouldNotifiy(quoteAndSize_.getQuote());
	}
	
	protected static boolean shouldNotifiy(Quote quote_) {
		return quote_ != null && quote_.getValidQuote();
	}
	
	@Override
	public void recordLastTrade(QuoteAndSize lastTrade_, MarketDataSession marketDataSession_) {
		getMarketModel().setPreviousLastTrade(getLastTrade());
		getMarketModel().setLastTrade(lastTrade_);
		if(shouldNotifiy(lastTrade_)) {
			marketDataSession_.recordNewLastTrade(getMarketCode(), lastTrade_, getUpdatedVersion());
		}
	}

	@Override
	public void recordOpeningQuote(Quote openingQuote_, MarketDataSession marketDataSession_) {
		getMarketModel().setPreviousOpeningQuote(getOpeningQuote());
		getMarketModel().setOpeningQuote(openingQuote_);
		if(shouldNotifiy(openingQuote_)) {
			marketDataSession_.recordOpeningQuote(getMarketCode(), openingQuote_, getUpdatedVersion());
		}
	}

	@Override
	public void recordClosingQuote(Quote closingQuote_, MarketDataSession marketDataSession_) {
		getMarketModel().setPreviousClosingQuote(getClosingQuote());
		getMarketModel().setClosingQuote(closingQuote_);
		if(shouldNotifiy(closingQuote_)) {
			marketDataSession_.recordClosingQuote(getMarketCode(), closingQuote_, getUpdatedVersion());
		}
	}

	// TODO: When moved to Event Queue processing, catch and log Market Runtime and Market Security 
	// 		 so transaction is not rolled back (but rolled back for other exceptions)
	@Override
	public void openMarket(OrderManagementContext orderManagementContext_) {
		checkMarketForOpen();

		QuoteAndSize previousBestBid = getBestBid();
		QuoteAndSize previousBestAsk = getBestAsk();
		
		PropertyChangeSession propertyChangeSession = orderManagementContext_.getPropertyChangeSession();
		
		_marketPropertyChangeExecutor.setState(MarketState.Opening, propertyChangeSession);
		_executionSystem.handleMarketOpen(orderManagementContext_);
		_marketPropertyChangeExecutor.setState(MarketState.Open, propertyChangeSession);		
		
		notifyAboutBidAskUpdate(orderManagementContext_.getMarketDataSession(), 
				previousBestBid, previousBestAsk);
	}

	@Override
	public void callMarket(OrderManagementContext orderManagementContext_) {
		checkMarketForCall();

		QuoteAndSize previousBestBid = getBestBid();
		QuoteAndSize previousBestAsk = getBestAsk();
		
		PropertyChangeSession propertyChangeSession = orderManagementContext_.getPropertyChangeSession();
		
		_marketPropertyChangeExecutor.setState(MarketState.Called, propertyChangeSession);
		_executionSystem.handleMarketCall(orderManagementContext_);
		_marketPropertyChangeExecutor.setState(MarketState.Open, propertyChangeSession);

		notifyAboutBidAskUpdate(orderManagementContext_.getMarketDataSession(), 
				previousBestBid, previousBestAsk);
	}
	
	// TODO: When moved to Event Queue processing, catch and log Market Runtime and Market Security 
	// 		 so transaction is not rolled back (but rolled back for other exceptions)
	@Override	
	public void executeCallMarketMaintenance(
			OrderManagementContext orderManagementContext_) {
		// TODO: If explicit market to market happens at the clearing agent it last trade shall be sent to clearing agent 
		
		// TODO: Consider, this query may retrieve a lot of Orders, which may cause timeouts and performance problems		
		OrderInternal[] activeOrdersOnMarket = _orderDatastore.findActiveOrdersOnMarket(getMarketCode());
		
		for (OrderInternal orderInternal : activeOrdersOnMarket) {
			OrderValidator.validateOrderForCancel(orderInternal.getOrderModel(), this.getMarketModel());
			
			cancel(orderInternal, MarketSystemMessageLanguageKeys.ORDER_CANCELED_AFTER_MARKET_CALL, orderManagementContext_);
		}
	}
	
	private ObjectVersion getUpdatedVersion() {
		ObjectVersion version = new ObjectVersion();
		// JPA has the current version of the object until it is committed to the datastore, 
		// but in the change notification messages version after update is needed (which is current version + 1) 
		if(_marketModel.getVersionNumber() != null) {			
			version.setVersion(_marketModel.getVersionNumber() + 1);
		} else {
			version.setVersion(0L);
		}
		
		return version;
	}

	// TODO: When moved to Event Queue processing, catch and log Market Runtime and Market Security 
	// 		 so transaction is not rolled back (but rolled back for other exceptions)
	@Override	
	public void executeRegularMarketMaintenance(
			OrderManagementContext orderManagementContext_) {
		// TODO: Send Update Settlement Position message to settlement system (see Settlement and Payment System � System Requirements Specification  for more details)
		// TODO: If explicit market to market happens at the clearing agent it last trade shall be sent to clearing agent 
		
		// delete orders action is not executed for call markets
		if(getExecutionSystem() != ExecutionSystem.CallMarketWithSingleSideAuction) {
			// TODO: Consider, this query may retrieve a lot of Orders, which may cause timeouts and performance problems		
			OrderInternal[] nonActiveOrdersOnMarket = _orderDatastore.findNonActiveOrdersOnMarket(getMarketCode());
			
			for (OrderInternal order : nonActiveOrdersOnMarket) {
				// TODO: Resolve that the Deleted OrderState Change Notification will not be sent to
				// 		 the listeners, because listeners will be deleted in the next line and
				//		 notification happens asynchronously (using task queues)
				// 		 Explicit delete of listeners needed, because anyway they may stay around and 
				// 		 will never be deleted.
				orderManagementContext_.notifyOrderStateChange(OrderState.Deleted, order.getOrderModel(), ChangeOrigin.System);
				
				orderManagementContext_.getPropertyChangeSession().getPropertyChangeBroker().getNotificationBroker().removeAllListeners(
						new DatastoreKey(order.getKey()), Order.class);
			}
			
			_orderDatastore.deleteNonActiveOrdersOnMarket(getMarketCode());
		}
	}
	
	private void checkMarketForCall() {
		if(getActivationStatus() != ActivationStatus.Activated) {
			throw MarketRuntimeException.createExceptionWithDetails(
					MarketRuntimeException.NON_ACTIVE_MARKET_CANNOT_BE_CALLED, null, 
					new Object[]{ this });	
		}
		if(getExecutionSystem() != ExecutionSystem.CallMarketWithSingleSideAuction) {
			throw MarketRuntimeException.createExceptionWithDetails(
					MarketRuntimeException.NON_CALL_MARKET_CANNOT_BE_CALLED, null, 
					new Object[]{ this });			
		}
		if(getState() != MarketState.Open) {
			throw MarketRuntimeException.createExceptionWithDetails(
					MarketRuntimeException.OPEN_MARKET_CAN_ONLY_BE_CALLED, null, 
					new Object[]{ this });			
		}
	}

	// TODO: When moved to Event Queue processing, catch and log Market Runtime and Market Security 
	// 		 so transaction is not rolled back (but rolled back for other exceptions)
	@Override
	public void haltMarket(final OrderManagementContext orderManagementContext_) {
		checkForHalt();

		QuoteAndSize previousBestBid = getBestBid();
		QuoteAndSize previousBestAsk = getBestAsk();
		
		PropertyChangeSession propertyChangeSession = orderManagementContext_.getPropertyChangeSession();
		
		HaltRule actualHaltRule = getActualHaltRule();
		
		if(actualHaltRule != null) {
			if(actualHaltRule.getHaltPeriod() > 0) {
				int haltPeriodMilliseconds = actualHaltRule.getHaltPeriod() * 60 * 1000;

				_marketPropertyChangeExecutor.setState(MarketState.Halted, propertyChangeSession);
				
				List<HaltRule> haltRules = getCircuitBreaker().getHaltRules();
				if(haltRules.size() > 1) {
					int newHaltLevel = getInitializedHaltLevel() + 1;
					
					_marketPropertyChangeExecutor.setHaltLevel(Math.min(newHaltLevel, haltRules.size() - 1), propertyChangeSession);
				}
				
				addReopenAction(haltPeriodMilliseconds, orderManagementContext_);
				
			} else {
				closeMarket(orderManagementContext_);
			}
	
			notifyAboutBidAskUpdate(orderManagementContext_.getMarketDataSession(), 
					previousBestBid, previousBestAsk);
		}
	}

	protected void addReopenAction(int haltPeriodMilliseconds_,
			final OrderManagementContext orderManagementContext_) {
		ReOpenMarketAction reOpenAction = new ReOpenMarketAction(getMarketCode());
		_timeTrigger.scheduleRelative(haltPeriodMilliseconds_, 
				reOpenAction);
		
		// no need to use Property Change Executor for System Property
		getMarketModel().setMarketReOpenActionKey(reOpenAction.getListenerKey());
	}
	
	@Override
	public HaltRule getActualHaltRule() {
		CircuitBreaker circuitBreaker = getMarketModel().getCircuitBreaker();
		
		HaltRule rule = null;
		if(circuitBreaker != null) {
			List<HaltRule> haltRules = circuitBreaker.getHaltRules();
			int haltLevel = getInitializedHaltLevel();
			
			if(haltRules != null && 
			   haltRules.size() > 0) {
				rule = haltRules.get(haltLevel);		
			}
		}	
		
		return rule;
	}

	private int getInitializedHaltLevel() {
		int haltLevel = getMarketModel().getHaltLevel();
		// initialize halt level
		if(haltLevel < 0) {
			haltLevel = 0;
		}
		return haltLevel;
	}

	// TODO: When moved to Event Queue processing, catch and log Market Runtime and Market Security 
	// 		 so transaction is not rolled back (but rolled back for other exceptions)
	@Override
	public void reOpenMarket(OrderManagementContext orderManagementContext_) {		
		if(getMarketModel().getState() != MarketState.Closed) {
			checkMarketForReOpen();
			
			QuoteAndSize previousBestBid = getBestBid();
			QuoteAndSize previousBestAsk = getBestAsk();
			
			PropertyChangeSession propertyChangeSession = orderManagementContext_.getPropertyChangeSession();
			
			_marketPropertyChangeExecutor.setState(MarketState.ReOpening, propertyChangeSession);
			
			_executionSystem.handleMarketReOpen(orderManagementContext_);

			// no need to use Property Change Executor for System Property
			getMarketModel().setMarketReOpenActionKey(null);
			
			_marketPropertyChangeExecutor.setState(MarketState.Open, propertyChangeSession);
			
			notifyAboutBidAskUpdate(orderManagementContext_.getMarketDataSession(), 
					previousBestBid, previousBestAsk);
		}
	}

	// TODO: When moved to Event Queue processing, catch and log Market Runtime and Market Security 
	// 		 so transaction is not rolled back (but rolled back for other exceptions)
	@Override
	public void closeMarket(OrderManagementContext orderManagementContext_) {
		checkMarketForClose();
		
		QuoteAndSize previousBestBid = getBestBid();
		QuoteAndSize previousBestAsk = getBestAsk();
		
		PropertyChangeSession propertyChangeSession = orderManagementContext_.getPropertyChangeSession();
		
		_marketPropertyChangeExecutor.setState(MarketState.Closing, propertyChangeSession);
		
		_executionSystem.handleMarketClose(orderManagementContext_);

		_marketPropertyChangeExecutor.setHaltLevel(-1, propertyChangeSession);
		_marketPropertyChangeExecutor.setState(MarketState.Closed, propertyChangeSession);
		
		
		notifyAboutBidAskUpdate(orderManagementContext_.getMarketDataSession(), 
				previousBestBid, previousBestAsk);
	}
	
	private void checkForHalt() {
		if(getActivationStatus() != ActivationStatus.Activated) {
			throw MarketRuntimeException.createExceptionWithDetails(
					MarketRuntimeException.NON_ACTIVE_MARKET_CANNOT_BE_HALTED, null, 
					new Object[]{ this });		
		}
		if(getState() != MarketState.Open) {
			throw MarketRuntimeException.createExceptionWithDetails(
					MarketRuntimeException.NON_OPEN_MARKET_CANNOT_BE_HALTED, null, 
					new Object[]{ this });	
		}
	}

	private void checkMarketForReOpen() {
		if(getActivationStatus() != ActivationStatus.Activated) {
			throw MarketRuntimeException.createExceptionWithDetails(
					MarketRuntimeException.NON_ACTIVE_MARKET_CANNOT_BE_OPENED, null, 
					new Object[]{ this });	
		}
		if(getState() != MarketState.Halted) {
			throw MarketRuntimeException.createExceptionWithDetails(
					MarketRuntimeException.NON_HALTED_MARKET_CANNOT_BE_OPENED, null, 
					new Object[]{ this });	
		}
	}
	
	private void checkMarketForOpen() {
		if(getActivationStatus() != ActivationStatus.Activated) {
			throw MarketRuntimeException.createExceptionWithDetails(
					MarketRuntimeException.NON_ACTIVE_MARKET_CANNOT_BE_OPENED, null, 
					new Object[]{ this });
		}
		if(getState() != MarketState.Closed) {
			throw MarketRuntimeException.createExceptionWithDetails(
					MarketRuntimeException.NON_CLOSED_MARKET_CANNOT_BE_OPENED, null, 
					new Object[]{ this });
		}
	}

	private void checkMarketForClose() {
		if(getActivationStatus() != ActivationStatus.Activated) {
			throw MarketRuntimeException.createExceptionWithDetails(
					MarketRuntimeException.NON_ACTIVE_MARKET_CANNOT_BE_CLOSED, null, 
					new Object[]{ this });
		}
		if(getState() != MarketState.Open && getState() != MarketState.Halted) {
			throw MarketRuntimeException.createExceptionWithDetails(
					MarketRuntimeException.NON_OPEN_MARKET_CANNOT_BE_CLOSED, null, 
					new Object[]{ this });
		}
	}
	
	@Override
	public ActivationStatus getActivationStatus() {
		return getMarketModel().getActivationStatus();
	}

	@Override
	public QuoteAndSize getBestAskSystem() {
		return getBestAsk(false).getQuoteAndSize();
	}
	

	@Override
	public QuoteAndSize getBestAsk() {
		return getBestAsk(true).getQuoteAndSize();
	}
	
	private QuoteAndSizeInfo getBestAsk(boolean forClient_) {
		OrderBookEntryInternal bestSellOrder = getSellBook().getBestOrderBookEntry();
		QuoteAndSize bestAsk = null;
		Boolean hasHiddenOrders = null;
		if(bestSellOrder != null && bestSellOrder.getType() == OrderType.Limit) {
			OrderBookEntrySize sizeObject = bestSellOrder.getSize(forClient_);
			
			hasHiddenOrders = sizeObject.getHasHiddenOrders();
			
			bestAsk = new QuoteAndSize();
			bestAsk.getQuote().setQuoteValue(bestSellOrder.getLimitQuote().getQuoteValue());
			bestAsk.setSize(sizeObject.getAggregateSize());
			bestAsk.getQuote().setValidQuote(true); 
		}
		
		return new QuoteAndSizeInfo(bestAsk, hasHiddenOrders);
	}
	
	@Override
	public QuoteAndSize getBestBidSystem() {
		return getBestBid(false).getQuoteAndSize();
	}

	@Override
	public QuoteAndSize getBestBid() {
		return getBestBid(true).getQuoteAndSize();
	}
	
	private QuoteAndSizeInfo getBestBid(boolean forClient_) {
		OrderBookEntryInternal bestBuyOrder = getBuyBook().getBestOrderBookEntry();
		QuoteAndSize bestBid = null;
		Boolean hasHiddenOrders = null;
		if(bestBuyOrder != null && bestBuyOrder.getType() == OrderType.Limit) {
			OrderBookEntrySize sizeObject = bestBuyOrder.getSize(forClient_);
			
			hasHiddenOrders = sizeObject.getHasHiddenOrders();
			
			bestBid = new QuoteAndSize();
			bestBid.getQuote().setQuoteValue(bestBuyOrder.getLimitQuote().getQuoteValue());
			bestBid.setSize(sizeObject.getAggregateSize());
			bestBid.getQuote().setValidQuote(true);
		}
		
		return new QuoteAndSizeInfo(bestBid, hasHiddenOrders);
	}
	
//	@Override
//	public BusinessEntity getBusinessEntity() {
//		return getMarketModel().getBusinessEntity();
//	}

	@Override
	public String getClearingBank() {
		return getMarketModel().getClearingBank();
	}
	
	@Override
	public double getCommission() {
		return getMarketModel().getCommission();
	}

	@Override
	public String getCommissionCurrency() {
		return getMarketModel().getCommissionCurrency();
	}
	
	@Override
	public String getDescription() {
		return getMarketModel().getDescription();
	}

	@Override
	public ExecutionSystem getExecutionSystem() {
		return getMarketModel().getExecutionSystem();
	}

	@Override
	public CircuitBreaker getCircuitBreaker() {
		return getMarketModel().getCircuitBreaker(); 
	}

	@Override
	public Instrument getInstrument() {
		return _instrumentDatastore.findInstrumentByCode(getMarketModel().getInstrumentCode());
	}

	@Override
	public QuoteAndSize getLastTrade() {
		return getMarketModel().getLastTrade();
	}

	@Override
	public QuoteAndSize getPreviousLastTrade() {
		return getMarketModel().getPreviousLastTrade();
	}
	
	@Override
	public Quote getOpeningQuote() {
		return getMarketModel().getOpeningQuote();
	}
	
	@Override
	public Quote getClosingQuote() {
		return getMarketModel().getClosingQuote();
	}


	@Override
	public Quote getPreviousClosingQuote() {
		return getMarketModel().getPreviousClosingQuote();
	}

	@Override
	public Quote getPreviousOpeningQuote() {
		return getMarketModel().getPreviousOpeningQuote();
	}
	
	@Override
	public String getMarketCode() {
		return getMarketModel().getMarketCode();
	}

	@Override
	public String getMarketOperationContract() {
		return getMarketModel().getMarketOperationContract();
	}

	@Override
	public int getMaximumContractsTraded() {
		return getMarketModel().getMaximumContractsTraded();
	}

	@Override
	public int getMinimumContractsTraded() {
		return getMarketModel().getMinimumContractsTraded();
	}

	@Override
	public double getMinimumQuoteIncrement() {
		return getMarketModel().getMinimumQuoteIncrement();
	}

	@Override
	public String getName() {
		return getMarketModel().getName();
	}

	@Override
	public QuoteType getQuoteType() {
		return getMarketModel().getQuoteType();
	}

	@Override
	public SecondaryOrderPrecedenceRuleType[] getSecondaryOrderPrecedenceRules() {
		SecondaryOrderPrecedenceRuleType[] secondaryOrderPrecedenceRulesArray = new SecondaryOrderPrecedenceRuleType[0];
	
		List<SecondaryOrderPrecedenceRuleType> secondaryOrderPrecedenceRules = getMarketModel().getSecondaryOrderPrecedenceRules();
	
		if(secondaryOrderPrecedenceRules != null) {
			secondaryOrderPrecedenceRulesArray = 
				secondaryOrderPrecedenceRules.toArray(new SecondaryOrderPrecedenceRuleType[secondaryOrderPrecedenceRules.size()]);
		}
		
		return secondaryOrderPrecedenceRulesArray;
	}

	@Override
	public OrderBookInternal getBuyBook() {
		if(_buyBook == null) {
			_buyBook = new OrderBookImpl(getBuyBookModel(), this, _orderDatastore, _marketDatastore);
		}
		return _buyBook;
	}
	
	@Override
	public OrderBookInternal getSellBook() {
		if(_sellBook == null) {
			_sellBook = new OrderBookImpl(getSellBookModel(), this, _orderDatastore, _marketDatastore);
		}
		return _sellBook;
	}

	protected OrderBookModel getSellBookModel() {
		SellBook sellBook = getMarketModel().getSellBook();
		if(sellBook == null) {
			sellBook = new SellBook();
			getMarketModel().setSellBook(sellBook);
		}
		
		return sellBook;
	}
	
	protected OrderBookModel getBuyBookModel() {
		BuyBook buyBook = getMarketModel().getBuyBook();
		if(buyBook == null) {
			buyBook = new BuyBook();
			getMarketModel().setBuyBook(buyBook);
		}
		
		return buyBook;
	}
	
	@Override
	public MarketState getState() {
		return getMarketModel().getState();
	}

	@Override
	public TimeOfDay getTradingDayEnd() {
		return getMarketModel().getTradingDayEnd();
	}

	@Override
	public TimePeriod getTradingHours() {
		return getMarketModel().getTradingHours();
	}

	@Override
	public TradingSession getTradingSession() {
		return getMarketModel().getTradingSession();
	}
	
	@Override
	public BusinessCalendar getBusinessCalendar() {
		return getMarketModel().getBusinessCalendar();
	}
	
	@Override
	public boolean getAllowHiddenOrders() {
		return getMarketModel().getAllowHiddenOrders();
	}
	
	@Override
	public boolean getAllowSizeRestrictionOnOrders() {
		return getMarketModel().getAllowSizeRestrictionOnOrders();
	}
	
	@Override
	public String getMarketTimeZoneID() {
		return getMarketModel().getMarketTimeZoneID();
	}
	
	
	/**
	 * @return String used in Debug and Exceptions 
	 */
	@Override
	public String toString() {
		return getMarketModel().toString();
	}

	@Override
	public MarketTimer getTimeTrigger() {
		return _timeTrigger;
	}
	
	// classes
	/**
	 * Internal Class to Enclose Quote and Sizes with Hidden Orders Information
	 */
	private static class QuoteAndSizeInfo {
		private final QuoteAndSize _quoteAndSize;
		private final Boolean _hasHiddenOrders;
		
		public QuoteAndSizeInfo(QuoteAndSize quoteAndSize_,
				Boolean hasHiddenOrders_) {
			super();
			_quoteAndSize = quoteAndSize_;
			_hasHiddenOrders = hasHiddenOrders_;
		}

		public QuoteAndSize getQuoteAndSize() {
			return _quoteAndSize;
		}

		public Boolean getHasHiddenOrders() {
			return _hasHiddenOrders;
		}
	}
}
