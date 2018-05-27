package com.imarcats.interfaces.client.v100.dto.types;

import java.io.Serializable;


/**
 * Holds all the Data for a Permission Request 
 * @author Adam
 *
 */
public class PermissionRequestDto implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private PermissionClass _permissionClass;
	private PermissionType _permissionType;
	private UserTypeOfPermission _userType;
	private String _userOrGroupID;
	private String _objectID;
	private String _objectClass;
	private boolean _withGrant;
	
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
	public UserTypeOfPermission getUserType() {
		return _userType;
	}
	public void setUserType(UserTypeOfPermission userType_) {
		_userType = userType_;
	}
	public String getUserOrGroupID() {
		return _userOrGroupID;
	}
	public void setUserOrGroupID(String userOrGroupID_) {
		_userOrGroupID = userOrGroupID_;
	}
	public String getObjectID() {
		return _objectID;
	}
	public void setObjectID(String objectID_) {
		_objectID = objectID_;
	}
	public String getObjectClass() {
		return _objectClass;
	}
	public void setObjectClass(String objectClass_) {
		_objectClass = objectClass_;
	}
	public boolean getWithGrant() {
		return _withGrant;
	}
	public void setWithGrant(boolean withGrant_) {
		_withGrant = withGrant_;
	}
	@Override
	public String toString() {
		return "PermissionRequest [_objectClass=" + _objectClass
				+ ", _objectID=" + _objectID + ", _permissionClass="
				+ _permissionClass + ", _permissionType=" + _permissionType
				+ ", _userOrGroupID=" + _userOrGroupID + ", _userType="
				+ _userType + ", _withGrant=" + _withGrant + "]";
	}
}
