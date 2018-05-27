package com.imarcats.market.management;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import com.imarcats.interfaces.client.v100.exception.MarketRuntimeException;
import com.imarcats.interfaces.server.v100.dto.mapping.MarketDtoMapping;
import com.imarcats.internal.server.infrastructure.datastore.AssetClassDatastore;
import com.imarcats.internal.server.infrastructure.datastore.AuditTrailEntryDatastore;
import com.imarcats.internal.server.infrastructure.datastore.InstrumentDatastore;
import com.imarcats.internal.server.infrastructure.datastore.MarketDatastore;
import com.imarcats.internal.server.infrastructure.datastore.MarketOperatorDatastore;
import com.imarcats.internal.server.infrastructure.datastore.ProductDatastore;
import com.imarcats.internal.server.infrastructure.notification.properties.PropertyChangeSession;
import com.imarcats.internal.server.infrastructure.timer.MarketTimer;
import com.imarcats.internal.server.infrastructure.timer.TimerUtils;
import com.imarcats.internal.server.infrastructure.trigger.CallMarketAction;
import com.imarcats.internal.server.infrastructure.trigger.CallMarketMaintenanceAction;
import com.imarcats.internal.server.infrastructure.trigger.CloseMarketAction;
import com.imarcats.internal.server.infrastructure.trigger.MarketMaintenanceAction;
import com.imarcats.internal.server.infrastructure.trigger.OpenMarketAction;
import com.imarcats.internal.server.interfaces.market.MarketInternal;
import com.imarcats.internal.server.interfaces.market.MarketPropertyChangeExecutor;
import com.imarcats.internal.server.interfaces.order.OrderManagementContext;
import com.imarcats.internal.server.interfaces.order.OrderManagementContextImpl;
import com.imarcats.market.management.validation.MarketValidator;
import com.imarcats.model.Instrument;
import com.imarcats.model.Market;
import com.imarcats.model.types.ActivationStatus;
import com.imarcats.model.types.AuditEntryAction;
import com.imarcats.model.types.BusinessCalendar;
import com.imarcats.model.types.ExecutionSystem;
import com.imarcats.model.types.MarketState;
import com.imarcats.model.types.RecurringActionDetail;
import com.imarcats.model.types.SecondaryOrderPrecedenceRuleType;
import com.imarcats.model.types.TimeOfDay;
import com.imarcats.model.types.TradingSession;

public class MarketManagementBase extends ManagementSystemBase {

	public static final long MAINTENANCE_DELAY_AFTER_MARKET_CALL_IN_MILLIS = 1 * 60 * 1000; // 1 min

	public static final long MAX_TIME_TILL_MARKET_CALL_IN_MILLIS = 15 * 24 * 60 * 60 * 1000;
	protected static final String OBJECT_TYPE = Market.class.getSimpleName();
	
	private final MarketDatastore _marketDatastore;
	private final MarketTimer _marketTimer;

	public MarketManagementBase(ProductDatastore productDatastore_, 
			AssetClassDatastore assetClassDatastore_, 
			InstrumentDatastore instrumentDatastore_, 
			MarketOperatorDatastore marketOperatorDatastore_, 
			MarketDatastore marketDatastore_, 
			AuditTrailEntryDatastore auditTrailEntryDatastore_, 
			MarketTimer marketTimer_) {
		super(productDatastore_, assetClassDatastore_, instrumentDatastore_, marketOperatorDatastore_, 
				marketDatastore_, auditTrailEntryDatastore_, marketTimer_);
		_marketDatastore = marketDatastore_;
		_marketTimer = marketTimer_;  
	}

