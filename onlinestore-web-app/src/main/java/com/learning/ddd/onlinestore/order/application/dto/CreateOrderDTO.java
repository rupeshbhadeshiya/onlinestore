package com.learning.ddd.onlinestore.order.application.dto;

import java.io.Serializable;

import com.learning.ddd.onlinestore.order.domain.Address;
import com.learning.ddd.onlinestore.payment.domain.PaymentMethod;

public class CreateOrderDTO implements Serializable {

	private static final long serialVersionUID = 7178297974688994634L;

	private String consumerId;
	private int cartId;
	private PaymentMethod paymentMethod;
	private Address billingAddress;
	private Address shippingAddress;
	
	//no-arg constructor required by many frameworks (e.g. JSON converters)
	public CreateOrderDTO() {
	}
	
	public CreateOrderDTO(String consumerId) {
		super();
	}
	
	public CreateOrderDTO(String consumerId, int cartId, PaymentMethod paymentMethod, Address billingAddress, Address shippingAddress) {
		super();
		this.consumerId = consumerId;
		this.cartId = cartId;
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
	
	public int getCartId() {
		return cartId;
	}
	
	public void setCartId(int cartId) {
		this.cartId = cartId;
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
				+ ", cartId=" + cartId 
				+ ", paymentMethod=" + paymentMethod 
				+ ", billingAddress=" + billingAddress 
				+ ", shippingAddress=" + shippingAddress 
				+ "]";
	}

}
