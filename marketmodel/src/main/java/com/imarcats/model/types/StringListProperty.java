package com.imarcats.model.types;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.imarcats.model.meta.DataLengths;

/**
 * Defines a Name, String List pair
 * @author Adam
 */
@Entity
@Table(name="STRING_LIST_PROPERTY")
public class StringListProperty implements Property {
	
	private static final long serialVersionUID = 1L;

	/**
	 * Primary Key of the Property
	 * Note: No need for accessing this field directly
	 */	
	@Id
	@GeneratedValue 
	@Column(name="ID")
    private Long _key;
	
	/**
	 * Name of the String List Property
	 * Required
	 */
	@Column(name="NAME", nullable=false, length=DataLengths.MARKET_OBJECT_PROPERTY_NAME_LENGTH)
	private String _name; 
	
	/**
	 * String List of the Property
	 * Required
	 */
	@ElementCollection(targetClass=String.class)
    @CollectionTable(name="STRING_LIST_PROPERTY_VALUES")
	@Column(name="VALUE", nullable=false)
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
	public Property cloneProperty() {
		StringListProperty newProperty = new StringListProperty();
		newProperty.setName(getName());
		newProperty.setList(cloneList(getList()));
		return newProperty;
	}

	private List<String> cloneList(List<String> list_) {
		List<String> newList = new ArrayList<String>();
		for (String string : list_) {
			newList.add(string);
		}
		
		return newList;
	}
	
	@Override
	public boolean equalsProperty(Property otherProperty_) {
		boolean equals = false;
		if(otherProperty_ == this) {
			equals = true;
		} else if(otherProperty_ instanceof StringListProperty) {
			StringListProperty otherListProperty = (StringListProperty) otherProperty_;
			equals = getName().equals(otherListProperty.getName()) && 
					 (getList() != null ? getList().equals(otherListProperty.getList()) : otherListProperty.getList() == null);
		}
		
		return equals;
	}
	
	@Override
	public PropertyType getPropertyType() {
		return PropertyType.StringList;
	}

	@Override
	public String toString() {
		return "StringListProperty [_key=" + _key + ", _name=" + _name
				+ ", _list=" + _list + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((_key == null) ? 0 : _key.hashCode());
		result = prime * result + ((_list == null) ? 0 : _list.hashCode());
		result = prime * result + ((_name == null) ? 0 : _name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		StringListProperty other = (StringListProperty) obj;
		if (_key == null) {
			if (other._key != null)
				return false;
		} else if (!_key.equals(other._key))
			return false;
		if (_list == null) {
			if (other._list != null)
				return false;
		} else if (!_list.equals(other._list))
			return false;
		if (_name == null) {
			if (other._name != null)
				return false;
		} else if (!_name.equals(other._name))
			return false;
		return true;
	}

}
