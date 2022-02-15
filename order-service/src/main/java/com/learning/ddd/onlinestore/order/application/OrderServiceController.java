package com.learning.ddd.onlinestore.order.application;

import java.text.ParseException;
import java.util.List;

import javax.jms.JMSException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.learning.ddd.onlinestore.order.application.dto.CreateOrderDTO;
import com.learning.ddd.onlinestore.order.application.dto.SearchOrdersRequestDTO;
import com.learning.ddd.onlinestore.order.application.dto.SearchOrdersResponseDTO;
import com.learning.ddd.onlinestore.order.domain.Order;
import com.learning.ddd.onlinestore.order.domain.service.OrderService;


@RequestMapping("/consumers")
@RestController
public class OrderServiceController {

	@Autowired
	private OrderService orderService;
	
	@PostMapping("/{consumerId}/orders")
	public ResponseEntity<Order> createOrder(
			@PathVariable String consumerId,
			@RequestBody CreateOrderDTO createOrderDTO) throws JMSException {
		
		//Cart cart = createOrderDTO.getCart();
		Order order = orderService.createOrderAndProcessPayment(
				//cart,
				createOrderDTO.getCartId(),
				createOrderDTO.getPaymentMethod(), 
				createOrderDTO.getBillingAddress(), 
				createOrderDTO.getShippingAddress()
			);
		
		return new ResponseEntity<Order>(order, HttpStatus.CREATED);
	}
	
	@GetMapping("/{consumerId}/orders")
	public ResponseEntity<List<Order>> getAllOrders(@PathVariable String consumerId) {
		
		List<Order> orders = orderService.getOrders(consumerId);
		
		System.out.println(
	 			"--------------------- getAllOrders() --------------------\n"
	 			+ " orders = " + orders +
	 			"\n--------------------------------------------------");
		
		return new ResponseEntity<List<Order>>(orders, HttpStatus.OK);
	}
	
	@GetMapping("/{consumerId}/orders/{orderId}")
	public ResponseEntity<Order> getOrder(
			@PathVariable String consumerId,
			@PathVariable Integer orderId) {
		
		Order order = orderService.getOrder(orderId);
		
		return new ResponseEntity<Order>(order, HttpStatus.OK);
	}
	
	@PostMapping("/{consumerId}/orders/searches")
	public ResponseEntity<SearchOrdersResponseDTO> searchOrders(
			@RequestBody SearchOrdersRequestDTO searchOrdersRequestDTO) 
					throws ParseException {
		
		System.out.println(
	  			"--------------------- OrderServiceController.searchOrders() --------------------\n"
	  						+ " searchOrdersDTO = " + searchOrdersRequestDTO
	  			+ "\n--------------------------------------------------");
		
		List<Order> orders = orderService.searchOrders(searchOrdersRequestDTO);
		
		System.out.println(
  			"--------------------- OrderServiceController.searchOrders() --------------------\n"
  						+ " searchOrdersDTO = " + searchOrdersRequestDTO
  						+ " orders = " + orders
  			+ "\n--------------------------------------------------");
		
		SearchOrdersResponseDTO responseDTO = new SearchOrdersResponseDTO(orders);
		
		return new ResponseEntity<SearchOrdersResponseDTO>(
			responseDTO, 
			HttpStatus.OK
		);
	}
	
	@DeleteMapping("/{consumerId}/orders/{orderId}")
	public ResponseEntity<?> cancelOrder(
			@PathVariable String consumerId,
			@PathVariable Integer orderId) throws CloneNotSupportedException, JMSException {
		
		orderService.cancelOrder(consumerId, orderId);
		
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	// URL will look like: /{consumerId}/orders?orderNumber=<order-number>
	@DeleteMapping("/{consumerId}/orders")
	public ResponseEntity<?> cancelOrder(
			@PathVariable String consumerId,
			@RequestParam String orderNumber) throws CloneNotSupportedException, JMSException {
		
		orderService.cancelOrder(consumerId, orderNumber);
		
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
}