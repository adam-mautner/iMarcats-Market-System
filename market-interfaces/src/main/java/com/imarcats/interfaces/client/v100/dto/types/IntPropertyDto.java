package com.imarcats.interfaces.client.v100.dto.types;


/**
 * Defines a Name, Long Value pair
 * @author Adam
 */
public class IntPropertyDto implements PropertyDto {
	
	/**
	 * Name of the Property
	 * Required
	 */
//	@Column(name="NAME", nullable=false, length=DataLengths.MARKET_OBJECT_PROPERTY_NAME_LENGTH)
	private String _name; 
	
	/**
	 * Int Value of the Property
	 * Required
	 */
	private long _value;

	/**
	 * Unit of the Property
	 * Optional
	 */
//	@Column(name = "UNIT", length=DataLengths.UNIT_LENGTH)
	private String _unit; 
	
	public String getName() {
		return _name;
	}

	public void setName(String name_) {
		_name = name_;
	}

	public long getValue() {
		return _value;
	}

	public void setValue(long value_) {
		_value = value_;
	}

	public PropertyType getPropertyType() {
		return PropertyType.Int;
	}	

	public void setUnit(String unit) {
		_unit = unit;
	}

	public String getUnit() {
		return _unit;
	}	
}
