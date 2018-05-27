package com.imarcats.interfaces.client.v100.dto.types;

import java.io.Serializable;
import java.util.Arrays;

import com.imarcats.interfaces.client.v100.dto.OrderDto;

/**
 * Paged list of Orders 
 * @author Adam
 */
public class PagedOrderListDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private OrderDto[] _orders;
	private String _cursorString;
	private int _maxNumberOfOrdersOnPage;
	
	public void setOrders(OrderDto[] orders) {
		_orders = orders;
	}

	public OrderDto[] getOrders() {
		return _orders;
	}

	public String getCursorString() {
		return _cursorString;
	}

	public void setCursorString(String cursorString_) {
		_cursorString = cursorString_;
	}

	public int getMaxNumberOfOrdersOnPage() {
		return _maxNumberOfOrdersOnPage;
	}

	public void setMaxNumberOfOrdersOnPage(
			int maxNumberOfOrdersOnPage_) {
		_maxNumberOfOrdersOnPage = maxNumberOfOrdersOnPage_;
	}

	@Override
	public String toString() {
		return "PagedOrderList [_cursorString=" + _cursorString
				+ ", _maxNumberOfOrdersOnPage=" + _maxNumberOfOrdersOnPage
				+ ", _orders=" + Arrays.toString(_orders) + "]";
	}
	
}
