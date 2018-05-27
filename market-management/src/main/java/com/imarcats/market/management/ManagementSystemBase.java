package com.imarcats.market.management;

import java.util.Date;

import com.imarcats.interfaces.client.v100.dto.AssetClassDto;
import com.imarcats.interfaces.client.v100.dto.InstrumentDto;
import com.imarcats.interfaces.client.v100.dto.MarketDto;
import com.imarcats.interfaces.client.v100.dto.MarketOperatorDto;
import com.imarcats.interfaces.client.v100.dto.ProductDto;
import com.imarcats.interfaces.client.v100.dto.types.ActivationStatus;
import com.imarcats.interfaces.client.v100.dto.types.PagedAssetClassListDto;
import com.imarcats.interfaces.client.v100.dto.types.PagedAuditTrailEntryListDto;
import com.imarcats.interfaces.client.v100.dto.types.PagedInstrumentListDto;
import com.imarcats.interfaces.client.v100.dto.types.PagedMarketListDto;
import com.imarcats.interfaces.client.v100.dto.types.PagedMarketOperatorListDto;
import com.imarcats.interfaces.client.v100.dto.types.PagedProductListDto;
import com.imarcats.interfaces.client.v100.exception.MarketRuntimeException;
import com.imarcats.interfaces.client.v100.util.DataUtils;
import com.imarcats.interfaces.server.v100.dto.mapping.AssetClassDtoMapping;
import com.imarcats.interfaces.server.v100.dto.mapping.AuditEntryDtoMapping;
import com.imarcats.interfaces.server.v100.dto.mapping.InstrumentDtoMapping;
import com.imarcats.interfaces.server.v100.dto.mapping.MarketDtoMapping;
import com.imarcats.interfaces.server.v100.dto.mapping.MarketOperatorDtoMapping;
import com.imarcats.interfaces.server.v100.dto.mapping.ProductDtoMapping;
import com.imarcats.internal.server.infrastructure.datastore.AssetClassDatastore;
import com.imarcats.internal.server.infrastructure.datastore.AuditTrailEntryDatastore;
import com.imarcats.internal.server.infrastructure.datastore.InstrumentDatastore;
import com.imarcats.internal.server.infrastructure.datastore.MarketDatastore;
import com.imarcats.internal.server.infrastructure.datastore.MarketOperatorDatastore;
import com.imarcats.internal.server.infrastructure.datastore.ProductDatastore;
import com.imarcats.internal.server.infrastructure.notification.properties.PropertyChangeSession;
import com.imarcats.internal.server.infrastructure.timer.MarketTimer;
import com.imarcats.internal.server.interfaces.market.MarketInternal;
import com.imarcats.internal.server.interfaces.market.MarketPropertyChangeExecutor;
import com.imarcats.market.management.validation.ValidatorUtils;
import com.imarcats.model.ActivatableMarketObject;
import com.imarcats.model.AssetClass;
import com.imarcats.model.AuditTrailEntry;
import com.imarcats.model.Instrument;
import com.imarcats.model.Market;
import com.imarcats.model.MarketOperator;
import com.imarcats.model.Product;
import com.imarcats.model.Rollable;
import com.imarcats.model.types.AuditEntryAction;
import com.imarcats.model.types.AuditInformation;
import com.imarcats.model.types.PagedInstrumentList;
import com.imarcats.model.types.PagedMarketList;
import com.imarcats.model.types.UnderlyingType;

/**
 * Common Base Class for Management Systems 
 * @author Adam
 */
public abstract class ManagementSystemBase {

	public static final String IMARCATS_BUSINESS_ENTITY_CODE = "IMARCATS";
	
	public static final int MAX_UNDERLYING_DEPENDENCY_DEPTH = 5;
	public static final int MAX_ASSET_CLASS_TREE_DEPTH = 10;
	
