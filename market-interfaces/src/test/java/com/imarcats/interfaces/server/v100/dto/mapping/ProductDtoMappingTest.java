package com.imarcats.interfaces.server.v100.dto.mapping;

import com.imarcats.interfaces.client.v100.dto.ProductDto;
import com.imarcats.interfaces.client.v100.dto.types.PagedProductListDto;
import com.imarcats.model.Product;
import com.imarcats.model.test.testutils.MarketObjectTestBase;
import com.imarcats.model.types.ActivationStatus;
import com.imarcats.model.types.PagedProductList;

public class ProductDtoMappingTest extends MarketObjectTestBase {
	
	public void testTypeRoundTripMapping() throws Exception {
		assertEquals(null, ProductDtoMapping.INSTANCE.toDto((ActivationStatus)null));
		assertEquals(com.imarcats.interfaces.client.v100.dto.types.ActivationStatus.Approved, ProductDtoMapping.INSTANCE.toDto(ActivationStatus.Approved));
		assertEquals(ActivationStatus.Approved, ProductDtoMapping.INSTANCE.fromDto(com.imarcats.interfaces.client.v100.dto.types.ActivationStatus.Approved));
	}
	
	public void testRoundTripMapping() throws Exception {
		Product product = createProduct("TestProduct");
		product.setVersionNumber(10L);
		
		ProductDto productDto = ProductDtoMapping.INSTANCE.toDto(product);
		Product productMapped = ProductDtoMapping.INSTANCE.fromDto(productDto); 
		
		checkProduct(product, productMapped);
	}

	private void checkProduct(Product product, Product productMapped) {
		assertEqualsProduct(product, productMapped);
		assertEquals(product.getLastUpdateTimestamp(), productMapped.getLastUpdateTimestamp());
		assertEquals(product.getVersionNumber(), productMapped.getVersionNumber());
	}
	
	public void testRoundTripListMapping() throws Exception {
		Product product = createProduct("Test1");
		Product product2 = createProduct("Test2");
		
		PagedProductList list = new PagedProductList();
		list.setProducts(new Product[] {product, product2});
		list.setCursorString("TestCursor");
		list.setMaxNumberOfProductsOnPage(2); 
		
		PagedProductListDto listDto = ProductDtoMapping.INSTANCE.toDto(list);
		PagedProductList listMapped = ProductDtoMapping.INSTANCE.fromDto(listDto); 
		
		assertEquals(list.getCursorString(), listMapped.getCursorString());
		assertEquals(list.getMaxNumberOfProductsOnPage(), listMapped.getMaxNumberOfProductsOnPage());
		assertEquals(list.getProducts().length, listMapped.getProducts().length);
		checkProduct(list.getProducts()[0], listMapped.getProducts()[0]);		
		checkProduct(list.getProducts()[1], listMapped.getProducts()[1]);		
	}

}
