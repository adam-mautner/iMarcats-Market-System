package com.imarcats.market.management.admin;

import java.util.Date;

import com.imarcats.interfaces.client.v100.dto.MarketDto;
import com.imarcats.interfaces.client.v100.dto.types.BusinessCalendarDto;
import com.imarcats.interfaces.client.v100.dto.types.TimeOfDayDto;
import com.imarcats.interfaces.client.v100.exception.MarketRuntimeException;
import com.imarcats.interfaces.client.v100.util.DataUtils;
import com.imarcats.interfaces.server.v100.dto.mapping.MarketDtoMapping;
import com.imarcats.internal.server.infrastructure.datastore.AssetClassDatastore;
import com.imarcats.internal.server.infrastructure.datastore.AuditTrailEntryDatastore;
import com.imarcats.internal.server.infrastructure.datastore.InstrumentDatastore;
import com.imarcats.internal.server.infrastructure.datastore.MarketDatastore;
import com.imarcats.internal.server.infrastructure.datastore.MarketOperatorDatastore;
import com.imarcats.internal.server.infrastructure.datastore.ProductDatastore;
import com.imarcats.internal.server.infrastructure.notification.properties.PropertyChangeSession;
import com.imarcats.internal.server.infrastructure.timer.MarketTimer;
import com.imarcats.internal.server.interfaces.market.MarketPropertyChangeExecutor;
import com.imarcats.market.management.MarketManagementContext;
import com.imarcats.market.management.MarketManagementBase;
import com.imarcats.market.management.validation.MarketValidator;
import com.imarcats.market.management.validation.ValidatorUtils;
import com.imarcats.model.Instrument;
import com.imarcats.model.Market;
import com.imarcats.model.MarketOperator;
import com.imarcats.model.Rollable;
import com.imarcats.model.types.ActivationStatus;
import com.imarcats.model.types.AuditEntryAction;
import com.imarcats.model.types.AuditInformation;
import com.imarcats.model.types.BusinessCalendar;
import com.imarcats.model.types.DatastoreKey;
import com.imarcats.model.types.MarketState;

/**
 * Market Administration System 
 * 
 * @author Adam
 *
 */
public class MarketAdministrationSubSystem extends MarketManagementBase {

	private final MarketDatastore _marketDatastore; 

	public MarketAdministrationSubSystem(ProductDatastore productDatastore_, 
			AssetClassDatastore assetClassDatastore_, 
			InstrumentDatastore instrumentDatastore_, 
			MarketOperatorDatastore marketOperatorDatastore_, 
			MarketDatastore marketDatastore_, 
			AuditTrailEntryDatastore auditTrailEntryDatastore_, 
			MarketTimer marketTimer_) {
		super(productDatastore_, assetClassDatastore_, instrumentDatastore_, marketOperatorDatastore_,
				marketDatastore_, auditTrailEntryDatastore_, marketTimer_);
		_marketDatastore = marketDatastore_;
	}
	
	/**
	 * Create a new Market
	 * @param marketDto_ Market to be Created
	 * @param user_ User 
	 * @param marketManagementContext_ Market Management Context
	 * @return Market Code
	 */
	public String createMarket(MarketDto marketDto_, String user_, MarketManagementContext marketManagementContext_) {
		Market market = MarketDtoMapping.INSTANCE.fromDto(marketDto_);
		if(market.getSecondaryOrderPrecedenceRules() == null) {
			market.setSecondaryOrderPrecedenceRules(createDefaultSecondaryPrecedenceRuleList());
		}
		
		MarketValidator.validateNewMarket(market);
		Instrument underlying = checkUnderlyingDependencies(market);
		
		// capitalize market code
		market.setMarketCode(DataUtils.adjustCodeToStandard(market.getCode()));
		
		MarketPropertyChangeExecutor marketPropertyChangeExecutor = new MarketPropertyChangeExecutor(market);
		
		setupNewMarket(marketPropertyChangeExecutor, user_, marketManagementContext_.getPropertyChangeSession());

		market.setQuoteType(underlying.getQuoteType());
		
		AuditInformation audit = createAudit(user_);
		marketPropertyChangeExecutor.setCreationAudit(audit, marketManagementContext_.getPropertyChangeSession());
		market.updateLastUpdateTimestampAndVersion();
		
		// TODO: Create a separate Audit Log
		
		String marketCode = _marketDatastore.createMarket(market);
		
		addAuditTrailEntry(AuditEntryAction.Created, audit, marketCode, OBJECT_TYPE);
		
		return marketCode; 
	}
	
