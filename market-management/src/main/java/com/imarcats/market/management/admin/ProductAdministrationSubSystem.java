package com.imarcats.market.management.admin;

import java.util.Date;

import com.imarcats.interfaces.client.v100.dto.ProductDto;
import com.imarcats.interfaces.client.v100.exception.MarketRuntimeException;
import com.imarcats.interfaces.client.v100.util.DataUtils;
import com.imarcats.interfaces.server.v100.dto.mapping.ProductDtoMapping;
import com.imarcats.internal.server.infrastructure.datastore.AssetClassDatastore;
import com.imarcats.internal.server.infrastructure.datastore.AuditTrailEntryDatastore;
import com.imarcats.internal.server.infrastructure.datastore.InstrumentDatastore;
import com.imarcats.internal.server.infrastructure.datastore.MarketDatastore;
import com.imarcats.internal.server.infrastructure.datastore.MarketOperatorDatastore;
import com.imarcats.internal.server.infrastructure.datastore.ProductDatastore;
import com.imarcats.internal.server.infrastructure.timer.MarketTimer;
import com.imarcats.market.management.MarketManagementContext;
import com.imarcats.market.management.MarketManagementBase;
import com.imarcats.market.management.rollover.RolloverManager;
import com.imarcats.market.management.validation.ProductValidator;
import com.imarcats.market.management.validation.ValidatorUtils;
import com.imarcats.model.Product;
import com.imarcats.model.types.ActivationStatus;
import com.imarcats.model.types.AuditEntryAction;
import com.imarcats.model.types.UnderlyingType;

/**
 * Product Administration Sub-System 
 * 
 * @author Adam
 *
 */
public class ProductAdministrationSubSystem extends MarketManagementBase {
	
	protected static final String OBJECT_TYPE = Product.class.getSimpleName();
	
	private final ProductDatastore _productDatastore;

	public ProductAdministrationSubSystem(ProductDatastore productDatastore_, 
			AssetClassDatastore assetClassDatastore_, 
			InstrumentDatastore instrumentDatastore_, 
			MarketOperatorDatastore marketOperatorDatastore_, 
			MarketDatastore marketDatastore_, 
			AuditTrailEntryDatastore auditTrailEntryDatastore_, 
			MarketTimer marketTimer_) {
		super(productDatastore_, assetClassDatastore_, instrumentDatastore_, marketOperatorDatastore_,
				marketDatastore_, auditTrailEntryDatastore_, marketTimer_);
		_productDatastore = productDatastore_;
	}
	
	/**
	 * Create a new Product
	 * @param productDto_ Product to be Created
	 * @param user_ User 
	 * @return Product Code
	 */
	public String createProduct(ProductDto productDto_, String user_) {
		Product product = ProductDtoMapping.INSTANCE.fromDto(productDto_);
		
		ProductValidator.validateNewProduct(product);

		setupNewProduct(product, user_);

		product.setCreationAudit(createAudit(user_));
		product.updateLastUpdateTimestamp();
		
		String productCode = _productDatastore.createProduct(product);
		
		addAuditTrailEntry(AuditEntryAction.Created, product.getCreationAudit(), product.getCode(), OBJECT_TYPE);
		
		return productCode; 
	}

	/**
	 * Change Product 
	 * @param productDto_ Product to be changed
	 * @param force_ Force the change, even if the 
	 * 				 Product is in Approved State (It will lose Approval), 
	 * 				 it will only change Product, if there are no dependencies on the Product
	 * @param user_ User  
	 *
	 */
	public void changeProduct(ProductDto productDto_, boolean force_, String user_) {
		Product originalProduct = getProductInternal(productDto_.getProductCode());
		Product newProduct = ProductDtoMapping.INSTANCE.fromDto(productDto_);
		
		ProductValidator.validateProductChange(newProduct);

		checkLastUpdateTimestamp(newProduct, originalProduct, 
				MarketRuntimeException.PRODUCT_CANNOT_BE_OVERWRITTEN_WITH_AN_OLDER_VERSION);
		
		checkObjectActivationStatusForChange(originalProduct, force_, 
			MarketRuntimeException.PRODUCT_ONLY_IN_CREATED_OR_SUSPEDED_STATE_CAN_BE_CHANGED, 
			UnderlyingType.Product, MarketRuntimeException.PRODUCT_THAT_HAS_NO_APPROVED_DEPENDENT_INSTRUMENTS_CAN_BE_FORCED_TO_BE_CHANGED);

		setupChangedProduct(originalProduct, newProduct);
		
		newProduct.setChangeAudit(createAudit(user_));
		newProduct.updateLastUpdateTimestamp();
		
		_productDatastore.updateProduct(newProduct); 
		
		addAuditTrailEntry(AuditEntryAction.Changed, newProduct.getChangeAudit(), productDto_.getProductCode(), OBJECT_TYPE);
	}

	/**
	 * Rollover Product
	 * @param productCode_ Code of the Product (Source) to be Rolled Over to the given Product
	 * @param rolledOverProductInputDto_ New Product to be he rolled over version of the above Product
	 * @param user_ User  
	 *
	 */
	public void rolloverProduct(String productCode_, ProductDto rolledOverProductInputDto_, String user_) {
		Product originalProduct = getProductInternal(productCode_);
		
		RolloverManager.checkActivationStatus(originalProduct);
		
		Product newRolledProduct = ProductDtoMapping.INSTANCE.fromDto(rolledOverProductInputDto_);
		
		ProductValidator.validateRolloverProduct(
				newRolledProduct, 
				originalProduct);

		setupChangedProduct(originalProduct, newRolledProduct);
		
		RolloverManager.rollover(originalProduct, newRolledProduct, null, user_);
		newRolledProduct.updateLastUpdateTimestamp();
		
		_productDatastore.createProduct(newRolledProduct);

		addAuditTrailEntry(AuditEntryAction.Rolled, newRolledProduct.getRolloverAudit(), newRolledProduct.getCode(), OBJECT_TYPE);
	}
	
