package com.imarcats.internal.server.infrastructure.datastore;

import com.imarcats.model.Instrument;
import com.imarcats.model.types.ActivationStatus;
import com.imarcats.model.types.PagedInstrumentList;
import com.imarcats.model.types.UnderlyingType;

/**
 * Data Access Object (DAO), which stores all the Instruments in the System. 
 * Implementation of this interface can be found in the project specific to
 * the platform where the Market Engine is hosted. 
 * 
 * @author Adam
 */
public interface InstrumentDatastore {

	/**
	 * Creates an Instrument
	 * @param instrument_ Instrument to be Created 
	 *
	 * @return Instrument Code
	 */
	public String createInstrument(Instrument instrument_);
	
	/**
	 * Explicitly updates Instrument in the Datastore with instance provided 
	 * @param changedInstrumentModel_ Instrument to be updated 
	 *
	 */
	public Instrument updateInstrument(Instrument changedInstrumentModel_);
	
	/**
	 * Finds the Instruments by Activation Status
	 * @param activationStatus_ Activation Status
	 * @param cursorString_ Cursor String for continuing a Paged Query or Null for a new Paged Query
	 * @param maxNumberOfInstrumentsOnPage_ Max Number of Instruments on a Page   
	 *
	 * @return Page Instrument List
	 */
	public PagedInstrumentList findInstrumentsFromCursorByActivationStatus(ActivationStatus activationStatus_, String cursorString_, int maxNumberOfInstrumentsOnPage_);

	/**
	 * Finds the Instruments for Product
	 * @param underlyingCode_ Underlying Code 
	 * @param underlyingType_ Underlying Type
	 * @param cursorString_ Cursor String for continuing a Paged Query or Null for a new Paged Query
	 * @param maxNumberOfInstrumentsOnPage_ Max Number of Instruments on a Page   
	 *
	 * @return Page Instrument List
	 */
	public PagedInstrumentList findInstrumentsFromCursorByUnderlying(String underlyingCode_, UnderlyingType underlyingType_, String cursorString_, int maxNumberOfInstrumentsOnPage_);

	/**
	 * Finds the Instruments for Product
	 * @param underlyingCode_ Underlying Code 
	 * @param underlyingType_ Underlying Type
	 *
	 * @return Instrument List
	 */
	public Instrument[] findInstrumentsByUnderlying(String underlyingCode_, UnderlyingType underlyingType_);

	/**
	 * Finds the Instruments for Product
	 * @param assetClassName_ Asset Class Name
	 * @param cursorString_ Cursor String for continuing a Paged Query or Null for a new Paged Query
	 * @param maxNumberOfInstrumentsOnPage_ Max Number of Instruments on a Page   
	 *
	 * @return Page Instrument List
	 */
	public PagedInstrumentList findInstrumentsFromCursorByAssetClass(String assetClassName_, String cursorString_, int maxNumberOfInstrumentsOnPage_);
	
	/**
	 * Finds All Instruments
	 * @param cursorString_ Cursor String for continuing a Paged Query or Null for a new Paged Query
	 * @param maxNumberOfInstrumentsOnPage_ Max Number of Instruments on a Page   
	 *
	 * @return Page Instrument List
	 */
	public PagedInstrumentList findAllInstrumentsFromCursor(String cursorString_, int maxNumberOfInstrumentsOnPage_);

	/**
	 * Finds the Instrument by its Code (Primary Key)
	 * @param instrumentCode_ Code (Primary Key) of the Instrument 
	 *
	 * @return Instrument 
	 */
	public Instrument findInstrumentByCode(String instrumentCode_);
	
	/**
	 * Deletes an Instrument 
	 * @param instrumentCode_ Instrument to be Deleted
	 *
	 */
	public void deleteInstrument(String instrumentCode_);
}
