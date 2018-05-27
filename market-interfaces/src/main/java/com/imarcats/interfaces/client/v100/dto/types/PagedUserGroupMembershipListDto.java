package com.imarcats.interfaces.client.v100.dto.types;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Paged list of Group Memberships 
 * @author Adam
 */
public class PagedUserGroupMembershipListDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private UserGroupMembershipDto[] _userGroupMemberships;
	private String _cursorString;
	private int _maxNumberOfUserGroupMembershipsOnPage;
	
	public UserGroupMembershipDto[] getUserGroupMemberships() {
		return _userGroupMemberships;
	}
	public void setUserGroupMemberships(UserGroupMembershipDto[] userGroupMemberships_) {
		_userGroupMemberships = userGroupMemberships_;
	}
	public String getCursorString() {
		return _cursorString;
	}
	public void setCursorString(String cursorString_) {
		_cursorString = cursorString_;
	}
	public int getMaxNumberOfUserGroupMembershipsOnPage() {
		return _maxNumberOfUserGroupMembershipsOnPage;
	}
	public void setMaxNumberOfUserGroupMembershipsOnPage(
			int maxNumberOfUserGroupMembershipsOnPage_) {
		_maxNumberOfUserGroupMembershipsOnPage = maxNumberOfUserGroupMembershipsOnPage_;
	}
	@Override
	public String toString() {
		return "PagedUserGroupMembershipList [_cursorString=" + _cursorString
				+ ", _maxNumberOfUserGroupMembershipsOnPage="
				+ _maxNumberOfUserGroupMembershipsOnPage
				+ ", _userGroupMemberships="
				+ Arrays.toString(_userGroupMemberships) + "]";
	}
	
	
}
