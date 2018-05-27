package com.imarcats.interfaces.client.v100.dto.types;



/**
 * Defines a Change on a Property List
 * @author Adam
 */
public class PropertyListValueChangeDto implements PropertyChangeDto {

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
	private PropertyDto _property;

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

	public PropertyDto getProperty() {
		return _property;
	}

	public void setProperty(PropertyDto property_) {
		_property = property_;
	}

	@Override
	public PropertyChangeDto clonePropertyChange() {
		PropertyListValueChangeDto newPropertyListValueChange = new PropertyListValueChangeDto();
		newPropertyListValueChange.setChangeAction(getChangeAction());
		newPropertyListValueChange.setProperty(getProperty());
		newPropertyListValueChange.setPropertyListName(getPropertyListName());
		return newPropertyListValueChange;
	}
}
