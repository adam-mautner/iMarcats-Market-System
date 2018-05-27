package com.imarcats.model.types;

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
	
}
