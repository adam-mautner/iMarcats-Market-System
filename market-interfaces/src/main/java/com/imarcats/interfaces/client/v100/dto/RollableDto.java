package com.imarcats.interfaces.client.v100.dto;

import java.util.Date;
import java.util.List;

import com.imarcats.interfaces.client.v100.dto.types.AuditInformationDto;
import com.imarcats.interfaces.client.v100.dto.types.PropertyDto;
import com.imarcats.interfaces.client.v100.dto.types.UnderlyingType;

/**
 * Rollable Object, which is subject to a Roll-over Process
 * @author Adam
 */
public interface RollableDto extends ActivatableMarketObjectDto {

	/**
	 * @return if this Object is set to be rollable  
	 */
	public boolean getRollable();
	
	/**
	 * @return Code of the Underlying Object
	 */
	public String getUnderlyingCode();

	/**
	 * @return Code of the Underlying Type
	 */
	public UnderlyingType getUnderlyingType();
	
	/**
	 * @return Code of the Object this Object was rolled from 
	 */
	public String getCodeRolledFrom();

	/**
	 * Sets Code of the Object this Object was rolled from 
	 * @param codeRolledFrom_ Code that this Object was rolled over from 
	 */
	public void setCodeRolledFrom(String codeRolledFrom_);
	
	/**
	 * Sets Activation Date of the Object
	 * @param activationDate_ Activation Date 
	 */
	public void setActivationDate(Date activationDate_);
	
	/**
	 * @return the list of Rollable Properties 
	 */
	public List<String> getRollablePropertyNames();
	
	/**
	 * @return Properties of the Object
	 */
	public PropertyDto[] getProperties(); 
	
	/**
	 * Sets Creation Audit
	 * @param creationAudit_ Creation Audit
	 */
	public void setCreationAudit(AuditInformationDto creationAudit_);
	
	/**
	 * Sets Rollover Audit
	 * @param rolloverAudit_ Rollover Audit
	 */
	public void setRolloverAudit(AuditInformationDto rolloverAudit_);
	
	/**
	 * Sets Code of the Object
	 * @param code_ Code
	 */	
	public void setNewCode(String code_);
}
