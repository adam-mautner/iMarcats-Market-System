package com.imarcats.model.types;

import java.io.Serializable;

/**
 * Key in a Datastore, it represents a ID (Long), Code (String) or Object Key.
 * @author Adam
 */
public class DatastoreKey implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final DatastoreKey NULL = new DatastoreKey(null, true);
	
	// choice 
	private String _codeKey;
	private Long _idKey;
	// end of choice 
	
	// for serialization 
	public DatastoreKey() {
		super();
	}
	
	private DatastoreKey(String codeKey_, boolean allowDatastoreKeyNull_) {
		_codeKey = codeKey_;
		if(_codeKey == null && !allowDatastoreKeyNull_) {
			throw new IllegalArgumentException(NULL.getCodeKey() + " cannot be used as Code Key");
		}
		_idKey = null;
	}
	
	public DatastoreKey(Long idKey_) {
		_idKey = idKey_;
		_codeKey = null;
	}
	
	public DatastoreKey(String codeKey_) {
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
	public int hashCode() {
		int hashCode = 0;
		if(getCodeKey() != null) {
			hashCode = getCodeKey().hashCode();
		} else if(getIdKey() != null) {
			hashCode = getIdKey().hashCode();
		}  
		return hashCode;
	}

	@Override
	public boolean equals(Object other_) {
		boolean equals = false;
		if(other_ == this) {
			equals = true;
		} else if(other_ instanceof DatastoreKey) {
			DatastoreKey otherKey = (DatastoreKey) other_;
			
			if(getCodeKey() != null) {
				equals = getCodeKey().equals(otherKey.getCodeKey());
			} else if(getIdKey() != null) {
				equals = getIdKey().equals(otherKey.getIdKey());
			} 
		}
		
		return equals;
	}

	@Override
	public String toString() {
		return "DatastoreKey [_codeKey=" + _codeKey + ", _idKey=" + _idKey + "]";
	}
}
