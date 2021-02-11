package com.learning.ddd.onlinestore.order.domain.service;

import java.util.Date;

import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

import com.learning.ddd.onlinestore.commons.util.UUIDGenerator;
import com.learning.ddd.onlinestore.order.domain.Order;
import com.learning.ddd.onlinestore.payment.domain.Address;
import com.learning.ddd.onlinestore.payment.domain.PaymentGateway;
import com.learning.ddd.onlinestore.payment.domain.PaymentMethod;
import com.learning.ddd.onlinestore.shopping.domain.ShoppingCart;
import com.learning.ddd.onlinestore.transaction.domain.TransactionReceipt;

//
// Consumer visits Checkout counter where Mart team helps in payment processing...
// 	Order order = CheckoutService.createOrder(shoppingCart), 
// 	order.setPaymentMethod() or .setBillingAddress() or .setShippingAddress()
// Consumer confirms payment of Order... TransactionReceipt txnReceipt = CheckoutService.checkout(order)
//

@RunWith(JUnitPlatform.class)
public class CheckoutService {
	
	private PaymentGateway paymentGateway;
	
	public void setPaymentGateway(PaymentGateway paymentGateway) {
		this.paymentGateway = paymentGateway;
	}

	public Order createOrder( ShoppingCart cart, PaymentMethod paymentMethod, 
							Address billingAddress, Address shippingAddress ) {
		
		Order order = new Order(
			UUIDGenerator.newUUID(),//orderId, 
			new Date(), 			// purchaseDate, 
			"xyz", 					// merchantName, 
			cart.getItems(), 
			cart.getItemCount(), 
			cart.computeAmount(), 
			paymentMethod, 
			shippingAddress, 
			billingAddress,
			null, 					// TODO: deliveryDate		---- to be computed!!
			null 					// TODO: shipmentTrackingId --- to be found out!!
		);
		
		return order;
	}

	public TransactionReceipt checkout(Order order) {
		
		return paymentGateway.doPayment(order.getPaymentMethod(), order.getAmount());
	}

}
