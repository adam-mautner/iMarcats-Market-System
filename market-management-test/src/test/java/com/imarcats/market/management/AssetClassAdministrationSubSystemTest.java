package com.imarcats.market.management;

import java.util.Date;

import com.imarcats.interfaces.client.v100.dto.AssetClassDto;
import com.imarcats.interfaces.client.v100.dto.types.PermissionRequestDto;
import com.imarcats.interfaces.client.v100.exception.ExceptionLanguageKeys;
import com.imarcats.interfaces.client.v100.exception.MarketRuntimeException;
import com.imarcats.internal.server.infrastructure.testutils.MockDatastoresBase;
import com.imarcats.market.management.admin.MarketManagementAdminSystem;
import com.imarcats.model.AssetClass;
import com.imarcats.model.Instrument;
import com.imarcats.model.types.AuditEntryAction;
import com.imarcats.model.types.Property;

public class AssetClassAdministrationSubSystemTest extends ManagementTestBase {
	
    public void testAssetClassManagement() throws Exception {
    	MockDatastoresBase datastores = createDatastores();
    	MarketManagementAdminSystem adminSystem = 
    		new MarketManagementAdminSystem(datastores, datastores, datastores, datastores, datastores, datastores, datastores, null);
    	 
    	// create asset class 1    	
		AssetClassDto assetClass = new AssetClassDto();
    	String assetClassName = "MY_ASSET_CLASS_1";
		assetClass.setName(assetClassName);
    	assetClass.setDescription("Test 1");
		String user = "test"; 
		testCreateAssetClass(assetClass, adminSystem, user);

		// make sure that audit entries are far apart enough 
		Thread.sleep(1000); 
		
    	// check asset class 1
    	AssetClassDto assetClass1Loaded = adminSystem.getAssetClass(assetClass.getName());
    	
    	assertEqualsAssetClassDto(assetClass, assetClass1Loaded);
    	
    	// create asset class 2
		AssetClass assetClass2Raw = new AssetClass();
		assetClass2Raw.setName("MY_ASSET_CLASS_2");
		assetClass2Raw.setDescription("Test 2");
    	for (Property property : createPropertyList()) {
    		assetClass2Raw.addProperty(property);	
		}
    	AssetClassDto assetClass2 = assetClassToDto(assetClass2Raw);
    			    	
    	testCreateAssetClass(assetClass2, adminSystem, user);
    	
    	// save to the datastore 
    	
    	// check asset class 1
    	AssetClassDto assetClass2Loaded = adminSystem.getAssetClass(assetClass2.getName());
    	
    	assertEqualsAssetClassDto(assetClass2, assetClass2Loaded);

    	checkAuditTrail(AuditEntryAction.Created, user, assetClass2.getName(), AssetClass.class.getSimpleName(), adminSystem);

    	
    	// check all asset classes 
    	assertEquals(2, adminSystem.getAllAssetClassesFromCursor(null, 10).getAssetClasses().length);
    	
    	// change asset class 1
		String originalName = assetClass.getName();
    	AssetClassDto assetClass1Like = new AssetClassDto();
    	assetClass1Like.setName("TEST");
    	assetClass1Like.setDescription(assetClass.getDescription());
    	assetClass1Like.setLastUpdateTimestamp(new Date());
    	assetClass1Like.setVersionNumber(assetClass1Loaded.getVersionNumber());
    	
//		permissionController.revokeObjectPermission(userSession.getUserName(), UserTypeOfPermission.User, PermissionClass.Administrator, 
//				PermissionType.ObjectAdministration, null, AssetClass.class.getSimpleName(), null, ADMIN_ROOT);
    	
		// make sure that audit entries are far apart enough 
		Thread.sleep(1000); 
    	
    	PermissionRequestDto permissionRequest = new PermissionRequestDto(); 
		permissionRequest.setObjectClass(AssetClass.class.getSimpleName());
		permissionRequest.setObjectID(null);
		permissionRequest.setPermissionClass(com.imarcats.interfaces.client.v100.dto.types.PermissionClass.Administrator);
		permissionRequest.setPermissionType(com.imarcats.interfaces.client.v100.dto.types.PermissionType.ObjectAdministration);
		permissionRequest.setUserOrGroupID(user);
		permissionRequest.setUserType(com.imarcats.interfaces.client.v100.dto.types.UserTypeOfPermission.User);
				
		// make sure that audit entries are far apart enough 
		Thread.sleep(1000); 
		
		try {
			adminSystem.changeAssetClass(assetClass1Like, user);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.ASSET_CLASS_NAME_CANNOT_BE_CHANGED, e.getLanguageKey());
		} 
		
