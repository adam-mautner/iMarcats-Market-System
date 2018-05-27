package com.imarcats.market.management.validation;

import java.util.List;

import com.imarcats.interfaces.client.v100.exception.ExceptionLanguageKeys;
import com.imarcats.interfaces.client.v100.exception.MarketRuntimeException;
import com.imarcats.model.Instrument;
import com.imarcats.model.types.ActivationStatus;
import com.imarcats.model.types.Address;
import com.imarcats.model.types.DeliveryPeriod;
import com.imarcats.model.types.InstrumentType;
import com.imarcats.model.types.Property;
import com.imarcats.model.types.QuoteType;
import com.imarcats.model.types.SettlementPrice;
import com.imarcats.model.types.SettlementType;
import com.imarcats.model.types.UnderlyingType;
import com.imarcats.model.utils.PropertyUtils;

public class InstrumentValidatorTest extends ValidatorTestCaseBase {

	public void testBasicValidation() throws Exception {		
		try {
			InstrumentValidator.validateInstrumentChange(null);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.NULL_INSTRUMENT_CANNOT_BE_CREATED, e.getLanguageKey());
		}
		
		Instrument instrument = new Instrument();
		
		try {
			InstrumentValidator.validateInstrumentChange(instrument);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.INSTRUMENT_MUST_HAVE_VALID_CODE, e.getLanguageKey());
		}
		instrument.setInstrumentCode("   ");
		try {
			InstrumentValidator.validateInstrumentChange(instrument);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.INSTRUMENT_MUST_HAVE_VALID_CODE, e.getLanguageKey());
		}
		instrument.setInstrumentCode(" Test");
		try {
			InstrumentValidator.validateInstrumentChange(instrument);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.INSTRUMENT_MUST_HAVE_VALID_CODE, e.getLanguageKey());
		}
		instrument.setInstrumentCode("Test ");
		try {
			InstrumentValidator.validateInstrumentChange(instrument);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.INSTRUMENT_MUST_HAVE_VALID_CODE, e.getLanguageKey());
		}
		instrument.setInstrumentCode("Te st");
		try {
			InstrumentValidator.validateInstrumentChange(instrument);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.INSTRUMENT_MUST_HAVE_VALID_CODE, e.getLanguageKey());
		}
		
		instrument.setInstrumentCode("TEST");

		try {
			InstrumentValidator.validateInstrumentChange(instrument);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.INSTRUMENT_MUST_HAVE_NAME, e.getLanguageKey());
		}
		instrument.setName("Test");
		
		try {
			InstrumentValidator.validateInstrumentChange(instrument);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.INSTRUMENT_MUST_HAVE_DESCRIPTION, e.getLanguageKey());
		}
		instrument.setDescription("Test");
		
		instrument.setIsin("<>");
		try {
			InstrumentValidator.validateInstrumentChange(instrument);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.ISIN_MUST_BE_VALID, e.getLanguageKey());
		}
		instrument.setIsin("Test");
		
		instrument.setSubType("<>");
		try {
			InstrumentValidator.validateInstrumentChange(instrument);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.SUB_TYPE_MUST_BE_VALID, e.getLanguageKey());
		}
		instrument.setSubType(null);

		try {
			InstrumentValidator.validateInstrumentChange(instrument);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.INSTRUMENT_MUST_HAVE_INSTRUMENT_TYPE, e.getLanguageKey());
		}
		instrument.setType(InstrumentType.Derivative);

		try {
			InstrumentValidator.validateInstrumentChange(instrument);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.INSTRUMENT_MUST_HAVE_UNDERLYING_CODE, e.getLanguageKey());
		}
		instrument.setUnderlyingCode("TEST");
		
		try {
			InstrumentValidator.validateInstrumentChange(instrument);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.INSTRUMENT_MUST_HAVE_UNDERLYING_TYPE, e.getLanguageKey());
		}
		instrument.setUnderlyingType(UnderlyingType.Product);

		// This is commented out, because we allow Derivative created at the top of a Product directly, 
		// like for electricity futures exist, where as there is no spot electricity market
		/*
		try {
			InstrumentValidator.validateInstrumentChange(instrument);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.DERIVATIVE_INSTRUMENT_MUST_HAVE_INSTRUMENT_UNDERLYING, e.getLanguageKey());
		}
		
		instrument.setUnderlyingType(UnderlyingType.Instrument);
		instrument.setType(InstrumentType.Spot);
		try {
			InstrumentValidator.validateInstrumentChange(instrument);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.SPOT_INSTRUMENT_MUST_HAVE_PRODUCT_UNDERLYING, e.getLanguageKey());
		}
		instrument.setType(InstrumentType.Derivative);		
		*/
		
		try {
			InstrumentValidator.validateInstrumentChange(instrument);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.INSTRUMENT_MUST_HAVE_DENOMINATION_CURRENCY, e.getLanguageKey());
		}
		instrument.setDenominationCurrency("USD");	
		
		instrument.setContractSize(0);
		try {
			InstrumentValidator.validateInstrumentChange(instrument);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.INSTRUMENT_MUST_HAVE_CONTRACT_SIZE, e.getLanguageKey());
		}
		instrument.setContractSize(0.1);
		
		try {
			InstrumentValidator.validateInstrumentChange(instrument);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.INSTRUMENT_MUST_HAVE_CONTRACT_SIZE_UNIT, e.getLanguageKey());
		}
		instrument.setContractSizeUnit("Tons");	
		
		instrument.setQuoteType(null);
		try {
			InstrumentValidator.validateInstrumentChange(instrument);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.INSTRUMENT_MUST_HAVE_QUOTE_TYPE, e.getLanguageKey());
		}
		instrument.setQuoteType(QuoteType.Price);

		instrument.setSettlementPrice(null);
		try {
			InstrumentValidator.validateInstrumentChange(instrument);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.INSTRUMENT_MUST_HAVE_SETTLEMENT_PRICE_TYPE, e.getLanguageKey());
		}
		instrument.setSettlementPrice(SettlementPrice.Dirty);

		try {
			InstrumentValidator.validateInstrumentChange(instrument);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.ONLY_CLEAN_SETTLEMENT_PRICE_TYPE_IS_SUPPORTED_ON_INSTRUMENTS, e.getLanguageKey());
		}
		instrument.setSettlementPrice(SettlementPrice.Clean);
		
		try {
			InstrumentValidator.validateInstrumentChange(instrument);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.INSTRUMENT_MUST_HAVE_SETTLEMENT_TYPE, e.getLanguageKey());
		}
		instrument.setSettlementType(SettlementType.PhysicalDelivery);
		
		try {
			InstrumentValidator.validateInstrumentChange(instrument);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.INSTRUMENT_MUST_HAVE_DELIVERY_LOCATION_FOR_PHYSICAL_DELIVERY, e.getLanguageKey());
		}
		instrument.setDeliveryLocation(new Address());		
		
		try {
			InstrumentValidator.validateInstrumentChange(instrument);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.ADDRESS_MUST_HAVE_CITY, e.getLanguageKey());
		}
		instrument.getDeliveryLocation().setCity("Test");			
		
		try {
			InstrumentValidator.validateInstrumentChange(instrument);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.ADDRESS_MUST_HAVE_COUNTRY, e.getLanguageKey());
		}
		instrument.getDeliveryLocation().setCountry("Test");
		
		try {
			InstrumentValidator.validateInstrumentChange(instrument);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.ADDRESS_MUST_HAVE_POSTAL_CODE, e.getLanguageKey());
		}
		instrument.getDeliveryLocation().setPostalCode("Test");
		
		try {
			InstrumentValidator.validateInstrumentChange(instrument);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.ADDRESS_MUST_HAVE_STREET, e.getLanguageKey());
		}
		instrument.getDeliveryLocation().setStreet("Test");
		
		try {
			InstrumentValidator.validateInstrumentChange(instrument);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.INSTRUMENT_MUST_HAVE_DELIVERY_PERIOD, e.getLanguageKey());
		}
		instrument.setDeliveryPeriod(DeliveryPeriod.T1);	
		
		InstrumentValidator.validateInstrumentChange(instrument);
		
		// test properties 
		final Instrument instrumentFinal = instrument;
		testRollableProperties(new RollablePropertyAccessor() {
			
			@Override
			public void validate() {
				InstrumentValidator.validateInstrumentChange(instrumentFinal);
			}
			
			@Override
			public Property[] getProperties() {
				return instrumentFinal.getProperties();
			}
			
			@Override
			public void deleteProperty(Property property_) {
				instrumentFinal.deleteProperty(property_);
			}
			
			@Override
			public void clearProperties() {
				instrumentFinal.clearProperties();
			}
			
			@Override
			public void addProperty(Property property_) {
				instrumentFinal.addProperty(property_);
			}

			@Override
			public List<String> getRollablePropertyNames() {
				return instrumentFinal.getRollablePropertyNames();
			}

			@Override
			public void setRollablePropertyNames(
					List<String> rollablePropertyNames_) {
				instrumentFinal.setRollablePropertyNames(rollablePropertyNames_);
			}
		});
	}

	public void testNewInstrumentValidation() throws Exception {
		Instrument instrument = createInstrument("TEST");
		
		try {
			InstrumentValidator.validateNewInstrument(instrument);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.NEW_INSTRUMENT_MUST_NOT_HAVE_ASSET_CLASS_NAME, e.getLanguageKey());
		}
		instrument.setAssetClassName(null);

		try {
			InstrumentValidator.validateNewInstrument(instrument);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.NEW_INSTRUMENT_MUST_NOT_HAVE_MASTER_AGREEMENT_DOCUMENT, e.getLanguageKey());
		}
		instrument.setMasterAgreementDocument(null);
		
		try {
			InstrumentValidator.validateNewInstrument(instrument);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.NEW_INSTRUMENT_MUST_NOT_HAVE_INSTRUMENT_CODE_ROLLED_FROM, e.getLanguageKey());
		}
		instrument.setInstrumentCodeRolledOverFrom(null);
		
		try {
			InstrumentValidator.validateNewInstrument(instrument);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.NEW_INSTRUMENT_MUST_NOT_HAVE_CREATION_AUDIT, e.getLanguageKey());
		}
		instrument.setCreationAudit(null);
		
		try {
			InstrumentValidator.validateNewInstrument(instrument);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.NEW_INSTRUMENT_MUST_NOT_HAVE_CHANGE_AUDIT, e.getLanguageKey());
		}
		instrument.setChangeAudit(null);
		
		try {
			InstrumentValidator.validateNewInstrument(instrument);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.NEW_INSTRUMENT_MUST_NOT_HAVE_ROLLOVER_AUDIT, e.getLanguageKey());
		}
		instrument.setRolloverAudit(null);
		
		try {
			InstrumentValidator.validateNewInstrument(instrument);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.NEW_INSTRUMENT_MUST_NOT_HAVE_APPROVAL_AUDIT, e.getLanguageKey());
		}
		instrument.setApprovalAudit(null);
		
		try {
			InstrumentValidator.validateNewInstrument(instrument);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.NEW_INSTRUMENT_MUST_NOT_HAVE_SUSPENSION_AUDIT, e.getLanguageKey());
		}
		instrument.setSuspensionAudit(null);

		try {
			InstrumentValidator.validateNewInstrument(instrument);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.NEW_INSTRUMENT_MUST_NOT_HAVE_ACTIVATION_DATE, e.getLanguageKey());
		}
		instrument.setActivationDate(null);
		
		try {
			InstrumentValidator.validateNewInstrument(instrument);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.NEW_INSTRUMENT_MUST_NOT_HAVE_ACTIVATION_STATUS, e.getLanguageKey());
		}
		
		instrument.setActivationStatus(ActivationStatus.Approved);
		try {
			InstrumentValidator.validateNewInstrument(instrument);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.NEW_INSTRUMENT_MUST_NOT_HAVE_ACTIVATION_STATUS, e.getLanguageKey());
		}
		instrument.setActivationStatus(null);
		
		instrument.getRollablePropertyNames().add("Test");
		try {
			InstrumentValidator.validateNewInstrument(instrument);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.ROLLABLE_PROPERTY_MUST_BE_VALID_PROPERTY_ON_OBJECT, e.getLanguageKey());
		}
		instrument.addProperty(PropertyUtils.createDoubleProperty("Test", 10.0));
		
		// TODO: Consider, if we need this 
