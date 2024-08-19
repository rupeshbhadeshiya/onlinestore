package com.learning.ddd.onlinestore.cart.domain.event;

import com.learning.ddd.onlinestore.cart.application.dto.CartInfo;
import com.learning.ddd.onlinestore.domain.event.OnlinestoreDomainEvent;
import com.learning.ddd.onlinestore.domain.event.OnlinestoreDomainEventName;
import com.learning.ddd.onlinestore.inventory.domain.Product;

public class ProductRemovedFromCartEvent extends OnlinestoreDomainEvent {

	private static final long serialVersionUID = -7005070935514488L;
	
	private Product product;

	public ProductRemovedFromCartEvent(CartInfo CartInfo, Product product) {
		super(
			OnlinestoreDomainEventName.PRODUCT_REMOVED_FROM_CART,
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
		return "ProductRemovedFromCartEvent: CartInfo = " + getCartInfo() + ", Product = " + getProduct();
	}
	
}

