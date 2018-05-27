package com.imarcats.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.imarcats.model.types.ChangeType;

/**
 * Halt Rule stops Trading, if the Price of the Last Trade is too far from the Opening Price 
 * if the Market. 
 * 
 * @author Adam
 */
@Entity
@Table(name="HALT_RULE")
public class HaltRule implements MarketModelObject {

	private static final long serialVersionUID = 1L;

	/**
	 * Primary Key of the HaltRule 
	 * Note: No need for accessing this field directly
	 */	
	@Id
	@GeneratedValue 
	@Column(name="ID")
    private Long _id;
	
	/**
	 * Amount of Quote Change after the Market is Halted, either absolute or 
	 * percentage of Open Quote (in this case x % -> x / 100)
	 */
	@Column(name="QUOTE_CHANGE_AMOUNT", nullable=false)
	private Double _quoteChangeAmount;
	
	/**
	 * Defines the Change Type of a Quote Value (Percentage or Absolute) - Value from ChangeType
	 */
	@Column(name="CHANGE_TYPE", nullable=false)
	@Enumerated(EnumType.STRING) 
	private ChangeType _changeType = ChangeType.Absolute;
	
	/**
	 * Period in Minutes, after the Market will Re-Open or -1, 
	 * if the Market remains Closed until the next Day
	 */
	@Column(name="HALT_PERIOD", nullable=false)
	private Integer _haltPeriod = -1;

	public static HaltRule create(HaltRule haltRule_) {
		HaltRule newHaltRule = new HaltRule();
		
		newHaltRule.setChangeType(haltRule_.getChangeType());
		newHaltRule.setHaltPeriod(haltRule_.getHaltPeriod());
		newHaltRule.setQuoteChangeAmount(haltRule_.getQuoteChangeAmount());
		
		return newHaltRule;
	}
	
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

	@Override
	public String toString() {
		return "HaltRule [_id=" + _id + ", _quoteChangeAmount="
				+ _quoteChangeAmount + ", _changeType=" + _changeType
				+ ", _haltPeriod=" + _haltPeriod + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((_changeType == null) ? 0 : _changeType.hashCode());
		result = prime * result
				+ ((_haltPeriod == null) ? 0 : _haltPeriod.hashCode());
		result = prime * result + ((_id == null) ? 0 : _id.hashCode());
		result = prime
				* result
				+ ((_quoteChangeAmount == null) ? 0 : _quoteChangeAmount
						.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		HaltRule other = (HaltRule) obj;
		if (_changeType != other._changeType)
			return false;
		if (_haltPeriod == null) {
			if (other._haltPeriod != null)
				return false;
		} else if (!_haltPeriod.equals(other._haltPeriod))
			return false;
		if (_id == null) {
			if (other._id != null)
				return false;
		} else if (!_id.equals(other._id))
			return false;
		if (_quoteChangeAmount == null) {
			if (other._quoteChangeAmount != null)
				return false;
		} else if (!_quoteChangeAmount.equals(other._quoteChangeAmount))
			return false;
		return true;
	}
	
}
