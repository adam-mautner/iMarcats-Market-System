package com.imarcats.internal.server.infrastructure.trigger;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.imarcats.internal.server.interfaces.market.MarketInternal;
import com.imarcats.internal.server.interfaces.order.OrderManagementContext;

/**
 * Executes regular maintenance on Market 
 * @author Adam
 */
@Entity
@Table(name="MARKET_MAINTENANCE_ACTION")
public class MarketMaintenanceAction extends MarketActionBase {

    /**
     * Constructor needed for Deserialization
     */
	public MarketMaintenanceAction() {
		super();
	}

	public MarketMaintenanceAction(String marketCode_) {
		super(marketCode_);
	}

	protected void executeMarketAction(MarketInternal market,
			OrderManagementContext orderManagementContext) {
		market.executeRegularMarketMaintenance(
				orderManagementContext);
	}
}
