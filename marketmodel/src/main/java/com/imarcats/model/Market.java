package com.imarcats.model;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;

import com.imarcats.model.meta.DataLengths;
import com.imarcats.model.types.ActivationStatus;
import com.imarcats.model.types.Ask;
import com.imarcats.model.types.AuditInformation;
import com.imarcats.model.types.Bid;
import com.imarcats.model.types.BusinessCalendar;
import com.imarcats.model.types.ClosingQuote;
import com.imarcats.model.types.ExecutionSystem;
import com.imarcats.model.types.LastTrade;
import com.imarcats.model.types.MarketState;
import com.imarcats.model.types.OpeningQuote;
import com.imarcats.model.types.PreviousClosingQuote;
import com.imarcats.model.types.PreviousLastTrade;
import com.imarcats.model.types.PreviousOpeningQuote;
import com.imarcats.model.types.Quote;
import com.imarcats.model.types.QuoteAndSize;
import com.imarcats.model.types.QuoteType;
import com.imarcats.model.types.RecurringActionDetail;
import com.imarcats.model.types.SecondaryOrderPrecedenceRuleType;
import com.imarcats.model.types.TimeOfDay;
import com.imarcats.model.types.TimePeriod;
import com.imarcats.model.types.TradingSession;

/**
 * Market is the place where Sellers meet Buyers.
 * @author Adam
 */
@Entity
@Table(name="MARKET")
public class Market implements MarketModelObject, ActivatableMarketObject, VersionedMarketObject {

	private static final long serialVersionUID = 1L;
	
	/**
	 * Short Code for the Market - Primary Key of the Market for the Users
	 * Required 
	 * 
	 * Note: The Validator requires Market Code to be _instrumentCode + MARKET_CODE_SEPARATOR + _marketOperatorCode.
	 */
	@Id
	@Column(name="MARKET_CODE", unique=true, length=DataLengths.MARKET_OBJECT_CODE_LENGTH)
	private String _marketCode;
	
	/**
	 * Current Best Bid on the Market
	 * 
	 * Note: This is only used, when the Market object is sent to the client, 
	 * 		 Internal processes should take the best bid from Order Book 
	 * Optional
	 * TODO: Remove, when DTOs are created 
	 */
	@Transient
	private Bid _currentBestBid;

	/**
	 * Current Best Ask on the Market
	 * 
	 * Note: This is only used, when the Market object is sent to the client, 
	 * 		 Internal processes should take the best ask from Order Book 
	 * Optional
	 * TODO: Remove, when DTOs are created 
	 */
	@Transient
	private Ask _currentBestAsk;
	
	/**
	 * ID of the Instrument traded on this Market
	 * Required
	 */
	@Column(name="INSTRUMENT_CODE", nullable=false, length=DataLengths.MARKET_OBJECT_CODE_LENGTH)
    private String _instrumentCode;
	
	/**
	 * Name of the Market
	 * Required
	 */
	@Column(name="NAME", nullable=false, length=DataLengths.MARKET_OBJECT_NAME_LENGTH)
	private String _name;
	
	/**
	 * Description of the Market
	 * Required
	 */
	@Column(name="DESCRIPTION", nullable=false, length=DataLengths.MARKET_OBJECT_DESCRIPTION_LENGTH)
	private String _description;

	/**
	 * Reference to the Market Operator (By Coder)
	 * Required
	 */
	@Column(name="MARKET_OPERATOR_CODE", nullable=false, length=DataLengths.MARKET_OBJECT_DESCRIPTION_LENGTH)
	private String _marketOperatorCode;
	
	/**
	 * Reference to the Market Operation Contract
	 * Optional
	 * 
	 * Note: This could be a URL.
	 */
	@Column(name="MARKET_OPERATION_CONTRACT", length=DataLengths.MARKET_OBJECT_DOCUMENT_LINK_LENGTH)
	private String _marketOperationContract;
	
	/**
	 * Type of the Value what is Quoted on the Market for this Instrument (Value from QuoteType)
	 * Optional (set by market management system from Instrument)
	 */
	@Column(name="QUOTE_TYPE", nullable=false)
	@Enumerated(EnumType.STRING) 
	private QuoteType _quoteType; 
	
	/**
	 * Reference to the Market's Business Entity 
	 * Required
	 */
	@Column(name="BUSINESS_ENTITY_CODE", nullable=false, length=DataLengths.MARKET_OBJECT_CODE_LENGTH)
	private String _businessEntityCode;

	/**
	 * Minimum Number of Contracts Traded (Lots, Odd-Lots), 0 if not defined
	 * Required
	 */
	@Column(name="MIMIMUM_CONTRACTS_TRADED", nullable=false)
	private Integer _minimumContractsTraded = 0;
	
	/**
	 * Maximum Number of Contracts Traded (Lots, Odd-Lots), Integer.MAX_VALUE if not defined
	 * Optional
	 */
	@Column(name="MAXIMUM_CONTRACTS_TRADED", nullable=false)
	private Integer _maximumContractsTraded = Integer.MAX_VALUE;
	
	/**
	 * Minimum Quote change that needs to be made on the Market
	 * Required
	 */
	@Column(name="MINIMUM_QUOTE_INCREMENT", nullable=false)
	private Double _minimumQuoteIncrement;
	
	/**
	 * Market Time Zone ID
	 * Required
	 */
	@Column(name="MARKET_TIMEZONE", nullable=false, length=DataLengths.TIMEZONE_LENGTH)
	private String _marketTimeZoneID;
	
	/**
	 * Trading Session type (Value from TradingSession)
	 * Required
	 */
	@Column(name="TRADING_SESSION", nullable=false)
	@Enumerated(EnumType.STRING) 
	private TradingSession _tradingSession = TradingSession.NonContinuous;

	/**
	 * Defines when the Market is Closed, and when Open - For Non-Continuous
	 * Optional
	 */
	@AttributeOverrides({
	    @AttributeOverride(name="_startTime",column=@Column(name="TRADING_HOURS_START_TIME_ID")),
	    @AttributeOverride(name="_endTime",column=@Column(name="TRADING_HOURS_END_TIME_ID")),
	  })
	@Embedded
	private TimePeriod _tradingHours;
	
	/**
	 * End of Trading Day, Positions Marked-to-Market and Settlement Happens - For Non-Call Markets Only
	 * Optional
	 */
	@AttributeOverrides({
	    @AttributeOverride(name="_hour",column=@Column(name="TRADING_DAY_END_HOUR")),
	    @AttributeOverride(name="_minute",column=@Column(name="TRADING_DAY_END_MINUTE")),
	    @AttributeOverride(name="_second",column=@Column(name="TRADING_DAY_END_SECOND")),
	    @AttributeOverride(name="_timeZone",column=@Column(name="TRADING_DAY_END_TIMEZONE")),
	  })
	@Embedded
	private TimeOfDay _tradingDayEnd;
	
	/**
	 * Business Calendar of the Market
	 * Required
	 */
	@Embedded
	private BusinessCalendar _businessCalendar;

