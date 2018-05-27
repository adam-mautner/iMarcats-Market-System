package com.imarcats.internal.server.infrastructure.trigger;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

import com.imarcats.interfaces.client.v100.dto.types.MarketDataType;
import com.imarcats.internal.server.interfaces.market.MarketInternal;
import com.imarcats.internal.server.interfaces.order.OrderInternal;
import com.imarcats.internal.server.interfaces.order.OrderManagementContext;
import com.imarcats.model.types.OrderSide;
import com.imarcats.model.types.OrderState;
import com.imarcats.model.types.QuoteAndSize;
import com.imarcats.model.types.QuoteType;

/**
 * Base Class for Stop Loss Triggers
 * @author Adam
 */
@Entity
@Table(name="STOP_LOSS_TRIGGER_BASE")
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
public abstract class StopLossTriggerBase extends QuoteChangeTriggerBase {

	/**
	 * Key of the Order to be canceled
	 */
	@Column(name="ORDER_KEY", nullable=false)
	protected Long _orderKey;	

    /**
     * Constructor needed for Deserialization
     */
	public StopLossTriggerBase() {
		super();
	}
    
	public StopLossTriggerBase(Long orderKey_) {
		super();
		_orderKey = orderKey_;
	}

	/**
	 * Checks, if the Stop Order should be triggered
	 * @param quoteValue_ Quote Value 
	 * @param order_ Order to be Triggered
	 * @param market_ Market, where the Order will be submitted
	 * @return Trigger 
	 */
	protected boolean shouldTriggerStop(double quoteValue_, OrderInternal order_, MarketInternal market_) {
		boolean stop = false;
		
		if(order_.getCurrentStopQuote().getValidQuote()) {
			if(order_.getSide() == OrderSide.Sell) {
				if(market_.getQuoteType() == QuoteType.Price) {
					stop = quoteValue_ <= order_.getCurrentStopQuote().getQuoteValue();
				} else {
					// yield
					stop = quoteValue_ >= order_.getCurrentStopQuote().getQuoteValue();
				}			
			} else if(order_.getSide() == OrderSide.Buy) {
				if(market_.getQuoteType() == QuoteType.Price) {
					stop = quoteValue_ >= order_.getCurrentStopQuote().getQuoteValue();
				} else {
					// yield
					stop = quoteValue_ <= order_.getCurrentStopQuote().getQuoteValue();
				}	
			}
		}
		
		return stop;
	}
	
	/**
	 * Triggers Stop Order, if needed
	 * @param marketDataType_ Type of Market Data Changed 
	 * @param value_ Quote Value of the Change
	 * @param order_ Order to be Triggered
	 * @param market_ Market, where the Order will be submitted 
	 * @param pm_ Datastore Persistence Manager 
	 * @param marketDataSession_ Market Data Session
	 */
	protected void triggerStopIfNeeded(MarketDataType marketDataType_, QuoteAndSize value_, OrderInternal order_, 
			MarketInternal market_, OrderManagementContext orderManagementContext_) {
		if(marketDataType_ == MarketDataType.Last && isValidQuote(value_)) {
			if(shouldTriggerStop(value_.getQuote().getQuoteValue(), order_, market_)) {
				// submit stop order
				market_.submit(order_, orderManagementContext_);
				// remove trigger listener from market
				if(orderManagementContext_.getMarketDataSession() != null && order_.getQuoteChangeTriggerKey() != null) {
					orderManagementContext_.getMarketDataSession().removeMarketDataChangeListener(order_.getQuoteChangeTriggerKey());
				}
			}
		}
	}

	protected boolean isValidQuote(QuoteAndSize value_) {
		return value_ != null && value_.getQuote() != null && value_.getQuote().getValidQuote();
	}
	
	public boolean canBeTriggered(OrderInternal order_) {
		return order_.getState() == OrderState.WaitingSubmit;
	}

	public boolean isTriggered(OrderInternal order_) {
		return order_.getState() == OrderState.Submitted ||
			   order_.getState() == OrderState.Executed;
	}

	public Long getOrderKey() {
		return _orderKey;
	}

	public void setOrderKey(Long orderKey_) {
		_orderKey = orderKey_;
	}
}
