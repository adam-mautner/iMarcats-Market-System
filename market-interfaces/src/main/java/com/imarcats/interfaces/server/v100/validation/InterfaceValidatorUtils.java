package com.imarcats.interfaces.server.v100.validation;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.imarcats.interfaces.client.v100.exception.MarketRuntimeException;
import com.imarcats.interfaces.client.v100.exception.MarketSecurityException;
import com.imarcats.interfaces.client.v100.util.DataUtils;
import com.imarcats.model.types.DoubleProperty;
import com.imarcats.model.types.Property;
import com.imarcats.model.types.IntProperty;
import com.imarcats.model.types.PropertyType;
import com.imarcats.model.types.StringListProperty;
import com.imarcats.model.types.StringProperty;
import com.imarcats.model.types.TimeOfDay;
import com.imarcats.model.types.TimePeriod;
import com.imarcats.model.types.TimeProperty;
import com.imarcats.model.types.TimeRangeProperty;
import com.imarcats.model.types.UnitProperty;
import com.imarcats.model.utils.PropertyUtils;

/**
 * Utility Class for Validators 
 * @author Adam
 */
public class InterfaceValidatorUtils extends DataUtils {

	protected InterfaceValidatorUtils() { /* static utility class */ }
	
	@SuppressWarnings("unchecked")
	private static final Map<PropertyType, IPropertyValidator> VALIDATORS = new HashMap<PropertyType, IPropertyValidator>();
	@SuppressWarnings("unchecked")
	private static final IPropertyValidator NULL_VALIDATOR = new IPropertyValidator<Property>() {
		@Override
		public void validate(Property property_, ITimeZoneListProvider timeZoneListProvider_) { /* does nothing */ }
	};
	static {
		VALIDATORS.put(PropertyType.Double, new IPropertyValidator<DoubleProperty>() {
			@Override
			public void validate(DoubleProperty property_, ITimeZoneListProvider timeZoneListProvider_) {
				if(!DataUtils.isValidOptionalString(property_.getUnit())) {
					throw MarketRuntimeException.createExceptionWithDetails(
							MarketRuntimeException.INVALID_PROPERTY_UNIT, 
							null, new Object[] { property_.getName(), DataUtils.VALID_STRINGS });
				}
			}
		});
		VALIDATORS.put(PropertyType.Int, new IPropertyValidator<IntProperty>() {
			@Override
			public void validate(IntProperty property_, ITimeZoneListProvider timeZoneListProvider_) {
				if(!DataUtils.isValidOptionalString(property_.getUnit())) {
					throw MarketRuntimeException.createExceptionWithDetails(
							MarketRuntimeException.INVALID_PROPERTY_UNIT, 
							null, new Object[] { property_.getName(), DataUtils.VALID_STRINGS });
				}
			}
		});
		VALIDATORS.put(PropertyType.String, new IPropertyValidator<StringProperty>() {
			@Override
			public void validate(StringProperty property_, ITimeZoneListProvider timeZoneListProvider_) {
				if(!DataUtils.isValidOptionalString(property_.getValue())) {
					throw MarketRuntimeException.createExceptionWithDetails(
							MarketRuntimeException.INVALID_PROPERTY_VALUE, 
							null, new Object[] { property_.getName(), DataUtils.VALID_STRINGS });
				}
			}
		});
		VALIDATORS.put(PropertyType.StringList, new IPropertyValidator<StringListProperty>() {
			@Override
			public void validate(StringListProperty property_, ITimeZoneListProvider timeZoneListProvider_) {
				for (String value : property_.getList()) {					
					if(!DataUtils.isValidOptionalString(value)) {
						throw MarketRuntimeException.createExceptionWithDetails(
								MarketRuntimeException.INVALID_PROPERTY_VALUE, 
								null, new Object[] { property_.getName(), DataUtils.VALID_STRINGS });
					}
				}
			}
		});
		VALIDATORS.put(PropertyType.Time, new IPropertyValidator<TimeProperty>() {
			@Override
			public void validate(TimeProperty property_, ITimeZoneListProvider timeZoneListProvider_) {
				validateTimeOfDay(property_.getValue(), timeZoneListProvider_);
			}
		});
		VALIDATORS.put(PropertyType.TimeRange, new IPropertyValidator<TimeRangeProperty>() {
			@Override
			public void validate(TimeRangeProperty property_, ITimeZoneListProvider timeZoneListProvider_) {
				validateTimePeriod(property_.getValue(), "Value", timeZoneListProvider_);
			}
		});
		VALIDATORS.put(PropertyType.Unit, new IPropertyValidator<UnitProperty>() {
			@Override
			public void validate(UnitProperty property_, ITimeZoneListProvider timeZoneListProvider_) {
				if(!DataUtils.isValidOptionalString(property_.getUnit())) {
					throw MarketRuntimeException.createExceptionWithDetails(
							MarketRuntimeException.INVALID_PROPERTY_UNIT, 
							null, new Object[] { property_.getName(), DataUtils.VALID_STRINGS });
				}
			}
		});
	}
	
	public static void checkForXssException(IllegalArgumentException e_) {
		if(DataUtils.XSS.equals(e_.getMessage())) {
			throw MarketSecurityException.USER_ATTEMPTED_CROSS_SITE_SCRIPTING;
		}
	}
	
