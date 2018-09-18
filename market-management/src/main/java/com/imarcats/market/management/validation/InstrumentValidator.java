package com.imarcats.market.management.validation;

import com.imarcats.interfaces.client.v100.exception.MarketRuntimeException;
import com.imarcats.interfaces.client.v100.util.DataUtils;
import com.imarcats.model.Instrument;
import com.imarcats.model.types.ActivationStatus;
import com.imarcats.model.types.Address;
import com.imarcats.model.types.Position;
import com.imarcats.model.types.SettlementPrice;
import com.imarcats.model.types.SettlementType;

/**
 * Validates Instrument on Create or on Change 
 * @author Adam
 *
 */
public class InstrumentValidator {

	private InstrumentValidator() { /* static utility class */ }
	
	/**
	 * Validates a Instrument being changed
	 * @param instrument_ Instrument to be Validated
	 * @throws MarketRuntimeException if Instrument cannot be validated
	 */
	public static void validateInstrumentChange(Instrument instrument_) {	
		try {
			validateInstrumentBasics(instrument_);
		} catch (IllegalArgumentException e) {
			ValidatorUtils.checkForXssException(e);
		} 
	}
	
	/**
	 * Validates a Newly Created Instrument
	 * @param instrument_ Instrument to be Validated
	 * @throws MarketRuntimeException if Instrument cannot be validated
	 */
	public static void validateNewInstrument(Instrument instrument_) {
		try {
			validateNewInstrumentInternal(instrument_);
		} catch (IllegalArgumentException e) {
			ValidatorUtils.checkForXssException(e);
		} 
	}
		
	private static void validateNewInstrumentInternal(Instrument instrument_) {
		validateInstrumentBasics(instrument_);
		
		// we have removed this check to make instrument creation single step 
//		if(ValidatorUtils.isValidObjectIdString(instrument_.getAssetClassName())) {
//			throw MarketRuntimeException.createExceptionWithDetails(MarketRuntimeException.NEW_INSTRUMENT_MUST_NOT_HAVE_ASSET_CLASS_NAME, 
//					null, new Object[] { instrument_.getInstrumentCode(), DataUtils.VALID_ID });	
//		}
		// we have removed this check to make instrument creation single step 
//		if(ValidatorUtils.isValidString(instrument_.getMasterAgreementDocument())) {			
//			throw MarketRuntimeException.createExceptionWithDetails(MarketRuntimeException.NEW_INSTRUMENT_MUST_NOT_HAVE_MASTER_AGREEMENT_DOCUMENT, 
//				null, new Object[] { instrument_.getInstrumentCode() });	
//		}
		if(ValidatorUtils.isValidString(instrument_.getInstrumentCodeRolledOverFrom())) {			
			throw MarketRuntimeException.createExceptionWithDetails(MarketRuntimeException.NEW_INSTRUMENT_MUST_NOT_HAVE_INSTRUMENT_CODE_ROLLED_FROM, 
					null, new Object[] { instrument_.getInstrumentCode() });	
		}
		if(instrument_.getCreationAudit() != null) {			
			throw MarketRuntimeException.createExceptionWithDetails(MarketRuntimeException.NEW_INSTRUMENT_MUST_NOT_HAVE_CREATION_AUDIT, 
					null, new Object[] { instrument_.getInstrumentCode() });	
		}
		if(instrument_.getChangeAudit() != null) {			
			throw MarketRuntimeException.createExceptionWithDetails(MarketRuntimeException.NEW_INSTRUMENT_MUST_NOT_HAVE_CHANGE_AUDIT, 
					null, new Object[] { instrument_.getInstrumentCode() });	
		}
		if(instrument_.getRolloverAudit() != null) {			
			throw MarketRuntimeException.createExceptionWithDetails(MarketRuntimeException.NEW_INSTRUMENT_MUST_NOT_HAVE_ROLLOVER_AUDIT, 
					null, new Object[] { instrument_.getInstrumentCode() });	
		}
		if(instrument_.getApprovalAudit() != null) {			
			throw MarketRuntimeException.createExceptionWithDetails(MarketRuntimeException.NEW_INSTRUMENT_MUST_NOT_HAVE_APPROVAL_AUDIT, 
					null, new Object[] { instrument_.getInstrumentCode() });				
		}
		if(instrument_.getSuspensionAudit() != null) {			
			throw MarketRuntimeException.createExceptionWithDetails(MarketRuntimeException.NEW_INSTRUMENT_MUST_NOT_HAVE_SUSPENSION_AUDIT, 
					null, new Object[] { instrument_.getInstrumentCode() });	
		}
		if(instrument_.getActivationDate() != null) {
			throw MarketRuntimeException.createExceptionWithDetails(MarketRuntimeException.NEW_INSTRUMENT_MUST_NOT_HAVE_ACTIVATION_DATE, 
				null, new Object[] { instrument_.getInstrumentCode() });	
		}
		if(instrument_.getActivationStatus() != null && instrument_.getActivationStatus() != ActivationStatus.Created) {
			throw MarketRuntimeException.createExceptionWithDetails(MarketRuntimeException.NEW_INSTRUMENT_MUST_NOT_HAVE_ACTIVATION_STATUS, 
				null, new Object[] { instrument_.getInstrumentCode() });		
		}
	}
	
