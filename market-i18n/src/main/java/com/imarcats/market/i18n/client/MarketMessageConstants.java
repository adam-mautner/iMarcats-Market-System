package com.imarcats.market.i18n.client;

/**
 * Internationalized Messages for Market System
 * @author Adam
 * NOTE: All other Messages are like Sentences
 */
public interface MarketMessageConstants {

	// General 
	public static final String LOADING_FROM = "loadingFrom";
	String loadingFrom(String brandName_);
	
	// Login Errors 
	public static final String LOGIN_FAILED = "loginFailed";
	String loginFailed();
	
	// Business Entity Management
	public static final String DO_YOU_WANT_TO_SUSPEND_BUSINESS_ENTITY = "doYouWantToSuspendBusinessEntity";
	String doYouWantToSuspendBusinessEntity(String businessEntityCode_);
	
	public static final String DO_YOU_WANT_TO_MARK_BUSINESS_ENTITY_DELETED_PERMANENTLY = "doYouWantToMarkBusinessEntityDeletedPemanently";
	String doYouWantToMarkBusinessEntityDeletedPemanently(String businessEntity_);
	
	// User Management
	public static final String DO_YOU_WANT_TO_CHANGE_APPROVED_ACCOUNT = "doYouWantToChangeApprovedAccount";
	String doYouWantToChangeApprovedAccount();	
	
	public static final String DO_YOU_WANT_TO_MARK_USER_DELETED_PERMANENTLY = "doYouWantToMarkUserDeletedPemanently";
	String doYouWantToMarkUserDeletedPemanently(String user_);
	
	public static final String FAILED_CREATING_USER = "failedCreatingUser";
	String failedCreatingUser();
	
	public static final String FAILED_CHANGING_USER = "failedChangingUser";
	String failedChangingUser();
	
	public static final String FAILED_CHANGING_PASSWORD = "failedChangingPassword";
	String failedChangingPassword();
	
	public static final String DO_YOU_WANT_TO_SUSPEND_USER = "doYouWantToSuspendUser";
	String doYouWantToSuspendUser(String userID_);
	
	public static final String DO_YOU_WANT_TO_CANCEL_ORDERS_FOR_USER = "doYouWantToCancelOrdersForUser";
	String doYouWantToCancelOrdersForUser(String userID_);
	
	public static final String DO_YOU_WANT_TO_DELETE_CONFIGURATION_FOR_USER = "doYouWantToDeleteConfigurationForUser";
	String doYouWantToDeleteConfigurationForUser(String userID_);
	
	// Group Management
	public static final String ALL_MY_GROUP_MEMBERSHIPS = "allMyGroupMemberships";
	String allMyGroupMemberships();
	
	public static final String MEMBERS_IN_GROUP = "membersInGroup";
	String membersInGroup(String groupID_);
	
	public static final String DO_YOU_WANT_TO_APPROVE_MEMBERSHIP = "doYouWantToApproveMembership";
	String doYouWantToApproveMembership(String userID_, String groupID_);
	
	public static final String DO_YOU_WANT_TO_DELETE_MEMBERSHIP = "doYouWantToDeleteMembership";
	String doYouWantToDeleteMembership(String userID_, String groupID_);
	
	public static final String DO_YOU_WANT_TO_DELETE_GROUP = "doYouWantToDeleteGroup";
	String doYouWantToDeleteGroup(String groupID_);
	
	// Properties 
	public static final String DO_YOU_WANT_TO_DELETE_PROPERTY = "doYouWantToDeleteProperty";
	String doYouWantToDeleteProperty(String propertyName_);
	
	// Market Objects 
	public static final String DO_YOU_WANT_TO_DELETE_MARKET_OBJECT = "doYouWantToDeleteMarketObject";
	String doYouWantToDeleteMarketObject(String marketObjectCode_, String marketObjectType_);
	
	public static final String DO_YOU_WANT_TO_CHANGE_APPROVED_MARKET_OBJECT = "doYouWantToChangeApprovedMarketObject";
	String doYouWantToChangeApprovedMarketObject(String marketObjectCode_, String marketObjectType_);	
	
	// Halt Rules
	public static final String DO_YOU_WANT_TO_DELETE_HALT_RULE = "doYouWantToDeleteHaltRule";
	String doYouWantToDeleteHaltRule();	
	
	// Property Error Messages
	public static final String PROPERTY_NAME_ALREADY_EXISTS = "propertyNameAlreadyExists";
	String propertyNameAlreadyExists(String propertyName_);
	
	// Permission Messages 
	public static final String ALL_MY_PERMISSIONS = "allMyPermissions";
	String allMyPermissions();
	
