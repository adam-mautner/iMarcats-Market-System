package com.imarcats.interfaces.client.v100.messages.response;

import com.imarcats.interfaces.client.v100.dto.types.OrderBookModelDto;
import com.imarcats.interfaces.client.v100.messages.MessageBase;

public class OrderBookResponse extends MessageBase {

	private static final long serialVersionUID = 1L;

	private OrderBookModelDto _orderBookModel;

	public void setOrderBookModel(OrderBookModelDto orderBookModel) {
		_orderBookModel = orderBookModel;
	}

	public OrderBookModelDto getOrderBookModel() {
		return _orderBookModel;
	}
}
