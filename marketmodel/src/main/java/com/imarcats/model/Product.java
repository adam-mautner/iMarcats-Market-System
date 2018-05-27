package com.imarcats.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
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
import com.imarcats.model.types.AuditInformation;
import com.imarcats.model.types.Property;
import com.imarcats.model.types.ProductType;
import com.imarcats.model.types.PropertyHolder;
import com.imarcats.model.types.UnderlyingType;

/**
 * Product traded on the Market 
 * @author Adam
 */
@Entity
@Table(name="PRODUCT")
public class Product implements Underlying, Rollable {

	private static final long serialVersionUID = 1L;

	/**
	 * Similar to Ticker Symbol - ID of the Product (Primary Key)
	 * Required
	 */
	@Id
	@Column(name="PRODUCT_CODE", length=DataLengths.MARKET_OBJECT_CODE_LENGTH)
	private String _productCode;
	
	/**
	 * Name of the Product
	 * Required
	 */
	@Column(name="NAME", nullable=false, length=DataLengths.MARKET_OBJECT_NAME_LENGTH)
	private String _name;
	
	/**
	 * Description of the Product
	 * Required
	 */
	@Column(name="DESCRIPTION", nullable=false, length=DataLengths.MARKET_OBJECT_DESCRIPTION_LENGTH)
	private String _description;

	/**
	 * Type of the Product (Value from ProductType)
	 * Required
	 */
	@Column(name="PRODUCT_TYPE", nullable=false)
	@Enumerated(EnumType.STRING) 
	private ProductType _type;
	
	/**
	 * Category, where this product falls (helps search)
	 * Optional
	 * 
	 * Note: This should be an Enum, but in that case new release would be 
	 * 		 needed for a new Category 
	 */
	@Column(name="CATEGORY", length=50)
	private String _category;

	/**
	 * Sub-Category, where this product falls (helps search)
	 * Optional
	 * 
	 * Note: This should be an Enum, but in that case new release would be 
	 * 		 needed for a new Sub-Category 
	 */
	@Column(name="SUB_CATEGORY", length=50)
	private String _subCategory;
	
	/**
	 * Shows, if this product can be rolled over to the next expiration date
	 * Required
	 */
	@Column(name="ROLLABLE", nullable=false)
	private Boolean _rollable;
	/**
	 * Ticker Symbol, that this Product was created from by a 
	 * Rollover (For Rollable Products Only)
	 * Optional
	 */
	@Column(name="PRODUCT_CODE_ROLLED_OVER_FROM", length=DataLengths.MARKET_OBJECT_CODE_LENGTH)
	private String _productCodeRolledOverFrom;

	/**
	 * List of Properties that can be changed during a rollover, like Future Expiration Dates
	 * Optional
	 */
	@ElementCollection(targetClass=String.class)
    @CollectionTable(name="PRODUCT_ROLLABLE_PROPERTY_NAMES")
	@Column(name="ROLLABLE_PROPERTY_NAME")
	private List<String> _rollablePropertyNames = new ArrayList<String>();
	
	/**
	 * Activation Date of the Product
	 */ 
	@Column(name="ACTIVATION_DATE")
	private java.sql.Date _activationDate;

	/**
	 * List of Properties for this Product
	 * Optional
	 */ 
	@OneToOne(cascade=CascadeType.ALL, orphanRemoval=true)
	@JoinColumn(name="PROPERTY_HOLDER_ID")
    private PropertyHolder _propertyHolder = new PropertyHolder();
	
	/**
	 * Reference to the Product Definition Document
	 * Optional
	 * 
	 * Note: This could be a URL.
	 */
	@Column(name="PRODUCT_DEFINITION_DOCUMENT", length=DataLengths.MARKET_OBJECT_DOCUMENT_LINK_LENGTH)
	private String _productDefinitionDocument;
	
	/**
	 * Audit Information, like who created the Product and when
	 * Optional
	 */
	@OneToOne(cascade=CascadeType.ALL, orphanRemoval=true) 
	@JoinColumn(name="CREATION_AUDIT_ID")
	private CreationAudit _creationAudit;

