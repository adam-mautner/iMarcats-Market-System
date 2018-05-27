package com.imarcats.internal.server.infrastructure.datastore;

import com.imarcats.model.AssetClass;
import com.imarcats.model.types.PagedAssetClassList;


/**
 * Data Access Object (DAO), which stores all the AssetClass in the System. 
 * Implementation of this interface can be found in the project specific to
 * the platform where the Market Engine is hosted. 
 * 
 * @author Adam
 */
public interface AssetClassDatastore {

	/**
	 * Creates Asset Class in the Datastore 
	 * @param assetClass_ Asset Class to be Created 
	 * @return Name of Asset Class
	 */
	public String createAssetClass(AssetClass assetClass_);
	
	/**
	 * Finds Asset Class by Name 
	 * @param name_ Name 
	 * @return Asset Class
	 */
	public AssetClass findAssetClassByName(String name_);
	
	/**
	 * Finds All Asset Classes
	 * @param cursorString_ Cursor String for continuing a Paged Query or Null for a new Paged Query
	 * @param maxNumberOfAssetClassesOnPage_ Max Number of Asset Classes on a Page   
	 * @return Paged Asset Class List
	 */
	public PagedAssetClassList findAllAssetClassesFromCursor(String cursorString_, int maxNumberOfAssetClassesOnPage_);
	
	/**
	 * Finds All Top-Level Asset Classes (that have no Parent)
	 * @param cursorString_ Cursor String for continuing a Paged Query or Null for a new Paged Query
	 * @param maxNumberOfAssetClassesOnPage_ Max Number of Asset Classes on a Page   
	 * @return Paged Asset Class List
	 */
	public PagedAssetClassList findAllTopLevelAssetClassesFromCursor(String cursorString_, int maxNumberOfAssetClassesOnPage_);
	
	
	/**
	 * Finds Asset Classes by Parent
	 * @param parentAssetClassName_ Name of the Parent Asset Class 
	 * @param cursorString_ Cursor String for continuing a Paged Query or Null for a new Paged Query
	 * @param maxNumberOfAssetClassesOnPage_ Max Number of Asset Classes on a Page   
	 * @return Paged Asset Class List
	 */
	public PagedAssetClassList findAssetClassesFromCursorByParent(String parentAssetClassName_, String cursorString_, int maxNumberOfAssetClassesOnPage_);
	
	/**
	 * Deletes Asset Class by Name 
	 * @param name_ Name of Asset Class to be Deleted
	 */
	public void deleteAssetClass(String name_);

	/**
	 * Explicitly updates Asset Class in the Datastore with instance provided 
	 * @param changedAssetClassModel_ Asset Class to be updated 
	 * @return the updated object 
	 */
	public AssetClass updateAssetClass(AssetClass changedAssetClassModel_);
}
