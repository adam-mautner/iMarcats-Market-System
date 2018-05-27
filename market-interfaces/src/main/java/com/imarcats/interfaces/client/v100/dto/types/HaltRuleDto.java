package com.imarcats.interfaces.client.v100.dto.types;

import com.imarcats.interfaces.client.v100.dto.MarketModelObjectDto;

/**
 * Halt Rule stops Trading, if the Price of the Last Trade is too far from the Opening Price 
 * if the Market. 
 * 
 * @author Adam
 */
public class HaltRuleDto implements MarketModelObjectDto {
	
	/**
	 * Amount of Quote Change after the Market is Halted, either absolute or 
	 * percentage of Open Quote (in this case x % -> x / 100)
	 */
	private Double _quoteChangeAmount;
	
	/**
	 * Defines the Change Type of a Quote Value (Percentage or Absolute) - Value from ChangeType
	 */
	private ChangeType _changeType = ChangeType.Absolute;
	
	/**
	 * Period in Minutes, after the Market will Re-Open or -1, 
	 * if the Market remains Closed until the next Day
	 */
	private Integer _haltPeriod = -1;
	
	public double getQuoteChangeAmount() {
		return _quoteChangeAmount != null ? _quoteChangeAmount : 0;
	}

	public void setQuoteChangeAmount(double quoteChangeAmount_) {
		_quoteChangeAmount = quoteChangeAmount_;
	}

	public ChangeType getChangeType() {
		return _changeType;
	}

	public void setChangeType(ChangeType changeType_) {
		_changeType = changeType_;
	}

	public int getHaltPeriod() {
		return _haltPeriod != null ? _haltPeriod : -1;
	}

	public void setHaltPeriod(int haltPeriod_) {
		_haltPeriod = haltPeriod_;
	}
}
