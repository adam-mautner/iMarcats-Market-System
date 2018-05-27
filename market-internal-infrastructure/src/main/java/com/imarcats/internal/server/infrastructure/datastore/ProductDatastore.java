package com.imarcats.internal.server.infrastructure.datastore;

import com.imarcats.model.Product;
import com.imarcats.model.types.ActivationStatus;
import com.imarcats.model.types.PagedProductList;

/**
 * Data Access Object (DAO), which stores all the Products in the System. 
 * Implementation of this interface can be found in the project specific to
 * the platform where the Market Engine is hosted. 
 * 
 * @author Adam
 */
public interface ProductDatastore {

	/**
	 * Creates a Product 
	 * @param product_ Product to be Created 
	 *
	 * @return Product Code
	 */
	public String createProduct(Product product_);	
	
	/**
	 * Explicitly updates Product in the Datastore with instance provided 
	 * @param changedProductModel_ Product to be updated 
	 *
	 * @return the updated object
	 */
	public Product updateProduct(Product changedProductModel_);
	
	/**
	 * Finds the Product by its Code (Primary Key)
	 * @param productCode_ Code (Primary Key) of the Product
	 *
	 * @return Product 
	 */
	public Product findProductByCode(String productCode_);

	/**
	 * Finds the Products by Activation Status
	 * @param activationStatus_ Activation Status
	 * @param cursorString_ Cursor String for continuing a Paged Query or Null for a new Paged Query
	 * @param maxNumberOfProductsOnPage_ Max Number of Products on a Page   
	 *
	 * @return Page Product List
	 */
	public PagedProductList findProductsFromCursorByActivationStatus(ActivationStatus activationStatus_, String cursorString_, int maxNumberOfProductsOnPage_);

	/**
	 * Finds All Products
	 * @param cursorString_ Cursor String for continuing a Paged Query or Null for a new Paged Query
	 * @param maxNumberOfProductsOnPage_ Max Number of Products on a Page   
	 *
	 * @return Page Product List
	 */
	public PagedProductList findAllProductsFromCursor(String cursorString_, int maxNumberOfProductsOnPage_);

	
	/**
	 * Deletes a Product 
	 * @param productCode_ Product to be Deleted
	 *
	 */
	public void deleteProduct(String productCode_);
	
}
