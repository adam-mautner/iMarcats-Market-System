package com.imarcats.interfaces.server.v100.service;

/**
 * Exception for Errors during Connection  
 * @author Adam
 */
@SuppressWarnings("serial")
public class ConnectionException extends Exception {

	public ConnectionException() {
		super();
	}

	public ConnectionException(String message_, Throwable cause_) {
		super(message_, cause_);
	}

	public ConnectionException(String message_) {
		super(message_);
	}

	public ConnectionException(Throwable cause_) {
		super(cause_);
	}

}
