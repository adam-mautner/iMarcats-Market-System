package com.imarcats.market.management.rollover;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.imarcats.interfaces.client.v100.exception.ExceptionLanguageKeys;
import com.imarcats.interfaces.client.v100.exception.MarketRuntimeException;
import com.imarcats.model.Rollable;
import com.imarcats.model.test.testutils.MarketObjectTestBase;
import com.imarcats.model.types.ActivationStatus;
import com.imarcats.model.types.AuditInformation;
import com.imarcats.model.types.BooleanProperty;
import com.imarcats.model.types.DoubleProperty;
import com.imarcats.model.types.IntProperty;
import com.imarcats.model.types.Property;
import com.imarcats.model.types.UnderlyingType;
import com.imarcats.model.utils.PropertyUtils;

public class RolloverManagerTest extends MarketObjectTestBase {
	private static final String TEST_CODE = "TestCode";
	private static final String TEST_CODE_ROLLED = "TestCodeRolled";
	private static final String TEST_CODE_UNDERLYING = "TestCodeUnderlying";
	private static final String TEST_CODE_UNDERLYING_ROLLED_FROM = "TestCodeUnderlyingRolledFrom";
	private static final String TEST_USER = "TestUser";

	public void testSimpleRollover() throws Exception {	
		// create rollables 
		MockRollable sourceRollable = createSourceRollable(TEST_CODE);
		MockRollable targetRollable = createTargetRollable(TEST_CODE_ROLLED);
		
		// rollover 
		RolloverManager.rollover(sourceRollable, targetRollable, null, TEST_USER);
		
		// check roll
		checkRollBasics(sourceRollable, targetRollable);
		
	}

	private void checkRollBasics(MockRollable sourceRollable,
			MockRollable targetRollable) {
		assertEquals(sourceRollable.getCode(), targetRollable.getCodeRolledFrom());
		assertEquals(ActivationStatus.Approved, targetRollable.getActivationStatus());
		assertTrue(targetRollable.getActivationDate() != null);
		assertTrue(targetRollable.getCreationAudit() != null);		
		assertEquals(TEST_USER, targetRollable.getCreationAudit().getUserID());
		assertTrue(targetRollable.getCreationAudit().getDateTime() != null);	
		assertTrue(targetRollable.getRolloverAudit() != null);		
		assertEquals(TEST_USER, targetRollable.getRolloverAudit().getUserID());
		assertTrue(targetRollable.getRolloverAudit().getDateTime() != null);		
		assertEquals(TEST_CODE_ROLLED.toUpperCase(), targetRollable.getCode());
	}

	public void testRolloverWithSameUnderlying() throws Exception {
		// create rollables 
		MockRollable sourceRollable = createSourceRollable(TEST_CODE);
		MockRollable targetRollable = createTargetRollable(TEST_CODE_ROLLED);
		MockRollable underlyingRollable = createUnderlyingRollable(TEST_CODE_UNDERLYING);
		
		// set same underlying
		setSameUnderlying(sourceRollable, targetRollable, underlyingRollable);
		
		// rollover 
		RolloverManager.rollover(sourceRollable, targetRollable, underlyingRollable, TEST_USER);
		
		// check roll
		checkRollBasics(sourceRollable, targetRollable);
	}

	private void setSameUnderlying(MockRollable sourceRollable,
			MockRollable targetRollable, MockRollable underlyingRollable) {
		sourceRollable.setUnderlyingCode(underlyingRollable.getCode());
		targetRollable.setUnderlyingCode(underlyingRollable.getCode());
	}
	
	public void testRolloverWithDifferentUnderlying() throws Exception {
		// create rollables 
		MockRollable sourceRollable = createSourceRollable(TEST_CODE);
		MockRollable targetRollable = createTargetRollable(TEST_CODE_ROLLED);
		MockRollable underlyingRollable = createUnderlyingRollable(TEST_CODE_UNDERLYING);
		
		// set different underlying
		setDifferentUnderlying(sourceRollable, targetRollable,
				underlyingRollable);
		
		// rollover 
		RolloverManager.rollover(sourceRollable, targetRollable, underlyingRollable, TEST_USER);
		
		// check roll
		checkRollBasics(sourceRollable, targetRollable);
	}

