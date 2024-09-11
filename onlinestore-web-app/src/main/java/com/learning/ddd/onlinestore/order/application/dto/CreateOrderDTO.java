package com.learning.ddd.onlinestore.order.application.dto;

import java.io.Serializable;

import com.learning.ddd.onlinestore.cart.domain.CartInfo;
import com.learning.ddd.onlinestore.order.domain.Address;
import com.learning.ddd.onlinestore.payment.domain.PaymentMethod;

public class CreateOrderDTO implements Serializable {

	private static final long serialVersionUID = 7178297974688994634L;

	private String consumerId;
	private CartInfo cartInfo;
	private PaymentMethod paymentMethod;
	private Address billingAddress;
	private Address shippingAddress;

	
	//no-arg constructor required by many frameworks (e.g. JSON converters)
	public CreateOrderDTO() {
	}
	
	public CreateOrderDTO(String consumerId) {
		super();
	}
	
	public CreateOrderDTO(String consumerId, CartInfo cartInfo, PaymentMethod paymentMethod, Address billingAddress, Address shippingAddress) {
		super();
		this.consumerId = consumerId;
		this.cartInfo = cartInfo;
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
	
	public void setCartInfo(CartInfo cartInfo) {
		this.cartInfo = cartInfo;
	}
	
	public CartInfo getCartInfo() {
		return cartInfo;
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
				+ ", cartInfo=" + cartInfo 
				+ ", paymentMethod=" + paymentMethod 
				+ ", billingAddress=" + billingAddress 
				+ ", shippingAddress=" + shippingAddress 
				+ "]";
	}

}
