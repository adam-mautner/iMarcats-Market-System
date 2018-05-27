package com.imarcats.model.test.mutators;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.imarcats.model.CircuitBreaker;
import com.imarcats.model.Market;
import com.imarcats.model.MarketPropertyNames;
import com.imarcats.model.mutators.DuplicatePropertyException;
import com.imarcats.model.mutators.SystemMarketMutator;
import com.imarcats.model.mutators.helpers.ActivationStatusWrapper;
import com.imarcats.model.mutators.helpers.ExecutionSystemWrapper;
import com.imarcats.model.mutators.helpers.MarketStateWrapper;
import com.imarcats.model.mutators.helpers.QuoteTypeWrapper;
import com.imarcats.model.mutators.helpers.RecurringActionDetailWrapper;
import com.imarcats.model.mutators.helpers.SecondaryOrderPrecedenceRuleTypeWrapper;
import com.imarcats.model.mutators.helpers.TradingSessionWrapper;
import com.imarcats.model.test.testutils.MarketObjectTestBase;
import com.imarcats.model.types.ActivationStatus;
import com.imarcats.model.types.AuditInformation;
import com.imarcats.model.types.BusinessCalendar;
import com.imarcats.model.types.ExecutionSystem;
import com.imarcats.model.types.MarketState;
import com.imarcats.model.types.QuoteType;
import com.imarcats.model.types.RecurringActionDetail;
import com.imarcats.model.types.SecondaryOrderPrecedenceRuleType;
import com.imarcats.model.types.TimeOfDay;
import com.imarcats.model.types.TimePeriod;
import com.imarcats.model.types.TradingSession;

public class MarketMutatorTest extends MarketObjectTestBase {

