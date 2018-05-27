package com.imarcats.model;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.imarcats.model.types.AuditInformation;

/**
 * Suspension audit for market objects 
 * @author Adam
 */
@Entity
@Table(name="SUSPENSION_AUDIT")
public class SuspensionAudit implements MarketModelObject {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * Primary Key of the Suspension Audit
	 * Note: No need for accessing this field directly
	 */	
	@Id
	@GeneratedValue 
	@Column(name="ID")
    private Long _id;
	
	/**
	 * Audit Information, like who suspended the Object and when
	 * Required
	 */
	@Embedded
	private AuditInformation _suspensionAudit;
	
	public SuspensionAudit(AuditInformation suspensionAudit_) {
		super();
		_suspensionAudit = suspensionAudit_;
	}

	public SuspensionAudit() {
		super();
	}

	public AuditInformation getSuspensionAudit() {
		return _suspensionAudit;
	}
	
	public void setSuspensionAudit(AuditInformation audit_) {
		_suspensionAudit = audit_;
	}

	@Override
	public String toString() {
		return "SuspensionAudit [_suspensionAudit=" + _suspensionAudit + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((_id == null) ? 0 : _id.hashCode());
		result = prime
				* result
				+ ((_suspensionAudit == null) ? 0 : _suspensionAudit.hashCode());
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
		SuspensionAudit other = (SuspensionAudit) obj;
		if (_id == null) {
			if (other._id != null)
				return false;
		} else if (!_id.equals(other._id))
			return false;
		if (_suspensionAudit == null) {
			if (other._suspensionAudit != null)
				return false;
		} else if (!_suspensionAudit.equals(other._suspensionAudit))
			return false;
		return true;
	}
	
}
