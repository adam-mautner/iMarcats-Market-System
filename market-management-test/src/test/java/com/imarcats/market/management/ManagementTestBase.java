package com.imarcats.market.management;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.imarcats.interfaces.client.v100.dto.AssetClassDto;
import com.imarcats.interfaces.client.v100.dto.InstrumentDto;
import com.imarcats.interfaces.client.v100.dto.MarketDto;
import com.imarcats.interfaces.client.v100.dto.MarketOperatorDto;
import com.imarcats.interfaces.client.v100.dto.MatchedTradeDto;
import com.imarcats.interfaces.client.v100.dto.ProductDto;
import com.imarcats.interfaces.client.v100.dto.types.AuditTrailEntryDto;
import com.imarcats.interfaces.client.v100.dto.types.BusinessCalendarDto;
import com.imarcats.interfaces.client.v100.dto.types.MarketDataType;
import com.imarcats.interfaces.client.v100.dto.types.PagedAuditTrailEntryListDto;
import com.imarcats.interfaces.client.v100.dto.types.ProductType;
import com.imarcats.interfaces.client.v100.dto.types.TradeSideDto;
import com.imarcats.interfaces.client.v100.notification.ListenerCallUserParameters;
import com.imarcats.interfaces.client.v100.notification.MarketDataChange;
import com.imarcats.interfaces.client.v100.notification.NotificationType;
import com.imarcats.interfaces.client.v100.notification.PropertyChanges;
import com.imarcats.interfaces.server.v100.dto.mapping.AssetClassDtoMapping;
import com.imarcats.interfaces.server.v100.dto.mapping.AuditEntryDtoMapping;
import com.imarcats.interfaces.server.v100.dto.mapping.InstrumentDtoMapping;
import com.imarcats.interfaces.server.v100.dto.mapping.MarketDtoMapping;
import com.imarcats.interfaces.server.v100.dto.mapping.MarketOperatorDtoMapping;
import com.imarcats.interfaces.server.v100.dto.mapping.MatchedTradeDtoMapping;
import com.imarcats.interfaces.server.v100.dto.mapping.ProductDtoMapping;
import com.imarcats.internal.server.infrastructure.marketdata.MarketDataSessionImpl;
import com.imarcats.internal.server.infrastructure.marketdata.MarketDataSource;
import com.imarcats.internal.server.infrastructure.marketdata.PersistedMarketDataChangeListener;
import com.imarcats.internal.server.infrastructure.notification.NotificationBroker;
import com.imarcats.internal.server.infrastructure.notification.PersistedListener;
import com.imarcats.internal.server.infrastructure.notification.properties.PersistedPropertyChangeListener;
import com.imarcats.internal.server.infrastructure.notification.properties.PropertyChangeBroker;
import com.imarcats.internal.server.infrastructure.notification.properties.PropertyChangeSessionImpl;
import com.imarcats.internal.server.infrastructure.notification.trades.TradeNotificationBroker;
import com.imarcats.internal.server.infrastructure.notification.trades.TradeNotificationSession;
import com.imarcats.internal.server.infrastructure.notification.trades.TradeNotificationSessionImpl;
import com.imarcats.internal.server.infrastructure.testutils.MockDatastoresBase;
import com.imarcats.internal.server.infrastructure.timer.MarketTimer;
import com.imarcats.internal.server.infrastructure.timer.TimerAction;
import com.imarcats.internal.server.interfaces.util.UniqueIDGenerator;
import com.imarcats.market.management.admin.MarketManagementAdminSystem;
import com.imarcats.model.AssetClass;
import com.imarcats.model.AuditTrailEntry;
import com.imarcats.model.Instrument;
import com.imarcats.model.Market;
import com.imarcats.model.MarketOperator;
import com.imarcats.model.MatchedTrade;
import com.imarcats.model.Product;
import com.imarcats.model.TradeSide;
import com.imarcats.model.test.testutils.MarketObjectTestBase;
import com.imarcats.model.types.ActivationStatus;
import com.imarcats.model.types.AuditEntryAction;
import com.imarcats.model.types.BusinessCalendar;
import com.imarcats.model.types.DatastoreKey;
import com.imarcats.model.types.DeliveryPeriod;
import com.imarcats.model.types.InstrumentType;
import com.imarcats.model.types.Position;
import com.imarcats.model.types.QuoteType;
import com.imarcats.model.types.RecurringActionDetail;
import com.imarcats.model.types.SettlementPrice;
import com.imarcats.model.types.SettlementType;
import com.imarcats.model.types.TimeOfDay;
import com.imarcats.model.types.UnderlyingType;

