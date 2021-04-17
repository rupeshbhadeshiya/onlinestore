package com.learning.ddd.onlinestore.commons.domain.event.kafka;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Map;

import org.apache.kafka.common.serialization.Deserializer;

public class KafkaBasedObjectDeserializer implements Deserializer<Object> {

	@Override
	public void configure(Map<String, ?> configs, boolean isKey) {
	}

	@Override
	public Object deserialize(String topic, byte[] data) {

		try {
			ByteArrayInputStream byteStream = new ByteArrayInputStream(data);
			ObjectInputStream objectInputStream = new ObjectInputStream(byteStream);
			return objectInputStream.readObject();
		
		} catch (IOException e) {
			e.printStackTrace();
		
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public void close() {
	}

}
