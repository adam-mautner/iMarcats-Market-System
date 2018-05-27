package com.imarcats.interfaces.client.v100.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.imarcats.interfaces.client.v100.dto.types.ActivationStatus;
import com.imarcats.interfaces.client.v100.dto.types.AuditInformationDto;
import com.imarcats.interfaces.client.v100.dto.types.UserRole;

/**
 * A User trading on the Market
 * @author Adam
 */
public class UserDto implements ActivatableMarketObjectDto {

	/**
	 * Login Name (User Name) of the User
	 * ID of the User
	 * 
	 * Required
	 */
//	@Column(name="USER_ID", unique=true, length=DataLengths.USER_ID_LENGTH)
	private String _userID;
    
	/**
	 * First Name of the User
	 * Required
	 */
//	@Column(name="FIRST_NAME", nullable=false, length=50)
	private String _firstName;

	/**
	 * Last Name of the User
	 * Required
	 */
//	@Column(name="LAST_NAME", nullable=false, length=50)
	private String _lastName;
	
	/**
	 * Type of the User Role (Value from UserRole)
	 * Required
	 */	
	private UserRole _role;
	
	/**
	 * Reference to the User's Business Entity 
	 * Required
	 */
//	@Column(name="BUSINESS_ENTITY_CODE", nullable=false, length=DataLengths.MARKET_OBJECT_CODE_LENGTH)
	private String _businessEntityCode;

	/**
	 * Defines Bank Account for the User 
	 * Required
	 */
	private List<AccountInformationDto> _accounts = new ArrayList<AccountInformationDto>();
	
	/**
	 * Notification Email Address
	 * Required
	 */
//	@Column(name="NOTIFICATION_EMAIL_ADDRESS", nullable=false, length=50)
	private String _notificationEmailAddress;
	
	/**
	 * Confirmation Email Address
	 * Required
	 */
//	@Column(name="CONFIRMATION_EMAIL_ADDRESS", nullable=false, length=50)
	private String _confirmationEmailAddress;
	
	/**
	 * Supports the Activation Workflow of the User (Value from ActivationStatus)
	 * 
	 * Note: This needs to be a String for Datastore to Query on this field
	 * Required 
	 */
	private ActivationStatus _activationStatus;
	
	/**
	 * Audit Information, like who created the User and when
	 * Optional
	 */
	private AuditInformationDto _creationAudit;

	/**
	 * Approval Audit Information, like who Approved the User and when
	 * Optional
	 */
	private AuditInformationDto _approvalAudit;

	/**
	 * Suspension Audit Information, like who Suspended the User and when
	 * Optional
	 */
	private AuditInformationDto _suspensionAudit;
	
	/**
	 * Change Audit Information, like who Changed the User and when
	 * Optional
	 */
	private AuditInformationDto _changeAudit;
	
	/**
	 * Deletion Audit Information, like who Deleted the User and when
	 * Optional
	 */
	private AuditInformationDto _deletionAudit;
	
	/**
     * Date and Time, when the Object was Last Updated
     * Required
     */
    // TODO: Change to timestamp
	private Date _lastUpdateTimestamp;
	
	/**
	 * Version of the object in the datastore 
	 */
	private Long _versionNumber;
	
	public String getFirstName() {
		return _firstName;
	}

	public void setFirstName(String firstName_) {
		_firstName = firstName_;
	}

	public String getLastName() {
		return _lastName;
	}

	public void setLastName(String lastName_) {
		_lastName = lastName_;
	}

	public String getUserID() {
		return _userID;
	}

	public void setUserID(String userID_) {
		_userID = userID_;
	}

	public UserRole getRole() {
		return _role;
	}

	public void setRole(UserRole role_) {
		_role = role_;
	}

	public void setActivationStatus(ActivationStatus activationStatus) {
		_activationStatus = activationStatus;
	}

	public ActivationStatus getActivationStatus() {
		return _activationStatus;
	}
	
	public AuditInformationDto getCreationAudit() {
		return _creationAudit;
	}

	public void setCreationAudit(AuditInformationDto creationAudit_) {
		_creationAudit = creationAudit_;
	}

	public AuditInformationDto getApprovalAudit() {
		return _approvalAudit;
	}

	public void setApprovalAudit(AuditInformationDto approvalAudit_) {
		_approvalAudit = approvalAudit_;
	}

	public AuditInformationDto getSuspensionAudit() {
		return _suspensionAudit;
	}

	public void setSuspensionAudit(AuditInformationDto suspensionAudit_) {
		_suspensionAudit = suspensionAudit_;
	}

	public AuditInformationDto getChangeAudit() {
		return _changeAudit;
	}

	public void setChangeAudit(AuditInformationDto changeAudit_) {
		_changeAudit = changeAudit_;
	}

	public AuditInformationDto getDeletionAudit() {
		return _deletionAudit;
	}

	public void setDeletionAudit(AuditInformationDto deletionAudit_) {
		_deletionAudit = deletionAudit_;
	}

	public void setLastUpdateTimestamp(Date lastUpdateTimestamp_) {
		_lastUpdateTimestamp = lastUpdateTimestamp_;
	}

	public Date getLastUpdateTimestamp() {
		return _lastUpdateTimestamp;
	}

	public List<AccountInformationDto> getAccounts() {
		return _accounts;
	}

	public void setAccounts(List<AccountInformationDto> accounts_) {
		_accounts = accounts_;
	}

	public String getNotificationEmailAddress() {
		return _notificationEmailAddress;
	}

	public void setNotificationEmailAddress(String notificationEmailAddress_) {
		_notificationEmailAddress = notificationEmailAddress_;
	}

	public String getConfirmationEmailAddress() {
		return _confirmationEmailAddress;
	}

	public void setConfirmationEmailAddress(String confirmationEmailAddress_) {
		_confirmationEmailAddress = confirmationEmailAddress_;
	}

	public void setBusinessEntityCode(String businessEntityCode) {
		_businessEntityCode = businessEntityCode;
	}

	public String getBusinessEntityCode() {
		return _businessEntityCode;
	}

	public Long getVersionNumber() {
		return _versionNumber;
	}

	public void setVersionNumber(Long versionNumber_) {
		_versionNumber = versionNumber_;
	}

	@Override
	public String getDescription() {
		return getRole().name();
	}

	@Override
	public String getName() {
		return getLastName() + ", " + getFirstName();
	}

	@Override
	public AuditInformationDto getRolloverAudit() {
		return null;
	}

	@Override
	public String getCode() {
		return getUserID();
	}
}