	/**
	 * Validates a Instrument being Rolled Over
	 * @param instrument_ Instrument to be Validated
	 * @param instrumentRolledOverFrom_ Instrument used as the Source for this Instrument in a Rollover
	 * @throws MarketRuntimeException if Instrument cannot be validated
	 */
	public static void validateRolloverInstrument(Instrument instrument_, Instrument instrumentRolledOverFrom_) {
		try {
			validateRolloverInstrumentInternal(instrument_, instrumentRolledOverFrom_);
		} catch (IllegalArgumentException e) {
			ValidatorUtils.checkForXssException(e);
		} 
	}
		
	private static void validateRolloverInstrumentInternal(Instrument instrument_, Instrument instrumentRolledOverFrom_) {
				
		validateInstrumentBasics(instrument_);
		
		if(!ValidatorUtils.isNonNullString(instrument_.getMasterAgreementDocument())) {
			throw MarketRuntimeException.createExceptionWithDetails(MarketRuntimeException.INSTRUMENT_MUST_HAVE_MASTER_AGREEMENT_DOCUMENT, 
					null, new Object[] { instrumentRolledOverFrom_, instrument_ });	
		}
		if(!instrumentRolledOverFrom_.getType().equals(instrument_.getType())) {
			throw MarketRuntimeException.createExceptionWithDetails(MarketRuntimeException.INSTRUMENT_TYPE_CANNOT_BE_CHANGED_WHILE_ROLLOVER, 
					null, new Object[] { instrumentRolledOverFrom_, instrument_ });			
		}
		if(!instrumentRolledOverFrom_.getAssetClassName().equals(instrument_.getAssetClassName())) {
			throw MarketRuntimeException.createExceptionWithDetails(MarketRuntimeException.INSTRUMENT_ASSET_CLASS_CANNOT_BE_CHANGED_WHILE_ROLLOVER, 
					null, new Object[] { instrumentRolledOverFrom_, instrument_ });		
		}
		if(((instrumentRolledOverFrom_.getSubType() != null) != (instrument_.getSubType() != null)) || 
		   instrumentRolledOverFrom_.getSubType() != null && 
		   !instrumentRolledOverFrom_.getSubType().equals(instrument_.getSubType())) {
			throw MarketRuntimeException.createExceptionWithDetails(MarketRuntimeException.INSTRUMENT_SUB_TYPE_CANNOT_BE_CHANGED_WHILE_ROLLOVER, 
					null, new Object[] { instrumentRolledOverFrom_, instrument_ });			
		}
		if(!instrumentRolledOverFrom_.getUnderlyingType().equals(instrument_.getUnderlyingType())) {
			throw MarketRuntimeException.createExceptionWithDetails(MarketRuntimeException.INSTRUMENT_UNDERLYING_TYPE_CANNOT_BE_CHANGED_WHILE_ROLLOVER, 
					null, new Object[] { instrumentRolledOverFrom_, instrument_ });		
		}		
		if(!instrumentRolledOverFrom_.getMasterAgreementDocument().equals(instrument_.getMasterAgreementDocument())) {
			throw MarketRuntimeException.createExceptionWithDetails(MarketRuntimeException.INSTRUMENT_MASTER_AGREEMENT_CANNOT_BE_CHANGED_WHILE_ROLLOVER, 
					null, new Object[] { instrumentRolledOverFrom_, instrument_ });		
		}	
		if(!instrumentRolledOverFrom_.getDenominationCurrency().equals(instrument_.getDenominationCurrency())) {
			throw MarketRuntimeException.createExceptionWithDetails(MarketRuntimeException.INSTRUMENT_DENOMINATION_CURRENCY_CANNOT_BE_CHANGED_WHILE_ROLLOVER, 
					null, new Object[] { instrumentRolledOverFrom_, instrument_ });		
		}	
		if(instrumentRolledOverFrom_.getContractSize() != (instrument_.getContractSize())) {
			throw MarketRuntimeException.createExceptionWithDetails(MarketRuntimeException.INSTRUMENT_CONTRACT_SIZE_CANNOT_BE_CHANGED_WHILE_ROLLOVER, 
					null, new Object[] { instrumentRolledOverFrom_, instrument_ });		
		}	
		if(!instrumentRolledOverFrom_.getContractSizeUnit().equals(instrument_.getContractSizeUnit())) {
			throw MarketRuntimeException.createExceptionWithDetails(MarketRuntimeException.INSTRUMENT_CONTRACT_SIZE_UNIT_CANNOT_BE_CHANGED_WHILE_ROLLOVER, 
					null, new Object[] { instrumentRolledOverFrom_, instrument_ });		
		}	
		if(!instrumentRolledOverFrom_.getQuoteType().equals(instrument_.getQuoteType())) {
			throw MarketRuntimeException.createExceptionWithDetails(MarketRuntimeException.INSTRUMENT_QUOTE_TYPE_CANNOT_BE_CHANGED_WHILE_ROLLOVER, 
					null, new Object[] { instrumentRolledOverFrom_, instrument_ });		
		}	
		if(!instrumentRolledOverFrom_.getSettlementPrice().equals(instrument_.getSettlementPrice())) {
			throw MarketRuntimeException.createExceptionWithDetails(MarketRuntimeException.INSTRUMENT_SETTLEMENT_PRICE_TYPE_CANNOT_BE_CHANGED_WHILE_ROLLOVER, 
					null, new Object[] { instrumentRolledOverFrom_, instrument_ });			
		}	
		if(!instrumentRolledOverFrom_.getSettlementType().equals(instrument_.getSettlementType())) {
			throw MarketRuntimeException.createExceptionWithDetails(MarketRuntimeException.INSTRUMENT_SETTLEMENT_TYPE_CANNOT_BE_CHANGED_WHILE_ROLLOVER, 
					null, new Object[] { instrumentRolledOverFrom_, instrument_ });			
		}	
		if(!instrumentRolledOverFrom_.getDeliveryPeriod().equals(instrument_.getDeliveryPeriod())) {			
			throw MarketRuntimeException.createExceptionWithDetails(MarketRuntimeException.INSTRUMENT_DELIVERY_PERIOD_CANNOT_BE_CHANGED_WHILE_ROLLOVER, 
				null, new Object[] { instrumentRolledOverFrom_, instrument_ });
		}
		if(!instrumentRolledOverFrom_.getRecordPurchaseAsPosition().equals(instrument_.getRecordPurchaseAsPosition())) {
			throw MarketRuntimeException.createExceptionWithDetails(MarketRuntimeException.INSTRUMENT_RECORD_PURCHASE_AS_TYPE_CANNOT_BE_CHANGED_WHILE_ROLLOVER, 
					null, new Object[] { instrumentRolledOverFrom_, instrument_ });
		}
		
		validateDeliveryAddressNotChanged(instrumentRolledOverFrom_.getDeliveryLocation(), instrument_.getDeliveryLocation(), instrument_);
	}
	
