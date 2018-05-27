package com.imarcats.internal.server.infrastructure.datastore;

import com.imarcats.model.AuditTrailEntry;
import com.imarcats.model.types.PagedAuditTrailEntryList;

/**
 * Data Access Object (DAO), which stores all the Audit Trail Entries in the System. 
 * Implementation of this interface can be found in the project specific to
 * the platform where the Market Engine is hosted. 
 * 
 * @author Adam
 */
public interface AuditTrailEntryDatastore {

	/**
	 * Creates a Audit Trail Entry 
	 * @param auditTrailEntry_ Audit Trail Entry to be created
	 *
	 */
	public void createAuditTrailEntry(AuditTrailEntry auditTrailEntry_);
	
	/**
	 * Finds All Audit Trail Entries
	 * @param cursorString_ Cursor String for continuing a Paged Query or Null for a new Paged Query
	 * @param maxNumberOfAuditTrailEntryOnPage_ Max Number of Audit Trail Entries on a Page   
	 *
	 * @return Paged Audit Trail Entry List
	 */
	public PagedAuditTrailEntryList findAllAuditTrailEntriesFromCursor(String cursorString_, int maxNumberOfAuditTrailEntryOnPage_);
	
}
