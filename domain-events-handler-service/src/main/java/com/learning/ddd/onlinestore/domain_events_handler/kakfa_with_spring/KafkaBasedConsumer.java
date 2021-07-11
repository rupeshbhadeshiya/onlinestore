package com.learning.ddd.onlinestore.domain_events_handler.kakfa_with_spring;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaBasedConsumer {

	@Value("${spring.kafka.consumer.group-id}")
	private String groupId;
	
//  Logger LOG = LoggerFactory.getLogger(KafkaListenersExample.class);

	@KafkaListener(topics = "onlinestore-inventory-events")
	void listener(String data) {
		
		System.out.println("==========> KafkaBasedConsumer, data="+data);
		
//    LOG.info(data);
	}

	@KafkaListener(topics = "onlinestore-inventory-events", 
		groupId = "${spring.kafka.consumer.group-id}")
	void commonListenerForMultipleTopics(String message) {
		
		System.out.println("==========> KafkaBasedConsumer, message="+message);
		
//    LOG.info("MultipleTopicListener - {}", message);
	}

}