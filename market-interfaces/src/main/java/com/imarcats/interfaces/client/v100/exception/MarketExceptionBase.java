package com.imarcats.interfaces.client.v100.exception;

import com.imarcats.interfaces.client.v100.messages.response.ExceptionMessage;
import com.imarcats.interfaces.client.v100.messages.response.ExceptionMessageType;

/**
 * Base Class for Market (Runtime) Exceptions 
 * @author Adam
 */
@SuppressWarnings("serial")
public abstract class MarketExceptionBase extends RuntimeException {

	/**
	 * Language Key to find the I18N version of this Error 
	 */
	private final String _languageKey;
	
	/**
	 * Type of the Exception - (Value from ExceptionType)
	 */
	private final ExceptionType _exceptionType;
	
	/**
	 * Objects Related to this Exception
	 */
	private final Object[] _relatedObjects;
	
	protected MarketExceptionBase(ExceptionMessage message) {
		this(message.getLanguageKey(), message.getExceptionType(), message.getMessage(), null, message.getRelatedObjectsString());
	}
	
	protected MarketExceptionBase(String languageKey_, ExceptionType exceptionType_, String messageInLog_, Throwable cause_, Object[] relatedObjects_) {
		super(messageInLog_, cause_);
		_languageKey = languageKey_;
		_exceptionType = exceptionType_;
		_relatedObjects = relatedObjects_;
	}

	protected MarketExceptionBase(String languageKey_, ExceptionType exceptionType_, String messageInLog_) {
		this(languageKey_, exceptionType_, messageInLog_, null, null);
	}

	// Do NOT use these constructors - Just Restricts Access
	@SuppressWarnings("unused")
	private MarketExceptionBase() {
		this(null, ExceptionType.IllegalArgumentException, null, null, null);
	}

	@SuppressWarnings("unused")
	private MarketExceptionBase(String message_, Throwable cause_) {
		this(message_, ExceptionType.IllegalArgumentException, null, cause_, null);
	}

	@SuppressWarnings("unused")
	private MarketExceptionBase(String message_) {
		this(message_, ExceptionType.IllegalArgumentException, null, null, null);
	}

	@SuppressWarnings("unused")
	private MarketExceptionBase(Throwable cause_) {
		this(null, ExceptionType.IllegalArgumentException, null, cause_, null);
	}
	// End of "Do NOT use these constructors"

	public String getLanguageKey() {
		return _languageKey;
	}

	public ExceptionType getExceptionType() {
		return _exceptionType;
	}

	public Object[] getRelatedObjects() {
		return _relatedObjects;
	}
	
	/**
	 * @return the exception in message format 
	 */
	public ExceptionMessage toExceptionMessage() {
		ExceptionMessage message = new ExceptionMessage();
		
		message.setExceptionMessageType(getExceptionMessageType());
		message.setExceptionType(getExceptionType());
		message.setLanguageKey(getLanguageKey());
		message.setMessage(getMessage());
		
		if(getRelatedObjects() != null) {
			String[] objectsStr = new String[getRelatedObjects().length];
			for (int i = 0; i < objectsStr.length; i++) {
				objectsStr[i] = getRelatedObjects()[i] != null ? getRelatedObjects()[i].toString() : ""; 
			}
			
			message.setRelatedObjectsString(objectsStr);
		}
		
		return message;
	}
	
	protected abstract ExceptionMessageType getExceptionMessageType();
}
