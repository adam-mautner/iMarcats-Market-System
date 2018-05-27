package com.imarcats.interfaces.test.v100.validation;

import java.util.Date;

import junit.framework.TestCase;

import com.imarcats.interfaces.client.v100.exception.ExceptionLanguageKeys;
import com.imarcats.interfaces.client.v100.exception.MarketRuntimeException;
import com.imarcats.interfaces.client.v100.exception.MarketSecurityException;
import com.imarcats.interfaces.server.v100.validation.OrderValidator;
import com.imarcats.model.CircuitBreaker;
import com.imarcats.model.Market;
import com.imarcats.model.Order;
import com.imarcats.model.OrderPropertyNames;
import com.imarcats.model.types.ActivationStatus;
import com.imarcats.model.types.AuditInformation;
import com.imarcats.model.types.DoubleProperty;
import com.imarcats.model.types.ExecutionSystem;
import com.imarcats.model.types.MarketState;
import com.imarcats.model.types.OrderExpirationInstruction;
import com.imarcats.model.types.OrderSide;
import com.imarcats.model.types.OrderState;
import com.imarcats.model.types.OrderTriggerInstruction;
import com.imarcats.model.types.OrderType;
import com.imarcats.model.types.Quote;
import com.imarcats.model.types.QuoteAndSize;
import com.imarcats.model.types.QuoteType;
import com.imarcats.model.utils.PropertyUtils;

/**
 * Tests order validation 
 * - Order Matching and Order Management � Business Requirement Specification: Validation section 
 * @author Adam
 *
 */
public class OrderValidatorTest extends TestCase {
	
	public void testNewOrderValidation() throws Exception {
		Order order = new Order();
		Market targetMarket = new Market();
		
		// test market for order
		try {
			OrderValidator.validateNewOrder(order, targetMarket);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.ORDER_CANNOT_BE_CREATED_NON_ACTIVE_MARKET, e.getLanguageKey());
		}
		targetMarket.setActivationStatus(ActivationStatus.Activated);
		
		// test system properties being set
		order.setKey(10L);
		try {
			OrderValidator.validateNewOrder(order, targetMarket);
			fail();
		} catch (MarketSecurityException e) {
			assertEquals(ExceptionLanguageKeys.USER_TRIED_TO_SET_A_SYSTEM_CONTROLLED_PROPERTY, e.getLanguageKey());
			assertEquals(OrderPropertyNames.KEY_PROPERTY, e.getRelatedObjects()[1]);
		}
		order.setKey(null);
		
		order.setSubmitterID("Test User");
		try {
			OrderValidator.validateNewOrder(order, targetMarket);
			fail();
		} catch (MarketSecurityException e) {
			assertEquals(ExceptionLanguageKeys.USER_TRIED_TO_SET_A_SYSTEM_CONTROLLED_PROPERTY, e.getLanguageKey());
			assertEquals(OrderPropertyNames.SUBMITTER_ID_PROPERTY, e.getRelatedObjects()[1]);
		}
		order.setSubmitterID(null);
		
		order.setTargetMarketCode("TestCode");
		try {
			OrderValidator.validateNewOrder(order, targetMarket);
			fail();
		} catch (MarketSecurityException e) {
			assertEquals(ExceptionLanguageKeys.USER_TRIED_TO_SET_A_SYSTEM_CONTROLLED_PROPERTY, e.getLanguageKey());
			assertEquals(OrderPropertyNames.TARGET_MARKET_CODE_PROPERTY, e.getRelatedObjects()[1]);
		}
		order.setTargetMarketCode(null);
		
		order.setExecutedSize(100);
		try {
			OrderValidator.validateNewOrder(order, targetMarket);
			fail();
		} catch (MarketSecurityException e) {
			assertEquals(ExceptionLanguageKeys.USER_TRIED_TO_SET_A_SYSTEM_CONTROLLED_PROPERTY, e.getLanguageKey());
			assertEquals(OrderPropertyNames.EXECUTED_SIZE_PROPERTY, e.getRelatedObjects()[1]);
		}
		order.setExecutedSize(0);
		
		order.setState(OrderState.Executed);
		try {
			OrderValidator.validateNewOrder(order, targetMarket);
			fail();
		} catch (MarketSecurityException e) {
			assertEquals(ExceptionLanguageKeys.USER_TRIED_TO_SET_A_SYSTEM_CONTROLLED_PROPERTY, e.getLanguageKey());
			assertEquals(OrderPropertyNames.STATE_PROPERTY, e.getRelatedObjects()[1]);
		}
		order.setState(OrderState.Created);
		
