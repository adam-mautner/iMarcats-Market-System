package com.imarcats.model.mutators;

/**
 * This Exception is thrown, if the Property Change is not supported in the System
 * @author Adam
 */
@SuppressWarnings("serial")
public class UnsupportedPropertyChangeException extends PropertyException {

	/**
	 * Name of the Change Action
	 */
	private final String _changeAction;
	
	public UnsupportedPropertyChangeException(Object object_, String propertyName_, String changeAction_) {
		super(object_, propertyName_);
		_changeAction = changeAction_; 	
	}

	public String getChangeAction() {
		return _changeAction;
	}
}

