package com.imarcats.interfaces.client.v100.dto.types;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.imarcats.interfaces.client.v100.dto.MarketModelObjectDto;

public interface TransferableObjectDto extends MarketModelObjectDto {
	@JsonIgnore
	public Object getObjectValue();
}
