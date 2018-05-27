package com.imarcats.model.mutators.helpers;

import com.imarcats.model.types.TransferableObject;
import com.imarcats.model.types.SecondaryOrderPrecedenceRuleType;

public class SecondaryOrderPrecedenceRuleTypeWrapper implements TransferableObject {

	private SecondaryOrderPrecedenceRuleType _value;

	public SecondaryOrderPrecedenceRuleTypeWrapper() {
		super();
	}

	public SecondaryOrderPrecedenceRuleTypeWrapper(SecondaryOrderPrecedenceRuleType value_) {
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
		SecondaryOrderPrecedenceRuleTypeWrapper other = (SecondaryOrderPrecedenceRuleTypeWrapper) obj;
		if (_value != other._value)
			return false;
		return true;
	}
	
	
	
}
