package com.imarcats.interfaces.client.v100.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.imarcats.interfaces.client.v100.dto.types.ActivationStatus;
import com.imarcats.interfaces.client.v100.dto.types.AuditInformationDto;
import com.imarcats.interfaces.client.v100.dto.types.PropertyDto;
import com.imarcats.interfaces.client.v100.dto.types.ProductType;
import com.imarcats.interfaces.client.v100.dto.types.UnderlyingType;

/**
 * Product traded on the Market 
 * @author Adam
 */
public class ProductDto implements UnderlyingDto, RollableDto {

	/**
	 * Similar to Ticker Symbol - ID of the Product (Primary Key)
	 * Required
	 */
//	@Column(name="PRODUCT_CODE", length=DataLengths.MARKET_OBJECT_CODE_LENGTH)
	private String _productCode;
	
	/**
	 * Name of the Product
	 * Required
	 */
//	@Column(name="NAME", nullable=false, length=DataLengths.MARKET_OBJECT_NAME_LENGTH)
	private String _name;
	
	/**
	 * Description of the Product
	 * Required
	 */
//	@Column(name="DESCRIPTION", nullable=false, length=DataLengths.MARKET_OBJECT_DESCRIPTION_LENGTH)
	private String _description;

	/**
	 * Type of the Product (Value from ProductType)
	 * Required
	 */
	private ProductType _type;
	
	/**
	 * Category, where this product falls (helps search)
	 * Optional
	 * 
	 * Note: This should be an Enum, but in that case new release would be 
	 * 		 needed for a new Category 
	 */
//	@Column(name="CATEGORY", length=50)
	private String _category;

	/**
	 * Sub-Category, where this product falls (helps search)
	 * Optional
	 * 
	 * Note: This should be an Enum, but in that case new release would be 
	 * 		 needed for a new Sub-Category 
	 */
//	@Column(name="SUB_CATEGORY", length=50)
	private String _subCategory;
	
	/**
	 * Shows, if this product can be rolled over to the next expiration date
	 * Required
	 */
	private Boolean _rollable;
	/**
	 * Ticker Symbol, that this Product was created from by a 
	 * Rollover (For Rollable Products Only)
	 * Optional
	 */
//	@Column(name="PRODUCT_CODE_ROLLED_OVER_FROM", length=DataLengths.MARKET_OBJECT_CODE_LENGTH)
	private String _productCodeRolledOverFrom;

	/**
	 * List of Properties that can be changed during a rollover, like Future Expiration Dates
	 * Optional
	 */
	private List<String> _rollablePropertyNames = new ArrayList<String>();
	
	/**
	 * Activation Date of the Product
	 */ 
	private java.sql.Date _activationDate;

	/**
	 * List of Properties for this Product
	 * Optional
	 */ 
    private PropertyDto[] _properties = new PropertyDto[0];
	
	/**
	 * Reference to the Product Definition Document
	 * Optional
	 * 
	 * Note: This could be a URL.
	 */
//	@Column(name="PRODUCT_DEFINITION_DOCUMENT", length=DataLengths.MARKET_OBJECT_DOCUMENT_LINK_LENGTH)
	private String _productDefinitionDocument;
	
	/**
	 * Audit Information, like who created the Product and when
	 * Optional
	 */
	private AuditInformationDto _creationAudit;

	/**
	 * Approval Audit Information, like who Approved the Product and when
	 * Optional
	 */
	private AuditInformationDto _approvalAudit;

	/**
	 * Suspension Audit Information, like who Suspended the Product and when
	 * Optional
	 */
	private AuditInformationDto _suspensionAudit;
	
	/**
	 * Change Audit Information, like who Changed the Product and when
	 * Optional
	 */
	private AuditInformationDto _changeAudit;

	/**
	 * Rollover Audit Information, like who Rolled Over the Product and when
	 * Optional
	 */
	private AuditInformationDto _rolloverAudit;

	/**
	 * Supports the Activation Workflow of the Product
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

	public ProductType getType() {
		return _type;
	}

	public void setType(ProductType type_) {
		_type = type_;
	}

	public String getCategory() {
		return _category;
	}

	public void setCategory(String category_) {
		_category = category_;
	}

	public String getSubCategory() {
		return _subCategory;
	}

	public void setSubCategory(String subCategory_) {
		_subCategory = subCategory_;
	}

	public boolean getRollable() {
		return _rollable != null ? _rollable : false;
	}

	public void setRollable(boolean rollable_) {
		_rollable = rollable_;
	}
	
	public String getProductCode() {
		return _productCode;
	}

	public void setProductCode(String productCode_) {
		_productCode = productCode_;
	}

	public String getProductDefinitionDocument() {
		return _productDefinitionDocument;
	}

	public void setProductDefinitionDocument(String productDefinitionDocument_) {
		_productDefinitionDocument = productDefinitionDocument_;
	}

	public void setActivationStatus(ActivationStatus activationStatus) {
		_activationStatus = activationStatus;
	}

	public ActivationStatus getActivationStatus() {
		return _activationStatus;
	}

	public String getProductCodeRolledOverFrom() {
		return _productCodeRolledOverFrom;
	}

	public void setProductCodeRolledOverFrom(String productCodeRolledOverFrom_) {
		_productCodeRolledOverFrom = productCodeRolledOverFrom_;
	}

	public Date getActivationDate() {
		return _activationDate;
	}

	public void setRollablePropertyNames(List<String> rollablePropertyNames_) {
		_rollablePropertyNames = rollablePropertyNames_;
	}

	public List<String> getRollablePropertyNames() {
		return _rollablePropertyNames;
	}

	public void setActivationDate(Date activationDate_) {
		_activationDate = 
				activationDate_ != null 
				? new java.sql.Date(activationDate_.getTime())
				: null;
	}

	public Date getLastUpdateTimestamp() {
		return _lastUpdateTimestamp;
	}

	public PropertyDto[] getProperties() {
		return _properties;
	}

	public void setProperties(PropertyDto[] properties_) {
		_properties = properties_;
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

	public void setRollable(Boolean rollable_) {
		_rollable = rollable_;
	}

	public void setLastUpdateTimestamp(Date lastUpdateTimestamp_) {
		_lastUpdateTimestamp = lastUpdateTimestamp_;
	}

	public Long getVersionNumber() {
		return _versionNumber;
	}

	public void setVersionNumber(Long versionNumber_) {
		_versionNumber = versionNumber_;
	}

	@Override
	public String getUnderlyingCode() {
		// there is no underlying for Product
		return null;
	}

	@Override
	public UnderlyingType getUnderlyingType() {
		// there is no underlying for Product
		return null;
	}
	
	@Override
	public String getCode() {
		return getProductCode();
	}

	@Override
	public String getCodeRolledFrom() {
		return getProductCodeRolledOverFrom();
	}

	@Override
	public void setCodeRolledFrom(String codeRolledFrom_) {
		setProductCodeRolledOverFrom(codeRolledFrom_);
	}

	@Override
	public void setNewCode(String code_) {
		setProductCode(code_);
	}
}
