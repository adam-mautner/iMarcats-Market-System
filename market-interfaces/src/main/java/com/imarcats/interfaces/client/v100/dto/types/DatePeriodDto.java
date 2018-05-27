package com.imarcats.interfaces.client.v100.dto.types;

import java.util.Date;

import com.imarcats.interfaces.client.v100.dto.MarketModelObjectDto;

/**
 * Defines a Date Period
 * @author Adam
 *
 */
public class DatePeriodDto implements MarketModelObjectDto {

    public static final String START_DATE_MUST_BE_BEFORE_END_DATE = "Start Date must be before End Date";

	/**
	 * Start Date Value of the Property
	 */
	private Date _startDate;
	
	/**
	 * End Date Value of the Property
	 */
	private Date _endDate;
	
	public Date getStartDate() {
		return _startDate;
	}

	public void setStartDate(Date startDate_) {
		if(_endDate != null) {
			checkStartEndTime(startDate_, getEndDate());
		}
		_startDate = startDate_;
	}

	public Date getEndDate() {
		return _endDate;
	}

	public void setEndDate(Date endDate_) {
		if(_startDate != null) {
			checkStartEndTime(getStartDate(), endDate_);
		}
		_endDate = endDate_;
	}

	private void checkStartEndTime(Date startDate_, Date endDate_) {		
		if(endDate_.compareTo(startDate_) < 0) {
			throw new IllegalArgumentException(START_DATE_MUST_BE_BEFORE_END_DATE);	
		}
	}

	@Override
	public String toString() {
		return "DatePeriod [_startDate=" + _startDate + ", _endDate="
				+ _endDate + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((_endDate == null) ? 0 : _endDate.hashCode());
		result = prime * result
				+ ((_startDate == null) ? 0 : _startDate.hashCode());
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
		DatePeriodDto other = (DatePeriodDto) obj;
		if (_endDate == null) {
			if (other._endDate != null)
				return false;
		} else if (!_endDate.equals(other._endDate))
			return false;
		if (_startDate == null) {
			if (other._startDate != null)
				return false;
		} else if (!_startDate.equals(other._startDate))
			return false;
		return true;
	}
	
}
