package com.imarcats.interfaces.server.v100.market;

import com.imarcats.model.CircuitBreaker;
import com.imarcats.model.Instrument;
import com.imarcats.model.types.ActivationStatus;
import com.imarcats.model.types.BusinessCalendar;
import com.imarcats.model.types.ExecutionSystem;
import com.imarcats.model.types.MarketState;
import com.imarcats.model.types.Quote;
import com.imarcats.model.types.QuoteAndSize;
import com.imarcats.model.types.QuoteType;
import com.imarcats.model.types.SecondaryOrderPrecedenceRuleType;
import com.imarcats.model.types.TimeOfDay;
import com.imarcats.model.types.TimePeriod;
import com.imarcats.model.types.TradingSession;

/**
 * Defines the set of Operations on the Market.
 */
public interface MarketInterface {

	/**
	 * @return Instrument traded on this Market
	 */
	public Instrument getInstrument();
	
	/**
	 * @return Name of the Market
	 */
	public String getName();
	
	/**
	 * @return Description of the Market
	 */
	public String getDescription();
	
	/**
	 * @return Short Code for the Market
	 */
	public String getMarketCode();
	
	/**
	 * @return Type of the Quote on the Market (Value from QuoteType)
	 */
	public QuoteType getQuoteType();
	
	/**
	 * @return Address of the Market, Corporate Information for the Market
	 */
//	public BusinessEntity getBusinessEntity();
	
	/**
	 * @return Reference to the Market Operation Contract
	 */
	String getMarketOperationContract();
	
	/**
	 * @return Minimum Number of Contracts Traded (Lots, Odd-Lots), 0 if not defined
	 */
	public int getMinimumContractsTraded();
	
	/**
	 * @return Maximum Number of Contracts Traded (Lots, Odd-Lots), Integer.MAX_VALUE if not defined
	 */
	public int getMaximumContractsTraded();
	
	/**
	 * @return Minimum Quote change that needs to be made on the Market
	 */
	public double getMinimumQuoteIncrement();
	
	/**
	 * @return Trading Session type, Continuous Trading or Market Closes - 
	 * 		   (Value from TradingSession)
	 */
	public TradingSession getTradingSession();
	
	/**
	 * @return Defines when the Market is Closed, and when Open
	 */
	public TimePeriod getTradingHours();
	
	/**
	 * @return End of Trading Day, Positions Marked-to-Market and Settlement Happens
	 */
	public TimeOfDay getTradingDayEnd();
	
	/**
	 * @return Business Day Calendar for the Market 
	 */
	public BusinessCalendar getBusinessCalendar();
	
	/**
	 * @return Defines how the Orders will be Executed on the Market (Value from ExecutionSystem)
	 */
	public ExecutionSystem getExecutionSystem();
	
	/**
	 * @return Defines if the Market allows Hidden Orders
	 */
	public boolean getAllowHiddenOrders();

	/**
	 * @return Defines if the Market allows Size Restriction on Orders
	 */
	public boolean getAllowSizeRestrictionOnOrders();
	
	/**
	 * @return Market TimeZone ID
	 */
	public String getMarketTimeZoneID();
	
	/**
	 * @return Defines how the Orders will be organized in the Book (Value from OrderPrecedenceRule)
	 */
	public SecondaryOrderPrecedenceRuleType[] getSecondaryOrderPrecedenceRules();
	
	/**
	 * @return Defines Circuit Breaker of the Marker
	 */
	public CircuitBreaker getCircuitBreaker();
	
	/**
	 * @return Clearing Bank
	 */
	public String getClearingBank();
	
	/**
	 * @return Commission charged for the Trade on the Market
	 */
	public double getCommission();
	
	/**
	 * @return Currency of Commission charged for the Trade on the Market
	 */
	public String getCommissionCurrency();
	
	/**
	 * @return Opening State of the Market (Value from MarketState)
	 */
	public MarketState getState();
	
	/**
	 * @return if the Market is Active or Suspended (Value from ActivationStatus)
	 */
	public ActivationStatus getActivationStatus();
	
	/**
	 * @return Best Bid on the Market to be Returned to the Client
	 * Note: Hidden Orders (DisplayOrder = False) will not be added to the Calculation
	 */
	public QuoteAndSize getBestBid();
	
	/**
	 * @return Best Ask (Offer) on the Market to be Returned to the Client
	 * Note: Hidden Orders (DisplayOrder = False) will not be added to the Calculation
	 */
	public QuoteAndSize getBestAsk();
	
	/**
	 * @return Last Trade on the Market
	 */
	public QuoteAndSize getLastTrade();
	
	/**
	 * @return Previous Last Trade on the Market
	 */
	public QuoteAndSize getPreviousLastTrade();
	
	/**
	 * @return Opening Quote of the Market
	 */
	public Quote getOpeningQuote();

	/**
	 * @return Closing Quote of the Market
	 */
	public Quote getClosingQuote();
	
	/**
	 * @return Previous Opening Quote of the Market
	 */
	public Quote getPreviousOpeningQuote();

	/**
	 * @return Previous Closing Quote of the Market
	 */
	public Quote getPreviousClosingQuote();

}
