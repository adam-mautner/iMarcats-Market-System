package com.imarcats.model;

/**
 * Changeable Market Property names
 * @author Adam
 */
public class MarketPropertyNames {

	// System Controlled
	public static final String CREATION_AUDIT_PROPERTY = "creationAudit";
	public static final String CHANGE_AUDIT_PROPERTY = "changeAudit";
	public static final String APPROVAL_AUDIT_PROPERTY = "approvalAudit";
	public static final String SUSPENSION_AUDIT_PROPERTY = "suspensionAudit";
	public static final String ROLLOVER_AUDIT_PROPERTY = "rolloverAudit";
	public static final String ACTIVATION_AUDIT_PROPERTY = "activationAudit";
	public static final String DEACTIVATION_AUDIT_PROPERTY = "deactivationAudit";
	public static final String NEXT_MARKET_CALL_DATE_PROPERTY = "nextMarketCallDate";
	public static final String STATE_PROPERTY = "state";
	public static final String ACTIVATION_STATUS_PROPERTY = "activationStatus";
	public static final String BUY_BOOK_PROPERTY = "buyBook";
	public static final String SELL_BOOK_PROPERTY = "sellBook";
	public static final String HALT_LEVEL_PROPERTY = "haltLevel";
	public static final String MARKET_CALL_ACTION_KEY_PROPERTY = "marketCallActionKey";
	public static final String MARKET_OPEN_ACTION_KEY_PROPERTY = "marketOpenActionKey";
	public static final String MARKET_CLOSE_ACTION_KEY_PROPERTY = "marketCloseActionKey";
	public static final String MARKET_RE_OPEN_ACTION_KEY_PROPERTY = "marketReOpenActionKey";
	public static final String MARKET_MAINTENANCE_ACTION_KEY_PROPERTY = "marketMaintenanceActionKey";
		
	// User Controlled
	public static final String MARKET_CODE_PROPERTY = "marketCode"; 
	public static final String INSTRUMENT_CODE_PROPERTY = "instrumentCode";
	public static final String NAME_PROPERTY = "name";
	public static final String DESCRIPTION_PROPERTY = "description";
	public static final String MARKET_OPERATOR_CODE_PROPERTY = "marketOperatorCode";
	public static final String QUOTE_TYPE_PROPERTY = "quoteType"; 
	// public static final String BUSINESS_ENTITY_PROPERTY = "businessEntity"; 
	public static final String BUSINESS_ENTITY_CODE_PROPERTY = "businessEntityCode"; 
	public static final String MINIMUM_CONTRACTS_TRADED_PROPERTY = "minimumContractsTraded";
	public static final String MAXIMUM_CONTRACTS_TRADED_PROPERTY = "maximumContractsTraded";
	public static final String MINIMUM_QUOTE_INCREMENT_PROPERTY = "minimumQuoteIncrement";
	public static final String TRADING_SESSION_PROPERTY = "tradingSession"; 
	public static final String TRADING_HOURS_PROPERTY = "tradingHours";
	public static final String TRADING_DAY_END_PROPERTY = "tradingDayEnd";
	public static final String BUSINESS_CALENDAR_PROPERTY = "businessCalendar";
	public static final String EXECUTION_SYSTEM_PROPERTY = "executionSystem";
	public static final String CIRCUIT_BREAKER_PROPERTY = "circuitBreaker";
	public static final String CLEARING_BANK_PROPERTY = "clearingBank";
	public static final String COMMISSION_PROPERTY = "commission";
	public static final String COMMISSION_CURRENCY_PROPERTY = "commissionCurrency";
	public static final String MARKET_OPERATION_DAYS = "marketOperationDays";
	public static final String ALLOW_HIDDEN_ORDERS = "allowHiddenOrders";
	public static final String ALLOW_SIZE_RESTRICTION_ON_ORDERS = "allowSizeRestrictionOnOrders";
	public static final String MARKET_TIMEZONE_ID = "marketTimeZoneID";
	
	// Administrator Controlled
	public static final String MARKET_OPERATOR_CONTRACT_PROPERTY = "MarketOperatorContract";
	
	// Lists
	public static final String SECONDARY_ORDER_PRECEDENCE_RULES_LIST = "SecondaryOrderPrecedenceRules";
	
	// Market Data 
	public static final String CURRENT_BEST_BID_PROPERTY = "CurrentBestBid"; 
	public static final String CURRENT_BEST_ASK_PROPERTY = "CurrentBestAsk"; 
	public static final String PREVIOUS_BEST_BID_PROPERTY = "PreviousBestBid"; 
	public static final String PREVIOUS_BEST_ASK_PROPERTY = "PreviousBestAsk";
	public static final String PREVIOUS_LAST_TRADE_PROPERTY = "PreviousLastTrade"; 	
	public static final String LAST_TRADE_PROPERTY = "LastTrade";
	public static final String OPENING_QUOTE_PROPERTY = "OpeningQuote";
	public static final String CLOSING_QUOTE_PROPERTY = "ClosingQuote";
	public static final String PREVIOUS_OPENING_QUOTE_PROPERTY = "PreviousOpeningQuote";
	public static final String PREVIOUS_CLOSING_QUOTE_PROPERTY = "PreviousClosingQuote";
	
	private MarketPropertyNames() { /* static class */ }
}
