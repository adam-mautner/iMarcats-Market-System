package com.imarcats.interfaces.client.v100.messages.response;

/**
 * Information about current positions for a client 
 * @author Adam
 */
public class CurrentPositionListResponse {

	private CurrentPositionResponse[] _positions;

	public void setPositions(CurrentPositionResponse[] positions) {
		_positions = positions;
	}

	public CurrentPositionResponse[] getPositions() {
		return _positions;
	}
	
}
