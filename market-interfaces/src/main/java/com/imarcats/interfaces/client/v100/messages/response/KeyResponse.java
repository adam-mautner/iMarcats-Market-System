package com.imarcats.interfaces.client.v100.messages.response;

import com.imarcats.interfaces.client.v100.dto.types.DatastoreKeyDto;
import com.imarcats.interfaces.client.v100.messages.MessageBase;
/**
 * Response with a Key Value 
 * @author Adam
 *
 */
public class KeyResponse extends MessageBase {

	private static final long serialVersionUID = 1L;

	private DatastoreKeyDto _key;

	public void setKey(DatastoreKeyDto key) {
		_key = key;
	}

	public DatastoreKeyDto getKey() {
		return _key;
	}
	
}
