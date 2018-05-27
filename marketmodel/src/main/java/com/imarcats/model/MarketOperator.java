package com.imarcats.model;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
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

/**
 * Market Operator creates and operates a one or more Markets
 * @author Adam
 */
@Entity
@Table(name="MARKET_OPERATOR")
public class MarketOperator implements MarketModelObject, ActivatableMarketObject {

	private static final long serialVersionUID = 1L;

	/**
	 * Code of the Market Operator - Primary Key of the Market Operator
	 * Required
	 */
	@Id
	@Column(name="CODE", length=DataLengths.MARKET_OBJECT_CODE_LENGTH)
	private String _code;

	/**
	 * Name of the Market Operator
	 * Required
	 */
	@Column(name="NAME", nullable=false, length=DataLengths.MARKET_OBJECT_NAME_LENGTH)
	private String _name;

	/**
	 * Description of the Market Operator
	 * Required
	 */
	@Column(name="DESCRIPTION", nullable=false, length=DataLengths.MARKET_OBJECT_DESCRIPTION_LENGTH)
	private String _description;
	
	/**
	 * User IDs behind this Market Operator 
	 * Optional
	 */
	@Column(name="OWNER_USER_ID", nullable=false, length=DataLengths.USER_ID_LENGTH)
	private String _ownerUserID;
	
	/**
	 * Reference to the Market Operator's Business Entity 
	 * Required
	 */
	@Column(name="BUSINESS_ENTITY_CODE", nullable=false, length=DataLengths.MARKET_OBJECT_CODE_LENGTH)
	private String _businessEntityCode;
	
	/**
	 * Reference to the Market Operator Agreement 
	 * Optional
	 * 
	 * Note: This could be a URL.
	 */
	@Column(name="MARKET_OPERTOR_AGREEMENT", length=DataLengths.MARKET_OBJECT_DOCUMENT_LINK_LENGTH)
	private String _marketOperatorAgreement;
	
	/**
	 * Audit Information, like who created the Market Operator and when
	 * Optional
	 */
	@OneToOne(cascade=CascadeType.ALL, orphanRemoval=true) 
	@JoinColumn(name="CREATION_AUDIT_ID")
	private CreationAudit _creationAudit;

	/**
	 * Approval Audit Information, like who Approved the Market Operator and when
	 * Optional
	 */
	@OneToOne(cascade=CascadeType.ALL, orphanRemoval=true) 
	@JoinColumn(name="APPROVAL_AUDIT_ID")
	private ApprovalAudit _approvalAudit;

	/**
	 * Suspension Audit Information, like who Suspended the Market Operator and when
	 * Optional
	 */
	@OneToOne(cascade=CascadeType.ALL, orphanRemoval=true) 
	@JoinColumn(name="SUSPENSION_AUDIT_ID")
	private SuspensionAudit _suspensionAudit;
	
	/**
	 * Change Audit Information, like who Changed the Market Operator and when
	 * Optional
	 */
	@OneToOne(cascade=CascadeType.ALL, orphanRemoval=true) 
	@JoinColumn(name="CHANGE_AUDIT_ID")
	private ChangeAudit _changeAudit;

