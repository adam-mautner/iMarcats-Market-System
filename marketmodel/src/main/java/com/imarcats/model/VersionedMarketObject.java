package com.imarcats.model;

/**
 * Versioned Market Object has a version number which needs to be incremented on every update 
 * 
 * @author Adam
 */
public interface VersionedMarketObject {

	public Long getVersionNumber();
	
	public void setVersionNumber(Long version_);
	
}
