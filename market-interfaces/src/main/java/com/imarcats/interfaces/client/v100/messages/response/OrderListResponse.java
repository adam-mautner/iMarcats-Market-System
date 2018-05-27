package com.imarcats.interfaces.client.v100.messages.response;

import com.imarcats.interfaces.client.v100.dto.types.PagedOrderListDto;
import com.imarcats.interfaces.client.v100.messages.MessageBase;

/**
 * Response with an Order List
 * @author Adam
 *
 */
public class OrderListResponse extends MessageBase {

	private static final long serialVersionUID = 1L;

	private PagedOrderListDto _orderList;

	public void setOrderList(PagedOrderListDto orderList) {
		_orderList = orderList;
	}

	public PagedOrderListDto getOrderList() {
		return _orderList;
	}
	
}
