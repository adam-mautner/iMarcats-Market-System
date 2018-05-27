package com.imarcats.interfaces.client.v100.dto.types;

import com.imarcats.interfaces.client.v100.dto.MarketModelObjectDto;


/**
 * Address of a User, Market Operator or Delivery Location
 * @author Adam
 */
public class AddressDto implements MarketModelObjectDto {
	
	/**
	 * Street Address
	 * Required
	 */
//	@Column(name="STREET", nullable=false, length=50)
	private String _street;
	
	/**
	 * City
	 * Required
	 */
//	@Column(name="CITY", nullable=false, length=50)
	private String _city;
	
	/**
	 * State
	 * Optional
	 */
//	@Column(name="STATE", length=2)
	private String _state; 
	
	/**
	 * Country
	 * Required
	 */
//	@Column(name="COUNTRY", nullable=false, length=50)
	private String _country;

	/**
	 * Prostal Code
	 * Optional
	 */
//	@Column(name="POSTAL_CODE", length=50)
	private String _postalCode; 
	
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

	public String getAddressStr() {
		// TODO: Localize address text 
		StringBuilder builder = new StringBuilder();
		builder.append(_street);
		builder.append(" ");
		builder.append(_city);
		if(_state != null) {
			builder.append(", ");
			builder.append(_city);			
		}
		builder.append(" ");
		builder.append(_country);
		if(_postalCode != null) {
			builder.append(" ");
			builder.append(_postalCode);			
		}
		
		return builder.toString();
	}
}
