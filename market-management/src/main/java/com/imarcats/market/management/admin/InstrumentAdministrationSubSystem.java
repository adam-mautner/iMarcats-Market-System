package com.imarcats.market.management.admin;

import java.util.Date;

import com.imarcats.interfaces.client.v100.dto.InstrumentDto;
import com.imarcats.interfaces.client.v100.exception.MarketRuntimeException;
import com.imarcats.interfaces.client.v100.util.DataUtils;
import com.imarcats.interfaces.server.v100.dto.mapping.InstrumentDtoMapping;
import com.imarcats.internal.server.infrastructure.datastore.AssetClassDatastore;
import com.imarcats.internal.server.infrastructure.datastore.AuditTrailEntryDatastore;
import com.imarcats.internal.server.infrastructure.datastore.InstrumentDatastore;
import com.imarcats.internal.server.infrastructure.datastore.MarketDatastore;
import com.imarcats.internal.server.infrastructure.datastore.MarketOperatorDatastore;
import com.imarcats.internal.server.infrastructure.datastore.ProductDatastore;
import com.imarcats.internal.server.infrastructure.timer.MarketTimer;
import com.imarcats.market.management.MarketManagementBase;
import com.imarcats.market.management.MarketManagementContext;
import com.imarcats.market.management.rollover.RolloverManager;
import com.imarcats.market.management.validation.InstrumentValidator;
import com.imarcats.market.management.validation.ValidatorUtils;
import com.imarcats.model.Instrument;
import com.imarcats.model.Rollable;
import com.imarcats.model.types.ActivationStatus;
import com.imarcats.model.types.AuditEntryAction;
import com.imarcats.model.types.AuditInformation;
import com.imarcats.model.types.UnderlyingType;

/**
 * Instrument Administration System 
 * 
 * @author Adam
 *
 */
public class InstrumentAdministrationSubSystem extends MarketManagementBase {

	protected static final String OBJECT_TYPE = Instrument.class.getSimpleName();
	
	private final InstrumentDatastore _instrumentDatastore;
	
	public InstrumentAdministrationSubSystem(ProductDatastore productDatastore_, 
			AssetClassDatastore assetClassDatastore_, 
			InstrumentDatastore instrumentDatastore_, 
			MarketOperatorDatastore marketOperatorDatastore_, 
			MarketDatastore marketDatastore_, 
			AuditTrailEntryDatastore auditTrailEntryDatastore_, 
			MarketTimer marketTimer_) {
		super(productDatastore_, assetClassDatastore_, instrumentDatastore_, marketOperatorDatastore_,
				marketDatastore_, auditTrailEntryDatastore_, marketTimer_);
		_instrumentDatastore = instrumentDatastore_;
	}
	
	/**
	 * Create a new Instrument
	 * @param instrumentDto_ Instrument to be Created
	 * @param user_ User Session 
	 * 
	 * @return Instrument Code
	 */
	public String createInstrument(InstrumentDto instrumentDto_, String user_) {
		Instrument instrument = InstrumentDtoMapping.INSTANCE.fromDto(instrumentDto_);
		
		InstrumentValidator.validateNewInstrument(instrument);

		setupNewInstrument(instrument, user_);

		checkUnderlyingDependencies(instrument);
		
		AuditInformation audit = createAudit(user_);
		instrument.setCreationAudit(audit);
		instrument.updateLastUpdateTimestamp();
		
		String instrumentCode = _instrumentDatastore.createInstrument(instrument);

		addAuditTrailEntry(AuditEntryAction.Created, audit, instrument.getCode(), OBJECT_TYPE);
		
		return instrumentCode; 
	}
	
	/**
	 * Change Instrument 
	 * @param instrumentDto_ Instrument to be changed
	 * @param force_ Force the change, even if the 
	 * 				 Product is in Approved State (It will lose Approval), 
	 * 				 it will only change Instrument, if there are no dependencies on the Instrument
	 * @param user_ User 
	 * 
	 */
	public void changeInstrument(InstrumentDto instrumentDto_, boolean force_, String user_) {
		Instrument originalInstrument = getInstrumentInternal(instrumentDto_.getInstrumentCode());
		
		Instrument newInstrument = InstrumentDtoMapping.INSTANCE.fromDto(instrumentDto_);

		InstrumentValidator.validateInstrumentChange(newInstrument);
		
		checkLastUpdateTimestamp(newInstrument, originalInstrument, 
				MarketRuntimeException.INSTRUMENT_CANNOT_BE_OVERWRITTEN_WITH_AN_OLDER_VERSION);
		
		checkObjectActivationStatusForChange(originalInstrument, force_, 
			MarketRuntimeException.INSTRUMENT_ONLY_IN_CREATED_OR_SUSPEDED_STATE_CAN_BE_CHANGED, 
			UnderlyingType.Instrument, MarketRuntimeException.INSTRUMENT_THAT_HAS_NO_APPROVED_DEPENDENT_INSTRUMENTS_OR_MARKETS_CAN_BE_FORCED_TO_BE_CHANGED);

		checkUnderlyingDependencies(newInstrument);
		
		setupChangedInstrument(originalInstrument, newInstrument);

		AuditInformation audit = createAudit(user_);
		newInstrument.setChangeAudit(audit);
		newInstrument.updateLastUpdateTimestamp();
		
		_instrumentDatastore.updateInstrument(newInstrument); 
		
		addAuditTrailEntry(AuditEntryAction.Changed, audit, newInstrument.getCode(), OBJECT_TYPE);
	}
	
