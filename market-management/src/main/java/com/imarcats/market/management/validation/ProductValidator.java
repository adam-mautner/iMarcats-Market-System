package com.imarcats.market.management.validation;

import com.imarcats.interfaces.client.v100.exception.MarketRuntimeException;
import com.imarcats.interfaces.client.v100.util.DataUtils;
import com.imarcats.model.Product;
import com.imarcats.model.types.ActivationStatus;

/**
 * Validates Product on Create or on Change 
 * @author Adam
 *
 */
public class ProductValidator {

	private ProductValidator() { /* static utility class */ }

	/**
	 * Validates a Product being changed
	 * @param product_ Product to be Validated
	 * @throws MarketRuntimeException if Product cannot be validated
	 */
	public static void validateProductChange(Product product_) {
		try {
			validateProductBasics(product_);
		} catch (IllegalArgumentException e) {
			ValidatorUtils.checkForXssException(e);
		} 
	}
	
	/**
	 * Validates a Newly Created Product 
	 * @param product_ Product to be Validated
	 * @throws MarketRuntimeException if Product cannot be validated
	 */
	public static void validateNewProduct(Product product_) {
		try {
			validateNewProductInternal(product_);
		} catch (IllegalArgumentException e) {
			ValidatorUtils.checkForXssException(e);
		} 
	}
	
	private static void validateNewProductInternal(Product product_) {
		validateProductBasics(product_);
		
		if(ValidatorUtils.isValidString(product_.getProductCodeRolledOverFrom())) {
			throw MarketRuntimeException.createExceptionWithDetails(MarketRuntimeException.NEW_PRODUCT_MUST_NOT_HAVE_PRODUCT_CODE_ROLLED_FROM, null, new Object[] { product_.getProductCode() });
		}
		if(product_.getActivationDate() != null) {
			throw MarketRuntimeException.createExceptionWithDetails(MarketRuntimeException.NEW_PRODUCT_MUST_NOT_HAVE_ACTIVATION_DATE, null, new Object[] { product_.getProductCode() });	
		}
		if(product_.getActivationStatus() != null && product_.getActivationStatus() != ActivationStatus.Created) {
			throw MarketRuntimeException.createExceptionWithDetails(MarketRuntimeException.NEW_PRODUCT_MUST_NOT_HAVE_ACTIVATION_STATUS, null, new Object[] { product_.getProductCode() });
		}
		// we have removed this check to make product creation single step 
//		if(ValidatorUtils.isValidString(product_.getProductDefinitionDocument())) {
//			throw MarketRuntimeException.createExceptionWithDetails(MarketRuntimeException.NEW_PRODUCT_MUST_NOT_HAVE_DEFINITION_DOCUMENT, null, new Object[] { product_.getProductCode() });
//		}
		if(product_.getCreationAudit() != null) {
			throw MarketRuntimeException.createExceptionWithDetails(MarketRuntimeException.NEW_PRODUCT_MUST_NOT_HAVE_CREATION_AUDIT, null, new Object[] { product_.getProductCode() });
		}
		if(product_.getChangeAudit() != null) {
			throw MarketRuntimeException.createExceptionWithDetails(MarketRuntimeException.NEW_PRODUCT_MUST_NOT_HAVE_CHANGE_AUDIT, null, new Object[] { product_.getProductCode() });
		}
		if(product_.getRolloverAudit() != null) {
			throw MarketRuntimeException.createExceptionWithDetails(MarketRuntimeException.NEW_PRODUCT_MUST_NOT_HAVE_ROLLOVER_AUDIT, null, new Object[] { product_.getProductCode() });
		}
		if(product_.getApprovalAudit() != null) {
			throw MarketRuntimeException.createExceptionWithDetails(MarketRuntimeException.NEW_PRODUCT_MUST_NOT_HAVE_APPROVAL_AUDIT, null, new Object[] { product_.getProductCode() });
		}
		if(product_.getSuspensionAudit() != null) {
			throw MarketRuntimeException.createExceptionWithDetails(MarketRuntimeException.NEW_PRODUCT_MUST_NOT_HAVE_SUSPENSION_AUDIT, null, new Object[] { product_.getProductCode() });
		}
	}
	
	/**
	 * Validates a Product being Rolled Over
	 * @param product_ Product to be Validated
	 * @param productRolledOverFrom_ Product used as the Source for this Product in a Rollover
	 * @throws MarketRuntimeException if Product cannot be validated
	 */
	public static void validateRolloverProduct(Product product_, Product productRolledOverFrom_) {
		try {
			validateRolloverProductInternal(product_, productRolledOverFrom_);
		} catch (IllegalArgumentException e) {
			ValidatorUtils.checkForXssException(e);
		} 
	}
		
