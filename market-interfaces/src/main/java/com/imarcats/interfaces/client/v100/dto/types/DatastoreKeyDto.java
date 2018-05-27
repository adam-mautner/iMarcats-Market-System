package com.imarcats.interfaces.client.v100.dto.types;

import com.imarcats.interfaces.client.v100.dto.MarketModelObjectDto;


/**
 * Key in a Datastore, it represents a ID (Long), Code (String) or Object Key.
 * @author Adam
 */
public class DatastoreKeyDto implements MarketModelObjectDto {

	public static final DatastoreKeyDto NULL = new DatastoreKeyDto(null, true);
	
	// choice 
	private String _codeKey;
	private Long _idKey;
	// end of choice 
	
	// for serialization 
	public DatastoreKeyDto() {
		super();
	}
	
	private DatastoreKeyDto(String codeKey_, boolean allowDatastoreKeyNull_) {
		_codeKey = codeKey_;
		if(_codeKey == null && !allowDatastoreKeyNull_) {
			throw new IllegalArgumentException(NULL.getCodeKey() + " cannot be used as Code Key");
		}
		_idKey = null;
	}
	
	public DatastoreKeyDto(Long idKey_) {
		_idKey = idKey_;
		_codeKey = null;
	}
	
	public DatastoreKeyDto(String codeKey_) {
		this(codeKey_, false);
	}

	public String getCodeKey() {
		return _codeKey;
	}

	public Long getIdKey() {
		return _idKey;
	}

	public void setCodeKey(String codeKey_) {
		_codeKey = codeKey_;
	}

	public void setIdKey(Long idKey_) {
		_idKey = idKey_;
	}

	/**
	 * @return Key Object from this Datastore Key
	 */
	public Object getKey() {
		Object key = null;
		if(getCodeKey() != null) {
			key = getCodeKey();
		} else if(getIdKey() != null) {
			key = getIdKey();
		} 
		
		return key;
	}

	@Override
	public String toString() {
		return "DatastoreKey [_codeKey=" + _codeKey + ", _idKey=" + _idKey + "]";
	}
}
