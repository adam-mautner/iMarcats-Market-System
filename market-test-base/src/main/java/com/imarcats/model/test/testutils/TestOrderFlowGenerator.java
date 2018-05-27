package com.imarcats.model.test.testutils;

import java.util.Date;

import com.imarcats.model.Market;
import com.imarcats.model.Order;
import com.imarcats.model.types.OrderSide;
import com.imarcats.model.types.OrderType;
import com.imarcats.model.types.Quote;
import com.imarcats.model.types.QuoteType;

public class TestOrderFlowGenerator {
	private final Order[] _orders = new Order[9];
	private final Order[] _expectedSellOrders = new Order[4];
	private final Order[] _expectedBuyOrders = new Order[5];
	
	public TestOrderFlowGenerator(QuoteType quoteType_, Market market_) throws InterruptedException { 
		this(quoteType_, market_, true);
	}
	
	/**
	 * @param quoteType_ Value from Quote Type
	 * @throws InterruptedException
	 */
	public TestOrderFlowGenerator(QuoteType quoteType_, Market market_, boolean withKey_) throws InterruptedException { 
		Order order = createOrder(quoteType_, 20.0, OrderSide.Buy, "Bea", OrderType.Limit, 3, market_, withKey_);
		_orders[0] = order;
		_expectedBuyOrders[2] = order;
		Thread.sleep(40);
		
		order =	createOrder(quoteType_, 20.1, OrderSide.Sell, "Sam", OrderType.Limit, 2, market_, withKey_);
		_orders[1] = order;
		_expectedSellOrders[2] = order;
		Thread.sleep(30);
		
		order = createOrder(quoteType_, 20.0, OrderSide.Buy, "Ben", OrderType.Limit, 2, market_, withKey_);
		_orders[2] = order;
		_expectedBuyOrders[3] = order;
		Thread.sleep(10);
 
		order = createOrder(quoteType_, 19.8, OrderSide.Sell, "Sol", OrderType.Limit, 1, market_, withKey_);
		_orders[3] = order;
		_expectedSellOrders[0] = order;
		Thread.sleep(10);

		order = createOrder(quoteType_, 20.2, OrderSide.Sell, "Stu", OrderType.Limit, 5, market_, withKey_);
		_orders[4] = order;
		_expectedSellOrders[3] = order;
		Thread.sleep(10);
		
		order = createOrder(quoteType_, 0, OrderSide.Buy, "Bif", OrderType.Market, 4, market_, withKey_);
		_orders[5] = order;
		_expectedBuyOrders[0] = order;
		Thread.sleep(30);
		
		order = createOrder(quoteType_, 20.1, OrderSide.Buy, "Bob", OrderType.Limit, 2, market_, withKey_);
		_orders[6] = order;
		_expectedBuyOrders[1] = order;
		Thread.sleep(20);
		
		order = createOrder(quoteType_, 20.0, OrderSide.Sell, "Sue", OrderType.Limit, 6, market_, withKey_);
		_orders[7] = order;
		_expectedSellOrders[1] = order;
		Thread.sleep(90);
		
		order = createOrder(quoteType_, 19.8, OrderSide.Buy, "Bud", OrderType.Limit, 7, market_, withKey_);
		_orders[8] = order;
		_expectedBuyOrders[4] = order;
	}

	public Order[] getOrders() {
		return _orders;
	}

	public Order[] getExpectedSellOrders() {
		return _expectedSellOrders;
	}

	public Order[] getExpectedBuyOrders() {
		return _expectedBuyOrders;
	}

	private Order createOrder(QuoteType quoteType_, double limitQuote_, OrderSide side_, String submitter_, OrderType type_, int size_, Market market_, boolean withKey_) {
		Order order = new Order();
		
		order.setTargetMarketCode(market_.getMarketCode());
		if(withKey_) {
			order.setKey(MockIdentityGenerator.getId());
		}
		double actualLimitQuote = limitQuote_;
		if(quoteType_ == QuoteType.Yield && type_ != OrderType.Market) {
			// models a Bond Yield
			actualLimitQuote = 100 - limitQuote_;
		}
		if(type_ == OrderType.Limit) {
			order.setLimitQuoteValue(Quote.createQuote(actualLimitQuote));
		} 
		order.setSide(side_);
		order.setSubmitterID(submitter_);
		order.setSubmissionDate(new Date());
		order.setType(type_);
		order.setSize(size_);
		
		return order;
	}
}
