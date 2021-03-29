package com.learning.ddd.onlinestore.main;

import com.learning.ddd.onlinestore.payment.domain.PaymentGateway;

public class CheckoutService {

	private PaymentGateway paymentGateway;

	public void setPaymentGateway(PaymentGateway paymentGateway) {
		this.paymentGateway = paymentGateway;
	}

}
