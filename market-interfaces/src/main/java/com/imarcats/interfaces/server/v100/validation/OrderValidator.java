package com.imarcats.interfaces.server.v100.validation;

import com.imarcats.interfaces.client.v100.exception.MarketRuntimeException;
import com.imarcats.interfaces.client.v100.exception.MarketSecurityException;
import com.imarcats.interfaces.server.v100.util.QuoteRounding;
import com.imarcats.model.CircuitBreaker;
import com.imarcats.model.Market;
import com.imarcats.model.Order;
import com.imarcats.model.OrderPropertyNames;
import com.imarcats.model.mutators.PropertyChange;
import com.imarcats.model.mutators.PropertyValueChange;
import com.imarcats.model.types.ActivationStatus;
import com.imarcats.model.types.ExecutionSystem;
import com.imarcats.model.types.MarketState;
import com.imarcats.model.types.OrderExpirationInstruction;
import com.imarcats.model.types.OrderRejectAction;
import com.imarcats.model.types.OrderSide;
import com.imarcats.model.types.OrderState;
import com.imarcats.model.types.OrderTriggerInstruction;
import com.imarcats.model.types.OrderType;
import com.imarcats.model.types.QuoteType;

/**
 * Validates Order after creation or property change
 * @author Adam
 */
public class OrderValidator {

	private static final int MAX_ALLOWED_YIELD = 100;
	private static final int MIN_ALLOWED_PRICE = 0; 

	private OrderValidator() { /* static utility class */ }
	
	/**
	 * Validates newly created Order
	 * @param order_ Order to be validated
	 * @param targetMarket_ Market where the Order is submitted
	 * @throws MarketSecurityException if Order violates Market Security
	 * @throws MarketRuntimeException if Order cannot be validated
	 */
	public static void validateNewOrder(Order order_, Market targetMarket_) {
		checkMarketForOrderCreation(order_, targetMarket_); 
		validateSystemProperties(order_, targetMarket_);
		validateOrderProperties(order_, targetMarket_);
	}
	
	private static void checkMarketForOrderCreation(Order order_, Market market_) {
		if(market_.getActivationStatus() != ActivationStatus.Activated) {
			throw MarketRuntimeException.createExceptionWithDetails(MarketRuntimeException.ORDER_CANNOT_BE_CREATED_NON_ACTIVE_MARKET, 
					null, new Object[] { market_ });
		}
	}
	
