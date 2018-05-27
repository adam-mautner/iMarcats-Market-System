package com.imarcats.interfaces.client.v100.exception;

/**
 * Language Keys for the Exceptions Canceled
 * @author Adam
 *
 */
public interface ExceptionLanguageKeys {

	// Order State Errors
	public static final String ORDER_ALREADY_SUBMITTED = "orderAlreadySubmitted";
	public String orderAlreadySubmitted();
	
	public static final String EXECUTED_ORDER_CANNOT_BE_RESUBMITTED = "executedOrderCannotBeResubmitted";
	public String executedOrderCannotBeResubmitted();
	
	public static final String ORDER_HAS_ALREADY_BEEN_CANCELED = "orderHasAlreadyBeenCanceled";
	public String orderHasAlreadyBeenCanceled();
	
	public static final String EXECUTED_ORDER_CANNOT_BE_CANCELED = "executedOrderCannotBeCanceled";
	public String executedOrderCannotBeCanceled();
	
	public static final String UNSUPPORTED_QUOTE_CHANGE_TRIGGER = "unsupportedQuoteChangeTrigger";
	public String unsupportedQuoteChangeTrigger();
	
	public static final String UNSUPPORTED_ORDER_EXPIRATION_INSTRUCTION = "unsupportedOrderExpirationInstruction";
	public String unsupportedOrderExpirationInstruction();
	
	public static final String SUBMITTED_ORDER_CANNOT_BE_DELETED_CANCEL_FIRST = "submittedOrderCannotBeDeletedCancelFirst";
	public String submittedOrderCannotBeDeletedCancelFirst();
		
	// Order Handling Errors	
	public static final String ORDER_SIZE_CANNOT_BE_ZERO = "orderSizeCannotBeZero";
	public String orderSizeCannotBeZero();
	
	public static final String ORDER_IS_TOO_BIG_FOR_THE_MARKET = "orderIsTooBigForTheMarket";
	public String orderIsTooBigForTheMarket();
	
	public static final String ORDER_IS_TOO_SMALL_FOR_THE_MARKET = "orderIsTooSmallForTheMarket";
	public String orderIsTooSmallForTheMarket();
	
	public static final String ORDER_SIDE_CANNOT_BE_IDENTIFIED = "orderSideCannotBeIdentified";
	public String orderSideCannotBeIdentified();
	
	public static final String ORDER_TYPE_CANNOT_BE_IDENTIFIED = "orderTypeCannotBeIdentified";
	public String orderTypeCannotBeIdentified();
	
	public static final String ORDER_CANNOT_BE_SUBMITTED_TO_NON_ACTIVE_MARKET = "orderCannotBeSubmittedToNonActiveMarket";
	public String orderCannotBeSubmittedToNonActiveMarket();
	
	public static final String ORDER_SIZE_IS_SMALLER_THAN_MINIMUM_SIZE_OF_EXECUTION = "orderSizeIsSmallerThanMinimumSizeOfExecution";
	public String orderSizeIsSmallerThanMinimumSizeOfExecution();
	
	public static final String ORDER_SHOULD_HAVE_MINIMUM_SIZE_OF_EXECUTION_OR_ENTIRE_AT_ONCE_SET = "orderShouldEitherHaveMinimumSizeOfExecutionOrEntireOrderAtOnceSet";
	public String orderShouldEitherHaveMinimumSizeOfExecutionOrEntireOrderAtOnceSet();
	
	public static final String IMMEDIATE_OR_CANCEL_ORDER_IS_NOT_SUPPORTED_ON_CALL_MARKET = "immediateOrCancelOrderIsNotSupportedOnCallMarket";
	public String immediateOrCancelOrderIsNotSupportedOnCallMarket();
	
	public static final String IMMEDIATE_OR_CANCEL_ORDER_IS_NOT_SUPPORTED_WHEN_THE_MARKET_IS_NOT_OPEN = "immediateOrCancelOrderIsNotSupportedWhenTheMarketIsNotOpen";
	public String immediateOrCancelOrderIsNotSupportedWhenTheMarketIsNotOpen();
	
	public static final String MARKET_ORDER_IS_NOT_SUPPORTED_WHEN_THE_MARKET_IS_NOT_OPEN = "marketOrderIsNotSupportedWhenTheMarketIsNotOpen";
	public String marketOrderIsNotSupportedWhenTheMarketIsNotOpen();
	
	public static final String MARKET_ORDER_IS_NOT_SUPPORTED_ON_CALL_MARKET = "marketOrderIsNotSupportedOnCallMarket";
	public String marketOrderIsNotSupportedOnCallMarket();
	
	public static final String ONLY_GOOD_TILL_CANCEL_ORDER_IS_SUPPORTED_ON_CALL_MARKET = "onlyGoodTillCancelOrderIsSupportedOnCallMarket";
	public String onlyGoodTillCancelOrderIsSupportedOnCallMarket();
	
	public static final String ONLY_IMMEDIATELY_TRIGGERED_ORDER_IS_SUPPORTED_ON_CALL_MARKET = "onlyImmediatelyTriggeredOrderIsSupportedOnCallMarket";
	public String onlyImmediatelyTriggeredOrderIsSupportedOnCallMarket();
	
	public static final String MARKET_ORDER_MUST_BE_IMMEDIATE_OR_CANCEL = "marketOrderMustBeImmediateOrCancel";
	public String marketOrderMustBeImmediateOrCancel();
	
	public static final String HIDDEN_ORDER_NOT_SUPPORTED_ON_MARKET = "hiddenOrderNotSupportedOnMarket";
	public String hiddenOrderNotSupportedOnMarket();
	
	public static final String SIZE_RESTRICTION_ON_ORDER_NOT_SUPPORTED_ON_MARKET = "sizeRestrictionOnOrdersNotSupportedOnMarket";
	public String sizeRestrictionOnOrdersNotSupportedOnMarket();
	
	public static final String MARKET_ORDER_SUBMITTED_CANNOT_BE_FILLED_ON_CALL_MARKET = "marketOrderSubmittedCannotBeFilledOnCallMarket";
	public String marketOrderSubmittedCannotBeFilledOnCallMarket();
	
	public static final String ENTIRE_ORDER_AT_ONCE_NOT_SUPPORTED_ON_CALL_MARKET = "entireOrderAtOnceNotSupportedOnCallMarket";
	public String entireOrderAtOnceNotSupportedOnCallMarket();
	
	public static final String MINIMUM_EXECUTION_SIZE_NOT_SUPPORTED_ON_CALL_MARKET = "minimumExecutionSizeNotSupportedOnCallMarket";
	public String minimumExecutionSizeNotSupportedOnCallMarket();
	
	public static final String ENTIRE_ORDER_AT_ONCE_NOT_SUPPORTED_WHEN_THE_MARKET_IS_NOT_OPEN = "entireOrderAtOnceNotSupportedWhenTheMarketIsNotOpen";
	public String entireOrderAtOnceNotSupportedWhenTheMarketIsNotOpen();
	
	public static final String MINIMUM_EXECUTION_SIZE_NOT_SUPPORTED_WHEN_THE_MARKET_IS_NOT_OPEN = "minimumExecutionSizeNotSupportedWhenTheMarketIsNotOpen";
	public String minimumExecutionSizeNotSupportedWhenTheMarketIsNotOpen();
	
	public static final String MARKET_ORDER_CANNOT_BE_SUBMITTED_IF_THE_OTHER_SIDE_BOOK_IS_EMPTY = "marketOrderCannotBeSubmittedIfTheOtherSideBookIsEmpty";
	public String marketOrderCannotBeSubmittedIfTheOtherSideBookIsEmpty();
	
	public static final String MARKET_ORDER_CANNOT_BE_SUBMITTED_IF_THE_OTHER_SIDE_BOOK_HAS_ONLY_MARKET_ORDERS = "marketOrderCannotBeSubmittedIfTheOtherSideBookHasOnlyMarketOrders";
	public String marketOrderCannotBeSubmittedIfTheOtherSideBookHasOnlyMarketOrders();
	
	public static final String ORDER_MUST_BE_SUBMITTED_WITH_A_VALID_MARKET_QUOTE = "orderMustBeSubmittedWithAValidMarketQuote";
	public String orderMustBeSubmittedWithAValidMarketQuote();
	
	public static final String ORDER_IMPROVES_QUOTE_WITH_MORE_THAN_MAXIMUM_QUOTE_IMPROVEMENT = "orderImprovesQuoteWithMoreThanMaximumQuoteImprovement";
	public String orderImprovesQuoteWithMoreThanMaximumQuoteImprovement();
	
	public static final String STOP_QUOTE_IS_NOT_DEFINED_ON_STOP_ORDER = "stopQuoteIsNotDefinedOnStopOrder";
	public String stopQuoteIsNotDefinedOnStopOrder();
	
	public static final String STOP_QUOTE_DIFFERENCE_IS_NOT_DEFINED_ON_STOP_ORDER = "stopQuoteDifferenceIsNotDefinedOnStopOrder";
	public String stopQuoteDifferenceIsNotDefinedOnStopOrder();
	
