package com.imarcats.market.management.admin;

import java.util.Date;

import com.imarcats.interfaces.client.v100.dto.AssetClassDto;
import com.imarcats.interfaces.client.v100.dto.InstrumentDto;
import com.imarcats.interfaces.client.v100.dto.MarketDto;
import com.imarcats.interfaces.client.v100.dto.MarketOperatorDto;
import com.imarcats.interfaces.client.v100.dto.ProductDto;
import com.imarcats.interfaces.client.v100.dto.types.BusinessCalendarDto;
import com.imarcats.interfaces.client.v100.dto.types.PagedAuditTrailEntryListDto;
import com.imarcats.interfaces.client.v100.dto.types.TimeOfDayDto;
import com.imarcats.internal.server.infrastructure.datastore.AssetClassDatastore;
import com.imarcats.internal.server.infrastructure.datastore.AuditTrailEntryDatastore;
import com.imarcats.internal.server.infrastructure.datastore.InstrumentDatastore;
import com.imarcats.internal.server.infrastructure.datastore.MarketDatastore;
import com.imarcats.internal.server.infrastructure.datastore.MarketOperatorDatastore;
import com.imarcats.internal.server.infrastructure.datastore.MatchedTradeDatastore;
import com.imarcats.internal.server.infrastructure.datastore.ProductDatastore;
import com.imarcats.internal.server.infrastructure.timer.MarketTimer;
import com.imarcats.market.management.MarketManagementContext;
import com.imarcats.market.management.ManagementSystemBase;

/**
 * Administration System for Market Management
 * @author Adam
 *
 */
public class MarketManagementAdminSystem extends ManagementSystemBase {
	
	private final AssetClassAdministrationSubSystem _assetClassAdminSystem;
	private final ProductAdministrationSubSystem _productAdminSystem;
	private final InstrumentAdministrationSubSystem _instrumentAdminSystem;
	private final MarketOperatorAdminstrationSubSystem _marketOperatorAdminSystem;
	private final MarketAdministrationSubSystem _marketAdminSystem;

	public MarketManagementAdminSystem(
			AssetClassDatastore assetClassDatastore_, 
			ProductDatastore productDatastore_,
			InstrumentDatastore instrumentDatastore_, 
			MarketOperatorDatastore marketOperatorDatastore_,
			MarketDatastore marketDatastore_, 
			AuditTrailEntryDatastore auditTrailEntryDatastore_, 
			MatchedTradeDatastore matchedTradeDatastore_,
			MarketTimer marketTimer_) {
		super(productDatastore_, assetClassDatastore_, instrumentDatastore_, 
				marketOperatorDatastore_, marketDatastore_, 
				auditTrailEntryDatastore_, 
				marketTimer_);
		
		_assetClassAdminSystem = new AssetClassAdministrationSubSystem(productDatastore_, 
				assetClassDatastore_, instrumentDatastore_, 
				marketOperatorDatastore_, marketDatastore_, 
				auditTrailEntryDatastore_, marketTimer_);	
		_productAdminSystem = new ProductAdministrationSubSystem(productDatastore_, 
				assetClassDatastore_, instrumentDatastore_, 
				marketOperatorDatastore_, marketDatastore_,
				auditTrailEntryDatastore_, marketTimer_);	
		_instrumentAdminSystem = new InstrumentAdministrationSubSystem(productDatastore_, 
				assetClassDatastore_, instrumentDatastore_, 
				marketOperatorDatastore_, marketDatastore_, 
				auditTrailEntryDatastore_, marketTimer_);	
		_marketOperatorAdminSystem = new MarketOperatorAdminstrationSubSystem(productDatastore_, 
				assetClassDatastore_, instrumentDatastore_, 
				marketOperatorDatastore_, marketDatastore_, 
				auditTrailEntryDatastore_, marketTimer_);		
		_marketAdminSystem = new MarketAdministrationSubSystem(productDatastore_, 
				assetClassDatastore_, instrumentDatastore_, 
				marketOperatorDatastore_, marketDatastore_, 
				auditTrailEntryDatastore_, marketTimer_);		
		
	}

