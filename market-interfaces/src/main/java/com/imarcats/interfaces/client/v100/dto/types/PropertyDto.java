package com.imarcats.interfaces.client.v100.dto.types;

import com.imarcats.interfaces.client.v100.dto.MarketModelObjectDto;


/**
 * Interface to mark Properties of Products and Markets
 * @author Adam
 */
public interface PropertyDto extends MarketModelObjectDto {

	/**
	 * @return name of the Property
	 */
	public String getName();
	
	/**
	 * @return type of this property (Value from PropertyType)
	 */
	public PropertyType getPropertyType();
}
