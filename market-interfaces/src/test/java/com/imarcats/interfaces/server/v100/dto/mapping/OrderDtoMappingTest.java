package com.imarcats.interfaces.server.v100.dto.mapping;

import java.util.ArrayList;
import java.util.List;

import com.imarcats.interfaces.client.v100.dto.OrderDto;
import com.imarcats.interfaces.client.v100.dto.types.PagedOrderListDto;
import com.imarcats.interfaces.server.v100.dto.mapping.OrderDtoMapping;
import com.imarcats.model.Order;
import com.imarcats.model.mutators.PropertyListValueChange;
import com.imarcats.model.mutators.helpers.BooleanWrapper;
import com.imarcats.model.mutators.helpers.LongWrapper;
import com.imarcats.model.mutators.helpers.OrderExpirationInstructionWrapper;
import com.imarcats.model.mutators.helpers.OrderSideWrapper;
import com.imarcats.model.mutators.helpers.OrderStateWrapper;
import com.imarcats.model.mutators.helpers.OrderTriggerInstructionWrapper;
import com.imarcats.model.mutators.helpers.OrderTypeWrapper;
import com.imarcats.model.types.Property;
import com.imarcats.model.types.OrderExpirationInstruction;
import com.imarcats.model.types.OrderSide;
import com.imarcats.model.types.OrderState;
import com.imarcats.model.types.OrderTriggerInstruction;
import com.imarcats.model.types.OrderType;
import com.imarcats.model.types.PagedOrderList;
import com.imarcats.model.utils.PropertyUtils;

public class OrderDtoMappingTest extends PropertyMappingTestBase {

	public void testRoundTripMapping() throws Exception {
		Order order = createOrderForDto("TestMarket");
		order.setKey(10L);
		
		OrderDto orderDto = OrderDtoMapping.INSTANCE.toDto(order);
		Order orderMapped = OrderDtoMapping.INSTANCE.fromDto(orderDto); 
		
		checkOrder(order, orderMapped);
	}

	public void testObjectPropertyChangeRoundTripMapping() throws Exception {
		List<Property> properties = new ArrayList<Property>();
		properties.add(PropertyUtils.createObjectProperty("Audit", createAudit()));
		properties.add(PropertyUtils.createObjectProperty("Bool", new BooleanWrapper(true)));
		properties.add(PropertyUtils.createObjectProperty("Long", new LongWrapper(10L)));
		properties.add(PropertyUtils.createObjectProperty("OrderExpirationInstruction", new OrderExpirationInstructionWrapper(OrderExpirationInstruction.DayOrder)));
		properties.add(PropertyUtils.createObjectProperty("OrderSide", new OrderSideWrapper(OrderSide.Buy)));
		properties.add(PropertyUtils.createObjectProperty("OrderState", new OrderStateWrapper(OrderState.Canceled)));
		properties.add(PropertyUtils.createObjectProperty("OrderTriggerInstruction", new OrderTriggerInstructionWrapper(OrderTriggerInstruction.Immediate)));
		properties.add(PropertyUtils.createObjectProperty("OrderType", new OrderTypeWrapper(OrderType.Limit)));

		for (Property property : properties) {			
			testProperty(property);
		}
	}

	private void testProperty(Property property) {
		PropertyListValueChange listChange = createListValueChange(property);
		testObjectProperties(property, listChange);
	}
	
	public void testPropertyChangeRoundTripMapping() throws Exception {
		List<Property> properties = createPropertyList();
		
		for (Property property : properties) {			
			testProperty(property);
		}
	}
	
	public void testTypeRoundTripMapping() throws Exception {
		assertEquals(null, OrderDtoMapping.INSTANCE.toDto((OrderSide)null));
		assertEquals(com.imarcats.interfaces.client.v100.dto.types.OrderSide.Buy, OrderDtoMapping.INSTANCE.toDto(OrderSide.Buy));
		assertEquals(OrderSide.Buy, OrderDtoMapping.INSTANCE.fromDto(com.imarcats.interfaces.client.v100.dto.types.OrderSide.Buy));
	}
	
	private void checkOrder(Order order, Order orderMapped) {
		orderMapped.setCommissionCharged(order.getCommissionCharged());
		orderMapped.setExpirationTriggerActionKey(order.getExpirationTriggerActionKey());
		orderMapped.setQuoteChangeTriggerKey(order.getQuoteChangeTriggerKey());
		
		assertEqualsOrder(order, orderMapped);
		assertEquals(order.getVersionNumber(), orderMapped.getVersionNumber());
	}
	
	protected Order createOrderForDto(String targetMarketCode_) throws Exception { 
		Order order = createOrder(targetMarketCode_);
		order.updateLastUpdateTimestamp();
		order.setVersionNumber(10L);
		
		return order;
	}
	
	public void testRoundTripListMapping() throws Exception {
		Order order = createOrderForDto("Test1");
		Order order2 = createOrderForDto("Test2");
		
		PagedOrderList list = new PagedOrderList();
		list.setOrders(new Order[] {order, order2});
		list.setCursorString("TestCursor");
		list.setMaxNumberOfOrdersOnPage(2); 
		
		PagedOrderListDto listDto = OrderDtoMapping.INSTANCE.toDto(list);
		PagedOrderList listMapped = OrderDtoMapping.INSTANCE.fromDto(listDto); 
		
		assertEquals(list.getCursorString(), listMapped.getCursorString());
		assertEquals(list.getMaxNumberOfOrdersOnPage(), listMapped.getMaxNumberOfOrdersOnPage());
		assertEquals(list.getOrders().length, listMapped.getOrders().length);
		checkOrder(list.getOrders()[0], listMapped.getOrders()[0]);		
		checkOrder(list.getOrders()[1], listMapped.getOrders()[1]);		
	}
}
