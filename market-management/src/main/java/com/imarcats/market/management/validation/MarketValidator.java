package com.imarcats.market.management.validation;

import java.util.List;

import com.imarcats.interfaces.client.v100.exception.MarketRuntimeException;
import com.imarcats.interfaces.client.v100.exception.MarketSecurityException;
import com.imarcats.interfaces.client.v100.util.DataUtils;
import com.imarcats.model.Market;
import com.imarcats.model.MarketPropertyNames;
import com.imarcats.model.types.ActivationStatus;
import com.imarcats.model.types.ExecutionSystem;
import com.imarcats.model.types.SecondaryOrderPrecedenceRuleType;
import com.imarcats.model.types.TradingSession;

public class MarketValidator {
	
	private MarketValidator() { /* static utility class */ }

	/**
	 * Validates a Market being changed
	 * @param market_ Market to be Validated
	 * @throws MarketRuntimeException if Market cannot be validated
	 */
	public static void validateMarketChange(Market market_) {
		try {
			validateMarketBasics(market_);
		} catch (IllegalArgumentException e) {
			ValidatorUtils.checkForXssException(e);
		} 
	}

	/**
	 * Validates a Newly Created Market
	 * @param market_ Market to be Validated
	 * @throws MarketRuntimeException if Market cannot be validated
	 */
	public static void validateNewMarket(Market market_) {
		try {
			validateNewMarketInternal(market_);
		} catch (IllegalArgumentException e) {
			ValidatorUtils.checkForXssException(e);
		} 
	}
	