	public static final String ORDER_CANNOT_BE_CREATED_NON_ACTIVE_MARKET = "orderCannotBeCreatedOnANonActiveMarket";
	public String orderCannotBeCreatedOnANonActiveMarket();
	
	public static final String NON_EXISTENT_ORDER = "nonExistentOrder";
	public String nonExistentOrder();
	
	public static final String TARGET_MARKET_NOT_SET_ON_ORDER = "targetMarketNotSetOnOrder";
	public String targetMarketNotSetOnOrder();
	
	public static final String UNSUPPORTED_ORDER_PROPERTY = "unsupportedOrderProperty";
	public String unsupportedOrderProperty();
	
	public static final String UNSUPPORTED_ORDER_PROPERTY_VALUE = "unsupportedOrderPropertyValue";
	public String unsupportedOrderPropertyValue();
	
	public static final String DUPLICATE_ORDER_PROPERTY = "duplicateOrderProperty";
	public String duplicateOrderProperty();
	
	public static final String UNSUPPORTED_ORDER_PROPERTY_CHANGE = "unsupportedOrderPropertyChange";
	public String unsupportedOrderPropertyChange();
	
	public static final String LIMIT_ORDER_MUST_HAVE_A_VALID_LIMIT_QUOTE = "limitOrderMustHaveValidLimitQuote";
	public String limitOrderMustHaveValidLimitQuote();
	
	public static final String NULL_ORDER_CANNOT_BE_CREATED = "nullOrderCannotBeCreated";
	public String nullOrderCannotBeCreated();
	
	public static final String ORDER_ID_MUST_NOT_BE_DEFINED = "orderIdMustNotBeDefined";
	public String orderIdMustNotBeDefined();
	
	public static final String ORDER_ALREADY_EXISTS = "orderAlreadyExists";
	public String orderAlreadyExists();
	
	public static final String MULTIPLE_ORDER_RETURNED_FOR_CODE = "multipleOrderReturnedForCode";	
	public String multipleOrderReturnedForCode();
	
	public static final String ORDER_CANNOT_BE_CREATED_ON_NON_EXISTENT_MARKET = "orderCannotBeCreatedOnNonExistentMarket";
	public String orderCannotBeCreatedOnNonExistentMarket();
	
	public static final String LIMIT_PRICE_MUST_BE_POSITIVE = "limitPriceMustBePositive";
	public String limitPriceMustBePositive();
	
	public static final String LIMIT_YIELD_MUST_BE_LESS_THAN_100 = "limitYieldMustBeLessThan100";
	public String limitYieldMustBeLessThan100();
	
	public static final String STOP_PRICE_MUST_BE_POSITIVE = "stopPriceMustBePositive";
	public String stopPriceMustBePositive();
	
	public static final String STOP_YIELD_MUST_BE_LESS_THAN_100 = "stopYieldMustBeLessThan100";
	public String stopYieldMustBeLessThan100();
	
	public static final String STOP_QUOTE_DIFFERENCE_MUST_BE_POSITIVE = "stopQuoteDifferenceMustBePositive";
	public String stopQuoteDifferenceMustBePositive();
	
	// Market Errors
	public static final String UNSUPPORTED_EXECUTION_SYSTEM_ORDER_MATCHING = "unsupportedExecutionSystemOrderMatching";
	public String unsupportedExecutionSystemOrderMatching();
	
	public static final String UNSUPPORTED_EXECUTION_SYSTEM_PRICING_RULE = "unsupportedExecutionSystemPricingRule";
	public String unsupportedExecutionSystemPricingRule();
	
	public static final String NON_ACTIVE_MARKET_CANNOT_BE_OPENED = "nonActiveMarketCannotBeOpened";
	public String nonActiveMarketCannotBeOpened();
	
	public static final String NON_ACTIVE_MARKET_CANNOT_BE_CALLED = "nonActiveMarketCannotBeCalled";
	public String nonActiveMarketCannotBeCalled();
	
	public static final String NON_ACTIVE_MARKET_CANNOT_BE_CLOSED = "nonActiveMarketCannotBeClosed";
	public String nonActiveMarketCannotBeClosed();
	
	public static final String NON_ACTIVE_MARKET_CANNOT_BE_HALTED = "nonActiveMarketCannotBeHalted";
	public String nonActiveMarketCannotBeHalted();
	
	public static final String NON_CLOSED_MARKET_CANNOT_BE_OPENED = "nonClosedMarketCannotBeOpened";
	public String nonClosedMarketCannotBeOpened();
	
	public static final String NON_HALTED_MARKET_CANNOT_BE_OPENED = "nonHaltedMarketCannotBeOpened";
	public String nonHaltedMarketCannotBeOpened();
	
	public static final String NON_OPEN_MARKET_CANNOT_BE_CLOSED = "nonOpenMarketCannotBeClosed";
	public String nonOpenMarketCannotBeClosed();
	
	public static final String NON_OPEN_MARKET_CANNOT_BE_HALTED = "nonOpenMarketCannotBeHalted";	
	public String nonOpenMarketCannotBeHalted();
	
	public static final String NON_CALL_MARKET_CANNOT_BE_CALLED = "nonCallMarketCannotBeCalled";
	public String nonCallMarketCannotBeCalled();
	
	public static final String OPEN_MARKET_CAN_ONLY_BE_CALLED = "openMarketCanOnlyBeCalled";
	public String openMarketCanOnlyBeCalled();
	
	public static final String NON_EXISTENT_MARKET = "nonExistentMarket";
	public String nonExistentMarket();
	
	
	// Message Handling Error 
	public static final String NULL_MESSAGE = "nullMessage";	
	public String nullMessage();
	
	public static final String UNSUPPORTED_MESSAGE = "unsupportedMessageTypeOrMessageVersion";	
	public String unsupportedMessageTypeOrMessageVersion();

	public static final String UNSUPPORTED_MARKET_OBJECT_TYPE = "unsupportedMarketObjectType";	
	public String unsupportedMarketObjectType();
	
	// Listener Errors
	public static final String NULL_LISTENER_CANNOT_BE_CREATED = "nullListenerCannotBeCreated";
	public String nullListenerCannotBeCreated();
	
	public static final String LISTENER_CANNOT_BE_INSTANTIATED = "listenerCannotBeInstantiated";	
	public String listenerCannotBeInstantiated();
	
	public static final String LISTENER_CANNOT_BE_CREATED_WITH_NULL_OBSERVED_OBJECT = "listenerCannotBeCreatedWithNullObservedObject";
	public String listenerCannotBeCreatedWithNullObservedObject();
	
	public static final String LISTENER_CANNOT_BE_QUERIED_WITH_NULL_OBSERVED_OBJECT = "listenerCannotBeQueriedWithNullObservedObject";
	public String listenerCannotBeQueriedWithNullObservedObject();
	
	public static final String LISTENER_CANNOT_BE_DELETED_WITH_NULL_OBSERVED_OBJECT = "listenerCannotBeDeletedWithNullObservedObject";
	public String listenerCannotBeDeletedWithNullObservedObject();
	
	public static final String LISTENER_CANNOT_BE_CREATED_WITH_NULL_OBSERVED_CLASS = "listenerCannotBeCreatedWithNullObservedClass";
	public String listenerCannotBeCreatedWithNullObservedClass();
	
	public static final String LISTENER_ALREADY_EXISTS = "listenerAlreadyExists";
	public String listenerAlreadyExists();
	
	public static final String MULTIPLE_LISTENERS_RETURNED_FOR_KEY = "multipleListenersReturnedForKey";
	public String multipleListenersReturnedForKey();
	
	public static final String NON_EXISTENT_LISTENER = "nonExistentListener";
	public String nonExistentListener();
			
	// Connection Error 
	public static final String ERROR_CONNECTING_MARKET_SYSTEM = "errorConnectingToMarketSystem";
	public String errorConnectingToMarketSystem();
	
	// System Error 
	public static final String INTERNAL_SYSTEM_ERROR = "internalSystemError";
	public String internalSystemError();
	
	// TODO: Should we move below to a different project ???
	
	// Asset Class Management Errors 
	public static final String ASSET_CLASS_CANNOT_BE_OVERWRITTEN_WITH_AN_OLDER_VERSION = "assetClassCannotBeOverwrittenWithAnOlderVersion";
	public String assetClassCannotBeOverwrittenWithAnOlderVersion();
	
	public static final String ASSET_CLASS_WITHOUT_VALID_NAME = "assetClassWithoutValidName";
	public String assetClassWithoutValidName();
	
	public static final String ASSET_CLASS_WITHOUT_DESCRIPTION = "assetClassWithoutDescription";
	public String assetClassWithoutDescription();
	
	public static final String ASSET_CLASS_NAME_CANNOT_BE_CHANGED = "assetClassNameCannotBeChanged";
	public String assetClassNameCannotBeChanged();
	
