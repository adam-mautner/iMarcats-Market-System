package com.imarcats.model.types;


/**
 * Defines the Order Expiration Instruction Type 
 * @author Adam
 */
public enum OrderExpirationInstruction {

	GoodTillCancel,
	DayOrder,
	ImmediateOrCancel; // called FillOrKill, if the ExecuteEntireOrderAtOnce Property is set to true

}