	private static void validateNewMarketInternal(Market market_) {
		validateMarketBasics(market_);
		if(market_.getActivationStatus() != null && market_.getActivationStatus() != ActivationStatus.Created) {
			throw MarketSecurityException.createExceptionWithDetails(
					MarketSecurityException.USER_TRIED_TO_SET_A_SYSTEM_CONTROLLED_PROPERTY, 
					null, new Object[] { market_, MarketPropertyNames.ACTIVATION_STATUS_PROPERTY });
		}
		if(market_.getCreationAudit() != null) {
			throw MarketSecurityException.createExceptionWithDetails(
					MarketSecurityException.USER_TRIED_TO_SET_A_SYSTEM_CONTROLLED_PROPERTY, 
					null, new Object[] { market_, MarketPropertyNames.CREATION_AUDIT_PROPERTY });
		}
		if(market_.getChangeAudit() != null) {
			throw MarketSecurityException.createExceptionWithDetails(
					MarketSecurityException.USER_TRIED_TO_SET_A_SYSTEM_CONTROLLED_PROPERTY, 
					null, new Object[] { market_, MarketPropertyNames.CHANGE_AUDIT_PROPERTY });
		}
		if(market_.getRolloverAudit() != null) {
			throw MarketSecurityException.createExceptionWithDetails(
					MarketSecurityException.USER_TRIED_TO_SET_A_SYSTEM_CONTROLLED_PROPERTY, 
					null, new Object[] { market_, MarketPropertyNames.ROLLOVER_AUDIT_PROPERTY });
		}
		if(market_.getApprovalAudit() != null) {
			throw MarketSecurityException.createExceptionWithDetails(
					MarketSecurityException.USER_TRIED_TO_SET_A_SYSTEM_CONTROLLED_PROPERTY, 
					null, new Object[] { market_, MarketPropertyNames.APPROVAL_AUDIT_PROPERTY });
		}
		if(market_.getSuspensionAudit() != null) {
			throw MarketSecurityException.createExceptionWithDetails(
					MarketSecurityException.USER_TRIED_TO_SET_A_SYSTEM_CONTROLLED_PROPERTY, 
					null, new Object[] { market_, MarketPropertyNames.SUSPENSION_AUDIT_PROPERTY });
		}
		if(market_.getActivationAudit() != null) {
			throw MarketSecurityException.createExceptionWithDetails(
					MarketSecurityException.USER_TRIED_TO_SET_A_SYSTEM_CONTROLLED_PROPERTY, 
					null, new Object[] { market_, MarketPropertyNames.ACTIVATION_AUDIT_PROPERTY });
		}
		if(market_.getDeactivationAudit() != null) {
			throw MarketSecurityException.createExceptionWithDetails(
					MarketSecurityException.USER_TRIED_TO_SET_A_SYSTEM_CONTROLLED_PROPERTY, 
					null, new Object[] { market_, MarketPropertyNames.DEACTIVATION_AUDIT_PROPERTY });
		}
		if(market_.getNextMarketCallDate() != null) {
			throw MarketSecurityException.createExceptionWithDetails(
					MarketSecurityException.USER_TRIED_TO_SET_A_SYSTEM_CONTROLLED_PROPERTY, 
					null, new Object[] { market_, MarketPropertyNames.NEXT_MARKET_CALL_DATE_PROPERTY });
		}
		if(market_.getState() != null) {
			throw MarketSecurityException.createExceptionWithDetails(
					MarketSecurityException.USER_TRIED_TO_SET_A_SYSTEM_CONTROLLED_PROPERTY, 
					null, new Object[] { market_, MarketPropertyNames.STATE_PROPERTY });
		}
		if(market_.getBuyBook() != null) {
			throw MarketSecurityException.createExceptionWithDetails(
					MarketSecurityException.USER_TRIED_TO_SET_A_SYSTEM_CONTROLLED_PROPERTY, 
					null, new Object[] { market_, MarketPropertyNames.BUY_BOOK_PROPERTY });
		}
		if(market_.getSellBook() != null) {
			throw MarketSecurityException.createExceptionWithDetails(
					MarketSecurityException.USER_TRIED_TO_SET_A_SYSTEM_CONTROLLED_PROPERTY, 
					null, new Object[] { market_, MarketPropertyNames.SELL_BOOK_PROPERTY });
		}
		if(market_.getHaltLevel() != -1) {
			throw MarketSecurityException.createExceptionWithDetails(
					MarketSecurityException.USER_TRIED_TO_SET_A_SYSTEM_CONTROLLED_PROPERTY, 
					null, new Object[] { market_, MarketPropertyNames.HALT_LEVEL_PROPERTY });
		}
		// we have removed this check to make market creation single step 
//		if(market_.getMarketOperationContract() != null) {
//			throw MarketSecurityException.createExceptionWithDetails(
//					MarketSecurityException.USER_TRIED_TO_SET_A_SYSTEM_CONTROLLED_PROPERTY, 
//					null, new Object[] { market_, MarketPropertyNames.MARKET_OPERATOR_CONTRACT_PROPERTY });
//		}
		if(market_.getCurrentBestBid() != null) {
			throw MarketSecurityException.createExceptionWithDetails(
					MarketSecurityException.USER_TRIED_TO_SET_A_SYSTEM_CONTROLLED_PROPERTY, 
					null, new Object[] { market_, MarketPropertyNames.CURRENT_BEST_BID_PROPERTY });
		}
		if(market_.getCurrentBestAsk() != null) {
			throw MarketSecurityException.createExceptionWithDetails(
					MarketSecurityException.USER_TRIED_TO_SET_A_SYSTEM_CONTROLLED_PROPERTY, 
					null, new Object[] { market_, MarketPropertyNames.CURRENT_BEST_ASK_PROPERTY });
		}
		if(market_.getPreviousBestBid() != null) {
			throw MarketSecurityException.createExceptionWithDetails(
					MarketSecurityException.USER_TRIED_TO_SET_A_SYSTEM_CONTROLLED_PROPERTY, 
					null, new Object[] { market_, MarketPropertyNames.PREVIOUS_BEST_BID_PROPERTY });
		}
		if(market_.getPreviousBestAsk() != null) {
			throw MarketSecurityException.createExceptionWithDetails(
					MarketSecurityException.USER_TRIED_TO_SET_A_SYSTEM_CONTROLLED_PROPERTY, 
					null, new Object[] { market_, MarketPropertyNames.PREVIOUS_BEST_ASK_PROPERTY });
		}
		if(market_.getLastTrade() != null) {
			throw MarketSecurityException.createExceptionWithDetails(
					MarketSecurityException.USER_TRIED_TO_SET_A_SYSTEM_CONTROLLED_PROPERTY, 
					null, new Object[] { market_, MarketPropertyNames.LAST_TRADE_PROPERTY });
		}
		if(market_.getPreviousLastTrade() != null) {
			throw MarketSecurityException.createExceptionWithDetails(
					MarketSecurityException.USER_TRIED_TO_SET_A_SYSTEM_CONTROLLED_PROPERTY, 
					null, new Object[] { market_, MarketPropertyNames.PREVIOUS_LAST_TRADE_PROPERTY });
		}
		if(market_.getOpeningQuote() != null) {
			throw MarketSecurityException.createExceptionWithDetails(
					MarketSecurityException.USER_TRIED_TO_SET_A_SYSTEM_CONTROLLED_PROPERTY, 
					null, new Object[] { market_, MarketPropertyNames.OPENING_QUOTE_PROPERTY });
		}
		if(market_.getPreviousOpeningQuote() != null) {
			throw MarketSecurityException.createExceptionWithDetails(
					MarketSecurityException.USER_TRIED_TO_SET_A_SYSTEM_CONTROLLED_PROPERTY, 
					null, new Object[] { market_, MarketPropertyNames.PREVIOUS_OPENING_QUOTE_PROPERTY });
		}
		if(market_.getClosingQuote() != null) {
			throw MarketSecurityException.createExceptionWithDetails(
					MarketSecurityException.USER_TRIED_TO_SET_A_SYSTEM_CONTROLLED_PROPERTY, 
					null, new Object[] { market_, MarketPropertyNames.CLOSING_QUOTE_PROPERTY });
		}
		if(market_.getPreviousClosingQuote() != null) {
			throw MarketSecurityException.createExceptionWithDetails(
					MarketSecurityException.USER_TRIED_TO_SET_A_SYSTEM_CONTROLLED_PROPERTY, 
					null, new Object[] { market_, MarketPropertyNames.PREVIOUS_CLOSING_QUOTE_PROPERTY });
		}
		if(market_.getMarketCallActionKey() != null) {
			throw MarketSecurityException.createExceptionWithDetails(
					MarketSecurityException.USER_TRIED_TO_SET_A_SYSTEM_CONTROLLED_PROPERTY, 
					null, new Object[] { market_, MarketPropertyNames.MARKET_CALL_ACTION_KEY_PROPERTY });
		}
		if(market_.getMarketOpenActionKey() != null) {
			throw MarketSecurityException.createExceptionWithDetails(
					MarketSecurityException.USER_TRIED_TO_SET_A_SYSTEM_CONTROLLED_PROPERTY, 
					null, new Object[] { market_, MarketPropertyNames.MARKET_OPEN_ACTION_KEY_PROPERTY });
		}
		if(market_.getMarketReOpenActionKey() != null) {
			throw MarketSecurityException.createExceptionWithDetails(
					MarketSecurityException.USER_TRIED_TO_SET_A_SYSTEM_CONTROLLED_PROPERTY, 
					null, new Object[] { market_, MarketPropertyNames.MARKET_RE_OPEN_ACTION_KEY_PROPERTY });
		}
		if(market_.getMarketCloseActionKey() != null) {
			throw MarketSecurityException.createExceptionWithDetails(
					MarketSecurityException.USER_TRIED_TO_SET_A_SYSTEM_CONTROLLED_PROPERTY, 
					null, new Object[] { market_, MarketPropertyNames.MARKET_CLOSE_ACTION_KEY_PROPERTY });
		}
		if(market_.getMarketMaintenanceActionKey() != null) {
			throw MarketSecurityException.createExceptionWithDetails(
					MarketSecurityException.USER_TRIED_TO_SET_A_SYSTEM_CONTROLLED_PROPERTY, 
					null, new Object[] { market_, MarketPropertyNames.MARKET_MAINTENANCE_ACTION_KEY_PROPERTY });
		}		
		if(market_.getCallMarketMaintenanceActionKey() != null) {
			throw MarketSecurityException.createExceptionWithDetails(
					MarketSecurityException.USER_TRIED_TO_SET_A_SYSTEM_CONTROLLED_PROPERTY, 
					null, new Object[] { market_, MarketPropertyNames.MARKET_MAINTENANCE_ACTION_KEY_PROPERTY });
		}
		if(market_.getQuoteType() != null) {
			throw MarketSecurityException.createExceptionWithDetails(
					MarketSecurityException.USER_TRIED_TO_SET_A_SYSTEM_CONTROLLED_PROPERTY, 
					null, new Object[] { market_, MarketPropertyNames.QUOTE_TYPE_PROPERTY });
		}
	}
	
