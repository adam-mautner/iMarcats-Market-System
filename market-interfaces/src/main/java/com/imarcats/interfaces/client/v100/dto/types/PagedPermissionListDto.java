package com.imarcats.interfaces.client.v100.dto.types;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Paged list of Permissions
 * @author Adam
 *
 */
public class PagedPermissionListDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private PermissionDto[] _permissions;
	private String _cursorString;
	private int _maxNumberOfPermissionsOnPage;
	
	public PermissionDto[] getPermissions() {
		return _permissions;
	}
	public void setPermissions(PermissionDto[] permissions_) {
		_permissions = permissions_;
	}
	public String getCursorString() {
		return _cursorString;
	}
	public void setCursorString(String cursorString_) {
		_cursorString = cursorString_;
	}
	public int getMaxNumberOfPermissionsOnPage() {
		return _maxNumberOfPermissionsOnPage;
	}
	public void setMaxNumberOfPermissionsOnPage(int maxNumberOfPermissionsOnPage_) {
		_maxNumberOfPermissionsOnPage = maxNumberOfPermissionsOnPage_;
	}
	@Override
	public String toString() {
		return "PagedPermissionList [_cursorString=" + _cursorString
				+ ", _maxNumberOfPermissionsOnPage="
				+ _maxNumberOfPermissionsOnPage + ", _permissions="
				+ Arrays.toString(_permissions) + "]";
	}
	
	
}
