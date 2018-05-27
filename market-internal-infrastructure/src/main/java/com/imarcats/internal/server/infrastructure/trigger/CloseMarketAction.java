package com.imarcats.internal.server.infrastructure.trigger;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.imarcats.internal.server.interfaces.market.MarketInternal;
import com.imarcats.internal.server.interfaces.order.OrderManagementContext;
import com.imarcats.model.types.MarketState;

/**
 * Closes Market 
 * @author Adam
 */
@Entity
@Table(name="CLOSE_MARKET_ACTION")
public class CloseMarketAction extends MarketActionBase {

    /**
     * Constructor needed for Deserialization
     */
	public CloseMarketAction() {
		super();
	}

	public CloseMarketAction(String marketCode_) {
		super(marketCode_);
	}

	protected void executeMarketAction(MarketInternal market,
			OrderManagementContext orderManagementContext) {
		// ignore this action, if market is already closed
		if(market.getState() == MarketState.Closed) {
			return;
		}
		market.closeMarket(
				orderManagementContext);
	}
	
}
