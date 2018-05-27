package com.imarcats.internal.server.infrastructure.notification.properties;

import java.util.Date;

import com.imarcats.interfaces.client.v100.dto.types.DatastoreKeyDto;
import com.imarcats.interfaces.client.v100.dto.types.PropertyChangeDto;
import com.imarcats.interfaces.client.v100.notification.ChangeOrigin;
import com.imarcats.interfaces.client.v100.notification.ObjectVersion;
import com.imarcats.interfaces.client.v100.notification.PropertyChanges;
import com.imarcats.interfaces.server.v100.dto.mapping.DatastoreKeyDtoMapping;
import com.imarcats.interfaces.server.v100.dto.mapping.PropertyDtoMapping;
import com.imarcats.model.mutators.PropertyChange;
import com.imarcats.model.types.DatastoreKey;


/**
 * Implementation of Property Change Session
 * @author Adam
 *
 */
public class PropertyChangeSessionImpl implements PropertyChangeSession {

	private final PropertyChangeBroker _propertyChangeBroker;

	public PropertyChangeSessionImpl(PropertyChangeBroker propertyChangeBroker_) {
		super();
		_propertyChangeBroker = propertyChangeBroker_;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void recordPropertyChanges(DatastoreKey objectKey_,
			Class objectClass_, DatastoreKey parentKey_, PropertyChange[] propertyChanges_, 
			Date changeTimestamp_, ObjectVersion version_, String owner_) {
		recordPropertyChanges(objectKey_, objectClass_, parentKey_, 
				propertyChanges_, changeTimestamp_, version_, owner_, null);	
	}

	@SuppressWarnings("unchecked")
	@Override
	public void recordPropertyChanges(DatastoreKey objectKey_, 
			Class objectClass_, DatastoreKey parentKey_, PropertyChange[] propertyChanges_, 
			Date changeTimestamp_, ObjectVersion version_, String owner_, ChangeOrigin changeOrigin_) {
		PropertyChangeDto[] propertyChangeDtos = PropertyDtoMapping.INSTANCE.toDto(propertyChanges_);
		DatastoreKeyDto parentKeyDto = DatastoreKeyDtoMapping.INSTANCE.toDto(parentKey_);
		DatastoreKeyDto objectKeyDto = DatastoreKeyDtoMapping.INSTANCE.toDto(objectKey_);
		notify(objectClass_, changeTimestamp_, version_, owner_, changeOrigin_,
				propertyChangeDtos, parentKeyDto, objectKeyDto);	
	}

	protected void notify(Class objectClass_, Date changeTimestamp_,
			ObjectVersion version_, String owner_, ChangeOrigin changeOrigin_,
			PropertyChangeDto[] propertyChangeDtos,
			DatastoreKeyDto parentKeyDto, DatastoreKeyDto objectKeyDto) {
		_propertyChangeBroker.notifyListeners(new PropertyChanges[] { new PropertyChanges(objectKeyDto, objectClass_, parentKeyDto, 
				propertyChangeDtos, changeTimestamp_, version_, owner_, changeOrigin_) });
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Long addPropertyChangeListener(DatastoreKey observedObjectKey_,
			Class observedObjectClass_,
			PersistedPropertyChangeListener listener_) {
		return _propertyChangeBroker.addPropertyChangeListener(observedObjectKey_, observedObjectClass_, listener_);
	}

	@Override
	public void removePropertyChangeListener(Long listenerID_) {
		_propertyChangeBroker.removePropertyChangeListener(listenerID_);	
	}

	@Override
	public PropertyChangeBroker getPropertyChangeBroker() {
		return _propertyChangeBroker;
	}
	
}