	/**
	 * Gets All Audit Trail Entries
	 * @param cursorString_ Cursor String for continuing a Paged Query or Null for a new Paged Query
	 * @param maxNumberOfAuditTrailEntryOnPage_ Max Number of Audit Trail Entries on a Page   
	 * that will be used for retrieval (if null, new Persistence Manager will be created)
	 * @return Paged Audit Trail Entry List
	 */
	public PagedAuditTrailEntryListDto getAllAuditTrailEntriesFromCursor(String cursorString_, int maxNumberOfAuditTrailEntryOnPage_, String user_) {
		return super.getAllAuditTrailEntriesFromCursor(cursorString_, maxNumberOfAuditTrailEntryOnPage_);
	}
	
	
	/**
	 * Creates Asset Class
	 * @param assetClassDto_ Asset Class to be Created
	 * @param user_ User Session 
	 * 
	 * @return Asset Class Name 
	 */
	public String createAssetClass(AssetClassDto assetClassDto_, String user_) {		
		return _assetClassAdminSystem.createAssetClass(assetClassDto_, user_); 
	}
	
	/**
	 * Changes an Asses Class 
	 * @param assetClassDto_ Asset Class to be Changed
	 * @param user_ User Session 
	 * 
	 */
	public void changeAssetClass(AssetClassDto assetClassDto_, String user_) {
		_assetClassAdminSystem.changeAssetClass(assetClassDto_, user_);
	}
	
	/**
	 * Deletes Asset Class
	 * @param assetClassName_ Asset Class to be Deleted
	 * @param user_ User Session 
	 * @param context_ Market Management Context 
	 */
	public void deleteAssetClass(String assetClassName_, String user_, MarketManagementContext context_) {
		_assetClassAdminSystem.deleteAssetClass(assetClassName_, user_, context_);
	}
	
	/**
	 * Create a new Product
	 * @param productDto_ Product to be Created
	 * @param user_ User Session 
	 * 
	 * @return Product Code
	 */
	public String createProduct(ProductDto productDto_, String user_) {
		return _productAdminSystem.createProduct(productDto_, user_); 
	}

	/**
	 * Change Product 
	 * @param productDto_ Product to be changed
	 * @param user_ User Session 
	 * 
	 */
	public void changeProduct(ProductDto productDto_, String user_) {
		_productAdminSystem.changeProduct(productDto_, false, user_);
	}

	/**
	 * Rollover Product
	 * @param productCode_ Code of the Product (Source) to be Rolled Over to the given Product
	 * @param rolledOverProductInputDto_ New Product to be he rolled over version of the above Product
	 * @param user_ User Session 
	 * 
	 */
	public void rolloverProduct(String productCode_, ProductDto rolledOverProductInputDto_, String user_) {
		_productAdminSystem.rolloverProduct(productCode_, rolledOverProductInputDto_, user_);
	}
	
	/**
	 * Delete Product
	 * @param productCode_ Product to be Deleted
	 * @param user_ User Session 
	 * @param context_ Market Management Context 
	 */
	public void deleteProduct(String productCode_, String user_, MarketManagementContext context_) {
		_productAdminSystem.deleteProduct(productCode_, user_, context_);
	}
	
	/**
	 * Set Product Definition Document on Product
	 * @param productCode_ Product to be Changed 
	 * @param productDefinitionDocument_ Definition Document to be set 
	 * @param user_ User Session 
	 * @param pm_ Persistence Manager
	 */
	public void setProductDefinitionDocument(String productCode_, String productDefinitionDocument_, String user_) {
		_productAdminSystem.setProductDefinitionDocument(productCode_, productDefinitionDocument_, user_);
	}
	
	/**
	 * Approve a Product 
	 * @param productCode_ Product to be Approved
	 * @param lastUpdateTimestamp_ Last Update Timestamp of the Object
	 * @param user_ User Session 
	 * 
	 */
	public void approveProduct(String productCode_, Date lastUpdateTimestamp_, String user_) {
		_productAdminSystem.approveProduct(productCode_, lastUpdateTimestamp_, user_);
	}

