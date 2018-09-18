package com.imarcats.market.management.validation;

import com.imarcats.interfaces.client.v100.exception.ExceptionLanguageKeys;
import com.imarcats.interfaces.client.v100.exception.MarketRuntimeException;
import com.imarcats.market.management.ManagementTestBase;
import com.imarcats.model.MarketOperator;
import com.imarcats.model.types.ActivationStatus;

public class MarketOperatorValidatorTest extends ManagementTestBase {

	public void testChangeValidation() throws Exception {
		try {
			MarketOperatorValidator.validateMarketOperatorChange(null);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.NULL_MARKET_OPERATOR_CANNOT_BE_CREATED, e.getLanguageKey());
		}
		
		MarketOperator marketOperator = new MarketOperator();
		
		try {
			MarketOperatorValidator.validateMarketOperatorChange(marketOperator);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.MARKET_OPERATOR_MUST_HAVE_CODE, e.getLanguageKey());
		}
		marketOperator.setCode("TestCode ");
		try {
			MarketOperatorValidator.validateMarketOperatorChange(marketOperator);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.MARKET_OPERATOR_MUST_HAVE_CODE, e.getLanguageKey());
		}
		marketOperator.setCode(" TestCode");
		try {
			MarketOperatorValidator.validateMarketOperatorChange(marketOperator);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.MARKET_OPERATOR_MUST_HAVE_CODE, e.getLanguageKey());
		}
		marketOperator.setCode("Test Code");
		try {
			MarketOperatorValidator.validateMarketOperatorChange(marketOperator);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.MARKET_OPERATOR_MUST_HAVE_CODE, e.getLanguageKey());
		}
		
		marketOperator.setCode("TEST_CODE");
		
		try {
			MarketOperatorValidator.validateMarketOperatorChange(marketOperator);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.MARKET_OPERATOR_MUST_HAVE_NAME, e.getLanguageKey());
		}
		marketOperator.setName("TestName");
		
		try {
			MarketOperatorValidator.validateMarketOperatorChange(marketOperator);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.MARKET_OPERATOR_MUST_HAVE_DESCRIPTION, e.getLanguageKey());
		}
		marketOperator.setDescription("TestDesc");
		
		try {
			MarketOperatorValidator.validateMarketOperatorChange(marketOperator);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.MARKET_OPERATOR_MUST_HAVE_BUSINESS_ENTITY, e.getLanguageKey());
		}
		marketOperator.setBusinessEntityCode("TEST_BE");
		
		marketOperator.setOwnerUserID(null);
		try {
			MarketOperatorValidator.validateNewMarketOperator(marketOperator);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.MARKET_OPERATOR_MUST_HAVE_OWNER_USER, e.getLanguageKey());
		}
		marketOperator.setOwnerUserID("testOwner");

		MarketOperatorValidator.validateMarketOperatorChange(marketOperator);
	}
	
	public void testNewValidation() throws Exception {
		MarketOperator marketOperator = createMarketOperator("TEST_CODE");

		// we have removed this check to make market operator creation single step 
//		try {
//			MarketOperatorValidator.validateNewMarketOperator(marketOperator);
//			fail();
//		} catch (MarketRuntimeException e) {
//			assertEquals(ExceptionLanguageKeys.NEW_MARKET_OPERATOR_MUST_NOT_HAVE_MARKET_OPERATOR_AGREEMENT, e.getLanguageKey());
//		}
//		marketOperator.setMarketOperatorAgreement(null);
		
		try {
			MarketOperatorValidator.validateNewMarketOperator(marketOperator);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.NEW_MARKET_OPERATOR_MUST_NOT_HAVE_ACTIVATION_STATUS, e.getLanguageKey());
		}
		
		marketOperator.setActivationStatus(ActivationStatus.Approved);
		try {
			MarketOperatorValidator.validateNewMarketOperator(marketOperator);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.NEW_MARKET_OPERATOR_MUST_NOT_HAVE_ACTIVATION_STATUS, e.getLanguageKey());
		}
		marketOperator.setActivationStatus(null);
		
		try {
			MarketOperatorValidator.validateNewMarketOperator(marketOperator);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.NEW_MARKET_OPERATOR_MUST_NOT_HAVE_CREATION_AUDIT, e.getLanguageKey());
		}
		marketOperator.setCreationAudit(null);
		
		try {
			MarketOperatorValidator.validateNewMarketOperator(marketOperator);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.NEW_MARKET_OPERATOR_MUST_NOT_HAVE_CHANGE_AUDIT, e.getLanguageKey());
		}
		marketOperator.setChangeAudit(null);
		
		try {
			MarketOperatorValidator.validateNewMarketOperator(marketOperator);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.NEW_MARKET_OPERATOR_MUST_NOT_HAVE_APPROVAL_AUDIT, e.getLanguageKey());
		}
		marketOperator.setApprovalAudit(null);
		
		try {
			MarketOperatorValidator.validateNewMarketOperator(marketOperator);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.NEW_MARKET_OPERATOR_MUST_NOT_HAVE_SUSPENSION_AUDIT, e.getLanguageKey());
		}
		marketOperator.setSuspensionAudit(null);
		
		MarketOperatorValidator.validateNewMarketOperator(marketOperator);
		
		// test with created status 
		marketOperator.setActivationStatus(ActivationStatus.Created);
		MarketOperatorValidator.validateNewMarketOperator(marketOperator);
	}
}
