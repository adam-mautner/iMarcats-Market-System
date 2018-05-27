package com.imarcats.market.engine.matching;

import com.imarcats.interfaces.server.v100.validation.OrderValidator;
import com.imarcats.internal.server.infrastructure.datastore.MarketDatastore;
import com.imarcats.internal.server.infrastructure.datastore.OrderDatastore;
import com.imarcats.internal.server.interfaces.market.MarketInternal;
import com.imarcats.internal.server.interfaces.order.OrderInternal;
import com.imarcats.internal.server.interfaces.order.OrderManagementContext;
import com.imarcats.model.types.OrderState;

public class OrderCancelActionExecutor extends OrderActionExecutorBase {
	
	private final MarketDatastore _marketDatastore;
	private final OrderDatastore _orderDatastore;
	
	public OrderCancelActionExecutor(MarketDatastore marketDatastore_,
			OrderDatastore orderDatastore_) {
		super();
		_marketDatastore = marketDatastore_;
		_orderDatastore = orderDatastore_;
	}
	
	public void cancelOrder(Long orderKey_, String cancellationCommentLanguageKey_, OrderManagementContext orderManagementContext_) {
		OrderInternal orderInternal = _orderDatastore.findOrderBy(orderKey_);
		checkOrder(orderKey_, orderInternal); 
		String marketCode = orderInternal.getOrderModel().getTargetMarketCode();
		MarketInternal targetMarket = _marketDatastore.findMarketBy(marketCode); 
		checkMarket(marketCode, targetMarket);
		
		cancelOrder(orderInternal, targetMarket, cancellationCommentLanguageKey_, orderManagementContext_);
	}
	
	private void cancelOrder(OrderInternal orderInternal, MarketInternal targetMarket, String cancellationCommentLanguageKey_, OrderManagementContext orderManagementContext_) {
		OrderValidator.validateOrderForCancel(orderInternal.getOrderModel(), targetMarket.getMarketModel());

		orderInternal.getOrderModel().updateLastUpdateTimestamp();
		
		// Release instruments and/or cash at clearing agent - if needed (Pre-Trade Lock)
		
		if(orderInternal.getState() == OrderState.Submitted) {
			targetMarket.cancel(orderInternal, cancellationCommentLanguageKey_, orderManagementContext_);
		} else if(orderInternal.getState() == OrderState.WaitingSubmit) {
			orderInternal.recordNewStopQuote(null);
			orderInternal.recordCancel(cancellationCommentLanguageKey_, orderManagementContext_);
		
			orderInternal.
				removeQuoteChangeTrigger(targetMarket, orderManagementContext_).
					removeExpirationTrigger(targetMarket, orderManagementContext_);
			
		} else if(orderInternal.getState() == OrderState.PendingSubmit) {
			orderInternal.recordCancel(cancellationCommentLanguageKey_, orderManagementContext_);
		}
		
	}
}
