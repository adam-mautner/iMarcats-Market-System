package com.imarcats.interfaces.client.v100.dto;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;

import com.imarcats.interfaces.client.v100.dto.types.ActivationStatus;
import com.imarcats.interfaces.client.v100.dto.types.AuditInformationDto;
import com.imarcats.interfaces.client.v100.dto.types.BusinessCalendarDto;
import com.imarcats.interfaces.client.v100.dto.types.CircuitBreakerDto;
import com.imarcats.interfaces.client.v100.dto.types.ExecutionSystem;
import com.imarcats.interfaces.client.v100.dto.types.MarketState;
import com.imarcats.interfaces.client.v100.dto.types.QuoteAndSizeDto;
import com.imarcats.interfaces.client.v100.dto.types.QuoteDto;
import com.imarcats.interfaces.client.v100.dto.types.QuoteType;
import com.imarcats.interfaces.client.v100.dto.types.RecurringActionDetail;
import com.imarcats.interfaces.client.v100.dto.types.SecondaryOrderPrecedenceRuleType;
import com.imarcats.interfaces.client.v100.dto.types.TimeOfDayDto;
import com.imarcats.interfaces.client.v100.dto.types.TimePeriodDto;
import com.imarcats.interfaces.client.v100.dto.types.TradingSession;

/**
 * Market is the place where Sellers meet Buyers.
 * @author Adam
 */
public class MarketDto implements ActivatableMarketObjectDto {

	/**
	 * Short Code for the Market - Primary Key of the Market for the Users
	 * Required 
	 * 
	 * Note: The Validator requires Market Code to be _instrumentCode + MARKET_CODE_SEPARATOR + _marketOperatorCode.
	 */
//	@Column(name="MARKET_CODE", unique=true, length=DataLengths.MARKET_OBJECT_CODE_LENGTH)
	private String _marketCode;
	
	/**
	 * Current Best Bid on the Market
	 * 
	 * Note: This is only used, when the Market object is sent to the client, 
	 * 		 Internal processes should take the best bid from Order Book 
	 * Optional
	 * TODO: Remove, when DTOs are created 
	 */
	private QuoteAndSizeDto _currentBestBid;

	/**
	 * Current Best Ask on the Market
	 * 
	 * Note: This is only used, when the Market object is sent to the client, 
	 * 		 Internal processes should take the best ask from Order Book 
	 * Optional
	 * TODO: Remove, when DTOs are created 
	 */
	private QuoteAndSizeDto _currentBestAsk;
	
	/**
	 * ID of the Instrument traded on this Market
	 * Required
	 */
//	@Column(name="INSTRUMENT_CODE", nullable=false, length=DataLengths.MARKET_OBJECT_CODE_LENGTH)
    private String _instrumentCode;
	
	/**
	 * Name of the Market
	 * Required
	 */
//	@Column(name="NAME", nullable=false, length=DataLengths.MARKET_OBJECT_NAME_LENGTH)
	private String _name;
	
	/**
	 * Description of the Market
	 * Required
	 */
//	@Column(name="DESCRIPTION", nullable=false, length=DataLengths.MARKET_OBJECT_DESCRIPTION_LENGTH)
	private String _description;

	/**
	 * Reference to the Market Operator (By Coder)
	 * Required
	 */
//	@Column(name="MARKET_OPERATOR_CODE", nullable=false, length=DataLengths.MARKET_OBJECT_DESCRIPTION_LENGTH)
	private String _marketOperatorCode;
	
	/**
	 * Reference to the Market Operation Contract
	 * Optional
	 * 
	 * Note: This could be a URL.
	 */
//	@Column(name="MARKET_OPERATION_CONTRACT", length=DataLengths.MARKET_OBJECT_DOCUMENT_LINK_LENGTH)
	private String _marketOperationContract;
	
	/**
	 * Type of the Value what is Quoted on the Market for this Instrument (Value from QuoteType)
	 * Required
	 */
	private QuoteType _quoteType; 
	
	/**
	 * Reference to the Market's Business Entity 
	 * Required
	 */
//	@Column(name="BUSINESS_ENTITY_CODE", nullable=false, length=DataLengths.MARKET_OBJECT_CODE_LENGTH)
	private String _businessEntityCode;

	/**
	 * Minimum Number of Contracts Traded (Lots, Odd-Lots), 0 if not defined
	 * Required
	 */
	private Integer _minimumContractsTraded = 0;
	
