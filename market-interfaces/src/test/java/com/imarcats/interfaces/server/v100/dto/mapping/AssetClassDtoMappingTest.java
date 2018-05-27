package com.imarcats.interfaces.server.v100.dto.mapping;

import com.imarcats.interfaces.client.v100.dto.AssetClassDto;
import com.imarcats.interfaces.client.v100.dto.types.PagedAssetClassListDto;
import com.imarcats.interfaces.server.v100.dto.mapping.AssetClassDtoMapping;
import com.imarcats.model.AssetClass;
import com.imarcats.model.test.testutils.MarketObjectTestBase;
import com.imarcats.model.types.PagedAssetClassList;

public class AssetClassDtoMappingTest extends MarketObjectTestBase {
	
	public void testRoundTripMapping() throws Exception {
		AssetClass assetClass = createAssetClass();
		assetClass.setVersionNumber(10L);
		
		AssetClassDto assetClassDto = AssetClassDtoMapping.INSTANCE.toDto(assetClass);
		AssetClass assetClassMapped = AssetClassDtoMapping.INSTANCE.fromDto(assetClassDto); 
		
		checkAssetClass(assetClass, assetClassMapped);		
	}

	public void testRoundTripListMapping() throws Exception {
		AssetClass assetClass = createAssetClass();
		assetClass.setName("AssetClass_1");
		AssetClass assetClass2 = createAssetClass();
		assetClass2.setName("AssetClass_2");
		
		PagedAssetClassList list = new PagedAssetClassList();
		list.setAssetClasses(new AssetClass[] {assetClass, assetClass2});
		list.setCursorString("TestCursor");
		list.setMaxNumberOfAssetClassesOnPage(2); 
		
		PagedAssetClassListDto listDto = AssetClassDtoMapping.INSTANCE.toDto(list);
		PagedAssetClassList listMapped = AssetClassDtoMapping.INSTANCE.fromDto(listDto); 
		
		assertEquals(list.getCursorString(), listMapped.getCursorString());
		assertEquals(list.getMaxNumberOfAssetClassesOnPage(), listMapped.getMaxNumberOfAssetClassesOnPage());
		assertEquals(list.getAssetClasses().length, listMapped.getAssetClasses().length);
		checkAssetClass(list.getAssetClasses()[0], listMapped.getAssetClasses()[0]);		
		checkAssetClass(list.getAssetClasses()[1], listMapped.getAssetClasses()[1]);		
	}
	
	private void checkAssetClass(AssetClass assetClass,
			AssetClass assetClassMapped) {
		assertEqualsAssetClass(assetClass, assetClassMapped);
		assertEquals(assetClass.getLastUpdateTimestamp(), assetClassMapped.getLastUpdateTimestamp());
		assertEquals(assetClass.getVersionNumber(), assetClassMapped.getVersionNumber());
	}

}
