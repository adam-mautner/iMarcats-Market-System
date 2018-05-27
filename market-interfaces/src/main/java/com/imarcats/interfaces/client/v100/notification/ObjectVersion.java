package com.imarcats.interfaces.client.v100.notification;

import java.io.Serializable;


/**
 * Information about the Version of the Updated Object 
 * @author Adam
 *
 */
public class ObjectVersion implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * Version number of the Object, updated every time any Object field is updated 
	 * If overflown is true version just restarted from 0
	 * Required
	 */
	private Long _version = 0L;

	public Long getVersion() {
		return _version;
	}

	public void setVersion(Long version_) {
		_version = version_;
	}
}
