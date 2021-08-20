package com.learning.ddd.onlinestore.commons.domain.event.pubsub.kafka;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Map;

import org.apache.kafka.common.serialization.Serializer;

public class KafkaBasedObjectSerializer implements Serializer<Object> {

	@Override
	public void configure(Map<String, ?> configs, boolean isKey) {
	}

	@Override
	public byte[] serialize(String topic, Object data) {
		
		try {
			ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteStream);
			objectOutputStream.writeObject(data);
			return byteStream.toByteArray();
			
		} catch (IOException e) {
			e.printStackTrace();
			
		}
		
		return null;
	}

	@Override
	public void close() {
	}

}
