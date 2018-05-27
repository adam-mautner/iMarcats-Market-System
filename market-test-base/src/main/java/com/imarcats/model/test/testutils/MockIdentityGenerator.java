package com.imarcats.model.test.testutils;


public class MockIdentityGenerator {
	private static long _id = 1;
	
	private MockIdentityGenerator() { /* static class */ }
	
	public static Long getId() { return _id++; }
}
