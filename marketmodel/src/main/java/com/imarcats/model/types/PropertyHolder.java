package com.imarcats.model.types;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.imarcats.model.MarketModelObject;

/**
 * Hold properties of different type
 * @author Adam
 */
@Entity
@Table(name="PROPERTY_HOLDER")
public class PropertyHolder implements MarketModelObject {

	private static final long serialVersionUID = 1L;

	/**
	 * Primary Key of the Property Holder
	 * Note: No need for accessing this field directly
	 */	
	@Id
	@GeneratedValue 
	@Column(name = "ID")
    private Long _id;
	
	// TODO: Use explicit ordering clause ! (Find out, why this is not working) - We expect that this list will be short and will not be changed frequently, so it is OK for now to not sort it
	// TODO: @Order(extensions = @Extension(vendorName="datanucleus", key="list-ordering", value="_name asc"))
	@Column(name="INT_PROPERTIES")
	@OneToMany(cascade=CascadeType.ALL, orphanRemoval=true)
	private List<IntProperty> _intProperties = new ArrayList<IntProperty>();

	// TODO: Use explicit ordering clause ! (Find out, why this is not working) - We expect that this list will be short and will not be changed frequently, so it is OK for now to not sort it
	// TODO: @Order(extensions = @Extension(vendorName="datanucleus", key="list-ordering", value="_name asc"))
	@Column(name="DOUBLE_PROPERTIES")
	@OneToMany(cascade=CascadeType.ALL, orphanRemoval=true)
	private List<DoubleProperty> _doubleProperties = new ArrayList<DoubleProperty>();

	// TODO: Use explicit ordering clause ! (Find out, why this is not working) - We expect that this list will be short and will not be changed frequently, so it is OK for now to not sort it
	// TODO: @Order(extensions = @Extension(vendorName="datanucleus", key="list-ordering", value="_name asc"))
	@Column(name="STRING_PROPERTIES")
	@OneToMany(cascade=CascadeType.ALL, orphanRemoval=true)
	private List<StringProperty> _stringProperties = new ArrayList<StringProperty>();

	// TODO: Use explicit ordering clause ! (Find out, why this is not working) - We expect that this list will be short and will not be changed frequently, so it is OK for now to not sort it
	// TODO: @Order(extensions = @Extension(vendorName="datanucleus", key="list-ordering", value="_name asc"))
	@Column(name="BOOLEAN_PROPERTIES")
	@OneToMany(cascade=CascadeType.ALL, orphanRemoval=true)
	private List<BooleanProperty> _booleanProperties = new ArrayList<BooleanProperty>();

	// TODO: Use explicit ordering clause ! (Find out, why this is not working) - We expect that this list will be short and will not be changed frequently, so it is OK for now to not sort it
	// TODO: @Order(extensions = @Extension(vendorName="datanucleus", key="list-ordering", value="_name asc"))
	@Column(name="DATE_PROPERTIES")
	@OneToMany(cascade=CascadeType.ALL, orphanRemoval=true)
	private List<DateProperty> _dateProperties = new ArrayList<DateProperty>();

	// TODO: Use explicit ordering clause ! (Find out, why this is not working) - We expect that this list will be short and will not be changed frequently, so it is OK for now to not sort it
	// TODO: @Order(extensions = @Extension(vendorName="datanucleus", key="list-ordering", value="_name asc"))
	@Column(name="DATE_RANGE_PROPERTIES")
	@OneToMany(cascade=CascadeType.ALL, orphanRemoval=true)
	private List<DateRangeProperty> _dateRangeProperties = new ArrayList<DateRangeProperty>();
	
	// TODO: Use explicit ordering clause ! (Find out, why this is not working) - We expect that this list will be short and will not be changed frequently, so it is OK for now to not sort it
	// TODO: @Order(extensions = @Extension(vendorName="datanucleus", key="list-ordering", value="_name asc"))
	@Column(name="TIME_PROPERTIES")
	@OneToMany(cascade=CascadeType.ALL, orphanRemoval=true)
	private List<TimeProperty> _timeProperties = new ArrayList<TimeProperty>();

