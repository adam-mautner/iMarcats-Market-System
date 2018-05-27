package com.imarcats.market.engine.market;

import java.util.ArrayList;
import java.util.List;

import com.imarcats.infrastructure.server.trigger.MockTimeTrigger;
import com.imarcats.interfaces.client.v100.exception.ExceptionLanguageKeys;
import com.imarcats.interfaces.client.v100.exception.MarketRuntimeException;
import com.imarcats.internal.server.interfaces.market.MarketInternal;
import com.imarcats.internal.server.interfaces.order.OrderManagementContext;
import com.imarcats.market.engine.testutils.MockDatastores;
import com.imarcats.market.engine.testutils.OrderCompareTestCaseBase;
import com.imarcats.model.CircuitBreaker;
import com.imarcats.model.HaltRule;
import com.imarcats.model.Market;
import com.imarcats.model.Order;
import com.imarcats.model.test.testutils.TestOrderFlowGenerator;
import com.imarcats.model.types.ActivationStatus;
import com.imarcats.model.types.ChangeType;
import com.imarcats.model.types.MarketState;
import com.imarcats.model.types.OrderSide;
import com.imarcats.model.types.OrderType;
import com.imarcats.model.types.QuoteType;

public class CircuitBreakerTest extends OrderCompareTestCaseBase {

	
	public void testSimpleHalt() throws Exception {
		simpleHaltTest(QuoteType.Price, 10, ChangeType.Absolute);
		simpleHaltTest(QuoteType.Yield, 10, ChangeType.Absolute);

		simpleHaltTest(QuoteType.Price, 50.0 / 100, ChangeType.Percentage);
		simpleHaltTest(QuoteType.Yield, 10.0 / 100, ChangeType.Percentage);
	}
	
	private void simpleHaltTest(QuoteType quoteType_, double changeValue_, ChangeType changeType_) throws InterruptedException {
		
		MockDatastores datastore = new MockDatastores();
		Market market = getMarket(datastore);
		
		market.setActivationStatus(ActivationStatus.Activated);
		market.setQuoteType(quoteType_);
		market.setState(MarketState.Closed);
		
		CircuitBreaker circuitBreaker = new CircuitBreaker();
		HaltRule haltRule = new HaltRule();
		haltRule.setQuoteChangeAmount(changeValue_);
		haltRule.setChangeType(changeType_);

		List<HaltRule> haltRules = new ArrayList<HaltRule> ();
		haltRules.add(haltRule);

		circuitBreaker.setHaltRules(haltRules);
		market.setCircuitBreaker(circuitBreaker);
		
		MarketInternal marketImpl = new MarketImpl(market, null, datastore, datastore, datastore, datastore);

		TestOrderFlowGenerator gen = new TestOrderFlowGenerator(quoteType_, market);
		Order[] orders = gen.getOrders();
		
		// create order to place it to the data source
		for (Order order : orders) {
			datastore.createOrder(order);
		}
		
		orders[0].setType(OrderType.Limit);
		orders[0].setLimitQuoteValue(getExpectedQuote(20.0, quoteType_));
		orders[0].setSize(2);		
		orders[0].setSide(OrderSide.Buy);
		
		orders[1].setType(OrderType.Limit);
		orders[1].setLimitQuoteValue(getExpectedQuote(20.0, quoteType_));
		orders[1].setSize(2);		
		orders[1].setSide(OrderSide.Sell);
		
		OrderManagementContext orderManagementContext = createOrderManagerContext();

		marketImpl.submit(wrapOrder(orders[0], datastore), orderManagementContext);
		marketImpl.submit(wrapOrder(orders[1], datastore), orderManagementContext);
		
		marketImpl.openMarket(orderManagementContext);
		
		assertEquals(getExpectedQuote(20.0, quoteType_).getQuoteValue(), marketImpl.getOpeningQuote().getQuoteValue());
		
		orders[2].setType(OrderType.Limit);
		orders[2].setLimitQuoteValue(getExpectedQuote(25.0, quoteType_));
		orders[2].setSize(2);		
		orders[2].setSide(OrderSide.Buy);
		
		orders[3].setType(OrderType.Limit);
		orders[3].setLimitQuoteValue(getExpectedQuote(25.0, quoteType_));
		orders[3].setSize(2);		
		orders[3].setSide(OrderSide.Sell);
		
		marketImpl.submit(wrapOrder(orders[3], datastore), orderManagementContext);
		marketImpl.submit(wrapOrder(orders[2], datastore), orderManagementContext);

		assertEquals(MarketState.Open, marketImpl.getState());	
		
		orders[4].setType(OrderType.Limit);
		orders[4].setLimitQuoteValue(getExpectedQuote(30.0, quoteType_));
		orders[4].setSize(2);		
		orders[4].setSide(OrderSide.Buy);
		
		orders[5].setType(OrderType.Limit);
		orders[5].setLimitQuoteValue(getExpectedQuote(30.0, quoteType_));
		orders[5].setSize(2);		
		orders[5].setSide(OrderSide.Sell);
		
		marketImpl.submit(wrapOrder(orders[4], datastore), orderManagementContext);
		marketImpl.submit(wrapOrder(orders[5], datastore), orderManagementContext);
		
		assertEquals(getExpectedQuote(30.0, quoteType_).getQuoteValue(), marketImpl.getClosingQuote().getQuoteValue());
		
		assertEquals(MarketState.Closed, marketImpl.getState());
	}
	