		order.setCurrentStopQuote(new Quote());
		try {
			OrderValidator.validateNewOrder(order, targetMarket);
			fail();
		} catch (MarketSecurityException e) {
			assertEquals(ExceptionLanguageKeys.USER_TRIED_TO_SET_A_SYSTEM_CONTROLLED_PROPERTY, e.getLanguageKey());
			assertEquals(OrderPropertyNames.CURRENT_STOP_QUOTE_PROPERTY, e.getRelatedObjects()[1]);
		}
		order.setCurrentStopQuote(null);
		
		order.setSubmissionDate(new Date());
		try {
			OrderValidator.validateNewOrder(order, targetMarket);
			fail();
		} catch (MarketSecurityException e) {
			assertEquals(ExceptionLanguageKeys.USER_TRIED_TO_SET_A_SYSTEM_CONTROLLED_PROPERTY, e.getLanguageKey());
			assertEquals(OrderPropertyNames.SUBMISSION_DATE_PROPERTY, e.getRelatedObjects()[1]);
		}
		order.setSubmissionDate(null);
		
		order.setCreationAudit(new AuditInformation());
		try {
			OrderValidator.validateNewOrder(order, targetMarket);
			fail();
		} catch (MarketSecurityException e) {
			assertEquals(ExceptionLanguageKeys.USER_TRIED_TO_SET_A_SYSTEM_CONTROLLED_PROPERTY, e.getLanguageKey());
			assertEquals(OrderPropertyNames.CREATION_AUDIT_PROPERTY, e.getRelatedObjects()[1]);
		}
		order.setCreationAudit(null);
		
		order.setQuoteChangeTriggerKey((long)1123);
		try {
			OrderValidator.validateNewOrder(order, targetMarket);
			fail();
		} catch (MarketSecurityException e) {
			assertEquals(ExceptionLanguageKeys.USER_TRIED_TO_SET_A_SYSTEM_CONTROLLED_PROPERTY, e.getLanguageKey());
			assertEquals(OrderPropertyNames.QUOTE_CHANGE_TRIGGER_KEY_PROPERTY, e.getRelatedObjects()[1]);
		}
		order.setQuoteChangeTriggerKey(null);

		order.setExpirationTriggerActionKey((long)1123);
		try {
			OrderValidator.validateNewOrder(order, targetMarket);
			fail();
		} catch (MarketSecurityException e) {
			assertEquals(ExceptionLanguageKeys.USER_TRIED_TO_SET_A_SYSTEM_CONTROLLED_PROPERTY, e.getLanguageKey());
			assertEquals(OrderPropertyNames.EXPIRATION_TRIGGER_ACTION_KEY_PROPERTY, e.getRelatedObjects()[1]);
		}
		order.setExpirationTriggerActionKey(null);
		
		order.setCommissionCharged(true);
		try {
			OrderValidator.validateNewOrder(order, targetMarket);
			fail();
		} catch (MarketSecurityException e) {
			assertEquals(ExceptionLanguageKeys.USER_TRIED_TO_SET_A_SYSTEM_CONTROLLED_PROPERTY, e.getLanguageKey());
			assertEquals(OrderPropertyNames.COMMISSION_CHARGED_PROPERTY, e.getRelatedObjects()[1]);
		}
		order.setCommissionCharged(false);
		
		order.setCancellationCommentLanguageKey("Test");
		try {
			OrderValidator.validateNewOrder(order, targetMarket);
			fail();
		} catch (MarketSecurityException e) {
			assertEquals(ExceptionLanguageKeys.USER_TRIED_TO_SET_A_SYSTEM_CONTROLLED_PROPERTY, e.getLanguageKey());
			assertEquals(OrderPropertyNames.CANCELLATION_COMMENT_LANGUAGE_KEY_PROPERTY, e.getRelatedObjects()[1]);
		}
		order.setCancellationCommentLanguageKey(null);
		order.setCommissionCharged(null);
		
		Quote limitQuote = new Quote();
		limitQuote.setQuoteValue(10);
		limitQuote.setValidQuote(true);
		order.setLimitQuoteValue(limitQuote);
		
		order.setSize(10);
		order.setSide(OrderSide.Buy);
		
		OrderValidator.validateNewOrder(order, targetMarket);
		