	// TODO: Use explicit ordering clause ! (Find out, why this is not working) - We expect that this list will be short and will not be changed frequently, so it is OK for now to not sort it
	// TODO: @Order(extensions = @Extension(vendorName="datanucleus", key="list-ordering", value="_name asc"))
	@Column(name="TIME_RANGE_PROPERTIES")
	@OneToMany(cascade=CascadeType.ALL, orphanRemoval=true)
	private List<TimeRangeProperty> _timeRangeProperties = new ArrayList<TimeRangeProperty>();
	
	// TODO: Use explicit ordering clause ! (Find out, why this is not working) - We expect that this list will be short and will not be changed frequently, so it is OK for now to not sort it
	// TODO: @Order(extensions = @Extension(vendorName="datanucleus", key="list-ordering", value="_name asc"))
	@Column(name="UNIT_PROPERTIES")
	@OneToMany(cascade=CascadeType.ALL, orphanRemoval=true)
	private List<UnitProperty> _unitProperties = new ArrayList<UnitProperty>();
	
	// TODO: Use explicit ordering clause ! (Find out, why this is not working) - We expect that this list will be short and will not be changed frequently, so it is OK for now to not sort it
	// TODO: @Order(extensions = @Extension(vendorName="datanucleus", key="list-ordering", value="_name asc"))
	@Column(name="STRING_LIST_PROPERTIES")
	@OneToMany(cascade=CascadeType.ALL, orphanRemoval=true)
	private List<StringListProperty> _stringListProperties = new ArrayList<StringListProperty>();
	
	@SuppressWarnings("unchecked")
	public void addProperty(Property property_) {
		getPropertyList(property_).add(property_);
	}

	@SuppressWarnings("unchecked")
	public void deleteProperty(Property property_) {
		List[] lists = { 
				_intProperties, 
				_doubleProperties, 
				_stringProperties, 
				_booleanProperties, 
				_dateProperties, 
				_dateRangeProperties, 
				_timeProperties, 
				_timeRangeProperties, 
				_unitProperties, 
				_stringListProperties
				};
		
		List propertyList = null;
		Property propertyFound = null;
		
		for (List list : lists) {
			propertyFound = findByName(property_, list);
			propertyList = list;
			
			if(propertyFound != null) {
				break;
			}
		}
		
		if(propertyFound != null) {
			propertyList.remove(propertyFound);
		}
	}
	
	@SuppressWarnings("unchecked")
	private Property findByName(Property property_, List list_) {
		Property found = null;
		for (Object object : list_) {
			Property property = (Property) object;
			if(property.getName().equals(property_.getName())) {
				found = property;
				break;
			}
		}
		
		return found;
	}
	
	@SuppressWarnings("unchecked")
	private List getPropertyList(Property property_) {
		List list = null;
		if (property_ instanceof IntProperty) {
			list = _intProperties;
		} else if (property_ instanceof DoubleProperty) {
			list = _doubleProperties;
		} else if (property_ instanceof BooleanProperty) {
			list = _booleanProperties;
		} else if (property_ instanceof StringProperty) {
			list = _stringProperties;
		} else if (property_ instanceof ObjectProperty) {
			throw new IllegalArgumentException("Object Property is not supported");	
		} else if (property_ instanceof DateProperty) {
			list = _dateProperties;
		} else if (property_ instanceof DateRangeProperty) {
			list = _dateRangeProperties;
		} else if (property_ instanceof TimeProperty) {
			list = _timeProperties;
		} else if (property_ instanceof TimeRangeProperty) {
			list = _timeRangeProperties;
		} else if (property_ instanceof UnitProperty) {
			list = _unitProperties;
		} else if (property_ instanceof StringListProperty) {
			list = _stringListProperties;
		}
		
		if(list == null) {
			throw new IllegalArgumentException("No Property List can be found for Property");
		}
		
		return list;
	}

