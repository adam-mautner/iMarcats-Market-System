package com.imarcats.interfaces.client.v100.dto.types;

import com.imarcats.interfaces.client.v100.dto.MarketModelObjectDto;


/**
 * Permission in the Market System
 * 
 * @author Adam
 */
public class PermissionDto implements MarketModelObjectDto {
		
	/**
	 * Class of the Permission 
	 */
	private PermissionClass _permissionClass;
	
	/**
	 * Type of the Permission 
	 */
    private PermissionType _permissionType;
    
	/**
	 * User can grant his/her permission to someone else 
	 */
    private boolean _withGrant;
    
	/**
     * Key of the Object, that has the permission 
     */
	// choice 
//	@Column(name="CODE_KEY", length=DataLengths.MARKET_OBJECT_CODE_LENGTH)
    private String _codeKey;
	private Long _idKey;
	// end of choice
	
	/**
	 * Class Name of the Object (Market.class.getSimpleName())
	 */
//	@Column(name="OBJECT_CLASS_NAME", nullable=false, length=100)
	private String _objectClassName;
	
	/**
     * Type of the User that has the Permission 
     */
	private UserTypeOfPermission _userType;

	/**
     * User ID or Group ID that has the Permission 
     */
    private String _userIdOrGroupId;

	public PermissionClass getPermissionClass() {
		return _permissionClass;
	}

	public void setPermissionClass(PermissionClass permissionClass_) {
		_permissionClass = permissionClass_;
	}

	public PermissionType getPermissionType() {
		return _permissionType;
	}

	public void setPermissionType(PermissionType permissionType_) {
		_permissionType = permissionType_;
	}

	public boolean getWithGrant() {
		return _withGrant;
	}

	public void setWithGrant(boolean withGrant_) {
		_withGrant = withGrant_;
	}

	public UserTypeOfPermission getUserType() {
		return _userType;
	}

	public void setUserType(UserTypeOfPermission userType_) {
		_userType = userType_;
	}

	public String getUserIdOrGroupId() {
		return _userIdOrGroupId;
	}

	public void setUserIdOrGroupId(String userIdOrGroupId_) {
		_userIdOrGroupId = userIdOrGroupId_;
	}

	public String getObjectClassName() {
		return _objectClassName;
	}

	public void setObjectClassName(String objectClassName_) {
		_objectClassName = objectClassName_;
	}

	public String getCodeKey() {
		return _codeKey;
	}

	public void setCodeKey(String codeKey_) {
		_codeKey = codeKey_;
	}

	public Long getIdKey() {
		return _idKey;
	}

	public void setIdKey(Long idKey_) {
		_idKey = idKey_;
	}	
}
