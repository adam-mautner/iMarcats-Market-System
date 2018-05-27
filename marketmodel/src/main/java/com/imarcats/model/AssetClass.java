package com.imarcats.model;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Version;

import com.imarcats.model.meta.DataLengths;
import com.imarcats.model.types.ActivationStatus;
import com.imarcats.model.types.AuditInformation;
import com.imarcats.model.types.Property;
import com.imarcats.model.types.PropertyHolder;

/**
 * Defines an Asset Class for Instruments 
 * Asset Classes can be organized into Hierarchies by setting a parent for the Asset Class 
 * @author Adam
 */
@Entity
@Table(name="ASSET_CLASS")
public class AssetClass implements ActivatableMarketObject {

	private static final long serialVersionUID = 1L;
	public static final int MAX_NUMBER_OF_SUB_ASSAT_CLASSES_FOR_PARENT = 10;

	/**
	 * Name of the Asset Class
	 * Required
	 */
	@Id
	@Column(name="NAME", length=DataLengths.MARKET_OBJECT_NAME_LENGTH)
	private String _name; 
	
	/**
	 * Name of the Parent Asset Class, 
	 * Null (DatastoreKey.NULL) means this is a Top-Level Asset Class
	 * Optional
	 */
	@Column(name="PARENT_NAME", length=DataLengths.MARKET_OBJECT_NAME_LENGTH)
	private String _parentName; 
    
    /**
     * Description of the Asset Class
     * Required
     */
	@Column(name="DESCRIPTION", nullable=false, length=DataLengths.MARKET_OBJECT_DESCRIPTION_LENGTH)
	private String _description;
	
	/**
	 * Properties of the Asset Class, defines details like Net Worth required for trading this Asset Class
	 * Optional
	 */
	@OneToOne(cascade=CascadeType.ALL, orphanRemoval=true)
	@JoinColumn(name="PROPERTY_HOLDER_ID")
    private PropertyHolder _propertyHolder = new PropertyHolder();
	
    /**
     * Date and Time, when the Object was Last Updated
     * Required
     */
	@Column(name="LAST_UPDATE_TIMESTAMP", nullable=false)
	private Timestamp _lastUpdateTimestamp;
	
	/**
	 * Version of the object in the datastore 
	 */
	@Version
	@Column(name="VERSION")
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
	public Property[] getProperties() {
		return _propertyHolder.getProperties();
	}
	
	public void setProperties(Property[] properties_) {
		clearProperties();
		if(properties_ != null) {			
			for (Property property : properties_) {
				addProperty(property);
			}
		}
	}
	
	public void addProperty(Property property_) {
		_propertyHolder.addProperty(property_);
	}
	
	public void deleteProperty(Property property_) {
		_propertyHolder.deleteProperty(property_);
	}
	
	public void clearProperties() {
		_propertyHolder.clearProperties();
	}

	public void updateLastUpdateTimestamp() {
		_lastUpdateTimestamp = new Timestamp(System.currentTimeMillis());
	}
	
	@Override
	public ActivationStatus getActivationStatus() {
		return ActivationStatus.Created;
	}

	@Override
	public AuditInformation getApprovalAudit() {
		return null;
	}

	@Override
	public AuditInformation getChangeAudit() {
		return null;
	}

	@Override
	public String getCode() {
		return getName();
	}

	@Override
	public AuditInformation getCreationAudit() {
		return null;
	}

	@Override
	public AuditInformation getRolloverAudit() {
		return null;
	}

	@Override
	public AuditInformation getSuspensionAudit() {
		return null;
	}

	@Override
	public void setActivationStatus(ActivationStatus activationStatus_) {
		// does nothing
	}

	public void setParentName(String parentName_) {
		_parentName = parentName_;
	}

	public String getParentName() {
		return _parentName;
	}

	// TODO: change to Timestamp
	@Override
	public Date getLastUpdateTimestamp() {
		return _lastUpdateTimestamp;
	}

	public PropertyHolder getPropertyHolder() {
		return _propertyHolder;
	}

	public void setPropertyHolder(PropertyHolder propertyHolder_) {
		_propertyHolder = propertyHolder_;
	}

	public void setLastUpdateTimestamp(Date lastUpdateTimestamp_) {
		_lastUpdateTimestamp = lastUpdateTimestamp_ != null ? new Timestamp(lastUpdateTimestamp_.getTime()) : null;
	}

	public Long getVersionNumber() {
		return _versionNumber;
	}

	public void setVersionNumber(Long version_) {
		_versionNumber = version_;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((_description == null) ? 0 : _description.hashCode());
		result = prime
				* result
				+ ((_lastUpdateTimestamp == null) ? 0 : _lastUpdateTimestamp
						.hashCode());
		result = prime * result + ((_name == null) ? 0 : _name.hashCode());
		result = prime * result
				+ ((_parentName == null) ? 0 : _parentName.hashCode());
		result = prime * result
				+ ((_propertyHolder == null) ? 0 : _propertyHolder.hashCode());
		result = prime * result
				+ ((_versionNumber == null) ? 0 : _versionNumber.hashCode());
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
		AssetClass other = (AssetClass) obj;
		if (_description == null) {
			if (other._description != null)
				return false;
		} else if (!_description.equals(other._description))
			return false;
		if (_lastUpdateTimestamp == null) {
			if (other._lastUpdateTimestamp != null)
				return false;
		} else if (!_lastUpdateTimestamp.equals(other._lastUpdateTimestamp))
			return false;
		if (_name == null) {
			if (other._name != null)
				return false;
		} else if (!_name.equals(other._name))
			return false;
		if (_parentName == null) {
			if (other._parentName != null)
				return false;
		} else if (!_parentName.equals(other._parentName))
			return false;
		if (_propertyHolder == null) {
			if (other._propertyHolder != null)
				return false;
		} else if (!_propertyHolder.equals(other._propertyHolder))
			return false;
		if (_versionNumber == null) {
			if (other._versionNumber != null)
				return false;
		} else if (!_versionNumber.equals(other._versionNumber))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "AssetClass [_name=" + _name + ", _parentName=" + _parentName
				+ ", _description=" + _description + ", _propertyHolder="
				+ _propertyHolder + ", _lastUpdateTimestamp="
				+ _lastUpdateTimestamp + ", _versionNumber=" + _versionNumber + "]";
	}

}
