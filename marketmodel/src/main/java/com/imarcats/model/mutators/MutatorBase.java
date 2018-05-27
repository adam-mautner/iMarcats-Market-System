package com.imarcats.model.mutators;

import java.util.HashMap;
import java.util.Map;

import com.imarcats.model.types.Property;
import com.imarcats.model.utils.PropertyUtils;

/**
 * Common Base Class for Mutators
 * @author Adam
 */
public class MutatorBase {

	protected final Map<String, ObjectPropertyMutator> _mapPropertyNameToMutator = new HashMap<String, ObjectPropertyMutator>();

	protected final Map<String, ObjectPropertyListAccessor> _mapPropertyListNameToAccessor = new HashMap<String, ObjectPropertyListAccessor>();	
	protected final Map<String, PropertyTransformer> _propertyTransformer = new HashMap<String, PropertyTransformer>();	
	
	/**
	 * Transforms Property to other Property (possibly String Property)
	 * @param property_ Property 
	 * @return Transformed Property 
	 */
	public Property transform(Property property_) {
		return getTransformer(property_).transform(property_);
	}
	
	/**
	 * Transforms Property back (possibly String Property to Object Property)
	 * @param property_ Transformed Property 
	 * @return Original Property 
	 */
	public Property transformBack(Property property_) {
		return getTransformer(property_).transformBack(property_);
	}

	private PropertyTransformer getTransformer(Property property_) {
		PropertyTransformer transformer = _propertyTransformer.get(property_.getName());
		if(transformer == null) {
			transformer = PropertyTransformer.NULL;
		}
		return transformer;
	}
	
	
	/**
	 * Executes Property Change 
	 * @param object_ Object hold properties 
	 * @param changes_ Changes in Properties
	 * @param listener_ Call-back Listener of Property Changes (used for sending out notification on property changes)
	 */
	public void executePropertyChanges(Object object_, PropertyChange[] changes_, PropertyChangeListener listener_) {
		if(changes_ == null) {
			throw new UnsupportedPropertyChangeException(object_, "", ""+changes_);
		}
		for (PropertyChange propertyChange : changes_) {
			executePropertyChange(object_, propertyChange);
			if(listener_ != null) {
				listener_.propertyChanged(object_, propertyChange);
			}
		}
	}
	
	/**
	 * Executes a Change of Property 
	 * @param object_ Object hold properties 
	 * @param change_ Change of Property
	 */
	protected void executePropertyChange(Object object_, PropertyChange change_) {
		if(change_ instanceof PropertyValueChange) {
			executePropertyValueChange(object_, (PropertyValueChange) change_);
		} else if(change_ instanceof PropertyListValueChange) {
			executeValueListChange(object_, (PropertyListValueChange) change_);
		} else {
			throw new UnsupportedPropertyChangeException(object_, "", ""+change_);
		}
	}
	
	/**
	 * Executes a Change of Property Value 
	 * @param object_ Object hold properties 
	 * @param valueChange_ Change of Value
	 */
	protected void executePropertyValueChange(Object object_, PropertyValueChange valueChange_) {
		changePropertyValue(object_, valueChange_.getProperty());
	}
	
	/**
	 * Executes a Change of Property Value List 
	 * @param object_ Object hold properties 
	 * @param listValueChange_ Change of the List
	 */
	protected void executeValueListChange(Object object_, PropertyListValueChange listValueChange_) {
		if(listValueChange_.getProperty() == null && listValueChange_.getChangeAction() != ChangeAction.Clear) {
			throw new UnsupportedPropertyValueException(object_, "Null", listValueChange_.getProperty());
		}
		
		if(listValueChange_.getChangeAction() == ChangeAction.Add) {
			addPropertyToList(object_, listValueChange_.getProperty(), listValueChange_.getPropertyListName());
		} else if(listValueChange_.getChangeAction() == ChangeAction.Remove) {
			removePropertyFromList(object_, listValueChange_.getProperty(), listValueChange_.getPropertyListName());
		} else if(listValueChange_.getChangeAction() == ChangeAction.ValueChange) {
			changePropertyOnList(object_, listValueChange_.getProperty(), listValueChange_.getPropertyListName());
		} else if(listValueChange_.getChangeAction() == ChangeAction.Clear) {
			clearPropertyList(object_, listValueChange_.getPropertyListName());
		} else {
			throw new UnsupportedPropertyChangeException(object_, listValueChange_.getProperty().getName(), ""+listValueChange_.getChangeAction());
		}
	}
	
