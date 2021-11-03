package com.learning.ddd.onlinestore.domain.event.pubsub.activemq;

public class Test_SampleActiveMQ_Topic {

    public static void main(String[] args) {
    	
    	SampleActiveMQ_Topic_Producer producer = new SampleActiveMQ_Topic_Producer();
        SampleActiveMQ_Topic_Consumer consumer = new SampleActiveMQ_Topic_Consumer();
 
        Thread producerThread = new Thread(producer);
        producerThread.start();
 
        Thread consumerThread = new Thread(consumer);
        consumerThread.start();
        
    }
}