	/**
	 * Approval Audit Information, like who Approved the Product and when
	 * Optional
	 */
	@OneToOne(cascade=CascadeType.ALL, orphanRemoval=true) 
	@JoinColumn(name="APPROVAL_AUDIT_ID")
	private ApprovalAudit _approvalAudit;

	/**
	 * Suspension Audit Information, like who Suspended the Product and when
	 * Optional
	 */
	@OneToOne(cascade=CascadeType.ALL, orphanRemoval=true) 
	@JoinColumn(name="SUSPENSION_AUDIT_ID")
	private SuspensionAudit _suspensionAudit;
	
	/**
	 * Change Audit Information, like who Changed the Product and when
	 * Optional
	 */
	@OneToOne(cascade=CascadeType.ALL, orphanRemoval=true) 
	@JoinColumn(name="CHANGE_AUDIT_ID")
	private ChangeAudit _changeAudit;

	/**
	 * Rollover Audit Information, like who Rolled Over the Product and when
	 * Optional
	 */
	@OneToOne(cascade=CascadeType.ALL, orphanRemoval=true) 
	@JoinColumn(name="ROLLOVER_AUDIT_ID")
	private RolloverAudit _rolloverAudit;

	/**
	 * Supports the Activation Workflow of the Product
	 * 
	 * Note: This needs to be a String for Datastore to Query on this field
	 * Required 
	 */
	@Column(name="ACTIVATION_STATUS", nullable=false)
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

	public Property[] getProperties() {
		return _propertyHolder.getProperties();
	}

	public void setProperties(Property[] properties_) {
		clearProperties();
		if(properties_ != null) {			
			for (Property property : properties_) {
				addProperty(property);
			}
		}
	}
	
	public void addProperty(Property expirationProperty_) {
		_propertyHolder.addProperty(expirationProperty_);
	}
	
	public void deleteProperty(Property property_) {
		_propertyHolder.deleteProperty(property_);
	}
	
