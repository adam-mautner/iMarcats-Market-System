package com.imarcats.market.management.admin;

import java.util.Date;

import com.imarcats.interfaces.client.v100.dto.MarketOperatorDto;
import com.imarcats.interfaces.client.v100.exception.MarketRuntimeException;
import com.imarcats.interfaces.client.v100.util.DataUtils;
import com.imarcats.interfaces.server.v100.dto.mapping.MarketOperatorDtoMapping;
import com.imarcats.internal.server.infrastructure.datastore.AssetClassDatastore;
import com.imarcats.internal.server.infrastructure.datastore.AuditTrailEntryDatastore;
import com.imarcats.internal.server.infrastructure.datastore.InstrumentDatastore;
import com.imarcats.internal.server.infrastructure.datastore.MarketDatastore;
import com.imarcats.internal.server.infrastructure.datastore.MarketOperatorDatastore;
import com.imarcats.internal.server.infrastructure.datastore.ProductDatastore;
import com.imarcats.internal.server.infrastructure.timer.MarketTimer;
import com.imarcats.market.management.MarketManagementContext;
import com.imarcats.market.management.MarketManagementBase;
import com.imarcats.market.management.validation.MarketOperatorValidator;
import com.imarcats.market.management.validation.ValidatorUtils;
import com.imarcats.model.MarketOperator;
import com.imarcats.model.types.ActivationStatus;
import com.imarcats.model.types.AuditEntryAction;

/**
 * Market Operator Administration System 
 * 
 * @author Adam
 *
 */
public class MarketOperatorAdminstrationSubSystem extends MarketManagementBase {
	
	protected static final String OBJECT_TYPE = MarketOperator.class.getSimpleName();
	
	private final MarketOperatorDatastore _marketOperatorDatastore; 

	public MarketOperatorAdminstrationSubSystem(ProductDatastore productDatastore_, 
			AssetClassDatastore assetClassDatastore_, 
			InstrumentDatastore instrumentDatastore_, 
			MarketOperatorDatastore marketOperatorDatastore_, 
			MarketDatastore marketDatastore_, 
			AuditTrailEntryDatastore auditTrailEntryDatastore_, 
			MarketTimer marketTimer_) {
		super(productDatastore_, assetClassDatastore_, instrumentDatastore_, marketOperatorDatastore_,
				marketDatastore_, auditTrailEntryDatastore_, marketTimer_);
		_marketOperatorDatastore = marketOperatorDatastore_;
	}

	/**
	 * Create a new Market Operator 
	 * @param marketOperatorDto_ Market Operator to be Created
	 * @param user_ User 
	 * @return Market Operator Code
	 */
	public String createMarketOperator(MarketOperatorDto marketOperatorDto_, String user_) {
		MarketOperator marketOperator = MarketOperatorDtoMapping.INSTANCE.fromDto(marketOperatorDto_);
		
		MarketOperatorValidator.validateNewMarketOperator(marketOperator);

		setupNewMarketOperator(marketOperator, user_);

		marketOperator.setCreationAudit(createAudit(user_));		
		marketOperator.updateLastUpdateTimestamp();
		
		String marketOperatorCode = _marketOperatorDatastore.createMarketOperator(marketOperator);
		
		addAuditTrailEntry(AuditEntryAction.Created, marketOperator.getCreationAudit(), marketOperator.getCode(), OBJECT_TYPE);
		
		return marketOperatorCode; 
	}

	/**
	 * Change Market Operator 
	 * @param marketOperatorDto_ Market Operator to be changed
	 * @param force_ Force the change, even if the 
	 * 				 Market Operator is in Approved State (It will lose Approval), 
	 * 				 it will only change Market Operator, if there are no dependencies on the Market Operator
	 * @param user_ User 
	 */
	public void changeMarketOperator(MarketOperatorDto marketOperatorDto_, boolean force_, String user_) {		
		MarketOperator originalMarketOperator = getMarketOperatorInternal(marketOperatorDto_.getCode());
		MarketOperator newMarketOperator = MarketOperatorDtoMapping.INSTANCE.fromDto(marketOperatorDto_);
		
		MarketOperatorValidator.validateMarketOperatorChange(newMarketOperator);

		checkLastUpdateTimestamp(newMarketOperator, originalMarketOperator, 
				MarketRuntimeException.MARKET_OPERATOR_CANNOT_BE_OVERWRITTEN_WITH_AN_OLDER_VERSION);
		
		checkMarketOperatorActivationStatusForChange(originalMarketOperator, force_, 
			MarketRuntimeException.MARKET_OPERATOR_ONLY_IN_CREATED_OR_SUSPEDED_STATE_CAN_BE_CHANGED, 
			MarketRuntimeException.MARKET_OPERATOR_THAT_HAS_NO_APPROVED_DEPENDENT_MARKETS_CAN_BE_FORCED_TO_BE_CHANGED);

		setupChangedMarketOperator(originalMarketOperator, newMarketOperator);

		newMarketOperator.setChangeAudit(createAudit(user_));
		newMarketOperator.updateLastUpdateTimestamp();
		
		_marketOperatorDatastore.updateMarketOperator(newMarketOperator); 
		
		addAuditTrailEntry(AuditEntryAction.Changed, newMarketOperator.getChangeAudit(), marketOperatorDto_.getCode(), OBJECT_TYPE);
	}
	
