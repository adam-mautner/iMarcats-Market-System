package com.imarcats.interfaces.client.v100.dto.types;

import java.io.Serializable;

public class PagedBusinessEntityListDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private BusinessEntityDto[] _businessEntities;
	private String _cursorString;
	private int _maxNumberOfBusinessEntitiesOnPage;
	
	public BusinessEntityDto[] getBusinessEntities() {
		return _businessEntities;
	}
	public void setBusinessEntities(BusinessEntityDto[] businessEntities_) {
		_businessEntities = businessEntities_;
	}
	public String getCursorString() {
		return _cursorString;
	}
	public void setCursorString(String cursorString_) {
		_cursorString = cursorString_;
	}
	public int getMaxNumberOfBusinessEntitiesOnPage() {
		return _maxNumberOfBusinessEntitiesOnPage;
	}
	public void setMaxNumberOfBusinessEntitiesOnPage(
			int maxNumberOfBusinessEntitiesOnPage_) {
		_maxNumberOfBusinessEntitiesOnPage = maxNumberOfBusinessEntitiesOnPage_;
	}
}
