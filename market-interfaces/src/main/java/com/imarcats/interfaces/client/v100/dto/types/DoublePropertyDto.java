package com.imarcats.interfaces.client.v100.dto.types;


/**
 * Defines a Name, Double Value pair
 * 
 * @author Adam
 */
public class DoublePropertyDto implements PropertyDto {

	/**
	 * Name of the Property Required
	 */
//	@Column(name = "NAME", nullable = false, length=DataLengths.MARKET_OBJECT_PROPERTY_NAME_LENGTH)
	private String _name;

	/**
	 * Double Value of the Property Required
	 */
	private Double _value;

	/**
	 * Unit of the Property Optional
	 */
//	@Column(name = "UNIT", length=50)
	private String _unit;

	public String getName() {
		return _name;
	}

	public void setName(String name_) {
		_name = name_;
	}

	public Double getValue() {
		return _value;
	}

	public void setValue(Double value_) {
		_value = value_;
	}

	public PropertyType getPropertyType() {
		return PropertyType.Double;
	}

	public void setUnit(String unit) {
		_unit = unit;
	}

	public String getUnit() {
		return _unit;
	}
}
