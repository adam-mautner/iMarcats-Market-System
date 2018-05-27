package com.imarcats.infrastructure.server.trigger;

import com.imarcats.interfaces.client.v100.i18n.MarketSystemMessageLanguageKeys;
import com.imarcats.internal.server.interfaces.market.MarketInternal;
import com.imarcats.internal.server.interfaces.order.OrderInternal;
import com.imarcats.internal.server.interfaces.order.OrderManagementContext;
import com.imarcats.market.engine.market.MarketImpl;
import com.imarcats.market.engine.testutils.MockDatastores;
import com.imarcats.market.engine.testutils.OrderCompareTestCaseBase;
import com.imarcats.model.Market;
import com.imarcats.model.Order;
import com.imarcats.model.test.testutils.TestOrderFlowGenerator;
import com.imarcats.model.types.ActivationStatus;
import com.imarcats.model.types.OrderExpirationInstruction;
import com.imarcats.model.types.OrderState;
import com.imarcats.model.types.QuoteType;

public class OrderExpirationTest extends OrderCompareTestCaseBase {
	
	public void testExpiration() throws Exception {
		MockDatastores datastore = new MockDatastores(true);		
		MockTimeTrigger trigger = datastore.getTrigger();
		
		OrderManagementContext orderManagementContext = createOrderManagerContext();
		
		Market market = getMarket(datastore);
		
		market.setActivationStatus(ActivationStatus.Activated);
		market.setTradingDayEnd(createTimePeriod().getStartTime());

		TestOrderFlowGenerator gen = new TestOrderFlowGenerator(QuoteType.Price, market);
		Order[] orders = gen.getOrders();
		
		for (Order order : orders) {
			datastore.createOrder(order);
		}
		
		MarketInternal marketImpl = new MarketImpl(market, trigger, datastore, datastore, datastore, datastore);
		
		OrderInternal orderO = wrapOrder(orders[0], datastore);
		marketImpl.submit(orderO, orderManagementContext);
		
		assertEquals(null, trigger.getAction());

		marketImpl.cancel(orderO, "", orderManagementContext);
		
		orders[1].setExpirationInstruction(OrderExpirationInstruction.DayOrder);
		OrderInternal order1 = wrapOrder(orders[1], datastore);
		
		marketImpl.submit(order1, orderManagementContext);
		
		marketImpl.cancel(order1, "", orderManagementContext);
		
		assertTrue(trigger.getAction() == null);
		
		orders[2].setExpirationInstruction(OrderExpirationInstruction.DayOrder);
		OrderInternal order2 = wrapOrder(orders[2], datastore);
		
		marketImpl.submit(order2, orderManagementContext);
		
		assertTrue(trigger.getAction() != null);
		assertTrue(order2.getExpirationTriggerActionKey() == trigger.getActionKey());
		
		assertEquals(OrderState.Submitted, order2.getState());
		
		trigger.fireTrigger();

		assertEquals(OrderState.Canceled, order2.getState());
		assertEquals(MarketSystemMessageLanguageKeys.ORDER_EXPIRED, order2.getCancellationCommentLanguageKey());
		
		assertTrue(trigger.getAction() == null);
		assertTrue(order2.getExpirationTriggerActionKey() == null);
	}
	
}
