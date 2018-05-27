package com.imarcats.model.mutators;

import com.imarcats.model.types.Property;

/**
 * Exception is throw, if the Property Value is not supported on the Object
 * @author Adam
 */
@SuppressWarnings("serial")
public class UnsupportedPropertyValueException extends PropertyException {

	private final Property _property;
	
	public UnsupportedPropertyValueException(Object object_, String propertyName_, Property property_) {
		super(object_, propertyName_);
		_property = property_;
	}

	public Property getProperty() {
		return _property;
	}

}