		assetClass1Like.setName(originalName);
		assetClass1Like.setDescription("Test 2222");
		testChangeAssetClass(assetClass1Like, adminSystem, user);
    	
    	// check asset class 1
    	assetClass1Loaded = adminSystem.getAssetClass(assetClass.getName());
    	
    	assertEqualsAssetClassDto(assetClass1Like, assetClass1Loaded);
		
    	// make sure that audit entries are far apart enough 
		Thread.sleep(1000); 
		
    	// change asset class 2 
    	AssetClass assetClass2LikeRaw = new AssetClass();
    	assetClass2LikeRaw.setName(assetClass2.getName());
    	assetClass2LikeRaw.setDescription(assetClass2.getDescription());
    	for (Property property : assetClass2Raw.getProperties()) {
    		assetClass2LikeRaw.addProperty(property.cloneProperty());
		}
    	assetClass2LikeRaw.setVersionNumber(assetClass2Loaded.getVersionNumber()); 
    	
    	AssetClassDto assetClass2Like = assetClassToDto(assetClass2LikeRaw);
    			
    	testChangeAssetClass(assetClass2Like, adminSystem, user);
    	
    	// check asset class 2
    	assetClass2Loaded = adminSystem.getAssetClass(assetClass2.getName());
    	
    	assertEqualsAssetClassDto(assetClass2Like, assetClass2Loaded);
    	
    	checkAuditTrail(AuditEntryAction.Changed, user, assetClass2Like.getName(), AssetClass.class.getSimpleName(), adminSystem);
    	
    	// test overwriting with earlier version 
    	AssetClassDto assetClassDtoLike1 = assetClass1Like;
    	Thread.sleep(1000);
    	
    	// cheating here
    	openTransaction();
    	AssetClass assetClassToBeUpdate = datastores.findAssetClassByName(assetClass1Like.getName());
    	assetClassToBeUpdate.updateLastUpdateTimestamp();
    	datastores.updateAssetClass(assetClassToBeUpdate); 
    	commitTransaction();
		