	public static final String ASSET_CLASS_CANNOT_BE_DELETED_IF_IT_IS_ASSIGNED_TO_INSTRUMENTS = "assetClassCannotBeDeletedIfItIsAssignedToInstruments";
	public String assetClassCannotBeDeletedIfItIsAssignedToInstruments();
	
	public static final String NULL_ASSET_CLASS_CANNOT_BE_CREATED = "nullAssetClassCannotBeCreated";
	public String nullAssetClassCannotBeCreated();
	
	public static final String ASSET_CLASS_CANNOT_BE_CREATED_WITH_NULL_NAME = "assetClassCannotBeCreatedWithNullName";
	public String assetClassCannotBeCreatedWithNullName();
	
	public static final String ASSET_CLASS_ALREADY_EXISTS = "assetClassAlreadyExists";
	public String assetClassAlreadyExists();
	
	public static final String MULTIPLE_ASSET_CLASSES_RETURNED_FOR_NAME = "multipleAssetClassReturnedForName";
	public String multipleAssetClassReturnedForName();
	
	public static final String NON_EXISTENT_ASSET_CLASS = "nonExistentAssetClass";
	public String nonExistentAssetClass();
	
	public static final String TOO_MANY_ASSET_CLASSES_FOR_THIS_PARENT = "tooManyAssetClassesForThisParent";
	public String tooManyAssetClassesForThisParent();
	
	public static final String TOO_DEEP_ASSET_CLASS_TREE = "tooDeepAssetClassTree";
	public String tooDeepAssetClassTree();
	
	public static final String CIRCULAR_ASSET_CLASS_TREE = "circularAssetClassTree";	
	public String circularAssetClassTree();
	
	public static final String ASSET_CLASS_PARENT_NAME_INVALID = "assetClassParentNameInvalid";	
	public String assetClassParentNameInvalid();
	
	// Product Management Errors 
	public static final String PRODUCT_CANNOT_BE_OVERWRITTEN_WITH_AN_OLDER_VERSION = "productCannotBeOverwrittenWithAnOlderVersion";
	public String productCannotBeOverwrittenWithAnOlderVersion();
	
	public static final String PRODUCT_MUST_HAVE_PRODUCT_VALID_CODE = "productMustHaveProductValidCode";
	public String productMustHaveProductValidCode();
	
	public static final String PRODUCT_MUST_HAVE_NAME = "productMustHaveName";
	public String productMustHaveName();
	
	public static final String PRODUCT_MUST_HAVE_DESCRIPTION = "productMustHaveDescription";
	public String productMustHaveDescription();
	
	public static final String PRODUCT_MUST_HAVE_PRODUCT_TYPE = "productMustHaveProductType";
	public String productMustHaveProductType();
	
	public static final String PRODUCT_MUST_HAVE_DEFINITION_DOCUMENT = "productMustHaveDefinitionDocument";
	public String productMustHaveDefinitionDocument();
	
	public static final String NEW_PRODUCT_MUST_NOT_HAVE_PRODUCT_CODE_ROLLED_FROM = "newProductMustNotHaveProductCodeRolledFrom";
	public String newProductMustNotHaveProductCodeRolledFrom();
	
	public static final String NEW_PRODUCT_MUST_NOT_HAVE_ACTIVATION_DATE = "newProductMustNotHaveActivationDate";
	public String newProductMustNotHaveActivationDate();
	
	public static final String NEW_PRODUCT_MUST_NOT_HAVE_CREATION_AUDIT = "newProductMustNotHaveCreationAudit";
	public String newProductMustNotHaveCreationAudit();
	
	public static final String NEW_PRODUCT_MUST_NOT_HAVE_CHANGE_AUDIT = "newProductMustNotHaveChangeAudit";
	public String newProductMustNotHaveChangeAudit();
	
	public static final String NEW_PRODUCT_MUST_NOT_HAVE_ROLLOVER_AUDIT = "newProductMustNotHaveRolloverAudit";
	public String newProductMustNotHaveRolloverAudit();
	
	public static final String NEW_PRODUCT_MUST_NOT_HAVE_APPROVAL_AUDIT = "newProductMustNotHaveApprovalAudit";
	public String newProductMustNotHaveApprovalAudit();
	
	public static final String NEW_PRODUCT_MUST_NOT_HAVE_SUSPENSION_AUDIT = "newProductMustNotHaveSuspensionAudit";
	public String newProductMustNotHaveSuspensionAudit();
	
	public static final String NEW_PRODUCT_MUST_NOT_HAVE_ACTIVATION_STATUS = "newProductMustNotHaveActivationStatus";
	public String newProductMustNotHaveActivationStatus();
	
	public static final String NEW_PRODUCT_MUST_NOT_HAVE_DEFINITION_DOCUMENT = "newProductMustNotHaveDefinitionDocument";
	public String newProductMustNotHaveDefinitionDocument();
	
	public static final String PRODUCT_TYPE_CANNOT_BE_CHANGED_WHILE_ROLLOVER = "productTypeCannotBeChangedWhileRolledOver";
	public String productTypeCannotBeChangedWhileRolledOver();
	
	public static final String PRODUCT_DEFINITION_DOCUMENT_CANNOT_BE_CHANGED_WHILE_ROLLOVER = "productDefinitionDocumentCannotBeChangedWhileRolledOver";
	public String productDefinitionDocumentCannotBeChangedWhileRolledOver();
	
	public static final String PRODUCT_CATEGORY_CANNOT_BE_CHANGED_WHILE_ROLLOVER = "productCategoryCannotBeChangedWhileRolledOver";
	public String productCategoryCannotBeChangedWhileRolledOver();
	
	public static final String PRODUCT_SUB_CATEGORY_CANNOT_BE_CHANGED_WHILE_ROLLOVER = "productSubCategoryCannotBeChangedWhileRolledOver";
	public String productSubCategoryCannotBeChangedWhileRolledOver();
	
	public static final String NULL_PRODUCT_CANNOT_BE_CREATED = "nullProductCannotBeCreated";
	public String nullProductCannotBeCreated();
	
	public static final String PRODUCT_CANNOT_BE_CREATED_WITH_NULL_CODE = "productCannotBeCreatedWithNullCode";
	public String productCannotBeCreatedWithNullCode();
	
	public static final String PRODUCT_ALREADY_EXISTS = "productAlreadyExists";
	public String productAlreadyExists();
	
	public static final String MULTIPLE_PRODUCTS_RETURNED_FOR_CODE = "multipleProductsReturnedForCode";				
	public String multipleProductsReturnedForCode();
	
	public static final String NON_EXISTENT_PRODUCT = "nonExistentProduct";
	public String nonExistentProduct();
	
	public static final String PRODUCT_ONLY_IN_CREATED_OR_SUSPEDED_STATE_CAN_BE_CHANGED = "productOnlyInCreatedOrSuspendedStateCanBeChanged";
	public String productOnlyInCreatedOrSuspendedStateCanBeChanged();
	
	public static final String PRODUCT_ONLY_IN_CREATED_OR_SUSPEDED_STATE_CAN_BE_DELETED = "productOnlyInCreatedOrSuspendedStateCanBeDeleted";
	public String productOnlyInCreatedOrSuspendedStateCanBeDeleted();
	
	public static final String PRODUCT_ONLY_IN_CREATED_OR_SUSPEDED_STATE_CAN_BE_APPROVED = "productOnlyInCreatedOrSuspendedStateCanBeApproved";
	public String productOnlyInCreatedOrSuspendedStateCanBeApproved();
	
	public static final String PRODUCT_NOT_IN_CREATED_OR_SUSPEDED_STATE_CAN_BE_SUSPENDED = "productNotInCreatedOrSuspendedStateCanBeSuspended";
	public String productNotInCreatedOrSuspendedStateCanBeSuspended();
	
	public static final String PRODUCT_THAT_HAS_NO_APPROVED_DEPENDENT_INSTRUMENTS_CAN_BE_FORCED_TO_BE_CHANGED = "productThatHasNoApprovedDependentInstrumentsCanBeForcedToBeChanged";
	public String productThatHasNoApprovedDependentInstrumentsCanBeForcedToBeChanged();
	
	public static final String PRODUCT_THAT_HAS_NO_DEPENDENT_INSTRUMENTS_CAN_BE_DELETED = "productThatHasNoDependentInstrumentsCanBeDeleted";
	public String productThatHasNoDependentInstrumentsCanBeDeleted();
	
	public static final String PRODUCT_CAN_ONLY_BE_SUSPENDED_IF_DEPENDENT_OBJECTS_ARE_SUSPENDED = "productCanOnlyBeSuspendedIfDependentObjectsAreSuspended";
	public String productCanOnlyBeSuspendedIfDependentObjectsAreSuspended();
	
	public static final String CATEGORY_MUST_BE_VALID = "categoryMustBeValid";
	public String categoryMustBeValid();
	
	public static final String SUB_CATEGORY_MUST_BE_VALID = "subCategoryMustBeValid";
	public String subCategoryMustBeValid();
	
