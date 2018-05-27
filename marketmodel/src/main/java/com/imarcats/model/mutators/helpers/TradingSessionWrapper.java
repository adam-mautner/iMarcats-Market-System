package com.imarcats.model.mutators.helpers;

import com.imarcats.model.types.TransferableObject;
import com.imarcats.model.types.TradingSession;

public class TradingSessionWrapper implements TransferableObject {

	private TradingSession _value;

	public TradingSessionWrapper(TradingSession value_) {
		super();
		_value = value_;
	}

	public TradingSessionWrapper() {
		super();
	}

	public TradingSession getValue() {
		return _value;
	}

	public void setValue(TradingSession value_) {
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
		TradingSessionWrapper other = (TradingSessionWrapper) obj;
		if (_value != other._value)
			return false;
		return true;
	}
}
