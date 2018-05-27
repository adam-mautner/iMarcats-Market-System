package com.imarcats.internal.server.infrastructure.marketdata;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

import com.imarcats.interfaces.client.v100.notification.MarketDataChange;
import com.imarcats.internal.server.infrastructure.notification.ListenerCallContext;
import com.imarcats.internal.server.infrastructure.notification.ListenerCallParameters;
import com.imarcats.internal.server.infrastructure.notification.MarketObjectListenerBase;

/**
 * Base Class for Persisted Market Data Listeners
 * @author Adam
 */
@Entity
@Table(name="PERSITED_MARKET_DATA_CHANGE_LISTENER")
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
public abstract class PersistedMarketDataChangeListener extends MarketObjectListenerBase {
	
	@Override
	public final void fireListener(ListenerCallParameters listenerParameters_,
			ListenerCallContext listenerContext_) {
		
		marketDataChanged(((MarketDataChange) listenerParameters_.getUserParameters()), 
				listenerContext_);
	}
	
	/**
	 * Called, when Market Data is changed on the Market
	 * @param value_ Market Data Change 
	 * @param listenerContext_ Context for this Listener Call
	 */
	public abstract void marketDataChanged(MarketDataChange value_, 
			ListenerCallContext listenerContext_);
 }
