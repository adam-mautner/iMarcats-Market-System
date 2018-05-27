package com.imarcats.model.types;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.imarcats.model.meta.DataLengths;

/**
 * Defines a Name, Date Value pair
 * @author Adam
 */
@Entity
@Table(name="DATE_PROPERTY")
public class DateProperty implements Property {

	private static final long serialVersionUID = 1L;

	/**
	 * Primary Key of the Property
	 * Note: No need for accessing this field directly
	 */	
	@Id
	@GeneratedValue 
	@Column(name="ID")
    private Long _id;
	
	/**
	 * Name of the Property
	 * Required
	 */
	@Column(name="NAME", nullable=false, length=DataLengths.MARKET_OBJECT_PROPERTY_NAME_LENGTH)
	private String _name; 
	
	/**
	 * Date Value of the Property
	 * Required
	 */ 
	@Column(name="VALUE", nullable=false)
	// TODO: Convert this to java.sql.Date once TimestampPropery DTO created for pushing updates on submission date
	private Timestamp _value;

	public String getName() {
		return _name;
	}

	public void setName(String name_) {
		_name = name_;
	}

	public Date getValue() {
		return _value;
	}

	public void setValue(Date value_) {
		_value = value_ != null ? new Timestamp(value_.getTime()) : null;
	}

	public PropertyType getPropertyType() {
		return PropertyType.Date;
	}	
	
	@Override
	public Property cloneProperty() {
		DateProperty newProperty = new DateProperty();
		newProperty.setName(getName());
		newProperty.setValue(getValue());
		return newProperty;
	}	
	
	@Override
	public boolean equalsProperty(Property otherProperty_) {
		boolean equals = false;
		if(otherProperty_ == this) {
			equals = true;
		} else if(otherProperty_ instanceof DateProperty) {
			DateProperty otherDateProperty = (DateProperty) otherProperty_;
			equals = getName().equals(otherDateProperty.getName()) && 
					 getValue().equals(otherDateProperty.getValue());
		}
		
		return equals;
	}

	@Override
	public String toString() {
		return "DateProperty [_id=" + _id + ", _name=" + _name + ", _value="
				+ _value + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((_id == null) ? 0 : _id.hashCode());
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
		DateProperty other = (DateProperty) obj;
		if (_id == null) {
			if (other._id != null)
				return false;
		} else if (!_id.equals(other._id))
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
