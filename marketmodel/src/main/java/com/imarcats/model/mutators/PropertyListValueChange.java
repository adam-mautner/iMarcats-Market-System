package com.imarcats.model.mutators;

import com.imarcats.model.types.Property;


/**
 * Defines a Change on a Property List
 * @author Adam
 */
public class PropertyListValueChange implements PropertyChange {

	private static final long serialVersionUID = 1L;

	/**
	 * Change Action (Value from ChangeAction)
	 */
	private ChangeAction _changeAction;
	
	/**
	 * Property List Name 
	 */
	private String _propertyListName; 
	
	/**
	 * Property being changed 
	 */
	private Property _property;

	public ChangeAction getChangeAction() {
		return _changeAction;
	}

	public void setChangeAction(ChangeAction changeAction_) {
		_changeAction = changeAction_;
	}

	public String getPropertyListName() {
		return _propertyListName;
	}

	public void setPropertyListName(String propertyListName_) {
		_propertyListName = propertyListName_;
	}

	public Property getProperty() {
		return _property;
	}

	public void setProperty(Property property_) {
		_property = property_;
	}

	@Override
	public PropertyChange clonePropertyChange() {
		PropertyListValueChange newPropertyListValueChange = new PropertyListValueChange();
		newPropertyListValueChange.setChangeAction(getChangeAction());
		newPropertyListValueChange.setProperty(getProperty());
		newPropertyListValueChange.setPropertyListName(getPropertyListName());
		return newPropertyListValueChange;
	}
}