	private static void validateDeliveryAddressNotChanged(Address address1_, Address address2_, Instrument instrument_) {
		MarketRuntimeException exception = MarketRuntimeException.createExceptionWithDetails(MarketRuntimeException.INSTRUMENT_DELIVERY_LOCATION_CANNOT_BE_CHANGED_WHILE_ROLLOVER, 
				null, new Object[] { address1_, address2_, instrument_ });
		if((address1_ != null) != (address2_ != null)) {
			throw exception;
		}
		if(address1_ != null) {
			if(!address1_.getCity().equals(address2_.getCity()) || 
			   !address1_.getCountry().equals(address2_.getCountry()) || 
			   !address1_.getStreet().equals(address2_.getStreet()) || 
			   !address1_.getPostalCode().equals(address2_.getPostalCode()) || 
			   (((address1_.getState() != null) != (address2_.getState() != null))
				|| address1_.getState() != null && !address1_.getState().equals(address2_.getState()))) {
				throw exception;
			}
		}
	}
	
	private static void validateInstrumentBasics(Instrument instrument_) {
		if(instrument_ == null) {
			throw MarketRuntimeException.createExceptionWithDetails(MarketRuntimeException.NULL_INSTRUMENT_CANNOT_BE_CREATED, 
					null, new Object[] {});	
		}
		if(!ValidatorUtils.isValidObjectIdString(instrument_.getInstrumentCode())) {
			throw MarketRuntimeException.createExceptionWithDetails(MarketRuntimeException.INSTRUMENT_MUST_HAVE_VALID_CODE, 
					null, new Object[] { DataUtils.VALID_ID });	
		}
		if(!ValidatorUtils.isValidString(instrument_.getName())) {
			throw MarketRuntimeException.createExceptionWithDetails(MarketRuntimeException.INSTRUMENT_MUST_HAVE_NAME, 
					null, new Object[] { instrument_.getInstrumentCode(), DataUtils.VALID_STRINGS });
		}
		if(!ValidatorUtils.isValidString(instrument_.getDescription())) {
			throw MarketRuntimeException.createExceptionWithDetails(MarketRuntimeException.INSTRUMENT_MUST_HAVE_DESCRIPTION, 
					null, new Object[] { instrument_.getInstrumentCode(), DataUtils.VALID_STRINGS });
		}
		if(!ValidatorUtils.isValidOptionalString(instrument_.getIsin())) {
			throw MarketRuntimeException.createExceptionWithDetails(MarketRuntimeException.ISIN_MUST_BE_VALID, 
					null, new Object[] { instrument_.getInstrumentCode(), DataUtils.VALID_STRINGS });
		}
		if(!ValidatorUtils.isValidOptionalString(instrument_.getSubType())) {
			throw MarketRuntimeException.createExceptionWithDetails(MarketRuntimeException.SUB_TYPE_MUST_BE_VALID, 
					null, new Object[] { instrument_.getInstrumentCode(), DataUtils.VALID_STRINGS });
		}
		if(instrument_.getType() == null) {
			throw MarketRuntimeException.createExceptionWithDetails(MarketRuntimeException.INSTRUMENT_MUST_HAVE_INSTRUMENT_TYPE, 
					null, new Object[] { instrument_.getInstrumentCode() });
		}
		if(!ValidatorUtils.isValidObjectIdString(instrument_.getUnderlyingCode())) {
			throw MarketRuntimeException.createExceptionWithDetails(MarketRuntimeException.INSTRUMENT_MUST_HAVE_UNDERLYING_CODE, 
					null, new Object[] { instrument_.getInstrumentCode(), DataUtils.VALID_ID });	
		}
		if(instrument_.getUnderlyingType() == null) {
			throw MarketRuntimeException.createExceptionWithDetails(MarketRuntimeException.INSTRUMENT_MUST_HAVE_UNDERLYING_TYPE, 
					null, new Object[] { instrument_.getInstrumentCode() });
		}
		// This is commented out, because we allow Derivative created at the top of a Product directly, 
		// like for electricity futures exist, where as there is no spot electricity market
		/*
		if(instrument_.getType() == InstrumentType.Spot && instrument_.getUnderlyingType() != UnderlyingType.Product ) {
			throw MarketRuntimeException.createExceptionWithDetails(MarketRuntimeException.SPOT_INSTRUMENT_MUST_HAVE_PRODUCT_UNDERLYING, 
					null, new Object[] { instrument_.getInstrumentCode() });
		}
		if(instrument_.getType() == InstrumentType.Derivative && instrument_.getUnderlyingType() != UnderlyingType.Instrument) {
			throw MarketRuntimeException.createExceptionWithDetails(MarketRuntimeException.DERIVATIVE_INSTRUMENT_MUST_HAVE_INSTRUMENT_UNDERLYING, 
					null, new Object[] { instrument_.getInstrumentCode() });
		}
		*/
		
		if(!ValidatorUtils.isValidString(instrument_.getDenominationCurrency())) {
			throw MarketRuntimeException.createExceptionWithDetails(MarketRuntimeException.INSTRUMENT_MUST_HAVE_DENOMINATION_CURRENCY, 
					null, new Object[] { instrument_.getInstrumentCode(), DataUtils.VALID_STRINGS });
		}
		if(instrument_.getContractSize() <= 0) {
			throw MarketRuntimeException.createExceptionWithDetails(MarketRuntimeException.INSTRUMENT_MUST_HAVE_CONTRACT_SIZE, 
					null, new Object[] { instrument_.getInstrumentCode() });
		}
		if(!ValidatorUtils.isValidString(instrument_.getContractSizeUnit())) {
			throw MarketRuntimeException.createExceptionWithDetails(MarketRuntimeException.INSTRUMENT_MUST_HAVE_CONTRACT_SIZE_UNIT, 
					null, new Object[] { instrument_.getInstrumentCode(), DataUtils.VALID_STRINGS });
		}
		if(instrument_.getQuoteType() == null) {
			throw MarketRuntimeException.createExceptionWithDetails(MarketRuntimeException.INSTRUMENT_MUST_HAVE_QUOTE_TYPE, 
					null, new Object[] { instrument_.getInstrumentCode() });
		}
		if(instrument_.getSettlementPrice() == null) {
			throw MarketRuntimeException.createExceptionWithDetails(MarketRuntimeException.INSTRUMENT_MUST_HAVE_SETTLEMENT_PRICE_TYPE, 
					null, new Object[] { instrument_.getInstrumentCode() });
		}
		if(instrument_.getSettlementPrice() != SettlementPrice.Clean) {
			throw MarketRuntimeException.createExceptionWithDetails(MarketRuntimeException.ONLY_CLEAN_SETTLEMENT_PRICE_TYPE_IS_SUPPORTED_ON_INSTRUMENTS, 
					null, new Object[] { instrument_.getInstrumentCode() });
		}
		if(instrument_.getSettlementType() == null) {
			throw MarketRuntimeException.createExceptionWithDetails(MarketRuntimeException.INSTRUMENT_MUST_HAVE_SETTLEMENT_TYPE, 
					null, new Object[] { instrument_.getInstrumentCode() });	
		}	
		if(instrument_.getSettlementType() == SettlementType.PhysicalDelivery) {
			validateDeliveryLocation(instrument_);
		}			
		if(instrument_.getDeliveryPeriod() == null) {
			throw MarketRuntimeException.createExceptionWithDetails(MarketRuntimeException.INSTRUMENT_MUST_HAVE_DELIVERY_PERIOD, 
					null, new Object[] { instrument_.getInstrumentCode() });
		}			
		if(instrument_.getRecordPurchaseAsPosition() == null) {
			throw MarketRuntimeException.createExceptionWithDetails(MarketRuntimeException.INSTRUMENT_MUST_HAVE_RECORD_PURSCHASE_AS_TYPE, 
					null, new Object[] { instrument_.getInstrumentCode() });
		}
		if(instrument_.getRecordPurchaseAsPosition() != Position.Long) {
			throw MarketRuntimeException.createExceptionWithDetails(MarketRuntimeException.ONLY_LONG_RECORD_PURSCHASE_AS_TYPE_SUPPORTED_ON_INSTRUMENTS, 
					null, new Object[] { instrument_.getInstrumentCode() });	
		}

		// TODO: Consider removing this 
//		if(!ValidatorUtils.isValidString(instrument_.getCurrencyOrUnit())) {
//			throw MarketRuntimeException.createExceptionWithDetails(
//					MarketRuntimeException.INSTRUMENT_MUST_HAVE_CURRENCY_OR_UNIT, 
//					null, new Object[] { instrument_.getInstrumentCode() });
//		}

		 ValidatorUtils.validateRollableProperties(instrument_.getRollablePropertyNames(), instrument_.getProperties());
	}

	private static void validateDeliveryLocation(Instrument instrument_) {
		if(instrument_.getDeliveryLocation() == null) {
			throw MarketRuntimeException.createExceptionWithDetails(MarketRuntimeException.INSTRUMENT_MUST_HAVE_DELIVERY_LOCATION_FOR_PHYSICAL_DELIVERY, 
					null, new Object[] { instrument_.getInstrumentCode() });	
		}
		ValidatorUtils.validateAddress(instrument_.getDeliveryLocation());
	}
	
}