		try {
			adminSystem.changeAssetClass(assetClassDtoLike1, user);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.ASSET_CLASS_CANNOT_BE_OVERWRITTEN_WITH_AN_OLDER_VERSION, e.getLanguageKey());
		} 
    	
    	// test delete asset class 
    	
    	
    	// add asset class dependency
    	String instrumentCode = "MyTestInstrument";
		Instrument instrument = createInstrument(instrumentCode);
    	instrument.setAssetClassName(assetClassName);
    	
    	createInstrument(instrument, datastores);
    	
    	MarketManagementContextImpl context = createMarketManagementContext();
    	    	
    	context = createMarketManagementContext();
    	
    	// delete asset class 1 - it has dependent instrument - will not succeed
    	try {
    		adminSystem.deleteAssetClass(assetClass.getName(), user, context);
    		fail();
    	} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.ASSET_CLASS_CANNOT_BE_DELETED_IF_IT_IS_ASSIGNED_TO_INSTRUMENTS, e.getLanguageKey());
		}

    	// remove dependency 
    	openTransaction();
    	datastores.findInstrumentByCode(instrumentCode).setAssetClassName(null);
    	commitTransaction();
    	
    	context = createMarketManagementContext();
    	
    	// delete asset class 1
    	testDeleteAssetClass(assetClass, adminSystem, user, context);
    	
    	checkAuditTrailForDeleteObject(user, assetClass1Like.getName(), AssetClass.class.getSimpleName(), adminSystem);
    	
    	// check asset class 1
    	try {
    		assetClass1Loaded = adminSystem.getAssetClass(assetClass.getName());
    		fail();
    	} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.NON_EXISTENT_ASSET_CLASS, e.getLanguageKey());
		}
    	
    	// check all asset classes 
    	assertEquals(1, adminSystem.getAllAssetClassesFromCursor(null, 10).getAssetClasses().length);

    	context = createMarketManagementContext(); 
    	
    	
    	testDeleteAssetClass(assetClass2, adminSystem, "test", context);
    	
    	// check asset class 2
	    try{
	    	assetClass2Loaded = adminSystem.getAssetClass(assetClass2.getName());
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.NON_EXISTENT_ASSET_CLASS, e.getLanguageKey());
		}
	    	
    	// check all asset classes 
    	assertEquals(0, adminSystem.getAllAssetClassesFromCursor(null, 10).getAssetClasses().length);

	}

	private void testDeleteAssetClass(AssetClassDto assetClass,
			MarketManagementAdminSystem adminSystem, String user,
			MarketManagementContextImpl context) {
		openTransaction();
    	adminSystem.deleteAssetClass(assetClass.getName(), user, context);
    	commitTransaction();
	}

	private void createInstrument(Instrument instrument,
			MockDatastoresBase datastores) {
		openTransaction();
    	datastores.createInstrument(instrument);
    	commitTransaction();
	}

	private void testChangeAssetClass(AssetClassDto assetClass2Like,
			MarketManagementAdminSystem adminSystem, String user) {
		openTransaction();
		adminSystem.changeAssetClass(assetClass2Like, user);
		commitTransaction();
	}

	private void testCreateAssetClass(AssetClassDto assetClass,
			MarketManagementAdminSystem adminSystem, String user) {
		openTransaction();
    	adminSystem.createAssetClass(assetClass, user);
    	commitTransaction();
	}

	public void testParentDependencyCheck() throws Exception {
	  	MockDatastoresBase datastores = createDatastores();

	  	MarketManagementAdminSystem adminSystem = 
    		new MarketManagementAdminSystem(datastores, datastores, datastores, datastores, datastores, datastores, datastores, null);

		AssetClass assetClass1 = createAssetClass();
    	String assetClassCode1 = assetClass1.getName();
    	assetClass1.setParentName(null);

    	String user = "test"; 
		testCreateAssetClass(assetClassToDto(assetClass1), adminSystem, user); 
		
		AssetClass assetClass2 = createAssetClass();
    	String assetClassCode2 = "TEST_TEST_TEST";
		assetClass2.setName(assetClassCode2);
    	assetClass2.setParentName(null);
    	
    	testCreateAssetClass(assetClassToDto(assetClass2), adminSystem, user); 
    	
    	// test too many Sub-AssetClasses
    	try {
	    	for (int i = 0; i < AssetClass.MAX_NUMBER_OF_SUB_ASSAT_CLASSES_FOR_PARENT + 1; i++) {
	    		AssetClass subAssetClass = createAssetClass();
	    		subAssetClass.setName("TEST_SUB" + i);
	    		subAssetClass.setParentName(assetClassCode2);
	        	
	    		openTransaction();
	    		
	    		adminSystem.createAssetClass(assetClassToDto(subAssetClass), user);
	    		
	    		commitTransaction();
			}
	    	fail();
    	} catch (MarketRuntimeException e) {
    		assertEquals(ExceptionLanguageKeys.TOO_MANY_ASSET_CLASSES_FOR_THIS_PARENT, e.getLanguageKey());
		} finally {
			commitTransaction();
		}
    	
		// test max dependency levels 
    	String parentName = assetClassCode1;
    	int cnt = 20;
    	String firstName = null;
    	String beforeLastName = null;
    	try {
			for (int i = 0; i < cnt; i++) {
				AssetClass assetClass = createAssetClass();
				assetClass.setName("TESTASSCL" + i);
				assetClass.setParentName(parentName);
				
				openTransaction();
				
				adminSystem.createAssetClass(assetClassToDto(assetClass), user);
				
				commitTransaction();
		    	
		    	parentName = assetClass.getName();
		    	
		    	if(firstName == null) {
		    		firstName = assetClass.getName();
		    	}

	    		beforeLastName = assetClass.getParentName();
			}
			fail();
    	} catch (MarketRuntimeException e) {
    		assertEquals(ExceptionLanguageKeys.TOO_DEEP_ASSET_CLASS_TREE, e.getLanguageKey());
		} finally {
			commitTransaction();
		}
    	
    	// test circular dependency
    	
    	// it is weird, that we had to provide null as  here to cut 
    	// the firstInstrument from its datastore version,
    	// if it is not cut from its datastore version (queried and changed in the same ), 
    	// in change a fresh version is loaded from the datastore it changes firstInstrument
    	// back to the same values as datastore has
    	AssetClass firstAssetClass = datastores.findAssetClassByName(firstName);
    	firstAssetClass.setParentName(beforeLastName);
    	
    	try {
	    	adminSystem.changeAssetClass(assetClassToDto(firstAssetClass), user);
	    	fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.CIRCULAR_ASSET_CLASS_TREE, e.getLanguageKey());
		}	
		
    	// test self dependency
		
    	// it is weird, that we had to provide null as  here to cut 
    	// the firstInstrument from its datastore version,
    	// if it is not cut from its datastore version (queried and changed in the same ), 
    	// in change a fresh version is loaded from the datastore it changes firstInstrument
    	// back to the same values as datastore has
    	firstAssetClass = datastores.findAssetClassByName(firstName);
    	firstAssetClass.setParentName(firstAssetClass.getName());
    	
    	try {
	    	adminSystem.changeAssetClass(assetClassToDto(firstAssetClass), user);
	    	fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.CIRCULAR_ASSET_CLASS_TREE, e.getLanguageKey());
		}	
	}
    
}
