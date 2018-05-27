package com.imarcats.model.mutators;

/**
 * Exception is throw, if the Property Name is not supported on the Object
 * @author Adam
 */
@SuppressWarnings("serial")
public class UnsupportedPropertyException extends PropertyException {

	public UnsupportedPropertyException(Object object_, String propertyName_) {
		super(object_, propertyName_);
	}

}
