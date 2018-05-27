package com.imarcats.market.management.validation;

import java.util.ArrayList;
import java.util.List;

import com.imarcats.interfaces.client.v100.exception.ExceptionLanguageKeys;
import com.imarcats.interfaces.client.v100.exception.MarketRuntimeException;
import com.imarcats.interfaces.client.v100.util.DataUtils;
import com.imarcats.market.management.ManagementTestBase;
import com.imarcats.model.types.DoubleProperty;
import com.imarcats.model.types.IntProperty;
import com.imarcats.model.types.Property;
import com.imarcats.model.types.StringListProperty;
import com.imarcats.model.types.StringProperty;
import com.imarcats.model.types.UnitProperty;
import com.imarcats.model.utils.PropertyUtils;

public class ValidatorTestCaseBase extends ManagementTestBase {

	private static final String INT1_PROPERTY = "int1";

	protected void testRollableProperties(RollablePropertyAccessor properties_) {
		properties_.getRollablePropertyNames().clear();
		testProperties(properties_);
		
		// test invalid name 
		List<String> list = new ArrayList<String>();
		list.add("Inv()");
		properties_.setRollablePropertyNames(list);
		try {
			properties_.validate();
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.INVALID_PROPERTY_NAME, e.getLanguageKey());
			// expected 
		}
		
		// test non-existent property 
		list = new ArrayList<String>();
		list.add("val");
		properties_.setRollablePropertyNames(list);
		try {
			properties_.validate();
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.ROLLABLE_PROPERTY_MUST_BE_VALID_PROPERTY_ON_OBJECT, e.getLanguageKey());
			// expected 
		}
		
		// successful validation 
		list = new ArrayList<String>();
		list.add(INT1_PROPERTY);
		properties_.setRollablePropertyNames(list);
		properties_.validate();
	}
	
	protected void testProperties(PropertyAccessor properties_) {
		// test duplicate properties 
		properties_.clearProperties();
		
		String dupeName = "TestDupe";
		properties_.addProperty(PropertyUtils.createBooleanProperty(dupeName, true));
		properties_.addProperty(PropertyUtils.createDoubleProperty(dupeName, 10));
		
		try {
			properties_.validate();
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.DUPLICATE_PROPERTY, e.getLanguageKey());
			// expected 
		}
		
		// test invalid name 
		properties_.clearProperties();
		String invalidName = "Test()";
		properties_.addProperty(PropertyUtils.createBooleanProperty(invalidName, true));
		
		try {
			properties_.validate();
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.INVALID_PROPERTY_NAME, e.getLanguageKey());
			// expected 
		}
		
		// test invalid values 
		properties_.clearProperties();
		String name = "Test Test" + DataUtils.VALID_STRINGS;
		
		DoubleProperty doubleProperty = (DoubleProperty)PropertyUtils.createDoubleProperty(name, 10, "()");
		properties_.addProperty(doubleProperty);
		
		try {
			properties_.validate();
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.INVALID_PROPERTY_UNIT, e.getLanguageKey());
			// expected 
		}
		doubleProperty.setUnit(DataUtils.VALID_STRINGS);
		
		IntProperty intProperty = (IntProperty)PropertyUtils.createIntProperty(INT1_PROPERTY, 10, "()");
		properties_.addProperty(intProperty);
		
		try {
			properties_.validate();
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.INVALID_PROPERTY_UNIT, e.getLanguageKey());
			// expected 
		}
		intProperty.setUnit(DataUtils.VALID_STRINGS);
		
		StringProperty stringProperty = (StringProperty)PropertyUtils.createStringProperty("str1", "()");
		properties_.addProperty(stringProperty);
		
		try {
			properties_.validate();
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.INVALID_PROPERTY_VALUE, e.getLanguageKey());
			// expected 
		}
		stringProperty.setValue(DataUtils.VALID_STRINGS);
		
		List<String> list = new ArrayList<String>();
		list.add("Valid");
		list.add("Inv()");
		StringListProperty stringListProperty = (StringListProperty)PropertyUtils.createStringListProperty("strL1", list);
		properties_.addProperty(stringListProperty);
		
		try {
			properties_.validate();
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.INVALID_PROPERTY_VALUE, e.getLanguageKey());
			// expected 
		}
		List<String> listFixed = new ArrayList<String>();
		listFixed.add("Valid");
		listFixed.add(DataUtils.VALID_STRINGS);
		stringListProperty.setList(listFixed);
		
		UnitProperty unitProperty = (UnitProperty)PropertyUtils.createUnitProperty("un1", "()");
		properties_.addProperty(unitProperty);
		
		try {
			properties_.validate();
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.INVALID_PROPERTY_UNIT, e.getLanguageKey());
			// expected 
		}
		unitProperty.setUnit(DataUtils.VALID_STRINGS);
		
		// successful validation
		properties_.validate();
	}
	
	// interfaces
	protected interface PropertyAccessor {
		public Property[] getProperties();
		public void addProperty(Property property_);
		public void deleteProperty(Property property_);
		public void clearProperties();
		
		public void validate();
	}
	
	protected interface RollablePropertyAccessor extends PropertyAccessor {
		public void setRollablePropertyNames(List<String> rollablePropertyNames_);
		public List<String> getRollablePropertyNames();
	}
}
