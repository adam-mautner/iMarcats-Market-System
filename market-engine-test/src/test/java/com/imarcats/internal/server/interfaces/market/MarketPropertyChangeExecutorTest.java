package com.imarcats.internal.server.interfaces.market;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.imarcats.interfaces.client.v100.dto.helpers.ExecutionSystemWrapperDto;
import com.imarcats.interfaces.client.v100.dto.helpers.MarketStateWrapperDto;
import com.imarcats.interfaces.client.v100.dto.helpers.QuoteTypeWrapperDto;
import com.imarcats.interfaces.client.v100.dto.helpers.RecurringActionDetailWrapperDto;
import com.imarcats.interfaces.client.v100.dto.helpers.TradingSessionWrapperDto;
import com.imarcats.interfaces.client.v100.dto.types.AuditInformationDto;
import com.imarcats.interfaces.client.v100.dto.types.BooleanPropertyDto;
import com.imarcats.interfaces.client.v100.dto.types.BusinessCalendarDto;
import com.imarcats.interfaces.client.v100.dto.types.DatePropertyDto;
import com.imarcats.interfaces.client.v100.dto.types.DoublePropertyDto;
import com.imarcats.interfaces.client.v100.dto.types.PropertyChangeDto;
import com.imarcats.interfaces.client.v100.dto.types.PropertyDto;
import com.imarcats.interfaces.client.v100.dto.types.IntPropertyDto;
import com.imarcats.interfaces.client.v100.dto.types.ObjectPropertyDto;
import com.imarcats.interfaces.client.v100.dto.types.PropertyListValueChangeDto;
import com.imarcats.interfaces.client.v100.dto.types.PropertyValueChangeDto;
import com.imarcats.interfaces.client.v100.dto.types.StringPropertyDto;
import com.imarcats.interfaces.client.v100.dto.types.TimeOfDayDto;
import com.imarcats.interfaces.client.v100.dto.types.TimePeriodDto;
import com.imarcats.interfaces.client.v100.notification.PropertyChanges;
import com.imarcats.interfaces.server.v100.dto.mapping.PropertyDtoMapping;
import com.imarcats.market.engine.testutils.MockPropertyChangeSessionImpl;
import com.imarcats.model.CircuitBreaker;
import com.imarcats.model.Market;
import com.imarcats.model.MarketPropertyNames;
import com.imarcats.model.mutators.PropertyChange;
import com.imarcats.model.mutators.SystemMarketMutator;
import com.imarcats.model.test.testutils.MarketObjectTestBase;
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


public class MarketPropertyChangeExecutorTest extends MarketObjectTestBase {