	/**
	 * Supports the Activation Workflow of the Market Operator (Value from ActivationStatus)
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
	
	public String getOwnerUserID() {
		return _ownerUserID;
	}

	public void setOwnerUserID(String ownerUserID_) {		
		_ownerUserID = ownerUserID_;
	}

	public String getMarketOperatorAgreement() {
		return _marketOperatorAgreement;
	}

	public void setMarketOperatorAgreement(String marketOperatorAgreement_) {
		_marketOperatorAgreement = marketOperatorAgreement_;
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

	public void setName(String name) {
		_name = name;
	}

	public String getName() {
		return _name;
	}

	public String getCode() {
		return _code;
	}

	public void setCode(String code_) {
		_code = code_;
	}

	public String getDescription() {
		return _description;
	}

	public void setDescription(String description_) {
		_description = description_;
	}
	
	public void setActivationStatus(ActivationStatus activationStatus) {
		_activationStatus = activationStatus;
	}

	public ActivationStatus getActivationStatus() {
		return _activationStatus;
	}

	@Override
	public AuditInformation getRolloverAudit() {
		return null;
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

	public void setBusinessEntityCode(String businessEntityCode) {
		_businessEntityCode = businessEntityCode;
	}

	public String getBusinessEntityCode() {
		return _businessEntityCode;
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
		result = prime
				* result
				+ ((_activationStatus == null) ? 0 : _activationStatus
						.hashCode());
		result = prime * result
				+ ((_approvalAudit == null) ? 0 : _approvalAudit.hashCode());
		result = prime
				* result
				+ ((_businessEntityCode == null) ? 0 : _businessEntityCode
						.hashCode());
		result = prime * result
				+ ((_changeAudit == null) ? 0 : _changeAudit.hashCode());
		result = prime * result + ((_code == null) ? 0 : _code.hashCode());
		result = prime * result
				+ ((_creationAudit == null) ? 0 : _creationAudit.hashCode());
		result = prime * result
				+ ((_description == null) ? 0 : _description.hashCode());
		result = prime
				* result
				+ ((_lastUpdateTimestamp == null) ? 0 : _lastUpdateTimestamp
						.hashCode());
		result = prime
				* result
				+ ((_marketOperatorAgreement == null) ? 0
						: _marketOperatorAgreement.hashCode());
		result = prime * result + ((_name == null) ? 0 : _name.hashCode());
		result = prime * result
				+ ((_ownerUserID == null) ? 0 : _ownerUserID.hashCode());
		result = prime
				* result
				+ ((_suspensionAudit == null) ? 0 : _suspensionAudit.hashCode());
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
		MarketOperator other = (MarketOperator) obj;
		if (_activationStatus != other._activationStatus)
			return false;
		if (_approvalAudit == null) {
			if (other._approvalAudit != null)
				return false;
		} else if (!_approvalAudit.equals(other._approvalAudit))
			return false;
		if (_businessEntityCode == null) {
			if (other._businessEntityCode != null)
				return false;
		} else if (!_businessEntityCode.equals(other._businessEntityCode))
			return false;
		if (_changeAudit == null) {
			if (other._changeAudit != null)
				return false;
		} else if (!_changeAudit.equals(other._changeAudit))
			return false;
		if (_code == null) {
			if (other._code != null)
				return false;
		} else if (!_code.equals(other._code))
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
		if (_marketOperatorAgreement == null) {
			if (other._marketOperatorAgreement != null)
				return false;
		} else if (!_marketOperatorAgreement
				.equals(other._marketOperatorAgreement))
			return false;
		if (_name == null) {
			if (other._name != null)
				return false;
		} else if (!_name.equals(other._name))
			return false;
		if (_ownerUserID == null) {
			if (other._ownerUserID != null)
				return false;
		} else if (!_ownerUserID.equals(other._ownerUserID))
			return false;
		if (_suspensionAudit == null) {
			if (other._suspensionAudit != null)
				return false;
		} else if (!_suspensionAudit.equals(other._suspensionAudit))
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
		return "MarketOperator [_code=" + _code + ", _name=" + _name
				+ ", _description=" + _description + ", _ownerUserID="
				+ _ownerUserID + ", _businessEntityCode=" + _businessEntityCode
				+ ", _marketOperatorAgreement=" + _marketOperatorAgreement
				+ ", _creationAudit=" + _creationAudit + ", _approvalAudit="
				+ _approvalAudit + ", _suspensionAudit=" + _suspensionAudit
				+ ", _changeAudit=" + _changeAudit + ", _activationStatus="
				+ _activationStatus + ", _lastUpdateTimestamp="
				+ _lastUpdateTimestamp + ", _versionNumber=" + _versionNumber
				+ "]";
	}

}
