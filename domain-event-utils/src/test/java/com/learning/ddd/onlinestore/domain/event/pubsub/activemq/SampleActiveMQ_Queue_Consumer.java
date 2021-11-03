package com.learning.ddd.onlinestore.domain.event.pubsub.activemq;

import javax.jms.Connection;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

public class SampleActiveMQ_Queue_Consumer implements Runnable {

	public void run() {
		
		try { 
			System.out.println("SampleActiveMQ_Queue_Consumer: started");
			
			// Create a connection factory.
			ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory("tcp://localhost:61616");
			System.out.println("SampleActiveMQ_Queue_Consumer: ActiveMQConnectionFactory created");

			// Create connection.
			Connection connection = factory.createConnection();
			System.out.println("SampleActiveMQ_Queue_Consumer: Connection created");

			// Start the connection
			connection.start();
			System.out.println("SampleActiveMQ_Queue_Consumer: Connection started");

			// Create a session which is non transactional
			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			System.out.println("SampleActiveMQ_Queue_Consumer: Session created");

			// Create Destination queue
			Queue queue = session.createQueue("SampleActiveMQQueue");
			System.out.println("SampleActiveMQ_Queue_Consumer: Queue created - " + queue);

			// Create a consumer
			MessageConsumer consumer = session.createConsumer(queue);
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
			System.out.println("SampleActiveMQ_Queue_Consumer: stopped");
		}
	}
}