	public void clearProperties() {
		_intProperties.clear();
		_doubleProperties.clear();
		_stringProperties.clear();
		_booleanProperties.clear();
		_dateProperties.clear();
		_dateRangeProperties.clear();
		_timeProperties.clear();
		_timeRangeProperties.clear();
		_unitProperties.clear();
		_stringListProperties.clear();
	}
	
	public Property[] getProperties() {
		List<Property> list = new ArrayList<Property>();
		for (Property property : _intProperties) {
			list.add(property);
		}
		for (Property property : _doubleProperties) {
			list.add(property);
		}
		for (Property property : _stringProperties) {
			list.add(property);
		}
		for (Property property : _booleanProperties) {
			list.add(property);
		}
		for (Property property : _dateProperties) {
			list.add(property);
		}
		for (Property property : _dateRangeProperties) {
			list.add(property);
		}
		for (Property property : _timeProperties) {
			list.add(property);
		}
		for (Property property : _timeRangeProperties) {
			list.add(property);
		}
		for (Property property : _unitProperties) {
			list.add(property);
		}
		for (Property property : _stringListProperties) {
			list.add(property);
		}
		
		return list.toArray(new Property[list.size()]);
	}

	/**
	 * Initializes lists, if they are not initialized (in case it was received from BlazeDS)
	 */
	public void initLists() {
		if(getIntProperties() == null) {		
			_intProperties = new ArrayList<IntProperty>();
		}
		if(getDoubleProperties() == null) {	
			_doubleProperties = new ArrayList<DoubleProperty>();
		}
		if(getStringProperties() == null) {	
			_stringProperties = new ArrayList<StringProperty>();
		}
		if(getBooleanProperties() == null) {	
			_booleanProperties = new ArrayList<BooleanProperty>();
		}
		if(getDateProperties() == null) {	
			_dateProperties = new ArrayList<DateProperty>();
		}
		if(getDateRangeProperties() == null) {	
			_dateRangeProperties = new ArrayList<DateRangeProperty>();
		}
		if(getTimeProperties() == null) {
			_timeProperties = new ArrayList<TimeProperty>();
		}
		if(getTimeRangeProperties() == null) {
			_timeRangeProperties = new ArrayList<TimeRangeProperty>();
		}
		if(getUnitProperties() == null) {
			_unitProperties = new ArrayList<UnitProperty>();
		}
		if(getStringListProperties() == null) {
			_stringListProperties = new ArrayList<StringListProperty>();
		}
	}
	
	public List<IntProperty> getIntProperties() {
		return _intProperties;
	}

	public void setIntProperties(List<IntProperty> intProperties_) {
		_intProperties = intProperties_;
	}

	public List<DoubleProperty> getDoubleProperties() {
		return _doubleProperties;
	}

	public void setDoubleProperties(List<DoubleProperty> doubleProperties_) {
		_doubleProperties = doubleProperties_;
	}

	public List<StringProperty> getStringProperties() {
		return _stringProperties;
	}

	public void setStringProperties(List<StringProperty> stringProperties_) {
		_stringProperties = stringProperties_;
	}

	public List<BooleanProperty> getBooleanProperties() {
		return _booleanProperties;
	}

	public void setBooleanProperties(List<BooleanProperty> booleanProperties_) {
		_booleanProperties = booleanProperties_;
	}

	public List<DateProperty> getDateProperties() {
		return _dateProperties;
	}

	public void setDateProperties(List<DateProperty> dateProperties_) {
		_dateProperties = dateProperties_;
	}

	public List<DateRangeProperty> getDateRangeProperties() {
		return _dateRangeProperties;
	}

	public void setDateRangeProperties(List<DateRangeProperty> dateRangeProperties_) {
		_dateRangeProperties = dateRangeProperties_;
	}

	public List<TimeProperty> getTimeProperties() {
		return _timeProperties;
	}

	public void setTimeProperties(List<TimeProperty> timeProperties_) {
		_timeProperties = timeProperties_;
	}

	public List<TimeRangeProperty> getTimeRangeProperties() {
		return _timeRangeProperties;
	}