	public static void setupChangedMarket(MarketPropertyChangeExecutor targetMarketChangeExecutor_, Market targetMarket_, Market originalMarket_, PropertyChangeSession propertyChangeSession_, Instrument underlying_) {
		targetMarket_.setBuyBook(originalMarket_.getBuyBook());
		targetMarket_.setSellBook(originalMarket_.getSellBook());
		targetMarket_.setHaltLevel(originalMarket_.getHaltLevel()); 
		targetMarket_.setMarketCallActionKey(originalMarket_.getMarketCallActionKey());
		targetMarket_.setMarketOpenActionKey(originalMarket_.getMarketOpenActionKey());
		targetMarket_.setMarketCloseActionKey(originalMarket_.getMarketCloseActionKey());
		targetMarket_.setMarketReOpenActionKey(originalMarket_.getMarketReOpenActionKey());
		targetMarket_.setMarketMaintenanceActionKey(originalMarket_.getMarketMaintenanceActionKey());
		targetMarket_.setCallMarketMaintenanceActionKey(originalMarket_.getCallMarketMaintenanceActionKey());
		
		targetMarket_.setActivationStatus(originalMarket_.getActivationStatus());
		
		targetMarketChangeExecutor_.setApprovalAudit(originalMarket_.getApprovalAudit(), propertyChangeSession_);
		targetMarketChangeExecutor_.setCreationAudit(originalMarket_.getCreationAudit(), propertyChangeSession_);
		targetMarketChangeExecutor_.setChangeAudit(originalMarket_.getChangeAudit(), propertyChangeSession_);
		targetMarketChangeExecutor_.setMarketOperationContract(originalMarket_.getMarketOperationContract(), propertyChangeSession_);
		targetMarketChangeExecutor_.setRolloverAudit(originalMarket_.getRolloverAudit(), propertyChangeSession_);
		targetMarketChangeExecutor_.setSuspensionAudit(originalMarket_.getSuspensionAudit(), propertyChangeSession_);
		targetMarketChangeExecutor_.setActivationAudit(originalMarket_.getActivationAudit(), propertyChangeSession_);
		targetMarketChangeExecutor_.setDeactivationAudit(originalMarket_.getDeactivationAudit(), propertyChangeSession_);

		targetMarket_.setClosingQuote(originalMarket_.getClosingQuote());
		targetMarket_.setOpeningQuote(originalMarket_.getOpeningQuote());
		targetMarket_.setPreviousClosingQuote(originalMarket_.getPreviousClosingQuote());
		targetMarket_.setPreviousOpeningQuote(originalMarket_.getPreviousOpeningQuote());
		targetMarket_.setCurrentBestAsk(originalMarket_.getCurrentBestAsk());
		targetMarket_.setCurrentBestBid(originalMarket_.getCurrentBestBid());
		targetMarket_.setPreviousBestAsk(originalMarket_.getPreviousBestAsk());
		targetMarket_.setPreviousBestBid(originalMarket_.getPreviousBestBid());

		targetMarket_.setState(originalMarket_.getState());

		targetMarketChangeExecutor_.setNextMarketCallDate(originalMarket_.getNextMarketCallDate(), propertyChangeSession_);
		
		targetMarketChangeExecutor_.setQuoteType(underlying_.getQuoteType(), propertyChangeSession_);
		
	}
	
//	protected static void copyMarketDataForChange(MarketPropertyChangeExecutor targetMarketChangeExecutor_, Market market_, IPropertyChangeSession propertyChangeSession_, Instrument underlying_) {
//		if(market_.getBusinessCalendar() != null) {
//			targetMarketChangeExecutor_.setBusinessCalendar(BusinessCalendar.create(market_.getBusinessCalendar()), propertyChangeSession_);
//		}
//		targetMarketChangeExecutor_.setBusinessEntityCode(market_.getBusinessEntityCode(), propertyChangeSession_);
//		if(market_.getCircuitBreaker() != null) {
//			targetMarketChangeExecutor_.setCircuitBreaker(CircuitBreaker.create(market_.getCircuitBreaker()), propertyChangeSession_);
//		}
//		targetMarketChangeExecutor_.setCommission(market_.getCommission(), propertyChangeSession_);
//		targetMarketChangeExecutor_.setCommissionCurrency(market_.getCommissionCurrency(), propertyChangeSession_);
//		targetMarketChangeExecutor_.setDescription(market_.getDescription(), propertyChangeSession_);
//		targetMarketChangeExecutor_.setExecutionSystem(market_.getExecutionSystem(), propertyChangeSession_);
//		targetMarketChangeExecutor_.setMaximumContractsTraded(market_.getMaximumContractsTraded(), propertyChangeSession_);
//		targetMarketChangeExecutor_.setMinimumContractsTraded(market_.getMinimumContractsTraded(), propertyChangeSession_);
//		targetMarketChangeExecutor_.setMinimumQuoteIncrement(market_.getMinimumQuoteIncrement(), propertyChangeSession_);
//		targetMarketChangeExecutor_.setName(market_.getName(), propertyChangeSession_);
//		
//		List<SecondaryOrderPrecedenceRuleType> secondaryOrderPrecedenceRule = new ArrayList<SecondaryOrderPrecedenceRuleType>();
//		for (SecondaryOrderPrecedenceRuleType secondaryOrderPrecedenceRuleType : market_.getSecondaryOrderPrecedenceRules()) {
//			secondaryOrderPrecedenceRule.add(secondaryOrderPrecedenceRuleType);
//		}
//		targetMarketChangeExecutor_.setSecondaryOrderPrecedenceRules(secondaryOrderPrecedenceRule, propertyChangeSession_);
//		if(market_.getTradingDayEnd() != null) {
//			targetMarketChangeExecutor_.setTradingDayEnd(TimeOfDay.create(market_.getTradingDayEnd()), propertyChangeSession_);
//		}
//		if(market_.getTradingHours() != null) {
//			targetMarketChangeExecutor_.setTradingHours(TimePeriod.create(market_.getTradingHours()), propertyChangeSession_);
//		}
//		targetMarketChangeExecutor_.setTradingSession(market_.getTradingSession(), propertyChangeSession_);
//		targetMarketChangeExecutor_.setMarketOperationDays(market_.getMarketOperationDays(), propertyChangeSession_);
//		
//		targetMarketChangeExecutor_.setQuoteType(underlying_.getQuoteType(), propertyChangeSession_);
//		
//		targetMarketChangeExecutor_.setAllowHiddenOrders(market_.getAllowHiddenOrders(), propertyChangeSession_);
//		targetMarketChangeExecutor_.setAllowSizeRestrictionOnOrders(market_.getAllowSizeRestrictionOnOrders(), propertyChangeSession_);
//		
//		targetMarketChangeExecutor_.setMarketTimeZoneID(market_.getMarketTimeZoneID(), propertyChangeSession_);
//		
//		targetMarketChangeExecutor_.setClearingBank(market_.getClearingBank(), propertyChangeSession_);
//	}

