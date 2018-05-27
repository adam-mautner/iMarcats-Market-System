package com.imarcats.interfaces.client.v100.dto;

import java.util.Date;

import com.imarcats.interfaces.client.v100.dto.types.ActivationStatus;
import com.imarcats.interfaces.client.v100.dto.types.AuditInformationDto;

/**
 * Market Operator creates and operates a one or more Markets
 * @author Adam
 */
public class MarketOperatorDto implements ActivatableMarketObjectDto {

	/**
	 * Code of the Market Operator - Primary Key of the Market Operator
	 * Required
	 */
//	@Column(name="CODE", length=DataLengths.MARKET_OBJECT_CODE_LENGTH)
	private String _code;

	/**
	 * Name of the Market Operator
	 * Required
	 */
//	@Column(name="NAME", nullable=false, length=DataLengths.MARKET_OBJECT_NAME_LENGTH)
	private String _name;

	/**
	 * Description of the Market Operator
	 * Required
	 */
//	@Column(name="DESCRIPTION", nullable=false, length=DataLengths.MARKET_OBJECT_DESCRIPTION_LENGTH)
	private String _description;
	
	/**
	 * User IDs behind this Market Operator 
	 * Optional
	 */
//	@Column(name="OWNER_USER_ID", nullable=false, length=DataLengths.USER_ID_LENGTH)
	private String _ownerUserID;
	
	/**
	 * Reference to the Market Operator's Business Entity 
	 * Required
	 */
//	@Column(name="BUSINESS_ENTITY_CODE", nullable=false, length=DataLengths.MARKET_OBJECT_CODE_LENGTH)
	private String _businessEntityCode;
	
	/**
	 * Reference to the Market Operator Agreement 
	 * Optional
	 * 
	 * Note: This could be a URL.
	 */
//	@Column(name="MARKET_OPERTOR_AGREEMENT", length=DataLengths.MARKET_OBJECT_DOCUMENT_LINK_LENGTH)
	private String _marketOperatorAgreement;
	
	/**
	 * Audit Information, like who created the Market Operator and when
	 * Optional
	 */
	private AuditInformationDto _creationAudit;

	/**
	 * Approval Audit Information, like who Approved the Market Operator and when
	 * Optional
	 */
	private AuditInformationDto _approvalAudit;

	/**
	 * Suspension Audit Information, like who Suspended the Market Operator and when
	 * Optional
	 */
	private AuditInformationDto _suspensionAudit;
	
	/**
	 * Change Audit Information, like who Changed the Market Operator and when
	 * Optional
	 */
	private AuditInformationDto _changeAudit;

	/**
	 * Supports the Activation Workflow of the Market Operator (Value from ActivationStatus)
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
	
	public String getOwnerUserID() {
		return _ownerUserID;
	}

	public void setOwnerUserID(String ownerUserID_) {		
		_ownerUserID = ownerUserID_;
	}

	public String getMarketOperatorAgreement() {
		return _marketOperatorAgreement;
	}

	public void setMarketOperatorAgreement(String marketOperatorAgreement_) {
		_marketOperatorAgreement = marketOperatorAgreement_;
	}

	public void setName(String name) {
		_name = name;
	}

	public String getName() {
		return _name;
	}

	public String getCode() {
		return _code;
	}

	public void setCode(String code_) {
		_code = code_;
	}

	public String getDescription() {
		return _description;
	}

	public void setDescription(String description_) {
		_description = description_;
	}
	
	public void setActivationStatus(ActivationStatus activationStatus) {
		_activationStatus = activationStatus;
	}

	public ActivationStatus getActivationStatus() {
		return _activationStatus;
	}

	public Date getLastUpdateTimestamp() {
		return _lastUpdateTimestamp;
	}

	public void setBusinessEntityCode(String businessEntityCode) {
		_businessEntityCode = businessEntityCode;
	}

	public String getBusinessEntityCode() {
		return _businessEntityCode;
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

	public void setLastUpdateTimestamp(Date lastUpdateTimestamp_) {
		_lastUpdateTimestamp = lastUpdateTimestamp_;
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