		// check, if null order state can be validated as well
		order.setState(null);
		OrderValidator.validateNewOrder(order, targetMarket);
	}
	
	public void testOrderChangeValidation() throws Exception {
		// test system property 
		try {
			OrderValidator.validateOrderChange(PropertyUtils.createPropertyChange(OrderPropertyNames.KEY_PROPERTY, 10));
		} catch (MarketSecurityException e) {
			assertEquals(ExceptionLanguageKeys.USER_TRIED_TO_SET_A_SYSTEM_CONTROLLED_PROPERTY, e.getLanguageKey());
			assertEquals(OrderPropertyNames.KEY_PROPERTY, e.getRelatedObjects()[0]);
		}
		
		// test non-system property
		OrderValidator.validateOrderChange(PropertyUtils.createPropertyChange(OrderPropertyNames.SIZE_PROPERTY, 10));
	}
	
	public void testSubmitValidation() throws Exception { 
		Order order = new Order();
		order.setSide(OrderSide.Buy);
		
		Market targetMarket = new Market();
		
		// test market for order
		try {
			OrderValidator.validateOrderForSubmit(order, targetMarket);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.ORDER_CANNOT_BE_SUBMITTED_TO_NON_ACTIVE_MARKET, e.getLanguageKey());
		}
		try {
			OrderValidator.validateOrderOnSubmit(order, targetMarket);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.ORDER_CANNOT_BE_SUBMITTED_TO_NON_ACTIVE_MARKET, e.getLanguageKey());
		}
		targetMarket.setActivationStatus(ActivationStatus.Activated);
		
		// test market order on closed market
		order.setType(OrderType.Market);
		try {
			OrderValidator.validateOrderForSubmit(order, targetMarket);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.MARKET_ORDER_IS_NOT_SUPPORTED_WHEN_THE_MARKET_IS_NOT_OPEN, e.getLanguageKey());
		}
		try {
			OrderValidator.validateOrderOnSubmit(order, targetMarket);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.MARKET_ORDER_IS_NOT_SUPPORTED_WHEN_THE_MARKET_IS_NOT_OPEN, e.getLanguageKey());
		}
		targetMarket.setState(MarketState.Open);
		
		// test market order on call market
		targetMarket.setExecutionSystem(ExecutionSystem.CallMarketWithSingleSideAuction);
		try {
			OrderValidator.validateOrderForSubmit(order, targetMarket);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.MARKET_ORDER_IS_NOT_SUPPORTED_ON_CALL_MARKET, e.getLanguageKey());
		}
		try {
			OrderValidator.validateOrderOnSubmit(order, targetMarket);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.MARKET_ORDER_IS_NOT_SUPPORTED_ON_CALL_MARKET, e.getLanguageKey());
		}
		order.setType(OrderType.Limit);

		// test day order on call market 
		order.setExpirationInstruction(OrderExpirationInstruction.DayOrder);
		try {
			OrderValidator.validateOrderForSubmit(order, targetMarket);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.ONLY_GOOD_TILL_CANCEL_ORDER_IS_SUPPORTED_ON_CALL_MARKET, e.getLanguageKey());
		}
		try {
			OrderValidator.validateOrderOnSubmit(order, targetMarket);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.ONLY_GOOD_TILL_CANCEL_ORDER_IS_SUPPORTED_ON_CALL_MARKET, e.getLanguageKey());
		}
		order.setExpirationInstruction(OrderExpirationInstruction.GoodTillCancel);
		
		// check stop loss order on call market
		order.setTriggerInstruction(OrderTriggerInstruction.StopLoss);
		try {
			OrderValidator.validateOrderForSubmit(order, targetMarket);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.ONLY_IMMEDIATELY_TRIGGERED_ORDER_IS_SUPPORTED_ON_CALL_MARKET, e.getLanguageKey());
		}
		try {
			OrderValidator.validateOrderOnSubmit(order, targetMarket);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.ONLY_IMMEDIATELY_TRIGGERED_ORDER_IS_SUPPORTED_ON_CALL_MARKET, e.getLanguageKey());
		}
		order.setTriggerInstruction(OrderTriggerInstruction.Immediate);
		
		// test size restriction on call markets
		order.setExecuteEntireOrderAtOnce(true);
		try {
			OrderValidator.validateOrderForSubmit(order, targetMarket);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.ENTIRE_ORDER_AT_ONCE_NOT_SUPPORTED_ON_CALL_MARKET, e.getLanguageKey());
		}
		try {
			OrderValidator.validateOrderOnSubmit(order, targetMarket);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.ENTIRE_ORDER_AT_ONCE_NOT_SUPPORTED_ON_CALL_MARKET, e.getLanguageKey());
		}
		order.setExecuteEntireOrderAtOnce(false);
		
		// test size restriction on call markets 2
		order.setMinimumSizeOfExecution(1);
		try {
			OrderValidator.validateOrderForSubmit(order, targetMarket);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.MINIMUM_EXECUTION_SIZE_NOT_SUPPORTED_ON_CALL_MARKET, e.getLanguageKey());
		}
		try {
			OrderValidator.validateOrderOnSubmit(order, targetMarket);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.MINIMUM_EXECUTION_SIZE_NOT_SUPPORTED_ON_CALL_MARKET, e.getLanguageKey());
		}
		
		targetMarket.setExecutionSystem(ExecutionSystem.Combined);
		
		// test hidden orders 
		order.setDisplayOrder(false);
		try {
			OrderValidator.validateOrderForSubmit(order, targetMarket);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.HIDDEN_ORDER_NOT_SUPPORTED_ON_MARKET, e.getLanguageKey());
		}
		try {
			OrderValidator.validateOrderOnSubmit(order, targetMarket);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.HIDDEN_ORDER_NOT_SUPPORTED_ON_MARKET, e.getLanguageKey());
		}
		targetMarket.setAllowHiddenOrders(true);
		
		// test size restricted order
		targetMarket.setAllowSizeRestrictionOnOrders(false);
		order.setExecuteEntireOrderAtOnce(true);
		try {
			OrderValidator.validateOrderForSubmit(order, targetMarket);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.SIZE_RESTRICTION_ON_ORDER_NOT_SUPPORTED_ON_MARKET, e.getLanguageKey());
		}
		try {
			OrderValidator.validateOrderOnSubmit(order, targetMarket);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.SIZE_RESTRICTION_ON_ORDER_NOT_SUPPORTED_ON_MARKET, e.getLanguageKey());
		}
		order.setExecuteEntireOrderAtOnce(false);
		order.setMinimumSizeOfExecution(10);
		try {
			OrderValidator.validateOrderForSubmit(order, targetMarket);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.SIZE_RESTRICTION_ON_ORDER_NOT_SUPPORTED_ON_MARKET, e.getLanguageKey());
		}
		try {
			OrderValidator.validateOrderOnSubmit(order, targetMarket);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.SIZE_RESTRICTION_ON_ORDER_NOT_SUPPORTED_ON_MARKET, e.getLanguageKey());
		}
		targetMarket.setAllowSizeRestrictionOnOrders(true);
		
		// test order state
		
		// test submitted 
		order.setState(OrderState.Submitted);
		try {
			OrderValidator.validateOrderForSubmit(order, targetMarket);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.ORDER_ALREADY_SUBMITTED, e.getLanguageKey());
		}		
		try {
			OrderValidator.validateOrderOnSubmit(order, targetMarket);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.ORDER_ALREADY_SUBMITTED, e.getLanguageKey());
		}

		// test waiting submit 
		// waiting submit should not be accepted, when validating FOR submit 
		order.setState(OrderState.WaitingSubmit);
		try {
			OrderValidator.validateOrderForSubmit(order, targetMarket);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.ORDER_ALREADY_SUBMITTED, e.getLanguageKey());
		}
		
		// test executed 
		order.setState(OrderState.Executed);
		try {
			OrderValidator.validateOrderForSubmit(order, targetMarket);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.EXECUTED_ORDER_CANNOT_BE_RESUBMITTED, e.getLanguageKey());
		}
		try {
			OrderValidator.validateOrderOnSubmit(order, targetMarket);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.EXECUTED_ORDER_CANNOT_BE_RESUBMITTED, e.getLanguageKey());
		}
		order.setState(OrderState.Created);
		
		// test order size
		order.setSize(0);
		try {
			OrderValidator.validateOrderForSubmit(order, targetMarket);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.ORDER_SIZE_CANNOT_BE_ZERO, e.getLanguageKey());
		}
		try {
			OrderValidator.validateOrderOnSubmit(order, targetMarket);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.ORDER_SIZE_CANNOT_BE_ZERO, e.getLanguageKey());
		}
		order.setSize(10);
		
		// test quote rounding 
		double minPriceIncrement = 0.05;
		targetMarket.setMinimumQuoteIncrement(minPriceIncrement);
		order.setLimitQuoteValue(Quote.createQuote(10.151));
		try {
			OrderValidator.validateOrderForSubmit(order, targetMarket);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.ORDER_MUST_BE_SUBMITTED_WITH_A_VALID_MARKET_QUOTE, e.getLanguageKey());
		}
		try {
			OrderValidator.validateOrderOnSubmit(order, targetMarket);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.ORDER_MUST_BE_SUBMITTED_WITH_A_VALID_MARKET_QUOTE, e.getLanguageKey());
		}
		order.setLimitQuoteValue(Quote.createQuote(10.15));
		
		// test maximum quote increment
		targetMarket.setLastTrade(new QuoteAndSize());
		targetMarket.getLastTrade().setQuote(Quote.createQuote(5));
		targetMarket.setCircuitBreaker(new CircuitBreaker());
		targetMarket.getCircuitBreaker().setMaximumQuoteImprovement(5);
		try {
			OrderValidator.validateOrderForSubmit(order, targetMarket);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.ORDER_IMPROVES_QUOTE_WITH_MORE_THAN_MAXIMUM_QUOTE_IMPROVEMENT, e.getLanguageKey());
		}
		try {
			OrderValidator.validateOrderOnSubmit(order, targetMarket);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.ORDER_IMPROVES_QUOTE_WITH_MORE_THAN_MAXIMUM_QUOTE_IMPROVEMENT, e.getLanguageKey());
		}
		targetMarket.getCircuitBreaker().setMaximumQuoteImprovement(6);
		
		// test successful validations 
		OrderValidator.validateOrderForSubmit(order, targetMarket);
		
		// waiting submit is accepted by validate ON submit
		order.setState(OrderState.WaitingSubmit);
		OrderValidator.validateOrderOnSubmit(order, targetMarket); 
		
		
		targetMarket.setState(MarketState.Closed);
		order.setState(OrderState.Created);
		
		// test size restriction on closed market
		order.setExecuteEntireOrderAtOnce(true);		
		order.setMinimumSizeOfExecution(0);
		try {
			OrderValidator.validateOrderForSubmit(order, targetMarket);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.ENTIRE_ORDER_AT_ONCE_NOT_SUPPORTED_WHEN_THE_MARKET_IS_NOT_OPEN, e.getLanguageKey());
		}
		try {
			OrderValidator.validateOrderOnSubmit(order, targetMarket);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.ENTIRE_ORDER_AT_ONCE_NOT_SUPPORTED_WHEN_THE_MARKET_IS_NOT_OPEN, e.getLanguageKey());
		}
		order.setExecuteEntireOrderAtOnce(false);
		order.setMinimumSizeOfExecution(1);
		try {
			OrderValidator.validateOrderForSubmit(order, targetMarket);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.MINIMUM_EXECUTION_SIZE_NOT_SUPPORTED_WHEN_THE_MARKET_IS_NOT_OPEN, e.getLanguageKey());
		}
		try {
			OrderValidator.validateOrderOnSubmit(order, targetMarket);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.MINIMUM_EXECUTION_SIZE_NOT_SUPPORTED_WHEN_THE_MARKET_IS_NOT_OPEN, e.getLanguageKey());
		}
		
		// test successful validation 
		targetMarket.setState(MarketState.Open);
		OrderValidator.validateOrderOnSubmit(order, targetMarket);
		
		order.setExecuteEntireOrderAtOnce(true);
		order.setMinimumSizeOfExecution(0);		
		OrderValidator.validateOrderOnSubmit(order, targetMarket);
	}
	
	public void testValidateForCancel() throws Exception {
		Order order = new Order();
		Market targetMarket = new Market();
		
		order.setState(OrderState.Executed);
		try {
			OrderValidator.validateOrderForCancel(order, targetMarket);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.EXECUTED_ORDER_CANNOT_BE_CANCELED, e.getLanguageKey());
		}
		order.setState(OrderState.Canceled);
		try {
			OrderValidator.validateOrderForCancel(order, targetMarket);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.ORDER_HAS_ALREADY_BEEN_CANCELED, e.getLanguageKey());
		}
		order.setState(OrderState.Created);
		
		OrderValidator.validateOrderForCancel(order, targetMarket);
	}
	
	public void testValidateForDelete() throws Exception {
		Order order = new Order();
		Market targetMarket = new Market();
		
		// test order state
		order.setState(OrderState.Submitted);
		try {
			OrderValidator.validateOrderForDelete(order, targetMarket);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.SUBMITTED_ORDER_CANNOT_BE_DELETED_CANCEL_FIRST, e.getLanguageKey());
		}	

		order.setState(OrderState.WaitingSubmit);
		try {
			OrderValidator.validateOrderForDelete(order, targetMarket);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.SUBMITTED_ORDER_CANNOT_BE_DELETED_CANCEL_FIRST, e.getLanguageKey());
		}
		order.setState(OrderState.Created);
		
		OrderValidator.validateOrderForDelete(order, targetMarket);
	}
	
	public void testOrderGeneralValidation() throws Exception {
		Order order = new Order();
		Market targetMarket = new Market();
		targetMarket.setExecutionSystem(ExecutionSystem.CallMarketWithSingleSideAuction);
		targetMarket.setActivationStatus(ActivationStatus.Activated);
				
		order.setSide(null);
		try {
			OrderValidator.validateExistingOrder(order, targetMarket);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.ORDER_SIDE_CANNOT_BE_IDENTIFIED, e.getLanguageKey());
		}
		order.setSide(OrderSide.Buy);
		
		order.setType(null);
		try {
			OrderValidator.validateExistingOrder(order, targetMarket);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.ORDER_TYPE_CANNOT_BE_IDENTIFIED, e.getLanguageKey());
		}
		order.setType(OrderType.Limit);
		
		order.setExpirationInstruction(OrderExpirationInstruction.DayOrder);
		try {
			OrderValidator.validateExistingOrder(order, targetMarket);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.ONLY_GOOD_TILL_CANCEL_ORDER_IS_SUPPORTED_ON_CALL_MARKET, e.getLanguageKey());
		}
		order.setExpirationInstruction(OrderExpirationInstruction.GoodTillCancel);

		order.setTriggerInstruction(OrderTriggerInstruction.StopLoss);
		try {
			OrderValidator.validateExistingOrder(order, targetMarket);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.ONLY_IMMEDIATELY_TRIGGERED_ORDER_IS_SUPPORTED_ON_CALL_MARKET, e.getLanguageKey());
		}
		order.setTriggerInstruction(OrderTriggerInstruction.Immediate);
		
