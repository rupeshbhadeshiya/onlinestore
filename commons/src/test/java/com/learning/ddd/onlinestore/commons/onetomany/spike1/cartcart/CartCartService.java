package com.learning.ddd.onlinestore.commons.onetomany.spike1.cartcart;

import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartCartService {

	@Autowired
	private CartCartRepository cartRepository;

	@Transactional
	public CartCart pullCartAndAddItems(PullCartCartDTO pullCartDTO) {
		
		CartCart cart = new CartCart();
		cart.setConsumerId(pullCartDTO.getConsumerId());
		
		cart.addItems(pullCartDTO.getItems());
		
		CartCart savedCart = cartRepository.save(cart);
		System.out.println("~~~> cartId = "+savedCart.getId());
		
		return savedCart;
	}

	public List<CartCart> getAllCarts() {
		return cartRepository.findAll();
	}

	@Transactional
	public void deleteCart(CartCart cart) {

		cartRepository.delete(cart);
	}

	@Transactional
	public void deleteCart(Integer cartId) {
		
		cartRepository.deleteById(cartId);
	}
	
}