	/**
	 * Validates System Properties of the Order
	 * @param order_ Order to be validated
	 * @param targetMarket_ Market where the Order is submitted
	 * @throws MarketSecurityException if Order violates Market Security
	 * @throws MarketRuntimeException if Order cannot be validated
	 */
	private static void validateSystemProperties(Order order_, Market targetMarket_) {
		if(order_.getKey() != null) {
			throw MarketSecurityException.createExceptionWithDetails(
					MarketSecurityException.USER_TRIED_TO_SET_A_SYSTEM_CONTROLLED_PROPERTY, 
					null, new Object[] { order_, OrderPropertyNames.KEY_PROPERTY });
		} 
		if(order_.getSubmitterID() != null) {
			throw MarketSecurityException.createExceptionWithDetails(
					MarketSecurityException.USER_TRIED_TO_SET_A_SYSTEM_CONTROLLED_PROPERTY, 
					null, new Object[] { order_, OrderPropertyNames.SUBMITTER_ID_PROPERTY });
		} 
		if(order_.getTargetMarketCode() != null) {
			throw MarketSecurityException.createExceptionWithDetails(
					MarketSecurityException.USER_TRIED_TO_SET_A_SYSTEM_CONTROLLED_PROPERTY, 
					null, new Object[] { order_, OrderPropertyNames.TARGET_MARKET_CODE_PROPERTY });
		}  
		if(order_.getExecutedSize() != 0) {
			throw MarketSecurityException.createExceptionWithDetails(
					MarketSecurityException.USER_TRIED_TO_SET_A_SYSTEM_CONTROLLED_PROPERTY, 
					null, new Object[] { order_, OrderPropertyNames.EXECUTED_SIZE_PROPERTY });
		}
		if(order_.getState() != null && order_.getState() != OrderState.Created) {
			throw MarketSecurityException.createExceptionWithDetails(
					MarketSecurityException.USER_TRIED_TO_SET_A_SYSTEM_CONTROLLED_PROPERTY, 
					null, new Object[] { order_, OrderPropertyNames.STATE_PROPERTY });
		}
		if(order_.getSubmissionDate() != null) {
			throw MarketSecurityException.createExceptionWithDetails(
					MarketSecurityException.USER_TRIED_TO_SET_A_SYSTEM_CONTROLLED_PROPERTY, 
					null, new Object[] { order_, OrderPropertyNames.SUBMISSION_DATE_PROPERTY });
		}
		if(order_.getCurrentStopQuote() != null) {
			throw MarketSecurityException.createExceptionWithDetails(
					MarketSecurityException.USER_TRIED_TO_SET_A_SYSTEM_CONTROLLED_PROPERTY, 
					null, new Object[] { order_, OrderPropertyNames.CURRENT_STOP_QUOTE_PROPERTY });
		}
		if(order_.getCreationAudit() != null) {
			throw MarketSecurityException.createExceptionWithDetails(
					MarketSecurityException.USER_TRIED_TO_SET_A_SYSTEM_CONTROLLED_PROPERTY, 
					null, new Object[] { order_, OrderPropertyNames.CREATION_AUDIT_PROPERTY });
		}
		if(order_.getQuoteChangeTriggerKey() != null) {
			throw MarketSecurityException.createExceptionWithDetails(
					MarketSecurityException.USER_TRIED_TO_SET_A_SYSTEM_CONTROLLED_PROPERTY, 
					null, new Object[] { order_, OrderPropertyNames.QUOTE_CHANGE_TRIGGER_KEY_PROPERTY });
		}
		if(order_.getExpirationTriggerActionKey() != null) {
			throw MarketSecurityException.createExceptionWithDetails(
					MarketSecurityException.USER_TRIED_TO_SET_A_SYSTEM_CONTROLLED_PROPERTY, 
					null, new Object[] { order_, OrderPropertyNames.EXPIRATION_TRIGGER_ACTION_KEY_PROPERTY });
		}
		if(order_.getCommissionCharged() != null && !Boolean.FALSE.equals(order_.getCommissionCharged())) {
			throw MarketSecurityException.createExceptionWithDetails(
					MarketSecurityException.USER_TRIED_TO_SET_A_SYSTEM_CONTROLLED_PROPERTY, 
					null, new Object[] { order_, OrderPropertyNames.COMMISSION_CHARGED_PROPERTY });
		}
		if(order_.getCancellationCommentLanguageKey() != null) {
			throw MarketSecurityException.createExceptionWithDetails(
					MarketSecurityException.USER_TRIED_TO_SET_A_SYSTEM_CONTROLLED_PROPERTY, 
					null, new Object[] { order_, OrderPropertyNames.CANCELLATION_COMMENT_LANGUAGE_KEY_PROPERTY });
		}
	}

	/**
	 * Validates Order Property Changes, tests if System Property has not been modified by this property change 
	 * @param propertyChanges_
	 * @param order_ 
	 */
	public static void validateOrderChange(PropertyChange[] propertyChanges_) { 
		if(propertyChanges_ == null) {
			throw MarketRuntimeException.createExceptionWithDetails(MarketRuntimeException.UNSUPPORTED_ORDER_PROPERTY_CHANGE, 
					null, new Object[] { propertyChanges_ });
		}
		for (PropertyChange propertyChange : propertyChanges_) {
			if (propertyChange instanceof PropertyValueChange) {
				PropertyValueChange valueChange = (PropertyValueChange) propertyChange;
				if(valueChange.getProperty() == null) {
					throw MarketRuntimeException.createExceptionWithDetails(MarketRuntimeException.UNSUPPORTED_ORDER_PROPERTY_VALUE, 
							null, new Object[] { valueChange });
				}
				String name = valueChange.getProperty().getName();
				if(isSystemProperty(name)) {
					throw MarketSecurityException.createExceptionWithDetails(
							MarketSecurityException.USER_TRIED_TO_SET_A_SYSTEM_CONTROLLED_PROPERTY, 
							null, new Object[] { name });
				}
			}
		}
	}
	
	private static boolean isSystemProperty(String name_) {
		boolean found = false;
		for (String propertyName : OrderPropertyNames.SYSTEM_PROPERTIES) {
			if(propertyName.equals(name_)) {
				found = true;
				break;
			}
		}
		
		return found;
	}
	
