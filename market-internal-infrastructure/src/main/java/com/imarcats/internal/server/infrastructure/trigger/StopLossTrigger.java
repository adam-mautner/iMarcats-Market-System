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
import com.imarcats.model.types.Quote;
import com.imarcats.model.utils.PropertyUtils;

/** 
 * Submits an Order to Stop loss of a Position. When the Last Trade price of the Market 
 * passes the given Stop Price the Order is Triggered. 
 *  
 * @author Adam
 */
@Entity
@Table(name="STOP_LOSS_TRIGGER")
public class StopLossTrigger extends StopLossTriggerBase {
    /**
     * Constructor needed for Deserialization
     */
	public StopLossTrigger() {
		super();
	}
	
	public StopLossTrigger(Long orderKey_) {
		super(orderKey_);
	}

	public void initTrigger(OrderInternal order_, MarketInternal market_,
			OrderManagementContext orderManagementContext_) {
		double stopQuoteValue = PropertyUtils.getDoublePropertyValue(
				order_.getTriggerProperties(), 
				OrderPropertyNames.STOP_QUOTE_PROPERTY_NAME,
				MarketRuntimeException.createExceptionWithDetails(
						MarketRuntimeException.STOP_QUOTE_IS_NOT_DEFINED_ON_STOP_ORDER, null, 
						new Object[]{ order_, market_ }));
		
		Quote stopQuote = new Quote();
		stopQuote.setValidQuote(true);
		stopQuote.setQuoteValue(stopQuoteValue);
		
		order_.recordNewStopQuote(stopQuote);
		
		triggerStopIfNeeded(MarketDataType.Last, market_.getLastTrade(), order_, market_, orderManagementContext_);
	}
	

	@Override
	public void marketDataChanged(MarketDataChange change_, 
			ListenerCallContext listenerContext_) {
		OrderInternal order = getOrder(_orderKey, listenerContext_);
		
		if(canBeTriggered(order)) {
			MarketInternal market = getMarket(change_.getMarketCode(), listenerContext_);
			triggerStopIfNeeded(change_.getChangeType(), MarketDtoMapping.INSTANCE.fromDto(change_.getNewQuoteAndSize()), order, market, listenerContext_.getOrderManagementContext());
		}
	}
}
