package com.imarcats.market.management.validation;

import java.util.List;
import java.util.TimeZone;

import com.imarcats.interfaces.client.v100.exception.MarketRuntimeException;
import com.imarcats.interfaces.client.v100.exception.MarketSecurityException;
import com.imarcats.interfaces.client.v100.util.DataUtils;
import com.imarcats.interfaces.server.v100.validation.InterfaceValidatorUtils;
import com.imarcats.internal.server.interfaces.util.BusinessCalendarUtils;
import com.imarcats.model.CircuitBreaker;
import com.imarcats.model.HaltRule;
import com.imarcats.model.types.Address;
import com.imarcats.model.types.BusinessCalendar;
import com.imarcats.model.types.CorporateInformation;
import com.imarcats.model.types.Property;
import com.imarcats.model.types.TimeOfDay;
import com.imarcats.model.types.TimePeriod;

/**
 * Utility Class for Validators 
 * @author Adam
 */
public class ValidatorUtils extends InterfaceValidatorUtils {

	private static final ITimeZoneListProvider TIME_ZONE_LIST_PROVIDER = new ITimeZoneListProvider() {
		@Override
		public String[] getAvailableIDs() {
			return TimeZone.getAvailableIDs();
		}
	};
	
	protected ValidatorUtils() { /* static utility class */ }

	public static void checkForXssException(IllegalArgumentException e_) {
		if(DataUtils.XSS.equals(e_.getMessage())) {
			throw MarketSecurityException.USER_ATTEMPTED_CROSS_SITE_SCRIPTING;
		}
	}
	
	public static void validateCircuitBreaker(CircuitBreaker circuitBreaker_) {
		if((circuitBreaker_.getHaltRules() == null || circuitBreaker_.getHaltRules().size() == 0) &&
		   (circuitBreaker_.getMaximumQuoteImprovement() <= 0)) {
			throw MarketRuntimeException.createExceptionWithDetails(MarketRuntimeException.CIRCUIT_BREAKER_MUST_HAVE_HALT_RULES_OR_MAX_QUOTE_IMPROVEMENT, 
					null, new Object[] { circuitBreaker_ });		
		}
		if(circuitBreaker_.getMaximumQuoteImprovement() > 0 && circuitBreaker_.getOrderRejectAction() == null) {
			throw MarketRuntimeException.createExceptionWithDetails(MarketRuntimeException.CIRCUIT_BREAKER_ORDER_REJECT_ACTION_MUST_BE_DEFINED_IF_MAX_QUOTE_IMPROVEMENT_DEFINED, 
					null, new Object[] { circuitBreaker_ });
		}
		if(circuitBreaker_.getHaltRules() != null) {
			for (HaltRule haltRule : circuitBreaker_.getHaltRules()) {
				if(haltRule.getQuoteChangeAmount() <= 0) {
					throw MarketRuntimeException.createExceptionWithDetails(MarketRuntimeException.CIRCUIT_BREAKER_HALT_RULE_MUST_HAVE_QUOTE_CHANGE_AMOUNT, 
							null, new Object[] { circuitBreaker_, haltRule });					
				}
				if(haltRule.getChangeType() == null) {
					throw MarketRuntimeException.createExceptionWithDetails(MarketRuntimeException.CIRCUIT_BREAKER_HALT_RULE_MUST_HAVE_QUOTE_CHANGE_TYPE, 
							null, new Object[] { circuitBreaker_, haltRule });	
				}
			}
		}
		
	}
	
	public static void validateBusinessCalendar(BusinessCalendar businessCalendar_) {
		try {
			BusinessCalendarUtils.parseFromCsv(BusinessCalendarUtils.writeToCsv(businessCalendar_));
		} catch (Exception e) {
			throw MarketRuntimeException.createExceptionWithDetails(MarketRuntimeException.INVALID_BUSINESS_CALENDAR, 
					e, new Object[] { businessCalendar_ });	
		}
	}
	
	public static void validateCorporateInformation(CorporateInformation corporateInformation_) {	
		if(!DataUtils.isValidString(corporateInformation_.getName())) {
			throw MarketRuntimeException.createExceptionWithDetails(MarketRuntimeException.CORPORATE_INFORMATION_MUST_HAVE_NAME, 
					null, new Object[] { DataUtils.VALID_STRINGS });
		}
		if(corporateInformation_.getAddress() == null) {
			throw MarketRuntimeException.createExceptionWithDetails(MarketRuntimeException.CORPORATE_INFORMATION_MUST_HAVE_ADDRESS, 
					null, new Object[] { corporateInformation_ });
		}		
		
		validateAddress(corporateInformation_.getAddress());
		
		if(!ValidatorUtils.isValidOptionalString(corporateInformation_.getWebSite())) {
			throw MarketRuntimeException.createExceptionWithDetails(
					MarketRuntimeException.CORPORATE_WEBSITE_MUST_BE_VALID, 
					null, new Object[] {  DataUtils.VALID_STRINGS });
		}
	}
	
	public static void validateAddress(Address address_) {
		if(!isValidString(address_.getCity())) {
			throw MarketRuntimeException.createExceptionWithDetails(MarketRuntimeException.ADDRESS_MUST_HAVE_CITY, 
					null, new Object[] { address_ });
		}
		if(!isValidString(address_.getCountry())) {
			throw MarketRuntimeException.createExceptionWithDetails(MarketRuntimeException.ADDRESS_MUST_HAVE_COUNTRY, 
					null, new Object[] { address_ });
		}
		if(!isValidString(address_.getPostalCode())) {
			throw MarketRuntimeException.createExceptionWithDetails(MarketRuntimeException.ADDRESS_MUST_HAVE_POSTAL_CODE, 
					null, new Object[] { address_ });
		}
		if(!isValidString(address_.getStreet())) {
			throw MarketRuntimeException.createExceptionWithDetails(MarketRuntimeException.ADDRESS_MUST_HAVE_STREET, 
					null, new Object[] { address_ });
		}
	}
	public static void validateTimeOfDay(TimeOfDay timeOfDay_) {
		validateTimeOfDay(timeOfDay_, TIME_ZONE_LIST_PROVIDER);
	}
	public static void validateTimePeriod(TimePeriod timePeriod_, String fieldName_) {
		validateTimePeriod(timePeriod_, fieldName_, TIME_ZONE_LIST_PROVIDER);
	}
	
	public static void validateProperties(Property[] properties_) {
		 validateProperties(properties_, TIME_ZONE_LIST_PROVIDER);
	}
	
	public static void validateRollableProperties(List<String> rollablePropertyNames_, Property[] properties_) {
		validateRollableProperties(rollablePropertyNames_, properties_, TIME_ZONE_LIST_PROVIDER);
	}
}
