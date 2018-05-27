package com.imarcats.interfaces.client.v100.dto.types;

/**
 * Defines the Type of the Settlement
 * @author Adam
 */
public enum SettlementPrice {

	/* Settlement Price include Accrued Interest */
	Dirty,
	/* Settlement Price is without Accrued Interest */
	Clean
}
