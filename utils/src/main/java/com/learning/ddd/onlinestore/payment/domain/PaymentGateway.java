package com.learning.ddd.onlinestore.payment.domain;

import com.learning.ddd.onlinestore.order.domain.Order;
import com.learning.ddd.onlinestore.order.domain.OrderTransaction;

public interface PaymentGateway {

	public OrderTransaction doPayment(Order order);
	
}
