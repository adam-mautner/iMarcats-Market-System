package com.imarcats.internal.server.infrastructure.i18n.lookup;

import java.io.IOException;

import com.imarcats.market.i18n.client.language.MarketExceptionLanguageConstants;
import com.imarcats.market.i18n.client.language.MarketMessageLanguageConstants;

/** 
 * Creates Constants and Messages i18n Properties
 * @author Adam
 */
@SuppressWarnings("unchecked")
public class Language {

	private static final Class MESSAGES_TYPE = MarketMessageLanguageConstants.class;
	private static final Class SYSTEM_MESSAGES_TYPE = MarketMessageLanguageConstants.class;
	private static final Class EXCEPTION_TYPE = MarketExceptionLanguageConstants.class;
	
	private static final String EXTENSION = "properties";
	
	public static final String DEFAULT_LANGUAGE = null;
	
	private static final String ENCODING_NAME = "UTF-8";
	
	private Language() { /* factory class */ }
	
	private static String getPropertyFileName(Class type_, String languageCode_) {
		String languageCode = "";
		if(languageCode_ != null) {
			languageCode = "_" + languageCode_;
		}
		
		return type_.getSimpleName() + languageCode + "." + EXTENSION;
	}
	
	public static Properties getMessages(String languageCode_) throws IOException {
		return getProperties(languageCode_, MESSAGES_TYPE);
	}
	
	public static Properties getSystemMessages(String languageCode_) throws IOException {
		return getProperties(languageCode_, SYSTEM_MESSAGES_TYPE);
	}

	public static Properties getExceptions(String languageCode_) throws IOException {
		return getProperties(languageCode_, EXCEPTION_TYPE);
	}
	
	private static Properties getProperties(String languageCode_, Class type_)
			throws IOException {
		String propertyFilePath = getPropertyFileName(type_, languageCode_);
		return Properties.loadProperties(type_, propertyFilePath, ENCODING_NAME);
	}
}
