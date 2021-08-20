package com.learning.ddd.onlinestore.order.application.dto;

import java.io.Serializable;

import com.learning.ddd.onlinestore.cart.domain.Cart;
import com.learning.ddd.onlinestore.order.domain.Address;
import com.learning.ddd.onlinestore.payment.domain.PaymentMethod;

public class CreateOrderDTO implements Serializable {

	private static final long serialVersionUID = 7178297974688994634L;

	private String consumerId;
	private Cart cart;
	private PaymentMethod paymentMethod;
	private Address billingAddress;
	private Address shippingAddress;
	
	//no-arg constructor required by many frameworks (e.g. JSON converters)
	public CreateOrderDTO(String consumerId) {
		super();
	}
	
	public CreateOrderDTO(String consumerId, Cart cart, PaymentMethod paymentMethod, Address billingAddress, Address shippingAddress) {
		super();
		this.consumerId = consumerId;
		this.cart = cart;
		this.paymentMethod = paymentMethod;
		this.billingAddress = billingAddress;
		this.shippingAddress = shippingAddress;
	}
	
	public String getConsumerId() {
		return consumerId;
	}
	
	public void setConsumerId(String consumerId) {
		this.consumerId = consumerId;
	}

	public Cart getCart() {
		return cart;
	}

	public void setCart(Cart cart) {
		this.cart = cart;
	}

	public PaymentMethod getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(PaymentMethod paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public Address getBillingAddress() {
		return billingAddress;
	}

	public void setBillingAddress(Address billingAddress) {
		this.billingAddress = billingAddress;
	}

	public Address getShippingAddress() {
		return shippingAddress;
	}

	public void setShippingAddress(Address shippingAddress) {
		this.shippingAddress = shippingAddress;
	}

	@Override
	public String toString() {
		return "OrderRequestDTO ["
				+ "consumerId=" + consumerId
				+ ", cart=" + cart 
				+ ", paymentMethod=" + paymentMethod 
				+ ", billingAddress=" + billingAddress 
				+ ", shippingAddress=" + shippingAddress 
				+ "]";
	}

}
