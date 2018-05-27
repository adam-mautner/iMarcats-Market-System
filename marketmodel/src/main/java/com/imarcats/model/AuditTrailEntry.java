package com.imarcats.model;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.imarcats.model.types.AuditEntryAction;

/**
 * Holds Audit Information for Audit Trail
 * @author Adam
 *
 */
@Entity
@Table(name="AUDIT_TRAIL_ENTRY")
public class AuditTrailEntry implements MarketModelObject {

	private static final long serialVersionUID = 1L;

	/**
	 * Primary Key of the AuditTrailEntry 
	 * Note: No need for accessing this field directly
	 */	
	@Id
	@GeneratedValue 
	@Column(name="ID")
    private Long _id;
	
	/**
	 * User ID recorded
	 * Optional
	 */
	@Column(name="USER_ID", length=50)
	private String _userID;
	
	/**
	 * Date and Time recorded
	 * Required
	 */
	// TODO: Increase the accuracy of this field in JPA/MySQL 
	// - https://dev.mysql.com/doc/refman/5.5/en/datetime.html
	// - http://dev.mysql.com/doc/refman/5.6/en/fractional-seconds.html
	@Column(name="DATE_TIME", nullable=false)
	private Timestamp _dateTime;
	
	/**
	 * Type Name of the Object (Market.class.getSimpleName()) Related to this Audit Entry 
	 */
	@Column(name="OBJECT_TYPE", length=50)
	private String _objectType;
    
	/**
	 * Information related to this Audit Entry 
	 */
	@Column(name="RELATED_INFORMATION", length=350)
    private String _relatedInformation;

	/**
	 * Action Related to this Audit Entry 
	 */
	@Column(name="AUDIT_ENTRY_ACTION", length=200)
	private AuditEntryAction _auditEntryAction;
    
	public AuditEntryAction getAuditEntryAction() {
		return _auditEntryAction;
	}

	public void setAuditEntryAction(AuditEntryAction action_) {
		_auditEntryAction = action_;
	}

	public String getUserID() {
		return _userID;
	}

	public void setUserID(String userID_) {
		_userID = userID_;
	}

	public Date getDateTime() {
		return _dateTime;
	}

	public void setDateTime(Date dateTime_) {
		_dateTime = new Timestamp(dateTime_.getTime());
	}

	public String getObjectTypeStr() {
		return _objectType;
	}

	public void setObjectTypeStr(String objectType_) {
		_objectType = objectType_;
	}

	public String getRelatedInformation() {
		return _relatedInformation;
	}

	public void setRelatedInformation(String relatedInformation_) {
		_relatedInformation = relatedInformation_;
	}

	@Override
	public String toString() {
		return "AuditTrailEntry [_id=" + _id + ", _userID=" + _userID
				+ ", _dateTime=" + _dateTime + ", _objectType=" + _objectType
				+ ", _relatedInformation=" + _relatedInformation + ", _auditEntryActionStr="
				+ _auditEntryAction + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((_auditEntryAction == null) ? 0 : _auditEntryAction
						.hashCode());
		result = prime * result
				+ ((_dateTime == null) ? 0 : _dateTime.hashCode());
		result = prime * result + ((_id == null) ? 0 : _id.hashCode());
		result = prime * result
				+ ((_relatedInformation == null) ? 0 : _relatedInformation.hashCode());
		result = prime * result
				+ ((_objectType == null) ? 0 : _objectType.hashCode());
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
		AuditTrailEntry other = (AuditTrailEntry) obj;
		if (_auditEntryAction == null) {
			if (other._auditEntryAction != null)
				return false;
		} else if (!_auditEntryAction.equals(other._auditEntryAction))
			return false;
		if (_dateTime == null) {
			if (other._dateTime != null)
				return false;
		} else if (!_dateTime.equals(other._dateTime))
			return false;
		if (_id == null) {
			if (other._id != null)
				return false;
		} else if (!_id.equals(other._id))
			return false;
		if (_relatedInformation == null) {
			if (other._relatedInformation != null)
				return false;
		} else if (!_relatedInformation.equals(other._relatedInformation))
			return false;
		if (_objectType == null) {
			if (other._objectType != null)
				return false;
		} else if (!_objectType.equals(other._objectType))
			return false;
		if (_userID == null) {
			if (other._userID != null)
				return false;
		} else if (!_userID.equals(other._userID))
			return false;
		return true;
	}
}
