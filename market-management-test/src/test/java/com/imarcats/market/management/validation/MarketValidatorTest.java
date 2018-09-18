package com.imarcats.market.management.validation;

import java.util.ArrayList;
import java.util.TimeZone;

import com.imarcats.interfaces.client.v100.exception.ExceptionLanguageKeys;
import com.imarcats.interfaces.client.v100.exception.MarketRuntimeException;
import com.imarcats.interfaces.client.v100.exception.MarketSecurityException;
import com.imarcats.model.CircuitBreaker;
import com.imarcats.model.HaltRule;
import com.imarcats.model.Market;
import com.imarcats.model.MarketPropertyNames;
import com.imarcats.model.test.testutils.MarketObjectTestBase;
import com.imarcats.model.types.ActivationStatus;
import com.imarcats.model.types.BusinessCalendar;
import com.imarcats.model.types.BusinessCalendarDay;
import com.imarcats.model.types.ChangeType;
import com.imarcats.model.types.Day;
import com.imarcats.model.types.ExecutionSystem;
import com.imarcats.model.types.OrderRejectAction;
import com.imarcats.model.types.RecurringActionDetail;
import com.imarcats.model.types.SecondaryOrderPrecedenceRuleType;
import com.imarcats.model.types.TimeOfDay;
import com.imarcats.model.types.TimePeriod;
import com.imarcats.model.types.TradingSession;

public class MarketValidatorTest extends MarketObjectTestBase {
	
