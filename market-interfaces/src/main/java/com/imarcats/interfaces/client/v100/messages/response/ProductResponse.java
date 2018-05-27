package com.imarcats.interfaces.client.v100.messages.response;

import com.imarcats.interfaces.client.v100.dto.ActivatableMarketObjectDto;
import com.imarcats.interfaces.client.v100.dto.ProductDto;

/**
 * Response with a Product 
 * @author Adam
 *
 */
public class ProductResponse extends MarketObjectResponse {

	private static final long serialVersionUID = 1L;
	
	private ProductDto _product;

	public void setProduct(ProductDto product) {
		_product = product;
	}

	public ProductDto getProduct() {
		return _product;
	}

	@Override
	public ActivatableMarketObjectDto getMarketObject() {
		return getProduct();
	}

}