	public static final String DO_YOU_WANT_TO_REVOKE_PERMISSIONS_FOR_USER_AND_OBJECT = "doYouWantToRevokePermissionForUserAndObject";
	String doYouWantToRevokePermissionForUserAndObject(String permissionType_, String userID_, String userType_, String objectID_, String objectClass_);

	public static final String PERMISSIONS_FOR_USER_AND_OBJECT = "permissionForUserAndObject";
	String permissionOnObject(String permissionClass_, String permissionType_, String objectID_, String objectClass_);
	
	public static final String SELECT_PERMISSION = "selectPermission";
	String selectPermission();
	
	public static final String FAILED_GRANTING_PERMISSION = "failedGrantingPermission";
	String failedGrantingPermission();
	
	public static final String FAILED_REVOKING_PERMISSION = "failedRevokingPermission";
	String failedRevokingPermission();
	
	// Validation Errors
	public static final String STRING_MUST_BE_VALID = "stringMustBeValidIdentifier";
	String stringMustBeValidIdentifier(String stringName_);
	
	public static final String FIELD_MUST_BE_VALID_EMAIL_ADDRESS = "fieldMustBeValidEmailAddress";
	String fieldMustBeValidEmailAddress(String stringName_);
	
	public static final String REQUIRED_FIELD = "requiredField";
	String requiredField(String fieldName_);

	public static final String FIELD_MUST_BE_VALID = "fieldMustBeValid";
	String fieldMustBeValid(String fieldName_, String type_);
	
	public static final String FIELD_IS_TOO_LONG_OR_SHORT = "fieldIsTooLongOrShort";
	String fieldIsTooLongOrShort(String fieldName_);
	
	public static final String PASSWORDS_DO_NOT_MATCH = "passwordsDoNotMatch";
	String passwordsDoNotMatch();
	
	public static final String FIELD_MUST_BE_POSITIVE_NUMBER = "fieldMustBePositiveNumber";
	String fieldMustBePositiveNumber(String fieldName_);
	
	// Selection Errors 
	public static final String NOTHING_HAS_BEEN_SELECTED = "nothingHasBeenSelected";
	String nothingHasBeenSelected();
	
	// Market Object Management
	public static final String SELECT_OBJECT = "selectObject";
	String selectObject(String objectClass_);
	
	public static final String DO_YOU_WANT_TO_APPROVE_OBJECT = "doYouWantToApproveObject";
	String doYouWantToApproveObject(String objectID_, String objectType_);	
	
	public static final String DO_YOU_WANT_TO_SUSPEND_OBJECT = "doYouWantToSuspendObject";
	String doYouWantToSuspendObject(String objectID_, String objectType_);	
	
	// Market Object Management Errors
	public static final String INVALID_PRODUCT = "invalidProduct";
	String invalidProduct(String code_);
	
	public static final String INVALID_INSTRUMENT = "invalidInstrument";
	String invalidInstrument(String code_);

	public static final String INVALID_MARKET_OPERATOR = "invalidMarketOperator";
	String invalidMarketOperator(String code_);
	
	public static final String INVALID_ASSET_CLASS = "invalidAssetClass";
	String invalidAssetClass(String code_);
	
	// Instrument Management
	public static final String FAILED_ASSIGNING_ASSET_CLASS = "failedAssigningAssetClass";
	String failedAssigningAssetClass();
	
	// Market Management
	public static final String DO_YOU_WANT_TO_ACTIVATE_MARKET = "doYouWantToActivateMarket";
	String doYouWantToActivateMarket(String code_);
	
	public static final String DO_YOU_WANT_TO_DEACTIVATE_MARKET = "doYouWantToDeactivateMarket";
	String doYouWantToDeactivateMarket(String code_);
	
	public static final String DO_YOU_WANT_TO_EMERGENCY_CLOSE_MARKET = "doYouWantToEmergencyCloseMarket";
	String doYouWantToEmergencyCloseMarket(String code_);
	
	public static final String FAILED_ACTIVATING_MARKET = "failedActivatingMarket";
	String failedActivatingMarket();
	
	public static final String PRODUCT_HINT = "productHint";
	String productHint();
	
	public static final String PRODUCT_PROPERTIES_HINT = "productPropertiesHint";
	String productPropertiesHint();
	
	public static final String INSTRUMENT_HINT = "instrumentHint";
	String instrumentHint();
	
	public static final String INSTRUMENT_PROPERTIES_HINT = "instrumentPropertiesHint";
	String instrumentPropertiesHint();
	
	public static final String ASSET_CLASS_HINT = "assetClassHint";
	String assetClassHint();
	
