package com.learning.ddd.onlinestore.cart.domain.service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.jms.JMSException;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learning.ddd.onlinestore.cart.application.dto.AddProductToCartDTO;
import com.learning.ddd.onlinestore.cart.domain.Cart;
import com.learning.ddd.onlinestore.cart.domain.CartItem;
import com.learning.ddd.onlinestore.cart.domain.event.CartEmptiedEvent;
import com.learning.ddd.onlinestore.cart.domain.event.ProductAddedToCartEvent;
import com.learning.ddd.onlinestore.cart.domain.event.ProductRemovedFromCartEvent;
import com.learning.ddd.onlinestore.cart.domain.event.pubsub.CartEventsProducer;
import com.learning.ddd.onlinestore.cart.domain.exception.CartItemNotFoundException;
import com.learning.ddd.onlinestore.cart.domain.exception.CartNotFoundException;
import com.learning.ddd.onlinestore.cart.domain.repository.CartRepository;
import com.learning.ddd.onlinestore.domain.event.OnlinestoreDomainEventName;
import com.learning.ddd.onlinestore.product.domain.Product;

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
	public Cart addProduct(AddProductToCartDTO addProductToCartDTO) throws JMSException {
		
		Cart cart;
		
		List<Cart> carts = cartRepository.findByConsumerId(addProductToCartDTO.getConsumerId());
		
		if (carts.isEmpty()) {
			
			cart = new Cart();
			cart.setConsumerId(addProductToCartDTO.getConsumerId());
			System.out.println("CartService.addItem() ====== creating new Cart "
					+ " (consumerId="+cart.getConsumerId() + ")");
			
		} else {
			
			cart = carts.get(0); // every consumer should have at max one Cart only
			System.out.println("CartService.addItem() ====== retrieved existring Cart "
					+ " (cartId="+cart.getCartId() + ", consumerId="+cart.getConsumerId() + ")");
			
		}

		cart.addItem(new CartItem(addProductToCartDTO.getProduct()));
		
		// ... persist the Product to CartItem table
		Cart persistedCart = cartRepository.save(cart);

		// ... and publish the change as a domain event
		ProductAddedToCartEvent event = new ProductAddedToCartEvent(
			persistedCart.getCartInfo(), addProductToCartDTO.getProduct()
		);
		cartEventsProducer.publishDomainEvent(event);
		
		//System.out.println("==== pullCartAndAddItems(): Cart - published event - " + itemsAddedToCartEvent);
		
		return persistedCart;
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
	public Cart removeProduct(Integer cartId, Integer productId) 
			throws CartNotFoundException, CartItemNotFoundException, CloneNotSupportedException, JMSException {
		
		Cart cart =  getCartInternal(cartId);
		
		CartItem cartItemToBeRemoved = null, copyOfItemToBeRemoved = null;
		
		for (CartItem cartItem : cart.getItems()) {
			
			if (cartItem.getProduct().getProductId() == productId) {
				
				cartItemToBeRemoved = cartItem;	// if remove here, then throws ConcurrentModificationException!
				break;							// so remove after exiting the for loop...
				
			}
		}
		
		if (cartItemToBeRemoved != null) {
			
			copyOfItemToBeRemoved = cartItemToBeRemoved.clone();
			
			cart.removeItem(cartItemToBeRemoved);
			
			if (cart.getItemCount() != 0) { // is Cart still having any Products in it?
				
				// ... persist the Product to CartItem table
				cartRepository.save(cart);
				
				// ... and publish the change as a domain event
				ProductRemovedFromCartEvent event = new ProductRemovedFromCartEvent(
					cart.getCartInfo(), copyOfItemToBeRemoved.getProduct()
				);
				cartEventsProducer.publishDomainEvent(event);

			} else { // has Cart become empty?
				
				// ... persist the Product to CartItem table
				cartRepository.delete(cart);
				
				// ... and publish the change as a domain event
				CartEmptiedEvent  event = new CartEmptiedEvent(
					cartId,
					Arrays.asList( new Product[] { copyOfItemToBeRemoved.getProduct() } )
				);
				cartEventsProducer.publishDomainEvent(event);
			}
			
		}
		
		return cart;
	}
	
	@Transactional
	public void emptyCart(Integer cartId, OnlinestoreDomainEventName eventName) throws CartNotFoundException, CloneNotSupportedException, JMSException {
		
		if ((eventName != OnlinestoreDomainEventName.ORDER_CONFIRMED)
				&& (eventName != OnlinestoreDomainEventName.CART_EMPTIED_BY_CONSUMER)) {
			
			throw new RuntimeException("emptyCart(): Unknown event = " + eventName);
		}
		
		List<Product> products = cartRepository.findById(cartId).get().getProducts();
		
		// ... delete the Cart from local data store which will also delete 
		// associated CartItems i.e. Products in the Cart from local data store
		cartRepository.deleteById(cartId);

		// ... and publish the change as a domain event
		CartEmptiedEvent event = null;
		if (eventName == OnlinestoreDomainEventName.ORDER_CONFIRMED) {
			eventName = OnlinestoreDomainEventName.CART_EMPTIED_DUE_TO_ORDER_CREATION;
			
		} else if (eventName == OnlinestoreDomainEventName.CART_EMPTIED_BY_CONSUMER) {
			eventName = OnlinestoreDomainEventName.CART_EMPTIED_BY_CONSUMER;

		}
		
		event = new CartEmptiedEvent(eventName, cartId, products);
		cartEventsProducer.publishDomainEvent(event);
		
		//System.out.println("==== emptyCart(cartId) - CartEmptiedEvent published - " + cartEmptiedEvent);
	}
	
}
