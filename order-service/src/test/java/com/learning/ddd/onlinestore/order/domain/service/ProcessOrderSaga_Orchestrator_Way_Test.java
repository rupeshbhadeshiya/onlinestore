package com.learning.ddd.onlinestore.order.domain.service;

import java.util.Date;

import org.junit.jupiter.api.Test;


public class ProcessOrderSaga_Orchestrator_Way_Test {

//	
//	order-service --> order-service: Place Order
//	<< code executing in main thread >>
//	
//	order-service --> inventory-service: ASYNC Prod Avail
//	inventory-service --> order-service: prod avail status
//	<< main thread spawns Thread1... main thread waits till Thread1 is over >>
//	
//	if (product not available) { return Order = Rejected }
//	<< code executing in main thread >>
//	
//	/* if (inventory-service not available) { retry 3 times } */ //this is hard to do
//	<< main thread spawns Thread1... main thread waits till Thread1 is over >>
//	<< main thread spawns Thread1... main thread waits till Thread1 is over >>
//	<< main thread spawns Thread1... main thread waits till Thread1 is over >>		
//	
//	if (product available) {
//		order-service --> payment-service: ASYNC Payment
//		payment-service --> order-service: txn receipt / err
//	}
//	<< main thread spawns Thread2... main thread waits till Thread2 is over >>
//	
//	[ order-service ]
//	if (txn failed) { return Order = Rejected }
//	<< code executing in main thread >>
//	
//	if (payment-service not available) { retry 3 times }
//	<< main thread spawns Thread2... main thread waits till Thread2 is over >>
//	<< main thread spawns Thread2... main thread waits till Thread2 is over >>
//	<< main thread spawns Thread2... main thread waits till Thread2 is over >>				
//		
//	if (txn receipt i.e. txn successful) {
//		order-service --> order-service: Confirm Order
//	}
//	<< code executing in main thread >>
//		
//	order-service --> web-app: Return final status / details of Order
//	<< code executing in main thread >>
//

	@Test
	void test() {
		
		final String msg = "notifying_piece";
		synchronized (msg) {
			System.out.println(new Date() + ": Main Thread: starting Thread1");
			new Thread(new CustomThread("Thread1", msg)).start();
			try {
				System.out.println(new Date() + ": Main Thread: waiting on msg for 100ms");
				msg.wait(100L);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println(new Date() + ": Main Thread: notified on msg");
		}
		
		final String msg2 = "notifying_piece2";
		synchronized (msg2) {
			System.out.println(new Date() + ": Main Thread: starting Thread2");
			new Thread(new CustomThread("Thread2", msg2)).start();
			try {
				System.out.println(new Date() + ": Main Thread: waiting on msg2 for 100ms");
				msg2.wait(100L);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}			
			System.out.println(new Date() + ": Main Thread: notified on msg2");
		}
	
	}
	
	class CustomThread implements Runnable {

		private String name;
		private String msg;

		public CustomThread(String name, String msg) {
			this.name = name;
			this.msg = msg;
		}
		
		public String getName() {
			return name;
		}
		
		public String getMsg() {
			return msg;
		}
		
		@Override
		public void run() {
			synchronized (msg) {
				System.out.println(new Date() + ": CustomThread [name = " + name + ", msg = " + msg + "] started");
				try {
					Thread.sleep(50L);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				msg.notify();
				System.out.println(new Date() + ": CustomThread [name = " + name + ", msg = " + msg + "] completed");				
			}
		}
		
	}

}