	// Instrument Management Error 
	public static final String INSTRUMENT_CANNOT_BE_OVERWRITTEN_WITH_AN_OLDER_VERSION = "instrumentCannotBeOverwrittenWithAnOlderVersion";
	public String instrumentCannotBeOverwrittenWithAnOlderVersion();
	
	public static final String NULL_INSTRUMENT_CANNOT_BE_CREATED = "nullInstrumentCannotBeCreated";
	public String nullInstrumentCannotBeCreated();
	
	public static final String INSTRUMENT_CANNOT_BE_CREATED_WITH_NULL_CODE = "instrumentCannotBeCreatedWithNullCode";
	public String instrumentCannotBeCreatedWithNullCode();
	
	public static final String INSTRUMENT_ALREADY_EXISTS = "instrumentAlreadyExists";
	public String instrumentAlreadyExists();
	
	public static final String MULTIPLE_INSTRUMENTS_RETURNED_FOR_CODE = "multipleInstrumentsReturnedForCode";
	public String multipleInstrumentsReturnedForCode();
					
	public static final String INSTRUMENT_MUST_HAVE_VALID_CODE = "instrumentMustHaveValidInstrumentCode";
	public String instrumentMustHaveValidInstrumentCode();
	
	public static final String INSTRUMENT_MUST_HAVE_NAME = "instrumentMustHaveName";
	public String instrumentMustHaveName();
	
	public static final String INSTRUMENT_MUST_HAVE_DESCRIPTION = "instrumentMustHaveDescription";
	public String instrumentMustHaveDescription();
	
	public static final String INSTRUMENT_MUST_HAVE_INSTRUMENT_TYPE = "instrumentMustHaveInstrumentType";
	public String instrumentMustHaveInstrumentType();
	
	public static final String INSTRUMENT_MUST_HAVE_UNDERLYING_CODE = "instrumentMustHaveUnderlyingCode";
	public String instrumentMustHaveUnderlyingCode();
	
	public static final String INSTRUMENT_MUST_HAVE_UNDERLYING_TYPE = "instrumentMustHaveUnderlyingType";
	public String instrumentMustHaveUnderlyingType();
	
	public static final String INSTRUMENT_MUST_HAVE_DENOMINATION_CURRENCY = "instrumentMustHaveDenominationCurrency";
	public String instrumentMustHaveDenominationCurrency();
	
	public static final String INSTRUMENT_MUST_HAVE_CONTRACT_SIZE = "instrumentMustHaveContractSize";
	public String instrumentMustHaveContractSize();
	
	public static final String INSTRUMENT_MUST_HAVE_CONTRACT_SIZE_UNIT = "instrumentMustHaveContractSizeUnit";
	public String instrumentMustHaveContractSizeUnit();
	
	public static final String INSTRUMENT_MUST_HAVE_MASTER_AGREEMENT_DOCUMENT = "instrumentMustHaveMasterAgreementDocument";
	public String instrumentMustHaveMasterAgreementDocument();
	
	public static final String INSTRUMENT_MUST_HAVE_QUOTE_TYPE = "instrumentMustHaveQuoteType";
	public String instrumentMustHaveQuoteType();
	
	public static final String INSTRUMENT_MUST_HAVE_SETTLEMENT_PRICE_TYPE = "instrumentMustHaveSettlementPriceType";
	public String instrumentMustHaveSettlementPriceType();
	
	public static final String ONLY_CLEAN_SETTLEMENT_PRICE_TYPE_IS_SUPPORTED_ON_INSTRUMENTS = "onlyCleanSettlementPriceTypeIsSupportedOnInstruments";
	public String onlyCleanSettlementPriceTypeIsSupportedOnInstruments();
	
	public static final String INSTRUMENT_MUST_HAVE_SETTLEMENT_TYPE = "instrumentMustHaveSettlementType";	
	public String instrumentMustHaveSettlementType();
	
	public static final String INSTRUMENT_MUST_HAVE_DELIVERY_PERIOD = "instrumentMustHaveDeliveryPeriod";
	public String instrumentMustHaveDeliveryPeriod();
	
	public static final String INSTRUMENT_MUST_HAVE_RECORD_PURSCHASE_AS_TYPE = "instrumentMustHaveRecordPurchaseAsType";
	public String instrumentMustHaveRecordPurchaseAsType();
	
	public static final String ONLY_LONG_RECORD_PURSCHASE_AS_TYPE_SUPPORTED_ON_INSTRUMENTS = "onlyLongRecordPurchaseAsTypeSupportedOnInstruments";
	public String onlyLongRecordPurchaseAsTypeSupportedOnInstruments();
	
	public static final String INSTRUMENT_MUST_HAVE_DELIVERY_LOCATION_FOR_PHYSICAL_DELIVERY = "instrumentMustHaveDeliveryLocationForPhysicalDelivery";
	public String instrumentMustHaveDeliveryLocationForPhysicalDelivery();
	
	public static final String NEW_INSTRUMENT_MUST_NOT_HAVE_INSTRUMENT_CODE_ROLLED_FROM = "newInstrumentMustNotHaveInstrumentCodeRolledFrom";
	public String newInstrumentMustNotHaveInstrumentCodeRolledFrom();
	
	public static final String NEW_INSTRUMENT_MUST_NOT_HAVE_ACTIVATION_DATE = "newInstrumentMustNotHaveActivationDate";
	public String newInstrumentMustNotHaveActivationDate();
	
	public static final String NEW_INSTRUMENT_MUST_NOT_HAVE_CREATION_AUDIT = "newInstrumentMustNotHaveCreationAudit";
	public String newInstrumentMustNotHaveCreationAudit();
	
	public static final String NEW_INSTRUMENT_MUST_NOT_HAVE_CHANGE_AUDIT = "newInstrumentMustNotHaveChangeAudit";
	public String newInstrumentMustNotHaveChangeAudit();
	
	public static final String NEW_INSTRUMENT_MUST_NOT_HAVE_ROLLOVER_AUDIT = "newInstrumentMustNotHaveRolloverAudit";
	public String newInstrumentMustNotHaveRolloverAudit();
	
	public static final String NEW_INSTRUMENT_MUST_NOT_HAVE_APPROVAL_AUDIT = "newInstrumentMustNotHaveApprovalAudit";
	public String newInstrumentMustNotHaveApprovalAudit();
	
	public static final String NEW_INSTRUMENT_MUST_NOT_HAVE_SUSPENSION_AUDIT = "newInstrumentMustNotHaveSuspensionAudit";
	public String newInstrumentMustNotHaveSuspensionAudit();
	
	public static final String NEW_INSTRUMENT_MUST_NOT_HAVE_ACTIVATION_STATUS = "newInstrumentMustNotHaveActivationStatus";
	public String newInstrumentMustNotHaveActivationStatus();
	
	public static final String NEW_INSTRUMENT_MUST_NOT_HAVE_MASTER_AGREEMENT_DOCUMENT = "newInstrumentMustNotHaveMasterAgreementDocument";
	public String newInstrumentMustNotHaveMasterAgreementDocument();
	
	public static final String NEW_INSTRUMENT_MUST_NOT_HAVE_ASSET_CLASS_NAME = "newInstrumentMustNotHaveAssetClassName";
	public String newInstrumentMustNotHaveAssetClassName();
	
	public static final String INSTRUMENT_TYPE_CANNOT_BE_CHANGED_WHILE_ROLLOVER = "instrumentTypeCannotBeChangedWhileRolledOver";
	public String instrumentTypeCannotBeChangedWhileRolledOver();
	
	public static final String INSTRUMENT_ASSET_CLASS_CANNOT_BE_CHANGED_WHILE_ROLLOVER = "instrumentAssetClassCannotBeChangedWhileRolledOver";	
	public String instrumentAssetClassCannotBeChangedWhileRolledOver();
	
	public static final String INSTRUMENT_SUB_TYPE_CANNOT_BE_CHANGED_WHILE_ROLLOVER = "instrumentSubTypeCannotBeChangedWhileRolledOver";	
	public String instrumentSubTypeCannotBeChangedWhileRolledOver();
	
	public static final String INSTRUMENT_UNDERLYING_TYPE_CANNOT_BE_CHANGED_WHILE_ROLLOVER = "instrumentUnderlyingTypeCannotBeChangedWhileRolledOver";	
	public String instrumentUnderlyingTypeCannotBeChangedWhileRolledOver();
	
	public static final String INSTRUMENT_MASTER_AGREEMENT_CANNOT_BE_CHANGED_WHILE_ROLLOVER = "instrumentMasterAgreementCannotBeChangedWhileRolledOver";
	public String instrumentMasterAgreementCannotBeChangedWhileRolledOver();
	
	public static final String INSTRUMENT_QUOTE_TYPE_CANNOT_BE_CHANGED_WHILE_ROLLOVER = "instrumentQuoteTypeCannotBeChangedWhileRolledOver";		
	public String instrumentQuoteTypeCannotBeChangedWhileRolledOver();
	
