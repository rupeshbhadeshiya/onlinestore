package com.learning.ddd.onlinestore.domain.event.pubsub.activemq;

import javax.jms.Connection;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;

public class SampleActiveMQ_Topic_Consumer implements Runnable {

	public void run() {
		
		Connection connection = null;
		Session session = null;
		
		try { 
			System.out.println("SampleActiveMQ_Queue_Consumer: started");
			
			// Create a connection factory.
			ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory("tcp://localhost:61616");
			System.out.println("SampleActiveMQ_Queue_Consumer: ActiveMQConnectionFactory created");

			// Create connection.
			connection = factory.createConnection();
			System.out.println("SampleActiveMQ_Queue_Consumer: Connection created");

			// Start the connection
			connection.start();
			System.out.println("SampleActiveMQ_Queue_Consumer: Connection started");

			// Create a session which is non transactional
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			System.out.println("SampleActiveMQ_Queue_Consumer: Session created");

			// Create Destination topic
			Topic topic = session.createTopic("SampleActiveMQTopic");
			System.out.println("SampleActiveMQ_Queue_Consumer: Topic created - " + topic);

			// Create a consumer
			MessageConsumer consumer = session.createConsumer(topic);
			System.out.println("SampleActiveMQ_Queue_Consumer: MessageConsumer created");

			System.out.println("SampleActiveMQ_Queue_Consumer: MessageConsumer ready to receive message...");
            Message message = consumer.receive(1000);

            if (message instanceof TextMessage) {
                TextMessage textMessage = (TextMessage) message;
                String msg = textMessage.getText();
                System.out.println("SampleActiveMQ_Queue_Consumer: MessageConsumer received TextMessage - " + msg);
            }

			session.close();
			System.out.println("SampleActiveMQ_Queue_Consumer: Session closed");
			
			connection.close();
			System.out.println("SampleActiveMQ_Queue_Consumer: Connection closed");

		} catch (Exception ex) {
			System.err.println("SampleActiveMQ_Queue_Consumer: Exception!");
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
			
			System.out.println("SampleActiveMQ_Queue_Consumer: stopped");
		}
	}
}