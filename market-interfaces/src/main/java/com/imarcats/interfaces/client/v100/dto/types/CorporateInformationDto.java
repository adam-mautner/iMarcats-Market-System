package com.imarcats.interfaces.client.v100.dto.types;

import com.imarcats.interfaces.client.v100.dto.MarketModelObjectDto;


/**
 * Corporate Information for a User trading on the Market
 * @author Adam
 */
public class CorporateInformationDto implements MarketModelObjectDto {
	
	/**
	 * Name of the Corporation
	 * Required
	 */
//	@Column(name="NAME", nullable=false, length=DataLengths.MARKET_OBJECT_NAME_LENGTH)
	private String _name;

	/**
	 * Address of the Corporation
	 * Required
	 */
	private AddressDto _address;

	/**
	 * Web Page of the Corporation
	 * Optional
	 * 
	 * Note: This could be a URL.
	 */
//	@Column(name="WEB_SITE", length=DataLengths.MARKET_OBJECT_DOCUMENT_LINK_LENGTH)
	private String _webSite;
	
	public String getName() {
		return _name;
	}

	public void setName(String name_) {
		_name = name_;
	}

	public AddressDto getAddress() {
		return _address;
	}

	public void setAddress(AddressDto address_) {
		_address = address_;
	}

	public String getWebSite() {
		return _webSite;
	}

	public void setWebSite(String webSite_) {
		_webSite = webSite_;
	}
}
