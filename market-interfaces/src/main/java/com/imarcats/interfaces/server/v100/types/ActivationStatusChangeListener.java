package com.imarcats.interfaces.server.v100.types;

import com.imarcats.model.types.ActivationStatus;

/**
 * Notifies about Change of Activation Status
 * @author Adam
 */
public interface ActivationStatusChangeListener {

	/**
	 * Called on Change of Activation Status of the Market
	 * @param changedObject_ Object being Changed
	 * @param newActivationStatus_ New Activation Status (Value from ActivationStatus)
	 */
	public void activationStatusChanged(Object changedObject_, ActivationStatus newActivationStatus_);
	
}
