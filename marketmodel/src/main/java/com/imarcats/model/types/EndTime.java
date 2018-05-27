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
@Table(name="END_TIME")
public class EndTime implements MarketModelObject {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	@Column(name="ID")
	private Long _id;
	
	@Embedded
	private TimeOfDay _endTime;

	public EndTime() {
		super();
	}

	public EndTime(TimeOfDay endTime_) {
		super();
		_endTime = endTime_;
	}

	public TimeOfDay getEndTime() {
		return _endTime;
	}

	public void setEndTime(TimeOfDay endTime_) {
		_endTime = endTime_;
	}

	@Override
	public String toString() {
		return "EndTime [_id=" + _id + ", _endTime=" + _endTime + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((_endTime == null) ? 0 : _endTime.hashCode());
		result = prime * result + ((_id == null) ? 0 : _id.hashCode());
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
		EndTime other = (EndTime) obj;
		if (_endTime == null) {
			if (other._endTime != null)
				return false;
		} else if (!_endTime.equals(other._endTime))
			return false;
		if (_id == null) {
			if (other._id != null)
				return false;
		} else if (!_id.equals(other._id))
			return false;
		return true;
	}
	
}