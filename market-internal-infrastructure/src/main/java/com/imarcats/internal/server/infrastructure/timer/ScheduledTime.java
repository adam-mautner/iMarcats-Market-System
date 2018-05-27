package com.imarcats.internal.server.infrastructure.timer;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.imarcats.model.types.TimeOfDay;

/**
 * Scheduled Time for Recurring Action
 */
@Entity
@Table(name="SCHEDULED_TIME")
public class ScheduledTime {

	/**
	 * Primary Key of the Scheduled Time
	 * Note: No need for accessing this field directly
	 */	
	@Id
	@GeneratedValue 
	@Column(name="ID")
    private Long _id;
	
    /**
	 * Scheduled Time
	 **/
    private TimeOfDay _scheduledTime;

	public TimeOfDay getScheduledTime() {
		return _scheduledTime;
	}

	public void setScheduledTime(TimeOfDay scheduledTime_) {
		_scheduledTime = scheduledTime_;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((_id == null) ? 0 : _id.hashCode());
		result = prime * result
				+ ((_scheduledTime == null) ? 0 : _scheduledTime.hashCode());
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
		ScheduledTime other = (ScheduledTime) obj;
		if (_id == null) {
			if (other._id != null)
				return false;
		} else if (!_id.equals(other._id))
			return false;
		if (_scheduledTime == null) {
			if (other._scheduledTime != null)
				return false;
		} else if (!_scheduledTime.equals(other._scheduledTime))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ScheduledTime [_id=" + _id + ", _scheduledTime="
				+ _scheduledTime + "]";
	}
	
    
}
