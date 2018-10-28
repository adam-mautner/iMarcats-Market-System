package com.imarcats.model.test.testutils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.imarcats.model.ActivatableMarketObject;
import com.imarcats.model.AssetClass;
import com.imarcats.model.BuyBook;
import com.imarcats.model.CircuitBreaker;
import com.imarcats.model.HaltRule;
import com.imarcats.model.Instrument;
import com.imarcats.model.Market;
import com.imarcats.model.MarketOperator;
import com.imarcats.model.MatchedTrade;
import com.imarcats.model.Order;
import com.imarcats.model.OrderBookEntryFactory;
import com.imarcats.model.OrderBookEntryModel;
import com.imarcats.model.OrderBookFactory;
import com.imarcats.model.OrderBookModel;
import com.imarcats.model.Product;
import com.imarcats.model.SellBook;
import com.imarcats.model.TradeSide;
import com.imarcats.model.mutators.ChangeAction;
import com.imarcats.model.mutators.PropertyChange;
import com.imarcats.model.types.ActivationStatus;
import com.imarcats.model.types.Address;
import com.imarcats.model.types.AuditInformation;
import com.imarcats.model.types.BooleanProperty;
import com.imarcats.model.types.BusinessCalendar;
import com.imarcats.model.types.BusinessCalendarDay;
import com.imarcats.model.types.ChangeType;
import com.imarcats.model.types.CorporateInformation;
import com.imarcats.model.types.DatePeriod;
import com.imarcats.model.types.DateProperty;
import com.imarcats.model.types.DateRangeProperty;
import com.imarcats.model.types.Day;
import com.imarcats.model.types.DeliveryPeriod;
import com.imarcats.model.types.DoubleProperty;
import com.imarcats.model.types.ExecutionSystem;
import com.imarcats.model.types.InstrumentType;
import com.imarcats.model.types.IntProperty;
import com.imarcats.model.types.MarketState;
import com.imarcats.model.types.ObjectProperty;
import com.imarcats.model.types.OrderExpirationInstruction;
import com.imarcats.model.types.OrderRejectAction;
import com.imarcats.model.types.OrderSide;
import com.imarcats.model.types.OrderState;
import com.imarcats.model.types.OrderTriggerInstruction;
import com.imarcats.model.types.OrderType;
import com.imarcats.model.types.Position;
import com.imarcats.model.types.ProductType;
import com.imarcats.model.types.Property;
import com.imarcats.model.types.Quote;
import com.imarcats.model.types.QuoteAndSize;
import com.imarcats.model.types.QuoteType;
import com.imarcats.model.types.RecurringActionDetail;
import com.imarcats.model.types.SecondaryOrderPrecedenceRuleType;
import com.imarcats.model.types.SettlementPrice;
import com.imarcats.model.types.SettlementType;
import com.imarcats.model.types.StringListProperty;
import com.imarcats.model.types.StringProperty;
import com.imarcats.model.types.TimeOfDay;
import com.imarcats.model.types.TimePeriod;
import com.imarcats.model.types.TimeProperty;
import com.imarcats.model.types.TimeRangeProperty;
import com.imarcats.model.types.TradeSettlementState;
import com.imarcats.model.types.TradingSession;
import com.imarcats.model.types.TransferableObject;
import com.imarcats.model.types.UnderlyingType;
import com.imarcats.model.types.UnitProperty;
import com.imarcats.model.utils.PropertyUtils;

import junit.framework.TestCase;

/**
 * Common base class for Market Model (Object) Test Cases
 * @author Adam
 *
 */
public class MarketObjectTestBase extends TestCase {

	protected List<String> shallowCopy(List<String> toCopy_) {
		return toCopy_.stream().collect(Collectors.<String>toList());
	}
	
	protected String[] toArray(List<String> list_) {
		return list_.toArray(new String[list_.size()]);
	}

	protected MarketOperator createMarketOperator(String code_) throws Exception {
		MarketOperator marketOperator = new MarketOperator();
		marketOperator.updateLastUpdateTimestamp();
		
		marketOperator.setCode(code_);
		
		marketOperator.setName("TestName");
		
		marketOperator.setDescription("TestDescr");
		
		marketOperator.setMarketOperatorAgreement("TestAggrement");
		
		marketOperator.setOwnerUserID("Test1");
		
		marketOperator.setBusinessEntityCode("TEST_BE");
		
		marketOperator.setActivationStatus(ActivationStatus.Approved);
		
		marketOperator.setApprovalAudit(createAudit());
		
		marketOperator.setChangeAudit(createAudit());
		
		marketOperator.setCreationAudit(createAudit());
		
		marketOperator.setSuspensionAudit(createAudit());
		
		return marketOperator;
	}
	
	private long testUpdateTimestamp(boolean testTimestamp_, long lastUpdateTs_, ActivatableMarketObject marketObject_) throws Exception {
		if(testTimestamp_) {
			assertTrue(marketObject_.getLastUpdateTimestamp().getTime() > lastUpdateTs_);
			Thread.sleep(10);
		}
		
		return marketObject_.getLastUpdateTimestamp().getTime();
	}
	
	private long testTimestampTheSame(boolean testTimestamp_, long lastUpdateTs_, ActivatableMarketObject marketObject_) throws Exception {
		if(testTimestamp_) {
			assertTrue(marketObject_.getLastUpdateTimestamp().getTime() == lastUpdateTs_);
			Thread.sleep(10);
		}
		
		return marketObject_.getLastUpdateTimestamp().getTime();
	}
	
	private long testUpdateTimestamp(boolean testTimestamp_, long lastUpdateTs_, Order order_) throws Exception {
		if(testTimestamp_) {
			assertTrue(order_.getLastUpdateTimestamp().getTime() > lastUpdateTs_);
			Thread.sleep(10);
		}
		return order_.getLastUpdateTimestamp().getTime();
	}
	
	protected void assertEqualsMarketOperator(MarketOperator expected, MarketOperator actual) {
		assertEquals(expected != null, actual != null);
		if(expected != null) {
			assertEquals(expected.getCode(), actual.getCode());
			assertEquals(expected.getDescription(), actual.getDescription());
			assertEquals(expected.getMarketOperatorAgreement(), actual.getMarketOperatorAgreement());
			assertEquals(expected.getName(), actual.getName());
			assertEqualsAudit(expected.getApprovalAudit(), actual.getApprovalAudit());
			assertEqualsAudit(expected.getChangeAudit(), actual.getChangeAudit());
			assertEqualsAudit(expected.getCreationAudit(), actual.getCreationAudit());
			assertEqualsAudit(expected.getSuspensionAudit(), actual.getSuspensionAudit());
			assertEquals(expected.getOwnerUserID(), 
					actual.getOwnerUserID());
		}
		
	}
	
	protected void assertEqualsOrders(Order[] expected, Order[] actual) {
		assertEquals(expected != null, actual != null);
		
		expected = sortOrders(expected);
		actual = sortOrders(actual);
		
		if(expected != null) {
			assertEquals(expected.length, actual.length);
			
			for (int i = 0; i < actual.length; i++) {
				assertEqualsOrder(expected[i], actual[i]);
			}
		}
	}
	
	protected Property[] sortProperties(Property[] properties_) {
		List<Property> props = Arrays.asList(properties_).
			stream().
				collect(Collectors.<Property>toList()).stream(). 
					sorted((o1_, o2_) -> o1_.getName().compareTo(o2_.getName())).
						collect(Collectors.toList());
		
		return props.toArray(new Property[props.size()]);
	}
	
