package com.imarcats.interfaces.test.v100.messages.serialization;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import com.imarcats.model.MarketModelObject;

/**
 * Helper Class to Serialize/Deserialize Market Model Objects 
 * @author Adam
 */
public class MarketModelObjectSerializer {

	private MarketModelObjectSerializer() { /* static utility class */ }

	public static void serializeToOutputStream(
			MarketModelObject marketModelObject_,
			OutputStream outputStream) throws IOException {
		ObjectOutputStream out = new ObjectOutputStream(outputStream);
		
		out.writeObject(marketModelObject_);
		out.close();
	}

	public static MarketModelObject deserializeFromInputStream(
			InputStream inputStream_) throws IOException,
			ClassNotFoundException {
		ObjectInputStream in = new ObjectInputStream(inputStream_);
		
		MarketModelObject marketModelObject = (MarketModelObject) in.readObject();
		
		in.close();
		return marketModelObject;
	}
	
}
