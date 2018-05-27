package com.imarcats.interfaces.client.v100.dto;

import com.imarcats.interfaces.client.v100.dto.types.OrderSide;
import com.imarcats.interfaces.client.v100.dto.types.QuoteDto;
import com.imarcats.interfaces.client.v100.dto.types.TradeSettlementState;
import com.imarcats.interfaces.client.v100.dto.types.TradeSideDto;

/**
 * Two Orders that can make up a Trade  
 * @author Adam
 */
public class MatchedTradeDto implements MarketModelObjectDto {
	
    /**
     * Unique Transaction ID for this Trade 
     */
    private Long _transactionID;
	
	/**
	 * Size of this Trade 
	 * Required
	 */
	private Integer _matchedSize;	
    
	/**
	 * Buy Side of the Trade
	 * Required
	 */
	private TradeSideDto _buySide;
    
	/**
	 * Sell Side of the Trade
	 * Required
	 */
	private TradeSideDto _sellSide;
	
	/**
	 * Defines, which side of the Matched Orders was a Standing Order in 
	 * the Book. This is only relevant for Discriminatory Pricing Rule and 
	 * it does not make sense for Uniform Pricing Rule (as all Matched Orders
	 * were in the Book)
	 * 
	 * (Value from OrderSide)
	 */
	private OrderSide _standingOrderSide;
	
	/**
	 * Code of the Market, where the Trade was made
	 */
//	@Column(name="MARKET_OF_THE_TRADE", nullable=false, length=DataLengths.MARKET_OBJECT_CODE_LENGTH)
	private String _marketOfTheTrade;
	
	/**
	 * Quote at the Trade was made, Valid Quote Tells, if the Trade Quote is Valid 
	 */
	private QuoteDto _tradeQuote;
	    
	/**
	 * Defines whether the Settlement State (Value from TradeSettlementState)
	 * Required
	 */
	private TradeSettlementState _settlementState = TradeSettlementState.NewTrade;
	
	public MatchedTradeDto() {
		super();
	}

	public Integer getMatchedSize() {
		return _matchedSize;
	}

	public String getMarketOfTheTrade() {
		return _marketOfTheTrade;
	}

	public OrderSide getStandingOrderSide() {
		return _standingOrderSide;
	}

	public Long getTransactionID() {
		return _transactionID;
	}

	public void setTransactionID(Long _transactionID) {
		this._transactionID = _transactionID;
	}

	public TradeSideDto getBuySide() {
		return _buySide;
	}

	public void setBuySide(TradeSideDto buySide_) {
		_buySide = buySide_;
	}

	public TradeSideDto getSellSide() {
		return _sellSide;
	}

	public void setSellSide(TradeSideDto sellSide_) {
		_sellSide = sellSide_;
	}

	public QuoteDto getTradeQuote() {
		return _tradeQuote;
	}

	public void setTradeQuote(QuoteDto tradeQuote_) {
		_tradeQuote = tradeQuote_;
	}

	public void setMatchedSize(Integer matchedSize_) {
		_matchedSize = matchedSize_;
	}

	public void setStandingOrderSide(OrderSide standingOrderSide_) {
		_standingOrderSide = standingOrderSide_;
	}

	public void setMarketOfTheTrade(String marketOfTheTrade_) {
		_marketOfTheTrade = marketOfTheTrade_;
	}

	public TradeSettlementState getSettlementState() {
		return _settlementState;
	}

	public void setSettlementState(TradeSettlementState settlementState_) {
		_settlementState = settlementState_;
	}
	
}
