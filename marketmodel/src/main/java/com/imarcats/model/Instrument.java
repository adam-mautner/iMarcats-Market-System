package com.imarcats.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import javax.persistence.Version;

import com.imarcats.model.meta.DataLengths;
import com.imarcats.model.types.ActivationStatus;
import com.imarcats.model.types.Address;
import com.imarcats.model.types.AuditInformation;
import com.imarcats.model.types.DeliveryPeriod;
import com.imarcats.model.types.Property;
import com.imarcats.model.types.InstrumentType;
import com.imarcats.model.types.Position;
import com.imarcats.model.types.PropertyHolder;
import com.imarcats.model.types.QuoteType;
import com.imarcats.model.types.SettlementPrice;
import com.imarcats.model.types.SettlementType;
import com.imarcats.model.types.UnderlyingType;

/**
 * Instrument defined on a Product or on an other Instruments. 
 * Instruments traded on the Markets.
 * @author Adam
 */
@Entity
@Table(name="INSTRUMENT")
public class Instrument implements Underlying, Rollable {

	private static final long serialVersionUID = 1L;

	/**
	 * Ticker Symbol or Derivative Code (Primary Key)
	 * Required
	 */
	@Id
	@Column(name="INSTRUMENT_CODE", length=DataLengths.MARKET_OBJECT_CODE_LENGTH)
	private String _instrumentCode;

	/**
	 * ISIN (International Security Identification Number) like CUSIP, 
	 * identifies this Instrument in External Systems
	 * Optional
	 */
	@Column(name="ISIN", length=12)
	private String _isin;    
    
	/**
	 * Name of the Instrument
	 * Required
	 */
	@Column(name="NAME", nullable=false, length=DataLengths.MARKET_OBJECT_NAME_LENGTH)
	private String _name;
	
	/**
	 * Description of the Instrument
	 * Required
	 */
	@Column(name="DESCRIPTION", nullable=false, length=DataLengths.MARKET_OBJECT_DESCRIPTION_LENGTH)
	private String _description;
	
	/**
	 * Type of the Instrument (Value from InstrumentType)
	 * Required
	 */
	@Column(name="TYPE", nullable=false)
	@Enumerated(EnumType.STRING) 
	private InstrumentType _type;
	
	/**
	 * Name of the Assigned Asset Class of the Instrument
	 * Optional
	 */
	@Column(name="ASSET_CLASS_NAME", length=DataLengths.MARKET_OBJECT_NAME_LENGTH)
	private String _assetClassName;
	
	/**
	 * Sub-Type of the Instrument
	 * Optional
	 * 
	 * Note: This should be an Enum, but in that case new release would be 
	 * 		 needed for a new Sub-Category 
	 */
	@Column(name="SUB_TYPE", length=50)
	private String _subType;
	
	/**
	 * Code (Primary Key) of Underlying Product or Instrument of this Instrument
	 * Required
	 */
	@Column(name="UNDERLYING_CODE", nullable=false, length=DataLengths.MARKET_OBJECT_CODE_LENGTH)
	private String _underlyingCode;
	
	/**
	 * Type of the Underlying Object (Product or Instrument) of this Instrument
	 * Required
	 */
	@Column(name="UNDERLYING_TYPE", nullable=false)
	@Enumerated(EnumType.STRING) 
	private UnderlyingType _underlyingType;
	
	/**
	 * Reference to the Master Agreement, that defines the Trade Contract for this Instrument 
	 * Optional
	 * 
	 * Note: This could be a URL.
	 */
	@Column(name="MASTER_AGREEMENT_DOCUMENT", length=DataLengths.MARKET_OBJECT_DOCUMENT_LINK_LENGTH)
	private String _masterAgreementDocument;

	/**
	 * Denomination Currency   
	 * Required 
	 */
	@Column(name="DENOMINATION_CURRENCY", nullable=false, length=DataLengths.CURRENCY_CODE_LENGTH)
	private String _denominationCurrency;

	/**
	 * Size of the Contract, default is 1 
	 * Note: Market System only supports Quoting in 1 Contract Unit 
	 * 		 (e.g. quote for 1 Ton, when the contract size is like 100 Tons)
	 * Required 
	 */
	@Column(name="CONTRACT_SIZE", nullable=false)
	private Double _contractSize = 1.0;
	
