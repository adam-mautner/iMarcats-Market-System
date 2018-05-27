package com.imarcats.interfaces.client.v100.notification;

import java.util.Arrays;
import java.util.Date;

import com.imarcats.interfaces.client.v100.dto.types.DatastoreKeyDto;
import com.imarcats.interfaces.client.v100.dto.types.PropertyChangeDto;

/**
 * Information about Property Changes 
 * @author Adam
 */
public class PropertyChanges extends ListenerCallUserParameters {

	private static final long serialVersionUID = 1L;
	
	private final DatastoreKeyDto _objectBeingChanged;
	
	/**
	 * For Order it is the Market Code, for Market it is Null
	 */
	private final DatastoreKeyDto _parentObject;
	
	@SuppressWarnings("unchecked")
	private final Class _classBeingChanged;
	private final PropertyChangeDto[] _changes;
	private final long _changeTimestamp;
	private final ObjectVersion _objectVersion;
	private final String _objectOwner;
	
	/**
	 * Origin of the System, User or Null - for now only provided for Order State changes
	 */
	private final ChangeOrigin _changeOrigin;

	@SuppressWarnings("unchecked")
	public PropertyChanges(DatastoreKeyDto objectBeingChanged_, Class classBeingChanged_, DatastoreKeyDto parentObject_,
			PropertyChangeDto[] changes_, Date changeTimestamp_, ObjectVersion objectVersion_, String objectOwner_, ChangeOrigin changeOrigin_) { 
		super();
		_objectBeingChanged = objectBeingChanged_;
		_parentObject = parentObject_; 
		_classBeingChanged = classBeingChanged_;
		_objectVersion = objectVersion_;
		_objectOwner = objectOwner_;
		_changeOrigin = changeOrigin_; 
		
		_changes = changes_;
		
		_changeTimestamp = changeTimestamp_.getTime();
	}
	
	public DatastoreKeyDto getObjectBeingChanged() {
		return _objectBeingChanged;
	}
	public PropertyChangeDto[] getChanges() {
		return _changes;
	}
	@SuppressWarnings("unchecked")
	public Class getClassBeingChanged() {
		return _classBeingChanged;
	}

	public Date getChangeTimestamp() {
		return new Date(_changeTimestamp);
	}

	public ObjectVersion getObjectVersion() {
		return _objectVersion;
	}

	public DatastoreKeyDto getParentObject() {
		return _parentObject;
	}

	public String getObjectOwner() {
		return _objectOwner;
	}

	@Override
	public String toString() {
		return "PropertyChanges [_changeTimestamp=" + _changeTimestamp
				+ ", _changes=" + Arrays.toString(_changes)
				+ ", _classBeingChanged=" + _classBeingChanged
				+ ", _objectBeingChanged=" + _objectBeingChanged
				+ ", _objectOwner=" + _objectOwner + ", _objectVersion="
				+ _objectVersion + ", _parentObject=" + _parentObject + "]";
	}

	public ChangeOrigin getChangeOrigin() {
		return _changeOrigin;
	}
}
