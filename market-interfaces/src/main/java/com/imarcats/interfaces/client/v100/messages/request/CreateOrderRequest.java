package com.imarcats.interfaces.client.v100.messages.request;

import com.imarcats.interfaces.client.v100.dto.OrderDto;
import com.imarcats.interfaces.client.v100.messages.MessageBase;

/**
 * Create Order Request 
 * @author Adam
 *
 */
public class CreateOrderRequest extends MessageBase {

	private static final long serialVersionUID = 1L;

	private String _marketCode;
	private OrderDto _order;
	
	public String getMarketCode() {
		return _marketCode;
	}
	public void setMarketCode(String marketCode_) {
		_marketCode = marketCode_;
	}
	public OrderDto getOrder() {
		return _order;
	}
	public void setOrder(OrderDto order_) {
		_order = order_;
	}	
}
