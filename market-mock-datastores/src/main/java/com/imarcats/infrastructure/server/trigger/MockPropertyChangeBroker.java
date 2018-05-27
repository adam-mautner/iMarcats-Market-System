package com.imarcats.infrastructure.server.trigger;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.imarcats.interfaces.client.v100.dto.types.DatastoreKeyDto;
import com.imarcats.interfaces.client.v100.dto.types.PropertyChangeDto;
import com.imarcats.interfaces.client.v100.notification.ChangeOrigin;
import com.imarcats.interfaces.client.v100.notification.ListenerCallUserParameters;
import com.imarcats.interfaces.client.v100.notification.NotificationType;
import com.imarcats.interfaces.client.v100.notification.ObjectVersion;
import com.imarcats.interfaces.client.v100.notification.PropertyChanges;
import com.imarcats.internal.server.infrastructure.marketdata.MarketDataSessionImpl;
import com.imarcats.internal.server.infrastructure.notification.NotificationBroker;
import com.imarcats.internal.server.infrastructure.notification.PersistedListener;
import com.imarcats.internal.server.infrastructure.notification.properties.PropertyChangeBroker;
import com.imarcats.internal.server.infrastructure.notification.properties.PersistedPropertyChangeListener;
import com.imarcats.internal.server.infrastructure.notification.properties.PropertyChangeSessionImpl;
import com.imarcats.internal.server.infrastructure.notification.trades.TradeNotificationSessionImpl;
import com.imarcats.internal.server.infrastructure.testutils.MockDatastoresBase;
import com.imarcats.model.test.testutils.MockIdentityGenerator;
import com.imarcats.model.types.DatastoreKey;

public class MockPropertyChangeBroker implements PropertyChangeBroker {

	private final List<PersistedPropertyChangeListener> _listenersToBeAdded = new ArrayList<PersistedPropertyChangeListener>();
	private final List<PersistedPropertyChangeListener> _listenersToBeRemoved = new ArrayList<PersistedPropertyChangeListener>();
	private final List<PersistedPropertyChangeListener> _listeners = new ArrayList<PersistedPropertyChangeListener>();
	
	private final Map<Long, PersistedPropertyChangeListener> _keyToListener = new HashMap<Long, PersistedPropertyChangeListener>(); 
	
	private final MockDatastoresBase _datastores;
	
	public MockPropertyChangeBroker(MockDatastoresBase datastores_) {
		super();
		_datastores = datastores_;
	}
	 
	@Override
	public void notifyListeners(PropertyChanges[] propertyChanges_) {
		for (PropertyChanges propertyChange : propertyChanges_) {
			propertyUpdated(propertyChange.getObjectBeingChanged(), 
					propertyChange.getClassBeingChanged(), propertyChange.getParentObject(), 
					propertyChange.getChanges(), 
					propertyChange.getChangeTimestamp(), propertyChange.getObjectVersion(), 
					propertyChange.getObjectOwner(), propertyChange.getChangeOrigin());
		}
	}

	@SuppressWarnings("unchecked")
	public void propertyUpdated(DatastoreKeyDto objectKey_, Class objectClass_, DatastoreKeyDto parentKey_, 
			PropertyChangeDto[] value_, 
			Date changeTimestamp_, ObjectVersion version_, String owner_, ChangeOrigin changeOrigin_) {
		updateListeners();
		
		
		
		MockMarketDataSource marketDataSource = new MockMarketDataSource(_datastores);
		MarketDataSessionImpl marketDataSessionImpl = new MarketDataSessionImpl(marketDataSource);
		
		PropertyChangeSessionImpl propertyChangeSession = new PropertyChangeSessionImpl(this); 
		TradeNotificationSessionImpl tradeNotificationSessionImpl = new TradeNotificationSessionImpl(new MockTradeNotificationBroker());
		
		for (PersistedPropertyChangeListener listener : _listeners) {
			listener.propertyChanged(new PropertyChanges(objectKey_, objectClass_, parentKey_, value_, changeTimestamp_, version_, owner_, changeOrigin_), new MockListenerContext(_datastores, marketDataSessionImpl, 
					propertyChangeSession, tradeNotificationSessionImpl));
		}
	}
	
	public boolean hasPropertyChangeListeners() {
		updateListeners();
		return !_listeners.isEmpty();
	}
	
	private void updateListeners() {
		_listenersToBeAdded.stream().filter(addedListener -> !_listeners.contains(addedListener)).forEach(_listeners::add); 
		_listenersToBeAdded.clear();
		
		_listenersToBeRemoved.stream().filter(removedListeners -> _listeners.contains(removedListeners)).forEach(_listeners::remove); 
		_listenersToBeRemoved.clear();
	} 

	
	private void removeListener(Long listenerID_) {
		PersistedPropertyChangeListener remove = _keyToListener.get(listenerID_); 
		if(remove != null) {
			_listenersToBeRemoved.add(remove);
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Long addPropertyChangeListener(DatastoreKey observedObjectKey_,
			Class observedObjectClass_,
			PersistedPropertyChangeListener listener_) {
		return addListener(observedObjectKey_,
				observedObjectClass_,
				listener_);
	}
	
	@SuppressWarnings("unchecked")
	private Long addListener(DatastoreKey observedObjectKey_,
			Class observedObjectClass_,
			PersistedPropertyChangeListener listener_) {
		Long id = MockIdentityGenerator.getId();
		
		listener_.setObservedObjectKey(observedObjectKey_);
		listener_.setObservedClassName(observedObjectClass_.getName());
		
		_listenersToBeAdded.add(listener_);
		_keyToListener.put(id, listener_);
		return id;
	} 

	@Override
	public void removePropertyChangeListener(Long listenerID_) {
		removeListener(listenerID_);
	}

	@Override
	public NotificationBroker getNotificationBroker() {
		return new NotificationBroker() {

			@SuppressWarnings("unchecked")
			@Override
			public Long addListener(
					DatastoreKey observedObject_,
					Class observedObjectClass_,
					NotificationType notificationType_,
					String filterString_,
					PersistedListener listener_) {
				// does nothing
				return null;
			}

			@SuppressWarnings("unchecked")
			@Override
			public void notifyListeners(DatastoreKey observedObject_,
					Class observedObjectClass_,
					NotificationType notificationType_, 
					String filterString_,
					ListenerCallUserParameters parameters_) {
				// does nothing
				
			}

			@SuppressWarnings("unchecked")
			@Override
			public void removeAllListeners(
					DatastoreKey observedObjectKey_,
					Class observedObjectClass_) {
				// does nothing
			}

			@Override
			public void removeListener(
					Long listenerKey_) {
				// does nothing
			}
			
		};
	}

}
