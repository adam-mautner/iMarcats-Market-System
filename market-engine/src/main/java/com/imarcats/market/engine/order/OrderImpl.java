package com.imarcats.market.engine.order;

import java.util.Date;

import com.imarcats.interfaces.client.v100.dto.types.MarketDataType;
import com.imarcats.interfaces.client.v100.i18n.MarketSystemMessageLanguageKeys;
import com.imarcats.interfaces.client.v100.notification.ChangeOrigin;
import com.imarcats.internal.server.infrastructure.datastore.MarketDatastore;
import com.imarcats.internal.server.infrastructure.timer.TimerAction;
import com.imarcats.internal.server.infrastructure.timer.TimerUtils;
import com.imarcats.internal.server.infrastructure.trigger.OrderExpirationAction;
import com.imarcats.internal.server.infrastructure.trigger.QuoteChangeTriggerBase;
import com.imarcats.internal.server.infrastructure.trigger.QuoteChangeTriggerFactory;
import com.imarcats.internal.server.interfaces.market.MarketInternal;
import com.imarcats.internal.server.interfaces.order.OrderInternal;
import com.imarcats.internal.server.interfaces.order.OrderManagementContext;
import com.imarcats.model.Order;
import com.imarcats.model.OrderPropertyNames;
import com.imarcats.model.types.AuditInformation;
import com.imarcats.model.types.Property;
import com.imarcats.model.types.OrderExpirationInstruction;
import com.imarcats.model.types.OrderSide;
import com.imarcats.model.types.OrderState;
import com.imarcats.model.types.OrderTriggerInstruction;
import com.imarcats.model.types.OrderType;
import com.imarcats.model.types.Quote;
import com.imarcats.model.types.TradeSide;
import com.imarcats.model.utils.PropertyUtils;

/**
 * Implementation of the Order Functionality
 * @author Adam
 */
public class OrderImpl implements OrderInternal {

	// TODO: Make this shorter 
	public static final long CANCEL_DELAY_AFTER_MARKET_MAINTENANCE_IN_MILLIS = 11 * 60 * 1000; // 11 mins
	
	private final Order _orderModel;
	private final MarketDatastore _marketDatastore;

	public OrderImpl(Order orderModel_, MarketDatastore marketDatastore_) {
		super();
		_orderModel = orderModel_;
		_marketDatastore = marketDatastore_;
	}

	@Override
	public Long getKey() {
		return _orderModel.getKey();
	}
	
	
	
	@Override
	public String getCancellationCommentLanguageKey() {
		return _orderModel.getCancellationCommentLanguageKey();
	}

	@Override
	public AuditInformation getCreationAudit() {
		return _orderModel.getCreationAudit();
	}

	@Override
	public OrderExpirationInstruction getExpirationInstruction() {
		return _orderModel.getExpirationInstruction();
	}

	@Override
	public Property[] getExpirationProperties() {
		Property[] properties = new Property[0];
		if(_orderModel.getExpirationProperties() != null) {
			properties = _orderModel.getExpirationProperties();
		}
		return properties;
	}

	@Override
	public Long getExpirationTriggerActionKey() {
		return _orderModel.getExpirationTriggerActionKey();
	}

	@Override
	public void setExpirationTriggerActionKey(Long timeTriggerActionKey_) {
		_orderModel.setExpirationTriggerActionKey(timeTriggerActionKey_);
	}

	@Override
	public Quote getLimitQuoteValue() {
		return _orderModel.getLimitQuoteValue();
	}

	@Override
	public OrderSide getSide() {
		return _orderModel.getSide();
	}

	@Override
	public int getSize() {
		return _orderModel.getSize();
	}

	@Override
	public boolean getExecuteEntireOrderAtOnce() {
		return _orderModel.getExecuteEntireOrderAtOnce();
	}

	@Override
	public int getMinimumSizeOfExecution() {
		return _orderModel.getMinimumSizeOfExecution();
	}

	@Override
	public boolean getDisplayOrder() {
		return _orderModel.getDisplayOrder();
	}
	
	@Override
	public int getRemainingSize() {
		return _orderModel.calculateRemainingSize();
	}

	@Override
	public int getExecutedSize() {
		return _orderModel.getExecutedSize();
	}
	
	@Override
	public OrderState getState() {
		return _orderModel.getState();
	}

	@Override
	public Quote getCurrentStopQuote() {
		return _orderModel.getCurrentStopQuote();
	}
	
	@Override
	public void recordNewStopQuote(Quote newStopQuote_) {
		_orderModel.setCurrentStopQuote(newStopQuote_);
	}

	@Override
	public Date getSubmissionDate() {
		return _orderModel.getSubmissionDate();
	}

	@Override
	public MarketInternal getTargetMarket() {
		return _marketDatastore.findMarketBy(_orderModel.getTargetMarketCode());
	}

