package com.imarcats.interfaces.client.v100.dto.types;


/**
 * Defines a Name, String Unit pair
 * @author Adam
 */
public class UnitPropertyDto implements PropertyDto {
	
	/**
	 * Name of the Unit Property
	 * Required
	 */
	// @Column(name="NAME", nullable=false, length=DataLengths.MARKET_OBJECT_PROPERTY_NAME_LENGTH)
	private String _name; 
	
	/**
	 * String Unit of the Property
	 * Required
	 */
	// @Column(name="UNIT", nullable=false, length=DataLengths.UNIT_LENGTH)
	private String _unit;

	public String getName() {
		return _name;
	}

	public void setName(String name_) {
		_name = name_;
	}

	public String getUnit() {
		return _unit;
	}

	public void setUnit(String unit_) {
		_unit = unit_;
	}

	public PropertyType getPropertyType() {
		return PropertyType.Unit;
	} 
}