	/**
	 * Rollover Market
	 * @param marketCode_ Code of the Market (Source) to be Rolled Over to the given Instrument
	 * @param rolledOverInstrumentCode_ Code of the New Instrument for the Market to be rolled over to
	 * @param user_ User 
	 * @param marketManagementContext_ Market Management Context
	 * 
	 * @return Code of the Rolledover Market
	 */
	protected String rolloverMarket(Market originalMarket_, String rolledOverInstrumentCode_, String user_, MarketManagementContext marketManagementContext_) {
		// check rollover criteria
		if(originalMarket_.getActivationStatus() != ActivationStatus.Activated &&
		   originalMarket_.getActivationStatus() != ActivationStatus.Approved &&
		   originalMarket_.getActivationStatus() != ActivationStatus.Deactivated) {
			throw MarketRuntimeException.createExceptionWithDetails(MarketRuntimeException.NON_APPROVED_OBJECT_CANNOT_BE_REFERRED_IN_ROLLOVER, 
					null, new Object[] { originalMarket_ });					
		}
		Instrument originalInstrument = getInstrumentInternal(originalMarket_.getInstrumentCode());
		
		Instrument newInstrument = getInstrumentInternal(rolledOverInstrumentCode_);		
		if(newInstrument.getActivationStatus() != ActivationStatus.Activated &&
		   newInstrument.getActivationStatus() != ActivationStatus.Approved) {
			throw MarketRuntimeException.createExceptionWithDetails(MarketRuntimeException.NON_APPROVED_OBJECT_CANNOT_BE_REFERRED_IN_ROLLOVER, 
					null, new Object[] { newInstrument });					
		}
		
		if(originalInstrument.getInstrumentCode().equals(rolledOverInstrumentCode_)) {
			throw MarketRuntimeException.createExceptionWithDetails(MarketRuntimeException.ROLLED_OBJECTS_HAVE_SAME_CODE, 
					null, new Object[] { originalInstrument.getInstrumentCode(), rolledOverInstrumentCode_ });				
		}
		
		if(!originalInstrument.getInstrumentCode().equals(newInstrument.getInstrumentCodeRolledOverFrom())) {
			throw MarketRuntimeException.createExceptionWithDetails(MarketRuntimeException.UNDERLYING_OBJECT_PROVIDED_FOR_ROLLOVER_IS_NOT_ROLLED_FROM_UNDERLYING_OF_SOURCE_OBJECT, 
					null, new Object[] { originalInstrument.getInstrumentCode(), newInstrument.getInstrumentCodeRolledOverFrom() });			
		}
		
		// partly cloning original market using DTO mapping
		Market newMarket = MarketDtoMapping.INSTANCE.fromDto(MarketDtoMapping.INSTANCE.toDto(originalMarket_));
		if(newMarket.getSecondaryOrderPrecedenceRules() == null) {
			newMarket.setSecondaryOrderPrecedenceRules(createDefaultSecondaryPrecedenceRuleList());
		}
		
		// setup new market
		newMarket.setInstrumentCode(rolledOverInstrumentCode_);
		newMarket.setMarketOperatorCode(originalMarket_.getMarketOperatorCode());
		newMarket.setMarketCode(Market.createMarketCode(newMarket.getMarketOperatorCode(), newMarket.getInstrumentCode()));
		
		// copy original data to the new market
		MarketPropertyChangeExecutor newMarketPropertyChangeExecutor = new MarketPropertyChangeExecutor(newMarket);
		PropertyChangeSession propertyChangeSession = marketManagementContext_.getPropertyChangeSession();
		setupChangedMarket(newMarketPropertyChangeExecutor, newMarket, originalMarket_, propertyChangeSession, newInstrument);
		
		// validate copied market
		MarketValidator.validateMarketChange(newMarket);
		
		// setup system controlled fields
		newMarketPropertyChangeExecutor.setActivationStatus(ActivationStatus.Approved, propertyChangeSession);
		newMarketPropertyChangeExecutor.setState(MarketState.Closed, propertyChangeSession);
		newMarketPropertyChangeExecutor.setMarketOperationContract(originalMarket_.getMarketOperationContract(), propertyChangeSession);

		newMarketPropertyChangeExecutor.setCreationAudit(createAudit(user_), propertyChangeSession);
		newMarketPropertyChangeExecutor.setRolloverAudit(createAudit(user_), propertyChangeSession);
		newMarket.updateLastUpdateTimestampAndVersion();
		// TODO: Create a separate Audit Log
		
		String marketCode = _marketDatastore.createMarket(newMarket);

		addAuditTrailEntry(AuditEntryAction.Rolled, newMarket.getRolloverAudit(), newMarket.getCode(), OBJECT_TYPE);
		
		return marketCode;		
	}