	public void testSimpleHaltReopen() throws Exception {
		
		MockDatastores datastore = new MockDatastores();
		Market market = getMarket(datastore);
		
		market.setActivationStatus(ActivationStatus.Activated);
		market.setState(MarketState.Closed);
		
		CircuitBreaker circuitBreaker = new CircuitBreaker();
		HaltRule haltRule = new HaltRule();
		haltRule.setHaltPeriod(10);
		haltRule.setQuoteChangeAmount(10);
		haltRule.setChangeType(ChangeType.Absolute);

		List<HaltRule> haltRules = new ArrayList<HaltRule>();
		haltRules.add(haltRule);

		circuitBreaker.setHaltRules(haltRules);
		market.setCircuitBreaker(circuitBreaker);
		
		MockTimeTrigger trigger = new MockTimeTrigger(datastore);;
		MarketInternal marketImpl = new MarketImpl(market, trigger, 
				datastore, datastore, datastore, datastore);

		TestOrderFlowGenerator gen = new TestOrderFlowGenerator(QuoteType.Price, market);
		Order[] orders = gen.getOrders();
		
		// create order to place it to the data source
		for (Order order : orders) {
			datastore.createOrder(order);
		} 
		
		orders[0].setType(OrderType.Limit);
		orders[0].setLimitQuoteValue(wrapQuote(20.0));
		orders[0].setSize(2);		
		orders[0].setSide(OrderSide.Buy);
		
		orders[1].setType(OrderType.Limit);
		orders[1].setLimitQuoteValue(wrapQuote(20.0));
		orders[1].setSize(2);		
		orders[1].setSide(OrderSide.Sell);
		
		OrderManagementContext orderManagementContext = createOrderManagerContext();

		
		marketImpl.submit(wrapOrder(orders[0], datastore), orderManagementContext);
		marketImpl.submit(wrapOrder(orders[1], datastore), orderManagementContext);
		
		marketImpl.openMarket(orderManagementContext);
		
		orders[4].setType(OrderType.Limit);
		orders[4].setLimitQuoteValue(wrapQuote(30.0));
		orders[4].setSize(2);		
		orders[4].setSide(OrderSide.Buy);
		
		orders[5].setType(OrderType.Limit);
		orders[5].setLimitQuoteValue(wrapQuote(30.0));
		orders[5].setSize(2);		
		orders[5].setSide(OrderSide.Sell);
		
		marketImpl.submit(wrapOrder(orders[4], datastore), orderManagementContext);
		marketImpl.submit(wrapOrder(orders[5], datastore), orderManagementContext);
		
		assertEquals(null, marketImpl.getClosingQuote());
		
		assertEquals(MarketState.Halted, marketImpl.getState());
		
		trigger.fireTrigger();
		
		assertEquals(MarketState.Open, marketImpl.getState());
		
		orders[6].setType(OrderType.Limit);
		orders[6].setLimitQuoteValue(wrapQuote(30.0));
		orders[6].setSize(2);		
		orders[6].setSide(OrderSide.Buy);
		
		orders[7].setType(OrderType.Limit);
		orders[7].setLimitQuoteValue(wrapQuote(30.0));
		orders[7].setSize(2);		
		orders[7].setSide(OrderSide.Sell);
		
		marketImpl.submit(wrapOrder(orders[6], datastore), orderManagementContext);
		marketImpl.submit(wrapOrder(orders[7], datastore), orderManagementContext);
		
		assertEquals(null, marketImpl.getClosingQuote());
		assertEquals(MarketState.Halted, marketImpl.getState());
	}
	
