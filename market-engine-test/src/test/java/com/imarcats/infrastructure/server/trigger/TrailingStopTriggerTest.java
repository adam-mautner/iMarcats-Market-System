package com.imarcats.infrastructure.server.trigger;

import com.imarcats.interfaces.client.v100.dto.types.MarketDataType;
import com.imarcats.interfaces.client.v100.dto.types.QuoteAndSizeDto;
import com.imarcats.interfaces.client.v100.dto.types.QuoteDto;
import com.imarcats.interfaces.client.v100.exception.ExceptionLanguageKeys;
import com.imarcats.interfaces.client.v100.exception.MarketRuntimeException;
import com.imarcats.interfaces.client.v100.notification.MarketDataChange;
import com.imarcats.interfaces.server.v100.dto.mapping.MarketDtoMapping;
import com.imarcats.internal.server.infrastructure.marketdata.MarketDataSessionImpl;
import com.imarcats.internal.server.infrastructure.notification.properties.PropertyChangeSessionImpl;
import com.imarcats.internal.server.infrastructure.notification.trades.TradeNotificationSessionImpl;
import com.imarcats.internal.server.infrastructure.trigger.TrailingStopLossTrigger;
import com.imarcats.internal.server.interfaces.market.MarketInternal;
import com.imarcats.internal.server.interfaces.order.OrderManagementContext;
import com.imarcats.internal.server.interfaces.order.OrderManagementContextImpl;
import com.imarcats.market.engine.market.MarketImpl;
import com.imarcats.market.engine.testutils.MockDatastores;
import com.imarcats.market.engine.testutils.OrderCompareTestCaseBase;
import com.imarcats.model.Market;
import com.imarcats.model.Order;
import com.imarcats.model.OrderPropertyNames;
import com.imarcats.model.test.testutils.TestOrderFlowGenerator;
import com.imarcats.model.types.ActivationStatus;
import com.imarcats.model.types.DoubleProperty;
import com.imarcats.model.types.OrderSide;
import com.imarcats.model.types.OrderState;
import com.imarcats.model.types.OrderTriggerInstruction;
import com.imarcats.model.types.Quote;
import com.imarcats.model.types.QuoteAndSize;
import com.imarcats.model.types.QuoteType;

public class TrailingStopTriggerTest extends OrderCompareTestCaseBase {

	private MockDatastores _datastore;
	private Market _market;
	private MockMarketDataSource _marketDataSource;
	private MarketDataSessionImpl _marketDataSession;
	private MockPropertyChangeBroker _propertyChangeBroker;
	private PropertyChangeSessionImpl _propertyChangeSession;
	private TradeNotificationSessionImpl _tradeNotificationSessionImpl;
	private OrderManagementContext _orderManagementContext;
	
    @Override
    public void setUp() throws Exception {
        super.setUp();
        
        _datastore = new MockDatastores();
        
		_market = getMarket(_datastore);
		_market.setActivationStatus(ActivationStatus.Activated);
		
		_marketDataSource = new MockMarketDataSource(_datastore);
		_marketDataSession = new MarketDataSessionImpl(_marketDataSource); 
		_propertyChangeBroker = new MockPropertyChangeBroker(_datastore);
		_propertyChangeSession = new PropertyChangeSessionImpl(_propertyChangeBroker);
		_tradeNotificationSessionImpl = new TradeNotificationSessionImpl(new MockTradeNotificationBroker());
		
		_orderManagementContext = new OrderManagementContextImpl(_marketDataSession, _propertyChangeSession, _tradeNotificationSessionImpl);
    }
	
	public void testIncompleteStopOrder() throws Exception {
		TestOrderFlowGenerator gen = new TestOrderFlowGenerator(QuoteType.Price, _market);
		Order[] orders = gen.getOrders();
		
		orders[0].setTriggerInstruction(OrderTriggerInstruction.TrailingStopLoss);
		
		MockTimeTrigger trigger = new MockTimeTrigger(_datastore);
		MarketInternal marketImpl = new MarketImpl(_market, trigger, null, null, null, null);
		
		try {
			(new TrailingStopLossTrigger(orders[0].getKey())).initTrigger(wrapOrder(orders[0], _datastore), marketImpl, _orderManagementContext);
			fail();
		} catch (MarketRuntimeException e) {
			// expected
			assertEquals(ExceptionLanguageKeys.STOP_QUOTE_DIFFERENCE_IS_NOT_DEFINED_ON_STOP_ORDER, e.getLanguageKey());
		}
	}
	
	public void testStopForPrice() throws Exception {
		stopTest(QuoteType.Price);
	}
	
	public void testStopForYield() throws Exception {
		stopTest(QuoteType.Yield);
	}
	
