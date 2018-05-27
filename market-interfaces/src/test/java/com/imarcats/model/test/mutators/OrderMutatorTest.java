package com.imarcats.model.test.mutators;

import java.util.Date;

import com.imarcats.model.Order;
import com.imarcats.model.OrderPropertyNames;
import com.imarcats.model.mutators.ChangeAction;
import com.imarcats.model.mutators.ClientOrderMutator;
import com.imarcats.model.mutators.DuplicatePropertyException;
import com.imarcats.model.mutators.PropertyChange;
import com.imarcats.model.mutators.SystemOrderMutator;
import com.imarcats.model.mutators.UnsupportedPropertyChangeException;
import com.imarcats.model.mutators.UnsupportedPropertyException;
import com.imarcats.model.mutators.UnsupportedPropertyValueException;
import com.imarcats.model.mutators.helpers.BooleanWrapper;
import com.imarcats.model.mutators.helpers.LongWrapper;
import com.imarcats.model.mutators.helpers.OrderExpirationInstructionWrapper;
import com.imarcats.model.mutators.helpers.OrderSideWrapper;
import com.imarcats.model.mutators.helpers.OrderStateWrapper;
import com.imarcats.model.mutators.helpers.OrderTriggerInstructionWrapper;
import com.imarcats.model.mutators.helpers.OrderTypeWrapper;
import com.imarcats.model.test.testutils.MarketObjectTestBase;
import com.imarcats.model.types.AuditInformation;
import com.imarcats.model.types.BooleanProperty;
import com.imarcats.model.types.DoubleProperty;
import com.imarcats.model.types.Property;
import com.imarcats.model.types.TransferableObject;
import com.imarcats.model.types.IntProperty;
import com.imarcats.model.types.ObjectProperty;
import com.imarcats.model.types.OrderExpirationInstruction;
import com.imarcats.model.types.OrderSide;
import com.imarcats.model.types.OrderState;
import com.imarcats.model.types.OrderTriggerInstruction;
import com.imarcats.model.types.OrderType;
import com.imarcats.model.types.Quote;
import com.imarcats.model.utils.PropertyUtils;

public class OrderMutatorTest extends MarketObjectTestBase {
    