	private final ProductDatastore _productDatastore;
	private final InstrumentDatastore _instrumentDatastore;
	private final MarketOperatorDatastore _marketOperatorDatastore;
	private final MarketDatastore _marketDatastore;
	private final AssetClassDatastore _assetClassDatastore;
	private final AuditTrailEntryDatastore _auditTrailEntryDatastore;

	public ManagementSystemBase(ProductDatastore productDatastore_, 
			AssetClassDatastore assetClassDatastore_,
			InstrumentDatastore instrumentDatastore_, 
			MarketOperatorDatastore marketOperatorDatastore_,
			MarketDatastore marketDatastore_,
			AuditTrailEntryDatastore auditTrailEntryDatastore_,
			MarketTimer marketTimer_) {
		super();
		_productDatastore = productDatastore_;
		_assetClassDatastore = assetClassDatastore_;
		_instrumentDatastore = instrumentDatastore_;
		_marketDatastore = marketDatastore_;
		_marketOperatorDatastore = marketOperatorDatastore_;
		_auditTrailEntryDatastore = auditTrailEntryDatastore_; 
	}

	protected void checkLastUpdateTimestamp(ActivatableMarketObject newObject_,
			ActivatableMarketObject originalObject_, MarketRuntimeException marketRuntimeException_) {
		if(newObject_.getLastUpdateTimestamp() != null && originalObject_.getLastUpdateTimestamp() != null) {
			if(newObject_.getLastUpdateTimestamp().getTime() < originalObject_.getLastUpdateTimestamp().getTime()) {
				throw MarketRuntimeException.createExceptionWithDetails(
						marketRuntimeException_, 
						null, new Object[] { newObject_, originalObject_ });
			}
		}
	}
	
	protected void checkLastUpdateTimestampForApproval(Date lastUpdateTimestamp_,
			ActivatableMarketObject originalObject_) {
		if(lastUpdateTimestamp_ != null && originalObject_.getLastUpdateTimestamp() != null) {
			if(lastUpdateTimestamp_.getTime() < originalObject_.getLastUpdateTimestamp().getTime()) {
				throw MarketRuntimeException.createExceptionWithDetails(
						MarketRuntimeException.STALE_OBJECT_CANNOT_BE_APPROVED, 
						null, new Object[] { originalObject_ });
			}
		}
	}
	
	protected void addAuditTrailEntry(AuditEntryAction action_, AuditInformation auditInformation_, String relatedObjectID_, String relatedObjectType_) {
		AuditTrailEntry auditTrailEntry = new AuditTrailEntry();
		
		auditTrailEntry.setAuditEntryAction(action_);
		auditTrailEntry.setRelatedInformation(relatedObjectID_);
		auditTrailEntry.setObjectTypeStr(relatedObjectType_);
		auditTrailEntry.setUserID(auditInformation_.getUserID());
		auditTrailEntry.setDateTime(auditInformation_.getDateTime());
		
		_auditTrailEntryDatastore.createAuditTrailEntry(auditTrailEntry);
	}
	
	/**
	 * Gets All Audit Trail Entries
	 * @param cursorString_ Cursor String for continuing a Paged Query or Null for a new Paged Query
	 * @param maxNumberOfAuditTrailEntryOnPage_ Max Number of Audit Trail Entries on a Page   
	 * 
	 * @return Paged Audit Trail Entry List
	 */
	protected PagedAuditTrailEntryListDto getAllAuditTrailEntriesFromCursor(String cursorString_, int maxNumberOfAuditTrailEntryOnPage_) {
		return AuditEntryDtoMapping.INSTANCE.toDto(_auditTrailEntryDatastore.findAllAuditTrailEntriesFromCursor(cursorString_, maxNumberOfAuditTrailEntryOnPage_));
	}
	