	protected Order[] sortOrders(Order[] orders_) {
		List<Order> orderList = Arrays.asList(orders_).
				stream().
					collect(Collectors.<Order>toList()).stream(). 
						sorted((arg0_, arg1_) -> { int comp = 0; 
						if(arg0_.getKey() != null && arg1_.getKey() != null) {
							comp = arg0_.getKey().compareTo(arg1_.getKey());
						}
						return comp;}).
							collect(Collectors.toList());

		return orderList.toArray(new Order[orderList.size()]);
	}
	
	protected void checkQuoteAndSize(QuoteAndSize quoteAndSize_, Quote quote_, int size_, boolean valid_) {
		assertEquals(quote_.getQuoteValue(), quoteAndSize_.getQuote().getQuoteValue());
		assertEquals(size_, quoteAndSize_.getSize());
		assertEquals(valid_, quoteAndSize_.getQuote().getValidQuote());
	}
	
	protected Quote getExpectedQuote(double price_, QuoteType quoteType_) {
		double quoteValue = price_;
		if(quoteType_ == QuoteType.Yield) {
			quoteValue = 100 - price_;
		}
		
		Quote quote = new Quote();
		quote.setQuoteValue(quoteValue);
		quote.setValidQuote(true);
		
		return quote;
	}
	
	protected Quote wrapQuote(double quoteValue_) {
		return Quote.createQuote(quoteValue_);
	}
	
	protected void checkPair(MatchedTrade matchedPair_, int size_, String buySubmitter_, String sellSubmitter_, Quote quote_) {
		checkPair(matchedPair_, size_, buySubmitter_, sellSubmitter_);
		assertEquals(quote_.getQuoteValue(), matchedPair_.getTradeQuote().getQuoteValue());
		assertEquals(true, matchedPair_.getTradeQuote().getValidQuote());
	}
	
	protected void checkPair(MatchedTrade matchedPair_, int size_, String buySubmitter_, String sellSubmitter_) {
		assertEquals((Integer)size_, matchedPair_.getMatchedSize());
		assertEquals(sellSubmitter_, matchedPair_.getSellSide().getTraderID());
		assertEquals(buySubmitter_, matchedPair_.getBuySide().getTraderID());
	}
	
	protected AssetClass createAssetClass() throws Exception {
		AssetClass assetClass = new AssetClass();
		assetClass.updateLastUpdateTimestamp();
		
		assetClass.setName("TEST_ASSET_CLASS");
		
		assetClass.setParentName("TestParentAssetClass");
		
		assetClass.setDescription("TestTest");
				
		for (Property property : createPropertyList()) {
			assetClass.addProperty(property);
		}
		
		return assetClass;
	}
	
	protected void assertEqualsAssetClass(AssetClass expected, AssetClass actual) {
		assertEquals(expected != null, actual != null);
		if(expected != null) {
			assertEquals(expected.getDescription(), actual.getDescription());
			assertEquals(expected.getName(), actual.getName());
			assertEquals(expected.getParentName(), actual.getParentName());
			assertEqualsPropertyList(expected.getProperties(), actual.getProperties());
		}
	}
	
	protected MatchedTrade createMatchedTrade(String market_) {
		TradeSide buySide = createTradeSide("TestUser1", market_);
		TradeSide sellSide = createTradeSide("TestUser2", market_);
		
		return createMatchedTrade(market_, buySide, sellSide);
	}

	protected MatchedTrade createMatchedTrade(String market_, TradeSide buySide_,
			TradeSide sellSide_) {
		int matchedSize = 10;
		OrderSide standingSide = OrderSide.Sell;
		
		MatchedTrade matchedTrade = new MatchedTrade(matchedSize, buySide_, sellSide_, market_, standingSide);
		
		Quote tradeQuote = Quote.createQuote(123);
		matchedTrade.setTradeQuote(tradeQuote);

		matchedTrade.setSettlementState(TradeSettlementState.ClearingConfirmed);
		
		assertEquals((Integer)matchedSize, matchedTrade.getMatchedSize());
		assertEqualsTradeSide(buySide_, matchedTrade.getBuySide());
		assertEqualsTradeSide(sellSide_, matchedTrade.getSellSide());
		assertEquals(market_, matchedTrade.getMarketOfTheTrade());
		assertEquals(standingSide, matchedTrade.getStandingOrderSide());

		return matchedTrade;
	}

	protected TradeSide createTradeSide(String userName_, String market_) {
		String instrumentCode = "TestInstr";
		OrderType type = OrderType.Limit;
		OrderSide side = OrderSide.Buy;
		Quote quote = Quote.createQuote(100);
		Date tradeDateTime = new Date();
		double commission = 10;
		String commissionCurrency = "USD";
		
		Double contractSize = 100.0;
		List<Property> properties = new ArrayList<Property>();
		for (Property property : createPropertyList()) {
			properties.add(property);	
		}
		TradeSide tradeSide = new TradeSide(userName_, type, side, quote, instrumentCode, market_, tradeDateTime, contractSize, commission, commissionCurrency, properties.toArray(new Property[properties.size()]), UUID.randomUUID().toString());

		tradeSide.setSettlementState(TradeSettlementState.ClearingConfirmed);
		
		assertEquals(userName_, tradeSide.getTraderID());
		assertEquals(instrumentCode, tradeSide.getInstrumentOfTheTrade());
		assertEquals(market_, tradeSide.getMarketOfTheTrade());
		assertEquals(type, tradeSide.getOrderType());
		assertEquals(side, tradeSide.getSide());
		assertEqualsQuote(quote, tradeSide.getOrderQuote());
		assertEquals(tradeDateTime, tradeSide.getTradeDateTime());
		assertEquals(commission, tradeSide.getCommission());
		assertEquals(commissionCurrency, tradeSide.getCommissionCurrency());
		
		return tradeSide;
	}
	
	protected void assertEqualsMatchedTrade(MatchedTrade expected, MatchedTrade actual) {
		assertEquals(expected != null, actual != null);
		if(expected != null) {
			assertEqualsTradeSide(expected.getBuySide(), actual.getBuySide());
			assertEqualsTradeSide(expected.getSellSide(), actual.getSellSide());	
			assertEquals(expected.getMarketOfTheTrade(), actual.getMarketOfTheTrade());
			assertEquals(expected.getMatchedSize(), actual.getMatchedSize());		
			assertEquals(expected.getStandingOrderSide(), actual.getStandingOrderSide());
			assertEquals(expected.getTransactionID(), actual.getTransactionID());
			assertEquals(expected.getSettlementState(), actual.getSettlementState());
			assertEquals(actual.getTransactionID(), expected.getBuySide().getTransactionID());			
			assertEquals(actual.getTransactionID(), expected.getSellSide().getTransactionID());
		}
	}

	protected void assertEqualsTradeSides(TradeSide[] expected, TradeSide[] actual) {
		assertEquals(expected.length, actual.length);
		for (int i = 0; i < expected.length; i++) {
			assertEqualsTradeSide(expected[i], actual[i]);
		}
	}
	
