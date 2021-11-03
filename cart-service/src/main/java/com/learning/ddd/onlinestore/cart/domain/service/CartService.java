package com.learning.ddd.onlinestore.cart.domain.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learning.ddd.onlinestore.cart.application.dto.PullCartDTO;
import com.learning.ddd.onlinestore.cart.domain.Cart;
import com.learning.ddd.onlinestore.cart.domain.CartItem;
import com.learning.ddd.onlinestore.cart.domain.event.CartEmptiedEvent;
import com.learning.ddd.onlinestore.cart.domain.event.CartPulledAndItemAddedEvent;
import com.learning.ddd.onlinestore.cart.domain.event.CartUpdatedEvent;
import com.learning.ddd.onlinestore.cart.domain.exception.CartItemNotFoundException;
import com.learning.ddd.onlinestore.cart.domain.exception.CartNotFoundException;
import com.learning.ddd.onlinestore.cart.domain.repository.CartRepository;
import com.learning.ddd.onlinestore.domain.event.pubsub.DomainEventPublisher;

@Service
public class CartService {

	@Autowired
	private CartRepository cartRepository;
	
	@Autowired
	private DomainEventPublisher domainEventPublisher;
	
	
	public CartService() {
	}
	

	@Transactional
	public Cart pullCartAndAddItems(String consumerId, PullCartDTO pullCartDTO) {
		
		Cart cart = new Cart();
		
		cart.setConsumerId(consumerId);
		
		cart.addItems(pullCartDTO.getItems());
		
		Cart savedCart = cartRepository.save(cart);
		
		CartPulledAndItemAddedEvent cartPulledEvent = new CartPulledAndItemAddedEvent(cart);
		domainEventPublisher.publishEvent(cartPulledEvent);
		
		System.out.println("==== Cart - published - " + cartPulledEvent);
		
		return savedCart;
	}

	public List<Cart> getAllCarts(String consumerId) {
		
		return cartRepository.findByConsumerId(consumerId);
	}

	public Cart getCart(int cartId) throws CartNotFoundException {
		
		return getCartInternal(cartId);
	}


	private Cart getCartInternal(int cartId) throws CartNotFoundException {
		
		Optional<Cart> cartInDatabase = cartRepository.findById(cartId);
		
		if (!cartInDatabase.isPresent()) {
			throw new CartNotFoundException(cartId);
		}
		
		return cartInDatabase.get();
	}
	
	@Transactional
	public Cart updateCart(Cart cart) {
		
		Cart updatedCart = cartRepository.save(cart);
		
		CartUpdatedEvent cartUpdatedEvent = new CartUpdatedEvent(cart);
		domainEventPublisher.publishEvent(cartUpdatedEvent);
		
		System.out.println("==== updateCart() - CartUpdatedEvent published - " + cartUpdatedEvent);
		
		return updatedCart;
	}
	
	@Transactional
	public void removeItems(int cartId, List<CartItem> itemsToRemove) 
			throws CartNotFoundException, CartItemNotFoundException {
		
		Cart cart =  getCartInternal(cartId);
		
		for (CartItem cartItemToRemove : itemsToRemove) {
			cart.removeItem(cartItemToRemove);
		}
		
		cartRepository.save(cart);
		
		// FIXME Add Event Publishing code
		
	}
	
	@Transactional
	public void emptyCart(Integer cartId) throws CartNotFoundException, CloneNotSupportedException {
		
		// prepare event to publish - very imp - to let Inventory know to reclaim these items
		
		Cart cart =  getCartInternal(cartId);
		
		List<CartItem> cartItemsBeingReleased = new ArrayList<>();
		for (CartItem cartItem : cart.getItems()) {
			cartItemsBeingReleased.add(cartItem.clone());
		}
		
		CartEmptiedEvent cartEmptiedEvent = new CartEmptiedEvent(cartId, cartItemsBeingReleased);
		
		// delete cart
		
		cartRepository.deleteById(cartId);
		
		// publish event
		
		domainEventPublisher.publishEvent(cartEmptiedEvent);
		
		System.out.println("==== emptyCart(cartId) - CartEmptiedEvent published - " + cartEmptiedEvent);
	}
	
	@Transactional
	public void emptyCartWithoutPublishingEvent(Integer cartId) throws CartNotFoundException, CloneNotSupportedException {
		
		// delete cart
		
		cartRepository.deleteById(cartId);
		
		// since this is internal one, event not to be published
		//domainEventPublisher.publishEvent(cartEmptiedEvent);
		//System.out.println("==== emptyCart(cartId) - CartEmptiedEvent published - " + cartEmptiedEvent);
	}
	
}