	/**
	 * Checks the Underlying Dependencies of the given Market 
	 * @param market_
	 * @return Underlying Instrument
	 */
	protected Instrument checkUnderlyingDependencies(Market market_) {
		getMarketOperator(market_.getMarketOperatorCode());	
		return getInstrumentInternal(market_.getInstrumentCode());
	}
	
	/**
	 * Checks the Underlying Dependencies of the given Instrument for circular and too deep dependencies (Max 5 levels)
	 * @param instrument_
	 */
	protected void checkUnderlyingDependencies(Instrument instrument_) {
		Rollable underlying = getUnderlying(instrument_); 
		UnderlyingType underlyingType = instrument_.getUnderlyingType();
		int depthCount = 0;
		while (ValidatorUtils.isValidString(underlying.getUnderlyingCode())) {
			if(depthCount >= MAX_UNDERLYING_DEPENDENCY_DEPTH) {
				throw MarketRuntimeException.createExceptionWithDetails(MarketRuntimeException.TOO_DEEP_UNDERLYING_DEPENDENCY, null, new Object[] { instrument_, underlying, depthCount });	
			}
			if(instrument_.getInstrumentCode().equals(underlying.getCode()) &&
			   underlyingType == UnderlyingType.Instrument) {
				throw MarketRuntimeException.createExceptionWithDetails(MarketRuntimeException.CIRCULAR_UNDERLYING_DEPENDENCY, null, new Object[] { instrument_, underlying, depthCount });
			}
			
			depthCount++;
			underlyingType = underlying.getUnderlyingType();
			underlying = getUnderlying(underlying); 
		}
	}

	protected static AuditInformation createAudit(String userName) {
		AuditInformation approvalAudit = new AuditInformation();
		approvalAudit.setDateTime(new Date());
		approvalAudit.setUserID(userName);
		return approvalAudit;
	}
	
	/**
	 * Get underlying for an Object
	 * @param object_ Object 
	 * @return Underlying 
	 */
	protected Rollable getUnderlying(Rollable object_) {
		Rollable underlying = null; 
		if(ValidatorUtils.isValidString(object_.getUnderlyingCode())) {
			if(object_.getUnderlyingType() == UnderlyingType.Instrument) {
				underlying = _instrumentDatastore.findInstrumentByCode(object_.getUnderlyingCode());
			} else if(object_.getUnderlyingType() == UnderlyingType.Product) {
				underlying = _productDatastore.findProductByCode(object_.getUnderlyingCode());
			}
		}
		
		if(underlying == null) {
			throw MarketRuntimeException.createExceptionWithDetails(MarketRuntimeException.NON_EXISTENT_UNDERLYING, null, new Object[] { object_.getUnderlyingCode(), object_.getUnderlyingType() });
		}
		
		return underlying;
	}
	
	/**
	 * Get Product by Code 
	 * @param productCode_ Product Code 
	 * @param  Persistence Manager 
	 * @return Product 
	 */
	public ProductDto getProduct(String productCode_) {
		Product product = getProductInternal(productCode_);
		return ProductDtoMapping.INSTANCE.toDto(product);
	}

	protected Product getProductInternal(String productCode_) {
		checkProductCode(productCode_);
		Product product = _productDatastore.findProductByCode(productCode_);
		if(product == null) {
			throw MarketRuntimeException.createExceptionWithDetails(MarketRuntimeException.NON_EXISTENT_PRODUCT, null, new Object[] { productCode_ });
		}
		return product;
	}

	private void checkProductCode(String productCode_) {
		if(!DataUtils.isValidObjectIdString(productCode_)) {
			throw MarketRuntimeException.createExceptionWithDetails(MarketRuntimeException.PRODUCT_MUST_HAVE_PRODUCT_VALID_CODE, null, new Object[] { DataUtils.VALID_ID });
		}
	}
	
