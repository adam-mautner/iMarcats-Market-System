package com.imarcats.interfaces.client.v100.dto.types;

import java.util.ArrayList;
import java.util.List;

/**
 * Defines a Name, String List pair
 * @author Adam
 */
public class StringListPropertyDto implements PropertyDto {
	
	/**
	 * Name of the String List Property
	 * Required
	 */
//	@Column(name="NAME", nullable=false, length=DataLengths.MARKET_OBJECT_PROPERTY_NAME_LENGTH)
	private String _name; 
	
	/**
	 * String List of the Property
	 * Required
	 */
	private List<String> _list = new ArrayList<String>();

	public String getName() {
		return _name;
	}

	public void setName(String name_) {
		_name = name_;
	}

	public List<String> getList() {
		return _list;
	}

	public void setList(List<String> list_) {
		_list = list_;
	}

	@Override
	public PropertyType getPropertyType() {
		return PropertyType.StringList;
	}
}
