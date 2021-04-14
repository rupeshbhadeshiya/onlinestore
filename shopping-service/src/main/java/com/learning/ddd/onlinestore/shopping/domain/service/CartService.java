package com.learning.ddd.onlinestore.shopping.domain.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learning.ddd.onlinestore.commons.domain.event.DomainEventsPublisher;
import com.learning.ddd.onlinestore.shopping.domain.Cart;
import com.learning.ddd.onlinestore.shopping.domain.Item;
import com.learning.ddd.onlinestore.shopping.domain.event.CartDeletedEvent;
import com.learning.ddd.onlinestore.shopping.domain.event.CartUpdatedEvent;
import com.learning.ddd.onlinestore.shopping.domain.event.ItemRemovedFromCartEvent;
import com.learning.ddd.onlinestore.shopping.domain.repository.CartRepository;

@Service
public class CartService {

	@Autowired
	private CartRepository cartRepository;
	
	@Autowired
	private DomainEventsPublisher domainEventPublisher;
	
	public Cart saveCart(Cart cart) {
		cart = cartRepository.save(cart);
		domainEventPublisher.publishEvent(new CartUpdatedEvent(cart));
		return cart;
	}
	
	public Cart getCart(int cartId) {
		Optional<Cart> cartFromDB = cartRepository.findById(cartId);
		return (cartFromDB != null && cartFromDB.isPresent()) ? cartFromDB.get() : null;
	}

	public List<Cart> getCarts(String consumerId) {
		return cartRepository.findByConsumerId(consumerId);
	}
	
	// removal must happen at both sides, Cart as well as Item
	public void removeItemFromCart(Cart cart, Item item) {
		
//		System.out.println("\n~~~~~~~-> BEFORE: cart.getItems())=" + cartRepository.findAll().get(0).getItems() + "\n");
		cart.removeItem(item);
		cartRepository.save(cart);		// update Cart that item is removed
		domainEventPublisher.publishEvent(new ItemRemovedFromCartEvent(cart, item));
//		System.out.println("\n~~~~~~~-> AFTER: cart.getItems())=" + cartRepository.findAll().get(0).getItems() + "\n");
		
//		item.setCart(null);
//		cart.getItems().remove(item);
//		cartRepository.save(cart);		// update Cart that item is removed
//		
//		System.out.println("\n~~~~~~~-> BEFORE: cart.getItems())=" + cartRepository.findAll().get(0).getItems() + "\n");
//		//item.setCart(null);
//		itemRepository.delete(item);	// remove item
//		System.out.println("\n~~~~~~~-> AFTER: cart.getItems())=" + cartRepository.findAll().get(0).getItems() + "\n");
	}
	
	public void deleteCart(Cart cart) {
		cartRepository.delete(cart);
		domainEventPublisher.publishEvent(new CartDeletedEvent(cart));
	}

}
