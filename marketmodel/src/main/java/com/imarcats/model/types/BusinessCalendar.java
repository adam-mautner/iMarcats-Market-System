package com.imarcats.model.types;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

import com.imarcats.model.MarketModelObject;

/**
 * Defines the Holidays and Business Day for a Period of Time
 * @author Adam
 */
@Embeddable
public class BusinessCalendar implements MarketModelObject, TransferableObject {

	private static final long serialVersionUID = 1L;
    
	/**
	 * Map from Date to Day (Business or Holiday)
	 * Required
	 */
	// TODO: Use explicit ordering clause ! (Find out, why this is not working) - We expect that this list will be short and will not be changed frequently, so it is OK for now to not sort it
	// @Order(extensions = @Extension(vendorName="datanucleus", key="list-ordering", value="_dateString asc"))
	@Column(name="BUSINESS_CALENDAR_DAYS")
	@OneToMany(cascade=CascadeType.ALL, orphanRemoval=true) 
	private List<BusinessCalendarDay> _businessCalendarDays = new ArrayList<BusinessCalendarDay>();

    public static BusinessCalendar create(BusinessCalendar calendar_) {
    	BusinessCalendar newCalendar = new BusinessCalendar();
    	
    	for (BusinessCalendarDay date : calendar_.getBusinessCalendarDays()) {
			newCalendar.getBusinessCalendarDays().add(BusinessCalendarDay.create(date));	
		}
    	
    	return newCalendar;
    }
	
	public void setBusinessCalendarDays(List<BusinessCalendarDay> businessCalendarDays_) {		
		_businessCalendarDays = businessCalendarDays_;
	}

	public List<BusinessCalendarDay> getBusinessCalendarDays() {
		return _businessCalendarDays;
	}
   
    
	/**
	 * Finds BusinessCalendarDay by Date String
	 * @param dateString_ Date String with yyyy/MM/dd Date Format
	 * @return day
	 */
	public BusinessCalendarDay findByDate(String dateString_) {
		BusinessCalendarDay found = null;
		for (BusinessCalendarDay day : _businessCalendarDays) {
			if(dateString_.equals(day.getDateString())) {
				found = day;
				break;
			}
		}
		
		return found;
	}

	@Override
	public Object getObjectValue() {
		return this;
	}

	@Override
	public String toString() {
		return "BusinessCalendar [_businessCalendarDays="
				+ _businessCalendarDays + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((_businessCalendarDays == null) ? 0 : _businessCalendarDays
						.hashCode());
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
		BusinessCalendar other = (BusinessCalendar) obj;
		if (_businessCalendarDays == null) {
			if (other._businessCalendarDays != null)
				return false;
		} else if (!_businessCalendarDays.equals(other._businessCalendarDays))
			return false;
		return true;
	}
	
}
