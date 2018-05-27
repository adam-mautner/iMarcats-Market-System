package com.imarcats.interfaces.client.v100.dto.types;

/**
 * One Day in a Business Calendar 
 * @author Adam
 */
public class BusinessCalendarDayDto {
	
	/**
	 * Date String with Date format yyyy/MM/dd, example: 2010/04/24
	 * Required
	 */
//	@Column(name="DATE_STRING", nullable=false, length=10)
    private String _dateString;
    
	/**
	 * Day type of this BusinessCalendar Day (like Holiday or BusinessDay)
	 * Required
	 */
    private Day _day;
    
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
}
