package com.imarcats.internal.server.infrastructure.timer;

import java.util.Date;
import java.util.GregorianCalendar;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.imarcats.internal.server.infrastructure.notification.ListenerCallContext;
import com.imarcats.internal.server.infrastructure.notification.ListenerCallParameters;
import com.imarcats.internal.server.infrastructure.notification.MarketObjectListenerBase;
import com.imarcats.model.types.BusinessCalendar;
import com.imarcats.model.types.RecurringActionDetail;
import com.imarcats.model.types.TimeOfDay;

/**
 * Base Class for Timer Actions
 * @author Adam
 */
@Entity
@Table(name="TIMER_ACTION")
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
public abstract class TimerAction extends MarketObjectListenerBase {

	/**
	 * Details for Recurring Action, if null it is not recurring action 
	 * 
	 * Note: This field will be set by the Timer Framework
	 */
	@Column(name="RECURRING_ACTION_DETAIL")
	private RecurringActionDetail _recurringActionDetail;	
	
    /**
	 * Business Calendar for Recurring Action
	 * 
	 * Note: This field will be set by the Timer Framework
	 */
	@Column(name="BUSINESS_CALENDAR", nullable=false)
	private BusinessCalendar _businessCalendar;
    
    /**
	 * Scheduled Time for Recurring Action
	 * 
	 * Note: This field will be set by the Timer Framework
	 */
	@OneToOne(cascade=CascadeType.ALL, orphanRemoval=true) 
	@JoinColumn(name="SCHEDULED_TIME_ID")
    private ScheduledTime _scheduledTime;
    
    /**
	 * ID for Scheduled Timer for Action
	 * This prevents, that an older Scheduled Timer triggers this Action
	 * 
	 * Note: This field will be set by the Timer Framework     
	 * Note: This field was introduced later, that's why it has Integer object type. 
     */ 
	@Column(name="SCHEDULED_TIMER_ID")
    private Long _scheduledTimerID;
    
	@Override
	public final void fireListener(ListenerCallParameters listenerParameters_,
			ListenerCallContext listenerContext_) {
		if(getScheduledTimerID() != null && listenerParameters_.getID() != getScheduledTimerID()) {
			// this listener is not scheduled for this timer 
			return;
		}
		
		Date actualCallTime = new Date();
		Date scheduledCallTime = listenerParameters_.getListenerCallTimestamp();
		
		execute(actualCallTime, scheduledCallTime, listenerParameters_, listenerContext_);

		// reschedule recurring
		// TODO: Remove this once we have recurring timer 
		if(getRecurringActionDetail() != null) {
			GregorianCalendar gregorianCalendar = new GregorianCalendar();
			gregorianCalendar.setTime(scheduledCallTime);
			
			// add 1 day to scheduled time 
			gregorianCalendar.add(GregorianCalendar.DATE, 1);
			
			// find next business day
			Date nextBusinessDate = TimerUtils.findNextBusinessDay(
					gregorianCalendar.getTime(), getRecurringActionDetail(), getBusinessCalendar());
			
			// reschedule to next business day
			listenerContext_.getMarketTimer().rescheduleToTime(
					getScheduledTimerID(), nextBusinessDate, getScheduledTime(), true, getListenerKey());
		}
		
	}

	/**
	 * Called, when the scheduled time comes
	 * @param actualCallTime_ Time when the Timer Action was called
	 * @param scheduledCallTime_ Time when the Timer Action was supposed to be called
	 * @param listenerParameters_ Parameters for this Listener Call
	 * @param listenerContext_ Context for this Listener Call
	 */
	public abstract void execute(Date actualCallTime_, Date scheduledCallTime_, ListenerCallParameters listenerParameters_, ListenerCallContext listenerContext_);

	public RecurringActionDetail getRecurringActionDetail() {
		return _recurringActionDetail;
	}

	public void setRecurringActionDetail(
			RecurringActionDetail recurringActionDetail_) {
		_recurringActionDetail = null; // this is needed, because Google App Engine's Datastore would not save the new value anyway
		_recurringActionDetail = recurringActionDetail_;
	}

	public BusinessCalendar getBusinessCalendar() {
		return _businessCalendar;
	}

	public void setBusinessCalendar(BusinessCalendar businessCalendar_) {
		_businessCalendar = null;  // this is needed, because Google App Engine's Datastore would not save the new value anyway
		_businessCalendar = businessCalendar_;
	}

	public TimeOfDay getScheduledTime() {
		return _scheduledTime != null 
					? _scheduledTime.getScheduledTime() 
					: null;
	}

	public void setScheduledTime(TimeOfDay scheduledTime_) {
		// this is needed, because Google App Engine's Datastore would not let 
		// change the uncommited classes, since this field may change many times 
		// before commit, we can only copy the values on the object
		// TODO: Is this the same as in JPA?
		if(_scheduledTime != null) {
			if(_scheduledTime == null) {
				_scheduledTime = 
					new ScheduledTime();
				_scheduledTime.setScheduledTime(TimeOfDay.create(scheduledTime_));
					// Cloning TimeOfDay is needed, because it might be copied from an other persisted Object
			
			} else {
				_scheduledTime.setScheduledTime(TimeOfDay.create(scheduledTime_));
			}
		} else {
			_scheduledTime = null;
		}
	}

	public void setScheduledTimerID(Long scheduledTimerID_) {
		_scheduledTimerID = scheduledTimerID_;
	}

	public Long getScheduledTimerID() {
		return _scheduledTimerID;
	}

	@Override
	public String toString() {
		return "TimerAction [_recurringActionDetail=" + _recurringActionDetail
				+ ", _businessCalendar=" + _businessCalendar
				+ ", _scheduledTime=" + _scheduledTime + ", _scheduledTimerID="
				+ _scheduledTimerID + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime
				* result
				+ ((_businessCalendar == null) ? 0 : _businessCalendar
						.hashCode());
		result = prime
				* result
				+ ((_recurringActionDetail == null) ? 0
						: _recurringActionDetail.hashCode());
		result = prime * result
				+ ((_scheduledTime == null) ? 0 : _scheduledTime.hashCode());
		result = prime
				* result
				+ ((_scheduledTimerID == null) ? 0 : _scheduledTimerID
						.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		TimerAction other = (TimerAction) obj;
		if (_businessCalendar == null) {
			if (other._businessCalendar != null)
				return false;
		} else if (!_businessCalendar.equals(other._businessCalendar))
			return false;
		if (_recurringActionDetail != other._recurringActionDetail)
			return false;
		if (_scheduledTime == null) {
			if (other._scheduledTime != null)
				return false;
		} else if (!_scheduledTime.equals(other._scheduledTime))
			return false;
		if (_scheduledTimerID == null) {
			if (other._scheduledTimerID != null)
				return false;
		} else if (!_scheduledTimerID.equals(other._scheduledTimerID))
			return false;
		return true;
	}
	
	
}
