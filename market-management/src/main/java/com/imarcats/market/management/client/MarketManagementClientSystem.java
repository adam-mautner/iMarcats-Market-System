package com.imarcats.market.management.client;

import java.util.Date;

import com.imarcats.interfaces.client.v100.dto.types.TimeOfDayDto;
import com.imarcats.interfaces.server.v100.dto.mapping.MarketDtoMapping;
import com.imarcats.internal.server.infrastructure.datastore.AssetClassDatastore;
import com.imarcats.internal.server.infrastructure.datastore.AuditTrailEntryDatastore;
import com.imarcats.internal.server.infrastructure.datastore.InstrumentDatastore;
import com.imarcats.internal.server.infrastructure.datastore.MarketDatastore;
import com.imarcats.internal.server.infrastructure.datastore.MarketOperatorDatastore;
import com.imarcats.internal.server.infrastructure.datastore.ProductDatastore;
import com.imarcats.internal.server.infrastructure.timer.MarketTimer;
import com.imarcats.market.management.MarketManagementContext;
import com.imarcats.market.management.MarketManagementBase;
import com.imarcats.model.Market;

/**
 * Market Management System 
 * 
 * @author Adam
 *
 */
public class MarketManagementClientSystem extends MarketManagementBase {

	public MarketManagementClientSystem(ProductDatastore productDatastore_, 
			AssetClassDatastore assetClassDatastore_, 
			InstrumentDatastore instrumentDatastore_, 
			MarketOperatorDatastore marketOperatorDatastore_, 
			MarketDatastore marketDatastore_, 
			AuditTrailEntryDatastore auditTrailEntryDatastore_, 
			MarketTimer marketTimer_) {
		super(productDatastore_, assetClassDatastore_, instrumentDatastore_, marketOperatorDatastore_,
				marketDatastore_, auditTrailEntryDatastore_, marketTimer_);
	}

	/**
	 * Closes Market in case of an Emergency
	 * @param marketCode_ Market to be Closed 
	 * @param user_ User 
	 * @param marketManagementContext_ Market Management Context 
	 */
	public void emergencyCloseMarket(String marketCode_, String user_, MarketManagementContext marketManagementContext_) {
		Market market = getMarketInternal(marketCode_);
		
		super.emergencyCloseMarket(market, user_, marketManagementContext_);
	}
	
	/**
	 * Deactivates Market 
	 * @param marketCode_ Market to be Deactivated
	 * @param user_ User 
	 * @param marketManagementContext_ Market Management Context 
	 */
	public void deactivateMarket(String marketCode_, String user_, MarketManagementContext marketManagementContext_) {
		Market market = getMarketInternal(marketCode_);
		
		super.deactivateMarket(market, user_, marketManagementContext_);
	}

	/**
	 * Activates a Call Market 
	 * @param marketCode_ Market to be Activated
	 * @param nextCallDate_ Next Market Call Date 
	 * @param nextCallTime_ Next Market Call Time 
	 * @param userSession_ User Session  
	 * @param marketManagementContext_ Market Management Context 
	 */
	public void activateCallMarket(String marketCode_, Date nextCallDate_, TimeOfDayDto nextMarketCallTime_, String user_, MarketManagementContext marketManagementContext_) {
		Market market = getAndCheckMarketForActivation(marketCode_,
				user_, marketManagementContext_);
		
		super.activateCallMarket(market, nextCallDate_, MarketDtoMapping.INSTANCE.fromDto(nextMarketCallTime_), user_, marketManagementContext_);
	}
	
	private Market getAndCheckMarketForActivation(String marketCode_,
			String user_,
			MarketManagementContext marketManagementContext_) {	
		Market market = getMarketInternal(marketCode_);

		checkActivationCriteria(market);
		return market;
	}

	/**
	 * Activates a Market 
	 * @param marketCode_ Market to be Activated
	 * @param user_ User 
	 * @param marketManagementContext_ Market Management Context 
	 */
	public void activateMarket(String marketCode_, String user_, MarketManagementContext marketManagementContext_) {
		Market market = getAndCheckMarketForActivation(marketCode_,
				user_, marketManagementContext_);
		
		super.activateMarket(market, user_, marketManagementContext_);
	}
}