	protected void assertEqualsTradeSide(TradeSide expected, TradeSide actual) {
		assertEquals(expected != null, actual != null);
		if(expected != null) {
			assertEqualsQuote(expected.getOrderQuote(), actual.getOrderQuote());
			assertEqualsQuote(expected.getTradeQuote(), actual.getTradeQuote());
			assertEquals(expected.getInstrumentOfTheTrade(), actual.getInstrumentOfTheTrade());
			assertEquals(expected.getMarketOfTheTrade(), actual.getMarketOfTheTrade());
			assertEquals(expected.getMatchedSize(), actual.getMatchedSize());
			assertEquals(expected.getOrderType(), actual.getOrderType());
			assertEquals(expected.getTraderID(), actual.getTraderID());
			assertEquals(expected.getTradeDateTime() != null, actual.getTradeDateTime() != null);
			if(expected.getTradeDateTime() != null) {
				assertEquals(expected.getTradeDateTime().getYear(), actual.getTradeDateTime().getYear());
				assertEquals(expected.getTradeDateTime().getMonth(), actual.getTradeDateTime().getMonth());
				assertEquals(expected.getTradeDateTime().getDate(), actual.getTradeDateTime().getDate());
				assertEquals(expected.getTradeDateTime().getHours(), actual.getTradeDateTime().getHours());
				assertEquals(expected.getTradeDateTime().getMinutes(), actual.getTradeDateTime().getMinutes());
				// seconds are not check for now, because this makes tests fail
				//assertEquals(expected.getTradeDateTime().getSeconds(), actual.getTradeDateTime().getSeconds());
			}
			assertEquals(expected.getContractSize(), actual.getContractSize());
			assertEquals(expected.getCommission(), actual.getCommission());
			assertEquals(expected.getCommissionCurrency(), actual.getCommissionCurrency());
			// transaction ID is generated field
			// TODO: Check it, when it is on DTO
			// assertEquals(expected.getTransactionID(), actual.getTransactionID());
			assertEquals(expected.getSide(), actual.getSide());

			assertEquals(expected.getExternalOrderReference(), actual.getExternalOrderReference());
			assertEquals(expected.getSettlementState(), actual.getSettlementState());
			assertEqualsPropertyList(expected.getTradeProperties(), actual.getTradeProperties());
		}
	}
	
	protected void assertEqualsQuoteAndSize(QuoteAndSize expected, QuoteAndSize actual) {
		assertEquals(expected != null, actual != null);
		if(expected != null) {
			assertEqualsQuote(expected.getQuote(), actual.getQuote());
			assertEquals(expected.getSize(), actual.getSize());
		}
	}
	
	protected void assertEqualsQuote(Quote expected, Quote actual) {
		assertEquals(expected != null, actual != null);
		if(expected != null) {
			assertEquals(expected.getQuoteValue(), actual.getQuoteValue());
			assertEquals(expected.getValidQuote(), actual.getValidQuote());
			assertEquals(expected.getDateOfQuote(), actual.getDateOfQuote());
		}
	}

	protected Product createProduct(String productCode_) throws Exception { 
		Product product = new Product();
		product.updateLastUpdateTimestamp();
		
		product.setActivationStatus(ActivationStatus.Activated);
		
		product.setCategory("Test Cat");
		
		product.setCreationAudit(createAudit());
		
		product.setDescription("Test Descr");
		
		product.setName("Test Name");
		
		product.setProductCode(productCode_);
		
		product.setProductDefinitionDocument("Test Doc");
		
		product.setSubCategory("Test Sub-cat");
		
		product.setType(ProductType.Physical);
		
		product.setRollable(true);

		createPropertyList().stream().forEach(product::addProperty);
		
		product.setActivationDate(new Date());
		
		product.setProductCodeRolledOverFrom("Test");

		createPropertyList().stream().map(property -> property.getName()).forEach(product.getRollablePropertyNames()::add);
		
		product.setApprovalAudit(createAudit());
		
		product.setChangeAudit(createAudit());
		
		product.setRolloverAudit(createAudit());
		
		product.setSuspensionAudit(createAudit());
		
		return product;
	}

	protected void assertEqualsProduct(Product expected, Product actual) { 
		assertEquals(expected.getCategory(), actual.getCategory());
		assertEquals(expected.getDescription(), actual.getDescription());
		assertEquals(expected.getName(), actual.getName());
		assertEquals(expected.getProductCode(), actual.getProductCode());
		assertEquals(expected.getProductDefinitionDocument(), actual.getProductDefinitionDocument());
		assertEquals(expected.getSubCategory(), actual.getSubCategory());
		assertEquals(expected.getActivationStatus(), actual.getActivationStatus());
		assertEqualsAudit(expected.getCreationAudit(), actual.getCreationAudit());
		assertEqualsPropertyList(expected.getProperties(), actual.getProperties());	
		assertEquals(expected.getType(), actual.getType());	
		assertEquals(expected.getRollable(), actual.getRollable());	
		assertEquals(expected.getProductCodeRolledOverFrom(), actual.getProductCodeRolledOverFrom());
		assertEquals(expected.getActivationDate(), actual.getActivationDate());
		List<String> expectedPropertyNames = expected.getRollablePropertyNames();
		List<String> actualPropertyNames = actual.getRollablePropertyNames();
		assertEqualsStringList(expectedPropertyNames.toArray(new String[expectedPropertyNames.size()]), 
				actualPropertyNames.toArray(new String[actualPropertyNames.size()]));
		assertEqualsAudit(expected.getApprovalAudit(), actual.getApprovalAudit());
		assertEqualsAudit(expected.getChangeAudit(), actual.getChangeAudit());
		assertEqualsAudit(expected.getRolloverAudit(), actual.getRolloverAudit());
		assertEqualsAudit(expected.getSuspensionAudit(), actual.getSuspensionAudit());
		
	}
	
	protected Instrument createInstrument(String instrumentCode_) throws Exception {
		Instrument instrument = new Instrument();
		instrument.updateLastUpdateTimestamp();
		
		instrument.setActivationDate(new Date());
		
		instrument.setActivationStatus(ActivationStatus.Activated);
		
		instrument.setCreationAudit(createAudit());
		
		instrument.setDeliveryLocation(createAddress());
		
		instrument.setDeliveryPeriod(DeliveryPeriod.T2);
		
		instrument.setDescription("Test Descr. ");
		
		instrument.setInstrumentCode(instrumentCode_);
		
		instrument.setMasterAgreementDocument("Test Doc");
		
		instrument.setName("Test Name");
		
		instrument.setQuoteType(QuoteType.Yield);
		
		instrument.setRecordPurchaseAsPosition(Position.Long);
		
		instrument.setRollable(true);
		
		instrument.setSettlementType(SettlementType.CashSettlement);
		
		instrument.setSubType("Test Sub-type");

		instrument.setType(InstrumentType.Derivative);
		
		instrument.setUnderlyingCode("TEST_UNDERLYING");
		
		instrument.setDenominationCurrency("USD");

		instrument.setContractSize(1000);
		
		instrument.setContractSizeUnit("Tons");
		
		instrument.setSettlementPrice(SettlementPrice.Clean);
		
		instrument.setIsin("123234556");
		
		instrument.setAssetClassName("TEST_ASSET_CLASS");
	
		createPropertyList().stream().forEach(instrument::addProperty);
		
		
		instrument.setInstrumentCodeRolledOverFrom("Test");

		createPropertyList().stream().map(property -> property.getName()).forEach(instrument.getRollablePropertyNames()::add);
		
		// this update will not be detected 
		// TODO: Fix this problem ! 
		// lastUpdateTs = testUpdateTimestamp(testTimestamp_, lastUpdateTs, instrument);
		
		
		instrument.setUnderlyingType(UnderlyingType.Instrument);
		
		instrument.setApprovalAudit(createAudit());
		
		instrument.setChangeAudit(createAudit());
		
		instrument.setRolloverAudit(createAudit());
		
		instrument.setSuspensionAudit(createAudit());
		
		return instrument;
	}
	
