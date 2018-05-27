package com.imarcats.internal.server.infrastructure.notification.properties;

import com.imarcats.interfaces.client.v100.notification.NotificationType;
import com.imarcats.interfaces.client.v100.notification.PropertyChanges;
import com.imarcats.interfaces.server.v100.dto.mapping.DatastoreKeyDtoMapping;
import com.imarcats.internal.server.infrastructure.notification.NotificationBroker;
import com.imarcats.model.types.DatastoreKey;

/**
 * Implementation for Property Change Broker 
 * @author Adam
 */
public class PropertyChangeBrokerImpl implements PropertyChangeBroker {

	private final NotificationBroker _broker;

	public PropertyChangeBrokerImpl(NotificationBroker broker_) {
		super();
		_broker = broker_;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Long addPropertyChangeListener(DatastoreKey observedObjectKey_,
			Class observedObjectClass_,
			PersistedPropertyChangeListener listener_) {
		return _broker.addListener(
				observedObjectKey_, 
				observedObjectClass_, 
				NotificationType.PropertyChanges,
				null, 
				listener_);
	}

	@Override
	public void removePropertyChangeListener(Long listenerID_) {
		_broker.removeListener(listenerID_);
	}

	@Override
	public void notifyListeners(PropertyChanges[] propertyChanges_) {
		for (PropertyChanges propertyChange : propertyChanges_) {
			
			_broker.notifyListeners(DatastoreKeyDtoMapping.INSTANCE.fromDto(propertyChange.getObjectBeingChanged()), 
					propertyChange.getClassBeingChanged(), 
					NotificationType.PropertyChanges,
					null, 
					propertyChange);	
		}
	}

	@Override
	public NotificationBroker getNotificationBroker() {
		return _broker;
	}
}
