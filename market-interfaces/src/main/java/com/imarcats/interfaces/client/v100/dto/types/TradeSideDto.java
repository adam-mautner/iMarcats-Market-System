package com.imarcats.interfaces.client.v100.dto.types;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;


/**
 * One Side of a Trade 
 * @author Adam
 */
public class TradeSideDto implements Serializable {
	
    /**
     * Unique Transaction ID for this Trade 
     */
    private Long _transactionID;
	
	/**
	 * Trader ID, who takes this Side of the Trade
	 * Required
	 */
//	@Column(name="TRADER_ID", nullable=false, length=DataLengths.USER_ID_LENGTH)
	private String _traderID;
    
	/**
	 * Type of the Order, from which this Trade Side was Created (Value from OrderType)
	 * Required
	 */
	private OrderType _orderType;

	/**
	 * Quote of the Order, from which this Trade Side was Created
	 * Required
	 */
	private QuoteDto _orderQuote;

	/**
	 * Code of the Instrument traded
	 * Required
	 * 
	 * Note: This is a redundant information, stored here only to ease querying.
	 */
//	@Column(name="INSTRUMENT_OF_THE_TRADE", nullable=false, length=DataLengths.MARKET_OBJECT_CODE_LENGTH)
	private String _instrumentOfTheTrade;
    
	/**
	 * Code of the Market, where the Trade was made
	 * Required
	 * 
	 * Note: This is a redundant information, stored here only to ease querying.
	 */
//	@Column(name="MARKET_OF_THE_TRADE", nullable=false, length=DataLengths.MARKET_OBJECT_CODE_LENGTH)
	private String _marketOfTheTrade; 

	/**
	 * Side of the Trade (like Buy or Sell)
	 * Required
	 * 
	 * Note: This is a redundant information, stored here only to ease querying.
	 */
	private OrderSide _side;
    
	/**
	 * Quote at the Trade was made, Valid Quote Tells, if the Trade Quote is Valid 
	 * Required
	 *	 
	 * Note: This is a redundant information, stored here only to ease querying.
	 */
	private QuoteDto _tradeQuote;
    
	/**
	 * Size of this Trade 
	 *	 
	 * Note: This is a redundant information, stored here only to ease querying.
	 * Required
	 */
	private int _matchedSize;	
    
	/**
	 * Date and Time of Trade 
	 * Required
	 */
	private Timestamp _tradeDateTime;
	
	/**
	 * Trade Properties 
	 * Optional
	 */
	private PropertyDto[] _tradeProperties;
	
	/**
	 * Defines whether the Settlement State (Value from TradeSettlementState)
	 * Required
	 */
	private TradeSettlementState _settlementState = TradeSettlementState.NewTrade;
	
	/**
	 * External order reference (copied from the order)
	 * Required
	 */
//	@Column(name="EXTERNAL_ORDER_REFERENCE", nullable=false, length=DataLengths.UUID_LENGTH)
	private String _externalOrderReference; 
	
	/**
	 * Commission charged for the Trade
	 * Required
	 */
	private double _commission;

	/**
	 * Currency of Commission charged for the Trade
	 * Required
	 */
	private String _commissionCurrency;
    
    /**
     * Contract Size of the Trade (Copied from Instrument)
     */
    private Double _contractSize = 1.0;

	public String getTraderID() {
		return _traderID;
	}

	public OrderType getOrderType() {
		return _orderType;
	}

	public int getMatchedSize() {
		return _matchedSize;
	}

	public void setMatchedSize(int matchedSize_) {
		_matchedSize = matchedSize_;
	}

	public String getMarketOfTheTrade() {
		return _marketOfTheTrade;
	}

	public Date getTradeDateTime() {
		return _tradeDateTime;
	}

	public double getCommission() {
		return _commission;
	}

	public String getCommissionCurrency() {
		return _commissionCurrency;
	}

	public OrderSide getSide() {
		return _side;
	}

	/*
	 * The following Setters are only needed for Blaze DS to see the given properties 
	 * other alternative is Flex Pasta Blaze DS annotations (see http://www.flexpasta.com/index.php/category/adobe-flex-security-java-blazeds/)
	 */
	public void setSide(OrderSide side_) {
		_side = side_;
	}

	public void setTraderID(String traderID_) {
		_traderID = traderID_;
	}

	public void setOrderType(OrderType orderType_) {
		_orderType = orderType_;
	}

	public void setMarketOfTheTrade(String marketOfTheTrade_) {
		_marketOfTheTrade = marketOfTheTrade_;
	}

	public void setTradeDateTime(Date tradeDateTime_) {
		_tradeDateTime = new Timestamp(tradeDateTime_.getTime());
	}

	public void setCommission(double commission_) {
		_commission = commission_;
	}

	public void setCommissionCurrency(String commissionCurrency_) {
		_commissionCurrency = commissionCurrency_;
	}

	public void setInstrumentOfTheTrade(String instrumentOfTheTrade) {
		_instrumentOfTheTrade = instrumentOfTheTrade;
	}

	public String getInstrumentOfTheTrade() {
		return _instrumentOfTheTrade;
	}

	public void setContractSize(double contractSize) {
		_contractSize = contractSize;
	}

	public double getContractSize() {
		return _contractSize != null ? _contractSize  : 1.0;
	}

	public QuoteDto getOrderQuote() {
		return _orderQuote;
	}

	public void setOrderQuote(QuoteDto orderQuote_) {
		_orderQuote = orderQuote_;
	}

	public QuoteDto getTradeQuote() {
		return _tradeQuote;
	}

	public void setTradeQuote(QuoteDto tradeQuote_) {
		_tradeQuote = tradeQuote_;
	}

//	public void setTradeDateTime(Timestamp tradeDateTime_) {
//		_tradeDateTime = tradeDateTime_;
//	}

	public void setContractSize(Double contractSize_) {
		_contractSize = contractSize_;
	}

	public Long getTransactionID() {
		return _transactionID;
	}

	public void setTransactionID(Long transactionID_) {
		_transactionID = transactionID_;
	}

	public PropertyDto[] getTradeProperties() {
		return _tradeProperties;
	}

	public void setTradeProperties(PropertyDto[] tradeProperties_) {
		_tradeProperties = tradeProperties_;
	}

	public TradeSettlementState getSettlementState() {
		return _settlementState;
	}

	public void setSettlementState(TradeSettlementState settlementState_) {
		_settlementState = settlementState_;
	}

	public String getExternalOrderReference() {
		return _externalOrderReference;
	}

	public void setExternalOrderReference(String externalOrderReference_) {
		_externalOrderReference = externalOrderReference_;
	}

}