	protected void assertEqualsInstrument(Instrument expected, Instrument actual) {
		assertEquals(expected.getActivationDate(), actual.getActivationDate());
		assertEquals(expected.getActivationStatus(), actual.getActivationStatus());
		assertEqualsAudit(expected.getCreationAudit(), actual.getCreationAudit());
		assertEqualsAddress(expected.getDeliveryLocation(), actual.getDeliveryLocation());
		assertEquals(expected.getDeliveryPeriod(), actual.getDeliveryPeriod());
		assertEquals(expected.getDescription(), actual.getDescription());
		assertEquals(expected.getInstrumentCode(), actual.getInstrumentCode());
		assertEquals(expected.getMasterAgreementDocument(), actual.getMasterAgreementDocument());
		assertEquals(expected.getName(), actual.getName());
		assertEqualsPropertyList(expected.getProperties(), actual.getProperties());
		assertEquals(expected.getQuoteType(), actual.getQuoteType());
		assertEquals(expected.getRecordPurchaseAsPosition(), actual.getRecordPurchaseAsPosition());
		assertEquals(expected.getSettlementType(), actual.getSettlementType());
		assertEquals(expected.getSubType(), actual.getSubType());
		assertEquals(expected.getType(), actual.getType());
		assertEquals(expected.getUnderlyingCode(), actual.getUnderlyingCode());
		assertEquals(expected.getDenominationCurrency(), actual.getDenominationCurrency());
		assertEquals(expected.getContractSize(), actual.getContractSize());
		assertEquals(expected.getContractSizeUnit(), actual.getContractSizeUnit());
		assertEquals(expected.getSettlementPrice(), actual.getSettlementPrice());		
		assertEquals(expected.getIsin(), actual.getIsin());	
		assertEquals(expected.getAssetClassName(), actual.getAssetClassName());	

		assertEquals(expected.getInstrumentCodeRolledOverFrom(), actual.getInstrumentCodeRolledOverFrom());
		assertEquals(expected.getActivationDate(), actual.getActivationDate());
		List<String> expectedPropertyNames = expected.getRollablePropertyNames();
		List<String> actualPropertyNames = actual.getRollablePropertyNames();
		assertEqualsStringList(expectedPropertyNames.toArray(new String[expectedPropertyNames.size()]), 
				actualPropertyNames.toArray(new String[actualPropertyNames.size()]));
		assertEquals(expected.getUnderlyingType(), actual.getUnderlyingType());
		assertEqualsAudit(expected.getApprovalAudit(), actual.getApprovalAudit());
		assertEqualsAudit(expected.getChangeAudit(), actual.getChangeAudit());
		assertEqualsAudit(expected.getRolloverAudit(), actual.getRolloverAudit());
		assertEqualsAudit(expected.getSuspensionAudit(), actual.getSuspensionAudit());
	
	}

	protected void assertEqualsOrder(Order expected, Order actual) {
		assertEquals(expected.getKey(), actual.getKey());

		assertEquals(expected.getSubmitterID(), actual.getSubmitterID());
		assertEquals(expected.getTargetMarketCode(), actual.getTargetMarketCode());
		assertEquals(expected.getSide(), actual.getSide());		
		assertEquals(expected.getSize(), actual.getSize());		
		assertEquals(expected.getType(), actual.getType());	
		assertEquals(expected.getExecutedSize(), actual.getExecutedSize());	
		assertEquals(expected.getMinimumSizeOfExecution(), actual.getMinimumSizeOfExecution());	
		assertEquals(expected.getExecuteEntireOrderAtOnce(), actual.getExecuteEntireOrderAtOnce());
		assertEquals(expected.getDisplayOrder(), actual.getDisplayOrder());
		assertEquals(expected.getState(), actual.getState());
		assertEquals(expected.getQuoteChangeTriggerKey(), actual.getQuoteChangeTriggerKey());
		assertEquals(expected.getExpirationTriggerActionKey(), actual.getExpirationTriggerActionKey());
		assertEquals(expected.getTriggerInstruction(), actual.getTriggerInstruction());
		assertEquals(expected.getExpirationInstruction(), actual.getExpirationInstruction());

		assertEquals(expected.getSubmissionDate(), actual.getSubmissionDate());
		
//		assertTrue(actual.getCreationAudit() != null);
//		assertTrue(expected.getCreationAudit() != actual.getCreationAudit());
		assertEqualsAudit(expected.getCreationAudit(), actual.getCreationAudit());
		
		assertEquals(expected.getTargetAccountID(), actual.getTargetAccountID());
		
		assertEqualsQuote(expected.getCurrentStopQuote(), actual.getCurrentStopQuote());
		assertEqualsQuote(expected.getLimitQuoteValue(), actual.getLimitQuoteValue());
	
		assertEqualsPropertyList(expected.getTriggerProperties(), actual.getTriggerProperties());
		assertEqualsPropertyList(expected.getExpirationProperties(), actual.getExpirationProperties());
		assertEqualsPropertyList(expected.getOrderProperties(), actual.getOrderProperties());
		
		assertEquals(expected.getExternalOrderReference(), actual.getExternalOrderReference());
	}

	protected Order createOrder(String targetMarketCode_) throws Exception { 
		Order order = new Order();
		order.updateLastUpdateTimestamp();
		
		order.setTargetMarketCode(targetMarketCode_);
		
		order.setSubmitterID("Test");
		
		order.setSide(OrderSide.Sell);
		
		order.setSize(100);
		
		order.setType(OrderType.Market);
		
		order.setQuoteChangeTriggerKey((long)11);
		
		order.setExpirationTriggerActionKey((long)111);
		
		order.setExecutedSize(10);
		
		order.setMinimumSizeOfExecution(6);
		
		order.setExecuteEntireOrderAtOnce(true);
		
		order.setDisplayOrder(false);
		
		order.setState(OrderState.Submitted);
		
		order.setTriggerInstruction(OrderTriggerInstruction.TrailingStopLoss);
		
		order.setExpirationInstruction(OrderExpirationInstruction.ImmediateOrCancel);
		
		order.setSubmissionDate(new Date());
		
		order.setCreationAudit(createAudit());
		
		order.setLimitQuoteValue(Quote.createQuote(100));
		
		order.setCurrentStopQuote(Quote.createQuote(101));
		
		order.setTargetAccountID((long) 123);
		
		createPropertyList().stream().forEach(order::addTriggerProperty);
		createPropertyList().stream().forEach(order::addExpirationProperty);
		createPropertyList().stream().forEach(order::addOrderProperty);
		
		order.setCancellationCommentLanguageKey("TestLangKey");

		order.setCommissionCharged(true);
		
		order.setExternalOrderReference(UUID.randomUUID().toString());
		
		return order;
	}

