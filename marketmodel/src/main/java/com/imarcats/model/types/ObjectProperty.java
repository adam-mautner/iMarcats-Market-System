package com.imarcats.model.types;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import com.imarcats.model.meta.DataLengths;

/**
 * Defines a Name, Object Value pair
 * @author Adam
 */
@Entity
@Table(name="OBJECT_PROPERTY")
public class ObjectProperty implements Property {

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
	 * Name of the Property
	 * Required
	 */
	@Column(name="NAME", nullable=false, length=DataLengths.MARKET_OBJECT_PROPERTY_NAME_LENGTH)
	private String _name; 
	
	/**
	 * Object Value of the Property
	 * Required
	 */
	@Column(name="VALUE", nullable=false)
	@Lob
	private TransferableObject _value;

	public String getName() {
		return _name;
	}

	public void setName(String name_) {
		_name = name_;
	}

	public TransferableObject getValue() {
		return _value;
	}

	public void setValue(TransferableObject value_) {
		_value = value_;
	}
	
	public PropertyType getPropertyType() {
		return PropertyType.Object;
	}	
	
	@Override
	public Property cloneProperty() {
		ObjectProperty newProperty = new ObjectProperty();
		newProperty.setName(getName());
		newProperty.setValue(getValue());
		return newProperty;
	}	
	
	@Override
	public boolean equalsProperty(Property otherProperty_) {
		boolean equals = false;
		if(otherProperty_ == this) {
			equals = true;
		} else if(otherProperty_ instanceof ObjectProperty) {
			ObjectProperty otherObjectProperty = (ObjectProperty) otherProperty_;
			equals = getName().equals(otherObjectProperty.getName()) && 
					 getValue().equals(otherObjectProperty.getValue());
		}
		
		return equals;
	}

	@Override
	public String toString() {
		return "ObjectProperty [_name=" + _name + ", _value=" + _value + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((_key == null) ? 0 : _key.hashCode());
		result = prime * result + ((_name == null) ? 0 : _name.hashCode());
		result = prime * result + ((_value == null) ? 0 : _value.hashCode());
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
		ObjectProperty other = (ObjectProperty) obj;
		if (_key == null) {
			if (other._key != null)
				return false;
		} else if (!_key.equals(other._key))
			return false;
		if (_name == null) {
			if (other._name != null)
				return false;
		} else if (!_name.equals(other._name))
			return false;
		if (_value == null) {
			if (other._value != null)
				return false;
		} else if (!_value.equals(other._value))
			return false;
		return true;
	}	
	
}