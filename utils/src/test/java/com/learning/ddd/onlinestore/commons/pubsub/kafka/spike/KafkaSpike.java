package com.learning.ddd.onlinestore.commons.pubsub.kafka.spike;

public class KafkaSpike {	

	public static void main(String[] args) {
		TestKafkaProducer p = new TestKafkaProducer();
		TestKafkaConsumer c = new TestKafkaConsumer();

		p.produce();
		c.consume();
	}
	
//	static void main2(String[] args) {
//		
//		Properties props = new Properties();
//        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "onlinestore");
//        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
//        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
//        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
//
//        final StreamsBuilder builder = new StreamsBuilder();
//
//        builder.stream("onlinestore-inventory-events"); //.to("streams-pipe-output");
//
//        final Topology topology = builder.build();
//
//        final KafkaStreams streams = new KafkaStreams(topology, props);
//        final CountDownLatch latch = new CountDownLatch(1);
//
//        // attach shutdown handler to catch control-c
//        Runtime.getRuntime().addShutdownHook(new Thread("onlinestore-streams-shutdown-hook") {
//            @Override
//            public void run() {
//                streams.close();
//                latch.countDown();
//            }
//        });
//
//        try {
//            streams.start();
//            latch.await();
//        } catch (Throwable e) {
//            System.exit(1);
//        }
//        System.exit(0);
//	}
	
}
