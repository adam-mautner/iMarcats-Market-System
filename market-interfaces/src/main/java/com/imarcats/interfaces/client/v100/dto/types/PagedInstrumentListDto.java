package com.imarcats.interfaces.client.v100.dto.types;

import java.io.Serializable;
import java.util.Arrays;

import com.imarcats.interfaces.client.v100.dto.InstrumentDto;

/**
 * Paged list of Instruments
 * @author Adam
 */
public class PagedInstrumentListDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private InstrumentDto[] _instruments;
	private String _cursorString;
	private int _maxNumberOfInstrumentsOnPage;
	
	public InstrumentDto[] getInstruments() {
		return _instruments;
	}
	public void setInstruments(InstrumentDto[] instruments_) {
		_instruments = instruments_;
	}
	public String getCursorString() {
		return _cursorString;
	}
	public void setCursorString(String cursorString_) {
		_cursorString = cursorString_;
	}
	public int getMaxNumberOfInstrumentsOnPage() {
		return _maxNumberOfInstrumentsOnPage;
	}
	public void setMaxNumberOfInstrumentsOnPage(int maxNumberOfInstrumentsOnPage_) {
		_maxNumberOfInstrumentsOnPage = maxNumberOfInstrumentsOnPage_;
	}
	@Override
	public String toString() {
		return "PagedInstrumentList [_cursorString=" + _cursorString
				+ ", _instruments=" + Arrays.toString(_instruments)
				+ ", _maxNumberOfInstrumentsOnPage="
				+ _maxNumberOfInstrumentsOnPage + "]";
	}
	
}