	protected static List<SecondaryOrderPrecedenceRuleType> createDefaultSecondaryPrecedenceRuleList() {
		List<SecondaryOrderPrecedenceRuleType> list = new ArrayList<SecondaryOrderPrecedenceRuleType>();
		list.add(SecondaryOrderPrecedenceRuleType.TimePrecedence);
		
		return list;
	}
	
	/**
	 * Closes Market in case of an Emergency
	 * @param market_ Market to be Closed 
	 * @param user_ User 
	 * @param marketManagementContext_ Market Management Context 
	 */
	protected void emergencyCloseMarket(Market market_, String user_, MarketManagementContext marketManagementContext_) {
		MarketInternal marketInternal = _marketDatastore.findMarketBy(market_.getCode());
		if(marketInternal == null) {
			throw MarketRuntimeException.createExceptionWithDetails(MarketRuntimeException.NON_EXISTENT_MARKET, null, new Object[] { market_.getCode() });
		}
		
		marketInternal.closeMarket(createOrderManagementContext(marketManagementContext_));
		
		addAuditTrailEntry(AuditEntryAction.Closed, createAudit(user_), market_.getCode(), OBJECT_TYPE); 

		// emergency close, deactivates the market as well, to prevent it from re-opening 
		deactivateMarket(market_, user_, marketManagementContext_);
	}
	