//		try {
//			InstrumentValidator.validateNewInstrument(instrument);
//			fail();
//		} catch (MarketRuntimeException e) {
//			assertEquals(ExceptionLanguageKeys.MARKET_MUST_HAVE_CURRENCY_OR_UNIT, e.getLanguageKey());
//		}		
//		instrument.setCurrencyOrUnit("USD");		
//		
		
		InstrumentValidator.validateNewInstrument(instrument);
		
		// test with created state 
		instrument.setActivationStatus(ActivationStatus.Created);
		InstrumentValidator.validateNewInstrument(instrument);
	}
	
	public void testRolloverValidation() throws Exception {
		Instrument instrument = createInstrument("TEST");
		
		Instrument instrumentRolled = createInstrument("TEST_ROLL");
		
		InstrumentValidator.validateRolloverInstrument(instrumentRolled, instrument);
		
		instrumentRolled.setMasterAgreementDocument(null);
		try {
			InstrumentValidator.validateRolloverInstrument(instrumentRolled, instrument);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.INSTRUMENT_MUST_HAVE_MASTER_AGREEMENT_DOCUMENT, e.getLanguageKey());
		}
		instrumentRolled.setMasterAgreementDocument(instrument.getMasterAgreementDocument());

		instrumentRolled.setType(InstrumentType.Spot);
		instrumentRolled.setUnderlyingType(UnderlyingType.Product);
		try {
			InstrumentValidator.validateRolloverInstrument(instrumentRolled, instrument);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.INSTRUMENT_TYPE_CANNOT_BE_CHANGED_WHILE_ROLLOVER, e.getLanguageKey());
		}
		instrumentRolled.setType(instrument.getType());
		instrumentRolled.setUnderlyingType(instrument.getUnderlyingType());
		
		instrumentRolled.setAssetClassName("Rolled");
		try {
			InstrumentValidator.validateRolloverInstrument(instrumentRolled, instrument);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.INSTRUMENT_ASSET_CLASS_CANNOT_BE_CHANGED_WHILE_ROLLOVER, e.getLanguageKey());
		}
		instrumentRolled.setAssetClassName(instrument.getAssetClassName());
		
		instrumentRolled.setSubType("Rolled");
		try {
			InstrumentValidator.validateRolloverInstrument(instrumentRolled, instrument);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.INSTRUMENT_SUB_TYPE_CANNOT_BE_CHANGED_WHILE_ROLLOVER, e.getLanguageKey());
		}
		instrumentRolled.setSubType(instrument.getSubType());
		
		instrument.setSubType(null);
		instrumentRolled.setSubType("Rolled");
		try {
			InstrumentValidator.validateRolloverInstrument(instrumentRolled, instrument);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.INSTRUMENT_SUB_TYPE_CANNOT_BE_CHANGED_WHILE_ROLLOVER, e.getLanguageKey());
		}
		instrumentRolled.setSubType(instrument.getSubType());
		
		instrumentRolled.setUnderlyingCode("ROLLED");
		
		instrumentRolled.setUnderlyingType(UnderlyingType.Product);
		try {
			InstrumentValidator.validateRolloverInstrument(instrumentRolled, instrument);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.INSTRUMENT_UNDERLYING_TYPE_CANNOT_BE_CHANGED_WHILE_ROLLOVER, e.getLanguageKey());
		}
		instrumentRolled.setUnderlyingType(instrument.getUnderlyingType());
		
		instrumentRolled.setMasterAgreementDocument("Rolled");
		try {
			InstrumentValidator.validateRolloverInstrument(instrumentRolled, instrument);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.INSTRUMENT_MASTER_AGREEMENT_CANNOT_BE_CHANGED_WHILE_ROLLOVER, e.getLanguageKey());
		}
		instrumentRolled.setMasterAgreementDocument(instrument.getMasterAgreementDocument());
		
		instrumentRolled.setDenominationCurrency("Rolled");
		try {
			InstrumentValidator.validateRolloverInstrument(instrumentRolled, instrument);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.INSTRUMENT_DENOMINATION_CURRENCY_CANNOT_BE_CHANGED_WHILE_ROLLOVER, e.getLanguageKey());
		}
		instrumentRolled.setDenominationCurrency(instrument.getDenominationCurrency());
		
		instrumentRolled.setContractSize(instrument.getContractSize() + 1);
		try {
			InstrumentValidator.validateRolloverInstrument(instrumentRolled, instrument);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.INSTRUMENT_CONTRACT_SIZE_CANNOT_BE_CHANGED_WHILE_ROLLOVER, e.getLanguageKey());
		}
		instrumentRolled.setContractSize(instrument.getContractSize());
		
		instrumentRolled.setContractSizeUnit(instrument.getContractSizeUnit() + "Rolled");
		try {
			InstrumentValidator.validateRolloverInstrument(instrumentRolled, instrument);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.INSTRUMENT_CONTRACT_SIZE_UNIT_CANNOT_BE_CHANGED_WHILE_ROLLOVER, e.getLanguageKey());
		}
		instrumentRolled.setContractSizeUnit(instrument.getContractSizeUnit());
		
		
		instrumentRolled.setSettlementType(SettlementType.PhysicalDelivery);
		try {
			InstrumentValidator.validateRolloverInstrument(instrumentRolled, instrument);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.INSTRUMENT_SETTLEMENT_TYPE_CANNOT_BE_CHANGED_WHILE_ROLLOVER, e.getLanguageKey());
		}
		instrumentRolled.setSettlementType(instrument.getSettlementType());
		
		instrumentRolled.setDeliveryPeriod(DeliveryPeriod.T3);
		try {
			InstrumentValidator.validateRolloverInstrument(instrumentRolled, instrument);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.INSTRUMENT_DELIVERY_PERIOD_CANNOT_BE_CHANGED_WHILE_ROLLOVER, e.getLanguageKey());
		}
		instrumentRolled.setDeliveryPeriod(instrument.getDeliveryPeriod());
		
		Address deliveryLocation = instrumentRolled.getDeliveryLocation();
		instrumentRolled.setDeliveryLocation(null);
		try {
			InstrumentValidator.validateRolloverInstrument(instrumentRolled, instrument);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.INSTRUMENT_DELIVERY_LOCATION_CANNOT_BE_CHANGED_WHILE_ROLLOVER, e.getLanguageKey());
		}
		instrumentRolled.setDeliveryLocation(deliveryLocation);
		
		instrumentRolled.getDeliveryLocation().setPostalCode("Rolled");
		try {
			InstrumentValidator.validateRolloverInstrument(instrumentRolled, instrument);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.INSTRUMENT_DELIVERY_LOCATION_CANNOT_BE_CHANGED_WHILE_ROLLOVER, e.getLanguageKey());
		}
		instrumentRolled.getDeliveryLocation().setPostalCode(
				instrument.getDeliveryLocation().getPostalCode());	
		
		instrumentRolled.getDeliveryLocation().setCity("Rolled");
		try {
			InstrumentValidator.validateRolloverInstrument(instrumentRolled, instrument);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.INSTRUMENT_DELIVERY_LOCATION_CANNOT_BE_CHANGED_WHILE_ROLLOVER, e.getLanguageKey());
		}
		instrumentRolled.getDeliveryLocation().setCity(
				instrument.getDeliveryLocation().getCity());	
		
		instrumentRolled.getDeliveryLocation().setCountry("Rolled");
		try {
			InstrumentValidator.validateRolloverInstrument(instrumentRolled, instrument);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.INSTRUMENT_DELIVERY_LOCATION_CANNOT_BE_CHANGED_WHILE_ROLLOVER, e.getLanguageKey());
		}
		instrumentRolled.getDeliveryLocation().setCountry(
				instrument.getDeliveryLocation().getCountry());
		
		instrumentRolled.getDeliveryLocation().setStreet("Rolled");
		try {
			InstrumentValidator.validateRolloverInstrument(instrumentRolled, instrument);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.INSTRUMENT_DELIVERY_LOCATION_CANNOT_BE_CHANGED_WHILE_ROLLOVER, e.getLanguageKey());
		}
		instrumentRolled.getDeliveryLocation().setStreet(
				instrument.getDeliveryLocation().getStreet());
		
		instrumentRolled.getDeliveryLocation().setState(null);
		try {
			InstrumentValidator.validateRolloverInstrument(instrumentRolled, instrument);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.INSTRUMENT_DELIVERY_LOCATION_CANNOT_BE_CHANGED_WHILE_ROLLOVER, e.getLanguageKey());
		}
		instrumentRolled.getDeliveryLocation().setState(
				instrument.getDeliveryLocation().getState());
		

		instrument.getDeliveryLocation().setState(null);
		instrumentRolled.getDeliveryLocation().setState("Rolled");
		try {
			InstrumentValidator.validateRolloverInstrument(instrumentRolled, instrument);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.INSTRUMENT_DELIVERY_LOCATION_CANNOT_BE_CHANGED_WHILE_ROLLOVER, e.getLanguageKey());
		}
		instrumentRolled.getDeliveryLocation().setState(
				instrument.getDeliveryLocation().getState());
		
		InstrumentValidator.validateRolloverInstrument(instrumentRolled, instrument);
		
	}
}
