package com.imarcats.internal.server.infrastructure.timer;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import com.imarcats.internal.server.interfaces.util.BusinessCalendarUtils;
import com.imarcats.model.types.BusinessCalendar;
import com.imarcats.model.types.RecurringActionDetail;
import com.imarcats.model.types.TimeOfDay;

/**
 * Utilities for Timer
 * @author Adam
 */
public class TimerUtils {

	private TimerUtils() { /* static utility class */ }
	
	/**
	 * Get TimeZone from TimeOfDay 
	 * @param timeOfDay_ TimeOfDay
	 * @return TimeZone 
	 */
	public static TimeZone getTimeZone(TimeOfDay timeOfDay_) {
		return timeOfDay_.getTimeZoneID() != null ? TimeZone.getTimeZone(timeOfDay_.getTimeZoneID()) : null;
	}

	/**
	 * Sets TimeZone to TimeOfDay
	 * @param timeOfDay_ TimeOfDay
	 * @param timeZone_ TimeZone 
	 */
	public static void setTimeZone(TimeOfDay timeOfDay_, TimeZone timeZone_) {
		String timeZoneStr = timeZone_ != null ? timeZone_.getID() : null;
		timeOfDay_.setTimeZoneID(timeZoneStr);
	}
	
	/**
	 * Adds Duration in Millis to TimeOfDay
	 * @param time_ TimeOfDay 
	 * @param duration_ Duration in Millis to be added 
	 * @return Result TimeOfDay
	 */
	public static TimeOfDay addToDate(TimeOfDay time_, long duration_) {
		GregorianCalendar timeOnActualDate = calculateTimeOfDateCalendar(time_, new Date());
		
		timeOnActualDate.add(GregorianCalendar.MILLISECOND, (int) duration_);
		
		return getTimeFromDate(timeOnActualDate.getTime(), getTimeZone(time_));
	}
	
	/**
	 * Get TimeOfDay from Date 
	 * @param date_ Date 
	 * @param timeZone_ Time Zone of the Date 
	 * @return TimeOfDay
	 */
	public static TimeOfDay getTimeFromDate(Date date_, TimeZone timeZone_) {
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(date_);
		calendar.setTimeZone(timeZone_);
		
		TimeOfDay timeOfDay = new TimeOfDay();
		timeOfDay.setHour(calendar.get(GregorianCalendar.HOUR_OF_DAY));
		timeOfDay.setMinute(calendar.get(GregorianCalendar.MINUTE));
		timeOfDay.setSecond(calendar.get(GregorianCalendar.SECOND));
		
		setTimeZone(timeOfDay, timeZone_);
		
		return timeOfDay;
	}
	
	/**
	 * Add Duration in Millis to Date 
	 * @param date_ Date 
	 * @param duration_ Duration in Millis 
	 * @return Calculated Date 
	 */
	public static Date addToDate(Date date_, long duration_) {
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(date_);
		
		calendar.add(Calendar.MILLISECOND, (int) duration_);
		
		return calendar.getTime();
	}
	
	/**
	 * Calculate the given time for the Actual (Current) Date
	 * @param time_ Time to be Calculated 
	 * @return Time of the Current Date
	 */
	public static Date calculateTimeOfActualDate(TimeOfDay time_) {
		Date now = new Date();
		return calculateTimeOfDate(time_, now);
	}

	/**
	 * Calculate the given time for the give Date
	 * @param time_ Time to be Calculated 
	 * @param date_ the given Date 
	 * @return Time of the given Date
	 */
	public static Date calculateTimeOfDate(TimeOfDay time_, Date date_) {
		GregorianCalendar calendar = calculateTimeOfDateCalendar(time_, date_);
		
		return calendar.getTime();
	}