	/**
	 * Get Asset Class by Name 
	 * @param assetClassName_ Name of the Asset Class 
	 * @param userSession_ User Session 
	 * @param  Persistence Manager 
	 * @return Asset Class 
	 */
	public AssetClassDto getAssetClass(String assetClassName_) {
		checkAssetClassName(assetClassName_);
		AssetClass assetClass = _assetClassDatastore.findAssetClassByName(assetClassName_);
		if(assetClass == null) {
			throw MarketRuntimeException.createExceptionWithDetails(MarketRuntimeException.NON_EXISTENT_ASSET_CLASS, null, new Object[] { assetClassName_ });
		}
		return AssetClassDtoMapping.INSTANCE.toDto(assetClass);
	}

	protected void checkAssetClassName(String assetClassName_) {
		// TODO: In the prototype, we have changed the too strict validation to a less strict one
		// 		 Change it back, when it is released to Prod 
		// if(!DataUtils.isValidObjectIdString(assetClassName_)) {
		if(!DataUtils.isValidString(assetClassName_)) {
			throw MarketRuntimeException.createExceptionWithDetails(MarketRuntimeException.ASSET_CLASS_WITHOUT_NAME, null, new Object[] { DataUtils.VALID_ID });
		}
	}
	
	/**
	 * Gets Asset Classes by Parent
	 * 
	 * @param parentAssetClassName_ Parent Asset Class Name 
	 * @param cursorString_ Cursor String for continuing a Paged Query or Null for a new Paged Query
	 * @param maxNumberOfOrderOnPage_ Max Number of Asset Classes on a Page  
	 * @param  Persistence Manager 
	 * 
	 * @return Paged Asset Class List
	 */
	public PagedAssetClassListDto getAssetClassesFromCursorByParent(String parentAssetClassName_, String cursorString_, int maxNumberOfAssetClassesOnPage_) {
		checkAssetClassName(parentAssetClassName_);
		return AssetClassDtoMapping.INSTANCE.toDto(_assetClassDatastore.findAssetClassesFromCursorByParent(parentAssetClassName_, cursorString_, maxNumberOfAssetClassesOnPage_));	
	}
	
	/**
	 * Gets All Asset Classes
	 * 
	 * @param cursorString_ Cursor String for continuing a Paged Query or Null for a new Paged Query
	 * @param maxNumberOfOrderOnPage_ Max Number of Asset Classes on a Page  
	 * @param  Persistence Manager 
	 * 
	 * @return Paged Asset Class List
	 */
	public PagedAssetClassListDto getAllAssetClassesFromCursor(String cursorString_, int maxNumberOfAssetClassesOnPage_) {
		return AssetClassDtoMapping.INSTANCE.toDto(_assetClassDatastore.findAllAssetClassesFromCursor(cursorString_, maxNumberOfAssetClassesOnPage_));	
	}
	
	/**
	 * Gets All Top-Level Asset Classes (with No Parent)
	 * 
	 * @param cursorString_ Cursor String for continuing a Paged Query or Null for a new Paged Query
	 * @param maxNumberOfOrderOnPage_ Max Number of Asset Classes on a Page  
	 * @param  Persistence Manager 
	 * 
	 * @return Paged Asset Class List
	 */
	public PagedAssetClassListDto getAllTopLevelAssetClassesFromCursor(String cursorString_, int maxNumberOfAssetClassesOnPage_) {
		return AssetClassDtoMapping.INSTANCE.toDto(_assetClassDatastore.findAllTopLevelAssetClassesFromCursor(cursorString_, maxNumberOfAssetClassesOnPage_));	
	}
	
	
	/**
	 * Gets Products by Activation Status
	 * @param activationStatus_ Activation Status 
	 * @param cursorString_ Cursor String for continuing a Paged Query or Null for a new Paged Query
	 * @param maxNumberOfProductsOnPage_ Max Number of Products on a Page  
	 * @param  Persistence Manager 
	 * @return Paged list of Products
	 */
	public PagedProductListDto getProductsFromCursorByActivationStatus(
			com.imarcats.interfaces.client.v100.dto.types.ActivationStatus activationStatus_, String cursorString_,
			int maxNumberOfProductsOnPage_) { 
		return ProductDtoMapping.INSTANCE.toDto(_productDatastore.findProductsFromCursorByActivationStatus(
				ProductDtoMapping.INSTANCE.fromDto(activationStatus_), cursorString_, maxNumberOfProductsOnPage_));
	}
	
