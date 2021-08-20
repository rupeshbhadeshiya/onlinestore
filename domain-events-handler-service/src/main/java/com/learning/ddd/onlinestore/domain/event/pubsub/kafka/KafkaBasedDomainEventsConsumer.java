package com.learning.ddd.onlinestore.commons.domain.event.pubsub.kafka;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

public class KafkaBasedDomainEventsConsumer {
	
	private static final String ONLINESTORE_INVENTORY_TOPIC = "onlinestore-inventory-events";

	public static void main(String[] args) {
		new KafkaBasedDomainEventsConsumer().consume();
	}

	void consume() {
		//org.apache.log4j.Logger.getLogger("kafka").setLevel(Level.WARN);
		//org.apache.logging.log4j.LogManager.getLogger("kafka");
		
		System.out.print("============================");
		System.out.print("[ KafkaBasedDomainEventsConsumer started ]");
		System.out.println("============================");
		
		Properties props = new Properties();
		props.setProperty("bootstrap.servers", "localhost:9092");
		props.setProperty("group.id", "test");
		props.setProperty("enable.auto.commit", "true");
		props.setProperty("auto.commit.interval.ms", "1000");
		props.setProperty("key.deserializer", 
			"org.apache.kafka.common.serialization.StringDeserializer");
		props.setProperty("value.deserializer", 
			//"org.apache.kafka.common.serialization.StringDeserializer");
			"com.learning.ddd.onlinestore.commons.domain.event.kafka.KafkaBasedObjectDeserializer"
		);
		
		KafkaConsumer<String, Object> consumer = new KafkaConsumer<>(props);
		consumer.subscribe(Arrays.asList(ONLINESTORE_INVENTORY_TOPIC));
		int i = 1;
		ConsumerRecords<String, Object> records = null;
		do {
			records = consumer.poll(Duration.ofMillis(500));
			for (ConsumerRecord<String, Object> record : records) {
				System.out.println("~~~~~~~~ KafkaBasedDomainEventsConsumer "
					+ "read record : key="+record.key() +", value="+record.value()
				);
			}
		} while (!records.isEmpty() || (i++ < 1000));
			
		consumer.close();
		
		System.out.print("============================");
		System.out.print("[ KafkaBasedDomainEventsConsumer completed ]");
		System.out.println("============================");
	}

}
