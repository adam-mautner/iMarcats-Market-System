package com.imarcats.interfaces.client.v100.dto.types;

public enum TradeSettlementState {
	NewTrade, 
	PendingClearing, 
	ClearingConfirmed, 
	ClearingRejected, 
	TradeConfirmed, 
	FullySettled
}