	/**
	 * Get Instrument by Code 
	 * @param instrumentCode_ Instrument Code  
	 * @param  Persistence Manager 
	 * @return Instrument
	 */
	public InstrumentDto getInstrument(String instrumentCode_) {
		Instrument instrument = getInstrumentInternal(instrumentCode_);
		return InstrumentDtoMapping.INSTANCE.toDto(instrument);
	}

	protected Instrument getInstrumentInternal(String instrumentCode_) {
		checkInstrumentCode(instrumentCode_);
		Instrument instrument = _instrumentDatastore.findInstrumentByCode(instrumentCode_);
		if(instrument == null) {
			throw MarketRuntimeException.createExceptionWithDetails(MarketRuntimeException.NON_EXISTENT_INSTRUMENT, null, new Object[] { instrumentCode_ });
		}
		return instrument;
	}

	private void checkInstrumentCode(String instrumentCode_) {
		if(!DataUtils.isValidObjectIdString(instrumentCode_)) {
			throw MarketRuntimeException.createExceptionWithDetails(MarketRuntimeException.INSTRUMENT_MUST_HAVE_VALID_CODE,  null, new Object[] { DataUtils.VALID_ID });
		}
	}

	/**
	 * Gets Instruments by Activation Status
	 * @param activationStatus_ Activation Status 
	 * @param cursorString_ Cursor String for continuing a Paged Query or Null for a new Paged Query
	 * @param maxNumberOfInstrumentsOnPage_ Max Number of Instruments on a Page  
	 * @param  Persistence Manager 
	 * @return Paged list of Instruments
	 */
	public PagedInstrumentListDto getInstrumentFromCursorByActivationStatus(
			ActivationStatus activationStatus_, String cursorString_,
			int maxNumberOfInstrumentsOnPage_) {
		return InstrumentDtoMapping.INSTANCE.toDto(_instrumentDatastore.findInstrumentsFromCursorByActivationStatus(InstrumentDtoMapping.INSTANCE.fromDto(activationStatus_), cursorString_, maxNumberOfInstrumentsOnPage_));
	}
	
	/**
	 * Get Market Operator by Code 
	 * @param marketOperatorCode_ Market Operator Code  
	 * @param  Persistence Manager 
	 * @return Market Operator
	 */
	public MarketOperatorDto getMarketOperator(String marketOperatorCode_) {
		MarketOperator marketOperator = getMarketOperatorInternal(
				marketOperatorCode_);
		return MarketOperatorDtoMapping.INSTANCE.toDto(marketOperator);
	}

	protected MarketOperator getMarketOperatorInternal(
			String marketOperatorCode_) {
		checkMarketOperatorCode(marketOperatorCode_);
		MarketOperator marketOperator = _marketOperatorDatastore.findMarketOperatorByCode(marketOperatorCode_);
		if(marketOperator == null) {
			throw MarketRuntimeException.createExceptionWithDetails(MarketRuntimeException.NON_EXISTENT_MARKET_OPERATOR, null, new Object[] { marketOperatorCode_ });
		}
		return marketOperator;
	}

	private void checkMarketOperatorCode(String marketOperatorCode_) {
		if(!DataUtils.isValidObjectIdString(marketOperatorCode_)) {
			throw MarketRuntimeException.createExceptionWithDetails(MarketRuntimeException.MARKET_OPERATOR_MUST_HAVE_CODE, null, new Object[] { DataUtils.VALID_ID });
		}
	}