	/**
	 * Change Market  
	 * @param marketDto_ Market to be changed
	 * @param force_ Force the change, even if the 
	 * 				 Product is in Approved State (It will lose Approval)
	 * @param user_ User 
	 * @param marketManagementContext_ Market Management Context
	 */
	public void changeMarket(MarketDto marketDto_, boolean force_, String user_, MarketManagementContext marketManagementContext_) {		
		Market originalMarket = getMarketInternal(marketDto_.getMarketCode());
		
		Market market = MarketDtoMapping.INSTANCE.fromDto(marketDto_);
		if(market.getSecondaryOrderPrecedenceRules() == null) {
			market.setSecondaryOrderPrecedenceRules(createDefaultSecondaryPrecedenceRuleList());
		}
		
		MarketValidator.validateMarketChange(market);
		PropertyChangeSession propertyChangeSession = marketManagementContext_.getPropertyChangeSession();
		
		checkLastUpdateTimestamp(market, originalMarket, 
				MarketRuntimeException.MARKET_CANNOT_BE_OVERWRITTEN_WITH_AN_OLDER_VERSION);
		
		checkMarketActivationStatusForChange(originalMarket, force_, 
				MarketRuntimeException.ACTIVE_MARKET_CANNOT_BE_CHANGED,
				MarketRuntimeException.MARKET_ONLY_IN_CREATED_OR_SUSPEDED_STATE_CAN_BE_CHANGED, propertyChangeSession);
		
		Instrument underlying = checkUnderlyingDependencies(market);
		
		MarketPropertyChangeExecutor marketPropertyChangeExecutor = new MarketPropertyChangeExecutor(market);
		
		setupChangedMarket(marketPropertyChangeExecutor, market, originalMarket, propertyChangeSession, underlying);

		market.updateLastUpdateTimestampAndVersion();

		addChangeAudit(market, marketPropertyChangeExecutor,
				propertyChangeSession, user_);
		
		_marketDatastore.updateMarket(market); 
	}
	
	/**
	 * Sets Market Calendar to Market
	 * @param marketCode_ Market to be changed
	 * @param marketCalendar_ Market Trading Calendar  
	 * @param force_ Force the change, even if the 
	 * 				 Product is in Approved State (It will lose Approval)
	 * @param user_ User 
	 * @param marketManagementContext_ Market Management Context
	 */
	public void setMarketCalendar(String marketCode_, BusinessCalendarDto marketCalendarDto_, boolean force_, String user_, MarketManagementContext marketManagementContext_) {
		BusinessCalendar marketCalendar = 
				marketCalendarDto_ != null ? 
				MarketDtoMapping.INSTANCE.fromDto(marketCalendarDto_) :
				null;
		Market market = getMarketInternal(marketCode_);
		
		if(marketCalendar != null) {			
			ValidatorUtils.validateBusinessCalendar(marketCalendar);
		}
		
		PropertyChangeSession propertyChangeSession = marketManagementContext_.getPropertyChangeSession();
		
		checkMarketActivationStatusForChange(market, force_, 
				MarketRuntimeException.ACTIVE_MARKET_CANNOT_BE_CHANGED,
				MarketRuntimeException.MARKET_ONLY_IN_CREATED_OR_SUSPEDED_STATE_CAN_BE_CHANGED, propertyChangeSession);
		
		MarketPropertyChangeExecutor marketPropertyChangeExecutor = new MarketPropertyChangeExecutor(market);
		
		marketPropertyChangeExecutor.setBusinessCalendar(
				marketCalendar != null ? BusinessCalendar.create(marketCalendar) : null, propertyChangeSession);
		
		addChangeAudit(market, marketPropertyChangeExecutor,
				propertyChangeSession, user_);
	}
	
	/**
	 * Delete Market
	 * @param marketCode_ Market to be Deleted
	 * @param user_ User 
	 * @param marketManagementContext_ Market Management Context
	 */
	public void deleteMarket(String marketCode_, String user_, MarketManagementContext marketManagementContext_) {
		Market market = getMarketInternal(marketCode_);
		
		if(market.getActivationStatus() == ActivationStatus.Activated) {
			throw MarketRuntimeException.createExceptionWithDetails(
					MarketRuntimeException.ACTIVE_MARKET_CANNOT_BE_DELETED, 
					null, new Object[] { marketCode_ });			
		} else  {
			checkForObjectForDelete(market, MarketRuntimeException.MARKET_ONLY_IN_CREATED_OR_SUSPEDED_STATE_CAN_BE_DELETED);

		}
		
		String marketCode = market.getCode();
		
		if(market.getActivationStatus() == ActivationStatus.Activated) {
			throw MarketRuntimeException.createExceptionWithDetails(
					MarketRuntimeException.ACTIVE_MARKET_CANNOT_BE_DELETED, 
					null, new Object[] { market.getCode() });			
		}
		
		// TODO: Check, if all trades on the market are fully settled
		
		_marketDatastore.deleteMarket(marketCode);
		
		marketManagementContext_.getPropertyChangeSession().getPropertyChangeBroker().getNotificationBroker().removeAllListeners(
				new DatastoreKey(marketCode), Market.class);
		
		addAuditTrailEntry(AuditEntryAction.Deleted, createAudit(user_), marketCode, OBJECT_TYPE);
	}
	