	/**
	 * Deactivates Market 
	 * @param market_ Market to be Deactivated
	 * @param userSession_ User Session  
	 * @param marketManagementContext_ Market Management Context 
	 */
	protected void deactivateMarket(Market market_, String user_, MarketManagementContext marketManagementContext_) {
		Market marketInternal = getMarketInternal(market_.getCode());
		if(marketInternal.getActivationStatus() != ActivationStatus.Activated) {
			throw MarketRuntimeException.createExceptionWithDetails(
					MarketRuntimeException.NON_ACTIVE_MARKET_CANNOT_BE_DEACTIVATED, 
					null, new Object[] { market_.getCode() });
		}
		if(marketInternal.getState() != MarketState.Closed) {
			throw MarketRuntimeException.createExceptionWithDetails(
					MarketRuntimeException.NON_CLOSED_MARKET_CANNOT_BE_DEACTIVATED, 
					null, new Object[] { market_.getCode() });
		}
		
		if(marketInternal.getMarketCallActionKey() != null) {
			_marketTimer.cancel(marketInternal.getMarketCallActionKey());
			marketInternal.setMarketCallActionKey(null);
		}
		if(marketInternal.getMarketCloseActionKey() != null) {
			_marketTimer.cancel(marketInternal.getMarketCloseActionKey());
			marketInternal.setMarketCloseActionKey(null);
		}
		if(marketInternal.getMarketOpenActionKey() != null) {
			_marketTimer.cancel(marketInternal.getMarketOpenActionKey());
			marketInternal.setMarketOpenActionKey(null);
		}
		if(marketInternal.getMarketReOpenActionKey() != null) {
			_marketTimer.cancel(marketInternal.getMarketReOpenActionKey());
			marketInternal.setMarketReOpenActionKey(null);
		}
		if(marketInternal.getMarketMaintenanceActionKey() != null) {
			_marketTimer.cancel(marketInternal.getMarketMaintenanceActionKey());
			marketInternal.setMarketMaintenanceActionKey(null);
		}
		if(marketInternal.getCallMarketMaintenanceActionKey() != null) {
			_marketTimer.cancel(marketInternal.getCallMarketMaintenanceActionKey());
			marketInternal.setCallMarketMaintenanceActionKey(null);
		}
		
		MarketPropertyChangeExecutor propertyChangeExecutor = new MarketPropertyChangeExecutor(marketInternal);
		
		PropertyChangeSession propertyChangeSession = marketManagementContext_.getPropertyChangeSession(); 
		
		propertyChangeExecutor.setActivationStatus(ActivationStatus.Deactivated, propertyChangeSession);
		propertyChangeExecutor.setDeactivationAudit(createAudit(user_), propertyChangeSession);
		market_.updateLastUpdateTimestampAndVersion();
		
		addAuditTrailEntry(AuditEntryAction.Deactivated, marketInternal.getDeactivationAudit(), marketInternal.getCode(), OBJECT_TYPE);
		
	}

