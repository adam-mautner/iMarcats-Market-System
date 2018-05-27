package com.imarcats.internal.server.interfaces.market;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.imarcats.interfaces.client.v100.notification.ObjectVersion;
import com.imarcats.internal.server.infrastructure.notification.properties.PropertyChangeSession;
import com.imarcats.model.CircuitBreaker;
import com.imarcats.model.Market;
import com.imarcats.model.MarketPropertyNames;
import com.imarcats.model.mutators.ChangeAction;
import com.imarcats.model.mutators.PropertyChange;
import com.imarcats.model.mutators.helpers.ActivationStatusWrapper;
import com.imarcats.model.mutators.helpers.ExecutionSystemWrapper;
import com.imarcats.model.mutators.helpers.MarketStateWrapper;
import com.imarcats.model.mutators.helpers.QuoteTypeWrapper;
import com.imarcats.model.mutators.helpers.RecurringActionDetailWrapper;
import com.imarcats.model.mutators.helpers.SecondaryOrderPrecedenceRuleTypeWrapper;
import com.imarcats.model.mutators.helpers.TradingSessionWrapper;
import com.imarcats.model.types.ActivationStatus;
import com.imarcats.model.types.AuditInformation;
import com.imarcats.model.types.BusinessCalendar;
import com.imarcats.model.types.DatastoreKey;
import com.imarcats.model.types.ExecutionSystem;
import com.imarcats.model.types.Property;
import com.imarcats.model.types.MarketState;
import com.imarcats.model.types.QuoteType;
import com.imarcats.model.types.RecurringActionDetail;
import com.imarcats.model.types.SecondaryOrderPrecedenceRuleType;
import com.imarcats.model.types.TimeOfDay;
import com.imarcats.model.types.TimePeriod;
import com.imarcats.model.types.TradingSession;
import com.imarcats.model.utils.PropertyUtils;

/**
 * Executes Property Change on the Market and 
 * takes care of the sending Notification Messages as well
 * 
 * Note: Market Data and some Properties used only by the System are NOT
 * 		 included to this mutator. These ones must be handled differently.
 * 
 * Note: For now it will always send notification, if value is set for a Property, 
 * 		 even if the value of the Property is the same. 
 * @author Adam
 *
 */
public class MarketPropertyChangeExecutor {

	private final Market _marketModel;

	public MarketPropertyChangeExecutor(Market marketModel_) {
		super();
		_marketModel = marketModel_;
	} 

	private DatastoreKey getMarketKey() {
		return new DatastoreKey(_marketModel.getMarketCode());
	}
	
	private void notifyAboutPropertyChange(
			PropertyChangeSession propertyChangeSession_,
			Property property_) {
		PropertyChange[] propertyChanges = PropertyUtils.createPropertyChangeList(property_); 
		propertyChangeSession_.recordPropertyChanges(
				getMarketKey(), 
				Market.class, 
				null,
				propertyChanges, 
				getLastUpdateTimestamp(), 
				getVersionObject(),
				null);
	}
	
	public void setState(MarketState state_, PropertyChangeSession propertyChangeSession_) {
		_marketModel.setState(state_);
		notifyAboutPropertyChange(propertyChangeSession_,
				PropertyUtils.createObjectProperty(MarketPropertyNames.STATE_PROPERTY, new MarketStateWrapper(state_)));
	}

	public void setActivationStatus(ActivationStatus activationStatus_, PropertyChangeSession propertyChangeSession_) {
		_marketModel.setActivationStatus(activationStatus_);
		notifyAboutPropertyChange(propertyChangeSession_,
				PropertyUtils.createObjectProperty(MarketPropertyNames.ACTIVATION_STATUS_PROPERTY, new ActivationStatusWrapper(activationStatus_)));
	}
	
	public void setNextMarketCallDate(Date nextMarketCallDate, PropertyChangeSession propertyChangeSession_) {
		_marketModel.setNextMarketCallDate(nextMarketCallDate);
		notifyAboutPropertyChange(propertyChangeSession_,
				PropertyUtils.createDateProperty(MarketPropertyNames.NEXT_MARKET_CALL_DATE_PROPERTY, nextMarketCallDate));
	}

	public void setName(String name_, PropertyChangeSession propertyChangeSession_) {
		_marketModel.setName(name_);
		notifyAboutPropertyChange(propertyChangeSession_,
				PropertyUtils.createStringProperty(MarketPropertyNames.NAME_PROPERTY, name_));
	}

