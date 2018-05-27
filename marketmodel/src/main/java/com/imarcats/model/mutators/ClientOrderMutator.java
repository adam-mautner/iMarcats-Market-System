package com.imarcats.model.mutators;

import com.imarcats.model.Order;
import com.imarcats.model.OrderPropertyNames;
import com.imarcats.model.mutators.helpers.OrderExpirationInstructionWrapper;
import com.imarcats.model.mutators.helpers.OrderSideWrapper;
import com.imarcats.model.mutators.helpers.OrderStateWrapper;
import com.imarcats.model.mutators.helpers.OrderTriggerInstructionWrapper;
import com.imarcats.model.mutators.helpers.OrderTypeWrapper;
import com.imarcats.model.types.BooleanProperty;
import com.imarcats.model.types.DoubleProperty;
import com.imarcats.model.types.Property;
import com.imarcats.model.types.IntProperty;
import com.imarcats.model.types.ObjectProperty;
import com.imarcats.model.types.OrderExpirationInstruction;
import com.imarcats.model.types.OrderSide;
import com.imarcats.model.types.OrderState;
import com.imarcats.model.types.OrderTriggerInstruction;
import com.imarcats.model.types.OrderType;
import com.imarcats.model.types.Quote;
import com.imarcats.model.types.StringProperty;
import com.imarcats.model.utils.PropertyUtils;

/**
 * Changes a Property of the Order, this should be used when the Client (User)
 * changes a Property of an Order (typically when calling Change Order Property Methods). 
 * 
 * This Mutator does not change the Properties controlled by the System.  
 * @author Adam
 */
public class ClientOrderMutator extends MutatorBase {

	public static final ClientOrderMutator INSTANCE = new ClientOrderMutator();
	