	/**
	 * Validates Order being changed
	 * @param order_ Order to be validated
	 * @param targetMarket_ Market where the Order is submitted
	 * @throws MarketSecurityException if Order violates Market Security
	 * @throws MarketRuntimeException if Order cannot be validated
	 */
	public static void validateExistingOrder(Order order_, Market targetMarket_) {
		validateOrderProperties(order_, targetMarket_);
	}
	
	/**
	 * Validates the User Properties of the Order
	 * @param order_ Order to be validated 
	 * @param targetMarket_ Market where the Order will be Submitted
	 * @throws MarketRuntimeException if Order cannot be validated
	 */
	private static void validateOrderProperties(Order order_, Market targetMarket_) {
		validateOrderBasics(order_, targetMarket_);
		checkOrderRestrictions(order_, targetMarket_);
		validateOrderQuote(order_, targetMarket_);
		validateOrderSize(order_, targetMarket_);
		QuoteChangeTriggerValidatorRegistry.getValidator(order_.getTriggerInstruction()).validate(order_, targetMarket_);
		ExpirationInstructionValidatorRegistry.getValidator(order_.getExpirationInstruction()).validate(order_, targetMarket_);		
	}
	
	/**
	 * Validates the Basic Properties of the Order
	 * @param order_ Order to be validated 
	 * @param targetMarket_ Market where the Order will be Submitted
	 * @throws MarketRuntimeException if Order cannot be validated
	 */
	private static void validateOrderBasics(Order order_, Market targetMarket_) {
		if(order_.getSide() != OrderSide.Buy && order_.getSide() != OrderSide.Sell) {
			throw MarketRuntimeException.createExceptionWithDetails(
					MarketRuntimeException.ORDER_SIDE_CANNOT_BE_IDENTIFIED, 
					null, new Object[] { order_.getSide(), order_, targetMarket_ });
		}
		if(order_.getType() != OrderType.Limit && order_.getType() != OrderType.Market) {
			throw MarketRuntimeException.createExceptionWithDetails(
					MarketRuntimeException.ORDER_TYPE_CANNOT_BE_IDENTIFIED, 
					null, new Object[] { order_.getType(), order_, targetMarket_ });
		}
	}

	private static void checkOrderRestrictions(Order order_,
			Market targetMarket_) {
		if(order_.getType() == OrderType.Market) {
			if(targetMarket_.getExecutionSystem() == ExecutionSystem.CallMarketWithSingleSideAuction) {
				throw MarketRuntimeException.createExceptionWithDetails(
						MarketRuntimeException.MARKET_ORDER_IS_NOT_SUPPORTED_ON_CALL_MARKET, null, 
						new Object[]{ order_, targetMarket_ });	
			}
		}
		if(targetMarket_.getExecutionSystem() == ExecutionSystem.CallMarketWithSingleSideAuction) {
			enforceOrderSizeRestrictions(order_, targetMarket_, true);
		} 
		if(targetMarket_.getExecutionSystem() == ExecutionSystem.CallMarketWithSingleSideAuction &&
		   order_.getExpirationInstruction() != OrderExpirationInstruction.GoodTillCancel) {
			throw MarketRuntimeException.createExceptionWithDetails(
					MarketRuntimeException.ONLY_GOOD_TILL_CANCEL_ORDER_IS_SUPPORTED_ON_CALL_MARKET, 
					null, new Object[] { order_, targetMarket_ });	
		}
		if(targetMarket_.getExecutionSystem() == ExecutionSystem.CallMarketWithSingleSideAuction &&
		   order_.getTriggerInstruction() != OrderTriggerInstruction.Immediate) {
			throw MarketRuntimeException.createExceptionWithDetails(
					MarketRuntimeException.ONLY_IMMEDIATELY_TRIGGERED_ORDER_IS_SUPPORTED_ON_CALL_MARKET, 
					null, new Object[] { order_, targetMarket_ });
		}
		if(order_.getType() == OrderType.Market && 
		   (order_.getExpirationInstruction() != OrderExpirationInstruction.ImmediateOrCancel)) {
			throw MarketRuntimeException.createExceptionWithDetails(
					MarketRuntimeException.MARKET_ORDER_MUST_BE_IMMEDIATE_OR_CANCEL, 
					null, new Object[] { order_ });
		}
		if(!order_.getDisplayOrder() && !targetMarket_.getAllowHiddenOrders()) {
			throw MarketRuntimeException.createExceptionWithDetails(
					MarketRuntimeException.HIDDEN_ORDER_NOT_SUPPORTED_ON_MARKET, 
					null, new Object[] { order_, targetMarket_ });
		}
		if((order_.getMinimumSizeOfExecution() > 0 || 
		   order_.getExecuteEntireOrderAtOnce()) && !targetMarket_.getAllowSizeRestrictionOnOrders()) {
			throw MarketRuntimeException.createExceptionWithDetails(
					MarketRuntimeException.SIZE_RESTRICTION_ON_ORDER_NOT_SUPPORTED_ON_MARKET, 
					null, new Object[] { order_, targetMarket_ });			
		}
	}
	
