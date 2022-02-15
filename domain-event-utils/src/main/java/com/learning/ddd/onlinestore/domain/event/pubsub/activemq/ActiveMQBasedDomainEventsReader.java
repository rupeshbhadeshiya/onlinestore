package com.learning.ddd.onlinestore.domain.event.pubsub.activemq;

import java.util.Set;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.learning.ddd.onlinestore.domain.event.pubsub.DomainEventsReader;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE) // V.IMP; each Consumer Bean using it must've exclusive copy!
														// otherwise it results into multithreading issues...
public class ActiveMQBasedDomainEventsReader implements DomainEventsReader {

	private static final String LISTENER_NAME = ActiveMQBasedDomainEventsReader.class.getSimpleName();
	
	// FIXME Should session be opened for each new message sending or just one session
	//			to be used for lifetime (like done right now) - which is the correct way?
	private Connection connection = null;
	private Session session = null;
	private MessageConsumer consumer = null;

	private String serviceAndListerName;

	private String topicName;
	//private String callingServiceName;
	
	
	@Override
	public void connect(String topicName, String callingServiceName) {
		
		this.topicName = topicName;
		//this.callingServiceName = callingServiceName;
		this.serviceAndListerName = callingServiceName + " - " + LISTENER_NAME;
		
		System.out.println(this.serviceAndListerName + ": connect() - started, Topic = " + topicName);
		
		try {
			
			// Create a connection factory.
			ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory("tcp://localhost:61616");
			System.out.println(this.serviceAndListerName + ": connect() - ActiveMQConnectionFactory created");

			// set necessary properties
			factory.setTrustAllPackages(true);
			
			// Create connection.
			connection = factory.createConnection();
			System.out.println(this.serviceAndListerName + ": Connection created, Topic = " + topicName);

			// Start the connection
			connection.start();
			System.out.println(this.serviceAndListerName + ": connect() - Connection started");

			// Create a session which is non transactional
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			System.out.println(this.serviceAndListerName + ": connect() - Session created, Topic = " + topicName);

			// Reuse existing Topic or Create a new Destination topic
			
			Topic topic = null; //session.createTopic(topicName);
			
			// first search
			Set<ActiveMQTopic> existingTopics = ((ActiveMQConnection) connection).getDestinationSource().getTopics();
			for (ActiveMQTopic activeMQTopic : existingTopics) {
				if (activeMQTopic.getTopicName().equals(topicName)) {
					topic = activeMQTopic;
					System.out.println(this.serviceAndListerName + ": connect() - Topic exists! - " + topic);
					break;
				}
			}
			
			if (topic == null) { // not found, so create a new one
				topic = session.createTopic(topicName);
				System.out.println(this.serviceAndListerName + ": connect() - Topic not existing so created - " + topic);
			}
			
			// Create a consumer
			consumer = session.createConsumer(topic);
			System.out.println(this.serviceAndListerName + ": connect() - MessageConsumer created, Topic = " + topicName);

		} catch (Exception ex) {
			System.err.println(this.serviceAndListerName + ": connect() - Exception while creating "
					+ "Connection/Session/MessageConsumer! Topic = " + topicName);
			ex.printStackTrace();
		}
			
		System.out.println(this.serviceAndListerName + ": connect() - completed, Topic = " + topicName);
	}

	@Override
	public ObjectMessage read() throws JMSException {
		
		//System.out.println("ActiveMQBasedDomainEventsListener: MessageConsumer started listening to receive message...");
		
		try { 
			
            Message message = consumer.receive(1000);
            
            if ((message != null) && !(message instanceof ObjectMessage)) {
            	System.err.println(this.serviceAndListerName + ": read(): received message is not "
            		+ "an instance of ObjectMessage! - " + message + ", Topic = " + topicName);
            	throw new RuntimeException("Not an instance of ObjectMessage! " + message);
            }
           
            return (ObjectMessage) message;
            
		} catch (JMSException ex) {
			System.err.println(this.serviceAndListerName + ": read(): Exception while receiving a message!"
				+ " Topic = " + topicName);
			ex.printStackTrace();
			throw ex;
			
		} finally {
			
			//System.out.println("ActiveMQBasedDomainEventsListener: MessageConsumer stopped listening to receive message");
		}
		
	}
	
	
	@Override
	public void disconnect() {
	
		System.out.println(this.serviceAndListerName + ": disconnect() - started, Topic = " + topicName);
		
		try {
			
			if (session != null) {
				session.close();
				System.out.println(this.serviceAndListerName + ": disconnect() - Session closed, Topic = " + topicName);
			}
			
		} catch (Exception ex) {
			System.err.println(this.serviceAndListerName + ": disconnect() - Exception while closing Session!"
				+ ", Topic = " + topicName);
			ex.printStackTrace();
		}
		
		try {
			
			if (connection != null) {
				connection.close();
				System.out.println(this.serviceAndListerName + ": disconnect() - Connection closed, Topic = " + topicName);
			}
			
		} catch (Exception ex) {
			System.err.println(this.serviceAndListerName + ": disconnect() - Exception while closing Connection!"
				+ ", Topic = " + topicName);
			ex.printStackTrace();
		}
		
		System.out.println(this.serviceAndListerName + ": disconnect() - completed, Topic = " + topicName);
	} 
	

}
