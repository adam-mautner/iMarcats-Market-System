package com.imarcats.internal.server.infrastructure.trigger;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.imarcats.internal.server.interfaces.market.MarketInternal;
import com.imarcats.internal.server.interfaces.order.OrderManagementContext;

/**
 * Executes maintenance on Call Market 
 * @author Adam
 */
@Entity
@Table(name="CALL_MARKET_MAINTENANCE_ACTION")
public class CallMarketMaintenanceAction extends MarketActionBase {

    /**
     * Constructor needed for Deserialization
     */
	public CallMarketMaintenanceAction() {
		super();
	}

	public CallMarketMaintenanceAction(String marketCode_) {
		super(marketCode_);
	}

	protected void executeMarketAction(MarketInternal market,
			OrderManagementContext orderManagementContext) {
		market.executeCallMarketMaintenance(
				orderManagementContext);
	}
}