	public void testChangeSecondaryPrecedenceRules() throws Exception {
		
		Market market = new Market();		
		market.setMarketCode("TestCode");
		market.setSecondaryOrderPrecedenceRules(createDefaultSecondaryPrecedenceRuleList());
		
		MarketPropertyChangeExecutor changeExecutor = new MarketPropertyChangeExecutor(market);
		
		MockPropertyChangeSessionImpl changeSession = new MockPropertyChangeSessionImpl(null);
		
		List<SecondaryOrderPrecedenceRuleType> rules = new ArrayList<SecondaryOrderPrecedenceRuleType>();
		rules.add(SecondaryOrderPrecedenceRuleType.DisplayPrecedence);
		rules.add(SecondaryOrderPrecedenceRuleType.UnrestrictedOrderPrecedence);
		
		changeExecutor.setSecondaryOrderPrecedenceRules(rules, changeSession);
		
		PropertyChanges[] propertyChanges = changeSession.getPropertyChanges();
		
		assertEquals(1, propertyChanges.length);
		assertEquals(3, propertyChanges[0].getChanges().length);
		
		assertEquals(MarketPropertyNames.SECONDARY_ORDER_PRECEDENCE_RULES_LIST, ((PropertyListValueChangeDto) propertyChanges[0].getChanges()[0]).getPropertyListName());
		assertEquals(com.imarcats.interfaces.client.v100.dto.types.ChangeAction.Clear, ((PropertyListValueChangeDto) propertyChanges[0].getChanges()[0]).getChangeAction());
		
		assertEquals(MarketPropertyNames.SECONDARY_ORDER_PRECEDENCE_RULES_LIST, ((PropertyListValueChangeDto) propertyChanges[0].getChanges()[1]).getPropertyListName());
		assertEquals(com.imarcats.interfaces.client.v100.dto.types.ChangeAction.Add, ((PropertyListValueChangeDto) propertyChanges[0].getChanges()[1]).getChangeAction());
		assertEquals(com.imarcats.interfaces.client.v100.dto.types.SecondaryOrderPrecedenceRuleType.DisplayPrecedence.name(), ((PropertyListValueChangeDto) propertyChanges[0].getChanges()[1]).getProperty().getName());
		assertEquals(com.imarcats.interfaces.client.v100.dto.types.SecondaryOrderPrecedenceRuleType.DisplayPrecedence, ((ObjectPropertyDto) ((PropertyListValueChangeDto) propertyChanges[0].getChanges()[1]).getProperty()).getValue().getObjectValue());

		assertEquals(MarketPropertyNames.SECONDARY_ORDER_PRECEDENCE_RULES_LIST, ((PropertyListValueChangeDto) propertyChanges[0].getChanges()[2]).getPropertyListName());
		assertEquals(com.imarcats.interfaces.client.v100.dto.types.ChangeAction.Add, ((PropertyListValueChangeDto) propertyChanges[0].getChanges()[2]).getChangeAction());
		assertEquals(com.imarcats.interfaces.client.v100.dto.types.SecondaryOrderPrecedenceRuleType.UnrestrictedOrderPrecedence.name(), ((PropertyListValueChangeDto) propertyChanges[0].getChanges()[2]).getProperty().getName());
		assertEquals(com.imarcats.interfaces.client.v100.dto.types.SecondaryOrderPrecedenceRuleType.UnrestrictedOrderPrecedence, ((ObjectPropertyDto) ((PropertyListValueChangeDto) propertyChanges[0].getChanges()[2]).getProperty()).getValue().getObjectValue());

		Market marketChanged = new Market(); 
		marketChanged.setSecondaryOrderPrecedenceRules(createDefaultSecondaryPrecedenceRuleList());
		SystemMarketMutator.INSTANCE.executePropertyChanges(marketChanged, fromDto(propertyChanges[0].getChanges()), null);
		
		assertEquals(2, marketChanged.getSecondaryOrderPrecedenceRules().size());
		assertEquals(SecondaryOrderPrecedenceRuleType.DisplayPrecedence, marketChanged.getSecondaryOrderPrecedenceRules().get(0));
		assertEquals(SecondaryOrderPrecedenceRuleType.UnrestrictedOrderPrecedence, marketChanged.getSecondaryOrderPrecedenceRules().get(1));
	}
	
