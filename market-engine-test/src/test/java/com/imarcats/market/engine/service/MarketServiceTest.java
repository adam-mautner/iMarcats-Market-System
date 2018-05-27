package com.imarcats.market.engine.service;

import com.imarcats.infrastructure.server.trigger.MockMarketDataSource;
import com.imarcats.infrastructure.server.trigger.MockPropertyChangeBroker;
import com.imarcats.infrastructure.server.trigger.MockTradeNotificationBroker;
import com.imarcats.interfaces.client.v100.dto.types.MarketDataType;
import com.imarcats.interfaces.client.v100.exception.ExceptionLanguageKeys;
import com.imarcats.interfaces.client.v100.exception.MarketRuntimeException;
import com.imarcats.interfaces.client.v100.notification.MarketDataChange;
import com.imarcats.internal.server.infrastructure.marketdata.MarketDataSessionImpl;
import com.imarcats.internal.server.infrastructure.notification.properties.PropertyChangeSessionImpl;
import com.imarcats.internal.server.infrastructure.notification.trades.TradeNotificationSessionImpl;
import com.imarcats.internal.server.interfaces.order.OrderManagementContextImpl;
import com.imarcats.market.engine.order.MockOrderActionRequestor;
import com.imarcats.market.engine.order.OrderManagementSystem;
import com.imarcats.market.engine.testutils.MockDatastores;
import com.imarcats.market.engine.testutils.MockMarketDataSessionImpl;
import com.imarcats.market.engine.testutils.OrderCompareTestCaseBase;
import com.imarcats.model.Market;
import com.imarcats.model.Order;
import com.imarcats.model.OrderBookEntryModel;
import com.imarcats.model.OrderBookModel;
import com.imarcats.model.test.testutils.TestOrderFlowGenerator;
import com.imarcats.model.types.ActivationStatus;
import com.imarcats.model.types.OrderSide;
import com.imarcats.model.types.OrderType;
import com.imarcats.model.types.Quote;
import com.imarcats.model.types.QuoteType;

public class MarketServiceTest extends OrderCompareTestCaseBase {
	
	private static final String BENS_SESSION = "Ben";
	
	public void testOrderBook() throws Exception {
		MockDatastores datastore = new MockDatastores();
		MockMarketDataSource marketDataSource = new MockMarketDataSource(datastore);
		MarketDataSessionImpl marketDataSession = new MockMarketDataSessionImpl(marketDataSource); 
		MockPropertyChangeBroker propertyChangeBroker = new MockPropertyChangeBroker(datastore);
		PropertyChangeSessionImpl propertyChangeSessionImpl = new PropertyChangeSessionImpl(propertyChangeBroker);
		TradeNotificationSessionImpl tradeNotificationSessionImpl = new TradeNotificationSessionImpl(new MockTradeNotificationBroker());
		
		OrderManagementContextImpl context = 
			new OrderManagementContextImpl(marketDataSession, propertyChangeSessionImpl, tradeNotificationSessionImpl);
		MockOrderActionRequestor mockOrderActionExecutor = new MockOrderActionRequestor(datastore, datastore);
		OrderManagementSystem orderManagementSystem = new OrderManagementSystem(datastore, datastore, datastore, mockOrderActionExecutor, mockOrderActionExecutor);
		
		String session = BENS_SESSION;
		
		MarketService marketService = new MarketService(datastore, orderManagementSystem);
		
		// test error case
		try {
			marketService.getOrderBookFor(MockDatastores.MARKET_ID, null);
			fail();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.ORDER_SIDE_CANNOT_BE_IDENTIFIED, e.getLanguageKey());
				
		}
		
		// test empty book 
		OrderBookModel emptyBook = orderFromDto(marketService.getOrderBookFor(MockDatastores.MARKET_ID, com.imarcats.interfaces.client.v100.dto.types.OrderSide.Buy));
		assertEquals(0, emptyBook.size());
		assertEquals(OrderSide.Buy, emptyBook.getSide());
		
