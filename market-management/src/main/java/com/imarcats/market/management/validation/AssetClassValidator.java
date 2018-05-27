package com.imarcats.market.management.validation;

import com.imarcats.interfaces.client.v100.exception.MarketRuntimeException;
import com.imarcats.interfaces.client.v100.util.DataUtils;
import com.imarcats.model.AssetClass;

/**
 * Validates Asset Class on Create or on Change 
 * @author Adam
 *
 */
public class AssetClassValidator {

	private AssetClassValidator() { /* static utility class */ }
	
	/**
	 * Validates an Asset Class
	 * @param assetClass_ Asset Class to be Validated
	 * @throws MarketRuntimeException if Asset Class cannot be validated
	 */
	public static void validateAssetClass(AssetClass assetClass_) {
		try {
			validateAssetClassInternal(assetClass_);
		} catch (IllegalArgumentException e) {
			ValidatorUtils.checkForXssException(e);
		} 
	}

	private static void validateAssetClassInternal(AssetClass assetClass_) {
		if(assetClass_ == null) {
			throw MarketRuntimeException.createExceptionWithDetails(
					MarketRuntimeException.NULL_ASSET_CLASS_CANNOT_BE_CREATED, 
					null, new Object[] { });
		} 
		if(!DataUtils.isValidObjectIdString(assetClass_.getName())) {
			throw MarketRuntimeException.createExceptionWithDetails(
					MarketRuntimeException.ASSET_CLASS_WITHOUT_NAME, 
					null, new Object[] { DataUtils.VALID_ID });
		} 
		if(!DataUtils.isValidString(assetClass_.getDescription())) {
			throw MarketRuntimeException.createExceptionWithDetails(
					MarketRuntimeException.ASSET_CLASS_WITHOUT_DESCRIPTION, 
					null, new Object[] { assetClass_.getName(), DataUtils.VALID_STRINGS });
		} 
		if(!DataUtils.isValidOptionalString(assetClass_.getParentName())) {
			throw MarketRuntimeException.createExceptionWithDetails(
					MarketRuntimeException.ASSET_CLASS_PARENT_NAME_INVALID, 
					null, new Object[] { assetClass_.getName(), DataUtils.VALID_ID  });
		} 
		
		ValidatorUtils.validateProperties(assetClass_.getProperties());
	}
}
