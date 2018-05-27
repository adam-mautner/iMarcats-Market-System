package com.imarcats.interfaces.server.v100.validation;

import java.util.HashMap;
import java.util.Map;

import com.imarcats.interfaces.client.v100.exception.MarketRuntimeException;
import com.imarcats.interfaces.server.v100.validation.InterfaceValidatorUtils.ITimeZoneListProvider;
import com.imarcats.model.Market;
import com.imarcats.model.Order;
import com.imarcats.model.OrderPropertyNames;
import com.imarcats.model.types.Property;
import com.imarcats.model.types.OrderTriggerInstruction;
import com.imarcats.model.types.QuoteType;
import com.imarcats.model.utils.PropertyUtils;

public class QuoteChangeTriggerValidatorRegistry {
	
	// TODO: It is OK to use empty TimeZone list here as there is no Time Property used here, 
	//		 when Time Properties are used here, we need to fix this  
	private static final ITimeZoneListProvider TIME_ZONE_LIST_PROVIDER = new ITimeZoneListProvider() {
		@Override
		public String[] getAvailableIDs() {
			return new String[0];
		}
	};

	private static final Map<OrderTriggerInstruction, QuoteChangeTriggerValidator> MAP_INSTRUCTION_TYPE_TO_VALIDATOR = new HashMap<OrderTriggerInstruction, QuoteChangeTriggerValidator>();
	
	private static final QuoteChangeTriggerValidator DEFAULT_VALIDATOR = new QuoteChangeTriggerValidator() {
		@Override
		public void validate(Order order_, Market market_) {
			validateProperties(order_.getTriggerProperties());
		}
	};
	private static final QuoteChangeTriggerValidator STOP_LOSS_VALIDATOR = new QuoteChangeTriggerValidator() {
		@Override
		public void validate(Order order_, Market market_) {
			validateProperties(order_.getTriggerProperties());
			double propertyValue = 
				PropertyUtils.getDoublePropertyValue(
					order_.getTriggerProperties(), 
					OrderPropertyNames.STOP_QUOTE_PROPERTY_NAME,
					MarketRuntimeException.createExceptionWithDetails(
							MarketRuntimeException.STOP_QUOTE_IS_NOT_DEFINED_ON_STOP_ORDER, null, 
							new Object[]{ order_, market_ }));
			
		   if(market_.getQuoteType() == QuoteType.Price && 
			  propertyValue <= 0) {
				throw MarketRuntimeException.createExceptionWithDetails(
						MarketRuntimeException.STOP_PRICE_MUST_BE_POSITIVE, 
						null, new Object[] { order_, market_ });
		   }
		   
		   if(market_.getQuoteType() == QuoteType.Yield && 
			  propertyValue >= 100) {
				throw MarketRuntimeException.createExceptionWithDetails(
						MarketRuntimeException.STOP_YIELD_MUST_BE_LESS_THAN_100, 
						null, new Object[] { order_, market_ });
		   }
		}
	};
	private static final QuoteChangeTriggerValidator TRAILING_STOP_LOSS_VALIDATOR = new QuoteChangeTriggerValidator() {
		@Override
		public void validate(Order order_, Market market_) {
			validateProperties(order_.getTriggerProperties());
			double propertyValue = 
				PropertyUtils.getDoublePropertyValue(
					order_.getTriggerProperties(), 
					OrderPropertyNames.STOP_QUOTE_DIFFERENCE_PROPERTY_NAME, 
					MarketRuntimeException.createExceptionWithDetails(
							MarketRuntimeException.STOP_QUOTE_DIFFERENCE_IS_NOT_DEFINED_ON_STOP_ORDER, null, 
							new Object[]{ order_, market_ }));
			
			   if(propertyValue <= 0) {
					throw MarketRuntimeException.createExceptionWithDetails(
							MarketRuntimeException.STOP_QUOTE_DIFFERENCE_MUST_BE_POSITIVE, 
							null, new Object[] { order_, market_ });
			   }
		}
	};
	
	
	static {
		MAP_INSTRUCTION_TYPE_TO_VALIDATOR.put(OrderTriggerInstruction.Immediate, DEFAULT_VALIDATOR);
		MAP_INSTRUCTION_TYPE_TO_VALIDATOR.put(OrderTriggerInstruction.StopLoss, STOP_LOSS_VALIDATOR);
		MAP_INSTRUCTION_TYPE_TO_VALIDATOR.put(OrderTriggerInstruction.TrailingStopLoss, TRAILING_STOP_LOSS_VALIDATOR);
	}
	
	private QuoteChangeTriggerValidatorRegistry() { /* static class */ }
	
	/**
	 * Gets Validator for Trigger Instruction
	 * @param triggerInstruction_ 
	 * @return Validator
	 */
	public static QuoteChangeTriggerValidator getValidator(OrderTriggerInstruction triggerInstruction_) {
		QuoteChangeTriggerValidator validator = MAP_INSTRUCTION_TYPE_TO_VALIDATOR.get(triggerInstruction_);
		if(validator == null) {
			throw MarketRuntimeException.createExceptionWithDetails(
					MarketRuntimeException.UNSUPPORTED_QUOTE_CHANGE_TRIGGER, null, 
					new Object[] { triggerInstruction_ });
		}
		
		return validator;
	}
	
	private static void validateProperties(Property[] properties_) {
		InterfaceValidatorUtils.validateProperties(properties_, TIME_ZONE_LIST_PROVIDER);
	}
}
