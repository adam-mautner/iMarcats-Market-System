package com.imarcats.interfaces.server.v100.util;

import com.imarcats.interfaces.client.v100.exception.MarketRuntimeException;


/**
 * Quote Rounding Utility Class 
 * @author Adam
 */
public class QuoteRounding {

	public static final int QUOTE_MAX_PRECISION = 10;
	public static final double QUOTE_ROUNDING_PROPORTION_TO_MINIMUM_PRICE_INCEREMENT = 100;
	
	private QuoteRounding() { /* static utility class */ }
	
	/**
	 * Gets the Threshold under which, Quotes can be rounded down or up to closest valid Quote, 
	 * above this threshold the Quote is invalid. Threshold is 1 / 100 of minimum price increment. 
	 * @param minimumQuoteIncrement_ Minimum Quote Increment of the Market
	 * @return Threshold
	 */
	public static double getQuoteRoundingThreshold(double minimumQuoteIncrement_) {
		return minimumQuoteIncrement_ / QUOTE_ROUNDING_PROPORTION_TO_MINIMUM_PRICE_INCEREMENT;
	}
	
	/**
	 * Checks the Minimum Price increment, that it can be used for decimal Quoting
	 * @param minimumPriceIncrement_ Price Increment value
	 * @return if it can be quoted
	 */
	public static boolean checkMinimumPriceIncrementCanBeQuotedInDecimal(double minimumPriceIncrement_) {
		double multiplier = getMultiplier();
		boolean canBeQuoted = false;
		if(minimumPriceIncrement_ > 1 / multiplier) {
			canBeQuoted = ((10 * multiplier) % (minimumPriceIncrement_ * multiplier)) == 0;	
		}
		return canBeQuoted;
	}

	private static double getMultiplier() {
		return Math.pow(10, QUOTE_MAX_PRECISION);
	}

	/**
	 * @return Maximum precision possible in the System
	 */
	public static double getMaximumPrecision() {
		return Math.pow(10, -QUOTE_MAX_PRECISION);
	}
	
	/**
	 * Gets the Closest Valid Quote to the given value 
	 * @param quote_ Quote Value 
	 * @param minimumPriceIncrement_ Minimum Price increment
	 * @return Closest Valid Quote
	 */
	public static double getClosestValidQuote(double quote_, double minimumPriceIncrement_) {
		double closestValidQuote = quote_;
		
		if(!inRange(0, minimumPriceIncrement_, getMaximumPrecision())) {
			double multiplier = getMultiplier();
			double quoteScaledUp = quote_ * multiplier;
			double priceIncrementScaledUp = minimumPriceIncrement_ * multiplier;
			
			double moduloQuoteAndIncrementScaledUp = quoteScaledUp % priceIncrementScaledUp;
			
			quoteScaledUp = quoteScaledUp - moduloQuoteAndIncrementScaledUp;
			
			double quote = quoteScaledUp / multiplier;
			double nextQuote = quote + minimumPriceIncrement_;
			
			closestValidQuote = quote;
			if(Math.abs(quote_ - quote) > Math.abs(quote_ - nextQuote)) {
				closestValidQuote = nextQuote;
			}
		}
		
		return closestValidQuote;
	}
	
	/**
	 * Rounds the Quote down or up to the next Valid Quote, if it is under 
	 * the given Threshold (1 /100 of Minimum Price Increment of the Market), or 
	 * throws a MarketRuntimeException if not.
	 * 
	 * @param quote_ Quote Value 
	 * @param minimumPriceIncrement_ Minimum Price increment
	 * @return the rounded Quote
	 * 
	 * @throws MarketRuntimeException
	 */
	public static double roundToValidQuote(double quote_, double minimumPriceIncrement_) {
		double closestValidQuote = getClosestValidQuote(quote_, minimumPriceIncrement_);
		
		if(!inRange(closestValidQuote, quote_, getQuoteRoundingThreshold(minimumPriceIncrement_))) {
			throw MarketRuntimeException.createExceptionWithDetails(
					MarketRuntimeException.ORDER_MUST_BE_SUBMITTED_WITH_A_VALID_MARKET_QUOTE, 
					null, "Minimum Price Increment=" + minimumPriceIncrement_);
		}
		
		return closestValidQuote;
	}
	
	private static boolean inRange(double center_, double value_, double range_) {
		return Math.abs(center_ - value_) <= range_;
		
	}
}