	/**
	 * Contract Size Unit (like Tons or MWh)   
	 * Required 
	 */
	@Column(name="CONTRACT_SIZE_UNIT", nullable=false, length=DataLengths.UNIT_LENGTH)
	private String _contractSizeUnit;
	
	/**
	 * Type of the Value what is Quoted on the Market for this Instrument (Value from QuoteType)
	 * Required
	 */
	@Column(name="QUOTE_TYPE", nullable=false)
	@Enumerated(EnumType.STRING) 
	private QuoteType _quoteType = QuoteType.Price;
	
	/**
	 * Shows, if this contract can be rolled over to the next expiration date
	 * Required
	 */
	@Column(name="ROLLABLE", nullable=false)
	private Boolean _rollable;

	/**
	 * Ticker Symbol or Derivative Code, that this Instrument was created from by a 
	 * Rollover (For Rollable Instruments Only)
	 * Optional
	 */
	@Column(name="INSTRUMENT_ROLLED_OVER_FROM", length=DataLengths.MARKET_OBJECT_CODE_LENGTH)
	private String _instrumentCodeRolledOverFrom;

	/**
	 * List of Properties that can be changed during a rollover, like Future Expiration Dates
	 * Optional
	 */
	@ElementCollection(targetClass=String.class)
    @CollectionTable(name="INSTRUMENT_ROLLABLE_PROPERTY_NAMES")
	@Column(name="ROLLABLE_PROPERTY_NAMES")
	private List<String> _rollablePropertyNames = new ArrayList<String>();
	
	/**
	 * Activation Date of the Contract
	 */
	@Column(name="ACTIVATION_DATE")
	private java.sql.Date _activationDate;

	/**
	 * Property of the Contract, defines details of the Master Agreement (Like Expiration Date, Strike Price)
	 * Optional
	 */
	@OneToOne(cascade=CascadeType.ALL, orphanRemoval=true)
	@JoinColumn(name="PROPERTY_HOLDER_ID")
    private PropertyHolder _propertyHolder = new PropertyHolder();

	/**
	 * Type of the Settlement Price (Value from SettlementPrice), default is clean
	 * If the Dirty price is defined, there needs to be MaturityDate, DayCountFraction, Coupon and StartDate or InterestPeriod defined 
	 * on the Instrument 
	 * Optional
	 */
	@Column(name="SETTLEMENT_PRICE")
	@Enumerated(EnumType.STRING) 
	private SettlementPrice _settlementPrice = SettlementPrice.Clean;
	
	/**
	 * Type of the Settlement (Value from SettlementType)
	 * Required
	 */
	@Column(name="SETTLEMENT_TYPE", nullable=false)
	@Enumerated(EnumType.STRING) 
	private SettlementType _settlementType;
	
	/**
	 * Type of the Delivery Period (Value from Delivery Period)
	 * How many days the trade (expiration of the Derivative Contract) should the delivery happen.
	 * Required
	 */
	@Column(name="DELIVERY_PERIOD", nullable=false)
	@Enumerated(EnumType.STRING) 
	private DeliveryPeriod _deliveryPeriod;
	
	/**
	 * Defines where the delivery should happen - Only for Physical Products + Physical Settlement
	 * Optional
	 */
	@Embedded
	private Address _deliveryLocation;
	
	/**
	 * Defines, how the Purchase of this Instrument will be Recorded on the Account
	 * for example Stock purchase will be a long position, but CDS purchase will be short. 
	 * Default value is Long
	 * 
	 * Value from Position
	 * Required
	 */
	@Column(name="RECORD_PURCHASE_AS_POSITION", nullable=false)
	@Enumerated(EnumType.STRING) 
	private Position _recordPurchaseAsPosition = Position.Long;
	
	/**
	 * Audit Information, like who created the Instrument and when
	 * Optional
	 */
	@OneToOne(cascade=CascadeType.ALL, orphanRemoval=true) 
	@JoinColumn(name="CREATION_AUDIT_ID")
	private CreationAudit _creationAudit;

	/**
	 * Approval Audit Information, like who Approved the Instrument and when
	 * Optional
	 */
	@OneToOne(cascade=CascadeType.ALL, orphanRemoval=true) 
	@JoinColumn(name="APPROVAL_AUDIT_ID")
	private ApprovalAudit _approvalAudit;

