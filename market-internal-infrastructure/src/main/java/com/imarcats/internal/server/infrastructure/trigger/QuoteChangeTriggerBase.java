package com.imarcats.internal.server.infrastructure.trigger;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

import com.imarcats.internal.server.infrastructure.marketdata.PersistedMarketDataChangeListener;
import com.imarcats.internal.server.interfaces.market.MarketInternal;
import com.imarcats.internal.server.interfaces.order.OrderInternal;
import com.imarcats.internal.server.interfaces.order.OrderManagementContext;

/**
 * Base Class for Quote Change Triggers
 * @author Adam
 */
@Entity
@Table(name="QUOTE_CHANGE_TRIGGER_BASE")
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
public abstract class QuoteChangeTriggerBase extends PersistedMarketDataChangeListener {

	/**
	 * Initializes Price Trigger 
	 * @param order_ Order to be Triggered
	 * @param market_ Target Market
	 * @param orderManagementContext_ Order Management Context 
	 */
	public abstract void initTrigger(OrderInternal order_, MarketInternal market_,
			OrderManagementContext orderManagementContext_);
	
	/**
	 * Returns, if the Order has been Triggered
	 * @param order_ Order to be Triggered
	 * @return if the Order has been triggered
	 */
	public abstract boolean isTriggered(OrderInternal order_);

	/**
	 * Returns, if the Order can be Triggered
	 * @param order_ Order to be Triggered
	 * @return if the Order can be triggered
	 */
	public abstract boolean canBeTriggered(OrderInternal order_);
}
