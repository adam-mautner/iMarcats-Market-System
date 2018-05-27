package com.imarcats.interfaces.client.v100.dto.types;

import java.util.Date;

import com.imarcats.interfaces.client.v100.dto.ActivatableMarketObjectDto;


/**
 * Defines a Business Entity, who can trade on the Market or be a Market Operator 
 * 
 * TODO: Find the right set of fields for Business Entity
 * TODO: Move to model main package 
 * 
 * @author Adam
 */
public class BusinessEntityDto implements ActivatableMarketObjectDto {

	/**
	 * Code of the Business Entity - Should be Primary Key of the Business Entity
	 * Required
	 */
//	@Column(name="CODE", length=DataLengths.MARKET_OBJECT_CODE_LENGTH)
	private String _code;

	/**
	 * Name of the Business Entity
	 * Required
	 */
//	@Column(name="NAME", nullable=false, length=DataLengths.MARKET_OBJECT_NAME_LENGTH)
	private String _name;

	/**
	 * Description of the Business Entity
	 * Required
	 */
//	@Column(name="DESCRIPTION", nullable=false, length=DataLengths.MARKET_OBJECT_DESCRIPTION_LENGTH)
	private String _description;
    
	/**
	 * Owner User of the Business Entity
	 * Optional
	 */
//	@Column(name="OWNER_USER_ID", nullable=false, length=DataLengths.USER_ID_LENGTH)
	private String _ownerUserID;
    
	/**
	 * Bank Account of the Business Entity (IBAN)
	 * Optional
	 * 
	 * TODO: Implement this correctly
	 */
//	@Column(name="BANK_ACCOUNT_INFORMATION", length=34)
	private String _bankAccountInformation;
	
	/**
	 * Address of the Business Entity
	 * Optional
	 */
	private AddressDto _physicalAddress;
	
	/**
	 * Corporate Information for the Business Entity
	 * Required
	 */
	private CorporateInformationDto _corporateInformation;

	/**
	 * Audit Information, like who created the Business Entity and when
	 * Optional
	 */
	private AuditInformationDto _creationAudit;

	/**
	 * Approval Audit Information, like who Approved the Business Entity and when
	 * Optional
	 */
	private AuditInformationDto _approvalAudit;

	/**
	 * Suspension Audit Information, like who Suspended the Business Entity and when
	 * Optional
	 */
	private AuditInformationDto _suspensionAudit;
	
	/**
	 * Deletion Audit Information, like who Deleted the Business Entity and when
	 * Optional
	 */
	private AuditInformationDto _deletionAudit;
	
	/**
	 * Change Audit Information, like who Changed the Business Entity and when
	 * Optional
	 */
	private AuditInformationDto _changeAudit;

	/**
	 * Supports the Activation Workflow of the Business Entity (Value from ActivationStatus)
	 * 
	 * Note: This needs to be a String for Datastore to Query on this field
	 * Required 
	 */
	private ActivationStatus _activationStatus;

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
	
	public String getBankAccountInformation() {
		return _bankAccountInformation;
	}

	public void setBankAccountInformation(String bankAccountInformation_) {
		_bankAccountInformation = bankAccountInformation_;
	}

	public AddressDto getPhysicalAddress() {
		return _physicalAddress;
	}

	public void setPhysicalAddress(AddressDto physicalAddress_) {
		_physicalAddress = physicalAddress_;
	}

	public CorporateInformationDto getCorporateInformation() {
		return _corporateInformation;
	}

	public void setCorporateInformation(CorporateInformationDto corporateInformation_) {
		_corporateInformation = corporateInformation_;
	}

	public String getCode() {
		return _code;
	}

	public void setCode(String code_) {
		_code = code_;
	}

	public String getName() {
		return _name;
	}

	public void setName(String name_) {
		_name = name_;
	}

	public String getDescription() {
		return _description;
	}

	public void setDescription(String description_) {
		_description = description_;
	}

	public void setOwnerUserID(String ownerUserID_) {
		_ownerUserID = ownerUserID_;
	}

	public String getOwnerUserID() {
		return _ownerUserID;
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

	public AuditInformationDto getDeletionAudit() {
		return _deletionAudit;
	}

	public void setDeletionAudit(AuditInformationDto deletionAudit_) {
		_deletionAudit = deletionAudit_;
	}

	public AuditInformationDto getChangeAudit() {
		return _changeAudit;
	}

	public void setChangeAudit(AuditInformationDto changeAudit_) {
		_changeAudit = changeAudit_;
	}

	public void setLastUpdateTimestamp(Date lastUpdateTimestamp_) {
		_lastUpdateTimestamp = lastUpdateTimestamp_;
	}

	public ActivationStatus getActivationStatus() {
		return _activationStatus;
	}

	public void setActivationStatus(ActivationStatus activationStatus_) {
		_activationStatus = activationStatus_;
	}

	public Date getLastUpdateTimestamp() {
		return _lastUpdateTimestamp;
	}

	public Long getVersionNumber() {
		return _versionNumber;
	}

	public void setVersionNumber(Long versionNumber_) {
		_versionNumber = versionNumber_;
	}

	@Override
	public AuditInformationDto getRolloverAudit() {
		return null;
	}	
}
