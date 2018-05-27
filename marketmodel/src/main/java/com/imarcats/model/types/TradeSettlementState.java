package com.imarcats.model.types;

public enum TradeSettlementState {
	NewTrade, 
	PendingClearing, 
	ClearingConfirmed, 
	ClearingRejected, 
	TradeConfirmed, 
	FullySettled
}
