package com.imarcats.market.engine.testutils;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.imarcats.infrastructure.server.trigger.MockMarketDataSource;
import com.imarcats.infrastructure.server.trigger.MockPropertyChangeBroker;
import com.imarcats.interfaces.client.v100.dto.OrderDto;
import com.imarcats.interfaces.client.v100.dto.types.DatePropertyDto;
import com.imarcats.interfaces.client.v100.dto.types.DoublePropertyDto;
import com.imarcats.interfaces.client.v100.dto.types.IntPropertyDto;
import com.imarcats.interfaces.client.v100.dto.types.MarketDataType;
import com.imarcats.interfaces.client.v100.dto.types.ObjectPropertyDto;
import com.imarcats.interfaces.client.v100.dto.types.OrderBookModelDto;
import com.imarcats.interfaces.client.v100.dto.types.PropertyValueChangeDto;
import com.imarcats.interfaces.client.v100.dto.types.StringPropertyDto;
import com.imarcats.interfaces.client.v100.notification.ChangeOrigin;
import com.imarcats.interfaces.client.v100.notification.MarketDataChange;
import com.imarcats.interfaces.client.v100.notification.PropertyChanges;
import com.imarcats.interfaces.server.v100.dto.mapping.MarketDtoMapping;
import com.imarcats.interfaces.server.v100.dto.mapping.OrderDtoMapping;
import com.imarcats.internal.server.interfaces.market.MarketInternal;
import com.imarcats.internal.server.interfaces.order.OrderManagementContext;
import com.imarcats.internal.server.interfaces.order.OrderInternal;
import com.imarcats.internal.server.interfaces.order.OrderManagementContextImpl;
import com.imarcats.model.Market;
import com.imarcats.model.Order;
import com.imarcats.model.OrderBookEntryModel;
import com.imarcats.model.OrderBookModel;
import com.imarcats.model.OrderPropertyNames;
import com.imarcats.model.test.testutils.MarketObjectTestBase;
import com.imarcats.model.types.OrderSide;
import com.imarcats.model.types.OrderState;
import com.imarcats.model.types.Quote;
import com.imarcats.model.types.QuoteType;

/**
 * Common base for Order Compare Test Cases 
 * @author Adam
 */
public class OrderCompareTestCaseBase extends MarketObjectTestBase {
	
	protected void checkSingleIntProperty(OrderManagementContext context,
			String propertyName, long propertyValue) {
		PropertyValueChangeDto propertyValueChange = getSinglePropertyChange(context);
		checkIntProperty(propertyName, propertyValue, propertyValueChange);
	}

	private void checkIntProperty(String propertyName, long propertyValue,
			PropertyValueChangeDto propertyValueChange) {
		assertEquals(propertyName, propertyValueChange.getProperty().getName());
		assertEquals(propertyValue, ((IntPropertyDto) propertyValueChange.getProperty()).getValue());
	}

	protected void checkSingleObjectProperty(OrderManagementContext  context,
			String propertyName, Object propertyValue) {
		PropertyValueChangeDto propertyValueChange = getSinglePropertyChange(context);
		checkObjectProperty(propertyName, propertyValue, propertyValueChange);
	}
	
	protected void checkObjectProperty(String propertyName, Object propertyValue,
			PropertyValueChangeDto propertyValueChange) {
		assertEquals(propertyName, propertyValueChange.getProperty().getName());
		assertEquals(propertyValue.toString(), ((ObjectPropertyDto) propertyValueChange.getProperty()).getValue().getObjectValue().toString());
	}
	
	protected void checkSingleDoubleProperty(OrderManagementContext context,
			String propertyName, double propertyValue) {
		PropertyValueChangeDto propertyValueChange = getSinglePropertyChange(context);
		checkDoubleProperty(propertyName, propertyValue, propertyValueChange);
	}

	protected void checkDoubleProperty(String propertyName, double propertyValue,
			PropertyValueChangeDto propertyValueChange) {
		assertEquals(propertyName, propertyValueChange.getProperty().getName());
		assertEquals(propertyValue, ((DoublePropertyDto) propertyValueChange.getProperty()).getValue(), 0.000001);
	}
	
	protected void checkStringProperty(String propertyName, String propertyValue,
			PropertyValueChangeDto propertyValueChange) {
		assertEquals(propertyName, propertyValueChange.getProperty().getName());
		assertEquals(propertyValue, ((StringPropertyDto) propertyValueChange.getProperty()).getValue());
	}

	protected void checkDateProperty(String propertyName, Date propertyValue,
			PropertyValueChangeDto propertyValueChange) {
		assertEquals(propertyName, propertyValueChange.getProperty().getName());
		assertEquals(new java.sql.Date(propertyValue.getTime()), ((DatePropertyDto) propertyValueChange.getProperty()).getValue());
	}
	
