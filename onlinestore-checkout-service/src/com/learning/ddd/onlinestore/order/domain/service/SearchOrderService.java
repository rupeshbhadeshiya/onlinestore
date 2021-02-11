package com.learning.ddd.onlinestore.order.domain.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.learning.ddd.onlinestore.commons.util.UUIDGenerator;
import com.learning.ddd.onlinestore.order.domain.Order;
import com.learning.ddd.onlinestore.payment.domain.AddressType;
import com.learning.ddd.onlinestore.payment.domain.PaymentMethod;
import com.learning.ddd.onlinestore.payment.domain.TestAddressFactory;

public class SearchOrderService {

	//public List<Order> search(SearchOrderCriteria searchOrderCriteria) {
		
	public List<Order> getAllOrders() {
		
		return Arrays.asList(
			new Order[] {
				new Order(
					UUIDGenerator.newUUID(),//orderId, 
					new Date(), 			// purchaseDate, 
					"xyz", 					// merchantName, 
					new ArrayList<>(),		// cart.getItems(), 
					0,						// cart.getItemCount(), 
					0.0,					// cart.computeAmount(), 
					PaymentMethod.CREDIT_CARD, 
					TestAddressFactory.dummyAddress(AddressType.BILLING_ADDRESS), 
					TestAddressFactory.dummyAddress(AddressType.SHIPPING_ADDRESS),
					null, 					// TODO: deliveryDate		---- to be computed!!
					null 					// TODO: shipmentTrackingId --- to be found out!!
				)
			}
		);
	}

}
