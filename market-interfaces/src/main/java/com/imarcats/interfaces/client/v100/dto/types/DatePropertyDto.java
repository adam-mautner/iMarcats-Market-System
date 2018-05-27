package com.imarcats.interfaces.client.v100.dto.types;

import java.sql.Timestamp;
import java.util.Date;

/**
 * Defines a Name, Date Value pair
 * @author Adam
 */
public class DatePropertyDto implements PropertyDto {

	/**
	 * Name of the Property
	 * Required
	 */
//	@Column(name="NAME", nullable=false, length=DataLengths.MARKET_OBJECT_PROPERTY_NAME_LENGTH)
	private String _name; 
	
	/**
	 * Date Value of the Property
	 * Required
	 */ 
	// TODO: Convert this to java.sql.Date once TimestampPropery DTO created for pushing updates on submission date
	private Timestamp _value;

	public String getName() {
		return _name;
	}

	public void setName(String name_) {
		_name = name_;
	}

	public Date getValue() {
		return _value;
	}

	public void setValue(Date value_) {
		_value = new Timestamp(value_.getTime());
	}

	public PropertyType getPropertyType() {
		return PropertyType.Date;
	}	
}