	/**
	 * Suspend a Product 
	 * @param productCode_ Product to be Suspended
	 * @param user_ User Session 
	 * 
	 */
	public void suspendProduct(String productCode_, String user_) {
		_productAdminSystem.suspendProduct(productCode_, user_);
	}
	
	/**
	 * Create a new Instrument
	 * @param instrumentDto_ Instrument to be Created
	 * @param user_ User Session 
	 * 
	 * @return Instrument Code
	 */
	public String createInstrument(InstrumentDto instrumentDto_, String user_) {
		return _instrumentAdminSystem.createInstrument(instrumentDto_, user_); 
	}
	
	/**
	 * Change Instrument 
	 * @param instrumentDto_ Instrument to be changed
	 * @param user_ User Session 
	 * 
	 */
	public void changeInstrument(InstrumentDto instrumentDto_, String user_) {
		_instrumentAdminSystem.changeInstrument(instrumentDto_, false, user_);
	}
	
	/**
	 * Rollover Instrument
	 * @param instrumentCode_ Code of the Instrument (Source) to be Rolled Over to the given Instrument
	 * @param rolledOverInstrumentInputDto_ New Instrument to be the rolled over version of the above Instrument
	 * @param user_ User Session 
	 * 
	 */
	public void rolloverInstrument(String instrumentCode_, InstrumentDto rolledOverInstrumentInputDto_, String user_) {		
		_instrumentAdminSystem.rolloverInstrument(instrumentCode_, rolledOverInstrumentInputDto_, user_);
	}
	
	/**
	 * Delete Instrument
	 * @param instrumentCode_ Instrument to be Deleted
	 * @param user_ User Session 
	 * @param context_ Market Management Context 
	 */
	public void deleteInstrument(String instrumentCode_, String user_, MarketManagementContext context_) {
		_instrumentAdminSystem.deleteInstrument(instrumentCode_, user_, context_);
	}
	
	/**
	 * Approve a Instrument 
	 * @param instrumentCode_ Instrument to be Approved
	 * @param lastUpdateTimestamp_ Last Update Timestamp of the Object
	 * @param user_ User Session 
	 * 
	 */
	public void approveInstrument(String instrumentCode_, Date lastUpdateTimestamp_, String user_) {		
		_instrumentAdminSystem.approveInstrument(instrumentCode_, lastUpdateTimestamp_, user_);
	}

	/**
	 * Set Master Agreement Document on Instrument
	 * @param instrumentCode_ Instrument to be Changed 
	 * @param masterAgreementDocument_ Master Agreement Document to be set 
	 * @param user_ User Session 
	 * @param pm_ Persistence Manager
	 */
	public void setInstrumentMasterAgreementDocument(String instrumentCode_, String masterAgreementDocument_, String user_) {
		_instrumentAdminSystem.setInstrumentMasterAgreementDocument(instrumentCode_, masterAgreementDocument_, user_);
	}
	
	/**
	 * Set Asset Class on Instrument
	 * @param instrumentCode_ Instrument to be Changed 
	 * @param assetClassName_ Name of the Asset Class to be set 
	 * @param user_ User Session 
	 * @param pm_ Persistence Manager
	 */
	public void setInstrumentAssetClass(String instrumentCode_, String assetClassName_, String user_) {
		_instrumentAdminSystem.setInstrumentAssetClass(instrumentCode_, assetClassName_, user_);
	}
	
	/**
	 * Suspend a Instrument 
	 * @param instrumentCode_ Instrument to be Suspended
	 * @param user_ User Session 
	 * 
	 */
	public void suspendInstrument(String instrumentCode_, String user_) {
		_instrumentAdminSystem.suspendInstrument(instrumentCode_, user_);
	}

	/**
	 * Create a new Market Operator 
	 * @param marketOperatorDto_ Market Operator to be Created
	 * @param user_ User Session 
	 * 
	 * @return Market Operator Code
	 */
	public String createMarketOperator(MarketOperatorDto marketOperatorDto_, String user_) {
		return _marketOperatorAdminSystem.createMarketOperator(marketOperatorDto_, user_); 
	}