	public void testExecutor() throws Exception {
	
		Market market = new Market();
		market.setMarketCode("TestCode");
		
		MarketPropertyChangeExecutor changeExecutor = new MarketPropertyChangeExecutor(market);
	
		MockPropertyChangeSessionImpl changeSession = new MockPropertyChangeSessionImpl(null);
		
		AuditInformation audit = createAudit();
		audit.setUserID("1");
		changeExecutor.setCreationAudit(audit, changeSession);
		assertEqualsAudit(audit, market.getCreationAudit());
		PropertyDto property = checkSinglePropertyChange(changeSession, MarketPropertyNames.CREATION_AUDIT_PROPERTY);
		checkAudit(audit, property);
		Market marketChanged = new Market();
		SystemMarketMutator.INSTANCE.executePropertyChanges(marketChanged, fromDto(changeSession.getPropertyChanges()[0].getChanges()), null);
		assertEqualsAudit(audit, marketChanged.getCreationAudit());		
		changeSession.rollback();
		
		audit = createAudit();
		audit.setUserID("2");
		changeExecutor.setChangeAudit(audit, changeSession);
		assertEqualsAudit(audit, market.getChangeAudit());
		property = 
			checkSinglePropertyChange(changeSession, MarketPropertyNames.CHANGE_AUDIT_PROPERTY);
		checkAudit(audit, property);
		marketChanged = new Market();
		SystemMarketMutator.INSTANCE.executePropertyChanges(marketChanged, fromDto(changeSession.getPropertyChanges()[0].getChanges()), null);
		assertEqualsAudit(audit, marketChanged.getChangeAudit());		
		changeSession.rollback();
		
		audit = createAudit();
		audit.setUserID("3");
		changeExecutor.setRolloverAudit(audit, changeSession);
		assertEqualsAudit(audit, market.getRolloverAudit());
		property = 
			checkSinglePropertyChange(changeSession, MarketPropertyNames.ROLLOVER_AUDIT_PROPERTY);
		checkAudit(audit, property);
		marketChanged = new Market();
		SystemMarketMutator.INSTANCE.executePropertyChanges(marketChanged, fromDto(changeSession.getPropertyChanges()[0].getChanges()), null);
		assertEqualsAudit(audit, marketChanged.getRolloverAudit());
		changeSession.rollback();
		

		audit = createAudit();
		audit.setUserID("4");
		changeExecutor.setApprovalAudit(audit, changeSession);
		assertEqualsAudit(audit, market.getApprovalAudit());
		property = 
			checkSinglePropertyChange(changeSession, MarketPropertyNames.APPROVAL_AUDIT_PROPERTY);
		checkAudit(audit, property);
		marketChanged = new Market();
		SystemMarketMutator.INSTANCE.executePropertyChanges(marketChanged, fromDto(changeSession.getPropertyChanges()[0].getChanges()), null);
		assertEqualsAudit(audit, marketChanged.getApprovalAudit());
		changeSession.rollback();

		audit = createAudit();
		audit.setUserID("5");
		changeExecutor.setSuspensionAudit(audit, changeSession);
		assertEqualsAudit(audit, market.getSuspensionAudit());
		property = 
			checkSinglePropertyChange(changeSession, MarketPropertyNames.SUSPENSION_AUDIT_PROPERTY);
		checkAudit(audit, property);
		marketChanged = new Market();
		SystemMarketMutator.INSTANCE.executePropertyChanges(marketChanged, fromDto(changeSession.getPropertyChanges()[0].getChanges()), null);
		assertEqualsAudit(audit, marketChanged.getSuspensionAudit());
		changeSession.rollback();

		audit = createAudit();
		audit.setUserID("6");
		changeExecutor.setActivationAudit(audit, changeSession);
		assertEqualsAudit(audit, market.getActivationAudit());
		property = 
			checkSinglePropertyChange(changeSession, MarketPropertyNames.ACTIVATION_AUDIT_PROPERTY);
		checkAudit(audit, property);
		marketChanged = new Market();
		SystemMarketMutator.INSTANCE.executePropertyChanges(marketChanged, fromDto(changeSession.getPropertyChanges()[0].getChanges()), null);
		assertEqualsAudit(audit, marketChanged.getActivationAudit());
		changeSession.rollback();

		audit = createAudit();
		audit.setUserID("7");
		changeExecutor.setDeactivationAudit(audit, changeSession);
		assertEqualsAudit(audit, market.getDeactivationAudit());
		property = 
			checkSinglePropertyChange(changeSession, MarketPropertyNames.DEACTIVATION_AUDIT_PROPERTY);
		checkAudit(audit, property);
		marketChanged = new Market();
		SystemMarketMutator.INSTANCE.executePropertyChanges(marketChanged, fromDto(changeSession.getPropertyChanges()[0].getChanges()), null);
		assertEqualsAudit(audit, marketChanged.getDeactivationAudit());
		changeSession.rollback();
		
		Date next = new Date();
		changeExecutor.setNextMarketCallDate(next, changeSession);
		assertEquals(next, market.getNextMarketCallDate());
		property = 
			checkSinglePropertyChange(changeSession, MarketPropertyNames.NEXT_MARKET_CALL_DATE_PROPERTY);
		assertEquals(next, (Date)((DatePropertyDto) property).getValue());
		marketChanged = new Market();
		SystemMarketMutator.INSTANCE.executePropertyChanges(marketChanged, fromDto(changeSession.getPropertyChanges()[0].getChanges()), null);
		assertEquals(next, marketChanged.getNextMarketCallDate());		
		changeSession.rollback();
		
		MarketState state = MarketState.Called;
		changeExecutor.setState(state, changeSession);
		assertEquals(state, market.getState());
		property = 
			checkSinglePropertyChange(changeSession, MarketPropertyNames.STATE_PROPERTY);
		assertEquals(state.toString(), ((MarketStateWrapperDto)((ObjectPropertyDto) property).getValue()).getValue().toString());
		marketChanged = new Market();
		SystemMarketMutator.INSTANCE.executePropertyChanges(marketChanged, fromDto(changeSession.getPropertyChanges()[0].getChanges()), null);
		assertEquals(state, marketChanged.getState());
		changeSession.rollback();

		String name = "TestName";
		changeExecutor.setName(name, changeSession);
		assertEquals(name, market.getName());
		property = 
			checkSinglePropertyChange(changeSession, MarketPropertyNames.NAME_PROPERTY);
		assertEquals(name, ((StringPropertyDto) property).getValue());
		marketChanged = new Market();
		SystemMarketMutator.INSTANCE.executePropertyChanges(marketChanged, fromDto(changeSession.getPropertyChanges()[0].getChanges()), null);
		assertEquals(name, marketChanged.getName());
		changeSession.rollback();

		String descr = "TestDescr";
		changeExecutor.setDescription(descr, changeSession);
		assertEquals(descr, market.getDescription());
		property = 
			checkSinglePropertyChange(changeSession, MarketPropertyNames.DESCRIPTION_PROPERTY);
		assertEquals(descr, ((StringPropertyDto) property).getValue());
		marketChanged = new Market();
		SystemMarketMutator.INSTANCE.executePropertyChanges(marketChanged, fromDto(changeSession.getPropertyChanges()[0].getChanges()), null);
		assertEquals(descr, marketChanged.getDescription());
		changeSession.rollback();
		
		QuoteType quoteType = QuoteType.Yield;
		changeExecutor.setQuoteType(quoteType, changeSession);
		assertEquals(quoteType, market.getQuoteType());
		property = 
			checkSinglePropertyChange(changeSession, MarketPropertyNames.QUOTE_TYPE_PROPERTY);
		assertEquals(quoteType.toString(), ((QuoteTypeWrapperDto)((ObjectPropertyDto) property).getValue()).getValue().toString());
		marketChanged = new Market();
		SystemMarketMutator.INSTANCE.executePropertyChanges(marketChanged, fromDto(changeSession.getPropertyChanges()[0].getChanges()), null);
		assertEquals(quoteType, marketChanged.getQuoteType());
		changeSession.rollback();
		
		String businessEntCode = "TestBusinessEntity";
		changeExecutor.setBusinessEntityCode(businessEntCode, changeSession);
		assertEquals(businessEntCode, market.getBusinessEntityCode());
		property = 
			checkSinglePropertyChange(changeSession, MarketPropertyNames.BUSINESS_ENTITY_CODE_PROPERTY);
		assertEquals(businessEntCode, ((StringPropertyDto) property).getValue());
		marketChanged = new Market();
		SystemMarketMutator.INSTANCE.executePropertyChanges(marketChanged, fromDto(changeSession.getPropertyChanges()[0].getChanges()), null);
		assertEquals(businessEntCode, marketChanged.getBusinessEntityCode());
		changeSession.rollback();
		
		int minContrs = 100;
		changeExecutor.setMinimumContractsTraded(minContrs, changeSession);
		assertEquals(minContrs, market.getMinimumContractsTraded());
		property = 
			checkSinglePropertyChange(changeSession, MarketPropertyNames.MINIMUM_CONTRACTS_TRADED_PROPERTY);
		assertEquals(minContrs, ((IntPropertyDto) property).getValue());
		marketChanged = new Market();
		SystemMarketMutator.INSTANCE.executePropertyChanges(marketChanged, fromDto(changeSession.getPropertyChanges()[0].getChanges()), null);
		assertEquals(minContrs, marketChanged.getMinimumContractsTraded());
		changeSession.rollback();
		
		int maxContrs = 200;
		changeExecutor.setMaximumContractsTraded(maxContrs, changeSession);
		assertEquals(maxContrs, market.getMaximumContractsTraded());
		property = 
			checkSinglePropertyChange(changeSession, MarketPropertyNames.MAXIMUM_CONTRACTS_TRADED_PROPERTY);
		assertEquals(maxContrs, ((IntPropertyDto) property).getValue());
		marketChanged = new Market();
		SystemMarketMutator.INSTANCE.executePropertyChanges(marketChanged, fromDto(changeSession.getPropertyChanges()[0].getChanges()), null);
		assertEquals(maxContrs, marketChanged.getMaximumContractsTraded());
		changeSession.rollback();
		
		double minQuoteIncr = 10.0;
		changeExecutor.setMinimumQuoteIncrement(minQuoteIncr, changeSession);
		assertEquals(minQuoteIncr, market.getMinimumQuoteIncrement());
		property = 
			checkSinglePropertyChange(changeSession, MarketPropertyNames.MINIMUM_QUOTE_INCREMENT_PROPERTY);
		assertEquals(minQuoteIncr, ((DoublePropertyDto) property).getValue());
		marketChanged = new Market();
		SystemMarketMutator.INSTANCE.executePropertyChanges(marketChanged, fromDto(changeSession.getPropertyChanges()[0].getChanges()), null);
		assertEquals(minQuoteIncr, marketChanged.getMinimumQuoteIncrement());
		changeSession.rollback();
				
		TradingSession tradingSession = TradingSession.Continuous;
		changeExecutor.setTradingSession(tradingSession, changeSession);
		assertEquals(tradingSession, market.getTradingSession());
		property = 
			checkSinglePropertyChange(changeSession, MarketPropertyNames.TRADING_SESSION_PROPERTY);
		assertEquals(tradingSession.toString(), ((TradingSessionWrapperDto)((ObjectPropertyDto) property).getValue()).getValue().toString());
		marketChanged = new Market();
		SystemMarketMutator.INSTANCE.executePropertyChanges(marketChanged, fromDto(changeSession.getPropertyChanges()[0].getChanges()), null);
		assertEquals(tradingSession, marketChanged.getTradingSession());
		changeSession.rollback();

		RecurringActionDetail operationDays = RecurringActionDetail.OnWeekdays;
		changeExecutor.setMarketOperationDays(operationDays, changeSession);
		assertEquals(operationDays, market.getMarketOperationDays());
		property = 
			checkSinglePropertyChange(changeSession, MarketPropertyNames.MARKET_OPERATION_DAYS);
		assertEquals(operationDays.toString(), ((RecurringActionDetailWrapperDto)((ObjectPropertyDto) property).getValue()).getValue().toString());
		marketChanged = new Market();
		SystemMarketMutator.INSTANCE.executePropertyChanges(marketChanged, fromDto(changeSession.getPropertyChanges()[0].getChanges()), null);
		assertEquals(operationDays, marketChanged.getMarketOperationDays());
		changeSession.rollback();
		
		boolean allowHiddenOrders = true;
		changeExecutor.setAllowHiddenOrders(allowHiddenOrders, changeSession);
		assertEquals(allowHiddenOrders, market.getAllowHiddenOrders());
		property = 
			checkSinglePropertyChange(changeSession, MarketPropertyNames.ALLOW_HIDDEN_ORDERS);
		assertEquals(allowHiddenOrders, (boolean)((BooleanPropertyDto) property).getValue());
		marketChanged = new Market();
		SystemMarketMutator.INSTANCE.executePropertyChanges(marketChanged, fromDto(changeSession.getPropertyChanges()[0].getChanges()), null);
		assertEquals(allowHiddenOrders, marketChanged.getAllowHiddenOrders());
		changeSession.rollback();
		
		boolean allowSizeRestrictionOnOrders = true;
		changeExecutor.setAllowSizeRestrictionOnOrders(allowSizeRestrictionOnOrders, changeSession);
		assertEquals(allowSizeRestrictionOnOrders, market.getAllowSizeRestrictionOnOrders());
		property = 
			checkSinglePropertyChange(changeSession, MarketPropertyNames.ALLOW_SIZE_RESTRICTION_ON_ORDERS);
		assertEquals(allowSizeRestrictionOnOrders, (boolean)((BooleanPropertyDto) property).getValue());
		marketChanged = new Market();
		SystemMarketMutator.INSTANCE.executePropertyChanges(marketChanged, fromDto(changeSession.getPropertyChanges()[0].getChanges()), null);
		assertEquals(allowSizeRestrictionOnOrders, marketChanged.getAllowSizeRestrictionOnOrders());
		changeSession.rollback();
		
		String marketTimeZoneID = "TestTZ";
		changeExecutor.setMarketTimeZoneID(marketTimeZoneID, changeSession);
		assertEquals(marketTimeZoneID, market.getMarketTimeZoneID());
		property = 
			checkSinglePropertyChange(changeSession, MarketPropertyNames.MARKET_TIMEZONE_ID);
		assertEquals(marketTimeZoneID, (String)((StringPropertyDto) property).getValue());
		marketChanged = new Market();
		SystemMarketMutator.INSTANCE.executePropertyChanges(marketChanged, fromDto(changeSession.getPropertyChanges()[0].getChanges()), null);
		assertEquals(marketTimeZoneID, marketChanged.getMarketTimeZoneID());
		changeSession.rollback();
		
		TimePeriod tradingHours = createTimePeriod();
		changeExecutor.setTradingHours(tradingHours, changeSession);
		assertEqualsTimePeriod(tradingHours, market.getTradingHours());
		property = 
			checkSinglePropertyChange(changeSession, MarketPropertyNames.TRADING_HOURS_PROPERTY);
		assertEqualsTimePeriodDto(tradingHours, ((TimePeriodDto)((ObjectPropertyDto) property).getValue()));
		marketChanged = new Market();
		SystemMarketMutator.INSTANCE.executePropertyChanges(marketChanged, fromDto(changeSession.getPropertyChanges()[0].getChanges()), null);
		assertEqualsTimePeriod(tradingHours, marketChanged.getTradingHours());
		changeSession.rollback();

		TimeOfDay tradingDayEnd = createTimePeriod().getStartTime();
		changeExecutor.setTradingDayEnd(tradingDayEnd, changeSession);
		assertEqualsTimeOfDay(tradingDayEnd, market.getTradingDayEnd());
		property = 
			checkSinglePropertyChange(changeSession, MarketPropertyNames.TRADING_DAY_END_PROPERTY);
		assertEqualsTimeOfDayDto(tradingDayEnd, (TimeOfDayDto)((ObjectPropertyDto) property).getValue());
		marketChanged = new Market();
		SystemMarketMutator.INSTANCE.executePropertyChanges(marketChanged, fromDto(changeSession.getPropertyChanges()[0].getChanges()), null);
		assertEqualsTimeOfDay(tradingDayEnd, marketChanged.getTradingDayEnd());
		changeSession.rollback();

		BusinessCalendar businessCalendar = createBusinessCalendar();
		changeExecutor.setBusinessCalendar(businessCalendar, changeSession);
		assertEqualsBusinessCalendar(businessCalendar, market.getBusinessCalendar());
		property = 
			checkSinglePropertyChange(changeSession, MarketPropertyNames.BUSINESS_CALENDAR_PROPERTY);
		assertEqualsBusinessCalendarDto(businessCalendar, (BusinessCalendarDto)((ObjectPropertyDto) property).getValue());
		marketChanged = new Market();
		SystemMarketMutator.INSTANCE.executePropertyChanges(marketChanged, fromDto(changeSession.getPropertyChanges()[0].getChanges()), null);
		assertEqualsBusinessCalendar(businessCalendar, marketChanged.getBusinessCalendar());
		changeSession.rollback();

		ExecutionSystem exec = ExecutionSystem.CallMarketWithSingleSideAuction;
		changeExecutor.setExecutionSystem(exec, changeSession);
		assertEquals(exec, market.getExecutionSystem());
		property = 
			checkSinglePropertyChange(changeSession, MarketPropertyNames.EXECUTION_SYSTEM_PROPERTY);
		assertEquals(exec.toString(), ((ExecutionSystemWrapperDto)((ObjectPropertyDto) property).getValue()).getValue().toString());
		marketChanged = new Market();
		SystemMarketMutator.INSTANCE.executePropertyChanges(marketChanged, fromDto(changeSession.getPropertyChanges()[0].getChanges()), null);
		assertEquals(exec, marketChanged.getExecutionSystem());
		changeSession.rollback();
		
		CircuitBreaker circuitBreaker = createCircuitBreaker();
		changeExecutor.setCircuitBreaker(circuitBreaker, changeSession);
		assertEqualsCircuitBreaker(circuitBreaker, market.getCircuitBreaker());
		property = 
			checkSinglePropertyChange(changeSession, MarketPropertyNames.CIRCUIT_BREAKER_PROPERTY);
		// assertEqualsCircuitBreaker(circuitBreaker, (CircuitBreakerDto)((ObjectPropertyDto) property).getValue());
		marketChanged = new Market();
		SystemMarketMutator.INSTANCE.executePropertyChanges(marketChanged, fromDto(changeSession.getPropertyChanges()[0].getChanges()), null);
		assertEqualsCircuitBreaker(circuitBreaker, marketChanged.getCircuitBreaker());
		changeSession.rollback();
		
		String clearingBank = "TestBank";
		changeExecutor.setClearingBank(clearingBank, changeSession);
		assertEquals(clearingBank, market.getClearingBank());
		property = 
			checkSinglePropertyChange(changeSession, MarketPropertyNames.CLEARING_BANK_PROPERTY);
		assertEquals(clearingBank, (String)((StringPropertyDto) property).getValue());
		marketChanged = new Market();
		SystemMarketMutator.INSTANCE.executePropertyChanges(marketChanged, fromDto(changeSession.getPropertyChanges()[0].getChanges()), null);
		assertEquals(clearingBank, marketChanged.getClearingBank());
		changeSession.rollback();
		
		double comission = 20.0;
		changeExecutor.setCommission(comission, changeSession);
		assertEquals(comission, market.getCommission());
		property = 
			checkSinglePropertyChange(changeSession, MarketPropertyNames.COMMISSION_PROPERTY);
		assertEquals(comission, ((DoublePropertyDto) property).getValue());
		marketChanged = new Market();
		SystemMarketMutator.INSTANCE.executePropertyChanges(marketChanged, fromDto(changeSession.getPropertyChanges()[0].getChanges()), null);
		assertEquals(comission, marketChanged.getCommission());
		changeSession.rollback();
		
		String comissionCurr = "USD";
		changeExecutor.setCommissionCurrency(comissionCurr, changeSession);
		assertEquals(comissionCurr, market.getCommissionCurrency());
		property = 
			checkSinglePropertyChange(changeSession, MarketPropertyNames.COMMISSION_CURRENCY_PROPERTY);
		assertEquals(comissionCurr, ((StringPropertyDto) property).getValue());
		marketChanged = new Market();
		SystemMarketMutator.INSTANCE.executePropertyChanges(marketChanged, fromDto(changeSession.getPropertyChanges()[0].getChanges()), null);
		assertEquals(comissionCurr, marketChanged.getCommissionCurrency());
		changeSession.rollback();
		
		String contract = "TestContract";
		changeExecutor.setMarketOperationContract(contract, changeSession);
		assertEquals(contract, market.getMarketOperationContract());
		property = 
			checkSinglePropertyChange(changeSession, MarketPropertyNames.MARKET_OPERATOR_CONTRACT_PROPERTY);
		assertEquals(contract, ((StringPropertyDto) property).getValue());
		marketChanged = new Market();
		SystemMarketMutator.INSTANCE.executePropertyChanges(marketChanged, fromDto(changeSession.getPropertyChanges()[0].getChanges()), null);
		assertEquals(contract, marketChanged.getMarketOperationContract());
		changeSession.rollback();

	}