	public static final String INSTRUMENT_DENOMINATION_CURRENCY_CANNOT_BE_CHANGED_WHILE_ROLLOVER = "instrumentDenominationCurrencyCannotBeChangedWhileRolledOver";	
	public String instrumentDenominationCurrencyCannotBeChangedWhileRolledOver();
	
	public static final String INSTRUMENT_CONTRACT_SIZE_CANNOT_BE_CHANGED_WHILE_ROLLOVER = "instrumentContractSizeCannotBeChangedWhileRolledOver";	
	public String instrumentContractSizeCannotBeChangedWhileRolledOver();
	
	public static final String INSTRUMENT_CONTRACT_SIZE_UNIT_CANNOT_BE_CHANGED_WHILE_ROLLOVER = "instrumentContractSizeUnitCannotBeChangedWhileRolledOver";	
	public String instrumentContractSizeUnitCannotBeChangedWhileRolledOver();
		
	public static final String INSTRUMENT_SETTLEMENT_PRICE_TYPE_CANNOT_BE_CHANGED_WHILE_ROLLOVER = "instrumentSettlementPriceTypeCannotBeChangedWhileRolledOver";
	public String instrumentSettlementPriceTypeCannotBeChangedWhileRolledOver();
		
	public static final String INSTRUMENT_SETTLEMENT_TYPE_CANNOT_BE_CHANGED_WHILE_ROLLOVER = "instrumentSettlementTypeCannotBeChangedWhileRolledOver";	
	public String instrumentSettlementTypeCannotBeChangedWhileRolledOver();
	
	public static final String INSTRUMENT_DELIVERY_PERIOD_CANNOT_BE_CHANGED_WHILE_ROLLOVER = "instrumentDeliveryPeriodCannotBeChangedWhileRolledOver";	
	public String instrumentDeliveryPeriodCannotBeChangedWhileRolledOver();
	
	public static final String INSTRUMENT_DELIVERY_LOCATION_CANNOT_BE_CHANGED_WHILE_ROLLOVER = "instrumentDeliveryLocationCannotBeChangedWhileRolledOver";
	public String instrumentDeliveryLocationCannotBeChangedWhileRolledOver();
		
	public static final String INSTRUMENT_RECORD_PURCHASE_AS_TYPE_CANNOT_BE_CHANGED_WHILE_ROLLOVER = "instrumentRecordPurchaseAsTypeCannotBeChangedWhileRolledOver";
	public String instrumentRecordPurchaseAsTypeCannotBeChangedWhileRolledOver();
		
	public static final String NON_EXISTENT_INSTRUMENT = "nonExistentInstrument";	
	public String nonExistentInstrument();
	
	public static final String INSTRUMENT_THAT_HAS_NO_DEPENDENT_INSTRUMENTS_OR_MARKETS_CAN_BE_SUSPENDED = "instrumentThatHasNoDependentInstrumentsOrMarketsCanBeSuspended";
	public String instrumentThatHasNoDependentInstrumentsOrMarketsCanBeSuspended();
	
	public static final String INSTRUMENT_ONLY_IN_CREATED_OR_SUSPEDED_STATE_CAN_BE_CHANGED = "instrumentOnlyInCreatedOrSuspendedStateCanBeChanged";
	public String instrumentOnlyInCreatedOrSuspendedStateCanBeChanged();

	public static final String INSTRUMENT_ONLY_IN_CREATED_OR_SUSPEDED_STATE_CAN_BE_DELETED = "instrumentOnlyInCreatedOrSuspendedStateCanBeDeleted";
	public String instrumentOnlyInCreatedOrSuspendedStateCanBeDeleted();
	
	public static final String INSTRUMENT_ONLY_IN_CREATED_OR_SUSPEDED_STATE_CAN_BE_APPROVED = "instrumentOnlyInCreatedOrSuspendedStateCanBeApproved";
	public String instrumentOnlyInCreatedOrSuspendedStateCanBeApproved();
	
	public static final String INSTRUMENT_NOT_IN_CREATED_OR_SUSPEDED_STATE_CAN_BE_SUSPENDED = "instrumentNotInCreatedOrSuspendedStateCanBeSuspended";
	public String instrumentNotInCreatedOrSuspendedStateCanBeSuspended();
	
	public static final String INSTRUMENT_THAT_HAS_NO_APPROVED_DEPENDENT_INSTRUMENTS_OR_MARKET_CAN_BE_FORCED_TO_BE_CHANGED = "instrumentThatHasNoApprovedDependentInstrumentsOrMarketsCanBeForcedToBeChanged";
	public String instrumentThatHasNoApprovedDependentInstrumentsOrMarketsCanBeForcedToBeChanged();
	
	public static final String INSTRUMENT_THAT_HAS_NO_DEPENDENT_INSTRUMENTS_OR_MARKET_CAN_BE_DELETED = "instrumentThatHasNoDependentInstrumentsOrMarketsCanBeDeleted";
	public String instrumentThatHasNoDependentInstrumentsOrMarketsCanBeDeleted();
	
	public static final String NON_EXISTENT_UNDERLYING = "nonExistentUnderlying";
	public String nonExistentUnderlying();
	
	public static final String INSTRUMENT_CAN_ONLY_BE_APPROVED_IF_UNDERLYING_IS_APPROVED = "instrumentCanOnlyBeApprovedIfUnderlyingIsApproved";
	public String instrumentCanOnlyBeApprovedIfUnderlyingIsApproved();
	
	public static final String INSTRUMENT_CAN_ONLY_BE_SUSPENDED_IF_DEPENDENT_OBJECTS_ARE_SUSPENDED = "instrumentCanOnlyBeSuspendedIfDependentObjectsAreSuspended";
	public String instrumentCanOnlyBeSuspendedIfDependentObjectsAreSuspended();
	
	public static final String INSTRUMENT_MUST_HAVE_ASSET_CLASS_NAME_FOR_APPROVAL = "instrumentMustHaveAssetClassNameForApproval";
	public String instrumentMustHaveAssetClassNameForApproval();
	
	//public static final String SPOT_INSTRUMENT_MUST_HAVE_PRODUCT_UNDERLYING = "spotInstrumentMustHaveProductUnderlying";
	//public static final String DERIVATIVE_INSTRUMENT_MUST_HAVE_INSTRUMENT_UNDERLYING = "derivativeInstrumentMustHaveInstrumentUnderlying";

	public static final String INSTRUMENT_MUST_HAVE_CURRENCY_OR_UNIT = "instrumentMustHaveCurrencyOrUnit";
	public String instrumentMustHaveCurrencyOrUnit();

	public static final String TOO_DEEP_UNDERLYING_DEPENDENCY = "tooDeepUnderlyingDependency";
	public String tooDeepUnderlyingDependency();
	
	public static final String CIRCULAR_UNDERLYING_DEPENDENCY = "circularUnderlyingDependency";	
	public String circularUnderlyingDependency();
	
	public static final String ISIN_MUST_BE_VALID = "isinMustBeValid";
	public String isinMustBeValid();
	
	public static final String SUB_TYPE_MUST_BE_VALID = "subTypeMustBeValid";
	public String subTypeMustBeValid();
		
	// Corporate Information Errors
	public static final String CORPORATE_INFORMATION_MUST_HAVE_NAME = "corporateInformationMustHaveName";
	public String corporateInformationMustHaveName();
	
	public static final String CORPORATE_INFORMATION_MUST_HAVE_ADDRESS = "corporateInformationMustHaveAddress";
	public String corporateInformationMustHaveAddress();
	
	public static final String CORPORATE_WEBSITE_MUST_BE_VALID = "corporateWebsiteMustBeValid";
	public String corporateWebsiteMustBeValid();
	
	// Address Errors
	public static final String ADDRESS_MUST_HAVE_STREET = "addressMustHaveStreet";
	public String addressMustHaveStreet();
	
	public static final String ADDRESS_MUST_HAVE_CITY = "addressMustHaveCity";
	public String addressMustHaveCity();
	
	public static final String ADDRESS_MUST_HAVE_COUNTRY = "addressMustHaveCountry";
	public String addressMustHaveCountry();
	
	public static final String ADDRESS_MUST_HAVE_POSTAL_CODE = "addressMustHavePostalCode";
	public String addressMustHavePostalCode();
	
	// Time Period Errors
	public static final String TIME_PERIOD_MUST_HAVE_START_TIME = "timePeriodMustHaveStartTime";
	public String timePeriodMustHaveStartTime();
	
	public static final String TIME_PERIOD_MUST_HAVE_END_TIME = "timePeriodMustHaveEndTime";
	public String timePeriodMustHaveEndTime();
	
	public static final String START_TIME_END_TIME_MUST_HAVE_SAME_TIME_ZONE = "startTimeEndTimeMustHaveSameTimeZone";
	public String startTimeEndTimeMustHaveSameTimeZone();
	
	public static final String START_TIME_MUST_HAVE_BE_BEFORE_END_TIME = "startTimeMustBeforeEndTime";
	public String startTimeMustBeforeEndTime();
	
