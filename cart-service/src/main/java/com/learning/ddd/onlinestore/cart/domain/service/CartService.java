package com.learning.ddd.onlinestore.cart.domain.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learning.ddd.onlinestore.cart.application.dto.AddItemToCartDTO;
import com.learning.ddd.onlinestore.cart.domain.Cart;
import com.learning.ddd.onlinestore.cart.domain.CartItem;
import com.learning.ddd.onlinestore.cart.domain.event.CartEmptiedEvent;
import com.learning.ddd.onlinestore.cart.domain.event.ItemRemovedFromCartEvent;
import com.learning.ddd.onlinestore.cart.domain.event.ItemsAddedToCartEvent;
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
	public Cart addItem(AddItemToCartDTO addItemToCartDTO) {
		
		Cart cart;
		
		List<Cart> carts = cartRepository.findByConsumerId(addItemToCartDTO.getConsumerId());
		
		if (carts.isEmpty()) {
			
			cart = new Cart();
			cart.setConsumerId(addItemToCartDTO.getConsumerId());
			System.out.println("CartService.addItem() ====== creating new Cart "
					+ " (consumerId="+cart.getConsumerId() + ")");
			
		} else {
			
			cart = carts.get(0); // every consumer should have at max one Cart only
			System.out.println("CartService.addItem() ====== retrieved existring Cart "
					+ " (cartId="+cart.getCartId() + ", consumerId="+cart.getConsumerId() + ")");
			
		}
		
		cart.addItems(addItemToCartDTO.getItems());
		
		Cart savedCart = cartRepository.save(cart);
		
		ItemsAddedToCartEvent itemsAddedToCartEvent = new ItemsAddedToCartEvent(savedCart, addItemToCartDTO.getItems());
		
		domainEventPublisher.publishEvent(itemsAddedToCartEvent);
		
		//System.out.println("==== pullCartAndAddItems(): Cart - published event - " + itemsAddedToCartEvent);
		
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
	public Cart removeItem(Integer cartId, Integer itemId) 
			throws CartNotFoundException, CartItemNotFoundException, CloneNotSupportedException {
		
		Cart cart =  getCartInternal(cartId);
		
		CartItem cartItemToBeRemoved = null, copyOfItemToBeRemoved = null;
		
		for (CartItem cartItem : cart.getItems()) {
			
			if (cartItem.getItemId() == itemId) {
				
				cartItemToBeRemoved = cartItem;	// if remove here, then throws ConcurrentModificationException!
				break;							// so remove after exiting the for loop...
				
			}
		}
		
		if (cartItemToBeRemoved != null) {
			
			copyOfItemToBeRemoved = cartItemToBeRemoved.clone();
			
			cart.removeItem(cartItemToBeRemoved);
			
			cartRepository.save(cart);
			
			ItemRemovedFromCartEvent itemRemovedFromCartEvent = new ItemRemovedFromCartEvent(cart, copyOfItemToBeRemoved);
			domainEventPublisher.publishEvent(itemRemovedFromCartEvent);
		}
		
		if (cart.getItems().isEmpty()) {
			
			this.emptyCart(cartId);
			
			cart = null;
		}
		
		return cart;
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
		
		//System.out.println("==== emptyCart(cartId) - CartEmptiedEvent published - " + cartEmptiedEvent);
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