	public void testHaltEscalationWithClose() throws Exception {
		
		MockDatastores datastore = new MockDatastores();
		Market market = getMarket(datastore);
		
		market.setActivationStatus(ActivationStatus.Activated);
		market.setState(MarketState.Closed);
		
		CircuitBreaker circuitBreaker = new CircuitBreaker();
		HaltRule haltRule = new HaltRule();
		haltRule.setHaltPeriod(10);
		haltRule.setQuoteChangeAmount(10);
		haltRule.setChangeType(ChangeType.Absolute);

		HaltRule haltRule2 = new HaltRule();
		haltRule2.setHaltPeriod(-1);
		haltRule2.setQuoteChangeAmount(20);
		haltRule2.setChangeType(ChangeType.Absolute);
		
		List<HaltRule> haltRules = new ArrayList<HaltRule>();
		haltRules.add(haltRule);
		haltRules.add(haltRule2);

		circuitBreaker.setHaltRules(haltRules);
		market.setCircuitBreaker(circuitBreaker);
		
		MockTimeTrigger trigger = new MockTimeTrigger(datastore);;
		MarketInternal marketImpl = new MarketImpl(market, trigger, datastore, datastore, datastore, datastore);

		TestOrderFlowGenerator gen = new TestOrderFlowGenerator(QuoteType.Price, market);
		Order[] orders = gen.getOrders();
		
		// create order to place it to the data source
		for (Order order : orders) {
			datastore.createOrder(order);
		}
		
		orders[0].setType(OrderType.Limit);
		orders[0].setLimitQuoteValue(wrapQuote(20.0));
		orders[0].setSize(2);		
		orders[0].setSide(OrderSide.Buy);
		
		orders[1].setType(OrderType.Limit);
		orders[1].setLimitQuoteValue(wrapQuote(20.0));
		orders[1].setSize(2);		
		orders[1].setSide(OrderSide.Sell);

		OrderManagementContext orderManagementContext = createOrderManagerContext();

		marketImpl.submit(wrapOrder(orders[0], datastore), orderManagementContext);
		marketImpl.submit(wrapOrder(orders[1], datastore), orderManagementContext);
		
		marketImpl.openMarket(orderManagementContext);
		
		orders[4].setType(OrderType.Limit);
		orders[4].setLimitQuoteValue(wrapQuote(30.0));
		orders[4].setSize(2);		
		orders[4].setSide(OrderSide.Buy);
		
		orders[5].setType(OrderType.Limit);
		orders[5].setLimitQuoteValue(wrapQuote(30.0));
		orders[5].setSize(2);		
		orders[5].setSide(OrderSide.Sell);
		
		marketImpl.submit(wrapOrder(orders[4], datastore), orderManagementContext);
		marketImpl.submit(wrapOrder(orders[5], datastore), orderManagementContext);
		
		assertEquals(null, marketImpl.getClosingQuote());
		
		assertEquals(MarketState.Halted, marketImpl.getState());
		
		trigger.fireTrigger();
		
		assertEquals(MarketState.Open, marketImpl.getState());
		
		orders[6].setType(OrderType.Limit);
		orders[6].setLimitQuoteValue(wrapQuote(40.0));
		orders[6].setSize(2);		
		orders[6].setSide(OrderSide.Buy);
		
		orders[7].setType(OrderType.Limit);
		orders[7].setLimitQuoteValue(wrapQuote(40.0));
		orders[7].setSize(2);		
		orders[7].setSide(OrderSide.Sell);
		
		marketImpl.submit(wrapOrder(orders[6], datastore), orderManagementContext);
		marketImpl.submit(wrapOrder(orders[7], datastore), orderManagementContext);
		
		assertEquals(40.0, marketImpl.getClosingQuote().getQuoteValue());
		
		assertEquals(MarketState.Closed, marketImpl.getState());
		
		try {
			trigger.fireTrigger();
		} catch (MarketRuntimeException e) {
			assertEquals(ExceptionLanguageKeys.NON_HALTED_MARKET_CANNOT_BE_OPENED, e.getLanguageKey());
		} 
	}
	
	
	public void testHaltEscalation() throws Exception {
		
		
		MockDatastores datastore = new MockDatastores();
		Market market = getMarket(datastore);
		
		market.setActivationStatus(ActivationStatus.Activated);
		market.setState(MarketState.Closed);
		
		CircuitBreaker circuitBreaker = new CircuitBreaker();
		HaltRule haltRule = new HaltRule();
		haltRule.setHaltPeriod(10);
		haltRule.setQuoteChangeAmount(10);
		haltRule.setChangeType(ChangeType.Absolute);

		HaltRule haltRule2 = new HaltRule();
		haltRule2.setHaltPeriod(20);
		haltRule2.setQuoteChangeAmount(20);
		haltRule2.setChangeType(ChangeType.Absolute);
		
		List<HaltRule> haltRules = new ArrayList<HaltRule>();
		haltRules.add(haltRule);
		haltRules.add(haltRule2);

		circuitBreaker.setHaltRules(haltRules);
		market.setCircuitBreaker(circuitBreaker);
		
		MockTimeTrigger trigger = new MockTimeTrigger(datastore);
		MarketInternal marketImpl = new MarketImpl(market, trigger, 
				datastore, datastore, datastore, datastore);

		TestOrderFlowGenerator gen = new TestOrderFlowGenerator(QuoteType.Price, market);
		Order[] orders = gen.getOrders();
		
		// create order to place it to the data source
		for (Order order : orders) {
			datastore.createOrder(order);
		}
		
		orders[0].setType(OrderType.Limit);
		orders[0].setLimitQuoteValue(wrapQuote(20.0));
		orders[0].setSize(2);		
		orders[0].setSide(OrderSide.Buy);
		
		orders[1].setType(OrderType.Limit);
		orders[1].setLimitQuoteValue(wrapQuote(20.0));
		orders[1].setSize(2);		
		orders[1].setSide(OrderSide.Sell);

		OrderManagementContext orderManagementContext = createOrderManagerContext();

		marketImpl.submit(wrapOrder(orders[0], datastore), orderManagementContext);
		marketImpl.submit(wrapOrder(orders[1], datastore), orderManagementContext);
		
		marketImpl.openMarket(orderManagementContext);
		
		orders[4].setType(OrderType.Limit);
		orders[4].setLimitQuoteValue(wrapQuote(30.0));
		orders[4].setSize(2);		
		orders[4].setSide(OrderSide.Buy);
		
		orders[5].setType(OrderType.Limit);
		orders[5].setLimitQuoteValue(wrapQuote(30.0));
		orders[5].setSize(2);		
		orders[5].setSide(OrderSide.Sell);
		
		marketImpl.submit(wrapOrder(orders[4], datastore), orderManagementContext);
		marketImpl.submit(wrapOrder(orders[5], datastore), orderManagementContext);
		
		assertEquals(null, marketImpl.getClosingQuote());
		
		assertEquals(MarketState.Halted, marketImpl.getState());
		
		trigger.fireTrigger();
		
		assertEquals(MarketState.Open, marketImpl.getState());
		
		orders[6].setType(OrderType.Limit);
		orders[6].setLimitQuoteValue(wrapQuote(40.0));
		orders[6].setSize(2);		
		orders[6].setSide(OrderSide.Buy);
		
		orders[7].setType(OrderType.Limit);
		orders[7].setLimitQuoteValue(wrapQuote(40.0));
		orders[7].setSize(2);		
		orders[7].setSide(OrderSide.Sell);
		
		marketImpl.submit(wrapOrder(orders[6], datastore), orderManagementContext);
		marketImpl.submit(wrapOrder(orders[7], datastore), orderManagementContext);
		
		assertEquals(MarketState.Halted, marketImpl.getState());
		
		trigger.fireTrigger();
		
		assertEquals(MarketState.Open, marketImpl.getState());
		
		gen = new TestOrderFlowGenerator(QuoteType.Price, market);
		orders = gen.getOrders();
		
		// create order to place it to the data source
		for (Order order : orders) {
			datastore.createOrder(order);
		}
		
		orders[0].setType(OrderType.Limit);
		orders[0].setLimitQuoteValue(wrapQuote(40.0));
		orders[0].setSize(2);		
		orders[0].setSide(OrderSide.Buy);
		
		orders[1].setType(OrderType.Limit);
		orders[1].setLimitQuoteValue(wrapQuote(40.0));
		orders[1].setSize(2);		
		orders[1].setSide(OrderSide.Sell);

		marketImpl.submit(wrapOrder(orders[0], datastore), orderManagementContext);
		marketImpl.submit(wrapOrder(orders[1], datastore), orderManagementContext);
		
		assertEquals(MarketState.Halted, marketImpl.getState());
		
		trigger.fireTrigger();
		
		assertEquals(MarketState.Open, marketImpl.getState());

	}
	
