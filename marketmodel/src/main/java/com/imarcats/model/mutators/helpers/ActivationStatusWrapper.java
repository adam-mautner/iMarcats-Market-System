package com.imarcats.model.mutators.helpers;

import com.imarcats.model.types.ActivationStatus;
import com.imarcats.model.types.TransferableObject;

public class ActivationStatusWrapper implements TransferableObject {

	private ActivationStatus _value;

	public ActivationStatusWrapper(ActivationStatus value_) {
		super();
		_value = value_;
	}

	public ActivationStatusWrapper() {
		super();
	}

	public ActivationStatus getValue() {
		return _value;
	}

	public void setValue(ActivationStatus value_) {
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
		ActivationStatusWrapper other = (ActivationStatusWrapper) obj;
		if (_value != other._value)
			return false;
		return true;
	}
}
