package com.imarcats.interfaces.client.v100.dto;

import java.util.Date;

import com.imarcats.interfaces.client.v100.dto.types.ActivationStatus;
import com.imarcats.interfaces.client.v100.dto.types.AuditInformationDto;
import com.imarcats.interfaces.client.v100.dto.types.PropertyDto;

/**
 * Defines an Asset Class for Instruments 
 * Asset Classes can be organized into Hierarchies by setting a parent for the Asset Class 
 * @author Adam
 */
public class AssetClassDto implements ActivatableMarketObjectDto {

	/**
	 * Name of the Asset Class
	 * Required
	 */
//	@Column(name="NAME", length=DataLengths.MARKET_OBJECT_NAME_LENGTH)
	private String _name; 
	
	/**
	 * Name of the Parent Asset Class, 
	 * Null (DatastoreKey.NULL) means this is a Top-Level Asset Class
	 * Optional
	 */
//	@Column(name="PARENT_NAME", length=DataLengths.MARKET_OBJECT_NAME_LENGTH)
	private String _parentName; 
    
    /**
     * Description of the Asset Class
     * Required
     */
//	@Column(name="DESCRIPTION", nullable=false, length=DataLengths.MARKET_OBJECT_DESCRIPTION_LENGTH)
	private String _description;
	
	/**
	 * Properties of the Asset Class, defines details like Net Worth required for trading this Asset Class
	 * Optional
	 */
    private PropertyDto[] _properties = new PropertyDto[0];
	
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
	
	public void setParentName(String parentName_) {
		_parentName = parentName_;
	}

	public String getParentName() {
		return _parentName;
	}

	public Date getLastUpdateTimestamp() {
		return _lastUpdateTimestamp;
	}

	public PropertyDto[] getProperties() {
		return _properties;
	}

	public void setProperties(PropertyDto[] properties_) {
		_properties = properties_;
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
	public ActivationStatus getActivationStatus() {
		return ActivationStatus.Created;
	}

	@Override
	public AuditInformationDto getApprovalAudit() {
		return null;
	}

	@Override
	public AuditInformationDto getChangeAudit() {
		return null;
	}

	@Override
	public String getCode() {
		return getName();
	}

	@Override
	public AuditInformationDto getCreationAudit() {
		return null;
	}

	@Override
	public AuditInformationDto getRolloverAudit() {
		return null;
	}

	@Override
	public AuditInformationDto getSuspensionAudit() {
		return null;
	}

	@Override
	public void setActivationStatus(ActivationStatus activationStatus_) {
		// does nothing
	}
}