	private static void validateMarketBasics(Market market_) {
		if(market_ == null) {
			throw MarketRuntimeException.createExceptionWithDetails(
					MarketRuntimeException.NULL_MARKET_CANNOT_BE_CREATED, 
					null, new Object[] {});
		}
		if(!ValidatorUtils.isValidMarketIdString(market_.getMarketCode())) {
			throw MarketRuntimeException.createExceptionWithDetails(
					MarketRuntimeException.MARKET_MUST_HAVE_CODE, 
					null, new Object[] { DataUtils.VALID_MARKET_ID });
		}
		if(!ValidatorUtils.isValidObjectIdString(market_.getInstrumentCode())) {
			throw MarketRuntimeException.createExceptionWithDetails(
					MarketRuntimeException.MARKET_MUST_HAVE_INSTRUMENT_CODE, 
					null, new Object[] { market_.getMarketCode(), DataUtils.VALID_ID });
		}
		if(!ValidatorUtils.isValidString(market_.getName())) {
			throw MarketRuntimeException.createExceptionWithDetails(
					MarketRuntimeException.MARKET_MUST_HAVE_NAME, 
					null, new Object[] { market_.getMarketCode(), DataUtils.VALID_STRINGS });
		}
		if(!ValidatorUtils.isValidString(market_.getDescription())) {
			throw MarketRuntimeException.createExceptionWithDetails(
					MarketRuntimeException.MARKET_MUST_HAVE_DESCRIPTION, 
					null, new Object[] { market_.getMarketCode(), DataUtils.VALID_STRINGS });
		}
		if(!ValidatorUtils.isValidObjectIdString(market_.getMarketOperatorCode())) {
			throw MarketRuntimeException.createExceptionWithDetails(
					MarketRuntimeException.MARKET_MUST_HAVE_MARKET_OPERATOR_CODE, 
					null, new Object[] { market_.getMarketCode(), DataUtils.VALID_ID });
		}
		if(!getRequiredMarketCode(market_).equals(market_.getMarketCode())) {
			throw MarketRuntimeException.createExceptionWithDetails(
					MarketRuntimeException.MARKET_CODE_MUST_BE_MARKET_OPERATOR_CODE_AND_INSTRUMENT_CODE, 
					null, new Object[] { market_.getMarketCode() });
		}
		if(!ValidatorUtils.isValidObjectIdString(market_.getBusinessEntityCode())) {
			throw MarketRuntimeException.createExceptionWithDetails(
					MarketRuntimeException.MARKET_MUST_HAVE_BUSINESS_ENTITY, 
					null, new Object[] { market_.getMarketCode() });
		}
		
		if(market_.getMinimumContractsTraded() <= 0) {
			throw MarketRuntimeException.createExceptionWithDetails(
					MarketRuntimeException.MARKET_MUST_HAVE_MINIMUM_CONTRACTS_TRADED, 
					null, new Object[] { market_.getMarketCode() });
		}
		if(market_.getMaximumContractsTraded() <= 0) {
			throw MarketRuntimeException.createExceptionWithDetails(
					MarketRuntimeException.MARKET_MUST_HAVE_POSITIVE_MAXIMUM_CONTRACTS_TRADED, 
					null, new Object[] { market_.getMarketCode() });
		}
		if(market_.getMinimumQuoteIncrement() <= 0) {
			throw MarketRuntimeException.createExceptionWithDetails(
					MarketRuntimeException.MARKET_MUST_HAVE_MINIMUM_QUOTE_INCREMENT, 
					null, new Object[] { market_.getMarketCode() });
		}
		if(market_.getExecutionSystem() == null) {
			throw MarketRuntimeException.createExceptionWithDetails(
					MarketRuntimeException.MARKET_MUST_HAVE_EXECUTION_SYSTEM, 
					null, new Object[] { market_.getMarketCode() });
		}
		if(market_.getTradingSession() == null) {
			throw MarketRuntimeException.createExceptionWithDetails(
					MarketRuntimeException.MARKET_MUST_HAVE_TRADING_SESSION, 
					null, new Object[] { market_.getMarketCode() });
		}
		if(market_.getExecutionSystem() == ExecutionSystem.CallMarketWithSingleSideAuction) {
			if(market_.getTradingSession() != TradingSession.NonContinuous) {
				throw MarketRuntimeException.createExceptionWithDetails(
						MarketRuntimeException.CALL_MARKET_MUST_BE_NON_CONTINUOUS, 
						null, new Object[] { market_.getMarketCode() });
			}			
		}
		if(market_.getMarketOperationDays() == null && 
		   market_.getTradingSession() != TradingSession.Continuous &&
		   market_.getExecutionSystem() != ExecutionSystem.CallMarketWithSingleSideAuction) {
			throw MarketRuntimeException.createExceptionWithDetails(
					MarketRuntimeException.MARKET_MUST_HAVE_MARKET_OPERATION_DAYS, 
					null, new Object[] { market_.getMarketCode() });
		}
		if(market_.getMarketTimeZoneID() == null) {
			throw MarketRuntimeException.createExceptionWithDetails(
					MarketRuntimeException.MARKET_MUST_HAVE_MARKET_TIMEZONE_ID, 
					null, new Object[] { market_.getMarketCode() });
		}
		if(market_.getTradingSession() == TradingSession.NonContinuous && 
		   market_.getExecutionSystem() != ExecutionSystem.CallMarketWithSingleSideAuction) {
			if(market_.getTradingHours() == null) {
				throw MarketRuntimeException.createExceptionWithDetails(
						MarketRuntimeException.NON_CONTINUOUS_MARKET_MUST_HAVE_TRADING_HOURS, 
						null, new Object[] { market_.getMarketCode() });
			}	
		}
		if(market_.getTradingHours() != null) { 
			ValidatorUtils.validateTimePeriod(market_.getTradingHours(), "Trading Hours");
		}
		if(market_.getTradingDayEnd() == null) {
			throw MarketRuntimeException.createExceptionWithDetails(
					MarketRuntimeException.MARKET_MUST_HAVE_TRADING_DAY_END, 
					null, new Object[] { market_.getMarketCode() });
		}
			
		ValidatorUtils.validateTimeOfDay(market_.getTradingDayEnd());
		
		if(market_.getBusinessCalendar() != null) { 
			ValidatorUtils.validateBusinessCalendar(market_.getBusinessCalendar());
		}
		if(market_.getSecondaryOrderPrecedenceRules() == null || market_.getSecondaryOrderPrecedenceRules().isEmpty()) {
			throw MarketRuntimeException.createExceptionWithDetails(
					MarketRuntimeException.MARKET_MUST_HAVE_SECONDARY_ORDER_PRECEDENCE_RULE, 
					null, new Object[] { market_.getMarketCode() });
		}
		if(market_.getAllowSizeRestrictionOnOrders() && 
		   market_.getSecondaryOrderPrecedenceRules().get(0) != SecondaryOrderPrecedenceRuleType.UnrestrictedOrderPrecedence) {			
			throw MarketRuntimeException.createExceptionWithDetails(
					MarketRuntimeException.MARKET_MUST_HAVE_UNRESTRICTED_ORDER_PRECEDENCE_AS_FIRST_SECONDARY_ORDER_PRECEDENCE_RULE_FOR_MARKETS_THAT_ALLOW_ORDER_SIZE_RESTRICTION, 
					null, new Object[] { market_.getMarketCode() });
		}
		validateSecondaryOrderPrecedenceRules(market_.getSecondaryOrderPrecedenceRules(), market_);
		
		// TODO: Should we explicitly disable circuit breaker for call markets (or just not use it)
		if(market_.getCircuitBreaker() != null) {
			if(market_.getExecutionSystem() == ExecutionSystem.CallMarketWithSingleSideAuction) {
				throw MarketRuntimeException.createExceptionWithDetails(
						MarketRuntimeException.CALL_MARKET_CANNOT_HAVE_CIRCUIT_BREAKER, 
						null, new Object[] { market_.getMarketCode() });
			}
			ValidatorUtils.validateCircuitBreaker(market_.getCircuitBreaker());
		}
		
		if(!ValidatorUtils.isValidString(market_.getClearingBank())) {
			throw MarketRuntimeException.createExceptionWithDetails(
					MarketRuntimeException.MARKET_MUST_HAVE_CLEARING_BANK, 
					null, new Object[] { market_.getMarketCode() });
		}
		if(market_.getCommission() <= 0) {
			throw MarketRuntimeException.createExceptionWithDetails(
					MarketRuntimeException.MARKET_MUST_HAVE_COMMISSION, 
					null, new Object[] { market_.getMarketCode() });
		}
		if(!ValidatorUtils.isValidString(market_.getCommissionCurrency())) {
			throw MarketRuntimeException.createExceptionWithDetails(
					MarketRuntimeException.MARKET_MUST_HAVE_COMMISSION_CURRENCY, 
					null, new Object[] { market_.getMarketCode(), DataUtils.VALID_STRINGS });
		}
	}	
	
