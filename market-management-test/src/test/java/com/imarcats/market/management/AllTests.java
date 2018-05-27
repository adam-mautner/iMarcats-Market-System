package com.imarcats.market.management;

import com.imarcats.market.management.rollover.RolloverManagerTest;
import com.imarcats.market.management.validation.AssetClassValidatorTest;
import com.imarcats.market.management.validation.InstrumentValidatorTest;
import com.imarcats.market.management.validation.MarketOperatorValidatorTest;
import com.imarcats.market.management.validation.MarketValidatorTest;
import com.imarcats.market.management.validation.ProductValidatorTest;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Test for com.imarcats.market.management");
		//$JUnit-BEGIN$
		suite.addTestSuite(InstrumentAdminstrationSubSystemTest.class);
		suite.addTestSuite(AssetClassAdministrationSubSystemTest.class);
		suite.addTestSuite(MarketOperatorAdminstrationSubSystemTest.class);
		suite.addTestSuite(ProductAdministrationSubSystemTest.class);
		suite.addTestSuite(MarketManagementAndAdministrationSubSystemTest.class);
		suite.addTestSuite(RolloverManagerTest.class);
		
		suite.addTestSuite(ProductValidatorTest.class);
		suite.addTestSuite(InstrumentValidatorTest.class);
		suite.addTestSuite(MarketValidatorTest.class);
		suite.addTestSuite(AssetClassValidatorTest.class);
		suite.addTestSuite(MarketOperatorValidatorTest.class);
		//$JUnit-END$
		return suite;
	}

}
