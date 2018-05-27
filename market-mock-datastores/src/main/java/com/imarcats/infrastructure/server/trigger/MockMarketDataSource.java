package com.imarcats.infrastructure.server.trigger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.imarcats.interfaces.client.v100.dto.types.MarketDataType;
import com.imarcats.interfaces.client.v100.notification.ListenerCallUserParameters;
import com.imarcats.interfaces.client.v100.notification.MarketDataChange;
import com.imarcats.interfaces.client.v100.notification.NotificationType;
import com.imarcats.interfaces.server.v100.dto.mapping.MarketDtoMapping;
import com.imarcats.internal.server.infrastructure.marketdata.MarketDataSession;
import com.imarcats.internal.server.infrastructure.marketdata.MarketDataSource;
import com.imarcats.internal.server.infrastructure.marketdata.MarketDataSessionImpl;
import com.imarcats.internal.server.infrastructure.marketdata.PersistedMarketDataChangeListener;
import com.imarcats.internal.server.infrastructure.notification.NotificationBroker;
import com.imarcats.internal.server.infrastructure.notification.PersistedListener;
import com.imarcats.internal.server.infrastructure.notification.properties.PersistedPropertyChangeListener;
import com.imarcats.internal.server.infrastructure.notification.properties.PropertyChangeSession;
import com.imarcats.internal.server.infrastructure.notification.properties.PropertyChangeSessionImpl;
import com.imarcats.internal.server.infrastructure.notification.trades.TradeNotificationSession;
import com.imarcats.internal.server.infrastructure.notification.trades.TradeNotificationSessionImpl;
import com.imarcats.internal.server.infrastructure.testutils.MockDatastoresBase;
import com.imarcats.internal.server.interfaces.order.OrderManagementContext;
import com.imarcats.model.Market;
import com.imarcats.model.test.testutils.MockIdentityGenerator;
import com.imarcats.model.types.DatastoreKey;
import com.imarcats.model.types.QuoteAndSize;

public class MockMarketDataSource implements MarketDataSource {

	private final List<PersistedMarketDataChangeListener> _listenersToBeAdded = new ArrayList<PersistedMarketDataChangeListener>();
	private final List<PersistedMarketDataChangeListener> _listenersToBeRemoved = new ArrayList<PersistedMarketDataChangeListener>();
	private final List<PersistedMarketDataChangeListener> _listeners = new ArrayList<PersistedMarketDataChangeListener>();
	
	private final Map<Long, PersistedMarketDataChangeListener> _keyToListener = new HashMap<Long, PersistedMarketDataChangeListener>(); 
	
	private final MockDatastoresBase _datastores;
	
	public MockMarketDataSource(MockDatastoresBase datastores_) {
		super();
		_datastores = datastores_;
	}
	 
	@Override
	public void notifyListeners(MarketDataChange[] marketDataChanges_) {
		for (MarketDataChange marketDataChange : marketDataChanges_) {
			marketDataUpdated(marketDataChange.getMarketCode(), 
					marketDataChange.getChangeType(), MarketDtoMapping.INSTANCE.fromDto(marketDataChange.getNewQuoteAndSize()), marketDataChange.getHasHiddenOrders());
		}
	}
	
	public void marketDataUpdated(String marketCode_, 
			MarketDataType marketDataType_, QuoteAndSize value_, Boolean hasHiddenOrders_) {
		updateListeners();
		
		
		
		MarketDataSessionImpl marketDataSession = new MarketDataSessionImpl(this);
		MockPropertyChangeBroker propertyChangeBroker = new MockPropertyChangeBroker(_datastores);
		PropertyChangeSessionImpl propertyChangeSessionImpl = new PropertyChangeSessionImpl(propertyChangeBroker);
		TradeNotificationSessionImpl tradeNotificationSessionImpl = new TradeNotificationSessionImpl(new MockTradeNotificationBroker());
		
		marketDataUpdated(marketCode_, marketDataType_, value_, hasHiddenOrders_, 
				marketDataSession, propertyChangeSessionImpl, tradeNotificationSessionImpl);
	}

	public void marketDataUpdated(String marketCode_,
			MarketDataType marketDataType_, QuoteAndSize value_, Boolean hasHiddenOrders_, OrderManagementContext context) {
		marketDataUpdated(marketCode_,
				marketDataType_, value_, hasHiddenOrders_, 
				context.getMarketDataSession(),
				context.getPropertyChangeSession(),
				context.getTradeNotificationSession());
	}
	
	private void marketDataUpdated(String marketCode_,
			MarketDataType marketDataType_, QuoteAndSize value_, Boolean hasHiddenOrders_,
			MarketDataSession marketDataSession,
			PropertyChangeSession propertyChangeSessionImpl, 
			TradeNotificationSession tradeNotificationSession_) {
		for (PersistedMarketDataChangeListener listener : _listeners) {
			listener.marketDataChanged(new MarketDataChange(marketCode_, marketDataType_, MarketDtoMapping.INSTANCE.toDto(value_), hasHiddenOrders_, null, null), 
					new MockListenerContext(_datastores, marketDataSession, 
							propertyChangeSessionImpl, tradeNotificationSession_));
		}
	}
	
	public boolean hasMarketDataChangeListeners() {
		updateListeners();
		return !_listeners.isEmpty();
	}
	
	private void updateListeners() {
		_listenersToBeAdded.stream().filter(addedListener -> !_listeners.contains(addedListener)).forEach(_listeners::add); 
		_listenersToBeAdded.clear();
		
		_listenersToBeRemoved.stream().filter(removedListeners -> _listeners.contains(removedListeners)).forEach(_listeners::remove); 
		_listenersToBeRemoved.clear();
	} 
	
	@Override
	public Long addMarketDataChangeListener(String marketCode_,
			MarketDataType marketDataType_,
			PersistedMarketDataChangeListener listener_) {
		return addListener(marketCode_, marketDataType_, listener_);
	}
	
	private Long addListener(String marketCode_,
			MarketDataType marketDataType_,
			PersistedMarketDataChangeListener listener_) {
		Long id = MockIdentityGenerator.getId();
		
		listener_.setObservedObjectKey(new DatastoreKey(marketCode_));
		listener_.setFilterString(marketDataType_.name());

		listener_.setObservedClassName(Market.class.getSimpleName());
		
		_listenersToBeAdded.add(listener_);
		_keyToListener.put(id, listener_);
		return id;
	} 

	@Override
	public void removeMarketDataChangeListener(Long listenerID_) {
		removeListener(listenerID_);
	}
	
	private void removeListener(Long listenerID_) {
		PersistedMarketDataChangeListener remove = _keyToListener.get(listenerID_); 
		if(remove != null) {
			_listenersToBeRemoved.add(remove);
		}
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
