package com.imarcats.interfaces.client.v100.dto.types;

/**
 * Action Recorded on Audit Trail Entry
 * @author Adam
 *
 */
public enum AuditEntryAction {
	
	Created,
	Changed,
	Rolled,
	
	Approved,
	
	// used for Markets only
	Activated, 
	// used for Markets only
	Deactivated, 
	// used for Markets only
	Closed, 
	
	Suspended,
	Deleted,
	
	// used for Groups Only
	RequestedMembershipToGroup, 
	ApprovedMembershipToGroup,
	DeletedFromGroup, 
	
	// used for Groups Only
	GrantedPermission, 
	RevokedPermission;
	
}