	/**
	 * Maximum Number of Contracts Traded (Lots, Odd-Lots), Integer.MAX_VALUE if not defined
	 * Optional
	 */
	private Integer _maximumContractsTraded = Integer.MAX_VALUE;
	
	/**
	 * Minimum Quote change that needs to be made on the Market
	 * Required
	 */
	private Double _minimumQuoteIncrement;
	
	/**
	 * Market Time Zone ID
	 * Required
	 */
//	@Column(name="MARKET_TIMEZONE", nullable=false, length=DataLengths.TIMEZONE_LENGTH)
	private String _marketTimeZoneID;
	
	/**
	 * Trading Session type (Value from TradingSession)
	 * Required
	 */
	private TradingSession _tradingSession = TradingSession.NonContinuous;

	/**
	 * Defines when the Market is Closed, and when Open - For Non-Continuous
	 * Optional
	 */
	private TimePeriodDto _tradingHours;
	
	/**
	 * End of Trading Day, Positions Marked-to-Market and Settlement Happens - For Non-Call Markets Only
	 * Optional
	 */
	private TimeOfDayDto _tradingDayEnd;
	
	/**
	 * Business Calendar of the Market
	 * Required
	 */
	private BusinessCalendarDto _businessCalendar;

	/**
	 * Defines on which days the Market will operate 
	 * In case of OnBusinessDaysOnly or OnBusinessDaysAndWeekdays 
	 * Business Calendar will be used  
	 * 
	 * Required for For Non-Call Markets and Non-Continuous Markets, anyway
	 * Optional
	 */
	private RecurringActionDetail _marketOperationDays;
	
	/**
	 * Defines how the Orders will be Executed on the Market (Value from ExecutionSystem)
	 * Required
	 */
	private ExecutionSystem _executionSystem = ExecutionSystem.Combined;
	
	/**
	 * Defines how the Orders will be organized in the Book (Value from SecondaryOrderPrecedenceRule)
	 * Required
	 */
	private List<SecondaryOrderPrecedenceRuleType> _secondaryOrderPrecedenceRules;
	
	/**
	 * Defines if the Market allows Hidden Orders
	 * Optional
	 */
	private Boolean _allowHiddenOrders;
	
	/**
	 * Defines if the Market allows Size Restriction on Orders
	 * Optional
	 */
	private Boolean _allowSizeRestrictionOnOrders;
	
	/**
	 * Defines the Circuit Breaker of the Market
	 * Required
	 */
	private CircuitBreakerDto _circuitBreaker;
	
	/**
	 * Defines the Clearing Bank
	 * Required
	 */
//	@Column(name="CLEARING_BANK", nullable=false, length=DataLengths.MARKET_OBJECT_CODE_LENGTH)
	private String _clearingBank;
	
	/**
	 * Commission charged for the Trade on the Market
	 * Required
	 */
	@Column(name="COMMISSION", nullable=false)
	private Double _commission;

	/**
	 * Currency  of Commission charged for the Trade on the Market
	 * Required
	 */
//	@Column(name="COMMISSION_CURRENCY", nullable=false, length=DataLengths.CURRENCY_CODE_LENGTH)
	private String _commissionCurrency;
	
	/**
	 * Audit Information, like who created the Market and when
	 * Optional
	 */
	private AuditInformationDto _creationAudit;

	/**
	 * Approval Audit Information, like who Approved the Market and when
	 * Optional
	 */
	private AuditInformationDto _approvalAudit;

	/**
	 * Suspension Audit Information, like who Suspended the Market and when
	 * Optional
	 */	
	private AuditInformationDto _suspensionAudit;
	
	/**
	 * Change Audit Information, like who Changed the Market and when
	 * Optional
	 */
	private AuditInformationDto _changeAudit;

	/**
	 * Rollover Audit Information, like who Rolled Over the Market and when
	 * Optional
	 */
	private AuditInformationDto _rolloverAudit;

	/**
	 * Activation Audit Information, like who Activated Over the Market and when
	 * Optional
	 */
	private AuditInformationDto _activationAudit;
	
	/**
	 * Deactivation Audit Information, like who Deactivated Over the Market and when
	 * Optional
	 */
	private AuditInformationDto _deactivationAudit;
	
	/**
	 * Date of the Next Market Call - For Call Markets Only
	 * Optional
	 */
	private Timestamp _nextMarketCallDate;
	
	/**
	 * Opening State of the Market (Value from MarketState)
	 * Required
	 */
	private MarketState _state;
	