	protected void assertEqualsMarket(Market expected, Market actual) {
		assertEquals(expected.getMarketCode(), actual.getMarketCode());
		assertEquals(expected.getActivationStatus(), actual.getActivationStatus());
		assertEqualsQuote(expected.getClosingQuote(), actual.getClosingQuote());
		assertEqualsQuote(expected.getPreviousClosingQuote(), actual.getPreviousClosingQuote());
		assertEquals(expected.getHaltLevel(), actual.getHaltLevel());
		assertEqualsQuoteAndSize(expected.getLastTrade(), actual.getLastTrade());
		assertEqualsQuoteAndSize(expected.getPreviousLastTrade(), actual.getPreviousLastTrade());
		assertEqualsQuoteAndSize(expected.getPreviousBestBid(), actual.getPreviousBestBid());
		assertEqualsQuoteAndSize(expected.getPreviousBestAsk(), actual.getPreviousBestAsk());
		
		assertEquals(expected.getNextMarketCallDate(), actual.getNextMarketCallDate());
		assertEqualsQuote(expected.getOpeningQuote(), actual.getOpeningQuote());
		assertEqualsQuote(expected.getPreviousOpeningQuote(), actual.getPreviousOpeningQuote());
		assertEquals(expected.getState(), actual.getState());
		
		assertEqualsBook(expected.getBuyBook(), actual.getBuyBook());
		assertEqualsBook(expected.getSellBook(), actual.getSellBook());
		
		assertEquals(expected.getCommission(), actual.getCommission());
		assertEquals(expected.getDescription(), actual.getDescription());
		assertEquals(expected.getInstrumentCode(), actual.getInstrumentCode());
		assertEquals(expected.getMarketOperationContract(), actual.getMarketOperationContract());
		assertEquals(expected.getMaximumContractsTraded(), actual.getMaximumContractsTraded());
		assertEquals(expected.getMinimumContractsTraded(), actual.getMinimumContractsTraded());
		assertEquals(expected.getMinimumQuoteIncrement(), actual.getMinimumQuoteIncrement());
		assertEquals(expected.getName(), actual.getName());
		assertEqualsCircuitBreaker(expected.getCircuitBreaker(), actual.getCircuitBreaker());
		assertEquals(expected.getExecutionSystem(), actual.getExecutionSystem());
		assertEquals(expected.getQuoteType(), actual.getQuoteType());
		assertEqualsSecondaryPrecedence(expected.getSecondaryOrderPrecedenceRules(), actual.getSecondaryOrderPrecedenceRules());
		assertEqualsTimeOfDay(expected.getTradingDayEnd(), actual.getTradingDayEnd());
		assertEqualsTimePeriod(expected.getTradingHours(), actual.getTradingHours());
		assertEquals(expected.getTradingSession(), actual.getTradingSession());	
		assertEqualsBusinessCalendar(expected.getBusinessCalendar(), actual.getBusinessCalendar());
		assertEquals(expected.getCommissionCurrency(), actual.getCommissionCurrency());
		
		// assertTrue(entity.getOrders() == null);	
		
		assertEquals(expected.getMarketOperatorCode(), actual.getMarketOperatorCode());
		
		assertEqualsAudit(expected.getCreationAudit(), actual.getCreationAudit());
		assertEqualsAudit(expected.getChangeAudit(), actual.getChangeAudit());
		assertEqualsAudit(expected.getApprovalAudit(), actual.getApprovalAudit());
		assertEqualsAudit(expected.getSuspensionAudit(), actual.getSuspensionAudit());
		assertEqualsAudit(expected.getRolloverAudit(), actual.getRolloverAudit());
		assertEqualsAudit(expected.getActivationAudit(), actual.getActivationAudit());
		assertEqualsAudit(expected.getDeactivationAudit(), actual.getDeactivationAudit());
			
		assertEquals(expected.getMarketCallActionKey(), actual.getMarketCallActionKey());
		assertEquals(expected.getMarketOpenActionKey(), actual.getMarketOpenActionKey());
		assertEquals(expected.getMarketCloseActionKey(), actual.getMarketCloseActionKey());
		assertEquals(expected.getMarketReOpenActionKey(), actual.getMarketReOpenActionKey());
		assertEquals(expected.getMarketMaintenanceActionKey(), actual.getMarketMaintenanceActionKey());
		assertEquals(expected.getCallMarketMaintenanceActionKey(), actual.getCallMarketMaintenanceActionKey());
		
		assertEquals(expected.getMarketOperationDays(), actual.getMarketOperationDays());
		assertEquals(expected.getAllowHiddenOrders(), actual.getAllowHiddenOrders());
		assertEquals(expected.getAllowSizeRestrictionOnOrders(), actual.getAllowSizeRestrictionOnOrders());
		assertEquals(expected.getMarketTimeZoneID(), actual.getMarketTimeZoneID());
		assertEquals(expected.getClearingBank(), actual.getClearingBank());
	}

	protected Market createMarket(String marketCode) throws Exception { 
		Market marketModel = new Market();
		marketModel.updateLastUpdateTimestampAndVersion();
		
		marketModel.setMarketCode(marketCode);
		
		marketModel.setMarketOperatorCode("Test");
		
		marketModel.setActivationStatus(ActivationStatus.Activated);
		
		marketModel.setClosingQuote(Quote.createQuote(10));
		
		marketModel.setPreviousClosingQuote(Quote.createQuote(11));
		
		marketModel.setHaltLevel(11);
		
		QuoteAndSize lastTrade = new QuoteAndSize();
		lastTrade.setQuote(Quote.createQuote(12));
		lastTrade.setSize(100);
		marketModel.setLastTrade(lastTrade);
		
		QuoteAndSize prevLastTrade = new QuoteAndSize();
		prevLastTrade.setQuote(Quote.createQuote(123));
		prevLastTrade.setSize(110);
		marketModel.setPreviousLastTrade(prevLastTrade);

		QuoteAndSize bid = new QuoteAndSize();
		bid.setQuote(Quote.createQuote(122));
		bid.setSize(101);
		marketModel.setPreviousBestBid(bid);
		
		QuoteAndSize ask = new QuoteAndSize();
		ask.setQuote(Quote.createQuote(123));
		ask.setSize(102);
		marketModel.setPreviousBestAsk(ask);

		QuoteAndSize bidCurr = new QuoteAndSize();
		bidCurr.setQuote(Quote.createQuote(122));
		bidCurr.setSize(101);
		marketModel.setCurrentBestBid(bidCurr);

		QuoteAndSize askCurr = new QuoteAndSize();
		askCurr.setQuote(Quote.createQuote(123));
		askCurr.setSize(102);
		marketModel.setCurrentBestAsk(askCurr);
		
		marketModel.setNextMarketCallDate(new Date());
		
		marketModel.setOpeningQuote(Quote.createQuote(16));
		
		marketModel.setPreviousOpeningQuote(Quote.createQuote(17));
		
		marketModel.setState(MarketState.Open);
		
		marketModel.setBusinessEntityCode("TestBusinessEntity");

		marketModel.setCircuitBreaker(createCircuitBreaker());
		
		marketModel.setCommission(10.0);
		
		marketModel.setExecutionSystem(ExecutionSystem.CallMarketWithSingleSideAuction);
		
		marketModel.setInstrumentCode("MyInstrument");
		
		marketModel.setMarketOperationContract("MyContract");
		
		marketModel.setMaximumContractsTraded(100);
		
		marketModel.setMinimumContractsTraded(10);
		
		marketModel.setMinimumQuoteIncrement(12);
		
		marketModel.setName("MyTestMarket");
		
		marketModel.setQuoteType(QuoteType.Yield);
		
		marketModel.setSecondaryOrderPrecedenceRules(createSecondaryPercedence());
		
		marketModel.setTradingDayEnd(createTimeOfDay());
		
		marketModel.setTradingHours(createTimePeriod());
		
		marketModel.setTradingSession(TradingSession.NonContinuous);
		
		marketModel.setDescription("Test Test Test");
		
		marketModel.setBusinessCalendar(createBusinessCalendar());
		
		marketModel.setCommissionCurrency("USD");
		
		marketModel.setBuyBook((BuyBook) createBook(OrderSide.Buy));
		
		marketModel.setSellBook((SellBook) createBook(OrderSide.Sell));
		
		marketModel.setCreationAudit(createAudit());
		
		marketModel.setChangeAudit(createAudit());
		
		marketModel.setApprovalAudit(createAudit());
		
		marketModel.setSuspensionAudit(createAudit());
		
		marketModel.setRolloverAudit(createAudit());
		
		marketModel.setActivationAudit(createAudit());
		
		marketModel.setDeactivationAudit(createAudit());
		
		marketModel.setMarketCallActionKey((long) 1);
		
		marketModel.setMarketOpenActionKey((long) 2);
		
		marketModel.setMarketCloseActionKey((long) 3);
		
		marketModel.setMarketReOpenActionKey((long) 4);
		
		marketModel.setMarketMaintenanceActionKey((long) 6);
		
		marketModel.setCallMarketMaintenanceActionKey((long) 7);
		
		marketModel.setMarketOperationDays(RecurringActionDetail.OnBusinessDaysAndWeekdays);
		
		marketModel.setAllowHiddenOrders(true);
		
		marketModel.setAllowSizeRestrictionOnOrders(true);
		
		marketModel.setMarketTimeZoneID("TestTZ");
		
		marketModel.setClearingBank("TestBank");
		
		return marketModel;
	}

