package com.imarcats.interfaces.server.v100.dto.mapping;

import com.imarcats.interfaces.client.v100.dto.types.DatastoreKeyDto;
import com.imarcats.interfaces.server.v100.dto.mapping.DatastoreKeyDtoMapping;
import com.imarcats.model.test.testutils.MarketObjectTestBase;
import com.imarcats.model.types.DatastoreKey;

public class DatastoreKeyDtoMappingTest extends MarketObjectTestBase {
	
	public void testTypeRoundTripMapping() throws Exception {	
		DatastoreKey key = new DatastoreKey(10L);
		DatastoreKeyDto keyDto = DatastoreKeyDtoMapping.INSTANCE.toDto(key);
		DatastoreKey keyMapped = DatastoreKeyDtoMapping.INSTANCE.fromDto(keyDto);
		
		assertEquals(key.getIdKey(), keyMapped.getIdKey());
	}
}
