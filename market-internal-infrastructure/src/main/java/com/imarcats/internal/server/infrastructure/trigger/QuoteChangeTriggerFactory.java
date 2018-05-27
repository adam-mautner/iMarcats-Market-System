package com.imarcats.internal.server.infrastructure.trigger;

import com.imarcats.internal.server.interfaces.order.OrderInternal;
import com.imarcats.model.types.OrderTriggerInstruction;

/**
 * List of Available Quote Change Triggers
 * @author Adam
 */
public class QuoteChangeTriggerFactory {
	
	private QuoteChangeTriggerFactory() { /* static factory class */ }

	public static QuoteChangeTriggerBase getQuoteChangeTrigger(OrderInternal order_) {
		QuoteChangeTriggerBase trigger = null;
		OrderTriggerInstruction triggerType = order_.getTriggerInstruction();
		if(triggerType == OrderTriggerInstruction.StopLoss) {
			trigger = new StopLossTrigger(order_.getOrderModel().getKey());
		} else if(triggerType == OrderTriggerInstruction.TrailingStopLoss) {
			trigger = new TrailingStopLossTrigger(order_.getOrderModel().getKey());
		}
		
		if(trigger == null) {
			throw new IllegalArgumentException("No Trigger can be found for " + triggerType + " Trigger type");
		}
		
		return trigger;
	}
}
