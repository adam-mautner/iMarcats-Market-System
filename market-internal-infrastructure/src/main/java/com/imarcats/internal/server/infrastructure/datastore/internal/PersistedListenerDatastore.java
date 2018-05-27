package com.imarcats.internal.server.infrastructure.datastore.internal;

import com.imarcats.interfaces.client.v100.notification.NotificationType;
import com.imarcats.internal.server.infrastructure.notification.PersistedListener;
import com.imarcats.model.types.DatastoreKey;

/**
 * Datastore for Persisted Listeners
 * @author Adam
 */
public interface PersistedListenerDatastore {

		
	/**
	 * Create a Listener in the Datastore 
	 * @param listener_
	 * @return Key of the Listener
	 */
	public Long createListener(PersistedListener listener_);
	
	/**
	 * Finds all the Listeners in the Datastore for a Key of Observed Object and a Filter String
	 * @param observedObjectKey_ Key of the Observed Object
	 * @param observedObjectClassName_ Name of the Observed Object Class
	 * @param notificationType_ Type of Notification
	 * @param filter_ Filter String
	 * @param pm_ Persistence Manager
	 * @return List of Listeners in Ascending Order by Creation Time
	 */
	public PersistedListener[] findListeners(
			DatastoreKey observedObjectKey_, 
			String observedObjectClassName_, 
			NotificationType notificationType_,
			String filter_);
	
	/**
	 * Updates a Listener 
	 * 
	 * Note: Always call this, when the Listener is changed 
	 * @param key_ Key of the Listener
	 * @param listener_ Listener to be Changed 
	 */
	public void updateListener(final Long key_, final PersistedListener listener_);
	
	/**
	 * Deletes a Listener with a give Key
	 * @param key_ Key of the Listener
	 */
	public void deleteListener(Long key_);
	
	/**
	 * Deletes all Listener for an Observed Object
	 * @param observedObjectKey_ Key of the Observed Object
	 * @param observedObjectClassName_ Name of the Observed Object Class
	 */
	public void deleteAllListeners(DatastoreKey observedObjectKey_, String observedObjectClassName_);	
}
