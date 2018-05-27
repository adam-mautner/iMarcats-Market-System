package com.imarcats.interfaces.client.v100.dto.helpers;

import com.imarcats.interfaces.client.v100.dto.types.TransferableObjectDto;
import com.imarcats.interfaces.client.v100.dto.types.OrderType;

public class OrderTypeWrapperDto implements TransferableObjectDto {

	private OrderType _value;

	public OrderTypeWrapperDto(OrderType value_) {
		super();
		_value = value_;
	}

	public OrderTypeWrapperDto() {
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
		OrderTypeWrapperDto other = (OrderTypeWrapperDto) obj;
		if (_value != other._value)
			return false;
		return true;
	}
	
	
	
}
