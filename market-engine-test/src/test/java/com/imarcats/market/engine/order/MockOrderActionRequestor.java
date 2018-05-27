package com.imarcats.market.engine.order;

import com.imarcats.internal.server.infrastructure.datastore.MarketDatastore;
import com.imarcats.internal.server.infrastructure.datastore.OrderDatastore;
import com.imarcats.internal.server.interfaces.order.OrderInternal;
import com.imarcats.internal.server.interfaces.order.OrderManagementContext;
import com.imarcats.market.engine.matching.OrderCancelActionExecutor;
import com.imarcats.market.engine.matching.OrderSubmitActionExecutor;

public class MockOrderActionRequestor implements OrderSubmitActionRequestor, OrderCancelActionRequestor {
	
	private OrderSubmitActionExecutor _orderSubmitActionExecutor;
	private OrderCancelActionExecutor _orderCancelActionExecutor;

	public MockOrderActionRequestor(MarketDatastore marketDatastore_,
			OrderDatastore orderDatastore_) {
		super();
		_orderSubmitActionExecutor = new OrderSubmitActionExecutor(marketDatastore_, orderDatastore_);
		_orderCancelActionExecutor = new OrderCancelActionExecutor(marketDatastore_, orderDatastore_);
	}
	
	@Override
	public void cancelOrder(OrderInternal orderInternal_,
			String cancellationCommentLanguageKey_,
			OrderManagementContext orderManagementContext_) {
		_orderCancelActionExecutor.cancelOrder(orderInternal_.getKey(), cancellationCommentLanguageKey_, orderManagementContext_);
	}

	@Override
	public void submitOrder(OrderInternal orderInternal_,
			OrderManagementContext orderManagementContext_) {
		_orderSubmitActionExecutor.submitOrder(orderInternal_.getKey(), orderManagementContext_);
	}

}