	@Override
	public OrderTriggerInstruction getTriggerInstruction() {
		return _orderModel.getTriggerInstruction();
	}

	@Override
	public Property[] getTriggerProperties() {
		Property[] properties = new Property[0];
		if(_orderModel.getTriggerProperties() != null) {
			properties = _orderModel.getTriggerProperties();
		}
		return properties;
	}
	
	public Property[] getOrderProperties() {
		Property[] properties = new Property[0];
		if(_orderModel.getOrderProperties() != null) {
			properties = _orderModel.getOrderProperties();
		}
		return properties;
	}
	
	public String getExternalOrderReference() {
		return _orderModel.getExternalOrderReference();
	}
	
	@Override
	public OrderType getType() {
		return _orderModel.getType();
	}

	@Override
	public Order getOrderModel() {
		return _orderModel;
	}

	@Override
	public String getSubmitterID() {
		return _orderModel.getSubmitterID();
	}

	@Override
	public void recordCancel(String cancellationCommentLanguageKey_, OrderManagementContext orderManagementContext_) {
		recordCancellationCommentLanguageKeyChange(cancellationCommentLanguageKey_, orderManagementContext_);
		
		_orderModel.setCommissionCharged(false);

		recordStateChange(OrderState.Canceled, 
				MarketSystemMessageLanguageKeys.ORDER_CANCELED_BY_USER.equals(cancellationCommentLanguageKey_) 
					? ChangeOrigin.User 
					: ChangeOrigin.System,
				orderManagementContext_);
	}

	@Override
	public void recordExecution(MarketInternal targetMarket_, int executedSide_, OrderManagementContext orderManagementContext_) {
		recordExecutedSizeChange(getExecutedSize() + executedSide_, orderManagementContext_);
		 if(_orderModel.getMinimumSizeOfExecution() > 0) {			
			// execution deletes the minimum size of execution, so user can set a new one for the rest of the order
			recordMinimumSizeOfExecutionChange(0, orderManagementContext_);
		 }
		if(getRemainingSize() == 0) {
			recordStateChange(OrderState.Executed, ChangeOrigin.System, orderManagementContext_);
			
			// remove triggers from executed order
			
			// this is just a safety measure, quote change trigger is removed at submission time
			removeQuoteChangeTrigger(targetMarket_, orderManagementContext_);
			
			// this is needed 
			removeExpirationTrigger(targetMarket_, orderManagementContext_);
		}
	}

	private void recordExecutedSizeChange(int executedSize_,
			OrderManagementContext orderManagementContext_) {
		_orderModel.setExecutedSize(executedSize_);
		notifyAboutExecutedSizeChange(orderManagementContext_);
	}

	private void recordMinimumSizeOfExecutionChange(int minimumSizeOfExecution_,
			OrderManagementContext orderManagementContext_) {
		_orderModel.setMinimumSizeOfExecution(minimumSizeOfExecution_);
		notifyAboutMinimumSizeChange(orderManagementContext_);
	}

	@Override
	public void recordSubmit(OrderManagementContext orderManagementContext_) {		
		recordCancellationCommentLanguageKeyChange(null, orderManagementContext_);
		OrderState previousState = _orderModel.getState();
		recordStateChange(
				OrderState.Submitted,
				previousState == OrderState.WaitingSubmit
					? ChangeOrigin.System
					: ChangeOrigin.User, 
				orderManagementContext_);
	}

	@Override
	public void recordPendingSubmit(OrderManagementContext orderManagementContext_) {		
		recordStateChange(
				OrderState.PendingSubmit,
				ChangeOrigin.User, 
				orderManagementContext_);
	}
	
	private void recordCancellationCommentLanguageKeyChange(
			String cancellationCommentLanguageKey_,
			OrderManagementContext orderManagementContext_) {
		_orderModel.setCancellationCommentLanguageKey(cancellationCommentLanguageKey_);
		notifyAboutCancellationComment(orderManagementContext_);
	}

	private void recordStateChange(
			OrderState state_, ChangeOrigin changeOrigin_,
			OrderManagementContext orderManagementContext_) {
		_orderModel.setState(state_);
		orderManagementContext_.notifyOrderStateChange(getState(), _orderModel, changeOrigin_);
	}

	@Override
	public void recordSubmitWaiting(OrderManagementContext orderManagementContext_) {
		recordStateChange(
				OrderState.WaitingSubmit, 
				ChangeOrigin.System, 
				orderManagementContext_);
	}

	public void setQuoteChangeTriggerKey(Long quoteChangeTriggerKey_) {
		_orderModel.setQuoteChangeTriggerKey(quoteChangeTriggerKey_);
	}

