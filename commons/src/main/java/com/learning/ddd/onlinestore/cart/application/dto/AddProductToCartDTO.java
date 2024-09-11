package com.learning.ddd.onlinestore.cart.application.dto;

import java.io.Serializable;

import com.learning.ddd.onlinestore.product.domain.Product;

public class AddProductToCartDTO implements Serializable {

	private static final long serialVersionUID = -4121629740413217523L;
	
	private String consumerId;
	private int cartId;
	private Product product;

	// no-arg default constructor is required by Spring ApplicationContext, without this, 
	// when a REST API Controller metho having this object as parameter is called, 
	// it results into below error
	// ...
	// 	com.fasterxml.jackson.databind.exc.InvalidDefinitionException: 
	//	Cannot construct instance of 
	//	`com.learning.ddd.onlinestore.cart.application.dto.AddItemToCartDTO` 
	//	(no Creators, like default construct, exist)
	// ...
	public AddProductToCartDTO() {
	}
	
	public AddProductToCartDTO(String consumerId, int cartId, Product product) {
		this.consumerId = consumerId;
		this.cartId = cartId;
		this.product = product;
	}


	public String getConsumerId() {
		return consumerId;
	}

	public int getCartId() {
		return cartId;
	}

	public Product getProduct() {
		return product;
	}


	@Override
	public String toString() {
		return "AddItemToCartDTO [consumerId=" + consumerId
			+ ", cartId=" + cartId + ", product = " + product + "]";
	}

}