public abstract class ManagementTestBase extends MarketObjectTestBase {

	protected AssetClassDto createAssetClassDto() throws Exception {
		return assetClassToDto(super.createAssetClass());
	}

	protected AssetClass assetClassFromDto(AssetClassDto assetClass_) {
		return AssetClassDtoMapping.INSTANCE.fromDto(assetClass_);
	}

	protected AssetClassDto assetClassToDto(AssetClass assetClass_) {
		return AssetClassDtoMapping.INSTANCE.toDto(assetClass_);
	}

	protected void assertEqualsAssetClassDto(AssetClassDto expected,
			AssetClassDto actual) {
		super.assertEqualsAssetClass(assetClassFromDto(expected),
				assetClassFromDto(actual));
	}

	protected ProductDto createProductDto(String code_) throws Exception {
		return productToDto(super.createProduct(code_));
	}

	protected Product productFromDto(ProductDto product_) {
		return ProductDtoMapping.INSTANCE.fromDto(product_);
	}

	protected ProductDto productToDto(Product product_) {
		return ProductDtoMapping.INSTANCE.toDto(product_);
	}

	protected void assertEqualsProductDto(ProductDto expected, ProductDto actual) {
		super.assertEqualsProduct(productFromDto(expected),
				productFromDto(actual));
	}

	protected InstrumentDto createInstrumentDto(String code_) throws Exception {
		return instrumentToDto(super.createInstrument(code_));
	}

	protected Instrument instrumentFromDto(InstrumentDto instrument_) {
		return InstrumentDtoMapping.INSTANCE.fromDto(instrument_);
	}

	protected InstrumentDto instrumentToDto(Instrument instrument_) {
		return InstrumentDtoMapping.INSTANCE.toDto(instrument_);
	}

	protected void assertEqualsInstrumentDto(InstrumentDto expected,
			InstrumentDto actual) {
		super.assertEqualsInstrument(instrumentFromDto(expected),
				instrumentFromDto(actual));
	}

	protected MarketOperatorDto createMarketOperatorDto(String code_)
			throws Exception {
		return marketOperatorToDto(super.createMarketOperator(code_));
	}

	protected MarketOperator marketOperatorFromDto(
			MarketOperatorDto marketOperator_) {
		return MarketOperatorDtoMapping.INSTANCE.fromDto(marketOperator_);
	}

	protected MarketOperatorDto marketOperatorToDto(
			MarketOperator marketOperator_) {
		return MarketOperatorDtoMapping.INSTANCE.toDto(marketOperator_);
	}

	protected void assertEqualsMarketOperatorDto(MarketOperatorDto expected,
			MarketOperatorDto actual) {
		super.assertEqualsMarketOperator(marketOperatorFromDto(expected),
				marketOperatorFromDto(actual));
	}

	protected TradeSide matchedTradeFromDto(TradeSideDto trade_) {
		return MatchedTradeDtoMapping.INSTANCE.fromDto(trade_);
	}

	protected TradeSideDto matchedTradeToDto(TradeSide trade_) {
		return MatchedTradeDtoMapping.INSTANCE.toDto(trade_);
	}

	protected MatchedTrade matchedTradeFromDto(MatchedTradeDto trade_) {
		return MatchedTradeDtoMapping.INSTANCE.fromDto(trade_);
	}

	protected MatchedTradeDto matchedTradeToDto(MatchedTrade trade_) {
		return MatchedTradeDtoMapping.INSTANCE.toDto(trade_);
	}

	protected AuditTrailEntry auditEntryFromDto(AuditTrailEntryDto entry_) {
		return AuditEntryDtoMapping.INSTANCE.fromDto(entry_);
	}

	protected AuditTrailEntryDto auditEntryToDto(AuditTrailEntry entry_) {
		return AuditEntryDtoMapping.INSTANCE.toDto(entry_);
	}

	protected Market marketFromDto(MarketDto market_) {
		return MarketDtoMapping.INSTANCE.fromDto(market_);
	}

	protected MarketDto marketToDto(Market market_) {
		return MarketDtoMapping.INSTANCE.toDto(market_);
	}