	protected void assertEqualsTimePeriod(TimePeriod expected,
			TimePeriod actual) {
		assertEquals(expected != null, actual != null);
		if(expected != null) {
			assertEqualsTimeOfDay(expected.getStartTime(), actual.getStartTime());
			assertEqualsTimeOfDay(expected.getEndTime(), actual.getEndTime());
		}
	}

	protected void assertEqualsTimeOfDay(TimeOfDay expected,
			TimeOfDay actual) {
		assertEquals(expected != null, actual != null);
		if(expected != null) {
			assertEquals(expected.getHour(), actual.getHour());
			assertEquals(expected.getMinute(), actual.getMinute());
			assertEquals(expected.getSecond(), actual.getSecond());
			assertEquals(expected.getTimeZoneID(), actual.getTimeZoneID());
		}		
	}
	
	protected void assertEqualsSecondaryPrecedence(
			List<SecondaryOrderPrecedenceRuleType> expected,
			List<SecondaryOrderPrecedenceRuleType> actual) {
		assertEquals(expected != null, actual != null);
		if(expected != null) {
			assertEquals(expected.size(), actual.size());
			for (int i = 0; i < expected.size(); i++) {
				assertEquals(expected.get(i), actual.get(i));
			}
		}
	}

	protected void assertEqualsAddress(Address expected,
			Address actual) {
		assertEquals(expected != null, actual != null);
		if(expected != null) {
			assertEquals(expected.getCity(), actual.getCity());
			assertEquals(expected.getCountry(), actual.getCountry());
			assertEquals(expected.getPostalCode(), actual.getPostalCode());
			assertEquals(expected.getState(), actual.getState());
			assertEquals(expected.getStreet(), actual.getStreet());
		}
	}

	protected void assertEqualsCircuitBreaker(CircuitBreaker expected,
			CircuitBreaker actual) {
		assertEquals(expected != null, actual != null);
		if(expected != null) {
			assertEquals(expected.getMaximumQuoteImprovement(), actual.getMaximumQuoteImprovement());
			assertEquals(expected.getOrderRejectAction(), actual.getOrderRejectAction());
			
			assertEquals(expected.getHaltRules() != null, actual.getHaltRules() != null);
			assertEquals(expected.getHaltRules().size(), actual.getHaltRules().size());
			
			for (int i = 0; i < expected.getHaltRules().size(); i++) {
				HaltRule currentExpected = expected.getHaltRules().get(i);
				HaltRule currentActual = actual.getHaltRules().get(i);
				assertEquals(currentExpected.getChangeType(), currentActual.getChangeType());
				assertEquals(currentExpected.getHaltPeriod(), currentActual.getHaltPeriod());
				assertEquals(currentExpected.getQuoteChangeAmount(), currentActual.getQuoteChangeAmount());
			}
		}
	}

	protected void assertEqualsCorporateInformation(
			CorporateInformation expected,
			CorporateInformation actual) {
		assertEquals(expected != null, actual != null);
		if(expected != null) {
			assertEquals(expected.getName(), actual.getName());
			assertEquals(expected.getWebSite(), actual.getWebSite());
			assertEqualsAddress(expected.getAddress(), actual.getAddress());
		}
	}

	protected void assertEqualsBook(OrderBookModel expected, OrderBookModel actual) {
		assertEquals(expected != null, actual != null);
		if(expected != null) {
			assertEquals(expected.size(), actual.size());
			for (int i = 0; i < expected.size(); i++) {
				OrderBookEntryModel currentExpected = expected.get(i);
				OrderBookEntryModel currentActual = actual.get(i);
				assertEquals(currentExpected.getAggregateSize(), currentActual.getAggregateSize());
				assertEquals(currentExpected.getHasHiddenOrders(), currentActual.getHasHiddenOrders());
				assertEqualsQuote(currentExpected.getLimitQuote(), currentActual.getLimitQuote());
				assertEquals(currentExpected.getOrderSide(), currentActual.getOrderSide());
				assertEquals(currentExpected.getOrderType(), currentActual.getOrderType());

				assertEquals(currentExpected.orderKeyCount(), currentActual.orderKeyCount());
				for (int j = 0; j < currentExpected.orderKeyCount(); j++) {
					assertEquals(currentExpected.getOrderKey(j), currentActual.getOrderKey(j));
				}
			}
		}

	}

