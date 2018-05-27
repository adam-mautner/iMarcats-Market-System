package com.imarcats.interfaces.client.v100.dto.types;

import java.util.ArrayList;
import java.util.List;

/**
 * Defines the Holidays and Business Day for a Period of Time
 * @author Adam
 */
public class BusinessCalendarDto implements TransferableObjectDto {
 
	/**
	 * Map from Date to Day (Business or Holiday)
	 * Required
	 */
	private List<BusinessCalendarDayDto> _businessCalendarDays = new ArrayList<BusinessCalendarDayDto>();

	public void setBusinessCalendarDays(List<BusinessCalendarDayDto> businessCalendarDays_) {		
		_businessCalendarDays = businessCalendarDays_;
	}

	public List<BusinessCalendarDayDto> getBusinessCalendarDays() {
		return _businessCalendarDays;
	}

	@Override
	public Object getObjectValue() {
		return this;
	}
}
