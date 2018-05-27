package com.imarcats.internal.server.interfaces.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import com.imarcats.model.types.BusinessCalendar;
import com.imarcats.model.types.BusinessCalendarDay;
import com.imarcats.model.types.Day;

/**
 * Utilities for Business Calendar 
 * @author Adam
 *
 */
public class BusinessCalendarUtils {

	public static final String ILLEGAL_FORMAT_FOR_BUSINESS_CALENDAR = "Illegal format for Business Calendar, \nRequired Format Example: \n2010/04/11,Holiday\n2010/04/12,BusinessDay\n2010/04/13,BusinessDay";
	public static final String DATE_CANNOT_BE_PARSED = "Date cannot be parsed";
	public static final String DUPLICATE_DATE = "Duplicate Date";

	public static final String DATE_FORMAT_STR = "yyyy/MM/dd";
	
	public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(DATE_FORMAT_STR);
	
	private BusinessCalendarUtils() { /* static utility class */ }
	
	/**
	 * @param date_ Date to be tested
	 * @param calendar_ Calendar
	 * @return if the given day is a business day in this calendar
	 * 
	 * Note: Only excludes the dates defined as holidays in the calendar, disregards the weekends
	 */
	public static boolean isBusinessDay(Date date_, BusinessCalendar calendar_) {
		String dateStr = DATE_FORMAT.format(date_);
		BusinessCalendarDay calendarDay = calendar_.findByDate(dateStr);
		Day day = null;
		if(calendarDay != null) {
			day = calendarDay.getDay();
		}
		
		return day != Day.Holiday;
	}
	
	/**
	 * Parses Business Calendar from CSV. 
	 * 
	 * Example: 2010/04/11,Holiday
	 *          2010/04/12,BusinessDay
	 *  		2010/04/13,BusinessDay
	 *  
	 * Note: A Date not listed as Holiday will be a Business Day 
	 * @param csv_ CSV version of Business Calendar
	 * @return new Calendar Instance 
	 */
	public static BusinessCalendar parseFromCsv(String csv_) {
		BusinessCalendar calendar = new BusinessCalendar();
		
		if(!"".equals(csv_)) {
			String[] lines = csv_.split("\n");
			IllegalArgumentException illegalFormatException = new IllegalArgumentException(ILLEGAL_FORMAT_FOR_BUSINESS_CALENDAR);
			
			
			for (String line : lines) {
				// ignore empty lines 
				if(line.trim().equals("")) {
					continue;
				}
				
				String[] dateAndDay = line.split(",");
				if(dateAndDay.length != 2) {
					throw illegalFormatException;
				}
				
				try {
					DATE_FORMAT.parse(dateAndDay[0].trim());
				} catch (ParseException e) {
					throw illegalFormatException;
				}
				Day day = null;
				try {
					day = Day.valueOf(dateAndDay[1].trim());
				} catch (Exception e) {
					throw illegalFormatException;
				}
				
				if(findByDate(dateAndDay[0], calendar) != null) {
					throw new IllegalArgumentException(DUPLICATE_DATE);
				}
				
				calendar.getBusinessCalendarDays().add(BusinessCalendarDay.create(dateAndDay[0], day));
			}
		}
		return calendar;
	}
	
	private static BusinessCalendarDay findByDate(String dateStr_, BusinessCalendar calendar_) {
		BusinessCalendarDay found = null;
		for (BusinessCalendarDay day : calendar_.getBusinessCalendarDays()) {
			if(day.getDateString().equals(dateStr_)) {
				found = day;
				break;
			}
		}
		
		return found;
	}
	
	/**
	 * Writes the Business Calendar to CSV format.
	 * @param calendar_ Calendar
	 * 
	 * Example: 2010/04/11,Holiday
	 *          2010/04/12,BusinessDay
	 *  		2010/04/13,BusinessDay
	 * @return String in CSV Format
	 */
	public static String writeToCsv(BusinessCalendar calendar_) {
		StringBuffer stringBuffer = new StringBuffer("");

		List<BusinessCalendarDay> sortedDays = new ArrayList<BusinessCalendarDay>();
		for (BusinessCalendarDay date : calendar_.getBusinessCalendarDays()) {
			sortedDays.add(date);
		}
		
		Collections.sort(sortedDays, new Comparator<BusinessCalendarDay>() {

			@Override
			public int compare(BusinessCalendarDay date1_, BusinessCalendarDay date2_) {
				try {
					Date date1 = DATE_FORMAT.parse(date1_.getDateString());
					Date date2 = DATE_FORMAT.parse(date2_.getDateString());
					return date1.compareTo(date2);
				} catch (ParseException e) {
					new IllegalArgumentException(DATE_CANNOT_BE_PARSED);
				}
				return 0;
			}
			
		});
		
		for (BusinessCalendarDay date : sortedDays) {
			stringBuffer.append(date.getDateString());
			stringBuffer.append(",");
			stringBuffer.append(date.getDay());
			stringBuffer.append("\n");	
		}
		
		return stringBuffer.toString();
	} 
}
