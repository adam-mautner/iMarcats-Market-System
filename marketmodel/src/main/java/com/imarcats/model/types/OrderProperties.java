package com.imarcats.model.types;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.imarcats.model.MarketModelObject;

/**
 * This class is needed to have a separate database table for this class
 * @author Adam
 */
@Entity
@Table(name="ORDER_PROPERTIES")
public class OrderProperties implements MarketModelObject {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * Primary Key of the OrderProperties
	 * Note: No need for accessing this field directly
	 */	
	@Id
	@GeneratedValue 
	@Column(name="ID")
    private Long _id;

	@OneToOne(cascade=CascadeType.ALL, orphanRemoval=true)
	@JoinColumn(name="PROPERTY_HOLDER_ID")
    private PropertyHolder _propertyHolder;
	
	public OrderProperties() {
		/* for Instantiation */
	}
    
	public OrderProperties(PropertyHolder propertyHolder_) {
		super();
		_propertyHolder = propertyHolder_;
	}

	public void setPropertyHolder(PropertyHolder propertyHolder) {
		_propertyHolder = propertyHolder;
	}

	public PropertyHolder getPropertyHolder() {
		return _propertyHolder;
	}

	@Override
	public String toString() {
		return "OrderProperties [_propertyHolder=" + _propertyHolder + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((_id == null) ? 0 : _id.hashCode());
		result = prime * result
				+ ((_propertyHolder == null) ? 0 : _propertyHolder.hashCode());
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
		OrderProperties other = (OrderProperties) obj;
		if (_id == null) {
			if (other._id != null)
				return false;
		} else if (!_id.equals(other._id))
			return false;
		if (_propertyHolder == null) {
			if (other._propertyHolder != null)
				return false;
		} else if (!_propertyHolder.equals(other._propertyHolder))
			return false;
		return true;
	}
}