	/**
	 * Activates a Call Market 
	 * @param market_ Market to be Activated
	 * @param nextCallDate_ Next Market Call Date 
	 * @param nextCallTime_ Next Market Call Time 
	 * @param userSession_ User Session  
	 * @param marketManagementContext_ Market Management Context 
	 */
	protected void activateCallMarket(Market market_, Date nextCallDate_, TimeOfDay nextCallTime_, String user_, MarketManagementContext marketManagementContext_) {
		Date nextCallDateTime = TimerUtils.calculateTimeOfDate(nextCallTime_, nextCallDate_);
		
		if(market_.getExecutionSystem() != ExecutionSystem.CallMarketWithSingleSideAuction) {
			throw MarketRuntimeException.createExceptionWithDetails(
					MarketRuntimeException.NON_CALL_MARKET_CANNOT_BE_CALLED, 
					null, new Object[] { market_.getCode() });
		}
		
		Date now = new Date();
		if(now.after(nextCallDateTime)) {
			throw MarketRuntimeException.createExceptionWithDetails(
					MarketRuntimeException.CALL_DATE_MUST_BE_IN_FUTURE, 
					null, new Object[] { market_.getCode() });			
		}
		if(Math.abs(now.getTime() - nextCallDateTime.getTime()) > MAX_TIME_TILL_MARKET_CALL_IN_MILLIS) {
			throw MarketRuntimeException.createExceptionWithDetails(
					MarketRuntimeException.CALL_DATE_MUST_NOT_FURTHER_15_DAYS, 
					null, new Object[] { market_.getCode() });			
		}
		
		// schedule market call 
		market_.setMarketCallActionKey(
			_marketTimer.scheduleAbsolute(nextCallDateTime, true, 
					new CallMarketAction(market_.getCode())));
			
		// schedule maintenance 
		market_.setCallMarketMaintenanceActionKey(
				_marketTimer.scheduleAbsolute( 
						TimerUtils.addToDate(nextCallDateTime, MAINTENANCE_DELAY_AFTER_MARKET_CALL_IN_MILLIS), 
						true, 
						new CallMarketMaintenanceAction(market_.getCode())));
		
		// schedule trading day end 
		// trading day end is executed on every day 
		market_.setMarketMaintenanceActionKey(
				_marketTimer.scheduleToTime(
						nextCallDateTime,  
						market_.getTradingDayEnd(), 
						RecurringActionDetail.Daily,
						null,
						true, 
						new MarketMaintenanceAction(market_.getCode())));
		
		MarketPropertyChangeExecutor propertyChangeExecutor = new MarketPropertyChangeExecutor(market_);
		
		PropertyChangeSession propertyChangeSession = marketManagementContext_.getPropertyChangeSession(); 
		
		setActivationDetails(user_, propertyChangeExecutor,
				propertyChangeSession, market_);
		
		propertyChangeExecutor.setNextMarketCallDate(nextCallDateTime, propertyChangeSession);
		
		addAuditTrailEntry(AuditEntryAction.Activated, market_.getActivationAudit(), market_.getCode(), OBJECT_TYPE);

		propertyChangeExecutor.setState(MarketState.Open, propertyChangeSession);
	}

	private static void setActivationDetails(String user_,
			MarketPropertyChangeExecutor propertyChangeExecutor,
			PropertyChangeSession propertyChangeSession, Market market_) {
		propertyChangeExecutor.setActivationStatus(ActivationStatus.Activated, propertyChangeSession);
		propertyChangeExecutor.setActivationAudit(createAudit(user_), propertyChangeSession);
		market_.updateLastUpdateTimestampAndVersion();
	}

	/**
	 * Activates a Market 
	 * @param market_ Market to be Activated
	 * @param userSession_ User Session  
	 * @param marketManagementContext_ Market Management Context 
	 */
	protected void activateMarket(Market market_, String user_, MarketManagementContext marketManagementContext_) {		
		Date scheduledDate = new Date();
		
		activateMarketInternal(market_, user_, marketManagementContext_,
				scheduledDate, _marketTimer, _marketDatastore);

		addAuditTrailEntry(AuditEntryAction.Activated, market_.getActivationAudit(), market_.getCode(), OBJECT_TYPE);
	}

