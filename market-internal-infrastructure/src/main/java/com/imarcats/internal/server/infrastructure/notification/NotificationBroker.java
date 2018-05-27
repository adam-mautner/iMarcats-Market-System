package com.imarcats.internal.server.infrastructure.notification;

import com.imarcats.interfaces.client.v100.notification.ListenerCallUserParameters;
import com.imarcats.interfaces.client.v100.notification.NotificationType;
import com.imarcats.model.types.DatastoreKey;

public interface NotificationBroker {

	/**
	 * Adds a Listener to a Object
	 * @param observedObject_ Object being Observed
	 * @param observedObjectClass_ Class of the Object being Observed
	 * @param notificationType_ Type of the Notification
	 * @param filterString_ Filter String
	 * @param listener_ Listener to be added
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Long addListener(DatastoreKey observedObject_, 
			Class observedObjectClass_, 
			NotificationType notificationType_,
			String filterString_, 
			PersistedListener listener_); 

	/**
	 * Removes a Listener by Key
	 * @param listenerKey_ Key of the Listener to be removed 
	 */
	public void removeListener(Long listenerKey_);
	
	/**
	 * Notifies all the Listeners in Ascending Order by Creation Time about the change of Observed Object
	 * @param observedObject_ Object being Observed
	 * @param observedObjectClass_ Class of the Object being Observed
	 * @param notificationType_ Type of the Notification
	 * @param filterString_ Filter String
	 * @param parameters_ Parameters 
	 */
	@SuppressWarnings("unchecked")
	public void notifyListeners(DatastoreKey observedObject_, 
			Class observedObjectClass_, 
			NotificationType notificationType_,
			String filterString_, 
			ListenerCallUserParameters parameters_);
	
	/**
	 * Removes all listener for a given Object
	 * @param observedObject_ Object being Observed
	 * @param observedObjectClass_ Class of the Object being Observed
	 */
	@SuppressWarnings("unchecked")
	public void removeAllListeners(DatastoreKey observedObjectKey_, 
			Class observedObjectClass_);
}
