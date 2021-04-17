package com.learning.ddd.onlinestore.test_service.kafka;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

public class TestKafkaConsumer {
	
	private static final String KAFKA_TOPIC = "test-service-events";

	public static void main(String[] args) {
		new TestKafkaConsumer().consume();
	}

	void consume() {
		System.out.println("------->>> TestKafkaConsumer started...");
		
		Properties props = new Properties();
		props.setProperty("bootstrap.servers", "localhost:9092");
		props.setProperty("group.id", "test");
		props.setProperty("enable.auto.commit", "true");
		props.setProperty("auto.commit.interval.ms", "1000");
		props.setProperty("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		props.setProperty("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		
		KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
		consumer.subscribe(Arrays.asList(KAFKA_TOPIC));
		int i = 1;
		ConsumerRecords<String, String> records = null;
//		while ((records!=null && !records.isEmpty()) && (i++ < 100)) {
		do {
			records = consumer.poll(Duration.ofMillis(100));
			for (ConsumerRecord<String, String> record : records) {
				//System.out.printf("~~~~~~~~  offset = %d, key = %s, value = %s%n", record.offset(), record.key(), record.value());
				System.out.println("~~~~~~~~ TestKafkaConsumer read record : key="+record.key() +", value="+record.value());
			}
		} while (!records.isEmpty() && (i++ < 100));
			
		consumer.close();
		
		System.out.println("-------<<< TestKafkaConsumer completed.");
	}

}
