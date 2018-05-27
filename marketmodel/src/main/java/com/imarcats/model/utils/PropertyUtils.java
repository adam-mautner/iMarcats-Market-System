package com.imarcats.model.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.imarcats.model.mutators.ChangeAction;
import com.imarcats.model.mutators.PropertyChange;
import com.imarcats.model.mutators.PropertyListValueChange;
import com.imarcats.model.mutators.PropertyValueChange;
import com.imarcats.model.types.BooleanProperty;
import com.imarcats.model.types.DatePeriod;
import com.imarcats.model.types.DateProperty;
import com.imarcats.model.types.DateRangeProperty;
import com.imarcats.model.types.DoubleProperty;
import com.imarcats.model.types.Property;
import com.imarcats.model.types.TransferableObject;
import com.imarcats.model.types.IntProperty;
import com.imarcats.model.types.ObjectProperty;
import com.imarcats.model.types.StringListProperty;
import com.imarcats.model.types.StringProperty;
import com.imarcats.model.types.TimeOfDay;
import com.imarcats.model.types.TimePeriod;
import com.imarcats.model.types.TimeProperty;
import com.imarcats.model.types.TimeRangeProperty;
import com.imarcats.model.types.UnitProperty;

/**
 * Utilities for Handling Properties 
 * @author Adam
 */
public class PropertyUtils {

	private PropertyUtils() { /* static utility class */ }
	
