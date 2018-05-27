package com.imarcats.model.mutators;

import com.imarcats.model.types.Property;

/**
 * Changes a Property on an Object
 * @author Adam
 *
 * @param <Y> Object 
 * @param <T> Property
 */
public interface ObjectPropertyMutator<Y, T extends Property> {
	public void changeProperty(Y object_, T property_);
}
