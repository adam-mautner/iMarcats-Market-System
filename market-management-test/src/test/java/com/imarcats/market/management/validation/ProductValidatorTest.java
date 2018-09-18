package com.imarcats.market.management.validation;

import java.util.Date;
import java.util.List;

import com.imarcats.interfaces.client.v100.exception.ExceptionLanguageKeys;
import com.imarcats.interfaces.client.v100.exception.MarketRuntimeException;
import com.imarcats.model.Product;
import com.imarcats.model.types.ActivationStatus;
import com.imarcats.model.types.AuditInformation;
import com.imarcats.model.types.ProductType;
import com.imarcats.model.types.Property;
import com.imarcats.model.utils.PropertyUtils;

public class ProductValidatorTest extends ValidatorTestCaseBase {

	public void testBasicValidation() throws Exception {
		try {
			ProductValidator.validateProductChange(null);	
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.NULL_PRODUCT_CANNOT_BE_CREATED, e.getLanguageKey());
		}
		
		Product product = new Product();
		
		testAllValidatorsForError(product, ExceptionLanguageKeys.PRODUCT_MUST_HAVE_PRODUCT_VALID_CODE);
		product.setProductCode(" Test");
		testAllValidatorsForError(product, ExceptionLanguageKeys.PRODUCT_MUST_HAVE_PRODUCT_VALID_CODE);
		product.setProductCode("Test ");
		testAllValidatorsForError(product, ExceptionLanguageKeys.PRODUCT_MUST_HAVE_PRODUCT_VALID_CODE);
		product.setProductCode("Te st");
		testAllValidatorsForError(product, ExceptionLanguageKeys.PRODUCT_MUST_HAVE_PRODUCT_VALID_CODE);
		product.setProductCode("Test");
		testAllValidatorsForError(product, ExceptionLanguageKeys.PRODUCT_MUST_HAVE_PRODUCT_VALID_CODE);
		product.setProductCode("TEST123456789_{}");
		testAllValidatorsForError(product, ExceptionLanguageKeys.PRODUCT_MUST_HAVE_PRODUCT_VALID_CODE);
		product.setProductCode("TEST_123456789_");
		
		testAllValidatorsForError(product, ExceptionLanguageKeys.PRODUCT_MUST_HAVE_NAME);
		product.setName("Test");
		testAllValidatorsForError(product, ExceptionLanguageKeys.PRODUCT_MUST_HAVE_DESCRIPTION);
		product.setDescription("Test");
		testAllValidatorsForError(product, ExceptionLanguageKeys.PRODUCT_MUST_HAVE_PRODUCT_TYPE);
		product.setType(ProductType.Financial);
		product.getRollablePropertyNames().add("Test");
		
		testAllValidatorsForError(product, ExceptionLanguageKeys.ROLLABLE_PROPERTY_MUST_BE_VALID_PROPERTY_ON_OBJECT);
		
		product.addProperty(PropertyUtils.createDoubleProperty("Test", 10.0));
		
		product.setCategory("<>");
		testAllValidatorsForError(product, ExceptionLanguageKeys.CATEGORY_MUST_BE_VALID);
		product.setCategory("Test");
		
		product.setSubCategory("<>");
		testAllValidatorsForError(product, ExceptionLanguageKeys.SUB_CATEGORY_MUST_BE_VALID);
		product.setSubCategory(null);
		
		ProductValidator.validateProductChange(product);	
		
		product.setProductDefinitionDocument("Test");
		ProductValidator.validateRolloverProduct(product, product);
		
		// test properties 
		final Product productFinal = product;

