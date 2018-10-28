package com.imarcats.model;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import com.imarcats.model.meta.DataLengths;
import com.imarcats.model.types.OrderQuote;
import com.imarcats.model.types.OrderSide;
import com.imarcats.model.types.OrderType;
import com.imarcats.model.types.Property;
import com.imarcats.model.types.PropertyHolder;
import com.imarcats.model.types.Quote;
import com.imarcats.model.types.TradeProperties;
import com.imarcats.model.types.TradeQuote;
import com.imarcats.model.types.TradeSettlementState;


/**
 * One Side of a Trade 
 * @author Adam
 */
@Entity
@Table(name="TRADE_SIDE")
public class TradeSide implements MarketModelObject {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue 
	@Column(name="ID")
    private Long _id;
	
	/**
	 * Trader ID, who takes this Side of the Trade
	 * Required
	 */
	@Column(name="TRADER_ID", nullable=false, length=DataLengths.USER_ID_LENGTH)
	private String _traderID;
    
	/**
	 * Type of the Order, from which this Trade Side was Created (Value from OrderType)
	 * Required
	 */
	@Column(name="ORDER_TYPE", nullable=false)
	@Enumerated(EnumType.STRING) 
	private OrderType _orderType;

	/**
	 * Quote of the Order, from which this Trade Side was Created
	 * Required
	 */
	@OneToOne(cascade=CascadeType.ALL, orphanRemoval=true)
	@JoinColumn(name="ORDER_QUOTE_ID")
	private OrderQuote _orderQuote;

	/**
	 * Code of the Instrument traded
	 * Required
	 * 
	 * Note: This is a redundant information, stored here only to ease querying.
	 */
	@Column(name="INSTRUMENT_OF_THE_TRADE", nullable=false, length=DataLengths.MARKET_OBJECT_CODE_LENGTH)
	private String _instrumentOfTheTrade;
    
	/**
	 * Code of the Market, where the Trade was made
	 * Required
	 * 
	 * Note: This is a redundant information, stored here only to ease querying.
	 */
	@Column(name="MARKET_OF_THE_TRADE", nullable=false, length=DataLengths.MARKET_OBJECT_CODE_LENGTH)
	private String _marketOfTheTrade; 

	/**
	 * Side of the Trade (like Buy or Sell)
	 * Required
	 * 
	 * Note: This is a redundant information, stored here only to ease querying.
	 */
	@Column(name="SIDE", nullable=false)
	@Enumerated(EnumType.STRING) 
	private OrderSide _side;
    
	/**
	 * Quote at the Trade was made, Valid Quote Tells, if the Trade Quote is Valid 
	 * Required
	 *	 
	 * Note: This is a redundant information, stored here only to ease querying.
	 */
	@OneToOne(cascade=CascadeType.ALL, orphanRemoval=true)
	@JoinColumn(name="TRADE_QUOTE_ID")
	private TradeQuote _tradeQuote;
    
	/**
	 * Size of this Trade 
	 *	 
	 * Note: This is a redundant information, stored here only to ease querying.
	 * Required
	 */
	@Column(name="MATCHED_SIZE", nullable=false)
	private int _matchedSize;	
    
	/**
	 * Date and Time of Trade 
	 * Required
	 */
	@Column(name="TRADE_DATE_TIME", nullable=false)
	private Timestamp _tradeDateTime;
	
	/**
	 * Properties for the Trade Side
	 * Optional
	 */
	@OneToOne(cascade=CascadeType.ALL, orphanRemoval=true)
	@JoinColumn(name="TRADE_PROPERTIES_ID")
	private TradeProperties _tradeProperties = new TradeProperties(new PropertyHolder());
	
	/**
	 * Defines whether the Settlement State (Value from TradeSettlementState)
	 * Required
	 */
	@Column(name="SETTLEMENT_STATE", nullable=false)
	@Enumerated(EnumType.STRING) 
	private TradeSettlementState _settlementState = TradeSettlementState.NewTrade;
	
	/**
	 * External order reference (copied from the order)
	 * Required
	 */
	@Column(name="EXTERNAL_ORDER_REFERENCE", nullable=false, length=DataLengths.UUID_LENGTH)
	private String _externalOrderReference; 
	
	/**
	 * Commission charged for the Trade
	 * Required
	 */
	@Column(name="COMMISSION", nullable=false)
	private double _commission;

	/**
	 * Currency of Commission charged for the Trade
	 * Required
	 */
	@Column(name="COMMISSION_CURRENCY", nullable=false, length=DataLengths.CURRENCY_CODE_LENGTH)
	private String _commissionCurrency;
    
