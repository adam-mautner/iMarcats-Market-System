package com.imarcats.interfaces.client.v100.dto.types;


/**
 * Defines the Activation Status of the Products, Markets, Instruments
 * @author Adam
 */
public enum ActivationStatus {
	Created,
	Approved,
	
	// used for Markets only
	Activated, 
	// used for Markets only
	Deactivated, 
	
	Suspended,
	Deleted;
}