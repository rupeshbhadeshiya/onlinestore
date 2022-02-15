package com.learning.ddd.onlinestore.domain.event.pubsub;

import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.beans.factory.annotation.Autowired;

import com.learning.ddd.onlinestore.domain.event.DomainEvent;

//@Component (no need for this bean to be a @Component, as extending bean will be anyway declaring itself a @Component)
public abstract class DomainEventsConsumer implements ServletContextListener {
	
	
	@Autowired
	private DomainEventsReader domainEventsReader; // must keep under @Component otherwise bean binding won't happen
	
	// worker must be a separate daemon Thread, which keeps listening...
	// as it gets message it spawns a consumerJob to process it
	// similarly, consumerJob should each be a separate Thread to execute received messages rapidly at nearby same time
	private DomainEventsReadingAndConsumingWorker domainEventsListeningWorker 
		= new DomainEventsReadingAndConsumingWorker();
	
	
	// ServletContextListener implementation - handle application start-time activity
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		
		System.out.println(getCallingServiceName() + ": contextInitialized() - started");
		
		domainEventsListeningWorker.start();
		
		System.out.println(getCallingServiceName() + ": contextInitialized() - completed");
	}
	
	// ServletContextListener implementation - handle application stop-time activity
	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		
		System.out.println(getCallingServiceName() + ": contextDestroyed() - started");
		
		domainEventsListeningWorker.stopWork();
		
		System.out.println(getCallingServiceName() + ": contextDestroyed() - completed");
	}
	
	
	
	/* Consumer specific implementation to be provided by the class extending this one */
	protected abstract void consumeDomainEvent(DomainEvent domainEvent) throws CloneNotSupportedException, JMSException;
	
	/* Consumer to provide name of Topic where this listener listens to */
	protected abstract String getTopicName();
	
	/* Consumer to provide name (e.g. cart-service: OrderEventsConsumer */
	protected abstract String getCallingServiceName();
	
	
	
	// ------------- DomainEventsReadingAndConsumingWorker -----------------
	
	public class DomainEventsReadingAndConsumingWorker extends Thread {
		
		
		private boolean isDomainEventsListenerRunning;
		private boolean isShutdownHookTriggered;


		@Override
		public void run() {
			
			System.out.println(getCallingServiceName() + " - DomainEventsReadingAndConsumingWorker: worker has started, trying to connect to Topic ["+getTopicName()+"]...");
			
			domainEventsReader.connect(getTopicName(), getCallingServiceName());
			
			System.out.println(getCallingServiceName() + " - DomainEventsReadingAndConsumingWorker: now listening to Topic ["+getTopicName()+"]...");
			
			while (!isShutdownHookTriggered) {
				
				isDomainEventsListenerRunning = true;
				try {
					
					ObjectMessage domainEvent = domainEventsReader.read();
					
					if (domainEvent != null) {
						
						DomainEventsConsumptionJob consumer = new DomainEventsConsumptionJob(domainEvent);
						new Thread(consumer).start();
						
					}
					
				} catch (JMSException e) {
					
					isDomainEventsListenerRunning = false;
					
					break; // exit the while loop
				}
			}
			
			isDomainEventsListenerRunning = false;
			System.out.println(getCallingServiceName() + " - DomainEventsReadingAndConsumingWorker: "
					+ "stopped listening to Topic ["+getTopicName()+"]");
			
		} // run() method ends


		public void stopWork() { // since extending Thread, can't name method like stop/shutdown etc so the custom name :)
			
			System.out.println(getCallingServiceName() + " - DomainEventsReadingAndConsumingWorker: "
					+ "stopping listening to Topic ["+getTopicName()+"]...");

			domainEventsListeningWorker.isShutdownHookTriggered = true;
			
			while (domainEventsListeningWorker.isDomainEventsListenerRunning) {
				// wait();
			}
			
			domainEventsReader.disconnect();
			
			System.out.println(getCallingServiceName() + " - DomainEventsReadingAndConsumingWorker: worker is now stopped. "
					+ "Disconnected from Topic ["+getTopicName()+"]...");
		}
		
		
	} // DomainEventsReadingAndConsumingWorker - ends ---------------------
	
	
	// ------------- DomainEventsConsumptionJob -----------------
	
	public class DomainEventsConsumptionJob implements Runnable {
		
		private ObjectMessage message;

		public DomainEventsConsumptionJob(ObjectMessage message) {
			this.message = message;
		}

		public void run() {
			
			System.out.println(getCallingServiceName() + " - DomainEventsConsumptionJob: started processing message - " + message + ", Topic = " + getTopicName());
			
			// FIXME Write proper handling of this once this basic validation is done
			try {
				DomainEvent domainEvent = (DomainEvent) message.getObject();
				System.out.println(getCallingServiceName() + " - DomainEventsConsumptionJob: received Domain Event: " + domainEvent + ", Topic = " + getTopicName());
				
				consumeDomainEvent(domainEvent);
				
				System.out.println(getCallingServiceName() + " - DomainEventsConsumptionJob: processed message - " + message + ", Topic = " + getTopicName());
				
			} catch (JMSException e) {
				System.err.println(getCallingServiceName() + " - DomainEventsConsumptionJob: error while receiving message from Topic " + getTopicName());
				e.printStackTrace();
			} catch (CloneNotSupportedException e) {
				System.err.println(getCallingServiceName() + " - DomainEventsConsumptionJob: error while consuming message received from Topic " + getTopicName());
				e.printStackTrace();
			}
			
			System.out.println(getCallingServiceName() + " - DomainEventsConsumptionJob: completed processing message - " + message + ", Topic = " + getTopicName());
		}
		
	} // DomainEventsConsumptionJob - ends ---------------------



}