		Market marketModel = datastore.findMarketBy(MockDatastores.MARKET_ID).getMarketModel();
		marketModel.setActivationStatus(ActivationStatus.Activated);
		marketModel.setAllowHiddenOrders(true);
		
		TestOrderFlowGenerator generator = new TestOrderFlowGenerator(QuoteType.Price, marketModel);
		Order[] orders = generator.getOrders();
		Order order = orders[0];
		
		order.setKey(null);
		order.setSubmitterID(null);
		order.setSubmissionDate(null);
		order.setTargetMarketCode(null);
		
		order.setType(OrderType.Limit);
		order.setSide(OrderSide.Buy);
		order.setSize(10); 
		order.setLimitQuoteValue(Quote.createQuote(10));
		
		Order order2 = orders[1];		
		
		order2.setKey(null);
		order2.setSubmitterID(null);
		order2.setSubmissionDate(null);
		order2.setTargetMarketCode(null);

		order2.setType(OrderType.Limit);
		order2.setSide(OrderSide.Buy);
		order2.setSize(10); 
		order2.setLimitQuoteValue(Quote.createQuote(10));

				
		Long orderKey = marketService.createOrder(MockDatastores.MARKET_ID, orderToDto(order), context, session);
		Long orderKey2 = marketService.createOrder(MockDatastores.MARKET_ID, orderToDto(order2), context, session);
		
		context = 
			new OrderManagementContextImpl(marketDataSession, propertyChangeSessionImpl, tradeNotificationSessionImpl);
		
		((MockMarketDataSessionImpl) context.getMarketDataSession()).rollback();
		marketService.submitOrder(orderKey, context, session);
		
		MarketDataChange[] marketDataChanges = filterMarketData(((MockMarketDataSessionImpl) context.getMarketDataSession()).getMarketDataChanges(), MarketDataType.Bid);
		
		assertEquals(1, marketDataChanges.length);
		checkMarketDataChange(QuoteType.Price, marketModel.getMarketCode(), marketDataChanges[0], MarketDataType.Bid, 10.0, 10, false);
		
		marketService.submitOrder(orderKey2, context, session);

		OrderBookModel book = orderFromDto(marketService.getOrderBookFor(MockDatastores.MARKET_ID, com.imarcats.interfaces.client.v100.dto.types.OrderSide.Buy));
		assertEquals(1, book.size());
		
		assertEquals(0, book.get(0).orderKeyCount());
		assertEquals(20, getBookTopEntry(book).getAggregateSize().intValue());
		assertEquals(10.0, getBookTopEntry(book).getLimitQuote().getQuoteValue());
		assertEquals(false, getBookTopEntry(book).getHasHiddenOrders().booleanValue());
		assertEquals(OrderType.Limit, getBookTopEntry(book).getOrderType());
		assertEquals(OrderSide.Buy, getBookTopEntry(book).getOrderSide());
		
		// test non-displayed order
		Order order3 = orders[2];		
		
		order3.setKey(null);
		order3.setSubmitterID(null);
		order3.setSubmissionDate(null);
		order3.setTargetMarketCode(null);

		order3.setType(OrderType.Limit);
		order3.setSide(OrderSide.Buy);
		order3.setSize(10); 
		order3.setLimitQuoteValue(Quote.createQuote(10));
		order3.setDisplayOrder(false);
		
		Long orderKey3 = marketService.createOrder(MockDatastores.MARKET_ID, orderToDto(order3), context, session);
		
		context = 
			new OrderManagementContextImpl(marketDataSession, propertyChangeSessionImpl, tradeNotificationSessionImpl);
		((MockMarketDataSessionImpl) context.getMarketDataSession()).rollback();
		
		marketService.submitOrder(orderKey3, context, session);
		
		marketDataChanges = filterMarketData(((MockMarketDataSessionImpl) context.getMarketDataSession()).getMarketDataChanges(), MarketDataType.Bid);
		
