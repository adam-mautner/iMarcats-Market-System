package com.imarcats.model.types;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.imarcats.model.MarketModelObject;

/**
 * This class is needed to have a separate database table for this class
 * @author Adam
 */
@Entity
@Table(name="START_TIME")
public class StartTime implements MarketModelObject {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	@Column(name="ID")
	private Long _id;
	
	@Embedded
	private TimeOfDay _startTime;

	public StartTime() {
		super();
	}

	public StartTime(TimeOfDay startTime_) {
		super();
		_startTime = startTime_;
	}

	public TimeOfDay getStartTime() {
		return _startTime;
	}

	public void setStartTime(TimeOfDay startTime_) {
		_startTime = startTime_;
	}

	@Override
	public String toString() {
		return "StartTime [_id=" + _id + ", _startTime=" + _startTime + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((_id == null) ? 0 : _id.hashCode());
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
		StartTime other = (StartTime) obj;
		if (_id == null) {
			if (other._id != null)
				return false;
		} else if (!_id.equals(other._id))
			return false;
		if (_startTime == null) {
			if (other._startTime != null)
				return false;
		} else if (!_startTime.equals(other._startTime))
			return false;
		return true;
	}
	
}
