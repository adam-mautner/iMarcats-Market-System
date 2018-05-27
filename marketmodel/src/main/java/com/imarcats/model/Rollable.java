package com.imarcats.model;

import java.util.Date;
import java.util.List;

import com.imarcats.model.types.AuditInformation;
import com.imarcats.model.types.Property;
import com.imarcats.model.types.UnderlyingType;

/**
 * Rollable Object, which is subject to a Roll-over Process
 * @author Adam
 */
public interface Rollable extends ActivatableMarketObject {

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
	public Property[] getProperties(); 
	
	/**
	 * Sets Creation Audit
	 * @param creationAudit_ Creation Audit
	 */
	public void setCreationAudit(AuditInformation creationAudit_);
	
	/**
	 * Sets Rollover Audit
	 * @param rolloverAudit_ Rollover Audit
	 */
	public void setRolloverAudit(AuditInformation rolloverAudit_);
	
	/**
	 * Sets Code of the Object
	 * @param code_ Code
	 */	
	public void setNewCode(String code_);
}