	/**
	 * Gets Market Operators by Activation Status
	 * @param activationStatus_ Activation Status 
	 * @param cursorString_ Cursor String for continuing a Paged Query or Null for a new Paged Query
	 * @param maxNumberOfMarketOperatorsOnPage_ Max Number of Market Operators on a Page  
	 * @param  Persistence Manager 
	 * @return Paged list of Market Operators
	 */ 
	public PagedMarketOperatorListDto getMarketOperatorFromCursorByActivationStatus(
			ActivationStatus activationStatus_, String cursorString_,
			int maxNumberOfMarketOperatorsOnPage_) {
		return MarketOperatorDtoMapping.INSTANCE.toDto(_marketOperatorDatastore.findMarketOperatorsFromCursorByActivationStatus(MarketOperatorDtoMapping.INSTANCE.fromDto(activationStatus_), cursorString_, maxNumberOfMarketOperatorsOnPage_));
	}
	
	/**
	 * Gets Market Operators by User
	 * @param userID_ User ID
	 * @param cursorString_ Cursor String for continuing a Paged Query or Null for a new Paged Query
	 * @param maxNumberOfMarketOperatorsOnPage_ Max Number of Market Operators on a Page  
	 * @param  
	 * @param  Persistence Manager  
	 * @return Paged list of Market Operators
	 */ 
	public PagedMarketOperatorListDto getMarketOperatorFromCursorForUser(String userID_, String cursorString_,
			int maxNumberOfMarketOperatorsOnPage_) {
		return MarketOperatorDtoMapping.INSTANCE.toDto(_marketOperatorDatastore.findMarketOperatorsFromCursorByUser(userID_, cursorString_, maxNumberOfMarketOperatorsOnPage_));
	}
	
	/**
	 * Get Market by Code 
	 * @param marketCode_ Market Code  
	 * @param  Persistence Manager 
	 * @return Market 
	 */
	public MarketDto getMarket(String marketCode_) {
		Market marketModel = getMarketInternal(marketCode_);
		return MarketDtoMapping.INSTANCE.toDto(marketModel);
	}

	protected Market getMarketInternal(String marketCode_) {
		checkMarketCode(marketCode_);
		MarketInternal market = _marketDatastore.findMarketBy(marketCode_);
		if(market == null) {
			throw MarketRuntimeException.createExceptionWithDetails(MarketRuntimeException.NON_EXISTENT_MARKET, null, new Object[] { marketCode_ });
		}
		Market marketModel = market.getMarketModel();
		return marketModel;
	}

	private void checkMarketCode(String marketCode_) {
		if(!DataUtils.isValidMarketIdString(marketCode_)) {
			throw MarketRuntimeException.createExceptionWithDetails(MarketRuntimeException.MARKET_MUST_HAVE_CODE, null, new Object[] { DataUtils.VALID_MARKET_ID });
		}
	}
	
	/**
	 * Gets Markets by Activation Status
	 * @param activationStatus_ Activation Status 
	 * @param cursorString_ Cursor String for continuing a Paged Query or Null for a new Paged Query
	 * @param maxNumberOfMarketsOnPage_ Max Number of Markets on a Page  
	 * @param  Persistence Manager 
	 * @return Paged list of Markets
	 */ 
	public PagedMarketListDto getMarketFromCursorByActivationStatus(
			ActivationStatus activationStatus_, String cursorString_,
			int maxNumberOfMarketsOnPage_) {
		return MarketDtoMapping.INSTANCE.toDto(_marketDatastore.findMarketModelsFromCursorByActivationStatus(MarketDtoMapping.INSTANCE.fromDto(activationStatus_), cursorString_, maxNumberOfMarketsOnPage_));
	}
	
