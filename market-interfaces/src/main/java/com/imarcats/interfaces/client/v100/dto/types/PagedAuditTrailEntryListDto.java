package com.imarcats.interfaces.client.v100.dto.types;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Paged list of Audit Trail Entries
 * @author Adam
 */
public class PagedAuditTrailEntryListDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private AuditTrailEntryDto[] _auditTrailEntries;
	private String _cursorString;
	private int _maxNumberOfAuditTrailEntriesOnPage;
	
	public AuditTrailEntryDto[] getAuditTrailEntries() {
		return _auditTrailEntries;
	}
	public void setAuditTrailEntries(AuditTrailEntryDto[] auditTrailEntries_) {
		_auditTrailEntries = auditTrailEntries_;
	}
	public String getCursorString() {
		return _cursorString;
	}
	public void setCursorString(String cursorString_) {
		_cursorString = cursorString_;
	}
	public int getMaxNumberOfAuditTrailEntriesOnPage() {
		return _maxNumberOfAuditTrailEntriesOnPage;
	}
	public void setMaxNumberOfAuditTrailEntriesOnPage(int maxNumberOfAuditTrailEntriesOnPage_) {
		_maxNumberOfAuditTrailEntriesOnPage = maxNumberOfAuditTrailEntriesOnPage_;
	}
	@Override
	public String toString() {
		return "PagedAuditTrailEntryList [_auditTrailEntries="
				+ Arrays.toString(_auditTrailEntries) + ", _cursorString="
				+ _cursorString + ", _maxNumberOfAuditTrailEntriesOnPage="
				+ _maxNumberOfAuditTrailEntriesOnPage + "]";
	}
	
}
