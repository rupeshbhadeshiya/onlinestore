package com.learning.ddd.onlinestore.order.proxy;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.learning.ddd.onlinestore.order.application.dto.CreateOrderDTO;
import com.learning.ddd.onlinestore.order.application.dto.SearchOrdersRequestDTO;
import com.learning.ddd.onlinestore.order.application.dto.SearchOrdersResponseDTO;
import com.learning.ddd.onlinestore.order.domain.Order;
import com.learning.ddd.onlinestore.payment.domain.PaymentMethod;

@Component
public class OrderServiceRestTemplateBasedProxy {

	// FIXME Once user mgmt is in place replace this with actual/runtime value
	private static final String CONSUMER_ID = "11";
	
	@Autowired
	private RestTemplate orderServiceRestTemplate;
	
//	@Bean(name = "orderServiceRestTemplate")
//    @LoadBalanced
//    public RestTemplate restTemplate() {
//        return new RestTemplate();
//    }
	
	
//	public Cart pullCartAndAddItem(InventoryItem inventoryItem) {
//		
//		PullCartDTO dto = new PullCartDTO(CONSUMER_ID);
//		dto.addItem(ItemsConversionUtil.fromInventoryItemToCartItem(inventoryItem));
//		
//		HttpEntity<PullCartDTO> request = new HttpEntity<PullCartDTO>(dto);
//		
//		Cart cart = cartServiceRestTemplate.exchange(
//			"http://cart-service/consumers/" + CONSUMER_ID + "/carts", 
//			HttpMethod.POST,
//			request,
//			new ParameterizedTypeReference<Cart>() {}
//		).getBody();
//		
//		return cart;
//	}
	
	public List<PaymentMethod> getSupportedPaymentMethods() {
		return Arrays.asList(PaymentMethod.values());
	}
	
//	public Order checkout(Cart cart, Order orderRequestData) {
//		
//		CreateOrderDTO dto = new CreateOrderDTO(CONSUMER_ID);
//		dto.setConsumerId(cart.getConsumerId());
//		dto.setCart(cart);
//		dto.setBillingAddress(orderRequestData.getBillingAddress());
//		dto.setShippingAddress(orderRequestData.getShippingAddress());
//		dto.setPaymentMethod(orderRequestData.getPaymentMethod());
//		
//		HttpEntity<CreateOrderDTO> request = new HttpEntity<CreateOrderDTO>(dto);
//		
//		Order order = orderServiceRestTemplate.exchange(
//			"http://order-service/consumers/" + CONSUMER_ID + "/orders", 
//			HttpMethod.POST,
//			request,
//			new ParameterizedTypeReference<Order>() {}
//		).getBody();
//		
//		return order;
//	}
	
	public Order checkout(int cartId, Order orderRequestData) {
		
		CreateOrderDTO dto = new CreateOrderDTO(CONSUMER_ID);
		dto.setConsumerId(CONSUMER_ID);
		dto.setCartId(cartId);
		dto.setBillingAddress(orderRequestData.getBillingAddress());
		dto.setShippingAddress(orderRequestData.getShippingAddress());
		dto.setPaymentMethod(orderRequestData.getPaymentMethod());
		
		HttpEntity<CreateOrderDTO> request = new HttpEntity<CreateOrderDTO>(dto);
		
		Order order = orderServiceRestTemplate.exchange(
			"http://order-service/consumers/" + CONSUMER_ID + "/orders", 
			HttpMethod.POST,
			request,
			new ParameterizedTypeReference<Order>() {}
		).getBody();
		
		return order;
	}
	
	public List<Order> getAllOrders() {
		
		List<Order> allOrders = orderServiceRestTemplate.exchange(
			"http://order-service/consumers/" + CONSUMER_ID + "/orders", 
			HttpMethod.GET,
			null,
			new ParameterizedTypeReference<List<Order>>() {}
		).getBody();
		
		return allOrders;
	}
	
	public Order getOrder(Integer orderId) {
		
		Order order = orderServiceRestTemplate.exchange(
			"http://order-service/consumers/" + CONSUMER_ID + "/orders/" + orderId, 
			HttpMethod.GET,
			null,
			new ParameterizedTypeReference<Order>() {}
		).getBody();
		
		return order;
	}
	
	public List<Order> searchOrders(SearchOrdersRequestDTO dto) {
		
		HttpEntity<SearchOrdersRequestDTO> request = 
				new HttpEntity<SearchOrdersRequestDTO>(dto);
		
		List<Order> orders = orderServiceRestTemplate.exchange(
			"http://order-service/consumers/" + CONSUMER_ID + "/orders/searches", 
			HttpMethod.POST,
			request,
			new ParameterizedTypeReference<SearchOrdersResponseDTO>() {}
		).getBody().getOrders();
		
		return orders;
	}
	

	public void cancelOrder(Integer orderId) {
		
		orderServiceRestTemplate.exchange(
			"http://order-service/consumers/" + CONSUMER_ID + "/orders/" + orderId,
			HttpMethod.DELETE,
			null,
			new ParameterizedTypeReference<Order>() {}
		);
	}

}
