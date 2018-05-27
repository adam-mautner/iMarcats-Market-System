package com.imarcats.model.mutators;

import com.imarcats.model.CircuitBreaker;
import com.imarcats.model.Market;
import com.imarcats.model.MarketPropertyNames;
import com.imarcats.model.mutators.helpers.ActivationStatusWrapper;
import com.imarcats.model.mutators.helpers.ExecutionSystemWrapper;
import com.imarcats.model.mutators.helpers.MarketStateWrapper;
import com.imarcats.model.mutators.helpers.QuoteTypeWrapper;
import com.imarcats.model.mutators.helpers.RecurringActionDetailWrapper;
import com.imarcats.model.mutators.helpers.SecondaryOrderPrecedenceRuleTypeWrapper;
import com.imarcats.model.mutators.helpers.TradingSessionWrapper;
import com.imarcats.model.types.AuditInformation;
import com.imarcats.model.types.BooleanProperty;
import com.imarcats.model.types.BusinessCalendar;
import com.imarcats.model.types.DateProperty;
import com.imarcats.model.types.DoubleProperty;
import com.imarcats.model.types.Property;
import com.imarcats.model.types.IntProperty;
import com.imarcats.model.types.ObjectProperty;
import com.imarcats.model.types.SecondaryOrderPrecedenceRuleType;
import com.imarcats.model.types.StringProperty;
import com.imarcats.model.types.TimeOfDay;
import com.imarcats.model.types.TimePeriod;
import com.imarcats.model.utils.PropertyUtils;

/**
 * Changes a Property of the Market, this should be used when the System 
 * changes a Property of a Market (typically in used in the Client to update
 * Market, when the Market Property Change Notification arrives to Client). 
 * 
 * This Mutator does change the Properties controlled by the System.  
 * @author Adam
 */
public class SystemMarketMutator extends MutatorBase {

	public static final SystemMarketMutator INSTANCE = new SystemMarketMutator();
	