	/**
	 * Rollover Market
	 * @param marketCode_ Code of the Market (Source) to be Rolled Over to the given Instrument
	 * @param rolledOverInstrumentCode_ Code of the New Instrument for the Market to be rolled over to
	 * @param user_ User  
	 * @param marketManagementContext_ Market Management Context
	 * 
	 * @return Code of the Rolleover Market
	 */
	public String rolloverMarket(String marketCode_, String rolledOverInstrumentCode_, String user_, MarketManagementContext marketManagementContext_) {
		// check rollover criteria
		Market originalMarket = getMarketInternal(marketCode_);

		return super.rolloverMarket(originalMarket, rolledOverInstrumentCode_, user_, marketManagementContext_);		
	}
	
	/**
	 * Closes Market in case of an Emergency
	 * @param marketCode_ Market to be Closed 
	 * @param user_ User 
	 * @param marketManagementContext_ Market Management Context 
	 */
	public void emergencyCloseMarket(String marketCode_, String user_, MarketManagementContext marketManagementContext_) {
		Market market = getMarketInternal(marketCode_);
		
		super.emergencyCloseMarket(market, user_, marketManagementContext_);
	}
	
	/**
	 * Deactivates Market 
	 * @param marketCode_ Market to be Deactivated
	 * @param user_ User  
	 * @param marketManagementContext_ Market Management Context 
	 */
	public void deactivateMarket(String marketCode_, String user_, MarketManagementContext marketManagementContext_) {
		Market market = getMarketInternal(marketCode_);

		super.deactivateMarket(market, user_, marketManagementContext_);
	}

	/**
	 * Activates a Call Market 
	 * @param marketCode_ Market to be Activated
	 * @param nextCallDate_ Next Market Call Date 
	 * @param nextCallTime_ Next Market Call Time 
	 * @param user_ User 
	 * @param marketManagementContext_ Market Management Context 
	 */
	public void activateCallMarket(String marketCode_, Date nextCallDate_, TimeOfDayDto nextMarketCallTime_, String user_, MarketManagementContext marketManagementContext_) {
		Market market = getAndCheckMarketForActivation(marketCode_,
				user_, marketManagementContext_);
		
		super.activateCallMarket(market, nextCallDate_, MarketDtoMapping.INSTANCE.fromDto(nextMarketCallTime_), user_, marketManagementContext_);
	}
	
	private Market getAndCheckMarketForActivation(String marketCode_,
			String user_,
			MarketManagementContext marketManagementContext_) {	
		Market market = getMarketInternal(marketCode_);

		checkActivationCriteria(market);
		return market;
	}

	/**
	 * Activates a Market 
	 * @param marketCode_ Market to be Activated
	 * @param user_ User   
	 * @param marketManagementContext_ Market Management Context 
	 */
	public void activateMarket(String marketCode_, String user_, MarketManagementContext marketManagementContext_) {
		Market market = getAndCheckMarketForActivation(marketCode_,
				user_, marketManagementContext_);
		
		super.activateMarket(market, user_, marketManagementContext_);
	}
	
	/**
	 * Approve a Market 
	 * @param marketCode_ Market to be Approved
	 * @param lastUpdateTimestamp_ Last Update Timestamp of the Object
	 * @param user_ User  
	 * @param marketManagementContext_ Market Management Context
	 */
	public void approveMarket(String marketCode_, Date lastUpdateTimestamp_, String user_, MarketManagementContext marketManagementContext_) {		
		Market market = getMarketInternal(marketCode_);
		checkLastUpdateTimestampForApproval(lastUpdateTimestamp_, market);
		
		checkIfObjectInactive(market, MarketRuntimeException.MARKET_ONLY_IN_CREATED_OR_SUSPEDED_STATE_CAN_BE_APPROVED);
		
		// check market underlying objects 
		// TODO: Make exceptions more accurate 
		Rollable underlying = getInstrumentInternal(market.getInstrumentCode());
		checkIfObjectActive(underlying, MarketRuntimeException.MARKET_CAN_ONLY_BE_APPROVED_IF_UNDERLYING_INSTRUMENT_IS_APPROVED);
		
		MarketOperator operator = getMarketOperatorInternal(market.getMarketOperatorCode());
		checkIfObjectActive(operator, MarketRuntimeException.MARKET_CAN_ONLY_BE_APPROVED_IF_UNDERLYING_MARKET_OPERATOR_IS_APPROVED);
		
		if(!ValidatorUtils.isNonNullString(market.getMarketOperationContract())) {
			throw MarketRuntimeException.createExceptionWithDetails(
					MarketRuntimeException.MARKET_MUST_HAVE_MARKET_OPERATOR_CONTRACT, 
					null, new Object[] { market });
		}
		
		MarketPropertyChangeExecutor marketPropertyChangeExecutor = new MarketPropertyChangeExecutor(market);
		
		PropertyChangeSession propertyChangeSession = marketManagementContext_.getPropertyChangeSession(); 
		
		marketPropertyChangeExecutor.setActivationStatus(ActivationStatus.Approved, propertyChangeSession);
		
		marketPropertyChangeExecutor.setApprovalAudit(createAudit(user_), propertyChangeSession);
		market.updateLastUpdateTimestampAndVersion();

		// TODO: Save to historical datastore 
		
		addAuditTrailEntry(AuditEntryAction.Approved, market.getApprovalAudit(), marketCode_, OBJECT_TYPE);
	}
	
