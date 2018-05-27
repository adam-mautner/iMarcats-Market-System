package com.imarcats.interfaces.client.v100.messages.response;

import com.imarcats.interfaces.client.v100.dto.OrderDto;
import com.imarcats.interfaces.client.v100.messages.MessageBase;

public class OrderResponse extends MessageBase {

	private static final long serialVersionUID = 1L;

	private OrderDto _order;

	public void setOrder(OrderDto order_) {
		_order = order_;
	}

	public OrderDto getOrder() {
		return _order;
	}

}