	/**
	 * Delete Product
	 * @param productCode_ Product to be Deleted
	 * @param user_ User  
	 * @param context_ Market Management Context 
	 */
	public void deleteProduct(String productCode_, String user_, MarketManagementContext context_) {
		Product product = getProductInternal(productCode_);
		checkForObjectForDelete(product, MarketRuntimeException.PRODUCT_ONLY_IN_CREATED_OR_SUSPEDED_STATE_CAN_BE_DELETED);
		
		
		checkIfDoesNotHaveDependenentObjects(product.getCode(), DependencyType.UnderlyingProductCode, 
				MarketRuntimeException.PRODUCT_THAT_HAS_NO_DEPENDENT_INSTRUMENTS_CAN_BE_DELETED);
		
		_productDatastore.deleteProduct(product.getCode());
		
		addAuditTrailEntry(AuditEntryAction.Deleted, createAudit(user_), product.getCode(), OBJECT_TYPE); 
	}
	
	/**
	 * Set Product Definition Document on Product
	 * @param productCode_ Product to be Changed 
	 * @param productDefinitionDocument_ Definition Document to be set 
	 * @param user_ User 
	 * @param pm_ Persistence Manager
	 */
	public void setProductDefinitionDocument(String productCode_, String productDefinitionDocument_, String user_) {
		Product product = getProductInternal(productCode_);	
		
		checkIfObjectInactive(product, MarketRuntimeException.PRODUCT_ONLY_IN_CREATED_OR_SUSPEDED_STATE_CAN_BE_CHANGED);
		
		product.setProductDefinitionDocument(productDefinitionDocument_);
		product.updateLastUpdateTimestamp();
		
		addAuditTrailEntry(AuditEntryAction.Changed, createAudit(user_), productCode_, OBJECT_TYPE);
	}
	
	/**
	 * Approve a Product 
	 * @param productCode_ Product to be Approved
	 * @param lastUpdateTimestamp_ Last Update Timestamp of the Object
	 * @param user_ User 
	 *
	 */
	public void approveProduct(String productCode_, Date lastUpdateTimestamp_, String user_) {
		Product product = getProductInternal(productCode_);
		checkLastUpdateTimestampForApproval(lastUpdateTimestamp_, product);
		
		checkIfObjectInactive(product, MarketRuntimeException.PRODUCT_ONLY_IN_CREATED_OR_SUSPEDED_STATE_CAN_BE_APPROVED);
		
		if(!ValidatorUtils.isNonNullString(product.getProductDefinitionDocument())) {
			throw MarketRuntimeException.createExceptionWithDetails(
					MarketRuntimeException.PRODUCT_MUST_HAVE_DEFINITION_DOCUMENT, 
					null, new Object[] { product });
		}
		
		product.setActivationDate(new Date());
		product.setActivationStatus(ActivationStatus.Approved);
		product.setApprovalAudit(createAudit(user_));
		product.updateLastUpdateTimestamp();

		// TODO: Save to historical datastore 
		
		addAuditTrailEntry(AuditEntryAction.Approved, product.getApprovalAudit(), productCode_, OBJECT_TYPE);
	}

	/**
	 * Suspend a Product 
	 * @param productCode_ Product to be Suspended
	 * @param user_ User 
	 *
	 */
	public void suspendProduct(String productCode_, String user_) {
		Product product = getProductInternal(productCode_);

		checkIfDependentObjectsInactive(product, UnderlyingType.Product, MarketRuntimeException.PRODUCT_CAN_ONLY_BE_SUSPENDED_IF_DEPENDENT_OBJECTS_ARE_SUSPENDED);
		
		checkIfObjectActive(product, MarketRuntimeException.PRODUCT_NOT_IN_CREATED_OR_SUSPEDED_STATE_CAN_BE_SUSPENDED);
		
		product.setActivationStatus(ActivationStatus.Suspended);
		product.setActivationDate(null);
		product.setSuspensionAudit(createAudit(user_));
		product.updateLastUpdateTimestamp();
		
		addAuditTrailEntry(AuditEntryAction.Suspended, product.getSuspensionAudit(), productCode_, OBJECT_TYPE);
	}
	

	public static void setupChangedProduct(Product originalProduct_,
			Product newProduct_) {
		newProduct_.setActivationDate(originalProduct_.getActivationDate());
		newProduct_.setActivationStatus(originalProduct_.getActivationStatus());
		newProduct_.setApprovalAudit(originalProduct_.getApprovalAudit());
		newProduct_.setCodeRolledFrom(originalProduct_.getProductCodeRolledOverFrom());
		newProduct_.setCreationAudit(originalProduct_.getCreationAudit());
		newProduct_.setChangeAudit(originalProduct_.getChangeAudit());
		newProduct_.setProductDefinitionDocument(originalProduct_.getProductDefinitionDocument());
		newProduct_.setRolloverAudit(originalProduct_.getRolloverAudit());
		newProduct_.setSuspensionAudit(originalProduct_.getSuspensionAudit());
	}
	
	private void setupNewProduct(Product product_, String user_) {
		// capitalize product code
		product_.setProductCode(DataUtils.adjustCodeToStandard(product_.getProductCode()));
		
		product_.setActivationStatus(ActivationStatus.Created);
	}	
}
