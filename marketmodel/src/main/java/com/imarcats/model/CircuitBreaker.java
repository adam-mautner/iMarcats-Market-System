package com.imarcats.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.imarcats.model.types.OrderRejectAction;
import com.imarcats.model.types.TransferableObject;

/**
 * Defines the Circuit Breaker of the Market. Circuit Breaker prevents 
 * Orders placed too far from the Market being submitted to Market.
 * 
 * Also Circuit Breaker halts trading, if the Market moved too much from the Opening Price.
 * @author Adam
 */
@Entity
@Table(name="CIRCUIT_BREAKER")
public class CircuitBreaker implements MarketModelObject, TransferableObject {

	private static final long serialVersionUID = 1L;

	/**
	 * Primary Key of the CircuitBreaker 
	 * Note: No need for accessing this field directly
	 */	
	@Id
	@GeneratedValue 
	@Column(name="ID")
    private Long _id;
	
	/**
	 * List of HaltRule objects, they define HaltRule of the Market. Each consecutive 
	 * HaltRule needs to have bigger Move Amount and longer Halt Period.
	 * 
	 * More Halt Rules can be only be defined, if the Enable Halt Rule Escalation is true. 
	 * Optional
	 */
	// TODO: We need to keep the original order of the entries here, as sorting them requires a complex logic
//	@Column(name="HALT_RULE")
	@OneToMany(cascade=CascadeType.ALL, orphanRemoval=true) 
	@JoinColumn(name = "CIRCUIT_BREAKER_ID", nullable = false) 
    private List<HaltRule> _haltRules = new ArrayList<HaltRule>();
	
	/**
	 * Defines the Maximum Amount the best Market Quote can be improved compared to 
	 * the Last Quote, or -1 if not defined, anyway Oder is handled as defined in 
	 * Order Reject Action 
	 * 
	 * Optional
	 */
	@Column(name="MAXIMUM_QUOTE_IMPROVEMENT")
	private Double _maximumQuoteImprovement = -1.0;
	
	/**
	 * Defines, what should happen with Orders that Improve the Market Quote too much - 
	 * Value from OrderRejectAction
	 * 
	 * Optional
	 */
	@Column(name="ORDER_REJECT_ACTION")
	@Enumerated(EnumType.STRING) 
	private OrderRejectAction _orderRejectAction = OrderRejectAction.RejectAutomatically;

	public static CircuitBreaker create(CircuitBreaker circuitBreaker_) {
		CircuitBreaker newCircuitBreaker = new CircuitBreaker();
		
		newCircuitBreaker.setMaximumQuoteImprovement(circuitBreaker_.getMaximumQuoteImprovement());
		newCircuitBreaker.setOrderRejectAction(circuitBreaker_.getOrderRejectAction());
		
		List<HaltRule> newHaltRules = new ArrayList<HaltRule>();
		for (HaltRule haltRule : circuitBreaker_.getHaltRules()) {
			newHaltRules.add(HaltRule.create(haltRule));
		}
		newCircuitBreaker.setHaltRules(newHaltRules);
		
		return newCircuitBreaker;
	}
	
	public List<HaltRule> getHaltRules() {
		return _haltRules;
	}

	public void setHaltRules(List<HaltRule> haltRules_) {
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
	
	@Override
	public String toString() {
		return "CircuitBreaker [_id=" + _id + ", _haltRules=" + _haltRules
				+ ", _maximumQuoteImprovement=" + _maximumQuoteImprovement
				+ ", _orderRejectAction=" + _orderRejectAction + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((_haltRules == null) ? 0 : _haltRules.hashCode());
		result = prime * result + ((_id == null) ? 0 : _id.hashCode());
		result = prime
				* result
				+ ((_maximumQuoteImprovement == null) ? 0
						: _maximumQuoteImprovement.hashCode());
		result = prime
				* result
				+ ((_orderRejectAction == null) ? 0 : _orderRejectAction
						.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CircuitBreaker other = (CircuitBreaker) obj;
		if (_haltRules == null) {
			if (other._haltRules != null)
				return false;
		} else if (!_haltRules.equals(other._haltRules))
			return false;
		if (_id == null) {
			if (other._id != null)
				return false;
		} else if (!_id.equals(other._id))
			return false;
		if (_maximumQuoteImprovement == null) {
			if (other._maximumQuoteImprovement != null)
				return false;
		} else if (!_maximumQuoteImprovement
				.equals(other._maximumQuoteImprovement))
			return false;
		if (_orderRejectAction != other._orderRejectAction)
			return false;
		return true;
	}
}
