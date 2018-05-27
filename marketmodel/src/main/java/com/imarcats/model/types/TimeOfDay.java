package com.imarcats.model.types;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import com.imarcats.model.MarketModelObject;
import com.imarcats.model.meta.DataLengths;

/**
 * Defines a Time of Day
 * @author Adam
 *
 */
@Embeddable
public class TimeOfDay implements MarketModelObject, Comparable<TimeOfDay>, TransferableObject {

	public static final String TIME_OF_DAY_MUST_HAVE_THE_SAME_TIME_ZONE_TO_BE_COMPARABLE = "TimeOfDay must have the same Time Zone to be Comparable";
	public static final String NO_TIME_ZONE_IS_DEFINED_ON_TIME_OF_DAY = "No Time Zone is defined on Time of Day";
	public static final String SECOND_MUST_BE_0_59 = "Second must be 0 - 59";
	public static final String MINUTE_MUST_BE_0_59 = "Minute must be 0 - 59";
	public static final String HOUR_MUST_BE_0_23 = "Hour must be 0 - 23";

	private static final long serialVersionUID = 1L;
	
	/**
	 * Hour (0 - 23)
	 * Required
	 */
	@Column(name="HOUR", nullable=false)
    private int _hour;

	/**
	 * Minute (0 - 59)
	 * Required
	 */
	@Column(name="MINUTE", nullable=false)
    private int _minute;

	/**
	 * Second (0 - 59)
	 * Required
	 */
	@Column(name="SECOND", nullable=false)
    private int _second;

	/**
	 * Time Zone
	 * Required
	 */
	@Column(name="TIMEZONE", nullable=false, length=DataLengths.TIMEZONE_LENGTH)
    private String _timeZone;
    
	public static TimeOfDay create(TimeOfDay timeOfDay_) {
		TimeOfDay newTimeOfDay = new TimeOfDay();

		newTimeOfDay.setHour(timeOfDay_.getHour());
		newTimeOfDay.setMinute(timeOfDay_.getMinute());
		newTimeOfDay.setSecond(timeOfDay_.getSecond());
		newTimeOfDay.setTimeZoneID(timeOfDay_.getTimeZoneID());
		
		return newTimeOfDay;
	}


	public int getHour() {
		return _hour;
	}

	public void setHour(int hour_) {
		if(hour_ > 23 || hour_ < 0) {
			throw new IllegalArgumentException(HOUR_MUST_BE_0_23);
		}
		_hour = hour_;
	}

	public int getMinute() {
		return _minute;
	}

	public void setMinute(int minute_) {
		if(minute_ > 59 || minute_ < 0) {
			throw new IllegalArgumentException(MINUTE_MUST_BE_0_59);
		}
		_minute = minute_;
	}

	public int getSecond() {
		return _second;
	}

	public void setSecond(int second_) {
		if(second_ > 59 || second_ < 0) {
			throw new IllegalArgumentException(SECOND_MUST_BE_0_59);
		}
		_second = second_;
	}

	public String getTimeZoneID() {
		return _timeZone;
	}

	public void setTimeZoneID(String timeZoneID_) {
		_timeZone = timeZoneID_;
	}

	@Override
	public Object getObjectValue() {
		return this;
	}
	
	@Override
	public int compareTo(TimeOfDay other_) {
		if(getTimeZoneID() == null) {
			throw new IllegalStateException(NO_TIME_ZONE_IS_DEFINED_ON_TIME_OF_DAY);
		}
		
		if(!getTimeZoneID().equals(other_.getTimeZoneID())) {
			throw new IllegalArgumentException(TIME_OF_DAY_MUST_HAVE_THE_SAME_TIME_ZONE_TO_BE_COMPARABLE);
		}
		
		int compareTo = getHour() - other_.getHour();
		if(compareTo == 0) {
			compareTo = getMinute() - other_.getMinute();
		}
		
		if(compareTo == 0) {
			compareTo = getSecond() - other_.getSecond();
		}
		
		return compareTo;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + _hour;
		result = prime * result + _minute;
		result = prime * result + _second;
		result = prime * result
				+ ((_timeZone == null) ? 0 : _timeZone.hashCode());
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
		TimeOfDay other = (TimeOfDay) obj;
		if (_hour != other._hour)
			return false;
		if (_minute != other._minute)
			return false;
		if (_second != other._second)
			return false;
		if (_timeZone == null) {
			if (other._timeZone != null)
				return false;
		} else if (!_timeZone.equals(other._timeZone))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "TimeOfDay [_hour=" + _hour + ", _minute=" + _minute
				+ ", _second=" + _second + ", _timeZone=" + _timeZone + "]";
	}

}
