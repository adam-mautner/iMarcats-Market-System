package com.imarcats.internal.server.infrastructure.util;

import junit.framework.TestCase;

import com.imarcats.internal.server.interfaces.util.BusinessCalendarUtils;
import com.imarcats.model.types.BusinessCalendar;
import com.imarcats.model.types.Day;

public class BusinessCalendarTest extends TestCase {

	public void testParseEmptyCsv() throws Exception {
		BusinessCalendar calendar = BusinessCalendarUtils.parseFromCsv("");
		assertEquals(0, calendar.getBusinessCalendarDays().size());
		
		calendar = BusinessCalendarUtils.parseFromCsv("\n");
		assertEquals(0, calendar.getBusinessCalendarDays().size());
	}
	
	public void testCalendar() throws Exception {
		String calendarStr = 
			"2010/04/11,Holiday\n2010/04/12,BusinessDay\n2010/04/13,BusinessDay\n";
		
		// test parse 
		BusinessCalendar calendar = BusinessCalendarUtils.parseFromCsv(calendarStr);
		
		assertEquals(Day.Holiday, calendar.findByDate("2010/04/11").getDay());
		assertEquals(Day.BusinessDay, calendar.findByDate("2010/04/12").getDay());
		assertEquals(Day.BusinessDay, calendar.findByDate("2010/04/13").getDay());
		
		// test to string
		assertEquals(calendarStr, BusinessCalendarUtils.writeToCsv(calendar));
		
		// test functionality
		assertEquals(false, BusinessCalendarUtils.isBusinessDay(BusinessCalendarUtils.DATE_FORMAT.parse("2010/04/11"), calendar));		
		assertEquals(true, BusinessCalendarUtils.isBusinessDay(BusinessCalendarUtils.DATE_FORMAT.parse("2010/04/12"), calendar));
		assertEquals(true, BusinessCalendarUtils.isBusinessDay(BusinessCalendarUtils.DATE_FORMAT.parse("2010/04/14"), calendar));
		
	}
	
	public void testParseError() throws Exception {
		try {
			BusinessCalendarUtils.parseFromCsv(null);
			fail();
		} catch (Exception e) {
			// expected
		}
		
		String error1 = "2010/04/11";
		String error2 = "xyz";
		String error3 = "2010/04/11,";
		String error4 = "2010/04/11,xyz";
		String error5 = "2010/04/11,Holiday,2010/04/13";
		String error6 = "2010/04/11,Holiday,\n2010/04/11,Holiday";
		
		try {
			BusinessCalendarUtils.parseFromCsv(error1);
			fail();
		} catch (Exception e) {
			// expected
		}

		try {
			BusinessCalendarUtils.parseFromCsv(error2);
			fail();
		} catch (Exception e) {
			// expected
		}

		try {
			BusinessCalendarUtils.parseFromCsv(error3);
			fail();
		} catch (Exception e) {
			// expected
		}

		try {
			BusinessCalendarUtils.parseFromCsv(error4);
			fail();
		} catch (Exception e) {
			// expected
		}

		try {
			BusinessCalendarUtils.parseFromCsv(error5);
			fail();
		} catch (Exception e) {
			// expected
		}
		
		try {
			BusinessCalendarUtils.parseFromCsv(error6);
			fail();
		} catch (IllegalArgumentException e) {
			assertEquals(BusinessCalendarUtils.DUPLICATE_DATE, e.getMessage());
		}
	}
}