package com.learning.ddd.onlinestore.cart.domain.service;

import java.util.List;
import java.util.Optional;

import javax.jms.JMSException;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learning.ddd.onlinestore.cart.application.dto.AddItemToCartDTO;
import com.learning.ddd.onlinestore.cart.domain.Cart;
import com.learning.ddd.onlinestore.cart.domain.CartItem;
import com.learning.ddd.onlinestore.cart.domain.event.CartEmptiedEventData;
import com.learning.ddd.onlinestore.cart.domain.event.ItemAddedToCartEventData;
import com.learning.ddd.onlinestore.cart.domain.event.ItemRemovedFromCartEventData;
import com.learning.ddd.onlinestore.cart.domain.event.pubsub.CartEventsProducer;
import com.learning.ddd.onlinestore.cart.domain.exception.CartItemNotFoundException;
import com.learning.ddd.onlinestore.cart.domain.exception.CartNotFoundException;
import com.learning.ddd.onlinestore.cart.domain.repository.CartRepository;
import com.learning.ddd.onlinestore.domain.event.DomainEvent;
import com.learning.ddd.onlinestore.domain.event.DomainEventName;

@Service
public class CartService {

	@Autowired
	private CartRepository cartRepository;
	
	@Autowired
	private CartEventsProducer cartEventsProducer;
	
	//@Autowired
	//private DomainEventPublisher domainEventPublisher;
	
	
	public CartService() {
	}
	
	@Transactional
	public Cart addItem(AddItemToCartDTO addItemToCartDTO) throws JMSException {
		
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
		
		cart.addItem(addItemToCartDTO.getItem());
		
		Cart savedCart = cartRepository.save(cart);
		
		ItemAddedToCartEventData eventData = new ItemAddedToCartEventData(savedCart, addItemToCartDTO.getItem());
		DomainEvent itemsAddedToCartEvent = new DomainEvent(DomainEventName.ITEM_ADDED_TO_CART, eventData);
		
		cartEventsProducer.publishDomainEvent(itemsAddedToCartEvent);
		
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
			throws CartNotFoundException, CartItemNotFoundException, CloneNotSupportedException, JMSException {
		
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
			
			ItemRemovedFromCartEventData eventData = new ItemRemovedFromCartEventData(cart, copyOfItemToBeRemoved);
			DomainEvent ItemRemovedFromCartEvent = new DomainEvent(DomainEventName.ITEM_REMOVED_FROM_CART, eventData);
			
			cartEventsProducer.publishDomainEvent(ItemRemovedFromCartEvent);
		}
		
		if (cart.getItems().isEmpty()) {
			
			this.emptyCart(cartId, DomainEventName.CART_EMPTIED_BY_CONSUMER);
			
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
	public void emptyCart(Integer cartId, DomainEventName eventThatTriggeredCartEmptying) throws CartNotFoundException, CloneNotSupportedException, JMSException {
		
		// prepare event to publish - very imp - to let Inventory know to reclaim these items
		
		Cart cart =  getCartInternal(cartId);
		
		Cart cartThatIsEmptied = (Cart) cart.clone();
		CartEmptiedEventData cartEmptiedEventData = new CartEmptiedEventData(cartThatIsEmptied);
		
		// delete cart
		cartRepository.deleteById(cartId);
		
		DomainEvent cartEmptiedEvent = null;
		if (eventThatTriggeredCartEmptying == DomainEventName.ORDER_CREATED) {
			cartEmptiedEvent = new DomainEvent(
				DomainEventName.CART_EMPTIED_DUE_TO_ORDER_CREATION, cartEmptiedEventData
			);
			
		} else if (eventThatTriggeredCartEmptying == DomainEventName.CART_EMPTIED_BY_CONSUMER) {
			cartEmptiedEvent = new DomainEvent(
				DomainEventName.CART_EMPTIED_BY_CONSUMER, cartEmptiedEventData
			);
			
		} else {
			throw new RuntimeException("emptyCart(): Unknown eventThatTriggeredCartEmptying = " + eventThatTriggeredCartEmptying);
		}
		
		cartEventsProducer.publishDomainEvent(cartEmptiedEvent);
		
		//System.out.println("==== emptyCart(cartId) - CartEmptiedEvent published - " + cartEmptiedEvent);
	}
	
}
