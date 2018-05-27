package com.imarcats.market.management.admin;

import com.imarcats.interfaces.client.v100.dto.AssetClassDto;
import com.imarcats.interfaces.client.v100.dto.types.PagedAssetClassListDto;
import com.imarcats.interfaces.client.v100.exception.MarketRuntimeException;
import com.imarcats.interfaces.server.v100.dto.mapping.AssetClassDtoMapping;
import com.imarcats.internal.server.infrastructure.datastore.AssetClassDatastore;
import com.imarcats.internal.server.infrastructure.datastore.AuditTrailEntryDatastore;
import com.imarcats.internal.server.infrastructure.datastore.InstrumentDatastore;
import com.imarcats.internal.server.infrastructure.datastore.MarketDatastore;
import com.imarcats.internal.server.infrastructure.datastore.MarketOperatorDatastore;
import com.imarcats.internal.server.infrastructure.datastore.ProductDatastore;
import com.imarcats.internal.server.infrastructure.timer.MarketTimer;
import com.imarcats.market.management.MarketManagementContext;
import com.imarcats.market.management.ManagementSystemBase;
import com.imarcats.market.management.validation.AssetClassValidator;
import com.imarcats.model.AssetClass;
import com.imarcats.model.types.AuditEntryAction;

/**
 * Asset Class Administration System 
 * 
 * @author Adam
 *
 */
public class AssetClassAdministrationSubSystem extends ManagementSystemBase {
	
	private static final String OBJECT_TYPE = AssetClass.class.getSimpleName();
	private final AssetClassDatastore _assetClassDatastore; 

	public AssetClassAdministrationSubSystem(ProductDatastore productDatastore_, 
			AssetClassDatastore assetClassDatastore_, 
			InstrumentDatastore instrumentDatastore_, 
			MarketOperatorDatastore marketOperatorDatastore_, 
			MarketDatastore marketDatastore_, 
			AuditTrailEntryDatastore auditTrailEntryDatastore_, 
			MarketTimer marketTimer_) {
		super(productDatastore_, assetClassDatastore_, instrumentDatastore_, marketOperatorDatastore_,
				marketDatastore_, auditTrailEntryDatastore_, marketTimer_);
		_assetClassDatastore = assetClassDatastore_;
	}
	
	/**
	 * Creates Asset Class
	 * @param assetClassDto_ Asset Class to be Created
	 * @param user_ User 
	 * 
	 * @return Asset Class Name 
	 */
	public String createAssetClass(AssetClassDto assetClassDto_, String user_) {
		AssetClass newAssetClassModel = AssetClassDtoMapping.INSTANCE.fromDto(assetClassDto_);

		AssetClassValidator.validateAssetClass(newAssetClassModel);
		
		checkAssetClassParentDependencies(assetClassDto_);
		
		newAssetClassModel.updateLastUpdateTimestamp();
		
		String assetClassCode = _assetClassDatastore.createAssetClass(newAssetClassModel);

		addAuditTrailEntry(AuditEntryAction.Created, createAudit(user_), assetClassDto_.getName(), OBJECT_TYPE);
		return assetClassCode;
	}
	
	/**
	 * Checks the Asset Class Parent Dependencies of the given Asset Class for circular and too deep dependencies (Max 10 levels)
	 * @param assetClass_
	 */
	protected void checkAssetClassParentDependencies(AssetClassDto assetClass_) {
		AssetClassDto parent = getParent(assetClass_); 
		int depthCount = 0;
		if(parent != null) {
			PagedAssetClassListDto assetClassList = getAssetClassesFromCursorByParent(parent.getName(), null, AssetClass.MAX_NUMBER_OF_SUB_ASSAT_CLASSES_FOR_PARENT);
			if(assetClassList.getAssetClasses().length >= AssetClass.MAX_NUMBER_OF_SUB_ASSAT_CLASSES_FOR_PARENT) {
				throw MarketRuntimeException.createExceptionWithDetails(MarketRuntimeException.TOO_MANY_ASSET_CLASSES_FOR_THIS_PARENT, null, new Object[] { assetClass_, parent, AssetClass.MAX_NUMBER_OF_SUB_ASSAT_CLASSES_FOR_PARENT });
			}
		}
		while (parent != null) {
			if(depthCount >= MAX_ASSET_CLASS_TREE_DEPTH) {
				throw MarketRuntimeException.createExceptionWithDetails(MarketRuntimeException.TOO_DEEP_ASSET_CLASS_TREE, null, new Object[] { assetClass_, parent, depthCount });	
			}
			if(assetClass_.getName().equals(parent.getName())) {
				throw MarketRuntimeException.createExceptionWithDetails(MarketRuntimeException.CIRCULAR_ASSET_CLASS_TREE, null, new Object[] { assetClass_, parent, depthCount });
			}
			
			depthCount++;
			parent = getParent(parent); 
		}
	}
	
	private AssetClassDto getParent(AssetClassDto assetClass_) {
		String parentName = assetClass_.getParentName();
		AssetClassDto parent = null;
		if(parentName != null) {
			parent = getAssetClass(parentName);
		}
		
		return parent;
	}
	
	/**
	 * Changes an Asses Class 
	 * @param assetClassDto_ Asset Class to be Changed
	 * @param user_ User  
	 * 
	 */
	public void changeAssetClass(AssetClassDto assetClassDto_, String userSession_) {
		AssetClass newChangedAssetClassModel = AssetClassDtoMapping.INSTANCE.fromDto(assetClassDto_);

		AssetClassValidator.validateAssetClass(newChangedAssetClassModel);
		
		checkAssetClassParentDependencies(assetClassDto_);
		
		AssetClass originalAssetClassToBeChanged = _assetClassDatastore.findAssetClassByName(newChangedAssetClassModel.getName());
		if(originalAssetClassToBeChanged == null) {
			throw MarketRuntimeException.createExceptionWithDetails(
					MarketRuntimeException.ASSET_CLASS_NAME_CANNOT_BE_CHANGED, 
					null, new Object[] { assetClassDto_ });
		}
		
		checkLastUpdateTimestamp(newChangedAssetClassModel, originalAssetClassToBeChanged, 
				MarketRuntimeException.ASSET_CLASS_CANNOT_BE_OVERWRITTEN_WITH_AN_OLDER_VERSION);
		
		// set system controlled fields 
		newChangedAssetClassModel.setActivationStatus(originalAssetClassToBeChanged.getActivationStatus());
		newChangedAssetClassModel.updateLastUpdateTimestamp();
		
		_assetClassDatastore.updateAssetClass(newChangedAssetClassModel);
		
		addAuditTrailEntry(AuditEntryAction.Changed, createAudit(userSession_), assetClassDto_.getName(), OBJECT_TYPE);
	}
	
	/**
	 * Deletes Asset Class
	 * @param assetClassName_ Asset Class to be Deleted
	 * @param user_ User 
	 * @param context_ Market Management Context 
	 */
	public void deleteAssetClass(String assetClassName_, String user_, MarketManagementContext context_) {
		checkAssetClassName(assetClassName_);
		checkIfDoesNotHaveDependenentObjects(assetClassName_, DependencyType.AssetClassName, 
				MarketRuntimeException.ASSET_CLASS_CANNOT_BE_DELETED_IF_IT_IS_ASSIGNED_TO_INSTRUMENTS);

		_assetClassDatastore.deleteAssetClass(assetClassName_);

		addAuditTrailEntry(AuditEntryAction.Deleted, createAudit(user_), assetClassName_, OBJECT_TYPE);
	}
}