	public void testPropertyChangeSystem() throws Exception {
		Order order = new Order();		

		// test system properties
		order.setKey(10L);
		SystemOrderMutator.INSTANCE.changePropertyValue(order, getObjectProperty(OrderPropertyNames.KEY_PROPERTY, new LongWrapper(1L)));
		assertEquals((long) 1, order.getKey().longValue());	
	
		String newUser = "Test2";
		order.setSubmitterID("Test");
		SystemOrderMutator.INSTANCE.changePropertyValue(order, getStringProperty(OrderPropertyNames.SUBMITTER_ID_PROPERTY, newUser));
		assertEquals(newUser, order.getSubmitterID());
		
		String newMarketCode = "NewCode";
		order.setTargetMarketCode("TestCode");
		SystemOrderMutator.INSTANCE.changePropertyValue(order, getStringProperty(OrderPropertyNames.TARGET_MARKET_CODE_PROPERTY, newMarketCode));
		assertEquals(newMarketCode, order.getTargetMarketCode());	

		order.setExecutedSize(0);
		SystemOrderMutator.INSTANCE.changePropertyValue(order, getIntProperty(OrderPropertyNames.EXECUTED_SIZE_PROPERTY, 1));
		assertEquals(1, order.getExecutedSize());

		SystemOrderMutator.INSTANCE.changePropertyValue(order, getObjectProperty(OrderPropertyNames.STATE_PROPERTY, new OrderStateWrapper(OrderState.Canceled)));
		assertEquals(OrderState.Canceled, order.getState());
		
		Date newDate = new Date();
		order.setSubmissionDate(new Date());
		SystemOrderMutator.INSTANCE.changePropertyValue(order, getDateProperty(OrderPropertyNames.SUBMISSION_DATE_PROPERTY, newDate));
		assertEquals(newDate, order.getSubmissionDate());

		AuditInformation newAudit = new AuditInformation();
		order.setCreationAudit(new AuditInformation());
		SystemOrderMutator.INSTANCE.changePropertyValue(order, getObjectProperty(OrderPropertyNames.CREATION_AUDIT_PROPERTY, newAudit));
		assertEquals(newAudit, order.getCreationAudit());
		
		Quote newQuote = new Quote();
		order.setCurrentStopQuote(new Quote());
		SystemOrderMutator.INSTANCE.changePropertyValue(order, getObjectProperty(OrderPropertyNames.CURRENT_STOP_QUOTE_PROPERTY, newQuote));
		assertEqualsQuote(newQuote, order.getCurrentStopQuote());

		SystemOrderMutator.INSTANCE.changePropertyValue(order, getIntProperty(OrderPropertyNames.QUOTE_CHANGE_TRIGGER_KEY_PROPERTY, 111));
		assertEquals((long)111, (long)order.getQuoteChangeTriggerKey());
		
		SystemOrderMutator.INSTANCE.changePropertyValue(order, getIntProperty(OrderPropertyNames.EXPIRATION_TRIGGER_ACTION_KEY_PROPERTY, 222));
		assertEquals((long)222, (long)order.getExpirationTriggerActionKey());
		
		SystemOrderMutator.INSTANCE.changePropertyValue(order, getObjectProperty(OrderPropertyNames.COMMISSION_CHARGED_PROPERTY, new BooleanWrapper(true)));
		assertEquals((boolean)true, (boolean)order.getCommissionCharged());
		
		SystemOrderMutator.INSTANCE.changePropertyValue(order, getStringProperty(OrderPropertyNames.CANCELLATION_COMMENT_LANGUAGE_KEY_PROPERTY, "TestLangKey"));
		assertEquals("TestLangKey", (String)order.getCancellationCommentLanguageKey());
		
		// tests bypass to client mutator
		order.setSide(OrderSide.Buy);
		SystemOrderMutator.INSTANCE.changePropertyValue(order, getObjectProperty(OrderPropertyNames.SIDE_PROPERTY, new OrderSideWrapper(OrderSide.Sell)));
		assertEquals(OrderSide.Sell, order.getSide());	
		
		// tests bypass to client mutator for list
		SystemOrderMutator.INSTANCE.executePropertyChanges(order, new PropertyChange[] { getValueListChange(ChangeAction.Add, getBooleanProperty("Test", true), OrderPropertyNames.TRIGGER_PROPERTY_LIST) }, null);
		
		assertEquals(1, order.getTriggerProperties().length);
	}
	
	public void testErrorOnSystemPropertyChange() throws Exception {
		Order order = new Order();
		for (String propertyName : OrderPropertyNames.SYSTEM_PROPERTIES) {
			try {
				ClientOrderMutator.INSTANCE.changePropertyValue(order, getObjectProperty(propertyName, null));
				fail();
			} catch (UnsupportedPropertyException e) {
				// expected
			}
		}
	}
	