	public void setDescription(String description_, PropertyChangeSession propertyChangeSession_) {
		_marketModel.setDescription(description_);
		notifyAboutPropertyChange(propertyChangeSession_,
				PropertyUtils.createStringProperty(MarketPropertyNames.DESCRIPTION_PROPERTY, description_));
	}

	public void setMarketOperationContract(String marketOperationContract_, PropertyChangeSession propertyChangeSession_) {
		_marketModel.setMarketOperationContract(marketOperationContract_);
		notifyAboutPropertyChange(propertyChangeSession_,
				PropertyUtils.createStringProperty(MarketPropertyNames.MARKET_OPERATOR_CONTRACT_PROPERTY, marketOperationContract_));
	}

	public void setQuoteType(QuoteType quoteType_, PropertyChangeSession propertyChangeSession_) {
		_marketModel.setQuoteType(quoteType_);
		notifyAboutPropertyChange(propertyChangeSession_,
				PropertyUtils.createObjectProperty(MarketPropertyNames.QUOTE_TYPE_PROPERTY, new QuoteTypeWrapper(quoteType_)));
	}

	public void setBusinessEntityCode(String businessEntityCode_, PropertyChangeSession propertyChangeSession_) {
		_marketModel.setBusinessEntityCode(businessEntityCode_);
		notifyAboutPropertyChange(propertyChangeSession_,
				PropertyUtils.createStringProperty(MarketPropertyNames.BUSINESS_ENTITY_CODE_PROPERTY, businessEntityCode_));
	}

	public void setMinimumContractsTraded(int minimumContractsTraded_, PropertyChangeSession propertyChangeSession_) {
		_marketModel.setMinimumContractsTraded(minimumContractsTraded_);
		notifyAboutPropertyChange(propertyChangeSession_,
				PropertyUtils.createIntProperty(MarketPropertyNames.MINIMUM_CONTRACTS_TRADED_PROPERTY, minimumContractsTraded_));
	}

	public void setMaximumContractsTraded(int maximumContractsTraded_, PropertyChangeSession propertyChangeSession_) {
		_marketModel.setMaximumContractsTraded(maximumContractsTraded_);
		notifyAboutPropertyChange(propertyChangeSession_,
				PropertyUtils.createIntProperty(MarketPropertyNames.MAXIMUM_CONTRACTS_TRADED_PROPERTY, maximumContractsTraded_));
	}

	public void setMinimumQuoteIncrement(double minimumQuoteIncrement_, PropertyChangeSession propertyChangeSession_) {
		_marketModel.setMinimumQuoteIncrement(minimumQuoteIncrement_);
		notifyAboutPropertyChange(propertyChangeSession_,
				PropertyUtils.createDoubleProperty(MarketPropertyNames.MINIMUM_QUOTE_INCREMENT_PROPERTY, minimumQuoteIncrement_));
	}

	public void setTradingSession(TradingSession tradingSession_, PropertyChangeSession propertyChangeSession_) {
		_marketModel.setTradingSession(tradingSession_);
		notifyAboutPropertyChange(propertyChangeSession_,
				PropertyUtils.createObjectProperty(MarketPropertyNames.TRADING_SESSION_PROPERTY, new TradingSessionWrapper(tradingSession_)));
	}

	public void setMarketOperationDays(RecurringActionDetail marketOperationDays_, PropertyChangeSession propertyChangeSession_) {
		_marketModel.setMarketOperationDays(marketOperationDays_);
		notifyAboutPropertyChange(propertyChangeSession_,
				PropertyUtils.createObjectProperty(MarketPropertyNames.MARKET_OPERATION_DAYS, new RecurringActionDetailWrapper(marketOperationDays_)));		
	}
	
	public void setAllowHiddenOrders(boolean allowHiddenOrders_, PropertyChangeSession propertyChangeSession_) {
		_marketModel.setAllowHiddenOrders(allowHiddenOrders_);
		notifyAboutPropertyChange(propertyChangeSession_,
				PropertyUtils.createBooleanProperty(MarketPropertyNames.ALLOW_HIDDEN_ORDERS, allowHiddenOrders_));		
	}

