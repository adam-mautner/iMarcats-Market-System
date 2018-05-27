package com.imarcats.model.types;

import com.imarcats.model.MarketModelObject;

/**
 * Interface to mark Properties of Products and Markets
 * @author Adam
 */
public interface Property extends MarketModelObject {

	/**
	 * @return name of the Property
	 */
	public String getName();
	
	/**
	 * @return type of this property (Value from PropertyType)
	 */
	public PropertyType getPropertyType();
	
	/**
	 * Clones Property to a new Property Object 
	 * @return New Property Object with the value
	 */
	public Property cloneProperty();

	/**
	 * @param otherProperty_ Other Property
	 * @return if this property equals to the other 
	 */
	public boolean equalsProperty(Property otherProperty_);
}
