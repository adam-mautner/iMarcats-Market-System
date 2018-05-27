package com.imarcats.interfaces.client.v100.dto.types;


/**
 * Defines a Time of Day
 * @author Adam
 *
 */
public class TimeOfDayDto implements TransferableObjectDto {

	public static final String TIME_OF_DAY_MUST_HAVE_THE_SAME_TIME_ZONE_TO_BE_COMPARABLE = "TimeOfDay must have the same Time Zone to be Comparable";
	public static final String NO_TIME_ZONE_IS_DEFINED_ON_TIME_OF_DAY = "No Time Zone is defined on Time of Day";
	public static final String SECOND_MUST_BE_0_59 = "Second must be 0 - 59";
	public static final String MINUTE_MUST_BE_0_59 = "Minute must be 0 - 59";
	public static final String HOUR_MUST_BE_0_23 = "Hour must be 0 - 23";
	
	/**
	 * Hour (0 - 23)
	 * Required
	 */
    private int _hour;

	/**
	 * Minute (0 - 59)
	 * Required
	 */
    private int _minute;

	/**
	 * Second (0 - 59)
	 * Required
	 */
    private int _second;

	/**
	 * Time Zone
	 * Required
	 */
//	@Column(name="TIMEZONE", nullable=false, length=DataLengths.TIMEZONE_LENGTH)
    private String _timeZone;
    
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
}
