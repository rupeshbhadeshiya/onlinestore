package com.learning.ddd.onlinestore.domain.event.pubsub.activemq;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.Topic;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.learning.ddd.onlinestore.domain.event.DomainEvent;
import com.learning.ddd.onlinestore.domain.event.pubsub.DomainEventConsumer;

@Component
public class ActiveMQMessagesConsumptionHandler implements ServletContextListener {

	@Autowired
	private DomainEventConsumer domainEventConsumer;
	
	private ActiveMQMessagesConsumer activeMQMessagesConsumer;
	
	private ActiveMQMessagesListener activeMQMessagesListener;
	
	@Value("${onlinestore.domain.events.topic.name:OnlinestoreDomainEventsTopic}")
	private String topicName;

	
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		
		System.out.println("ActiveMQMessagesConsumptionHandler: contextInitialized() - started");
		
		activeMQMessagesConsumer = new ActiveMQMessagesConsumer();
		activeMQMessagesConsumer.init();
		
		activeMQMessagesListener = new ActiveMQMessagesListener(false);
		new Thread(activeMQMessagesListener).start();
		
		System.out.println("ActiveMQMessagesConsumptionHandler: contextInitialized() - completed");
	}
	
	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		
		System.out.println("ActiveMQMessagesConsumptionHandler: contextDestroyed() - started");
		
		activeMQMessagesListener.setShutdownHookTriggered(true);
		
		while (activeMQMessagesListener.isActiveMQMessagesListenerRunning()) {
			// wait();
		}
		
		activeMQMessagesConsumer.shutdown();
		
		System.out.println("ActiveMQMessagesConsumptionHandler: contextDestroyed() - completed");
	}
	
	
	public class ActiveMQMessagesConsumer {
		
		private Connection connection = null;
		// FIXME Should session be opened for each new message sending or just one session
		//			to be used for lifetime (like done right now) - which is the correct way?
		private Session session = null;
		private MessageConsumer consumer = null;
		
		public MessageConsumer getConsumer() {
			return consumer;
		}
		
		public void init() {
			System.out.println("ActiveMQMessagesConsumer: init() - started");
			
			try {
				
				// Create a connection factory.
				ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory("tcp://localhost:61616");
				System.out.println("ActiveMQMessagesConsumer: init() - ActiveMQConnectionFactory created");

				// set necessary properties
				factory.setTrustAllPackages(true);
				
				// Create connection.
				connection = factory.createConnection();
				System.out.println("ActiveMQMessagesConsumer: init() - Connection created");

				// Start the connection
				connection.start();
				System.out.println("ActiveMQMessagesConsumer: init() - Connection started");

				// Create a session which is non transactional
				session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
				System.out.println("ActiveMQMessagesConsumer: init() - Session created");

				// Create Destination topic
				Topic topic = session.createTopic(topicName);
				System.out.println("ActiveMQMessagesConsumer: init() - Topic created - " + topic);

				// Create a consumer
				consumer = session.createConsumer(topic);
				System.out.println("ActiveMQMessagesConsumer: init() - MessageConsumer created");

			} catch (Exception ex) {
				System.err.println("ActiveMQMessagesConsumer: init() - Exception while creating Connection/Session/MessageProducer!");
				ex.printStackTrace();
			}
				
			System.out.println("ActiveMQMessagesConsumer: contextInitialized() - completed");
		} // method ends
		
		public ObjectMessage receiveMessage() throws JMSException {
			
			//System.out.println("ActiveMQMessagesConsumer: MessageConsumer started listening to receive message...");
			
			try { 
				
	            Message message = consumer.receive(1000);
	            
	            if ((message != null) && !(message instanceof ObjectMessage)) {
	            	System.err.println("ActiveMQMessagesConsumer: received Not an instance of ObjectMessage! - " + message);
	            	throw new RuntimeException("Not an instance of ObjectMessage! " + message);
	            }
	           
	            return (ObjectMessage) message;
	            
			} catch (JMSException ex) {
				System.err.println("ActiveMQMessagesConsumer: Exception while receiving a message!");
				ex.printStackTrace();
				throw ex;
				
			} finally {
				
				//System.out.println("ActiveMQMessagesConsumer: MessageConsumer stopped listening to receive message");
			}
			
		} // method ends
		
		public void shutdown() {
			System.out.println("ActiveMQMessagesConsumer: shutdown() - started");
			
			try {
				
				if (session != null) {
					session.close();
					System.out.println("ActiveMQMessagesConsumer: shutdown() - Session closed");
				}
				
			} catch (Exception ex) {
				System.err.println("ActiveMQMessagesConsumer: shutdown() - Exception while closing Session!");
				ex.printStackTrace();
			}
			
			try {
				
				if (connection != null) {
					connection.close();
					System.out.println("ActiveMQMessagesConsumer: shutdown() - Connection closed");
				}
				
			} catch (Exception ex) {
				System.err.println("ActiveMQMessagesConsumer: shutdown() - Exception while closing Connection!");
				ex.printStackTrace();
			}
			
			System.out.println("ActiveMQMessagesConsumer: shutdown() - completed");
		} // method ends
		
	} // class ends
	
	
	public class ActiveMQMessagesListener implements Runnable {
		
		private boolean isActiveMQMessagesListenerRunning;
		private boolean isShutdownHookTriggered;
		
		public ActiveMQMessagesListener(boolean isShutdownHookTriggered) {
			this.isShutdownHookTriggered = isShutdownHookTriggered;
		}

		public void run() {
			
			System.out.println("ActiveMQMessagesListener: started listening to receive message...");
			
			while (!isShutdownHookTriggered) {
				isActiveMQMessagesListenerRunning = true;
				try {
					ObjectMessage message = activeMQMessagesConsumer.receiveMessage();
					if (message != null) {
						new Thread(new ActiveMQMessagesProcessor(message)).start();
					}
				} catch (JMSException e) {
					isActiveMQMessagesListenerRunning = false;
					break; // exit while loop
				}
			}
			
			isActiveMQMessagesListenerRunning = false;
			System.out.println("ActiveMQMessagesListener: completed listening to receive message");
		}
		
		public boolean isActiveMQMessagesListenerRunning() {
			return isActiveMQMessagesListenerRunning;
		}
		public void setShutdownHookTriggered(boolean isShutdownHookTriggered) {
			this.isShutdownHookTriggered = isShutdownHookTriggered;
		}
		
	} // class ends
	
	
	public class ActiveMQMessagesProcessor implements Runnable {
		
		private ObjectMessage message;

		public ActiveMQMessagesProcessor(ObjectMessage message) {
			this.message = message;
		}

		public void run() {
			
			System.out.println("ActiveMQMessagesProcessor: started processing message - " + message);
			
			// FIXME Write proper handling of this once this basic validation is done
			try {
				DomainEvent domainEvent = (DomainEvent) message.getObject();
				System.out.println("ActiveMQMessagesProcessor: received Domain Event:  ~~~~~~~~~~~> " + domainEvent);
				
				domainEventConsumer.consumeEvent(domainEvent);
				System.out.println("ActiveMQMessagesProcessor: processed ~~~~~~~~~~~> " +message);
				
			} catch (JMSException e) {
				e.printStackTrace();
			}
			
			System.out.println("ActiveMQMessagesProcessor: completed processing message - " + message);
		}
		
	} // class ends
	
}
