package com.imarcats.model;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.imarcats.model.types.AuditInformation;

/**
 * Change audit for market objects 
 * @author Adam
 */
@Entity
@Table(name="CHANGE_AUDIT")
public class ChangeAudit implements MarketModelObject {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * Primary Key of the Change Audit
	 * Note: No need for accessing this field directly
	 */	
	@Id
	@GeneratedValue 
	@Column(name="ID")
    private Long _id;
	
	/**
	 * Audit Information, like who changed the Object and when
	 * Required
	 */
    @Embedded
	private AuditInformation _changeAudit;
	
	public ChangeAudit() {
		super();
	}

	public ChangeAudit(AuditInformation changeAudit_) {
		super();
		_changeAudit = changeAudit_;
	}

	public AuditInformation getChangeAudit() {
		return _changeAudit;
	}
	
	public void setChangeAudit(AuditInformation audit_) {
		_changeAudit = audit_;
	}

	@Override
	public String toString() {
		return "ChangeAudit [_id=" + _id + ", _changeAudit=" + _changeAudit
				+ "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((_changeAudit == null) ? 0 : _changeAudit.hashCode());
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
		ChangeAudit other = (ChangeAudit) obj;
		if (_changeAudit == null) {
			if (other._changeAudit != null)
				return false;
		} else if (!_changeAudit.equals(other._changeAudit))
			return false;
		if (_id == null) {
			if (other._id != null)
				return false;
		} else if (!_id.equals(other._id))
			return false;
		return true;
	}
}
