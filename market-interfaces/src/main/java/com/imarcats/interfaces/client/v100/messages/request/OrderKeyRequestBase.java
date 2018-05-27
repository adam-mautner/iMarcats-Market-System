package com.imarcats.interfaces.client.v100.messages.request;

import com.imarcats.interfaces.client.v100.messages.MessageBase;

/**
 * Request with an Order Key
 * @author Adam
 *
 */
public abstract class OrderKeyRequestBase extends MessageBase {

	private static final long serialVersionUID = 1L;

	private Long _orderKey;

	public void setOrderKey(Long orderKey) {
		_orderKey = orderKey;
	}

	public Long getOrderKey() {
		return _orderKey;
	}
}
