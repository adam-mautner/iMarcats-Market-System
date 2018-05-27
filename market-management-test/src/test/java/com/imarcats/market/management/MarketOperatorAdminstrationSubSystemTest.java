package com.imarcats.market.management;

import java.util.Date;

import com.imarcats.interfaces.client.v100.dto.MarketOperatorDto;
import com.imarcats.interfaces.client.v100.exception.ExceptionLanguageKeys;
import com.imarcats.interfaces.client.v100.exception.MarketRuntimeException;
import com.imarcats.internal.server.infrastructure.testutils.MockDatastoresBase;
import com.imarcats.internal.server.interfaces.market.MarketInternal;
import com.imarcats.market.management.admin.MarketManagementAdminSystem;
import com.imarcats.market.management.admin.MarketOperatorAdminstrationSubSystem;
import com.imarcats.model.Market;
import com.imarcats.model.MarketOperator;
import com.imarcats.model.types.ActivationStatus;
import com.imarcats.model.types.AuditEntryAction;

public class MarketOperatorAdminstrationSubSystemTest extends ManagementTestBase {

	public void testCreate() throws Exception {
	  	MockDatastoresBase datastores = createDatastores();
    	
    	MarketManagementAdminSystem adminSystem = 
    		new MarketManagementAdminSystem(datastores, datastores, datastores, datastores, datastores, datastores, datastores, null);
    	
    	String businessEntityCode = "TEST_BE";
    	
    	// test create market operator 
    	String mktOptCode1 = "MYTESTKKTOPT"; 
		MarketOperatorDto mktOpt1 = createMarketOperator1(mktOptCode1);
		mktOpt1.setBusinessEntityCode(businessEntityCode);
    	
		String ownerUserID = "ownerUserID"; 
		mktOpt1.setOwnerUserID(ownerUserID);
		
		String user = "test";
		
		// successful creation
		testCreateMarketOperator(mktOpt1, adminSystem, user);
    	
    	checkAuditTrail(AuditEntryAction.Created, user, mktOptCode1, MarketOperator.class.getSimpleName(), adminSystem);
    	
    	// check market operator
    	MarketOperator mktOpt1Loaded = datastores.findMarketOperatorByCode(mktOptCode1);

    	assertEquals(mktOptCode1, mktOpt1Loaded.getCode());
    	assertEquals(ownerUserID, mktOpt1Loaded.getOwnerUserID());
       	assertEquals(ActivationStatus.Created, mktOpt1Loaded.getActivationStatus());
    	assertEquals(user, mktOpt1Loaded.getCreationAudit().getUserID());       	
	}

	private void testCreateMarketOperator(MarketOperatorDto mktOpt,
			MarketManagementAdminSystem adminSystem,
			String user) {
		openTransaction();
    	adminSystem.createMarketOperator(mktOpt, user); 
    	commitTransaction();
	}