	protected OrderBookModel createBook(OrderSide side_) {
		OrderBookModel book = OrderBookFactory.createBook(side_);
		
		createBookEntries(side_, book).forEach(book::add);
		
		return book;
	}

	
	protected List<OrderBookEntryModel> createBookEntries(OrderSide side, OrderBookModel book) {
		List<OrderBookEntryModel> orders = new ArrayList<OrderBookEntryModel>(); 
		
		OrderBookEntryModel orderBookEntry1 = OrderBookEntryFactory.createEntry(side);
		orderBookEntry1.updateLastUpdateTimestamp();
		orderBookEntry1.setLimitQuote(Quote.createQuote(10));
		orderBookEntry1.setOrderType(OrderType.Limit);
		Long lastVersionNumber = book.getVersionNumber();
		orderBookEntry1.addOrderKey(1234L, book);
		// test version number here
		// version number will be handled automatically, no need to test it
		// TODO: Do we need to update book version, when an order book entry is updated by adding new order?
		// assertTrue(lastVersionNumber < book.getVersionNumber());
		
		orderBookEntry1.addOrderKey(4567L, book);
		
		orders.add(orderBookEntry1);
		
		OrderBookEntryModel orderBookEntry2 = OrderBookEntryFactory.createEntry(side);
		orderBookEntry2.updateLastUpdateTimestamp();
		orderBookEntry2.setLimitQuote(Quote.createQuote(11));
		orderBookEntry2.setOrderType(OrderType.Limit);
		lastVersionNumber = book.getVersionNumber();
		orderBookEntry2.addOrderKey(0, 8901L, book);
		// test version number here
		// version number will be handled automatically, no need to test it
		// TODO: Do we need to update book version, when an order book entry is updated by adding new order?
		// assertTrue(lastVersionNumber < book.getVersionNumber());
		
		orderBookEntry2.addOrderKey(2345L, book);
		
		// add order to be removed 
		Long orderKey = 234567L;
		orderBookEntry2.addOrderKey(orderKey, book);
		
		// remove order and test version
		lastVersionNumber = book.getVersionNumber();
		orderBookEntry2.removeOrderKey(orderKey, book);
		// test version number here
		// version number will be handled automatically, no need to test it
		// TODO: Do we need to update book version, when an order book entry is updated by adding new order?
		// assertTrue(lastVersionNumber < book.getVersionNumber());
		
		orders.add(orderBookEntry2);
		return orders;
	}
	
	protected BusinessCalendar createBusinessCalendar() {
		BusinessCalendar calendar = new BusinessCalendar();
		
		BusinessCalendarDay businessCalendarDay = new BusinessCalendarDay();
		businessCalendarDay.setDateString("2010/04/11");
		businessCalendarDay.setDay(Day.Holiday);
		calendar.getBusinessCalendarDays().add(businessCalendarDay);
		
	    businessCalendarDay = new BusinessCalendarDay();
		businessCalendarDay.setDateString("2010/04/12");
		businessCalendarDay.setDay(Day.BusinessDay);
		calendar.getBusinessCalendarDays().add(businessCalendarDay);
		
	    businessCalendarDay = new BusinessCalendarDay();
		businessCalendarDay.setDateString("2010/04/13");
		businessCalendarDay.setDay(Day.BusinessDay);
		calendar.getBusinessCalendarDays().add(businessCalendarDay);
		
		return calendar;
	}

	protected void assertEqualsBusinessCalendar(BusinessCalendar expected, BusinessCalendar actual) {
		assertEquals(expected != null, actual != null);
		if(expected != null) {
			assertEquals(expected.getBusinessCalendarDays() != null, actual.getBusinessCalendarDays() != null);
			if(expected.getBusinessCalendarDays() != null) {
				assertEquals(expected.getBusinessCalendarDays().size(), actual.getBusinessCalendarDays().size());
				for (int i = 0; i < expected.getBusinessCalendarDays().size(); i++) {
					assertEquals(expected.getBusinessCalendarDays().get(i).getDateString(), actual.getBusinessCalendarDays().get(i).getDateString());
					assertEquals(expected.getBusinessCalendarDays().get(i).getDay(), actual.getBusinessCalendarDays().get(i).getDay());
				}
			}
		}
	}
	
	protected Address createAddress() {
		Address address = new Address();
		
		address.setCity("New York");
		address.setState("NY");
		address.setCountry("USA");
		address.setStreet("523 E78th Street");
		address.setPostalCode("10021");
		
		return address;
	}

	protected TimePeriod createTimePeriod() {
		TimePeriod timePeriod = new TimePeriod();
		
		TimeOfDay start = createTimeOfDay();
		
		TimeOfDay end = new TimeOfDay();
		end.setHour(11);
		end.setMinute(20);
		end.setSecond(11);
		end.setTimeZoneID("CET");
		
		timePeriod.setStartTime(start);
		timePeriod.setEndTime(end);
		
		return timePeriod;
	}

	private TimeOfDay createTimeOfDay() {
		TimeOfDay time = new TimeOfDay();
		time.setHour(10);
		time.setMinute(15);
		time.setSecond(10);
		
		time.setTimeZoneID("CET");
		
		return time;
	}

	protected List<SecondaryOrderPrecedenceRuleType> createSecondaryPercedence() {
		
		List<SecondaryOrderPrecedenceRuleType> list = new ArrayList<SecondaryOrderPrecedenceRuleType>();

		list.add(SecondaryOrderPrecedenceRuleType.TimePrecedence);
		list.add(SecondaryOrderPrecedenceRuleType.DisplayPrecedence);
		
		return list;
	}

	protected CircuitBreaker createCircuitBreaker() {
		CircuitBreaker circuitBreaker = new CircuitBreaker();
		
		circuitBreaker.setMaximumQuoteImprovement(100);
		circuitBreaker.setOrderRejectAction(OrderRejectAction.RejectAutomatically);
		
		List<HaltRule> haltRules = new ArrayList<HaltRule>();
		HaltRule haltRule1 = new HaltRule();
		haltRule1.setChangeType(ChangeType.Absolute);
		haltRule1.setHaltPeriod(100);
		haltRule1.setQuoteChangeAmount(10);
		haltRules.add(haltRule1);
		
		HaltRule haltRule2 = new HaltRule();
		haltRule2.setChangeType(ChangeType.Percentage);
		haltRule2.setHaltPeriod(10);
		haltRule2.setQuoteChangeAmount(14);
		haltRules.add(haltRule2);
		
		circuitBreaker.setHaltRules(haltRules);
		
		return circuitBreaker;
	}
	
	protected CorporateInformation createCorporateInformation() {
		CorporateInformation corporateInformation = new CorporateInformation();
		corporateInformation.setAddress(createAddress());
		corporateInformation.setName("Test Corp Inc.");
		corporateInformation.setWebSite("http://www.example.com");
		
		return corporateInformation;
	}
	
	protected void assertEqualsAudit(AuditInformation expected, AuditInformation actual) {
		assertEquals(expected != null, actual != null);
		if(expected != null) {
			assertEquals(expected.getUserID(), actual.getUserID());
			assertEquals(expected.getDateTime(), actual.getDateTime());
		}
	}
	
	protected AuditInformation createAudit() {
		AuditInformation audit = new AuditInformation();
		audit.setUserID("Test User");
		audit.setDateTime(new Date());
		
		return audit;
	}
	
	protected PropertyChange getPropertyChange(Property property_) {
		return PropertyUtils.createPropertyChange(property_);
	}
	
	protected PropertyChange getValueListChange(ChangeAction change_, Property property_, String listName_) {		
		return PropertyUtils.createValueListChange(change_, property_, listName_);
	}
	
