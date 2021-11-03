package com.learning.ddd.onlinestore.order.domain.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learning.ddd.onlinestore.cart.domain.Cart;
import com.learning.ddd.onlinestore.cart.domain.CartItem;
import com.learning.ddd.onlinestore.commons.util.ItemConversionUtil;
import com.learning.ddd.onlinestore.domain.event.pubsub.DomainEventPublisher;
import com.learning.ddd.onlinestore.order.application.dto.SearchOrdersRequestDTO;
import com.learning.ddd.onlinestore.order.domain.Address;
import com.learning.ddd.onlinestore.order.domain.Order;
import com.learning.ddd.onlinestore.order.domain.OrderItem;
import com.learning.ddd.onlinestore.order.domain.OrderTransaction;
import com.learning.ddd.onlinestore.order.domain.event.OrderCancelledEvent;
import com.learning.ddd.onlinestore.order.domain.event.OrderCreatedEvent;
import com.learning.ddd.onlinestore.order.domain.repository.OrderRepository;
import com.learning.ddd.onlinestore.payment.domain.PaymentGateway;
import com.learning.ddd.onlinestore.payment.domain.PaymentMethod;

@Service
public class OrderService {

	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private PaymentGateway paymentGateway;

	@Autowired
	private DomainEventPublisher domainEventPublisher;
	

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
		
		domainEventPublisher.publishEvent(new OrderCreatedEvent(cart, order));
		
		return order;
	}

	private List<OrderItem> convertCartItemsToOrderItems(
			Cart cart, Order order) {
		
		List<OrderItem> orderItems = new ArrayList<OrderItem>();
		for (CartItem cartItem : cart.getItems()) {
			OrderItem orderItem = ItemConversionUtil.fromCartItemToOrderItem(cartItem);
			orderItem.setOrder(order);// set bi-direction (OrderItem -> Order)
			orderItems.add(orderItem);// set bi-direction (Order -> OrderItem)
		}
		
		return orderItems;
	}

	public List<Order> getOrders(String consumerId) {
		return orderRepository.findByConsumerId(consumerId);
	}
	
	public Order getOrder(int orderId) {
		return orderRepository.findById(orderId).get();
	}
	
//	public List<Order> searchOrders(String consumerId, String searchText, Date orderPlacedDate) {
//
//		Object[] objects = orderRepository.findBySearchCriteria(consumerId, searchText, orderPlacedDate);
//		System.out.println(Arrays.asList(objects));
//		return new ArrayList<>();
//	}
	
	public List<Order> searchOrders(SearchOrdersRequestDTO searchOrdersRequestDTO) throws ParseException {
		
		Date purchaseDate = null;
		try { 
			purchaseDate = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a").parse(searchOrdersRequestDTO.getPurchaseDate());	
		} catch (ParseException e) {
			purchaseDate = new Date();	
		}
		
		PaymentMethod paymentMethod = null;
		if ( (searchOrdersRequestDTO.getPaymentMethod() != null) 
				&& !searchOrdersRequestDTO.getPaymentMethod().isEmpty() ) {
			
			PaymentMethod.valueOf(searchOrdersRequestDTO.getPaymentMethod()); 
		}
		
		return orderRepository.findByExample(
			searchOrdersRequestDTO.getOrderNumber(),
			searchOrdersRequestDTO.getTransactionNumber(),
			purchaseDate,
			paymentMethod,
			searchOrdersRequestDTO.getItemDetails(),
			searchOrdersRequestDTO.getAddressDetails()
		);
		
	}

	@Transactional
	public void cancelOrder(String consumerId, String orderNumber) {
		
		System.out.println("--------- cancelOrder(orderNumber): orderNumber = " + orderNumber + " -----------");
		
		Order order = orderRepository.findByOrderNumber(orderNumber);
		
		System.out.println("--------- cancelOrder(orderNumber): order = " + order + " -----------");
		
		OrderCancelledEvent orderCancelledEvent = new OrderCancelledEvent(order.getItems());
		
		orderRepository.deleteByConsumerIdAndOrderNumber(consumerId, orderNumber);
		
		domainEventPublisher.publishEvent(orderCancelledEvent);
	}
	
	@Transactional
	public void cancelOrder(String consumerId, int orderId) {
		
		System.out.println("--------- cancelOrder(orderId): orderId = " + orderId + " -----------");
		
		Order order = orderRepository.findById(orderId).get();
		
		System.out.println("--------- cancelOrder(orderId): order = " + order + " -----------");
		
		OrderCancelledEvent orderCancelledEvent = new OrderCancelledEvent(order.getItems());
		
		orderRepository.deleteById(orderId);
		
		domainEventPublisher.publishEvent(orderCancelledEvent);
	}


}
