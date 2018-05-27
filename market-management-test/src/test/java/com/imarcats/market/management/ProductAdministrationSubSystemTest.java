package com.imarcats.market.management;

import java.util.Date;

import com.imarcats.interfaces.client.v100.dto.ProductDto;
import com.imarcats.interfaces.client.v100.dto.types.ActivationStatus;
import com.imarcats.interfaces.client.v100.exception.ExceptionLanguageKeys;
import com.imarcats.interfaces.client.v100.exception.MarketRuntimeException;
import com.imarcats.interfaces.client.v100.util.DataUtils;
import com.imarcats.internal.server.infrastructure.testutils.MockDatastoresBase;
import com.imarcats.market.management.admin.MarketManagementAdminSystem;
import com.imarcats.market.management.admin.ProductAdministrationSubSystem;
import com.imarcats.model.Instrument;
import com.imarcats.model.Product;
import com.imarcats.model.types.AuditEntryAction;
import com.imarcats.model.types.UnderlyingType;

public class ProductAdministrationSubSystemTest extends ManagementTestBase {

	public void testCreate() throws Exception {
	  	MockDatastoresBase datastores = createDatastores();

    	MarketManagementAdminSystem adminSystem = 
    		new MarketManagementAdminSystem(datastores, datastores, datastores, datastores, datastores, datastores, datastores, null);
    	
    	// test create product 
    	
    	String productCode1 = "MYTESTPRODUCT"; 
		ProductDto product1 = createProduct1(productCode1);
		
		String user = "test";
		
		// successful creation 
    	testCreate(product1, adminSystem, user); 
    	
    	checkAuditTrail(AuditEntryAction.Created, user, productCode1, Product.class.getSimpleName(), adminSystem);
    	
    	// check product 1 
    	Product product1Loaded = datastores.findProductByCode(productCode1);

    	assertEquals(productCode1, product1Loaded.getCode());
       	assertEquals(com.imarcats.model.types.ActivationStatus.Created, product1Loaded.getActivationStatus());
    	assertEquals(user, product1Loaded.getCreationAudit().getUserID());       	
	}

	private void testCreate(ProductDto product,
			MarketManagementAdminSystem adminSystem, String user) {
		openTransaction();
		adminSystem.createProduct(product, user);
		commitTransaction();
	}
	