	public void setTimeRangeProperties(List<TimeRangeProperty> timeRangeProperties_) {
		_timeRangeProperties = timeRangeProperties_;
	}

	public List<UnitProperty> getUnitProperties() {
		return _unitProperties;
	}

	public void setUnitProperties(List<UnitProperty> unitProperties_) {
		_unitProperties = unitProperties_;
	}

	public void setStringListProperties(List<StringListProperty> stringListProperties) {
		_stringListProperties = stringListProperties;
	}

	public List<StringListProperty> getStringListProperties() {
		return _stringListProperties;
	}

	@Override
	public String toString() {
		return "PropertyHolder [_id=" + _id + ", _intProperties="
				+ _intProperties + ", _doubleProperties=" + _doubleProperties
				+ ", _stringProperties=" + _stringProperties
				+ ", _booleanProperties=" + _booleanProperties
				+ ", _dateProperties=" + _dateProperties
				+ ", _dateRangeProperties=" + _dateRangeProperties
				+ ", _timeProperties=" + _timeProperties
				+ ", _timeRangeProperties=" + _timeRangeProperties
				+ ", _unitProperties=" + _unitProperties
				+ ", _stringListProperties=" + _stringListProperties + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((_booleanProperties == null) ? 0 : _booleanProperties
						.hashCode());
		result = prime * result
				+ ((_dateProperties == null) ? 0 : _dateProperties.hashCode());
		result = prime
				* result
				+ ((_dateRangeProperties == null) ? 0 : _dateRangeProperties
						.hashCode());
		result = prime
				* result
				+ ((_doubleProperties == null) ? 0 : _doubleProperties
						.hashCode());
		result = prime * result
				+ ((_intProperties == null) ? 0 : _intProperties.hashCode());
		result = prime
				* result
				+ ((_stringListProperties == null) ? 0 : _stringListProperties
						.hashCode());
		result = prime
				* result
				+ ((_stringProperties == null) ? 0 : _stringProperties
						.hashCode());
		result = prime * result
				+ ((_timeProperties == null) ? 0 : _timeProperties.hashCode());
		result = prime
				* result
				+ ((_timeRangeProperties == null) ? 0 : _timeRangeProperties
						.hashCode());
		result = prime * result
				+ ((_unitProperties == null) ? 0 : _unitProperties.hashCode());
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
		PropertyHolder other = (PropertyHolder) obj;
		if (_booleanProperties == null) {
			if (other._booleanProperties != null)
				return false;
		} else if (!_booleanProperties.equals(other._booleanProperties))
			return false;
		if (_dateProperties == null) {
			if (other._dateProperties != null)
				return false;
		} else if (!_dateProperties.equals(other._dateProperties))
			return false;
		if (_dateRangeProperties == null) {
			if (other._dateRangeProperties != null)
				return false;
		} else if (!_dateRangeProperties.equals(other._dateRangeProperties))
			return false;
		if (_doubleProperties == null) {
			if (other._doubleProperties != null)
				return false;
		} else if (!_doubleProperties.equals(other._doubleProperties))
			return false;
		if (_intProperties == null) {
			if (other._intProperties != null)
				return false;
		} else if (!_intProperties.equals(other._intProperties))
			return false;
		if (_stringListProperties == null) {
			if (other._stringListProperties != null)
				return false;
		} else if (!_stringListProperties.equals(other._stringListProperties))
			return false;
		if (_stringProperties == null) {
			if (other._stringProperties != null)
				return false;
		} else if (!_stringProperties.equals(other._stringProperties))
			return false;
		if (_timeProperties == null) {
			if (other._timeProperties != null)
				return false;
		} else if (!_timeProperties.equals(other._timeProperties))
			return false;
		if (_timeRangeProperties == null) {
			if (other._timeRangeProperties != null)
				return false;
		} else if (!_timeRangeProperties.equals(other._timeRangeProperties))
			return false;
		if (_unitProperties == null) {
			if (other._unitProperties != null)
				return false;
		} else if (!_unitProperties.equals(other._unitProperties))
			return false;
		return true;
	}

	
}
