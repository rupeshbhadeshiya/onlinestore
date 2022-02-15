package com.learning.ddd.onlinestore.cart.application;

import java.util.List;

import javax.jms.JMSException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.learning.ddd.onlinestore.cart.application.dto.AddItemToCartDTO;
import com.learning.ddd.onlinestore.cart.domain.Cart;
import com.learning.ddd.onlinestore.cart.domain.exception.CartItemNotFoundException;
import com.learning.ddd.onlinestore.cart.domain.exception.CartNotFoundException;
import com.learning.ddd.onlinestore.cart.domain.service.CartService;
import com.learning.ddd.onlinestore.domain.event.DomainEventName;

@RequestMapping("/consumers")
//@RequestMapping("/carts")
@RestController
public class CartServiceController {

	@Autowired
	private CartService cartService;
	
	@PostMapping("/{consumerId}/carts")
	public ResponseEntity<Cart> addItemToCart(@PathVariable String consumerId, 
			@RequestBody AddItemToCartDTO addItemToCartDTO) throws JMSException {
		
		Cart cart = cartService.addItem(addItemToCartDTO);
		
		return new ResponseEntity<Cart>(cart, HttpStatus.CREATED);
	}
	
	@GetMapping("/{consumerId}/carts")
	public ResponseEntity<List<Cart>> getAllCarts(@PathVariable String consumerId) {
		
		List<Cart> carts = cartService.getAllCarts(consumerId);
		
		System.out.println("################ getAllCarts() : carts = " + carts + " ################");
		
		return new ResponseEntity<List<Cart>>(carts, HttpStatus.OK);
	}
	
	@GetMapping("/{consumerId}/carts/{cartId}")
	public ResponseEntity<Cart> getCart(@PathVariable String consumerId,
			@PathVariable Integer cartId) throws CartNotFoundException {
		
		Cart cart = cartService.getCart(cartId);
		
		return new ResponseEntity<Cart>(cart, HttpStatus.OK);
	}
	
	@DeleteMapping("/{consumerId}/carts/{cartId}/items/{itemId}")
	public ResponseEntity<Cart> removeItemFromCart(@PathVariable String consumerId,
			@PathVariable Integer cartId, @PathVariable Integer itemId) 
				throws CartNotFoundException, CartItemNotFoundException, CloneNotSupportedException, JMSException {
		
		Cart cart = cartService.removeItem(cartId, itemId);
		
		return new ResponseEntity<Cart>(cart, HttpStatus.OK);
	}
	
	@DeleteMapping("/{consumerId}/carts/{cartId}")
	public ResponseEntity<?> emptyCart(@PathVariable String consumerId,
			@PathVariable Integer cartId) 
					throws CartNotFoundException, CloneNotSupportedException, JMSException {
		
		cartService.emptyCart(cartId, DomainEventName.CART_EMPTIED_BY_CONSUMER);
		
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
//	// --------- inventory verifiers ----------
//	
//	@GetMapping("/inventory/number") // the /inventory just to differentiate from /cart
//	public Integer produceRandomNumber() {
//		
//		return new Random().nextInt();
//	}
//	
//	@GetMapping("/inventory/items") // the /inventory just to differentiate from /cart
//	public List<CartItem> getInventoryItems() {
//		
//		return inventoryServiceProxy.getItems();
//	}
	
}