	/**
	 * Supports the Activation Workflow of the Market (Value from ActivationStatus)
	 * 
	 * Note: This needs to be a String for Datastore to Query on this field
	 * Required 
	 */
	private ActivationStatus _activationStatus;

	/**
	 * Last Trade on the Market
	 * Optional
	 */
	private QuoteAndSizeDto _lastTrade;
	
	/**
	 * Previous Last Trade on the Market
	 * Optional
	 */
	private QuoteAndSizeDto _previousLastTrade; 

	/**
	 * Previous Best Bid on the Market
	 * Optional
	 */
	private QuoteAndSizeDto _previousBestBid;

	/**
	 * Previous Best Ask on the Market
	 * Optional
	 */
	private QuoteAndSizeDto _previousBestAsk;
	
	/**
	 * Opening Quote of the Market, or null
	 * Optional
	 */
	private QuoteDto _openingQuote;
	
	/**
	 * Closing Quote of the Market, or null
	 * Optional
	 */
	private QuoteDto _closingQuote;
	
	/**
	 * Previous Opening Quote of the Market, or null
	 * Optional
	 */
	private QuoteDto _previousOpeningQuote;
	
	/**
	 * Previous Closing Quote of the Market, or null
	 * Optional
	 */
	private QuoteDto _previousClosingQuote;

	/**
     * Date and Time, when the Object was Last Updated
     * Required
     */
    // TODO: Change to timestamp
	private Date _lastUpdateTimestamp;
	
	/**
	 * Version of the object in the datastore 
	 */
	private Long _versionNumber;
	
	public MarketState getState() {
		return _state;
	}

	public void setState(MarketState state_) {
		_state = state_;
	}

	public void setActivationStatus(ActivationStatus activationStatus) {
		_activationStatus = activationStatus;
	}

	public ActivationStatus getActivationStatus() {
		return _activationStatus;
	}
	
	public void setNextMarketCallDate(Date nextMarketCallDate) {
		_nextMarketCallDate = nextMarketCallDate != null 
				? new Timestamp(nextMarketCallDate.getTime())
				: null;
	}

	public Date getNextMarketCallDate() {
		return _nextMarketCallDate;
	}
	public String getInstrumentCode() {
		return _instrumentCode;
	}

	public void setInstrumentCode(String instrumentCode_) {
		_instrumentCode = instrumentCode_;
	}

	public String getName() {
		return _name;
	}

	public void setName(String name_) {
		_name = name_;
	}

	public String getDescription() {
		return _description;
	}

	public void setDescription(String description_) {
		_description = description_;
	}

	public String getMarketOperationContract() {
		return _marketOperationContract;
	}

	public void setMarketOperationContract(String marketOperationContract_) {
		_marketOperationContract = marketOperationContract_;
	}

	public QuoteType getQuoteType() {
		return _quoteType;
	}

	public void setQuoteType(QuoteType quoteType_) {
		_quoteType = quoteType_;
	}

	public int getMinimumContractsTraded() {
		return _minimumContractsTraded != null ? _minimumContractsTraded : 0;
	}

	public void setMinimumContractsTraded(int minimumContractsTraded_) {
		_minimumContractsTraded = minimumContractsTraded_;
	}

	public int getMaximumContractsTraded() {
		return _maximumContractsTraded != null ? _maximumContractsTraded : Integer.MAX_VALUE;
	}

	public void setMaximumContractsTraded(int maximumContractsTraded_) {
		_maximumContractsTraded = maximumContractsTraded_;
	}

	public double getMinimumQuoteIncrement() {
		return _minimumQuoteIncrement != null ? _minimumQuoteIncrement : 0;
	}

	public void setMinimumQuoteIncrement(double minimumQuoteIncrement_) {
		_minimumQuoteIncrement = minimumQuoteIncrement_;
	}

	public TradingSession getTradingSession() {
		return _tradingSession;
	}

	public void setTradingSession(TradingSession tradingSession_) {
		_tradingSession = tradingSession_;
	}

	public TimePeriodDto getTradingHours() {
		return _tradingHours;
	}

	public void setTradingHours(TimePeriodDto tradingHours_) {
		_tradingHours = tradingHours_;
	}

	public TimeOfDayDto getTradingDayEnd() {
		return _tradingDayEnd;
	}

	public void setTradingDayEnd(TimeOfDayDto tradingDayEnd_) {
		_tradingDayEnd = tradingDayEnd_;
	}

	public ExecutionSystem getExecutionSystem() {
		return _executionSystem;
	}

	public void setExecutionSystem(ExecutionSystem executionSystem_) {
		_executionSystem = executionSystem_;
	}