	/**
	 * Validates the Quote of the Order 
	 * @param order_ Order to be validated 
	 * @param targetMarket_ Market where the Order will be Submitted
	 * @throws MarketRuntimeException if Order cannot be validated
	 */
	private static void validateOrderQuote(Order order_, Market targetMarket_) {
		if(order_.getType() == OrderType.Limit) {
		   if(order_.getLimitQuoteValue() == null ||
		      !order_.getLimitQuoteValue().getValidQuote()) {
				throw MarketRuntimeException.createExceptionWithDetails(
						MarketRuntimeException.LIMIT_ORDER_MUST_HAVE_A_VALID_LIMIT_QUOTE, 
						null, new Object[] { order_, targetMarket_ });
		   } 
		   
		   if(targetMarket_.getQuoteType() == QuoteType.Price && order_.getLimitQuoteValue().getQuoteValue() <= MIN_ALLOWED_PRICE) {
				throw MarketRuntimeException.createExceptionWithDetails(
						MarketRuntimeException.LIMIT_PRICE_MUST_BE_POSITIVE, 
						null, new Object[] { order_, targetMarket_ });
		   }
	   
		   if(targetMarket_.getQuoteType() == QuoteType.Yield && order_.getLimitQuoteValue().getQuoteValue() >= MAX_ALLOWED_YIELD) {
				throw MarketRuntimeException.createExceptionWithDetails(
						MarketRuntimeException.LIMIT_YIELD_MUST_BE_LESS_THAN_100, 
						null, new Object[] { order_, targetMarket_ });
		   }
		}
	}
	
	/**
	 * Validates the Size of the Order 
	 * @param order_ Order to be validated 
	 * @param targetMarket_ Market where the Order will be Submitted
	 * @throws MarketRuntimeException if Order cannot be validated
	 */
	private static void validateOrderSize(Order order_, Market targetMarket_) {
		// TODO: Consider contract size
		if(order_.calculateRemainingSize() <= 0) {
			throw MarketRuntimeException.createExceptionWithDetails(
					MarketRuntimeException.ORDER_SIZE_CANNOT_BE_ZERO, null, 
					new Object[]{ order_, targetMarket_ });
		}
		if(order_.calculateRemainingSize() > targetMarket_.getMaximumContractsTraded()) {
			throw MarketRuntimeException.createExceptionWithDetails(
					MarketRuntimeException.ORDER_IS_TOO_BIG_FOR_THE_MARKET, null, 
					new Object[]{ order_, targetMarket_ });
		}
		if(order_.calculateRemainingSize() < targetMarket_.getMinimumContractsTraded()) {
			throw MarketRuntimeException.createExceptionWithDetails(
					MarketRuntimeException.ORDER_IS_TOO_SMALL_FOR_THE_MARKET, null, 
					new Object[]{ order_, targetMarket_ });
		}
		if(order_.calculateRemainingSize() < order_.getMinimumSizeOfExecution()) {
			throw MarketRuntimeException.createExceptionWithDetails(
					MarketRuntimeException.ORDER_SIZE_IS_SMALLER_THAN_MINIMUM_SIZE_OF_EXECUTION, null, 
					new Object[]{ order_, targetMarket_ });
		}
		if(order_.getMinimumSizeOfExecution() > 0 &&
		   order_.getExecuteEntireOrderAtOnce()) {
			throw MarketRuntimeException.createExceptionWithDetails(
					MarketRuntimeException.ORDER_SHOULD_HAVE_MINIMUM_SIZE_OF_EXECUTION_OR_ENTIRE_AT_ONCE_SET, null, 
					new Object[]{ order_, targetMarket_ });
		}
	}
	
	
	/**
	 * Checks the Order, before it is submitted in Order Management System 
	 * @param order_ Order to be submitted
	 * @param market_ Market, where the Order will be submitted
	 * @throws MarketRuntimeException, if the Order is not suitable for Submit
	 */
	public static void validateOrderForSubmit(Order order_, Market market_) {
		checkOrderStateForSubmit(order_, market_);		
		validateOrderForCommonSubmit(order_, market_);
	}