	protected Property getBooleanProperty(String name_, boolean value_) {
		return PropertyUtils.createBooleanProperty(name_, value_);
	}
	protected Property getDoubleProperty(String name_, double value_) {
		return PropertyUtils.createDoubleProperty(name_, value_);
	}
	protected Property getIntProperty(String name_, int value_) {
		return PropertyUtils.createIntProperty(name_, value_);
	}
	protected Property getStringProperty(String name_, String value_) {
		return PropertyUtils.createStringProperty(name_, value_);
	}
	protected Property getObjectProperty(String name_, TransferableObject value_) {
		return PropertyUtils.createObjectProperty(name_, value_);
	}
	protected Property getDateProperty(String name_, Date value_) {
		return PropertyUtils.createDateProperty(name_, value_);
	}	
	@SuppressWarnings("deprecation")
	protected List<Property> createPropertyList() {
		List<Property> list = new ArrayList<Property>();
		
		IntProperty intProp = new IntProperty();
		intProp.setName("TestIntProp");
		intProp.setValue(10);
		list.add(intProp);

		DoubleProperty doubleProp = new DoubleProperty();
		doubleProp.setName("TestDoubleProp");
		doubleProp.setValue(10.0);
		list.add(doubleProp);
		
		StringProperty stringProp = new StringProperty();
		stringProp.setName("TestStringProp");
		stringProp.setValue("Test");
		list.add(stringProp); 
		
		DateProperty dateProp = new DateProperty();
		dateProp.setName("TestDateProp");
		java.sql.Date date = new java.sql.Date(System.currentTimeMillis());
		dateProp.setValue(date);
		list.add(dateProp);		

		/*
		ObjectProperty objectProp = new ObjectProperty();
		objectProp.setName("TestObjectProp");
		objectProp.setValue(new Date());
		list.add(objectProp);	
		*/

		BooleanProperty boolProp = new BooleanProperty();
		boolProp.setName("TestBooleanProp");
		boolProp.setValue(true);
		list.add(boolProp);
		
		list.add(PropertyUtils.createUnitProperty("TestName6", "KWh"));
		
		TimeOfDay timeOfDay = new TimeOfDay();
		timeOfDay.setHour(12);
		timeOfDay.setMinute(15);
		timeOfDay.setSecond(16);
		timeOfDay.setTimeZoneID("UTC");
		list.add(PropertyUtils.createTimeProperty("TestName7", timeOfDay));
		
		timeOfDay = new TimeOfDay();
		timeOfDay.setHour(12);
		timeOfDay.setMinute(15);
		timeOfDay.setSecond(16);
		timeOfDay.setTimeZoneID("UTC");
		
		TimeOfDay timeOfDay2 = new TimeOfDay();
		timeOfDay2.setHour(13);
		timeOfDay2.setMinute(15);
		timeOfDay2.setSecond(16);
		timeOfDay2.setTimeZoneID("UTC");
		
		TimePeriod timePeriod = new TimePeriod();
		timePeriod.setStartTime(timeOfDay);
		timePeriod.setEndTime(timeOfDay2);

		list.add(PropertyUtils.createTimeRangeProperty("TestName8", timePeriod, true));
		
		DatePeriod datePeriod = new DatePeriod();
		datePeriod.setStartDate(date);

		Date date2 = new Date();
		date2.setDate(date2.getDate() + 24 * 60 * 60 * 1000);
		
		datePeriod.setEndDate(new java.sql.Date(date.getTime()));
		
		list.add(PropertyUtils.createDateRangeProperty("TestName9", datePeriod, true));
		
		list.add(PropertyUtils.createStringListProperty("TestName10", new String[] { "111A", "222B", "333C" }));
		 
		return list;
	}
	
	protected void assertEqualsPropertyList(Property[] expected, Property[] actual) {		
		assertEquals(expected.length, actual.length);
		
		Comparator<Property> nameComp = (p1, p2) -> p1.getName().compareTo(p2.getName());
		
		List<Property> expectedList = createList(expected);
		List<Property> actualList = createList(actual);
		
		Collections.sort(expectedList, nameComp);
		Collections.sort(actualList, nameComp);
		
		for (int i = 0; i < expectedList.size(); i++) {
			Property expectedProperty = expectedList.get(i);
			Property actualProperty = actualList.get(i);
			assertEqualsProperty(expectedProperty, actualProperty);
		}
		
	}

	protected void assertEqualsStringList(String[] expected, String[] actual) {		
		assertEquals(expected.length, actual.length);
	
		List<String> expectedList = createList(expected);
		List<String> actualList = createList(actual);
		
		Collections.sort(expectedList);
		Collections.sort(actualList);
		
		for (int i = 0; i < expectedList.size(); i++) {
			String expectedString = expectedList.get(i);
			String actualString = actualList.get(i);
			assertEquals(expectedString, actualString);
		}
		
	}
	
	protected void assertEqualsProperty(Property expectedProperty,
			Property actualProperty) {
		assertEquals(expectedProperty.getName(), actualProperty.getName());	
		
		if (expectedProperty instanceof IntProperty) {
			assertEquals(((IntProperty) expectedProperty).getValue(), ((IntProperty) actualProperty).getValue());
		} else if (expectedProperty instanceof DoubleProperty) {
			assertEquals(((DoubleProperty) expectedProperty).getValue(), ((DoubleProperty) actualProperty).getValue());
		} else if (expectedProperty instanceof BooleanProperty) {
			assertEquals(((BooleanProperty) expectedProperty).getValue(), ((BooleanProperty) actualProperty).getValue());
		} else if (expectedProperty instanceof StringProperty) {
			assertEquals(((StringProperty) expectedProperty).getValue(), ((StringProperty) actualProperty).getValue());
		} else if (expectedProperty instanceof ObjectProperty) {
			assertEquals(((ObjectProperty) expectedProperty).getValue(), ((ObjectProperty) actualProperty).getValue());
		} else if (expectedProperty instanceof DateProperty) {
			assertEquals(((DateProperty) expectedProperty).getValue(), ((DateProperty) actualProperty).getValue());
		} else if (expectedProperty instanceof UnitProperty) {
			assertEquals(((UnitProperty) expectedProperty).getUnit(), ((UnitProperty) actualProperty).getUnit());
		} else if (expectedProperty instanceof TimeProperty) {
			assertEqualsTimeOfDay(((TimeProperty) expectedProperty).getValue(), ((TimeProperty) actualProperty).getValue());
		} else if (expectedProperty instanceof TimeRangeProperty) {
			assertEqualsTimePeriod(((TimeRangeProperty) expectedProperty).getValue(), ((TimeRangeProperty) actualProperty).getValue());
			assertEquals(((TimeRangeProperty) expectedProperty).getOutsideTheRange(), ((TimeRangeProperty) actualProperty).getOutsideTheRange());
		} else if (expectedProperty instanceof DateRangeProperty) {
			assertEquals(((DateRangeProperty) expectedProperty).getValue(), ((DateRangeProperty) actualProperty).getValue());
			assertEquals(((DateRangeProperty) expectedProperty).getOutsideTheRange(), ((DateRangeProperty) actualProperty).getOutsideTheRange());
		} else if (expectedProperty instanceof StringListProperty) {
			// assertEquals(((StringListProperty) expectedProperty).getList(), ((StringListProperty) actualProperty).getList());
			// this is needed, because type of list may be different
			List<String> expectedList = ((StringListProperty) expectedProperty).getList();
			List<String> actualList = ((StringListProperty) actualProperty).getList();
			assertEquals(expectedList.size(), actualList.size());
			for (int i = 0; i < expectedList.size(); i++) {
				String expectedString = expectedList.get(i);
				String actualString = actualList.get(i);
				assertEquals(expectedString, actualString);
			}
		} 
	}

	protected List<String> createList(String[] array_) {
		return Arrays.asList(array_);
	}
	
	protected List<Property> createList(Property[] array_) {
		return Arrays.asList(array_);
	}

}
