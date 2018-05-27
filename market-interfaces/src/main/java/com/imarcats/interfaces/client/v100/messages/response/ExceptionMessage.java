package com.imarcats.interfaces.client.v100.messages.response;

import com.imarcats.interfaces.client.v100.exception.ExceptionType;
import com.imarcats.interfaces.client.v100.messages.MessageBase;

/**
 * Message to Return Exception details
 * @author Adam
 */
public class ExceptionMessage extends MessageBase {

	private static final long serialVersionUID = 1L;
	
	private String _message; 
	
	private ExceptionMessageType _exceptionMessageType;
	
	private ExceptionType _exceptionType;
	
	private String _languageKey;
	
	private String[] _relatedObjectsString;

	public ExceptionMessageType getExceptionMessageType() {
		return _exceptionMessageType;
	}

	public void setExceptionMessageType(ExceptionMessageType exceptionMessageType_) {
		_exceptionMessageType = exceptionMessageType_;
	}

	public ExceptionType getExceptionType() {
		return _exceptionType;
	}

	public void setExceptionType(ExceptionType exceptionType_) {
		_exceptionType = exceptionType_;
	}

	public String getLanguageKey() {
		return _languageKey;
	}

	public void setLanguageKey(String languageKey_) {
		_languageKey = languageKey_;
	}

	public String[] getRelatedObjectsString() {
		return _relatedObjectsString;
	}

	public void setRelatedObjectsString(String[] relatedObjectsString_) {
		_relatedObjectsString = relatedObjectsString_;
	}

	public void setMessage(String message) {
		_message = message;
	}

	public String getMessage() {
		return _message;
	}
}