	/**
	 * Suspension Audit Information, like who Suspended the Instrument and when
	 * Optional
	 */
	@OneToOne(cascade=CascadeType.ALL, orphanRemoval=true) 
	@JoinColumn(name="SUSPENSION_AUDIT_ID")
	private SuspensionAudit _suspensionAudit;
	
	/**
	 * Change Audit Information, like who Changed the Instrument and when
	 * Optional
	 */
	@OneToOne(cascade=CascadeType.ALL, orphanRemoval=true) 
	@JoinColumn(name="CHANGE_AUDIT_ID")
	private ChangeAudit _changeAudit;

	/**
	 * Rollover Audit Information, like who Rolled Over the Instrument and when
	 * Optional
	 */
	@OneToOne(cascade=CascadeType.ALL, orphanRemoval=true) 
	@JoinColumn(name="ROLLOVER_AUDIT_ID")
	private RolloverAudit _rolloverAudit;

	/**
	 * Supports the Activation Workflow of the Instrument
	 * 
	 * Note: This needs to be a String for Datastore to Query on this field
	 * Required 
	 */
	@Column(name="ACTIVATION_STATUS", nullable=false)
	@Enumerated(EnumType.STRING) 
	private ActivationStatus _activationStatus;
	
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

	public InstrumentType getType() {
		return _type;
	}

	public void setType(InstrumentType type_) {
		_type = type_;
	}

	public String getSubType() {
		return _subType;
	}

	public void setSubType(String subType_) {
		_subType = subType_;
	}

	public String getUnderlyingCode() {
		return _underlyingCode;
	}

	public void setUnderlyingCode(String underlyingCode_) {
		_underlyingCode = underlyingCode_;
	}

	public String getMasterAgreementDocument() {
		return _masterAgreementDocument;
	}

	public void setMasterAgreementDocument(String masterAgreementDocument_) {
		_masterAgreementDocument = masterAgreementDocument_;
	}
	
	public QuoteType getQuoteType() {
		return _quoteType;
	}

	public void setQuoteType(QuoteType quoteType_) {
		_quoteType = quoteType_;
	}

	public String getInstrumentCode() {
		return _instrumentCode;
	}

	public void setInstrumentCode(String instrumentCode_) {
		_instrumentCode = instrumentCode_;
	}

	public boolean getRollable() {
		return _rollable != null ? _rollable : false;
	}

	public void setRollable(boolean rollable_) {
		_rollable = rollable_;
	}

	public Date getActivationDate() {
		return _activationDate;
	}

	public void setActivationDate(Date activationDate_) {
		_activationDate = activationDate_ != null 
				? new java.sql.Date(activationDate_.getTime())
				: null;
	}

	public void setProperties(Property[] properties_) {
		clearProperties();
		if(properties_ != null) {			
			for (Property property : properties_) {
				addProperty(property);
			}
		}
	}
	
	public Property[] getProperties() {
		return _propertyHolder.getProperties();
	}

	public void addProperty(Property property_) {
		_propertyHolder.addProperty(property_);
	}
	
	public void deleteProperty(Property property_) {
		_propertyHolder.deleteProperty(property_);
	}
	
	public void clearProperties() {
		_propertyHolder.clearProperties();
	}

	public SettlementType getSettlementType() {
		return _settlementType;
	}

	public void setSettlementType(SettlementType settlementType_) {
		_settlementType = settlementType_;
	}

	public DeliveryPeriod getDeliveryPeriod() {
		return _deliveryPeriod;
	}

	public void setDeliveryPeriod(DeliveryPeriod deliveryPeriod_) {
		_deliveryPeriod = deliveryPeriod_;
	}

	public Address getDeliveryLocation() {
		return _deliveryLocation;
	}

	public void setDeliveryLocation(Address deliveryLocation_) {
		_deliveryLocation = deliveryLocation_;
	}

	public void setActivationStatus(ActivationStatus activationStatus) {
		_activationStatus = activationStatus; 
	}

	public ActivationStatus getActivationStatus() {
		return _activationStatus;
	}

	public void setRecordPurchaseAsPosition(Position recordPurchaseAsPosition) {
		_recordPurchaseAsPosition = recordPurchaseAsPosition;
	}

	public Position getRecordPurchaseAsPosition() {
		return _recordPurchaseAsPosition;
	}

