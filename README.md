# iMarcats-Market-System
Framework for stock, commodity or any other marketplaces 

## Using Market System Java API 

### 1) Implement the following datastores e.g. using JPA (note market model implementation assumes that JPA will be used)
AssetClassDatastore 
ProductDatastore 
InstrumentDatastore 
MarketOperatorDatastore 
MarketDatastore  
AuditTrailEntryDatastore  
MatchedTradeDatastore

### 2) Implement the MarketTimer e.g. using Quartz 

### 3) Implement contexts 
MarketDataSession 
PropertyChangeSession 
TradeNotificationSession

### 4) Instantiate market administration system 

		MarketManagementAdminSystem adminSystem = 
    		new MarketManagementAdminSystem(assetClassDatastore_, 
			productDatastore_,
			instrumentDatastore_, 
			marketOperatorDatastore_,
			marketDatastore_, 
			auditTrailEntryDatastore_, 
			matchedTradeDatastore_,
			marketTimer_);
    	 
## Market Management using Java 

### Asset Class (needed for Product)

### Simple Asset Class 

		AssetClassDto assetClass = new AssetClassDto();
    	String assetClassName = "MY_ASSET_CLASS_1";
		assetClass.setName(assetClassName);
    	assetClass.setDescription("Test 1");
    	
    	adminSystem.createAssetClass(assetClass, "Adam");

### Hierarchical Asset Class (setting asset class 1 as parent)

		AssetClass assetClass2 = createAssetClass();
    	String assetClassCode2 = "TEST_TEST_TEST";
		assetClass2.setName(assetClassCode2);
    	assetClass2.setParentName(assetClassName);

    	adminSystem.createAssetClass(assetClass, "Adam");

### Change Asset Class 

		assetClass1.setDescription("Test 2222");
		adminSystem.changeAssetClass(assetClass1, "Adam");

### Delete Asset Class (note parent cannot be deleted before the child)

		MarketManagementContextImpl context = new 			MarketManagementContextImpl(
				marketDataSession, propertyChangeSession,
				tradeNotificationSession);

    	adminSystem.deleteAssetClass(assetClass2.getName(), user, context);

## Product (needed for Instrument)

### Create Product 

		String productCode1 = "MYPROD";

		ProductDto product1 = new ProductDto();
		product1.setProductCode(productCode1);
		product1.setName("Test Product");
		product1.setDescription("Test Product");
		product1.setType(ProductType.Financial);
		product1.setProductDefinitionDocument("test");

		adminSystem.createProduct(product, "Adam");

### Approve Product 

		adminSystem.approveProduct(productCode1, new Date(), "Adam");

### Suspend Product (note product can only be suspended, if the dependent objects are suspended)

		adminSystem.suspendProduct(productCode1, user);

### Change Product (note it has to be suspended first)

		product1.setDescription("TestChange");
		adminSystem.changeProduct(product1, "Adam");

### Delete Product (note product can only be deleted, if the dependent objects are deleted)

		adminSystem.deleteProduct(productCode1, "Adam", context);

## Instrument (needed for the market)

### Create Instrument (note instrument has to have an underlying a product or another instrument)

		String instrumentCode1 = "MYTESTINSTR"; 
		
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
	
		instrument1.setUnderlyingType(underlyingType_);
		instrument1.setUnderlyingCode(underlyingCode_);
		if (underlyingType_ == UnderlyingType.Product) {
			instrument1.setType(InstrumentType.Spot);
		} else {
			instrument1.setType(InstrumentType.Derivative);
		}
		instrument1.setMasterAgreementDocument("test");
		
		adminSystem.createInstrument(instrument, "Adam"); 

### Approve Instrument (note instrument can only be approved, if the underlying is approved)

		adminSystem.approveInstrument(instrumentCode1, new Date(), "Adam");

### Suspend Instrument (note instrument can only be suspended, if the dependent objects are suspended)

		adminSystem.suspendInstrument(instrumentCode1, "Adam");

### Change Instrument 

		instrument1.setMasterAgreementDocument("test2");
		
		adminSystem.changeInstrument(instrument1, "Adam");

### Delete Instrument (note product can only be deleted, if the dependent objects are deleted)

		adminSystem.deleteInstrument(instrumentCode, "Adam", context);

## Market Operator (needed for the market)

### Create Market Operator 

    	String mktOptCode1 = "MYTESTKKTOPT"; 
		MarketOperatorDto mktOperator1 = new MarketOperatorDto();
		mktOperator1.setCode(mktOptCode1);
		mktOperator1.setName("Test Market Operator");
		mktOperator1.setDescription("Test Market Operator");
		mktOperator1.setOwnerUserID("TestOwner");

		mktOpt1.setBusinessEntityCode(businessEntityCode);
    	
		String ownerUserID = "ownerUserID"; 
		mktOpt1.setOwnerUserID(ownerUserID);
		
		adminSystem.createMarketOperator(mktOpt, "Adam"); 

### Approve Market Operator 
    	
    	mktOpt1.setMarketOperatorAgreement("Test");
    	adminSystem.approveMarketOperator(mktOptCode1, new Date(), "Adam");
    	
### Suspend Market Operator 

		adminSystem.suspendMarketOperator(mktOptCode, "Adam");

