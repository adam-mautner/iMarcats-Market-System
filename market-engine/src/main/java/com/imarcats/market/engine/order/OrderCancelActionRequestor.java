package com.imarcats.market.engine.order;

import com.imarcats.internal.server.interfaces.order.OrderInternal;
import com.imarcats.internal.server.interfaces.order.OrderManagementContext;

public interface OrderCancelActionRequestor {
	public void cancelOrder(OrderInternal orderInternal, String cancellationCommentLanguageKey_, OrderManagementContext orderManagementContext_);
}
