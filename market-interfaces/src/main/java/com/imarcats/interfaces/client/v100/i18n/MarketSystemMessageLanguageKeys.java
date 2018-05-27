package com.imarcats.interfaces.client.v100.i18n;

/**
 * Internationalized System Messages and for Market System
 * @author Adam
 * NOTE: All other Messages are like Sentences
 */
public interface MarketSystemMessageLanguageKeys {
	
	// Cancellation Messages 
	public static final String ORDER_EXPIRED = "orderExpired";
	String orderExpired();
	
	public static final String ORDER_CANCELED_BY_ADMIN = "orderCanceledByAdmin";
	String orderCanceledByAdmin();
	
	public static final String ORDER_CANCELED_BY_SUPERVISOR = "orderCanceledBySupervisor";
	String orderCanceledBySupervisor();

	public static final String ORDER_CANCELED_BY_USER = "orderCanceledByUser";
	String orderCanceledByUser();

	public static final String ORDER_CANCELED_BECAUSE_NO_FUNDING_ON_BANK_USER_ACCOUNT= "orderCanceledBecauseNoFundingOnUserBankAccount";
	String orderCanceledBecauseNoFundingOnUserBankAccount();
	
	public static final String ORDER_CANCELED_AFTER_MARKET_CALL = "orderCanceledAfterMarketCall";
	String orderCanceledAfterMarketCall();
	
	public static final String ORDER_CANCELED_IT_WAS_IMMEDIATE_OR_CANCEL_ORDER = "orderCanceledItWasImmediateOrCancelOrder";
	String orderCanceledItWasImmediateOrCancelOrder();
}