	public void testPropertyChangeInClient() throws Exception {
		Order order = new Order();
		
		try{
			ClientOrderMutator.INSTANCE.changePropertyValue(order, null);
			fail();
		} catch (UnsupportedPropertyValueException e) {
			// expected
		} catch (Exception e) {
			fail();
		}
		
		try{
			ClientOrderMutator.INSTANCE.changePropertyValue(order, getBooleanProperty("Bogus", false));
			fail();
		} catch (UnsupportedPropertyException e) {
			// expected
		} catch (Exception e) {
			fail();
		}
		
		order.setSide(OrderSide.Buy);
		ClientOrderMutator.INSTANCE.changePropertyValue(order, getObjectProperty(OrderPropertyNames.SIDE_PROPERTY, new OrderSideWrapper(OrderSide.Sell)));
		assertEquals(OrderSide.Sell, order.getSide());

		order.setType(OrderType.Limit);
		ClientOrderMutator.INSTANCE.changePropertyValue(order, getObjectProperty(OrderPropertyNames.TYPE_PROPERTY, new OrderTypeWrapper(OrderType.Market)));
		assertEquals(OrderType.Market, order.getType());

		ClientOrderMutator.INSTANCE.changePropertyValue(order, getDoubleProperty(OrderPropertyNames.LIMIT_QUOTE_VALUE_PROPERTY, 10.0));
		// This has been removed to keep the Property Change System simple
		// assertEquals(OrderType.Limit, order.getType());
		assertEquals(10.0, order.getLimitQuoteValue().getQuoteValue());
		
		ClientOrderMutator.INSTANCE.changePropertyValue(order, getIntProperty(OrderPropertyNames.MINIMUM_SIZE_OF_EXECUTION_PROPERTY, 10));
		assertEquals(10, order.getMinimumSizeOfExecution());
		
		ClientOrderMutator.INSTANCE.changePropertyValue(order, getBooleanProperty(OrderPropertyNames.EXECUTE_ENTIRE_ORDER_AT_ONCE_PROPERTY, true));
		assertEquals(true, order.getExecuteEntireOrderAtOnce());

		ClientOrderMutator.INSTANCE.changePropertyValue(order, getBooleanProperty(OrderPropertyNames.DISPLAY_ORDER_PROPERTY, true));
		assertEquals(true, order.getDisplayOrder());

		ClientOrderMutator.INSTANCE.changePropertyValue(order, getIntProperty(OrderPropertyNames.TARGET_ACCOUNT_ID_PROPERTY, 123));
		assertEquals(new Long(123), order.getTargetAccountID());
		
		ClientOrderMutator.INSTANCE.changePropertyValue(order, getObjectProperty(OrderPropertyNames.TRIGGER_INSTRUCTION_PROPERTY, new OrderTriggerInstructionWrapper(OrderTriggerInstruction.StopLoss)));
		assertEquals(OrderTriggerInstruction.StopLoss, order.getTriggerInstruction());
		
		ClientOrderMutator.INSTANCE.changePropertyValue(order, getObjectProperty(OrderPropertyNames.EXPIRATION_INSTRUCTION_PROPERTY, new OrderExpirationInstructionWrapper(OrderExpirationInstruction.GoodTillCancel)));
		assertEquals(OrderExpirationInstruction.GoodTillCancel, order.getExpirationInstruction());
	}
	
