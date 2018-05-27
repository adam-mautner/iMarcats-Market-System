package com.imarcats.interfaces.client.v100.dto.types;


/**
 * Defines a Name, Boolean Value pair
 * @author Adam
 */
public class BooleanPropertyDto implements PropertyDto {
	
	/**
	 * Name of the Property
	 * Required
	 */
//	@Column(name="NAME", nullable=false, length=DataLengths.MARKET_OBJECT_PROPERTY_NAME_LENGTH)
	private String _name; 
	
	/**
	 * Boolean Value of the Property
	 * Required
	 */
	private boolean _value;

	public String getName() {
		return _name;
	}

	public void setName(String name_) {
		_name = name_;
	}

	public boolean getValue() {
		return _value;
	}

	public void setValue(boolean value_) {
		_value = value_;
	}

	public PropertyType getPropertyType() {
		return PropertyType.Boolean;
	}
}