	public List<SecondaryOrderPrecedenceRuleType> getSecondaryOrderPrecedenceRules() {
		return _secondaryOrderPrecedenceRules;
	}

	public void setSecondaryOrderPrecedenceRules(List<SecondaryOrderPrecedenceRuleType> secondaryOrderPrecedenceRule_) {
		_secondaryOrderPrecedenceRules = secondaryOrderPrecedenceRule_;
	}

	public double getCommission() {
		return _commission != null ? _commission : 0;
	}

	public void setCommission(double commission_) {
		_commission = commission_;
	}
	
	public void setCircuitBreaker(CircuitBreakerDto circuitBreaker) {
		_circuitBreaker = circuitBreaker;
	}

	public CircuitBreakerDto getCircuitBreaker() {
		return _circuitBreaker;
	}
	

	public void setBusinessCalendar(BusinessCalendarDto businessCalendar) {
		_businessCalendar = businessCalendar;
	}

	public BusinessCalendarDto getBusinessCalendar() {
		return _businessCalendar;
	}
	
	public void setMarketCode(String marketCode) {
		_marketCode = marketCode;
	}

	public String getMarketCode() {
		return _marketCode;
	}

	public void setMarketOperatorCode(String marketOperatorCode) {
		_marketOperatorCode = marketOperatorCode;
	}

	public String getMarketOperatorCode() {
		return _marketOperatorCode;
	}

	public void setCommissionCurrency(String commissionCurrency) {
		_commissionCurrency = commissionCurrency;
	}

	public String getCommissionCurrency() {
		return _commissionCurrency;
	}

	public void setMarketOperationDays(RecurringActionDetail marketOperationDays) {
		_marketOperationDays = marketOperationDays;
	}

	public RecurringActionDetail getMarketOperationDays() {
		return _marketOperationDays;
	}

	public void setAllowHiddenOrders(boolean allowHiddenOrders) {
		_allowHiddenOrders = allowHiddenOrders;
	}

	public boolean getAllowHiddenOrders() {
		return _allowHiddenOrders != null ? _allowHiddenOrders : false;
	}

	public void setAllowSizeRestrictionOnOrders(boolean allowSizeRestrictionOnOrders) {
		_allowSizeRestrictionOnOrders = allowSizeRestrictionOnOrders;
	}

	public boolean getAllowSizeRestrictionOnOrders() {
		return _allowSizeRestrictionOnOrders != null ? _allowSizeRestrictionOnOrders : false;
	}
	
	/**
	 * Constructs Market Code out of the Instrument Code and the Market Operator Code, 
	 * like InstrumentCode.MarketOperatorCode, this Code is used within the System. 
	 * Example: COKTCJANDA11.TRG
	 * 
	 * @param marketOperatorCode_ Market Operator Code 
	 * @param instrumentCode_ Instrument Code 
	 * @return Market Code
	 */
	public static String createMarketCode(String marketOperatorCode_, String instrumentCode_) {
		return instrumentCode_ + "." + marketOperatorCode_;
	}

	public void setMarketTimeZoneID(String marketTimeZoneID) {
		_marketTimeZoneID = marketTimeZoneID;
	}

	public String getMarketTimeZoneID() {
		return _marketTimeZoneID;
	}
	
	public Date getLastUpdateTimestamp() {
		return _lastUpdateTimestamp;
	}

	public void setLastUpdateTimestamp(Date lastUpdateTimestamp_) {
		_lastUpdateTimestamp = lastUpdateTimestamp_;
	}

	public void setClearingBank(String clearingBank) {
		_clearingBank = clearingBank;
	}

	public String getClearingBank() {
		return _clearingBank;
	}	

	public void setBusinessEntityCode(String businessEntityCode) {
		_businessEntityCode = businessEntityCode;
	}

	public String getBusinessEntityCode() {
		return _businessEntityCode;
	}

	public QuoteAndSizeDto getCurrentBestBid() {
		return _currentBestBid;
	}

	public void setCurrentBestBid(QuoteAndSizeDto currentBestBid_) {
		_currentBestBid = currentBestBid_;
	}

	public QuoteAndSizeDto getCurrentBestAsk() {
		return _currentBestAsk;
	}

	public void setCurrentBestAsk(QuoteAndSizeDto currentBestAsk_) {
		_currentBestAsk = currentBestAsk_;
	}

	public AuditInformationDto getCreationAudit() {
		return _creationAudit;
	}

