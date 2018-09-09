package com.imarcats.interfaces.client.v100.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.imarcats.interfaces.client.v100.dto.types.ActivationStatus;
import com.imarcats.interfaces.client.v100.dto.types.AuditInformationDto;


/**
 * Interface for Activatable Market Objects 
 * @author Adam
 *
 */
public interface ActivatableMarketObjectDto extends MarketModelObjectDto {

	/**
	 * @return Code of the Object
	 */
	@JsonIgnore
	public String getCode();

	/**
	 * @return Name of the Object
	 */
	public String getName();
	
	/**
	 * @return Description of the Object
	 */
	public String getDescription();
	
	/**
	 * @return Activation Status of the Object
	 */
	public ActivationStatus getActivationStatus();

	/**
	 * Sets Activation Status of the Object
	 * @param activationStatus_ Activation Status
	 */	
	public void setActivationStatus(ActivationStatus activationStatus_);
	
	/**
	 * @return Creation Audit
	 */
	public AuditInformationDto getCreationAudit();
	
	/**
	 * @return Approval Audit
	 */
	public AuditInformationDto getApprovalAudit();
	
	/**
	 * @return Change Audit
	 */
	public AuditInformationDto getChangeAudit();
	
	/**
	 * @return Rollover Audit
	 */
	public AuditInformationDto getRolloverAudit();
	
	/**
	 * @return Suspension Audit
	 */
	public AuditInformationDto getSuspensionAudit();
	
	/**
	 * @return Return the Date and Time, when the Object was Last Updated
	 */
	public Date getLastUpdateTimestamp();
}
