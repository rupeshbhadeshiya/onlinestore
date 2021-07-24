package com.learning.ddd.onlinestore.cart.domain.service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learning.ddd.onlinestore.cart.PullCartDTO;
import com.learning.ddd.onlinestore.cart.domain.Cart;
import com.learning.ddd.onlinestore.cart.domain.CartItem;
import com.learning.ddd.onlinestore.cart.domain.exception.CartItemNotFoundException;
import com.learning.ddd.onlinestore.cart.domain.exception.CartNotFoundException;
import com.learning.ddd.onlinestore.cart.domain.repository.CartRepository;

@Service
public class CartService {

	@Autowired
	private CartRepository cartRepository;

	@Transactional
	public Cart pullCartAndAddItems(String consumerId, PullCartDTO pullCartDTO) {
		
		Cart cart = new Cart();
		
		cart.setConsumerId(consumerId);
		
		cart.addItems(pullCartDTO.getItems());
		
		Cart savedCart = cartRepository.save(cart);
		//System.out.println("~~~> cartId = "+savedCart.getId());
		
		return savedCart;
	}

	public List<Cart> getAllCarts(String consumerId) {
		
		return cartRepository.findByConsumerId(consumerId);
	}

	public Cart getCart(int cartId) {
		
		Optional<Cart> cartInDatabase = cartRepository.findById(cartId);
		return cartInDatabase.isPresent() ? cartInDatabase.get() : null;
	}
	
	@Transactional
	public void removeItems(int cartId, List<CartItem> itemsToRemove) 
			throws CartNotFoundException, CartItemNotFoundException {
		
		Optional<Cart> cartInDatabase = cartRepository.findById(cartId);
		Cart cart = cartInDatabase.isPresent() ? cartInDatabase.get() : null;
		
		if (cart == null) {
			throw new CartNotFoundException(cartId);
		}
		
		for (CartItem cartItemToRemove : itemsToRemove) {
			cart.removeItem(cartItemToRemove);
		}
		
		cartRepository.save(cart);
		
//		Cart updatedCart = cartRepository.save(cart);
//		return updatedCart;
	}
	
	@Transactional
	public void releaseCartAndRemoveItems(Integer cartId) {
		
		cartRepository.deleteById(cartId);
	}
	
	@Transactional
	public void releaseCartAndRemoveItems(Cart cart) {

		cartRepository.delete(cart);
	}

}
