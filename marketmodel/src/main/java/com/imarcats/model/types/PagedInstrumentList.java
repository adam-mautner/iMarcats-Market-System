package com.imarcats.model.types;

import java.io.Serializable;
import java.util.Arrays;

import com.imarcats.model.Instrument;

/**
 * Paged list of Instruments
 * @author Adam
 */
public class PagedInstrumentList implements Serializable {

	private static final long serialVersionUID = 1L;

	private Instrument[] _instruments;
	private String _cursorString;
	private int _maxNumberOfInstrumentsOnPage;
	
	public Instrument[] getInstruments() {
		return _instruments;
	}
	public void setInstruments(Instrument[] instruments_) {
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
