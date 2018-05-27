package com.imarcats.internal.server.infrastructure.trigger;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.imarcats.internal.server.interfaces.market.MarketInternal;
import com.imarcats.internal.server.interfaces.order.OrderManagementContext;

/**
 * Calls a Call Market 
 * @author Adam
 */
@Entity
@Table(name="CALL_MARKET_ACTION")
public class CallMarketAction extends MarketActionBase {

    /**
     * Constructor needed for Deserialization
     */
	public CallMarketAction() {
		super();
	}

	public CallMarketAction(String marketCode_) {
		super(marketCode_);
	}

	protected void executeMarketAction(MarketInternal market,
			OrderManagementContext orderManagementContext) {
		market.callMarket(
				orderManagementContext);
	}
	
}