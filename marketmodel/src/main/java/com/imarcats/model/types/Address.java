package com.imarcats.model.types;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import com.imarcats.model.MarketModelObject;

/**
 * Address of a User, Market Operator or Delivery Location
 * @author Adam
 */
@Embeddable
public class Address implements MarketModelObject {

	private static final long serialVersionUID = 1L;
	
	/**
	 * Street Address
	 * Required
	 */
	@Column(name="STREET", nullable=false, length=50)
	private String _street;
	
	/**
	 * City
	 * Required
	 */
	@Column(name="CITY", nullable=false, length=50)
	private String _city;
	
	/**
	 * State
	 * Optional
	 */
	@Column(name="STATE", length=2)
	private String _state; 
	
	/**
	 * Country
	 * Required
	 */
	@Column(name="COUNTRY", nullable=false, length=50)
	private String _country;

	/**
	 * Prostal Code
	 * Optional
	 */
	@Column(name="POSTAL_CODE", length=50)
	private String _postalCode; 
	
    public static Address create(Address address_) {
    	Address newAddress = new Address();
    	
    	newAddress.setCity(address_.getCity());
    	newAddress.setCountry(address_.getCountry());
    	newAddress.setPostalCode(address_.getPostalCode());
    	newAddress.setState(address_.getState());
    	newAddress.setStreet(address_.getStreet());
    	
    	return newAddress;
    }
    
	public String getStreet() {
		return _street;
	}

	public void setStreet(String street_) {
		_street = street_;
	}

	public String getCity() {
		return _city;
	}

	public void setCity(String city_) {
		_city = city_;
	}

	public String getState() {
		return _state;
	}

	public void setState(String state_) {
		_state = state_;
	}

	public String getCountry() {
		return _country;
	}

	public void setCountry(String country_) {
		_country = country_;
	}

	public void setPostalCode(String postalCode) {
		_postalCode = postalCode;
	}

	public String getPostalCode() {
		return _postalCode;
	}

	@Override
	public String toString() {
		return "Address [_street=" + _street + ", _city=" + _city + ", _state="
				+ _state + ", _country=" + _country + ", _postalCode="
				+ _postalCode + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((_city == null) ? 0 : _city.hashCode());
		result = prime * result
				+ ((_country == null) ? 0 : _country.hashCode());
		result = prime * result
				+ ((_postalCode == null) ? 0 : _postalCode.hashCode());
		result = prime * result + ((_state == null) ? 0 : _state.hashCode());
		result = prime * result + ((_street == null) ? 0 : _street.hashCode());
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
		Address other = (Address) obj;
		if (_city == null) {
			if (other._city != null)
				return false;
		} else if (!_city.equals(other._city))
			return false;
		if (_country == null) {
			if (other._country != null)
				return false;
		} else if (!_country.equals(other._country))
			return false;
		if (_postalCode == null) {
			if (other._postalCode != null)
				return false;
		} else if (!_postalCode.equals(other._postalCode))
			return false;
		if (_state == null) {
			if (other._state != null)
				return false;
		} else if (!_state.equals(other._state))
			return false;
		if (_street == null) {
			if (other._street != null)
				return false;
		} else if (!_street.equals(other._street))
			return false;
		return true;
	}

	
}