	// Time of Day Errors
	public static final String TIME_OF_DAY_MUST_HAVE_VALID_HOURS = "timeOfDayMustHaveValidHours";
	public String timeOfDayMustHaveValidHours();
	
	public static final String TIME_OF_DAY_MUST_HAVE_VALID_MINUTES = "timeOfDayMustHaveValidMinutes";
	public String timeOfDayMustHaveValidMinutes();
	
	public static final String TIME_OF_DAY_MUST_HAVE_VALID_SECONDS = "timeOfDayMustHaveValidSeconds";
	public String timeOfDayMustHaveValidSeconds();
	
	public static final String TIME_OF_DAY_MUST_HAVE_VALID_TIME_ZONE = "timeOfDayMustHaveValidTimeZone";
	public String timeOfDayMustHaveValidTimeZone();
	
	// Business Calendar Errors
	public static final String INVALID_BUSINESS_CALENDAR = "invalidBusinessCalendar";
	public String invalidBusinessCalendar();
	
	// Audit Trail
	public static final String NULL_AUDIT_TRAIL_ENTRY_CANNOT_BE_CREATED = "nullAuditTrailEntryCannotBeCreated";
	public String nullAuditTrailEntryCannotBeCreated();
	
	public static final String AUDIT_TRAIL_MUST_HAVE_DATE = "auditTrailMustHaveDate";
	public String auditTrailMustHaveDate();
	
	public static final String AUDIT_TRAIL_MUST_HAVE_USER = "auditTrailMustHaveUser";
	public String auditTrailMustHaveUser();
	
	public static final String AUDIT_TRAIL_MUST_HAVE_OBJECT_TYPE = "auditTrailMustHaveObjectType";
	public String auditTrailMustHaveObjectType();
	
	public static final String AUDIT_TRAIL_MUST_HAVE_OBJECT_ID = "auditTrailMustHaveObjectID";
	public String auditTrailMustHaveObjectID();
	
	public static final String AUDIT_TRAIL_MUST_HAVE_ACTION = "auditTrailMustHaveAction";
	public String auditTrailMustHaveAction();
	
	// Rollover Errors 
	public static final String NON_ROLLABLE_OBJECT_CANNOT_BE_ROLLED_OVER = "nonRollableObjectCannotBeRolledOver";
	public String nonRollableObjectCannotBeRolledOver();
	
	public static final String OBJECT_HAS_TO_REMAIN_ROLLABLE = "objectHasToRemainRollable";
	public String objectHasToRemainRollable();
	
	public static final String UNDERLYING_OBJECT_CANNOT_BE_REMOVED_WHILE_ROLLOVER = "underlyingObjectCannotBeRemovedWhileRollover";
	public String underlyingObjectCannotBeRemovedWhileRollover();
	
	public static final String UNDERLYING_OBJECT_CANNOT_BE_ADDED_WHILE_ROLLOVER = "underlyingObjectCannotBeAddedWhileRollover";	
	public String underlyingObjectCannotBeAddedWhileRollover();
	
	public static final String INVALID_UNDELYING_OBJECT_PROVIDED_FOR_ROLLOVER = "invalidUnderlyingObjectProvidedForRollover";
	public String invalidUnderlyingObjectProvidedForRollover();
	
	public static final String UNDERLYING_OBJECT_PROVIDED_FOR_ROLLOVER_IS_NOT_ROLLED_FROM_UNDERLYING_OF_SOURCE_OBJECT = "underlyingObjectProvidedForRolloverIsNotRolledFromTheUnderlyingOfSourceObject";
	public String underlyingObjectProvidedForRolloverIsNotRolledFromTheUnderlyingOfSourceObject();
	
	public static final String NON_APPROVED_OBJECT_CANNOT_BE_REFERRED_IN_ROLLOVER = "nonApprovedObjectCannotBeReferredInRollover";	
	public String nonApprovedObjectCannotBeReferredInRollover();
		
	public static final String ROLLOVER_CANNOT_CHANGE_LIST_OF_ROLLABLE_PROPERTIES = "rolloverCannotChangeTheListOfRollableProperties";
	public String rolloverCannotChangeTheListOfRollableProperties();
			
	public static final String ROLLOVER_CANNOT_CHANGE_NUMBER_OF_PROPERTIES = "rolloverCannotChangeTheNumberOfProperties";	
	public String rolloverCannotChangeTheNumberOfProperties();
		
	public static final String ROLLOVER_CANNOT_ADD_OR_REMOVE_PROPERTIES = "rolloverCannotAddOrRemoveProperties";
	public String rolloverCannotAddOrRemoveProperties();
	
	public static final String ROLLOVER_CANNOT_CHANGE_NON_ROLLABLE_PROPERTIES = "rolloverCannotChangeNonRollableProperties";
	public String rolloverCannotChangeNonRollableProperties();
	
	public static final String ROLLED_OBJECTS_HAVE_SAME_CODE = "rolledObjectsHaveSameCode";
	public String rolledObjectsHaveSameCode();
	
	public static final String ROLLABLE_PROPERTY_MUST_BE_VALID_PROPERTY_ON_OBJECT = "rollablePropertyMustBeValidPropertyOnObject";
	public String rollablePropertyMustBeValidPropertyOnObject();
	
	// Market Operator Management Errors
	public static final String MARKET_OPERATOR_CANNOT_BE_OVERWRITTEN_WITH_AN_OLDER_VERSION = "marketOperatorCannotBeOverwrittenWithAnOlderVersion";
	public String marketOperatorCannotBeOverwrittenWithAnOlderVersion();
	
	public static final String MARKET_OPERATOR_MUST_HAVE_CODE = "marketOperatorMustHaveCode";
	public String marketOperatorMustHaveCode();
	
	public static final String MARKET_OPERATOR_MUST_HAVE_NAME = "marketOperatorMustHaveName";
	public String marketOperatorMustHaveName();
	
	public static final String MARKET_OPERATOR_MUST_HAVE_DESCRIPTION = "marketOperatorMustHaveDescription";
	public String marketOperatorMustHaveDescription();
	
	public static final String MARKET_OPERATOR_MUST_HAVE_BUSINESS_ENTITY = "marketOperatorMustHaveBusinessEntity";
	public String marketOperatorMustHaveBusinessEntity();
	
	public static final String NEW_MARKET_OPERATOR_MUST_NOT_HAVE_CREATION_AUDIT = "newMarketOperatorMustNotHaveCreationAudit";
	public String newMarketOperatorMustNotHaveCreationAudit();
	
	public static final String NEW_MARKET_OPERATOR_MUST_NOT_HAVE_CHANGE_AUDIT = "newMarketOperatorMustNotHaveChangeAudit";
	public String newMarketOperatorMustNotHaveChangeAudit();
	
	public static final String NEW_MARKET_OPERATOR_MUST_NOT_HAVE_APPROVAL_AUDIT = "newMarketOperatorMustNotHaveApprovalAudit";
	public String newMarketOperatorMustNotHaveApprovalAudit();
	
	public static final String NEW_MARKET_OPERATOR_MUST_NOT_HAVE_SUSPENSION_AUDIT = "newMarketOperatorMustNotHaveSuspensionAudit";
	public String newMarketOperatorMustNotHaveSuspensionAudit();
	
	public static final String NEW_MARKET_OPERATOR_MUST_NOT_HAVE_ACTIVATION_STATUS = "newMarketOperatorMustNotHaveActivationStatus";
	public String newMarketOperatorMustNotHaveActivationStatus();
	
	public static final String NEW_MARKET_OPERATOR_MUST_NOT_HAVE_MARKET_OPERATOR_AGREEMENT = "newMarketOperatorMustNotHaveMarketOperatorAgreement";
	public String newMarketOperatorMustNotHaveMarketOperatorAgreement();
	
	public static final String NULL_MARKET_OPERATOR_CANNOT_BE_CREATED = "nullMarketOperatorCannotBeCreated";
	public String nullMarketOperatorCannotBeCreated();
	
	public static final String MARKET_OPERATOR_MUST_HAVE_OWNER_USER = "marketOperatorMustHaveOwnerUser";
	public String marketOperatorMustHaveOwnerUser();
	
	public static final String MARKET_OPERATOR_CANNOT_BE_CREATED_WITH_NULL_CODE = "marketOperatorCannotBeCreatedWithNullCode";
	public String marketOperatorCannotBeCreatedWithNullCode();
	
	public static final String MARKET_OPERATOR_ALREADY_EXISTS = "marketOperatorAlreadyExists";
	public String marketOperatorAlreadyExists();
	
	public static final String MULTIPLE_MARKET_OPERATORS_RETURNED_FOR_NAME = "multipleMarketOperatorReturnedForName";
	public String multipleMarketOperatorReturnedForName();
	
	public static final String NON_EXISTENT_MARKET_OPERATOR = "nonExistentMarketOperator";
	public String nonExistentMarketOperator();
	