	public void setDenominationCurrency(String denominationCurrency) {
		_denominationCurrency = denominationCurrency;
	}

	public String getDenominationCurrency() {
		return _denominationCurrency;
	}
	
	public void setContractSize(double contractSize) {
		_contractSize = contractSize;
	}

	public double getContractSize() {
		return _contractSize != null ? _contractSize : 1.0;
	}

	public void setContractSizeUnit(String contractSizeUnit) {
		_contractSizeUnit = contractSizeUnit;
	}

	public String getContractSizeUnit() {
		return _contractSizeUnit;
	}
	
	public void setSettlementPrice(SettlementPrice settlementPrice) {
		_settlementPrice = settlementPrice;
	}

	public SettlementPrice getSettlementPrice() {
		return _settlementPrice;
	}

	public void setIsin(String isin) {
		_isin = isin;
	}

	public String getIsin() {
		return _isin;
	}

	public void setAssetClassName(String assetClassName) {
		_assetClassName = assetClassName;
	}

	public String getAssetClassName() {
		return _assetClassName;
	}
	
	public String getInstrumentCodeRolledOverFrom() {
		return _instrumentCodeRolledOverFrom;
	}

	public void setInstrumentCodeRolledOverFrom(String instrumentCodeRolledOverFrom_) {
		_instrumentCodeRolledOverFrom = instrumentCodeRolledOverFrom_;
	}
	
	@Override
	public String getCode() {
		return getInstrumentCode();
	}

	@Override
	public String getCodeRolledFrom() {
		return getInstrumentCodeRolledOverFrom();
	}

	@Override
	public void setCodeRolledFrom(String codeRolledFrom_) {
		setInstrumentCodeRolledOverFrom(codeRolledFrom_);
	}
	
	public List<String> getRollablePropertyNames() {
		return _rollablePropertyNames;
	}

	public void setRollablePropertyNames(List<String> rollablePropertyNames) {
		_rollablePropertyNames = rollablePropertyNames;
	}

	public void setUnderlyingType(UnderlyingType underlyingType) {
		_underlyingType = underlyingType;
	}

