package com.imarcats.interfaces.server.v100.validation;

import com.imarcats.interfaces.client.v100.exception.MarketRuntimeException;
import com.imarcats.model.Market;
import com.imarcats.model.Order;

/**
 * Validates the Expiration Instruction attached to an Order
 * @author Adam
 */
public interface ExpirationInstructionValidator {
	
	/**
	 * Validate the Expiration Instruction
	 * @param order_ Order to be validated
	 * @param market_ Traget Market
	 * @throws MarketRuntimeException if Order cannot be validated
	 */
	public void validate(Order order_, Market market_);
}
