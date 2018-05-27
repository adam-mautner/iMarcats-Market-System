package com.imarcats.interfaces.client.v100.dto.types;

/**
 * Type of the Permission
 * 
 * @author Adam
 */
public enum PermissionType {

	ObjectAdministration,
	ObjectApprover,
	
	// TODO: Remove ObjectCreation
	ObjectCreation,
	// TODO: Remove ObjectManipulation
	ObjectManipulation, 
	
	Operations, 
	Trade;
	
}
