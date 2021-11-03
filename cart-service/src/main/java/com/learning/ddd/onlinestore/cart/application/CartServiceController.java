package com.learning.ddd.onlinestore.cart.application;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.learning.ddd.onlinestore.cart.application.dto.PullCartDTO;
import com.learning.ddd.onlinestore.cart.domain.Cart;
import com.learning.ddd.onlinestore.cart.domain.CartItem;
import com.learning.ddd.onlinestore.cart.domain.exception.CartItemNotFoundException;
import com.learning.ddd.onlinestore.cart.domain.exception.CartNotFoundException;
import com.learning.ddd.onlinestore.cart.domain.service.CartService;

@RequestMapping("/consumers")
@RestController
public class CartServiceController {

	@Autowired
	private CartService cartService;
	
//	@Autowired
//	private InventoryServiceRestTemplateBasedProxy inventoryServiceProxy;
	
	@PostMapping("/{consumerId}/carts")
	public ResponseEntity<Cart> pullCartAndAddItems(@PathVariable String consumerId,
			@RequestBody PullCartDTO pullCartDTO) {
		
		Cart cart = cartService.pullCartAndAddItems(consumerId, pullCartDTO);
		
		return new ResponseEntity<Cart>(cart, HttpStatus.CREATED);
	}
	
	@PutMapping("/{consumerId}/carts/{cartId}")
	public ResponseEntity<Cart> continueShoppingInSameCart(
			@PathVariable String consumerId, @PathVariable Integer cartId, 
			@RequestBody PullCartDTO pullCartDTO) throws CartNotFoundException {
		
		Cart cart = cartService.getCart(cartId);
		
		cart.addItems(pullCartDTO.getItems());
		
		cartService.updateCart(cart);
		
		return new ResponseEntity<Cart>(cart, HttpStatus.CREATED);
	}
	
	@GetMapping("/{consumerId}/carts")
	public ResponseEntity<List<Cart>> getAllCarts(@PathVariable String consumerId) {
		
		List<Cart> carts = cartService.getAllCarts(consumerId);
		
		return new ResponseEntity<List<Cart>>(carts, HttpStatus.OK);
	}
	
	@GetMapping("/{consumerId}/carts/{cartId}")
	public ResponseEntity<Cart> getCart(@PathVariable String consumerId,
			@PathVariable Integer cartId) throws CartNotFoundException {
		
		Cart cart = cartService.getCart(cartId);
		
		return new ResponseEntity<Cart>(cart, HttpStatus.OK);
	}
	
	@DeleteMapping("/{consumerId}/carts/{cartId}/items")
	public ResponseEntity<?> removeItemsFromCart(@PathVariable String consumerId,
			@PathVariable Integer cartId, @RequestBody List<CartItem> itemsToRemove) 
					throws CartNotFoundException, CartItemNotFoundException {
		
		cartService.removeItems(cartId, itemsToRemove);
		
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	@DeleteMapping("/{consumerId}/carts/{cartId}")
	public ResponseEntity<?> emptyCart(@PathVariable String consumerId,
			@PathVariable Integer cartId) 
					throws CartNotFoundException, CloneNotSupportedException {
		
		cartService.emptyCart(cartId);
		
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