	public void testChange() throws Exception {
	  	MockDatastoresBase datastores = createDatastores();
    	
    	MarketManagementAdminSystem adminSystem = 
    		new MarketManagementAdminSystem(datastores, datastores, datastores, datastores, datastores, datastores, datastores, null);
    	
    	// create product 
    	String productCode1 = "MYTESTPRODUCT"; 
		ProductDto product1 = createProduct1(productCode1);
		product1.setActivationStatus(ActivationStatus.Created);
		product1.setLastUpdateTimestamp(new Date());
		
		createProductInDatastore(product1, datastores);
		
    	// test change product 
		ProductDto product1Changed = createProduct1(productCode1);
		product1Changed.setDescription("New Descrition");

	   	// recreate product to get a newer version
		ProductDto productChanged2 = createProduct1(productCode1);
		productChanged2.setLastUpdateTimestamp(new Date());
		
    	Thread.sleep(1000);
		
    	Product product1Loaded = datastores.findProductByCode(productCode1);
		product1Changed = createProduct1(productCode1);
    	product1Changed.setDescription("TestChange");
    	product1Changed.setVersionNumber(product1Loaded.getVersionNumber());
    	
    	String user = "test";
    	
    	// successful product change
    	testChange(product1Changed, adminSystem, user);
    	
    	// check change of product 
    	product1Loaded = datastores.findProductByCode(productCode1);

    	assertEquals(productCode1, product1Loaded.getCode());
    	assertEquals(product1Changed.getDescription(), product1Loaded.getDescription());
    	assertEquals(com.imarcats.model.types.ActivationStatus.Created, product1Loaded.getActivationStatus());
    	assertEquals(null, product1Loaded.getActivationDate());
    	assertEquals(user, product1Loaded.getChangeAudit().getUserID());
    	
		setProductToSuspended(productCode1, datastores);
    	
		product1Changed = createProduct1(productCode1);
    	product1Changed.setDescription("TestChange"); 
    	product1Changed.setVersionNumber(product1Loaded.getVersionNumber());
    	
    	testChange(product1Changed, adminSystem, user);
    	
		// test overwriting product
    	try {
    		testChange(productChanged2, adminSystem, user);
    		fail();
    	} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.PRODUCT_CANNOT_BE_OVERWRITTEN_WITH_AN_OLDER_VERSION, e.getLanguageKey());
		} finally {
			commitTransaction();
		}
		
    	// change product 
    	product1Changed = createProduct1(productCode1);
    	product1Changed.setDescription("New Descrition");
    	product1Changed.setVersionNumber(product1Loaded.getVersionNumber());
    	
    	testChange(product1Changed, adminSystem, user);
    	
    	setProductToApproved(productCode1, datastores);
		
    	product1Changed = createProduct1(productCode1);
    	product1Changed.setDescription("New Descrition");
    	
		try {
			testChange(product1Changed, adminSystem, user);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.PRODUCT_ONLY_IN_CREATED_OR_SUSPEDED_STATE_CAN_BE_CHANGED, e.getLanguageKey());
		} finally {
			commitTransaction();
		}
    	setProductToSuspended(productCode1, datastores);
    	
    	product1Changed = createProduct1(productCode1);
    	product1Changed.setDescription("New Descrition");
    	product1Changed.setVersionNumber(product1Loaded.getVersionNumber());
    	
    	testChange(product1Changed, adminSystem, user);
    	
    	// non-existent product
    	product1Changed = createProduct1(productCode1 + "1");
    	product1Changed.setDescription("New Descrition");
    	
		try {
			testChange(product1Changed, adminSystem, user);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.NON_EXISTENT_PRODUCT, e.getLanguageKey());
		} finally {
			commitTransaction();
		}
	}

	private void testChange(ProductDto productChanged,
			MarketManagementAdminSystem adminSystem, String user) {
		openTransaction();
		adminSystem.changeProduct(productChanged, user);
		commitTransaction();
	}
	
	@SuppressWarnings("deprecation")
	public void testApproveAndSuspend() throws Exception {
	  	MockDatastoresBase datastores = createDatastores();
    	
    	MarketManagementAdminSystem adminSystem = 
    		new MarketManagementAdminSystem(datastores, datastores, datastores, datastores, datastores, datastores, datastores, null);
    	
    	// create product 
    	String productCode1 = "MYTESTPRODUCT"; 
		ProductDto product1 = createProduct1(productCode1);
		product1.setActivationStatus(ActivationStatus.Created);
		product1.setLastUpdateTimestamp(new Date());
		
		createProductInDatastore(product1, datastores);
		
		String user = "test";
    	
    	// no product definition 
		try {
			testApprove(productCode1, adminSystem, user);
			fail();
    	} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.PRODUCT_MUST_HAVE_DEFINITION_DOCUMENT, e.getLanguageKey());
		} finally {
			commitTransaction();
		}
    	
    	// add product definition 
    	openTransaction();
		datastores.findProductByCode(productCode1).setProductDefinitionDocument("test");
    	commitTransaction();
    	
		try {
			adminSystem.approveProduct("BLABLA", new Date(), user);
			fail();
    	} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.NON_EXISTENT_PRODUCT, e.getLanguageKey());
		} 
    		
    	// stale object 
		try {
			Date lastUpdateTimestamp = new Date();
			lastUpdateTimestamp.setMinutes(lastUpdateTimestamp.getMinutes() - 10);
			adminSystem.approveProduct(productCode1, lastUpdateTimestamp, user);
			fail();
    	} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.STALE_OBJECT_CANNOT_BE_APPROVED, e.getLanguageKey());
		} 
    	
    	// successful approval 
		testApprove(productCode1, adminSystem, user);
		
    	checkAuditTrail(AuditEntryAction.Approved, user, productCode1, Product.class.getSimpleName(), adminSystem);
    	
    	// check product approval
    	Product product1Loaded = datastores.findProductByCode(productCode1);
		
    	assertEquals(productCode1, product1Loaded.getCode());
    	assertEquals(com.imarcats.model.types.ActivationStatus.Approved, product1Loaded.getActivationStatus());
    	assertTrue(product1Loaded.getActivationDate() != null);
    	assertEquals(user, product1Loaded.getApprovalAudit().getUserID());
    	
    	// try approving again 
		try {
			testApprove(productCode1, adminSystem, user);
			fail();
    	} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.PRODUCT_ONLY_IN_CREATED_OR_SUSPEDED_STATE_CAN_BE_APPROVED, e.getLanguageKey());
		} finally {
			commitTransaction();
		}
 		
    	// suspend product 
		testSuspend(productCode1, adminSystem, user);
		
    	checkAuditTrail(AuditEntryAction.Suspended, user, productCode1, Product.class.getSimpleName(), adminSystem);
    	
		// check product after suspend 
    	product1Loaded = datastores.findProductByCode(productCode1);

    	assertEquals(productCode1, product1Loaded.getCode());
    	assertEquals(com.imarcats.model.types.ActivationStatus.Suspended, product1Loaded.getActivationStatus());
    	assertEquals(null, product1Loaded.getActivationDate());
    	assertEquals(user, product1Loaded.getSuspensionAudit().getUserID());
    	
    	// already suspended 
    	try {
			testSuspend(productCode1, adminSystem, user);
			fail();
    	} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.PRODUCT_NOT_IN_CREATED_OR_SUSPEDED_STATE_CAN_BE_SUSPENDED, e.getLanguageKey());
		} finally {
			commitTransaction();
		}
    	
    	testApprove(productCode1, adminSystem, user);
    	
    	// create a dependent instrument
    	String testInstrumentCode = "TestInstrumentCode";
    	Instrument instrument = createInstrument(testInstrumentCode);
    	instrument.setUnderlyingCode(productCode1);
    	instrument.setUnderlyingType(UnderlyingType.Product);
    	instrument.setActivationStatus(com.imarcats.model.types.ActivationStatus.Approved);
    	
    	createInstrumentInDatastore(instrument, datastores);
    	
    	// test suspend product 
		try {
			testSuspend(productCode1, adminSystem, user);
			fail();
    	} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.PRODUCT_CAN_ONLY_BE_SUSPENDED_IF_DEPENDENT_OBJECTS_ARE_SUSPENDED, e.getLanguageKey());
		} finally {
			commitTransaction();
		}
    	
		// remove instrument approval
		setInstrumentToSuspended(testInstrumentCode, datastores); 
    	
		testSuspend(productCode1, adminSystem, user);
	}
	
	private void testSuspend(String productCode,
			MarketManagementAdminSystem adminSystem, String user) {
		openTransaction();
		adminSystem.suspendProduct(productCode, user);
		commitTransaction();
	}

	private void testApprove(String productCode,
			MarketManagementAdminSystem adminSystem, String user) {
		openTransaction();
		adminSystem.approveProduct(productCode, new Date(), user);
		commitTransaction();
	}

	public void testAddProductDefinitionDocument() throws Exception {
	  	MockDatastoresBase datastores = createDatastores();
    	
    	MarketManagementAdminSystem adminSystem = 
    		new MarketManagementAdminSystem(datastores, datastores, datastores, datastores, datastores, datastores, datastores, null);
    	
    	// create product 
    	String productCode1 = "MYTESTPRODUCT"; 
		ProductDto product1 = createProduct1(productCode1);
		product1.setActivationStatus(ActivationStatus.Created);
		product1.setLastUpdateTimestamp(new Date());
		
		String user = "test";
		
		createProductInDatastore(product1, datastores);
		
		String productDefinitionDocument = "My Test Product Definition Documement";

    	// non-existent 
    	try {
			adminSystem.setProductDefinitionDocument("BLABLA", productDefinitionDocument, user);
			fail();
    	} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.NON_EXISTENT_PRODUCT, e.getLanguageKey());
		}
    	
    	setProductToApproved(productCode1, datastores);
    	
    	try {
			adminSystem.setProductDefinitionDocument(productCode1, "New Document", user);
			fail();
    	} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.PRODUCT_ONLY_IN_CREATED_OR_SUSPEDED_STATE_CAN_BE_CHANGED, e.getLanguageKey());
		}
    	
    	setProductToCreated(productCode1, datastores);
    	
    	// successful definition setting 
    	testSetProductDefinition(user, adminSystem,
				productCode1, productDefinitionDocument);
    	
    	checkAuditTrail(AuditEntryAction.Changed, user, productCode1, Product.class.getSimpleName(), adminSystem);
    	
    	// check product definition set
    	Product product1Loaded = datastores.findProductByCode(productCode1);

    	assertEquals(productCode1, product1Loaded.getCode());
    	assertEquals(productDefinitionDocument, product1Loaded.getProductDefinitionDocument());

    	setProductToSuspended(productCode1, datastores);
    	
    	testSetProductDefinition(user, adminSystem,
				productCode1, productDefinitionDocument);
	}

	private void testSetProductDefinition(String user,
			MarketManagementAdminSystem adminSystem, String productCode1,
			String productDefinitionDocument) {
		openTransaction();
		adminSystem.setProductDefinitionDocument(productCode1, productDefinitionDocument, user);
		commitTransaction();
	}

	public void testDelete() throws Exception {
	  	MockDatastoresBase datastores = createDatastores();
    	
    	MarketManagementAdminSystem adminSystem = 
    		new MarketManagementAdminSystem(datastores, datastores, datastores, datastores, datastores, datastores, datastores, null);
    	
    	String user = "test";
    	
    	// create product 
    	String productCode1 = "MYTESTPRODUCT"; 
		ProductDto product1 = createProduct1(productCode1);
		product1.setActivationStatus(ActivationStatus.Created);
		product1.setLastUpdateTimestamp(new Date());
		
		createProductInDatastore(product1, datastores);
		
    	MarketManagementContextImpl context = createMarketManagementContext();

    	context = createMarketManagementContext();
		
		setProductToApproved(productCode1, datastores);
		
		context = createMarketManagementContext();
    	try {
			testDelete(productCode1, adminSystem, context, user);
    		fail();
    	} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.PRODUCT_ONLY_IN_CREATED_OR_SUSPEDED_STATE_CAN_BE_DELETED, e.getLanguageKey());
		} finally {
			commitTransaction();
		}

    	setProductToCreated(productCode1, datastores);
		
    	// create a dependent instrument
    	String testInstrumentCode = "TestInstrumentCode";
    	Instrument instrument = createInstrument(testInstrumentCode);
    	instrument.setUnderlyingCode(productCode1);
    	instrument.setUnderlyingType(UnderlyingType.Product);
    	instrument.setActivationStatus(com.imarcats.model.types.ActivationStatus.Approved);
    	instrument.updateLastUpdateTimestamp();
    	
    	createInstrumentInDatastore(instrument, datastores);
    	
    	// test delete product 
    	 
		try {
			testDelete(productCode1, adminSystem, context, user);
			fail();
    	} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.PRODUCT_THAT_HAS_NO_DEPENDENT_INSTRUMENTS_CAN_BE_DELETED, e.getLanguageKey());
		} finally {
			commitTransaction();
		}

		// remove dependency on product
    	openTransaction();
		Instrument instrumentLoaded = datastores.findInstrumentByCode(testInstrumentCode);
		instrumentLoaded.setUnderlyingCode("BOGUS");
		commitTransaction();
    	
		// make sure that audit entries are far apart enough 
		Thread.sleep(1000); 
		
		testDelete(productCode1, adminSystem, context, user);

		checkAuditTrailForDeleteObject(user, productCode1, Product.class.getSimpleName(), adminSystem);
		
		// check product after delete
    	Product product1Loaded = datastores.findProductByCode(productCode1);

    	assertEquals(null, product1Loaded);
	}

	private void testDelete(String productCode,
			MarketManagementAdminSystem adminSystem,
			MarketManagementContextImpl context, String user) {
		openTransaction();
		adminSystem.deleteProduct(productCode, user, context);
		commitTransaction();
	}
	
	public void testSetupChangedProduct() throws Exception {
		Product originalProduct = new Product();
		
		// setup system fields

		originalProduct.setActivationStatus(com.imarcats.model.types.ActivationStatus.Activated);
		originalProduct.setActivationDate(new Date());
		originalProduct.setProductDefinitionDocument("testDef");
		originalProduct.setProductCodeRolledOverFrom("testCode");
		
		originalProduct.setCreationAudit(createAudit());
		originalProduct.setChangeAudit(createAudit());
		originalProduct.setApprovalAudit(createAudit());
		originalProduct.setSuspensionAudit(createAudit());
		originalProduct.setRolloverAudit(createAudit());
		
		
		Product newProduct = new Product();
		
		ProductAdministrationSubSystem.setupChangedProduct(originalProduct, newProduct);
		
		assertEqualsProduct(originalProduct, newProduct);
	}
	
	public void testProductRollover() throws Exception {
	  	MockDatastoresBase datastores = createDatastores();

    	MarketManagementAdminSystem adminSystem = 
    		new MarketManagementAdminSystem(datastores, datastores, datastores, datastores, datastores, datastores, datastores, null);
    	
    	String user = "test";
    	
    	// create product 
    	String productCode1 = "TESTPRODUCT"; 
		ProductDto product1 = createProduct1(productCode1);
		product1.setRollable(true);

		testCreate(product1, adminSystem, user);
    	
    	// set product definition
    	String productDefinitionDocument = "Test Doc";
		testSetProductDefinition(user, adminSystem,
				productCode1, productDefinitionDocument);
    	
    	// this is needed because, it needs reloading from the datastore anyway
    	product1.setProductDefinitionDocument(productDefinitionDocument);
    	
		// test rollover of non-approved objects 
    	String productCode1Rolled = "TEST_PROD_ROLLED";
    	
    	ProductDto product1Rolled = createProduct1(productCode1Rolled);
		product1Rolled.setRollable(true);
		
		try {
    		testRollover(productCode1, product1Rolled, adminSystem,
					user);
    		fail();
    	} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.NON_APPROVED_OBJECT_CANNOT_BE_REFERRED_IN_ROLLOVER, e.getLanguageKey());
		} finally {
			commitTransaction();
		}
    	
    	testApprove(productCode1, adminSystem, user);
    	
		// test rollover to the same 
    	try {
    		testRollover(productCode1, product1, adminSystem,
					user);
    		fail();
    	} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.ROLLED_OBJECTS_HAVE_SAME_CODE, e.getLanguageKey());
		} finally {
			commitTransaction();
		}
    	
		// test validation problem 
		try {
			testRollover(productCode1, product1Rolled, adminSystem,
					user);
    		fail();
    	} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.PRODUCT_MUST_HAVE_DEFINITION_DOCUMENT, e.getLanguageKey());
		} finally {
			commitTransaction();
		}
    	
    	product1Rolled.setProductDefinitionDocument(productDefinitionDocument);
    	
    	// test successful rollover 
    	testRollover(productCode1, product1Rolled, adminSystem,
				user);
    	
    	// product code will be capitalized
    	productCode1Rolled = productCode1Rolled.toUpperCase();
    	
		// check rolled over product 
    	Product product1RolledLoaded = datastores.findProductByCode(productCode1Rolled);
    	
    	assertTrue(product1RolledLoaded != null);
    	assertEquals(productCode1Rolled, product1RolledLoaded.getProductCode());
    	assertEquals(productCode1, product1RolledLoaded.getProductCodeRolledOverFrom());
    	assertEquals(com.imarcats.model.types.ActivationStatus.Approved, product1RolledLoaded.getActivationStatus());
    	assertEquals(user, product1RolledLoaded.getCreationAudit().getUserID());
    	assertEquals(user, product1RolledLoaded.getRolloverAudit().getUserID());
    	assertEquals(productDefinitionDocument, product1RolledLoaded.getProductDefinitionDocument());
    	assertEquals(true, product1RolledLoaded.getRollable());
    	
    	// test rollover by admin 
    	String productCode2Rolled = "TEST_PROD_ROLLED_2";
    	
    	ProductDto product2Rolled = createProduct1(productCode2Rolled);
		product2Rolled.setRollable(true);
		product2Rolled.setProductDefinitionDocument(productDefinitionDocument);
 		
    	testRollover(productCode1, product2Rolled, adminSystem,
				user);
    	
    	checkAuditTrail(AuditEntryAction.Rolled, user, DataUtils.adjustCodeToStandard(product2Rolled.getProductCode()), Product.class.getSimpleName(), adminSystem);
	}

	private void testRollover(String productCode, ProductDto productRolled,
			MarketManagementAdminSystem adminSystem, String user) {
		openTransaction();
		adminSystem.rolloverProduct(productCode, productRolled, user);
		commitTransaction();
	}
	
	public void testLoadAndChange() throws Exception {
	  	MockDatastoresBase datastores = createDatastores();
	  	
	  	MarketManagementAdminSystem adminSystem = 
    		new MarketManagementAdminSystem(datastores, datastores, datastores, datastores, datastores, datastores, datastores, null);
    	
    	// create product 
    	String productCode1 = "TEST_PROD"; 
		ProductDto product1 = createProduct1(productCode1);
	
		String user = "test";
		
    	testCreate(product1, adminSystem, user);
    	
    	// product code will be capitalized 
    	productCode1 = productCode1.toUpperCase();
    	
    	// load product 
    	
    	// it is weird, that we had to provide null as PM here to cut 
    	// the productToBeChanged from its datastore version,
    	// if it is not cut from its datastore version (queried and changed in the same PM), 
    	// in change a fresh version is loaded from the datastore it changes firstInstrument
    	// back to the same values as datastore has
    	Product productToBeChanged = datastores.findProductByCode(productCode1);

    	productToBeChanged.setDescription("New Descr");
    	String propertyName = "Test";
		productToBeChanged.getRollablePropertyNames().add(propertyName);
    	
    	productToBeChanged.addProperty(getBooleanProperty(propertyName, true));
    	
    	// change product 
    	testChange(productToBeChanged, adminSystem, user);

    	// test changed
    	Product product1Loaded = datastores.findProductByCode(productCode1);

    	assertEquals(productCode1, product1Loaded.getCode());
    	assertEquals(productToBeChanged.getDescription(), product1Loaded.getDescription());
    	assertEqualsStringList(productToBeChanged.getRollablePropertyNames().toArray(new String[productToBeChanged.getRollablePropertyNames().size()]), 
    			product1Loaded.getRollablePropertyNames().toArray(new String[product1Loaded.getRollablePropertyNames().size()]));
    	assertEqualsPropertyList(productToBeChanged.getProperties(), product1Loaded.getProperties());
	}

	private void testChange(Product productToBeChanged,
			MarketManagementAdminSystem adminSystem, String user) {
		openTransaction();
		adminSystem.changeProduct(productToDto(productToBeChanged), user);
		commitTransaction();
	}
}
