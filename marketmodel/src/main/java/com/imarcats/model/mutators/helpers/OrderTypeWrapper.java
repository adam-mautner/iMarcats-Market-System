package com.imarcats.model.mutators.helpers;

import com.imarcats.model.types.TransferableObject;
import com.imarcats.model.types.OrderType;

public class OrderTypeWrapper implements TransferableObject {

	private OrderType _value;

	public OrderTypeWrapper(OrderType value_) {
		super();
		_value = value_;
	}

	public OrderTypeWrapper() {
		super();
	}

	public OrderType getValue() {
		return _value;
	}

	public void setValue(OrderType value_) {
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
		OrderTypeWrapper other = (OrderTypeWrapper) obj;
		if (_value != other._value)
			return false;
		return true;
	}
	
	
	
}
