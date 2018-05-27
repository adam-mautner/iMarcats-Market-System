package com.imarcats.interfaces.client.v100.messages;

/**
 * Constants for Messages
 * @author Adam
 */
public class MessagingConstants {

	public static final long HEARTBEAT_PERIOD_IN_MILLIS = 60 * 1000;

	public static final String BINARY_HTTP_CONTENT_TYPE = "application/octet-stream";
	
	public static final String CONTENT_TYPE = "Content-Type";
	public static final String METHOD_POST = "POST";
	
	private MessagingConstants() { /* static constants class */ }
	
}
