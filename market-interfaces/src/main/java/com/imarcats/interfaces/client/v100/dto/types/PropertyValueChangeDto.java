package com.imarcats.interfaces.client.v100.dto.types;


/**
 * Defines a Change in a Property Value 
 * @author Adam
 */
public class PropertyValueChangeDto implements PropertyChangeDto {

	private static final long serialVersionUID = 1L;
	
	/**
	 * Property being Changed
	 */
	private PropertyDto _property;

	public void setProperty(PropertyDto property) {
		_property = property;
	}

	public PropertyDto getProperty() {
		return _property;
	}

	@Override
	public PropertyChangeDto clonePropertyChange() {
		PropertyValueChangeDto newPropertyValueChange = new PropertyValueChangeDto();
		newPropertyValueChange.setProperty(getProperty());
		return newPropertyValueChange;
	}

	@Override
	public String toString() {
		return "PropertyValueChange [_property=" + _property + "]";
	}
	
}