//		try {
//			OrderValidator.validateExistingOrder(order, targetMarket);
//			fail();
//		} catch (MarketRuntimeException e) {
//			assertEquals(ExceptionLanguageKeys.ONLY_IMMEDIATELY_TRIGGERED_ORDER_IS_SUPPORTED_ON_CALL_MARKET, e.getLanguageKey());
//		}
		
		try {
			OrderValidator.validateExistingOrder(order, targetMarket);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.LIMIT_ORDER_MUST_HAVE_A_VALID_LIMIT_QUOTE, e.getLanguageKey());
		}
		
		Quote limitQuote = new Quote();
		order.setLimitQuoteValue(limitQuote);
		try {
			OrderValidator.validateExistingOrder(order, targetMarket);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.LIMIT_ORDER_MUST_HAVE_A_VALID_LIMIT_QUOTE, e.getLanguageKey());
		}
		// test invalid quote values
		targetMarket.setQuoteType(QuoteType.Price);
		limitQuote.setQuoteValue(0);
		limitQuote.setValidQuote(true);
		// needs to be set again, since it is copied in the model
		order.setLimitQuoteValue(limitQuote);
		try {
			OrderValidator.validateExistingOrder(order, targetMarket);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.LIMIT_PRICE_MUST_BE_POSITIVE, e.getLanguageKey());
		}
		limitQuote.setQuoteValue(100);
		// needs to be set again, since it is copied in the model
		order.setLimitQuoteValue(limitQuote);
		targetMarket.setQuoteType(QuoteType.Yield);
		try {
			OrderValidator.validateExistingOrder(order, targetMarket);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.LIMIT_YIELD_MUST_BE_LESS_THAN_100, e.getLanguageKey());
		}
		targetMarket.setQuoteType(QuoteType.Price);
		limitQuote.setQuoteValue(10);
		limitQuote.setValidQuote(true);
		// needs to be set again, since it is copied in the model
		order.setLimitQuoteValue(limitQuote);		
		try {
			OrderValidator.validateExistingOrder(order, targetMarket);
			fail();
		} catch (MarketRuntimeException e) {
			// expected
			assertEquals(ExceptionLanguageKeys.ORDER_SIZE_CANNOT_BE_ZERO, e.getLanguageKey());
		}
		order.setExecutedSize(10);
		order.setSize(10);
		try {
			OrderValidator.validateExistingOrder(order, targetMarket);
			fail();
		} catch (MarketRuntimeException e) {
			// expected
			assertEquals(ExceptionLanguageKeys.ORDER_SIZE_CANNOT_BE_ZERO, e.getLanguageKey());
		}
		targetMarket.setMinimumContractsTraded(20);
		order.setExecutedSize(0);
		try {
			OrderValidator.validateExistingOrder(order, targetMarket);
			fail();
		} catch (MarketRuntimeException e) {
			// expected
			assertEquals(ExceptionLanguageKeys.ORDER_IS_TOO_SMALL_FOR_THE_MARKET, e.getLanguageKey());
		}
		targetMarket.setMinimumContractsTraded(20);
		order.setExecutedSize(10);
		order.setSize(20);
		try {
			OrderValidator.validateExistingOrder(order, targetMarket);
			fail();
		} catch (MarketRuntimeException e) {
			// expected
			assertEquals(ExceptionLanguageKeys.ORDER_IS_TOO_SMALL_FOR_THE_MARKET, e.getLanguageKey());
		}
		
		order.setSize(20);
		order.setExecutedSize(0);
		OrderValidator.validateExistingOrder(order, targetMarket);
		
		targetMarket.setMaximumContractsTraded(100);
		order.setSize(100);

		OrderValidator.validateExistingOrder(order, targetMarket);
		
		order.setSize(101);
		try {
			OrderValidator.validateExistingOrder(order, targetMarket);
			fail();
		} catch (MarketRuntimeException e) {
			// expected
			assertEquals(ExceptionLanguageKeys.ORDER_IS_TOO_BIG_FOR_THE_MARKET, e.getLanguageKey());
		}
		order.setSize(100);		

		targetMarket.setAllowSizeRestrictionOnOrders(true);
		order.setSize(20);
		order.setMinimumSizeOfExecution(25);
		targetMarket.setExecutionSystem(ExecutionSystem.ContinuousTwoSidedAuction);
		try {
			OrderValidator.validateExistingOrder(order, targetMarket);
			fail();
		} catch (MarketRuntimeException e) {
			// expected
			assertEquals(ExceptionLanguageKeys.ORDER_SIZE_IS_SMALLER_THAN_MINIMUM_SIZE_OF_EXECUTION, e.getLanguageKey());
		}
		order.setSize(25);
		order.setExecutedSize(10);
		order.setMinimumSizeOfExecution(16);
		targetMarket.setMinimumContractsTraded(0);
		try {
			OrderValidator.validateExistingOrder(order, targetMarket);
			fail();
		} catch (MarketRuntimeException e) {
			// expected
			assertEquals(ExceptionLanguageKeys.ORDER_SIZE_IS_SMALLER_THAN_MINIMUM_SIZE_OF_EXECUTION, e.getLanguageKey());
		}
		order.setExecutedSize(0);
		OrderValidator.validateExistingOrder(order, targetMarket);
		
		/*
		order.setTriggerInstruction(OrderTriggerInstruction.MarketNotHeld); 
		try {
			OrderValidator.validateExistingOrder(order, targetMarket);
			fail();
		} catch (MarketRuntimeException e) {
			// expected
			assertEquals(ExceptionLanguageKeys.UNSUPPORTED_QUOTE_CHANGE_TRIGGER, e.getLanguageKey());
		}	
		*/
		
		targetMarket.setExecutionSystem(ExecutionSystem.Combined);
		order.setTriggerInstruction(OrderTriggerInstruction.StopLoss); 
		try {
			OrderValidator.validateExistingOrder(order, targetMarket);
			fail();
		} catch (MarketRuntimeException e) {
			// expected
			assertEquals(ExceptionLanguageKeys.STOP_QUOTE_IS_NOT_DEFINED_ON_STOP_ORDER, e.getLanguageKey());
		}
		
		order.setTriggerInstruction(OrderTriggerInstruction.StopLoss); 
		DoubleProperty property = new DoubleProperty();
		property.setName("Test");
		property.setValue(10.0);
		
		order.addTriggerProperty(property);
		
		try {
			OrderValidator.validateExistingOrder(order, targetMarket);
			fail();
		} catch (MarketRuntimeException e) {
			// expected
			assertEquals(ExceptionLanguageKeys.STOP_QUOTE_IS_NOT_DEFINED_ON_STOP_ORDER, e.getLanguageKey());
		}
		
		property.setName(OrderPropertyNames.STOP_QUOTE_PROPERTY_NAME);
		property.setValue(10.0);
		OrderValidator.validateExistingOrder(order, targetMarket);
		
		property.setValue(0.0);
		try {
			OrderValidator.validateExistingOrder(order, targetMarket);
			fail();
		} catch (MarketRuntimeException e) {
			// expected
			assertEquals(ExceptionLanguageKeys.STOP_PRICE_MUST_BE_POSITIVE, e.getLanguageKey());
		}	
		
		property.setValue(1.0);		
		OrderValidator.validateExistingOrder(order, targetMarket);
		
		targetMarket.setQuoteType(QuoteType.Yield);
		property.setValue(100.0);
		
		try {
			OrderValidator.validateExistingOrder(order, targetMarket);
			fail();
		} catch (MarketRuntimeException e) {
			// expected
			assertEquals(ExceptionLanguageKeys.STOP_YIELD_MUST_BE_LESS_THAN_100, e.getLanguageKey());
		}
		
		property.setValue(99.0);		
		OrderValidator.validateExistingOrder(order, targetMarket);
		
		order.setTriggerInstruction(OrderTriggerInstruction.TrailingStopLoss);
		order.clearTriggerProperties();
		try {
			OrderValidator.validateExistingOrder(order, targetMarket);
			fail();
		} catch (MarketRuntimeException e) {
			// expected
			assertEquals(ExceptionLanguageKeys.STOP_QUOTE_DIFFERENCE_IS_NOT_DEFINED_ON_STOP_ORDER, e.getLanguageKey());
		}
		
		order.addTriggerProperty(property);
		try {
			OrderValidator.validateExistingOrder(order, targetMarket);
			fail();
		} catch (MarketRuntimeException e) {
			// expected
			assertEquals(ExceptionLanguageKeys.STOP_QUOTE_DIFFERENCE_IS_NOT_DEFINED_ON_STOP_ORDER, e.getLanguageKey());
		}		
		
		property.setName(OrderPropertyNames.STOP_QUOTE_DIFFERENCE_PROPERTY_NAME);
		OrderValidator.validateExistingOrder(order, targetMarket);
		
		property.setValue(-1.0);
		try {
			OrderValidator.validateExistingOrder(order, targetMarket);
			fail();
		} catch (MarketRuntimeException e) {
			// expected
			assertEquals(ExceptionLanguageKeys.STOP_QUOTE_DIFFERENCE_MUST_BE_POSITIVE, e.getLanguageKey());
		}	
		property.setValue(1.0);
		
		order.setExecuteEntireOrderAtOnce(true);
		try {
			OrderValidator.validateExistingOrder(order, targetMarket);
			fail();
		} catch (MarketRuntimeException e) {
			// expected
			assertEquals(ExceptionLanguageKeys.ORDER_SHOULD_HAVE_MINIMUM_SIZE_OF_EXECUTION_OR_ENTIRE_AT_ONCE_SET, e.getLanguageKey());
		}	
		
		order.setMinimumSizeOfExecution(-1);
		OrderValidator.validateExistingOrder(order, targetMarket);

		order.setMinimumSizeOfExecution(0);
		OrderValidator.validateExistingOrder(order, targetMarket);
		
		// check Market Order defaults 
		order.setType(OrderType.Market);
		order.setExecuteEntireOrderAtOnce(false);
		order.setExpirationInstruction(OrderExpirationInstruction.GoodTillCancel);
		try {
			OrderValidator.validateNewOrder(order, targetMarket);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.MARKET_ORDER_MUST_BE_IMMEDIATE_OR_CANCEL, e.getLanguageKey());
		}
		order.setExpirationInstruction(OrderExpirationInstruction.ImmediateOrCancel);
		
		OrderValidator.validateExistingOrder(order, targetMarket);
		
		order.setDisplayOrder(false);
		try {
			OrderValidator.validateNewOrder(order, targetMarket);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.HIDDEN_ORDER_NOT_SUPPORTED_ON_MARKET, e.getLanguageKey());
		}
		
		targetMarket.setAllowHiddenOrders(true);
		OrderValidator.validateExistingOrder(order, targetMarket);
		
		// test minimum size of execution 
		order.setSize(25);
		order.setExecutedSize(10);
		order.setMinimumSizeOfExecution(15);
		OrderValidator.validateExistingOrder(order, targetMarket);
		
		targetMarket.setExecutionSystem(ExecutionSystem.CallMarketWithSingleSideAuction);
		order.setType(OrderType.Market);
		try {
			OrderValidator.validateExistingOrder(order, targetMarket);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.MARKET_ORDER_IS_NOT_SUPPORTED_ON_CALL_MARKET, e.getLanguageKey());
		}
		targetMarket.setExecutionSystem(ExecutionSystem.Combined);
		order.setType(OrderType.Limit);
		OrderValidator.validateExistingOrder(order, targetMarket);
	}
}
