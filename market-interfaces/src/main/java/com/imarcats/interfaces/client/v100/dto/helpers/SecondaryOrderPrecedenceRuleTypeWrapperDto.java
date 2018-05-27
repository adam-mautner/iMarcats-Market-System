package com.imarcats.interfaces.client.v100.dto.helpers;

import com.imarcats.interfaces.client.v100.dto.types.TransferableObjectDto;
import com.imarcats.interfaces.client.v100.dto.types.SecondaryOrderPrecedenceRuleType;

public class SecondaryOrderPrecedenceRuleTypeWrapperDto implements TransferableObjectDto {
	private SecondaryOrderPrecedenceRuleType _value;

	public SecondaryOrderPrecedenceRuleTypeWrapperDto() {
		super();
	}

	public SecondaryOrderPrecedenceRuleTypeWrapperDto(SecondaryOrderPrecedenceRuleType value_) {
		super();
		_value = value_;
	}

	public SecondaryOrderPrecedenceRuleType getValue() {
		return _value;
	}

	public void setValue(SecondaryOrderPrecedenceRuleType value_) {
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
		SecondaryOrderPrecedenceRuleTypeWrapperDto other = (SecondaryOrderPrecedenceRuleTypeWrapperDto) obj;
		if (_value != other._value)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "SecondaryOrderPrecedenceRuleTypeWrapperDto [_value=" + _value
				+ "]";
	}
	
}
