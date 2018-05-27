package com.imarcats.internal.server.infrastructure.notification.properties;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

import com.imarcats.interfaces.client.v100.notification.PropertyChanges;
import com.imarcats.internal.server.infrastructure.notification.ListenerCallContext;
import com.imarcats.internal.server.infrastructure.notification.ListenerCallParameters;
import com.imarcats.internal.server.infrastructure.notification.MarketObjectListenerBase;

/**
 * Base Class for Persisted Property Change Listeners
 * @author Adam
 */
@Entity
@Table(name="PERSISTED_PROPERTY_CHANGE_LISTENER")
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
public abstract class PersistedPropertyChangeListener extends MarketObjectListenerBase {

	@Override
	public final void fireListener(ListenerCallParameters listenerParameters_,
			ListenerCallContext listenerContext_) {
		
		propertyChanged(((PropertyChanges) listenerParameters_.getUserParameters()), 
				listenerContext_);
	}
	
	/**
	 * Called, when a Property is changed on the Observed Object 
	 * @param value_ Property Changes 
	 * @param listenerContext_ Context for this Listener Call
	 */
	public abstract void propertyChanged(PropertyChanges value_, 
			ListenerCallContext listenerContext_);
 }