### Change Market Operator 

		String ownerUserID = "ownerUserID1"; 
		mktOpt1.setOwnerUserID(ownerUserID);
		
		adminSystem.changeMarketOperator(mktOptChanged, "Adam");

### Delete Market Operator

		adminSystem.deleteMarketOperator(mktOptCode, "Adam", context);

## Market

### Create Market  

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

		Market market = new Market();
    	market.setMarketCode(instrumentCode + "." + marketOperatorCode);
    	market.setInstrumentCode(instrumentCode);
    	market.setMarketOperatorCode(marketOperatorCode);
    	market.setName("TestName");
    	market.setDescription("TestDescription");    	
    	market.setBusinessCalendar(MarketDtoMapping.INSTANCE.toDto(calendar));
    	market.setMinimumContractsTraded(10);
    	market.setMinimumQuoteIncrement(10);
    	if(continuous) {
	    	market.setTradingSession(TradingSession.Continuous);
    		market.setMarketOperationDays(RecurringActionDetail.Daily);
    	} else {
	    	market.setTradingSession(TradingSession.NonContinuous);
			
    		if(callMarket) {
				market.setExecutionSystem(ExecutionSystem.CallMarketWithSingleSideAuction);
    		} else { 
				TimePeriod timePeriod = new TimePeriod();
		
				TimeOfDay start = new TimeOfDay();
				start.setHour(10);
				start.setMinute(15);
				start.setSecond(10);
		
				start.setTimeZoneID("CET");
		
				TimeOfDay end = new TimeOfDay();
				end.setHour(11);
				end.setMinute(20);
				end.setSecond(11);
				end.setTimeZoneID("CET");
		
				timePeriod.setStartTime(start);
				timePeriod.setEndTime(end);
		
    			market.setTradingHours(timePeriod);
    				market.setMarketOperationDays(RecurringActionDetail.OnWeekdays);
    		}
    	}
    	market.setTradingDayEnd(createTimePeriod().getEndTime());
    	market.setCommission(10);
    	market.setCommissionCurrency("USD");
    	market.setMarketTimeZoneID("TestTZID");
    	market.setClearingBank("Testbank");
    	market.setState(null);
    	market.setAllowHiddenOrders(false);
    	market.setAllowSizeRestrictionOnOrders(false);

		adminSystem.createMarket(market, "Adam", context); 

### Approve Market  (note market can only be approved, if the underlying instrument and market operator are approved)

		market.setMarketOperationContract("test contract");
		
		adminSystem.approveMarket(marketCode, new Date(), "Adam", context);

### Activate Continuous Market

		managementSystem.activateMarket(marketCode, "Adam", context);

### Activate Call Market 

    	GregorianCalendar nowCalendar = new GregorianCalendar();
    	// this is set to a fixed date to avoid problems when we switch to day light saving
    	nowCalendar.set(Calendar.DATE, 11);
    	nowCalendar.set(Calendar.MONTH, 1);
    	nowCalendar.set(Calendar.YEAR, 111);
    	
    	TimeOfDay nowTimeOfDay = TimerUtils.getTimeFromDate(nowCalendar.getTime(), nowCalendar.getTimeZone());
		
		Date scheduledDateTime = TimerUtils.addToDate(nowCalendar.getTime(), DAY_IN_MILLIS);
		    	
		managementSystem.activateCallMarket(marketCode, scheduledDateTime, MarketDtoMapping.INSTANCE.toDto(scheduledTimeOfDay), "Adam", context);    	

### Emergency Close Market

		managementSystem.emergencyCloseMarket(marketCode, "Adam", context);

### Deactivate Market

		adminSystem.suspendMarket(marketCode, "Adam", context);

### Suspend Market 

		adminSystem.suspendMarket(marketCode, "Adam", context);

### Change Market 

		market.setBusinessEntityCode("Test_BE");
    	adminSystem.changeMarket(market, "Adam", context);

### Delete Market 

		adminSystem.deleteMarket(marketCode, user, context);
		
## Order Management using Java 

### Instantiate Order Management System

		OrderManagementSystem orderManagementSystem = new OrderManagementSystem(marketDatastore, 
			orderDatastore,
			tradeDatastore,
			orderSubmitExecutor_, 
			orderCancelExecutor);

### Create Order 

		Order order = new Order();
		
		order.setTargetMarketCode(marketCode);
		double actualLimitQuote = limitQuote;
		if(quoteType == QuoteType.Yield && type != OrderType.Market) {
			// models a Bond Yield
			actualLimitQuote = 100 - limitQuote;
		}
		if(type_ == OrderType.Limit) {
			order.setLimitQuoteValue(Quote.createQuote(actualLimitQuote));
		} 
		order.setSide(side);
		order.setType(type);
		order.setSize(size);


		OrderManagementContextImpl context = 
			new OrderManagementContextImpl(marketDataSession, propertyChangeSession, tradeNotificationSessionImpl);

		orderManagementSystem.createOrder(marketCode, order, "Adam", context);

### Submit Order 

		orderManagementSystem.submitOrder(order.getKey(), "Adam", context);

### Cancel Order 

		orderManagementSystem.cancelOrder(order.getKey(), "Adam", context);

### Delete Order

		orderManagementSystem.deleteOrder(order.getKey(), "Adam", context);