	/** singleton */
	private ClientOrderMutator() { 
		// User Properties 
		_mapPropertyNameToMutator.put(OrderPropertyNames.SIDE_PROPERTY, new ObjectPropertyMutator<Order, ObjectProperty>() {
			@Override
			public void changeProperty(Order order_, ObjectProperty property_) {
				order_.setSide(((OrderSideWrapper) property_.getValue()).getValue());
			}
			
		});
		_mapPropertyNameToMutator.put(OrderPropertyNames.TYPE_PROPERTY, new ObjectPropertyMutator<Order, ObjectProperty>() {
			@Override
			public void changeProperty(Order order_, ObjectProperty property_) {
				order_.setType(((OrderTypeWrapper) property_.getValue()).getValue());
			}
			
		});
		_mapPropertyNameToMutator.put(OrderPropertyNames.LIMIT_QUOTE_VALUE_PROPERTY, new ObjectPropertyMutator<Order, DoubleProperty>() {
			@Override
			public void changeProperty(Order order_, DoubleProperty property_) {
				order_.setLimitQuoteValue(Quote.createQuote(property_.getValue()));
				
				// This has been removed to keep the Property Change System simple
				// set type to limit
				//order_.setType(OrderType.Limit);
			}
			
		});	
		_mapPropertyNameToMutator.put(OrderPropertyNames.SIZE_PROPERTY, new ObjectPropertyMutator<Order, IntProperty>() {
			@Override
			public void changeProperty(Order order_, IntProperty property_) {
				order_.setSize((int) property_.getValue());
			}
			
		});	
		_mapPropertyNameToMutator.put(OrderPropertyNames.MINIMUM_SIZE_OF_EXECUTION_PROPERTY, new ObjectPropertyMutator<Order, IntProperty>() {
			@Override
			public void changeProperty(Order order_, IntProperty property_) {
				order_.setMinimumSizeOfExecution((int) property_.getValue());
			}
			
		});	
		_mapPropertyNameToMutator.put(OrderPropertyNames.EXECUTE_ENTIRE_ORDER_AT_ONCE_PROPERTY, new ObjectPropertyMutator<Order, BooleanProperty>() {
			@Override
			public void changeProperty(Order order_, BooleanProperty property_) {
				order_.setExecuteEntireOrderAtOnce(property_.getValue());
			}
			
		});		
		_mapPropertyNameToMutator.put(OrderPropertyNames.DISPLAY_ORDER_PROPERTY, new ObjectPropertyMutator<Order, BooleanProperty>() {
			@Override
			public void changeProperty(Order order_, BooleanProperty property_) {
				order_.setDisplayOrder((boolean) property_.getValue());
			}
			
		});
		_mapPropertyNameToMutator.put(OrderPropertyNames.TRIGGER_INSTRUCTION_PROPERTY, new ObjectPropertyMutator<Order, ObjectProperty>() {
			@Override
			public void changeProperty(Order order_, ObjectProperty property_) {
				order_.setTriggerInstruction(((OrderTriggerInstructionWrapper) property_.getValue()).getValue());
			}
			
		});	
		_mapPropertyNameToMutator.put(OrderPropertyNames.EXPIRATION_INSTRUCTION_PROPERTY, new ObjectPropertyMutator<Order, ObjectProperty>() {
			@Override
			public void changeProperty(Order order_, ObjectProperty property_) {
				order_.setExpirationInstruction(((OrderExpirationInstructionWrapper) property_.getValue()).getValue());
			}
			
		});	
		_mapPropertyNameToMutator.put(OrderPropertyNames.TARGET_ACCOUNT_ID_PROPERTY, new ObjectPropertyMutator<Order, IntProperty>() {
			@Override
			public void changeProperty(Order order_, IntProperty property_) {
				 
				order_.setTargetAccountID(property_.getValue());
			}
			
		});		
		// End of User Properties 
		

		// List Properties 
		_mapPropertyListNameToAccessor.put(OrderPropertyNames.TRIGGER_PROPERTY_LIST, new ObjectPropertyListAccessor<Order>() {
			@Override
			public Property[] getList(Order order_) {
				return order_.getTriggerProperties();
			}

			@Override
			public void addToList(Order order_, Property property_) {
				order_.addTriggerProperty(property_);
			}

			@Override
			public void clearList(Order order_) {
				order_.clearTriggerProperties();
			}

			@Override
			public void deleteFromList(Order order_, Property property_) {
				order_.deleteTriggerProperty(property_);
			}
		});
		_mapPropertyListNameToAccessor.put(OrderPropertyNames.EXPIRATION_PROPERTY_LIST, new ObjectPropertyListAccessor<Order>() {
			@Override
			public Property[] getList(Order order_) {
				return order_.getExpirationProperties();
			}
			
			@Override
			public void addToList(Order order_, Property property_) {
				order_.addExpirationProperty(property_);
			}

			@Override
			public void clearList(Order order_) {
				order_.clearExpirationProperties();
			}

			@Override
			public void deleteFromList(Order order_, Property property_) {
				order_.deleteExpirationProperty(property_);
			}
		});
		_mapPropertyListNameToAccessor.put(OrderPropertyNames.ORDER_PROPERTY_LIST, new ObjectPropertyListAccessor<Order>() {
			@Override
			public Property[] getList(Order order_) {
				return order_.getOrderProperties();
			}
			
			@Override
			public void addToList(Order order_, Property property_) {
				order_.addOrderProperty(property_);
			}

			@Override
			public void clearList(Order order_) {
				order_.clearOrderProperties();
			}

			@Override
			public void deleteFromList(Order order_, Property property_) {
				order_.deleteOrderProperty(property_);
			}
		});
		// End of List Properties 
		
		// Properties Translator
		_propertyTransformer.put(OrderPropertyNames.EXPIRATION_INSTRUCTION_PROPERTY, new ObjectTransformerBase() {
			
			@Override
			public Property transformBack(Property property_) {
				String value = ((StringProperty)property_).getValue();
				OrderExpirationInstruction object = OrderExpirationInstruction.valueOf(value);
				return PropertyUtils.createObjectProperty(property_.getName(), new OrderExpirationInstructionWrapper(object));
			}
		});
		_propertyTransformer.put(OrderPropertyNames.SIDE_PROPERTY, new ObjectTransformerBase() {
			
			@Override
			public Property transformBack(Property property_) {
				String value = ((StringProperty)property_).getValue();
				OrderSide object = OrderSide.valueOf(value);
				return PropertyUtils.createObjectProperty(property_.getName(), new OrderSideWrapper(object));
			}
		});
		_propertyTransformer.put(OrderPropertyNames.STATE_PROPERTY, new ObjectTransformerBase() {
			
			@Override
			public Property transformBack(Property property_) {
				String value = ((StringProperty)property_).getValue();
				OrderState object = OrderState.valueOf(value);
				return PropertyUtils.createObjectProperty(property_.getName(), new OrderStateWrapper(object));
			}
		});
		_propertyTransformer.put(OrderPropertyNames.TRIGGER_INSTRUCTION_PROPERTY, new ObjectTransformerBase() {
			
			@Override
			public Property transformBack(Property property_) {
				String value = ((StringProperty)property_).getValue();
				OrderTriggerInstruction object = OrderTriggerInstruction.valueOf(value);
				return PropertyUtils.createObjectProperty(property_.getName(), new OrderTriggerInstructionWrapper(object));
			}
		});
		_propertyTransformer.put(OrderPropertyNames.TYPE_PROPERTY, new ObjectTransformerBase() {
			
			@Override
			public Property transformBack(Property property_) {
				String value = ((StringProperty)property_).getValue();
				OrderType object = OrderType.valueOf(value);
				return PropertyUtils.createObjectProperty(property_.getName(), new OrderTypeWrapper(object));
			}
		});
		// End of Properties Translator
	}
	
	private static abstract class ObjectTransformerBase implements PropertyTransformer {
		@Override
		public Property transform(Property property_) {
			Object value = ((ObjectProperty) property_).getValue().getObjectValue();
			return PropertyUtils.createStringProperty(property_.getName(), value.toString());
		}
	}
}
