package com.imarcats.internal.server.infrastructure.trigger;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.imarcats.internal.server.interfaces.market.MarketInternal;
import com.imarcats.internal.server.interfaces.order.OrderManagementContext;

/**
 * Opens Market 
 * @author Adam
 */
@Entity
@Table(name="OPEN_MARKET_ACTION")
public class OpenMarketAction extends MarketActionBase {

    /**
     * Constructor needed for Deserialization
     */
	public OpenMarketAction() {
		super();
	}

	public OpenMarketAction(String marketCode_) {
		super(marketCode_);
	}

	protected void executeMarketAction(MarketInternal market,
			OrderManagementContext orderManagementContext) {
		market.openMarket(
				orderManagementContext);
	}
	
}
