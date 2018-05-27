package com.imarcats.market.engine.matching;

import java.util.Date;

import com.imarcats.interfaces.server.v100.validation.OrderValidator;
import com.imarcats.internal.server.infrastructure.datastore.MarketDatastore;
import com.imarcats.internal.server.infrastructure.datastore.OrderDatastore;
import com.imarcats.internal.server.interfaces.market.MarketInternal;
import com.imarcats.internal.server.interfaces.order.OrderInternal;
import com.imarcats.internal.server.interfaces.order.OrderManagementContext;
import com.imarcats.model.OrderPropertyNames;
import com.imarcats.model.types.Property;
import com.imarcats.model.types.OrderTriggerInstruction;
import com.imarcats.model.utils.PropertyUtils;

public class OrderSubmitActionExecutor extends OrderActionExecutorBase {

	private final MarketDatastore _marketDatastore;
	private final OrderDatastore _orderDatastore;
	
	public OrderSubmitActionExecutor(MarketDatastore marketDatastore_,
			OrderDatastore orderDatastore_) {
		super();
		_marketDatastore = marketDatastore_;
		_orderDatastore = orderDatastore_;
	}

	public void submitOrder(Long orderKey_, OrderManagementContext orderManagementContext_) {
		OrderInternal orderInternal = _orderDatastore.findOrderBy(orderKey_);
		checkOrder(orderKey_, orderInternal);
		String marketCode = orderInternal.getOrderModel().getTargetMarketCode();
		MarketInternal targetMarket = _marketDatastore.findMarketBy(marketCode); 
		checkMarket(marketCode, targetMarket);
		
		submitOrder(orderInternal, targetMarket, orderManagementContext_);
	}
	
	private void submitOrder(final OrderInternal orderInternal, final MarketInternal targetMarket, OrderManagementContext orderManagementContext_) {
		OrderValidator.validateOrderForSubmit(orderInternal.getOrderModel(), targetMarket.getMarketModel());
		
		orderInternal.getOrderModel().updateLastUpdateTimestamp();

		// Lock instruments and/or cash at clearing agent - if needed (Pre-Trade Lock)

		
		Date submissionDate = new Date();
		if(orderInternal.getTriggerInstruction() == OrderTriggerInstruction.Immediate) {
			// set submission date - but notification will be sent later as submission date may be deleted, if order cannot be submitted
			orderInternal.getOrderModel().setSubmissionDate(submissionDate);
			
			try {
				targetMarket.submit(orderInternal, orderManagementContext_);
			} catch(RuntimeException e) {
				// delete submission date as the order cannot be submitted 
				orderInternal.getOrderModel().setSubmissionDate(null);
			
				throw e;
			}
			
			// send the submission date notification here 
			orderInternal.getOrderModel().setSubmissionDate(submissionDate); // TODO: Remove, not needed as setup above 
			notifyAboutOrderSubmissionDateChange(
					orderManagementContext_, 
					orderInternal);
			
		} else {
			// set submission date
			orderInternal.getOrderModel().setSubmissionDate(submissionDate);
			
			notifyAboutOrderSubmissionDateChange(
					orderManagementContext_, 
					orderInternal);
			
			// add triggers
			orderInternal.
				addQuoteChangeTrigger(targetMarket, orderManagementContext_).
					addExpirationTrigger(targetMarket, orderManagementContext_);
		}
	} 
	
	private void notifyAboutOrderSubmissionDateChange(
			OrderManagementContext orderManagementContext_,
			OrderInternal order_) {
		Property property = 
			PropertyUtils.createDateProperty(
					OrderPropertyNames.SUBMISSION_DATE_PROPERTY, 
					order_.getOrderModel().getSubmissionDate());
		orderManagementContext_.notifyAboutOrderPropertyChange(property, order_.getOrderModel());
	}
}
