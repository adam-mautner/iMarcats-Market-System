package com.imarcats.model.types;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import com.imarcats.model.MarketModelObject;
import com.imarcats.model.meta.DataLengths;

/**
 * Audit information, like when was done something and by whom
 * @author Adam
 */
@Embeddable
public class AuditInformation implements MarketModelObject, TransferableObject {

	private static final long serialVersionUID = 1L;
	
	/**
	 * Date and Time recorded
	 * Required
	 */
	@Column(name="DATE_TIME", nullable=false)
	private Timestamp _dateTime;
	
	/**
	 * User ID recorded
	 * Optional
	 */
	@Column(name="USER_ID", length=DataLengths.USER_ID_LENGTH)
	private String _userID;

	public static AuditInformation create(AuditInformation audit_) {
		AuditInformation newAuditInformation = new AuditInformation();
		newAuditInformation.setDateTime(audit_.getDateTime());
		newAuditInformation.setUserID(audit_.getUserID());
		
		return newAuditInformation;
	}
	
	public Date getDateTime() {
		return _dateTime;
	}

	public void setDateTime(Date dateTime_) {
		_dateTime = dateTime_ != null 
				? new Timestamp(dateTime_.getTime())
				: null;
	}

	public String getUserID() {
		return _userID;
	}

	public void setUserID(String userID_) {
		_userID = userID_;
	}
	
	@Override
	public Object getObjectValue() {
		return this;
	}

	@Override
	public String toString() {
		return "AuditInformation [_dateTime=" + _dateTime + ", _userID="
				+ _userID + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((_dateTime == null) ? 0 : _dateTime.hashCode());
		result = prime * result + ((_userID == null) ? 0 : _userID.hashCode());
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
		AuditInformation other = (AuditInformation) obj;
		if (_dateTime == null) {
			if (other._dateTime != null)
				return false;
		} else if (!_dateTime.equals(other._dateTime))
			return false;
		if (_userID == null) {
			if (other._userID != null)
				return false;
		} else if (!_userID.equals(other._userID))
			return false;
		return true;
	}
}
