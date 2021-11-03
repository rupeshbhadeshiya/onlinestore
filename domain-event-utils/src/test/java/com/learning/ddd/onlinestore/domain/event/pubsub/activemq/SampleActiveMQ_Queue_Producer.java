package com.learning.ddd.onlinestore.domain.event.pubsub.activemq;

import java.util.Date;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

public class SampleActiveMQ_Queue_Producer implements Runnable {

	public void run() {
		
		try { 
			System.out.println("SampleActiveMQ_Queue_Producer: started");
			
			// Create a connection factory.
			ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory("tcp://localhost:61616");
			System.out.println("SampleActiveMQ_Queue_Producer: ActiveMQConnectionFactory created");

			// Create connection.
			Connection connection = factory.createConnection();
			System.out.println("SampleActiveMQ_Queue_Producer: Connection created");

			// Start the connection
			connection.start();
			System.out.println("SampleActiveMQ_Queue_Producer: Connection started");

			// Create a session which is non transactional
			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			System.out.println("SampleActiveMQ_Queue_Producer: Session created");

			// Create Destination queue
			Queue queue = session.createQueue("SampleActiveMQQueue");
			System.out.println("SampleActiveMQ_Queue_Producer: Queue created - " + queue);

			// Create a producer
			MessageProducer producer = session.createProducer(queue);
			System.out.println("SampleActiveMQ_Queue_Producer: MessageProducer created");

			producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

			String msg = "Hello World! Today is " + new Date();

			// insert message
			System.out.println("SampleActiveMQ_Queue_Producer: MessageProducer sending message - " + msg);
			TextMessage message = session.createTextMessage(msg);
			
			producer.send(message);
			System.out.println("SampleActiveMQ_Queue_Producer: MessageProducer sent TextMessage - " + msg);

			session.close();
			System.out.println("SampleActiveMQ_Queue_Producer: Session closed");
			
			connection.close();
			System.out.println("SampleActiveMQ_Queue_Producer: Connection closed");

		} catch (Exception ex) {
			System.err.println("SampleActiveMQ_Queue_Producer: Exception!");
			ex.printStackTrace();
			
		} finally {
			System.out.println("SampleActiveMQ_Queue_Producer: stopped");
		}
	}
}