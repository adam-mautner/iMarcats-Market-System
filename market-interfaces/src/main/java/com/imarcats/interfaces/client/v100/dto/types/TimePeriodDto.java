package com.imarcats.interfaces.client.v100.dto.types;



/**
 * Defines a Time Period
 * @author Adam
 *
 */
public class TimePeriodDto implements TransferableObjectDto {

	/**
	 * Start Time
	 * Required
	 */
	private TimeOfDayDto _startTime;

	/**
	 * Start Time
	 * Required
	 */
	private TimeOfDayDto _endTime;
	
	public TimeOfDayDto getStartTime() {
		return _startTime;
	}

	public void setStartTime(TimeOfDayDto startTime_) {
		_startTime = startTime_;
	}

	public TimeOfDayDto getEndTime() {
		return _endTime;
	}

	public void setEndTime(TimeOfDayDto endTime_) {
		_endTime = endTime_;
	}

	@Override
	public Object getObjectValue() {
		return this;
	}
}