	public static void validateTimePeriod(TimePeriod timePeriod_, String fieldName_, ITimeZoneListProvider timeZoneListProvider_) {
		Object[] relatedObjects = new Object[] { timePeriod_, fieldName_ };
		if(timePeriod_.getStartTime() == null) {
			throw MarketRuntimeException.createExceptionWithDetails(MarketRuntimeException.TIME_PERIOD_MUST_HAVE_START_TIME, 
					null, relatedObjects);				
		}
		validateTimeOfDay(timePeriod_.getStartTime(), timeZoneListProvider_);
		if(timePeriod_.getEndTime() == null) {
			throw MarketRuntimeException.createExceptionWithDetails(MarketRuntimeException.TIME_PERIOD_MUST_HAVE_END_TIME, 
					null, relatedObjects);				
		}
		validateTimeOfDay(timePeriod_.getEndTime(), timeZoneListProvider_);
		

		if(!timePeriod_.getEndTime().getTimeZoneID().equals(timePeriod_.getStartTime().getTimeZoneID())) {
			throw MarketRuntimeException.createExceptionWithDetails(MarketRuntimeException.START_TIME_END_TIME_MUST_HAVE_SAME_TIME_ZONE, 
					null, relatedObjects);
		}
		
		if(timePeriod_.getEndTime().compareTo(timePeriod_.getStartTime()) < 0) {			
			throw MarketRuntimeException.createExceptionWithDetails(MarketRuntimeException.START_TIME_MUST_HAVE_BE_BEFORE_END_TIME, 
				null, relatedObjects);
		}
	}
	
	public static void validateTimeOfDay(TimeOfDay timeOfDay_, ITimeZoneListProvider timeZoneListProvider_) {
		if(timeOfDay_.getHour() > 23 || timeOfDay_.getHour() < 0) {
			throw MarketRuntimeException.createExceptionWithDetails(MarketRuntimeException.TIME_OF_DAY_MUST_HAVE_VALID_HOURS, 
					null, new Object[] { timeOfDay_ });			
		}
		if(timeOfDay_.getMinute() > 59 || timeOfDay_.getMinute() < 0) {
			throw MarketRuntimeException.createExceptionWithDetails(MarketRuntimeException.TIME_OF_DAY_MUST_HAVE_VALID_MINUTES, 
					null, new Object[] { timeOfDay_ });			
		}
		if(timeOfDay_.getSecond() > 59 || timeOfDay_.getSecond() < 0) {
			throw MarketRuntimeException.createExceptionWithDetails(MarketRuntimeException.TIME_OF_DAY_MUST_HAVE_VALID_SECONDS, 
					null, new Object[] { timeOfDay_ });			
		}
		MarketRuntimeException timeZoneException = MarketRuntimeException.createExceptionWithDetails(MarketRuntimeException.TIME_OF_DAY_MUST_HAVE_VALID_TIME_ZONE, 
				null, new Object[] { timeOfDay_ });
		if(!InterfaceValidatorUtils.isValidString(timeOfDay_.getTimeZoneID())) {
			throw timeZoneException;			
		} else {
			boolean found = false;
			for (String id : timeZoneListProvider_.getAvailableIDs()) {
				if(id.equals(timeOfDay_.getTimeZoneID())) {
					found = true;
					break;
				}
			}
			
			if(!found) {
				throw timeZoneException;	
			}
		}
	}
	
	public static void validateRollableProperties(List<String> rollablePropertyNames_, Property[] properties_, ITimeZoneListProvider timeZoneListProvider_) {
		validateProperties(properties_, timeZoneListProvider_);
		for (String rollablePropertyName : rollablePropertyNames_) {
			checkPropertyName(rollablePropertyName);
			if(PropertyUtils.findProperty(rollablePropertyName, properties_) == null) {
				throw MarketRuntimeException.createExceptionWithDetails(MarketRuntimeException.ROLLABLE_PROPERTY_MUST_BE_VALID_PROPERTY_ON_OBJECT, 
						null, new Object[] { rollablePropertyName, properties_ });				
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public static void validateProperties(Property[] properties_, ITimeZoneListProvider timeZoneListProvider_) {
		Set<String> names = new HashSet<String>();
		for (Property property : properties_) {
			checkPropertyName(property.getName());
			getValidator(property.getPropertyType()).validate(property, timeZoneListProvider_);
			
			if(names.contains(property.getName())) {
				throw MarketRuntimeException.createExceptionWithDetails(
						MarketRuntimeException.DUPLICATE_PROPERTY, 
						null, new Object[] { property.getName() });
			}
			
			names.add(property.getName());
		}
	}

	private static void checkPropertyName(String name) {
		if(!DataUtils.isValidString(name)) {
			throw MarketRuntimeException.createExceptionWithDetails(
					MarketRuntimeException.INVALID_PROPERTY_NAME, 
					null, new Object[] { DataUtils.VALID_STRINGS });
		}
	}

	@SuppressWarnings("unchecked")
	private static IPropertyValidator getValidator(PropertyType type_) {
		IPropertyValidator validator = VALIDATORS.get(type_);
		if(validator == null) {
			validator = NULL_VALIDATOR;
		}
		
		return validator;
	}
	
	// interfaces 
	private static interface IPropertyValidator<T extends Property> {
		public void validate(T property_, ITimeZoneListProvider timeZoneListProvider_);
	}
	
	public interface ITimeZoneListProvider {
		public String[] getAvailableIDs();
	}
}
