package com.learning.ddd.onlinestore.order.domain.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learning.ddd.onlinestore.commons.domain.event.DomainEventsPublisher;
import com.learning.ddd.onlinestore.order.domain.Address;
import com.learning.ddd.onlinestore.order.domain.Order;
import com.learning.ddd.onlinestore.order.domain.OrderItem;
import com.learning.ddd.onlinestore.order.domain.event.OrderCreatedEvent;
import com.learning.ddd.onlinestore.order.domain.repository.OrderRepository;
import com.learning.ddd.onlinestore.payment.domain.PaymentMethod;
import com.learning.ddd.onlinestore.cart.domain.Cart;
import com.learning.ddd.onlinestore.cart.domain.CartItem;

@Service
public class OrderService {

	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private DomainEventsPublisher domainEventsPublisher;

	public Order createOrder(Cart cart, PaymentMethod paymentMethod,
			Address billingAddress, Address shipingAddress) {
		
		Order order = new Order();
		
		order.setConsumerId(cart.getConsumerId());
		order.setItems( convertCartItemsToOrderItems(cart, order) );
		order.setItemCount(cart.getItemCount());
		order.setPaymentMethod(paymentMethod);
		order.setBillingAddress(billingAddress);
		order.setShippingAddress(shipingAddress);
		
		order = orderRepository.save(order);
		
		domainEventsPublisher.publishEvent(new OrderCreatedEvent(order));
		
		return order;
	}

	private List<OrderItem> convertCartItemsToOrderItems(
			Cart cart, Order order) {
		
		List<OrderItem> orderItems = new ArrayList<OrderItem>();
		for (CartItem cartItem : cart.getItems()) {
			OrderItem orderItem = new OrderItem(
				cartItem.getCategory(), 
				cartItem.getSubCategory(), 
				cartItem.getName(), 
				cartItem.getQuantity(), 
				cartItem.getPrice()
			);
			orderItem.setOrder(order);// set bi-direction (OrderItem -> Order)
			orderItems.add(orderItem);// set bi-direction (Order -> OrderItem)
		}
		
		return orderItems;
	}

	public List<Order> getOrders(String consumerId) {
		
		return orderRepository.findByConsumerId(consumerId);
	}

}