	public static final String ASSET_CLASS_PROPERTIES_HINT = "assetClassPropertiesHint";
	String assetClassPropertiesHint();
	
	public static final String MARKET_OPERATOR_HINT = "marketOperatorHint";
	String marketOperatorHint();
	
	public static final String MARKET_HINT = "marketHint";
	String marketHint();
	
	public static final String ROLLABLE_PROPERTIES_HINT = "rollablePropertiesHint";
	String rollablePropertiesHint();
	
	public static final String PERMISSION_HINT = "permissionHint";
	String permissionHint();

	public static final String GROUP_HINT = "groupHint";
	String groupHint();
	
	public static final String USER_HINT = "userHint";
	String userHint();

	public static final String AUDIT_HINT = "auditHint";
	public String auditHint();
	
	public static final String TRADE_HINT = "tradeHint";
	public String tradeHint();

	public static final String BUSINESS_ENTITY_HINT = "businessEntityHint";
	public String businessEntityHint();
	
	public static final String ENTER_MARKET_CODE = "enterMarketCode";
	public String enterMarketCode();
	
	public static final String MARKET_OPEN_FOR_TRADING = "marketOpenForTrading";
	public String marketOpenForTrading();
	
	public static final String MARKET_CLOSED_FOR_TRADING = "marketClosedForTrading";
	public String marketClosedForTrading();
	
	public static final String MARKET_CLOSED_ON_WEEKENDS_UNLESS_MARKED_OTHERWISE = "marketClosedOnWeekendsUnlessMarkedOtherwise";
	public String marketClosedOnWeekendsUnlessMarkedOtherwise();
	
	public static final String MARKET_MUST_BE_CREATED_AND_SAVED_FOR_CALENDAR_UPLOAD = "marketMustBeCreatedAndSavedForCalendarUpload";
	public String marketMustBeCreatedAndSavedForCalendarUpload();
	
	// Document Errors
	public static final String UNABLE_TO_UPLOAD_DOCUMENT = "unableToUploadDocument";
	String unableToUploadDocument(String documentName_);
	
	public static final String NO_DOCUMENT_FILE_SELECTED = "noDocumentFileSelected";
	String noDocumentFileSelected(String documentName_);
	
	// Date Time Errors
	public static final String START_DATE_MUST_BE_BEFORE_DATE = "startDateMustBeBeforeEndDate";
	String startDateMustBeBeforeEndDate();
	
	public static final String START_TIME_MUST_BE_BEFORE_TIME = "startTimeMustBeBeforeEndTime";
	String startTimeMustBeBeforeEndTime();
	
	public static final String START_TIME_MUST_BE_VALID_TIME_STRING = "startTimeMustBeValidTimeString";
	String startTimeMustBeValidTimeString();
	
	public static final String END_TIME_MUST_BE_VALID_TIME_STRING = "endTimeMustBeValidTimeString";
	String endTimeMustBeValidTimeString();
	
	public static final String TIME_MUST_BE_VALID_TIME_STRING = "timeMustBeValidTimeString";
	String timeMustBeValidTimeString();
	
	public static final String TIME_OF_DAY_OBJECTS_IN_TIME_PERIOD_MUST_HAVE_THE_SAME_TIME_ZONE = "timeOfDayObjectsInTimePeriodMustHaveTheSameTimeZone";
	String timeOfDayObjectsInTimePeriodMustHaveTheSameTimeZone();
	
	// order
	public static final String LIMIT_QUOTE_MUST_BE_VALID_DECIMAL = "limitQuoteMustBeValidDecimalNumber";
	String limitQuoteMustBeValidDecimalNumber();
	
	// market 
	public static final String MAX_OPEN_MARKET_LIMIT_EXCEEDED = "maxOpenMarketLimitExceeded";
	String maxOpenMarketLimitExceeded();
	
	public static final String SELECT_ASSET_CLASS_INSTRUMENT_AND_MARKET_THEN_OPEN = "selectAssetClassInstrumentAndMarketThenOpen";
	String selectAssetClassInstrumentAndMarketThenOpen();
	
	public static final String MARKET_OPENED_OPEN_MORE_MARKETS_OR_CLOSE_THE_DIALOG = "marketOpenedOpenMoreMarketsOrCloseTheDialog";
	String marketOpenedOpenMoreMarketsOrCloseTheDialog();
	
	// trades 
	public static final String ALL_TRADES_FOR_USER = "allTradesForUser";
	String allTradesForUser(String userId_);
	
	public static final String ALL_TRADES_FOR_USER_ON_MARKET = "allTradesForUserOnMarket";
	String allTradesForUserOnMarket(String userId_, String marketCode_);
}
