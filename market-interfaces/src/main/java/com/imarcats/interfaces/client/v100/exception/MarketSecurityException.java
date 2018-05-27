package com.imarcats.interfaces.client.v100.exception;

import com.imarcats.interfaces.client.v100.messages.response.ExceptionMessage;
import com.imarcats.interfaces.client.v100.messages.response.ExceptionMessageType;

@SuppressWarnings("serial")
public class MarketSecurityException extends MarketExceptionBase {

	// Order Security
	public static final MarketSecurityException USER_TRIED_TO_SET_A_SYSTEM_CONTROLLED_PROPERTY = new MarketSecurityException(ExceptionLanguageKeys.USER_TRIED_TO_SET_A_SYSTEM_CONTROLLED_PROPERTY, ExceptionType.IllegalArgumentException, "User tried to set a System Controlled Property");

	// User Security
	public static final MarketSecurityException USER_ATTEMPTED_CROSS_SITE_SCRIPTING = new MarketSecurityException(ExceptionLanguageKeys.USER_ATTEMPTED_CROSS_SITE_SCRIPTING, ExceptionType.IllegalArgumentException, "User attempted Cross-site Scripting Attack");

	protected MarketSecurityException(ExceptionMessage message_) {
		super(message_);
	}
	
	/**
	 * Creates Market Security Exception 
	 * @param prototype_ Prototype Exception
	 * @param cause_ Cause for this Exception
	 * @param additionalLogInformation_ Additional Log Information
	 * @return Created Runtime Exception
	 */
	public static MarketSecurityException createExceptionWithDetails(MarketSecurityException prototype_, Throwable cause_, String additionalLogInformation_) {
		return new MarketSecurityException(
				prototype_.getLanguageKey(), 
				prototype_.getExceptionType(), 
				prototype_.getMessage() + 
					(additionalLogInformation_ != null || "".equals(additionalLogInformation_) 
					? (": " + additionalLogInformation_)
					: ""), 
				cause_, 
				null);
	
	}
	
	/**
	 * Creates Market Security Exception 
	 * @param prototype_ Prototype Exception
	 * @param cause_ Cause for this Exception
	 * @param relatedObjects_ Objects related to this Exception
	 * @return Created Runtime Exception
	 */
	public static MarketSecurityException createExceptionWithDetails(MarketSecurityException prototype_, Throwable cause_, Object[] relatedObjects_) {
		return new MarketSecurityException(
				prototype_.getLanguageKey(), 
				prototype_.getExceptionType(), 
				prototype_.getMessage(),
				null,
				relatedObjects_);
	}
	
	protected MarketSecurityException(String languageKey_, ExceptionType exceptionType_,
			String messageInLog_, Throwable cause_, Object[] relatedObjects_) {
		super(languageKey_, exceptionType_, messageInLog_, cause_, relatedObjects_);
	}

	public MarketSecurityException(String languageKey_, ExceptionType exceptionType_,
			String messageInLog_) {
		super(languageKey_, exceptionType_, messageInLog_);
	}	
	
	@Override
	protected ExceptionMessageType getExceptionMessageType() {
		return ExceptionMessageType.MarketSecurityException;
	}
}
