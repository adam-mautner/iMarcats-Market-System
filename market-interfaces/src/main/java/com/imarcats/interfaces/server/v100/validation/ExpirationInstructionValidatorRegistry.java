package com.imarcats.interfaces.server.v100.validation;

import java.util.HashMap;
import java.util.Map;

import com.imarcats.interfaces.client.v100.exception.MarketRuntimeException;
import com.imarcats.interfaces.server.v100.validation.InterfaceValidatorUtils.ITimeZoneListProvider;
import com.imarcats.model.Market;
import com.imarcats.model.Order;
import com.imarcats.model.types.Property;
import com.imarcats.model.types.OrderExpirationInstruction;

/**
 * Registry of Expiration Instruction Validator
 * @author Adam
 */
public class ExpirationInstructionValidatorRegistry {

	// TODO: It is OK to use empty TimeZone list here as there is no Time Property used here, 
	//		 when Time Properties are used here, we need to fix this  
	private static final ITimeZoneListProvider TIME_ZONE_LIST_PROVIDER = new ITimeZoneListProvider() {
		@Override
		public String[] getAvailableIDs() {
			return new String[0];
		}
	};
	
	private static final Map<OrderExpirationInstruction, ExpirationInstructionValidator> MAP_INSTRUCTION_TYPE_TO_VALIDATOR = new HashMap<OrderExpirationInstruction, ExpirationInstructionValidator>();

	private static final ExpirationInstructionValidator DEFAULT_VALIDATOR = new ExpirationInstructionValidator() {
		@Override
		public void validate(Order order_, Market market_) {
			validateProperties(order_.getTriggerProperties());
		}
	};
	
	static {
		MAP_INSTRUCTION_TYPE_TO_VALIDATOR.put(OrderExpirationInstruction.DayOrder, DEFAULT_VALIDATOR);
		MAP_INSTRUCTION_TYPE_TO_VALIDATOR.put(OrderExpirationInstruction.GoodTillCancel, DEFAULT_VALIDATOR);
		MAP_INSTRUCTION_TYPE_TO_VALIDATOR.put(OrderExpirationInstruction.ImmediateOrCancel, DEFAULT_VALIDATOR);
	}
	
	private ExpirationInstructionValidatorRegistry() { /* static class */ }
	
	/**
	 * Gets Validator for Expiration Instruction
	 * @param expirationInstruction_ 
	 * @return Validator
	 */
	public static ExpirationInstructionValidator getValidator(OrderExpirationInstruction expirationInstruction_) {
		ExpirationInstructionValidator validator = MAP_INSTRUCTION_TYPE_TO_VALIDATOR.get(expirationInstruction_);
		if(validator == null) {
			if(validator == null) {
				throw MarketRuntimeException.createExceptionWithDetails(
						MarketRuntimeException.UNSUPPORTED_ORDER_EXPIRATION_INSTRUCTION, null, 
						new Object[] { expirationInstruction_ });
			}
		}
		
		return validator;
	}
	
	private static void validateProperties(Property[] properties_) {
		InterfaceValidatorUtils.validateProperties(properties_, TIME_ZONE_LIST_PROVIDER);
	}
}