	/**
	 * Change Market Operator 
	 * @param marketOperatorDto_ Market Operator to be changed
	 * @param user_ User Session 
	 * 
	 */
	public void changeMarketOperator(MarketOperatorDto marketOperatorDto_, String user_) {
		_marketOperatorAdminSystem.changeMarketOperator(marketOperatorDto_, false, user_);
	}
	
	/**
	 * Delete Market Operator
	 * @param marketOperatorCode_ Market Operator to be Deleted
	 * @param user_ User Session 
	 * @param context_ Market Management Context 
	 */
	public void deleteMarketOperator(String marketOperatorCode_, String user_, MarketManagementContext context_) {
		_marketOperatorAdminSystem.deleteMarketOperator(marketOperatorCode_, user_, context_);
	}
	
	/**
	 * Approve a Market Operator 
	 * @param marketOperatorCode_ Market Operator to be Approved
	 * @param lastUpdateTimestamp_ Last Update Timestamp of the Object
	 * @param user_ User Session 
	 * 
	 */
	public void approveMarketOperator(String marketOperatorCode_, Date lastUpdateTimestamp_, String user_) {		
		_marketOperatorAdminSystem.approveMarketOperator(marketOperatorCode_, lastUpdateTimestamp_, user_);
	}

	/**
	 * Set Market Operator Agreement Document on Market Operator
	 * @param marketOperatorCode_ Market Operator to be Changed 
	 * @param marketOperatorAgreement_ Market Operator Agreement to be set 
	 * @param user_ User Session 
	 * @param pm_ Persistence Manager
	 */
	public void setMarketOperatorMasterAgreementDocument(String marketOperatorCode_, String marketOperatorAgreement_, String user_) {
		_marketOperatorAdminSystem.setMarketOperatorMasterAgreementDocument(marketOperatorCode_, marketOperatorAgreement_, user_);
	}
	
	/**
	 * Suspend a Market Operator 
	 * @param marketOperatorCode_ Market Operator to be Suspended
	 * @param user_ User Session 
	 * 
	 */
	public void suspendMarketOperator(String marketOperatorCode_, String user_) {
		_marketOperatorAdminSystem.suspendMarketOperator(marketOperatorCode_, user_);
	}
	
	/**
	 * Create a new Market
	 * @param marketDto_ Market to be Created
	 * @param user_ User Session 
	 * @param marketManagementContext_ Market Management Context
	 * @return Market Code
	 */
	public String createMarket(MarketDto marketDto_, String user_, MarketManagementContext marketManagementContext_) {
		return _marketAdminSystem.createMarket(marketDto_, user_, marketManagementContext_); 
	}

	/**
	 * Change Market  
	 * @param marketDto_ Market to be changed
	 * @param user_ User Session 
	 * @param marketManagementContext_ Market Management Context
	 */
	public void changeMarket(MarketDto marketDto_, String user_, MarketManagementContext marketManagementContext_) {
		_marketAdminSystem.changeMarket(marketDto_, false, user_, marketManagementContext_);
	}
	
	/**
	 * Sets Market Calendar to Market
	 * @param marketCode_ Market to be changed
	 * @param marketCalendarDto_ Market Trading Calendar  
	 * @param user_ User Session 
	 * @param marketManagementContext_ Market Management Context
	 */
	public void setMarketCalendar(String marketCode_, BusinessCalendarDto marketCalendarDto_, String user_, MarketManagementContext marketManagementContext_) {
		_marketAdminSystem.setMarketCalendar(marketCode_, marketCalendarDto_, false, user_, marketManagementContext_);
	}
	
	/**
	 * Delete Market
	 * @param marketCode_ Market to be Deleted
	 * @param user_ User Session
	 * @param marketManagementContext_ Market Management Context
	 */
	public void deleteMarket(String marketCode_, String user_, MarketManagementContext marketManagementContext_) {
		_marketAdminSystem.deleteMarket(marketCode_, user_, marketManagementContext_);
	}
	