	public static final String MARKET_OPERATOR_CAN_ONLY_BE_APPROVED_IF_UNDERLYING_BUSINESS_ENTITY_IS_APPROVED = "marketOperatorCanOnlyBeApprovedIfUnderlyingBusinessEntityIsApproved";
	public String marketOperatorCanOnlyBeApprovedIfUnderlyingBusinessEntityIsApproved();
	
	public static final String MARKET_OPERATOR_CAN_ONLY_BE_APPROVED_IF_OWNER_IS_APPROVED = "marketOperatorCanOnlyBeApprovedIfOwnerIsApproved";
	public String marketOperatorCanOnlyBeApprovedIfOwnerIsApproved();
	
	public static final String MARKET_OPERATOR_THAT_HAS_NO_DEPENDENT_MARKETS_CAN_BE_SUSPENDED = "marketOperatorThatHasNoDependentMarketsCanBeSuspended";
	public String marketOperatorThatHasNoDependentMarketsCanBeSuspended();
	
	public static final String MARKET_OPERATOR_ONLY_IN_CREATED_OR_SUSPEDED_STATE_CAN_BE_CHANGED = "marketOperatorOnlyInCreatedOrSuspendedStateCanBeChanged";
	public String marketOperatorOnlyInCreatedOrSuspendedStateCanBeChanged();
	
	public static final String MARKET_OPERATOR_ONLY_IN_CREATED_OR_SUSPEDED_STATE_CAN_BE_DELETED = "marketOperatorOnlyInCreatedOrSuspendedStateCanBeDeleted";
	public String marketOperatorOnlyInCreatedOrSuspendedStateCanBeDeleted();
	
	public static final String MARKET_OPERATOR_ONLY_IN_CREATED_OR_SUSPEDED_STATE_CAN_BE_APPROVED = "marketOperatorOnlyInCreatedOrSuspendedStateCanBeApproved";
	public String marketOperatorOnlyInCreatedOrSuspendedStateCanBeApproved();
	
	public static final String MARKET_OPERATOR_NOT_IN_CREATED_OR_SUSPEDED_STATE_CAN_BE_SUSPENDED = "marketOperatorNotInCreatedOrSuspendedStateCanBeSuspended";
	public String marketOperatorNotInCreatedOrSuspendedStateCanBeSuspended();
	
	public static final String MARKET_OPERATOR_THAT_HAS_NO_APPROVED_DEPENDENT_MARKET_CAN_BE_FORCED_TO_BE_CHANGED = "marketOperatorThatHasNoApprovedDependentMarketsCanBeForcedToBeChanged";
	public String marketOperatorThatHasNoApprovedDependentMarketsCanBeForcedToBeChanged();
	
	public static final String MARKET_OPERATOR_THAT_HAS_NO_DEPENDENT_MARKET_CAN_BE_DELETED = "marketOperatorThatHasNoDependentMarketsCanBeDeleted";
	public String marketOperatorThatHasNoDependentMarketsCanBeDeleted();
	
	public static final String MARKET_OPERATOR_MUST_HAVE_MARKET_OPERATOR_AGREEMENT_DOCUMENT = "marketOperatorMustHaveMarketOperatorAgreementDocument";
	public String marketOperatorMustHaveMarketOperatorAgreementDocument();
	
	public static final String MARKET_OPERATOR_CAN_ONLY_BE_SUSPENDED_IF_DEPENDENT_OBJECTS_ARE_SUSPENDED = "marketOperatorCanOnlyBeSuspendedIfDependentObjectsAreSuspended";
	public String marketOperatorCanOnlyBeSuspendedIfDependentObjectsAreSuspended();
	
	// Market Management Errors
	public static final String MARKET_CANNOT_BE_OVERWRITTEN_WITH_AN_OLDER_VERSION = "marketCannotBeOverwrittenWithAnOlderVersion";
	public String marketCannotBeOverwrittenWithAnOlderVersion();
	
	public static final String NULL_MARKET_CANNOT_BE_CREATED = "nullMarketCannotBeCreated";
	public String nullMarketCannotBeCreated();
	
	public static final String MARKET_CANNOT_BE_CREATED_WITH_NULL_CODE = "marketCannotBeCreatedWithNullCode";
	public String marketCannotBeCreatedWithNullCode();
	
	public static final String MARKET_ALREADY_EXISTS = "marketAlreadyExists";
	public String marketAlreadyExists();
	
	public static final String MULTIPLE_MARKET_RETURNED_FOR_CODE = "multipleMarketReturnedForCode";	
	public String multipleMarketReturnedForCode();
	
	public static final String NO_MARKET_RETURNED_FOR_CODE = "noMarketReturnedForCode";	
	public String noMarketReturnedForCode();
	
	public static final String MARKET_MUST_HAVE_NAME = "marketMustHaveName";
	public String marketMustHaveName();
	
	public static final String MARKET_MUST_HAVE_DESCRIPTION = "marketMustHaveDescription";
	public String marketMustHaveDescription();
	
	public static final String MARKET_MUST_HAVE_CODE = "marketMustHaveCode";
	public String marketMustHaveCode();
	
	public static final String MARKET_CODE_MUST_BE_MARKET_OPERATOR_CODE_AND_INSTRUMENT_CODE = "marketCodeMustBeMarketOperatorCodeAndInstrumentCode";
	public String marketCodeMustBeMarketOperatorCodeAndInstrumentCode();
	
	public static final String MARKET_MUST_HAVE_INSTRUMENT_CODE = "marketMustHaveInstrumentCode";
	public String marketMustHaveInstrumentCode();
	
	public static final String MARKET_MUST_HAVE_MARKET_OPERATOR_CODE = "marketMustHaveMarketOperatorCode";
	public String marketMustHaveMarketOperatorCode();
	
	public static final String MARKET_MUST_HAVE_QUOTE_TYPE = "marketMustHaveQuoteType";
	public String marketMustHaveQuoteType();
	
	public static final String MARKET_MUST_HAVE_BUSINESS_ENTITY = "marketMustHaveBusinessEntity";
	public String marketMustHaveBusinessEntity();
	
	public static final String MARKET_MUST_HAVE_MINIMUM_CONTRACTS_TRADED = "marketMustHaveMinimumContractsTraded";
	public String marketMustHaveMinimumContractsTraded();
	
	public static final String MARKET_MUST_HAVE_POSITIVE_MAXIMUM_CONTRACTS_TRADED = "marketMustHavePositiveMaximumContractsTraded";
	public String marketMustHavePositiveMaximumContractsTraded();
	
	public static final String MARKET_MUST_HAVE_MINIMUM_QUOTE_INCREMENT = "marketMustHaveMinimumQuoteIncrement";
	public String marketMustHaveMinimumQuoteIncrement();
	
	public static final String MARKET_MUST_HAVE_TRADING_SESSION = "marketMustHaveTradingSession";
	public String marketMustHaveTradingSession();
	
	public static final String MARKET_MUST_HAVE_MARKET_OPERATION_DAYS = "marketMustHaveMarketOperationDay";
	public String marketMustHaveMarketOperationDay();
	
	public static final String MARKET_MUST_HAVE_MARKET_TIMEZONE_ID = "marketMustHaveMarketTimeZoneID";
	public String marketMustHaveMarketTimeZoneID();
	
	public static final String NON_CONTINUOUS_MARKET_MUST_HAVE_TRADING_HOURS = "nonContinuousMarketMustHaveTradingHours";
	public String nonContinuousMarketMustHaveTradingHours();
	
	public static final String MARKET_MUST_HAVE_TRADING_DAY_END = "marketMustHaveTradingDayEnd";
	public String marketMustHaveTradingDayEnd();
	
	public static final String CALL_MARKET_MUST_BE_NON_CONTINUOUS = "callMarketMustBeNonContinuous";
	public String callMarketMustBeNonContinuous();
	
	public static final String MARKET_MUST_HAVE_EXECUTION_SYSTEM = "marketMustHaveExecutionSystem";
	public String marketMustHaveExecutionSystem();
	
	public static final String MARKET_MUST_HAVE_SECONDARY_ORDER_PRECEDENCE_RULE = "marketMustHaveSecondaryOrderPrecedenceRule";
	public String marketMustHaveSecondaryOrderPrecedenceRule();
	
	public static final String MARKET_MUST_HAVE_TIME_PRECEDENCE_AS_SECONDARY_ORDER_PRECEDENCE_RULE = "marketMustHaveTimePrecedenceAsSecondaryOrderPrecedenceRule";
	public String marketMustHaveTimePrecedenceAsSecondaryOrderPrecedenceRule();
	
	public static final String MARKET_MUST_HAVE_UNRESTRICTED_ORDER_PRECEDENCE_AS_FIRST_SECONDARY_ORDER_PRECEDENCE_RULE_FOR_MARKETS_THAT_ALLOW_ORDER_SIZE_RESTRICTION = "marketMustHaveUnrestrictedOrderPrecedenceAsFirstSecondaryOrderPrecedenceRuleForMarketsThatAllowOrderSizeRestriction";
	public String marketMustHaveUnrestrictedOrderPrecedenceAsFirstSecondaryOrderPrecedenceRuleForMarketsThatAllowOrderSizeRestriction();
	
