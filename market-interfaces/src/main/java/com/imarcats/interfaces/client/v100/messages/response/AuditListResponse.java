package com.imarcats.interfaces.client.v100.messages.response;

import com.imarcats.interfaces.client.v100.dto.types.PagedAuditTrailEntryListDto;
import com.imarcats.interfaces.client.v100.messages.MessageBase;

/**
 * Response with Audit List
 * @author Adam
 *
 */
public class AuditListResponse extends MessageBase {

	private static final long serialVersionUID = 1L;
	
	private PagedAuditTrailEntryListDto _auditTrailEntryList;

	public PagedAuditTrailEntryListDto getAuditTrailEntryList() {
		return _auditTrailEntryList;
	}

	public void setAuditTrailEntryList(PagedAuditTrailEntryListDto auditTrailEntryList_) {
		_auditTrailEntryList = auditTrailEntryList_;
	}

}