	public void setAllowSizeRestrictionOnOrders(boolean allowSizeRestrictionOnOrders_, PropertyChangeSession propertyChangeSession_) {
		_marketModel.setAllowSizeRestrictionOnOrders(allowSizeRestrictionOnOrders_);
		notifyAboutPropertyChange(propertyChangeSession_,
				PropertyUtils.createBooleanProperty(MarketPropertyNames.ALLOW_SIZE_RESTRICTION_ON_ORDERS, allowSizeRestrictionOnOrders_));		
	}
	
	public void setMarketTimeZoneID(String marketTimeZoneID_, PropertyChangeSession propertyChangeSession_) {
		_marketModel.setMarketTimeZoneID(marketTimeZoneID_);
		notifyAboutPropertyChange(propertyChangeSession_,
				PropertyUtils.createStringProperty(MarketPropertyNames.MARKET_TIMEZONE_ID, marketTimeZoneID_));		
	}
	
	public void setTradingHours(TimePeriod tradingHours_, PropertyChangeSession propertyChangeSession_) {
		_marketModel.setTradingHours(tradingHours_);
		notifyAboutPropertyChange(propertyChangeSession_,
				PropertyUtils.createObjectProperty(MarketPropertyNames.TRADING_HOURS_PROPERTY, tradingHours_));
	}

	public void setTradingDayEnd(TimeOfDay tradingDayEnd_, PropertyChangeSession propertyChangeSession_) {
		_marketModel.setTradingDayEnd(tradingDayEnd_);
		notifyAboutPropertyChange(propertyChangeSession_,
				PropertyUtils.createObjectProperty(MarketPropertyNames.TRADING_DAY_END_PROPERTY, tradingDayEnd_));
	}


	public void setExecutionSystem(ExecutionSystem executionSystem_, PropertyChangeSession propertyChangeSession_) {
		_marketModel.setExecutionSystem(executionSystem_);
		notifyAboutPropertyChange(propertyChangeSession_,
				PropertyUtils.createObjectProperty(MarketPropertyNames.EXECUTION_SYSTEM_PROPERTY, new ExecutionSystemWrapper(executionSystem_)));
	}

	public void setSecondaryOrderPrecedenceRules(List<SecondaryOrderPrecedenceRuleType> secondaryOrderPrecedenceRule_, PropertyChangeSession propertyChangeSession_) {
		List<PropertyChange> changes = new ArrayList<PropertyChange>();
		
		_marketModel.getSecondaryOrderPrecedenceRules().clear();
		changes.add(
				PropertyUtils.createValueListChange(ChangeAction.Clear, null, MarketPropertyNames.SECONDARY_ORDER_PRECEDENCE_RULES_LIST));
		
		for (SecondaryOrderPrecedenceRuleType secondaryOrderPrecedenceRuleType : secondaryOrderPrecedenceRule_) {
			_marketModel.getSecondaryOrderPrecedenceRules().add(secondaryOrderPrecedenceRuleType);
			changes.add(
					PropertyUtils.createValueListChange(ChangeAction.Add, 
							PropertyUtils.createObjectProperty(secondaryOrderPrecedenceRuleType.name(), new SecondaryOrderPrecedenceRuleTypeWrapper(secondaryOrderPrecedenceRuleType)), 
							MarketPropertyNames.SECONDARY_ORDER_PRECEDENCE_RULES_LIST));
		}
		
		propertyChangeSession_.recordPropertyChanges(
				getMarketKey(), 
				Market.class, 
				null,
				changes.toArray(new PropertyChange[changes.size()]), 
				getLastUpdateTimestamp(), 
				getVersionObject(), 
				null);
	}

	private Date getLastUpdateTimestamp() {
		return _marketModel.getLastUpdateTimestamp() != null ? _marketModel.getLastUpdateTimestamp() : new Date();
	}

	public void setClearingBank(String clearingBank_, PropertyChangeSession propertyChangeSession_) {
		_marketModel.setClearingBank(clearingBank_);
		notifyAboutPropertyChange(propertyChangeSession_,
				PropertyUtils.createStringProperty(MarketPropertyNames.CLEARING_BANK_PROPERTY, clearingBank_));
	}
	
	public void setCommission(double commission_, PropertyChangeSession propertyChangeSession_) {
		_marketModel.setCommission(commission_);
		notifyAboutPropertyChange(propertyChangeSession_,
				PropertyUtils.createDoubleProperty(MarketPropertyNames.COMMISSION_PROPERTY, commission_));
	}