	private static void validateSecondaryOrderPrecedenceRules(
			List<SecondaryOrderPrecedenceRuleType> secondaryOrderPrecedenceRules_, Market market_) {

		boolean timePrecedenceFound = false;
		for (SecondaryOrderPrecedenceRuleType typeToBeFound : secondaryOrderPrecedenceRules_) {
			int count = 0;
			if(typeToBeFound == SecondaryOrderPrecedenceRuleType.TimePrecedence) {
				timePrecedenceFound = true;
			}
			for (SecondaryOrderPrecedenceRuleType typeSearch : secondaryOrderPrecedenceRules_) {
				if(typeToBeFound.equals(typeSearch)) {
					count++;
				}
				
				if(count > 1) {
					throw MarketRuntimeException.createExceptionWithDetails(
							MarketRuntimeException.MARKET_MUST_HAVE_EACH_SECONDARY_ORDER_PRECEDENCE_RULE_ONCE, 
							null, new Object[] { typeToBeFound, market_.getMarketCode() });			
				}
			}
			
			
		}
		
		if(!timePrecedenceFound) {
			throw MarketRuntimeException.createExceptionWithDetails(
					MarketRuntimeException.MARKET_MUST_HAVE_TIME_PRECEDENCE_AS_SECONDARY_ORDER_PRECEDENCE_RULE, 
					null, new Object[] { market_.getMarketCode() });
		}
		
	}

	private static String getRequiredMarketCode(Market market_) {
		return Market.createMarketCode(market_.getMarketOperatorCode(), market_.getInstrumentCode());
	}
}