	/**
	 * Delete Market Operator
	 * @param marketOperatorCode_ Market Operator to be Deleted
	 * @param user_ User  
	 * @param context_ Market Management Context 
	 */
	public void deleteMarketOperator(String marketOperatorCode_, String user_, MarketManagementContext context_) {
		MarketOperator marketOperator = getMarketOperatorInternal(marketOperatorCode_);
		
		checkForObjectForDelete(marketOperator, MarketRuntimeException.MARKET_OPERATOR_ONLY_IN_CREATED_OR_SUSPEDED_STATE_CAN_BE_DELETED);


		checkIfDoesNotHaveDependenentObjects(marketOperator.getCode(), DependencyType.UnderlyingMarketOperatorCode, 
				MarketRuntimeException.MARKET_OPERATOR_THAT_HAS_NO_DEPENDENT_MARKET_CAN_BE_DELETED);
 
		_marketOperatorDatastore.deleteMarketOperator(marketOperator.getCode());
		
		addAuditTrailEntry(AuditEntryAction.Deleted, createAudit(user_), marketOperator.getCode(), OBJECT_TYPE);
		
	}
	
	
	/**
	 * Approve a Market Operator 
	 * @param marketOperatorCode_ Market Operator to be Approved
	 * @param lastUpdateTimestamp_ Last Update Timestamp of the Object
	 * @param user_ User 
	 */
	public void approveMarketOperator(String marketOperatorCode_, Date lastUpdateTimestamp_, String user_) {		
		MarketOperator marketOperator = getMarketOperatorInternal(marketOperatorCode_);
		checkLastUpdateTimestampForApproval(lastUpdateTimestamp_, marketOperator);
		
		checkIfObjectInactive(marketOperator, MarketRuntimeException.MARKET_OPERATOR_ONLY_IN_CREATED_OR_SUSPEDED_STATE_CAN_BE_APPROVED);
		
		if(!ValidatorUtils.isNonNullString(marketOperator.getMarketOperatorAgreement())) {
			throw MarketRuntimeException.createExceptionWithDetails(
					MarketRuntimeException.MARKET_OPERATOR_MUST_HAVE_MARKET_OPERATOR_AGREEMENT_DOCUMENT, 
					null, new Object[] { marketOperator });
		}
		
		marketOperator.setActivationStatus(ActivationStatus.Approved);
		marketOperator.setApprovalAudit(createAudit(user_));
		marketOperator.updateLastUpdateTimestamp();
		
		// TODO: Save to historical datastore 
		
		addAuditTrailEntry(AuditEntryAction.Approved, marketOperator.getApprovalAudit(), marketOperatorCode_, OBJECT_TYPE);
	}

	/**
	 * Set Market Operator Agreement Document on Market Operator
	 * @param marketOperatorCode_ Market Operator to be Changed 
	 * @param marketOperatorAgreement_ Market Operator Agreement to be set 
	 * @param user_ User 
	 */
	public void setMarketOperatorMasterAgreementDocument(String marketOperatorCode_, String marketOperatorAgreement_, String user_) {
		MarketOperator marketOperator = getMarketOperatorInternal(marketOperatorCode_);	

		checkIfObjectInactive(marketOperator, MarketRuntimeException.MARKET_OPERATOR_ONLY_IN_CREATED_OR_SUSPEDED_STATE_CAN_BE_CHANGED);
		
		marketOperator.setMarketOperatorAgreement(marketOperatorAgreement_);
		marketOperator.updateLastUpdateTimestamp();
		
		addAuditTrailEntry(AuditEntryAction.Changed, createAudit(user_), marketOperatorCode_, OBJECT_TYPE);
	}
	
	/**
	 * Suspend a Market Operator 
	 * @param marketOperatorCode_ Market Operator to be Suspended
	 * @param user_ User 
	 */
	public void suspendMarketOperator(String marketOperatorCode_, String user_) {
		MarketOperator marketOperator = getMarketOperatorInternal(marketOperatorCode_);
		
		checkIfDependentMarketsInactive(marketOperator, MarketRuntimeException.MARKET_OPERATOR_CAN_ONLY_BE_SUSPENDED_IF_DEPENDENT_OBJECTS_ARE_SUSPENDED);
		
		checkIfObjectActive(marketOperator, MarketRuntimeException.MARKET_OPERATOR_NOT_IN_CREATED_OR_SUSPEDED_STATE_CAN_BE_SUSPENDED);
		
		marketOperator.setActivationStatus(ActivationStatus.Suspended);
		marketOperator.setSuspensionAudit(createAudit(user_));
		marketOperator.updateLastUpdateTimestamp();
		
		addAuditTrailEntry(AuditEntryAction.Suspended, marketOperator.getSuspensionAudit(), marketOperatorCode_, OBJECT_TYPE);
	}
	
	private void setupNewMarketOperator(MarketOperator marketOperator_,
			String user_) {
		// capitalize market operator code
		marketOperator_.setCode(DataUtils.adjustCodeToStandard(marketOperator_.getCode()));
		
		marketOperator_.setActivationStatus(ActivationStatus.Created);
	}
	
	public static void setupChangedMarketOperator(MarketOperator originalMarketOperator_,
			MarketOperator newMarketOperator_) {
		newMarketOperator_.setActivationStatus(originalMarketOperator_.getActivationStatus());
		newMarketOperator_.setApprovalAudit(originalMarketOperator_.getApprovalAudit());
		newMarketOperator_.setCreationAudit(originalMarketOperator_.getCreationAudit());
		newMarketOperator_.setChangeAudit(originalMarketOperator_.getChangeAudit());
		newMarketOperator_.setMarketOperatorAgreement(originalMarketOperator_.getMarketOperatorAgreement());
		newMarketOperator_.setSuspensionAudit(originalMarketOperator_.getSuspensionAudit());
	}
}