	public void testPropertyListInClient() throws Exception {
		Order order = new Order();
		
		try{
			ClientOrderMutator.INSTANCE.changePropertyOnList(order, getBooleanProperty("Bogus", false), "BogusList");
			fail();
		} catch (UnsupportedPropertyException e) {
			// expected
		} catch (Exception e) {
			fail();
		}
		
		ClientOrderMutator.INSTANCE.changePropertyOnList(order, getBooleanProperty("Test1", true), OrderPropertyNames.TRIGGER_PROPERTY_LIST);
		ClientOrderMutator.INSTANCE.addPropertyToList(order, getIntProperty("Test2", 1), OrderPropertyNames.TRIGGER_PROPERTY_LIST);
		ClientOrderMutator.INSTANCE.addPropertyToList(order, getDoubleProperty("Test3", 10.0), OrderPropertyNames.TRIGGER_PROPERTY_LIST);
		
		assertEquals(3, order.getTriggerProperties().length);
		
		Property[] sortedProperties = sortProperties(order.getTriggerProperties());
		
		assertEquals("Test1", ((BooleanProperty) sortedProperties[0]).getName());
		assertEquals(true, ((BooleanProperty) sortedProperties[0]).getValue());
		
		assertEquals("Test2", ((IntProperty) sortedProperties[1]).getName());
		assertEquals(1, ((IntProperty) sortedProperties[1]).getValue());
		
		assertEquals("Test3", ((DoubleProperty) sortedProperties[2]).getName());
		assertEquals(10.0, ((DoubleProperty) sortedProperties[2]).getValue());
		
		try{
			ClientOrderMutator.INSTANCE.addPropertyToList(order, getBooleanProperty("Test1", true), OrderPropertyNames.TRIGGER_PROPERTY_LIST);
			fail();
		} catch (DuplicatePropertyException e) {
			// expected
		} catch (Exception e) {
			fail();
		}
		
		assertEquals(3, order.getTriggerProperties().length);
		
		ClientOrderMutator.INSTANCE.changePropertyOnList(order, getIntProperty("Test1", 1), OrderPropertyNames.TRIGGER_PROPERTY_LIST);
		
		assertEquals(3, order.getTriggerProperties().length);
		
		sortedProperties = sortProperties(order.getTriggerProperties());
		
		assertEquals("Test1", ((IntProperty) sortedProperties[0]).getName());
		assertEquals(1, ((IntProperty) sortedProperties[0]).getValue());
		
		assertEquals("Test2", ((IntProperty) sortedProperties[1]).getName());
		assertEquals(1, ((IntProperty) sortedProperties[1]).getValue());
		
		assertEquals("Test3", ((DoubleProperty) sortedProperties[2]).getName());
		assertEquals(10.0, ((DoubleProperty) sortedProperties[2]).getValue());
		
		
		ClientOrderMutator.INSTANCE.changePropertyOnList(order, getIntProperty("Test2", 2), OrderPropertyNames.TRIGGER_PROPERTY_LIST);
		
		assertEquals(3, order.getTriggerProperties().length);
		
		sortedProperties = sortProperties(order.getTriggerProperties());		
		
		assertEquals("Test1", ((IntProperty) sortedProperties[0]).getName());
		assertEquals(1, ((IntProperty) sortedProperties[0]).getValue());
		
		assertEquals("Test2", ((IntProperty) sortedProperties[1]).getName());
		assertEquals(2, ((IntProperty) sortedProperties[1]).getValue());
		
		assertEquals("Test3", ((DoubleProperty) sortedProperties[2]).getName());
		assertEquals(10.0, ((DoubleProperty) sortedProperties[2]).getValue());
		
		ClientOrderMutator.INSTANCE.removePropertyFromList(order, getIntProperty("Bogus", 2), OrderPropertyNames.TRIGGER_PROPERTY_LIST);
		
		assertEquals(3, order.getTriggerProperties().length);		
		
		ClientOrderMutator.INSTANCE.removePropertyFromList(order, getIntProperty("Test1", 2), OrderPropertyNames.TRIGGER_PROPERTY_LIST);
		
		assertEquals(2, order.getTriggerProperties().length);
		
		sortedProperties = sortProperties(order.getTriggerProperties());	
		
		assertEquals("Test2", ((IntProperty) sortedProperties[0]).getName());
		assertEquals(2, ((IntProperty) sortedProperties[0]).getValue());
		
		assertEquals("Test3", ((DoubleProperty) sortedProperties[1]).getName());
		assertEquals(10.0, ((DoubleProperty) sortedProperties[1]).getValue());
		
		assertEquals(0, order.getExpirationProperties().length);
		ClientOrderMutator.INSTANCE.addPropertyToList(order, getIntProperty("Test1", 1), OrderPropertyNames.EXPIRATION_PROPERTY_LIST);
		assertEquals(1, order.getExpirationProperties().length);
		ClientOrderMutator.INSTANCE.changePropertyOnList(order, getIntProperty("Test1", 2), OrderPropertyNames.EXPIRATION_PROPERTY_LIST);
		assertEquals(1, order.getExpirationProperties().length);
		assertEquals("Test1", ((IntProperty) order.getExpirationProperties()[0]).getName());
		assertEquals(2, ((IntProperty) order.getExpirationProperties()[0]).getValue());
		ClientOrderMutator.INSTANCE.removePropertyFromList(order, getIntProperty("Test1", 2), OrderPropertyNames.EXPIRATION_PROPERTY_LIST);
		assertEquals(0, order.getExpirationProperties().length);
		
		assertEquals(0, order.getOrderProperties().length);
		ClientOrderMutator.INSTANCE.addPropertyToList(order, getIntProperty("Test1", 1), OrderPropertyNames.ORDER_PROPERTY_LIST);
		assertEquals(1, order.getOrderProperties().length);
		ClientOrderMutator.INSTANCE.changePropertyOnList(order, getIntProperty("Test1", 2), OrderPropertyNames.ORDER_PROPERTY_LIST);
		assertEquals(1, order.getOrderProperties().length);
		assertEquals("Test1", ((IntProperty) order.getOrderProperties()[0]).getName());
		assertEquals(2, ((IntProperty) order.getOrderProperties()[0]).getValue());
		ClientOrderMutator.INSTANCE.removePropertyFromList(order, getIntProperty("Test1", 2), OrderPropertyNames.ORDER_PROPERTY_LIST);
		assertEquals(0, order.getOrderProperties().length);
	}
	
