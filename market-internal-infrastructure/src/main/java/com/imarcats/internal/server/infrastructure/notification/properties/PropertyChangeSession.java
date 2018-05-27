package com.imarcats.internal.server.infrastructure.notification.properties;

import java.util.Date;

import com.imarcats.interfaces.client.v100.notification.ChangeOrigin;
import com.imarcats.interfaces.client.v100.notification.ObjectVersion;
import com.imarcats.model.mutators.PropertyChange;
import com.imarcats.model.types.DatastoreKey;

/**
 * Collects all the Property Changes into a Session, so they can be 
 * committed to the Broker 
 * @author Adam
 */
public interface PropertyChangeSession {
	
	/**
	 * Records Property Changes 
	 * @param objectKey_ Key of the Object on which Property was changed
	 * @param parentKey_ Key of the Parent Object (Null for Market, Market Code for Order)
	 * @param objectClass_ Class of the Object on which Property was changed
	 * @param propertyChanges_ Property Changes  
	 * @param changeTimestamp_ Change Timestamp
	 * @param version_ Version of the Updated Object 
	 * @param owner_ Owner of the Object
	 */
	@SuppressWarnings("unchecked")
	public void recordPropertyChanges(DatastoreKey objectKey_,
			Class objectClass_, DatastoreKey parentKey_, PropertyChange[] propertyChanges_, 
			Date changeTimestamp_, ObjectVersion version_, String owner_);
	
	/**
	 * Records Property Changes 
	 * @param objectKey_ Key of the Object on which Property was changed
	 * @param parentKey_ Key of the Parent Object (Null for Market, Market Code for Order)
	 * @param objectClass_ Class of the Object on which Property was changed
	 * @param propertyChanges_ Property Changes  
	 * @param changeTimestamp_ Change Timestamp
	 * @param version_ Version of the Updated Object 
	 * @param owner_ Owner of the Object
	 * @param changeOrigin_ Origin of the Change 
	 */
	@SuppressWarnings("unchecked")
	public void recordPropertyChanges(DatastoreKey objectKey_, 
			Class objectClass_, DatastoreKey parentKey_, PropertyChange[] propertyChanges_, 
			Date changeTimestamp_, ObjectVersion version_, String owner_, ChangeOrigin changeOrigin_);
	
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
	 * @return Property Change Broker
	 */
	public PropertyChangeBroker getPropertyChangeBroker();
}
