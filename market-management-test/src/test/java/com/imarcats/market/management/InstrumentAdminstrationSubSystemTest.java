package com.imarcats.market.management;

import java.util.Date;

import com.imarcats.interfaces.client.v100.dto.InstrumentDto;
import com.imarcats.interfaces.client.v100.dto.ProductDto;
import com.imarcats.interfaces.client.v100.exception.ExceptionLanguageKeys;
import com.imarcats.interfaces.client.v100.exception.MarketRuntimeException;
import com.imarcats.internal.server.infrastructure.testutils.MockDatastoresBase;
import com.imarcats.internal.server.interfaces.market.MarketInternal;
import com.imarcats.market.management.admin.InstrumentAdministrationSubSystem;
import com.imarcats.market.management.admin.MarketManagementAdminSystem;
import com.imarcats.model.AssetClass;
import com.imarcats.model.Instrument;
import com.imarcats.model.Market;
import com.imarcats.model.types.ActivationStatus;
import com.imarcats.model.types.AuditEntryAction;
import com.imarcats.model.types.UnderlyingType;

public class InstrumentAdminstrationSubSystemTest extends ManagementTestBase {

	public void testCreate() throws Exception {
	  	MockDatastoresBase datastores = createDatastores();
    	
    	MarketManagementAdminSystem adminSystem = 
    		new MarketManagementAdminSystem(datastores, datastores, datastores, datastores, datastores, datastores, datastores, null);
    	
    	// create product 
    	String productCode1 = "MYTESTPRODUCT"; 
		ProductDto product1 = createProduct1(productCode1);
		product1.setActivationStatus(com.imarcats.interfaces.client.v100.dto.types.ActivationStatus.Created);
		product1.setLastUpdateTimestamp(new Date());
		
		createProductInDatastore(product1, datastores);
    	
    	// test create instrument 
    	String instrumentCode1 = "MYTESTINSTR"; 
		InstrumentDto instrument1 = createInstrument1Dto(instrumentCode1, UnderlyingType.Product, productCode1); 
			
		// successful creation
		String testUser = "test";
		testCreateInstrument(instrument1, adminSystem, testUser);
    	
    	checkAuditTrail(AuditEntryAction.Created, testUser, instrument1.getInstrumentCode(), Instrument.class.getSimpleName(), adminSystem);
    	
    	// check instrument 1 
    	Instrument instrument1Loaded = datastores.findInstrumentByCode(instrumentCode1);

    	assertEquals(instrumentCode1, instrument1Loaded.getCode());
       	assertEquals(ActivationStatus.Created, instrument1Loaded.getActivationStatus());
    	assertEquals(testUser, instrument1Loaded.getCreationAudit().getUserID());       	
    	
    	InstrumentDto instrumentNonExistentUnderlying = createInstrument1Dto(instrumentCode1, UnderlyingType.Instrument, "BLA");
    	try {
    		adminSystem.createInstrument(instrumentNonExistentUnderlying, testUser);
    		fail();
    	} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.NON_EXISTENT_UNDERLYING, e.getLanguageKey());
		}
    	
    	instrumentNonExistentUnderlying = createInstrument1Dto(instrumentCode1, UnderlyingType.Product, "BLA");
    	try {
    		adminSystem.createInstrument(instrumentNonExistentUnderlying, testUser);
    		fail();
    	} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.NON_EXISTENT_UNDERLYING, e.getLanguageKey());
		}
	}

	private void testCreateInstrument(InstrumentDto instrument,
			MarketManagementAdminSystem adminSystem,
			String adminUserSession) {
		openTransaction();
    	adminSystem.createInstrument(instrument, adminUserSession); 
    	commitTransaction();
	}

	public void testChange() throws Exception {
	  	MockDatastoresBase datastores = createDatastores();
    	
    	MarketManagementAdminSystem adminSystem = 
    		new MarketManagementAdminSystem(datastores, datastores, datastores, datastores, datastores, datastores, datastores, null);
    	
    	String productCode1 = "MYTESTPRODUCT"; 
		ProductDto product1 = createProduct1(productCode1);
		product1.setActivationStatus(com.imarcats.interfaces.client.v100.dto.types.ActivationStatus.Created);
		product1.setLastUpdateTimestamp(new Date());
		
		createProductInDatastore(product1, datastores); 
		
    	String instrumentCode1 = "MYTESTINSTR"; 
		Instrument instrument1 = createInstrument1(instrumentCode1, UnderlyingType.Product, productCode1); 
		instrument1.setActivationStatus(ActivationStatus.Created);
		instrument1.updateLastUpdateTimestamp();
		createInstrumentInDatastore(instrument1, datastores);
		
		// test change instrument 
    	InstrumentDto instrument1Changed = createInstrument1Dto(instrumentCode1, UnderlyingType.Product, productCode1);
		instrument1Changed.setDescription("New Descrition");

		InstrumentDto instrument1ChangedOutDated = createInstrument1Dto(instrumentCode1, UnderlyingType.Product, productCode1);
		instrument1ChangedOutDated.setDescription("New Descrition");
		instrument1ChangedOutDated.setLastUpdateTimestamp(new Date());
		
		Thread.sleep(1000);
		
		setInstrumentToApproved(instrumentCode1, datastores);
		
		// recreate instrument to get a newer version 
		instrument1Changed = createInstrument1Dto(instrumentCode1, UnderlyingType.Product, productCode1);
		instrument1Changed.setDescription("New Descrition");
		
		String adminUser = "Test"; 
		// changed approved 
    	try {
			adminSystem.changeInstrument(instrument1Changed, adminUser);
    		fail();
    	} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.INSTRUMENT_ONLY_IN_CREATED_OR_SUSPEDED_STATE_CAN_BE_CHANGED, e.getLanguageKey());
		}


    	setInstrumentToCreated(instrumentCode1, datastores);
		
	   	Thread.sleep(1000);
		
		// recreate instrument to get a newer version 
	   	Instrument instrument1Loaded = datastores.findInstrumentByCode(instrumentCode1);
		instrument1Changed = createInstrument1Dto(instrumentCode1, UnderlyingType.Product, productCode1);
		instrument1Changed.setDescription("New Descrition");
		instrument1Changed.setVersionNumber(instrument1Loaded.getVersionNumber());
		
    	// successful change 
		testChangeInstrument(instrument1Changed, adminUser, adminSystem);
    	
    	checkAuditTrail(AuditEntryAction.Changed, adminUser, instrumentCode1, Instrument.class.getSimpleName(), adminSystem);
    	
    	// check change of product 
    	instrument1Loaded = datastores.findInstrumentByCode(instrumentCode1);

    	assertEquals(instrumentCode1, instrument1Loaded.getCode());
    	assertEquals(instrument1Changed.getDescription(), instrument1Loaded.getDescription());
    	assertEquals(ActivationStatus.Created, instrument1Loaded.getActivationStatus());
    	assertEquals(null, instrument1Loaded.getActivationDate());
    	assertEquals(adminUser, instrument1Loaded.getChangeAudit().getUserID());
    	
		// change using outdated instrument
    	try {
			adminSystem.changeInstrument(instrument1ChangedOutDated, adminUser);
    		fail();
    	} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.INSTRUMENT_CANNOT_BE_OVERWRITTEN_WITH_AN_OLDER_VERSION, e.getLanguageKey());
		}

    	setInstrumentToSuspended(instrumentCode1, datastores);
		
	   	// recreate instrument to get a newer version 
		instrument1Changed = createInstrument1Dto(instrumentCode1, UnderlyingType.Product, productCode1);
		instrument1Changed.setDescription("New Descrition");
		instrument1Changed.setVersionNumber(instrument1Loaded.getVersionNumber());
		
    	testChangeInstrument(instrument1Changed, adminUser, adminSystem);
	}

	private void testChangeInstrument(InstrumentDto instrumentChanged,
			String userSession, MarketManagementAdminSystem adminSystem) {
		openTransaction();
    	adminSystem.changeInstrument(instrumentChanged, userSession);
    	commitTransaction();
	}

	public void testSetMasterAgreementDocumentAndAssetClass() throws Exception {
		MockDatastoresBase datastores = createDatastores();

    	MarketManagementAdminSystem adminSystem = 
    		new MarketManagementAdminSystem(datastores, datastores, datastores, datastores, datastores, datastores, datastores, null);
    	
   
    	String productCode1 = "MYTESTPRODUCT"; 
		ProductDto product1 = createProduct1(productCode1);
		product1.setActivationStatus(com.imarcats.interfaces.client.v100.dto.types.ActivationStatus.Created);
		product1.setLastUpdateTimestamp(new Date()); 
		createProductInDatastore(product1, datastores);
		
    	String instrumentCode1 = "MYTESTINSTR"; 
		Instrument instrument1 = createInstrument1(instrumentCode1, UnderlyingType.Product, productCode1); 
		instrument1.setActivationStatus(ActivationStatus.Created);
		instrument1.updateLastUpdateTimestamp();
    	
		createInstrumentInDatastore(instrument1, datastores);
		
		// test set master agreement document
		String masterAgreementDocument = "testDoc";
		
		String user = "test";
		// non-existent instrument
    	try {
			adminSystem.setInstrumentMasterAgreementDocument("BOGUS", masterAgreementDocument, user);
    		fail();
    	} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.NON_EXISTENT_INSTRUMENT, e.getLanguageKey());
		}
		
		setInstrumentToApproved(instrumentCode1, datastores);
		
    	try {
			testSetMasterAgreement(masterAgreementDocument, instrumentCode1,
					adminSystem, user);
    		fail();
    	} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.INSTRUMENT_ONLY_IN_CREATED_OR_SUSPEDED_STATE_CAN_BE_CHANGED, e.getLanguageKey());
		} finally {
			commitTransaction();
		}
    	
		setInstrumentToSuspended(instrumentCode1, datastores);
		
		// test successful change - suspended 
		testSetMasterAgreement(masterAgreementDocument, instrumentCode1,
				adminSystem, user);
	
		Instrument instrumentLoaded = datastores.findInstrumentByCode(instrumentCode1);
		
		assertEquals(instrumentCode1, instrumentLoaded.getCode());
		assertEquals(masterAgreementDocument, instrumentLoaded.getMasterAgreementDocument());
		
		checkAuditTrail(AuditEntryAction.Changed, user, instrumentCode1, Instrument.class.getSimpleName(), adminSystem);
	
		setInstrumentToCreated(instrumentCode1, datastores);
		
		testSetMasterAgreement(masterAgreementDocument, instrumentCode1,
				adminSystem, user);
		
		// test add asset class 
		String assetClassName = "TEST";
		AssetClass assetClass = createAssetClass();
		assetClass.setName(assetClassName);
		
		createAssetClassInDatastore(assetClass, datastores);

		// non-existent instrument
    	try {
			adminSystem.setInstrumentAssetClass("BOGUS", assetClassName, user);
    		fail();
    	} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.NON_EXISTENT_INSTRUMENT, e.getLanguageKey());
		}
    	
		setInstrumentToApproved(instrumentCode1, datastores);
    	
    	try {
			adminSystem.setInstrumentAssetClass(instrumentCode1, assetClassName, user);
    		fail();
    	} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.INSTRUMENT_ONLY_IN_CREATED_OR_SUSPEDED_STATE_CAN_BE_CHANGED, e.getLanguageKey());
		} 
    	
		setInstrumentToSuspended(instrumentCode1, datastores);
		
		// test successful change - suspended 
		testSetInstrumentAssetClass(instrumentCode1, assetClassName,
				user, adminSystem);
		
		instrumentLoaded = datastores.findInstrumentByCode(instrumentCode1);
		
		assertEquals(instrumentCode1, instrumentLoaded.getCode());
		assertEquals(assetClassName, instrumentLoaded.getAssetClassName());
		
		checkAuditTrail(AuditEntryAction.Changed, user, instrumentCode1, Instrument.class.getSimpleName(), adminSystem);
		
		setInstrumentToCreated(instrumentCode1, datastores);
		
		testSetInstrumentAssetClass(instrumentCode1, assetClassName,
				user, adminSystem);
	}

	private void testSetMasterAgreement(String masterAgreementDocument,
			String instrumentCode1, MarketManagementAdminSystem adminSystem,
			String user) {
		openTransaction();
		adminSystem.setInstrumentMasterAgreementDocument(instrumentCode1, masterAgreementDocument, user);
		commitTransaction();
	}

	private void testSetInstrumentAssetClass(String instrumentCode,
			String assetClassName, String user,
			MarketManagementAdminSystem adminSystem) {
		openTransaction();
		adminSystem.setInstrumentAssetClass(instrumentCode, assetClassName, user);
		commitTransaction();
	}

	@SuppressWarnings("deprecation")
	public void testApproveSuspend() throws Exception {
		MockDatastoresBase datastores = createDatastores();

    	MarketManagementAdminSystem adminSystem = 
    		new MarketManagementAdminSystem(datastores, datastores, datastores, datastores, datastores, datastores, datastores, null);
    	
    	String productCode1 = "MYTESTPRODUCT"; 
		ProductDto product1 = createProduct1(productCode1);
		product1.setActivationStatus(com.imarcats.interfaces.client.v100.dto.types.ActivationStatus.Approved);
		product1.setLastUpdateTimestamp(new Date());
		
    	createProductInDatastore(product1, datastores);
    	
    	String instrumentCode1 = "MYTESTINSTR"; 
		Instrument instrument1 = createInstrument1(instrumentCode1, UnderlyingType.Product, productCode1); 
		instrument1.setActivationStatus(ActivationStatus.Created);
		instrument1.updateLastUpdateTimestamp();
		
		createInstrumentInDatastore(instrument1, datastores);
		
		String user = "test"; 
		
    	// no master agreement 
		try {
			adminSystem.approveInstrument(instrumentCode1, new Date(), user);
			fail();
    	} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.INSTRUMENT_MUST_HAVE_MASTER_AGREEMENT_DOCUMENT, e.getLanguageKey());
		}

		openTransaction();
    	datastores.findInstrumentByCode(instrumentCode1).setMasterAgreementDocument("test");
    	commitTransaction();
    	
    	// no asset class 
		try {
			adminSystem.approveInstrument(instrumentCode1, new Date(), user);
			fail();
    	} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.INSTRUMENT_MUST_HAVE_ASSET_CLASS_NAME_FOR_APPROVAL, e.getLanguageKey());
		}

		openTransaction();
    	datastores.findInstrumentByCode(instrumentCode1).setAssetClassName("test");    	
    	datastores.findProductByCode(productCode1).setActivationStatus(ActivationStatus.Created);
    	commitTransaction();
    	
    	// stale object 
		try {
			Date lastUpdateTimestamp = new Date();
			lastUpdateTimestamp.setMinutes(lastUpdateTimestamp.getMinutes() - 10);
			adminSystem.approveInstrument(instrumentCode1, lastUpdateTimestamp, user);
			fail();
    	} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.STALE_OBJECT_CANNOT_BE_APPROVED, e.getLanguageKey());
		}
    	
    	// non-approved underlying - product 
		try {
			adminSystem.approveInstrument(instrumentCode1, new Date(), user);
			fail();
    	} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.INSTRUMENT_CAN_ONLY_BE_APPROVED_IF_UNDERLYING_IS_APPROVED, e.getLanguageKey());
		}

    	setProductToApproved(productCode1, datastores);
    	
    	// non-existent
		try {
			adminSystem.approveInstrument("BOGUS", new Date(), user);
			fail();
    	} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.NON_EXISTENT_INSTRUMENT, e.getLanguageKey());
		}
    	
    	// successful approval - created
		testApproveInstrument(instrumentCode1, user, adminSystem);
		
		Instrument instrumentLoaded = datastores.findInstrumentByCode(instrumentCode1);
		
    	assertEquals(instrumentCode1, instrumentLoaded.getCode());
    	assertEquals(ActivationStatus.Approved, instrumentLoaded.getActivationStatus());
    	assertTrue(null != instrumentLoaded.getActivationDate());

		checkAuditTrail(AuditEntryAction.Approved, user, instrumentCode1, Instrument.class.getSimpleName(), adminSystem);

		setInstrumentToSuspended(instrumentCode1, datastores);
    	
    	testApproveInstrument(instrumentCode1, user, adminSystem);
    	
    	// approve again 
		try {
			adminSystem.approveInstrument(instrumentCode1, new Date(), user);
			fail();
    	} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.INSTRUMENT_ONLY_IN_CREATED_OR_SUSPEDED_STATE_CAN_BE_APPROVED, e.getLanguageKey());
		}
		
		// add dependent market 
    	String marketCode = "TestMarketCode";
    	Market market = createMarket(marketCode);
    	market.setInstrumentCode(instrumentCode1);
    	market.setActivationStatus(ActivationStatus.Activated);
    	
    	openTransaction();
    	datastores.createMarket(market);
    	commitTransaction();
    	
    	// non-existent
		try {
			adminSystem.suspendInstrument("BOGUS", user);
			fail();
    	} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.NON_EXISTENT_INSTRUMENT, e.getLanguageKey());
		}
    		
    	// dependent not suspended - market 
		try {
			adminSystem.suspendInstrument(instrumentCode1, user);
			fail();
    	} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.INSTRUMENT_CAN_ONLY_BE_SUSPENDED_IF_DEPENDENT_OBJECTS_ARE_SUSPENDED, e.getLanguageKey());
		}
    	
    	setMarketToApproved(marketCode, datastores);
    	
    	// dependent not suspended - market 
		try {
			adminSystem.suspendInstrument(instrumentCode1, user);
			fail();
    	} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.INSTRUMENT_CAN_ONLY_BE_SUSPENDED_IF_DEPENDENT_OBJECTS_ARE_SUSPENDED, e.getLanguageKey());
		}
    	
    	setMarketToSuspended(marketCode, datastores);
    	
    	String instrumentCode2 = "testInstr2"; 
		Instrument dependentInstrument = createInstrument(instrumentCode2);
		dependentInstrument.setActivationStatus(ActivationStatus.Approved);
		dependentInstrument.setUnderlyingType(UnderlyingType.Instrument);
		dependentInstrument.setUnderlyingCode(instrumentCode1);
		
		createInstrumentInDatastore(dependentInstrument, datastores);
    	
    	// dependent not suspended - instrument 
		try {
			adminSystem.suspendInstrument(instrumentCode1, user);
			fail();
    	} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.INSTRUMENT_CAN_ONLY_BE_SUSPENDED_IF_DEPENDENT_OBJECTS_ARE_SUSPENDED, e.getLanguageKey());
		}
    	
    	setInstrumentToSuspended(instrumentCode2, datastores);
    	
       	// successful suspend
    	openTransaction();
		adminSystem.suspendInstrument(instrumentCode1, user);
		commitTransaction();
		
		instrumentLoaded = datastores.findInstrumentByCode(instrumentCode1);
		
    	assertEquals(instrumentCode1, instrumentLoaded.getCode());
    	assertEquals(ActivationStatus.Suspended, instrumentLoaded.getActivationStatus());
    	assertEquals(null, instrumentLoaded.getActivationDate());
    	assertEquals(user, instrumentLoaded.getApprovalAudit().getUserID());
    	
		checkAuditTrail(AuditEntryAction.Suspended, user, instrumentCode1, Instrument.class.getSimpleName(), adminSystem);
		
    	// suspend again  
		try {
			adminSystem.suspendInstrument(instrumentCode1, user);
			fail();
    	} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.INSTRUMENT_NOT_IN_CREATED_OR_SUSPEDED_STATE_CAN_BE_SUSPENDED, e.getLanguageKey());
		}
	}

	public void testDelete() throws Exception {
		MockDatastoresBase datastores = createDatastores();

		MarketManagementAdminSystem adminSystem = 
    		new MarketManagementAdminSystem(datastores, datastores, datastores, datastores, datastores, datastores, datastores, null);
    	
    	String productCode1 = "MYTESTPRODUCT"; 
		ProductDto product1 = createProduct1(productCode1); 		
		product1.setActivationStatus(com.imarcats.interfaces.client.v100.dto.types.ActivationStatus.Created);
		product1.setLastUpdateTimestamp(new Date());
    	
		createProductInDatastore(product1, datastores); 
    	
    	String instrumentCode1 = "MYTESTINSTR"; 
		Instrument instrument1 = createInstrument1(instrumentCode1, UnderlyingType.Product, productCode1); 
		instrument1.setActivationStatus(ActivationStatus.Created);
		instrument1.updateLastUpdateTimestamp();
    	
		createInstrumentInDatastore(instrument1, datastores);
		
		// test delete
    	MarketManagementContextImpl context = createMarketManagementContext();
    	
    	String user = "test";

    	setInstrumentToApproved(instrumentCode1, datastores);

    	// delete approved
		context = createMarketManagementContext();
    	try {
			testDeleteInstrument(instrumentCode1, adminSystem,
					user, context);
    		fail();
    	} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.INSTRUMENT_ONLY_IN_CREATED_OR_SUSPEDED_STATE_CAN_BE_DELETED, e.getLanguageKey());
		} finally {
			commitTransaction();
		}
    	
    	context = createMarketManagementContext();

    	setInstrumentToSuspended(instrumentCode1, datastores); 
		
      	// create a dependent instrument
    	String instrumentCode2 = "TestInstrumentCode";
    	Instrument instrument2 = createInstrument1(instrumentCode2, UnderlyingType.Instrument, instrumentCode1);
    	instrument2.updateLastUpdateTimestamp();
    	instrument2.setActivationStatus(ActivationStatus.Created);
    	createInstrumentInDatastore(instrument2, datastores); 
    	
		// create a dependent market 
    	String marketCode = "marketCode"; 
    	Market market = createMarket(marketCode);
    	market.setInstrumentCode(instrumentCode1);
    	
    	createMarketInDatastore(market, datastores); 
    	
    	context = createMarketManagementContext();

    	// dependent object - instrument 
		try {
			testDeleteInstrument(instrumentCode1, adminSystem,
					user, context);
			fail();
    	} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.INSTRUMENT_THAT_HAS_NO_DEPENDENT_INSTRUMENTS_OR_MARKET_CAN_BE_DELETED, e.getLanguageKey());
		} finally {
			commitTransaction();
		}
    	
    	
    	// remove dependency on instrument
    	deleteInstrumentInDatastore(instrumentCode2, datastores);
    	
    	
    	
    	// dependent object - instrument 
		try {
			testDeleteInstrument(instrumentCode1, adminSystem,
					user, context);
			fail();
    	} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.INSTRUMENT_THAT_HAS_NO_DEPENDENT_INSTRUMENTS_OR_MARKET_CAN_BE_DELETED, e.getLanguageKey());
		} finally {
			commitTransaction();
		}

		openTransaction();
    	MarketInternal marketInternal = datastores.findMarketBy(marketCode);
    	marketInternal.getMarketModel().setInstrumentCode("BOGUS");
    	commitTransaction();
    	
    	// successful delete 
    	testDeleteInstrument(instrumentCode1, adminSystem, user,
				context);

	}

	private void testDeleteInstrument(String instrumentCode,
			MarketManagementAdminSystem adminSystem,
			String user, MarketManagementContextImpl context) {
		openTransaction();
		adminSystem.deleteInstrument(instrumentCode, user, context);
		commitTransaction();
	}
	
	public void testSetupForChange() throws Exception {
		Instrument originalInstrument = new Instrument();
		
		// setup system fields

		originalInstrument.setActivationStatus(com.imarcats.model.types.ActivationStatus.Activated);
		originalInstrument.setActivationDate(new Date());
		originalInstrument.setMasterAgreementDocument("testDef");
		originalInstrument.setInstrumentCodeRolledOverFrom("testCode");
		
		originalInstrument.setCreationAudit(createAudit());
		originalInstrument.setChangeAudit(createAudit());
		originalInstrument.setApprovalAudit(createAudit());
		originalInstrument.setSuspensionAudit(createAudit());
		originalInstrument.setRolloverAudit(createAudit());
		
		
		Instrument newInstrument = new Instrument();
		
		InstrumentAdministrationSubSystem.setupChangedInstrument(originalInstrument, newInstrument);
		
		assertEqualsInstrument(originalInstrument, newInstrument);
	}
	
	public void testInstrumentRollover() throws Exception {
	  	MockDatastoresBase datastores = createDatastores();

	  	MarketManagementAdminSystem adminSystem = 
    		new MarketManagementAdminSystem(datastores, datastores, datastores, datastores, datastores, datastores, datastores, null);
    	
    	
    	// create test product 
    	String productCode1 = "TESTPRODUCT"; 
		ProductDto product1 = createProduct1(productCode1);
		product1.setActivationStatus(com.imarcats.interfaces.client.v100.dto.types.ActivationStatus.Created);
		product1.setLastUpdateTimestamp(new Date()); 
		product1.setRollable(true);

		String masterAgreementDocument = "Test Doc";
		product1.setProductDefinitionDocument(masterAgreementDocument);
		createProductInDatastore(product1, datastores);
		
    	// create test instrument
    	String instrumentCode1 = "TESTINSTR"; 
		InstrumentDto instrument1 = createInstrument1Dto(instrumentCode1, UnderlyingType.Product, productCode1);
		instrument1.setRollable(true);
		String user = "test";
		
		testCreateInstrument(instrument1, adminSystem, user);

    	// set master agreement
		testSetInstrumentMasterAgreementDocument(instrumentCode1,
				masterAgreementDocument, adminSystem, user);
    	
    	// this is needed because, DTO must have master agreement document for rollover 
    	instrument1.setMasterAgreementDocument(masterAgreementDocument);		
    	
    	String assetClassName = "TEST_ASSET_CLASS";
    	AssetClass assetClass = new AssetClass();
    	assetClass.setName(assetClassName);
    	assetClass.setDescription("Test");
    	assetClass.updateLastUpdateTimestamp();

    	createAssetClassInDatastore(assetClass, datastores); 
    	
    	testSetInstrumentAssetClass(instrumentCode1, assetClassName,
				user, adminSystem);
    	
    	// this is needed because, it needs reloading from the datastore anyway
    	instrument1.setAssetClassName(assetClassName); 
    	
		// test rollover of non-approved objects 
    	String instrumentCode1Rolled = "TEST_INSTR_ROLLED";
    	
    	InstrumentDto intrument1Rolled = createInstrument1Dto(instrumentCode1Rolled, UnderlyingType.Product, productCode1);
		intrument1Rolled.setRollable(true);
    	
		try {
    		testRolloverInstrument(instrumentCode1, intrument1Rolled,
					adminSystem, user);
    		fail();
    	} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.NON_APPROVED_OBJECT_CANNOT_BE_REFERRED_IN_ROLLOVER, e.getLanguageKey());
		} finally {
			commitTransaction();
		}
    	
		// approve product 
    	setProductToApproved(productCode1, datastores);
    	
    	// approve instrument
    	testApproveInstrument(instrumentCode1, user, adminSystem); 
    	
		// make sure that audit entries are far apart enough 
		Thread.sleep(1000); 
    	
		// test rollover to the same 
    	try {
    		testRolloverInstrument(instrumentCode1, instrument1, adminSystem,
					user);
    		fail();
    	} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.ROLLED_OBJECTS_HAVE_SAME_CODE, e.getLanguageKey());
		} finally {
			commitTransaction();
		}
    	
		// test validation problem 
		try {
			testRolloverInstrument(instrumentCode1, intrument1Rolled,
					adminSystem, user);
    		fail();
    	} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.INSTRUMENT_MUST_HAVE_MASTER_AGREEMENT_DOCUMENT, e.getLanguageKey());
		} finally {
			commitTransaction();
		}

    	intrument1Rolled.setMasterAgreementDocument(masterAgreementDocument);
    	intrument1Rolled.setAssetClassName(assetClassName);   	
    	
    	// test successful rollover 
    	testRolloverInstrument(instrumentCode1, intrument1Rolled, adminSystem,
				user);
    	
    	// capitalize rolled code 
    	instrumentCode1Rolled = instrumentCode1Rolled.toUpperCase();
    	
    	checkAuditTrail(AuditEntryAction.Rolled, user, instrumentCode1Rolled, Instrument.class.getSimpleName(), adminSystem);
    	
		// check rolled over instrument 
    	Instrument instrument1RolledLoaded = datastores.findInstrumentByCode(instrumentCode1Rolled);
    	
    	assertTrue(instrument1RolledLoaded != null);
    	assertEquals(instrumentCode1Rolled, instrument1RolledLoaded.getInstrumentCode());
    	assertEquals(instrumentCode1, instrument1RolledLoaded.getInstrumentCodeRolledOverFrom());
    	assertEquals(ActivationStatus.Approved, instrument1RolledLoaded.getActivationStatus());
    	assertEquals(user, instrument1RolledLoaded.getCreationAudit().getUserID());
    	assertEquals(user, instrument1RolledLoaded.getRolloverAudit().getUserID());
    	
    	// create instrument 2
    	String instrumentCode2 = "TESTINSTR2"; 
		InstrumentDto instrument2 = createInstrument1Dto(instrumentCode2, UnderlyingType.Instrument, instrumentCode1);
		instrument2.setRollable(true);
		
		testCreateInstrument(instrument2, adminSystem, user);
		
    	testSetInstrumentMasterAgreementDocument(instrumentCode2,
				masterAgreementDocument, adminSystem, user);
    	
    	// this is needed because, it needs reloading from the datastore anyway
    	instrument2.setMasterAgreementDocument(masterAgreementDocument);		
    	
    	testSetInstrumentAssetClass(instrumentCode2, assetClassName,
				user, adminSystem);
    	
    	// this is needed because, it needs reloading from the datastore anyway
    	instrument2.setAssetClassName(assetClassName); 
    	
    	testApproveInstrument(instrumentCode2, user, adminSystem);
    	
    	// test roll over
    	String instrumentCode2Rolled = "TEST_INSTR_ROLLED2";
    	
    	InstrumentDto intrument2Rolled = createInstrument1Dto(instrumentCode2Rolled, UnderlyingType.Instrument, instrumentCode1Rolled);
		intrument2Rolled.setRollable(true);
    	intrument2Rolled.setMasterAgreementDocument(masterAgreementDocument);
    	intrument2Rolled.setAssetClassName(assetClassName);
    	
    	// test successful rollover 
    	testRolloverInstrument(instrumentCode2, intrument2Rolled, adminSystem,
				user);
    	
    	// capitalize rolled code 
    	instrumentCode2Rolled = instrumentCode2Rolled.toUpperCase();
    	
		// check rolled over instrument 
    	Instrument instrument2RolledLoaded = datastores.findInstrumentByCode(instrumentCode2Rolled);
    	
    	assertTrue(instrument2RolledLoaded != null);
    	assertEquals(instrumentCode2Rolled, instrument2RolledLoaded.getInstrumentCode());
    	assertEquals(instrumentCode2, instrument2RolledLoaded.getInstrumentCodeRolledOverFrom());
    	assertEquals(ActivationStatus.Approved, instrument2RolledLoaded.getActivationStatus());
    	assertEquals(user, instrument2RolledLoaded.getCreationAudit().getUserID());
    	assertEquals(user, instrument2RolledLoaded.getRolloverAudit().getUserID());
    	assertEquals(assetClassName, instrument2RolledLoaded.getAssetClassName());
    	assertEquals(masterAgreementDocument, instrument2RolledLoaded.getMasterAgreementDocument());
    	assertEquals(true, instrument2RolledLoaded.getRollable());
    	
    	// test rollover by admin 
    	String instrumentCode3Rolled = "TEST_INSTR_ROLLED_3";
    	
    	InstrumentDto instrument3Rolled = createInstrument1Dto(instrumentCode3Rolled, UnderlyingType.Product, productCode1); 
		instrument3Rolled.setRollable(true);
		instrument3Rolled.setAssetClassName(assetClassName);
		instrument3Rolled.setMasterAgreementDocument(masterAgreementDocument);
		
    	testRolloverInstrument(instrumentCode1, instrument3Rolled, adminSystem,
				user);
	}

	private void testRolloverInstrument(String instrumentCode,
			InstrumentDto intrumentRolled,
			MarketManagementAdminSystem adminSystem, String user) {
		openTransaction();
		adminSystem.rolloverInstrument(instrumentCode, intrumentRolled, user);
		commitTransaction();
	}

	private void testSetInstrumentMasterAgreementDocument(
			String instrumentCode, String masterAgreementDocument,
			MarketManagementAdminSystem adminSystem, String user) {
    	testSetMasterAgreement(masterAgreementDocument, instrumentCode,
				adminSystem, user);
	}
	
	public void testDependencyCheck() throws Exception {
	  	MockDatastoresBase datastores = createDatastores();
    	MarketManagementAdminSystem adminSystem = 
    		new MarketManagementAdminSystem(datastores, datastores, datastores, datastores, datastores, datastores, datastores, null);
    	
    	String user = "test";
    	
    	// create test product 
    	String productCode1 = "TESTPRODUCT"; 
		ProductDto product1 = createProduct1(productCode1);
		product1.setActivationStatus(com.imarcats.interfaces.client.v100.dto.types.ActivationStatus.Created);
		product1.setLastUpdateTimestamp(new Date()); 
		product1.setRollable(true);

		createProductInDatastore(product1, datastores); 
		
    	// test max dependency levels 
    	String underlyingCode = productCode1;
    	UnderlyingType underlyingType = UnderlyingType.Product;
    	int cnt = 10;
    	String firstCode = null;
    	String beforeLastCode = null;
    	try {
			for (int i = 0; i < cnt; i++) {
				InstrumentDto instrument = createInstrument1Dto("TESTINSTR" + i, underlyingType, underlyingCode);
				
				testCreateInstrument(instrument, adminSystem, user);
		    	
		    	underlyingCode = instrument.getInstrumentCode();
		    	underlyingType = UnderlyingType.Instrument;
		    	
		    	if(firstCode == null) {
		    		firstCode = instrument.getInstrumentCode();
		    	}

	    		beforeLastCode = instrument.getUnderlyingCode();
			}
			fail();
    	} catch (MarketRuntimeException e) {
    		assertEquals(ExceptionLanguageKeys.TOO_DEEP_UNDERLYING_DEPENDENCY, e.getLanguageKey());
		}
    	
    	// test circular dependency
    	
    	// it is weird, that we had to provide null as  here to cut 
    	// the firstInstrument from its datastore version,
    	// if it is not cut from its datastore version (queried and changed in the same ), 
    	// in change a fresh version is loaded from the datastore it changes firstInstrument
    	// back to the same values as datastore has
    	Instrument firstInstrument = datastores.findInstrumentByCode(firstCode);
    	firstInstrument.setUnderlyingCode(beforeLastCode);
    	firstInstrument.setUnderlyingType(UnderlyingType.Instrument);
    	
    	try {
    		adminSystem.changeInstrument(instrumentToDto(firstInstrument), user);
	    	fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.CIRCULAR_UNDERLYING_DEPENDENCY, e.getLanguageKey());
		}	
		
    	// test self dependency
		
    	// it is weird, that we had to provide null as  here to cut 
    	// the firstInstrument from its datastore version,
    	// if it is not cut from its datastore version (queried and changed in the same ), 
    	// in change a fresh version is loaded from the datastore it changes firstInstrument
    	// back to the same values as datastore has
    	firstInstrument = datastores.findInstrumentByCode(firstCode);
    	firstInstrument.setUnderlyingCode(firstInstrument.getInstrumentCode());
    	firstInstrument.setUnderlyingType(UnderlyingType.Instrument);
    	
    	try {
    		adminSystem.changeInstrument(instrumentToDto(firstInstrument), user);
	    	fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.CIRCULAR_UNDERLYING_DEPENDENCY, e.getLanguageKey());
		}	
	}
	
	public void testLoadAndChange() throws Exception {
	  	MockDatastoresBase datastores = createDatastores();
	  	
	  	MarketManagementAdminSystem adminSystem = 
    		new MarketManagementAdminSystem(datastores, datastores, datastores, datastores, datastores, datastores, datastores, null);
    	
    	 
    	// create test product 
    	String productCode1 = "TESTPRODUCT"; 
		ProductDto product1 = createProduct1(productCode1);
		product1.setActivationStatus(com.imarcats.interfaces.client.v100.dto.types.ActivationStatus.Created);
		product1.setLastUpdateTimestamp(new Date()); 
		product1.setRollable(true);
		
		createProductInDatastore(product1, datastores);
		
    	// create test instrument 
		String instrumentCode1 = "TESTINSTR";
		InstrumentDto instrument = createInstrument1Dto(instrumentCode1, UnderlyingType.Product, productCode1);
		
		String user = "test"; 
		testCreateInstrument(instrument, adminSystem, user);
		
    	// load instrument
    	
    	// it is weird, that we had to provide null as  here to cut 
    	// the instrumentToBeChanged from its datastore version,
    	// if it is not cut from its datastore version (queried and changed in the same ), 
    	// in change a fresh version is loaded from the datastore it changes firstInstrument
    	// back to the same values as datastore has
    	Instrument instrumentToBeChanged = datastores.findInstrumentByCode(instrumentCode1);
    	instrumentToBeChanged.setDescription("New Descr");
    	String city = "London";
		instrumentToBeChanged.getDeliveryLocation().setCity(city);

    	// change instrument
		testChangeInstrument(instrumentToBeChanged, adminSystem, user);
		
    	// check change 
    	Instrument instrumentLoaded = datastores.findInstrumentByCode(instrumentCode1);
    	
    	assertEquals(instrumentToBeChanged.getInstrumentCode(), instrumentLoaded.getInstrumentCode());
    	assertEquals(instrumentToBeChanged.getDescription(), instrumentLoaded.getDescription());
    	assertEquals(city, instrumentLoaded.getDeliveryLocation().getCity());
    	
	}

	private void testChangeInstrument(Instrument instrumentToBeChanged,
			MarketManagementAdminSystem adminSystem, String user) {
		openTransaction();
		adminSystem.changeInstrument(instrumentToDto(instrumentToBeChanged), user);
		commitTransaction();
	}
}