	public void testNewMarketValidation() throws Exception {
		
		String instr = "TEST_INSTR";
		String marketOpt = "TEST_MTKT_OPT";
		String businessEntity = "TEST_BE";
		
		Market market = createMarket(Market.createMarketCode(marketOpt, instr));
		market.setExecutionSystem(ExecutionSystem.Combined);
		market.setMarketOperatorCode(marketOpt);
		market.setInstrumentCode(instr);
		market.setBusinessEntityCode(businessEntity);
		market.setAllowSizeRestrictionOnOrders(false);
		
		market.getCircuitBreaker().setMaximumQuoteImprovement(-1);

		try {
			MarketValidator.validateNewMarket(market);
			fail();
		} catch (MarketSecurityException e) {
			assertEquals(ExceptionLanguageKeys.USER_TRIED_TO_SET_A_SYSTEM_CONTROLLED_PROPERTY, e.getLanguageKey());
			assertEquals(MarketPropertyNames.ACTIVATION_STATUS_PROPERTY, e.getRelatedObjects()[1]);
		}
		
		market.setActivationStatus(ActivationStatus.Approved);
		try {
			MarketValidator.validateNewMarket(market);
			fail();
		} catch (MarketSecurityException e) {
			assertEquals(ExceptionLanguageKeys.USER_TRIED_TO_SET_A_SYSTEM_CONTROLLED_PROPERTY, e.getLanguageKey());
			assertEquals(MarketPropertyNames.ACTIVATION_STATUS_PROPERTY, e.getRelatedObjects()[1]);
		}
		market.setActivationStatus(null);
		
		try {
			MarketValidator.validateNewMarket(market);
			fail();
		} catch (MarketSecurityException e) {
			assertEquals(ExceptionLanguageKeys.USER_TRIED_TO_SET_A_SYSTEM_CONTROLLED_PROPERTY, e.getLanguageKey());
			assertEquals(MarketPropertyNames.CREATION_AUDIT_PROPERTY, e.getRelatedObjects()[1]);
		}
		market.setCreationAudit(null);
		
		try {
			MarketValidator.validateNewMarket(market);
			fail();
		} catch (MarketSecurityException e) {
			assertEquals(ExceptionLanguageKeys.USER_TRIED_TO_SET_A_SYSTEM_CONTROLLED_PROPERTY, e.getLanguageKey());
			assertEquals(MarketPropertyNames.CHANGE_AUDIT_PROPERTY, e.getRelatedObjects()[1]);
		}		
		market.setChangeAudit(null);
		
		try {
			MarketValidator.validateNewMarket(market);
			fail();
		} catch (MarketSecurityException e) {
			assertEquals(ExceptionLanguageKeys.USER_TRIED_TO_SET_A_SYSTEM_CONTROLLED_PROPERTY, e.getLanguageKey());
			assertEquals(MarketPropertyNames.ROLLOVER_AUDIT_PROPERTY, e.getRelatedObjects()[1]);
		}		
		market.setRolloverAudit(null);
		
		try {
			MarketValidator.validateNewMarket(market);
			fail();
		} catch (MarketSecurityException e) {
			assertEquals(ExceptionLanguageKeys.USER_TRIED_TO_SET_A_SYSTEM_CONTROLLED_PROPERTY, e.getLanguageKey());
			assertEquals(MarketPropertyNames.APPROVAL_AUDIT_PROPERTY, e.getRelatedObjects()[1]);
		}		
		market.setApprovalAudit(null);

		try {
			MarketValidator.validateNewMarket(market);
			fail();
		} catch (MarketSecurityException e) {
			assertEquals(ExceptionLanguageKeys.USER_TRIED_TO_SET_A_SYSTEM_CONTROLLED_PROPERTY, e.getLanguageKey());
			assertEquals(MarketPropertyNames.SUSPENSION_AUDIT_PROPERTY, e.getRelatedObjects()[1]);
		}		
		market.setSuspensionAudit(null);

		try {
			MarketValidator.validateNewMarket(market);
			fail();
		} catch (MarketSecurityException e) {
			assertEquals(ExceptionLanguageKeys.USER_TRIED_TO_SET_A_SYSTEM_CONTROLLED_PROPERTY, e.getLanguageKey());
			assertEquals(MarketPropertyNames.ACTIVATION_AUDIT_PROPERTY, e.getRelatedObjects()[1]);
		}		
		market.setActivationAudit(null);

		try {
			MarketValidator.validateNewMarket(market);
			fail();
		} catch (MarketSecurityException e) {
			assertEquals(ExceptionLanguageKeys.USER_TRIED_TO_SET_A_SYSTEM_CONTROLLED_PROPERTY, e.getLanguageKey());
			assertEquals(MarketPropertyNames.DEACTIVATION_AUDIT_PROPERTY, e.getRelatedObjects()[1]);
		}		
		market.setDeactivationAudit(null);
		try {
			MarketValidator.validateNewMarket(market);
			fail();
		} catch (MarketSecurityException e) {
			assertEquals(ExceptionLanguageKeys.USER_TRIED_TO_SET_A_SYSTEM_CONTROLLED_PROPERTY, e.getLanguageKey());
			assertEquals(MarketPropertyNames.NEXT_MARKET_CALL_DATE_PROPERTY, e.getRelatedObjects()[1]);
		}		
		market.setNextMarketCallDate(null);

		try {
			MarketValidator.validateNewMarket(market);
			fail();
		} catch (MarketSecurityException e) {
			assertEquals(ExceptionLanguageKeys.USER_TRIED_TO_SET_A_SYSTEM_CONTROLLED_PROPERTY, e.getLanguageKey());
			assertEquals(MarketPropertyNames.STATE_PROPERTY, e.getRelatedObjects()[1]);
		}		
		market.setState(null);

		try {
			MarketValidator.validateNewMarket(market);
			fail();
		} catch (MarketSecurityException e) {
			assertEquals(ExceptionLanguageKeys.USER_TRIED_TO_SET_A_SYSTEM_CONTROLLED_PROPERTY, e.getLanguageKey());
			assertEquals(MarketPropertyNames.BUY_BOOK_PROPERTY, e.getRelatedObjects()[1]);
		}		
		market.setBuyBook(null);
		
		try {
			MarketValidator.validateNewMarket(market);
			fail();
		} catch (MarketSecurityException e) {
			assertEquals(ExceptionLanguageKeys.USER_TRIED_TO_SET_A_SYSTEM_CONTROLLED_PROPERTY, e.getLanguageKey());
			assertEquals(MarketPropertyNames.SELL_BOOK_PROPERTY, e.getRelatedObjects()[1]);
		}		
		market.setSellBook(null);

		try {
			MarketValidator.validateNewMarket(market);
			fail();
		} catch (MarketSecurityException e) {
			assertEquals(ExceptionLanguageKeys.USER_TRIED_TO_SET_A_SYSTEM_CONTROLLED_PROPERTY, e.getLanguageKey());
			assertEquals(MarketPropertyNames.HALT_LEVEL_PROPERTY, e.getRelatedObjects()[1]);
		}		
		market.setHaltLevel(-1);

		// we have removed this check to make market creation single step 
//		try {
//			MarketValidator.validateNewMarket(market);
//			fail();
//		} catch (MarketSecurityException e) {
//			assertEquals(ExceptionLanguageKeys.USER_TRIED_TO_SET_A_SYSTEM_CONTROLLED_PROPERTY, e.getLanguageKey());
//			assertEquals(MarketPropertyNames.MARKET_OPERATOR_CONTRACT_PROPERTY, e.getRelatedObjects()[1]);
//		}		
//		market.setMarketOperationContract(null);
		
		try {
			MarketValidator.validateNewMarket(market);
			fail();
		} catch (MarketSecurityException e) {
			assertEquals(ExceptionLanguageKeys.USER_TRIED_TO_SET_A_SYSTEM_CONTROLLED_PROPERTY, e.getLanguageKey());
			assertEquals(MarketPropertyNames.CURRENT_BEST_BID_PROPERTY, e.getRelatedObjects()[1]);
		}		
		market.setCurrentBestBid(null);

		try {
			MarketValidator.validateNewMarket(market);
			fail();
		} catch (MarketSecurityException e) {
			assertEquals(ExceptionLanguageKeys.USER_TRIED_TO_SET_A_SYSTEM_CONTROLLED_PROPERTY, e.getLanguageKey());
			assertEquals(MarketPropertyNames.CURRENT_BEST_ASK_PROPERTY, e.getRelatedObjects()[1]);
		}		
		market.setCurrentBestAsk(null);
		
		try {
			MarketValidator.validateNewMarket(market);
			fail();
		} catch (MarketSecurityException e) {
			assertEquals(ExceptionLanguageKeys.USER_TRIED_TO_SET_A_SYSTEM_CONTROLLED_PROPERTY, e.getLanguageKey());
			assertEquals(MarketPropertyNames.PREVIOUS_BEST_BID_PROPERTY, e.getRelatedObjects()[1]);
		}		
		market.setPreviousBestBid(null);
		
		try {
			MarketValidator.validateNewMarket(market);
			fail();
		} catch (MarketSecurityException e) {
			assertEquals(ExceptionLanguageKeys.USER_TRIED_TO_SET_A_SYSTEM_CONTROLLED_PROPERTY, e.getLanguageKey());
			assertEquals(MarketPropertyNames.PREVIOUS_BEST_ASK_PROPERTY, e.getRelatedObjects()[1]);
		}		
		market.setPreviousBestAsk(null);
		
		try {
			MarketValidator.validateNewMarket(market);
			fail();
		} catch (MarketSecurityException e) {
			assertEquals(ExceptionLanguageKeys.USER_TRIED_TO_SET_A_SYSTEM_CONTROLLED_PROPERTY, e.getLanguageKey());
			assertEquals(MarketPropertyNames.LAST_TRADE_PROPERTY, e.getRelatedObjects()[1]);
		}		
		market.setLastTrade(null);
		
		try {
			MarketValidator.validateNewMarket(market);
			fail();
		} catch (MarketSecurityException e) {
			assertEquals(ExceptionLanguageKeys.USER_TRIED_TO_SET_A_SYSTEM_CONTROLLED_PROPERTY, e.getLanguageKey());
			assertEquals(MarketPropertyNames.PREVIOUS_LAST_TRADE_PROPERTY, e.getRelatedObjects()[1]);
		}		
		market.setPreviousLastTrade(null);
		
		try {
			MarketValidator.validateNewMarket(market);
			fail();
		} catch (MarketSecurityException e) {
			assertEquals(ExceptionLanguageKeys.USER_TRIED_TO_SET_A_SYSTEM_CONTROLLED_PROPERTY, e.getLanguageKey());
			assertEquals(MarketPropertyNames.OPENING_QUOTE_PROPERTY, e.getRelatedObjects()[1]);
		}		
		market.setOpeningQuote(null);

		try {
			MarketValidator.validateNewMarket(market);
			fail();
		} catch (MarketSecurityException e) {
			assertEquals(ExceptionLanguageKeys.USER_TRIED_TO_SET_A_SYSTEM_CONTROLLED_PROPERTY, e.getLanguageKey());
			assertEquals(MarketPropertyNames.PREVIOUS_OPENING_QUOTE_PROPERTY, e.getRelatedObjects()[1]);
		}		
		market.setPreviousOpeningQuote(null);
		
		try {
			MarketValidator.validateNewMarket(market);
			fail();
		} catch (MarketSecurityException e) {
			assertEquals(ExceptionLanguageKeys.USER_TRIED_TO_SET_A_SYSTEM_CONTROLLED_PROPERTY, e.getLanguageKey());
			assertEquals(MarketPropertyNames.CLOSING_QUOTE_PROPERTY, e.getRelatedObjects()[1]);
		}		
		market.setClosingQuote(null);
		
		try {
			MarketValidator.validateNewMarket(market);
			fail();
		} catch (MarketSecurityException e) {
			assertEquals(ExceptionLanguageKeys.USER_TRIED_TO_SET_A_SYSTEM_CONTROLLED_PROPERTY, e.getLanguageKey());
			assertEquals(MarketPropertyNames.PREVIOUS_CLOSING_QUOTE_PROPERTY, e.getRelatedObjects()[1]);
		}		
		market.setPreviousClosingQuote(null);
		
		try {
			MarketValidator.validateNewMarket(market);
			fail();
		} catch (MarketSecurityException e) {
			assertEquals(ExceptionLanguageKeys.USER_TRIED_TO_SET_A_SYSTEM_CONTROLLED_PROPERTY, e.getLanguageKey());
			assertEquals(MarketPropertyNames.MARKET_CALL_ACTION_KEY_PROPERTY, e.getRelatedObjects()[1]);
		}
		market.setMarketCallActionKey(null);
		
		try {
			MarketValidator.validateNewMarket(market);
			fail();
		} catch (MarketSecurityException e) {
			assertEquals(ExceptionLanguageKeys.USER_TRIED_TO_SET_A_SYSTEM_CONTROLLED_PROPERTY, e.getLanguageKey());
			assertEquals(MarketPropertyNames.MARKET_OPEN_ACTION_KEY_PROPERTY, e.getRelatedObjects()[1]);
		}
		market.setMarketOpenActionKey(null);
		
		try {
			MarketValidator.validateNewMarket(market);
			fail();
		} catch (MarketSecurityException e) {
			assertEquals(ExceptionLanguageKeys.USER_TRIED_TO_SET_A_SYSTEM_CONTROLLED_PROPERTY, e.getLanguageKey());
			assertEquals(MarketPropertyNames.MARKET_RE_OPEN_ACTION_KEY_PROPERTY, e.getRelatedObjects()[1]);
		}
		market.setMarketReOpenActionKey(null);
		
		try {
			MarketValidator.validateNewMarket(market);
			fail();
		} catch (MarketSecurityException e) {
			assertEquals(ExceptionLanguageKeys.USER_TRIED_TO_SET_A_SYSTEM_CONTROLLED_PROPERTY, e.getLanguageKey());
			assertEquals(MarketPropertyNames.MARKET_CLOSE_ACTION_KEY_PROPERTY, e.getRelatedObjects()[1]);
		}
		market.setMarketCloseActionKey(null);
		
		try {
			MarketValidator.validateNewMarket(market);
			fail();
		} catch (MarketSecurityException e) {
			assertEquals(ExceptionLanguageKeys.USER_TRIED_TO_SET_A_SYSTEM_CONTROLLED_PROPERTY, e.getLanguageKey());
			assertEquals(MarketPropertyNames.MARKET_MAINTENANCE_ACTION_KEY_PROPERTY, e.getRelatedObjects()[1]);
		}
		market.setMarketMaintenanceActionKey(null);
		
		try {
			MarketValidator.validateNewMarket(market);
			fail();
		} catch (MarketSecurityException e) {
			assertEquals(ExceptionLanguageKeys.USER_TRIED_TO_SET_A_SYSTEM_CONTROLLED_PROPERTY, e.getLanguageKey());
			assertEquals(MarketPropertyNames.MARKET_MAINTENANCE_ACTION_KEY_PROPERTY, e.getRelatedObjects()[1]);
		}
		market.setCallMarketMaintenanceActionKey(null);
		
		try {
			MarketValidator.validateNewMarket(market);
			fail();
		} catch (MarketSecurityException e) {
			assertEquals(ExceptionLanguageKeys.USER_TRIED_TO_SET_A_SYSTEM_CONTROLLED_PROPERTY, e.getLanguageKey());
			assertEquals(MarketPropertyNames.QUOTE_TYPE_PROPERTY, e.getRelatedObjects()[1]);
		}
		market.setQuoteType(null);
		
		MarketValidator.validateNewMarket(market);

		// test with created status 
		market.setActivationStatus(ActivationStatus.Created);
		MarketValidator.validateNewMarket(market);
	}
	