	public static final String MARKET_MUST_HAVE_EACH_SECONDARY_ORDER_PRECEDENCE_RULE_ONCE = "marketMustHaveEachSecondaryOrderPrecedenceRuleOnce";
	public String marketMustHaveEachSecondaryOrderPrecedenceRuleOnce();
	
	public static final String MARKET_MUST_HAVE_COMMISSION = "marketMustHaveCommission";
	public String marketMustHaveCommission();
	
	public static final String MARKET_MUST_HAVE_CLEARING_BANK = "marketMustHaveClearingBank";
	public String marketMustHaveClearingBank();
	
	public static final String MARKET_MUST_HAVE_COMMISSION_CURRENCY = "marketMustHaveCommissionCurrency";
	public String marketMustHaveCommissionCurrency();
	
	public static final String MARKET_ONLY_IN_CREATED_OR_SUSPEDED_STATE_CAN_BE_CHANGED = "marketOnlyInCreatedOrSuspendedStateCanBeChanged";
	public String marketOnlyInCreatedOrSuspendedStateCanBeChanged();
	
	public static final String MARKET_ONLY_IN_CREATED_OR_SUSPEDED_STATE_CAN_BE_DELETED = "marketOnlyInCreatedOrSuspendedStateCanBeDeleted";
	public String marketOnlyInCreatedOrSuspendedStateCanBeDeleted();
	
	public static final String ACTIVE_MARKET_CANNOT_BE_SUSPENDED = "activeMarketCannotBeSuspended";
	public String activeMarketCannotBeSuspended();
	
	public static final String ACTIVE_MARKET_CANNOT_BE_DELETED = "activeMarketCannotBeDeleted";
	public String activeMarketCannotBeDeleted();
	
	public static final String ACTIVE_MARKET_CANNOT_BE_CHANGED = "activeMarketCannotBeChanged";
	public String activeMarketCannotBeChanged();
	
	public static final String MARKET_ONLY_IN_CREATED_OR_SUSPEDED_STATE_CAN_BE_APPROVED = "marketOnlyInCreatedOrSuspendedStateCanBeApproved";
	public String marketOnlyInCreatedOrSuspendedStateCanBeApproved();
	
	public static final String MARKET_ONLY_IN_APPROVED_OR_DEACTIVATED_STATE_CAN_BE_SUSPENDED = "marketOnlyInApprovedOrDeactivatedStateCanBeSuspended";
	public String marketOnlyInApprovedOrDeactivatedStateCanBeSuspended();
	
	public static final String MARKET_CAN_ONLY_BE_APPROVED_IF_UNDERLYING_INSTRUMENT_IS_APPROVED = "marketCanOnlyBeApprovedIfUnderlyingInstrumentIsApproved";
	public String marketCanOnlyBeApprovedIfUnderlyingInstrumentIsApproved();
	
	public static final String MARKET_CAN_ONLY_BE_APPROVED_IF_UNDERLYING_MARKET_OPERATOR_IS_APPROVED = "marketCanOnlyBeApprovedIfUnderlyingMarketOperatorIsApproved";
	public String marketCanOnlyBeApprovedIfUnderlyingMarketOperatorIsApproved();
	
	public static final String MARKET_CAN_ONLY_BE_APPROVED_IF_UNDERLYING_BUSINESS_ENTITY_IS_APPROVED = "marketCanOnlyBeApprovedIfUnderlyingBusinessEntityIsApproved";
	public String marketCanOnlyBeApprovedIfUnderlyingBusinessEntityIsApproved();
	
	public static final String MARKET_MUST_HAVE_MARKET_OPERATOR_CONTRACT = "marketMustHaveMarketOperatorContract";
	public String marketMustHaveMarketOperatorContract();
	
	public static final String NON_CLOSED_MARKET_CANNOT_BE_DEACTIVATED = "nonClosedMarketCannotBeDeactivated";
	public String nonClosedMarketCannotBeDeactivated();
	
	public static final String NON_ACTIVE_MARKET_CANNOT_BE_DEACTIVATED = "nonActiveMarketCannotBeDeactivated";
	public String nonActiveMarketCannotBeDeactivated();
	
	public static final String NON_APPROVED_MARKET_CANNOT_BE_ACTIVATED = "nonApprovedMarketCannotBeActivated";
	public String nonApprovedMarketCannotBeActivated();
	
	public static final String ACTIVE_MARKET_CANNOT_BE_ACTIVATED = "activeMarketCannotBeActivated";
	public String activeMarketCannotBeActivated();
	
	public static final String CALL_DATE_MUST_BE_IN_FUTURE = "callDateMustBeInFuture";
	public String callDateMustBeInFuture();
	
	public static final String CALL_DATE_MUST_NOT_FURTHER_15_DAYS = "callDateMustNotBeFurther15Days";
	public String callDateMustNotBeFurther15Days();
	
	public static final String CALL_MARKET_MUST_BE_ACTIVATED_FOR_MARKET_CALL = "callMarketMustBeActivatedForMarketCall";	
	public String callMarketMustBeActivatedForMarketCall();

	public static final String CALL_MARKET_CANNOT_HAVE_CIRCUIT_BREAKER = "callMarketCannotHaveCircuitBreaker";	
	public String callMarketCannotHaveCircuitBreaker();
	
	public static final String STALE_OBJECT_CANNOT_BE_APPROVED = "staleObjectCannotBeApproved";	
	public String staleObjectCannotBeApproved();
	
	// Circuit Breaker Errors
	public static final String CIRCUIT_BREAKER_MUST_HAVE_HALT_RULES_OR_MAX_QUOTE_IMPROVEMENT = "circuitBreakerMustHaveHaltRulesOrMaxQuoteImprovement";
	public String circuitBreakerMustHaveHaltRulesOrMaxQuoteImprovement();
	
	public static final String CIRCUIT_BREAKER_ORDER_REJECT_ACTION_MUST_BE_DEFINED_IF_MAX_QUOTE_IMPROVEMENT_DEFINED = "circuitBreakerOrderRejectActionMustBeDefinedIfMaximumQuoteImprovementDefined";
	public String circuitBreakerOrderRejectActionMustBeDefinedIfMaximumQuoteImprovementDefined();
	
	public static final String CIRCUIT_BREAKER_HALT_RULE_MUST_HAVE_QUOTE_CHANGE_AMOUNT = "circuitBreakerHaltRuleMustHaveQuoteChangeAmount";
	public String circuitBreakerHaltRuleMustHaveQuoteChangeAmount();
	
	public static final String CIRCUIT_BREAKER_HALT_RULE_MUST_HAVE_QUOTE_CHANGE_TYPE = "circuitBreakerHaltRuleMustHaveQuoteChangeType";
	public String circuitBreakerHaltRuleMustHaveQuoteChangeType();
	
	// Matched Trade Errors 
	public static final String NULL_TRADE_CANNOT_BE_CREATED = "nullTradeCannotBeCreated";
	public String nullTradeCannotBeCreated();
	
	public static final String TRADE_ID_MUST_NOT_BE_DEFINED = "tradeIdMustNotBeDefined";
	public String tradeIdMustNotBeDefined();
	
	public static final String TRADE_ALREADY_EXISTS = "tradeAlreadyExists";
	public String tradeAlreadyExists();
	
	public static final String MULTIPLE_TRADE_RETURNED_FOR_KEY = "multipleTradeReturnedForKey";	
	public String multipleTradeReturnedForKey();
	
	public static final String MULTIPLE_TRADE_RETURNED_FOR_TRANSACTION_ID = "multipleTradeReturnedForTransactionId";	
	public String multipleTradeReturnedForTransactionId();
	
	public static final String TRADE_CANNOT_BE_CREATED_ON_NON_EXISTENT_MARKET = "tradeCannotBeCreatedOnNonExistentMarket";
	public String tradeCannotBeCreatedOnNonExistentMarket();
		
	// properties 
	public static final String DUPLICATE_PROPERTY = "duplicateProperty";
	public String duplicateProperty();
	
	public static final String INVALID_PROPERTY_NAME = "invalidPropertyName";
	public String invalidPropertyName();
	
	public static final String INVALID_PROPERTY_VALUE = "invalidPropertyValue";
	public String invalidPropertyValue();
	
	public static final String INVALID_PROPERTY_UNIT = "invalidPropertyUnit";
	public String invalidPropertyUnit();
	
	public static final String USER_ATTEMPTED_CROSS_SITE_SCRIPTING = "userAttemptedCrossSiteScripting";
	public String userAttemptedCrossSiteScripting();
	
	public static final String USER_TRIED_TO_SET_A_SYSTEM_CONTROLLED_PROPERTY = "userTriedToSetASystemControlledProperty";
	public String userTriedToSetASystemControlledProperty();
	
	
}