	private static void validateOrderForCommonSubmit(Order order_,
			Market market_) {
		checkMarketForSubmit(order_, market_);
		checkOrderRestrictions(order_, market_);
		validateOrderSize(order_, market_);		
		checkAndRoundOrderLimitQuote(order_, market_);
		checkLimitOrderQuoteWithMaximumQuoteImprovement(order_, market_);
	}
	
	/**
	 * Compares the difference of the Last Quote (if exists) and Quote of Limit Order to 
	 * the Maximum Price Improvement value of the Market, rejects if it is over 
	 * the Maximum Price Improvement
	 * 
	 * @param order_ Order 
	 * @param market_ Traget Market
	 */
	private static void checkLimitOrderQuoteWithMaximumQuoteImprovement(Order order_,
			Market market_) {
		if(order_.getType() == OrderType.Limit) {
			if(market_.getLastTrade() != null && 
			   market_.getLastTrade().getQuote().getValidQuote()) {
				CircuitBreaker circuitBreaker = market_.getCircuitBreaker();
				
				if(circuitBreaker != null && 
				   circuitBreaker.getMaximumQuoteImprovement() > 0) {
					
					double marketMove = 
						Math.abs(market_.getLastTrade().getQuote().getQuoteValue() - order_.getLimitQuoteValue().getQuoteValue());
					if(marketMove > circuitBreaker.getMaximumQuoteImprovement()) {
						
						if(circuitBreaker.getOrderRejectAction() == OrderRejectAction.RejectAutomatically) {
							throw MarketRuntimeException.createExceptionWithDetails(
									MarketRuntimeException.ORDER_IMPROVES_QUOTE_WITH_MORE_THAN_MAXIMUM_QUOTE_IMPROVEMENT, null, 
									new Object[]{ order_, market_ });		
						}
					}					
				}
			}
		}
	}
	
	/**
	 * Checks the Order, when it is submitted in Order Matching System
	 * @param order_ Order to be submitted
	 * @param market_ Market, where the Order will be submitted
	 * @throws MarketRuntimeException, if the Order is not suitable for Submit
	 */
	public static void validateOrderOnSubmit(Order order_, Market market_) {
		// this will accept submit on waiting submit order
		checkOrderStateOnSubmit(order_, market_);
		validateOrderForCommonSubmit(order_, market_);
	}
	
	private static void checkMarketForSubmit(Order order_, Market market_) {
		if(market_.getActivationStatus() != ActivationStatus.Activated) {
			throw MarketRuntimeException.createExceptionWithDetails(
					MarketRuntimeException.ORDER_CANNOT_BE_SUBMITTED_TO_NON_ACTIVE_MARKET, null, 
					new Object[]{ order_, market_ });	
		}
		// this check placed here, because market may not be open, when the order is created, 
		// but open, when submitted 
		if(order_.getType() == OrderType.Market) {
			if(market_.getState() != MarketState.Open) {
				throw MarketRuntimeException.createExceptionWithDetails(
						MarketRuntimeException.MARKET_ORDER_IS_NOT_SUPPORTED_WHEN_THE_MARKET_IS_NOT_OPEN, null, 
						new Object[]{ order_, market_ });	
			}
		}
		if(market_.getState() != MarketState.Open) {
			enforceOrderSizeRestrictions(order_, market_, false);
		}
	}
	
	private static void enforceOrderSizeRestrictions(Order order_, Market market_, boolean callMarket_) {
		if(order_.getMinimumSizeOfExecution() > 0) {
			throw MarketRuntimeException.createExceptionWithDetails(
					callMarket_ 
						? MarketRuntimeException.MINIMUM_EXECUTION_SIZE_NOT_SUPPORTED_ON_CALL_MARKET
						: MarketRuntimeException.MINIMUM_EXECUTION_SIZE_NOT_SUPPORTED_WHEN_THE_MARKET_IS_NOT_OPEN, null, 
					new Object[]{ order_, market_ });
			
		}
		if(order_.getExecuteEntireOrderAtOnce()) {
			throw MarketRuntimeException.createExceptionWithDetails(
					callMarket_
						? MarketRuntimeException.ENTIRE_ORDER_AT_ONCE_NOT_SUPPORTED_ON_CALL_MARKET
						: MarketRuntimeException.ENTIRE_ORDER_AT_ONCE_NOT_SUPPORTED_WHEN_THE_MARKET_IS_NOT_OPEN, null, 
					new Object[]{ order_, market_ });	
		}
	}
	