	public UnderlyingType getUnderlyingType() {
		return _underlyingType;
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
	
	private void setAudit(AuditInformation source_, AuditInformation target_) {
		if(source_ != null && target_ != null) {
			target_.setDateTime(source_.getDateTime());
			target_.setUserID(source_.getUserID());	
		} 
	}

	@Override
	public void setNewCode(String code_) {
		setInstrumentCode(code_);
	}
	
	public void updateLastUpdateTimestamp() {
		_lastUpdateTimestamp = new Timestamp(System.currentTimeMillis());
	}

	public PropertyHolder getPropertyHolder() {
		return _propertyHolder;
	}

	public void setPropertyHolder(PropertyHolder propertyHolder_) {
		_propertyHolder = propertyHolder_;
	}

	@Override
	public Date getLastUpdateTimestamp() {
		return _lastUpdateTimestamp;
	}

	public void setLastUpdateTimestamp(Date lastUpdateTimestamp_) {
		_lastUpdateTimestamp = lastUpdateTimestamp_ != null ? new Timestamp(lastUpdateTimestamp_.getTime()) : null;
	}

	public Long getVersionNumber() {
		return _versionNumber;
	}

	public void setVersionNumber(Long versionNumber_) {
		_versionNumber = versionNumber_;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((_activationDate == null) ? 0 : _activationDate.hashCode());
		result = prime
				* result
				+ ((_activationStatus == null) ? 0 : _activationStatus
						.hashCode());
		result = prime * result
				+ ((_approvalAudit == null) ? 0 : _approvalAudit.hashCode());
		result = prime * result
				+ ((_assetClassName == null) ? 0 : _assetClassName.hashCode());
		result = prime * result
				+ ((_changeAudit == null) ? 0 : _changeAudit.hashCode());
		result = prime * result
				+ ((_contractSize == null) ? 0 : _contractSize.hashCode());
		result = prime
				* result
				+ ((_contractSizeUnit == null) ? 0 : _contractSizeUnit
						.hashCode());
		result = prime * result
				+ ((_creationAudit == null) ? 0 : _creationAudit.hashCode());
		result = prime
				* result
				+ ((_deliveryLocation == null) ? 0 : _deliveryLocation
						.hashCode());
		result = prime * result
				+ ((_deliveryPeriod == null) ? 0 : _deliveryPeriod.hashCode());
		result = prime
				* result
				+ ((_denominationCurrency == null) ? 0 : _denominationCurrency
						.hashCode());
		result = prime * result
				+ ((_description == null) ? 0 : _description.hashCode());
		result = prime * result
				+ ((_instrumentCode == null) ? 0 : _instrumentCode.hashCode());
		result = prime
				* result
				+ ((_instrumentCodeRolledOverFrom == null) ? 0
						: _instrumentCodeRolledOverFrom.hashCode());
		result = prime * result + ((_isin == null) ? 0 : _isin.hashCode());
		result = prime
				* result
				+ ((_lastUpdateTimestamp == null) ? 0 : _lastUpdateTimestamp
						.hashCode());
		result = prime
				* result
				+ ((_masterAgreementDocument == null) ? 0
						: _masterAgreementDocument.hashCode());
		result = prime * result + ((_name == null) ? 0 : _name.hashCode());
		result = prime * result
				+ ((_propertyHolder == null) ? 0 : _propertyHolder.hashCode());
		result = prime * result
				+ ((_quoteType == null) ? 0 : _quoteType.hashCode());
		result = prime
				* result
				+ ((_recordPurchaseAsPosition == null) ? 0
						: _recordPurchaseAsPosition.hashCode());
		result = prime * result
				+ ((_rollable == null) ? 0 : _rollable.hashCode());
		result = prime
				* result
				+ ((_rollablePropertyNames == null) ? 0
						: _rollablePropertyNames.hashCode());
		result = prime * result
				+ ((_rolloverAudit == null) ? 0 : _rolloverAudit.hashCode());
		result = prime
				* result
				+ ((_settlementPrice == null) ? 0 : _settlementPrice.hashCode());
		result = prime * result
				+ ((_settlementType == null) ? 0 : _settlementType.hashCode());
		result = prime * result
				+ ((_subType == null) ? 0 : _subType.hashCode());
		result = prime
				* result
				+ ((_suspensionAudit == null) ? 0 : _suspensionAudit.hashCode());
		result = prime * result + ((_type == null) ? 0 : _type.hashCode());
		result = prime * result
				+ ((_underlyingCode == null) ? 0 : _underlyingCode.hashCode());
		result = prime * result
				+ ((_underlyingType == null) ? 0 : _underlyingType.hashCode());
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
		Instrument other = (Instrument) obj;
		if (_activationDate == null) {
			if (other._activationDate != null)
				return false;
		} else if (!_activationDate.equals(other._activationDate))
			return false;
		if (_activationStatus != other._activationStatus)
			return false;
		if (_approvalAudit == null) {
			if (other._approvalAudit != null)
				return false;
		} else if (!_approvalAudit.equals(other._approvalAudit))
			return false;
		if (_assetClassName == null) {
			if (other._assetClassName != null)
				return false;
		} else if (!_assetClassName.equals(other._assetClassName))
			return false;
		if (_changeAudit == null) {
			if (other._changeAudit != null)
				return false;
		} else if (!_changeAudit.equals(other._changeAudit))
			return false;
		if (_contractSize == null) {
			if (other._contractSize != null)
				return false;
		} else if (!_contractSize.equals(other._contractSize))
			return false;
		if (_contractSizeUnit == null) {
			if (other._contractSizeUnit != null)
				return false;
		} else if (!_contractSizeUnit.equals(other._contractSizeUnit))
			return false;
		if (_creationAudit == null) {
			if (other._creationAudit != null)
				return false;
		} else if (!_creationAudit.equals(other._creationAudit))
			return false;
		if (_deliveryLocation == null) {
			if (other._deliveryLocation != null)
				return false;
		} else if (!_deliveryLocation.equals(other._deliveryLocation))
			return false;
		if (_deliveryPeriod != other._deliveryPeriod)
			return false;
		if (_denominationCurrency == null) {
			if (other._denominationCurrency != null)
				return false;
		} else if (!_denominationCurrency.equals(other._denominationCurrency))
			return false;
		if (_description == null) {
			if (other._description != null)
				return false;
		} else if (!_description.equals(other._description))
			return false;
		if (_instrumentCode == null) {
			if (other._instrumentCode != null)
				return false;
		} else if (!_instrumentCode.equals(other._instrumentCode))
			return false;
		if (_instrumentCodeRolledOverFrom == null) {
			if (other._instrumentCodeRolledOverFrom != null)
				return false;
		} else if (!_instrumentCodeRolledOverFrom
				.equals(other._instrumentCodeRolledOverFrom))
			return false;
		if (_isin == null) {
			if (other._isin != null)
				return false;
		} else if (!_isin.equals(other._isin))
			return false;
		if (_lastUpdateTimestamp == null) {
			if (other._lastUpdateTimestamp != null)
				return false;
		} else if (!_lastUpdateTimestamp.equals(other._lastUpdateTimestamp))
			return false;
		if (_masterAgreementDocument == null) {
			if (other._masterAgreementDocument != null)
				return false;
		} else if (!_masterAgreementDocument
				.equals(other._masterAgreementDocument))
			return false;
		if (_name == null) {
			if (other._name != null)
				return false;
		} else if (!_name.equals(other._name))
			return false;
		if (_propertyHolder == null) {
			if (other._propertyHolder != null)
				return false;
		} else if (!_propertyHolder.equals(other._propertyHolder))
			return false;
		if (_quoteType != other._quoteType)
			return false;
		if (_recordPurchaseAsPosition != other._recordPurchaseAsPosition)
			return false;
		if (_rollable == null) {
			if (other._rollable != null)
				return false;
		} else if (!_rollable.equals(other._rollable))
			return false;
		if (_rollablePropertyNames == null) {
			if (other._rollablePropertyNames != null)
				return false;
		} else if (!_rollablePropertyNames.equals(other._rollablePropertyNames))
			return false;
		if (_rolloverAudit == null) {
			if (other._rolloverAudit != null)
				return false;
		} else if (!_rolloverAudit.equals(other._rolloverAudit))
			return false;
		if (_settlementPrice != other._settlementPrice)
			return false;
		if (_settlementType != other._settlementType)
			return false;
		if (_subType == null) {
			if (other._subType != null)
				return false;
		} else if (!_subType.equals(other._subType))
			return false;
		if (_suspensionAudit == null) {
			if (other._suspensionAudit != null)
				return false;
		} else if (!_suspensionAudit.equals(other._suspensionAudit))
			return false;
		if (_type != other._type)
			return false;
		if (_underlyingCode == null) {
			if (other._underlyingCode != null)
				return false;
		} else if (!_underlyingCode.equals(other._underlyingCode))
			return false;
		if (_underlyingType != other._underlyingType)
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
		return "Instrument [_instrumentCode=" + _instrumentCode + ", _isin="
				+ _isin + ", _name=" + _name + ", _description=" + _description
				+ ", _type=" + _type + ", _assetClassName=" + _assetClassName
				+ ", _subType=" + _subType + ", _underlyingCode="
				+ _underlyingCode + ", _underlyingType=" + _underlyingType
				+ ", _masterAgreementDocument=" + _masterAgreementDocument
				+ ", _denominationCurrency=" + _denominationCurrency
				+ ", _contractSize=" + _contractSize + ", _contractSizeUnit="
				+ _contractSizeUnit + ", _quoteType=" + _quoteType
				+ ", _rollable=" + _rollable
				+ ", _instrumentCodeRolledOverFrom="
				+ _instrumentCodeRolledOverFrom + ", _rollablePropertyNames="
				+ _rollablePropertyNames + ", _activationDate="
				+ _activationDate + ", _propertyHolder=" + _propertyHolder
				+ ", _settlementPrice=" + _settlementPrice
				+ ", _settlementType=" + _settlementType + ", _deliveryPeriod="
				+ _deliveryPeriod + ", _deliveryLocation=" + _deliveryLocation
				+ ", _recordPurchaseAsPosition=" + _recordPurchaseAsPosition
				+ ", _creationAudit=" + _creationAudit + ", _approvalAudit="
				+ _approvalAudit + ", _suspensionAudit=" + _suspensionAudit
				+ ", _changeAudit=" + _changeAudit + ", _rolloverAudit="
				+ _rolloverAudit + ", _activationStatus=" + _activationStatus
				+ ", _lastUpdateTimestamp=" + _lastUpdateTimestamp
				+ ", _versionNumber=" + _versionNumber + "]";
	}

}