    /**
     * Matched Trade belongs to this trade side
     * Required
     */
	@ManyToOne
	@PrimaryKeyJoinColumn
    private MatchedTrade _matchedTrade;
    
    /**
     * Contract Size of the Trade (Copied from Instrument)
     * TODO: Rename to size
     */
	@Column(name="CONTRACT_SIDE", nullable=false)
    private Double _contractSize = 1.0;
	
	public TradeSide() {
		super();
	}

	public TradeSide(String traderID_, OrderType orderType_, OrderSide side_, Quote orderQuote_, String instrumentOfTheTrade_, String marketOfTheTrade_, Date tradeDateTime_, double contractSize_, double commission_, String commissionCurrency_, Property[] tradeProperties_, String externalOrderReference_) {
		super();
		_traderID = traderID_;
		_orderType = orderType_;
		_side = side_;
		setOrderQuote(orderQuote_); 
		_instrumentOfTheTrade = instrumentOfTheTrade_; 
		_marketOfTheTrade = marketOfTheTrade_;
		_tradeDateTime = new Timestamp(tradeDateTime_.getTime());
		_contractSize = contractSize_;
		_commission = commission_;
		_commissionCurrency = commissionCurrency_; 
		_tradeProperties = new TradeProperties();
		if(tradeProperties_ != null) {			
			for (Property tradeProperty : tradeProperties_) {			
				_tradeProperties.getPropertyHolder().addProperty(tradeProperty);
			}
		}
		_externalOrderReference = externalOrderReference_; 
	}

	public MatchedTrade getMatchedTrade() {
		return _matchedTrade;
	}

	public void setMatchedTrade(MatchedTrade matchedTrade_) {
		_matchedTrade = matchedTrade_;
	}

	public String getTraderID() {
		return _traderID;
	}

	public OrderType getOrderType() {
		return _orderType;
	}

	public void setOrderQuote(Quote orderQuote_) {
		_orderQuote = new OrderQuote(Quote.createQuote(orderQuote_)); // Cloning Quote is needed, because it might be copied from an other persisted Object
	}
	
	public Quote getOrderQuote() {
		return _orderQuote != null 
					? _orderQuote.getQuote() 
					: null;
	}

	public Quote getTradeQuote() {
		return _tradeQuote != null 
					? _tradeQuote.getQuote() 
					: null;
	}

	public void setTradeQuote(Quote tradeQuote_) {
		_tradeQuote = new TradeQuote(Quote.createQuote(tradeQuote_)); // Cloning Quote is needed, because it might be copied from an other persisted Object
	}
	
	public Integer getMatchedSize() {
		return _matchedSize;
	}