	public void setCreationAudit(AuditInformation creationAudit_, PropertyChangeSession propertyChangeSession_) {
		_marketModel.setCreationAudit(creationAudit_);
		notifyAboutPropertyChange(propertyChangeSession_,
				PropertyUtils.createObjectProperty(MarketPropertyNames.CREATION_AUDIT_PROPERTY, creationAudit_));
	}
	
	public void setApprovalAudit(AuditInformation approvalAudit, PropertyChangeSession propertyChangeSession_) {
		_marketModel.setApprovalAudit(approvalAudit);
		notifyAboutPropertyChange(propertyChangeSession_,
				PropertyUtils.createObjectProperty(MarketPropertyNames.APPROVAL_AUDIT_PROPERTY, approvalAudit));
	}

	public void setChangeAudit(AuditInformation changeAudit_, PropertyChangeSession propertyChangeSession_) {
		_marketModel.setChangeAudit(changeAudit_);
		notifyAboutPropertyChange(propertyChangeSession_,
				PropertyUtils.createObjectProperty(MarketPropertyNames.CHANGE_AUDIT_PROPERTY, changeAudit_));
	}

	public void setRolloverAudit(AuditInformation rolloverAudit_, PropertyChangeSession propertyChangeSession_) {
		_marketModel.setRolloverAudit(rolloverAudit_);
		notifyAboutPropertyChange(propertyChangeSession_,
				PropertyUtils.createObjectProperty(MarketPropertyNames.ROLLOVER_AUDIT_PROPERTY, rolloverAudit_));
	}

	public void setSuspensionAudit(AuditInformation suspensionAudit, PropertyChangeSession propertyChangeSession_) {
		_marketModel.setSuspensionAudit(suspensionAudit);
		notifyAboutPropertyChange(propertyChangeSession_,
				PropertyUtils.createObjectProperty(MarketPropertyNames.SUSPENSION_AUDIT_PROPERTY, suspensionAudit));
	}

	public void setActivationAudit(AuditInformation activationAudit, PropertyChangeSession propertyChangeSession_) {
		_marketModel.setActivationAudit(activationAudit);
		notifyAboutPropertyChange(propertyChangeSession_,
				PropertyUtils.createObjectProperty(MarketPropertyNames.ACTIVATION_AUDIT_PROPERTY, activationAudit));
	}
	
	public void setDeactivationAudit(AuditInformation deactivationAudit, PropertyChangeSession propertyChangeSession_) {
		_marketModel.setDeactivationAudit(deactivationAudit);
		notifyAboutPropertyChange(propertyChangeSession_,
				PropertyUtils.createObjectProperty(MarketPropertyNames.DEACTIVATION_AUDIT_PROPERTY, deactivationAudit));
	}
	
	public void setCircuitBreaker(CircuitBreaker circuitBreaker, PropertyChangeSession propertyChangeSession_) {
		_marketModel.setCircuitBreaker(circuitBreaker);
		notifyAboutPropertyChange(propertyChangeSession_,
				PropertyUtils.createObjectProperty(MarketPropertyNames.CIRCUIT_BREAKER_PROPERTY, circuitBreaker));
	}

	public void setBusinessCalendar(BusinessCalendar businessCalendar, PropertyChangeSession propertyChangeSession_) {
		_marketModel.setBusinessCalendar(businessCalendar);
		notifyAboutPropertyChange(propertyChangeSession_,
				PropertyUtils.createObjectProperty(MarketPropertyNames.BUSINESS_CALENDAR_PROPERTY, businessCalendar));
	}

	public void setHaltLevel(int haltLevel, PropertyChangeSession propertyChangeSession_) {
		_marketModel.setHaltLevel(haltLevel);
		notifyAboutPropertyChange(propertyChangeSession_,
				PropertyUtils.createIntProperty(MarketPropertyNames.HALT_LEVEL_PROPERTY, haltLevel));
	}
	
	public void setCommissionCurrency(String commissionCurrency, PropertyChangeSession propertyChangeSession_) {
		_marketModel.setCommissionCurrency(commissionCurrency);
		notifyAboutPropertyChange(propertyChangeSession_,
				PropertyUtils.createStringProperty(MarketPropertyNames.COMMISSION_CURRENCY_PROPERTY, commissionCurrency));
	}

	private ObjectVersion getVersionObject() {
		ObjectVersion version = new ObjectVersion();
		version.setVersion(_marketModel.getVersionNumber());
		
		return version;
	}
}