	private void notifyAboutMinimumSizeChange(OrderManagementContext orderManagementContext_) {
		Property property = PropertyUtils.createIntProperty(OrderPropertyNames.MINIMUM_SIZE_OF_EXECUTION_PROPERTY, getMinimumSizeOfExecution());
		orderManagementContext_.notifyAboutOrderPropertyChange(property, _orderModel);
	}
	
	private void notifyAboutExecutedSizeChange(OrderManagementContext orderManagementContext_) {
		Property property = PropertyUtils.createIntProperty(OrderPropertyNames.EXECUTED_SIZE_PROPERTY, getExecutedSize());
		orderManagementContext_.notifyAboutOrderPropertyChange(property, _orderModel);
	}

	private void notifyAboutCancellationComment(OrderManagementContext orderManagementContext_) {
		Property property = PropertyUtils.createStringProperty(OrderPropertyNames.CANCELLATION_COMMENT_LANGUAGE_KEY_PROPERTY, getCancellationCommentLanguageKey());
		orderManagementContext_.notifyAboutOrderPropertyChange(property, _orderModel);
	}
	
	public Long getQuoteChangeTriggerKey() {
		return _orderModel.getQuoteChangeTriggerKey();
	}
	
	@Override
	public TradeSide getTradeSide(Date tradeDateTime_) {
		return new TradeSide(
					getSubmitterID(), 
					getType(), 
					getSide(), 
					getLimitQuoteValue(), 
					getTargetMarket().getMarketModel().getInstrumentCode(),
					getOrderModel().getTargetMarketCode(), 
					tradeDateTime_, 
					getTargetMarket().getInstrument().getContractSize(), 
					getCommission(), 
					getTargetMarket().getCommissionCurrency(),
					PropertyUtils.clonePropertyList(getOrderProperties()), 
					getExternalOrderReference());
	}

	private double getCommission() {
		double commission = 0;
		if(_orderModel.getCommissionCharged() == null || _orderModel.getCommissionCharged() == Boolean.FALSE) {
			commission = getTargetMarket().getCommission();
			
			_orderModel.setCommissionCharged(true);
		}
		return commission;
	}
	
	@Override
	public OrderInternal addQuoteChangeTrigger(MarketInternal targetMarket_,
			OrderManagementContext orderManagementContext_) {
		QuoteChangeTriggerBase quoteTrigger = QuoteChangeTriggerFactory.getQuoteChangeTrigger(this);
		quoteTrigger.initTrigger(this, targetMarket_, orderManagementContext_);
		
		if(!quoteTrigger.isTriggered(this)) {
			Long listenerKey = 
				orderManagementContext_.getMarketDataSession().addMarketDataChangeListener(targetMarket_.getMarketCode(), 
					MarketDataType.Last, quoteTrigger);
			setQuoteChangeTriggerKey(listenerKey);
			recordSubmitWaiting(orderManagementContext_);
		}
		
		return this;
	}
	
	@Override
	public OrderInternal removeQuoteChangeTrigger(MarketInternal targetMarket_,
			OrderManagementContext orderManagementContext_) {
		Long quoteChangeTriggerKey = getQuoteChangeTriggerKey();
		if(quoteChangeTriggerKey != null) {			
			orderManagementContext_.getMarketDataSession().removeMarketDataChangeListener(
					quoteChangeTriggerKey);
			setQuoteChangeTriggerKey(null);
		}
		
		return this;
	}
	
	@Override
	public OrderInternal addExpirationTrigger(MarketInternal targetMarket_,
			final OrderManagementContext orderManagementContext_) {
		if(getState() != OrderState.Executed && 
		   getExpirationInstruction() == OrderExpirationInstruction.DayOrder) {
			TimerAction cancelAction = 
				new OrderExpirationAction(getOrderModel().getKey(), 
						targetMarket_.getMarketCode());
			
			Long triggerActionKey = targetMarket_.getTimeTrigger().scheduleToTime(
					new Date(), 
					TimerUtils.addToDate(targetMarket_.getTradingDayEnd(), CANCEL_DELAY_AFTER_MARKET_MAINTENANCE_IN_MILLIS), null,
					targetMarket_.getBusinessCalendar(), true, cancelAction);
			
			setExpirationTriggerActionKey(triggerActionKey);
		}
		
		return this;
	}

	@Override
	public OrderInternal removeExpirationTrigger(MarketInternal targetMarket_, OrderManagementContext context_) {
		if(getExpirationInstruction() == OrderExpirationInstruction.DayOrder) {
			targetMarket_.getTimeTrigger().cancel(getExpirationTriggerActionKey());
		}
		setExpirationTriggerActionKey(null);
		
		return this;
	}
	
	/**
	 * @return String used in Debug and Exceptions 
	 */
	@Override
	public String toString() {
		return _orderModel.toString();
	}
}
