package com.imarcats.internal.server.infrastructure.notification;
/**
 * Exception for Listener Specific Runtime Exceptions 
 * @author Adam
 */
@SuppressWarnings("serial")
public class ListenerRuntimeException extends RuntimeException {

	public ListenerRuntimeException() {
		super();
	}

	public ListenerRuntimeException(String arg0_, Throwable arg1_) {
		super(arg0_, arg1_);
	}

	public ListenerRuntimeException(String arg0_) {
		super(arg0_);
	}

	public ListenerRuntimeException(Throwable arg0_) {
		super(arg0_);
	}

}