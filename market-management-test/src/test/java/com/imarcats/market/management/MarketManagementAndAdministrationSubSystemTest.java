package com.imarcats.market.management;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import com.imarcats.interfaces.client.v100.dto.InstrumentDto;
import com.imarcats.interfaces.client.v100.dto.MarketDto;
import com.imarcats.interfaces.client.v100.dto.MarketOperatorDto;
import com.imarcats.interfaces.client.v100.dto.ProductDto;
import com.imarcats.interfaces.client.v100.dto.types.BusinessCalendarDto;
import com.imarcats.interfaces.client.v100.dto.types.PagedAuditTrailEntryListDto;
import com.imarcats.interfaces.client.v100.dto.types.PermissionRequestDto;
import com.imarcats.interfaces.client.v100.exception.ExceptionLanguageKeys;
import com.imarcats.interfaces.client.v100.exception.MarketRuntimeException;
import com.imarcats.interfaces.server.v100.dto.mapping.MarketDtoMapping;
import com.imarcats.internal.server.infrastructure.testutils.MockDatastoresBase;
import com.imarcats.internal.server.infrastructure.timer.TimerUtils;
import com.imarcats.internal.server.infrastructure.trigger.CallMarketAction;
import com.imarcats.internal.server.infrastructure.trigger.CallMarketMaintenanceAction;
import com.imarcats.internal.server.infrastructure.trigger.CloseMarketAction;
import com.imarcats.internal.server.infrastructure.trigger.MarketMaintenanceAction;
import com.imarcats.internal.server.infrastructure.trigger.OpenMarketAction;
import com.imarcats.internal.server.interfaces.market.MarketInternal;
import com.imarcats.internal.server.interfaces.market.MarketPropertyChangeExecutor;
import com.imarcats.market.management.admin.MarketAdministrationSubSystem;
import com.imarcats.market.management.admin.MarketManagementAdminSystem;
import com.imarcats.market.management.client.MarketManagementSubSystem;
import com.imarcats.model.AssetClass;
import com.imarcats.model.Instrument;
import com.imarcats.model.Market;
import com.imarcats.model.types.ActivationStatus;
import com.imarcats.model.types.AuditEntryAction;
import com.imarcats.model.types.ExecutionSystem;
import com.imarcats.model.types.MarketState;
import com.imarcats.model.types.QuoteType;
import com.imarcats.model.types.RecurringActionDetail;
import com.imarcats.model.types.TimeOfDay;
import com.imarcats.model.types.TradingSession;
import com.imarcats.model.types.UnderlyingType;

public class MarketManagementAndAdministrationSubSystemTest extends ManagementTestBase {

	private static final String TEST_BUSINESS_ENITY_0 = "TEST_BE";
	private static final int DAY_IN_MILLIS = 1 * 24 * 60 * 60 * 1000;
	
	private void createUnderlying(MarketManagementAdminSystem adminSystem, MockDatastoresBase datastores, String productCode1, String instrumentCode1, String marketOperatorCode1, String user) throws Exception {
    	
    	
    	openTransaction();
    	
    	// create test product 
		ProductDto product1 = createProduct1(productCode1);
		product1.setActivationStatus(com.imarcats.interfaces.client.v100.dto.types.ActivationStatus.Created);
		product1.setLastUpdateTimestamp(new Date()); 
		datastores.createProduct(productFromDto(product1));
    	
    	// test create instrument 
    	Instrument instrument1 = createInstrument1(instrumentCode1, UnderlyingType.Product, productCode1);
    	instrument1.setAssetClassName("TEST");
    	instrument1.setMasterAgreementDocument("Test");
    	instrument1.setActivationStatus(ActivationStatus.Created);
    	instrument1.setLastUpdateTimestamp(new Date()); 
    	datastores.createInstrument(instrument1);
    	
    	commitTransaction();
    	
    	// test create market operator 
		MarketOperatorDto mktOpt1 = createMarketOperator1(marketOperatorCode1);
		mktOpt1.setLastUpdateTimestamp(new Date()); 
		mktOpt1.setActivationStatus(com.imarcats.interfaces.client.v100.dto.types.ActivationStatus.Created);

    	// create business entity
    	String businessEntityCode = TEST_BUSINESS_ENITY_0;
	
    	mktOpt1.setBusinessEntityCode(businessEntityCode);
	
    	openTransaction();
    	datastores.createMarketOperator(marketOperatorFromDto(mktOpt1));
    	commitTransaction();
	}
	
	protected void createProductInDatastore(ProductDto product1,
			MarketManagementAdminSystem adminSystem,
			String user) {
		adminSystem.createProduct(product1, user);
	}
	
	public void testCreate() throws Exception {
	 	MockDatastoresBase datastores = createDatastores();
    	
	 	MarketManagementAdminSystem adminSystem = 
    		new MarketManagementAdminSystem(datastores, datastores, datastores, datastores, datastores, datastores, datastores, null);

    	String user = "test";
	 	
    	String productCode1 = "MYTESTPROD"; 
    	String instrumentCode1 = "MYTESTISTR"; 
    	String marketOperatorCode1 = "MYTESTMKTOPT"; 
    	
    	createUnderlying(adminSystem, datastores, productCode1, instrumentCode1, marketOperatorCode1, user);
    	
    	// test create market 
    	String marketCode = Market.createMarketCode(marketOperatorCode1, instrumentCode1);
		
    	MarketDto market1 = createMarket1Dto(marketCode, instrumentCode1,
				marketOperatorCode1);
		
		MarketManagementContextImpl context = createMarketManagementContext();

		context = createMarketManagementContext();
		
    	market1.setBusinessEntityCode("IMARCATS");
    	
    	String businessEntityCode = "TEST_BE_2";
    	market1.setBusinessEntityCode(businessEntityCode);
    	
		context = createMarketManagementContext();
		
		market1 = createMarket1Dto(marketCode, instrumentCode1,
				marketOperatorCode1);
		market1.setBusinessEntityCode(businessEntityCode);

		// make sure that audit entries are far apart enough 
		Thread.sleep(1000); 
		
		// successful create 
		testCreateMarket(market1, adminSystem, user, context);
    	
    	checkAuditTrail(AuditEntryAction.Created, user, market1.getMarketCode(), Market.class.getSimpleName(), adminSystem);
    	
    	// check Market 1 
    	Market market1Loaded = datastores.findMarketBy(marketCode).getMarketModel();

    	assertEquals(marketCode, market1Loaded.getCode());
       	assertEquals(ActivationStatus.Created, market1Loaded.getActivationStatus());
    	assertEquals(user, market1Loaded.getCreationAudit().getUserID());       	
	}

	private void testCreateMarket(MarketDto market,
			MarketManagementAdminSystem adminSystem,
			String user, MarketManagementContextImpl context) {
		openTransaction();
    	adminSystem.createMarket(market, user, context); 
    	commitTransaction();
	}