	/** singleton */ 
	private SystemMarketMutator() { 
		// System Properties 
		_mapPropertyNameToMutator.put(MarketPropertyNames.CREATION_AUDIT_PROPERTY, new ObjectPropertyMutator<Market, ObjectProperty>() {
			@Override
			public void changeProperty(Market market_, ObjectProperty property_) {
				market_.setCreationAudit((AuditInformation) property_.getValue());
			}
			
		});	
		_mapPropertyNameToMutator.put(MarketPropertyNames.CHANGE_AUDIT_PROPERTY, new ObjectPropertyMutator<Market, ObjectProperty>() {
			@Override
			public void changeProperty(Market market_, ObjectProperty property_) {
				market_.setChangeAudit((AuditInformation) property_.getValue());
			}
			
		});	
		_mapPropertyNameToMutator.put(MarketPropertyNames.ROLLOVER_AUDIT_PROPERTY, new ObjectPropertyMutator<Market, ObjectProperty>() {
			@Override
			public void changeProperty(Market market_, ObjectProperty property_) {
				market_.setRolloverAudit((AuditInformation) property_.getValue());
			}
			
		});	
		_mapPropertyNameToMutator.put(MarketPropertyNames.APPROVAL_AUDIT_PROPERTY, new ObjectPropertyMutator<Market, ObjectProperty>() {
			@Override
			public void changeProperty(Market market_, ObjectProperty property_) {
				market_.setApprovalAudit((AuditInformation) property_.getValue());
			}
			
		});	
		_mapPropertyNameToMutator.put(MarketPropertyNames.SUSPENSION_AUDIT_PROPERTY, new ObjectPropertyMutator<Market, ObjectProperty>() {
			@Override
			public void changeProperty(Market market_, ObjectProperty property_) {
				market_.setSuspensionAudit((AuditInformation) property_.getValue());
			}
			
		});	
		_mapPropertyNameToMutator.put(MarketPropertyNames.ACTIVATION_AUDIT_PROPERTY, new ObjectPropertyMutator<Market, ObjectProperty>() {
			@Override
			public void changeProperty(Market market_, ObjectProperty property_) {
				market_.setActivationAudit((AuditInformation) property_.getValue());
			}
			
		});	
		_mapPropertyNameToMutator.put(MarketPropertyNames.DEACTIVATION_AUDIT_PROPERTY, new ObjectPropertyMutator<Market, ObjectProperty>() {
			@Override
			public void changeProperty(Market market_, ObjectProperty property_) {
				market_.setDeactivationAudit((AuditInformation) property_.getValue());
			}
			
		});	
		_mapPropertyNameToMutator.put(MarketPropertyNames.NEXT_MARKET_CALL_DATE_PROPERTY, new ObjectPropertyMutator<Market, DateProperty>() {
			@Override
			public void changeProperty(Market market_, DateProperty property_) {
				market_.setNextMarketCallDate(property_.getValue());
			}
			
		});	
		_mapPropertyNameToMutator.put(MarketPropertyNames.STATE_PROPERTY, new ObjectPropertyMutator<Market, ObjectProperty>() {
			@Override
			public void changeProperty(Market market_, ObjectProperty property_) {
				market_.setState(((MarketStateWrapper) property_.getValue()).getValue());
			}
			
		});	
		_mapPropertyNameToMutator.put(MarketPropertyNames.ACTIVATION_STATUS_PROPERTY, new ObjectPropertyMutator<Market, ObjectProperty>() {
			@Override
			public void changeProperty(Market market_, ObjectProperty property_) {
				market_.setActivationStatus(((ActivationStatusWrapper) property_.getValue()).getValue());
			}
			
		});	
		_mapPropertyNameToMutator.put(MarketPropertyNames.HALT_LEVEL_PROPERTY, new ObjectPropertyMutator<Market, IntProperty>() {
			@Override
			public void changeProperty(Market market_, IntProperty property_) {
				market_.setHaltLevel((int) property_.getValue());
			}
			
		});	
		
		// MARKET_CALL_ACTION_KEY_PROPERTY not needed, not visible for the user
		// MARKET_OPEN_ACTION_KEY_PROPERTY not needed, not visible for the user
		// MARKET_CLOSE_ACTION_KEY_PROPERTY not needed, not visible for the user
		// MARKET_RE_OPEN_ACTION_KEY_PROPERTY not needed, not visible for the user
		// MARKET_SETTLEMENT_ACTION_KEY_PROPERTY not needed, not visible for the user
		// MARKET_MAINTENANCE_ACTION_KEY_PROPERTY not needed, not visible for the user
		
		_mapPropertyNameToMutator.put(MarketPropertyNames.MARKET_OPERATION_DAYS, new ObjectPropertyMutator<Market, ObjectProperty>() {
			@Override
			public void changeProperty(Market market_, ObjectProperty property_) {
				market_.setMarketOperationDays(((RecurringActionDetailWrapper) property_.getValue()).getValue());
			}
			
		});	
		// End of System Properties 		
		
		// User Properties 
		_mapPropertyNameToMutator.put(MarketPropertyNames.MARKET_CODE_PROPERTY, new ObjectPropertyMutator<Market, StringProperty>() {
			@Override
			public void changeProperty(Market market_, StringProperty property_) {
				market_.setMarketCode(property_.getValue());
			}
			
		});	
		_mapPropertyNameToMutator.put(MarketPropertyNames.INSTRUMENT_CODE_PROPERTY, new ObjectPropertyMutator<Market, StringProperty>() {
			@Override
			public void changeProperty(Market market_, StringProperty property_) {
				market_.setInstrumentCode(property_.getValue());
			}
			
		});	
		_mapPropertyNameToMutator.put(MarketPropertyNames.NAME_PROPERTY, new ObjectPropertyMutator<Market, StringProperty>() {
			@Override
			public void changeProperty(Market market_, StringProperty property_) {
				market_.setName(property_.getValue());
			}
			
		});	
		_mapPropertyNameToMutator.put(MarketPropertyNames.DESCRIPTION_PROPERTY, new ObjectPropertyMutator<Market, StringProperty>() {
			@Override
			public void changeProperty(Market market_, StringProperty property_) {
				market_.setDescription(property_.getValue());
			}
			
		});	
		_mapPropertyNameToMutator.put(MarketPropertyNames.MARKET_OPERATOR_CODE_PROPERTY, new ObjectPropertyMutator<Market, StringProperty>() {
			@Override
			public void changeProperty(Market market_, StringProperty property_) {
				market_.setMarketOperatorCode(property_.getValue());
			}
			
		});	
		_mapPropertyNameToMutator.put(MarketPropertyNames.QUOTE_TYPE_PROPERTY, new ObjectPropertyMutator<Market, ObjectProperty>() {
			@Override
			public void changeProperty(Market market_, ObjectProperty property_) {
				market_.setQuoteType(((QuoteTypeWrapper) property_.getValue()).getValue());
			}
			
		});	
		_mapPropertyNameToMutator.put(MarketPropertyNames.BUSINESS_ENTITY_CODE_PROPERTY, new ObjectPropertyMutator<Market, StringProperty>() {
			@Override
			public void changeProperty(Market market_, StringProperty property_) {
				market_.setBusinessEntityCode(property_.getValue());
			}
			
		});	
		_mapPropertyNameToMutator.put(MarketPropertyNames.MINIMUM_CONTRACTS_TRADED_PROPERTY, new ObjectPropertyMutator<Market, IntProperty>() {
			@Override
			public void changeProperty(Market market_, IntProperty property_) {
				market_.setMinimumContractsTraded((int) property_.getValue());
			}
			
		});	
		_mapPropertyNameToMutator.put(MarketPropertyNames.MAXIMUM_CONTRACTS_TRADED_PROPERTY, new ObjectPropertyMutator<Market, IntProperty>() {
			@Override
			public void changeProperty(Market market_, IntProperty property_) {
				market_.setMaximumContractsTraded((int) property_.getValue());
			}
			
		});
		_mapPropertyNameToMutator.put(MarketPropertyNames.MINIMUM_QUOTE_INCREMENT_PROPERTY, new ObjectPropertyMutator<Market, DoubleProperty>() {
			@Override
			public void changeProperty(Market market_, DoubleProperty property_) {
				market_.setMinimumQuoteIncrement(property_.getValue());
			}
			
		});	
		_mapPropertyNameToMutator.put(MarketPropertyNames.TRADING_SESSION_PROPERTY, new ObjectPropertyMutator<Market, ObjectProperty>() {
			@Override
			public void changeProperty(Market market_, ObjectProperty property_) {
				market_.setTradingSession(((TradingSessionWrapper) property_.getValue()).getValue());
			}
			
		});	
		_mapPropertyNameToMutator.put(MarketPropertyNames.TRADING_HOURS_PROPERTY, new ObjectPropertyMutator<Market, ObjectProperty>() {
			@Override
			public void changeProperty(Market market_, ObjectProperty property_) {
				market_.setTradingHours((TimePeriod) property_.getValue());
			}
			
		});	
		_mapPropertyNameToMutator.put(MarketPropertyNames.TRADING_DAY_END_PROPERTY, new ObjectPropertyMutator<Market, ObjectProperty>() {
			@Override
			public void changeProperty(Market market_, ObjectProperty property_) {
				market_.setTradingDayEnd((TimeOfDay) property_.getValue());
			}
			
		});		
		_mapPropertyNameToMutator.put(MarketPropertyNames.BUSINESS_CALENDAR_PROPERTY, new ObjectPropertyMutator<Market, ObjectProperty>() {
			@Override
			public void changeProperty(Market market_, ObjectProperty property_) {
				market_.setBusinessCalendar((BusinessCalendar) property_.getValue());
			}
			
		});	
		_mapPropertyNameToMutator.put(MarketPropertyNames.EXECUTION_SYSTEM_PROPERTY, new ObjectPropertyMutator<Market, ObjectProperty>() {
			@Override
			public void changeProperty(Market market_, ObjectProperty property_) {
				market_.setExecutionSystem(((ExecutionSystemWrapper) property_.getValue()).getValue());
			}
			
		});	
		_mapPropertyNameToMutator.put(MarketPropertyNames.CIRCUIT_BREAKER_PROPERTY, new ObjectPropertyMutator<Market, ObjectProperty>() {
			@Override
			public void changeProperty(Market market_, ObjectProperty property_) {
				market_.setCircuitBreaker((CircuitBreaker) property_.getValue());
			}
			
		});	
		_mapPropertyNameToMutator.put(MarketPropertyNames.CLEARING_BANK_PROPERTY, new ObjectPropertyMutator<Market, StringProperty>() {
			@Override
			public void changeProperty(Market market_, StringProperty property_) {
				market_.setClearingBank(property_.getValue());
			}
			
		});	
		_mapPropertyNameToMutator.put(MarketPropertyNames.COMMISSION_PROPERTY, new ObjectPropertyMutator<Market, DoubleProperty>() {
			@Override
			public void changeProperty(Market market_, DoubleProperty property_) {
				market_.setCommission(property_.getValue());
			}
			
		});	
		_mapPropertyNameToMutator.put(MarketPropertyNames.COMMISSION_CURRENCY_PROPERTY, new ObjectPropertyMutator<Market, StringProperty>() {
			@Override
			public void changeProperty(Market market_, StringProperty property_) {
				market_.setCommissionCurrency(property_.getValue());
			}
			
		});	
		_mapPropertyNameToMutator.put(MarketPropertyNames.ALLOW_HIDDEN_ORDERS, new ObjectPropertyMutator<Market, BooleanProperty>() {
			@Override
			public void changeProperty(Market market_, BooleanProperty property_) {
				market_.setAllowHiddenOrders(property_.getValue());
			}
			
		});	
		_mapPropertyNameToMutator.put(MarketPropertyNames.ALLOW_SIZE_RESTRICTION_ON_ORDERS, new ObjectPropertyMutator<Market, BooleanProperty>() {
			@Override
			public void changeProperty(Market market_, BooleanProperty property_) {
				market_.setAllowSizeRestrictionOnOrders(property_.getValue());
			}
			
		});	
		_mapPropertyNameToMutator.put(MarketPropertyNames.MARKET_TIMEZONE_ID, new ObjectPropertyMutator<Market, StringProperty>() {
			@Override
			public void changeProperty(Market market_, StringProperty property_) {
				market_.setMarketTimeZoneID(property_.getValue());
			}
			
		});	
		// End of User Properties 

		// Administrator Properties
		_mapPropertyNameToMutator.put(MarketPropertyNames.MARKET_OPERATOR_CONTRACT_PROPERTY, new ObjectPropertyMutator<Market, StringProperty>() {
			@Override
			public void changeProperty(Market market_, StringProperty property_) {
				market_.setMarketOperationContract(property_.getValue());
			}
			
		});	
		// End of Administrator Properties 
		
		// Property List
		_mapPropertyListNameToAccessor.put(MarketPropertyNames.SECONDARY_ORDER_PRECEDENCE_RULES_LIST, new ObjectPropertyListAccessor<Market>() {
			@Override
			public Property[] getList(Market market_) {
				Property[] properties = new Property[market_.getSecondaryOrderPrecedenceRules().size()];
				for (int i = 0; i < properties.length; i++) {
					SecondaryOrderPrecedenceRuleType rule = market_.getSecondaryOrderPrecedenceRules().get(i);
					properties[i] = PropertyUtils.createObjectProperty(rule.name(), new SecondaryOrderPrecedenceRuleTypeWrapper(rule));
				}
				return properties;
			}

			@Override
			public void addToList(Market market_, Property property_) {
				SecondaryOrderPrecedenceRuleType ruleToBeAdded = getRule(property_);	
				market_.getSecondaryOrderPrecedenceRules().add(ruleToBeAdded);
			}

			@Override
			public void clearList(Market market_) {
				market_.getSecondaryOrderPrecedenceRules().clear();
			}

			@Override
			public void deleteFromList(Market market_, Property property_) {
				SecondaryOrderPrecedenceRuleType ruleToBeDeleted = getRule(property_);
				market_.getSecondaryOrderPrecedenceRules().remove(ruleToBeDeleted);
			}

			private SecondaryOrderPrecedenceRuleType getRule(Property property_) {
				ObjectProperty objectProperty = (ObjectProperty) property_;
				return ((SecondaryOrderPrecedenceRuleTypeWrapper) objectProperty.getValue()).getValue();
			}
		});
		// End of Property List
	}
	
}
