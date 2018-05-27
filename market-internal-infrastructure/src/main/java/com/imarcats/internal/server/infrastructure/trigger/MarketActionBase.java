package com.imarcats.internal.server.infrastructure.trigger;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

import com.imarcats.internal.server.infrastructure.notification.ListenerCallContext;
import com.imarcats.internal.server.infrastructure.notification.ListenerCallParameters;
import com.imarcats.internal.server.infrastructure.timer.TimerAction;
import com.imarcats.internal.server.interfaces.market.MarketInternal;
import com.imarcats.internal.server.interfaces.order.OrderManagementContext;
import com.imarcats.internal.server.interfaces.order.OrderManagementContextImpl;
import com.imarcats.model.meta.DataLengths;

/**
 * Common Base Class for Market Actions 
 * @author Adam
 */
@Entity
@Table(name="MARKET_ACTION_BASE")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
public abstract class MarketActionBase extends TimerAction {

	/**
	 * Code of the Market
	 */
	@Column(name="MARKET_CODE", nullable=false, length=DataLengths.MARKET_OBJECT_CODE_LENGTH)
	private String _marketCode;

    /**
     * Constructor needed for Deserialization
     */
	public MarketActionBase() {
		super();
	}

	public MarketActionBase(String marketCode_) {
		super();
		_marketCode = marketCode_;
	}
	
	@Override
	public final void execute(Date triggerTime_, 
			Date scheduledCallTime_,
			ListenerCallParameters listenerParameters_,
			ListenerCallContext listenerContext_) {
		MarketInternal market = getMarket(_marketCode, listenerContext_);
		OrderManagementContext orderManagementContext = createOrderManagementContext(listenerContext_);
		executeMarketAction(market, orderManagementContext);
	}

	public String getMarketCode() {
		return _marketCode;
	}

	public void setMarketCode(String marketCode_) {
		_marketCode = marketCode_;
	}
	
	private OrderManagementContext createOrderManagementContext(
			ListenerCallContext listenerContext_) {
		return new OrderManagementContextImpl(
				listenerContext_.getMarketDataSession(),
				listenerContext_.getPropertyChangeSession(),
				listenerContext_.getTradeNotificationSession());
	}

	/**
	 * Executes action on Market 
	 * @param market_ Market 
	 * @param orderManagementContext_ Order Management Context 
	 */
	protected abstract void executeMarketAction(MarketInternal market_,
			OrderManagementContext orderManagementContext_);

	@Override
	public String toString() {
		return "MarketActionBase [_marketCode=" + _marketCode + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((_marketCode == null) ? 0 : _marketCode.hashCode());
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
		MarketActionBase other = (MarketActionBase) obj;
		if (_marketCode == null) {
			if (other._marketCode != null)
				return false;
		} else if (!_marketCode.equals(other._marketCode))
			return false;
		return true;
	}
	
	
}