	/**
	 * Defines on which days the Market will operate 
	 * In case of OnBusinessDaysOnly or OnBusinessDaysAndWeekdays 
	 * Business Calendar will be used  
	 * 
	 * Required for For Non-Call Markets and Non-Continuous Markets, anyway
	 * Optional
	 */
	@Column(name="MARKET_OPERATION_DAYS")
	@Enumerated(EnumType.STRING) 
	private RecurringActionDetail _marketOperationDays;
	
	/**
	 * Defines how the Orders will be Executed on the Market (Value from ExecutionSystem)
	 * Required
	 */
	@Column(name="EXECUTION_SYSTEM", nullable=false)
	@Enumerated(EnumType.STRING) 
	private ExecutionSystem _executionSystem = ExecutionSystem.Combined;
	
	/**
	 * Defines how the Orders will be organized in the Book (Value from SecondaryOrderPrecedenceRule)
	 * Required
	 */
	// TODO: We need to keep the original order of the entries here, as sorting them requires a complex logic
    @ElementCollection(targetClass=SecondaryOrderPrecedenceRuleType.class)
    @Enumerated(EnumType.STRING) 
    @CollectionTable(name="MARKET_SECONDARY_ORDER_PRECEDENCE_RULES")
    @Column(name="SECONDARY_ORDER_PRECEDENCE_RULES")
	private List<SecondaryOrderPrecedenceRuleType> _secondaryOrderPrecedenceRules;// = createDefaultSecondaryPrecedenceRuleList();
	
	/**
	 * Defines if the Market allows Hidden Orders
	 * Optional
	 */
	@Column(name="ALLOW_HIDDEN_ORDERS", nullable=false)
	private Boolean _allowHiddenOrders;
	
	/**
	 * Defines if the Market allows Size Restriction on Orders
	 * Optional
	 */
	@Column(name="ALLOW_SIZE_RESTRICTION_ON_ORDERS", nullable=false)
	private Boolean _allowSizeRestrictionOnOrders;
	
	/**
	 * Defines the Circuit Breaker of the Market
	 * Required
	 */
	@OneToOne(cascade=CascadeType.ALL, orphanRemoval=true) 
	@JoinColumn(name="CIRCUIT_BREAKER_ID")
	private CircuitBreaker _circuitBreaker;
	
	/**
	 * Defines the Clearing Bank
	 * Required
	 */
	@Column(name="CLEARING_BANK", nullable=false, length=DataLengths.MARKET_OBJECT_CODE_LENGTH)
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
	@Column(name="COMMISSION_CURRENCY", nullable=false, length=DataLengths.CURRENCY_CODE_LENGTH)
	private String _commissionCurrency;
	
	/**
	 * Audit Information, like who created the Market and when
	 * Optional
	 */
	@OneToOne(cascade=CascadeType.ALL, orphanRemoval=true) 
	@JoinColumn(name="CREATION_AUDIT_ID")
	private CreationAudit _creationAudit;

	/**
	 * Approval Audit Information, like who Approved the Market and when
	 * Optional
	 */
	@OneToOne(cascade=CascadeType.ALL, orphanRemoval=true) 
	@JoinColumn(name="APPROVAL_AUDIT_ID")
	private ApprovalAudit _approvalAudit;

	/**
	 * Suspension Audit Information, like who Suspended the Market and when
	 * Optional
	 */	
	@OneToOne(cascade=CascadeType.ALL, orphanRemoval=true) 
	@JoinColumn(name="SUSPENSION_AUDIT_ID")
	private SuspensionAudit _suspensionAudit;
	
	/**
	 * Change Audit Information, like who Changed the Market and when
	 * Optional
	 */
	@OneToOne(cascade=CascadeType.ALL, orphanRemoval=true) 
	@JoinColumn(name="CHANGE_AUDIT_ID")
	private ChangeAudit _changeAudit;

	/**
	 * Rollover Audit Information, like who Rolled Over the Market and when
	 * Optional
	 */
	@OneToOne(cascade=CascadeType.ALL, orphanRemoval=true) 
	@JoinColumn(name="ROLLOVER_AUDIT_ID")
	private RolloverAudit _rolloverAudit;

	/**
	 * Activation Audit Information, like who Activated Over the Market and when
	 * Optional
	 */
	@OneToOne(cascade=CascadeType.ALL, orphanRemoval=true) 
	@JoinColumn(name="ACTIVATION_AUDIT_ID")
	private ActivationAudit _activationAudit;
	
	/**
	 * Deactivation Audit Information, like who Deactivated Over the Market and when
	 * Optional
	 */
	@OneToOne(cascade=CascadeType.ALL, orphanRemoval=true) 
	@JoinColumn(name="DEACTIVATION_AUDIT_ID")
	private DeactivationAudit _deactivationAudit;
	
	/**
	 * Date of the Next Market Call - For Call Markets Only
	 * Optional
	 */
	@Column(name="NEXT_MARKET_CALL_DATE")
	private Timestamp _nextMarketCallDate;
	
	/**
	 * Opening State of the Market (Value from MarketState)
	 * Required
	 */
	@Column(name="STATE", nullable=false)
	@Enumerated(EnumType.STRING) 
	private MarketState _state;
	
	/**
	 * Supports the Activation Workflow of the Market (Value from ActivationStatus)
	 * 
	 * Note: This needs to be a String for Datastore to Query on this field
	 * Required 
	 */
	@Column(name="ACTIVATION_STATUS", nullable=false)
	@Enumerated(EnumType.STRING) 
	private ActivationStatus _activationStatus;

	/**
	 * Buy Book of the Market
	 * 
 	 * Note: This field is defined transient, so it will not be serialized and sent to
	 * 		 the Client (because this object is too heavy weight)
	 * Required
	 */
	@OneToOne(cascade=CascadeType.ALL, orphanRemoval=true) 
	@JoinColumn(name="BUY_BOOK_ID")
	private BuyBook _buyBook;

	/**
	 * Sell Book of the Market
	 * 
	 * Note: This field is defined transient, so it will not be serialized and sent to
	 * 		 the Client (because this object is too heavy weight)
	 * Required
	 */
	@OneToOne(cascade=CascadeType.ALL, orphanRemoval=true) 
	@JoinColumn(name="SELL_BOOK_ID")
	private SellBook _sellBook;

	/**
	 * Last Trade on the Market
	 * Optional
	 */
	@OneToOne(cascade=CascadeType.ALL, orphanRemoval=true) 
	@JoinColumn(name="LAST_TRADE_ID")
	private LastTrade _lastTrade;
	
	/**
	 * Previous Last Trade on the Market
	 * Optional
	 */
	@OneToOne(cascade=CascadeType.ALL, orphanRemoval=true) 
	@JoinColumn(name="PREVIOUS_LAST_TRADE_ID")
	private PreviousLastTrade _previousLastTrade; 

	/**
	 * Previous Best Bid on the Market
	 * Optional
	 */
	@OneToOne(cascade=CascadeType.ALL, orphanRemoval=true) 
	@JoinColumn(name="PREVIOUS_BEST_BID_ID")
	private Bid _previousBestBid;

