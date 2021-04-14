package com.learning.ddd.onlinestore.microservice_app;

import com.learning.ddd.onlinestore.payment.domain.PaymentGateway;

public class CheckoutService {

	private PaymentGateway paymentGateway;

	public void setPaymentGateway(PaymentGateway paymentGateway) {
		this.paymentGateway = paymentGateway;
	}

}