	protected MarketDto createMarketDto(String code_) throws Exception {
		return marketToDto(createMarket(code_));
	}

	protected void assertEqualsMarketDto(MarketDto expected, MarketDto actual) {
		super.assertEqualsMarket(marketFromDto(expected), marketFromDto(actual));
	}

	protected BusinessCalendar businessCalendarFromDto(
			BusinessCalendarDto calendar_) {
		return MarketDtoMapping.INSTANCE.fromDto(calendar_);
	}

	protected BusinessCalendarDto businessCalendarToDto(
			BusinessCalendar calendar_) {
		return MarketDtoMapping.INSTANCE.toDto(calendar_);
	}

	protected BusinessCalendarDto createBusinessCalendarDto() throws Exception {
		return businessCalendarToDto(createBusinessCalendar());
	}

	protected void assertEqualsBusinessCalendarDto(
			BusinessCalendarDto expected, BusinessCalendarDto actual) {
		super.assertEqualsBusinessCalendar(businessCalendarFromDto(expected),
				businessCalendarFromDto(actual));
	}

	public void testAuditQuery() throws Exception {
		MockDatastoresBase datastores = createDatastores();

		MarketManagementAdminSystem adminSystem = new MarketManagementAdminSystem(
				datastores, datastores, datastores, datastores, datastores,
				datastores, datastores, null);

		adminSystem.getAllAuditTrailEntriesFromCursor(null, 10, "test");
	}
	
	protected void openTransaction() {
		// does nothing
	}

	protected void commitTransaction() {
		// does nothing
	}

	protected MarketOperatorDto createMarketOperator1(String mktOptCode1)
			throws Exception {
		MarketOperatorDto mktOperator1 = new MarketOperatorDto();
		mktOperator1.setCode(mktOptCode1);
		mktOperator1.setName("Test Market Operator");
		mktOperator1.setDescription("Test Market Operator");
		mktOperator1.setOwnerUserID("TestOwner");
//		mktOperator1.setLastUpdateTimestamp(new Date()); 
//		mktOperator1.setActivationStatus(com.imarcats.interfaces.client.v100.dto.types.ActivationStatus.Created);
		return mktOperator1;
	}

	protected ProductDto createProduct1(String productCode1) {
		ProductDto product1 = new ProductDto();
		product1.setProductCode(productCode1);
		product1.setName("Test Product");
		product1.setDescription("Test Product");
		product1.setType(ProductType.Financial);
//		product1.setActivationStatus(com.imarcats.interfaces.client.v100.dto.types.ActivationStatus.Approved);
//		product1.setLastUpdateTimestamp(new Date()); 
		
		return product1;
	}

	protected InstrumentDto createInstrument1Dto(String instrumentCode1,
			UnderlyingType underlyingType_, String underlyingCode_) {
		return InstrumentDtoMapping.INSTANCE.toDto(createInstrument1(
				instrumentCode1, underlyingType_, underlyingCode_));
	}

	protected Instrument createInstrument1(String instrumentCode1,
			UnderlyingType underlyingType_, String underlyingCode_) {
		Instrument instrument1 = new Instrument();
		instrument1.setInstrumentCode(instrumentCode1);
		instrument1.setName("Test Instrument");
		instrument1.setDescription("Test Instrument");
		instrument1.setDenominationCurrency("USD");
		instrument1.setContractSize(100);
		instrument1.setContractSizeUnit("MWh");
		instrument1.setDeliveryLocation(createAddress());
		instrument1.setDeliveryPeriod(DeliveryPeriod.T0);
		instrument1.setQuoteType(QuoteType.Price);
		instrument1.setRecordPurchaseAsPosition(Position.Long);
		instrument1.setSettlementPrice(SettlementPrice.Clean);
		instrument1.setSettlementType(SettlementType.PhysicalDelivery);
		instrument1.setRollable(true);
		// instrument1.setActivationStatus(ActivationStatus.Created);

		instrument1.setUnderlyingType(underlyingType_);
		instrument1.setUnderlyingCode(underlyingCode_);
		if (underlyingType_ == UnderlyingType.Product) {
			instrument1.setType(InstrumentType.Spot);
		} else {
			instrument1.setType(InstrumentType.Derivative);
		}

//		instrument1.updateLastUpdateTimestamp();
		
		return instrument1;
	}

