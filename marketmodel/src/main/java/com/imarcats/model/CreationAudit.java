package com.imarcats.model;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.imarcats.model.types.AuditInformation;

/**
 * Creation audit for market objects 
 * @author Adam
 */
@Entity
@Table(name="CREATION_AUDIT")
public class CreationAudit implements MarketModelObject {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * Primary Key of the Creation Audit
	 * Note: No need for accessing this field directly
	 */	
	@Id
	@GeneratedValue 
	@Column(name="ID")
    private Long _id;
	
	/**
	 * Audit Information, like who created the Object and when
	 * Required
	 */
	@Embedded
	private AuditInformation _creationAudit;
	
	public CreationAudit() {
		super();
	}

	public CreationAudit(AuditInformation creationAudit_) {
		super();
		_creationAudit = creationAudit_;
	}

	public AuditInformation getCreationAudit() {
		return _creationAudit;
	}
	
	public void setCreationAudit(AuditInformation creationAudit_) {
		_creationAudit = creationAudit_;
	}

	@Override
	public String toString() {
		return "CreationAudit [_creationAudit=" + _creationAudit + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((_creationAudit == null) ? 0 : _creationAudit.hashCode());
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
		CreationAudit other = (CreationAudit) obj;
		if (_creationAudit == null) {
			if (other._creationAudit != null)
				return false;
		} else if (!_creationAudit.equals(other._creationAudit))
			return false;
		if (_id == null) {
			if (other._id != null)
				return false;
		} else if (!_id.equals(other._id))
			return false;
		return true;
	}
	
}
