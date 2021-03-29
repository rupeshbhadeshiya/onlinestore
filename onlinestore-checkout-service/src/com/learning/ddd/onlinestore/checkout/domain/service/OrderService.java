package com.learning.ddd.onlinestore.checkout.domain.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learning.ddd.onlinestore.checkout.domain.Address;
import com.learning.ddd.onlinestore.checkout.domain.Cart;
import com.learning.ddd.onlinestore.checkout.domain.CartItem;
import com.learning.ddd.onlinestore.checkout.domain.Order;
import com.learning.ddd.onlinestore.checkout.domain.OrderItem;
import com.learning.ddd.onlinestore.checkout.domain.repository.OrderRepository;
import com.learning.ddd.onlinestore.payment.domain.PaymentMethod;

@Service
public class OrderService {

	@Autowired
	private OrderRepository orderRepository;

	public Order createOrder(Cart cart, PaymentMethod paymentMethod,
								Address billingAddress, Address shipingAddress) {
		
		Order order = new Order();
		
		order.setConsumerId(cart.getConsumerId());
		order.setItems(convertToOrderItems(order, cart.getItems()));
		order.setItemCount(cart.getItemCount());
		order.setPaymentMethod(paymentMethod);
		order.setBillingAddress(billingAddress);
		order.setShippingAddress(shipingAddress);
		
		order = orderRepository.save(order);
		
		return order;
	}

	private List<OrderItem> convertToOrderItems(Order order, List<CartItem> cartItems) {
		List<OrderItem> orderItems = new ArrayList<OrderItem>();
		for (CartItem cartItem : cartItems) {
			OrderItem orderItem = new OrderItem(
				cartItem.getCategory(), cartItem.getSubCategory(), 
				cartItem.getName(), cartItem.getQuantity(), cartItem.getPrice());
			orderItem.setOrder(order);	// set bi-directional (this is one direction)
			orderItems.add(orderItem);	// set bi-directional (this is other direction)
		}
		return orderItems;
	}

	public List<Order> getOrders(String consumerId) {
		return orderRepository.findByConsumerId(consumerId);
	}

}
