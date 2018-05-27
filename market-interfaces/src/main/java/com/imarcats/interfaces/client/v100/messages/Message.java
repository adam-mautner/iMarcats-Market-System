package com.imarcats.interfaces.client.v100.messages;

import java.io.Serializable;

/**
 * Common Interface for Messages 
 * @author Adam
 */
public interface Message extends Serializable {
	public static final Message NULL_MESSAGE = new NullMessage();
}