	public void testChange() throws Exception {
	  	MockDatastoresBase datastores = createDatastores();
    	
    	MarketManagementAdminSystem adminSystem = 
    		new MarketManagementAdminSystem(datastores, datastores, datastores, datastores, datastores, datastores, datastores, null);
    	
    	String user = "test";
    	String businessEntityCode = "TEST_BE";
    	
    	// test create market operator 
    	String mktOptCode1 = "MYTESTKKTOPT"; 
		MarketOperatorDto mktOpt1 = createMarketOperator1(mktOptCode1);
		mktOpt1.setLastUpdateTimestamp(new Date());
		mktOpt1.setActivationStatus(com.imarcats.interfaces.client.v100.dto.types.ActivationStatus.Created);
		mktOpt1.setBusinessEntityCode(businessEntityCode);
		
		createMarketOperatorInDatastore(mktOpt1, datastores);
		
     	// test change market operator 
    	MarketOperatorDto mktOpt1Changed = createMarketOperator1(mktOptCode1);
		mktOpt1Changed.setDescription("New Descrition");
		mktOpt1Changed.setBusinessEntityCode("BOGUS");
		mktOpt1Changed.setLastUpdateTimestamp(new Date());

		// change approved market operator 
		setMarketOperatorToApproved(mktOptCode1, datastores);
		
    	// recreate MarketOperator to get a later version 
    	mktOpt1Changed = createMarketOperator1(mktOptCode1);
		mktOpt1Changed.setDescription(mktOpt1Changed.getDescription());
		mktOpt1Changed.setBusinessEntityCode("IMARCATS");
		mktOpt1Changed.setLastUpdateTimestamp(new Date());
		
    	// recreate MarketOperator to get a later version 
    	mktOpt1Changed = createMarketOperator1(mktOptCode1);
		mktOpt1Changed.setDescription(mktOpt1Changed.getDescription());
		mktOpt1Changed.setBusinessEntityCode("BOGUS");
		mktOpt1Changed.setLastUpdateTimestamp(new Date());
		
		mktOpt1Changed.setBusinessEntityCode(businessEntityCode);

		String ownerUserID = "ownerUserID1"; 
		mktOpt1Changed.setOwnerUserID(ownerUserID);
    	
    	// non-existent market operator 
		mktOpt1Changed.setCode(mktOptCode1 + "1"); 
    	try {
			testChangeMarketOperator(mktOpt1Changed, adminSystem,
					user);
			fail();
    	} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.NON_EXISTENT_MARKET_OPERATOR, e.getLanguageKey());
		} finally {
			commitTransaction();
		}
		mktOpt1Changed.setCode(mktOptCode1); 
		
		// approved 
    	try {
			testChangeMarketOperator(mktOpt1Changed, adminSystem,
					user);
    		fail();
    	} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.MARKET_OPERATOR_ONLY_IN_CREATED_OR_SUSPEDED_STATE_CAN_BE_CHANGED, e.getLanguageKey());
		} finally {
			commitTransaction();
		}
    	
    	setMarketOperatorToSuspended(mktOptCode1, datastores);
    	
    	// recreate MarketOperator to get a later version 
    	MarketOperator mktOpt1Loaded = datastores.findMarketOperatorByCode(mktOptCode1);
    	mktOpt1Changed = createMarketOperator1(mktOptCode1);
    	mktOpt1Changed.setDescription(mktOpt1Changed.getDescription());
    	mktOpt1Changed.setBusinessEntityCode(businessEntityCode);
    	mktOpt1Changed.setOwnerUserID(ownerUserID);
		mktOpt1Changed.setLastUpdateTimestamp(new Date());
		mktOpt1Changed.setVersionNumber(mktOpt1Loaded.getVersionNumber());
		
		// make sure that audit entries are far apart enough 
		Thread.sleep(1000); 
		
    	// successful change 
    	testChangeMarketOperator(mktOpt1Changed, adminSystem, user);
    	
    	checkAuditTrail(AuditEntryAction.Changed, user, mktOptCode1, MarketOperator.class.getSimpleName(), adminSystem);
    	
    	// check change of market operator 
    	mktOpt1Loaded = datastores.findMarketOperatorByCode(mktOptCode1);

    	assertEquals(mktOptCode1, mktOpt1Loaded.getCode());
    	assertEquals(mktOpt1Changed.getDescription(), mktOpt1Loaded.getDescription());
    	assertEquals(ActivationStatus.Suspended, mktOpt1Loaded.getActivationStatus());
    	assertEquals(user, mktOpt1Loaded.getChangeAudit().getUserID());
  
    	setMarketOperatorToSuspended(mktOptCode1, datastores);
    	
    	// recreate MarketOperator to get a later version 
    	mktOpt1Changed = createMarketOperator1(mktOptCode1);
    	mktOpt1Changed.setDescription(mktOpt1Changed.getDescription());
    	mktOpt1Changed.setBusinessEntityCode(businessEntityCode);
    	mktOpt1Changed.setOwnerUserID(ownerUserID);
		mktOpt1Changed.setLastUpdateTimestamp(new Date());
		mktOpt1Changed.setVersionNumber(mktOpt1Loaded.getVersionNumber());

    	Thread.sleep(1000);
		
    	// successful change
    	testChangeMarketOperator(mktOpt1Changed, adminSystem, user);
    	
    	// overwrite with older version 
    	openTransaction();
    	datastores.findMarketOperatorByCode(mktOpt1Changed.getCode()).setDescription("Test Decr");
    	commitTransaction();
    	
    	try {
    		testChangeMarketOperator(mktOpt1Changed, adminSystem,
					user);
    		fail();
    	} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.MARKET_OPERATOR_CANNOT_BE_OVERWRITTEN_WITH_AN_OLDER_VERSION, e.getLanguageKey());
		} finally {
			commitTransaction();
		}
	}

	private void testChangeMarketOperator(MarketOperatorDto mktOptChanged,
			MarketManagementAdminSystem adminSystem,
			String user) {
		openTransaction();
		adminSystem.changeMarketOperator(mktOptChanged, user);
		commitTransaction();
	}

	public void testSetMarketOperatorMasterAgreementDocument() throws Exception {
	  	MockDatastoresBase datastores = createDatastores();

	  	MarketManagementAdminSystem adminSystem = 
    		new MarketManagementAdminSystem(datastores, datastores, datastores, datastores, datastores, datastores, datastores, null);
    	
    	String businessEntityCode = "TestBusinessEntity";
    	String user = "test";
    	
    	// test create market operator 
    	String mktOptCode1 = "MYTESTKKTOPT"; 
		MarketOperatorDto mktOpt1 = createMarketOperator1(mktOptCode1);
		mktOpt1.setActivationStatus(com.imarcats.interfaces.client.v100.dto.types.ActivationStatus.Created);
		mktOpt1.setBusinessEntityCode(businessEntityCode);
		mktOpt1.setLastUpdateTimestamp(new Date());

		createMarketOperatorInDatastore(mktOpt1, datastores);
		
	   	// test set market operator agreement document

		String mktOptAgreementDocument = "My Mkt Opt Agreement Documement";
		
    	// non-existent market operator 
    	try {
			adminSystem.setMarketOperatorMasterAgreementDocument("BOGUS", mktOptAgreementDocument, user);
			fail();
    	} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.NON_EXISTENT_MARKET_OPERATOR, e.getLanguageKey());
		}
    	
    	testSetMarketOperationDocument(mktOptCode1, mktOptAgreementDocument,
				adminSystem, user);
    	
    	checkAuditTrail(AuditEntryAction.Changed, user, mktOptCode1, MarketOperator.class.getSimpleName(), adminSystem);
    	
    	// check market operator agreement set
    	MarketOperator mktOpt1Loaded = datastores.findMarketOperatorByCode(mktOptCode1);

    	assertEquals(mktOptCode1, mktOpt1Loaded.getCode());
    	assertEquals(mktOptAgreementDocument, mktOpt1Loaded.getMarketOperatorAgreement());

    	setMarketOperatorToApproved(mktOptCode1, datastores);
    	
    	// try changing agreement document
    	try {
			adminSystem.setMarketOperatorMasterAgreementDocument(mktOptCode1, "New Document", user);
			fail();
    	} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.MARKET_OPERATOR_ONLY_IN_CREATED_OR_SUSPEDED_STATE_CAN_BE_CHANGED, e.getLanguageKey());
		}
    	
    	setMarketOperatorToSuspended(mktOptCode1, datastores);
    	
    	// successful change - suspended 
    	testSetMarketOperationDocument(mktOptCode1, "New Document", adminSystem, user);
    	
    	setMarketOperatorToCreated(mktOptCode1, datastores);
    	
    	// successful change - created 
    	testSetMarketOperationDocument(mktOptCode1, "New Document", adminSystem, user);
	}

	private void testSetMarketOperationDocument(String mktOptCode,
			String mktOptAgreementDocument,
			MarketManagementAdminSystem adminSystem,
			String user) {
		openTransaction();
    	adminSystem.setMarketOperatorMasterAgreementDocument(mktOptCode, mktOptAgreementDocument, user);
    	commitTransaction();
	}
	
	@SuppressWarnings("deprecation")
	public void testApproveSuspend() throws Exception {
	  	MockDatastoresBase datastores = createDatastores();

	  	MarketManagementAdminSystem adminSystem = 
    		new MarketManagementAdminSystem(datastores, datastores, datastores, datastores, datastores, datastores, datastores, null);
    	
    	String businessEntityCode = "TBE";
    	
		String ownerUserID = "ownerUserID"; 
		String user = "test";
    	// test create market operator 
    	String mktOptCode1 = "MYTESTKKTOPT"; 
		MarketOperatorDto mktOpt1 = createMarketOperator1(mktOptCode1);
		mktOpt1.setActivationStatus(com.imarcats.interfaces.client.v100.dto.types.ActivationStatus.Created);
		mktOpt1.setBusinessEntityCode(businessEntityCode);
		mktOpt1.setOwnerUserID(ownerUserID);
		mktOpt1.setLastUpdateTimestamp(new Date());
	
		createMarketOperatorInDatastore(mktOpt1, datastores);
		
    	// no market operator agreement
		try {
			testApprove(user, adminSystem, mktOptCode1);
			fail();
    	} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.MARKET_OPERATOR_MUST_HAVE_MARKET_OPERATOR_AGREEMENT_DOCUMENT, e.getLanguageKey());
		} finally {
			commitTransaction();
		}
    	
    	openTransaction();
    	datastores.findMarketOperatorByCode(mktOptCode1).setMarketOperatorAgreement("Test");
    	commitTransaction();
    	
    	// non-existent market operator
		try {
			adminSystem.approveMarketOperator("BOGUS", new Date(), user);
			fail();
    	} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.NON_EXISTENT_MARKET_OPERATOR, e.getLanguageKey());
		}
    	
    	// stale object 
		try {
			Date lastUpdateTimestamp = new Date();
			lastUpdateTimestamp.setMinutes(lastUpdateTimestamp.getMinutes() - 10);
			adminSystem.approveMarketOperator(mktOptCode1, lastUpdateTimestamp, user);
			fail();
    	} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.STALE_OBJECT_CANNOT_BE_APPROVED, e.getLanguageKey());
		}
    	
    	// successful approval 
		testApprove(user, adminSystem, mktOptCode1);
    	
    	checkAuditTrail(AuditEntryAction.Approved, user, mktOptCode1, MarketOperator.class.getSimpleName(), adminSystem);
    	
    	// check market operator approval
    	MarketOperator mktOpt1Loaded = datastores.findMarketOperatorByCode(mktOptCode1);
		
    	assertEquals(mktOptCode1, mktOpt1Loaded.getCode());
    	assertEquals(ActivationStatus.Approved, mktOpt1Loaded.getActivationStatus());
    	assertEquals(user, mktOpt1Loaded.getApprovalAudit().getUserID());
    	
    	// try approving again 
		try {
			testApprove(user, adminSystem, mktOptCode1);
			fail();
    	} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.MARKET_OPERATOR_ONLY_IN_CREATED_OR_SUSPEDED_STATE_CAN_BE_APPROVED, e.getLanguageKey());
		} finally {
			commitTransaction();
		}
    	
		// add dependent market 
    	String marketCode = "TestMarketCode";
    	Market market = createMarket(marketCode);
    	market.setMarketOperatorCode(mktOptCode1);
    	market.setActivationStatus(ActivationStatus.Activated);
    	
    	createMarketInDatastore(market, datastores);
    	
    	// dependent market  
		try {
			testSuspend(mktOptCode1, adminSystem, user);
			fail();
    	} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.MARKET_OPERATOR_CAN_ONLY_BE_SUSPENDED_IF_DEPENDENT_OBJECTS_ARE_SUSPENDED, e.getLanguageKey());
		} finally {
			commitTransaction();
		}
    	
    	// suspend market 
    	setMarketToSuspended(marketCode, datastores);
    	
    	setMarketOperatorToSuspended(mktOptCode1, datastores);
    	
    	// not approved 
    	try {
			testSuspend(mktOptCode1, adminSystem, user);
			fail();
    	} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.MARKET_OPERATOR_NOT_IN_CREATED_OR_SUSPEDED_STATE_CAN_BE_SUSPENDED, e.getLanguageKey());
		} finally {
			commitTransaction();
		}

    	setMarketOperatorToApproved(mktOptCode1, datastores);
    	
    	// successful suspend
		testSuspend(mktOptCode1, adminSystem, user);
		
    	// somehow this does not work in GAE tests 