	public void testChange() throws Exception {
		MockDatastoresBase datastores = createDatastores();
    	
		MarketManagementAdminSystem adminSystem = 
    		new MarketManagementAdminSystem(datastores, datastores, datastores, datastores, datastores, datastores, datastores, null);

		String user = "test";
		
    	String productCode1 = "MYTESTPROD"; 
    	String instrumentCode1 = "MYTESTISTR"; 
    	String marketOperatorCode1 = "MYTESTMKTOPT"; 
    	
    	createUnderlying(adminSystem, datastores, productCode1, instrumentCode1, marketOperatorCode1, user);
    	
    	Date oldLastUpdateTimestamp = new Date();
		
    	Thread.sleep(1000);
		
    	// test change market + set market calendar
       	String marketCode = Market.createMarketCode(marketOperatorCode1, instrumentCode1);
       	
    	MarketDto market1Changed = createMarket1DtoForDirectDatastoreCreate(marketCode, instrumentCode1, marketOperatorCode1);
		market1Changed.setDescription("New Descrition");
		market1Changed.setBusinessEntityCode("BOGUS_CODE");
		
		createMarketInDatastore(marketFromDto(market1Changed), datastores); 

		MarketManagementContextImpl context = createMarketManagementContext();

		context = createMarketManagementContext();
		
		market1Changed = createMarket1Dto(marketCode, instrumentCode1,
				marketOperatorCode1);
		market1Changed.setBusinessEntityCode(TEST_BUSINESS_ENITY_0);
		
		setMarketToCreated(marketCode, datastores);
		
		context = createMarketManagementContext();
		
    	try { 
			market1Changed.setLastUpdateTimestamp(oldLastUpdateTimestamp);
			adminSystem.changeMarket(market1Changed, user, context);
    		fail();
    	} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.MARKET_CANNOT_BE_OVERWRITTEN_WITH_AN_OLDER_VERSION, e.getLanguageKey());
		}
    	
		context = createMarketManagementContext();
		
		
		setMarketToApproved(marketCode, datastores);
		
		context = createMarketManagementContext();

		// recreate market to get a later version
		market1Changed = createMarket1Dto(marketCode, instrumentCode1,
				marketOperatorCode1);
		market1Changed.setDescription(market1Changed.getDescription());
		market1Changed.setBusinessEntityCode("BOGUS");

		// approved market 
    	try {
			adminSystem.changeMarket(market1Changed, user, context);
    		fail();
    	} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.MARKET_ONLY_IN_CREATED_OR_SUSPEDED_STATE_CAN_BE_CHANGED, e.getLanguageKey());
		}
    	
       	// approved market - change market calendar
    	try {
			adminSystem.setMarketCalendar(marketCode, new BusinessCalendarDto(), user, context);
    		fail();
    	} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.MARKET_ONLY_IN_CREATED_OR_SUSPEDED_STATE_CAN_BE_CHANGED, e.getLanguageKey());
		}

		
		
		setMarketToActivated(marketCode, datastores);
		
		context = createMarketManagementContext();
		
		// recreate market to get a later version
		market1Changed = createMarket1Dto(marketCode, instrumentCode1,
				marketOperatorCode1);
		market1Changed.setDescription(market1Changed.getDescription());
		market1Changed.setBusinessEntityCode("BOGUS");
		
		
		// activated market 
    	try {
			adminSystem.changeMarket(market1Changed, user, context);
    		fail();
    	} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.ACTIVE_MARKET_CANNOT_BE_CHANGED, e.getLanguageKey());
		}
    	
       	// activated market - change market calendar
    	try {
			adminSystem.setMarketCalendar(marketCode, new BusinessCalendarDto(), user, context);
    		fail();
    	} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.ACTIVE_MARKET_CANNOT_BE_CHANGED, e.getLanguageKey());
		}

		
		
		setMarketToDeactivated(marketCode, datastores);
		
    	context = createMarketManagementContext();
    	
		// recreate market to get a later version
		market1Changed = createMarket1Dto(marketCode, instrumentCode1,
				marketOperatorCode1);
		market1Changed.setDescription(market1Changed.getDescription());
		market1Changed.setBusinessEntityCode("BOGUS");
		
		// deactivated market 
    	try {
			adminSystem.changeMarket(market1Changed, user, context);
    		fail();
    	} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.MARKET_ONLY_IN_CREATED_OR_SUSPEDED_STATE_CAN_BE_CHANGED, e.getLanguageKey());
		}
    	
       	// deactivated market - change market calendar
    	try {
			adminSystem.setMarketCalendar(marketCode, new BusinessCalendarDto(), user, context);
    		fail();
    	} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.MARKET_ONLY_IN_CREATED_OR_SUSPEDED_STATE_CAN_BE_CHANGED, e.getLanguageKey());
		}
    	
		
		setMarketToCreated(marketCode, datastores);
		
    	context = createMarketManagementContext();

		// recreate market to get an earlier version
    	MarketInternal marketLoaded = datastores.findMarketBy(marketCode);
		market1Changed = createMarket1Dto(marketCode, instrumentCode1, marketOperatorCode1);
		market1Changed.setDescription(market1Changed.getDescription());
		market1Changed.setBusinessEntityCode("BOGUS");
		market1Changed.setVersionNumber(marketLoaded.getMarketModel().getVersionNumber());

    	
		market1Changed.setBusinessEntityCode(TEST_BUSINESS_ENITY_0);

		// successful change 
		testChangeMarket(market1Changed, context, user, adminSystem);
    	
    	checkAuditTrail(AuditEntryAction.Changed, user, marketCode, Market.class.getSimpleName(), adminSystem);
    	
    	// check change of market 
    	Market market1Loaded = datastores.findMarketBy(marketCode).getMarketModel();

    	assertEquals(marketCode, market1Loaded.getCode());
    	assertEquals(market1Changed.getDescription(), market1Loaded.getDescription());
    	assertEquals(ActivationStatus.Created, market1Loaded.getActivationStatus());
    	assertEquals(user, market1Loaded.getChangeAudit().getUserID());
  
		context = createMarketManagementContext();
		
    	// successful change - market calendar - default calendar
    	testSetMarketCalendar(marketCode, null, context,
				user, adminSystem);
    	
    	
    	
    	
    	checkAuditTrail(AuditEntryAction.Changed, user, marketCode, Market.class.getSimpleName(), adminSystem);
    	
    	// check change of market calendar
    	market1Loaded =  datastores.findMarketBy(marketCode).getMarketModel();

    	assertTrue(market1Loaded.getBusinessCalendar().getBusinessCalendarDays().isEmpty());
    	
    	// successful change - market calendar
    	BusinessCalendarDto businessCalendarDto = createBusinessCalendarDto();
    	testSetMarketCalendar(marketCode, businessCalendarDto, context,
				user, adminSystem);
    	
    	
    	
    	
    	checkAuditTrail(AuditEntryAction.Changed, user, marketCode, Market.class.getSimpleName(), adminSystem);
    	
    	// check change of market calendar
    	market1Loaded =  datastores.findMarketBy(marketCode).getMarketModel();

    	assertEqualsBusinessCalendar(businessCalendarFromDto(businessCalendarDto), market1Loaded.getBusinessCalendar());
    	
    	datastores.findMarketBy(marketCode).getMarketModel().setActivationStatus(ActivationStatus.Suspended);
    	setMarketToSuspended(marketCode, datastores);
		
    	// successful change 
    	market1Changed = createMarket1Dto(marketCode, instrumentCode1, marketOperatorCode1);
		market1Changed.setDescription("New Descrition");
		market1Changed.setBusinessEntityCode(TEST_BUSINESS_ENITY_0);
		market1Changed.setVersionNumber(market1Loaded.getVersionNumber());
		
		testChangeMarket(market1Changed, context, user, adminSystem);
    	
    	
    	
    	// successful change - market calendar
    	testSetMarketCalendar(marketCode, null, context, user, adminSystem);
    	
    	
	}

	private void testSetMarketCalendar(String marketCode,
			BusinessCalendarDto businessCalendarDto, MarketManagementContextImpl context,
			String user,
			MarketManagementAdminSystem adminSystem) {
		openTransaction();
    	adminSystem.setMarketCalendar(marketCode, businessCalendarDto,  user, context);
    	commitTransaction();
	}

	private void testChangeMarket(MarketDto marketChanged,
			MarketManagementContextImpl context, String user,
			MarketManagementAdminSystem adminSystem) {
		openTransaction();
    	adminSystem.changeMarket(marketChanged, user, context);
    	commitTransaction();
	}

	public void testSetMarketOperationContract() throws Exception {
		MockDatastoresBase datastores = createDatastores();

		MarketManagementAdminSystem adminSystem = 
    		new MarketManagementAdminSystem(datastores, datastores, datastores, datastores, datastores, datastores, datastores, null);

    	
    	String productCode1 = "MYTESTPROD"; 
    	String instrumentCode1 = "MYTESTISTR"; 
    	String marketOperatorCode1 = "MYTESTMKTOPT"; 
    	
    	String user = "test";
		
    	createUnderlying(adminSystem, datastores, productCode1, instrumentCode1, marketOperatorCode1, user);
    	
    	// test market operation contract 
      	String marketCode = Market.createMarketCode(marketOperatorCode1, instrumentCode1);
      	
    	Market market = createMarket1(marketCode, instrumentCode1, marketOperatorCode1);
		market.setDescription("New Descrition");
		market.setBusinessEntityCode("BogusCode"); 
		market.setActivationStatus(ActivationStatus.Created);
		market.updateLastUpdateTimestampAndVersion();
		market.setQuoteType(QuoteType.Price);
		market.setState(MarketState.Closed);
		
		createMarketInDatastore(market, datastores); 
		
		MarketManagementContextImpl context = createMarketManagementContext();
		String marketOperationContract = "My Test Market Opt Contract";
    	context = createMarketManagementContext();
    	
    	// non existent market 
    	try {
			adminSystem.setMarketOperationContract("BOGUS", marketOperationContract, user, context);
			fail();
    	} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.NON_EXISTENT_MARKET, e.getLanguageKey());
		}
    	
    	setMarketToApproved(marketCode, datastores);
    	
    	context = createMarketManagementContext();
    	
    	// approved market 
    	try {
			adminSystem.setMarketOperationContract(marketCode, marketOperationContract, user, context);
			fail();
    	} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.MARKET_ONLY_IN_CREATED_OR_SUSPEDED_STATE_CAN_BE_CHANGED, e.getLanguageKey());
		}
    	
    	setMarketToActivated(marketCode, datastores);
    	
    	context = createMarketManagementContext();
    	
    	// activated market 
    	try {
			adminSystem.setMarketOperationContract(marketCode, marketOperationContract, user, context);
			fail();
    	} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.MARKET_ONLY_IN_CREATED_OR_SUSPEDED_STATE_CAN_BE_CHANGED, e.getLanguageKey());
		}
    	
    	setMarketToDeactivated(marketCode, datastores);
    	
    	context = createMarketManagementContext();
    	
    	// deactivated market 
    	try {
			adminSystem.setMarketOperationContract(marketCode, marketOperationContract, user, context);
			fail();
    	} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.MARKET_ONLY_IN_CREATED_OR_SUSPEDED_STATE_CAN_BE_CHANGED, e.getLanguageKey());
		}
    	
    	setMarketToSuspended(marketCode, datastores);
    	
    	context = createMarketManagementContext();
    	
    	// success 
    	testSetMarketOperationContract(marketCode, marketOperationContract,
				adminSystem, context, user);
    	
    	context = createMarketManagementContext();
    	
    	checkAuditTrail(AuditEntryAction.Changed, user, marketCode, Market.class.getSimpleName(), adminSystem);
    	
    	// check market operator contract 
    	Market marketLoaded = datastores.findMarketBy(marketCode).getMarketModel();

    	assertEquals(marketCode, marketLoaded.getCode());
    	assertEquals(marketOperationContract, marketLoaded.getMarketOperationContract());

    	setMarketToCreated(marketCode, datastores);
    	
    	context = createMarketManagementContext();
    	
    	testSetMarketOperationContract(marketCode, marketOperationContract,
				adminSystem, context, user);
	}

	private void testSetMarketOperationContract(String marketCode,
			String marketOperationContract,
			MarketManagementAdminSystem adminSystem,
			MarketManagementContextImpl context, String user) {
		openTransaction();
    	adminSystem.setMarketOperationContract(marketCode, marketOperationContract, user, context);
    	commitTransaction();
	}
	
	@SuppressWarnings("deprecation")
	public void testApproveSuspend() throws Exception {
		MockDatastoresBase datastores = createDatastores();

		MarketManagementAdminSystem adminSystem = 
    		new MarketManagementAdminSystem(datastores, datastores, datastores, datastores, datastores, datastores, datastores, null);
    	
    	String productCode1 = "MYTESTPROD"; 
    	String instrumentCode1 = "MYTESTISTR"; 
    	String marketOperatorCode1 = "MYTESTMKTOPT"; 
    	String businessEntityCode = TEST_BUSINESS_ENITY_0; 
    	
    	String user = "test";
    	
    	createUnderlying(adminSystem, datastores, productCode1, instrumentCode1, marketOperatorCode1, user);
    	
    	// test approve 
      	String marketCode = Market.createMarketCode(marketOperatorCode1, instrumentCode1);
      	Market market = createMarket1ForDirectDatastoreCreate(marketCode, instrumentCode1, marketOperatorCode1);
      	market.setActivationStatus(ActivationStatus.Created);
      	market.setBusinessEntityCode(TEST_BUSINESS_ENITY_0);
      	
      	createMarketInDatastore(market, datastores);
      	
		MarketManagementContextImpl context = createMarketManagementContext();

		context = createMarketManagementContext();
    	
		setInstrumentToApproved(market.getInstrumentCode(), datastores); 
		
		setMarketOperatorToApproved(market.getMarketOperatorCode(), datastores);
		
		// make sure no contract is set 
		openTransaction();
		datastores.findMarketBy(marketCode).getMarketModel().setMarketOperationContract(null);
		commitTransaction();
		
    	// no market operator contract 
		try {
			adminSystem.approveMarket(marketCode, new Date(), user, context);
			fail();
    	} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.MARKET_MUST_HAVE_MARKET_OPERATOR_CONTRACT, e.getLanguageKey());
		}

    	context = createMarketManagementContext();

    	openTransaction();
		datastores.findMarketBy(marketCode).getMarketModel().setMarketOperationContract("TestContract");
    	datastores.findInstrumentByCode(instrumentCode1).setActivationStatus(ActivationStatus.Created);
    	datastores.findProductByCode(productCode1).setActivationStatus(ActivationStatus.Created);
       	datastores.findMarketOperatorByCode(marketOperatorCode1).setActivationStatus(ActivationStatus.Created);
    	commitTransaction();

    	context = createMarketManagementContext();
    	
    	// underlying instrument is not approved 
		try {
			adminSystem.approveMarket(marketCode, new Date(), user, context);
			fail();
    	} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.MARKET_CAN_ONLY_BE_APPROVED_IF_UNDERLYING_INSTRUMENT_IS_APPROVED, e.getLanguageKey());
		}
    	
    	// approve instrument
    	approveInstrument(user, adminSystem,
				productCode1, instrumentCode1, datastores);  	
  
    	context = createMarketManagementContext();

    	// underlying market operator is not approved 
		try {
			adminSystem.approveMarket(marketCode, new Date(), user, context);
			fail();
    	} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.MARKET_CAN_ONLY_BE_APPROVED_IF_UNDERLYING_MARKET_OPERATOR_IS_APPROVED, e.getLanguageKey());
		}    	
    	
    	// approve market operator 
		setMarketOperatorToApproved(marketOperatorCode1, datastores);
    	    	
    	context = createMarketManagementContext();
    	
       	// non-existent market 
		try {
			adminSystem.approveMarket("BOGUS", new Date(), user, context);
			fail();
    	} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.NON_EXISTENT_MARKET, e.getLanguageKey());
		}
   
    	context = createMarketManagementContext(); 
    	
    	// stale object 
		try {
			Date lastUpdateTimestamp = new Date();
			lastUpdateTimestamp.setMinutes(lastUpdateTimestamp.getMinutes() - 10);
			adminSystem.approveMarket(marketCode, lastUpdateTimestamp, user, context);
			fail();
    	} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.STALE_OBJECT_CANNOT_BE_APPROVED, e.getLanguageKey());
		}
    	
    	context = createMarketManagementContext(); 
		
    	// make sure that audit entries are far apart enough 
		Thread.sleep(1000); 
		
    	// success
    	testApproveMarket(marketCode, adminSystem, context, user);
		
    	context = createMarketManagementContext();
		
    	checkAuditTrail(AuditEntryAction.Approved, user, marketCode, Market.class.getSimpleName(), adminSystem);
    	
    	// check market approval
    	Market marketLoaded = datastores.findMarketBy(marketCode).getMarketModel();
		
    	assertEquals(marketCode, marketLoaded.getCode());
    	assertEquals(ActivationStatus.Approved, marketLoaded.getActivationStatus());
    	assertEquals(user, marketLoaded.getApprovalAudit().getUserID());
    	
    	context = createMarketManagementContext();   	
    	
    	// try approving again 
		try {
			adminSystem.approveMarket(marketCode, new Date(), user, context);
			fail();
    	} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.MARKET_ONLY_IN_CREATED_OR_SUSPEDED_STATE_CAN_BE_APPROVED, e.getLanguageKey());
		}
    	
		setMarketToActivated(marketCode, datastores);
    	
    	context = createMarketManagementContext();   	
    	
    	// try approving again - in activated 
		try {
			adminSystem.approveMarket(marketCode, new Date(), user, context);
			fail();
    	} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.MARKET_ONLY_IN_CREATED_OR_SUSPEDED_STATE_CAN_BE_APPROVED, e.getLanguageKey());
		}
    	
		setMarketToDeactivated(marketCode, datastores);
    	
    	context = createMarketManagementContext();   	
    	
    	// try approving again - in deactivated 
		try {
			adminSystem.approveMarket(marketCode, new Date(), user, context);
			fail();
    	} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.MARKET_ONLY_IN_CREATED_OR_SUSPEDED_STATE_CAN_BE_APPROVED, e.getLanguageKey());
		}
    	
    	setMarketToSuspended(marketCode, datastores);
    	
    	context = createMarketManagementContext();   
    	
    	testApproveMarket(marketCode, adminSystem, context, user);
    	
    	context = createMarketManagementContext();
    	
    	// test suspend 
    	
    	setMarketToCreated(marketCode, datastores);
    	
    	context = createMarketManagementContext();
    	
    	// in created state
    	try {
			adminSystem.suspendMarket(marketCode, user, context);
			fail();
    	} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.MARKET_ONLY_IN_APPROVED_OR_DEACTIVATED_STATE_CAN_BE_SUSPENDED, e.getLanguageKey());
		}
    	
    	setMarketToActivated(marketCode, datastores);
    	
    	context = createMarketManagementContext();
    	
    	// in activated state
    	try {
			adminSystem.suspendMarket(marketCode, user, context);
			fail();
    	} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.ACTIVE_MARKET_CANNOT_BE_SUSPENDED, e.getLanguageKey());
		}
    	
    	setMarketToApproved(marketCode, datastores);
    	
    	context = createMarketManagementContext();

		// make sure that audit entries are far apart enough 
		Thread.sleep(1000); 
		
    	// suspend market 
    	testSuspendMarket(marketCode, adminSystem, context, user);
		
    	checkAuditTrail(AuditEntryAction.Suspended, user, marketCode, Market.class.getSimpleName(), adminSystem);
    	
		// check market after suspend 
    	marketLoaded = datastores.findMarketBy(marketCode).getMarketModel();

    	assertEquals(marketCode, marketLoaded.getCode());
    	assertEquals(ActivationStatus.Suspended, marketLoaded.getActivationStatus());
    	assertEquals(user, marketLoaded.getSuspensionAudit().getUserID());

    	context = createMarketManagementContext();

    	setMarketToDeactivated(marketCode, datastores);
    	
    	context = createMarketManagementContext();
    	
    	testSuspendMarket(marketCode, adminSystem, context, user);
	}

	private void testSuspendMarket(String marketCode,
			MarketManagementAdminSystem adminSystem,
			MarketManagementContextImpl context,
			String user) {
		openTransaction();
		adminSystem.suspendMarket(marketCode, user, context);
		commitTransaction();
	}

	private void testApproveMarket(String marketCode,
			MarketManagementAdminSystem adminSystem,
			MarketManagementContextImpl context,
			String user) {
		openTransaction();
		adminSystem.approveMarket(marketCode, new Date(), user, context);
		commitTransaction();
	}

	public void testDelete() throws Exception {
		MockDatastoresBase datastores = createDatastores();
    	
		MarketManagementAdminSystem adminSystem = 
    		new MarketManagementAdminSystem(datastores, datastores, datastores, datastores, datastores, datastores, datastores, null);
	    	
    	String productCode1 = "MYTESTPROD"; 
    	String instrumentCode1 = "MYTESTISTR"; 
    	String marketOperatorCode1 = "MYTESTMKTOPT"; 
    	
    	String user = "test";
    	
    	createUnderlying(adminSystem, datastores, productCode1, instrumentCode1, marketOperatorCode1, user);
    	
    	// test delete market 
      	String marketCode = Market.createMarketCode(marketOperatorCode1, instrumentCode1);
      	Market market = createMarket1ForDirectDatastoreCreate(marketCode, instrumentCode1, marketOperatorCode1);
      	market.setBusinessEntityCode(TEST_BUSINESS_ENITY_0);

      	createMarketInDatastore(market, datastores);

		MarketManagementContextImpl context = createMarketManagementContext();
		context = createMarketManagementContext();
    			
		// delete approved market 
		setMarketToApproved(marketCode, datastores);
		
		context = createMarketManagementContext();
    	try {
			adminSystem.deleteMarket(marketCode, user, context);
    		fail();
    	} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.MARKET_ONLY_IN_CREATED_OR_SUSPEDED_STATE_CAN_BE_DELETED, e.getLanguageKey());
		}
		context = createMarketManagementContext();
		
		setMarketToActivated(marketCode, datastores);
		
		context = createMarketManagementContext();
    	try {
			adminSystem.deleteMarket(marketCode, user, context);
    		fail();
    	} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.ACTIVE_MARKET_CANNOT_BE_DELETED, e.getLanguageKey());
		}
		context = createMarketManagementContext();

		setMarketToDeactivated(marketCode, datastores);
		
		context = createMarketManagementContext();
    	try {
			adminSystem.deleteMarket(marketCode, user, context);
    		fail();
    	} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.MARKET_ONLY_IN_CREATED_OR_SUSPEDED_STATE_CAN_BE_DELETED, e.getLanguageKey());
		}
		context = createMarketManagementContext();

		setMarketToCreated(marketCode, datastores);
		
    	context = createMarketManagementContext();

    	// success 
    	testDeleteMarket(marketCode, adminSystem, user, context);
		
		PagedAuditTrailEntryListDto trailEntries = adminSystem.getAllAuditTrailEntriesFromCursor(null, 2);
    	assertTrue(trailEntries.getAuditTrailEntries()[0] != null);
    	
		checkAuditTrailForDeleteObject(user, marketCode, Market.class.getSimpleName(), adminSystem);

		// check market after delete
    	MarketInternal marketInternalLoaded = datastores.findMarketBy(marketCode);

    	assertEquals(null, marketInternalLoaded);
		
	}

	private void testDeleteMarket(String marketCode,
			MarketManagementAdminSystem adminSystem,
			String user, MarketManagementContextImpl context) {
		openTransaction();
		adminSystem.deleteMarket(marketCode, user, context);
		commitTransaction();
	}
	
	private PermissionRequestDto createAdminPermission(
			String user_) {
		PermissionRequestDto permissionRequest = new PermissionRequestDto(); 
		permissionRequest.setObjectClass(Market.class.getSimpleName());
		permissionRequest.setPermissionClass(com.imarcats.interfaces.client.v100.dto.types.PermissionClass.Administrator);
		permissionRequest.setPermissionType(com.imarcats.interfaces.client.v100.dto.types.PermissionType.ObjectAdministration);
		permissionRequest.setUserOrGroupID(user_);
		permissionRequest.setUserType(com.imarcats.interfaces.client.v100.dto.types.UserTypeOfPermission.User);
		return permissionRequest;
	}
	
	public void testRolloverMarket() throws Exception {
	  	MockDatastoresBase datastores = createDatastores();

    	MarketManagementAdminSystem adminSystem = 
    		new MarketManagementAdminSystem(datastores, datastores, datastores, datastores, datastores, datastores, datastores, null);		
		
    	String productCode1 = "MYTESTPROD"; 
    	String instrumentCode1 = "MYTESTISTR"; 
    	String marketOperatorCode1 = "MYTESTMKTOPT"; 
    	
    	String user = "test";
    	
    	createUnderlying(adminSystem, datastores, productCode1, instrumentCode1, marketOperatorCode1, user);
    	
    	MarketManagementContextImpl context = createMarketManagementContext();
    	
    	// create market  
    	String marketCode = Market.createMarketCode(marketOperatorCode1, instrumentCode1);
		
	   	String businessEntityCode = "BE_2"; 
		
    	Market market = createMarket1ForDirectDatastoreCreate(marketCode, instrumentCode1,
				marketOperatorCode1);
    	market.updateLastUpdateTimestampAndVersion();
    	market.setActivationStatus(ActivationStatus.Created); 
    	
    	market.setBusinessEntityCode(businessEntityCode);
		
    	context = createMarketManagementContext();
    	
    	createMarketInDatastore(market, datastores);
    	
    	context = createMarketManagementContext();
    	
      	String productCode2 = "MYTESTPROD1"; 
    	String instrumentCode2 = "MYTESTISTR1"; 
    	String marketOperatorCode2 = "MYTESTMKTOPT1"; 
    	
    	createUnderlying(adminSystem, datastores, productCode2, instrumentCode2, marketOperatorCode2, user);
    	
    	context = createMarketManagementContext();
    	
		// test rollover 
    
    	context = createMarketManagementContext();
    	
    	// non-approved object 
		try {
			adminSystem.rolloverMarket(marketCode, instrumentCode2, user, context);
			fail();
    	} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.NON_APPROVED_OBJECT_CANNOT_BE_REFERRED_IN_ROLLOVER, e.getLanguageKey());
		}
    	
    	context = createMarketManagementContext();
    	
    	// approve instrument 1
    	setInstrumentToApproved(instrumentCode1, datastores);
    	
    	context = createMarketManagementContext();
    	
    	// still not approved 
		try {
			adminSystem.rolloverMarket(marketCode, instrumentCode2, user, context);
			fail();
    	} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.NON_APPROVED_OBJECT_CANNOT_BE_REFERRED_IN_ROLLOVER, e.getLanguageKey());
		}
    	
    	context = createMarketManagementContext();

    	// approve instrument 2
    	setInstrumentToApproved(instrumentCode2, datastores);
    	
    	context = createMarketManagementContext();
    	
       	// still not approved 
		try {
			adminSystem.rolloverMarket(marketCode, instrumentCode2, user, context);
			fail();
    	} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.NON_APPROVED_OBJECT_CANNOT_BE_REFERRED_IN_ROLLOVER, e.getLanguageKey());
		}
    	
    	context = createMarketManagementContext();
    	
    	setMarketOperatorToApproved(marketOperatorCode1, datastores);
    	
    	context = createMarketManagementContext();
    	
       	// still not approved 
		try {
			adminSystem.rolloverMarket(marketCode, instrumentCode2, user, context);
			fail();
    	} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.NON_APPROVED_OBJECT_CANNOT_BE_REFERRED_IN_ROLLOVER, e.getLanguageKey());
		}
    	
    	context = createMarketManagementContext();
    	
    	// approve market 
    	setMarketToApproved(marketCode, datastores);
    	
    	context = createMarketManagementContext();
    	
    	// rolling over to different instrument 
		try {
			adminSystem.rolloverMarket(marketCode, instrumentCode2, user, context);
			fail();
    	} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.UNDERLYING_OBJECT_PROVIDED_FOR_ROLLOVER_IS_NOT_ROLLED_FROM_UNDERLYING_OF_SOURCE_OBJECT, e.getLanguageKey());
		}
    	
    	
    	// same market code 
		try {
			adminSystem.rolloverMarket(marketCode, instrumentCode1, user, context);
			fail();
    	} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.ROLLED_OBJECTS_HAVE_SAME_CODE, e.getLanguageKey());
		}
    	
    	
    	
    	String instrumentCode1Rolled = instrumentCode1 + "ROLLED"; 
		InstrumentDto rolledOverInstrument = createInstrument1Dto(instrumentCode1Rolled, UnderlyingType.Product, productCode1);
    	rolledOverInstrument.setMasterAgreementDocument("Test");
    	rolledOverInstrument.setAssetClassName("TEST");
    	
    	
    	
    	setProductToApproved(productCode1, datastores);
    	
    	context = createMarketManagementContext();
    	
    	// rollover instrument
    	testRolloverInstrument(instrumentCode1, rolledOverInstrument, adminSystem, user);
    	
    	
		
		// rollover market by admin
    	String rolloverMarketCode = testRolloverMarket(marketCode,
				instrumentCode1Rolled, adminSystem, user, context);
		
		// check rolled market 
    	
		MarketInternal marketLoaded = datastores.findMarketBy(rolloverMarketCode);

		assertEquals(datastores.findInstrumentByCode(instrumentCode1Rolled).getQuoteType(), marketLoaded.getQuoteType());
		assertEquals(ActivationStatus.Approved, marketLoaded.getMarketModel().getActivationStatus());
		assertEquals(MarketState.Closed, marketLoaded.getMarketModel().getState());
		assertEquals(user, marketLoaded.getMarketModel().getCreationAudit().getUserID());
		assertEquals(user, marketLoaded.getMarketModel().getRolloverAudit().getUserID());
		
	 	// rollover instrument 
    	String instrumentCode2Rolled = instrumentCode1 + "ROLLED2"; 
		InstrumentDto rolledOverInstrument2 = createInstrument1Dto(instrumentCode2Rolled, UnderlyingType.Product, productCode1);
    	rolledOverInstrument2.setMasterAgreementDocument("Test");
    	rolledOverInstrument2.setAssetClassName("TEST");
		
    	
    	context = createMarketManagementContext();
    	
    	testRolloverInstrument(instrumentCode1, rolledOverInstrument2, adminSystem, user);
   
	}

	private String testRolloverMarket(String marketCode,
			String instrumentCodeRolled,
			MarketManagementAdminSystem adminSystem, String user,
			MarketManagementContextImpl context) {
		openTransaction();
		String rolloverMarketCode = adminSystem.rolloverMarket(marketCode, instrumentCodeRolled, user, context);
		commitTransaction();
		return rolloverMarketCode;
	}

	private void testRolloverInstrument(String instrumentCode1,
			InstrumentDto rolledOverInstrument, MarketManagementAdminSystem adminSystem,
			String user) {
		openTransaction();
    	adminSystem.rolloverInstrument(instrumentCode1, rolledOverInstrument, user);
    	commitTransaction();
	}
	
	private void createMarketsForActivate(MarketManagementAdminSystem adminSystem, String instrumentCode1, String marketOperatorCode1, String marketOperatorCode2, 
			String user, TradingSession tradingSession1, TradingSession tradingSession2, MockDatastoresBase datastores) throws Exception {
    	
    	
    	String productCode1 = "MYTESTPROD"; 
    	
    	createUnderlying(adminSystem, datastores, productCode1, instrumentCode1, marketOperatorCode1, user);
    	
    	// test create market operator 
    	openTransaction();
    	if(datastores.findMarketOperatorByCode(marketOperatorCode1) == null) {
    		MarketOperatorDto mktOpt1 = createMarketOperator1(marketOperatorCode1);
    		
    		adminSystem.createMarketOperator(mktOpt1, user);    		
    	}
    	commitTransaction();
    	
	   	String businessEntityCode = "BE_2"; 
		
    	// test create market operator 2
    	openTransaction();
    	if(datastores.findMarketOperatorByCode(marketOperatorCode2) == null) {
			MarketOperatorDto mktOpt2 = createMarketOperator1(marketOperatorCode2);
			mktOpt2.setBusinessEntityCode(TEST_BUSINESS_ENITY_0);
			mktOpt2.setActivationStatus(com.imarcats.interfaces.client.v100.dto.types.ActivationStatus.Created); 
			mktOpt2.setLastUpdateTimestamp(new Date()); 
			datastores.createMarketOperator(marketOperatorFromDto(mktOpt2));
    	}
    	commitTransaction();
    	
    	MarketManagementContextImpl context = createMarketManagementContext();
    	
    	// test create market  
    	String marketCode = Market.createMarketCode(marketOperatorCode1, instrumentCode1);
		
    	Market marketModel = createMarket1(marketCode, instrumentCode1,
				marketOperatorCode1);
    	marketModel.setTradingSession(tradingSession1);
    	marketModel.setTradingHours(createTimePeriod());
    	marketModel.setMarketOperationDays(RecurringActionDetail.OnWeekdays);
    	marketModel.setBusinessEntityCode(businessEntityCode);
    	MarketDto market = MarketDtoMapping.INSTANCE.toDto(marketModel); 
    	
    	testCreateMarket(market, adminSystem, user, context);
    	
    	context = createMarketManagementContext();
    	
    	// test create market  
    	String marketCode2 = Market.createMarketCode(marketOperatorCode2, instrumentCode1);
		
    	Market market2Model = createMarket1(marketCode2, instrumentCode1,
				marketOperatorCode2);
    	market2Model.setTradingSession(tradingSession2);
    	market2Model.setTradingHours(createTimePeriod());
    	market2Model.setBusinessEntityCode(businessEntityCode);
    	MarketDto market2 = MarketDtoMapping.INSTANCE.toDto(market2Model); 
    	
    	testCreateMarket(market2, adminSystem, user, context);
    	
    	context = createMarketManagementContext(); 
    	
	}
	
	private void approveMarketsForActivate(MarketManagementAdminSystem adminSystem, String instrumentCode1, String marketOperatorCode1, String marketOperatorCode2, String user, MockDatastoresBase datastores) {
    	
    	
    	openTransaction();
    	
		// approve instrument 1
    	datastores.findInstrumentByCode(instrumentCode1).setActivationStatus(ActivationStatus.Approved);

    	// approve market operator 
    	datastores.findMarketOperatorByCode(marketOperatorCode1).setActivationStatus(ActivationStatus.Approved);

    	// approve market operator 2  
    	datastores.findMarketOperatorByCode(marketOperatorCode2).setActivationStatus(ActivationStatus.Approved);
    	
    	commitTransaction();
    	
    	// approve market 
    	MarketManagementContextImpl context = createMarketManagementContext();
    	
    	String marketCode = Market.createMarketCode(marketOperatorCode1, instrumentCode1);
		
    	openTransaction();    	
		adminSystem.setMarketOperationContract(marketCode, "Test Contract", user, context);
		commitTransaction();
    	context = createMarketManagementContext(); 

    	testApproveMarket(marketCode, adminSystem, context, user);
		
    	context = createMarketManagementContext(); 

    	// approve market 
    	context = createMarketManagementContext();
    	String marketCode2 = Market.createMarketCode(marketOperatorCode2, instrumentCode1);
		
    	openTransaction(); 
    	adminSystem.setMarketOperationContract(marketCode2, "Test Contract", user, context);
    	commitTransaction();
		
    	context = createMarketManagementContext(); 
    	
    	testApproveMarket(marketCode2, adminSystem, context,
				user);
		
    	context = createMarketManagementContext(); 
	}

	public void testSetupChangedMarket() throws Exception {
		Instrument instrument = new Instrument();
		Market originalMarket = new Market();
		originalMarket.setMarketCode("Test");
		originalMarket.setQuoteType(instrument.getQuoteType());
		// setup system fields

		originalMarket.setActivationStatus(com.imarcats.model.types.ActivationStatus.Activated);

		Market newMarket = new Market();
		newMarket.setMarketCode(originalMarket.getMarketCode());
		
		MarketPropertyChangeExecutor marketPropertyChangeExecutor = new MarketPropertyChangeExecutor(newMarket);
		MarketAdministrationSubSystem.setupChangedMarket(marketPropertyChangeExecutor, newMarket, originalMarket, createMarketManagementContext().getPropertyChangeSession(), instrument);
		
		assertEqualsMarket(originalMarket, newMarket);
	}
	
