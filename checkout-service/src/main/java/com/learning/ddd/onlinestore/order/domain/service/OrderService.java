package com.learning.ddd.onlinestore.order.domain.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learning.ddd.onlinestore.cart.domain.Cart;
import com.learning.ddd.onlinestore.cart.domain.CartItem;
import com.learning.ddd.onlinestore.commons.domain.event.DomainEventsPublisher;
import com.learning.ddd.onlinestore.order.domain.Address;
import com.learning.ddd.onlinestore.order.domain.Order;
import com.learning.ddd.onlinestore.order.domain.OrderItem;
import com.learning.ddd.onlinestore.order.domain.OrderTransaction;
import com.learning.ddd.onlinestore.order.domain.event.OrderCreatedEvent;
import com.learning.ddd.onlinestore.order.domain.repository.OrderRepository;
import com.learning.ddd.onlinestore.payment.domain.PaymentGateway;
import com.learning.ddd.onlinestore.payment.domain.PaymentMethod;

@Service
public class OrderService {

	@Autowired
	private OrderRepository orderRepository;
	
	//@Autowired
	//private TransactionRepository transactionRepository;
	
	@Autowired
	private PaymentGateway paymentGateway;

	@Autowired
	private DomainEventsPublisher domainEventsPublisher;
	

	@Transactional
	public Order createOrderAndProcessPayment(
			Cart cart, PaymentMethod paymentMethod,
			Address billingAddress, Address shippingAddress) {
		
		// create an Order
		
		Order order = new Order();
		
		// IMP: this is good step, you create new OrderItem from given CartItem
		// if you would be using OrderItem from request as is (say if it was in request)
		// then you would have faced an error 'detached entity passed to persist
		order.setItems(convertCartItemsToOrderItems(cart, order));
		order.setConsumerId(cart.getConsumerId());
		order.setItemCount(cart.getItemCount());
		order.setAmount(cart.computeAmount());
		order.setPaymentMethod(paymentMethod);
		order.setBillingAddress(billingAddress);
		order.setShippingAddress(shippingAddress);
		
		
		// process the Payment
		OrderTransaction transactionReceipt = paymentGateway.doPayment(order);
		
		// and persist the Order as well as Order-Transaction association
		
		order.addTransaction(transactionReceipt);
		
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
		
		//orderRepository.
		
		return orderRepository.findByConsumerId(consumerId);
	}
	
//	public List<Order> searchOrders(String consumerId, String searchText, Date orderPlacedDate) {
//
//		Object[] objects = orderRepository.findBySearchCriteria(consumerId, searchText, orderPlacedDate);
//		System.out.println(Arrays.asList(objects));
//		return new ArrayList<>();
//	}

	@Transactional
	public void deleteOrder(String consumerId, String orderNumber) {
		
		orderRepository.deleteByConsumerIdAndOrderNumber(consumerId, orderNumber);
	}

}