//    	pm = saveToDatastore(pm);
//    	
//    	checkAuditTrail(AuditEntryAction.Suspended, approverUserSession.getUserName(), marketOperatorCode1, MarketOperator.class.getSimpleName(), adminSystem);
//    	pm = saveToDatastore(pm);
    	
		// check market operator after suspend 
    	mktOpt1Loaded = datastores.findMarketOperatorByCode(mktOptCode1);

    	assertEquals(mktOptCode1, mktOpt1Loaded.getCode());
    	assertEquals(ActivationStatus.Suspended, mktOpt1Loaded.getActivationStatus());
    	assertEquals(user, mktOpt1Loaded.getSuspensionAudit().getUserID());
	}

	private void testSuspend(String mktOptCode,
			MarketManagementAdminSystem adminSystem,
			String user) {
		openTransaction();
		adminSystem.suspendMarketOperator(mktOptCode, user);
		commitTransaction();
	}

	private void testApprove(
			String user,
			MarketManagementAdminSystem adminSystem, String mktOptCode1) {
		openTransaction();
		adminSystem.approveMarketOperator(mktOptCode1, new Date(), user);
		commitTransaction();
	}
	
	public void testDelete() throws Exception {
	  	MockDatastoresBase datastores = createDatastores();
    	
	  	MarketManagementAdminSystem adminSystem = 
    		new MarketManagementAdminSystem(datastores, datastores, datastores, datastores, datastores, datastores, datastores, null);
    	
    	String businessEntityCode = "TestBusinessEntity";
    	String user = "test";
    	
    	// test create market operator 
    	String mktOptCode1 = "MYTESTKKTOPT"; 
		MarketOperatorDto mktOpt1 = createMarketOperator1(mktOptCode1);
		mktOpt1.setActivationStatus(com.imarcats.interfaces.client.v100.dto.types.ActivationStatus.Created);
		mktOpt1.setBusinessEntityCode(businessEntityCode);
		mktOpt1.setLastUpdateTimestamp(new Date());

		createMarketOperatorInDatastore(mktOpt1, datastores); 
		
		MarketManagementContextImpl context = createMarketManagementContext();
		
    	setMarketOperatorToApproved(mktOptCode1, datastores);
    	
    	context = createMarketManagementContext();
    	
		// make sure that audit entries are far apart enough 
		Thread.sleep(1000); 
    	
    	// approved market operator 
    	try {
			testDelete(mktOptCode1, adminSystem, user, context);
    		fail();
    	} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.MARKET_OPERATOR_ONLY_IN_CREATED_OR_SUSPEDED_STATE_CAN_BE_DELETED, e.getLanguageKey());
		} finally {
			commitTransaction();
		}
    	
    	setMarketOperatorToSuspended(mktOptCode1, datastores);
    	
		// add dependent market 
    	String marketCode = "TestMarketCode";
    	Market market = createMarket(marketCode);
    	market.setMarketOperatorCode(mktOptCode1);
    	market.setActivationStatus(ActivationStatus.Activated);
    	
    	createMarketInDatastore(market, datastores);
    	
    	 
    	
    	// dependent market 
		try {
			testDelete(mktOptCode1, adminSystem, user, context);
			fail();
    	} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.MARKET_OPERATOR_THAT_HAS_NO_DEPENDENT_MARKET_CAN_BE_DELETED, e.getLanguageKey());
		} finally {
			commitTransaction();
		}

		// remove dependency on market operator 
    	openTransaction();
    	MarketInternal marketInternal = datastores.findMarketBy(marketCode);
    	marketInternal.getMarketModel().setMarketOperatorCode("BOGUS");
    	commitTransaction();

		// make sure that audit entries are far apart enough 
		Thread.sleep(1000); 
    	
    	// successful delete
		testDelete(mktOptCode1, adminSystem, user, context);

		// check mkt opt after delete
    	Object mktOpt1Loaded = datastores.findMarketOperatorByCode(mktOptCode1);

    	assertEquals(null, mktOpt1Loaded);
    	
    	checkAuditTrailForDeleteObject(user, mktOptCode1, MarketOperator.class.getSimpleName(), adminSystem);
	}

	private void testDelete(String mktOptCode,
			MarketManagementAdminSystem adminSystem, String user,
			MarketManagementContextImpl context) {
		openTransaction();
		adminSystem.deleteMarketOperator(mktOptCode, user, context);
		commitTransaction();
	}

	public void testSetupChangedMarketOperator() throws Exception {
		MarketOperator marketOperatorProduct = new MarketOperator();
		
		// reset system fields - as they will not be copied

		marketOperatorProduct.setActivationStatus(com.imarcats.model.types.ActivationStatus.Activated);
		marketOperatorProduct.setMarketOperatorAgreement("testDef");
		
		marketOperatorProduct.setCreationAudit(createAudit());
		marketOperatorProduct.setChangeAudit(createAudit());
		marketOperatorProduct.setApprovalAudit(createAudit());
		marketOperatorProduct.setSuspensionAudit(createAudit());
		
		MarketOperator newMarketOperator = new MarketOperator();
		
		MarketOperatorAdminstrationSubSystem.setupChangedMarketOperator(marketOperatorProduct, newMarketOperator);
		
		assertEqualsMarketOperator(marketOperatorProduct, newMarketOperator);
	}
		
	public void testLoadAndChange() throws Exception {
	  	MockDatastoresBase datastores = createDatastores();

    	MarketManagementAdminSystem adminSystem = 
    		new MarketManagementAdminSystem(datastores, datastores, datastores, datastores, datastores, datastores, datastores, null);
    	
    	String user = "test";
    	
    	// create market operator  
    	String mktOptCode1 = "TEST_MKT_OPT"; 
    	MarketOperatorDto mktOpt1 = createMarketOperator1(mktOptCode1);
	
    	String businessEntityCode = "TEST_BE";
    	mktOpt1.setBusinessEntityCode(businessEntityCode);
    	mktOpt1.setOwnerUserID(user);
		mktOpt1.setLastUpdateTimestamp(new Date());
		mktOpt1.setActivationStatus(com.imarcats.interfaces.client.v100.dto.types.ActivationStatus.Created);

    	mktOpt1.setActivationStatus(com.imarcats.interfaces.client.v100.dto.types.ActivationStatus.Created);
    	createMarketOperatorInDatastore(mktOpt1, datastores);
    	
    	// market operator code will be capitalized
    	 mktOptCode1 = mktOptCode1.toUpperCase();
    	
    	// load market operator  
    	
    	MarketOperator marketOperatorToBeChanged = datastores.findMarketOperatorByCode(mktOptCode1);

    	marketOperatorToBeChanged.setDescription("New Descr");
    
    	String manipulatorUser = "testManipulator"; 
		// change market operator 
    	marketOperatorToBeChanged.setOwnerUserID(manipulatorUser);
    	testChange(marketOperatorToBeChanged, adminSystem, user);

    	// test changed
    	MarketOperator mktOpt1Loaded = datastores.findMarketOperatorByCode(mktOptCode1);

    	assertEquals(mktOptCode1, mktOpt1Loaded.getCode());
    	assertEquals(marketOperatorToBeChanged.getDescription(), mktOpt1Loaded.getDescription());
    	// assertEqualsBusinessEntity(marketOperatorToBeChanged.getBusinessEntity(), mktOpt1Loaded.getBusinessEntity());    	
	}

	private void testChange(MarketOperator marketOperatorToBeChanged,
			MarketManagementAdminSystem adminSystem, String user) {
		openTransaction();
		adminSystem.changeMarketOperator(marketOperatorToDto(marketOperatorToBeChanged), user);
		commitTransaction();
	}
}
