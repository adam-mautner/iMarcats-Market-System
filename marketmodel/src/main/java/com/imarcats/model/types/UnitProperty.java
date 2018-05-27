package com.imarcats.model.types;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.imarcats.model.meta.DataLengths;

/**
 * Defines a Name, String Unit pair
 * @author Adam
 */
@Entity
@Table(name="UNIT_PROPERTY")
public class UnitProperty implements Property {
	
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
	 * Name of the Unit Property
	 * Required
	 */
	@Column(name="NAME", nullable=false, length=DataLengths.MARKET_OBJECT_PROPERTY_NAME_LENGTH)
	private String _name; 
	
	/**
	 * String Unit of the Property
	 * Required
	 */
	@Column(name="UNIT", nullable=false, length=DataLengths.UNIT_LENGTH)
	private String _unit;

	public String getName() {
		return _name;
	}

	public void setName(String name_) {
		_name = name_;
	}

	public String getUnit() {
		return _unit;
	}

	public void setUnit(String unit_) {
		_unit = unit_;
	}

	public PropertyType getPropertyType() {
		return PropertyType.Unit;
	} 
	
	@Override
	public Property cloneProperty() {
		UnitProperty newProperty = new UnitProperty();
		newProperty.setName(getName());
		newProperty.setUnit(getUnit());
		return newProperty;
	}
	
	@Override
	public boolean equalsProperty(Property otherProperty_) {
		boolean equals = false;
		if(otherProperty_ == this) {
			equals = true;
		} else if(otherProperty_ instanceof UnitProperty) {
			UnitProperty otherUnitProperty = (UnitProperty) otherProperty_;
			equals = getName().equals(otherUnitProperty.getName()) && 
					 getUnit().equals(otherUnitProperty.getUnit());
		}
		
		return equals;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((_id == null) ? 0 : _id.hashCode());
		result = prime * result + ((_name == null) ? 0 : _name.hashCode());
		result = prime * result + ((_unit == null) ? 0 : _unit.hashCode());
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
		UnitProperty other = (UnitProperty) obj;
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
		return true;
	}

	@Override
	public String toString() {
		return "UnitProperty [_id=" + _id + ", _name=" + _name + ", _unit="
				+ _unit + "]";
	}
}
