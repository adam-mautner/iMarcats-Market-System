package com.imarcats.model.mutators.helpers;

import com.imarcats.model.types.TransferableObject;

public class LongWrapper implements TransferableObject {

	private Long _value;

	public LongWrapper(Long value_) {
		super();
		_value = value_;
	}

	public LongWrapper() {
		super();
	}

	public Long getValue() {
		return _value;
	}

	public void setValue(Long value_) {
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
		LongWrapper other = (LongWrapper) obj;
		if (_value == null) {
			if (other._value != null)
				return false;
		} else if (!_value.equals(other._value))
			return false;
		return true;
	}
	
	
	
}
