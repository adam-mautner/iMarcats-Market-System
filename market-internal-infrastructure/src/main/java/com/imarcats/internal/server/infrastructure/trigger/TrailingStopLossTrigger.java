package com.imarcats.internal.server.infrastructure.trigger;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.imarcats.interfaces.client.v100.dto.types.MarketDataType;
import com.imarcats.interfaces.client.v100.exception.MarketRuntimeException;
import com.imarcats.interfaces.client.v100.notification.MarketDataChange;
import com.imarcats.interfaces.server.v100.dto.mapping.MarketDtoMapping;
import com.imarcats.internal.server.infrastructure.notification.ListenerCallContext;
import com.imarcats.internal.server.interfaces.market.MarketInternal;
import com.imarcats.internal.server.interfaces.order.OrderInternal;
import com.imarcats.internal.server.interfaces.order.OrderManagementContext;
import com.imarcats.model.OrderPropertyNames;
import com.imarcats.model.types.OrderSide;
import com.imarcats.model.types.Quote;
import com.imarcats.model.types.QuoteAndSize;
import com.imarcats.model.types.QuoteType;
import com.imarcats.model.utils.PropertyUtils;

/**
 * Trailing Stop Loss Order follows the Favorable changes in Market Quote, but protects 
 * against the Non-Favorable ones. 
 * 
 * If the Market moves to the favorable direction the Stop Price is updated to 
 * this new value minus the Stop Price Difference. 
 * 
 * If the Market moves to Non-favorable Direction Trigger Submits the Order. 
 * 
 * @author Adam
 */
@Entity
@Table(name="TRAILING_STOP_LOSS_TRIGGER")
public class TrailingStopLossTrigger extends StopLossTriggerBase {

    /**
     * Constructor needed for Deserialization
     */
	public TrailingStopLossTrigger() {
		super();
	}
	
	public TrailingStopLossTrigger(Long orderKey_) {
		super(orderKey_);
	}

	@Override
	public void initTrigger(OrderInternal order_, MarketInternal market_,
			OrderManagementContext orderManagementContext_) {
		updateStopQuote(market_.getLastTrade(), order_, market_); 
		triggerStopIfNeeded(MarketDataType.Last, market_.getLastTrade(), order_, market_, orderManagementContext_);
	}

	private double getStopQuoteDifference(OrderInternal order_,
			MarketInternal market_) {
		return PropertyUtils.getDoublePropertyValue(
				order_.getTriggerProperties(), 
				OrderPropertyNames.STOP_QUOTE_DIFFERENCE_PROPERTY_NAME, 
				MarketRuntimeException.createExceptionWithDetails(
						MarketRuntimeException.STOP_QUOTE_DIFFERENCE_IS_NOT_DEFINED_ON_STOP_ORDER, null, 
						new Object[]{ order_, market_ }));
	}
	
	/**
	 * Updates the Stop Quote with the given Quote
	 * @param value_
	 * @param order_ 
	 * @param market_ 
	 */
	private void updateStopQuote(QuoteAndSize value_, OrderInternal order_, MarketInternal market_) {
		Quote stopQuote = order_.getCurrentStopQuote();
		double stopQuoteDifference = getStopQuoteDifference(order_, market_);
		if(stopQuote == null) {
			stopQuote = new Quote();
		}
		
		if(isValidQuote(value_)) {
			double quoteValue = value_.getQuote().getQuoteValue();
			
			if(order_.getSide() == OrderSide.Sell) {
				if(market_.getQuoteType() == QuoteType.Price) {
					double newStopQuoteProspect = quoteValue - stopQuoteDifference;
					if(!stopQuote.getValidQuote() || 
					   stopQuote.getQuoteValue() < newStopQuoteProspect) {
						setNewStopQuoteValue(stopQuote, newStopQuoteProspect);
					}
				} else {
					double newStopQuoteProspect = quoteValue + stopQuoteDifference;
					if(!stopQuote.getValidQuote() || 
					   stopQuote.getQuoteValue() > newStopQuoteProspect) {
						setNewStopQuoteValue(stopQuote, newStopQuoteProspect);
					}
				}			
			} else if(order_.getSide() == OrderSide.Buy) {
				if(market_.getQuoteType() == QuoteType.Price) {
					double newStopQuoteProspect = quoteValue + stopQuoteDifference;
					if(!stopQuote.getValidQuote() || 
					   stopQuote.getQuoteValue() > newStopQuoteProspect) {
						setNewStopQuoteValue(stopQuote, newStopQuoteProspect);
					}
				} else {
					double newStopQuoteProspect = quoteValue - stopQuoteDifference;
					if(!stopQuote.getValidQuote() || 
					   stopQuote.getQuoteValue() < newStopQuoteProspect) {
						setNewStopQuoteValue(stopQuote, newStopQuoteProspect);
					}
				}	
			}
		}
		
		order_.recordNewStopQuote(stopQuote);
	}

	/**
	 * Updates the Stop Quote Value
	 * @param stopQuote_ Quote
	 * @param newStopQuoteProspect_ New Value 
	 */
	private void setNewStopQuoteValue(Quote stopQuote_, double newStopQuoteProspect_) {
		stopQuote_.setQuoteValue(newStopQuoteProspect_);
		stopQuote_.setValidQuote(true);
	}

	/**
	 * Triggers the Stop Order Submit or Updates the Stop Price 
	 */
	@Override
	public void marketDataChanged(MarketDataChange value_, 
			ListenerCallContext listenerContext_) {
		OrderInternal order = getOrder(_orderKey, listenerContext_);
		MarketInternal market = getMarket(value_.getMarketCode(), listenerContext_);
		if(value_.getChangeType() == MarketDataType.Last && value_.getNewQuoteValid()) {
			if(shouldTriggerStop(value_.getNewQuoteValue(), order, market) && canBeTriggered(order)) {
				// submit stop order
				market.submit(order, listenerContext_.getOrderManagementContext());
			} else {
				updateStopQuote(MarketDtoMapping.INSTANCE.fromDto(value_.getNewQuoteAndSize()), order, market);
			}
		}
	}
	
}
