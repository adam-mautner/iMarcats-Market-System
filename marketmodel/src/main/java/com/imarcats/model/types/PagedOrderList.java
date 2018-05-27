package com.imarcats.model.types;

import java.io.Serializable;
import java.util.Arrays;

import com.imarcats.model.Order;

/**
 * Paged list of Orders 
 * @author Adam
 */
public class PagedOrderList implements Serializable {

	private static final long serialVersionUID = 1L;

	private Order[] _orders;
	private String _cursorString;
	private int _maxNumberOfOrdersOnPage;
	
	public void setOrders(Order[] orders) {
		_orders = orders;
	}

	public Order[] getOrders() {
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
