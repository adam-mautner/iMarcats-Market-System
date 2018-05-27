package com.imarcats.interfaces.client.v100.dto.types;


/**
 * Defines a Name, Time Value pair
 * @author Adam
 */
public class TimePropertyDto implements PropertyDto {
	
	/**
	 * Name of the Property
	 * Required
	 */
//	@Column(name="NAME", nullable=false, length=DataLengths.MARKET_OBJECT_PROPERTY_NAME_LENGTH)
	private String _name; 
	
	/**
	 * Time Value of the Property
	 * Required
	 */
	private TimeOfDayDto _value;

	public String getName() {
		return _name;
	}

	public void setName(String name_) {
		_name = name_;
	}

	public TimeOfDayDto getValue() {
		return _value;
	}

	public void setValue(TimeOfDayDto value_) {
		_value = value_;
	}

	public PropertyType getPropertyType() {
		return PropertyType.Time;
	}	
}