	public void testChangeValidation() throws Exception {
		
		try {
			MarketValidator.validateMarketChange(null);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.NULL_MARKET_CANNOT_BE_CREATED, e.getLanguageKey());
		}
		
		Market market = new Market();
		
		String instr = "TEST_INSTR";
		String marketOpt = "TEST_MKT_OPT";
		
		String code = Market.createMarketCode(marketOpt, instr);
		
		try {
			MarketValidator.validateMarketChange(market);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.MARKET_MUST_HAVE_CODE, e.getLanguageKey());
		}
		market.setMarketCode(" Test");
		try {
			MarketValidator.validateMarketChange(market);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.MARKET_MUST_HAVE_CODE, e.getLanguageKey());
		}
		market.setMarketCode("Test ");
		try {
			MarketValidator.validateMarketChange(market);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.MARKET_MUST_HAVE_CODE, e.getLanguageKey());
		}		
		market.setMarketCode("Te st");
		try {
			MarketValidator.validateMarketChange(market);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.MARKET_MUST_HAVE_CODE, e.getLanguageKey());
		}
		market.setMarketCode("TEST");
		
		try {
			MarketValidator.validateMarketChange(market);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.MARKET_MUST_HAVE_INSTRUMENT_CODE, e.getLanguageKey());
		}
		market.setInstrumentCode(instr);
	
		try {
			MarketValidator.validateMarketChange(market);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.MARKET_MUST_HAVE_NAME, e.getLanguageKey());
		}
		// invalid name 
		market.setName("TestName's");
		try {
			MarketValidator.validateMarketChange(market);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.MARKET_MUST_HAVE_NAME, e.getLanguageKey());
		}
		// XSS attempt  
		market.setName("<script>");
		try {
			MarketValidator.validateMarketChange(market);
			fail();
		} catch (MarketSecurityException e) {
			assertEquals(ExceptionLanguageKeys.USER_ATTEMPTED_CROSS_SITE_SCRIPTING, e.getLanguageKey());
		}
		market.setName(":TestNameéáóüöö1458-/_@+/,.\u4e00");
		
		try {
			MarketValidator.validateMarketChange(market);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.MARKET_MUST_HAVE_DESCRIPTION, e.getLanguageKey());
		}
		market.setDescription("TestDesc");	
		
		try {
			MarketValidator.validateMarketChange(market);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.MARKET_MUST_HAVE_MARKET_OPERATOR_CODE, e.getLanguageKey());
		}
		market.setMarketOperatorCode(marketOpt);
		
		try {
			MarketValidator.validateMarketChange(market);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.MARKET_CODE_MUST_BE_MARKET_OPERATOR_CODE_AND_INSTRUMENT_CODE, e.getLanguageKey());
		}
		market.setMarketCode(code);
		
		try {
			MarketValidator.validateMarketChange(market);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.MARKET_MUST_HAVE_BUSINESS_ENTITY, e.getLanguageKey());
		}
		market.setBusinessEntityCode("TEST_BE");
		
		try {
			MarketValidator.validateMarketChange(market);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.MARKET_MUST_HAVE_MINIMUM_CONTRACTS_TRADED, e.getLanguageKey());
		}
		market.setMinimumContractsTraded(100);
		
		market.setMaximumContractsTraded(-100);
		try {
			MarketValidator.validateMarketChange(market);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.MARKET_MUST_HAVE_POSITIVE_MAXIMUM_CONTRACTS_TRADED, e.getLanguageKey());
		}
		market.setMaximumContractsTraded(100);
		
		try {
			MarketValidator.validateMarketChange(market);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.MARKET_MUST_HAVE_MINIMUM_QUOTE_INCREMENT, e.getLanguageKey());
		}
		market.setMinimumQuoteIncrement(10);

		market.setExecutionSystem(null);
		try {
			MarketValidator.validateMarketChange(market);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.MARKET_MUST_HAVE_EXECUTION_SYSTEM, e.getLanguageKey());
		}
		market.setExecutionSystem(ExecutionSystem.CallMarketWithSingleSideAuction);
		
		market.setTradingSession(TradingSession.Continuous);
		try {
			MarketValidator.validateMarketChange(market);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.CALL_MARKET_MUST_BE_NON_CONTINUOUS, e.getLanguageKey());
		}
		market.setTradingSession(TradingSession.NonContinuous);

		market.setTradingSession(null);
		try {
			MarketValidator.validateMarketChange(market);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.MARKET_MUST_HAVE_TRADING_SESSION, e.getLanguageKey());
		}
		market.setTradingSession(TradingSession.NonContinuous);
		
		market.setExecutionSystem(ExecutionSystem.Combined);

		market.setMarketOperationDays(null);
		try {
			MarketValidator.validateMarketChange(market);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.MARKET_MUST_HAVE_MARKET_OPERATION_DAYS, e.getLanguageKey());
		}
		market.setMarketOperationDays(RecurringActionDetail.Daily);	
		
		try {
			MarketValidator.validateMarketChange(market);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.MARKET_MUST_HAVE_MARKET_TIMEZONE_ID, e.getLanguageKey());
		}
		market.setMarketTimeZoneID("TestID");
		
		try {
			MarketValidator.validateMarketChange(market);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.NON_CONTINUOUS_MARKET_MUST_HAVE_TRADING_HOURS, e.getLanguageKey());
		}
		market.setTradingHours(createAndTestTimePeriod());

		try {
			MarketValidator.validateMarketChange(market);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.MARKET_MUST_HAVE_TRADING_DAY_END, e.getLanguageKey());
		}
		market.setTradingDayEnd(new TimeOfDay());

		try {
			MarketValidator.validateMarketChange(market);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.TIME_OF_DAY_MUST_HAVE_VALID_TIME_ZONE, e.getLanguageKey());
		}
		market.getTradingDayEnd().setTimeZoneID("BLABLABLA");
		
		try {
			MarketValidator.validateMarketChange(market);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.TIME_OF_DAY_MUST_HAVE_VALID_TIME_ZONE, e.getLanguageKey());
		}
		market.getTradingDayEnd().setTimeZoneID(TimeZone.getDefault().getID());
		
		market.setBusinessCalendar(new BusinessCalendar());
		BusinessCalendarDay day = new BusinessCalendarDay();
		day.setDateString("Fake");
		day.setDay(Day.BusinessDay);
		market.getBusinessCalendar().getBusinessCalendarDays().add(new BusinessCalendarDay());
		
		try {
			MarketValidator.validateMarketChange(market);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.INVALID_BUSINESS_CALENDAR, e.getLanguageKey());
		}
		market.getBusinessCalendar().getBusinessCalendarDays().clear();

		market.setSecondaryOrderPrecedenceRules(null);
		try {
			MarketValidator.validateMarketChange(market);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.MARKET_MUST_HAVE_SECONDARY_ORDER_PRECEDENCE_RULE, e.getLanguageKey());
		}
		
		market.setSecondaryOrderPrecedenceRules(new ArrayList<SecondaryOrderPrecedenceRuleType>());
		try {
			MarketValidator.validateMarketChange(market);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.MARKET_MUST_HAVE_SECONDARY_ORDER_PRECEDENCE_RULE, e.getLanguageKey());
		}
		
		market.getSecondaryOrderPrecedenceRules().add(SecondaryOrderPrecedenceRuleType.DisplayPrecedence);
		try {
			MarketValidator.validateMarketChange(market);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.MARKET_MUST_HAVE_TIME_PRECEDENCE_AS_SECONDARY_ORDER_PRECEDENCE_RULE, e.getLanguageKey());
		}		
		market.getSecondaryOrderPrecedenceRules().clear();
		market.getSecondaryOrderPrecedenceRules().add(SecondaryOrderPrecedenceRuleType.DisplayPrecedence);
		market.getSecondaryOrderPrecedenceRules().add(SecondaryOrderPrecedenceRuleType.TimePrecedence);
		market.getSecondaryOrderPrecedenceRules().add(SecondaryOrderPrecedenceRuleType.DisplayPrecedence);

		try {
			MarketValidator.validateMarketChange(market);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.MARKET_MUST_HAVE_EACH_SECONDARY_ORDER_PRECEDENCE_RULE_ONCE, e.getLanguageKey());
		}				
		market.getSecondaryOrderPrecedenceRules().clear();
		market.getSecondaryOrderPrecedenceRules().add(SecondaryOrderPrecedenceRuleType.TimePrecedence);
		market.getSecondaryOrderPrecedenceRules().add(SecondaryOrderPrecedenceRuleType.DisplayPrecedence);
		market.getSecondaryOrderPrecedenceRules().add(SecondaryOrderPrecedenceRuleType.UnrestrictedOrderPrecedence);		
		market.setAllowSizeRestrictionOnOrders(true);
		try {
			MarketValidator.validateMarketChange(market);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.MARKET_MUST_HAVE_UNRESTRICTED_ORDER_PRECEDENCE_AS_FIRST_SECONDARY_ORDER_PRECEDENCE_RULE_FOR_MARKETS_THAT_ALLOW_ORDER_SIZE_RESTRICTION, e.getLanguageKey());
		}		
		market.getSecondaryOrderPrecedenceRules().clear();
		market.getSecondaryOrderPrecedenceRules().add(SecondaryOrderPrecedenceRuleType.UnrestrictedOrderPrecedence);		
		market.getSecondaryOrderPrecedenceRules().add(SecondaryOrderPrecedenceRuleType.DisplayPrecedence);
		market.getSecondaryOrderPrecedenceRules().add(SecondaryOrderPrecedenceRuleType.TimePrecedence);
		
		market.setCircuitBreaker(new CircuitBreaker());
		market.setExecutionSystem(ExecutionSystem.CallMarketWithSingleSideAuction);
		try {
			MarketValidator.validateMarketChange(market);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.CALL_MARKET_CANNOT_HAVE_CIRCUIT_BREAKER, e.getLanguageKey());
		}
		market.setExecutionSystem(ExecutionSystem.Combined);
		
		try {
			MarketValidator.validateMarketChange(market);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.CIRCUIT_BREAKER_MUST_HAVE_HALT_RULES_OR_MAX_QUOTE_IMPROVEMENT, e.getLanguageKey());
		}		
		
		market.getCircuitBreaker().setMaximumQuoteImprovement(10);
		market.getCircuitBreaker().setOrderRejectAction(null);	
		
		try {
			MarketValidator.validateMarketChange(market);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.CIRCUIT_BREAKER_ORDER_REJECT_ACTION_MUST_BE_DEFINED_IF_MAX_QUOTE_IMPROVEMENT_DEFINED, e.getLanguageKey());
		}
		market.getCircuitBreaker().setOrderRejectAction(OrderRejectAction.RejectAutomatically);		
		
		market.getCircuitBreaker().setMaximumQuoteImprovement(0);		
		HaltRule haltRule = new HaltRule();
		market.getCircuitBreaker().getHaltRules().add(haltRule);	
		market.getCircuitBreaker().getHaltRules().add(haltRule);	
		market.getCircuitBreaker().setMaximumQuoteImprovement(10);
		
		try {
			MarketValidator.validateMarketChange(market);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.CIRCUIT_BREAKER_HALT_RULE_MUST_HAVE_QUOTE_CHANGE_AMOUNT, e.getLanguageKey());
		}
		
		haltRule.setQuoteChangeAmount(10);
		haltRule.setChangeType(null);
		try {
			MarketValidator.validateMarketChange(market);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.CIRCUIT_BREAKER_HALT_RULE_MUST_HAVE_QUOTE_CHANGE_TYPE, e.getLanguageKey());
		}
		haltRule.setChangeType(ChangeType.Absolute);
		
		try {
			MarketValidator.validateMarketChange(market);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.MARKET_MUST_HAVE_CLEARING_BANK, e.getLanguageKey());
		}		
		market.setClearingBank("TestBank");
		
		try {
			MarketValidator.validateMarketChange(market);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.MARKET_MUST_HAVE_COMMISSION, e.getLanguageKey());
		}		
		market.setCommission(100);
		
		try {
			MarketValidator.validateMarketChange(market);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.MARKET_MUST_HAVE_COMMISSION_CURRENCY, e.getLanguageKey());
		}		
		market.setCommissionCurrency("USD");
		
		MarketValidator.validateMarketChange(market);
		
		// check call market
		market.setExecutionSystem(ExecutionSystem.CallMarketWithSingleSideAuction);
		market.setTradingDayEnd(null);
		market.setTradingHours(null);
		market.setMarketOperationDays(null);
		market.setCircuitBreaker(null);
		market.setTradingDayEnd(new TimeOfDay());
		market.getTradingDayEnd().setTimeZoneID(TimeZone.getDefault().getID());
		
		MarketValidator.validateMarketChange(market);		
		
		// check continuous market
		market.setExecutionSystem(ExecutionSystem.Combined);
		market.setTradingSession(TradingSession.Continuous);
		market.setTradingDayEnd(createTimePeriod().getEndTime());
		market.setTradingHours(null);
		market.setMarketOperationDays(null);
		
		MarketValidator.validateMarketChange(market);
	}
	
	private TimePeriod createAndTestTimePeriod() {
		TimeOfDay startTime = new TimeOfDay();
		
		try {
			startTime.setHour(-1);
			fail();
		} catch (Exception e) {
			// expected
		}
		try {
			startTime.setHour(24);
			fail();
		} catch (Exception e) {
			// expected
		}
		startTime.setHour(10);
		try {
			startTime.setMinute(-1);
			fail();
		} catch (Exception e) {
			// expected
		}
		try {
			startTime.setMinute(60);
			fail();
		} catch (Exception e) {
			// expected
		}
		startTime.setMinute(10);
		try {
			startTime.setSecond(-1);
			fail();
		} catch (Exception e) {
			// expected
		}
		try {
			startTime.setSecond(60);
			fail();
		} catch (Exception e) {
			// expected
		}
		startTime.setSecond(10);
		
		TimeOfDay endTime = TimeOfDay.create(startTime);
		endTime.setSecond(11);
		
		TimePeriod timePeriod = new TimePeriod();
		timePeriod.setStartTime(endTime);
		
		try{
			timePeriod.setEndTime(startTime);
			fail();
		} catch (Exception e) {
			// expected
		}
		endTime.setTimeZoneID(TimeZone.getDefault().getID());
		try{
			timePeriod.setEndTime(startTime);
			fail();
		} catch (Exception e) {
			// expected
		}
		startTime.setTimeZoneID(TimeZone.getDefault().getID());
		try{
			timePeriod.setEndTime(startTime);
			fail();
		} catch (Exception e) {
			// expected
		}
		timePeriod.setStartTime(startTime);
		timePeriod.setEndTime(endTime);
		
		return timePeriod;
	}
	
}
