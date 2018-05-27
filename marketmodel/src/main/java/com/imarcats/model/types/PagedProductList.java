package com.imarcats.model.types;

import java.io.Serializable;
import java.util.Arrays;

import com.imarcats.model.Product;

/**
 * Paged list of Products 
 * @author Adam
 */
public class PagedProductList implements Serializable {

	private static final long serialVersionUID = 1L;

	private Product[] _products;
	private String _cursorString;
	private int _maxNumberOfProductsOnPage;
	
	public Product[] getProducts() {
		return _products;
	}
	public void setProducts(Product[] products_) {
		_products = products_;
	}
	public String getCursorString() {
		return _cursorString;
	}
	public void setCursorString(String cursorString_) {
		_cursorString = cursorString_;
	}
	public int getMaxNumberOfProductsOnPage() {
		return _maxNumberOfProductsOnPage;
	}
	public void setMaxNumberOfProductsOnPage(int maxNumberOfProductsOnPage_) {
		_maxNumberOfProductsOnPage = maxNumberOfProductsOnPage_;
	}
	@Override
	public String toString() {
		return "PagedProductList [_cursorString=" + _cursorString
				+ ", _maxNumberOfProductsOnPage=" + _maxNumberOfProductsOnPage
				+ ", _products=" + Arrays.toString(_products) + "]";
	}
	
	

}
