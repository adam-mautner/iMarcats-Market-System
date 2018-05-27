package com.imarcats.interfaces.client.v100.dto;

import java.io.Serializable;

import com.imarcats.interfaces.client.v100.dto.types.PropertyDto;

/**
 * Configuration Data for User and Type
 * @author Adam
 */
public class ConfigurationDataDto implements Serializable {

	/**
	 * Key of the Configuration, created as _userID|_configurationType
	 * Required
	 */
//	@Column(name="CONFIGURATION_CODE", length=100)
	private String _configurationCode;
	
    /**
     * User, where this configuration belongs
     * Required
     */
//	@Column(name="USER_ID", nullable=false, length=50)
    private String _userID;
    
    /**
     * Type of the configuration
     * Required
     */
//	@Column(name="TYPE", nullable=false, length=50)
    private String _type;
    
	/**
	 * Properties of the Configuration
	 * Optional
	 */
    private PropertyDto[] _properties;

	public String getConfigurationCode() {
		return _configurationCode;
	}

	public void setConfigurationCode(String configurationCode_) {
		_configurationCode = configurationCode_;
	}

	public String getUserID() {
		return _userID;
	}

	public void setUserID(String userID_) {
		_userID = userID_;
	}

	public String getType() {
		return _type;
	}

	public void setType(String type_) {
		_type = type_;
	}

	public PropertyDto[] getProperties() {
		return _properties;
	}

	public void setProperties(PropertyDto[] properties_) {
		_properties = properties_;
	}
    
}