	protected void checkIfNoPropertyChange(OrderManagementContext context) {
		MockPropertyChangeSessionImpl propertyChangeSession = (MockPropertyChangeSessionImpl) context.getPropertyChangeSession();
		PropertyChanges[] propertyChanges = propertyChangeSession.getPropertyChanges();
		assertEquals(0, propertyChanges.length);		
	}
	
	protected void checkStrictStateChangeWithCommentAndSubmissionDate(OrderManagementContext context_, OrderState orderState_, String cancellationComment_, Date submissionDate_, ChangeOrigin changeOrigin_) {
		boolean expectSubmitDate = submissionDate_ != null;
		if(expectSubmitDate) {
			assertEquals(4, ((MockPropertyChangeSessionImpl)((MockPropertyChangeSessionImpl)context_.getPropertyChangeSession())).getPropertyChanges().length);
		} else {
			assertEquals(2, ((MockPropertyChangeSessionImpl)context_.getPropertyChangeSession()).getPropertyChanges().length);
		}
		
		checkStateChangeWithCommentAndSubmissionDate(context_, orderState_,
				cancellationComment_, submissionDate_, changeOrigin_);
	}

	protected void checkStateChangeWithCommentAndSubmissionDate(
			OrderManagementContext context_, 
			OrderState orderState_,
			String cancellationComment_, 
			Date submissionDate_,
			ChangeOrigin changeOrigin_) {
		PropertyValueChangeDto propertyValueChange;
		
		boolean expectSubmitDate = submissionDate_ != null;
		int changeCnt = 0;
		
		if(expectSubmitDate) {			
			assertEquals(1, ((MockPropertyChangeSessionImpl)context_.getPropertyChangeSession()).getPropertyChanges()[changeCnt].getChanges().length);
			assertEquals(changeOrigin_, ((MockPropertyChangeSessionImpl)context_.getPropertyChangeSession()).getPropertyChanges()[changeCnt].getChangeOrigin());	
			propertyValueChange = getPropertyChange(changeCnt, 0, context_);
			checkObjectProperty(OrderPropertyNames.STATE_PROPERTY, OrderState.PendingSubmit, propertyValueChange);
			changeCnt++;
		}
		
		assertEquals(1, ((MockPropertyChangeSessionImpl)context_.getPropertyChangeSession()).getPropertyChanges()[changeCnt].getChanges().length);
		propertyValueChange = getPropertyChange(changeCnt, 0, context_);
		checkStringProperty(OrderPropertyNames.CANCELLATION_COMMENT_LANGUAGE_KEY_PROPERTY, cancellationComment_, propertyValueChange);
		changeCnt++;
		
		assertEquals(1, ((MockPropertyChangeSessionImpl)context_.getPropertyChangeSession()).getPropertyChanges()[changeCnt].getChanges().length);
		assertEquals(changeOrigin_, ((MockPropertyChangeSessionImpl)context_.getPropertyChangeSession()).getPropertyChanges()[changeCnt].getChangeOrigin());	
		propertyValueChange = getPropertyChange(changeCnt, 0, context_);
		checkObjectProperty(OrderPropertyNames.STATE_PROPERTY, orderState_, propertyValueChange);
		changeCnt++;

		if(expectSubmitDate) {		
			assertEquals(1, ((MockPropertyChangeSessionImpl)context_.getPropertyChangeSession()).getPropertyChanges()[changeCnt].getChanges().length);
			propertyValueChange = getPropertyChange(changeCnt, 0, context_);
			checkDateProperty(OrderPropertyNames.SUBMISSION_DATE_PROPERTY, submissionDate_, propertyValueChange);
			
			changeCnt++;
		}
	}
	
	protected void checkStateChange(OrderManagementContext context_, OrderState orderState_, ChangeOrigin changeOrigin_) {
		PropertyValueChangeDto propertyValueChange;
		assertEquals(1, ((MockPropertyChangeSessionImpl)context_.getPropertyChangeSession()).getPropertyChanges()[0].getChanges().length);
		assertEquals(changeOrigin_, ((MockPropertyChangeSessionImpl)context_.getPropertyChangeSession()).getPropertyChanges()[0].getChangeOrigin());	
		propertyValueChange = getPropertyChange(0, 0, context_);
		checkObjectProperty(OrderPropertyNames.STATE_PROPERTY, orderState_, propertyValueChange);
	}
	
	private PropertyValueChangeDto getSinglePropertyChange(
			OrderManagementContext context) {
		MockPropertyChangeSessionImpl propertyChangeSession = (MockPropertyChangeSessionImpl) context.getPropertyChangeSession();
		PropertyChanges[] propertyChanges = propertyChangeSession.getPropertyChanges();
		assertEquals(1, propertyChanges.length);
		assertEquals(1, propertyChanges[0].getChanges().length);
		assertTrue(propertyChanges[0].getChanges()[0] instanceof PropertyValueChangeDto);
		PropertyValueChangeDto propertyValueChange = (PropertyValueChangeDto) propertyChanges[0].getChanges()[0];
		return propertyValueChange;
	}
	
