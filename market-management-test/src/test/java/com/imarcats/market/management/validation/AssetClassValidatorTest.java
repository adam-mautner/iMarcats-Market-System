package com.imarcats.market.management.validation;

import com.imarcats.interfaces.client.v100.exception.ExceptionLanguageKeys;
import com.imarcats.interfaces.client.v100.exception.MarketRuntimeException;
import com.imarcats.model.AssetClass;
import com.imarcats.model.types.Property;

public class AssetClassValidatorTest extends ValidatorTestCaseBase {

	public void testValidation() throws Exception {		
		try {
			AssetClassValidator.validateAssetClass(null);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.NULL_ASSET_CLASS_CANNOT_BE_CREATED, e.getLanguageKey());
			// expected 
		}
	
		AssetClass assetClass = new AssetClass();
		
		// test missing name 
		try {
			AssetClassValidator.validateAssetClass(assetClass);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.ASSET_CLASS_WITHOUT_VALID_NAME, e.getLanguageKey());
			// expected 
		}
		
		// test invalid name 
		assetClass.setName("Name ");
		try {
			AssetClassValidator.validateAssetClass(assetClass);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.ASSET_CLASS_WITHOUT_VALID_NAME, e.getLanguageKey());
			// expected 
		}
		assetClass.setName(" Name");
		try {
			AssetClassValidator.validateAssetClass(assetClass);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.ASSET_CLASS_WITHOUT_VALID_NAME, e.getLanguageKey());
			// expected 
		}
		assetClass.setName("Na me");
		try {
			AssetClassValidator.validateAssetClass(assetClass);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.ASSET_CLASS_WITHOUT_VALID_NAME, e.getLanguageKey());
			// expected 
		}
		assetClass.setName("    ");
		try {
			AssetClassValidator.validateAssetClass(assetClass);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.ASSET_CLASS_WITHOUT_VALID_NAME, e.getLanguageKey());
			// expected 
		}
		
		assetClass.setName("NAME");
		
		// test missing description
		try {
			AssetClassValidator.validateAssetClass(assetClass);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.ASSET_CLASS_WITHOUT_DESCRIPTION, e.getLanguageKey());
			// expected 
		}
		
		assetClass.setDescription("Descr");
		
		// test successful validation
		AssetClassValidator.validateAssetClass(assetClass);		
		
		// test invalid asset class parent
		assetClass.setParentName("ahdhs//=//");
		try {
			AssetClassValidator.validateAssetClass(assetClass);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.ASSET_CLASS_PARENT_NAME_INVALID, e.getLanguageKey());
			// expected 
		}
		
		assetClass.setParentName("TEST");
		
		// test successful validation
		AssetClassValidator.validateAssetClass(assetClass);	
		
		// test properties 	
		final AssetClass assetClassFinal = assetClass;
		testProperties(new PropertyAccessor() {
			
			@Override
			public void validate() {
				AssetClassValidator.validateAssetClass(assetClassFinal);
			}
			
			@Override
			public Property[] getProperties() {
				return assetClassFinal.getProperties();
			}
			
			@Override
			public void deleteProperty(Property property_) {
				assetClassFinal.deleteProperty(property_);
			}
			
			@Override
			public void clearProperties() {
				assetClassFinal.clearProperties();
			}
			
			@Override
			public void addProperty(Property property_) {
				assetClassFinal.addProperty(property_);
			}
		});
	}
	
}
