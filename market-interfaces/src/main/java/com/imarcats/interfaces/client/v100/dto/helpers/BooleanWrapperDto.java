package com.imarcats.interfaces.client.v100.dto.helpers;

import com.imarcats.interfaces.client.v100.dto.types.TransferableObjectDto;

public class BooleanWrapperDto implements TransferableObjectDto {

	private Boolean _value;

	public BooleanWrapperDto(Boolean value_) {
		super();
		_value = value_;
	}

	public BooleanWrapperDto() {
		super();
	}

	public Boolean getValue() {
		return _value;
	}

	public void setValue(Boolean value_) {
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
		BooleanWrapperDto other = (BooleanWrapperDto) obj;
		if (_value == null) {
			if (other._value != null)
				return false;
		} else if (!_value.equals(other._value))
			return false;
		return true;
	}	
}
