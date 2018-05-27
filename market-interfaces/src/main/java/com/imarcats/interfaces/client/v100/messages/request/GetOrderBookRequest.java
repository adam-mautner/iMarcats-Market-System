package com.imarcats.interfaces.client.v100.messages.request;

import com.imarcats.interfaces.client.v100.dto.types.OrderSide;


/**
 * Gets Order Book for a Market and Side 
 * @author Adam
 *
 */
public class GetOrderBookRequest extends MarketCodeRequestBase {

	private static final long serialVersionUID = 1L;

	private OrderSide _orderSide;

	public void setOrderSide(OrderSide orderSide) {
		_orderSide = orderSide;
	}

	public OrderSide getOrderSide() {
		return _orderSide;
	}
}