//	public void testCopyForChange() throws Exception {
//		String marketCode = "test";
//		
//		Market originalMarket = createMarket(marketCode);
//		
//		// reset system fields - as they will not be copied
//		originalMarket.setBuyBook(null);
//		originalMarket.setSellBook(null);
//		
//		originalMarket.setClosingQuote(null);
//		originalMarket.setPreviousClosingQuote(null);
//		originalMarket.setHaltLevel(-1);
//		
//		originalMarket.setLastTrade(null);
//		originalMarket.setPreviousLastTrade(null);
//
//		originalMarket.setPreviousBestBid(null);
//
//		originalMarket.setPreviousBestAsk(null);
//
//		originalMarket.setCurrentBestBid(null);
//
//		originalMarket.setCurrentBestAsk(null);
//		
//		originalMarket.setNextMarketCallDate(null);
//		
//		originalMarket.setOpeningQuote(null);
//		originalMarket.setPreviousOpeningQuote(null);
//		
//		originalMarket.setState(null);
//		originalMarket.setActivationStatus(null);
//		
//		originalMarket.setCreationAudit(null);
//		originalMarket.setChangeAudit(null);
//		originalMarket.setApprovalAudit(null);
//		originalMarket.setSuspensionAudit(null);
//		originalMarket.setRolloverAudit(null);
//		originalMarket.setActivationAudit(null);
//		originalMarket.setDeactivationAudit(null);
//		
//		originalMarket.setMarketCallActionKey(null);
//		originalMarket.setMarketOpenActionKey(null);
//		originalMarket.setMarketCloseActionKey(null);
//		originalMarket.setMarketReOpenActionKey(null);
//		originalMarket.setMarketMaintenanceActionKey(null);
//		originalMarket.setCallMarketMaintenanceActionKey(null);
//		
//		originalMarket.setInstrumentCode(null);
//		originalMarket.setMarketOperatorCode(null);
//		originalMarket.setMarketOperationContract(null);
//		originalMarket.setQuoteType(null);
//		
//		Market newMarket = new Market();
//		newMarket.setMarketCode(marketCode);
//		
//		Instrument instrument = new Instrument();
//		instrument.setQuoteType(QuoteType.Yield);
//		
//		MarketPropertyChangeExecutor originalMarketChangeExecutor = new MarketPropertyChangeExecutor(newMarket);
//		
//		IPropertyChangeSession propertyChangeSession = createMarketManagementContext(null).getPropertyChangeSession();
//		
//		MarketManagementClientSystem.copyMarketDataForChange(originalMarketChangeExecutor, originalMarket, propertyChangeSession, instrument);
//
//		assertEquals(instrument.getQuoteType(), newMarket.getQuoteType());
//		
//		// bit cheating here
//		originalMarket.setQuoteType(QuoteType.Yield);
//		
//		assertEqualsMarket(originalMarket, newMarket);
//	}
	
	public void testTimerUtilsAddToDate() throws Exception {
		TimeOfDay originalTime = createTimePeriod().getStartTime();
		long hour = 60 * 60 * 1000;
		TimeOfDay newTime = TimerUtils.addToDate(originalTime, hour);
		
		originalTime.setHour(originalTime.getHour() + 1);
		
		assertEqualsTimeOfDay(originalTime, newTime);
	}
	
	public void testActivateCallMarket() throws Exception {
		
	  	MockDatastoresBase datastores = createDatastores();

	  	MockMarketTimer timer = createTimer();
    	MarketManagementSubSystem managementSystem = 
    		new MarketManagementSubSystem(datastores, datastores, datastores, datastores, datastores, datastores, timer);
    	MarketManagementAdminSystem adminSystem = 
    		new MarketManagementAdminSystem(datastores, datastores, datastores, datastores, datastores, datastores, datastores, timer);	
		

    	String marketOperatorCode1 = "MYTESTMKTOPT";
    	String marketOperatorCode2 = "MYTESTMKTOPT2";  
    	String instrumentCode1 = "MYTESTISTR"; 
    	
    	String marketCode = Market.createMarketCode(marketOperatorCode1, instrumentCode1);
    	String marketCode2 = Market.createMarketCode(marketOperatorCode2, instrumentCode1);

    	String user = "test";
    	
    	createMarketsForActivate(adminSystem, instrumentCode1, marketOperatorCode1, marketOperatorCode2, 
    			user, TradingSession.NonContinuous, TradingSession.Continuous, datastores);

    	// set market to call market
    	openTransaction();
    	Market marketLoaded = datastores.findMarketBy(marketCode).getMarketModel();
    	marketLoaded.setExecutionSystem(ExecutionSystem.CallMarketWithSingleSideAuction);
    	commitTransaction();
    	
    	//openTransaction();
    	MarketManagementContextImpl context = createMarketManagementContext(); 
    	
    	// test activate market - by client 
   
    	// not approved market 
		try {
			managementSystem.activateMarket(marketCode, user, context);
			fail();
    	} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.NON_APPROVED_MARKET_CANNOT_BE_ACTIVATED, e.getLanguageKey());
		}
		
    	context = createMarketManagementContext();
 
    	approveMarketsForActivate(adminSystem, instrumentCode1, marketOperatorCode1, marketOperatorCode2, user, datastores);
    	    	
    	// non-call market 
		try {
			managementSystem.activateMarket(marketCode, user, context);
			fail();
    	} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.CALL_MARKET_MUST_BE_ACTIVATED_FOR_MARKET_CALL, e.getLanguageKey());
		}

    	 

    	GregorianCalendar nowCalendar = new GregorianCalendar();
    	// this is set to a fixed date to avoid problems when we switch to day light saving
    	nowCalendar.set(Calendar.DATE, 11);
    	nowCalendar.set(Calendar.MONTH, 1);
    	nowCalendar.set(Calendar.YEAR, 111);
    	
    	TimeOfDay nowTimeOfDay = TimerUtils.getTimeFromDate(nowCalendar.getTime(), nowCalendar.getTimeZone());
		
    	Thread.sleep(1000);
    	
    	// non-future call date
    	try {
			Date nextCallDate = new Date();
			// subtract 1 second - to make sure scheduled time is just earlier
			nowTimeOfDay.setSecond(nowTimeOfDay.getSecond() - 1);
			testActivateMarketCall(marketCode, nextCallDate, nowTimeOfDay,
					managementSystem, context, user);
			fail();
    	} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.CALL_DATE_MUST_BE_IN_FUTURE, e.getLanguageKey());
		} finally {
			commitTransaction();
		}

    	
    	
    	nowCalendar = new GregorianCalendar();
    	nowTimeOfDay = TimerUtils.getTimeFromDate(nowCalendar.getTime(), nowCalendar.getTimeZone());
    	// market call too far out 
    	try {
    		// 15 days + 24 hours
    		// TODO: Find out, why adding 1 hour is not enough
			managementSystem.activateCallMarket(marketCode, TimerUtils.addToDate(nowCalendar.getTime(), MarketManagementBase.MAX_TIME_TILL_MARKET_CALL_IN_MILLIS + 24 * 60 * 60 * 1000), MarketDtoMapping.INSTANCE.toDto(nowTimeOfDay), user, context);
			fail();
    	} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.CALL_DATE_MUST_NOT_FURTHER_15_DAYS, e.getLanguageKey());
		}

    	
    	
    	// test activate market 
    	nowCalendar = new GregorianCalendar();
    	// this is to avoid problems when we switch to day light saving
    	if(nowCalendar.get(Calendar.MONTH) == 2 && nowCalendar.get(Calendar.DAY_OF_MONTH) < 27) {
	    	nowCalendar.set(Calendar.DATE, 27);
	    	nowCalendar.set(Calendar.MONTH, 2);
    	} else if(nowCalendar.get(Calendar.MONTH) == 9 && nowCalendar.get(Calendar.DAY_OF_MONTH) < 21)
    	{
	    	nowCalendar.set(Calendar.DATE, 21);
	    	nowCalendar.set(Calendar.MONTH, 9);
    	}
    	
    	nowTimeOfDay = TimerUtils.getTimeFromDate(nowCalendar.getTime(), nowCalendar.getTimeZone());
    	// Date scheduledDateTime = TimerUtils.addToDate(nowCalendar.getTime(), MarketManagementBase.MAX_TIME_TILL_MARKET_CALL_IN_MILLIS - DAY_IN_MILLIS);
    	Date scheduledDateTime = TimerUtils.addToDate(nowCalendar.getTime(), DAY_IN_MILLIS);
    	// successful activation 
    	
		// make sure that audit entries are far apart enough 
		Thread.sleep(1000); 
    	
    	testActivateMarketCall(marketCode, scheduledDateTime, nowTimeOfDay,
				managementSystem, context, user);

    	context = createMarketManagementContext(); 
    	
		// make sure that audit entries are far apart enough 
		Thread.sleep(1000); 
    	
    	checkAuditTrail(AuditEntryAction.Activated, user, marketCode, Market.class.getSimpleName(), adminSystem);

    	
    	
    	// check activated market 
    	marketLoaded = datastores.findMarketBy(marketCode).getMarketModel();

    	assertEquals(MarketState.Open, marketLoaded.getState());
    	assertEquals(ActivationStatus.Activated, marketLoaded.getActivationStatus());
    	assertEquals(user, marketLoaded.getActivationAudit().getUserID());
    	assertTrue(marketLoaded.getNextMarketCallDate() != null);
        
    	assertEquals(null, marketLoaded.getMarketOpenActionKey());
    	assertEquals(null, marketLoaded.getMarketCloseActionKey());
    	assertEquals(null, marketLoaded.getMarketReOpenActionKey());
  
    	assertTrue(timer.getAction(marketLoaded.getMarketMaintenanceActionKey()) instanceof MarketMaintenanceAction);
    	assertTrue(timer.getAction(marketLoaded.getCallMarketMaintenanceActionKey()) instanceof CallMarketMaintenanceAction);
    	assertTrue(timer.getAction(marketLoaded.getMarketCallActionKey()) instanceof CallMarketAction);

    	checkMarketCallDate(timer, marketLoaded);
    	checkScheduledTime(marketLoaded, scheduledDateTime);
    	
    	
    	
    	nowCalendar = new GregorianCalendar();
    	nowTimeOfDay = TimerUtils.getTimeFromDate(nowCalendar.getTime(), nowCalendar.getTimeZone());
		
    	// already active 
    	try {
			managementSystem.activateCallMarket(marketCode, nowCalendar.getTime(), MarketDtoMapping.INSTANCE.toDto(nowTimeOfDay), user, context);
			fail();
    	} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.ACTIVE_MARKET_CANNOT_BE_ACTIVATED, e.getLanguageKey());
		}
    	
    	
    	// test deactivate market 
