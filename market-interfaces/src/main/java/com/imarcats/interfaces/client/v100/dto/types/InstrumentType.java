package com.imarcats.interfaces.client.v100.dto.types;


/**
 * Defines the Type of the Instrument
 * @author Adam
 */
public enum InstrumentType {

	/** Spot - Underlying is a Product from Product Store,
	 *  No Margin Account needed */
	Spot,
	/** Derivative - Underlying is an Instrument from Instrument Store, 
	 *  Margin needed */
	Derivative;
	
}