	public static MarketManagementContextImpl createMarketManagementContext() {
		
		

		MockMarketDataSource marketDataSource = new MockMarketDataSource();
		MockPropertyChangeBroker propertyChangeBroker = new MockPropertyChangeBroker();

		MarketDataSessionImpl marketDataSession = new MarketDataSessionImpl(
				marketDataSource);
		PropertyChangeSessionImpl propertyChangeSession = new PropertyChangeSessionImpl(
				propertyChangeBroker);

		TradeNotificationBroker tradeNotificationBroker = new MockTradeNotificationBroker();
		TradeNotificationSession tradeNotificationSession = new TradeNotificationSessionImpl(
				tradeNotificationBroker);

		MarketManagementContextImpl context = new MarketManagementContextImpl(
				marketDataSession, propertyChangeSession,
				tradeNotificationSession);
		return context;
	}

	protected MockDatastoresBase createDatastores() {
		return new MockDatastoresBase();
	}

	protected static class MockMarketTimer implements MarketTimer {

		private final Map<Long, MockEvent> _mapIdToEvent = new HashMap<Long, MockEvent>();

		public TimerAction getAction(Long action_) {
			return _mapIdToEvent.get(action_).getAction();
		}

		public MockEvent getEvent(Long action_) {
			return _mapIdToEvent.get(action_);
		}

		@Override
		public void cancel(Long action_) {
			_mapIdToEvent.remove(action_);
		}

		@Override
		public Long rescheduleAbsolute(Long scheduledTimerID_,
				Date scheduledTime_, boolean moveToNextDayIfInPast_,
				Long actionKey_) {
			MockEvent mockEvent = _mapIdToEvent.get(actionKey_);
			mockEvent.setMoveToNextDayIfInPast(moveToNextDayIfInPast_);
			mockEvent.setScheduledTime(scheduledTime_);
			mockEvent.setScheduledTimerID(scheduledTimerID_);
			return actionKey_;
		}

		@Override
		public Long rescheduleRelative(Long scheduledTimerID_,
				long relativeTimePeriodMilliseconds_, Long actionKey_) {

			MockEvent mockEvent = _mapIdToEvent.get(actionKey_);
			mockEvent
					.setRelativeTimePeriodMilliseconds(relativeTimePeriodMilliseconds_);
			mockEvent.setScheduledTimerID(scheduledTimerID_);
			return actionKey_;
		}

		@Override
		public Long rescheduleToTime(Long scheduledTimerID_, Date date_,
				TimeOfDay time_, boolean moveToNextDayIfInPast_,
				Long actionKey_) {
			MockEvent mockEvent = _mapIdToEvent.get(actionKey_);
			mockEvent.setScheduledTime(date_);
			mockEvent.setTime(time_);
			mockEvent.setScheduledTimerID(scheduledTimerID_);

			return actionKey_;
		}

		@Override
		public Long scheduleAbsolute(Date scheduledTime_,
				boolean moveToNextDayIfInPast_, TimerAction action_) {
			Long key = getKey();

			return scheduleAbsolute(scheduledTime_, moveToNextDayIfInPast_,
					action_, key);
		}

		protected Long scheduleAbsolute(Date scheduledTime_,
				boolean moveToNextDayIfInPast_, TimerAction action_, Long key) {
			MockEvent mockEvent = new MockEvent();
			mockEvent.setAction(action_);
			mockEvent.setMoveToNextDayIfInPast(moveToNextDayIfInPast_);
			mockEvent.setScheduledTime(scheduledTime_);
			mockEvent.setScheduledTimerID(UniqueIDGenerator.nextID());

			_mapIdToEvent.put(key, mockEvent);

			return key;
		}

		@Override
		public Long scheduleRelative(long relativeTimePeriodMilliseconds_,
				TimerAction action_) {
			Long key = getKey();

			return scheduleRelative(relativeTimePeriodMilliseconds_, action_,
					key);
		}

		protected Long scheduleRelative(long relativeTimePeriodMilliseconds_,
				TimerAction action_, Long key) {
			MockEvent mockEvent = new MockEvent();
			mockEvent.setAction(action_);
			mockEvent
					.setRelativeTimePeriodMilliseconds(relativeTimePeriodMilliseconds_);
			mockEvent.setScheduledTimerID(UniqueIDGenerator.nextID());

			_mapIdToEvent.put(key, mockEvent);

			return key;
		}