//    	permissionController.revokeObjectPermission(_manipulatorUserSession.getUserName(), UserTypeOfPermission.User,
//				PermissionClass.Client, PermissionType.ObjectManipulation, new DatastoreKey(marketCode), Market.class.getSimpleName(), userSession.getUserName(), userSession);

		PermissionRequestDto permissionRequest = new PermissionRequestDto(); 
		permissionRequest.setObjectClass(Market.class.getSimpleName());
		permissionRequest.setObjectID(marketCode);
		permissionRequest.setPermissionClass(com.imarcats.interfaces.client.v100.dto.types.PermissionClass.Client);
		permissionRequest.setPermissionType(com.imarcats.interfaces.client.v100.dto.types.PermissionType.Operations);
		permissionRequest.setUserOrGroupID(user);
		permissionRequest.setUserType(com.imarcats.interfaces.client.v100.dto.types.UserTypeOfPermission.User);
		
    	// test deactivate 
    	testEmergencyCloseMarket(marketCode, managementSystem, context, user);
		
    	// check deactivated market 
    	marketLoaded = datastores.findMarketBy(marketCode).getMarketModel();

    	assertEquals(MarketState.Closed, marketLoaded.getState());
    	assertEquals(ActivationStatus.Deactivated, marketLoaded.getActivationStatus());
    	assertEquals(user, marketLoaded.getDeactivationAudit().getUserID());

    	assertEquals(null, marketLoaded.getMarketCallActionKey());
    	assertEquals(null, marketLoaded.getMarketMaintenanceActionKey());
    	assertEquals(null, marketLoaded.getCallMarketMaintenanceActionKey());
    	assertEquals(null, marketLoaded.getMarketOpenActionKey());
    	assertEquals(null, marketLoaded.getMarketCloseActionKey());
    	assertEquals(null, marketLoaded.getMarketReOpenActionKey());
    	
    	
    	
    	// test activate market 2
    	nowCalendar = new GregorianCalendar();
    	nowTimeOfDay = TimerUtils.getTimeFromDate(nowCalendar.getTime(), nowCalendar.getTimeZone());
		
    	// call non-call market 
    	try {
			managementSystem.activateCallMarket(marketCode2, nowCalendar.getTime(), MarketDtoMapping.INSTANCE.toDto(nowTimeOfDay), user, context);
			fail();
    	} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.NON_CALL_MARKET_CANNOT_BE_CALLED, e.getLanguageKey());
		}
    	
    	nowCalendar = new GregorianCalendar();
    	nowTimeOfDay = TimerUtils.getTimeFromDate(nowCalendar.getTime(), nowCalendar.getTimeZone());
		
    	nowCalendar = new GregorianCalendar();
    	nowTimeOfDay = TimerUtils.getTimeFromDate(nowCalendar.getTime(), nowCalendar.getTimeZone());

    	// schedule 1 second to the future
    	nowCalendar = new GregorianCalendar();
    	nowCalendar.add(Calendar.SECOND, 1);
    	nowTimeOfDay = TimerUtils.getTimeFromDate(nowCalendar.getTime(), nowCalendar.getTimeZone());
    	
    	// successful activation 
    	testActivateMarketCall(marketCode, nowCalendar.getTime(), nowTimeOfDay, adminSystem, context, user); 
    	
		testEmergencyCloseMarket(marketCode, adminSystem, context, user);
	}

	private void testEmergencyCloseMarket(String marketCode,
			MarketManagementSubSystem managementSystem,
			MarketManagementContextImpl context, String user) {
		openTransaction();
		managementSystem.emergencyCloseMarket(marketCode, user, context);
		commitTransaction();
	}

	private void testEmergencyCloseMarket(String marketCode,
			MarketManagementAdminSystem adminSystem,
			MarketManagementContextImpl context, String user) {
		openTransaction();
		adminSystem.emergencyCloseMarket(marketCode, user, context);
		commitTransaction();
	}
	
	private void testActivateMarketCall(
			String marketCode, Date scheduledDateTime,
			TimeOfDay scheduledTimeOfDay, MarketManagementSubSystem managementSystem,
			MarketManagementContextImpl context, String user) {
		openTransaction();
		managementSystem.activateCallMarket(marketCode, scheduledDateTime, MarketDtoMapping.INSTANCE.toDto(scheduledTimeOfDay), user, context);
		commitTransaction();
	}

	private void testActivateMarketCall(
			String marketCode, Date scheduledDateTime,
			TimeOfDay scheduledTimeOfDay, MarketManagementAdminSystem adminSystem,
			MarketManagementContextImpl context, String user) {
		openTransaction();
		adminSystem.activateCallMarket(marketCode, scheduledDateTime, MarketDtoMapping.INSTANCE.toDto(scheduledTimeOfDay), user, context);
		commitTransaction();
	}
	
	@SuppressWarnings("deprecation")
	private void checkScheduledTime(Market marketLoaded_,
			Date scheduledDateTime_) {
		assertEquals(scheduledDateTime_.getYear(), marketLoaded_.getNextMarketCallDate().getYear());
    	assertEquals(scheduledDateTime_.getMonth(), marketLoaded_.getNextMarketCallDate().getMonth());
    	assertEquals(scheduledDateTime_.getDate(), marketLoaded_.getNextMarketCallDate().getDate());
    	assertEquals(scheduledDateTime_.getHours(), marketLoaded_.getNextMarketCallDate().getHours());
    	assertEquals(scheduledDateTime_.getMinutes(), marketLoaded_.getNextMarketCallDate().getMinutes());
    	assertEquals(scheduledDateTime_.getSeconds(), marketLoaded_.getNextMarketCallDate().getSeconds());
	}

	protected void checkMarketCallDate(MockMarketTimer timer, Market marketLoaded) {
		assertEquals(marketLoaded.getNextMarketCallDate(), new Timestamp(timer.getEvent(marketLoaded.getMarketCallActionKey()).getScheduledTime().getTime()));
    	assertEquals(TimerUtils.addToDate(marketLoaded.getNextMarketCallDate(), MarketManagementBase.MAINTENANCE_DELAY_AFTER_MARKET_CALL_IN_MILLIS), timer.getEvent(marketLoaded.getCallMarketMaintenanceActionKey()).getScheduledTime());
    	checkScheduleToTime(marketLoaded, timer.getEvent(marketLoaded.getMarketMaintenanceActionKey()), marketLoaded.getTradingDayEnd());
	}
	
	protected MockMarketTimer createTimer() {
		return new MockMarketTimer();
	}
	
	@SuppressWarnings("deprecation")
	public void testActivateMarket() throws Exception {
	
	  	MockDatastoresBase datastores = createDatastores();
    	
    	MockMarketTimer timer = createTimer();
    	MarketManagementSubSystem managementSystem = 
    		new MarketManagementSubSystem(datastores, datastores, datastores, datastores, datastores, datastores, timer);
    	MarketManagementAdminSystem adminSystem = 
    		new MarketManagementAdminSystem(datastores, datastores, datastores, datastores, datastores, datastores, datastores, timer);

    	String marketOperatorCode1 = "MYTESTMKTOPT";
    	String marketOperatorCode2 = "MYTESTMKTOPT2";  
    	String instrumentCode1 = "MYTESTMKTOPT"; 
    	
    	String marketCode = Market.createMarketCode(marketOperatorCode1, instrumentCode1);
    	String marketCode2 = Market.createMarketCode(marketOperatorCode2, instrumentCode1);

    	String user = "test";
    	
    	// market 1 NonContinuous; market 2 Continuous
    	createMarketsForActivate(adminSystem, instrumentCode1, marketOperatorCode1, marketOperatorCode2, 
    			user, TradingSession.NonContinuous, TradingSession.Continuous, datastores);
 
    	//openTransaction();
    	MarketManagementContextImpl context = createMarketManagementContext(); 
    	
    	// test activate market - by client 
		context = createMarketManagementContext();
    	
    	// non-approved market 
		try {
			managementSystem.activateMarket(marketCode, user, context);
			fail();
    	} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.NON_APPROVED_MARKET_CANNOT_BE_ACTIVATED, e.getLanguageKey());
		}
		
		context = createMarketManagementContext();
    	
		approveMarketsForActivate(adminSystem, instrumentCode1, marketOperatorCode1, marketOperatorCode2, user, datastores);
    	
    	testActivateMarket(marketCode, managementSystem, context, user);
    	
    	// check activated market 
    	Market marketLoaded = datastores.findMarketBy(marketCode).getMarketModel();

    	assertEquals(ActivationStatus.Activated, marketLoaded.getActivationStatus());
    	assertEquals(user, marketLoaded.getActivationAudit().getUserID());

    	assertEquals(null, marketLoaded.getMarketCallActionKey());
    	assertTrue(timer.getAction(marketLoaded.getMarketMaintenanceActionKey()) instanceof MarketMaintenanceAction);
    	assertEquals(null, marketLoaded.getCallMarketMaintenanceActionKey());
    	assertTrue(timer.getAction(marketLoaded.getMarketOpenActionKey()) instanceof OpenMarketAction);
    	assertTrue(timer.getAction(marketLoaded.getMarketCloseActionKey()) instanceof CloseMarketAction);
    	assertEquals(null, marketLoaded.getMarketReOpenActionKey());
    	
    	checkScheduleToTime(marketLoaded, timer.getEvent(marketLoaded.getMarketMaintenanceActionKey()), marketLoaded.getTradingDayEnd());
    	checkScheduleToTime(marketLoaded, timer.getEvent(marketLoaded.getMarketOpenActionKey()), marketLoaded.getTradingHours().getStartTime());
    	checkScheduleToTime(marketLoaded, timer.getEvent(marketLoaded.getMarketCloseActionKey()), marketLoaded.getTradingHours().getEndTime());
    	
    	
    	
    	// activate again 
		try {
			managementSystem.activateMarket(marketCode, user, context);
			fail();
    	} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.ACTIVE_MARKET_CANNOT_BE_ACTIVATED, e.getLanguageKey());
		}
    	
    	
    	// test deactivate market 
    	openTransaction();
       	marketLoaded = datastores.findMarketBy(marketCode).getMarketModel();
       	marketLoaded.setState(MarketState.Open);
    	commitTransaction();

    	// open market 
    	try {
			managementSystem.deactivateMarket(marketCode, user, context);
			fail();
    	} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.NON_CLOSED_MARKET_CANNOT_BE_DEACTIVATED, e.getLanguageKey());
		}
    	
    	 
    	
    	testEmergencyCloseMarket(marketCode, managementSystem, context, user);
    	
    	

    	// check closed and deactivated market 
    	marketLoaded = datastores.findMarketBy(marketCode).getMarketModel();

    	assertEquals(MarketState.Closed, marketLoaded.getState());
    	assertEquals(ActivationStatus.Deactivated, marketLoaded.getActivationStatus());
    	assertEquals(user, marketLoaded.getDeactivationAudit().getUserID());

    	assertEquals(null, marketLoaded.getMarketCallActionKey());
    	assertEquals(null, marketLoaded.getMarketMaintenanceActionKey());
    	assertEquals(null, marketLoaded.getCallMarketMaintenanceActionKey());
    	assertEquals(null, marketLoaded.getMarketOpenActionKey());
    	assertEquals(null, marketLoaded.getMarketCloseActionKey());
    	assertEquals(null, marketLoaded.getMarketReOpenActionKey());
    	
    	
    	
    	// test activation time modification, because it activation date falls on non-business date 
    	
    	// non business day activation
    	Date scheduledTime = new Date();
    	scheduledTime.setDate(13);
    	scheduledTime.setMonth(2);
    	scheduledTime.setYear(111);

    	// activate 
    	testActivateMarket(marketCode, scheduledTime, timer, datastores,
				user, context);
    	
    	marketLoaded = datastores.findMarketBy(marketCode).getMarketModel();
    	MockEvent event = timer.getEvent(marketLoaded.getMarketOpenActionKey());
    	
    	// check, if moved to next day
    	assertEquals(14, event.getScheduledTime().getDate());
    	assertEquals(2, event.getScheduledTime().getMonth());
    	assertEquals(111, event.getScheduledTime().getYear());
    	
    	
    	
    	testDeactivateMarket(marketCode, managementSystem, context, user);
    	
    	
    	
    	// activation time moved to next day, because market cannot be opened this day, too late 
    	scheduledTime.setDate(14);
    	scheduledTime.setHours(11);
    	
    	
    	testActivateMarket(marketCode, scheduledTime, timer, datastores,
				user, context);
    	
    	

    	marketLoaded = datastores.findMarketBy(marketCode).getMarketModel();
    	event = timer.getEvent(marketLoaded.getMarketOpenActionKey());

    	// check, if moved to next day
    	assertEquals(15, event.getScheduledTime().getDate());
    	assertEquals(2, event.getScheduledTime().getMonth());
    	assertEquals(111, event.getScheduledTime().getYear());
    	assertEquals(11, event.getScheduledTime().getHours());

    	
    	testDeactivateMarket(marketCode, managementSystem, context, user);
    	
    	
    	
    	// test activate market 2 - continuous 
    	testActivateMarket(marketCode2, managementSystem, context, user);
    	
    	
    	
    	// check activated market 
    	marketLoaded = datastores.findMarketBy(marketCode2).getMarketModel();

    	assertEquals(ActivationStatus.Activated, marketLoaded.getActivationStatus());
    	assertEquals(MarketState.Open, marketLoaded.getState());
    	assertEquals(user, marketLoaded.getActivationAudit().getUserID());

    	assertEquals(null, marketLoaded.getMarketCallActionKey());
    	assertEquals(null, marketLoaded.getMarketOpenActionKey());
    	assertEquals(null, marketLoaded.getMarketCloseActionKey());
    	assertTrue(timer.getAction(marketLoaded.getMarketMaintenanceActionKey()) instanceof MarketMaintenanceAction);
    	assertEquals(null, marketLoaded.getCallMarketMaintenanceActionKey());
    	assertEquals(null, marketLoaded.getMarketReOpenActionKey());
    	
    	checkScheduleToTime(marketLoaded, timer.getEvent(marketLoaded.getMarketMaintenanceActionKey()),marketLoaded.getTradingDayEnd());

    	context = createMarketManagementContext();
    	
    	// successful activate 
    	testActivateMarket(marketCode, adminSystem, context, user);
    	
    	context = createMarketManagementContext();
		
    	checkAuditTrail(AuditEntryAction.Activated, user, marketCode, Market.class.getSimpleName(), adminSystem);
    	context = createMarketManagementContext();
    	
    	// check activated market 
    	marketLoaded = datastores.findMarketBy(marketCode).getMarketModel();

    	assertEquals(ActivationStatus.Activated, marketLoaded.getActivationStatus());
    	
    	// test close - by admin
    	openTransaction();
    	datastores.findMarketBy(marketCode).getMarketModel().setState(MarketState.Open);
    	commitTransaction();

    	context = createMarketManagementContext();
    	
    	// successful close and deactivate
    	testEmergencyCloseMarket(marketCode, adminSystem, context, user); 
    	context = createMarketManagementContext();

