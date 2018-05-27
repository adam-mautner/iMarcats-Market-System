package com.imarcats.model;

/**
 * Changeable Order Property names
 * @author Adam
 */
public class OrderPropertyNames {

	// System Controlled	
	public static final String KEY_PROPERTY = "key"; 
	public static final String SUBMITTER_ID_PROPERTY = "submitterID"; 
	public static final String TARGET_MARKET_CODE_PROPERTY = "targetMarketCode"; 

	public static final String EXECUTED_SIZE_PROPERTY = "executedSize";
	public static final String STATE_PROPERTY = "state";
	public static final String SUBMISSION_DATE_PROPERTY = "submissionDate";
	public static final String CURRENT_STOP_QUOTE_PROPERTY = "currentStopQuote";
	public static final String CREATION_AUDIT_PROPERTY = "creationAudit";
	public static final String QUOTE_CHANGE_TRIGGER_KEY_PROPERTY = "quoteChangeTriggerKey";
	public static final String EXPIRATION_TRIGGER_ACTION_KEY_PROPERTY = "expirationTriggerActionKey";
	public static final String CANCELLATION_COMMENT_LANGUAGE_KEY_PROPERTY = "cancellationCommentLanguageKey";
	public static final String COMMISSION_CHARGED_PROPERTY = "commissionCharged";
	
	public static final String[] SYSTEM_PROPERTIES = {
		KEY_PROPERTY,
		SUBMITTER_ID_PROPERTY,
		TARGET_MARKET_CODE_PROPERTY, 
		EXECUTED_SIZE_PROPERTY, 
		STATE_PROPERTY, 
		SUBMISSION_DATE_PROPERTY, 
		CURRENT_STOP_QUOTE_PROPERTY, 
		CREATION_AUDIT_PROPERTY, 
		QUOTE_CHANGE_TRIGGER_KEY_PROPERTY, 
		EXPIRATION_TRIGGER_ACTION_KEY_PROPERTY, 
		CANCELLATION_COMMENT_LANGUAGE_KEY_PROPERTY, 
		COMMISSION_CHARGED_PROPERTY
	};
	
	// User Controlled
	public static final String SIDE_PROPERTY = "side"; 
	public static final String TYPE_PROPERTY = "type"; 
	public static final String LIMIT_QUOTE_VALUE_PROPERTY = "limitQuoteValue";
	public static final String SIZE_PROPERTY = "size"; 
	public static final String MINIMUM_SIZE_OF_EXECUTION_PROPERTY = "minimumSizeOfExecution";
	public static final String EXECUTE_ENTIRE_ORDER_AT_ONCE_PROPERTY = "executeEntireOrderAtOnce";
	public static final String DISPLAY_ORDER_PROPERTY = "displayOrder";
	public static final String TRIGGER_INSTRUCTION_PROPERTY = "triggerInstruction";
	public static final String EXPIRATION_INSTRUCTION_PROPERTY = "expirationInstruction";
	public static final String TARGET_ACCOUNT_ID_PROPERTY = "targetAccountID";
	
	// Lists
	public static final String TRIGGER_PROPERTY_LIST = "triggerProperties";
	public static final String EXPIRATION_PROPERTY_LIST = "expirationProperties";
	public static final String ORDER_PROPERTY_LIST = "orderProperties";
	
	// Properties on Trigger List 
	public static final String STOP_QUOTE_PROPERTY_NAME = "stopQuoteProperty";
	public static final String STOP_QUOTE_DIFFERENCE_PROPERTY_NAME = "stopQuoteDifferenceProperty";

	private OrderPropertyNames() { /* static class */ }
	
}
