package com.imarcats.model.mutators;

import com.imarcats.model.types.Property;

/**
 * Gets a Property List of an Object
 * @author Adam
 *
 * @param <T> Object
 */
public interface ObjectPropertyListAccessor<T> {
	public Property[] getList(T object_);
	
	public void addToList(T object_, Property property_);
	
	public void deleteFromList(T object_, Property property_);
	
	public void clearList(T object_);
}
