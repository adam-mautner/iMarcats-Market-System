package com.imarcats.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.imarcats.model.meta.DataLengths;
import com.imarcats.model.types.OrderSide;
import com.imarcats.model.types.Quote;
import com.imarcats.model.types.TradeSettlementState;
import com.imarcats.model.types.TradeSide;

/**
 * Two Orders that can make up a Trade  
 * @author Adam
 */
@Entity
@Table(name="MATCHED_TRADE")
public class MatchedTrade implements MarketModelObject {

	private static final long serialVersionUID = 1L;
	
    /**
     * Unique Transaction ID for this Trade 
     */
	@Id
	@GeneratedValue 
	@Column(name="TRANSACTION_ID")
    private Long _transactionID;
	
	/**
	 * Size of this Trade 
	 * Required
	 */
	@Column(name="MATCHED_SIZE", nullable=false)
	private Integer _matchedSize;	
    
	/**
	 * Buy Side of the Trade
	 * Required
	 */
	@OneToOne(cascade=CascadeType.ALL, orphanRemoval=true)
	@JoinColumn(name="BUY_SIDE_ID")
	private TradeSide _buySide;
    
	/**
	 * Sell Side of the Trade
	 * Required
	 */
	@OneToOne(cascade=CascadeType.ALL, orphanRemoval=true)
	@JoinColumn(name="SELL_SIDE_ID")
	private TradeSide _sellSide;
	
	/**
	 * Defines, which side of the Matched Orders was a Standing Order in 
	 * the Book. This is only relevant for Discriminatory Pricing Rule and 
	 * it does not make sense for Uniform Pricing Rule (as all Matched Orders
	 * were in the Book)
	 * 
	 * (Value from OrderSide)
	 */
	@Column(name="STANDING_ORDER_SIDE")
	@Enumerated(EnumType.STRING) 
	private OrderSide _standingOrderSide;
	
	/**
	 * Code of the Market, where the Trade was made
	 */
	@Column(name="MARKET_OF_THE_TRADE", nullable=false, length=DataLengths.MARKET_OBJECT_CODE_LENGTH)
	private String _marketOfTheTrade;
	
	/**
	 * Defines whether the Settlement State (Value from TradeSettlementState)
	 * Required
	 */
	@Column(name="SETTLEMENT_STATE", nullable=false)
	@Enumerated(EnumType.STRING) 
	private TradeSettlementState _settlementState = TradeSettlementState.NewTrade;
	
	/**
	 * Quote at the Trade was made, Valid Quote Tells, if the Trade Quote is Valid 
	 */
	@Embedded
	private Quote _tradeQuote;
	
	public MatchedTrade() {
		super();
	}

	public MatchedTrade(int matchedSize_, TradeSide buySide_,
			TradeSide sellSide_, String marketOfTheTrade_, OrderSide standingOrderSide_) {
		super();
		_matchedSize = matchedSize_;
		
		buySide_.setMatchedTrade(this);
		sellSide_.setMatchedTrade(this);
		
		_buySide = buySide_;
		_sellSide = sellSide_;
		_marketOfTheTrade = marketOfTheTrade_;
		_standingOrderSide = standingOrderSide_;
		
		getBuySide().setMatchedSize(_matchedSize);
		getSellSide().setMatchedSize(_matchedSize);
	}
	
	public Integer getMatchedSize() {
		return _matchedSize != null ? _matchedSize : 0;
	}

	public void setTradeQuote(Quote tradeQuote_) {
		_tradeQuote = Quote.createQuote(tradeQuote_); // Cloning Quote is needed, because it might be copied from an other persisted Object
		if(getBuySide() != null) {			
			getBuySide().setTradeQuote(tradeQuote_);
		}
		if(getSellSide() != null) {			
			getSellSide().setTradeQuote(tradeQuote_);
		}
	}

	public Quote getTradeQuote() {
		return _tradeQuote;
	}

	public TradeSide getBuySide() {
		return _buySide;
	}

	public TradeSide getSellSide() {
		return _sellSide;
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

	public void setMatchedSize(Integer matchedSize_) {
		_matchedSize = matchedSize_;
	}

	public void setBuySide(TradeSide buySide_) {
		_buySide = buySide_;
	}

	public void setSellSide(TradeSide sellSide_) {
		_sellSide = sellSide_;
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

	@Override
	public String toString() {
		return "MatchedTrade [_transactionID=" + _transactionID
				+ ", _matchedSize=" + _matchedSize + ", _buySide=" + _buySide
				+ ", _sellSide=" + _sellSide + ", _standingOrderSide="
				+ _standingOrderSide + ", _marketOfTheTrade="
				+ _marketOfTheTrade + ", _settlementState=" + _settlementState
				+ ", _tradeQuote=" + _tradeQuote + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((_buySide == null) ? 0 : _buySide.hashCode());
		result = prime
				* result
				+ ((_marketOfTheTrade == null) ? 0 : _marketOfTheTrade
						.hashCode());
		result = prime * result
				+ ((_matchedSize == null) ? 0 : _matchedSize.hashCode());
		result = prime * result
				+ ((_sellSide == null) ? 0 : _sellSide.hashCode());
		result = prime
				* result
				+ ((_settlementState == null) ? 0 : _settlementState.hashCode());
		result = prime
				* result
				+ ((_standingOrderSide == null) ? 0 : _standingOrderSide
						.hashCode());
		result = prime * result
				+ ((_tradeQuote == null) ? 0 : _tradeQuote.hashCode());
		result = prime * result
				+ ((_transactionID == null) ? 0 : _transactionID.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MatchedTrade other = (MatchedTrade) obj;
		if (_buySide == null) {
			if (other._buySide != null)
				return false;
		} else if (!_buySide.equals(other._buySide))
			return false;
		if (_marketOfTheTrade == null) {
			if (other._marketOfTheTrade != null)
				return false;
		} else if (!_marketOfTheTrade.equals(other._marketOfTheTrade))
			return false;
		if (_matchedSize == null) {
			if (other._matchedSize != null)
				return false;
		} else if (!_matchedSize.equals(other._matchedSize))
			return false;
		if (_sellSide == null) {
			if (other._sellSide != null)
				return false;
		} else if (!_sellSide.equals(other._sellSide))
			return false;
		if (_settlementState != other._settlementState)
			return false;
		if (_standingOrderSide != other._standingOrderSide)
			return false;
		if (_tradeQuote == null) {
			if (other._tradeQuote != null)
				return false;
		} else if (!_tradeQuote.equals(other._tradeQuote))
			return false;
		if (_transactionID == null) {
			if (other._transactionID != null)
				return false;
		} else if (!_transactionID.equals(other._transactionID))
			return false;
		return true;
	}
}
