package com.imarcats.interfaces.client.v100.messages.response;

import com.imarcats.interfaces.client.v100.dto.types.PagedUserGroupMembershipListDto;
import com.imarcats.interfaces.client.v100.messages.MessageBase;

/**
 * Response with Group List
 * @author Adam
 *
 */
public class GroupListResponse extends MessageBase {

	private static final long serialVersionUID = 1L;
	
	private PagedUserGroupMembershipListDto _userGroupMembershipList;

	public void setUserGroupMembershipList(PagedUserGroupMembershipListDto userGroupMembershipList) {
		_userGroupMembershipList = userGroupMembershipList;
	}

	public PagedUserGroupMembershipListDto getUserGroupMembershipList() {
		return _userGroupMembershipList;
	}
}
