package com.imarcats.interfaces.server.v100.dto.mapping;

import com.imarcats.interfaces.client.v100.dto.ProductDto;
import com.imarcats.interfaces.client.v100.dto.types.PagedProductListDto;
import com.imarcats.model.Product;
import com.imarcats.model.types.PagedProductList;

public class ProductDtoMapping extends DtoMappingBase {
	public static ProductDtoMapping INSTANCE = new ProductDtoMapping();
	
	public ProductDto toDto(Product product_) {
		return _mapper.map(product_, ProductDto.class);
	}
	
	public Product fromDto(ProductDto product_) {
		return _mapper.map(product_, Product.class);
	}
	
	public PagedProductListDto toDto(PagedProductList productList_) {
		return _mapper.map(productList_, PagedProductListDto.class);
	}
	
	public PagedProductList fromDto(PagedProductListDto productList_) {
		return _mapper.map(productList_, PagedProductList.class);
	}
}
