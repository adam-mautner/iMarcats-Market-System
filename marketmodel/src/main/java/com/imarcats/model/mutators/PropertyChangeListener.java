package com.imarcats.model.mutators;


/**
 * Call-back Listener of Property Changes
 * 
 * @author Adam
 *
 */
public interface PropertyChangeListener {
	/**
	 * Called on the change of a Property 
	 * @param changedObject_ Object being Changed
	 * @param propertyChange_ Property Change
	 */
	public void propertyChanged(Object changedObject_, PropertyChange propertyChange_);
}