	public void clearProperties() {
		_propertyHolder.clearProperties();
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
	public List<String> getRollablePropertyNames() {
		return _rollablePropertyNames;
	}

	@Override
	public void setActivationDate(Date activationDate_) {
		_activationDate = 
				activationDate_ != null 
				? new java.sql.Date(activationDate_.getTime())
				: null;
	}

	@Override
	public void setCodeRolledFrom(String codeRolledFrom_) {
		setProductCodeRolledOverFrom(codeRolledFrom_);
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
	
	private void setAudit(AuditInformation source_, AuditInformation target_) {
		if(source_ != null && target_ != null) {
			target_.setDateTime(source_.getDateTime());
			target_.setUserID(source_.getUserID());		
		} 
	}

	@Override
	public void setNewCode(String code_) {
		setProductCode(code_);
	}
	
	public void updateLastUpdateTimestamp() {
		_lastUpdateTimestamp = new Timestamp(System.currentTimeMillis());
	}
	
	public void setLastUpdateTimestamp(Date lastUpdateTimestamp_) {
		_lastUpdateTimestamp = lastUpdateTimestamp_ != null ? new Timestamp(lastUpdateTimestamp_.getTime()) : null;
	}
	
	@Override
	public Date getLastUpdateTimestamp() {
		return _lastUpdateTimestamp;
	}

	public PropertyHolder getPropertyHolder() {
		return _propertyHolder;
	}

	public void setPropertyHolder(PropertyHolder propertyHolder_) {
		_propertyHolder = propertyHolder_;
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
				+ ((_category == null) ? 0 : _category.hashCode());
		result = prime * result
				+ ((_changeAudit == null) ? 0 : _changeAudit.hashCode());
		result = prime * result
				+ ((_creationAudit == null) ? 0 : _creationAudit.hashCode());
		result = prime * result
				+ ((_description == null) ? 0 : _description.hashCode());
		result = prime
				* result
				+ ((_lastUpdateTimestamp == null) ? 0 : _lastUpdateTimestamp
						.hashCode());
		result = prime * result + ((_name == null) ? 0 : _name.hashCode());
		result = prime * result
				+ ((_productCode == null) ? 0 : _productCode.hashCode());
		result = prime
				* result
				+ ((_productCodeRolledOverFrom == null) ? 0
						: _productCodeRolledOverFrom.hashCode());
		result = prime
				* result
				+ ((_productDefinitionDocument == null) ? 0
						: _productDefinitionDocument.hashCode());
		result = prime * result
				+ ((_propertyHolder == null) ? 0 : _propertyHolder.hashCode());
		result = prime * result
				+ ((_rollable == null) ? 0 : _rollable.hashCode());
		result = prime
				* result
				+ ((_rollablePropertyNames == null) ? 0
						: _rollablePropertyNames.hashCode());
		result = prime * result
				+ ((_rolloverAudit == null) ? 0 : _rolloverAudit.hashCode());
		result = prime * result
				+ ((_subCategory == null) ? 0 : _subCategory.hashCode());
		result = prime
				* result
				+ ((_suspensionAudit == null) ? 0 : _suspensionAudit.hashCode());
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
		Product other = (Product) obj;
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
		if (_category == null) {
			if (other._category != null)
				return false;
		} else if (!_category.equals(other._category))
			return false;
		if (_changeAudit == null) {
			if (other._changeAudit != null)
				return false;
		} else if (!_changeAudit.equals(other._changeAudit))
			return false;
		if (_creationAudit == null) {
			if (other._creationAudit != null)
				return false;
		} else if (!_creationAudit.equals(other._creationAudit))
			return false;
		if (_description == null) {
			if (other._description != null)
				return false;
		} else if (!_description.equals(other._description))
			return false;
		if (_lastUpdateTimestamp == null) {
			if (other._lastUpdateTimestamp != null)
				return false;
		} else if (!_lastUpdateTimestamp.equals(other._lastUpdateTimestamp))
			return false;
		if (_name == null) {
			if (other._name != null)
				return false;
		} else if (!_name.equals(other._name))
			return false;
		if (_productCode == null) {
			if (other._productCode != null)
				return false;
		} else if (!_productCode.equals(other._productCode))
			return false;
		if (_productCodeRolledOverFrom == null) {
			if (other._productCodeRolledOverFrom != null)
				return false;
		} else if (!_productCodeRolledOverFrom
				.equals(other._productCodeRolledOverFrom))
			return false;
		if (_productDefinitionDocument == null) {
			if (other._productDefinitionDocument != null)
				return false;
		} else if (!_productDefinitionDocument
				.equals(other._productDefinitionDocument))
			return false;
		if (_propertyHolder == null) {
			if (other._propertyHolder != null)
				return false;
		} else if (!_propertyHolder.equals(other._propertyHolder))
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
		if (_subCategory == null) {
			if (other._subCategory != null)
				return false;
		} else if (!_subCategory.equals(other._subCategory))
			return false;
		if (_suspensionAudit == null) {
			if (other._suspensionAudit != null)
				return false;
		} else if (!_suspensionAudit.equals(other._suspensionAudit))
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

	@Override
	public String toString() {
		return "Product [_productCode=" + _productCode + ", _name=" + _name
				+ ", _description=" + _description + ", _type=" + _type
				+ ", _category=" + _category + ", _subCategory=" + _subCategory
				+ ", _rollable=" + _rollable + ", _productCodeRolledOverFrom="
				+ _productCodeRolledOverFrom + ", _rollablePropertyNames="
				+ _rollablePropertyNames + ", _activationDate="
				+ _activationDate + ", _propertyHolder=" + _propertyHolder
				+ ", _productDefinitionDocument=" + _productDefinitionDocument
				+ ", _creationAudit=" + _creationAudit + ", _approvalAudit="
				+ _approvalAudit + ", _suspensionAudit=" + _suspensionAudit
				+ ", _changeAudit=" + _changeAudit + ", _rolloverAudit="
				+ _rolloverAudit + ", _activationStatus=" + _activationStatus
				+ ", _lastUpdateTimestamp=" + _lastUpdateTimestamp
				+ ", _versionNumber=" + _versionNumber + "]";
	}

}
