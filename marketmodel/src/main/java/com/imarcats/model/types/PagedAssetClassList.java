package com.imarcats.model.types;

import java.io.Serializable;
import java.util.Arrays;

import com.imarcats.model.AssetClass;

/**
 * Paged list of Asset Classes
 * @author Adam
 */
public class PagedAssetClassList implements Serializable {

	private static final long serialVersionUID = 1L;

	private AssetClass[] _assetClasses;
	private String _cursorString;
	private int _maxNumberOfAssetClassesOnPage;
	
	public AssetClass[] getAssetClasses() {
		return _assetClasses;
	}
	public void setAssetClasses(AssetClass[] assetClasses_) {
		_assetClasses = assetClasses_;
	}
	public String getCursorString() {
		return _cursorString;
	}
	public void setCursorString(String cursorString_) {
		_cursorString = cursorString_;
	}
	public int getMaxNumberOfAssetClassesOnPage() {
		return _maxNumberOfAssetClassesOnPage;
	}
	public void setMaxNumberOfAssetClassesOnPage(int maxNumberOfAssetClassesOnPage_) {
		_maxNumberOfAssetClassesOnPage = maxNumberOfAssetClassesOnPage_;
	}
	@Override
	public String toString() {
		return "PagedAssetClassList [_assetClasses="
				+ Arrays.toString(_assetClasses) + ", _cursorString="
				+ _cursorString + ", _maxNumberOfAssetClassesOnPage="
				+ _maxNumberOfAssetClassesOnPage + "]";
	}
	
	
}
