package com.imarcats.model.types;


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
