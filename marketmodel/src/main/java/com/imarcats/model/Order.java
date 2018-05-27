package com.imarcats.model;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;

import com.imarcats.model.meta.DataLengths;
import com.imarcats.model.types.AuditInformation;
import com.imarcats.model.types.ExpirationProperties;
import com.imarcats.model.types.Property;
import com.imarcats.model.types.LimitQuote;
import com.imarcats.model.types.OrderExpirationInstruction;
import com.imarcats.model.types.OrderProperties;
import com.imarcats.model.types.OrderSide;
import com.imarcats.model.types.OrderState;
import com.imarcats.model.types.OrderTriggerInstruction;
import com.imarcats.model.types.OrderType;
import com.imarcats.model.types.PropertyHolder;
import com.imarcats.model.types.Quote;
import com.imarcats.model.types.StopQuote;
import com.imarcats.model.types.TriggerProperties;


/**
 * Order is sent to the Market to define that a Trader wants to buy or sell certain product
 * 
 * @author Adam
 */
@Entity
@Table(name="ORDER_ENTITY", uniqueConstraints={@UniqueConstraint(columnNames = {"EXTERNAL_ORDER_REFERENCE", "SUBMITTER_ID", "TARGET_MARKET_CODE"})})
public class Order implements MarketModelObject, VersionedMarketObject {

	private static final long serialVersionUID = 1L;

	/**
	 * Primary Key of the Order
	 * Note: No need for accessing this field directly
	 */	
	@Id
	@GeneratedValue 
	@Column(name="ID")
    private Long _id;

	/**
	 * External order reference (it will be copied to the trade)
	 * Required
	 */
	@Column(name="EXTERNAL_ORDER_REFERENCE", nullable=false, length=DataLengths.UUID_LENGTH)
	private String _externalOrderReference; 
	
	/**
	 * Target Account of the User, if not defined the User's Default Account is used
	 * Optional
	 */
	@Column(name="TARGET_ACCOUNT_ID")
	private Long _targetAccountID;
	
	/**
	 * User ID who has submitted the Order 
	 * Optional
	 */
	@Column(name="SUBMITTER_ID", length=DataLengths.USER_ID_LENGTH)
	private String _submitterID;
	
	/**
	 * Market ID where the Order was Submitted
	 * Optional
	 */
	@Column(name="TARGET_MARKET_CODE", length=DataLengths.MARKET_OBJECT_CODE_LENGTH)
	private String _targetMarketCode;
	
	/**
	 * Sell or Buy Side Order (Value from OrderSide)
	 * Required
	 */
	@Column(name="SIDE", nullable=false)
	@Enumerated(EnumType.STRING) 
	private OrderSide _side;
	
	/**
	 * Market or Limit Order (Value from OrderType)
	 * Required
	 */
	@Column(name="TYPE", nullable=false)
	@Enumerated(EnumType.STRING) 
	private OrderType _type = OrderType.Limit;
	
	/**
	 * Value of Limit Price or Rate - Only for Limit Orders
	 * Optional
	 */
	@OneToOne(cascade=CascadeType.ALL, orphanRemoval=true)
	@JoinColumn(name="LIMIT_QUOTE_VALUE_ID")
	private LimitQuote _limitQuoteValue;
	
	/**
	 * Size of the Order, How many Contracts will be traded
	 * Required
	 */
	@Column(name="SIZE", nullable=false)
	private Integer _size;
	
	/**
	 * Defines the Minimum Size that has to be Executed from the Order or 
	 * 0, there is no minimum size  
	 * Optional
	 */
	@Column(name="MINIMUM_SIZE_OF_EXECUTION")
	private Integer _minimumSizeOfExecution = 0;
	
	/**
	 * Defines, if the Order has to be fully Executed or left un-touched
	 * Optional
	 * 
	 * TODO: This should be merged with _minimumSizeOfExecution (_minimumSizeOfExecution == calculateRemainingSize() -> _executeEntireOrderAtOnce = true)
	 */
	@Column(name="EXECUTE_ENTRIRE_ORDER_AT_ONCE")
	private Boolean _executeEntireOrderAtOnce;

