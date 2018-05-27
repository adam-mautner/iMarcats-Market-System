package com.imarcats.model.mutators;

import com.imarcats.model.types.Property;

/**
 * Transforms Properties to different Properties and back 
 * (to transfer them through channels that do not support certain Properties).
 * @author Adam
 *
 */
public interface PropertyTransformer {
	
	public static final PropertyTransformer NULL = new PropertyTransformer() {
		
		@Override
		public Property transformBack(Property property_) {
			return property_;
		}
		
		@Override
		public Property transform(Property property_) {
			return property_;
		}
	};
	
	/**
	 * Transforms Property to other Property (possibly String Property)
	 * @param property_ Property 
	 * @return Transformed Property 
	 */
	public Property transform(Property property_);
	
	/**
	 * Transforms Property back (possibly String Property to Object Property)
	 * @param property_ Transformed Property 
	 * @return Original Property 
	 */
	public Property transformBack(Property property_);
}
