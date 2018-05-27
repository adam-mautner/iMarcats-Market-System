package com.imarcats.interfaces.client.v100.dto.types;

import java.util.ArrayList;
import java.util.List;

/**
 * Defines the Circuit Breaker of the Market. Circuit Breaker prevents 
 * Orders placed too far from the Market being submitted to Market.
 * 
 * Also Circuit Breaker halts trading, if the Market moved too much from the Opening Price.
 * @author Adam
 */
public class CircuitBreakerDto implements TransferableObjectDto {
	
	/**
	 * List of HaltRule objects, they define HaltRule of the Market. Each consecutive 
	 * HaltRule needs to have bigger Move Amount and longer Halt Period.
	 * 
	 * More Halt Rules can be only be defined, if the Enable Halt Rule Escalation is true. 
	 * Optional
	 */
    private List<HaltRuleDto> _haltRules = new ArrayList<HaltRuleDto>();
	
	/**
	 * Defines the Maximum Amount the best Market Quote can be improved compared to 
	 * the Last Quote, or -1 if not defined, anyway Oder is handled as defined in 
	 * Order Reject Action 
	 * 
	 * Optional
	 */
	private Double _maximumQuoteImprovement = -1.0;
	
	/**
	 * Defines, what should happen with Orders that Improve the Market Quote too much - 
	 * Value from OrderRejectAction
	 * 
	 * Optional
	 */
	private OrderRejectAction _orderRejectAction = OrderRejectAction.RejectAutomatically;
	
	public List<HaltRuleDto> getHaltRules() {
		return _haltRules;
	}

	public void setHaltRules(List<HaltRuleDto> haltRules_) {
		_haltRules = haltRules_;
	}

	public double getMaximumQuoteImprovement() {
		return _maximumQuoteImprovement != null ? _maximumQuoteImprovement : -1;
	}

	public void setMaximumQuoteImprovement(double maximumQuoteImprovement_) {
		_maximumQuoteImprovement = maximumQuoteImprovement_;
	}

	public OrderRejectAction getOrderRejectAction() {
		return _orderRejectAction;
	}

	public void setOrderRejectAction(OrderRejectAction orderRejectAction_) {
		_orderRejectAction = orderRejectAction_;
	}

	@Override
	public Object getObjectValue() {
		return this;
	}
}
