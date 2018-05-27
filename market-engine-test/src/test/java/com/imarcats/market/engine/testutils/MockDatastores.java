package com.imarcats.market.engine.testutils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.imarcats.infrastructure.server.trigger.MockTimeTrigger;
import com.imarcats.internal.server.infrastructure.datastore.AssetClassDatastore;
import com.imarcats.internal.server.infrastructure.datastore.InstrumentDatastore;
import com.imarcats.internal.server.infrastructure.datastore.MarketDatastore;
import com.imarcats.internal.server.infrastructure.datastore.MatchedTradeDatastore;
import com.imarcats.internal.server.infrastructure.marketdata.MarketDataSession;
import com.imarcats.internal.server.infrastructure.testutils.MockDatastoresBase;
import com.imarcats.internal.server.infrastructure.timer.MarketTimer;
import com.imarcats.internal.server.interfaces.market.MarketInternal;
import com.imarcats.internal.server.interfaces.order.OrderInternal;
import com.imarcats.market.engine.market.MarketImpl;
import com.imarcats.model.Instrument;
import com.imarcats.model.Market;
import com.imarcats.model.Order;
import com.imarcats.model.types.ActivationStatus;
import com.imarcats.model.types.MarketState;
import com.imarcats.model.types.PagedMarketList;
import com.imarcats.model.types.QuoteAndSize;
import com.imarcats.model.types.QuoteType;

public class MockDatastores extends MockDatastoresBase implements  
								MarketDatastore, 
								InstrumentDatastore,
								MatchedTradeDatastore,
								AssetClassDatastore {
	
	public static final String TEST_MARKET_OWNER = "TestMarketOwner";
	public static final String TEST_MARKET_ID = "MKT_TEST.TEST";
	public static final String MARKET_ID = "MKT." + 1;
	public static final String MARKET_ID_2 = "MKT." + 1 + 1000;
	
	private final Map<String, MarketInternal> _mapIdToMarket = new HashMap<String, MarketInternal>();

	protected MockTimeTrigger _trigger;


	public MockTimeTrigger getTrigger() {
		return _trigger;
	}

	public MockDatastores(boolean addTrigger_) {
		if(addTrigger_) {
			_trigger = new MockTimeTrigger(this);
		}
		initData();
	}
	
	public MockDatastores() {
		this(false);
	}

	public void initData() {
		addMarket(TEST_MARKET_ID);
		addMarket(MARKET_ID);
		addMarket(MARKET_ID_2);
	}

	protected void addMarket(String marketCode_) {
		Market market = createMarket(marketCode_);
		
		MarketInternal marketImpl = new MockMarket(market, _trigger, this);
		_mapIdToMarket.put(market.getMarketCode(), marketImpl);
	}

	protected static Market createMarket(String marketCode_) {
		Market market = new Market();
		market.setQuoteType(QuoteType.Price);
		// market.setExecutionSystem(ExecutionSystem.ContinuousTwoSidedAuction);
		market.setInstrumentCode("TestCode");
		market.setName("test");
		market.setMarketCode(marketCode_);
		market.setMarketOperatorCode("TEST");
		market.setMarketTimeZoneID("GMT"); 
		market.setDescription("Test");
		market.setAllowSizeRestrictionOnOrders(true);
		market.setMinimumQuoteIncrement(0.1); 
		market.setCommission(10);
		market.setCommissionCurrency("USD"); 
		
		market.setActivationStatus(ActivationStatus.Created); 
		market.setQuoteType(QuoteType.Price); 
		market.setAllowHiddenOrders(true);
		market.setBusinessEntityCode("TEST"); 
		market.setClearingBank("TEST");
		market.setState(MarketState.Closed); 
		 
		market.updateLastUpdateTimestampAndVersion();
		
		return market;
	}
	
	@Override
	public String createMarket(Market market_) {
		_mapIdToMarket.put(market_.getMarketCode(), new MockMarket( market_, null, this) );
		return market_.getMarketCode();
	}

	@Override
	public void deleteMarket(String marketCode_) {
		_mapIdToMarket.remove(marketCode_);
	}
	
	@Override
	public MarketInternal findMarketBy(String marketCode_) {
		return _mapIdToMarket.get(marketCode_);
	}

	@Override
	public Market[] findMarketModelsByInstrument(String instrumentCode_) {
		List<Market> markets = new ArrayList<Market>();
		for (MarketInternal market : _mapIdToMarket.values()) {
			if(instrumentCode_.equals(market.getMarketModel().getInstrumentCode())) {
				markets.add(market.getMarketModel());
			}
		}
		
		return markets.toArray(new Market[markets.size()]);
	}
	
	@Override
	public PagedMarketList findMarketModelsFromCursorByInstrument(
			String instrumentCode_, String cursorString_,
			int maxNumberOfMarketsOnPage_) {
		PagedMarketList list = new PagedMarketList();
		list.setMarkets(findMarketModelsByInstrument(instrumentCode_));
		return list;
	}

//	@Override
//	public PagedOrderList findActiveOrdersFromCursorBy(String userID_,
//			String cursorString_, int maxNumberOfOrderOnPage_,
//			) {
//		List<IOrderInternal> orderList = new ArrayList<IOrderInternal>();
//		for (IOrderInternal order : _mapIdToOrder.values()) {
//			if((order.getState() == OrderState.Submitted || order.getState() == OrderState.WaitingSubmit)
//					&& userID_.equals(order.getSubmitterID())) {
//				orderList.add(order);
//			}
//		}
//		IOrderInternal[] orders = orderList.toArray(new IOrderInternal[orderList.size()]);
//		
//		return toPagedOrderList(orders);
//	}
	
	protected OrderInternal wrapOrder(Order orderModel_,
			MockDatastoresBase mockDatastoresBase_) {
		return TestUtilities.wrapOrder(orderModel_, mockDatastoresBase_);
	}
	
	public static class MockMarket extends MarketImpl {

		
		public MockMarket(Market marketModel_, MarketTimer timeTrigger_, MockDatastores datastores_) {
			super(marketModel_, timeTrigger_, datastores_, datastores_, datastores_, datastores_);
		}

		
		@Override
		public void recordLastTrade(QuoteAndSize lastTrade_, MarketDataSession marketDataSession_) {
			super.recordLastTrade(lastTrade_, marketDataSession_);
		}


		@Override
		public Instrument getInstrument() {
			Instrument instrument = new Instrument();
			instrument.setContractSize(100);
			return instrument;
		}

	}
}
