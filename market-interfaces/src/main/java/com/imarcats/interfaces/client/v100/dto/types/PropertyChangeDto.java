package com.imarcats.interfaces.client.v100.dto.types;

import com.imarcats.interfaces.client.v100.dto.MarketModelObjectDto;

/**
 * Change of Property
 * @author Adam
 */
public interface PropertyChangeDto extends MarketModelObjectDto {

	/**
	 * Clones Property Change to a new Property change Object 
	 * @return New Property Change Object with the value
	 */
	public PropertyChangeDto clonePropertyChange();
	
}