	protected void assertEqualsBusinessCalendarDto(BusinessCalendar expected, BusinessCalendarDto actual) {
		assertEquals(expected != null, actual != null);
		if(expected != null) {
			assertEquals(expected.getBusinessCalendarDays() != null, actual.getBusinessCalendarDays() != null);
			if(expected.getBusinessCalendarDays() != null) {
				assertEquals(expected.getBusinessCalendarDays().size(), actual.getBusinessCalendarDays().size());
				for (int i = 0; i < expected.getBusinessCalendarDays().size(); i++) {
					assertEquals(expected.getBusinessCalendarDays().get(i).getDateString(), actual.getBusinessCalendarDays().get(i).getDateString());
					assertEquals(expected.getBusinessCalendarDays().get(i).getDay().toString(), actual.getBusinessCalendarDays().get(i).getDay().toString());
				}
			}
		}
	}

	protected void assertEqualsTimePeriodDto(TimePeriod expected,
			TimePeriodDto actual) {
		assertEquals(expected != null, actual != null);
		if(expected != null) {
			assertEqualsTimeOfDayDto(expected.getStartTime(), actual.getStartTime());
			assertEqualsTimeOfDayDto(expected.getEndTime(), actual.getEndTime());
		}
	}

	protected void assertEqualsTimeOfDayDto(TimeOfDay expected,
			TimeOfDayDto actual) {
		assertEquals(expected != null, actual != null);
		if(expected != null) {
			assertEquals(expected.getHour(), actual.getHour());
			assertEquals(expected.getMinute(), actual.getMinute());
			assertEquals(expected.getSecond(), actual.getSecond());
			assertEquals(expected.getTimeZoneID(), actual.getTimeZoneID());
		}		
	}