	private void setDifferentUnderlying(MockRollable sourceRollable,
			MockRollable targetRollable, MockRollable underlyingRollable) {
		underlyingRollable.setCodeRolledFrom(TEST_CODE_UNDERLYING_ROLLED_FROM);
		sourceRollable.setUnderlyingCode(underlyingRollable.getCodeRolledFrom());
		targetRollable.setUnderlyingCode(underlyingRollable.getCode());
	}
	
	public void testRolloverWithProperties() throws Exception {
		// create rollables 
		MockRollable sourceRollable = createSourceRollable(TEST_CODE);
		MockRollable targetRollable = createTargetRollable(TEST_CODE_ROLLED);
		
		// set properties 
		List<String> rollableProperties = createRollableProperties();
		
		Property[] properties = createProperties(rollableProperties);
		
		Property[] propertiesExpected = PropertyUtils.clonePropertyList(properties);
		Property[] rolledPropertyList = PropertyUtils.clonePropertyList(properties);
		((DoubleProperty) rolledPropertyList[0]).setValue(11.0);
		((IntProperty) rolledPropertyList[1]).setValue(234);
		Property[] rolledPropertyListExpected = PropertyUtils.clonePropertyList(rolledPropertyList);
		
		sourceRollable.setProperties(properties);
		sourceRollable.setRollablePropertyNames(rollableProperties);
		targetRollable.setProperties(rolledPropertyList);
		targetRollable.setRollablePropertyNames(rollableProperties);		
		
		// rollover 
		RolloverManager.rollover(sourceRollable, targetRollable, null, TEST_USER);
		
		// check roll
		checkRollBasics(sourceRollable, targetRollable);
		assertEqualsStringList(toArray(sourceRollable.getRollablePropertyNames()), 
				toArray(targetRollable.getRollablePropertyNames()));
		assertEqualsPropertyList(propertiesExpected, sourceRollable.getProperties());
		assertEqualsPropertyList(rolledPropertyListExpected, targetRollable.getProperties());
	}

	private Property[] createProperties(List<String> rollableProperties) {
		String nonRollableName1 = "Non-Rollable1";
		String nonRollableName2 = "Non-Rollable2";
		Property[] properties = {
				getDoubleProperty(rollableProperties.get(0), 10.0),
				getIntProperty(rollableProperties.get(1), 223),
				getStringProperty(nonRollableName1, "Test"),
				getBooleanProperty(nonRollableName2, true)
		};
		return properties;
	}

	private List<String> createRollableProperties() {
		String rollableName1 = "Rollable1";
		String rollableName2 = "Rollable2";
		List<String> rollableProperties = new ArrayList<String>();
		rollableProperties.add(rollableName1);
		rollableProperties.add(rollableName2);
		return rollableProperties;
	}
	
