package com.imarcats.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.imarcats.model.types.AuditInformation;

/**
 * Deletion audit for market objects 
 * @author Adam
 */
@Entity
@Table(name="DELETION_AUDIT")
public class DeletionAudit implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/**
	 * Primary Key of the Change Audit
	 * Note: No need for accessing this field directly
	 */	
	@Id
	@GeneratedValue 
	@Column(name="ID")
    private Long _key;
	
	/**
	 * Audit Information, like who changed the Object and when
	 * Required
	 */
	@Embedded
	private AuditInformation _changeAudit;
	
	public DeletionAudit() {
		super();
	}

	public DeletionAudit(AuditInformation changeAudit_) {
		super();
		_changeAudit = changeAudit_;
	}

	public AuditInformation getDeletionAudit() {
		return _changeAudit;
	}
	
	public void setDeletionAudit(AuditInformation audit_) {
		_changeAudit = audit_;
	}

	@Override
	public String toString() {
		return "DeletionAudit [_changeAudit=" + _changeAudit + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((_changeAudit == null) ? 0 : _changeAudit.hashCode());
		result = prime * result + ((_key == null) ? 0 : _key.hashCode());
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
		DeletionAudit other = (DeletionAudit) obj;
		if (_changeAudit == null) {
			if (other._changeAudit != null)
				return false;
		} else if (!_changeAudit.equals(other._changeAudit))
			return false;
		if (_key == null) {
			if (other._key != null)
				return false;
		} else if (!_key.equals(other._key))
			return false;
		return true;
	}
	
}
