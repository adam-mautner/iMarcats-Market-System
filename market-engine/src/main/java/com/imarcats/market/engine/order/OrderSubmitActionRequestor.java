package com.imarcats.market.engine.order;

import com.imarcats.internal.server.interfaces.order.OrderInternal;
import com.imarcats.internal.server.interfaces.order.OrderManagementContext;

public interface OrderSubmitActionRequestor {
	public void submitOrder(final OrderInternal orderInternal, OrderManagementContext orderManagementContext_);
}