	protected PropertyValueChangeDto getPropertyChange(int changeIndex_, int changeIndexInsideChange_,
			OrderManagementContext context) {
		MockPropertyChangeSessionImpl propertyChangeSession = (MockPropertyChangeSessionImpl) context.getPropertyChangeSession();
		PropertyChanges[] propertyChanges = propertyChangeSession.getPropertyChanges();
		assertTrue(propertyChanges[changeIndex_].getChanges()[changeIndexInsideChange_] instanceof PropertyValueChangeDto);
		PropertyValueChangeDto propertyValueChange = (PropertyValueChangeDto) propertyChanges[changeIndex_].getChanges()[changeIndexInsideChange_];
		return propertyValueChange;
	}
	
	protected MarketDataChange[] filterMarketData(MarketDataChange[] input_, MarketDataType marketDataType_) {
		List<MarketDataChange> filtered = new ArrayList<MarketDataChange>();
		for (MarketDataChange marketDataChange : input_) {
			if(marketDataChange.getChangeType() == marketDataType_) {
				filtered.add(marketDataChange);
			}
		}
		
		return filtered.toArray(new MarketDataChange[filtered.size()]);
	}
	
	protected void checkMarketDataChange(QuoteType quoteType_, String marketCode,
			MarketDataChange marketData, MarketDataType marketDataType, double quoteValue, int size, Boolean hasHiddenOrders_) {
		assertEquals(marketDataType, marketData.getChangeType()); 
		assertEquals(size, marketData.getNewQuoteSize());
		assertEqualsQuote(getExpectedQuote(quoteValue, quoteType_), MarketDtoMapping.INSTANCE.fromDto(marketData.getNewQuote()));
		assertEquals(true, marketData.getNewQuoteValid());
		assertEquals(marketCode, marketData.getMarketCode());
		assertEquals(hasHiddenOrders_, marketData.getHasHiddenOrders());
	}

	protected OrderManagementContext createOrderManagerContext() {
		return TestUtilities.createOrderManagerContext();
	}
	
	protected OrderManagementContextImpl createOrderManagerContext(
			
			MockMarketDataSource marketDataSource, 
			MockPropertyChangeBroker propertyChangeBroker_) {
		return TestUtilities.createOrderManagerContext( 
				marketDataSource, propertyChangeBroker_);
	}
	
	protected Market getMarket(MockDatastores dataSources) {
		MarketInternal marketInternal = dataSources.findMarketBy(MockDatastores.MARKET_ID);
		Market market = marketInternal.getMarketModel();
		return market;
	}

	
	protected OrderInternal wrapOrder(Order order_, MockDatastores datastore) {
		return TestUtilities.wrapOrder(order_, datastore);
	}
	
	
	protected Order[] bookToOrders(OrderBookModel bookModel_, MockDatastores datastore_) {
		
		
		List<Order> orders = new ArrayList<Order>();
		
		for (Iterator<OrderBookEntryModel> entryIt = bookModel_.getEntryIterator(); entryIt.hasNext();) {
			OrderBookEntryModel entry = entryIt.next();
			for (Iterator<Long> orderIt = entry.getOrderKeyIterator(); orderIt.hasNext();) {
				orders.add(datastore_.findOrderBy(orderIt.next()).getOrderModel());
			}
		}
		
		return orders.toArray(new Order[orders.size()]);
	}
	
	protected void checkEntry(OrderBookModel book_, int entryIndex_, int orderIndex_, String submitter_, int size_, Quote limitQuote_, OrderSide side_, MockDatastores datastore_) {
		
		
		OrderBookEntryModel entry = ((OrderBookEntryModel) book_.get(entryIndex_));

		Order order = datastore_.findOrderBy(entry.getOrderKey(orderIndex_)).getOrderModel();
		
		assertEquals(submitter_, order.getSubmitterID());
		assertEquals(side_, order.getSide());
		// check the remaining size 
		assertEquals(size_, order.getSize() - order.getExecutedSize());
		assertEquals(limitQuote_.getQuoteValue(), order.getLimitQuoteValue().getQuoteValue());
		
	}
	
	public OrderBookModelDto orderBookToDto(OrderBookModel orderBook_) {
		return MarketDtoMapping.INSTANCE.toDto(orderBook_);
	}

	public OrderBookModel orderFromDto(OrderBookModelDto orderBook_) {
		return MarketDtoMapping.INSTANCE.fromDto(orderBook_);
	}
	
	public OrderDto orderToDto(Order order_) {
		return OrderDtoMapping.INSTANCE.toDto(order_);
	}

	public Order orderFromDto(OrderDto order_) {
		return OrderDtoMapping.INSTANCE.fromDto(order_);
	}
}