	public void testExecuteChangeInClient() throws Exception {
		Order order = new Order();
		try{
			ClientOrderMutator.INSTANCE.executePropertyChanges(order, new PropertyChange[] { getValueListChange(ChangeAction.Add, null, OrderPropertyNames.TRIGGER_PROPERTY_LIST) }, null);
			fail();
		} catch (UnsupportedPropertyValueException e) {
			// expected
		} catch (Exception e) {
			fail();
		}
		try{
			ClientOrderMutator.INSTANCE.executePropertyChanges(order, new PropertyChange[] { getValueListChange(null, getBooleanProperty("Test", true), OrderPropertyNames.TRIGGER_PROPERTY_LIST) }, null);
			fail();
		} catch (UnsupportedPropertyChangeException e) {
			// expected
		} catch (Exception e) {
			fail();
		}
		ClientOrderMutator.INSTANCE.executePropertyChanges(order, new PropertyChange[] { getValueListChange(ChangeAction.Add, getBooleanProperty("Test", true), OrderPropertyNames.TRIGGER_PROPERTY_LIST) }, null);
		
		assertEquals(1, order.getTriggerProperties().length);
		
		ClientOrderMutator.INSTANCE.executePropertyChanges(order, new PropertyChange[] { getValueListChange(ChangeAction.ValueChange, getBooleanProperty("Test", false), OrderPropertyNames.TRIGGER_PROPERTY_LIST) }, null);
		
		assertEquals(1, order.getTriggerProperties().length);
		assertEquals(false, ((BooleanProperty) order.getTriggerProperties()[0]).getValue());
		
		ClientOrderMutator.INSTANCE.executePropertyChanges(order, new PropertyChange[] { getValueListChange(ChangeAction.Remove, getBooleanProperty("Test", true), OrderPropertyNames.TRIGGER_PROPERTY_LIST) }, null);
		
		assertEquals(0, order.getTriggerProperties().length);
		
		try {
			ClientOrderMutator.INSTANCE.executePropertyChanges(order, new PropertyChange[] { getPropertyChange(getIntProperty(OrderPropertyNames.LIMIT_QUOTE_VALUE_PROPERTY, 100)) }, null);
			fail();	
		} catch (UnsupportedPropertyValueException e) { 
			// Expected
		} catch (Exception e) {
			fail();
		}
		assertEquals(null, order.getLimitQuoteValue());		
		
		ClientOrderMutator.INSTANCE.executePropertyChanges(order, new PropertyChange[] { getPropertyChange(getDoubleProperty(OrderPropertyNames.LIMIT_QUOTE_VALUE_PROPERTY, 100)) }, null);
		assertEquals(100.0, order.getLimitQuoteValue().getQuoteValue());	
	}
	
	public void testOrderPropertyTransformers() throws Exception {
		testPropertyTransformation(OrderPropertyNames.STATE_PROPERTY, 
				new OrderStateWrapper(OrderState.Canceled));
		testPropertyTransformation(OrderPropertyNames.SIDE_PROPERTY, 
				new OrderSideWrapper(OrderSide.Sell));
		testPropertyTransformation(OrderPropertyNames.TYPE_PROPERTY, 
				new OrderTypeWrapper(OrderType.Limit));
		testPropertyTransformation(OrderPropertyNames.TRIGGER_INSTRUCTION_PROPERTY, 
				new OrderTriggerInstructionWrapper(OrderTriggerInstruction.TrailingStopLoss));
		testPropertyTransformation(OrderPropertyNames.EXPIRATION_INSTRUCTION_PROPERTY, 
				new OrderExpirationInstructionWrapper(OrderExpirationInstruction.GoodTillCancel));
	}

	private void testPropertyTransformation(String name_, TransferableObject value_) {
		Property transformed = ClientOrderMutator.INSTANCE.transform(PropertyUtils.createObjectProperty(name_, value_));
		assertEquals(name_, transformed.getName());
		assertEquals(value_, 
				((ObjectProperty)ClientOrderMutator.INSTANCE.transformBack(transformed)).getValue());
	}
}
