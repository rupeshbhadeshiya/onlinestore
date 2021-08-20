package com.learning.ddd.onlinestore.domain.event.pubsub.activemq;

import java.util.Date;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;

public class SampleActiveMQ_Topic_Producer implements Runnable {

	public void run() {
		
		Connection connection = null;
		Session session = null;
		
		try { 
			System.out.println("SampleActiveMQ_Topic_Producer: started");
			
			// Create a connection factory.
			ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory("tcp://localhost:61616");
			System.out.println("SampleActiveMQ_Topic_Producer: ActiveMQConnectionFactory created");

			// Create connection.
			connection = factory.createConnection();
			System.out.println("SampleActiveMQ_Topic_Producer: Connection created");

			// Start the connection
			connection.start();
			System.out.println("SampleActiveMQ_Topic_Producer: Connection started");

			// Create a session which is non transactional
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			System.out.println("SampleActiveMQ_Topic_Producer: Session created");

			// Create Destination topic
			Topic topic = session.createTopic("SampleActiveMQTopic");
			System.out.println("SampleActiveMQ_Topic_Producer: Topic created - " + topic);

			// Create a producer
			MessageProducer producer = session.createProducer(topic);
			System.out.println("SampleActiveMQ_Topic_Producer: MessageProducer created");

			producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

			String msg = "Hello World! Today is " + new Date();

			// insert message
			System.out.println("SampleActiveMQ_Topic_Producer: MessageProducer sending message - " + msg);
			TextMessage message = session.createTextMessage(msg);
			
			producer.send(message);
			System.out.println("SampleActiveMQ_Topic_Producer: MessageProducer sent TextMessage - " + msg);

		} catch (Exception ex) {
			System.err.println("SampleActiveMQ_Topic_Producer: Exception while sending a message to a Topic!");
			ex.printStackTrace();
			
		} finally {
			
			try {
				
				if (session != null) {
					session.close();
					System.out.println("SampleActiveMQ_Topic_Producer: Session closed");
				}
				
			} catch (Exception ex) {
				System.err.println("SampleActiveMQ_Topic_Producer: Exception while closing Session!");
				ex.printStackTrace();
			}
			
			try {
				
				if (connection != null) {
					connection.close();
					System.out.println("SampleActiveMQ_Topic_Producer: Connection closed");
				}
				
			} catch (Exception ex) {
				System.err.println("SampleActiveMQ_Topic_Producer: Exception while closing Connection!");
				ex.printStackTrace();
			}
			
			System.out.println("SampleActiveMQ_Topic_Producer: stopped");
		}
	}
}