	public void setCreationAudit(AuditInformationDto creationAudit_) {
		_creationAudit = creationAudit_;
	}

	public AuditInformationDto getApprovalAudit() {
		return _approvalAudit;
	}

	public void setApprovalAudit(AuditInformationDto approvalAudit_) {
		_approvalAudit = approvalAudit_;
	}

	public AuditInformationDto getSuspensionAudit() {
		return _suspensionAudit;
	}

	public void setSuspensionAudit(AuditInformationDto suspensionAudit_) {
		_suspensionAudit = suspensionAudit_;
	}

	public AuditInformationDto getChangeAudit() {
		return _changeAudit;
	}

	public void setChangeAudit(AuditInformationDto changeAudit_) {
		_changeAudit = changeAudit_;
	}

	public AuditInformationDto getRolloverAudit() {
		return _rolloverAudit;
	}

	public void setRolloverAudit(AuditInformationDto rolloverAudit_) {
		_rolloverAudit = rolloverAudit_;
	}

	public AuditInformationDto getActivationAudit() {
		return _activationAudit;
	}

	public void setActivationAudit(AuditInformationDto activationAudit_) {
		_activationAudit = activationAudit_;
	}

	public AuditInformationDto getDeactivationAudit() {
		return _deactivationAudit;
	}

	public void setDeactivationAudit(AuditInformationDto deactivationAudit_) {
		_deactivationAudit = deactivationAudit_;
	}

	public QuoteAndSizeDto getLastTrade() {
		return _lastTrade;
	}

	public void setLastTrade(QuoteAndSizeDto lastTrade_) {
		_lastTrade = lastTrade_;
	}

	public QuoteAndSizeDto getPreviousLastTrade() {
		return _previousLastTrade;
	}

	public void setPreviousLastTrade(QuoteAndSizeDto previousLastTrade_) {
		_previousLastTrade = previousLastTrade_;
	}

	public QuoteAndSizeDto getPreviousBestBid() {
		return _previousBestBid;
	}

	public void setPreviousBestBid(QuoteAndSizeDto previousBestBid_) {
		_previousBestBid = previousBestBid_;
	}

	public QuoteAndSizeDto getPreviousBestAsk() {
		return _previousBestAsk;
	}

	public void setPreviousBestAsk(QuoteAndSizeDto previousBestAsk_) {
		_previousBestAsk = previousBestAsk_;
	}

	public QuoteDto getOpeningQuote() {
		return _openingQuote;
	}

	public void setOpeningQuote(QuoteDto openingQuote_) {
		_openingQuote = openingQuote_;
	}

	public QuoteDto getClosingQuote() {
		return _closingQuote;
	}

	public void setClosingQuote(QuoteDto closingQuote_) {
		_closingQuote = closingQuote_;
	}

	public QuoteDto getPreviousOpeningQuote() {
		return _previousOpeningQuote;
	}

	public void setPreviousOpeningQuote(QuoteDto previousOpeningQuote_) {
		_previousOpeningQuote = previousOpeningQuote_;
	}

	public QuoteDto getPreviousClosingQuote() {
		return _previousClosingQuote;
	}

	public void setPreviousClosingQuote(QuoteDto previousClosingQuote_) {
		_previousClosingQuote = previousClosingQuote_;
	}

	public void setMinimumContractsTraded(Integer minimumContractsTraded_) {
		_minimumContractsTraded = minimumContractsTraded_;
	}

	public void setMaximumContractsTraded(Integer maximumContractsTraded_) {
		_maximumContractsTraded = maximumContractsTraded_;
	}

	public void setMinimumQuoteIncrement(Double minimumQuoteIncrement_) {
		_minimumQuoteIncrement = minimumQuoteIncrement_;
	}

	public void setAllowHiddenOrders(Boolean allowHiddenOrders_) {
		_allowHiddenOrders = allowHiddenOrders_;
	}

	public void setAllowSizeRestrictionOnOrders(
			Boolean allowSizeRestrictionOnOrders_) {
		_allowSizeRestrictionOnOrders = allowSizeRestrictionOnOrders_;
	}

	public void setCommission(Double commission_) {
		_commission = commission_;
	}

//	public void setNextMarketCallDate(Timestamp nextMarketCallDate_) {
//		_nextMarketCallDate = nextMarketCallDate_;
//	}

	public Long getVersionNumber() {
		return _versionNumber;
	}

	public void setVersionNumber(Long versionNumber_) {
		_versionNumber = versionNumber_;
	}

	@Override
	public String getCode() {
		return getMarketCode();
	}
}