	/**
	 * Defines, if the Order Information will be displayed to the Public 
	 * Optional
	 */
	@Column(name="DISPLAY_ORDER")
	private Boolean _displayOrder = true;
	
	/**
	 * Executed Size of the Order, How many Contracts have been
	 * executed on the Market. Remaining = Original - Executed
	 * Optional
	 */
	@Column(name="EXECUTED_SIZE")
	private Integer _executedSize;
	
	/**
	 * Defines whether the Order is Submitted, Canceled (Value from OrderState)
	 * Required
	 * 
	 * Note: State is stored as the String Representation of the actual State to 
	 * 		 make queries possible on this field. 
	 */
	@Column(name="STATE", nullable=false)
	@Enumerated(EnumType.STRING) 
	private OrderState _state = OrderState.Created;
	
	/**
	 * Defines how the Order will be submitted (Value from OrderTriggerInstruction)
	 * Required
	 */
	@Column(name="TRIGGER_INSTRUCTION", nullable=false)
	private OrderTriggerInstruction _triggerInstruction = OrderTriggerInstruction.Immediate;
	
	/**
	 * Properties for Trigger Instruction
	 * Optional
	 */
	@OneToOne(cascade=CascadeType.ALL, orphanRemoval=true)
	@JoinColumn(name="TRIGGER_PROPERTIES_ID")
	private TriggerProperties _triggerProperties = new TriggerProperties(new PropertyHolder());

	/**
	 * Key for Trigger
	 * Optional
	 */
	@Column(name="QUOTE_CHANGE_TRIGGER_KEY")
	private Long _quoteChangeTriggerKey;
	
	/**
	 * Defines when the Order will expire and should be canceled (Value from OrderExpirationInstruction)
	 * Required
	 */
	@Column(name="EXPIRATION_INSTRUCTION", nullable=false)
	@Enumerated(EnumType.STRING) 
	private OrderExpirationInstruction _expirationInstruction = OrderExpirationInstruction.GoodTillCancel;
	
	/**
	 * Properties for Expiration Instruction
	 * Optional
	 */
	@OneToOne(cascade=CascadeType.ALL, orphanRemoval=true)
	@JoinColumn(name="EXPIRATION_PROPERTIES_ID")
	private ExpirationProperties _expirationProperties = new ExpirationProperties(new PropertyHolder());

	/**
	 * Order Properties
	 * Optional
	 */
	@OneToOne(cascade=CascadeType.ALL, orphanRemoval=true)
	@JoinColumn(name="ORDER_PROPERTIES_ID")
	private OrderProperties _orderProperties = new OrderProperties(new PropertyHolder());
	
	/**
	 * Key for Expiration Trigger
	 * Optional
	 */
	@Column(name="EXPIRATION_TRIGGER_ACTION_KEY")
	private Long _expirationTriggerActionKey;

	/**
	 * Current Stop Quote of the Stop Loss Order (Static for Non-Trailing Stop Loss Order)
	 */
	@OneToOne(cascade=CascadeType.ALL, orphanRemoval=true)
	@JoinColumn(name="CURRENT_STOP_QUOTE_ID")
	private StopQuote _currentStopQuote;
	
	/**
	 * Date and Time when the Order was submitted
	 */
	@Column(name="SUBMISSION_DATE")
	private Timestamp _submissionDate;
	
    /**
     * Tells, if Commission was charged for this Order already, 
     * this is relevant when the Order is executed in portions, 
     * so we can make sure commission is charged only once 
     *  
     * Optional
     */
	@Column(name="COMMISSION_CHARGED")
	private Boolean _commissionCharged = Boolean.FALSE;
	
    /**
     * Gives information to the user, why his/her Order was canceled. 
     * It is a language key used to retrieve actual text from I18N System 
     *  
     * Optional
     */
	@Column(name="CANCELLATION_COMMENT_LANGUAGE_KEY")
	private String _cancellationCommentLanguageKey;
	