	public void testMaximumPriceImprovement() throws Exception {
		maximumPriceImprovementTest(QuoteType.Price);
		maximumPriceImprovementTest(QuoteType.Yield);
	}
	
	private void maximumPriceImprovementTest(QuoteType quoteType_) throws InterruptedException {
		
		
		MockDatastores datastore = new MockDatastores();
		Market market = getMarket(datastore);
		
		market.setActivationStatus(ActivationStatus.Activated);
		market.setQuoteType(quoteType_);
		market.setState(MarketState.Closed);
		
		CircuitBreaker circuitBreaker = new CircuitBreaker();

		
		market.setCircuitBreaker(circuitBreaker);
		
		MockTimeTrigger trigger = new MockTimeTrigger(datastore);;
		MarketInternal marketImpl = new MarketImpl(market, trigger, 
				datastore, datastore, datastore, datastore);
		
		TestOrderFlowGenerator gen = new TestOrderFlowGenerator(quoteType_, market);
		Order[] orders = gen.getOrders();
		
		// create order to place it to the data source
		for (Order order : orders) {
			datastore.createOrder(order);
		}
		
		orders[0].setType(OrderType.Limit);
		orders[0].setLimitQuoteValue(getExpectedQuote(20.0, quoteType_));
		orders[0].setSize(2);		
		orders[0].setSide(OrderSide.Buy);
		
		orders[1].setType(OrderType.Limit);
		orders[1].setLimitQuoteValue(getExpectedQuote(20.0, quoteType_));
		orders[1].setSize(2);		
		orders[1].setSide(OrderSide.Sell);

		OrderManagementContext orderManagementContext = createOrderManagerContext();

		marketImpl.submit(wrapOrder(orders[0], datastore), orderManagementContext);
		marketImpl.submit(wrapOrder(orders[1], datastore), orderManagementContext);
		
		marketImpl.openMarket(orderManagementContext);
		
		orders[3].setType(OrderType.Limit);
		orders[3].setLimitQuoteValue(getExpectedQuote(30.0, quoteType_));
		orders[3].setSize(2);		
		orders[3].setSide(OrderSide.Buy);
		
		marketImpl.submit(wrapOrder(orders[3], datastore), orderManagementContext);
		
		circuitBreaker.setMaximumQuoteImprovement(10);
		
		orders[4].setType(OrderType.Limit);
		orders[4].setLimitQuoteValue(getExpectedQuote(25.0, quoteType_));
		orders[4].setSize(2);		
		orders[4].setSide(OrderSide.Buy);
		
		marketImpl.submit(wrapOrder(orders[4], datastore), orderManagementContext);
		
		orders[5].setType(OrderType.Limit);
		orders[5].setLimitQuoteValue(getExpectedQuote(31.0, quoteType_));
		orders[5].setSize(2);		
		orders[5].setSide(OrderSide.Buy);
		
		try{
			marketImpl.submit(wrapOrder(orders[5], datastore), orderManagementContext);
			fail();
		} catch (MarketRuntimeException e) {
			// expected
			assertEquals(ExceptionLanguageKeys.ORDER_IMPROVES_QUOTE_WITH_MORE_THAN_MAXIMUM_QUOTE_IMPROVEMENT, e.getLanguageKey());
		}
	}
}
