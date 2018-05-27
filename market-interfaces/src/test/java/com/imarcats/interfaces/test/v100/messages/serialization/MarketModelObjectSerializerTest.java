package com.imarcats.interfaces.test.v100.messages.serialization;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.imarcats.model.AssetClass;
import com.imarcats.model.Instrument;
import com.imarcats.model.Market;
import com.imarcats.model.MarketModelObject;
import com.imarcats.model.MarketOperator;
import com.imarcats.model.MatchedTrade;
import com.imarcats.model.Order;
import com.imarcats.model.OrderBookModel;
import com.imarcats.model.Product;
import com.imarcats.model.test.testutils.MarketObjectTestBase;
import com.imarcats.model.types.OrderSide;

public class MarketModelObjectSerializerTest extends MarketObjectTestBase {
	
	public void testMarketSerialization() throws Exception {
		Market market = createMarket("TestMarket");
		
		// Books are not serialized 
		market.setBuyBook(null);
		market.setSellBook(null);
		
		Market deserialized = (Market) serializeDeserialize(market);
		assertEqualsMarket(market, deserialized);
	}

	public void testOrderSerialization() throws Exception {
		Order order = createOrder("TestMarket");
		assertEqualsOrder(order, (Order) serializeDeserialize(order));
	}
	
	public void testMarketOperatorSerialization() throws Exception {
		MarketOperator marketOperator = createMarketOperator("TestMktOpt");
		
		MarketOperator deserialized = (MarketOperator) serializeDeserialize(marketOperator);
		assertEqualsMarketOperator(marketOperator, deserialized);
	}
	
	public void testProductSerialization() throws Exception {
		Product product = createProduct("TestProduct");
		
		Product deserialized = (Product) serializeDeserialize(product);
		assertEqualsProduct(product, deserialized);
	}

	public void testInstrumentSerialization() throws Exception {
		Instrument instrument = createInstrument("TestInstrument");
		
		Instrument deserialized = (Instrument) serializeDeserialize(instrument);
		assertEqualsInstrument(instrument, deserialized);
	}

	public void testMatchedTradeSerialization() throws Exception {
		MatchedTrade matchedTrade = createMatchedTrade("TestMarket");
		assertEqualsMatchedTrade(matchedTrade, (MatchedTrade) serializeDeserialize(matchedTrade));
	}
		
	public void testOrderBookSerialization() throws Exception {
		OrderBookModel book = createBook(OrderSide.Buy);
		assertEqualsBook(book, (OrderBookModel) serializeDeserialize(book));
	}

	public void testAssetClassSerialization() throws Exception {
		AssetClass assetClass = createAssetClass();
		
		AssetClass deserialized = (AssetClass) serializeDeserialize(assetClass);
		assertEqualsAssetClass(assetClass, deserialized);
	}
	
	private MarketModelObject serializeDeserialize(MarketModelObject marketModelObject_) throws IOException, ClassNotFoundException {
		ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
		MarketModelObjectSerializer.serializeToOutputStream(marketModelObject_, byteOutputStream);
		byteOutputStream.close();
		
		ByteArrayInputStream byteInputStream = new ByteArrayInputStream(byteOutputStream.toByteArray());
		

		MarketModelObject marketModelObjectDeserialized = MarketModelObjectSerializer.deserializeFromInputStream(byteInputStream);
		byteInputStream.close();
		
		return marketModelObjectDeserialized;
	}
}
