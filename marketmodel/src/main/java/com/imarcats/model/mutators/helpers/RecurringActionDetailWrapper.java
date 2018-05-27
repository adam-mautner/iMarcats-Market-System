package com.imarcats.model.mutators.helpers;

import com.imarcats.model.types.TransferableObject;
import com.imarcats.model.types.RecurringActionDetail;

public class RecurringActionDetailWrapper implements TransferableObject {

	private RecurringActionDetail _value;

	public RecurringActionDetailWrapper(RecurringActionDetail value_) {
		super();
		_value = value_;
	}

	public RecurringActionDetailWrapper() {
		super();
	}

	public RecurringActionDetail getValue() {
		return _value;
	}

	public void setValue(RecurringActionDetail value_) {
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
		RecurringActionDetailWrapper other = (RecurringActionDetailWrapper) obj;
		if (_value != other._value)
			return false;
		return true;
	}
	
}