	/**
	 * Creates Boolean Property
	 * @param name_ Property Name
	 * @param value_ Property Value
	 * @return Property
	 */
	public static Property createBooleanProperty(String name_, boolean value_) {
		BooleanProperty property = new BooleanProperty();
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
	public static Property createDoubleProperty(String name_, double value_) {
		DoubleProperty property = new DoubleProperty();
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
	public static Property createDoubleProperty(String name_, double value_, String unit_) {
		DoubleProperty property = new DoubleProperty();
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
	public static Property createIntProperty(String name_, long value_) {
		IntProperty property = new IntProperty();
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
	public static Property createIntProperty(String name_, long value_, String unit_) {
		IntProperty property = new IntProperty();
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
	public static Property createStringProperty(String name_, String value_) {
		StringProperty property = new StringProperty();
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
	public static Property createUnitProperty(String name_, String unit_) {
		UnitProperty property = new UnitProperty();
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
	public static Property createStringListProperty(String name_, List<String> list_) {
		StringListProperty property = new StringListProperty();
		property.setName(name_);
		property.setList(list_);
		 
		return property;
	}
	
	/**
	 * Creates String List Property
	 * @param name_ Property Name
	 * @param list_ Property List
	 * @return Property
	 */
	public static Property createStringListProperty(String name_, String[] array_) {
		StringListProperty property = new StringListProperty();
		property.setName(name_);
		List<String> list = new ArrayList<String>();
		for (String string : array_) {
			list.add(string);
		}
		property.setList(list);
		 
		return property;
	}
	
	/**
	 * Creates Date Range Property
	 * @param name_ Property Name
	 * @param value_ Property Value
	 * @param outsideTheRange_ Property Value is Outside the Defined Range
	 * @return Property
	 */
	public static Property createDateRangeProperty(String name_, DatePeriod value_, boolean outsideTheRange_) {
		DateRangeProperty property = new DateRangeProperty();
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
	public static Property createTimeProperty(String name_, TimeOfDay value_) {
		TimeProperty property = new TimeProperty();
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
	public static Property createTimeRangeProperty(String name_, TimePeriod value_, boolean outsideTheRange_) {
		TimeRangeProperty property = new TimeRangeProperty();
		property.setName(name_);
		property.setValue(value_);
		property.setOutsideTheRange(outsideTheRange_);
		
		return property;
	}
	
	/**
	 * Creates Object Property
	 * @param name_ Property Name
	 * @param value_ Property Value
	 * @return Property
	 */
	public static Property createObjectProperty(String name_, TransferableObject value_) {
		ObjectProperty property = new ObjectProperty();
		property.setName(name_);
		property.setValue(value_);
		
		return property;
	}

	/**
	 * Creates Date Property
	 * @param name_ Property Name
	 * @param value_ Property Value
	 * @return Property
	 */
	public static Property createDateProperty(String name_, Date value_) {
		DateProperty property = new DateProperty();
		property.setName(name_);
		property.setValue(value_ != null ? new java.sql.Date(value_.getTime()) : null);
		
		return property;
	}
	
	/**
	 * Creates a Property List Value Change 
	 * @param change_ Property Change on List 
	 * @param property_ Property Value 
	 * @param listName_ List Name 
	 * @return Property Change 
	 */
	public static PropertyChange createValueListChange(ChangeAction change_, Property property_, String listName_) {
		PropertyListValueChange listChange = new PropertyListValueChange();
		listChange.setChangeAction(change_);
		listChange.setProperty(property_);
		listChange.setPropertyListName(listName_);
		
		return listChange;
	}
	
	/**
	 * Creates a Property Change 
	 * @param property_ Property being Changed
	 * @return Property Change 
	 */
	public static PropertyChange createPropertyChange(Property property_) {
		PropertyValueChange valueChange = new PropertyValueChange();
		valueChange.setProperty(property_);
		
		return valueChange;
	}
	
	/**
	 * Create a Property Change for Int Property
	 * @param property_ Property to be changed
	 * @return List of Changes with a single change on it
	 */
	public static PropertyChange[] createPropertyChange(String propertyName_, long propertyValue_) {
		return createPropertyChangeList(createIntProperty(propertyName_, propertyValue_));
	}

	/**
	 * Create a Property Change for Boolean Property
	 * @param property_ Property to be changed
	 * @return List of Changes with a single change on it
	 */
	public static PropertyChange[] createPropertyChange(String propertyName_, boolean propertyValue_) {
		return createPropertyChangeList(createBooleanProperty(propertyName_, propertyValue_));
	}

	/**
	 * Create a Property Change for String Property
	 * @param property_ Property to be changed
	 * @return List of Changes with a single change on it
	 */
	public static PropertyChange[] createPropertyChange(String propertyName_, String propertyValue_) {
		return createPropertyChangeList(createStringProperty(propertyName_, propertyValue_));
	}
	

	/**
	 * Create a Property Change for Object Property
	 * @param property_ Property to be changed
	 * @return List of Changes with a single change on it
	 */
	public static PropertyChange[] createPropertyChange(String propertyName_, TransferableObject propertyValue_) {
		return createPropertyChangeList(createObjectProperty(propertyName_, propertyValue_));
	}

	/**
	 * Create a Property Change for Double Property
	 * @param property_ Property to be changed
	 * @return List of Changes with a single change on it
	 */
	public static PropertyChange[] createPropertyChange(String propertyName_, double propertyValue_) {
		return createPropertyChangeList(createDoubleProperty(propertyName_, propertyValue_));
	}
	
	/**
	 * Creates a Property Change for Property and adds it to a Change List 
	 * @param property_ Property to be changed
	 * @return List of Changes with a single change on it
	 */
	public static PropertyChange[] createPropertyChangeList(Property property_) {
		PropertyValueChange change = new PropertyValueChange();
		change.setProperty(property_);
		
		PropertyChange[] propertyChanges = { change };
		return propertyChanges;
	}

	
	/**
	 * Deep Clones Property List
	 * @param properties_ Properties 
	 * @return Array of Cloned Properties
	 */
	public static Property[] clonePropertyList(Property[] properties_) {
		Property[] newProperties = new Property[properties_.length];
		for (int i = 0; i < newProperties.length; i++) {
			newProperties[i] = properties_[i] != null ? properties_[i].cloneProperty() : null;
		}
		
		return newProperties;
	}
	
	/**
	 * Finds Property on the List 
	 * @param properties_ List 
	 * @param property_ Property
	 * @return Property from the List
	 */
	public static Property findProperty(Property[] properties_, Property property_) {
		return findProperty(property_.getName(), properties_);
	}
	
	/**
	 * Find Property on List 
	 * @param name_ Name of the Property
	 * @param properties_ Property List 
	 * @return Property
	 */
	public static Property findProperty(String name_, Property[] properties_) {
		Property propertyFound = null; 
		
		for (Property property : properties_) {
			if(property.getName().equals(name_)) {
				propertyFound = property;
				break;
			}
		}
		
		return propertyFound;
	}
	
	/**
	 * Finds a Double Property with a given Name 
	 * @param properties_ List of Properties
	 * @param propertyName_ Name of the Property
	 * @param exceptionIfNotFound_ Exception thrown if not found
	 * @return Double value of the Property
	 */
	public static double getDoublePropertyValue(Property[] properties_, String propertyName_, RuntimeException exceptionIfNotFound_) {
		Property foundProperty = com.imarcats.model.utils.PropertyUtils.findProperty(propertyName_, properties_);
		
		if(foundProperty == null) {
			throw exceptionIfNotFound_;
		}
		
		if(!(foundProperty instanceof DoubleProperty)) {
			throw exceptionIfNotFound_;
		}
		
		return ((DoubleProperty) foundProperty).getValue();
	}
	
	/**
	 * Copies Properties from List to Array
	 * @param propertyList_ Property List
	 * @return Property Array
	 */
	private static Property[] toPropertyArray(List<Property> propertyList_) {
		return (Property[]) propertyList_.toArray(new Property[propertyList_.size()]);
	}
	
	/**
	 * Finds a Double Property with a given Name 
	 * @param properties_ List of Properties
	 * @param propertyName_ Name of the Property
	 * @param exceptionIfNotFound_ Exception thrown if not found
	 * @return Double value of the Property
	 */
	public static double getDoublePropertyValue(List<Property> propertyList_, String propertyName_, RuntimeException exceptionIfNotFound_) {
		return getDoublePropertyValue(toPropertyArray(propertyList_), propertyName_, exceptionIfNotFound_);
	}
}
