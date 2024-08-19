package com.learning.ddd.onlinestore.order.domain.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.jms.JMSException;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learning.ddd.onlinestore.cart.application.dto.CartInfo;
import com.learning.ddd.onlinestore.cart.proxy.CartServiceRestTemplateBasedProxy;
import com.learning.ddd.onlinestore.inventory.domain.Product;
import com.learning.ddd.onlinestore.order.application.dto.SearchOrdersRequestDTO;
import com.learning.ddd.onlinestore.order.domain.Address;
import com.learning.ddd.onlinestore.order.domain.Order;
import com.learning.ddd.onlinestore.order.domain.OrderItem;
import com.learning.ddd.onlinestore.order.domain.OrderTransaction;
import com.learning.ddd.onlinestore.order.domain.event.OrderCancelledEvent;
import com.learning.ddd.onlinestore.order.domain.event.OrderCreatedEvent;
import com.learning.ddd.onlinestore.order.domain.event.pubsub.OrderEventsProducer;
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
	private OrderEventsProducer orderEventsProducer;
	
	@Autowired
	private CartServiceRestTemplateBasedProxy cartServiceProxy;
	

	@Transactional
	public Order createOrderAndProcessPayment(
			//Cart cart,
			int cartId,
			PaymentMethod paymentMethod,
			Address billingAddress, Address shippingAddress) throws JMSException {
		
		System.out.println(
			"OrderService: createOrderAndProcessPayment() - started; "
			+ "cartId="+cartId+", paymentMethod="+paymentMethod
			+ ", billingAddress="+billingAddress+", shippingAddress="+shippingAddress);
		
		
		// FIXME Asynchronously talk to Cart Service to retrieve Cart details
		CartInfo CartInfo = cartServiceProxy.getCartInfo(cartId);

//		// First look in local store to find Cart for given cartId
//		Optional<Cart> cartInDB = cartRepository.findById(cartId);
//		
//		if (cartInDB.isPresent()) {
//		
//			cart = cartInDB.get();
//			
//			System.out.println(
//				"OrderService: createOrderAndProcessPayment() - Found Cart in local DB; "
//				+ "cartId="+cartId + ", cart="+cart);
//			
//		} else { // try calling cart-service directly to confirm that Indeed the Cart does not exist
//			
//			System.out.println(
//				"OrderService: createOrderAndProcessPayment() - Did not found the Cart in local DB; "
//				+ " cartId="+cartId + ", so trying to get it from cart-service");
//			
//			cart = cartServiceProxy.getCart(cartId);
//			
//			if (cart == null) {
//				System.err.println(
//					"ERROR - OrderService: createOrderAndProcessPayment() - Did not found the Cart in cart-service too!! "
//					+ " cartId="+cartId + ", looks like a Cart which does not exist!"
//				);
//				throw new CartNotFoundException(cartId);
//			}
//			System.out.println(
//				"OrderService: createOrderAndProcessPayment() - Found out the Cart in cart-service; "
//				+ "cartId="+cartId + ", cart="+cart);
//		}
		
		// create an Order
		
		Order order = new Order();
		
		// IMP: this is good step, you create new OrderItem from given CartItem
		// if you would be using OrderItem from request as is (say if it was in request)
		// then you would have faced an error 'detached entity passed to persist
		order.setCartId(cartId);
		order.setItems(convertCartProductsToOrderItems(CartInfo, order));
		order.setConsumerId(CartInfo.getConsumerId());
		order.setItemCount(CartInfo.getProductCount());
		order.setAmount(CartInfo.computeAmount());
		order.setPaymentMethod(paymentMethod);
		order.setBillingAddress(billingAddress);
		order.setShippingAddress(shippingAddress);
		
		
		// process the Payment
		
		OrderTransaction transactionReceipt = paymentGateway.doPayment(order);
		
		// ... and persist the Order as well as Order-Transaction association
		
		order.addTransaction(transactionReceipt);
		
		order = orderRepository.save(order);
		
		System.out.println(
			"OrderService: createOrderAndProcessPayment() - Order created; "
			+ "order="+order
			+ "cartId="+cartId);

		// ... and publish the change as a domain event
		
		OrderCreatedEvent event = new OrderCreatedEvent(order.getOrderInfo(), CartInfo);
		orderEventsProducer.publishDomainEvent(event);
		
		return order;
	}

	private List<OrderItem> convertCartProductsToOrderItems(
			CartInfo CartInfo, Order order) {
		
		List<OrderItem> orderItems = new ArrayList<OrderItem>();
		for (Product productInCart : CartInfo.getProducts()) {
			OrderItem orderItem = new OrderItem(productInCart);
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
	public void cancelOrder(String consumerId, String orderNumber) throws CloneNotSupportedException, JMSException {
		
		System.out.println(
			"OrderService: cancelOrder() - Going to cancel Order; "
			+ "consumerId="+consumerId
			+ "orderNumber="+orderNumber);
		
		Order order = orderRepository.findByOrderNumber(orderNumber);
		Order copyOfOrderToBeCancelled = order.clone();
		
		System.out.println("--------- cancelOrder(orderNumber): order = " + order + " -----------");
		
		orderRepository.deleteByConsumerIdAndOrderNumber(consumerId, orderNumber);
		
		System.out.println(
			"OrderService: cancelOrder() - Cancelled the Order; "
			+ "consumerId="+consumerId
			+ "orderNumber="+orderNumber);
		
		OrderCancelledEvent event = new OrderCancelledEvent(copyOfOrderToBeCancelled.getOrderInfo());
		orderEventsProducer.publishDomainEvent(event);
	}
	
	@Transactional
	public void cancelOrder(String consumerId, int orderId) throws CloneNotSupportedException, JMSException {
		
		System.out.println("--------- cancelOrder(orderId): orderId = " + orderId + " -----------");
		
		Order order = orderRepository.findById(orderId).get();
		
		System.out.println("--------- cancelOrder(orderId): order = " + order + " -----------");
		
		Order copyOfOrderToBeCancelled = order.clone();
		
		System.out.println("--------- cancelOrder(orderNumber): order = " + order + " -----------");
		
		orderRepository.deleteById(orderId);
		
		OrderCancelledEvent event = new OrderCancelledEvent(copyOfOrderToBeCancelled.getOrderInfo());
		orderEventsProducer.publishDomainEvent(event);
	}


}