	/**
	 * Audit Information, like who created the Order and when
	 * Optional
	 */
	@OneToOne(cascade=CascadeType.ALL, orphanRemoval=true) 
	@JoinColumn(name="CREATION_AUDIT_ID")
	private CreationAudit _creationAudit;
	
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

	public Quote getLimitQuoteValue() {
		return _limitQuoteValue != null 
					? _limitQuoteValue.getQuote()
				    : null;
	}

	public void setLimitQuoteValue(Quote limitQuote_) {
		_limitQuoteValue = new LimitQuote(Quote.createQuote(limitQuote_)); // Cloning Quote is needed, because it might be copied from an other persisted Object
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

	public Property[] getTriggerProperties() {
		return _triggerProperties.getPropertyHolder().getProperties();
	}

	public void setTriggerProperties(Property[] properties_) {
		clearTriggerProperties();
		if(properties_ != null) {					
			for (Property property : properties_) {
				addTriggerProperty(property);
			}
		}
	}
	
	public void addTriggerProperty(Property triggerProperty_) {
		_triggerProperties.getPropertyHolder().addProperty(triggerProperty_);
	}
	
	public void deleteTriggerProperty(Property triggerProperty_) {
		_triggerProperties.getPropertyHolder().deleteProperty(triggerProperty_);
	}

	public void clearTriggerProperties() {
		_triggerProperties.getPropertyHolder().clearProperties();
	}
	
	public OrderExpirationInstruction getExpirationInstruction() {
		return _expirationInstruction;
	}

	public void setExpirationInstruction(OrderExpirationInstruction expirationInstruction_) {
		_expirationInstruction = expirationInstruction_;
	}

	public Property[] getExpirationProperties() {
		return _expirationProperties.getPropertyHolder().getProperties();
	}

	public void setExpirationProperties(Property[] properties_) {
		clearExpirationProperties();
		if(properties_ != null) {			
			for (Property property : properties_) {
				addExpirationProperty(property);
			}
		}
	}
	
	public void addExpirationProperty(Property expirationProperty_) {
		_expirationProperties.getPropertyHolder().addProperty(expirationProperty_);
	}
	
	public void deleteExpirationProperty(Property expirationProperty_) {
		_expirationProperties.getPropertyHolder().deleteProperty(expirationProperty_);
	}
	
	public void clearExpirationProperties() {
		_expirationProperties.getPropertyHolder().clearProperties();
	}
	
	public void setSubmissionDate(Date submissionDate) {
		_submissionDate = submissionDate != null ? new Timestamp(submissionDate.getTime()) : null;
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
	
	private void setAudit(AuditInformation source_, AuditInformation target_) {
		if(source_ != null && target_ != null) {
			target_.setDateTime(source_.getDateTime());
			target_.setUserID(source_.getUserID());		
		} 
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

	public void setCurrentStopQuote(Quote currentStopQuote_) {
		// this is needed, because Google App Engine's Datastore would not let 
		// change the uncommited classes, since this field may change many times 
		// before commit, we can only copy the values on the object
		// TODO: Is this the same as in JPA?
		if(currentStopQuote_ != null) {
			if(_currentStopQuote == null) {
				_currentStopQuote = 
						new StopQuote(Quote.createQuote(currentStopQuote_)); // Cloning Quote is needed, because it might be copied from an other persisted Object
			
			} else {
				_currentStopQuote.setQuote(Quote.createQuote(currentStopQuote_));
			}
		} else {
			_currentStopQuote = null;
		}
	}
	
	public Quote getCurrentStopQuote() {
		return _currentStopQuote != null 
					? _currentStopQuote.getQuote() 
					: null;
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

	public void setQuoteChangeTriggerKey(Long quoteChangeTriggerKey) {
		_quoteChangeTriggerKey = quoteChangeTriggerKey;
	}

	public Long getQuoteChangeTriggerKey() {
		return _quoteChangeTriggerKey;
	}

	public void setExpirationTriggerActionKey(Long expirationTriggerActionKey) {
		_expirationTriggerActionKey = expirationTriggerActionKey;
	}

	public Long getExpirationTriggerActionKey() {
		return _expirationTriggerActionKey;
	}

	public Boolean getCommissionCharged() {
		return _commissionCharged;
	}

	public void setCommissionCharged(Boolean commissionCharged_) {
		_commissionCharged = commissionCharged_;
	}

	public String getCancellationCommentLanguageKey() {
		return _cancellationCommentLanguageKey;
	}

	public void setCancellationCommentLanguageKey(String cancellationCommentLanguageKey_) {
		_cancellationCommentLanguageKey = cancellationCommentLanguageKey_;
	}

	public void updateLastUpdateTimestamp() {
		_lastUpdateTimestamp = new Timestamp(System.currentTimeMillis());
	}
	
	public Date getLastUpdateTimestamp() {
		return _lastUpdateTimestamp;
	}
	
	public void setLastUpdateTimestamp(Date lastUpdateTimestamp_) {
		_lastUpdateTimestamp = lastUpdateTimestamp_ != null ? new Timestamp(lastUpdateTimestamp_.getTime()) : null;
	}
	
	public TriggerProperties getTriggerPropertiesObject() {
		return _triggerProperties;
	}
	
	public void setTriggerPropertiesObject(TriggerProperties triggerProperties_) {
		_triggerProperties = triggerProperties_;
	}

	public ExpirationProperties getExpirationPropertiesObject() {
		return _expirationProperties;
	}
	
	public void setExpirationPropertiesObject(ExpirationProperties expirationProperties_) {
		_expirationProperties = expirationProperties_;
	}

	@Override
	public Long getVersionNumber() {
		return _versionNumber;
	}

	@Override
	public void setVersionNumber(Long version_) {
		_versionNumber = version_;
	}
	
	public String getExternalOrderReference() {
		return _externalOrderReference;
	}

	public void setExternalOrderReference(String externalOrderReference_) {
		_externalOrderReference = externalOrderReference_;
	}

	public Property[] getOrderProperties() {
		if(checkOrderProperties()) {			
			return _orderProperties.getPropertyHolder().getProperties();
		}
		
		return null;
	}

	public void setOrderProperties(Property[] properties_) {
		clearOrderProperties();
		if(properties_ != null) {					
			for (Property property : properties_) {
				addOrderProperty(property);
			}
		}
	}
	
	public void addOrderProperty(Property orderProperty_) {
		initOrderProperties();
		_orderProperties.getPropertyHolder().addProperty(orderProperty_);
	}

	private void initOrderProperties() {
		if(_orderProperties == null) {
			_orderProperties = new OrderProperties(new PropertyHolder());
		}
	}
	
	private boolean checkOrderProperties() {
		return _orderProperties != null && _orderProperties.getPropertyHolder() != null;
	}
	
	public void deleteOrderProperty(Property orderProperty_) {
		if(checkOrderProperties()) {				
			_orderProperties.getPropertyHolder().deleteProperty(orderProperty_);
		}
	}

	public void clearOrderProperties() {
		if(checkOrderProperties()) {				
			_orderProperties.getPropertyHolder().clearProperties();
		}
	}

	@Override
	public String toString() {
		return "Order [_id=" + _id + ", _externalOrderReference="
				+ _externalOrderReference + ", _targetAccountID="
				+ _targetAccountID + ", _submitterID=" + _submitterID
				+ ", _targetMarketCode=" + _targetMarketCode + ", _side="
				+ _side + ", _type=" + _type + ", _limitQuoteValue="
				+ _limitQuoteValue + ", _size=" + _size
				+ ", _minimumSizeOfExecution=" + _minimumSizeOfExecution
				+ ", _executeEntireOrderAtOnce=" + _executeEntireOrderAtOnce
				+ ", _displayOrder=" + _displayOrder + ", _executedSize="
				+ _executedSize + ", _state=" + _state
				+ ", _triggerInstruction=" + _triggerInstruction
				+ ", _triggerProperties=" + _triggerProperties
				+ ", _quoteChangeTriggerKey=" + _quoteChangeTriggerKey
				+ ", _expirationInstruction=" + _expirationInstruction
				+ ", _expirationProperties=" + _expirationProperties
				+ ", _orderProperties=" + _orderProperties
				+ ", _expirationTriggerActionKey="
				+ _expirationTriggerActionKey + ", _currentStopQuote="
				+ _currentStopQuote + ", _submissionDate=" + _submissionDate
				+ ", _commissionCharged=" + _commissionCharged
				+ ", _cancellationCommentLanguageKey="
				+ _cancellationCommentLanguageKey + ", _creationAudit="
				+ _creationAudit + ", _lastUpdateTimestamp="
				+ _lastUpdateTimestamp + ", _versionNumber=" + _versionNumber
				+ "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((_cancellationCommentLanguageKey == null) ? 0
						: _cancellationCommentLanguageKey.hashCode());
		result = prime
				* result
				+ ((_commissionCharged == null) ? 0 : _commissionCharged
						.hashCode());
		result = prime * result
				+ ((_creationAudit == null) ? 0 : _creationAudit.hashCode());
		result = prime
				* result
				+ ((_currentStopQuote == null) ? 0 : _currentStopQuote
						.hashCode());
		result = prime * result
				+ ((_displayOrder == null) ? 0 : _displayOrder.hashCode());
		result = prime
				* result
				+ ((_executeEntireOrderAtOnce == null) ? 0
						: _executeEntireOrderAtOnce.hashCode());
		result = prime * result
				+ ((_executedSize == null) ? 0 : _executedSize.hashCode());
		result = prime
				* result
				+ ((_expirationInstruction == null) ? 0
						: _expirationInstruction.hashCode());
		result = prime
				* result
				+ ((_expirationProperties == null) ? 0 : _expirationProperties
						.hashCode());
		result = prime
				* result
				+ ((_expirationTriggerActionKey == null) ? 0
						: _expirationTriggerActionKey.hashCode());
		result = prime
				* result
				+ ((_externalOrderReference == null) ? 0
						: _externalOrderReference.hashCode());
		result = prime * result + ((_id == null) ? 0 : _id.hashCode());
		result = prime
				* result
				+ ((_lastUpdateTimestamp == null) ? 0 : _lastUpdateTimestamp
						.hashCode());
		result = prime
				* result
				+ ((_limitQuoteValue == null) ? 0 : _limitQuoteValue.hashCode());
		result = prime
				* result
				+ ((_minimumSizeOfExecution == null) ? 0
						: _minimumSizeOfExecution.hashCode());
		result = prime
				* result
				+ ((_orderProperties == null) ? 0 : _orderProperties.hashCode());
		result = prime
				* result
				+ ((_quoteChangeTriggerKey == null) ? 0
						: _quoteChangeTriggerKey.hashCode());
		result = prime * result + ((_side == null) ? 0 : _side.hashCode());
		result = prime * result + ((_size == null) ? 0 : _size.hashCode());
		result = prime * result + ((_state == null) ? 0 : _state.hashCode());
		result = prime * result
				+ ((_submissionDate == null) ? 0 : _submissionDate.hashCode());
		result = prime * result
				+ ((_submitterID == null) ? 0 : _submitterID.hashCode());
		result = prime
				* result
				+ ((_targetAccountID == null) ? 0 : _targetAccountID.hashCode());
		result = prime
				* result
				+ ((_targetMarketCode == null) ? 0 : _targetMarketCode
						.hashCode());
		result = prime
				* result
				+ ((_triggerInstruction == null) ? 0 : _triggerInstruction
						.hashCode());
		result = prime
				* result
				+ ((_triggerProperties == null) ? 0 : _triggerProperties
						.hashCode());
		result = prime * result + ((_type == null) ? 0 : _type.hashCode());
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
		Order other = (Order) obj;
		if (_cancellationCommentLanguageKey == null) {
			if (other._cancellationCommentLanguageKey != null)
				return false;
		} else if (!_cancellationCommentLanguageKey
				.equals(other._cancellationCommentLanguageKey))
			return false;
		if (_commissionCharged == null) {
			if (other._commissionCharged != null)
				return false;
		} else if (!_commissionCharged.equals(other._commissionCharged))
			return false;
		if (_creationAudit == null) {
			if (other._creationAudit != null)
				return false;
		} else if (!_creationAudit.equals(other._creationAudit))
			return false;
		if (_currentStopQuote == null) {
			if (other._currentStopQuote != null)
				return false;
		} else if (!_currentStopQuote.equals(other._currentStopQuote))
			return false;
		if (_displayOrder == null) {
			if (other._displayOrder != null)
				return false;
		} else if (!_displayOrder.equals(other._displayOrder))
			return false;
		if (_executeEntireOrderAtOnce == null) {
			if (other._executeEntireOrderAtOnce != null)
				return false;
		} else if (!_executeEntireOrderAtOnce
				.equals(other._executeEntireOrderAtOnce))
			return false;
		if (_executedSize == null) {
			if (other._executedSize != null)
				return false;
		} else if (!_executedSize.equals(other._executedSize))
			return false;
		if (_expirationInstruction != other._expirationInstruction)
			return false;
		if (_expirationProperties == null) {
			if (other._expirationProperties != null)
				return false;
		} else if (!_expirationProperties.equals(other._expirationProperties))
			return false;
		if (_expirationTriggerActionKey == null) {
			if (other._expirationTriggerActionKey != null)
				return false;
		} else if (!_expirationTriggerActionKey
				.equals(other._expirationTriggerActionKey))
			return false;
		if (_externalOrderReference == null) {
			if (other._externalOrderReference != null)
				return false;
		} else if (!_externalOrderReference
				.equals(other._externalOrderReference))
			return false;
		if (_id == null) {
			if (other._id != null)
				return false;
		} else if (!_id.equals(other._id))
			return false;
		if (_lastUpdateTimestamp == null) {
			if (other._lastUpdateTimestamp != null)
				return false;
		} else if (!_lastUpdateTimestamp.equals(other._lastUpdateTimestamp))
			return false;
		if (_limitQuoteValue == null) {
			if (other._limitQuoteValue != null)
				return false;
		} else if (!_limitQuoteValue.equals(other._limitQuoteValue))
			return false;
		if (_minimumSizeOfExecution == null) {
			if (other._minimumSizeOfExecution != null)
				return false;
		} else if (!_minimumSizeOfExecution
				.equals(other._minimumSizeOfExecution))
			return false;
		if (_orderProperties == null) {
			if (other._orderProperties != null)
				return false;
		} else if (!_orderProperties.equals(other._orderProperties))
			return false;
		if (_quoteChangeTriggerKey == null) {
			if (other._quoteChangeTriggerKey != null)
				return false;
		} else if (!_quoteChangeTriggerKey.equals(other._quoteChangeTriggerKey))
			return false;
		if (_side != other._side)
			return false;
		if (_size == null) {
			if (other._size != null)
				return false;
		} else if (!_size.equals(other._size))
			return false;
		if (_state != other._state)
			return false;
		if (_submissionDate == null) {
			if (other._submissionDate != null)
				return false;
		} else if (!_submissionDate.equals(other._submissionDate))
			return false;
		if (_submitterID == null) {
			if (other._submitterID != null)
				return false;
		} else if (!_submitterID.equals(other._submitterID))
			return false;
		if (_targetAccountID == null) {
			if (other._targetAccountID != null)
				return false;
		} else if (!_targetAccountID.equals(other._targetAccountID))
			return false;
		if (_targetMarketCode == null) {
			if (other._targetMarketCode != null)
				return false;
		} else if (!_targetMarketCode.equals(other._targetMarketCode))
			return false;
		if (_triggerInstruction != other._triggerInstruction)
			return false;
		if (_triggerProperties == null) {
			if (other._triggerProperties != null)
				return false;
		} else if (!_triggerProperties.equals(other._triggerProperties))
			return false;
		if (_type != other._type)
			return false;
		if (_versionNumber == null) {
			if (other._versionNumber != null)
				return false;
		} else if (!_versionNumber.equals(other._versionNumber))
			return false;
		return true;
	}
}