		@Override
		public Long scheduleToTime(Date date_, TimeOfDay time_,
				RecurringActionDetail recurringActionDetail_,
				BusinessCalendar calendar_, boolean moveToNextDayIfInPast_,
				TimerAction action_) {
			Long key = getKey();

			return scheduleToTime(date_, time_, recurringActionDetail_,
					calendar_, moveToNextDayIfInPast_, action_, key);
		}

		protected Long scheduleToTime(Date date_, TimeOfDay time_,
				RecurringActionDetail recurringActionDetail_,
				BusinessCalendar calendar_, boolean moveToNextDayIfInPast_,
				TimerAction action_, Long key) {
			MockEvent mockEvent = new MockEvent();
			mockEvent.setAction(action_);
			mockEvent.setTime(time_);
			mockEvent.setScheduledTime(date_);
			mockEvent.setMoveToNextDayIfInPast(moveToNextDayIfInPast_);
			mockEvent.setScheduledTimerID(UniqueIDGenerator.nextID());

			// this is needed to make sure calendar days are loaded for
			// datastore version of the test
			if (calendar_ != null) {
				calendar_.getBusinessCalendarDays();
			}

			mockEvent.setCalendar(calendar_);
			mockEvent.setRecurringActionDetail(recurringActionDetail_);

			_mapIdToEvent.put(key, mockEvent);

			return key;
		}

		private long getKey() {
			return UniqueIDGenerator.nextID();
		}

	}

	private static class MockPropertyChangeBroker implements
			PropertyChangeBroker {

		@SuppressWarnings("unchecked")
		@Override
		public Long addPropertyChangeListener(DatastoreKey observedObjectKey_,
				Class observedObjectClass_,
				PersistedPropertyChangeListener listener_) {

			return null;
		}

		@Override
		public NotificationBroker getNotificationBroker() {
			return new MockNotificationBroker();
		}

		@Override
		public void notifyListeners(PropertyChanges[] propertyChanges_) {

		}

		@Override
		public void removePropertyChangeListener(Long listenerID_) {

		}

	}

	protected void checkAuditTrailForDeleteObject(String user, String objectID,
			String objectType, MarketManagementAdminSystem adminSystem) {
		PagedAuditTrailEntryListDto trailEntries = adminSystem
				.getAllAuditTrailEntriesFromCursor(null, 2);
		assertTrue(trailEntries.getAuditTrailEntries()[0] != null);

		// these events are too close to each other datastore might get the
		// order wrong
		String permissionRevokeID = "All for " + objectType + ":" + objectID;
		if (trailEntries.getAuditTrailEntries()[0].getAuditEntryAction() == com.imarcats.interfaces.client.v100.dto.types.AuditEntryAction.Deleted) {

			checkAuditTrail(AuditEntryAction.Deleted, user, objectID,
					objectType,
					auditEntryFromDto(trailEntries.getAuditTrailEntries()[0]));
			
		} else {
			fail();
		}
	}

	protected void checkAuditTrail(AuditEntryAction action, String user,
			String objectID, String objectType,
			MarketManagementAdminSystem adminSystem) {
		PagedAuditTrailEntryListDto trailEntries = adminSystem
				.getAllAuditTrailEntriesFromCursor(null, 1);
		assertTrue(trailEntries.getAuditTrailEntries()[0] != null);
		checkAuditTrail(action, user, objectID, objectType,
				auditEntryFromDto(trailEntries.getAuditTrailEntries()[0]));
	}

	protected void checkAuditTrail(AuditEntryAction action, String user,
			String releatedInformation, String objectType, AuditTrailEntry trailEntry) {
		assertEquals(action, trailEntry.getAuditEntryAction());
		assertEquals(user, trailEntry.getUserID());
		assertEquals(releatedInformation, trailEntry.getRelatedInformation());
		assertEquals(objectType, trailEntry.getObjectTypeStr());
	}

	protected void createMarketInDatastore(Market market,
			MockDatastoresBase datastores) {
		openTransaction();
		datastores.createMarket(market);
		commitTransaction();
	}

	protected void createMarketOperatorInDatastore(
			MarketOperatorDto marketOperator, MockDatastoresBase datastores) {
		openTransaction();
		datastores.createMarketOperator(marketOperatorFromDto(marketOperator));
		commitTransaction();
	}

