package com.imarcats.interfaces.client.v100.dto.types;



/**
 * Defines the Order State
 * @author Adam
 */
public enum OrderState {
	
	Created,
	PendingSubmit,
	WaitingSubmit,
	Submitted,
	Executed,
	Canceled,
	Deleted;

}
