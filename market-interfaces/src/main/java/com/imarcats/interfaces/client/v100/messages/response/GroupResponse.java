package com.imarcats.interfaces.client.v100.messages.response;

import com.imarcats.interfaces.client.v100.dto.types.UserGroupMembershipDto;
import com.imarcats.interfaces.client.v100.messages.MessageBase;

/**
 * Response with a Group 
 * @author Adam
 *
 */
public class GroupResponse extends MessageBase {

	private static final long serialVersionUID = 1L;

	private UserGroupMembershipDto _userGroupMembership;

	public void setUserGroupMembership(UserGroupMembershipDto userGroupMembership) {
		_userGroupMembership = userGroupMembership;
	}

	public UserGroupMembershipDto getUserGroupMembership() {
		return _userGroupMembership;
	}
}
