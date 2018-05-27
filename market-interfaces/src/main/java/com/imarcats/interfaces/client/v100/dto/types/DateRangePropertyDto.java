package com.imarcats.interfaces.client.v100.dto.types;



/**
 * Defines a Name, Date Range Value pair
 * @author Adam
 */
public class DateRangePropertyDto implements PropertyDto {
	
	/**
	 * Name of the Property
	 * Required
	 */
//	@Column(name="NAME", nullable=false, length=DataLengths.MARKET_OBJECT_PROPERTY_NAME_LENGTH)
	private String _name; 
	
	/**
	 * Date Range Value of the Property
	 * Required
	 */
	private DatePeriodDto _value;


	/**
	 * Tells, if we mean the Inverse Range (Outside the given Range)
	 * Optional
	 */
	private boolean _outsideTheRange;

	public String getName() {
		return _name;
	}

	public void setName(String name_) {
		_name = name_;
	}

	public DatePeriodDto getValue() {
		return _value;
	}

	public void setValue(DatePeriodDto value_) {
		_value = value_;
	}

	public boolean getOutsideTheRange() {
		return _outsideTheRange;
	}

	public void setOutsideTheRange(boolean outsideTheRange_) {
		_outsideTheRange = outsideTheRange_;
	}
	
	public PropertyType getPropertyType() {
		return PropertyType.DateRange;
	}	
}
