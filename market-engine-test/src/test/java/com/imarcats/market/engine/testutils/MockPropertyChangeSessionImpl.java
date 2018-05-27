package com.imarcats.market.engine.testutils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.imarcats.interfaces.client.v100.dto.types.DatastoreKeyDto;
import com.imarcats.interfaces.client.v100.dto.types.PropertyChangeDto;
import com.imarcats.interfaces.client.v100.notification.ChangeOrigin;
import com.imarcats.interfaces.client.v100.notification.ObjectVersion;
import com.imarcats.interfaces.client.v100.notification.PropertyChanges;
import com.imarcats.internal.server.infrastructure.notification.properties.PropertyChangeBroker;
import com.imarcats.internal.server.infrastructure.notification.properties.PropertyChangeSessionImpl;

public class MockPropertyChangeSessionImpl extends PropertyChangeSessionImpl {

	private final List<PropertyChanges> _propertyChanges = new ArrayList<PropertyChanges>();

	public MockPropertyChangeSessionImpl(PropertyChangeBroker propertyChangeBroker_) {
		super(propertyChangeBroker_);
	}

	public void commit() {
		// _propertyChangeBroker.notifyListeners(getPropertyChanges());
		// clear the property changes already sent to prevent duplications
		_propertyChanges.clear();
	}
	
	public void rollback() {
		_propertyChanges.clear();
	}
	
	public PropertyChanges[] getPropertyChanges() {
		return _propertyChanges.toArray(new PropertyChanges[_propertyChanges.size()]);
	}

	protected void notify(Class objectClass_, Date changeTimestamp_,
			ObjectVersion version_, String owner_, ChangeOrigin changeOrigin_,
			PropertyChangeDto[] propertyChangeDtos,
			DatastoreKeyDto parentKeyDto, DatastoreKeyDto objectKeyDto) {
		_propertyChanges.add(new PropertyChanges(objectKeyDto, objectClass_, parentKeyDto, 
				propertyChangeDtos, changeTimestamp_, version_, owner_, changeOrigin_));
	}
}
