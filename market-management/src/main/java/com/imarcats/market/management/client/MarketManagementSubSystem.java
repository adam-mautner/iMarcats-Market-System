package com.imarcats.market.management.client;

import java.util.Date;

import com.imarcats.interfaces.client.v100.dto.types.TimeOfDayDto;
import com.imarcats.internal.server.infrastructure.datastore.AssetClassDatastore;
import com.imarcats.internal.server.infrastructure.datastore.AuditTrailEntryDatastore;
import com.imarcats.internal.server.infrastructure.datastore.InstrumentDatastore;
import com.imarcats.internal.server.infrastructure.datastore.MarketDatastore;
import com.imarcats.internal.server.infrastructure.datastore.MarketOperatorDatastore;
import com.imarcats.internal.server.infrastructure.datastore.ProductDatastore;
import com.imarcats.internal.server.infrastructure.timer.MarketTimer;
import com.imarcats.market.management.MarketManagementContext;
import com.imarcats.market.management.ManagementSystemBase;

/**
 * System for Market Management
 * @author Adam
 *
 */
public class MarketManagementSubSystem extends ManagementSystemBase {
	
	private final MarketManagementClientSystem _marketManagement;
	
	public MarketManagementSubSystem(
			ProductDatastore productDatastore_, 
			AssetClassDatastore assetClassDatastore_,
			InstrumentDatastore instrumentDatastore_, 
			MarketOperatorDatastore marketOperatorDatastore_,
			MarketDatastore marketDatastore_, 
			AuditTrailEntryDatastore auditTrailEntryDatastore_,
			MarketTimer marketTimer_) {
		super(productDatastore_, assetClassDatastore_, instrumentDatastore_, marketOperatorDatastore_,
				marketDatastore_, auditTrailEntryDatastore_, marketTimer_);
		_marketManagement = new MarketManagementClientSystem(productDatastore_, 
				assetClassDatastore_, instrumentDatastore_, 
				marketOperatorDatastore_, marketDatastore_, 
				auditTrailEntryDatastore_, marketTimer_);
	}
	
	/**
	 * Closes Market in case of an Emergency
	 * @param marketCode_ Market to be Closed 
	 * @param user_ User 
	 * @param marketManagementContext_ Market Management Context 
	 */
	public void emergencyCloseMarket(String marketCode_, String user_, MarketManagementContext marketManagementContext_) {
		_marketManagement.emergencyCloseMarket(marketCode_, user_, marketManagementContext_);
	}

	/**
	 * Deactivates Market 
	 * @param marketCode_ Market to be Deactivated
	 * @param user_ User 
	 * @param marketManagementContext_ Market Management Context 
	 */
	public void deactivateMarket(String marketCode_, String user_, MarketManagementContext marketManagementContext_) {
		_marketManagement.deactivateMarket(marketCode_, user_, marketManagementContext_);
	}

	/**
	 * Activates a Call Market 
	 * @param marketCode_ Market to be Activated
	 * @param nextCallDate_ Next Market Call Date 
	 * @param nextCallTime_ Next Market Call Time 
	 * @param user_ User 
	 * @param marketManagementContext_ Market Management Context 
	 */
	public void activateCallMarket(String marketCode_, Date nextCallDate_, TimeOfDayDto nextMarketCallTime_, String user_, MarketManagementContext marketManagementContext_) {
		_marketManagement.activateCallMarket(marketCode_, nextCallDate_, nextMarketCallTime_, user_, marketManagementContext_);
	}

	/**
	 * Activates a Market 
	 * @param marketCode_ Market to be Activated
	 * @param user_ User 
	 * @param marketManagementContext_ Market Management Context 
	 */
	public void activateMarket(String marketCode_, String user_, MarketManagementContext marketManagementContext_) {
		_marketManagement.activateMarket(marketCode_, user_, marketManagementContext_);
	}

	/**
	 * Create a new User
	 * @param user_ User to be Created
	 * @param password_ Password 
	 * @return User Code
	 * 
	 * NOTE: This is used for Testing Only, Users cannot register themselves to the System an User Administrator has to do it
	 * 
	 */
	/*
	private String createUser(User user_, String password_, IDatastorePersistenceManager pm_) {
		UserValidator.validateNewUser(user_);
		setupNewUser(user_);

		user_.setCreationAudit(createAudit(user_.getUserID()));
		
		// TODO: Create a separate Audit Log
		
		return _userDatastore.createUser(user_, password_, pm_); 
	}
	*/

}