	private static GregorianCalendar calculateTimeOfDateCalendar(
			TimeOfDay time_, Date date_) {
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTimeZone(getTimeZone(time_));
		calendar.setTime(date_);
		// calendar.add(Calendar.MILLISECOND, calendar.getTimeZone().getOffset(date_.getTime())); 
		
		calendar.set(Calendar.HOUR_OF_DAY, time_.getHour());
		calendar.set(Calendar.MINUTE, time_.getMinute());
		calendar.set(Calendar.SECOND, time_.getSecond());
		return calendar;
	}
	
	/**
	 * Finds next Day, which corresponds to the criteria defined by the RecurringActionDetail
	 * @param date_ Input Date
	 * @param detail_ Daily, OnWeekdays, OnBusinessDaysOnly, OnBusinessDaysAndWeekdays;
	 * @param calendar_ Business Calendar
	 * @return the first Day on or after, that given corresponds to the criteria
	 */
	public static Date findNextBusinessDay(Date date_, RecurringActionDetail detail_, BusinessCalendar calendar_) {
		Date nextBusinessDay = date_;
		
		if(detail_ == RecurringActionDetail.OnWeekdays) {
			nextBusinessDay = findNextWeekday(date_);
		} else if(detail_ == RecurringActionDetail.OnBusinessDaysOnly) {
			nextBusinessDay = findNextBusinessDay(date_, calendar_);
		} else if(detail_ == RecurringActionDetail.OnBusinessDaysAndWeekdays) {
			nextBusinessDay = findNextBusinessWeekday(date_, calendar_);
		}
		
		return nextBusinessDay;
	}

	private static Date findNextBusinessWeekday(Date date_, BusinessCalendar calendar_) {
		GregorianCalendar gregorianCalendar = new GregorianCalendar();
		gregorianCalendar.setTime(date_);
		
		while (isWeekend(gregorianCalendar) || isHoliday(calendar_, gregorianCalendar)) {
			gregorianCalendar.add(GregorianCalendar.DATE, 1);
		} 
		
		return gregorianCalendar.getTime();
	}

	
	private static Date findNextWeekday(Date date_) {
		GregorianCalendar gregorianCalendar = new GregorianCalendar();
		gregorianCalendar.setTime(date_);
		
		while (isWeekend(gregorianCalendar)) {
			gregorianCalendar.add(GregorianCalendar.DATE, 1);
		}
		
		return gregorianCalendar.getTime();
	}

	private static boolean isWeekend(GregorianCalendar gregorianCalendar) {
		return gregorianCalendar.get(GregorianCalendar.DAY_OF_WEEK) == GregorianCalendar.SATURDAY || 
				gregorianCalendar.get(GregorianCalendar.DAY_OF_WEEK) == GregorianCalendar.SUNDAY;
	}
	
	public static boolean isBusinessDay(Date date_, RecurringActionDetail detail_, BusinessCalendar calendar_) {
		GregorianCalendar dateCalendar = new GregorianCalendar();
		dateCalendar.setTime(date_);
		
		boolean isBusinessDay = true;
		if(detail_ == RecurringActionDetail.OnWeekdays) {
			isBusinessDay = !isWeekend(dateCalendar);
		} else if(detail_ == RecurringActionDetail.OnBusinessDaysOnly) {
			isBusinessDay = !isHoliday(calendar_, dateCalendar);
		} else if(detail_ == RecurringActionDetail.OnBusinessDaysAndWeekdays) {
			isBusinessDay = !isWeekend(dateCalendar) && !isHoliday(calendar_, dateCalendar);
		}
		
		return isBusinessDay;
	}
	
	private static Date findNextBusinessDay(Date date_, BusinessCalendar calendar_) {
		GregorianCalendar gregorianCalendar = new GregorianCalendar();
		gregorianCalendar.setTime(date_);
		
		while (isHoliday(calendar_, gregorianCalendar)) {
			gregorianCalendar.add(GregorianCalendar.DATE, 1);
		}
		
		return gregorianCalendar.getTime();
	}

	private static boolean isHoliday(BusinessCalendar calendar_,
			GregorianCalendar gregorianCalendar) {
		return !BusinessCalendarUtils.isBusinessDay(gregorianCalendar.getTime(), calendar_);
	}
}
