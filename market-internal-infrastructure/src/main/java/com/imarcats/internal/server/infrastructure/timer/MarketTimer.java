package com.imarcats.internal.server.infrastructure.timer;

import java.util.Date;

import com.imarcats.model.types.BusinessCalendar;
import com.imarcats.model.types.RecurringActionDetail;
import com.imarcats.model.types.TimeOfDay;


/**
 * Market Timer Interface
 * @author Adam
 */
public interface MarketTimer {
	
	/**
	 * Schedules the given action to the given absolute time 
	 * @param scheduledTime_ Time, when the Action should be executed
	 * @param moveToNextDayIfInPast_ Moves Scheduled Date to next Date, 
	 * 		  if scheduled time is in the past, anyway throws an Exception
	 * @param action_ Action to be executed
	 *
	 * @return Key of the Timer Action
	 */
	public Long scheduleAbsolute(Date scheduledTime_, boolean moveToNextDayIfInPast_, TimerAction action_);

	/**
	 * Reschedules the an existing action to the given absolute time 
	 * @param scheduledTimerID_ Unique ID for the Original Scheduled Timer 
	 * @param scheduledTime_ Time, when the Action should be executed
	 * @param moveToNextDayIfInPast_ Moves Scheduled Date to next Date, 
	 * 		  if scheduled time is in the past, anyway throws an Exception
	 * @param actionKey_ Action to be rescheduled
	 *
	 * @return Key of the Timer Action
	 * 
	 * Note: Deletes recurring details, if there is any
	 */
	public Long rescheduleAbsolute(Long scheduledTimerID_, Date scheduledTime_, boolean moveToNextDayIfInPast_, Long actionKey_);
	
	/**
	 * Schedules the Action to now + the given Time Period 
	 * @param relativeTimePeriodMilliseconds_ Time Period in Millis 
	 * @param action_ Action to be scheduled
	 *
	 * @return Key of the Timer Action
	 */
	public Long scheduleRelative(long relativeTimePeriodMilliseconds_, TimerAction action_);

	/**
	 * Reschedules an existing Action to now + the given Time Period 
	 * @param scheduledTimerID_ Unique ID for the Original Scheduled Timer 
	 * @param relativeTimePeriodMilliseconds_ Time Period in Millis 
	 * @param actionKey_ Action to be rescheduled
	 *
	 * @return Key of the Timer Action
	 * 
	 * Note: Deletes recurring details, if there is any
	 */
	public Long rescheduleRelative(Long scheduledTimerID_, long relativeTimePeriodMilliseconds_, Long actionKey_);
	
	/**
	 * Schedules the Action to given Time of the given Day 
	 * @param date_ Date to Schedule the Action to 
	 * @param time_ Time to Schedule the Action to
	 * @param recurringActionDetail_ Details for Recurring Actions - null, if not recurring  
	 * @param calendar_ Business Calendar, used for Recurring Actions 
	 * @param moveToNextDayIfInPast_ Moves Scheduled Date to next Date, 
	 * 		  if scheduled time is in the past, anyway throws an Exception
	 * @param action_ Action to be scheduled
	 *
	 * @return Key of the Timer Action
	 */
	public Long scheduleToTime(Date date_, TimeOfDay time_, 
			RecurringActionDetail recurringActionDetail_, 
			BusinessCalendar calendar_, boolean moveToNextDayIfInPast_, TimerAction action_);

	
	/**
	 * Reschedules an existing Action to given Time of the given Day 
	 * @param scheduledTimerID_ Unique ID for the Original Scheduled Timer 
	 * @param date_ Date to Schedule the Action to 
	 * @param time_ Time to Schedule the Action to 
	 * @param moveToNextDayIfInPast_ Moves Scheduled Date to next Date, 
	 * 		  if scheduled time is in the past, anyway throws an Exception 
	 * @param actionKey_ Action to be rescheduled
	 *
	 * @return Key of the Timer Action
	 */
	public Long rescheduleToTime(Long scheduledTimerID_, Date date_, TimeOfDay time_, boolean moveToNextDayIfInPast_, Long actionKey_);
	
	/**
	 * Cancels a Scheduled Time Trigger Action
	 * @param action_ Key of the Timer Action
	 *
	 */
	public void cancel(Long action_);
}