	/**
	 * Rollover Instrument
	 * @param instrumentCode_ Code of the Instrument (Source) to be Rolled Over to the given Instrument
	 * @param rolledOverInstrumentInputDto_ New Instrument to be the rolled over version of the above Instrument
	 * @param user_ User 
	 * 
	 */
	public void rolloverInstrument(String instrumentCode_, InstrumentDto rolledOverInstrumentInputDto_, String user_) {		
		Instrument originalInstrument = getInstrumentInternal(instrumentCode_);
		
		RolloverManager.checkActivationStatus(originalInstrument);
		
		Instrument newRolledInstrument = InstrumentDtoMapping.INSTANCE.fromDto(rolledOverInstrumentInputDto_);

		InstrumentValidator.validateRolloverInstrument(newRolledInstrument, originalInstrument);

		Rollable underlying = getUnderlying(newRolledInstrument);
		
		// copy user fields
		setupChangedInstrument(originalInstrument, newRolledInstrument);
		
		RolloverManager.rollover(originalInstrument, newRolledInstrument, underlying, user_);
		
		newRolledInstrument.updateLastUpdateTimestamp();
		
		_instrumentDatastore.createInstrument(newRolledInstrument);
		
		addAuditTrailEntry(AuditEntryAction.Rolled, newRolledInstrument.getRolloverAudit(), newRolledInstrument.getCode(), OBJECT_TYPE);
	}
	
	/**
	 * Delete Instrument
	 * @param instrumentCode_ Instrument to be Deleted
	 * @param user_ User 
	 * @param context_ Market Management Context 
	 */
	public void deleteInstrument(String instrumentCode_, String user_, MarketManagementContext context_) {
		Instrument instrument = getInstrumentInternal(instrumentCode_);
		
		checkForObjectForDelete(instrument, MarketRuntimeException.INSTRUMENT_ONLY_IN_CREATED_OR_SUSPEDED_STATE_CAN_BE_DELETED);
		
		checkIfDoesNotHaveDependenentObjects(instrument.getCode(), DependencyType.UnderlyingInstrumentCode, 
				MarketRuntimeException.INSTRUMENT_THAT_HAS_NO_DEPENDENT_INSTRUMENTS_OR_MARKETS_CAN_BE_DELETED);

		_instrumentDatastore.deleteInstrument(instrument.getCode());
		
		addAuditTrailEntry(AuditEntryAction.Deleted, createAudit(user_), instrument.getCode(), OBJECT_TYPE);
	}
	
	/**
	 * Approve a Instrument 
	 * @param instrumentCode_ Instrument to be Approved
	 * @param lastUpdateTimestamp_ Last Update Timestamp of the Object
	 * @param user_ User 
	 * 
	 */
	public void approveInstrument(String instrumentCode_, Date lastUpdateTimestamp_, String user_) {		
		Instrument instrument = getInstrumentInternal(instrumentCode_);
		checkLastUpdateTimestampForApproval(lastUpdateTimestamp_, instrument);
		
		checkIfObjectInactive(instrument, MarketRuntimeException.INSTRUMENT_ONLY_IN_CREATED_OR_SUSPEDED_STATE_CAN_BE_APPROVED);
		
		Rollable underlying = getUnderlying(instrument);
		checkIfObjectActive(underlying, MarketRuntimeException.INSTRUMENT_CAN_ONLY_BE_APPROVED_IF_UNDERLYING_IS_APPROVED);
		
		if(!ValidatorUtils.isNonNullString(instrument.getMasterAgreementDocument())) {
			throw MarketRuntimeException.createExceptionWithDetails(
					MarketRuntimeException.INSTRUMENT_MUST_HAVE_MASTER_AGREEMENT_DOCUMENT, 
					null, new Object[] { instrument });
		}
		if(!ValidatorUtils.isValidString(instrument.getAssetClassName())) {
			throw MarketRuntimeException.createExceptionWithDetails(
					MarketRuntimeException.INSTRUMENT_MUST_HAVE_ASSET_CLASS_NAME_FOR_APPROVAL, 
					null, new Object[] { instrument });
		}	
		
		instrument.setActivationDate(new Date());
		instrument.setActivationStatus(ActivationStatus.Approved);
		instrument.setApprovalAudit(createAudit(user_));
		instrument.updateLastUpdateTimestamp();

		// TODO: Save to historical datastore 
		
		addAuditTrailEntry(AuditEntryAction.Approved, instrument.getApprovalAudit(), instrument.getCode(), OBJECT_TYPE);
	}

