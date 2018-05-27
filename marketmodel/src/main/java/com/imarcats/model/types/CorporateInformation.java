package com.imarcats.model.types;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.imarcats.model.MarketModelObject;
import com.imarcats.model.meta.DataLengths;

/**
 * Corporate Information for a User trading on the Market
 * @author Adam
 */
@Entity
@Table(name="CORPORATE_INFORMATION")
public class CorporateInformation implements MarketModelObject {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(name="ID")
	private Long _id;
	
	/**
	 * Name of the Corporation
	 * Required
	 */
	@Column(name="NAME", nullable=false, length=DataLengths.MARKET_OBJECT_NAME_LENGTH)
	private String _name;

	/**
	 * Address of the Corporation
	 * Required
	 */
	@Embedded
	private Address _address;

	/**
	 * Web Page of the Corporation
	 * Optional
	 * 
	 * Note: This could be a URL.
	 */
	@Column(name="WEB_SITE", length=DataLengths.MARKET_OBJECT_DOCUMENT_LINK_LENGTH)
	private String _webSite;

    public static CorporateInformation create(CorporateInformation corporateInformation_) {
    	CorporateInformation newInformation = new CorporateInformation();
    	
    	newInformation.setAddress(Address.create(corporateInformation_.getAddress()));
    	newInformation.setName(corporateInformation_.getName());
    	newInformation.setWebSite(corporateInformation_.getWebSite());
    	
    	return newInformation;
    }
	
	public String getName() {
		return _name;
	}

	public void setName(String name_) {
		_name = name_;
	}

	public Address getAddress() {
		return _address;
	}

	public void setAddress(Address address_) {
		_address = address_;
	}

	public String getWebSite() {
		return _webSite;
	}

	public void setWebSite(String webSite_) {
		_webSite = webSite_;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((_address == null) ? 0 : _address.hashCode());
		result = prime * result + ((_id == null) ? 0 : _id.hashCode());
		result = prime * result + ((_name == null) ? 0 : _name.hashCode());
		result = prime * result
				+ ((_webSite == null) ? 0 : _webSite.hashCode());
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
		CorporateInformation other = (CorporateInformation) obj;
		if (_address == null) {
			if (other._address != null)
				return false;
		} else if (!_address.equals(other._address))
			return false;
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
		if (_webSite == null) {
			if (other._webSite != null)
				return false;
		} else if (!_webSite.equals(other._webSite))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "CorporateInformation [_id=" + _id + ", _name=" + _name
				+ ", _address=" + _address + ", _webSite=" + _webSite + "]";
	}

	
}