	protected static void activateMarketInternal(Market market_,
			String user_,
			MarketManagementContext marketManagementContext_,
			Date scheduledDate_, MarketTimer marketTimer_, MarketDatastore marketDatastore_) {
		if(market_.getExecutionSystem() == ExecutionSystem.CallMarketWithSingleSideAuction) {
			throw MarketRuntimeException.createExceptionWithDetails(
					MarketRuntimeException.CALL_MARKET_MUST_BE_ACTIVATED_FOR_MARKET_CALL, 
					null, new Object[] { market_.getCode() });
		}
		Date scheduledDateOriginal = scheduledDate_;
		
		BusinessCalendar businessCalendar = new BusinessCalendar();
		if(market_.getBusinessCalendar() != null) {
			businessCalendar = market_.getBusinessCalendar();
		}
		
		RecurringActionDetail marketOperationDays = market_.getMarketOperationDays(); 
		
		// check this date if it is business day
		if(!TimerUtils.isBusinessDay(scheduledDate_, marketOperationDays, businessCalendar)) {
			scheduledDate_ = TimerUtils.findNextBusinessDay(scheduledDate_, marketOperationDays, businessCalendar);
		}
		
		// add activation detail
		MarketPropertyChangeExecutor propertyChangeExecutor = new MarketPropertyChangeExecutor(market_);
		
		PropertyChangeSession propertyChangeSession = marketManagementContext_.getPropertyChangeSession(); 
		
		setActivationDetails(user_, propertyChangeExecutor,
				propertyChangeSession, market_);
		
		if(market_.getTradingSession() != TradingSession.Continuous) {
			// check, if opening time for today is in the past 
			Date scheduledOpen = TimerUtils.calculateTimeOfDate(market_.getTradingHours().getStartTime(), scheduledDate_);
			if(scheduledDateOriginal.getTime() > scheduledOpen.getTime()) {
				GregorianCalendar scheduledCalendar = new GregorianCalendar();
				scheduledCalendar.setTime(scheduledDate_);
				
				scheduledCalendar.add(GregorianCalendar.DATE, 1);
				
				scheduledDate_ = TimerUtils.findNextBusinessDay(scheduledCalendar.getTime(), marketOperationDays, businessCalendar);
			}
			
			// schedule open 
			market_.setMarketOpenActionKey(
					marketTimer_.scheduleToTime( 
							scheduledDate_,
							market_.getTradingHours().getStartTime(), 
							marketOperationDays,
							businessCalendar,
							true, 
							new OpenMarketAction(market_.getCode())));
				
			// schedule close 
			market_.setMarketCloseActionKey(
					marketTimer_.scheduleToTime(
							scheduledDate_,  
							market_.getTradingHours().getEndTime(), 
							marketOperationDays,
							businessCalendar,
							true, 
							new CloseMarketAction(market_.getCode())));
			
		} else {
			// open continuous market right now 
			MarketInternal marketInternal = marketDatastore_.findMarketBy(market_.getCode());
			marketInternal.openMarket(createOrderManagementContext(marketManagementContext_));
		}
		
		// schedule maintenance
		if(market_.getTradingSession() != TradingSession.Continuous) {
			market_.setMarketMaintenanceActionKey(
					marketTimer_.scheduleToTime(
							scheduledDate_,  
							market_.getTradingDayEnd(), 
							marketOperationDays,
							businessCalendar,
							true, 
							new MarketMaintenanceAction(market_.getCode())));
		} else {
			// trading day end is executed on every day for Continuous
			market_.setMarketMaintenanceActionKey(
					marketTimer_.scheduleToTime(
							scheduledDate_,  
							market_.getTradingDayEnd(), 
							RecurringActionDetail.Daily,
							null,
							true, 
							new MarketMaintenanceAction(market_.getCode())));
		}
	
	}

	protected static OrderManagementContext createOrderManagementContext(
			MarketManagementContext marketManagementContext_) {
		return new OrderManagementContextImpl(
				marketManagementContext_.getMarketDataSession(),
				marketManagementContext_.getPropertyChangeSession(),
				marketManagementContext_.getTradeNotificationSession());
	}
	
	protected void checkActivationCriteria(Market market) {
		if(market.getActivationStatus() == ActivationStatus.Activated) {
			throw MarketRuntimeException.createExceptionWithDetails(
					MarketRuntimeException.ACTIVE_MARKET_CANNOT_BE_ACTIVATED, 
					null, new Object[] { market.getMarketCode() });
		}
		if(market.getActivationStatus() != ActivationStatus.Deactivated) {
			if(market.getActivationStatus() != ActivationStatus.Approved) {
				throw MarketRuntimeException.createExceptionWithDetails(
						MarketRuntimeException.NON_APPROVED_MARKET_CANNOT_BE_ACTIVATED, 
						null, new Object[] { market.getMarketCode() });			
			}
		}
	}
}