	private void checkAudit(AuditInformation audit, PropertyDto property) {
		AuditInformationDto auditDto = (AuditInformationDto)((ObjectPropertyDto) property).getValue();
		assertEquals(audit.getUserID(), auditDto.getUserID());
		assertEquals(audit.getDateTime(), auditDto.getDateTime());
	}

	private PropertyChange[] fromDto(PropertyChangeDto[] change_) {
		return PropertyDtoMapping.INSTANCE.fromDto(change_);
	} 
	
	private PropertyDto checkSinglePropertyChange(
			MockPropertyChangeSessionImpl changeSession, String name) {
		assertEquals(1, changeSession.getPropertyChanges().length);
		assertEquals(1, changeSession.getPropertyChanges()[0].getChanges().length);
		PropertyDto property = ((PropertyValueChangeDto) changeSession.getPropertyChanges()[0].getChanges()[0]).getProperty();
		assertEquals(name, property.getName());
		
		return property;
	}
	
	private static List<SecondaryOrderPrecedenceRuleType> createDefaultSecondaryPrecedenceRuleList() {
		List<SecondaryOrderPrecedenceRuleType> list = new ArrayList<SecondaryOrderPrecedenceRuleType>();
		list.add(SecondaryOrderPrecedenceRuleType.TimePrecedence);
		
		return list;
	}
}