	/**
	 * Adds a Property to the given Property List 
	 * @param object_ Object hold properties 
	 * @param property_ Property
	 * @param propertyListName_ Property List Name
	 */
	@SuppressWarnings("unchecked")
	public void addPropertyToList(Object object_, Property property_, String propertyListName_) {
		ObjectPropertyListAccessor listAccessor = getAndCheckPropertyListAccessor(object_, propertyListName_);
		Property propertyFound = PropertyUtils.findProperty(listAccessor.getList(object_), property_);
		if(propertyFound != null) {
			throw new DuplicatePropertyException(object_, propertyListName_, property_);
		}
		
		listAccessor.addToList(object_, property_);
	}
	
	/**
	 * Removes a Property from the given Property List 
	 * @param object_ Object hold properties 
	 * @param property_ Property
	 * @param propertyListName_ Property List Name
	 */
	@SuppressWarnings("unchecked")
	public void removePropertyFromList(Object object_, Property property_, String propertyListName_) {
		ObjectPropertyListAccessor listAccessor = getAndCheckPropertyListAccessor(object_, propertyListName_);
		Property propertyFound = PropertyUtils.findProperty(listAccessor.getList(object_), property_);
		if(propertyFound != null) {
			listAccessor.deleteFromList(object_, propertyFound);
		}
	}
	
	/**
	 * Changes a Property on the given Property List 
	 * @param object_ Object hold properties 
	 * @param property_ Property
	 * @param propertyListName_ Property List Name
	 */
	public void changePropertyOnList(Object object_, Property property_, String propertyListName_) {
		removePropertyFromList(object_, property_, propertyListName_);
		addPropertyToList(object_, property_, propertyListName_);		
	}
	
	/**
	 * Clears the given Property List
	 * @param object_ Object hold properties 
	 * @param propertyListName_ Property List Name
	 */
	@SuppressWarnings("unchecked")
	protected void clearPropertyList(Object object_, String propertyListName_) {
		ObjectPropertyListAccessor listAccessor = getAndCheckPropertyListAccessor(object_, propertyListName_);
		listAccessor.clearList(object_);	
	}	
	
	/**
	 * Changes a Property Value 
	 * @param object_ Object hold properties 
	 * @param property_ Property
	 */
	@SuppressWarnings("unchecked")
	public void changePropertyValue(Object object_, Property property_) {
		if(property_ == null) {
			throw new UnsupportedPropertyValueException(object_, "Null", property_);
		}
		
		ObjectPropertyMutator propertyMutator = getPropertyMutator(property_);
		
		if(propertyMutator == null) {
			throw new UnsupportedPropertyException(object_, property_.getName());
		}
		try {
			propertyMutator.changeProperty(object_, property_);	
		} catch (Exception e) {
			throw new UnsupportedPropertyValueException(object_, property_.getName(), property_);
		}
	}
	
	/**
	 * Gets Property Mutator for the Property
	 * @param property_ Property
	 * @return Property Mutator
	 */
	@SuppressWarnings("unchecked")
	protected ObjectPropertyMutator getPropertyMutator(Property property_) {
		ObjectPropertyMutator propertyMutator = _mapPropertyNameToMutator.get(property_.getName());
		return propertyMutator;
	}

	/**
	 * Gets Property and Checks List Accessor for the given List Name
	 * @param object_ Object hold properties 
	 * @param listName_ List Name 
	 * @return Accessor 
	 */
	@SuppressWarnings("unchecked")
	private ObjectPropertyListAccessor getAndCheckPropertyListAccessor(Object object_, String listName_) {
		ObjectPropertyListAccessor propertyListAccessor = getPropertyListAccessor(listName_);
		
		if(propertyListAccessor == null) {
			throw new UnsupportedPropertyException(object_, listName_);
		}
		
		return propertyListAccessor;
	}
	
	/**
	 * Gets Property List Accessor for the given List Name
	 * @param listName_ List Name 
	 * @return Accessor 
	 */
	@SuppressWarnings("unchecked")
	protected ObjectPropertyListAccessor getPropertyListAccessor(String listName_) {
		ObjectPropertyListAccessor propertyListAccessor = _mapPropertyListNameToAccessor.get(listName_);		
		return propertyListAccessor;
	}
}

