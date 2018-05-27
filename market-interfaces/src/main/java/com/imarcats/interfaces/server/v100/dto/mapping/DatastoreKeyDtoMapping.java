package com.imarcats.interfaces.server.v100.dto.mapping;

import com.imarcats.interfaces.client.v100.dto.types.DatastoreKeyDto;
import com.imarcats.model.types.DatastoreKey;

public class DatastoreKeyDtoMapping extends DtoMappingBase {

	public static DatastoreKeyDtoMapping INSTANCE = new DatastoreKeyDtoMapping();
	
	private DatastoreKeyDtoMapping() {
		super();
	}
	
	public DatastoreKeyDto toDto(DatastoreKey key_) {
		if(key_ == null) {
			return null;
		}
		return _mapper.map(key_, DatastoreKeyDto.class);
	}
	
	public DatastoreKey fromDto(DatastoreKeyDto key_) {
		if(key_ == null) {
			return null;
		}
		return _mapper.map(key_, DatastoreKey.class);
	}
}