	protected void setMarketOperatorToSuspended(String marketOperatorCode,
			MockDatastoresBase datastores) {
		openTransaction();
		datastores.findMarketOperatorByCode(marketOperatorCode)
				.setActivationStatus(ActivationStatus.Suspended);
		commitTransaction();
	}


	protected void deleteMarketInDatastore(String marketCode,
			MockDatastoresBase datastores) {
		openTransaction();
		datastores.deleteMarket(marketCode);
		commitTransaction();
	}

	protected void deleteMarketOperatorInDatastore(String marketOperatorCode,
			MockDatastoresBase datastores) {
		openTransaction();
		datastores.deleteMarketOperator(marketOperatorCode);
		commitTransaction();
	}

	protected void createProductInDatastore(ProductDto product,
			MockDatastoresBase datastores) {
		openTransaction();
		datastores.createProduct(productFromDto(product));
		commitTransaction();
	}

	protected void setInstrumentToApproved(String instrumentCode,
			MockDatastoresBase datastores) {
		openTransaction();
		datastores.findInstrumentByCode(instrumentCode)
				.setActivationStatus(ActivationStatus.Approved);
		commitTransaction();
	}

	protected void createInstrumentInDatastore(Instrument instrument,
			MockDatastoresBase datastores) {
		openTransaction();
		datastores.createInstrument(instrument);
		commitTransaction();
	}

	protected void setInstrumentToSuspended(String instrumentCode,
			MockDatastoresBase datastores) {
		openTransaction();
		datastores.findInstrumentByCode(instrumentCode)
				.setActivationStatus(ActivationStatus.Suspended);
		commitTransaction();
	}

	protected void setInstrumentToCreated(String instrumentCode,
			MockDatastoresBase datastores) {
		openTransaction();
		datastores.findInstrumentByCode(instrumentCode)
				.setActivationStatus(ActivationStatus.Created);
		commitTransaction();
	}

	protected void createAssetClassInDatastore(AssetClass assetClass,
			MockDatastoresBase datastores) {
		openTransaction();
		datastores.createAssetClass(assetClass);
		commitTransaction();
	}

	protected void setProductToApproved(String productCode,
			MockDatastoresBase datastores) {
		openTransaction();
		datastores.findProductByCode(productCode).setActivationStatus(
				ActivationStatus.Approved);
		commitTransaction();
	}

	protected void setMarketToSuspended(String marketCode,
			MockDatastoresBase datastores) {
		openTransaction();
		datastores.findMarketBy(marketCode).getMarketModel()
				.setActivationStatus(ActivationStatus.Suspended);
		commitTransaction();
	}

	protected void setMarketToApproved(String marketCode,
			MockDatastoresBase datastores) {
		openTransaction();
		datastores.findMarketBy(marketCode).getMarketModel()
				.setActivationStatus(ActivationStatus.Approved);
		commitTransaction();
	}

	protected void testApproveInstrument(String instrumentCode,
			String user, MarketManagementAdminSystem adminSystem) {
		openTransaction();
		adminSystem.approveInstrument(instrumentCode, new Date(), user);
		commitTransaction();
	}

	protected void deleteInstrumentInDatastore(String instrumentCode,
			MockDatastoresBase datastores) {
		openTransaction();
		datastores.deleteInstrument(instrumentCode);
		commitTransaction();
	}

	protected void setMarketToCreated(String marketCode,
			MockDatastoresBase datastores) {
		openTransaction();
		Market marketModel = datastores.findMarketBy(marketCode)
				.getMarketModel();
		marketModel.setActivationStatus(ActivationStatus.Created);
		commitTransaction();
	}

	protected void setMarketToActivated(String marketCode,
			MockDatastoresBase datastores) {
		openTransaction();
		datastores.findMarketBy(marketCode).getMarketModel()
				.setActivationStatus(ActivationStatus.Activated);
		commitTransaction();
	}

	protected void setMarketToDeactivated(String marketCode,
			MockDatastoresBase datastores) {
		openTransaction();
		datastores.findMarketBy(marketCode).getMarketModel()
				.setActivationStatus(ActivationStatus.Deactivated);
		commitTransaction();
	}

	protected void setMarketOperatorToApproved(String marketOperatorCode,
			MockDatastoresBase datastores) {
		openTransaction();
		datastores.findMarketOperatorByCode(marketOperatorCode)
				.setActivationStatus(ActivationStatus.Approved);
		commitTransaction();
	}