	/**
	 * Previous Best Ask on the Market
	 * Optional
	 */
	@OneToOne(cascade=CascadeType.ALL, orphanRemoval=true) 
	@JoinColumn(name="PREVIOUS_BEST_ASK_ID")
	private Ask _previousBestAsk;
	
	/**
	 * Opening Quote of the Market, or null
	 * Optional
	 */
	@OneToOne(cascade=CascadeType.ALL, orphanRemoval=true) 
	@JoinColumn(name="OPENING_QUOTE_ID")
	private OpeningQuote _openingQuote;
	
	/**
	 * Closing Quote of the Market, or null
	 * Optional
	 */
	@OneToOne(cascade=CascadeType.ALL, orphanRemoval=true) 
	@JoinColumn(name="CLOSING_QUOTE_ID")
	private ClosingQuote _closingQuote;
	
	/**
	 * Previous Opening Quote of the Market, or null
	 * Optional
	 */
	@OneToOne(cascade=CascadeType.ALL, orphanRemoval=true) 
	@JoinColumn(name="PREVIOUS_OPENING_QUOTE_ID")
	private PreviousOpeningQuote _previousOpeningQuote;
	
	/**
	 * Previous Closing Quote of the Market, or null
	 * Optional
	 */
	@OneToOne(cascade=CascadeType.ALL, orphanRemoval=true) 
	@JoinColumn(name="PREVIOUS_CLOSING_QUOTE_ID")
	private PreviousClosingQuote _previousClosingQuote;
	
	/**
	 * Current Halt Level of the Market, or -1, if the Market has not been Halted today
	 */
	@Column(name="HALT_LEVEL")
	private Integer _haltLevel = -1;
	
	/**
	 * Key for Market Call Action
	 * Optional
	 */
	@Column(name="MARKET_CALL_ACTION_KEY")
	private Long _marketCallActionKey;
	
	/**
	 * Key for Market Open Action
	 * Optional
	 */
	@Column(name="MARKET_OPEN_ACTION_KEY")
	private Long _marketOpenActionKey;
	
	/**
	 * Key for Market Close Action
	 * Optional
	 */
	@Column(name="MARKET_CLOSE_ACTION_KEY")
	private Long _marketCloseActionKey;

	/**
	 * Key for Market Re-Open Action
	 * Optional
	 */
	@Column(name="MARKET_REOPEN_ACTION_KEY")
	private Long _marketReOpenActionKey;
	
	/**
	 * Key for Market Maintenance Action
	 * Optional
	 */
	@Column(name="MARKET_MAINTENANCE_ACTION_KEY")
	private Long _marketMaintenanceActionKey;
	
	/**
	 * Key for Call Market Maintenance Action
	 * Optional
	 */
	@Column(name="CALL_MARKET_MAINTENANCE_ACTION_KEY")
	private Long _callMarketMaintenanceActionKey;
	
	/**
     * Date and Time, when the Object was Last Updated
     * Required
     */
	@Column(name="LAST_UPDATE_TIMESTAMP", nullable=false)
	private Timestamp _lastUpdateTimestamp;
	
	/**
	 * Version of the object in the datastore 
	 */
	@Version
	@Column(name="VERSION")
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

	public BuyBook getBuyBook() {
		return _buyBook;
	}

	public void setBuyBook(BuyBook buyBook_) {
		_buyBook = buyBook_;
	}

	public SellBook getSellBook() {
		return _sellBook;
	}

	public void setSellBook(SellBook sellBook_) {
		_sellBook = sellBook_;
	}

	public void setPreviousLastTrade(QuoteAndSize previousLastTrade_) {
		// this is needed, because Google App Engine's Datastore would not let 
		// change the uncommited classes, since this field may change many times 
		// before commit, we can only copy the values on the object
		// TODO: Is this the same as in JPA?
		if(previousLastTrade_ != null) {
			if(_previousLastTrade == null) {
				_previousLastTrade = 
					new PreviousLastTrade(
							QuoteAndSize.createQuoteAndSize(previousLastTrade_)); // Cloning Quote is needed, because it might be copied from an other persisted Object
			
			} else {
				setQuoteSizeValues(previousLastTrade_, _previousLastTrade.getPreviousLastTrade());
			}
		} else {
			_previousLastTrade = null;
		}
	}

	public QuoteAndSize getPreviousLastTrade() {
		return _previousLastTrade != null 
				? _previousLastTrade.getPreviousLastTrade()
				: null;
	}
	
	public QuoteAndSize getLastTrade() {
		return _lastTrade != null 
					? _lastTrade.getLastTrade() 
					: null;
	}

	public void setLastTrade(QuoteAndSize lastTrade_) {
		// this is needed, because Google App Engine's Datastore would not let 
		// change the uncommited classes, since this field may change many times 
		// before commit, we can only copy the values on the object
		// TODO: Is this the same as in JPA?
		if(lastTrade_ != null) {
			if(_lastTrade == null) {
				_lastTrade = 
					new LastTrade(
							QuoteAndSize.createQuoteAndSize(lastTrade_)); // Cloning Quote is needed, because it might be copied from an other persisted Object
			
			} else {
				setQuoteSizeValues(lastTrade_, _lastTrade.getLastTrade());
			}
		} else {
			_lastTrade = null;
		}
	}

	private void setQuoteSizeValues(QuoteAndSize source_, QuoteAndSize target_) {
		if(source_ != null && target_ != null) {
			target_.setSize(source_.getSize());	
			if(target_.getQuote() == null && source_.getQuote() != null) {
				target_.setQuote(new Quote());
			}
			setQuoteValues(source_.getQuote(), target_.getQuote());		
		}
	}
	
	private void setQuoteValues(Quote source_, Quote target_) {
		if(source_ != null && target_ != null) {
			target_.setQuoteValue(source_.getQuoteValue());
			target_.setValidQuote(source_.getValidQuote());
			target_.setDateOfQuote(source_.getDateOfQuote());
		}
	}

	public QuoteAndSize getCurrentBestBid() {
		return _currentBestBid != null 
					? _currentBestBid.getBid() 
					: null;
	}
	
	public void setCurrentBestBid(QuoteAndSize bid_) {
		// this is needed, because Google App Engine's Datastore would not let 
		// change the uncommited classes, since this field may change many times 
		// before commit, we can only copy the values on the object
		// TODO: Is this the same as in JPA?
		if(bid_ != null) {
			if(_currentBestBid == null) {
				_currentBestBid = 
					new Bid(
							QuoteAndSize.createQuoteAndSize(bid_)); // Cloning Quote is needed, because it might be copied from an other persisted Object
			} else {
				setQuoteSizeValues(bid_, _currentBestBid.getBid());
			}
		} else {
			_currentBestBid = null;
		}
	}
	
	public QuoteAndSize getPreviousBestBid() {
		return _previousBestBid != null 
					? _previousBestBid.getBid() 
					: null;
	}

