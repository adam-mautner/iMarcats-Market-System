package com.imarcats.model.mutators;

import com.imarcats.model.MarketModelObject;

/**
 * Change of Property
 * @author Adam
 */
public interface PropertyChange extends MarketModelObject {

	/**
	 * Clones Property Change to a new Property change Object 
	 * @return New Property Change Object with the value
	 */
	public PropertyChange clonePropertyChange();
	
}
