package com.imarcats.infrastructure.server.trigger;

import java.util.Date;

import com.imarcats.internal.server.infrastructure.marketdata.MarketDataSource;
import com.imarcats.internal.server.infrastructure.marketdata.MarketDataSessionImpl;
import com.imarcats.internal.server.infrastructure.notification.properties.PropertyChangeSessionImpl;
import com.imarcats.internal.server.infrastructure.notification.trades.TradeNotificationSessionImpl;
import com.imarcats.internal.server.infrastructure.testutils.MockDatastoresBase;
import com.imarcats.internal.server.infrastructure.timer.MarketTimer;
import com.imarcats.internal.server.infrastructure.timer.TimerAction;
import com.imarcats.model.test.testutils.MockIdentityGenerator;
import com.imarcats.model.types.BusinessCalendar;
import com.imarcats.model.types.RecurringActionDetail;
import com.imarcats.model.types.TimeOfDay;

public class MockTimeTrigger implements MarketTimer {

	private Long _actionKey;
	private TimerAction _action;

	private final MockDatastoresBase _datastores;

	public MockTimeTrigger(MockDatastoresBase datastores_) {
		super();
		_datastores = datastores_;
	}

	public Long getActionKey() {
		return _actionKey;
	}
	
	public TimerAction getAction() {
		return _action;
	}
	
	@Override
	public void cancel(Long action_) {
		_actionKey = null;
		_action = null;
	}

	@Override
	public Long scheduleAbsolute(Date scheduledTime_, boolean moveToNextDayIfInPast_, 
			TimerAction action_) {
		return addAction(action_);
	}

	public Long addAction(TimerAction action_) {
		_actionKey = MockIdentityGenerator.getId();
		_action = action_;	
		return _actionKey;
	}

	@Override
	public Long scheduleRelative(long relativeTimePeriodMilliseconds_,
			TimerAction action_) {
		return addAction(action_);
	}

	@Override
	public Long scheduleToTime(Date date_, TimeOfDay time_, 
			RecurringActionDetail recurringActionDetail_, 
			BusinessCalendar calendar_, boolean moveToNextDayIfInPast_, TimerAction action_) {
		return addAction(action_);
	}
	
	
	public void fireTrigger() {
		MarketDataSource marketDataSourceImpl = new MockMarketDataSource(_datastores);
		MockPropertyChangeBroker propertyChangeBroker = new MockPropertyChangeBroker(_datastores);
	
		MarketDataSessionImpl marketDataSessionImpl = new MarketDataSessionImpl(
				marketDataSourceImpl);
		PropertyChangeSessionImpl propertyChangeSession = 
			new PropertyChangeSessionImpl(propertyChangeBroker);
		TradeNotificationSessionImpl tradeNotificationSessionImpl = new TradeNotificationSessionImpl(new MockTradeNotificationBroker());
		
		_action.execute(
				new Date(), 
				new Date(),
				null, 
				new MockListenerContext(
						_datastores, 
						marketDataSessionImpl, 
						propertyChangeSession, 
						tradeNotificationSessionImpl));			
	}

	@Override
	public Long rescheduleAbsolute(Long scheduledTimerID_, Date scheduledTime_, boolean moveToNextDayIfInPast_, Long actionKey_) {
		// does nothing
		return null;
	}

	@Override
	public Long rescheduleRelative(Long scheduledTimerID_, long relativeTimePeriodMilliseconds_,
			Long actionKey_) {
		// does nothing
		return null;
	}

	@Override
	public Long rescheduleToTime(Long scheduledTimerID_, Date date_, TimeOfDay time_, boolean moveToNextDayIfInPast_, Long actionKey_) {
		// does nothing
		return null;
	}
	
}
