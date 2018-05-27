package com.imarcats.model.types;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.imarcats.model.meta.DataLengths;

/**
 * Defines a Name, Double Value pair
 * 
 * @author Adam
 */
@Entity
@Table(name = "DOUBLE_PROPERTY")
public class DoubleProperty implements Property {

	private static final long serialVersionUID = 1L;

	/**
	 * Primary Key of the Property Note: No need for accessing this field
	 * directly
	 */
	@Id
	@GeneratedValue 
	@Column(name = "ID")
	private Long _id;

	/**
	 * Name of the Property Required
	 */
	@Column(name = "NAME", nullable = false, length=DataLengths.MARKET_OBJECT_PROPERTY_NAME_LENGTH)
	private String _name;

	/**
	 * Double Value of the Property Required
	 */
	@Column(name = "VALUE", nullable = false)
	private Double _value;

	/**
	 * Unit of the Property Optional
	 */
	@Column(name = "UNIT", length=DataLengths.UNIT_LENGTH)
	private String _unit;

	public String getName() {
		return _name;
	}

	public void setName(String name_) {
		_name = name_;
	}

	public Double getValue() {
		return _value;
	}

	public void setValue(Double value_) {
		_value = value_;
	}

	public PropertyType getPropertyType() {
		return PropertyType.Double;
	}

	@Override
	public Property cloneProperty() {
		DoubleProperty newProperty = new DoubleProperty();
		newProperty.setName(getName());
		newProperty.setValue(getValue());
		newProperty.setUnit(getUnit());
		return newProperty;
	}

	@Override
	public boolean equalsProperty(Property otherProperty_) {
		boolean equals = false;
		if (otherProperty_ == this) {
			equals = true;
		} else if (otherProperty_ instanceof DoubleProperty) {
			DoubleProperty otherDoubleProperty = (DoubleProperty) otherProperty_;
			equals = getName().equals(otherDoubleProperty.getName())
					&& getValue().equals(otherDoubleProperty.getValue())
					&& (getUnit() != null ? getUnit().equals(
							otherDoubleProperty.getUnit())
							: otherDoubleProperty.getUnit() == null);
		}

		return equals;
	}

	public void setUnit(String unit) {
		_unit = unit;
	}

	public String getUnit() {
		return _unit;
	}

	@Override
	public String toString() {
		return "DoubleProperty [_id=" + _id + ", _name=" + _name
				+ ", _value=" + _value + ", _unit=" + _unit + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((_id == null) ? 0 : _id.hashCode());
		result = prime * result + ((_name == null) ? 0 : _name.hashCode());
		result = prime * result + ((_unit == null) ? 0 : _unit.hashCode());
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
		DoubleProperty other = (DoubleProperty) obj;
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
		if (_unit == null) {
			if (other._unit != null)
				return false;
		} else if (!_unit.equals(other._unit))
			return false;
		if (_value == null) {
			if (other._value != null)
				return false;
		} else if (!_value.equals(other._value))
			return false;
		return true;
	}
}