	public void setPreviousBestBid(QuoteAndSize bid_) {
		// this is needed, because Google App Engine's Datastore would not let 
		// change the uncommited classes, since this field may change many times 
		// before commit, we can only copy the values on the object
		// TODO: Is this the same as in JPA?
		if(bid_ != null) {
			if(_previousBestBid == null) {
				_previousBestBid = 
					new Bid(
							QuoteAndSize.createQuoteAndSize(bid_)); // Cloning Quote is needed, because it might be copied from an other persisted Object
			} else {
				setQuoteSizeValues(bid_, _previousBestBid.getBid());
			}
		} else {
			_previousBestBid = null;
		}
	}
	
	public QuoteAndSize getPreviousBestAsk() {
		return _previousBestAsk != null 
					? _previousBestAsk.getAsk() 
					: null;
	}

	public QuoteAndSize getCurrentBestAsk() {
		return _currentBestAsk != null 
					? _currentBestAsk.getAsk() 
					: null;
	}
	
	public void setPreviousBestAsk(QuoteAndSize ask_) {
		// this is needed, because Google App Engine's Datastore would not let 
		// change the uncommited classes, since this field may change many times 
		// before commit, we can only copy the values on the object
		// TODO: Is this the same as in JPA?
		if(ask_ != null) {
			if(_previousBestAsk == null) {	
				_previousBestAsk = 
					new Ask(
							QuoteAndSize.createQuoteAndSize(ask_)); // Cloning Quote is needed, because it might be copied from an other persisted Object
			} else {
				setQuoteSizeValues(ask_, _previousBestAsk.getAsk());
			}
		} else {
			_previousBestAsk = null;
		}
	}

