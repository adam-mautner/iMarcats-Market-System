package com.imarcats.internal.server.infrastructure.marketdata;

import java.util.Date;

import com.imarcats.interfaces.client.v100.dto.types.MarketDataType;
import com.imarcats.interfaces.client.v100.notification.MarketDataChange;
import com.imarcats.interfaces.client.v100.notification.ObjectVersion;
import com.imarcats.interfaces.server.v100.dto.mapping.MarketDtoMapping;
import com.imarcats.model.types.Quote;
import com.imarcats.model.types.QuoteAndSize;

/**
 * Implementation of Market Data Session
 * @author Adam
 *
 */
public class MarketDataSessionImpl implements MarketDataSession {

	private final MarketDataSource _marketDataSource;

	public MarketDataSessionImpl(MarketDataSource marketDataSource_) {
		super();
		_marketDataSource = marketDataSource_;
	}
	
	private Date getChangeDate(QuoteAndSize quoteAndSize_) {
		return getChangeDate(quoteAndSize_.getQuote());
	}
	
	private Date getChangeDate(Quote quote_) {
		return quote_ != null && quote_.getDateOfQuote() != null 
						? quote_.getDateOfQuote() 
						: new Date();
	}

	@Override
	public void recordClosingQuote(String marketCode_, Quote closingQuote_, ObjectVersion version_) {
		notify(new MarketDataChange(marketCode_, MarketDataType.Close, MarketDtoMapping.INSTANCE.toDto(closingQuote_), getChangeDate(closingQuote_), version_));
	}

	@Override
	public void recordNewBestAsk(String marketCode_, QuoteAndSize newBestAsk_, Boolean hasHiddenOrders_, ObjectVersion version_) {
		notify(new MarketDataChange(marketCode_, MarketDataType.Ask, MarketDtoMapping.INSTANCE.toDto(newBestAsk_), hasHiddenOrders_, getChangeDate(newBestAsk_), version_));
		
	}

	@Override
	public void recordNewLevelIIBid(String marketCode_, QuoteAndSize newLevelIIBid_, Boolean hasHiddenOrders_, ObjectVersion version_) {
		notify(new MarketDataChange(marketCode_, MarketDataType.LevelIIBid, MarketDtoMapping.INSTANCE.toDto(newLevelIIBid_), hasHiddenOrders_, getChangeDate(newLevelIIBid_), version_));
	}	
	
	@Override
	public void recordNewBestBid(String marketCode_, QuoteAndSize newBestBid_, Boolean hasHiddenOrders_, ObjectVersion version_) {
		notify(new MarketDataChange(marketCode_, MarketDataType.Bid, MarketDtoMapping.INSTANCE.toDto(newBestBid_), hasHiddenOrders_, getChangeDate(newBestBid_), version_));
		
	}

	@Override
	public void recordNewLevelIIAsk(String marketCode_, QuoteAndSize newLevelIIAsk_, Boolean hasHiddenOrders_, ObjectVersion version_) {
		notify(new MarketDataChange(marketCode_, MarketDataType.LevelIIAsk, MarketDtoMapping.INSTANCE.toDto(newLevelIIAsk_), hasHiddenOrders_, getChangeDate(newLevelIIAsk_), version_));
	}
	
	@Override
	public void recordNewLastTrade(String marketCode_, QuoteAndSize newLastTrade_, ObjectVersion version_) {
		notify(new MarketDataChange(marketCode_, MarketDataType.Last, MarketDtoMapping.INSTANCE.toDto(newLastTrade_), null, getChangeDate(newLastTrade_), version_));
		
	}

	@Override
	public void recordOpeningQuote(String marketCode_, Quote openingQuote_, ObjectVersion version_) {
		notify(new MarketDataChange(marketCode_, MarketDataType.Open, MarketDtoMapping.INSTANCE.toDto(openingQuote_), getChangeDate(openingQuote_), version_));
	}

	protected void notify(MarketDataChange change) {
		_marketDataSource.notifyListeners(new MarketDataChange[] { change });
	}
	
	@Override
	public Long addMarketDataChangeListener(String marketCode_,
			MarketDataType marketDataType_,
			PersistedMarketDataChangeListener listener_) {
		return _marketDataSource.addMarketDataChangeListener(marketCode_, marketDataType_, listener_);
	}
	
	@Override
	public void removeMarketDataChangeListener(Long listenerID_) {
		_marketDataSource.removeMarketDataChangeListener(listenerID_);	
	}

	@Override
	public MarketDataSource getMarketDataSource() {
		return _marketDataSource;
	}

}
