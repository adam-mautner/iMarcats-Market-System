package com.imarcats.market.management.validation;

import com.imarcats.interfaces.client.v100.exception.MarketRuntimeException;
import com.imarcats.interfaces.client.v100.util.DataUtils;
import com.imarcats.model.MarketOperator;
import com.imarcats.model.types.ActivationStatus;

/**
 * Validates Market Operator on Create or on Change 
 * @author Adam
 *
 */
public class MarketOperatorValidator {

	private MarketOperatorValidator() { /* static utility class */ }
	
	/**
	 * Validates a Market Operator being changed
	 * @param marketOperator_ Market Operator to be Validated
	 * @throws MarketRuntimeException if Market Operator cannot be validated
	 */
	public static void validateMarketOperatorChange(MarketOperator marketOperator_) {
		try {
			validateMarketOperatorBasics(marketOperator_);
		} catch (IllegalArgumentException e) {
			ValidatorUtils.checkForXssException(e);
		} 
	}

	/**
	 * Validates a Newly Created Market Operator
	 * @param marketOperator_ Market Operator to be Validated
	 * @throws MarketRuntimeException if Market Operator cannot be validated 
	 */
	public static void validateNewMarketOperator(MarketOperator marketOperator_) {
		try {
			validateNewMarketOperatorInternal(marketOperator_);
		} catch (IllegalArgumentException e) {
			ValidatorUtils.checkForXssException(e);
		} 
	}
		
	private static void validateNewMarketOperatorInternal(MarketOperator marketOperator_) {
		validateMarketOperatorBasics(marketOperator_);
		// we have removed this check to make market operator creation single step 
//		if(ValidatorUtils.isValidString(marketOperator_.getMarketOperatorAgreement())) {
//			throw MarketRuntimeException.createExceptionWithDetails(
//					MarketRuntimeException.NEW_MARKET_OPERATOR_MUST_NOT_HAVE_MARKET_OPERATOR_AGREEMENT, 
//					null, new Object[] { marketOperator_.getCode() });
//		}
		if(marketOperator_.getActivationStatus() != null && marketOperator_.getActivationStatus() != ActivationStatus.Created) {
			throw MarketRuntimeException.createExceptionWithDetails(
					MarketRuntimeException.NEW_MARKET_OPERATOR_MUST_NOT_HAVE_ACTIVATION_STATUS, 
					null, new Object[] { marketOperator_.getCode() });
		}
		if(marketOperator_.getCreationAudit() != null) {
			throw MarketRuntimeException.createExceptionWithDetails(
					MarketRuntimeException.NEW_MARKET_OPERATOR_MUST_NOT_HAVE_CREATION_AUDIT, 
					null, new Object[] { marketOperator_.getCode() });
		}
		if(marketOperator_.getChangeAudit() != null) {
			throw MarketRuntimeException.createExceptionWithDetails(
					MarketRuntimeException.NEW_MARKET_OPERATOR_MUST_NOT_HAVE_CHANGE_AUDIT, 
					null, new Object[] { marketOperator_.getCode() });	
		}
		if(marketOperator_.getApprovalAudit() != null) {
			throw MarketRuntimeException.createExceptionWithDetails(
					MarketRuntimeException.NEW_MARKET_OPERATOR_MUST_NOT_HAVE_APPROVAL_AUDIT, 
					null, new Object[] { marketOperator_.getCode() });
		}
		if(marketOperator_.getSuspensionAudit() != null) {
			throw MarketRuntimeException.createExceptionWithDetails(
					MarketRuntimeException.NEW_MARKET_OPERATOR_MUST_NOT_HAVE_SUSPENSION_AUDIT, 
					null, new Object[] { marketOperator_.getCode() });	
		}
	}	

	private static void validateMarketOperatorBasics(MarketOperator marketOperator_) {
		if(marketOperator_ == null) {
			throw MarketRuntimeException.createExceptionWithDetails(
					MarketRuntimeException.NULL_MARKET_OPERATOR_CANNOT_BE_CREATED, 
					null, new Object[] { });
		}
		if(!ValidatorUtils.isValidObjectIdString(marketOperator_.getCode())) {
			throw MarketRuntimeException.createExceptionWithDetails(
					MarketRuntimeException.MARKET_OPERATOR_MUST_HAVE_CODE, 
					null, new Object[] { DataUtils.VALID_ID });
		}
		if(!ValidatorUtils.isValidString(marketOperator_.getName())) {
			throw MarketRuntimeException.createExceptionWithDetails(
					MarketRuntimeException.MARKET_OPERATOR_MUST_HAVE_NAME, 
					null, new Object[] { marketOperator_.getCode(), DataUtils.VALID_STRINGS });
		}
		if(!ValidatorUtils.isValidString(marketOperator_.getDescription())) {
			throw MarketRuntimeException.createExceptionWithDetails(
					MarketRuntimeException.MARKET_OPERATOR_MUST_HAVE_DESCRIPTION, 
					null, new Object[] { marketOperator_.getCode(), DataUtils.VALID_STRINGS });
		}
		if(!ValidatorUtils.isValidObjectIdString(marketOperator_.getBusinessEntityCode())) {
			throw MarketRuntimeException.createExceptionWithDetails(
					MarketRuntimeException.MARKET_OPERATOR_MUST_HAVE_BUSINESS_ENTITY, 
					null, new Object[] { marketOperator_.getCode(), DataUtils.VALID_ID });	
		}
		if(!ValidatorUtils.isValidUserIdString(marketOperator_.getOwnerUserID())) {
			throw MarketRuntimeException.createExceptionWithDetails(
					MarketRuntimeException.MARKET_OPERATOR_MUST_HAVE_OWNER_USER, 
					null, new Object[] { marketOperator_.getCode(), DataUtils.VALID_USER_ID });	
		}
	}
}
