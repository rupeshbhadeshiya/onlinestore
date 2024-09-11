package com.learning.ddd.onlinestore.order.domain.service;

import java.util.Date;

import org.springframework.stereotype.Component;

import com.learning.ddd.onlinestore.order.domain.repository.OrderRepository;

@Component
public class CustomThread implements Runnable {

	private String name;
	private String msg;
	private int orderId;
	
	//@Autowired
	private OrderRepository orderRepository;
	
	public CustomThread() {
	}

	public CustomThread(String name, String msg, int orderId, OrderRepository orderRepository) {
		this.name = name;
		this.msg = msg;
		this.orderId = orderId;
		this.orderRepository = orderRepository;
	}
	
	public String getName() {
		return name;
	}
	
	public String getMsg() {
		return msg;
	}
	
	public int getOrderId() {
		return orderId;
	}
	
	@Override
	public void run() {
		synchronized (msg) {
			System.out.println(new Date() + ": CustomThread [name = " + name + ", msg = " + msg + "] started");
			// wait till 500 ms are not over
			// and, mainly, wait till Products availability from Inventory is not known
			int i = 5; 
			while ((i > 0) && !this.orderRepository.existsProductsAvailableInInventory(orderId)){
				
				//System.out.println("~~~~~~~~~~ Remaining: " + i + " seconds");
				
				try {
					i--;
				   	Thread.sleep(100L);    // 1000L = 1000ms = 1 second
				} catch (InterruptedException e) {
					e.printStackTrace();	
					//I don't think you need to do anything for your particular problem
				}
			}
			
			msg.notify();
			
			System.out.println(new Date() + ": CustomThread [name = " + name + ", msg = " + msg + "] completed");				
		}
	}

}
