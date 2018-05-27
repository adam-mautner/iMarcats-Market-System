package com.imarcats.interfaces.client.v100.dto.types;

import com.imarcats.interfaces.client.v100.dto.MarketModelObjectDto;



/**
 * Defines a Membership in a User Group for a User
 * 
 * @author Adam
 *
 */
public class UserGroupMembershipDto implements MarketModelObjectDto {
	
	/**
	 * Name of the Group
	 */	
//	@Column(name="GROUP_NAME", nullable=false, length=DataLengths.USER_ID_LENGTH)
    private String _groupName;
	
	/**
	 * Name of the User
	 */	
//	@Column(name="USER_NAME", nullable=false, length=DataLengths.USER_ID_LENGTH)
    private String _userName;
    
	/**
	 * Type of the Membership within the Group
	 * 
	 * Note: This needs to be a String for Datastore to Query on this field
	 */	
    private MembershipType _membershipType;
    
	/**
	 * Roles of the Users within this Group 
	 * 
	 * Note: This needs to be a String for Datastore to Query on this field
	 */	
    private UserRole _userRole;
    
	/**
	 * If the Membership is approved by a Supervisor
	 * 
	 * Note: This needs to be a String for Datastore to Query on this field
	 */	
    private boolean _approved = Boolean.FALSE;
    
	public String getGroupName() {
		return _groupName;
	}

	public void setGroupName(String groupName_) {
		_groupName = groupName_;
	}

	public String getUserName() {
		return _userName;
	}

	public void setUserName(String userName_) {
		_userName = userName_;
	}

	public UserRole getUserRole() {
		return _userRole;
	}

	public void setUserRole(UserRole userRole_) {
		_userRole = userRole_;
	}
	
	public MembershipType getMembershipType() {
		return _membershipType;
	}

	public void setMembershipType(MembershipType membershipType_) {
		_membershipType = membershipType_;	
	}

	public boolean getApproved() {
		return _approved;
	}

	public void setApproved(boolean approved_) {
		_approved = approved_;
	}	
}
