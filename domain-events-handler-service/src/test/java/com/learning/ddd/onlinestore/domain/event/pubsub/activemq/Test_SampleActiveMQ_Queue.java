package com.learning.ddd.onlinestore.domain.event.pubsub.activemq;

public class Test_SampleActiveMQ_Queue {

    public static void main(String[] args) {
    	
        SampleActiveMQ_Queue_Producer producer = new SampleActiveMQ_Queue_Producer();
        SampleActiveMQ_Queue_Consumer consumer = new SampleActiveMQ_Queue_Consumer();
 
        Thread producerThread = new Thread(producer);
        producerThread.start();
 
        Thread consumerThread = new Thread(consumer);
        consumerThread.start();
        
    }
}