package com.imarcats.internal.server.infrastructure.trigger;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.imarcats.interfaces.client.v100.i18n.MarketSystemMessageLanguageKeys;
import com.imarcats.internal.server.infrastructure.notification.ListenerCallContext;
import com.imarcats.internal.server.infrastructure.notification.ListenerCallParameters;
import com.imarcats.internal.server.infrastructure.timer.TimerAction;
import com.imarcats.internal.server.interfaces.market.MarketInternal;
import com.imarcats.internal.server.interfaces.order.OrderInternal;
import com.imarcats.model.meta.DataLengths;
/**
 * Cancels Expired Orders, when the time comes
 * @author Adam
 */
@Entity
@Table(name="ORDER_EXPIRATION_ACTION")
public class OrderExpirationAction extends TimerAction {

	/**
	 * Key of the Order to be canceled
	 */
	@Column(name="ORDER_KEY", nullable=false)
	private Long _orderKey;	

	/**
	 * Code of the Target Market
	 */
	@Column(name="TRARGET_MARKET_CODE", nullable=false, length=DataLengths.MARKET_OBJECT_CODE_LENGTH)
	private String _targetMarketCode;

    /**
     * Constructor needed for Deserialization
     */
	public OrderExpirationAction() {
		super();
	}

	public OrderExpirationAction(Long orderKey_, String targetMarketCode_) {
		super();
		_orderKey = orderKey_;
		_targetMarketCode = targetMarketCode_;
	}

	@Override
	public void execute(Date triggerTime_, Date scheduledCallTime_, ListenerCallParameters listenerParameters_, ListenerCallContext listenerContext_) {
		MarketInternal targetMarket = getMarket(_targetMarketCode, listenerContext_);
		OrderInternal orderInternal = getOrder(_orderKey, listenerContext_);

		targetMarket.cancel(orderInternal, MarketSystemMessageLanguageKeys.ORDER_EXPIRED, listenerContext_.getOrderManagementContext());		
	}

	public Long getOrderKey() {
		return _orderKey;
	}

	public void setOrderKey(Long orderKey_) {
		_orderKey = orderKey_;
	}

	public String getTargetMarketCode() {
		return _targetMarketCode;
	}

	public void setTargetMarketCode(String targetMarketCode_) {
		_targetMarketCode = targetMarketCode_;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((_orderKey == null) ? 0 : _orderKey.hashCode());
		result = prime
				* result
				+ ((_targetMarketCode == null) ? 0 : _targetMarketCode
						.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		OrderExpirationAction other = (OrderExpirationAction) obj;
		if (_orderKey == null) {
			if (other._orderKey != null)
				return false;
		} else if (!_orderKey.equals(other._orderKey))
			return false;
		if (_targetMarketCode == null) {
			if (other._targetMarketCode != null)
				return false;
		} else if (!_targetMarketCode.equals(other._targetMarketCode))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "OrderExpirationAction [_orderKey=" + _orderKey
				+ ", _targetMarketCode=" + _targetMarketCode + "]";
	}
	
	
}