	/**
	 * Rollover Market
	 * @param marketCode_ Code of the Market (Source) to be Rolled Over to the given Instrument
	 * @param rolledOverInstrumentCode_ Code of the New Instrument for the Market to be rolled over to
	 * @param user_ User Session 
	 * @param marketManagementContext_ Market Management Context
	 * 
	 * @return Code of the Rolledover Market
	 */
	public String rolloverMarket(String marketCode_, String rolledOverInstrumentCode_, String user_, MarketManagementContext marketManagementContext_) {
		return _marketAdminSystem.rolloverMarket(marketCode_, rolledOverInstrumentCode_, user_, marketManagementContext_);		
	}
	
	/**
	 * Activates a Call Market 
	 * @param marketCode_ Market to be Activated
	 * @param nextCallDate_ Next Market Call Date 
	 * @param nextCallTime_ Next Market Call Time 
	 * @param user_ User Session  
	 * @param marketManagementContext_ Market Management Context 
	 */
	public void activateCallMarket(String marketCode_, Date nextCallDate_, TimeOfDayDto nextMarketCallTime_, String user_, MarketManagementContext marketManagementContext_) {
		_marketAdminSystem.activateCallMarket(marketCode_, nextCallDate_, nextMarketCallTime_, user_, marketManagementContext_);
	}

	/**
	 * Activates a Market 
	 * @param marketCode_ Market to be Activated
	 * @param user_ User Session  
	 * @param marketManagementContext_ Market Management Context 
	 */
	public void activateMarket(String marketCode_, String user_, MarketManagementContext marketManagementContext_) {
		_marketAdminSystem.activateMarket(marketCode_, user_, marketManagementContext_);
	}

	
	/**
	 * Approve a Market 
	 * @param marketCode_ Market to be Approved
	 * @param lastUpdateTimestamp_ Last Update Timestamp of the Object
	 * @param user_ User Session 
	 * @param marketManagementContext_ Market Management Context
	 */
	public void approveMarket(String marketCode_, Date lastUpdateTimestamp_, String user_, MarketManagementContext marketManagementContext_) {		
		_marketAdminSystem.approveMarket(marketCode_, lastUpdateTimestamp_, user_, marketManagementContext_);
	}
	
	/**
	 * Set Market Operation Contract on Market
	 * @param marketCode_ Market to be Changed 
	 * @param marketOperationContract_ Market Operation Contract to be set 
	 * @param user_ User Session 
	 * @param marketManagementContext_ Market Management Context
	 */
	public void setMarketOperationContract(String marketCode_, String marketOperationContract_, String user_, MarketManagementContext marketManagementContext_) {
		_marketAdminSystem.setMarketOperationContract(marketCode_, marketOperationContract_, user_, marketManagementContext_);
	}
	
	/**
	 * Suspend a Market 
	 * @param marketCode_ Market to be Suspended
	 * @param user_ User Session 
	 * @param marketManagementContext_ Market Management Context
	 */
	public void suspendMarket(String marketCode_, String user_, MarketManagementContext marketManagementContext_) {
		_marketAdminSystem.suspendMarket(marketCode_, user_, marketManagementContext_);
	}
	
	/**
	 * Closes Market in case of an Emergency
	 * @param marketCode_ Market to be Closed 
	 * @param user_ User Session  
	 * @param marketManagementContext_ Market Management Context 
	 */
	public void emergencyCloseMarket(String marketCode_, String user_, MarketManagementContext marketManagementContext_) {
		_marketAdminSystem.emergencyCloseMarket(marketCode_, user_, marketManagementContext_);

	}

	/**
	 * Deactivates Market 
	 * @param marketCode_ Market to be Deactivated
	 * @param user_ User Session  
	 * @param marketManagementContext_ Market Management Context 
	 */
	public void deactivateMarket(String marketCode_, String user_, MarketManagementContext marketManagementContext_) {
		_marketAdminSystem.deactivateMarket(marketCode_, user_, marketManagementContext_);
	}
}