//    	PagedAuditTrailEntryListDto trailEntries = adminSystem.getAllAuditTrailEntriesFromCursor(null, 2);
//    	checkAuditTrail(AuditEntryAction.Deactivated, approverUserSession.getUserName(), marketCode, Market.class.getSimpleName(),  auditEntryFromDto(trailEntries.getAuditTrailEntries()[0]));
//    	checkAuditTrail(AuditEntryAction.Closed, approverUserSession.getUserName(), marketCode, Market.class.getSimpleName(),  auditEntryFromDto(trailEntries.getAuditTrailEntries()[1]));
    	checkAuditTrailForDeactivateMarket(user,
    			marketCode, adminSystem);
    			
    	context = createMarketManagementContext();
    	
    	// check closed market and deactivated market 
    	marketLoaded = datastores.findMarketBy(marketCode).getMarketModel();

    	assertEquals(MarketState.Closed, marketLoaded.getState());
    	assertEquals(ActivationStatus.Deactivated, marketLoaded.getActivationStatus());
    	
    	context = createMarketManagementContext();

    	openTransaction();
    	adminSystem.activateMarket(marketCode, user, context);
    	commitTransaction();
    	
    	context = createMarketManagementContext();
		
    	// successful deactivate 
    	testDeactivateMarket(marketCode, adminSystem, context, user);
    	context = createMarketManagementContext();
    	
    	checkAuditTrail(AuditEntryAction.Deactivated, user, marketCode, Market.class.getSimpleName(), adminSystem);
    	context = createMarketManagementContext();
    	
    	// check deactivated market 
    	marketLoaded = datastores.findMarketBy(marketCode).getMarketModel();

    	assertEquals(ActivationStatus.Deactivated, marketLoaded.getActivationStatus());
    	
       	// suspend deactivated market
    	context = createMarketManagementContext();
    	
    	testSuspendMarket(marketCode, adminSystem, context, user);
       	
    	// check suspended market 
    	marketLoaded = datastores.findMarketBy(marketCode).getMarketModel();

    	assertEquals(ActivationStatus.Suspended, marketLoaded.getActivationStatus());
	}

	protected void checkAuditTrailForDeactivateMarket(String user,
			String marketCode, MarketManagementAdminSystem adminSystem) {
		PagedAuditTrailEntryListDto trailEntries = adminSystem
				.getAllAuditTrailEntriesFromCursor(null, 2);
		assertTrue(trailEntries.getAuditTrailEntries()[0] != null);
		assertTrue(trailEntries.getAuditTrailEntries()[1] != null);

		// these events are too close to each other datastore might get the
		// order wrong
		if (trailEntries.getAuditTrailEntries()[0].getAuditEntryAction() == com.imarcats.interfaces.client.v100.dto.types.AuditEntryAction.Deactivated
				&& trailEntries.getAuditTrailEntries()[1].getAuditEntryAction() == com.imarcats.interfaces.client.v100.dto.types.AuditEntryAction.Closed) {

	    	checkAuditTrail(AuditEntryAction.Deactivated, user, marketCode, Market.class.getSimpleName(),  auditEntryFromDto(trailEntries.getAuditTrailEntries()[0]));
	    	checkAuditTrail(AuditEntryAction.Closed, user, marketCode, Market.class.getSimpleName(),  auditEntryFromDto(trailEntries.getAuditTrailEntries()[1]));

		} else if (trailEntries.getAuditTrailEntries()[0].getAuditEntryAction() == com.imarcats.interfaces.client.v100.dto.types.AuditEntryAction.Closed
				&& trailEntries.getAuditTrailEntries()[1].getAuditEntryAction() == com.imarcats.interfaces.client.v100.dto.types.AuditEntryAction.Deactivated) {

	    	checkAuditTrail(AuditEntryAction.Closed, user, marketCode, Market.class.getSimpleName(),  auditEntryFromDto(trailEntries.getAuditTrailEntries()[0]));
	    	checkAuditTrail(AuditEntryAction.Deactivated, user, marketCode, Market.class.getSimpleName(),  auditEntryFromDto(trailEntries.getAuditTrailEntries()[1]));

		} else {
			fail();
		}
	}
	
	private void testActivateMarket(String marketCode,
			MarketManagementSubSystem managementSystem,
			MarketManagementContextImpl context, String user) {
		openTransaction();
    	managementSystem.activateMarket(marketCode, user, context);
    	commitTransaction();
	}

	private void testActivateMarket(String marketCode,
			MarketManagementAdminSystem adminSystem,
			MarketManagementContextImpl context, String user) {
		openTransaction();
		adminSystem.activateMarket(marketCode, user, context);
    	commitTransaction();
	}
	
	private void testDeactivateMarket(String marketCode,
			MarketManagementSubSystem managementSystem,
			MarketManagementContextImpl context, String user) {
		openTransaction();
    	managementSystem.deactivateMarket(marketCode, user, context);
    	commitTransaction();
	}

	private void testDeactivateMarket(String marketCode,
			MarketManagementAdminSystem adminSystem,
			MarketManagementContextImpl context, String user) {
		openTransaction();
    	adminSystem.deactivateMarket(marketCode, user, context);
    	commitTransaction();
	}

	
	private void testActivateMarket(String marketCode,
			Date scheduledTime, MockMarketTimer timer,
			MockDatastoresBase datastores, String user,
			MarketManagementContextImpl context) {
		openTransaction();
		Market marketLoaded = datastores.findMarketBy(marketCode).getMarketModel();
    	MarketManagementBase.activateMarketInternal(marketLoaded, user, context, scheduledTime, timer, datastores);
    	commitTransaction();
	}
	
	public void checkScheduleToTime(Market market, MockEvent event_, TimeOfDay timeOfDay_) {
		if(market.getTradingSession() != TradingSession.Continuous && 
		   market.getExecutionSystem() != ExecutionSystem.CallMarketWithSingleSideAuction) {
			// for non-continuous markets, market calendar and recurring action is used
			assertEqualsBusinessCalendar(market.getBusinessCalendar(), event_.getCalendar());
			assertEquals(market.getMarketOperationDays(), event_.getRecurringActionDetail());
		} else {
			// for continuous markets, market calendar is not used and recurring action is set to daily
			assertEqualsBusinessCalendar(null, event_.getCalendar());
			assertEquals(RecurringActionDetail.Daily, event_.getRecurringActionDetail());
		}
		assertEqualsTimeOfDay(timeOfDay_, event_.getTime());
	}
	
	private void approveInstrument(
			String user,
			MarketManagementAdminSystem adminSystem,
			String productCode1,
			String instrumentCode1, MockDatastoresBase datastores) {
		
		MarketManagementContextImpl context = createMarketManagementContext();
    	openTransaction();
		adminSystem.setProductDefinitionDocument(productCode1, "Test Doc", user);
		commitTransaction();

		
		context = createMarketManagementContext();
    	openTransaction();
    	adminSystem.approveProduct(productCode1, new Date(), user);
		commitTransaction();
    	
		
		context = createMarketManagementContext();
    	openTransaction();
    	adminSystem.setInstrumentMasterAgreementDocument(instrumentCode1, "Test", user);
		commitTransaction();
		
    	AssetClass assetClass = new AssetClass();
    	String assetClassName = "TEST"; 
		assetClass.setName(assetClassName);
    	assetClass.setDescription("Test");
    	
    	if(datastores.findAssetClassByName(assetClassName) == null) {
    		
    		context = createMarketManagementContext();
        	openTransaction();
        	
        	adminSystem.createAssetClass(assetClassToDto(assetClass), user);
        	
    		commitTransaction();
    	}
    	
		
		context = createMarketManagementContext();
    	openTransaction();
    	
    	adminSystem.setInstrumentAssetClass(instrumentCode1, assetClassName, user);
    	
		commitTransaction();
    	
		
		context = createMarketManagementContext();
    	openTransaction();
    	
    	adminSystem.approveInstrument(instrumentCode1, new Date(), user);    
    	
		commitTransaction();
	}
	

	private MarketDto createMarket1Dto(String marketCode, String instrumentCode1,
			String marketOperatorCode1) throws Exception {
		return MarketDtoMapping.INSTANCE.toDto(createMarket1(marketCode, instrumentCode1, marketOperatorCode1));
	}

	private MarketDto createMarket1DtoForDirectDatastoreCreate(String marketCode, String instrumentCode1,
			String marketOperatorCode1) throws Exception {
		return MarketDtoMapping.INSTANCE.toDto(createMarket1ForDirectDatastoreCreate(marketCode, instrumentCode1, marketOperatorCode1));
	}
	
	private Market createMarket1ForDirectDatastoreCreate(String marketCode, String instrumentCode1,
			String marketOperatorCode1) throws Exception {
		Market market = createMarket1(marketCode, instrumentCode1, marketOperatorCode1);
		market.setLastUpdateTimestamp(new Date());
		market.setActivationStatus(ActivationStatus.Created);
		market.setQuoteType(QuoteType.Price);
		market.setState(MarketState.Closed);
		
		return market;
	}
	
	private Market createMarket1(String marketCode, String instrumentCode1,
			String marketOperatorCode1) throws Exception {
		Market market = new Market();
    	market.setMarketCode(marketCode);
    	market.setInstrumentCode(instrumentCode1);
    	market.setMarketOperatorCode(marketOperatorCode1);
    	market.setName("TestName");
    	market.setDescription("TestDescription");    	
    	// market.setBusinessEntity(createBusinessEntity());
    	market.setBusinessCalendar(createBusinessCalendar());
    	market.setMinimumContractsTraded(10);
    	market.setMinimumQuoteIncrement(10);
    	market.setTradingSession(TradingSession.Continuous);
    	market.setMarketOperationDays(RecurringActionDetail.Daily);
    	market.setTradingDayEnd(createTimePeriod().getEndTime());
    	market.setCommission(10);
    	market.setCommissionCurrency("USD");
    	market.setMarketTimeZoneID("TestTZID");
    	market.setClearingBank("Testbank");
    	market.setState(null);
    	market.setAllowHiddenOrders(false);
    	market.setAllowSizeRestrictionOnOrders(false);
    	// system controlled property 
//    	market.setQuoteType(QuoteType.Price); 
		return market;
	}
	
}
