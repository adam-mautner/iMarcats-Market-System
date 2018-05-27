package com.imarcats.model.mutators.helpers;

import com.imarcats.model.types.ExecutionSystem;
import com.imarcats.model.types.TransferableObject;

public class ExecutionSystemWrapper implements TransferableObject {

	private ExecutionSystem _value;

	public ExecutionSystemWrapper(ExecutionSystem value_) {
		super();
		_value = value_;
	}

	public ExecutionSystemWrapper() {
		super();
	}

	public ExecutionSystem getValue() {
		return _value;
	}

	public void setValue(ExecutionSystem value_) {
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
		ExecutionSystemWrapper other = (ExecutionSystemWrapper) obj;
		if (_value != other._value)
			return false;
		return true;
	}
	
	
	
}