	protected void checkIfObjectInactive(ActivatableMarketObject object_, MarketRuntimeException exceptionOnActive_) {
		if(object_.getActivationStatus() != com.imarcats.model.types.ActivationStatus.Created && 
		   object_.getActivationStatus() != com.imarcats.model.types.ActivationStatus.Suspended) {
				throw MarketRuntimeException.createExceptionWithDetails(
						exceptionOnActive_, 
						null, new Object[] { object_.getCode() });
		}
	}
	
	protected void checkIfObjectActive(ActivatableMarketObject object_, MarketRuntimeException exceptionOnInactive_) {
		if(object_.getActivationStatus() == com.imarcats.model.types.ActivationStatus.Created || 
		   object_.getActivationStatus() == com.imarcats.model.types.ActivationStatus.Suspended || 
		   object_.getActivationStatus() == com.imarcats.model.types.ActivationStatus.Deleted) {
				throw MarketRuntimeException.createExceptionWithDetails(
						exceptionOnInactive_, 
						null, new Object[] { object_.getCode() });

		}
	}
	
	protected void checkForObjectForDelete(ActivatableMarketObject object_, MarketRuntimeException marketRuntimeException_) {
		if(object_.getActivationStatus() != com.imarcats.model.types.ActivationStatus.Created && 
		   object_.getActivationStatus() != com.imarcats.model.types.ActivationStatus.Suspended) {
			throw MarketRuntimeException.createExceptionWithDetails(
					marketRuntimeException_, 
					null, new Object[] { object_.getCode() });
		}
	}
	
	protected void checkObjectActivationStatusForChange(Rollable object_, boolean force_, MarketRuntimeException exceptionOnActiveStatus_, UnderlyingType objectType_, MarketRuntimeException exceptionOnDependentObject_) {
		if(object_.getActivationStatus() != com.imarcats.model.types.ActivationStatus.Created && 
		   object_.getActivationStatus() != com.imarcats.model.types.ActivationStatus.Suspended) {
			if(force_) {
				checkIfDependentObjectsInactive(object_, objectType_, 
						exceptionOnDependentObject_);

				object_.setActivationStatus(com.imarcats.model.types.ActivationStatus.Created);
				object_.setActivationDate(null);
			} else {
				throw MarketRuntimeException.createExceptionWithDetails(
						exceptionOnActiveStatus_, 
						null, new Object[] { object_.getCode() });
			}
		}
	}
	
	protected void checkIfDependentObjectsInactive(Rollable object_, UnderlyingType objectType_, MarketRuntimeException errorException_) {
		Instrument[] dependentInstruments = _instrumentDatastore.findInstrumentsByUnderlying(object_.getCode(), objectType_);
		
		for (Instrument instrument : dependentInstruments) {
			checkIfObjectInactive(instrument, errorException_);
		}
		
		if(objectType_ == UnderlyingType.Instrument) {
			Market[] dependentMarkets = _marketDatastore.findMarketModelsByInstrument(object_.getCode());
			
			for (Market market : dependentMarkets) {
				checkIfObjectInactive(market, errorException_);
			}
		}
	}

	protected void checkMarketActivationStatusForChange(Market market_, boolean force_, MarketRuntimeException exceptionOnActiveStatus_, MarketRuntimeException exceptionOnApprovedStatus_, PropertyChangeSession propertyChangeSession_) {
		if(market_.getActivationStatus() == com.imarcats.model.types.ActivationStatus.Activated) {
			throw MarketRuntimeException.createExceptionWithDetails(
					exceptionOnActiveStatus_, 
					null, new Object[] { market_.getCode() });			
		}
		
		if(market_.getActivationStatus() != com.imarcats.model.types.ActivationStatus.Created && 
		   market_.getActivationStatus() != com.imarcats.model.types.ActivationStatus.Suspended) {
			if(force_) {
				MarketPropertyChangeExecutor marketPropertyChangeExecutor = new MarketPropertyChangeExecutor(market_);
				
				marketPropertyChangeExecutor.setActivationStatus(com.imarcats.model.types.ActivationStatus.Created, propertyChangeSession_);
			} else {
				throw MarketRuntimeException.createExceptionWithDetails(
						exceptionOnApprovedStatus_, 
						null, new Object[] { market_.getCode() });
			}
		}
	}
	
