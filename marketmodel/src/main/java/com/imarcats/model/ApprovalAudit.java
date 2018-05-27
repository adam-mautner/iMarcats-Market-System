package com.imarcats.model;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.imarcats.model.types.AuditInformation;

/**
 * Approval audit for market objects 
 * @author Adam
 */
@Entity
@Table(name="APPROVAL_AUDIT")
public class ApprovalAudit implements MarketModelObject {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * Primary Key of the Approval Audit
	 * Note: No need for accessing this field directly
	 */	
	@Id
	@GeneratedValue 
	@Column(name="ID")
    private Long _id;
	
	/**
	 * Audit Information, like who approved the Object and when
	 * Required
	 */
	@Embedded
	private AuditInformation _approvalAudit;
	
	public ApprovalAudit() {
		super();
	}

	public ApprovalAudit(AuditInformation approvalAudit_) {
		super();
		_approvalAudit = approvalAudit_;
	}

	public AuditInformation getApprovalAudit() {
		return _approvalAudit;
	}
	
	public void setApprovalAudit(AuditInformation audit_) {
		_approvalAudit = audit_;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((_approvalAudit == null) ? 0 : _approvalAudit.hashCode());
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
		ApprovalAudit other = (ApprovalAudit) obj;
		if (_approvalAudit == null) {
			if (other._approvalAudit != null)
				return false;
		} else if (!_approvalAudit.equals(other._approvalAudit))
			return false;
		if (_id == null) {
			if (other._id != null)
				return false;
		} else if (!_id.equals(other._id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ApprovalAudit [_id=" + _id + ", _approvalAudit="
				+ _approvalAudit + "]";
	}
	
}