	public void setMatchedSize(Integer matchedSize_) {
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

	public Long getTransactionID() {
		return _matchedTrade.getTransactionID();
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

	public void setOrderQuote(OrderQuote orderQuote_) {
		_orderQuote = orderQuote_;
	}

	public void setMarketOfTheTrade(String marketOfTheTrade_) {
		_marketOfTheTrade = marketOfTheTrade_;
	}

	public void setTradeQuote(TradeQuote tradeQuote_) {
		_tradeQuote = tradeQuote_;
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
	public Property[] getTradeProperties() {
		if(checkTradeProperties()) {			
			return _tradeProperties.getPropertyHolder().getProperties();
		}
		
		return null;
	}

	private void initTradeProperties() {
		if(_tradeProperties == null) {
			_tradeProperties = new TradeProperties(new PropertyHolder());
		}
	}
	
	private boolean checkTradeProperties() {
		return _tradeProperties != null && _tradeProperties.getPropertyHolder() != null;
	}
	
	public void setTradeProperties(Property[] properties_) {
		clearTradeProperties();
		for (Property property : properties_) {
			addTradeProperty(property);
		}
	}
	
	public void addTradeProperty(Property tradeProperty_) {
		initTradeProperties();
		_tradeProperties.getPropertyHolder().addProperty(tradeProperty_);
	}
	
	public void deleteTradeProperty(Property tradeProperty_) {
		if(checkTradeProperties()) {					
			_tradeProperties.getPropertyHolder().deleteProperty(tradeProperty_);
		}
	}

	public void clearTradeProperties() {
		if(checkTradeProperties()) {			
			_tradeProperties.getPropertyHolder().clearProperties();
		}
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

	// NOTE: Matched Trade (Parent) must be excluded to avoid recursion and stack overflow
	@Override
	public String toString() {
		return "TradeSide [_id=" + _id + ", _traderID=" + _traderID
				+ ", _orderType=" + _orderType + ", _orderQuote=" + _orderQuote
				+ ", _instrumentOfTheTrade=" + _instrumentOfTheTrade
				+ ", _marketOfTheTrade=" + _marketOfTheTrade + ", _side="
				+ _side + ", _tradeQuote=" + _tradeQuote + ", _matchedSize="
				+ _matchedSize + ", _tradeDateTime=" + _tradeDateTime
				+ ", _tradeProperties=" + _tradeProperties
				+ ", _settlementState=" + _settlementState
				+ ", _externalOrderReference=" + _externalOrderReference
				+ ", _commission=" + _commission + ", _commissionCurrency="
				+ _commissionCurrency + ", _contractSize=" + _contractSize
				+ "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(_commission);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime
				* result
				+ ((_commissionCurrency == null) ? 0 : _commissionCurrency
						.hashCode());
		result = prime * result
				+ ((_contractSize == null) ? 0 : _contractSize.hashCode());
		result = prime
				* result
				+ ((_externalOrderReference == null) ? 0
						: _externalOrderReference.hashCode());
		result = prime * result + ((_id == null) ? 0 : _id.hashCode());
		result = prime
				* result
				+ ((_instrumentOfTheTrade == null) ? 0 : _instrumentOfTheTrade
						.hashCode());
		result = prime
				* result
				+ ((_marketOfTheTrade == null) ? 0 : _marketOfTheTrade
						.hashCode());
		result = prime * result + _matchedSize;
		result = prime * result
				+ ((_orderQuote == null) ? 0 : _orderQuote.hashCode());
		result = prime * result
				+ ((_orderType == null) ? 0 : _orderType.hashCode());
		result = prime
				* result
				+ ((_settlementState == null) ? 0 : _settlementState.hashCode());
		result = prime * result + ((_side == null) ? 0 : _side.hashCode());
		result = prime * result
				+ ((_tradeDateTime == null) ? 0 : _tradeDateTime.hashCode());
		result = prime
				* result
				+ ((_tradeProperties == null) ? 0 : _tradeProperties.hashCode());
		result = prime * result
				+ ((_tradeQuote == null) ? 0 : _tradeQuote.hashCode());
		result = prime * result
				+ ((_traderID == null) ? 0 : _traderID.hashCode());
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
		TradeSide other = (TradeSide) obj;
		if (Double.doubleToLongBits(_commission) != Double
				.doubleToLongBits(other._commission))
			return false;
		if (_commissionCurrency == null) {
			if (other._commissionCurrency != null)
				return false;
		} else if (!_commissionCurrency.equals(other._commissionCurrency))
			return false;
		if (_contractSize == null) {
			if (other._contractSize != null)
				return false;
		} else if (!_contractSize.equals(other._contractSize))
			return false;
		if (_externalOrderReference == null) {
			if (other._externalOrderReference != null)
				return false;
		} else if (!_externalOrderReference
				.equals(other._externalOrderReference))
			return false;
		if (_id == null) {
			if (other._id != null)
				return false;
		} else if (!_id.equals(other._id))
			return false;
		if (_instrumentOfTheTrade == null) {
			if (other._instrumentOfTheTrade != null)
				return false;
		} else if (!_instrumentOfTheTrade.equals(other._instrumentOfTheTrade))
			return false;
		if (_marketOfTheTrade == null) {
			if (other._marketOfTheTrade != null)
				return false;
		} else if (!_marketOfTheTrade.equals(other._marketOfTheTrade))
			return false;
		if (_matchedSize != other._matchedSize)
			return false;
		if (_orderQuote == null) {
			if (other._orderQuote != null)
				return false;
		} else if (!_orderQuote.equals(other._orderQuote))
			return false;
		if (_orderType != other._orderType)
			return false;
		if (_settlementState != other._settlementState)
			return false;
		if (_side != other._side)
			return false;
		if (_tradeDateTime == null) {
			if (other._tradeDateTime != null)
				return false;
		} else if (!_tradeDateTime.equals(other._tradeDateTime))
			return false;
		if (_tradeProperties == null) {
			if (other._tradeProperties != null)
				return false;
		} else if (!_tradeProperties.equals(other._tradeProperties))
			return false;
		if (_tradeQuote == null) {
			if (other._tradeQuote != null)
				return false;
		} else if (!_tradeQuote.equals(other._tradeQuote))
			return false;
		if (_traderID == null) {
			if (other._traderID != null)
				return false;
		} else if (!_traderID.equals(other._traderID))
			return false;
		return true;
	}	
}