		testRollableProperties(new RollablePropertyAccessor() {
			
			@Override
			public void validate() {
				ProductValidator.validateProductChange(productFinal);
			}
			
			@Override
			public Property[] getProperties() {
				return productFinal.getProperties();
			}
			
			@Override
			public void deleteProperty(Property property_) {
				productFinal.deleteProperty(property_);
			}
			
			@Override
			public void clearProperties() {
				productFinal.clearProperties();
			}
			
			@Override
			public void addProperty(Property property_) {
				productFinal.addProperty(property_);
			}

			@Override
			public List<String> getRollablePropertyNames() {
				return productFinal.getRollablePropertyNames();
			}

			@Override
			public void setRollablePropertyNames(
					List<String> rollablePropertyNames_) {
				productFinal.setRollablePropertyNames(rollablePropertyNames_);
			}
		});
	}
	
	public void testRolloverValidation() throws Exception {
		Product productOriginal = createProduct();
		Product product = createProduct();
		
		ProductValidator.validateRolloverProduct(product, productOriginal);	
		
		product.setType(ProductType.Physical);
		try {
			ProductValidator.validateRolloverProduct(product, productOriginal);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.PRODUCT_TYPE_CANNOT_BE_CHANGED_WHILE_ROLLOVER, e.getLanguageKey());
		}
		product.setType(productOriginal.getType());

		productOriginal.setCategory("Test23");
		product.setCategory("Test45");
		try {
			ProductValidator.validateRolloverProduct(product, productOriginal);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.PRODUCT_CATEGORY_CANNOT_BE_CHANGED_WHILE_ROLLOVER, e.getLanguageKey());
		}
		product.setCategory(productOriginal.getCategory());

		productOriginal.setCategory(null);
		product.setCategory("Test45");
		try {
			ProductValidator.validateRolloverProduct(product, productOriginal);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.PRODUCT_CATEGORY_CANNOT_BE_CHANGED_WHILE_ROLLOVER, e.getLanguageKey());
		}
		product.setCategory(productOriginal.getCategory());
		
		productOriginal.setSubCategory("Test78");
		product.setSubCategory("Test90");
		try {
			ProductValidator.validateRolloverProduct(product, productOriginal);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.PRODUCT_SUB_CATEGORY_CANNOT_BE_CHANGED_WHILE_ROLLOVER, e.getLanguageKey());
		}
		product.setSubCategory(productOriginal.getSubCategory());

		productOriginal.setSubCategory(null);
		product.setSubCategory("Test90");
		try {
			ProductValidator.validateRolloverProduct(product, productOriginal);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.PRODUCT_SUB_CATEGORY_CANNOT_BE_CHANGED_WHILE_ROLLOVER, e.getLanguageKey());
		}
		product.setSubCategory(productOriginal.getSubCategory());
		
		product.setProductDefinitionDocument(null);
		try {
			ProductValidator.validateRolloverProduct(product, productOriginal);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.PRODUCT_MUST_HAVE_DEFINITION_DOCUMENT, e.getLanguageKey());
		}
		product.setProductDefinitionDocument(productOriginal.getProductDefinitionDocument());
		
		product.setProductDefinitionDocument("NewDoc");
		try {
			ProductValidator.validateRolloverProduct(product, productOriginal);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.PRODUCT_DEFINITION_DOCUMENT_CANNOT_BE_CHANGED_WHILE_ROLLOVER, e.getLanguageKey());
		}
		product.setProductDefinitionDocument(productOriginal.getProductDefinitionDocument());
		
		ProductValidator.validateRolloverProduct(product, productOriginal);	
	}
	
	public void testNewProductValidation() throws Exception {
		Product product = createProduct();
		product.setProductDefinitionDocument(null);
		
		ProductValidator.validateNewProduct(product);
		
		product.setActivationDate(new Date());
		try {
			ProductValidator.validateNewProduct(product);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.NEW_PRODUCT_MUST_NOT_HAVE_ACTIVATION_DATE, e.getLanguageKey());
		}
		product.setActivationDate(null);
		
		product.setActivationStatus(ActivationStatus.Approved);
		try {
			ProductValidator.validateNewProduct(product);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.NEW_PRODUCT_MUST_NOT_HAVE_ACTIVATION_STATUS, e.getLanguageKey());
		}
		product.setActivationStatus(null);
		
		product.setProductCodeRolledOverFrom("Test");
		try {
			ProductValidator.validateNewProduct(product);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.NEW_PRODUCT_MUST_NOT_HAVE_PRODUCT_CODE_ROLLED_FROM, e.getLanguageKey());
		}
		product.setProductCodeRolledOverFrom(null);
				
		product.setProductDefinitionDocument("TestDef");
		// we have removed this check to make product creation single step 
//		try {
//			ProductValidator.validateNewProduct(product);
//			fail();
//		} catch (MarketRuntimeException e) {
//			assertEquals(ExceptionLanguageKeys.NEW_PRODUCT_MUST_NOT_HAVE_DEFINITION_DOCUMENT, e.getLanguageKey());
//		}
//		product.setProductDefinitionDocument(null);
		
		product.setCreationAudit(new AuditInformation());
		try {
			ProductValidator.validateNewProduct(product);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.NEW_PRODUCT_MUST_NOT_HAVE_CREATION_AUDIT, e.getLanguageKey());
		}
		product.setCreationAudit(null);
		
		product.setChangeAudit(new AuditInformation());
		try {
			ProductValidator.validateNewProduct(product);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.NEW_PRODUCT_MUST_NOT_HAVE_CHANGE_AUDIT, e.getLanguageKey());
		}
		product.setChangeAudit(null);
		
		product.setRolloverAudit(new AuditInformation());
		try {
			ProductValidator.validateNewProduct(product);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.NEW_PRODUCT_MUST_NOT_HAVE_ROLLOVER_AUDIT, e.getLanguageKey());
		}
		product.setRolloverAudit(null);
		
		product.setApprovalAudit(new AuditInformation());
		try {
			ProductValidator.validateNewProduct(product);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.NEW_PRODUCT_MUST_NOT_HAVE_APPROVAL_AUDIT, e.getLanguageKey());
		}
		product.setApprovalAudit(null);
		
		
		product.setSuspensionAudit(new AuditInformation());
		try {
			ProductValidator.validateNewProduct(product);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.NEW_PRODUCT_MUST_NOT_HAVE_SUSPENSION_AUDIT, e.getLanguageKey());
		}
		product.setSuspensionAudit(null);
		

		ProductValidator.validateNewProduct(product);
		// test with created status 
		product.setActivationStatus(ActivationStatus.Created);
		ProductValidator.validateNewProduct(product);
	}

	private Product createProduct() {
		Product product = new Product();
		
		product.setProductCode("TEST1");
		product.setName("Test2");
		product.setDescription("Test3");
		product.setType(ProductType.Financial);
		product.setProductDefinitionDocument("Test4");
		return product;
	}
	
	private void testAllValidatorsForError(Product product_, String error_) {
		try {
			ProductValidator.validateNewProduct(product_);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(error_, e.getLanguageKey());
		}
		try {
			ProductValidator.validateProductChange(product_);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(error_, e.getLanguageKey());
		}try {
			ProductValidator.validateRolloverProduct(product_, product_);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(error_, e.getLanguageKey());
		}
	}
	
}