	/**
	 * Checks and Rounds the Limit Quote of the Order
	 * @param order_ Order to be submitted
	 * @param market_ Market, where the Order will be submitted
	 * @return if the Quote was changed
	 * @throws MarketRuntimeException, if the Order is not suitable for the Market
	 */
	private static boolean checkAndRoundOrderLimitQuote(Order order_, Market market_) {
		boolean limitQuoteChanged = false;
		if(order_.getType() == OrderType.Limit) {
			double oldQuoteValue = order_.getLimitQuoteValue().getQuoteValue();
			double roundedQuote = 
				QuoteRounding.roundToValidQuote(
						order_.getLimitQuoteValue().getQuoteValue(), 
						market_.getMinimumQuoteIncrement());
			order_.getLimitQuoteValue().setQuoteValue(roundedQuote);
			limitQuoteChanged = (oldQuoteValue != roundedQuote);
		}
		
		return limitQuoteChanged;
	}

	private static void checkOrderStateForSubmit(Order order_,
			Market market_) {
		// TODO: Add Pending Submit 
		if(order_.getState() == OrderState.Submitted || order_.getState() == OrderState.WaitingSubmit) {
			throw MarketRuntimeException.createExceptionWithDetails(
					MarketRuntimeException.ORDER_ALREADY_SUBMITTED, null, 
					new Object[]{ order_, market_ });
		}
		if(order_.getState() == OrderState.Executed) {
			throw MarketRuntimeException.createExceptionWithDetails(
					MarketRuntimeException.EXECUTED_ORDER_CANNOT_BE_RESUBMITTED, null, 
					new Object[]{ order_, market_ });
		}
	}
	
	private static void checkOrderStateOnSubmit(Order order_,
			Market market_) {
		// TODO: This check should really be check if order is in Pending Submit or Waiting Submit
		if(order_.getState() == OrderState.Submitted) {
			throw MarketRuntimeException.createExceptionWithDetails(
					MarketRuntimeException.ORDER_ALREADY_SUBMITTED, null, 
					new Object[]{ order_, market_ });
		}
		if(order_.getState() == OrderState.Executed) {
			throw MarketRuntimeException.createExceptionWithDetails(
					MarketRuntimeException.EXECUTED_ORDER_CANNOT_BE_RESUBMITTED, null, 
					new Object[]{ order_, market_ });
		}
	}
	
	/**
	 * Checks the Order, when it is canceled
	 * @param order_ Order to be canceled
	 * @param market_ Market, where the Order will be canceled
	 * @throws MarketRuntimeException, if the Order is not suitable for Cancel
	 */
	public static void validateOrderForCancel(Order order_, Market market_) {
		if(order_.getState() == OrderState.Canceled) {
			throw MarketRuntimeException.createExceptionWithDetails(
					MarketRuntimeException.ORDER_HAS_ALREADY_BEEN_CANCELED, null, 
					new Object[]{ order_, market_ });	
		}
		if(order_.getState() == OrderState.Executed) {
			throw MarketRuntimeException.createExceptionWithDetails(
					MarketRuntimeException.EXECUTED_ORDER_CANNOT_BE_CANCELLED, null, 
					new Object[]{ order_, market_ });	
		}
	}
	
	/**
	 * Checks the Order, when it is deleted
	 * @param order_ Order to be deleted
	 * @param market_ Market, where the Order will be deleted
	 * @throws MarketRuntimeException, if the Order is not suitable for Delete
	 */
	public static void validateOrderForDelete(Order order_, Market market_) { 
		// TODO: Add Pending Submit 
		if(order_.getState() == OrderState.Submitted ||
		   order_.getState() == OrderState.WaitingSubmit) {
			throw MarketRuntimeException.createExceptionWithDetails(MarketRuntimeException.SUBMITTED_ORDER_CANNOT_BE_DELETED_CANCEL_FIRST, 
					null, new Object[] { order_ });
		}
	}
}
