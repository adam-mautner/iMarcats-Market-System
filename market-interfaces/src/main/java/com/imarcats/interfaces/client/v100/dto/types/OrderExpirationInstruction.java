package com.imarcats.interfaces.client.v100.dto.types;


/**
 * Defines the Order Expiration Instruction Type 
 * @author Adam
 */
public enum OrderExpirationInstruction {

	GoodTillCancel,
	DayOrder,
	ImmediateOrCancel; // called FillOrKill, if the ExecuteEntireOrderAtOnce Property is set to true

}