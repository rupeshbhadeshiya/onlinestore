package com.learning.ddd.onlinestore.domain.event.pubsub.kafka;

import java.util.Properties;

import javax.jms.JMSException;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;

import com.learning.ddd.onlinestore.domain.event.OnlinestoreDomainEvent;
import com.learning.ddd.onlinestore.domain.event.pubsub.DomainEventsPublisher;

//@Primary
//@Component
public class KafkaBasedDomainEventsPublisher extends DomainEventsPublisher {

	private static final String ONLINESTORE_INVENTORY_TOPIC = "onlinestore-inventory-events";
	
//	private Producer<DomainEventName, DomainEvent> kafkaProducer;
	private Producer<String, Object> kafkaProducer;
	
	public KafkaBasedDomainEventsPublisher() {
		
		Properties props = new Properties();
		props.put("bootstrap.servers", "localhost:9092");
		props.put("acks", "all");
		props.put("retries", 0);
		props.put("linger.ms", 1);
		props.put("key.serializer", 
			"org.apache.kafka.common.serialization.StringSerializer");
		//props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		props.put("value.serializer", 
			"com.learning.ddd.onlinestore.commons.domain.event.kafka.KafkaBasedObjectSerializer");

		kafkaProducer = new KafkaProducer<>(props);
	}

	@Override
	public void publishDomainEvent(OnlinestoreDomainEvent domainEvent) throws JMSException {
		System.out.println("------- KafkaBasedDomainEventsPublisher.publishEvent() started...");
		
//		if (domainEvent instanceof ItemsAddedToInventoryEvent) {
//			
//			ItemsAddedToInventoryEvent itemsAddedEvent = (ItemsAddedToInventoryEvent) domainEvent;
//		
////			ProducerRecord<DomainEventName, DomainEvent> record = new ProducerRecord<>(
////				ONLINESTORE_INVENTORY_TOPIC, inventoryEvent.getEventName(), inventoryEvent
////			);
//			
//			System.out.println("~~~~~~~~ KafkaBasedDomainEventsPublisher received event : " +  itemsAddedEvent);
//			
//			for (InventoryItem item : itemsAddedEvent.getItems()) {
//				
////				ProducerRecord<String, String> record = new ProducerRecord<>(
////					ONLINESTORE_INVENTORY_TOPIC, 
////					itemsAddedEvent.getEventName().name(), 
////					item.getItemId() + "-" + item.getName()
////				);
//				
//				ProducerRecord<String, Object> record = new ProducerRecord<>(
//					ONLINESTORE_INVENTORY_TOPIC, 
//					itemsAddedEvent.getEventName().name(), 
//					item
//				);
//				
//				kafkaProducer.send(record);
//				
//				System.out.println("~~~~~~~~ KafkaBasedDomainEventsPublisher sent record : " +  record);
//			}
//
//			kafkaProducer.close();
//		}

		System.out.println("------- KafkaBasedDomainEventsPublisher.publishEvent() completed.");
	}

	@Override
	protected String getTopicName() {
		return ONLINESTORE_INVENTORY_TOPIC;
	}

	@Override
	protected String getCallingServiceName() {
		// TODO Auto-generated method stub
		return null;
	}

}
