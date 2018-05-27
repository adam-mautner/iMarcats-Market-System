package com.imarcats.interfaces.client.v100.dto.types;


/**
 * Defines a Name, String Value pair
 * @author Adam
 */
public class StringPropertyDto implements PropertyDto {
	
	/**
	 * Name of the Property
	 * Required
	 */
//	@Column(name="NAME", nullable=false, length=DataLengths.MARKET_OBJECT_PROPERTY_NAME_LENGTH)
	private String _name; 
	
	/**
	 * String Value of the Property
	 * Required
	 */
//	@Column(name="VALUE", nullable=false, length=150)
	private String _value;

	public String getName() {
		return _name;
	}

	public void setName(String name_) {
		_name = name_;
	}

	public String getValue() {
		return _value;
	}

	public void setValue(String value_) {
		_value = value_;
	}

	public PropertyType getPropertyType() {
		return PropertyType.String;
	} 
}