	/**
	 * Set Master Agreement Document on Instrument
	 * @param instrumentCode_ Instrument to be Changed 
	 * @param masterAgreementDocument_ Master Agreement Document to be set 
	 * @param user_ User 
	 * @param pm_ Persistence Manager
	 */
	public void setInstrumentMasterAgreementDocument(String instrumentCode_, String masterAgreementDocument_, String user_) {
		Instrument instrument = getInstrumentInternal(instrumentCode_);	

		checkIfObjectInactive(instrument, MarketRuntimeException.INSTRUMENT_ONLY_IN_CREATED_OR_SUSPEDED_STATE_CAN_BE_CHANGED);
		
		instrument.setMasterAgreementDocument(masterAgreementDocument_);
		instrument.updateLastUpdateTimestamp();
		
		addAuditTrailEntry(AuditEntryAction.Changed, createAudit(user_), instrumentCode_, OBJECT_TYPE);
	}
	
	/**
	 * Set Asset Class on Instrument
	 * @param instrumentCode_ Instrument to be Changed 
	 * @param assetClassName_ Name of the Asset Class to be set 
	 * @param user_ User  
	 * @param pm_ Persistence Manager
	 */
	public void setInstrumentAssetClass(String instrumentCode_, String assetClassName_, String user_) {
		getAssetClass(assetClassName_);
		
		Instrument instrument = getInstrumentInternal(instrumentCode_);	
		
		checkIfObjectInactive(instrument, MarketRuntimeException.INSTRUMENT_ONLY_IN_CREATED_OR_SUSPEDED_STATE_CAN_BE_CHANGED);
		
		instrument.setAssetClassName(assetClassName_);
		instrument.updateLastUpdateTimestamp();
		
		addAuditTrailEntry(AuditEntryAction.Changed, createAudit(user_), instrumentCode_, OBJECT_TYPE);
	}
	
	/**
	 * Suspend a Instrument 
	 * @param instrumentCode_ Instrument to be Suspended
	 * @param user_ User 
	 * 
	 */
	public void suspendInstrument(String instrumentCode_, String user_) {
		Instrument instrument = getInstrumentInternal(instrumentCode_);
		
		checkIfDependentObjectsInactive(instrument, UnderlyingType.Instrument, MarketRuntimeException.INSTRUMENT_CAN_ONLY_BE_SUSPENDED_IF_DEPENDENT_OBJECTS_ARE_SUSPENDED);
		
		checkIfObjectActive(instrument, MarketRuntimeException.INSTRUMENT_NOT_IN_CREATED_OR_SUSPEDED_STATE_CAN_BE_SUSPENDED);
		
		instrument.setActivationStatus(ActivationStatus.Suspended);
		instrument.setActivationDate(null);
		instrument.setSuspensionAudit(createAudit(user_));
		instrument.updateLastUpdateTimestamp();

		addAuditTrailEntry(AuditEntryAction.Suspended, instrument.getSuspensionAudit(), instrument.getCode(), OBJECT_TYPE);
	}

	public static void setupChangedInstrument(Instrument originalInstrument_,
			Instrument newInstrument_) {
		newInstrument_.setActivationDate(originalInstrument_.getActivationDate());
		newInstrument_.setActivationStatus(originalInstrument_.getActivationStatus());
		newInstrument_.setApprovalAudit(originalInstrument_.getApprovalAudit());
		newInstrument_.setCodeRolledFrom(originalInstrument_.getInstrumentCodeRolledOverFrom());
		newInstrument_.setAssetClassName(originalInstrument_.getAssetClassName());
 		newInstrument_.setCreationAudit(originalInstrument_.getCreationAudit());
		newInstrument_.setChangeAudit(originalInstrument_.getChangeAudit());
		newInstrument_.setMasterAgreementDocument(originalInstrument_.getMasterAgreementDocument());
		newInstrument_.setRolloverAudit(originalInstrument_.getRolloverAudit());
		newInstrument_.setSuspensionAudit(originalInstrument_.getSuspensionAudit());
	}	
	
	private void setupNewInstrument(Instrument instrument_, String user_) {
		// capitalize instrument code
		instrument_.setInstrumentCode(DataUtils.adjustCodeToStandard(instrument_.getInstrumentCode()));
		
		instrument_.setActivationStatus(ActivationStatus.Created);
	}
}
