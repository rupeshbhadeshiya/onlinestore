package com.learning.ddd.onlinestore.cart.domain.event;

import com.learning.ddd.onlinestore.cart.domain.CartInfo;
import com.learning.ddd.onlinestore.domain.event.OnlinestoreDomainEvent;
import com.learning.ddd.onlinestore.domain.event.OnlinestoreDomainEventName;
import com.learning.ddd.onlinestore.product.domain.Product;

public class ProductAddedToCartEvent extends OnlinestoreDomainEvent {

	private static final long serialVersionUID = -5850194583556585018L;
	
	private Product product;

	public ProductAddedToCartEvent(CartInfo CartInfo, Product product) {
		super(
			OnlinestoreDomainEventName.PRODUCT_ADDED_TO_CART,
			CartInfo
		);
		this.product = product;
	}

	public CartInfo getCartInfo() {
		return (CartInfo) getEventData();
	}
	
	public Product getProduct() {
		return product;
	}
	
	@Override
	public String toString() {
		return "ProductAddedToCartEvent: CartInfo = " + getCartInfo() + ", Product = " + getProduct();
	}
	
}
