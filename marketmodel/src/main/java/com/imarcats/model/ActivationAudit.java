package com.imarcats.model;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.imarcats.model.types.AuditInformation;

/**
 * Activation audit for market objects 
 * @author Adam
 */
@Entity
@Table(name="ACTIVATION_AUDIT")
public class ActivationAudit implements MarketModelObject {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * Primary Key of the Activation Audit
	 * Note: No need for accessing this field directly
	 */	
	@Id
	@GeneratedValue 
	@Column(name="ID")
    private Long _id;
	
	/**
	 * Audit Information, like who activated over the Object and when
	 * Required
	 */
	@Embedded
	private AuditInformation _activationAudit;
	
	public ActivationAudit(AuditInformation activationAudit_) {
		super();
		_activationAudit = activationAudit_;
	}

	public ActivationAudit() {
		super();
	}

	public AuditInformation getActivationAudit() {
		return _activationAudit;
	}
	
	public void setActivationAudit(AuditInformation audit_) {
		_activationAudit = audit_;
	}

	@Override
	public String toString() {
		return "ActivationAudit [_id=" + _id + ", _activationAudit="
				+ _activationAudit + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((_activationAudit == null) ? 0 : _activationAudit.hashCode());
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
		ActivationAudit other = (ActivationAudit) obj;
		if (_activationAudit == null) {
			if (other._activationAudit != null)
				return false;
		} else if (!_activationAudit.equals(other._activationAudit))
			return false;
		if (_id == null) {
			if (other._id != null)
				return false;
		} else if (!_id.equals(other._id))
			return false;
		return true;
	}

}
