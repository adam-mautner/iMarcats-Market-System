package com.imarcats.interfaces.client.v100.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;

import com.imarcats.interfaces.client.v100.dto.types.ActivationStatus;
import com.imarcats.interfaces.client.v100.dto.types.AddressDto;
import com.imarcats.interfaces.client.v100.dto.types.AuditInformationDto;
import com.imarcats.interfaces.client.v100.dto.types.DeliveryPeriod;
import com.imarcats.interfaces.client.v100.dto.types.PropertyDto;
import com.imarcats.interfaces.client.v100.dto.types.InstrumentType;
import com.imarcats.interfaces.client.v100.dto.types.Position;
import com.imarcats.interfaces.client.v100.dto.types.QuoteType;
import com.imarcats.interfaces.client.v100.dto.types.SettlementPrice;
import com.imarcats.interfaces.client.v100.dto.types.SettlementType;
import com.imarcats.interfaces.client.v100.dto.types.UnderlyingType;

/**
 * Instrument defined on a Product or on an other Instruments. 
 * Instruments traded on the Markets.
 * @author Adam
 */
public class InstrumentDto implements UnderlyingDto, RollableDto {

	/**
	 * Ticker Symbol or Derivative Code (Primary Key)
	 * Required
	 */
//	@Column(name="INSTRUMENT_CODE", length=DataLengths.MARKET_OBJECT_CODE_LENGTH)
	private String _instrumentCode;

	/**
	 * ISIN (International Security Identification Number) like CUSIP, 
	 * identifies this Instrument in External Systems
	 * Optional
	 */
//	@Column(name="ISIN", length=12)
	private String _isin;    
    
	/**
	 * Name of the Instrument
	 * Required
	 */
//	@Column(name="NAME", nullable=false, length=DataLengths.MARKET_OBJECT_NAME_LENGTH)
	private String _name;
	
	/**
	 * Description of the Instrument
	 * Required
	 */
//	@Column(name="DESCRIPTION", nullable=false, length=DataLengths.MARKET_OBJECT_DESCRIPTION_LENGTH)
	private String _description;
	
	/**
	 * Type of the Instrument (Value from InstrumentType)
	 * Required
	 */
	private InstrumentType _type;
	
	/**
	 * Name of the Assigned Asset Class of the Instrument
	 * Optional
	 */
//	@Column(name="ASSET_CLASS_NAME", length=DataLengths.MARKET_OBJECT_NAME_LENGTH)
	private String _assetClassName;
	
	/**
	 * Sub-Type of the Instrument
	 * Optional
	 * 
	 * Note: This should be an Enum, but in that case new release would be 
	 * 		 needed for a new Sub-Category 
	 */
//	@Column(name="SUB_TYPE", length=50)
	private String _subType;
	
	/**
	 * Code (Primary Key) of Underlying Product or Instrument of this Instrument
	 * Required
	 */
//	@Column(name="UNDERLYING_CODE", nullable=false, length=DataLengths.MARKET_OBJECT_CODE_LENGTH)
	private String _underlyingCode;
	
	/**
	 * Type of the Underlying Object (Product or Instrument) of this Instrument
	 * Required
	 */
	private UnderlyingType _underlyingType;
	
	/**
	 * Reference to the Master Agreement, that defines the Trade Contract for this Instrument 
	 * Optional
	 * 
	 * Note: This could be a URL.
	 */
//	@Column(name="MASTER_AGREEMENT_DOCUMENT", length=DataLengths.MARKET_OBJECT_DOCUMENT_LINK_LENGTH)
	private String _masterAgreementDocument;

	/**
	 * Denomination Currency   
	 * Required 
	 */
//	@Column(name="DENOMINATION_CURRENCY", nullable=false, length=DataLengths.CURRENCY_CODE_LENGTH)
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
//	@Column(name="CONTRACT_SIZE_UNIT", nullable=false, length=DataLengths.UNIT_LENGTH)
	private String _contractSizeUnit;
	
	/**
	 * Type of the Value what is Quoted on the Market for this Instrument (Value from QuoteType)
	 * Required
	 */
	private QuoteType _quoteType = QuoteType.Price;
	
	/**
	 * Shows, if this contract can be rolled over to the next expiration date
	 * Required
	 */
	private Boolean _rollable;

	/**
	 * Ticker Symbol or Derivative Code, that this Instrument was created from by a 
	 * Rollover (For Rollable Instruments Only)
	 * Optional
	 */
//	@Column(name="INSTRUMENT_ROLLED_OVER_FROM", length=DataLengths.MARKET_OBJECT_CODE_LENGTH)
	private String _instrumentCodeRolledOverFrom;

