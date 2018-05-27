package com.imarcats.interfaces.client.v100.dto;


/**
 * Describes a Bank Account for a User 
 * @author Adam
 *
 */
public class AccountInformationDto implements MarketModelObjectDto {
	
	/**
	 * Name of the Bank 
	 * Required
	 */
//	@Column(name="BANK_NAME", nullable=false, length=100)
	private String _bankName;
	
	/**
	 * String representation of an Bank Account Number (IBAN)
	 * Required
	 */
//	@Column(name="ACCOUNT_NUMBER", nullable=false, length=34)
	private String _accountNumber;

	public String getBankName() {
		return _bankName;
	}

	public void setBankName(String bankName_) {
		_bankName = bankName_;
	}

	public String getAccountNumber() {
		return _accountNumber;
	}

	public void setAccountNumber(String accountNumber_) {
		_accountNumber = accountNumber_;
	}	
}
