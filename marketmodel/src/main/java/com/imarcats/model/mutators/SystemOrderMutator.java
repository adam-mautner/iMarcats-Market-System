package com.imarcats.model.mutators;

import com.imarcats.model.Order;
import com.imarcats.model.OrderPropertyNames;
import com.imarcats.model.mutators.helpers.BooleanWrapper;
import com.imarcats.model.mutators.helpers.LongWrapper;
import com.imarcats.model.mutators.helpers.OrderStateWrapper;
import com.imarcats.model.types.AuditInformation;
import com.imarcats.model.types.DateProperty;
import com.imarcats.model.types.Property;
import com.imarcats.model.types.IntProperty;
import com.imarcats.model.types.ObjectProperty;
import com.imarcats.model.types.Quote;
import com.imarcats.model.types.StringProperty;


/**
 * Changes a Property of the Order, this should be used when the System 
 * changes a Property of an Order (typically in used in the Client to update
 * Order, when the Order Property Change Notification arrives to Client). 
 * 
 * This Mutator changes the Properties controlled by the System.  
 * @author Adam
 */
public class SystemOrderMutator extends MutatorBase {
	
	public static final SystemOrderMutator INSTANCE = new SystemOrderMutator();
	
	/** singleton */ 
	private SystemOrderMutator() { 
		// System Properties 
		_mapPropertyNameToMutator.put(OrderPropertyNames.KEY_PROPERTY, new ObjectPropertyMutator<Order, ObjectProperty>() {
			@Override
			public void changeProperty(Order order_, ObjectProperty property_) {
				order_.setKey(((LongWrapper) property_.getValue()).getValue());
			}
			
		});
		_mapPropertyNameToMutator.put(OrderPropertyNames.SUBMITTER_ID_PROPERTY, new ObjectPropertyMutator<Order, StringProperty>() {
			@Override
			public void changeProperty(Order order_, StringProperty property_) {
				order_.setSubmitterID(property_.getValue());
			}
			
		});
		_mapPropertyNameToMutator.put(OrderPropertyNames.TARGET_MARKET_CODE_PROPERTY, new ObjectPropertyMutator<Order, StringProperty>() {
			@Override
			public void changeProperty(Order order_, StringProperty property_) {
				order_.setTargetMarketCode(property_.getValue());
			}
			
		});
		_mapPropertyNameToMutator.put(OrderPropertyNames.EXECUTED_SIZE_PROPERTY, new ObjectPropertyMutator<Order, IntProperty>() {
			@Override
			public void changeProperty(Order order_, IntProperty property_) {
				order_.setExecutedSize((int) property_.getValue());
			}
			
		});
		_mapPropertyNameToMutator.put(OrderPropertyNames.STATE_PROPERTY, new ObjectPropertyMutator<Order, ObjectProperty>() {
			@Override
			public void changeProperty(Order order_, ObjectProperty property_) {
				order_.setState(((OrderStateWrapper) property_.getValue()).getValue());
			}
			
		});
		_mapPropertyNameToMutator.put(OrderPropertyNames.SUBMISSION_DATE_PROPERTY, new ObjectPropertyMutator<Order, DateProperty>() {
			@Override
			public void changeProperty(Order order_, DateProperty property_) {
				order_.setSubmissionDate(property_.getValue());
			}
			
		});
		_mapPropertyNameToMutator.put(OrderPropertyNames.CURRENT_STOP_QUOTE_PROPERTY, new ObjectPropertyMutator<Order, ObjectProperty>() {
			@Override
			public void changeProperty(Order order_, ObjectProperty property_) {
				order_.setCurrentStopQuote((Quote) property_.getValue());
			}
			
		});
		_mapPropertyNameToMutator.put(OrderPropertyNames.CREATION_AUDIT_PROPERTY, new ObjectPropertyMutator<Order, ObjectProperty>() {
			@Override
			public void changeProperty(Order order_, ObjectProperty property_) {
				order_.setCreationAudit((AuditInformation) property_.getValue());
			}
			
		});
		_mapPropertyNameToMutator.put(OrderPropertyNames.QUOTE_CHANGE_TRIGGER_KEY_PROPERTY, new ObjectPropertyMutator<Order, IntProperty>() {
			@Override
			public void changeProperty(Order order_, IntProperty property_) {
				order_.setQuoteChangeTriggerKey(property_.getValue());
			}
			
		});
		_mapPropertyNameToMutator.put(OrderPropertyNames.EXPIRATION_TRIGGER_ACTION_KEY_PROPERTY, new ObjectPropertyMutator<Order, IntProperty>() {
			@Override
			public void changeProperty(Order order_, IntProperty property_) {
				order_.setExpirationTriggerActionKey(property_.getValue());
			}
			
		});
		_mapPropertyNameToMutator.put(OrderPropertyNames.COMMISSION_CHARGED_PROPERTY, new ObjectPropertyMutator<Order, ObjectProperty>() {
			@Override
			public void changeProperty(Order order_, ObjectProperty property_) {
				order_.setCommissionCharged(((BooleanWrapper) property_.getValue()).getValue());
			}
			
		});
		_mapPropertyNameToMutator.put(OrderPropertyNames.CANCELLATION_COMMENT_LANGUAGE_KEY_PROPERTY, new ObjectPropertyMutator<Order, StringProperty>() {
			@Override
			public void changeProperty(Order order_, StringProperty property_) {
				order_.setCancellationCommentLanguageKey(property_.getValue());
			}
			
		});
		// End of System Properties 
	}
	
	/**
	 * Gets Property Mutator for the System Properties or for User Properties
	 * @param property_ Property
	 * @return Property Mutator
	 */
	@SuppressWarnings("unchecked")
	protected ObjectPropertyMutator getPropertyMutator(Property property_) {
		ObjectPropertyMutator propertyMutator = super.getPropertyMutator(property_);

		if(propertyMutator == null) {
			propertyMutator = ClientOrderMutator.INSTANCE.getPropertyMutator(property_);
		}
		
		return propertyMutator;
	}
	
	/**
	 * Gets System or User Property List Accessor for the given List Name
	 * @param listName_ List Name 
	 * @return Accessor 
	 */
	@SuppressWarnings("unchecked")
	protected ObjectPropertyListAccessor getPropertyListAccessor(String listName_) {
		ObjectPropertyListAccessor propertyListAccessor = super.getPropertyListAccessor(listName_);
		
		if(propertyListAccessor == null) {
			propertyListAccessor = ClientOrderMutator.INSTANCE.getPropertyListAccessor(listName_);
		}
		
		return propertyListAccessor;
	}
}
