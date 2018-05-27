package com.imarcats.interfaces.server.v100.dto.mapping;

import com.imarcats.interfaces.client.v100.dto.InstrumentDto;
import com.imarcats.interfaces.client.v100.dto.types.PagedInstrumentListDto;
import com.imarcats.interfaces.server.v100.dto.mapping.InstrumentDtoMapping;
import com.imarcats.model.Instrument;
import com.imarcats.model.test.testutils.MarketObjectTestBase;
import com.imarcats.model.types.PagedInstrumentList;

public class InstrumentDtoMappingTest extends MarketObjectTestBase {
	public void testRoundTripMapping() throws Exception {
		Instrument instrument = createInstrument("TestInstrument");
		instrument.setVersionNumber(10L);
		
		InstrumentDto instrumentDto = InstrumentDtoMapping.INSTANCE.toDto(instrument);
		Instrument instrumentMapped = InstrumentDtoMapping.INSTANCE.fromDto(instrumentDto); 
		
		checkInstrument(instrument, instrumentMapped);
	}

	private void checkInstrument(Instrument instrument, Instrument instrumentMapped) {
		assertEqualsInstrument(instrument, instrumentMapped);
		assertEquals(instrument.getLastUpdateTimestamp(), instrumentMapped.getLastUpdateTimestamp());
		assertEquals(instrument.getVersionNumber(), instrumentMapped.getVersionNumber());
	}
	
	public void testRoundTripListMapping() throws Exception {
		Instrument instrument = createInstrument("Test1");
		Instrument instrument2 = createInstrument("Test2");
		
		PagedInstrumentList list = new PagedInstrumentList();
		list.setInstruments(new Instrument[] {instrument, instrument2});
		list.setCursorString("TestCursor");
		list.setMaxNumberOfInstrumentsOnPage(2); 
		
		PagedInstrumentListDto listDto = InstrumentDtoMapping.INSTANCE.toDto(list);
		PagedInstrumentList listMapped = InstrumentDtoMapping.INSTANCE.fromDto(listDto); 
		
		assertEquals(list.getCursorString(), listMapped.getCursorString());
		assertEquals(list.getMaxNumberOfInstrumentsOnPage(), listMapped.getMaxNumberOfInstrumentsOnPage());
		assertEquals(list.getInstruments().length, listMapped.getInstruments().length);
		checkInstrument(list.getInstruments()[0], listMapped.getInstruments()[0]);		
		checkInstrument(list.getInstruments()[1], listMapped.getInstruments()[1]);		
	}
}
