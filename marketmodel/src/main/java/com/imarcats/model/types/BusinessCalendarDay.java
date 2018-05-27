package com.imarcats.model.types;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.imarcats.model.MarketModelObject;

/**
 * One Day in a Business Calendar 
 * @author Adam
 */
@Entity
@Table(name="BUSINESS_CALENDAR_DAY")
public class BusinessCalendarDay implements MarketModelObject {

	private static final long serialVersionUID = 1L;

	/**
	 * Primary Key of the BusinessCalendarDay
	 * Note: No need for accessing this field directly
	 */	
	@Id
	@GeneratedValue 
	@Column(name="ID")
    private Long _key;
	
	/**
	 * Date String with Date format yyyy/MM/dd, example: 2010/04/24
	 * Required
	 */
	@Column(name="DATE_STRING", nullable=false, length=10)
    private String _dateString;
    
	/**
	 * Day type of this BusinessCalendar Day (like Holiday or BusinessDay)
	 * Required
	 */
    @Column(name="DAY", nullable=false)
    @Enumerated(EnumType.STRING) 
    private Day _day;

    public static BusinessCalendarDay create(BusinessCalendarDay day_) {
    	BusinessCalendarDay newDay = new BusinessCalendarDay();
    	newDay.setDateString(day_.getDateString());
    	newDay.setDay(day_.getDay());
    	
    	return newDay;
    }
    
    public static BusinessCalendarDay create(String dateString_, Day day_) {
    	BusinessCalendarDay newDay = new BusinessCalendarDay();
    	newDay.setDateString(dateString_);
    	newDay.setDay(day_);
    	
    	return newDay;
    }
    
    
	public String getDateString() {
		return _dateString;
	}

	public void setDateString(String dateString_) {
		_dateString = dateString_;
	}

	public Day getDay() {
		return _day;
	}

	public void setDay(Day day_) {
		_day = day_;
	}

	@Override
	public String toString() {
		return "BusinessCalendarDay [_dateString=" + _dateString + ", _day="
				+ _day + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((_dateString == null) ? 0 : _dateString.hashCode());
		result = prime * result + ((_day == null) ? 0 : _day.hashCode());
		result = prime * result + ((_key == null) ? 0 : _key.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BusinessCalendarDay other = (BusinessCalendarDay) obj;
		if (_dateString == null) {
			if (other._dateString != null)
				return false;
		} else if (!_dateString.equals(other._dateString))
			return false;
		if (_day != other._day)
			return false;
		if (_key == null) {
			if (other._key != null)
				return false;
		} else if (!_key.equals(other._key))
			return false;
		return true;
	}
	
}
