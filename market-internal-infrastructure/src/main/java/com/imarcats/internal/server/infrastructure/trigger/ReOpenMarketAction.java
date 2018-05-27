package com.imarcats.internal.server.infrastructure.trigger;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.imarcats.internal.server.interfaces.market.MarketInternal;
import com.imarcats.internal.server.interfaces.order.OrderManagementContext;

/**
 * Reopens Market after Halt
 * @author Adam
 */
@Entity
@Table(name="RE_OPEN_MARKET_ACTION")
public class ReOpenMarketAction extends MarketActionBase {

    /**
     * Constructor needed for Deserialization
     */
	public ReOpenMarketAction() {
		super();
	}

	public ReOpenMarketAction(String marketCode_) {
		super(marketCode_);
	}

	protected void executeMarketAction(MarketInternal market,
			OrderManagementContext orderManagementContext) {
		market.reOpenMarket(
				orderManagementContext);
	}
}