	private static void validateRolloverProductInternal(Product product_, Product productRolledOverFrom_) {
		validateProductBasics(product_);
	
		if(!productRolledOverFrom_.getType().equals(product_.getType())) {
			throw MarketRuntimeException.createExceptionWithDetails(MarketRuntimeException.PRODUCT_TYPE_CANNOT_BE_CHANGED_WHILE_ROLLOVER, null, new Object[] { productRolledOverFrom_, product_ });				
		}
		if(!ValidatorUtils.isNonNullString(product_.getProductDefinitionDocument())) {
			throw MarketRuntimeException.createExceptionWithDetails(MarketRuntimeException.PRODUCT_MUST_HAVE_DEFINITION_DOCUMENT, null, new Object[] { productRolledOverFrom_, product_ });	
		}
		if(!productRolledOverFrom_.getProductDefinitionDocument().equals(product_.getProductDefinitionDocument())) {
			throw MarketRuntimeException.createExceptionWithDetails(MarketRuntimeException.PRODUCT_DEFINITION_DOCUMENT_CANNOT_BE_CHANGED_WHILE_ROLLOVER, null, new Object[] { productRolledOverFrom_, product_ });				
		}	
		if(((productRolledOverFrom_.getCategory() != null) != (product_.getCategory() != null)) || 
		   productRolledOverFrom_.getCategory() != null && 
		   !productRolledOverFrom_.getCategory().equals(product_.getCategory())) {
			throw MarketRuntimeException.createExceptionWithDetails(MarketRuntimeException.PRODUCT_CATEGORY_CANNOT_BE_CHANGED_WHILE_ROLLOVER, null, new Object[] { productRolledOverFrom_, product_ });				
		}
		if(((productRolledOverFrom_.getSubCategory() != null) != (product_.getSubCategory() != null)) || 
		   productRolledOverFrom_.getSubCategory() != null && 
		   !productRolledOverFrom_.getSubCategory().equals(product_.getSubCategory())) {
			throw MarketRuntimeException.createExceptionWithDetails(MarketRuntimeException.PRODUCT_SUB_CATEGORY_CANNOT_BE_CHANGED_WHILE_ROLLOVER, null, new Object[] { productRolledOverFrom_, product_ });				
		}
	}
	
	private static void validateProductBasics(Product product_) {
		if(product_ == null) {
			throw MarketRuntimeException.createExceptionWithDetails(MarketRuntimeException.NULL_PRODUCT_CANNOT_BE_CREATED, null, new Object[] { });
		}
		if(!ValidatorUtils.isValidObjectIdString(product_.getProductCode())) {
			throw MarketRuntimeException.createExceptionWithDetails(MarketRuntimeException.PRODUCT_MUST_HAVE_PRODUCT_VALID_CODE, null, new Object[] { DataUtils.VALID_ID });
		}
		if(!ValidatorUtils.isValidString(product_.getName())) {
			throw MarketRuntimeException.createExceptionWithDetails(MarketRuntimeException.PRODUCT_MUST_HAVE_NAME, null, new Object[] { product_.getProductCode(), DataUtils.VALID_STRINGS });	
		}
		if(!ValidatorUtils.isValidString(product_.getDescription())) {
			throw MarketRuntimeException.createExceptionWithDetails(MarketRuntimeException.PRODUCT_MUST_HAVE_DESCRIPTION, null, new Object[] { product_.getProductCode(), DataUtils.VALID_STRINGS });	
		}
		if(!ValidatorUtils.isValidOptionalString(product_.getCategory())) {
			throw MarketRuntimeException.createExceptionWithDetails(MarketRuntimeException.CATEGORY_MUST_BE_VALID, null, new Object[] { product_.getProductCode(), DataUtils.VALID_STRINGS });	
		}
		if(!ValidatorUtils.isValidOptionalString(product_.getSubCategory())) {
			throw MarketRuntimeException.createExceptionWithDetails(MarketRuntimeException.SUB_CATEGORY_MUST_BE_VALID, null, new Object[] { product_.getProductCode(), DataUtils.VALID_STRINGS });	
		}
		if(product_.getType() == null) {
			throw MarketRuntimeException.createExceptionWithDetails(MarketRuntimeException.PRODUCT_MUST_HAVE_PRODUCT_TYPE, null, new Object[] { product_.getProductCode() });	
		}	
		
		ValidatorUtils.validateRollableProperties(product_.getRollablePropertyNames(), product_.getProperties());
	}
	
}
