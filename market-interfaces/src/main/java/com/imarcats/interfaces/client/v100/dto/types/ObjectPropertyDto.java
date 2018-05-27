package com.imarcats.interfaces.client.v100.dto.types;


/**
 * Defines a Name, Object Value pair
 * @author Adam
 */
public class ObjectPropertyDto implements PropertyDto {
	
	/**
	 * Name of the Property
	 * Required
	 */
//	@Column(name="NAME", nullable=false, length=DataLengths.MARKET_OBJECT_PROPERTY_NAME_LENGTH)
	private String _name; 
	
	/**
	 * Object Value of the Property
	 * Required
	 */
	private TransferableObjectDto _value;

	public String getName() {
		return _name;
	}

	public void setName(String name_) {
		_name = name_;
	}

	public TransferableObjectDto getValue() {
		return _value;
	}

	public void setValue(TransferableObjectDto value_) {
		_value = value_;
	}

	public PropertyType getPropertyType() {
		return PropertyType.Object;
	}
}