package com.imarcats.internal.server.interfaces.util;



/**
 * Generates Unique IDs in the System
 * @author Adam
 * TODO: Change this to a Real ID Generator (This should be a Transactional Sequence in a DB)
 */
public class UniqueIDGenerator {

	private static int _id = 0;
	
	public UniqueIDGenerator() { /* static class */ }

	public static long nextID() {
		return _id++;
		// return UUID.randomUUID().clockSequence();
	}
	
}
