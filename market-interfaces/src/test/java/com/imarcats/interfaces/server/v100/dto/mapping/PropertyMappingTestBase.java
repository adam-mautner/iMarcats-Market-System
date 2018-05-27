package com.imarcats.interfaces.server.v100.dto.mapping;

import com.imarcats.interfaces.client.v100.dto.types.PropertyListValueChangeDto;
import com.imarcats.interfaces.client.v100.dto.types.PropertyValueChangeDto;
import com.imarcats.interfaces.server.v100.dto.mapping.PropertyDtoMapping;
import com.imarcats.model.mutators.ChangeAction;
import com.imarcats.model.mutators.PropertyListValueChange;
import com.imarcats.model.mutators.PropertyValueChange;
import com.imarcats.model.test.testutils.MarketObjectTestBase;
import com.imarcats.model.types.Property;

public class PropertyMappingTestBase extends MarketObjectTestBase {
	protected void testObjectProperties(Property property,
			PropertyListValueChange listChange) {
		PropertyListValueChangeDto listChangeDto = PropertyDtoMapping.INSTANCE.toDto(listChange);
		PropertyListValueChange listChangeMapped = PropertyDtoMapping.INSTANCE.fromDto(listChangeDto); 
		
		checkListValueChange(listChange, listChangeMapped);

		PropertyValueChange valueChange = createValueChange(property);
		
		PropertyValueChangeDto valueChangeDto = PropertyDtoMapping.INSTANCE.toDto(valueChange);
		PropertyValueChange valueChangeMapped = PropertyDtoMapping.INSTANCE.fromDto(valueChangeDto); 
		
		assertEqualsProperty(valueChange.getProperty(), valueChangeMapped.getProperty());
	}
	
	protected PropertyValueChange createValueChange(Property property) {
		PropertyValueChange valueChange = new PropertyValueChange();
		valueChange.setProperty(property);
		return valueChange;
	}

	protected void checkListValueChange(PropertyListValueChange listChange,
			PropertyListValueChange listChangeMapped) {
		assertEquals(listChange.getPropertyListName(), listChangeMapped.getPropertyListName());
		assertEquals(listChange.getChangeAction(), listChangeMapped.getChangeAction());
		assertEqualsProperty(listChange.getProperty(), listChangeMapped.getProperty());
	}

	protected PropertyListValueChange createListValueChange(Property property) {
		PropertyListValueChange listChange = new PropertyListValueChange();
		listChange.setProperty(property);
		listChange.setChangeAction(ChangeAction.Add);
		listChange.setPropertyListName("TestName");
		return listChange;
	}
}