	protected void setMarketOperatorToCreated(String mktOptCode1,
			MockDatastoresBase datastores) {
		openTransaction();
		datastores.findMarketOperatorByCode(mktOptCode1)
				.setActivationStatus(ActivationStatus.Created);
		commitTransaction();
	}

	protected void setProductToSuspended(String productCode,
			MockDatastoresBase datastores) {
		openTransaction();
		datastores.findProductByCode(productCode).setActivationStatus(
				com.imarcats.model.types.ActivationStatus.Suspended);
		commitTransaction();
	}

	protected void setProductToCreated(String productCode,
			MockDatastoresBase datastores) {
		openTransaction();
		datastores.findProductByCode(productCode).setActivationStatus(
				com.imarcats.model.types.ActivationStatus.Created);
		commitTransaction();
	}

	private static class MockNotificationBroker implements NotificationBroker {

		@SuppressWarnings("unchecked")
		@Override
		public Long addListener(DatastoreKey observedObject_,
				Class observedObjectClass_, NotificationType notificationType_,
				String filterString_, PersistedListener listener_) {

			return null;
		}

		@SuppressWarnings("unchecked")
		@Override
		public void notifyListeners(DatastoreKey observedObject_,
				Class observedObjectClass_, NotificationType notificationType_,
				String filterString_, ListenerCallUserParameters parameters_) {

		}

		@SuppressWarnings("unchecked")
		@Override
		public void removeAllListeners(DatastoreKey observedObjectKey_,
				Class observedObjectClass_) {

		}

		@Override
		public void removeListener(Long listenerKey_) {

		}

	}

	protected static class MockEvent {
		private Date _scheduledTime;
		private boolean _moveToNextDayIfInPast;
		private long _relativeTimePeriodMilliseconds;
		private TimerAction _action;
		private RecurringActionDetail _recurringActionDetail;
		private BusinessCalendar _calendar;
		private TimeOfDay _time;
		private Long _scheduledTimerID;

		public TimeOfDay getTime() {
			return _time;
		}

		public void setTime(TimeOfDay time_) {
			_time = time_;
		}

		public Date getScheduledTime() {
			return _scheduledTime;
		}

		public void setScheduledTime(Date scheduledTime_) {
			_scheduledTime = scheduledTime_;
		}

		public boolean isMoveToNextDayIfInPast() {
			return _moveToNextDayIfInPast;
		}

		public void setMoveToNextDayIfInPast(boolean moveToNextDayIfInPast_) {
			_moveToNextDayIfInPast = moveToNextDayIfInPast_;
		}

		public long getRelativeTimePeriodMilliseconds() {
			return _relativeTimePeriodMilliseconds;
		}

		public void setRelativeTimePeriodMilliseconds(
				long relativeTimePeriodMilliseconds_) {
			_relativeTimePeriodMilliseconds = relativeTimePeriodMilliseconds_;
		}

		public TimerAction getAction() {
			return _action;
		}

		public void setAction(TimerAction action_) {
			_action = action_;
		}

		public RecurringActionDetail getRecurringActionDetail() {
			return _recurringActionDetail;
		}

		public void setRecurringActionDetail(
				RecurringActionDetail recurringActionDetail_) {
			_recurringActionDetail = recurringActionDetail_;
		}

		public BusinessCalendar getCalendar() {
			return _calendar;
		}

		public void setCalendar(BusinessCalendar calendar_) {
			_calendar = calendar_;
		}

		public void setScheduledTimerID(Long scheduledTimerID) {
			_scheduledTimerID = scheduledTimerID;
		}

		public Long getScheduledTimerID() {
			return _scheduledTimerID;
		}
	}

	private static class MockTradeNotificationBroker implements
			TradeNotificationBroker {

		@Override
		public NotificationBroker getNotificationBroker() {
			return null;
		}

		@Override
		public void notifyListeners(MatchedTradeDto[] matchedTrades_) {

		}

	}

	private static class MockMarketDataSource implements MarketDataSource {

		@Override
		public Long addMarketDataChangeListener(String marketCode_,
				MarketDataType marketDataType_,
				PersistedMarketDataChangeListener listener_) {

			return null;
		}

		@Override
		public NotificationBroker getNotificationBroker() {

			return null;
		}

		@Override
		public void notifyListeners(MarketDataChange[] marketDataChanges_) {

		}

		@Override
		public void removeMarketDataChangeListener(Long listenerID_) {

		}

	}
}
