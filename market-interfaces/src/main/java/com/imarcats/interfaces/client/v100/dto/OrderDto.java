package com.imarcats.interfaces.client.v100.dto;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;

import com.imarcats.interfaces.client.v100.dto.types.AuditInformationDto;
import com.imarcats.interfaces.client.v100.dto.types.PropertyDto;
import com.imarcats.interfaces.client.v100.dto.types.OrderExpirationInstruction;
import com.imarcats.interfaces.client.v100.dto.types.OrderSide;
import com.imarcats.interfaces.client.v100.dto.types.OrderState;
import com.imarcats.interfaces.client.v100.dto.types.OrderTriggerInstruction;
import com.imarcats.interfaces.client.v100.dto.types.OrderType;
import com.imarcats.interfaces.client.v100.dto.types.QuoteDto;
import com.imarcats.model.meta.DataLengths;


/**
 * Order is sent to the Market to define that a Trader wants to buy or sell certain product
 * 
 * @author Adam
 */
public class OrderDto implements MarketModelObjectDto {

	/**
	 * Primary Key of the Order
	 * Note: No need for accessing this field directly
	 */	
    private Long _id;

	/**
	 * External order reference (it will be copied to the trade)
	 * Required
	 */
//	@Column(name="EXTERNAL_ORDER_REFERENCE", nullable=false, length=DataLengths.UUID_LENGTH)
	private String _externalOrderReference; 
    
	/**
	 * Target Account of the User, if not defined the User's Default Account is used
	 * Optional
	 */
	private Long _targetAccountID;
	
	/**
	 * User ID who has submitted the Order 
	 * Optional
	 */
	// @Column(name="SUBMITTER_ID", length=DataLengths.USER_ID_LENGTH)
	private String _submitterID;
	
	/**
	 * Market ID where the Order was Submitted
	 * Optional
	 */
	// @Column(name="TARGET_MARKET_CODE", length=DataLengths.MARKET_OBJECT_CODE_LENGTH)
	private String _targetMarketCode;
	
	/**
	 * Sell or Buy Side Order (Value from OrderSide)
	 * Required
	 */
	private OrderSide _side;
	
	/**
	 * Market or Limit Order (Value from OrderType)
	 * Required
	 */
	private OrderType _type = OrderType.Limit;
	
	/**
	 * Value of Limit Price or Rate - Only for Limit Orders
	 * Optional
	 */
	private QuoteDto _limitQuoteValue;
	
	/**
	 * Size of the Order, How many Contracts will be traded
	 * Required
	 */
	private Integer _size;
	
	/**
	 * Defines the Minimum Size that has to be Executed from the Order or 
	 * 0, there is no minimum size  
	 * Optional
	 */
	private Integer _minimumSizeOfExecution = 0;
	
	/**
	 * Defines, if the Order has to be fully Executed or left un-touched
	 * Optional
	 * 
	 * TODO: This should be merged with _minimumSizeOfExecution (_minimumSizeOfExecution == calculateRemainingSize() -> _executeEntireOrderAtOnce = true)
	 */
	private Boolean _executeEntireOrderAtOnce;

	/**
	 * Defines, if the Order Information will be displayed to the Public 
	 * Optional
	 */
	private Boolean _displayOrder = true;
	
	/**
	 * Executed Size of the Order, How many Contracts have been
	 * executed on the Market. Remaining = Original - Executed
	 * Optional
	 */
	private Integer _executedSize;
	
	/**
	 * Defines whether the Order is Submitted, Canceled (Value from OrderState)
	 * Required
	 * 
	 * Note: State is stored as the String Representation of the actual State to 
	 * 		 make queries possible on this field. 
	 */
	private OrderState _state = OrderState.Created;
	
	/**
	 * Defines how the Order will be submitted (Value from OrderTriggerInstruction)
	 * Required
	 */
	private OrderTriggerInstruction _triggerInstruction = OrderTriggerInstruction.Immediate;
	
	/**
	 * Properties for Trigger Instruction
	 * Optional
	 */
	private PropertyDto[] _triggerProperties;
	
	/**
	 * Defines when the Order will expire and should be canceled (Value from OrderExpirationInstruction)
	 * Required
	 */
	private OrderExpirationInstruction _expirationInstruction = OrderExpirationInstruction.GoodTillCancel;
	
	/**
	 * Properties for Expiration Instruction
	 * Optional
	 */
	private PropertyDto[] _expirationProperties = new PropertyDto[0];
	
	/**
	 * Order Properties 
	 * Optional
	 */
	private PropertyDto[] _orderProperties = new PropertyDto[0];
	
	/**
	 * Current Stop Quote of the Stop Loss Order (Static for Non-Trailing Stop Loss Order)
	 */
	private QuoteDto _currentStopQuote;
	
	/**
	 * Date and Time when the Order was submitted
	 */
	private Timestamp _submissionDate;
	
    /**
     * Gives information to the user, why his/her Order was canceled. 
     * It is a language key used to retrieve actual text from I18N System 
     *  
     * Optional
     */
	private String _cancellationCommentLanguageKey;
	
	/**
	 * Audit Information, like who created the Order and when
	 * Optional
	 */
	private AuditInformationDto _creationAudit;
	
    /**
     * Date and Time, when the Object was Last Updated
     * Required
     */
    // TODO: Change to timestamp
	private Date _lastUpdateTimestamp;
	
	/**
	 * Version number of theÍO Order, updated every time any Order field is updated 
	 * If overflown is true version just restarted from 0
	 * Required
	 */
	private Long _versionNumber;
	
