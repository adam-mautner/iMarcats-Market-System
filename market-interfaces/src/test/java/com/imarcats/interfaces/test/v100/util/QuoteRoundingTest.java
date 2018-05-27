package com.imarcats.interfaces.test.v100.util;

import junit.framework.TestCase;

import com.imarcats.interfaces.server.v100.util.QuoteRounding;

public class QuoteRoundingTest extends TestCase {

	
	public void testMinimumPriceIncrementCanBeQuotedInDecimal() throws Exception {
		assertEquals(true, QuoteRounding.checkMinimumPriceIncrementCanBeQuotedInDecimal(0.05));
		assertEquals(true, QuoteRounding.checkMinimumPriceIncrementCanBeQuotedInDecimal(0.005));
		assertEquals(true, QuoteRounding.checkMinimumPriceIncrementCanBeQuotedInDecimal(0.25));
		assertEquals(true, QuoteRounding.checkMinimumPriceIncrementCanBeQuotedInDecimal(0.50));
		assertEquals(false, QuoteRounding.checkMinimumPriceIncrementCanBeQuotedInDecimal(0.3));
		assertEquals(true, QuoteRounding.checkMinimumPriceIncrementCanBeQuotedInDecimal(0.10));
		assertEquals(true, QuoteRounding.checkMinimumPriceIncrementCanBeQuotedInDecimal(0.4));
		assertEquals(false, QuoteRounding.checkMinimumPriceIncrementCanBeQuotedInDecimal(4));
		assertEquals(false, QuoteRounding.checkMinimumPriceIncrementCanBeQuotedInDecimal(0.0075));
		assertEquals(false, QuoteRounding.checkMinimumPriceIncrementCanBeQuotedInDecimal(0.00000000001));

	}
	
	public void testClosestValidQuote() throws Exception {
		double minPriceIncrement = 0.05;	
		
		assertEquals(10.15, QuoteRounding.getClosestValidQuote(10.15, minPriceIncrement));
		assertEquals(10.15, QuoteRounding.getClosestValidQuote(10.1505, minPriceIncrement));
		assertEquals(10.15, QuoteRounding.getClosestValidQuote(10.1504, minPriceIncrement));
		assertEquals(10.20, QuoteRounding.getClosestValidQuote(10.1996, minPriceIncrement), QuoteRounding.getQuoteRoundingThreshold(minPriceIncrement));
		assertEquals(10.20, QuoteRounding.getClosestValidQuote(10.1995, minPriceIncrement), QuoteRounding.getQuoteRoundingThreshold(minPriceIncrement));
		assertEquals(10.20, QuoteRounding.getClosestValidQuote(10.20, minPriceIncrement));
		
		assertEquals(10.15, QuoteRounding.getClosestValidQuote(10.151, minPriceIncrement));
		assertEquals(10.20, QuoteRounding.getClosestValidQuote(10.18, minPriceIncrement), QuoteRounding.getQuoteRoundingThreshold(minPriceIncrement));	
		assertEquals(10.15, QuoteRounding.getClosestValidQuote(10.16, minPriceIncrement), QuoteRounding.getQuoteRoundingThreshold(minPriceIncrement));	
		assertEquals(10.15, QuoteRounding.getClosestValidQuote(10.1506, minPriceIncrement));			
		assertEquals(10.20, QuoteRounding.getClosestValidQuote(10.1994, minPriceIncrement), QuoteRounding.getQuoteRoundingThreshold(minPriceIncrement));
		
		assertEquals(10.1506, QuoteRounding.getClosestValidQuote(10.1506, 0));
	}
	
	public void testRounding() throws Exception {
		double minPriceIncrement = 0.05;
		
		assertEquals(10.15, QuoteRounding.roundToValidQuote(10.15, minPriceIncrement));
		assertEquals(10.15, QuoteRounding.roundToValidQuote(10.1505, minPriceIncrement));
		assertEquals(10.15, QuoteRounding.roundToValidQuote(10.1504, minPriceIncrement));
		assertEquals(10.20, QuoteRounding.roundToValidQuote(10.1996, minPriceIncrement), QuoteRounding.getQuoteRoundingThreshold(minPriceIncrement));
		assertEquals(10.20, QuoteRounding.roundToValidQuote(10.19956, minPriceIncrement), QuoteRounding.getQuoteRoundingThreshold(minPriceIncrement));
		assertEquals(10.20, QuoteRounding.roundToValidQuote(10.20, minPriceIncrement));
		
		assertEquals(10.1504, QuoteRounding.roundToValidQuote(10.1504, 0));
		
		try {
			QuoteRounding.roundToValidQuote(10.151, minPriceIncrement);
			fail();
		} catch (Exception e) {
			// expected
		}
		try {
			QuoteRounding.roundToValidQuote(10.18, minPriceIncrement);
			fail();
		} catch (Exception e) {
			// expected
		}
		try {
			QuoteRounding.roundToValidQuote(10.16, minPriceIncrement);
			fail();
		} catch (Exception e) {
			// expected
		}
		try {
			QuoteRounding.roundToValidQuote(10.1506, minPriceIncrement);
			fail();
		} catch (Exception e) {
			// expected
		}
		try {
			QuoteRounding.roundToValidQuote(10.1994, minPriceIncrement);
			fail();
		} catch (Exception e) {
			// expected
		}		
		
		
	}
	
}