	/**
	 * Set Market Operation Contract on Market
	 * @param marketCode_ Market to be Changed 
	 * @param marketOperationContract_ Market Operation Contract to be set 
	 * @param user_ User  
	 * @param marketManagementContext_ Market Management Context
	 */
	public void setMarketOperationContract(String marketCode_, String marketOperationContract_, String user_, MarketManagementContext marketManagementContext_) {
		Market market = getMarketInternal(marketCode_);	

		checkIfObjectInactive(market, MarketRuntimeException.MARKET_ONLY_IN_CREATED_OR_SUSPEDED_STATE_CAN_BE_CHANGED);
		
		MarketPropertyChangeExecutor marketPropertyChangeExecutor = new MarketPropertyChangeExecutor(market);
		
		marketPropertyChangeExecutor.setMarketOperationContract(marketOperationContract_, marketManagementContext_.getPropertyChangeSession());
		market.updateLastUpdateTimestampAndVersion();
		
		addAuditTrailEntry(AuditEntryAction.Changed, createAudit(user_), marketCode_, OBJECT_TYPE);
	}
	
	/**
	 * Suspend a Market 
	 * @param marketCode_ Market to be Suspended
	 * @param userSession_ User Session 
	 * @param marketManagementContext_ Market Management Context
	 */
	public void suspendMarket(String marketCode_, String user_, MarketManagementContext marketManagementContext_) {
		Market market = getMarketInternal(marketCode_);
		
		if(market.getActivationStatus() == ActivationStatus.Activated) {
				throw MarketRuntimeException.createExceptionWithDetails(
						MarketRuntimeException.ACTIVE_MARKET_CANNOT_BE_SUSPENDED, 
						null, new Object[] { marketCode_ });
		}
		
		checkIfObjectActive(market, MarketRuntimeException.MARKET_ONLY_IN_APPROVED_OR_DEACTIVATED_STATE_CAN_BE_SUSPENDED);
		
		MarketPropertyChangeExecutor marketPropertyChangeExecutor = new MarketPropertyChangeExecutor(market);
		
		PropertyChangeSession propertyChangeSession = marketManagementContext_.getPropertyChangeSession();
		
		marketPropertyChangeExecutor.setActivationStatus(ActivationStatus.Suspended, propertyChangeSession);
		marketPropertyChangeExecutor.setSuspensionAudit(createAudit(user_), propertyChangeSession);
		market.updateLastUpdateTimestampAndVersion();
		
		addAuditTrailEntry(AuditEntryAction.Suspended, market.getSuspensionAudit(), marketCode_, OBJECT_TYPE);
	}

	private void setupNewMarket(MarketPropertyChangeExecutor marketPropertyChangeExecutor_, String user_, PropertyChangeSession propertyChangeSession_) {		
		marketPropertyChangeExecutor_.setActivationStatus(ActivationStatus.Created, propertyChangeSession_);
		marketPropertyChangeExecutor_.setState(MarketState.Closed, propertyChangeSession_);
	} 
	
	private void addChangeAudit(Market market_,
			MarketPropertyChangeExecutor marketPropertyChangeExecutor_,
			PropertyChangeSession propertyChangeSession,
			String user_) {
		AuditInformation audit = createAudit(user_);
		marketPropertyChangeExecutor_.setChangeAudit(audit, propertyChangeSession);
		market_.updateLastUpdateTimestampAndVersion();
		
		addAuditTrailEntry(AuditEntryAction.Changed, audit, market_.getCode(), OBJECT_TYPE);
	}	
}