	protected void checkMarketOperatorActivationStatusForChange(MarketOperator marketOperator_, boolean force_, MarketRuntimeException exceptionOnActiveStatus_, MarketRuntimeException exceptionOnDependentObject_) {
		if(marketOperator_.getActivationStatus() != com.imarcats.model.types.ActivationStatus.Created && 
		   marketOperator_.getActivationStatus() != com.imarcats.model.types.ActivationStatus.Suspended) {
			if(force_) {
				checkIfDependentMarketsInactive(marketOperator_, 
						exceptionOnDependentObject_);

				marketOperator_.setActivationStatus(com.imarcats.model.types.ActivationStatus.Created);
			} else {
				throw MarketRuntimeException.createExceptionWithDetails(
						exceptionOnActiveStatus_, 
						null, new Object[] { marketOperator_.getCode() });
			}
		}
	}
	
	protected void checkIfDependentMarketsInactive(MarketOperator marketOperator_, MarketRuntimeException errorException_) {
		Market[] dependentMarkets = _marketDatastore.findMarketModelsByMarketOperator(marketOperator_.getCode());
		
		for (Market market : dependentMarkets) {
			checkIfObjectInactive(market, errorException_);
		}
	
	}

	// TODO: Refactor 
	protected void checkIfDoesNotHaveDependenentObjects(String objectCodeName_, DependencyType dependencyType_, MarketRuntimeException errorException_) {
		PagedInstrumentList instrumentList = null;

		if(dependencyType_ == DependencyType.AssetClassName) {
			instrumentList = _instrumentDatastore.findInstrumentsFromCursorByAssetClass(objectCodeName_, null, 1);
		} else if(dependencyType_ == DependencyType.UnderlyingProductCode) {
			instrumentList = _instrumentDatastore.findInstrumentsFromCursorByUnderlying(objectCodeName_, UnderlyingType.Product, null, 1);
		} else if(dependencyType_ == DependencyType.UnderlyingInstrumentCode) {
			instrumentList = _instrumentDatastore.findInstrumentsFromCursorByUnderlying(objectCodeName_, UnderlyingType.Instrument, null, 1);
		} else if(dependencyType_ == DependencyType.UnderlyingMarketOperatorCode) {
			// does nothing
		} else {
			throw new IllegalArgumentException("Unknown Dependency Type");
		}
		
		if(instrumentList != null && instrumentList.getInstruments().length > 0) {
			throw MarketRuntimeException.createExceptionWithDetails(
					errorException_, 
					null, new Object[] { objectCodeName_, Instrument.class.getName() });
		}
		
		if(dependencyType_ == DependencyType.UnderlyingInstrumentCode) {
			PagedMarketList marketList = _marketDatastore.findMarketModelsFromCursorByInstrument(objectCodeName_, null, 1);
			
			if(marketList.getMarkets().length > 0) {
				throw MarketRuntimeException.createExceptionWithDetails(
						errorException_, 
						null, new Object[] { objectCodeName_, Market.class.getName() });
			}
		} else if(dependencyType_ == DependencyType.UnderlyingMarketOperatorCode) {
			PagedMarketList marketList = _marketDatastore.findMarketModelsFromCursorByMarketOperator(objectCodeName_, null, 1);
			
			if(marketList.getMarkets().length > 0) {
				throw MarketRuntimeException.createExceptionWithDetails(
						errorException_, 
						null, new Object[] { objectCodeName_, Market.class.getName() });
			}
		}
	}

	protected enum DependencyType {
		AssetClassName,
		UnderlyingProductCode,
		UnderlyingInstrumentCode,
		UnderlyingMarketOperatorCode;
	}
}