	private void stopTest(QuoteType quoteType_) throws InterruptedException {
		MockListenerContext context = 
			new MockListenerContext(_datastore, _marketDataSession, _propertyChangeSession, _tradeNotificationSessionImpl);
		
		_market.setQuoteType(quoteType_);

		TestOrderFlowGenerator gen = new TestOrderFlowGenerator(quoteType_, _market);
		Order[] orders = gen.getOrders();
		
		for (Order order : orders) {
			_datastore.createOrder(order);
		}
		
		orders[0].setTriggerInstruction(OrderTriggerInstruction.TrailingStopLoss);
		DoubleProperty property = new DoubleProperty();
		property.setName(OrderPropertyNames.STOP_QUOTE_DIFFERENCE_PROPERTY_NAME);
		property.setValue(5.0);
		
		orders[0].addTriggerProperty(property);
		orders[0].setSide(OrderSide.Buy);
		
		MockTimeTrigger trigger = new MockTimeTrigger(_datastore);;
		MarketInternal marketImpl = new MarketImpl(_market, trigger, null, null, null, null);
		
		TrailingStopLossTrigger stpTrigger = new TrailingStopLossTrigger(orders[0].getKey());
		stpTrigger.initTrigger(wrapOrder(orders[0], _datastore), marketImpl, _orderManagementContext);
		
		assertEquals(false, orders[0].getCurrentStopQuote().getValidQuote());
		
		QuoteDto quote = new QuoteDto();
		quote.setQuoteValue(getExpectedQuote(19, quoteType_).getQuoteValue());
		quote.setValidQuote(true);
		QuoteAndSizeDto quoteAndSize = new QuoteAndSizeDto();
		quoteAndSize.setQuote(quote);
		
		marketImpl.recordLastTrade(MarketDtoMapping.INSTANCE.fromDto(quoteAndSize), context.getMarketDataSession());
		
		stpTrigger = new TrailingStopLossTrigger(orders[0].getKey());
		stpTrigger.initTrigger(wrapOrder(orders[0], _datastore), marketImpl, _orderManagementContext);
		
		assertEquals(true, orders[0].getCurrentStopQuote().getValidQuote());
		assertEquals(getExpectedQuote(24, quoteType_).getQuoteValue(), orders[0].getCurrentStopQuote().getQuoteValue());		
		
		// model submit operation
		orders[0].setState(OrderState.WaitingSubmit);
		
		stpTrigger.marketDataChanged(new MarketDataChange(_market.getMarketCode(), MarketDataType.Last, quoteAndSize, false, null, null), context);
		
		assertEquals(OrderState.WaitingSubmit, orders[0].getState());
		
		quote.setQuoteValue(getExpectedQuote(24, quoteType_).getQuoteValue());
		
		stpTrigger.marketDataChanged(new MarketDataChange(_market.getMarketCode(), MarketDataType.Last, quoteAndSize, false, null, null), context);
		
		assertEquals(OrderState.Submitted, orders[0].getState());
		
		orders[1].setTriggerInstruction(OrderTriggerInstruction.TrailingStopLoss);
	    property = new DoubleProperty();
		property.setName(OrderPropertyNames.STOP_QUOTE_DIFFERENCE_PROPERTY_NAME);
		property.setValue(5.0);
		
		orders[1].addTriggerProperty(property);
		orders[1].setSide(OrderSide.Sell);
		
		// need to re-record last trade (since it is copied in the model)
		marketImpl.recordLastTrade(MarketDtoMapping.INSTANCE.fromDto(quoteAndSize), context.getMarketDataSession());
		
	    stpTrigger = new TrailingStopLossTrigger(orders[1].getKey());
	    stpTrigger.initTrigger(wrapOrder(orders[1], _datastore), marketImpl, _orderManagementContext);
		
		assertEquals(true, orders[1].getCurrentStopQuote().getValidQuote());
		assertEquals(getExpectedQuote(19, quoteType_).getQuoteValue(), orders[1].getCurrentStopQuote().getQuoteValue());
	    
		// model submit operation
		orders[1].setState(OrderState.WaitingSubmit);

		stpTrigger.marketDataChanged(new MarketDataChange(_market.getMarketCode(), MarketDataType.Last, quoteAndSize, false, null, null), context);
		
		assertEquals(OrderState.WaitingSubmit, orders[1].getState());
		
		quote.setQuoteValue(getExpectedQuote(19, quoteType_).getQuoteValue());
		
		stpTrigger.marketDataChanged(new MarketDataChange(_market.getMarketCode(), MarketDataType.Last, quoteAndSize, false, null, null), context);
		
		assertEquals(OrderState.Submitted, orders[1].getState()); 
	}
}
