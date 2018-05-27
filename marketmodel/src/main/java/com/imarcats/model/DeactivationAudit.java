package com.imarcats.model;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.imarcats.model.types.AuditInformation;

/**
 * Deactivation audit for market objects 
 * @author Adam
 */
//@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable="true")
@Entity
@Table(name="DEACTIVATION_AUDIT")
public class DeactivationAudit implements MarketModelObject {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * Primary Key of the Deactivation Audit
	 * Note: No need for accessing this field directly
	 */	
	@Id
	@GeneratedValue 
	@Column(name="ID")
    private Long _id;
	
	/**
	 * Audit Information, like who deactivated over the Object and when
	 * Required
	 */
	@Embedded
	private AuditInformation _deactivationAudit;
	
	public DeactivationAudit(AuditInformation deactivationAudit_) {
		super();
		_deactivationAudit = deactivationAudit_;
	}

	public DeactivationAudit() {
		super();
	}

	public AuditInformation getDeactivationAudit() {
		return _deactivationAudit;
	}
	
	public void setDeactivationAudit(AuditInformation audit_) {
		_deactivationAudit = audit_;
	}

	@Override
	public String toString() {
		return "DeactivationAudit [_deactivationAudit=" + _deactivationAudit
				+ "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((_deactivationAudit == null) ? 0 : _deactivationAudit
						.hashCode());
		result = prime * result + ((_id == null) ? 0 : _id.hashCode());
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
		DeactivationAudit other = (DeactivationAudit) obj;
		if (_deactivationAudit == null) {
			if (other._deactivationAudit != null)
				return false;
		} else if (!_deactivationAudit.equals(other._deactivationAudit))
			return false;
		if (_id == null) {
			if (other._id != null)
				return false;
		} else if (!_id.equals(other._id))
			return false;
		return true;
	}
	
}