	public void testSimpleRolloverErrors() throws Exception {
		MockRollable sourceRollable = createSourceRollable(TEST_CODE);
		MockRollable targetRollable = createTargetRollable(TEST_CODE_ROLLED);
		MockRollable underlyingRollable = createUnderlyingRollable(TEST_CODE_UNDERLYING);
		
		// set different underlying
		setDifferentUnderlying(sourceRollable, targetRollable,
				underlyingRollable);
		
		// non-rollable object 		
		sourceRollable.setRollable(false);
		targetRollable.setRollable(false);
		try{
			RolloverManager.rollover(sourceRollable, targetRollable, underlyingRollable, TEST_USER);			
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.NON_ROLLABLE_OBJECT_CANNOT_BE_ROLLED_OVER, e.getLanguageKey());
		}
		sourceRollable.setRollable(true);
		try{
			RolloverManager.rollover(sourceRollable, targetRollable, underlyingRollable, TEST_USER);			
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.OBJECT_HAS_TO_REMAIN_ROLLABLE, e.getLanguageKey());
		}
		targetRollable.setRollable(true);
		
		// non-active object 
		sourceRollable.setActivationStatus(ActivationStatus.Suspended);
		underlyingRollable.setActivationStatus(ActivationStatus.Suspended);
		try{
			RolloverManager.rollover(sourceRollable, targetRollable, underlyingRollable, TEST_USER);			
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.NON_APPROVED_OBJECT_CANNOT_BE_REFERRED_IN_ROLLOVER, e.getLanguageKey());
		}
		sourceRollable.setActivationStatus(ActivationStatus.Activated);
		try{
			RolloverManager.rollover(sourceRollable, targetRollable, underlyingRollable, TEST_USER);			
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.NON_APPROVED_OBJECT_CANNOT_BE_REFERRED_IN_ROLLOVER, e.getLanguageKey());
		}
		underlyingRollable.setActivationStatus(ActivationStatus.Activated);
		
		// test same code
		String code = targetRollable.getCode();
		targetRollable.setCode(sourceRollable.getCode());
		try{
			RolloverManager.rollover(sourceRollable, targetRollable, underlyingRollable, TEST_USER);			
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.ROLLED_OBJECTS_HAVE_SAME_CODE, e.getLanguageKey());
		}
		targetRollable.setCode(code);
	}
	
	public void testUndelyingErrors() throws Exception {
		MockRollable sourceRollable = createSourceRollable(TEST_CODE);
		MockRollable targetRollable = createTargetRollable(TEST_CODE_ROLLED);
		MockRollable underlyingRollable = createUnderlyingRollable(TEST_CODE_UNDERLYING);

		// test deleted underlying
		sourceRollable.setUnderlyingCode(underlyingRollable.getCode());
		targetRollable.setUnderlyingCode(null);
		try{
			RolloverManager.rollover(sourceRollable, targetRollable, underlyingRollable, TEST_USER);			
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.UNDERLYING_OBJECT_CANNOT_BE_REMOVED_WHILE_ROLLOVER, e.getLanguageKey());
		}
		
		// test added underlying
		sourceRollable.setUnderlyingCode(null);
		targetRollable.setUnderlyingCode(underlyingRollable.getCode());
		try{
			RolloverManager.rollover(sourceRollable, targetRollable, underlyingRollable, TEST_USER);			
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.UNDERLYING_OBJECT_CANNOT_BE_ADDED_WHILE_ROLLOVER, e.getLanguageKey());
		}		
		
		// set same underlying
		setDifferentUnderlying(sourceRollable, targetRollable, underlyingRollable);
		
		// test underlying not provided
		try{
			RolloverManager.rollover(sourceRollable, targetRollable, null, TEST_USER);			
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.INVALID_UNDELYING_OBJECT_PROVIDED_FOR_ROLLOVER, e.getLanguageKey());
		}	
		
		// test different underlying provided
		MockRollable underlyingRollableInvalid = createUnderlyingRollable("BlaBla");
		try{
			RolloverManager.rollover(sourceRollable, targetRollable, underlyingRollableInvalid, TEST_USER);			
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.INVALID_UNDELYING_OBJECT_PROVIDED_FOR_ROLLOVER, e.getLanguageKey());
		}
		
		// test underlying is not rolled over from the same object
		sourceRollable.setUnderlyingCode("BlaBla");
		try{
			RolloverManager.rollover(sourceRollable, targetRollable, underlyingRollable, TEST_USER);			
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.UNDERLYING_OBJECT_PROVIDED_FOR_ROLLOVER_IS_NOT_ROLLED_FROM_UNDERLYING_OF_SOURCE_OBJECT, e.getLanguageKey());
		}
	}
	
	public void testPropertyErrors() throws Exception {
		// create rollables 
		MockRollable sourceRollable = createSourceRollable(TEST_CODE);
		MockRollable targetRollable = createTargetRollable(TEST_CODE_ROLLED);
		
		// set properties 
		List<String> rollableProperties = createRollableProperties();
		Property[] properties = createProperties(rollableProperties);
		
		sourceRollable.setRollablePropertyNames(rollableProperties);
		targetRollable.setRollablePropertyNames(rollableProperties);
		sourceRollable.setProperties(properties);
		targetRollable.setProperties(properties);
		
		// test 1 rollable property name deleted
		List<String> copiedPropertyNames = shallowCopy(rollableProperties);
		copiedPropertyNames.remove(0);
		sourceRollable.setRollablePropertyNames(copiedPropertyNames);		
		
		try{
			RolloverManager.rollover(sourceRollable, targetRollable, null, TEST_USER);			
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.ROLLOVER_CANNOT_CHANGE_LIST_OF_ROLLABLE_PROPERTIES, e.getLanguageKey());
		}
		
		// test 1 rollable property name changed
		copiedPropertyNames = shallowCopy(rollableProperties);
		copiedPropertyNames.remove(0);
		copiedPropertyNames.add("BlaBla");
		sourceRollable.setRollablePropertyNames(copiedPropertyNames);	
		
		try{
			RolloverManager.rollover(sourceRollable, targetRollable, null, TEST_USER);			
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.ROLLOVER_CANNOT_CHANGE_LIST_OF_ROLLABLE_PROPERTIES, e.getLanguageKey());
		}
		
		// test just change order of the rollable property names
		copiedPropertyNames = shallowCopy(rollableProperties);
		String removed = copiedPropertyNames.remove(0);
		copiedPropertyNames.add(removed);
		sourceRollable.setRollablePropertyNames(copiedPropertyNames);
	
		RolloverManager.rollover(sourceRollable, targetRollable, null, TEST_USER);
		
		// test deleted property
		List<Property> copiedProperties = new ArrayList<Property>(); 
		for (int i = 0; i < properties.length - 1; i++) {
			copiedProperties.add(properties[i]);
		}
		Property[] copiedPropertyArray = copiedProperties.toArray(new Property[copiedProperties.size()]);
		targetRollable.setProperties(copiedPropertyArray);
		try{
			RolloverManager.rollover(sourceRollable, targetRollable, null, TEST_USER);			
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.ROLLOVER_CANNOT_CHANGE_NUMBER_OF_PROPERTIES, e.getLanguageKey());
		}
		
		// test changed property name 
		copiedPropertyArray = PropertyUtils.clonePropertyList(properties);
		((DoubleProperty) copiedPropertyArray[0]).setName("BlaBla");
		targetRollable.setProperties(copiedPropertyArray);
		try{
			RolloverManager.rollover(sourceRollable, targetRollable, null, TEST_USER);			
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.ROLLOVER_CANNOT_ADD_OR_REMOVE_PROPERTIES, e.getLanguageKey());
		}
		
		// test changed property value 
		copiedPropertyArray = PropertyUtils.clonePropertyList(properties);
		((BooleanProperty) copiedPropertyArray[3]).setValue(false);
		targetRollable.setProperties(copiedPropertyArray);
		try{
			RolloverManager.rollover(sourceRollable, targetRollable, null, TEST_USER);			
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.ROLLOVER_CANNOT_CHANGE_NON_ROLLABLE_PROPERTIES, e.getLanguageKey());
		}
		
		// test just change order of the properties
		copiedPropertyArray = PropertyUtils.clonePropertyList(properties);
		Property property = copiedPropertyArray[3];
		copiedPropertyArray[3] = copiedPropertyArray[0];
		copiedPropertyArray[0] = property; 
		targetRollable.setProperties(copiedPropertyArray);
		
		RolloverManager.rollover(sourceRollable, targetRollable, null, TEST_USER);			
		
	}
	
	private MockRollable createTargetRollable(String code_) {
		MockRollable targetRollable = createRollable(code_);
		
		targetRollable.setActivationDate(null);
		targetRollable.setActivationStatus(null); 
		targetRollable.setCreationAudit(null);
		targetRollable.setCodeRolledFrom(null);
		
		return targetRollable;
	}

	private MockRollable createUnderlyingRollable(String code_) {
		MockRollable underlyingRollable = createRollable(code_);
		return underlyingRollable;
	}
	
	private MockRollable createSourceRollable(String code_) {
		MockRollable sourceRollable = createRollable(code_);
		return sourceRollable;
	}

	private MockRollable createRollable(String code_) {
		MockRollable rollable = new MockRollable();
		rollable.setActivationDate(new Date());
		rollable.setActivationStatus(ActivationStatus.Activated);
		rollable.setCode(code_);
		rollable.setCodeRolledFrom(null);
		rollable.setCreationAudit(createAudit());
		rollable.setRollable(true);
		
		return rollable;
	}
	
	private static class MockRollable implements Rollable {

		private static final long serialVersionUID = 1L;
		
		private ActivationStatus _activationStatus;
		private String _code;
		private String _codeRolledFrom;
		private Property[] _properties;
		private boolean _rollable;
		private List<String> _rollablePropertyNames;
		private Date _activationDate;
		private AuditInformation _creationAudit;
		private AuditInformation _rolloverAudit;
		private String _underlyingCode;
		
		public void setRolloverAudit(AuditInformation rolloverAudit_) {
			_rolloverAudit = rolloverAudit_;
		}

		public void setUnderlyingCode(String underlyingCode_) {
			_underlyingCode = underlyingCode_;
		}

		public AuditInformation getCreationAudit() {
			return _creationAudit;
		}

		public void setRollable(boolean rollable_) {
			_rollable = rollable_;
		}

		public void setRollablePropertyNames(List<String> rollablePropertyNames_) {
			_rollablePropertyNames = rollablePropertyNames_;
		}

		public Date getActivationDate() {
			return _activationDate;
		}

		@Override
		public ActivationStatus getActivationStatus() {
			return _activationStatus;
		}

		@Override
		public String getCode() {
			return _code;
		}

		@Override
		public String getCodeRolledFrom() {
			return _codeRolledFrom;
		}

		@Override
		public Property[] getProperties() {
			return _properties;
		}

		@Override
		public boolean getRollable() {
			return _rollable;
		}

		public void setProperties(Property[] properties_) {
			_properties = properties_;
		}

		@Override
		public List<String> getRollablePropertyNames() {
			return _rollablePropertyNames;
		}

		@Override
		public String getUnderlyingCode() {
			return _underlyingCode;
		}

		@Override
		public void setActivationDate(Date activationDate_) {
			_activationDate = activationDate_;
		}

		@Override
		public void setActivationStatus(ActivationStatus activationStatus_) {
			_activationStatus = activationStatus_;
		}

		@Override
		public void setCodeRolledFrom(String codeRolledFrom_) {
			_codeRolledFrom = codeRolledFrom_;
		}

		@Override
		public void setCreationAudit(AuditInformation creationAudit_) {
			_creationAudit = creationAudit_;
		}

		public void setCode(String code) {
			_code = code;
		}

		@Override
		public UnderlyingType getUnderlyingType() {
			// no need for this 
			return null;
		}

		@Override
		public AuditInformation getApprovalAudit() {
			
			return null;
		}

		@Override
		public AuditInformation getChangeAudit() {
			
			return null;
		}

		@Override
		public AuditInformation getRolloverAudit() {
			
			return _rolloverAudit;
		}

		@Override
		public AuditInformation getSuspensionAudit() {
			
			return null;
		}

		@Override
		public void setNewCode(String code_) {
			setCode(code_);
		}

		@Override
		public Date getLastUpdateTimestamp() {
			return null;
		}

		@Override
		public String getDescription() {
			return null;
		}

		@Override
		public String getName() {
			return null;
		}
		
	}

}
