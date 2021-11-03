package com.learning.ddd.onlinestore.cart.domain.event;

import java.util.List;

import com.learning.ddd.onlinestore.cart.domain.CartItem;
import com.learning.ddd.onlinestore.domain.event.DomainEvent;
import com.learning.ddd.onlinestore.domain.event.DomainEventName;

public class CartEmptiedEvent implements DomainEvent {

	private static final long serialVersionUID = -263118167133003092L;

	private Integer cartId;

	private List<CartItem> cartItemsBeingReleased;

	public CartEmptiedEvent(Integer cartId, List<CartItem> cartItemsBeingReleased) {
		this.cartId = cartId;
		this.cartItemsBeingReleased = cartItemsBeingReleased;
	}
	
	@Override
	public DomainEventName getEventName() {
		return DomainEventName.CART_EMPTIED;
	}
	
	@Override
	public Object getEventData() {
		return cartId;
	}
	
	public Integer getCartId() {
		return cartId;
	}
	
	public List<CartItem> getCartItemsBeingReleased() {
		return cartItemsBeingReleased;
	}

	@Override
	public String toString() {
		return "CartEmptiedEvent [cartId=" + cartId 
				+ ", cartItemsBeingReleased=" + cartItemsBeingReleased 
				+ "]";
	}
	
}
