package com.imarcats.interfaces.test.v100;

import com.imarcats.interfaces.server.v100.dto.mapping.AssetClassDtoMappingTest;
import com.imarcats.interfaces.server.v100.dto.mapping.AuditEntryDtoMappingTest;
import com.imarcats.interfaces.server.v100.dto.mapping.DatastoreKeyDtoMappingTest;
import com.imarcats.interfaces.server.v100.dto.mapping.InstrumentDtoMappingTest;
import com.imarcats.interfaces.server.v100.dto.mapping.MarketDtoMappingTest;
import com.imarcats.interfaces.server.v100.dto.mapping.MarketOperatorDtoMappingTest;
import com.imarcats.interfaces.server.v100.dto.mapping.MatchedTradeDtoMappingTest;
import com.imarcats.interfaces.server.v100.dto.mapping.OrderDtoMappingTest;
import com.imarcats.interfaces.server.v100.dto.mapping.ProductDtoMappingTest;
import com.imarcats.interfaces.test.v100.messages.serialization.MarketModelObjectSerializerTest;
import com.imarcats.interfaces.test.v100.util.QuoteRoundingTest;
import com.imarcats.interfaces.test.v100.validation.OrderValidatorTest;
import com.imarcats.model.test.mutators.MarketMutatorTest;
import com.imarcats.model.test.mutators.OrderMutatorTest;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Test for com.imarcats.interfaces.client.v100");
		//$JUnit-BEGIN$
		suite.addTestSuite(AssetClassDtoMappingTest.class);
		suite.addTestSuite(AuditEntryDtoMappingTest.class);
		suite.addTestSuite(DatastoreKeyDtoMappingTest.class);
		suite.addTestSuite(InstrumentDtoMappingTest.class);
		suite.addTestSuite(MarketDtoMappingTest.class);
		suite.addTestSuite(MarketOperatorDtoMappingTest.class);
		suite.addTestSuite(MatchedTradeDtoMappingTest.class);
		suite.addTestSuite(OrderDtoMappingTest.class);
		suite.addTestSuite(ProductDtoMappingTest.class);
		
		suite.addTestSuite(MarketModelObjectSerializerTest.class);
		suite.addTestSuite(QuoteRoundingTest.class);
		suite.addTestSuite(OrderValidatorTest.class);
		
		suite.addTestSuite(OrderMutatorTest.class);
		suite.addTestSuite(MarketMutatorTest.class);
		//$JUnit-END$
		return suite;
	}

}
