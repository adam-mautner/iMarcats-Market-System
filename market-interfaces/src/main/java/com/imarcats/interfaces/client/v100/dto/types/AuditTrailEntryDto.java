package com.imarcats.interfaces.client.v100.dto.types;

import java.util.Date;

/**
 * Holds Audit Information for Audit Trail
 * @author Adam
 *
 */
public class AuditTrailEntryDto {

	/**
	 * User ID recorded
	 * Optional
	 */
//	@Column(name="USER_ID", length=50)
	private String _userID;
	
	/**
	 * Date and Time recorded
	 * Required
	 */
    // TODO: Change to timestamp
	private Date _dateTime;
	
	/**
	 * Type Name of the Object (Market.class.getSimpleName()) Related to this Audit Entry 
	 */
//	@Column(name="OBJECT_TYPE", length=50)
	private String _objectType;
    
	/**
	 * Information related to this Audit Entry 
	 */
//	@Column(name="RELATED_INFORMATION", length=350)
    private String _relatedInformation;

	/**
	 * Action Related to this Audit Entry 
	 */
//	@Column(name="AUDIT_ENTRY_ACTION_STR", length=200)
	private AuditEntryAction _auditEntryAction;

	public String getObjectType() {
		return _objectType;
	}

	public void setObjectType(String objectType_) {
		_objectType = objectType_;
	}

	public AuditEntryAction getAuditEntryAction() {
		return _auditEntryAction;
	}

	public void setAuditEntryAction(AuditEntryAction auditEntryAction_) {
		_auditEntryAction = auditEntryAction_;
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
		_dateTime = dateTime_;
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

}
