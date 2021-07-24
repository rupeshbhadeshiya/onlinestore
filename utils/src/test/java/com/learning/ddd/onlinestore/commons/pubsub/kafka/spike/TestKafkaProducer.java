package com.learning.ddd.onlinestore.commons.pubsub.kafka.spike;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

public class TestKafkaProducer {

	private static final String KAFKA_TOPIC = "test-service-events";
	
	public static void main(String[] args) {
		new TestKafkaProducer().produce();
	}

	void produce() {
		System.out.println("------->>> TestKafkaProducer started...");
		
		Properties props = new Properties();
		props.put("bootstrap.servers", "localhost:9092");
		props.put("acks", "all");
		props.put("retries", 0);
		props.put("linger.ms", 1);
		props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

		Producer<String, String> producer = new KafkaProducer<>(props);
		for (int i = 10; i < 100; i+=10) {
			ProducerRecord<String, String> record = new ProducerRecord<String, String>(
				KAFKA_TOPIC, Integer.toString(i), Integer.toString(i));
			producer.send(record);
			System.out.println("~~~~~~~~ TestKafkaProducer send record : " +  Integer.toString(i));
		}

		producer.close();

		System.out.println("-------<<< TestKafkaProducer completed.");
	}

}
