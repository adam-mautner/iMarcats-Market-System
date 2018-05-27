package com.imarcats.model.mutators;

import com.imarcats.model.types.Property;

/**
 * Defines a Change in a Property Value 
 * @author Adam
 */
public class PropertyValueChange implements PropertyChange {

	private static final long serialVersionUID = 1L;
	
	/**
	 * Property being Changed
	 */
	private Property _property;

	public void setProperty(Property property) {
		_property = property;
	}

	public Property getProperty() {
		return _property;
	}

	@Override
	public PropertyChange clonePropertyChange() {
		PropertyValueChange newPropertyValueChange = new PropertyValueChange();
		newPropertyValueChange.setProperty(getProperty());
		return newPropertyValueChange;
	}

	@Override
	public String toString() {
		return "PropertyValueChange [_property=" + _property + "]";
	}
	
}