	public void setCurrentBestAsk(QuoteAndSize ask_) {
		// this is needed, because Google App Engine's Datastore would not let 
		// change the uncommited classes, since this field may change many times 
		// before commit, we can only copy the values on the object
		// TODO: Is this the same as in JPA?
		if(ask_ != null) {
			if(_currentBestAsk == null) {
				_currentBestAsk = 
					new Ask(
							QuoteAndSize.createQuoteAndSize(ask_)); // Cloning Quote is needed, because it might be copied from an other persisted Object
			} else {
				setQuoteSizeValues(ask_, _currentBestAsk.getAsk());
			}
		} else {
			_currentBestAsk = null;
		}
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

	public TimePeriod getTradingHours() {
		return _tradingHours;
	}

	public void setTradingHours(TimePeriod tradingHours_) {
		_tradingHours = tradingHours_;
	}

	public TimeOfDay getTradingDayEnd() {
		return _tradingDayEnd;
	}

	public void setTradingDayEnd(TimeOfDay tradingDayEnd_) {
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

	public AuditInformation getCreationAudit() {
		return _creationAudit != null 
					? _creationAudit.getCreationAudit() 
					: null;
	}

	public void setCreationAudit(AuditInformation creationAudit_) {
		// this is needed, because Google App Engine's Datastore would not let 
		// change the uncommited classes, since this field may change many times 
		// before commit, we can only copy the values on the object
		// TODO: Is this the same as in JPA?
		if(creationAudit_ != null) {
			if(_creationAudit == null) {
				_creationAudit = 
					new CreationAudit(
							AuditInformation.create(creationAudit_)); 
					// Cloning Audit is needed, because it might be copied from an other persisted Object
			
			} else {
				setAudit(creationAudit_, _creationAudit.getCreationAudit());
			}
		} else {
			_creationAudit = null;
		}
	}
	
	public void setApprovalAudit(AuditInformation approvalAudit) {
		// this is needed, because Google App Engine's Datastore would not let 
		// change the uncommited classes, since this field may change many times 
		// before commit, we can only copy the values on the object
		// TODO: Is this the same as in JPA?
		if(approvalAudit != null) {
			if(_approvalAudit == null) {
				_approvalAudit = 
					new ApprovalAudit(
							AuditInformation.create(approvalAudit)); 
					// Cloning Audit is needed, because it might be copied from an other persisted Object
			
			} else {
				setAudit(approvalAudit, _approvalAudit.getApprovalAudit());
			}
		} else {
			_approvalAudit = null;
		}
	}

	public AuditInformation getApprovalAudit() {
		return _approvalAudit != null 
					? _approvalAudit.getApprovalAudit() 
					: null;
	}
	
	public AuditInformation getChangeAudit() {
		return _changeAudit != null 
				? _changeAudit.getChangeAudit() 
				: null;
	}

	public void setChangeAudit(AuditInformation changeAudit_) {
		// this is needed, because Google App Engine's Datastore would not let 
		// change the uncommited classes, since this field may change many times 
		// before commit, we can only copy the values on the object
		// TODO: Is this the same as in JPA?
		if(changeAudit_ != null) {
			if(_changeAudit == null) {
				_changeAudit = 
					new ChangeAudit(
							AuditInformation.create(changeAudit_)); 
					// Cloning Audit is needed, because it might be copied from an other persisted Object
			
			} else {
				setAudit(changeAudit_, _changeAudit.getChangeAudit());
			}
		} else {
			_changeAudit = null;
		}
	}

	public AuditInformation getRolloverAudit() {
		return _rolloverAudit != null 
					? _rolloverAudit.getRolloverAudit() 
					: null;
	}

	public void setRolloverAudit(AuditInformation rolloverAudit_) {
		// this is needed, because Google App Engine's Datastore would not let 
		// change the uncommited classes, since this field may change many times 
		// before commit, we can only copy the values on the object
		// TODO: Is this the same as in JPA?
		if(rolloverAudit_ != null) {
			if(_rolloverAudit == null) {
				_rolloverAudit = 
					new RolloverAudit(
							AuditInformation.create(rolloverAudit_)); 
					// Cloning Audit is needed, because it might be copied from an other persisted Object
			
			} else {
				setAudit(rolloverAudit_, _rolloverAudit.getRolloverAudit());
			}
		} else {
			_rolloverAudit = null;
		}
	}

	public void setSuspensionAudit(AuditInformation suspensionAudit) {
		// this is needed, because Google App Engine's Datastore would not let 
		// change the uncommited classes, since this field may change many times 
		// before commit, we can only copy the values on the object
		// TODO: Is this the same as in JPA?
		if(suspensionAudit != null) {
			if(_suspensionAudit == null) {
				_suspensionAudit = 
					new SuspensionAudit(
							AuditInformation.create(suspensionAudit)); 
					// Cloning Audit is needed, because it might be copied from an other persisted Object
			
			} else {
				setAudit(suspensionAudit, _suspensionAudit.getSuspensionAudit());
			}
		} else {
			_suspensionAudit = null;
		}
	}

	public AuditInformation getSuspensionAudit() {
		return _suspensionAudit != null 
					? _suspensionAudit.getSuspensionAudit() 
					: null;
	}
	
	public AuditInformation getActivationAudit() {
		return _activationAudit != null 
					? _activationAudit.getActivationAudit() 
					: null;
	}

	public void setActivationAudit(AuditInformation activationAudit_) {
		// this is needed, because Google App Engine's Datastore would not let 
		// change the uncommited classes, since this field may change many times 
		// before commit, we can only copy the values on the object
		// TODO: Is this the same as in JPA?
		if(activationAudit_ != null) {
			if(_activationAudit == null) {
				_activationAudit = 
					new ActivationAudit(
							AuditInformation.create(activationAudit_)); 
					// Cloning Audit is needed, because it might be copied from an other persisted Object
			
			} else {
				setAudit(activationAudit_, _activationAudit.getActivationAudit());
			}
		} else {
			_activationAudit = null;
		}
	}
	
	public AuditInformation getDeactivationAudit() {
		return _deactivationAudit != null 
					? _deactivationAudit.getDeactivationAudit() 
					: null;
	}

	public void setDeactivationAudit(AuditInformation deactivationAudit_) {
		// this is needed, because Google App Engine's Datastore would not let 
		// change the uncommited classes, since this field may change many times 
		// before commit, we can only copy the values on the object
		// TODO: Is this the same as in JPA?
		if(deactivationAudit_ != null) {
			if(_deactivationAudit == null) {
				_deactivationAudit = 
					new DeactivationAudit(
							AuditInformation.create(deactivationAudit_)); 
					// Cloning Audit is needed, because it might be copied from an other persisted Object
			
			} else {
				setAudit(deactivationAudit_, _deactivationAudit.getDeactivationAudit());
			}
		} else {
			_deactivationAudit = null;
		}
	}
	
	
	private void setAudit(AuditInformation source_, AuditInformation target_) {
		if(source_ != null && target_ != null) {
			target_.setDateTime(source_.getDateTime());
			target_.setUserID(source_.getUserID());		
		} 
	}
	
	public void setCircuitBreaker(CircuitBreaker circuitBreaker) {
		_circuitBreaker = circuitBreaker;
	}

	public CircuitBreaker getCircuitBreaker() {
		return _circuitBreaker;
	}
	

	public void setBusinessCalendar(BusinessCalendar businessCalendar) {
		// This is needed because of org.hibernate.HibernateException: A collection with cascade="all-delete-orphan" was no longer referenced by the owning entity instance: com.imarcats.model.Market._businessCalendar._businessCalendarDays
		// Likely reason that business calendar (embedded entity) is reset using null
		// Related link: http://stackoverflow.com/questions/5587482/hibernate-a-collection-with-cascade-all-delete-orphan-was-no-longer-referenc
		// TODO: Find a better solution 
		if(businessCalendar != null) {
			if(_businessCalendar == null) {
				_businessCalendar = businessCalendar;
			} else {
				_businessCalendar.getBusinessCalendarDays().clear();
				_businessCalendar.getBusinessCalendarDays().addAll(businessCalendar.getBusinessCalendarDays());
			}
		} else {
			if(_businessCalendar != null) {
				_businessCalendar.getBusinessCalendarDays().clear();
			}
		}
	}

	public BusinessCalendar getBusinessCalendar() {
		return _businessCalendar;
	}
	
	public void setOpeningQuote(Quote openingQuote) {
		// this is needed, because Google App Engine's Datastore would not let 
		// change the uncommited classes, since this field may change many times 
		// before commit, we can only copy the values on the object
		// TODO: Is this the same as in JPA?
		if(openingQuote != null) {
			if(_openingQuote == null) {
				_openingQuote = 
					new OpeningQuote(
							Quote.createQuote(openingQuote)); // Cloning Quote is needed, because it might be copied from an other persisted Object
			
			} else {
				setQuoteValues(openingQuote, _openingQuote.getOpeningQuote());
			}
		} else {
			_openingQuote = null; 
		}
	}

	public Quote getOpeningQuote() {
		return _openingQuote != null 
					? _openingQuote.getOpeningQuote() 
					: null;
	}

	public void setPreviousOpeningQuote(Quote previousOpeningQuote) {
		// this is needed, because Google App Engine's Datastore would not let 
		// change the uncommited classes, since this field may change many times 
		// before commit, we can only copy the values on the object
		// TODO: Is this the same as in JPA?
		if(previousOpeningQuote != null) {
			if(_previousOpeningQuote == null) {
				_previousOpeningQuote = 
					new PreviousOpeningQuote(
							Quote.createQuote(previousOpeningQuote)); // Cloning Quote is needed, because it might be copied from an other persisted Object
			
			} else {
				setQuoteValues(previousOpeningQuote, _previousOpeningQuote.getPreviousOpeningQuote());
			}
		} else {
			_previousOpeningQuote = null; 
		}
	}

	public Quote getPreviousOpeningQuote() {
		return _previousOpeningQuote != null 
					? _previousOpeningQuote.getPreviousOpeningQuote() 
					: null;
	}
	
	public void setClosingQuote(Quote closingQuote) {
		// this is needed, because Google App Engine's Datastore would not let 
		// change the uncommited classes, since this field may change many times 
		// before commit, we can only copy the values on the object
		// TODO: Is this the same as in JPA?
		if(closingQuote != null) {
			if(_closingQuote == null) {
				_closingQuote = 
					new ClosingQuote(
							Quote.createQuote(closingQuote)); // Cloning Quote is needed, because it might be copied from an other persisted Object
			
			} else {
				setQuoteValues(closingQuote, _closingQuote.getClosingQuote());
			} 
		} else {
			_closingQuote = null;
		}
	}

	public Quote getClosingQuote() {
		return _closingQuote != null 
					? _closingQuote.getClosingQuote() 
					: null;
	}

	public void setPreviousClosingQuote(Quote previousClosingQuote) {
		// this is needed, because Google App Engine's Datastore would not let 
		// change the uncommited classes, since this field may change many times 
		// before commit, we can only copy the values on the object
		// TODO: Is this the same as in JPA?
		if(previousClosingQuote != null) {
			if(_previousClosingQuote == null) {
				_previousClosingQuote = 
					new PreviousClosingQuote(
							Quote.createQuote(previousClosingQuote)); // Cloning Quote is needed, because it might be copied from an other persisted Object
			
			} else {
				setQuoteValues(previousClosingQuote, _previousClosingQuote.getPreviousClosingQuote());
			} 
		} else {
			_previousClosingQuote = null;
		}
	}

	public Quote getPreviousClosingQuote() {
		return _previousClosingQuote != null 
					? _previousClosingQuote.getPreviousClosingQuote() 
					: null;
	}
	
	public void setHaltLevel(int haltLevel) {
		_haltLevel = haltLevel;
	}

	public int getHaltLevel() {
		return _haltLevel != null ? _haltLevel : -1;
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

	public Long getMarketCallActionKey() {
		return _marketCallActionKey;
	}

	public void setMarketCallActionKey(Long marketCallActionKey_) {
		_marketCallActionKey = marketCallActionKey_;
	}

	public Long getMarketOpenActionKey() {
		return _marketOpenActionKey;
	}

	public void setMarketOpenActionKey(Long marketOpenActionKey_) {
		_marketOpenActionKey = marketOpenActionKey_;
	}

	public Long getMarketCloseActionKey() {
		return _marketCloseActionKey;
	}

	public void setMarketCloseActionKey(Long marketCloseActionKey_) {
		_marketCloseActionKey = marketCloseActionKey_;
	}

	public Long getMarketReOpenActionKey() {
		return _marketReOpenActionKey;
	}

	public void setMarketReOpenActionKey(Long marketOpenActionKey_) {
		_marketReOpenActionKey = marketOpenActionKey_;
	}

	public Long getMarketMaintenanceActionKey() {
		return _marketMaintenanceActionKey;
	}

	public void setMarketMaintenanceActionKey(Long marketMaintenanceActionKey_) {
		_marketMaintenanceActionKey = marketMaintenanceActionKey_;
	}
	
	public Long getCallMarketMaintenanceActionKey() {
		return _callMarketMaintenanceActionKey;
	}

	public void setCallMarketMaintenanceActionKey(Long callMarketMaintenanceActionKey_) {
		_callMarketMaintenanceActionKey = callMarketMaintenanceActionKey_;
	}

	@Override
	public String getCode() {
		return getMarketCode();
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
	
	public void updateLastUpdateTimestampAndVersion() {
		_lastUpdateTimestamp = new Timestamp(System.currentTimeMillis());
	}

	@Override
	public Date getLastUpdateTimestamp() {
		return _lastUpdateTimestamp;
	}
	
	public void setLastUpdateTimestamp(Date lastUpdateTimestamp_) {
		_lastUpdateTimestamp = lastUpdateTimestamp_ != null ? new Timestamp(lastUpdateTimestamp_.getTime()) : null;
	}
	
	public void setClearingBank(String clearingBank) {
		_clearingBank = clearingBank;
	}

	public String getClearingBank() {
		return _clearingBank;
	}	

	public Long getVersionNumber() {
		return _versionNumber;
	}

	public void setVersionNumber(Long versionNumber_) {
		_versionNumber = versionNumber_;
	}

	public void setBusinessEntityCode(String businessEntityCode) {
		_businessEntityCode = businessEntityCode;
	}

	public String getBusinessEntityCode() {
		return _businessEntityCode;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((_activationAudit == null) ? 0 : _activationAudit.hashCode());
		result = prime
				* result
				+ ((_activationStatus == null) ? 0 : _activationStatus
						.hashCode());
		result = prime
				* result
				+ ((_allowHiddenOrders == null) ? 0 : _allowHiddenOrders
						.hashCode());
		result = prime
				* result
				+ ((_allowSizeRestrictionOnOrders == null) ? 0
						: _allowSizeRestrictionOnOrders.hashCode());
		result = prime * result
				+ ((_approvalAudit == null) ? 0 : _approvalAudit.hashCode());
		result = prime
				* result
				+ ((_businessCalendar == null) ? 0 : _businessCalendar
						.hashCode());
		result = prime
				* result
				+ ((_businessEntityCode == null) ? 0 : _businessEntityCode
						.hashCode());
		result = prime * result
				+ ((_buyBook == null) ? 0 : _buyBook.hashCode());
		result = prime
				* result
				+ ((_callMarketMaintenanceActionKey == null) ? 0
						: _callMarketMaintenanceActionKey.hashCode());
		result = prime * result
				+ ((_changeAudit == null) ? 0 : _changeAudit.hashCode());
		result = prime * result
				+ ((_circuitBreaker == null) ? 0 : _circuitBreaker.hashCode());
		result = prime * result
				+ ((_clearingBank == null) ? 0 : _clearingBank.hashCode());
		result = prime * result
				+ ((_closingQuote == null) ? 0 : _closingQuote.hashCode());
		result = prime * result
				+ ((_commission == null) ? 0 : _commission.hashCode());
		result = prime
				* result
				+ ((_commissionCurrency == null) ? 0 : _commissionCurrency
						.hashCode());
		result = prime * result
				+ ((_creationAudit == null) ? 0 : _creationAudit.hashCode());
		result = prime * result
				+ ((_currentBestAsk == null) ? 0 : _currentBestAsk.hashCode());
		result = prime * result
				+ ((_currentBestBid == null) ? 0 : _currentBestBid.hashCode());
		result = prime
				* result
				+ ((_deactivationAudit == null) ? 0 : _deactivationAudit
						.hashCode());
		result = prime * result
				+ ((_description == null) ? 0 : _description.hashCode());
		result = prime
				* result
				+ ((_executionSystem == null) ? 0 : _executionSystem.hashCode());
		result = prime * result
				+ ((_haltLevel == null) ? 0 : _haltLevel.hashCode());
		result = prime * result
				+ ((_instrumentCode == null) ? 0 : _instrumentCode.hashCode());
		result = prime * result
				+ ((_lastTrade == null) ? 0 : _lastTrade.hashCode());
		result = prime
				* result
				+ ((_lastUpdateTimestamp == null) ? 0 : _lastUpdateTimestamp
						.hashCode());
		result = prime
				* result
				+ ((_marketCallActionKey == null) ? 0 : _marketCallActionKey
						.hashCode());
		result = prime
				* result
				+ ((_marketCloseActionKey == null) ? 0 : _marketCloseActionKey
						.hashCode());
		result = prime * result
				+ ((_marketCode == null) ? 0 : _marketCode.hashCode());
		result = prime
				* result
				+ ((_marketMaintenanceActionKey == null) ? 0
						: _marketMaintenanceActionKey.hashCode());
		result = prime
				* result
				+ ((_marketOpenActionKey == null) ? 0 : _marketOpenActionKey
						.hashCode());
		result = prime
				* result
				+ ((_marketOperationContract == null) ? 0
						: _marketOperationContract.hashCode());
		result = prime
				* result
				+ ((_marketOperationDays == null) ? 0 : _marketOperationDays
						.hashCode());
		result = prime
				* result
				+ ((_marketOperatorCode == null) ? 0 : _marketOperatorCode
						.hashCode());
		result = prime
				* result
				+ ((_marketReOpenActionKey == null) ? 0
						: _marketReOpenActionKey.hashCode());
		result = prime
				* result
				+ ((_marketTimeZoneID == null) ? 0 : _marketTimeZoneID
						.hashCode());
		result = prime
				* result
				+ ((_maximumContractsTraded == null) ? 0
						: _maximumContractsTraded.hashCode());
		result = prime
				* result
				+ ((_minimumContractsTraded == null) ? 0
						: _minimumContractsTraded.hashCode());
		result = prime
				* result
				+ ((_minimumQuoteIncrement == null) ? 0
						: _minimumQuoteIncrement.hashCode());
		result = prime * result + ((_name == null) ? 0 : _name.hashCode());
		result = prime
				* result
				+ ((_nextMarketCallDate == null) ? 0 : _nextMarketCallDate
						.hashCode());
		result = prime * result
				+ ((_openingQuote == null) ? 0 : _openingQuote.hashCode());
		result = prime
				* result
				+ ((_previousBestAsk == null) ? 0 : _previousBestAsk.hashCode());
		result = prime
				* result
				+ ((_previousBestBid == null) ? 0 : _previousBestBid.hashCode());
		result = prime
				* result
				+ ((_previousClosingQuote == null) ? 0 : _previousClosingQuote
						.hashCode());
		result = prime
				* result
				+ ((_previousLastTrade == null) ? 0 : _previousLastTrade
						.hashCode());
		result = prime
				* result
				+ ((_previousOpeningQuote == null) ? 0 : _previousOpeningQuote
						.hashCode());
		result = prime * result
				+ ((_quoteType == null) ? 0 : _quoteType.hashCode());
		result = prime * result
				+ ((_rolloverAudit == null) ? 0 : _rolloverAudit.hashCode());
		result = prime
				* result
				+ ((_secondaryOrderPrecedenceRules == null) ? 0
						: _secondaryOrderPrecedenceRules.hashCode());
		result = prime * result
				+ ((_sellBook == null) ? 0 : _sellBook.hashCode());
		result = prime * result + ((_state == null) ? 0 : _state.hashCode());
		result = prime
				* result
				+ ((_suspensionAudit == null) ? 0 : _suspensionAudit.hashCode());
		result = prime * result
				+ ((_tradingDayEnd == null) ? 0 : _tradingDayEnd.hashCode());
		result = prime * result
				+ ((_tradingHours == null) ? 0 : _tradingHours.hashCode());
		result = prime * result
				+ ((_tradingSession == null) ? 0 : _tradingSession.hashCode());
		result = prime * result
				+ ((_versionNumber == null) ? 0 : _versionNumber.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Market other = (Market) obj;
		if (_activationAudit == null) {
			if (other._activationAudit != null)
				return false;
		} else if (!_activationAudit.equals(other._activationAudit))
			return false;
		if (_activationStatus != other._activationStatus)
			return false;
		if (_allowHiddenOrders == null) {
			if (other._allowHiddenOrders != null)
				return false;
		} else if (!_allowHiddenOrders.equals(other._allowHiddenOrders))
			return false;
		if (_allowSizeRestrictionOnOrders == null) {
			if (other._allowSizeRestrictionOnOrders != null)
				return false;
		} else if (!_allowSizeRestrictionOnOrders
				.equals(other._allowSizeRestrictionOnOrders))
			return false;
		if (_approvalAudit == null) {
			if (other._approvalAudit != null)
				return false;
		} else if (!_approvalAudit.equals(other._approvalAudit))
			return false;
		if (_businessCalendar == null) {
			if (other._businessCalendar != null)
				return false;
		} else if (!_businessCalendar.equals(other._businessCalendar))
			return false;
		if (_businessEntityCode == null) {
			if (other._businessEntityCode != null)
				return false;
		} else if (!_businessEntityCode.equals(other._businessEntityCode))
			return false;
		if (_buyBook == null) {
			if (other._buyBook != null)
				return false;
		} else if (!_buyBook.equals(other._buyBook))
			return false;
		if (_callMarketMaintenanceActionKey == null) {
			if (other._callMarketMaintenanceActionKey != null)
				return false;
		} else if (!_callMarketMaintenanceActionKey
				.equals(other._callMarketMaintenanceActionKey))
			return false;
		if (_changeAudit == null) {
			if (other._changeAudit != null)
				return false;
		} else if (!_changeAudit.equals(other._changeAudit))
			return false;
		if (_circuitBreaker == null) {
			if (other._circuitBreaker != null)
				return false;
		} else if (!_circuitBreaker.equals(other._circuitBreaker))
			return false;
		if (_clearingBank == null) {
			if (other._clearingBank != null)
				return false;
		} else if (!_clearingBank.equals(other._clearingBank))
			return false;
		if (_closingQuote == null) {
			if (other._closingQuote != null)
				return false;
		} else if (!_closingQuote.equals(other._closingQuote))
			return false;
		if (_commission == null) {
			if (other._commission != null)
				return false;
		} else if (!_commission.equals(other._commission))
			return false;
		if (_commissionCurrency == null) {
			if (other._commissionCurrency != null)
				return false;
		} else if (!_commissionCurrency.equals(other._commissionCurrency))
			return false;
		if (_creationAudit == null) {
			if (other._creationAudit != null)
				return false;
		} else if (!_creationAudit.equals(other._creationAudit))
			return false;
		if (_currentBestAsk == null) {
			if (other._currentBestAsk != null)
				return false;
		} else if (!_currentBestAsk.equals(other._currentBestAsk))
			return false;
		if (_currentBestBid == null) {
			if (other._currentBestBid != null)
				return false;
		} else if (!_currentBestBid.equals(other._currentBestBid))
			return false;
		if (_deactivationAudit == null) {
			if (other._deactivationAudit != null)
				return false;
		} else if (!_deactivationAudit.equals(other._deactivationAudit))
			return false;
		if (_description == null) {
			if (other._description != null)
				return false;
		} else if (!_description.equals(other._description))
			return false;
		if (_executionSystem != other._executionSystem)
			return false;
		if (_haltLevel == null) {
			if (other._haltLevel != null)
				return false;
		} else if (!_haltLevel.equals(other._haltLevel))
			return false;
		if (_instrumentCode == null) {
			if (other._instrumentCode != null)
				return false;
		} else if (!_instrumentCode.equals(other._instrumentCode))
			return false;
		if (_lastTrade == null) {
			if (other._lastTrade != null)
				return false;
		} else if (!_lastTrade.equals(other._lastTrade))
			return false;
		if (_lastUpdateTimestamp == null) {
			if (other._lastUpdateTimestamp != null)
				return false;
		} else if (!_lastUpdateTimestamp.equals(other._lastUpdateTimestamp))
			return false;
		if (_marketCallActionKey == null) {
			if (other._marketCallActionKey != null)
				return false;
		} else if (!_marketCallActionKey.equals(other._marketCallActionKey))
			return false;
		if (_marketCloseActionKey == null) {
			if (other._marketCloseActionKey != null)
				return false;
		} else if (!_marketCloseActionKey.equals(other._marketCloseActionKey))
			return false;
		if (_marketCode == null) {
			if (other._marketCode != null)
				return false;
		} else if (!_marketCode.equals(other._marketCode))
			return false;
		if (_marketMaintenanceActionKey == null) {
			if (other._marketMaintenanceActionKey != null)
				return false;
		} else if (!_marketMaintenanceActionKey
				.equals(other._marketMaintenanceActionKey))
			return false;
		if (_marketOpenActionKey == null) {
			if (other._marketOpenActionKey != null)
				return false;
		} else if (!_marketOpenActionKey.equals(other._marketOpenActionKey))
			return false;
		if (_marketOperationContract == null) {
			if (other._marketOperationContract != null)
				return false;
		} else if (!_marketOperationContract
				.equals(other._marketOperationContract))
			return false;
		if (_marketOperationDays != other._marketOperationDays)
			return false;
		if (_marketOperatorCode == null) {
			if (other._marketOperatorCode != null)
				return false;
		} else if (!_marketOperatorCode.equals(other._marketOperatorCode))
			return false;
		if (_marketReOpenActionKey == null) {
			if (other._marketReOpenActionKey != null)
				return false;
		} else if (!_marketReOpenActionKey.equals(other._marketReOpenActionKey))
			return false;
		if (_marketTimeZoneID == null) {
			if (other._marketTimeZoneID != null)
				return false;
		} else if (!_marketTimeZoneID.equals(other._marketTimeZoneID))
			return false;
		if (_maximumContractsTraded == null) {
			if (other._maximumContractsTraded != null)
				return false;
		} else if (!_maximumContractsTraded
				.equals(other._maximumContractsTraded))
			return false;
		if (_minimumContractsTraded == null) {
			if (other._minimumContractsTraded != null)
				return false;
		} else if (!_minimumContractsTraded
				.equals(other._minimumContractsTraded))
			return false;
		if (_minimumQuoteIncrement == null) {
			if (other._minimumQuoteIncrement != null)
				return false;
		} else if (!_minimumQuoteIncrement.equals(other._minimumQuoteIncrement))
			return false;
		if (_name == null) {
			if (other._name != null)
				return false;
		} else if (!_name.equals(other._name))
			return false;
		if (_nextMarketCallDate == null) {
			if (other._nextMarketCallDate != null)
				return false;
		} else if (!_nextMarketCallDate.equals(other._nextMarketCallDate))
			return false;
		if (_openingQuote == null) {
			if (other._openingQuote != null)
				return false;
		} else if (!_openingQuote.equals(other._openingQuote))
			return false;
		if (_previousBestAsk == null) {
			if (other._previousBestAsk != null)
				return false;
		} else if (!_previousBestAsk.equals(other._previousBestAsk))
			return false;
		if (_previousBestBid == null) {
			if (other._previousBestBid != null)
				return false;
		} else if (!_previousBestBid.equals(other._previousBestBid))
			return false;
		if (_previousClosingQuote == null) {
			if (other._previousClosingQuote != null)
				return false;
		} else if (!_previousClosingQuote.equals(other._previousClosingQuote))
			return false;
		if (_previousLastTrade == null) {
			if (other._previousLastTrade != null)
				return false;
		} else if (!_previousLastTrade.equals(other._previousLastTrade))
			return false;
		if (_previousOpeningQuote == null) {
			if (other._previousOpeningQuote != null)
				return false;
		} else if (!_previousOpeningQuote.equals(other._previousOpeningQuote))
			return false;
		if (_quoteType != other._quoteType)
			return false;
		if (_rolloverAudit == null) {
			if (other._rolloverAudit != null)
				return false;
		} else if (!_rolloverAudit.equals(other._rolloverAudit))
			return false;
		if (_secondaryOrderPrecedenceRules == null) {
			if (other._secondaryOrderPrecedenceRules != null)
				return false;
		} else if (!_secondaryOrderPrecedenceRules
				.equals(other._secondaryOrderPrecedenceRules))
			return false;
		if (_sellBook == null) {
			if (other._sellBook != null)
				return false;
		} else if (!_sellBook.equals(other._sellBook))
			return false;
		if (_state != other._state)
			return false;
		if (_suspensionAudit == null) {
			if (other._suspensionAudit != null)
				return false;
		} else if (!_suspensionAudit.equals(other._suspensionAudit))
			return false;
		if (_tradingDayEnd == null) {
			if (other._tradingDayEnd != null)
				return false;
		} else if (!_tradingDayEnd.equals(other._tradingDayEnd))
			return false;
		if (_tradingHours == null) {
			if (other._tradingHours != null)
				return false;
		} else if (!_tradingHours.equals(other._tradingHours))
			return false;
		if (_tradingSession != other._tradingSession)
			return false;
		if (_versionNumber == null) {
			if (other._versionNumber != null)
				return false;
		} else if (!_versionNumber.equals(other._versionNumber))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Market [_marketCode=" + _marketCode + ", _currentBestBid="
				+ _currentBestBid + ", _currentBestAsk=" + _currentBestAsk
				+ ", _instrumentCode=" + _instrumentCode + ", _name=" + _name
				+ ", _description=" + _description + ", _marketOperatorCode="
				+ _marketOperatorCode + ", _marketOperationContract="
				+ _marketOperationContract + ", _quoteType=" + _quoteType
				+ ", _businessEntityCode=" + _businessEntityCode
				+ ", _minimumContractsTraded=" + _minimumContractsTraded
				+ ", _maximumContractsTraded=" + _maximumContractsTraded
				+ ", _minimumQuoteIncrement=" + _minimumQuoteIncrement
				+ ", _marketTimeZoneID=" + _marketTimeZoneID
				+ ", _tradingSession=" + _tradingSession + ", _tradingHours="
				+ _tradingHours + ", _tradingDayEnd=" + _tradingDayEnd
				+ ", _businessCalendar=" + _businessCalendar
				+ ", _marketOperationDays=" + _marketOperationDays
				+ ", _executionSystem=" + _executionSystem
				+ ", _secondaryOrderPrecedenceRules="
				+ _secondaryOrderPrecedenceRules + ", _allowHiddenOrders="
				+ _allowHiddenOrders + ", _allowSizeRestrictionOnOrders="
				+ _allowSizeRestrictionOnOrders + ", _circuitBreaker="
				+ _circuitBreaker + ", _clearingBank=" + _clearingBank
				+ ", _commission=" + _commission + ", _commissionCurrency="
				+ _commissionCurrency + ", _creationAudit=" + _creationAudit
				+ ", _approvalAudit=" + _approvalAudit + ", _suspensionAudit="
				+ _suspensionAudit + ", _changeAudit=" + _changeAudit
				+ ", _rolloverAudit=" + _rolloverAudit + ", _activationAudit="
				+ _activationAudit + ", _deactivationAudit="
				+ _deactivationAudit + ", _nextMarketCallDate="
				+ _nextMarketCallDate + ", _state=" + _state
				+ ", _activationStatus=" + _activationStatus + ", _buyBook="
				+ _buyBook + ", _sellBook=" + _sellBook + ", _lastTrade="
				+ _lastTrade + ", _previousLastTrade=" + _previousLastTrade
				+ ", _previousBestBid=" + _previousBestBid
				+ ", _previousBestAsk=" + _previousBestAsk + ", _openingQuote="
				+ _openingQuote + ", _closingQuote=" + _closingQuote
				+ ", _previousOpeningQuote=" + _previousOpeningQuote
				+ ", _previousClosingQuote=" + _previousClosingQuote
				+ ", _haltLevel=" + _haltLevel + ", _marketCallActionKey="
				+ _marketCallActionKey + ", _marketOpenActionKey="
				+ _marketOpenActionKey + ", _marketCloseActionKey="
				+ _marketCloseActionKey + ", _marketReOpenActionKey="
				+ _marketReOpenActionKey + ", _marketMaintenanceActionKey="
				+ _marketMaintenanceActionKey
				+ ", _callMarketMaintenanceActionKey="
				+ _callMarketMaintenanceActionKey + ", _lastUpdateTimestamp="
				+ _lastUpdateTimestamp + ", _versionNumber=" + _versionNumber
				+ "]";
	}
}
