package com.imarcats.market.engine.matching;

import com.imarcats.interfaces.client.v100.exception.MarketRuntimeException;
import com.imarcats.internal.server.interfaces.market.MarketInternal;
import com.imarcats.internal.server.interfaces.order.OrderInternal;

public class OrderActionExecutorBase {

	protected void checkOrder(Long orderKey_, OrderInternal orderInternal) {
		if(orderInternal == null) {
			throw MarketRuntimeException.createExceptionWithDetails(MarketRuntimeException.NON_EXISTENT_ORDER, 
					null, new Object[] { "Order ID=" + orderKey_ });
		}
	}
	
	protected void checkMarket(String marketCode_, MarketInternal targetMarket) {
		if(targetMarket == null) {
			throw MarketRuntimeException.createExceptionWithDetails(MarketRuntimeException.NON_EXISTENT_MARKET, 
					null, new Object[] { "Market ID=" + marketCode_ });
		}
	}
}