	public Long getKey() {
		return _id;
	}

	public void setKey(Long key_) {
		_id = key_;
	}

	public String getSubmitterID() {
		return _submitterID;
	}

	public void setSubmitterID(String submitterID_) {
		_submitterID = submitterID_;
	}

	public String getTargetMarketCode() {
		return _targetMarketCode;
	}

	public void setTargetMarketCode(String targetMarketCode_) {
		_targetMarketCode = targetMarketCode_;
	}

	public OrderSide getSide() {
		return _side;
	}

	public void setSide(OrderSide side_) {
		_side = side_;
	}

	public OrderType getType() {
		return _type;
	}

	public void setType(OrderType type_) {
		_type = type_;
	}

	public QuoteDto getLimitQuoteValue() {
		return _limitQuoteValue;
	}

	public void setLimitQuoteValue(QuoteDto limitQuote_) {
		_limitQuoteValue = limitQuote_;
	}

	public int getSize() {
		return _size != null ? _size : 0;
	}

	public void setSize(int size_) {
		_size = size_;
	}

	public int getExecutedSize() {
		return _executedSize != null ? _executedSize : 0;
	}

	public void setExecutedSize(int executedSize_) {
		_executedSize = executedSize_;
	}
	
	public int calculateRemainingSize() {
		return getSize() - getExecutedSize();
	}
	
	public OrderState getState() {
		return _state;
	}

	public void setState(OrderState state_) {
		_state = state_;
	}

	public OrderTriggerInstruction getTriggerInstruction() {
		return _triggerInstruction;
	}

	public void setTriggerInstruction(OrderTriggerInstruction triggerInstruction_) {
		_triggerInstruction = triggerInstruction_;
	}

	public PropertyDto[] getTriggerProperties() {
		return _triggerProperties;
	}

	public void setTriggerProperties(PropertyDto[] triggerProperties_) {
		_triggerProperties = triggerProperties_;
	}

	public OrderExpirationInstruction getExpirationInstruction() {
		return _expirationInstruction;
	}

	public void setExpirationInstruction(OrderExpirationInstruction expirationInstruction_) {
		_expirationInstruction = expirationInstruction_;
	}
	
	public PropertyDto[] getExpirationProperties() {
		return _expirationProperties;
	}

	public void setExpirationProperties(PropertyDto[] expirationProperties_) {
		_expirationProperties = expirationProperties_;
	}

	public void setSubmissionDate(Date submissionDate) {
		_submissionDate = submissionDate != null ? new Timestamp(submissionDate.getTime()) : null;
	}
	
	public AuditInformationDto getCreationAudit() {
		return _creationAudit;
	}

	public void setCreationAudit(AuditInformationDto creationAudit_) {
		_creationAudit = creationAudit_;
	}
	
	public Date getSubmissionDate() {
		return _submissionDate;
	}

	public void setMinimumSizeOfExecution(int minimumSizeOfExecution) {
		_minimumSizeOfExecution = minimumSizeOfExecution;
	}

	public int getMinimumSizeOfExecution() {
		return _minimumSizeOfExecution != null ? _minimumSizeOfExecution : 0;
	}

	public void setExecuteEntireOrderAtOnce(boolean executeEntireOrderAtOnce) {
		_executeEntireOrderAtOnce = executeEntireOrderAtOnce;
	}

	public boolean getExecuteEntireOrderAtOnce() {
		return _executeEntireOrderAtOnce != null ? _executeEntireOrderAtOnce : false;
	}

	public void setCurrentStopQuote(QuoteDto currentStopQuote) {
		_currentStopQuote = currentStopQuote;
	}

	public QuoteDto getCurrentStopQuote() {
		return _currentStopQuote;
	}
	
	public void setDisplayOrder(boolean displayOrder) {
		_displayOrder = displayOrder;
	}

	public boolean getDisplayOrder() {
		return _displayOrder != null ? _displayOrder : true;
	}
	
	public void setTargetAccountID(Long targetAccountID) {
		_targetAccountID = targetAccountID;
	}

	public Long getTargetAccountID() {
		return _targetAccountID;
	}

	public String getCancellationCommentLanguageKey() {
		return _cancellationCommentLanguageKey;
	}

	public void setCancellationCommentLanguageKey(String cancellationCommentLanguageKey_) {
		_cancellationCommentLanguageKey = cancellationCommentLanguageKey_;
	}
	
	public Date getLastUpdateTimestamp() {
		return _lastUpdateTimestamp;
	}

	public Long getVersionNumber() {
		return _versionNumber;
	}

	public void setVersionNumber(Long version_) {
		_versionNumber = version_;
	}	
	
	public PropertyDto[] getOrderProperties() {
		return _orderProperties;
	}

	public void setOrderProperties(PropertyDto[] orderProperties_) {
		_orderProperties = orderProperties_;
	}

	public String getExternalOrderReference() {
		return _externalOrderReference;
	}

	public void setExternalOrderReference(String externalOrderReference_) {
		_externalOrderReference = externalOrderReference_;
	}

	public void setLastUpdateTimestamp(Date _lastUpdateTimestamp) {
		this._lastUpdateTimestamp = _lastUpdateTimestamp;
	}

	/**
	 * @return String used in Debug and Exceptions 
	 */
	@Override
	public String toString() {
		return "Order=[ " + getSide() + ", " + getType() + ", " + 
			   getSize() + ", @" + getLimitQuoteValue() + " ]"; 
	}
}
