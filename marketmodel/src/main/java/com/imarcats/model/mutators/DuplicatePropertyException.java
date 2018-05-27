package com.imarcats.model.mutators;

import com.imarcats.model.types.Property;

/**
 * Exception is throw, if the Property has already been added to the Object
 * @author Adam
 */
@SuppressWarnings("serial")
public class DuplicatePropertyException extends PropertyException {

	private final Property _property;
	
	public DuplicatePropertyException(Object object_, String propertyName_, Property property_) {
		super(object_, propertyName_);
		_property = property_;
	}

	public Property getProperty() {
		return _property;
	}
}