		assertEquals(0, marketDataChanges.length);
		
		// market does not send notification for Hidden Order added to the book, unless it changes the bis/ask 
		// but the fact that there are Hidden Orders will be included to the notification
//		assertEquals(1, marketDataChanges.length);
//		checkMarketDataChange(QuoteType.Price, marketModel.getMarketCode(), marketDataChanges[0], MarketDataType.Bid, 10.0, 20, true);
		
		book = orderFromDto(marketService.getOrderBookFor(MockDatastores.MARKET_ID, com.imarcats.interfaces.client.v100.dto.types.OrderSide.Buy));
		assertEquals(1, book.size());
		
		assertEquals(0, book.get(0).orderKeyCount());
		assertEquals(20, getBookTopEntry(book).getAggregateSize().intValue());
		assertEquals(10.0, getBookTopEntry(book).getLimitQuote().getQuoteValue());
		assertEquals(true, getBookTopEntry(book).getHasHiddenOrders().booleanValue());
		assertEquals(OrderType.Limit, getBookTopEntry(book).getOrderType());
		assertEquals(OrderSide.Buy, getBookTopEntry(book).getOrderSide());
		
		// test market order 
		Order order4 = orders[3];		
		
		order4.setKey(null);
		order4.setSubmitterID(null);
		order4.setSubmissionDate(null);
		order4.setTargetMarketCode(null);

		order4.setType(OrderType.Limit);
		order4.setMinimumSizeOfExecution(-1);
		order4.setSide(OrderSide.Buy);
		order4.setSize(5); 
		order4.setLimitQuoteValue(Quote.createQuote(11));

		Order order5 = orders[4];		
		
		order5.setKey(null);
		order5.setSubmitterID(null);
		order5.setSubmissionDate(null);
		order5.setTargetMarketCode(null);

		order5.setType(OrderType.Limit);
		order5.setSide(OrderSide.Sell);
		order5.setSize(10); 
		order5.setLimitQuoteValue(Quote.createQuote(10));
		order5.setDisplayOrder(false);

		Long orderKey5 = marketService.createOrder(MockDatastores.MARKET_ID, orderToDto(order5), context, session);
		Long orderKey6 = marketService.createOrder(MockDatastores.MARKET_ID, orderToDto(order4), context, session);

		context = 
			new OrderManagementContextImpl(marketDataSession, propertyChangeSessionImpl, tradeNotificationSessionImpl);
		
		marketService.submitOrder(orderKey5, context, session);
		marketService.submitOrder(orderKey6, context, session);

		book = orderFromDto(marketService.getOrderBookFor(MockDatastores.MARKET_ID, com.imarcats.interfaces.client.v100.dto.types.OrderSide.Buy));
		assertEquals(2, book.size());

		assertEquals(0, book.get(0).orderKeyCount());
		assertEquals(5, getBookTopEntry(book).getAggregateSize().intValue());
		assertEquals(11.0, getBookTopEntry(book).getLimitQuote().getQuoteValue());
		assertEquals(OrderType.Limit, getBookTopEntry(book).getOrderType());
		assertEquals(OrderSide.Buy, getBookTopEntry(book).getOrderSide());
		
		assertEquals(0, book.get(1).orderKeyCount());
		assertEquals(20, getBookSecondTopEntry(book).getAggregateSize().intValue());
		assertEquals(10.0, getBookSecondTopEntry(book).getLimitQuote().getQuoteValue());
		assertEquals(OrderType.Limit, getBookSecondTopEntry(book).getOrderType());
		assertEquals(OrderSide.Buy, getBookSecondTopEntry(book).getOrderSide());
		
	}

	private OrderBookEntryModel getBookSecondTopEntry(OrderBookModel book) {
		return book.get(1);
	}

	private OrderBookEntryModel getBookTopEntry(OrderBookModel book) {
		return book.get(0);
	}
}