	public void testPropertyChange() throws Exception {
		Market market = new Market();
		market.setSecondaryOrderPrecedenceRules(createDefaultSecondaryPrecedenceRuleList());
		
		// test system properties
		AuditInformation auditInformation = createAudit();
		auditInformation.setUserID("1");
		SystemMarketMutator.INSTANCE.changePropertyValue(market, getObjectProperty(MarketPropertyNames.CREATION_AUDIT_PROPERTY, auditInformation));
		assertEqualsAudit(auditInformation, market.getCreationAudit());	
		
		auditInformation = createAudit();
		auditInformation.setUserID("2");
		SystemMarketMutator.INSTANCE.changePropertyValue(market, getObjectProperty(MarketPropertyNames.CHANGE_AUDIT_PROPERTY, auditInformation));
		assertEqualsAudit(auditInformation, market.getChangeAudit());
		
		auditInformation = createAudit();
		auditInformation.setUserID("3");
		SystemMarketMutator.INSTANCE.changePropertyValue(market, getObjectProperty(MarketPropertyNames.ROLLOVER_AUDIT_PROPERTY, auditInformation));
		assertEqualsAudit(auditInformation, market.getRolloverAudit());		
		
		auditInformation = createAudit();
		auditInformation.setUserID("3");
		SystemMarketMutator.INSTANCE.changePropertyValue(market, getObjectProperty(MarketPropertyNames.APPROVAL_AUDIT_PROPERTY, auditInformation));
		assertEqualsAudit(auditInformation, market.getApprovalAudit());
		
		auditInformation = createAudit();
		auditInformation.setUserID("4");
		SystemMarketMutator.INSTANCE.changePropertyValue(market, getObjectProperty(MarketPropertyNames.SUSPENSION_AUDIT_PROPERTY, auditInformation));
		assertEqualsAudit(auditInformation, market.getSuspensionAudit());

		auditInformation = createAudit();
		auditInformation.setUserID("5");
		SystemMarketMutator.INSTANCE.changePropertyValue(market, getObjectProperty(MarketPropertyNames.ACTIVATION_AUDIT_PROPERTY, auditInformation));
		assertEqualsAudit(auditInformation, market.getActivationAudit());
		
		auditInformation = createAudit();
		auditInformation.setUserID("6");
		SystemMarketMutator.INSTANCE.changePropertyValue(market, getObjectProperty(MarketPropertyNames.DEACTIVATION_AUDIT_PROPERTY, auditInformation));
		assertEqualsAudit(auditInformation, market.getDeactivationAudit());
		
		Date nextCall = new Date();
		SystemMarketMutator.INSTANCE.changePropertyValue(market, getDateProperty(MarketPropertyNames.NEXT_MARKET_CALL_DATE_PROPERTY, nextCall));
		assertEquals(nextCall.getTime(), market.getNextMarketCallDate().getTime());
		
		MarketState state = MarketState.Called;
		SystemMarketMutator.INSTANCE.changePropertyValue(market, getObjectProperty(MarketPropertyNames.STATE_PROPERTY, new MarketStateWrapper(state)));
		assertEquals(state, market.getState());
		
		ActivationStatus status = ActivationStatus.Suspended;
		SystemMarketMutator.INSTANCE.changePropertyValue(market, getObjectProperty(MarketPropertyNames.ACTIVATION_STATUS_PROPERTY, new ActivationStatusWrapper(status)));
		assertEquals(status, market.getActivationStatus());
		
		int haltLevel = 123;
		SystemMarketMutator.INSTANCE.changePropertyValue(market, getIntProperty(MarketPropertyNames.HALT_LEVEL_PROPERTY, haltLevel));
		assertEquals(haltLevel, market.getHaltLevel());
		
		// test user properties
		RecurringActionDetail operationDays = RecurringActionDetail.OnBusinessDaysOnly;
		SystemMarketMutator.INSTANCE.changePropertyValue(market, getObjectProperty(MarketPropertyNames.MARKET_OPERATION_DAYS, new RecurringActionDetailWrapper(operationDays)));
		assertEquals(operationDays, market.getMarketOperationDays());
		
		boolean allowHiddenOrders = true;
		SystemMarketMutator.INSTANCE.changePropertyValue(market, getBooleanProperty(MarketPropertyNames.ALLOW_HIDDEN_ORDERS, allowHiddenOrders));
		assertEquals(allowHiddenOrders, market.getAllowHiddenOrders());
		
		boolean allowSizeRestrictionOnOrders = true;
		SystemMarketMutator.INSTANCE.changePropertyValue(market, getBooleanProperty(MarketPropertyNames.ALLOW_SIZE_RESTRICTION_ON_ORDERS, allowSizeRestrictionOnOrders));
		assertEquals(allowSizeRestrictionOnOrders, market.getAllowSizeRestrictionOnOrders());
		
		String marketTimeZoneID = "TestZone";
		SystemMarketMutator.INSTANCE.changePropertyValue(market, getStringProperty(MarketPropertyNames.MARKET_TIMEZONE_ID, marketTimeZoneID));
		assertEquals(marketTimeZoneID, market.getMarketTimeZoneID());
		
		String code = "TestCode";
		SystemMarketMutator.INSTANCE.changePropertyValue(market, getStringProperty(MarketPropertyNames.MARKET_CODE_PROPERTY, code));
		assertEquals(code, market.getMarketCode());		

		String instr = "TestInstr";
		SystemMarketMutator.INSTANCE.changePropertyValue(market, getStringProperty(MarketPropertyNames.INSTRUMENT_CODE_PROPERTY, instr));
		assertEquals(instr, market.getInstrumentCode());	
		
		String name = "TestName";
		SystemMarketMutator.INSTANCE.changePropertyValue(market, getStringProperty(MarketPropertyNames.NAME_PROPERTY, name));
		assertEquals(name, market.getName());	
		
		String descr = "TestDescr";
		SystemMarketMutator.INSTANCE.changePropertyValue(market, getStringProperty(MarketPropertyNames.DESCRIPTION_PROPERTY, descr));
		assertEquals(descr, market.getDescription());
		
		String opt = "TestOpt";
		SystemMarketMutator.INSTANCE.changePropertyValue(market, getStringProperty(MarketPropertyNames.MARKET_OPERATOR_CODE_PROPERTY, opt));
		assertEquals(opt, market.getMarketOperatorCode());
		
		QuoteType qt = QuoteType.Yield;
		SystemMarketMutator.INSTANCE.changePropertyValue(market, getObjectProperty(MarketPropertyNames.QUOTE_TYPE_PROPERTY, new QuoteTypeWrapper(qt)));
		assertEquals(qt, market.getQuoteType());
		
		String businessEntity = "TestBusinessEntity";
		SystemMarketMutator.INSTANCE.changePropertyValue(market, getStringProperty(MarketPropertyNames.BUSINESS_ENTITY_CODE_PROPERTY, businessEntity));
		assertEquals(businessEntity, market.getBusinessEntityCode());
		
		int minc = 10;
		SystemMarketMutator.INSTANCE.changePropertyValue(market, getIntProperty(MarketPropertyNames.MINIMUM_CONTRACTS_TRADED_PROPERTY, minc));
		assertEquals(minc, market.getMinimumContractsTraded());
		
		int maxc = 100;
		SystemMarketMutator.INSTANCE.changePropertyValue(market, getIntProperty(MarketPropertyNames.MAXIMUM_CONTRACTS_TRADED_PROPERTY, maxc));
		assertEquals(maxc, market.getMaximumContractsTraded());
		
		double minq = 100.0;
		SystemMarketMutator.INSTANCE.changePropertyValue(market, getDoubleProperty(MarketPropertyNames.MINIMUM_QUOTE_INCREMENT_PROPERTY, minq));
		assertEquals(minq, market.getMinimumQuoteIncrement());
		
		TradingSession ts = TradingSession.Continuous;
		SystemMarketMutator.INSTANCE.changePropertyValue(market, getObjectProperty(MarketPropertyNames.TRADING_SESSION_PROPERTY, new TradingSessionWrapper(ts)));
		assertEquals(ts, market.getTradingSession());
		
		TimePeriod tp = createTimePeriod();
		SystemMarketMutator.INSTANCE.changePropertyValue(market, getObjectProperty(MarketPropertyNames.TRADING_HOURS_PROPERTY, tp));
		assertEquals(tp, market.getTradingHours());
		
		TimeOfDay tof = createTimePeriod().getStartTime();
		SystemMarketMutator.INSTANCE.changePropertyValue(market, getObjectProperty(MarketPropertyNames.TRADING_DAY_END_PROPERTY, tof));
		assertEquals(tof, market.getTradingDayEnd());
		
		BusinessCalendar bc = createBusinessCalendar();
		SystemMarketMutator.INSTANCE.changePropertyValue(market, getObjectProperty(MarketPropertyNames.BUSINESS_CALENDAR_PROPERTY, bc));
		assertEquals(bc, market.getBusinessCalendar());
		
		ExecutionSystem ex = ExecutionSystem.CallMarketWithSingleSideAuction;
		SystemMarketMutator.INSTANCE.changePropertyValue(market, getObjectProperty(MarketPropertyNames.EXECUTION_SYSTEM_PROPERTY, new ExecutionSystemWrapper(ex)));
		assertEquals(ex, market.getExecutionSystem());
		
		CircuitBreaker cb = createCircuitBreaker();
		SystemMarketMutator.INSTANCE.changePropertyValue(market, getObjectProperty(MarketPropertyNames.CIRCUIT_BREAKER_PROPERTY, cb));
		assertEquals(cb, market.getCircuitBreaker());
		
		String clearingB = "TestBank";
		SystemMarketMutator.INSTANCE.changePropertyValue(market, getStringProperty(MarketPropertyNames.CLEARING_BANK_PROPERTY, clearingB));
		assertEquals(clearingB, market.getClearingBank());
		
		double comm = 1011.0;
		SystemMarketMutator.INSTANCE.changePropertyValue(market, getDoubleProperty(MarketPropertyNames.COMMISSION_PROPERTY, comm));
		assertEquals(comm, market.getCommission());
		
		String commc = "USD";
		SystemMarketMutator.INSTANCE.changePropertyValue(market, getStringProperty(MarketPropertyNames.COMMISSION_CURRENCY_PROPERTY, commc));
		assertEquals(commc, market.getCommissionCurrency());
		
		// test administrator properties 
		String mopc = "TestDoc";
		SystemMarketMutator.INSTANCE.changePropertyValue(market, getStringProperty(MarketPropertyNames.MARKET_OPERATOR_CONTRACT_PROPERTY, mopc));
		assertEquals(mopc, market.getMarketOperationContract());
		
		// test property lists 
		SecondaryOrderPrecedenceRuleType toBeAdded = SecondaryOrderPrecedenceRuleType.TimePrecedence;
		try {
			SystemMarketMutator.INSTANCE.addPropertyToList(market, getObjectProperty(toBeAdded.name(), new SecondaryOrderPrecedenceRuleTypeWrapper(toBeAdded)), MarketPropertyNames.SECONDARY_ORDER_PRECEDENCE_RULES_LIST);
			fail();
		} catch (DuplicatePropertyException e) {
			// expected
		}
		
		toBeAdded = SecondaryOrderPrecedenceRuleType.DisplayPrecedence;
		SystemMarketMutator.INSTANCE.addPropertyToList(market, getObjectProperty(toBeAdded.name(), new SecondaryOrderPrecedenceRuleTypeWrapper(toBeAdded)), MarketPropertyNames.SECONDARY_ORDER_PRECEDENCE_RULES_LIST);
		
		assertEquals(2, market.getSecondaryOrderPrecedenceRules().size());
		assertEquals(SecondaryOrderPrecedenceRuleType.TimePrecedence, market.getSecondaryOrderPrecedenceRules().get(0));
		assertEquals(toBeAdded, market.getSecondaryOrderPrecedenceRules().get(1));
		
		SystemMarketMutator.INSTANCE.removePropertyFromList(market, getObjectProperty(SecondaryOrderPrecedenceRuleType.TimePrecedence.name(), new SecondaryOrderPrecedenceRuleTypeWrapper(SecondaryOrderPrecedenceRuleType.TimePrecedence)), MarketPropertyNames.SECONDARY_ORDER_PRECEDENCE_RULES_LIST);
	
		assertEquals(1, market.getSecondaryOrderPrecedenceRules().size());
		assertEquals(toBeAdded, market.getSecondaryOrderPrecedenceRules().get(0));

	}
	
	private static List<SecondaryOrderPrecedenceRuleType> createDefaultSecondaryPrecedenceRuleList() {
		List<SecondaryOrderPrecedenceRuleType> list = new ArrayList<SecondaryOrderPrecedenceRuleType>();
		list.add(SecondaryOrderPrecedenceRuleType.TimePrecedence);
		
		return list;
	}
}
