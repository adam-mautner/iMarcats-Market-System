package com.imarcats.interfaces.client.v100.util;

import java.util.Date;
import java.util.List;

import com.imarcats.interfaces.client.v100.dto.types.BooleanPropertyDto;
import com.imarcats.interfaces.client.v100.dto.types.DatePeriodDto;
import com.imarcats.interfaces.client.v100.dto.types.DatePropertyDto;
import com.imarcats.interfaces.client.v100.dto.types.DateRangePropertyDto;
import com.imarcats.interfaces.client.v100.dto.types.DoublePropertyDto;
import com.imarcats.interfaces.client.v100.dto.types.PropertyDto;
import com.imarcats.interfaces.client.v100.dto.types.IntPropertyDto;
import com.imarcats.interfaces.client.v100.dto.types.StringListPropertyDto;
import com.imarcats.interfaces.client.v100.dto.types.StringPropertyDto;
import com.imarcats.interfaces.client.v100.dto.types.TimeOfDayDto;
import com.imarcats.interfaces.client.v100.dto.types.TimePeriodDto;
import com.imarcats.interfaces.client.v100.dto.types.TimePropertyDto;
import com.imarcats.interfaces.client.v100.dto.types.TimeRangePropertyDto;
import com.imarcats.interfaces.client.v100.dto.types.UnitPropertyDto;

/**
 * Utilities for Handling Properties 
 * @author Adam
 */
public class PropertyDtoUtils {

	private PropertyDtoUtils() { /* static utility class */ }
	
	/**
	 * Creates Boolean Property
	 * @param name_ Property Name
	 * @param value_ Property Value
	 * @return Property
	 */
	public static PropertyDto createBooleanPropertyDto(String name_, boolean value_) {
		BooleanPropertyDto property = new BooleanPropertyDto();
		property.setName(name_);
		property.setValue(value_);
		
		return property;
	}

	/**
	 * Creates Double Property
	 * @param name_ Property Name
	 * @param value_ Property Value
	 * @return Property
	 */
	public static PropertyDto createDoublePropertyDto(String name_, double value_) {
		DoublePropertyDto property = new DoublePropertyDto();
		property.setName(name_);
		property.setValue(value_);
		
		return property;
	}

	/**
	 * Creates Double Property
	 * @param name_ Property Name
	 * @param value_ Property Value
	 * @param unit_ Property Unit
	 * @return Property
	 */
	public static PropertyDto createDoublePropertyDto(String name_, double value_, String unit_) {
		DoublePropertyDto property = new DoublePropertyDto();
		property.setName(name_);
		property.setValue(value_);
		property.setUnit(unit_);
		
		return property;
	}
	
	/**
	 * Creates Int Property
	 * @param name_ Property Name
	 * @param value_ Property Value
	 * @return Property
	 */
	public static PropertyDto createIntPropertyDto(String name_, long value_) {
		IntPropertyDto property = new IntPropertyDto();
		property.setName(name_);
		property.setValue(value_);
		
		return property;
	}

	/**
	 * Creates Int Property
	 * @param name_ Property Name
	 * @param value_ Property Value
	 * @param unit_ Property Unit
	 * @return Property
	 */
	public static PropertyDto createIntPropertyDto(String name_, long value_, String unit_) {
		IntPropertyDto property = new IntPropertyDto();
		property.setName(name_);
		property.setValue(value_);
		property.setUnit(unit_);
		
		return property;
	}
	
	/**
	 * Creates String Property
	 * @param name_ Property Name
	 * @param value_ Property Value
	 * @return Property
	 */
	public static PropertyDto createStringPropertyDto(String name_, String value_) {
		StringPropertyDto property = new StringPropertyDto();
		property.setName(name_);
		property.setValue(value_);
		
		return property;
	}

	/**
	 * Creates Unit Property
	 * @param name_ Property Name
	 * @param unit_ Property Unit
	 * @return Property
	 */
	public static PropertyDto createUnitPropertyDto(String name_, String unit_) {
		UnitPropertyDto property = new UnitPropertyDto();
		property.setName(name_);
		property.setUnit(unit_);
		
		return property;
	}
	
	/**
	 * Creates String List Property
	 * @param name_ Property Name
	 * @param list_ Property List
	 * @return Property
	 */
	public static PropertyDto createStringListPropertyDto(String name_, List<String> list_) {
		StringListPropertyDto property = new StringListPropertyDto();
		property.setName(name_);
		property.setList(list_);
		 
		return property;
	}
	
	/**
	 * Creates Date Property
	 * @param name_ Property Name
	 * @param value_ Property Value
	 * @return Property
	 */
	public static PropertyDto createDatePropertyDto(String name_, Date value_) {
		DatePropertyDto property = new DatePropertyDto();
		property.setName(name_);
		property.setValue(value_ != null ? new java.sql.Date(value_.getTime()) : null);
		
		return property;
	}
	
	/**
	 * Creates Date Range Property
	 * @param name_ Property Name
	 * @param value_ Property Value
	 * @param outsideTheRange_ Property Value is Outside the Defined Range
	 * @return Property
	 */
	public static PropertyDto createDateRangePropertyDto(String name_, DatePeriodDto value_, boolean outsideTheRange_) {
		DateRangePropertyDto property = new DateRangePropertyDto();
		property.setName(name_);
		property.setValue(value_);
		property.setOutsideTheRange(outsideTheRange_);
		
		return property;
	}
	
	/**
	 * Creates Time Property
	 * @param name_ Property Name
	 * @param value_ Property Value
	 * @return Property
	 */
	public static PropertyDto createTimePropertyDto(String name_, TimeOfDayDto value_) {
		TimePropertyDto property = new TimePropertyDto();
		property.setName(name_);
		property.setValue(value_);
		
		return property;
	}
	
	/**
	 * Creates Time Range Property
	 * @param name_ Property Name
	 * @param value_ Property Value
	 * @param outsideTheRange_ Property Value is Outside the Defined Range
	 * @return Property
	 */
	public static PropertyDto createTimeRangePropertyDto(String name_, TimePeriodDto value_, boolean outsideTheRange_) {
		TimeRangePropertyDto property = new TimeRangePropertyDto();
		property.setName(name_);
		property.setValue(value_);
		property.setOutsideTheRange(outsideTheRange_);
		
		return property;
	}
	
	/**
	 * Find Property on List 
	 * @param name_ Name of the Property
	 * @param properties_ Property List 
	 * @return Property
	 */
	public static PropertyDto findPropertyDto(String name_, PropertyDto[] properties_) {
		PropertyDto propertyFound = null; 
		
		for (PropertyDto property : properties_) {
			if(property.getName().equals(name_)) {
				propertyFound = property;
				break;
			}
		}
		
		return propertyFound;
	}
}
