package com.imarcats.interfaces.server.v100.dto.mapping;

import java.util.Date;

import com.imarcats.interfaces.client.v100.dto.types.AuditEntryAction;
import com.imarcats.interfaces.client.v100.dto.types.AuditTrailEntryDto;
import com.imarcats.interfaces.client.v100.dto.types.PagedAuditTrailEntryListDto;
import com.imarcats.interfaces.server.v100.dto.mapping.AuditEntryDtoMapping;
import com.imarcats.model.AuditTrailEntry;
import com.imarcats.model.test.testutils.MarketObjectTestBase;
import com.imarcats.model.types.PagedAuditTrailEntryList;

public class AuditEntryDtoMappingTest extends MarketObjectTestBase {
	
	public void testRoundTripMapping() throws Exception {
		AuditTrailEntry entry = createAuditEntry();
		
		AuditTrailEntryDto auditTrailEntryDto = AuditEntryDtoMapping.INSTANCE.toDto(entry);
		AuditTrailEntry auditTrailEntryMapped = AuditEntryDtoMapping.INSTANCE.fromDto(auditTrailEntryDto); 
		
		assertEquals(entry, auditTrailEntryMapped);
	}

	private AuditTrailEntry createAuditEntry() {
		AuditTrailEntry entry = new AuditTrailEntry();
		entry.setAuditEntryAction(com.imarcats.model.types.AuditEntryAction.Activated);
		entry.setDateTime(new Date());
		entry.setRelatedInformation("Test");
		entry.setObjectTypeStr("Test Str");
		entry.setUserID("testUser"); 
		return entry;
	}
	
	public void testRoundTripListMapping() throws Exception {
		AuditTrailEntry entry = createAuditEntry();
		AuditTrailEntry entry2 = createAuditEntry();
		
		PagedAuditTrailEntryList list = new PagedAuditTrailEntryList();
		list.setAuditTrailEntries(new AuditTrailEntry[] {entry, entry2});
		list.setCursorString("TestCursor");
		list.setMaxNumberOfAuditTrailEntriesOnPage(2); 
		
		PagedAuditTrailEntryListDto listDto = AuditEntryDtoMapping.INSTANCE.toDto(list);
		PagedAuditTrailEntryList listMapped = AuditEntryDtoMapping.INSTANCE.fromDto(listDto); 
		
		assertEquals(list.getCursorString(), listMapped.getCursorString());
		assertEquals(list.getMaxNumberOfAuditTrailEntriesOnPage(), listMapped.getMaxNumberOfAuditTrailEntriesOnPage());
		assertEquals(list.getAuditTrailEntries().length, listMapped.getAuditTrailEntries().length);
		assertEquals(list.getAuditTrailEntries()[0], listMapped.getAuditTrailEntries()[0]);		
		assertEquals(list.getAuditTrailEntries()[1], listMapped.getAuditTrailEntries()[1]);		
	}

}
