package com.learning.ddd.onlinestore.domain.event.pubsub.activemq;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.Topic;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.learning.ddd.onlinestore.domain.event.DomainEvent;
import com.learning.ddd.onlinestore.domain.event.pubsub.DomainEventPublisher;
import com.learning.ddd.onlinestore.domain.event.pubsub.exception.DomainEventPublishingFailedException;

@Component
public class ActiveMQMessagesPublisher implements DomainEventPublisher, ServletContextListener {

	private Connection connection = null;
	// FIXME Should session be opened for each new message sending or just one session
	//			to be used for lifetime (like done right now) - which is the correct way?
	private Session session = null;
	private MessageProducer producer = null;
	
	@Value("${onlinestore.domain.events.topic.name:OnlinestoreDomainEventsTopic}")
	private String topicName;
	
	@Override
	public void publishEvent(DomainEvent domainEvent) throws DomainEventPublishingFailedException {
		System.out.println("ActiveMQMessagesPublisher: publishEvent() - started");
		
		try { 
			//String msg = "Hello World! Today is " + new Date();
			//TextMessage message = session.createTextMessage(message);

			ObjectMessage message = session.createObjectMessage(domainEvent);
			System.out.println("ActiveMQMessagesPublisher: publishEvent() - MessageProducer sending ObjectMessage - " + message);
			
			producer.send(message);
			System.out.println("ActiveMQMessagesPublisher: publishEvent() - MessageProducer sent ObjectMessage - " + message);

		} catch (JMSException ex) {
			System.err.println("ActiveMQMessagesPublisher: publishEvent() - Exception while sending ObjectMessage!");
			ex.printStackTrace();
			throw new DomainEventPublishingFailedException(ex);
			
		} finally {
			System.out.println("ActiveMQMessagesPublisher: publishEvent() - completed");
		}
	}
	
	/**
	 * On Spring Application start-up, create ActiveMQ Connection and Session
	 */
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		System.out.println("ActiveMQMessagesPublisher: contextInitialized() - started");
		
		try {
			
			// Create a connection factory.
			ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory("tcp://localhost:61616");
			System.out.println("ActiveMQMessagesPublisher: contextInitialized() - ActiveMQConnectionFactory created");

			// Create connection.
			connection = factory.createConnection();
			System.out.println("ActiveMQMessagesPublisher: contextInitialized() - Connection created");

			// Start the connection
			connection.start();
			System.out.println("ActiveMQMessagesPublisher: contextInitialized() - Connection started");

			// Create a session which is non transactional
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			System.out.println("ActiveMQMessagesPublisher: contextInitialized() - Session created");

			// Create Destination topic
			Topic topic = session.createTopic(topicName);
			System.out.println("ActiveMQMessagesPublisher: contextInitialized() - Topic created - " + topic);

			// Create a producer
			producer = session.createProducer(topic);
			System.out.println("ActiveMQMessagesPublisher: contextInitialized() - MessageProducer created");

			producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
			
		} catch (Exception ex) {
			System.err.println("ActiveMQMessagesPublisher: contextInitialized() - Exception while creating Connection/Session/MessageProducer!");
			ex.printStackTrace();
		}
			
		System.out.println("ActiveMQMessagesPublisher: contextInitialized() - completed");
	}
	
	/**
	 * On Spring Application shut-down, close ActiveMQ Session and Connection
	 */
	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		System.out.println("ActiveMQMessagesPublisher: contextDestroyed() - started");
		
		try {
			
			if (session != null) {
				session.close();
				System.out.println("ActiveMQMessagesPublisher: contextDestroyed() - Session closed");
			}
			
		} catch (Exception ex) {
			System.err.println("ActiveMQMessagesPublisher: contextDestroyed() - Exception while closing Session!");
			ex.printStackTrace();
		}
		
		try {
			
			if (connection != null) {
				connection.close();
				System.out.println("ActiveMQMessagesPublisher: contextDestroyed() - Connection closed");
			}
			
		} catch (Exception ex) {
			System.err.println("ActiveMQMessagesPublisher: contextDestroyed() - Exception while closing Connection!");
			ex.printStackTrace();
		}
		
		System.out.println("ActiveMQMessagesPublisher: contextDestroyed() - completed");
	}

}