package com.imarcats.model;

import java.util.Date;

import com.imarcats.model.types.ActivationStatus;
import com.imarcats.model.types.AuditInformation;

/**
 * Interface for Activatable Market Objects 
 * @author Adam
 *
 */
public interface ActivatableMarketObject extends MarketModelObject {

	/**
	 * @return Code of the Object
	 */
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
	public AuditInformation getCreationAudit();
	
	/**
	 * @return Approval Audit
	 */
	public AuditInformation getApprovalAudit();
	
	/**
	 * @return Change Audit
	 */
	public AuditInformation getChangeAudit();
	
	/**
	 * @return Rollover Audit
	 */
	public AuditInformation getRolloverAudit();
	
	/**
	 * @return Suspension Audit
	 */
	public AuditInformation getSuspensionAudit();
	
	/**
	 * @return Return the Date and Time, when the Object was Last Updated
	 */
	public Date getLastUpdateTimestamp();
}
