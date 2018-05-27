package com.imarcats.model.mutators.helpers;

import com.imarcats.model.types.TransferableObject;
import com.imarcats.model.types.OrderSide;

public class OrderSideWrapper implements TransferableObject {

	private OrderSide _value;

	public OrderSideWrapper(OrderSide value_) {
		super();
		_value = value_;
	}

	public OrderSideWrapper() {
		super();
	}

	public OrderSide getValue() {
		return _value;
	}

	public void setValue(OrderSide value_) {
		_value = value_;
	}
	
	@Override
	public Object getObjectValue() {
		return getValue();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((_value == null) ? 0 : _value.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OrderSideWrapper other = (OrderSideWrapper) obj;
		if (_value != other._value)
			return false;
		return true;
	}
	
	
	
}
