package com.imarcats.interfaces.server.v100.dto.mapping;

import com.imarcats.interfaces.client.v100.dto.types.AuditTrailEntryDto;
import com.imarcats.interfaces.client.v100.dto.types.PagedAuditTrailEntryListDto;
import com.imarcats.model.AuditTrailEntry;
import com.imarcats.model.types.PagedAuditTrailEntryList;

public class AuditEntryDtoMapping extends DtoMappingBase {
	
	public static AuditEntryDtoMapping INSTANCE = new AuditEntryDtoMapping();
	
	public AuditTrailEntryDto toDto(AuditTrailEntry entry_) {
		return _mapper.map(entry_, AuditTrailEntryDto.class);
	}
	
	public AuditTrailEntry fromDto(AuditTrailEntryDto entry_) {
		return _mapper.map(entry_, AuditTrailEntry.class);
	}
	
	public PagedAuditTrailEntryListDto toDto(PagedAuditTrailEntryList entryList_) {
		return _mapper.map(entryList_, PagedAuditTrailEntryListDto.class);
	}
	
	public PagedAuditTrailEntryList fromDto(PagedAuditTrailEntryListDto entryList_) {
		return _mapper.map(entryList_, PagedAuditTrailEntryList.class);
	}
}
