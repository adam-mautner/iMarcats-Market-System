package com.imarcats.interfaces.server.v100.dto.mapping;

import com.imarcats.interfaces.client.v100.dto.InstrumentDto;
import com.imarcats.interfaces.client.v100.dto.types.PagedInstrumentListDto;
import com.imarcats.model.Instrument;
import com.imarcats.model.types.PagedInstrumentList;

public class InstrumentDtoMapping extends DtoMappingBase {
	public static InstrumentDtoMapping INSTANCE = new InstrumentDtoMapping();
	
	public InstrumentDto toDto(Instrument instrument_) {
		return _mapper.map(instrument_, InstrumentDto.class);
	}
	
	public Instrument fromDto(InstrumentDto instrument_) {
		return _mapper.map(instrument_, Instrument.class);
	}
	
	public PagedInstrumentListDto toDto(PagedInstrumentList instrumentList_) {
		return _mapper.map(instrumentList_, PagedInstrumentListDto.class);
	}
	
	public PagedInstrumentList fromDto(PagedInstrumentListDto instrumentList_) {
		return _mapper.map(instrumentList_, PagedInstrumentList.class);
	}
}
