package com.imarcats.model;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.imarcats.model.types.AuditInformation;

/**
 * Rollover audit for market objects 
 * @author Adam
 */
@Entity
@Table(name="ROLLOVER_AUDIT")
public class RolloverAudit implements MarketModelObject {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * Primary Key of the Rollover Audit
	 * Note: No need for accessing this field directly
	 */	
	@Id
	@GeneratedValue 
	@Column(name="ID")
    private Long _id;
	
	/**
	 * Audit Information, like who rolled over the Object and when
	 * Required
	 */
	@Embedded
	private AuditInformation _rolloverAudit;
	
	public RolloverAudit(AuditInformation rolloverAudit_) {
		super();
		_rolloverAudit = rolloverAudit_;
	}

	public RolloverAudit() {
		super();
	}

	public AuditInformation getRolloverAudit() {
		return _rolloverAudit;
	}
	
	public void setRolloverAudit(AuditInformation audit_) {
		_rolloverAudit = audit_;
	}

	@Override
	public String toString() {
		return "RolloverAudit [_id=" + _id + ", _rolloverAudit="
				+ _rolloverAudit + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((_id == null) ? 0 : _id.hashCode());
		result = prime * result
				+ ((_rolloverAudit == null) ? 0 : _rolloverAudit.hashCode());
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
		RolloverAudit other = (RolloverAudit) obj;
		if (_id == null) {
			if (other._id != null)
				return false;
		} else if (!_id.equals(other._id))
			return false;
		if (_rolloverAudit == null) {
			if (other._rolloverAudit != null)
				return false;
		} else if (!_rolloverAudit.equals(other._rolloverAudit))
			return false;
		return true;
	}
}
