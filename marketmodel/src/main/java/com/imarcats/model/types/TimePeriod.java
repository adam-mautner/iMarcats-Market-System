package com.imarcats.model.types;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import com.imarcats.model.MarketModelObject;

/**
 * Defines a Time Period
 * @author Adam
 *
 */
@Embeddable
public class TimePeriod implements MarketModelObject, TransferableObject {

	public static final String START_TIME_MUST_BE_BEFORE_END_TIME = "Start Time must be before End Time";
	public static final String TIME_ZONE_DIFFERENT_ERROR = "TimeOfDay objects in Time Period must have the same Time Zone";

	private static final long serialVersionUID = 1L;
	
	/**
	 * Start Time
	 * Required
	 */
	@OneToOne(cascade=CascadeType.ALL, orphanRemoval=true) 
	@JoinColumn(name="START_TIME_ID")
	private StartTime _startTime;

	/**
	 * Start Time
	 * Required
	 */
	@OneToOne(cascade=CascadeType.ALL, orphanRemoval=true) 
	@JoinColumn(name="END_TIME_ID")	
	private EndTime _endTime;

	public static TimePeriod create(TimePeriod timePeriod_) {
		TimePeriod newPeriod = new TimePeriod();
		newPeriod.setStartTime(TimeOfDay.create(timePeriod_.getStartTime()));
		newPeriod.setEndTime(TimeOfDay.create(timePeriod_.getEndTime()));
		
		return newPeriod;
	}
	
	public TimeOfDay getStartTime() {
		return _startTime != null 
					? _startTime.getStartTime()
					: null;
	}

	public void setStartTime(TimeOfDay startTime_) {
		TimeOfDay endTime = getEndTime();
		if(endTime != null) {
			if(endTime.getTimeZoneID() == null) {
				throw new IllegalStateException("No Time Zone is defined on End Time of Time Period");
			}
			
			checkStartEndTime(startTime_, endTime);
		}
		
		// this is needed, because Google App Engine's Datastore would not let 
		// change the uncommited classes, since this field may change many times 
		// before commit, we can only copy the values on the object
		// TODO: Is this the same as in JPA?
		if(_startTime == null) {
			if(startTime_ != null) {
				_startTime = 
					new StartTime(
							TimeOfDay.create(startTime_)); // Cloning Time is needed, because it might be copied from an other persisted Object
			}
		} else {
			setTime(startTime_, _startTime.getStartTime());
		}
	}

	private void checkStartEndTime(TimeOfDay startTime_, TimeOfDay endTime) {
		if(!endTime.getTimeZoneID().equals(startTime_.getTimeZoneID())) {
			throw new IllegalArgumentException(TIME_ZONE_DIFFERENT_ERROR);
		}
		
		if(endTime.compareTo(startTime_) < 0) {
			throw new IllegalArgumentException(START_TIME_MUST_BE_BEFORE_END_TIME);	
		}
	}


	public TimeOfDay getEndTime() {
		return _endTime != null 
					? _endTime.getEndTime() 
					: null;
	}

	public void setEndTime(TimeOfDay endTime_) {
		TimeOfDay startTime = getStartTime();
		if(startTime != null) {
			if(startTime.getTimeZoneID() == null) {
				throw new IllegalStateException("No Time Zone is defined on Start Time of Time Period");
			}
			
			checkStartEndTime(startTime, endTime_);
		}
		
		// this is needed, because Google App Engine's Datastore would not let 
		// change the uncommited classes, since this field may change many times 
		// before commit, we can only copy the values on the object
		// TODO: Is this the same as in JPA?
		if(_endTime == null) {
			if(endTime_ != null) {
				_endTime = 
					new EndTime(
							TimeOfDay.create(endTime_)); // Cloning Time is needed, because it might be copied from an other persisted Object
			}
		} else {
			setTime(endTime_, _endTime.getEndTime());
		}
	}

	private void setTime(TimeOfDay source_, TimeOfDay target_) {
		if(source_ != null && target_ != null) {
			target_.setHour(source_.getHour());
			target_.setMinute(source_.getMinute());
			target_.setSecond(source_.getSecond());
			target_.setTimeZoneID(source_.getTimeZoneID());
		}
	}
	
	@Override
	public Object getObjectValue() {
		return this;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((_endTime == null) ? 0 : _endTime.hashCode());
		result = prime * result
				+ ((_startTime == null) ? 0 : _startTime.hashCode());
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
		TimePeriod other = (TimePeriod) obj;
		if (_endTime == null) {
			if (other._endTime != null)
				return false;
		} else if (!_endTime.equals(other._endTime))
			return false;
		if (_startTime == null) {
			if (other._startTime != null)
				return false;
		} else if (!_startTime.equals(other._startTime))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "TimePeriod [_startTime=" + _startTime + ", _endTime="
				+ _endTime + "]";
	}
	
	
}
