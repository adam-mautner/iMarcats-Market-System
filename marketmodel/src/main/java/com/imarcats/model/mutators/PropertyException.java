package com.imarcats.model.mutators;

/**
 * Base for all Property Exceptions 
 * @author Adam
 */
@SuppressWarnings("serial")
public abstract class PropertyException extends RuntimeException {
	
	private final String _propertyName;
	private final Object _object;

	public PropertyException(Object object_, String propertyName_) {		
		super(propertyName_);
		_object = object_;
		_propertyName = propertyName_;
	}
	
	// Do NOT use
	@SuppressWarnings("unused")
	private PropertyException() {
		super();
		_propertyName = null;
		_object = null;
	}

	@SuppressWarnings("unused")
	private PropertyException(String propertyName_, Throwable arg1_) {
		super(propertyName_, arg1_);
		_propertyName = propertyName_;
		_object = null;
	}

	@SuppressWarnings("unused")
	private PropertyException(Throwable arg0_) {
		super(arg0_);
		_propertyName = null;
		_object = null;
	} 
	// End of Do NOT use

	public String getPropertyName() {
		return _propertyName;
	}

	public Object getObject() {
		return _object;
	}
}
