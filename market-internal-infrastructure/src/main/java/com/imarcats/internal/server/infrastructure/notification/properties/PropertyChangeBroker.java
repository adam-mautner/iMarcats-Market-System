package com.imarcats.internal.server.infrastructure.notification.properties;

import com.imarcats.interfaces.client.v100.notification.PropertyChanges;
import com.imarcats.internal.server.infrastructure.notification.NotificationBroker;
import com.imarcats.model.types.DatastoreKey;

/**
 * Brokers Property Changes between Components
 * @author Adam
 */
public interface PropertyChangeBroker {

	/**
	 * Adds Property Change Listener to an Object 
	 * @param observedObjectKey_ Key of the Object to be Observed
	 * @param observedObjectClass_ Class of the Object to be Observed
	 * @param listener_ Listener
	 */
	@SuppressWarnings("unchecked")
	public Long addPropertyChangeListener(DatastoreKey observedObjectKey_, 
			Class observedObjectClass_,
			PersistedPropertyChangeListener listener_);

	/**
	 * Remove a Listener from the Property Change Broker
	 * @param listenerID_ Key of the Listener to be removed 
	 */
	public void removePropertyChangeListener(Long listenerID_);
	
	/**
	 * Notifies all the Listeners in Ascending Order by Creation Time about Property Changes
	 * @param propertyChanges_ Property Changes
	 */
	public void notifyListeners(PropertyChanges[] propertyChanges_);
	
	/**
	 * @return Notification Broker
	 */
	public NotificationBroker getNotificationBroker();
}