	/**
	 * List of Properties that can be changed during a rollover, like Future Expiration Dates
	 * Optional
	 */
	private List<String> _rollablePropertyNames = new ArrayList<String>();
	
	/**
	 * Activation Date of the Contract
	 */
	private java.sql.Date _activationDate;

	/**
	 * Property of the Contract, defines details of the Master Agreement (Like Expiration Date, Strike Price)
	 * Optional
	 */
    private PropertyDto[] _properties = new PropertyDto[0];

	/**
	 * Type of the Settlement Price (Value from SettlementPrice), default is clean
	 * If the Dirty price is defined, there needs to be MaturityDate, DayCountFraction, Coupon and StartDate or InterestPeriod defined 
	 * on the Instrument 
	 * Optional
	 */
	private SettlementPrice _settlementPrice = SettlementPrice.Clean;
	
	/**
	 * Type of the Settlement (Value from SettlementType)
	 * Required
	 */
	private SettlementType _settlementType;
	
	/**
	 * Type of the Delivery Period (Value from Delivery Period)
	 * How many days the trade (expiration of the Derivative Contract) should the delivery happen.
	 * Required
	 */
	private DeliveryPeriod _deliveryPeriod;
	
	/**
	 * Defines where the delivery should happen - Only for Physical Products + Physical Settlement
	 * Optional
	 */
	private AddressDto _deliveryLocation;
	
	/**
	 * Defines, how the Purchase of this Instrument will be Recorded on the Account
	 * for example Stock purchase will be a long position, but CDS purchase will be short. 
	 * Default value is Long
	 * 
	 * Value from Position
	 * Required
	 */
	private Position _recordPurchaseAsPosition = Position.Long;
	
	/**
	 * Audit Information, like who created the Instrument and when
	 * Optional
	 */
	private AuditInformationDto _creationAudit;

	/**
	 * Approval Audit Information, like who Approved the Instrument and when
	 * Optional
	 */
	private AuditInformationDto _approvalAudit;

	/**
	 * Suspension Audit Information, like who Suspended the Instrument and when
	 * Optional
	 */
	private AuditInformationDto _suspensionAudit;
	
	/**
	 * Change Audit Information, like who Changed the Instrument and when
	 * Optional
	 */
	private AuditInformationDto _changeAudit;

	/**
	 * Rollover Audit Information, like who Rolled Over the Instrument and when
	 * Optional
	 */
	private AuditInformationDto _rolloverAudit;

	/**
	 * Supports the Activation Workflow of the Instrument
	 * 
	 * Note: This needs to be a String for Datastore to Query on this field
	 * Required 
	 */ 
	private ActivationStatus _activationStatus;
	
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

	public AddressDto getDeliveryLocation() {
		return _deliveryLocation;
	}

	public void setDeliveryLocation(AddressDto deliveryLocation_) {
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

	public PropertyDto[] getProperties() {
		return _properties;
	}

	public void setProperties(PropertyDto[] properties_) {
		_properties = properties_;
	}

	public Date getLastUpdateTimestamp() {
		return _lastUpdateTimestamp;
	}

	public void setLastUpdateTimestamp(Date lastUpdateTimestamp_) {
		_lastUpdateTimestamp = lastUpdateTimestamp_;
	}

	public void setContractSize(Double contractSize_) {
		_contractSize = contractSize_;
	}

	public void setCreationAudit(AuditInformationDto creationAudit_) {
		_creationAudit = creationAudit_;
	}

	public void setApprovalAudit(AuditInformationDto approvalAudit_) {
		_approvalAudit = approvalAudit_;
	}

	public void setSuspensionAudit(AuditInformationDto suspensionAudit_) {
		_suspensionAudit = suspensionAudit_;
	}

	public void setChangeAudit(AuditInformationDto changeAudit_) {
		_changeAudit = changeAudit_;
	}

	public void setRolloverAudit(AuditInformationDto rolloverAudit_) {
		_rolloverAudit = rolloverAudit_;
	}

	public AuditInformationDto getCreationAudit() {
		return _creationAudit;
	}

	public AuditInformationDto getApprovalAudit() {
		return _approvalAudit;
	}

	public AuditInformationDto getSuspensionAudit() {
		return _suspensionAudit;
	}

	public AuditInformationDto getChangeAudit() {
		return _changeAudit;
	}

	public AuditInformationDto getRolloverAudit() {
		return _rolloverAudit;
	}

	public Long getVersionNumber() {
		return _versionNumber;
	}

	public void setVersionNumber(Long versionNumber_) {
		_versionNumber = versionNumber_;
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

	@Override
	public void setNewCode(String code_) {
		setInstrumentCode(code_);
	}
}
