package com.imarcats.interfaces.client.v100.messages.response;

import com.imarcats.interfaces.client.v100.dto.ActivatableMarketObjectDto;
import com.imarcats.interfaces.client.v100.dto.types.PagedProductListDto;

/**
 * Response with Product List
 * @author Adam
 *
 */
public class ProductListResponse extends MarketObjectListResponse {

	private static final long serialVersionUID = 1L;

	private PagedProductListDto _productList;

	public void setProductList(PagedProductListDto productList) {
		_productList = productList;
	}

	public PagedProductListDto getProductList() {
		return _productList;
	}
	
	@Override
	public ActivatableMarketObjectDto[] getMarketObjects() {
		ActivatableMarketObjectDto[] list = new ActivatableMarketObjectDto[0];
		
		if(_productList != null && _productList.getProducts() != null) {
			list = new ActivatableMarketObjectDto[_productList.getProducts().length];
			for (int i = 0; i < list.length; i++) {
				list[i] = _productList.getProducts()[i];
			}
		}
		
		return list;
	}

	@Override
	public String getCursorString() {
		return _productList.getCursorString();
	}
}
