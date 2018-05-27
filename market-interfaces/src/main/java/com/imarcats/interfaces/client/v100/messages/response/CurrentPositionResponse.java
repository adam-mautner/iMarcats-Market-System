package com.imarcats.interfaces.client.v100.messages.response;

import java.util.Date;

/**
 * Information about current position of a client in a given Instrument and Market
 * @author Adam
 */
public class CurrentPositionResponse {

	/**
	 * Instrument code of instrument of the position
	 */
	private String _marketCode;
	
	/**
	 * Instrument code of instrument of the position
	 */
	private String _instrumentCode;
	
	/**
	 * Currency of the position
	 */
	private String _currency;
	
	/**
	 * Contract size of instrument of the position
	 */
	private double _contractSize;
	
	/**
	 * Contract unit size of instrument of the position
	 */
	private String _contractSizeUnit;
	
	/**
	 * Current position 
	 * - Buy trade sizes added as positive number 
	 * - Sell trade sizes added as negative number
	 */
	private int _currentPosition;
	
	/**
	 * Total cost of the trades is sum of the buy and sell trade costs (negative cost means income)
	 * 		Cost of a Buy Trade = – Trade Price * Trade Size 
	 * 		Cost of a Sell Trade = + Trade Price * Trade Size 
	 * 		Markets that quote in yield shall calculate Price = 100 – Yield
	 */
	private double _totalCost;
	
	/**
	 * Transaction ID from the latest trade 
	 * Note: If  transaction ID or timestamp is older in Trader GUI than in the message, 
	 * 		 message shall be used to update information in Trader GUI
	 */
	private Long _transactionIdOfLatestTrade;
	
	/**
	 * Time when current position was requested
	 * Note: If  transaction ID or timestamp is older in Trader GUI than in the message, 
	 * 		 message shall be used to update information in Trader GUI
	 */
	private Date _timeOfRequest;

	/**
	 * Force update is used to tell Trader GUI, that this position update shall result 
	 * a data update on Trader GUI, even if the transaction ID and timestamp of the message is the same as 
	 * the transaction ID and timestamp known on Trader GUI. 
	 * If transaction ID and timestamp is older in the message even with force, message shall be discarded.
	 */
	private boolean _forceUpdate;
	
	public String getInstrumentCode() {
		return _instrumentCode;
	}

	public void setInstrumentCode(String instrumentCode_) {
		_instrumentCode = instrumentCode_;
	}

	public int getCurrentPosition() {
		return _currentPosition;
	}

	public void setCurrentPosition(int currentPosition_) {
		_currentPosition = currentPosition_;
	}

	public double getTotalCost() {
		return _totalCost;
	}

	public void setTotalCost(double totalCost_) {
		_totalCost = totalCost_;
	}

	public Long getTransactionIdOfLatestTrade() {
		return _transactionIdOfLatestTrade;
	}

	public void setTransactionIdOfLatestTrade(Long transactionIdOfLatestTrade_) {
		_transactionIdOfLatestTrade = transactionIdOfLatestTrade_;
	}

	public Date getTimeOfRequest() {
		return _timeOfRequest;
	}

	public void setTimeOfRequest(Date timeOfRequest_) {
		_timeOfRequest = timeOfRequest_;
	}

	public void setForceUpdate(boolean forceUpdate) {
		_forceUpdate = forceUpdate;
	}

	public boolean getForceUpdate() {
		return _forceUpdate;
	}

	public void setMarketCode(String marketCode) {
		_marketCode = marketCode;
	}

	public String getMarketCode() {
		return _marketCode;
	}

	public String getCurrency() {
		return _currency;
	}

	public void setCurrency(String currency_) {
		_currency = currency_;
	}

	public double getContractSize() {
		return _contractSize;
	}

	public void setContractSize(double contractSize_) {
		_contractSize = contractSize_;
	}

	public String getContractSizeUnit() {
		return _contractSizeUnit;
	}

	public void setContractSizeUnit(String contractSizeUnit_) {
		_contractSizeUnit = contractSizeUnit_;
	}
}
