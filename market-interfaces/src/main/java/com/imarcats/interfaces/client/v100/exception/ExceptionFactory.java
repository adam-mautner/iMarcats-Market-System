package com.imarcats.interfaces.client.v100.exception;

import com.imarcats.interfaces.client.v100.messages.response.ExceptionMessage;
import com.imarcats.interfaces.client.v100.messages.response.ExceptionMessageType;

/**
 * Creates Exceptions
 * @author Adam
 */
public class ExceptionFactory {

	private ExceptionFactory() { /* static factory class */ }
	
	public static MarketExceptionBase createExceptionFromMessage(ExceptionMessage message_) {
		MarketExceptionBase exception = null;
		if(message_.getExceptionMessageType() == ExceptionMessageType.MarketRuntimeException) {
			exception = new MarketRuntimeException(message_);
		} else if(message_.getExceptionMessageType() == ExceptionMessageType.MarketSecurityException) {
			exception = new MarketSecurityException(message_);
		} else {
			new RuntimeException("Unknown Exception");
		}
		
		return exception;
	} 
	
}
