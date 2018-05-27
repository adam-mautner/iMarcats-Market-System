package com.imarcats.internal.server.infrastructure.notification;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

import com.imarcats.interfaces.client.v100.exception.MarketRuntimeException;
import com.imarcats.internal.server.interfaces.market.MarketInternal;
import com.imarcats.internal.server.interfaces.order.OrderInternal;

/**
 * Base Class for Market Object Listeners
 * @author Adam
 */
@Entity
@Table(name="MARKET_OBJECT_LISTENER_BASE")
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
public abstract class MarketObjectListenerBase extends PersistedListener {
	/**
	 * Get Order from Listener Context and Parameters 
	 * @param orderKey_
	 * @param listenerContext_
	 * @return Order
	 */
	protected OrderInternal getOrder(Long orderKey_, ListenerCallContext listenerContext_) {
		OrderInternal orderInternal = 
			listenerContext_.getOrderDatastore().findOrderBy(orderKey_);
		if(orderInternal == null) {
			throw MarketRuntimeException.createExceptionWithDetails(MarketRuntimeException.NON_EXISTENT_ORDER, 
					null, new Object[] { "Order ID=" + orderKey_ });
		}
		
		return orderInternal;
	}
	
	/**
	 * Get Market from Listener Context
	 * @param marketCode_ Market Code 
	 * @param listenerContext_ Listener Context
	 * @return Market
	 */
	protected MarketInternal getMarket(String marketCode_, ListenerCallContext listenerContext_) { 
		MarketInternal targetMarket = listenerContext_.getMarketDatastore().findMarketBy(
				marketCode_);
		if(targetMarket == null) {
			throw MarketRuntimeException.createExceptionWithDetails(MarketRuntimeException.NON_EXISTENT_MARKET, 
					null, new Object[] { "Market ID=" + marketCode_ });
		}
		return targetMarket;
	}
}
