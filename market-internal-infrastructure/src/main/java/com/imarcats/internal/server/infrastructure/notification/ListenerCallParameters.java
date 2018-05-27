package com.imarcats.internal.server.infrastructure.notification;

import java.util.Date;

import com.imarcats.interfaces.client.v100.notification.ListenerCallUserParameters;
import com.imarcats.interfaces.client.v100.notification.NotificationType;
import com.imarcats.model.types.DatastoreKey;

/**
 * Parameters of the Listener Call.
 * @author Adam
 */
public interface ListenerCallParameters {

	/**
	 * @return ID for the Listener Call 
	 */
	public long getID();
	
	/**
	 * @return Time, when listener was called (guaranteed to be unique together with Listener Call ID)
	 */
	public Date getListenerCallTimestamp();
	
	/**
	 * @return ID of this Listener Call (guaranteed to be unique together with Listener Call Timestamp)
	 */	
	public String getListenerCallID();

	/**
	 * @return Filter String of this Listener Call
	 */
	public String getFilterString();
	
	/**
	 * @return User Parameters of the Listener Call
	 */
	public ListenerCallUserParameters getUserParameters();
	
	/**
	 * @return Key of the Observed Object
	 */
	public DatastoreKey getObservedObjectKey();
	
	/**
	 * @return Class Name of the Observed Object (Order.class.getSimpleName()) 
	 */
	public String getObservedClassName();
	
	/**
	 * @return Notification Type 
	 */
	public NotificationType getNotificationType();
}
