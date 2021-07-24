package com.learning.ddd.onlinestore.cart;

import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.learning.ddd.onlinestore.cart.domain.Cart;
import com.learning.ddd.onlinestore.cart.domain.CartItem;
import com.learning.ddd.onlinestore.cart.domain.exception.CartItemNotFoundException;
import com.learning.ddd.onlinestore.cart.domain.exception.CartNotFoundException;
import com.learning.ddd.onlinestore.cart.domain.service.CartService;
import com.learning.ddd.onlinestore.inventory.proxy.InventoryServiceRestTemplateBasedProxy;

@ComponentScan( {"com.learning.ddd.onlinestore"} )
@SpringBootApplication//(scanBasePackages = "com.learning.ddd.onlinestore.*")
@RestController
@RequestMapping("/consumers")
@EnableEurekaClient
public class CartServiceBootApp extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(CartServiceBootApp.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(CartServiceBootApp.class, args);
	}
	
	@Autowired
	private CartService cartService;
	
	@Autowired
	private InventoryServiceRestTemplateBasedProxy inventoryServiceProxy;
	
	@PostMapping("/{consumerId}/carts")
	public ResponseEntity<Cart> pullCartAndAddItems(@PathVariable String consumerId,
			@RequestBody PullCartDTO pullCartDTO) {
		
		Cart cart = cartService.pullCartAndAddItems(consumerId, pullCartDTO);
		
		return new ResponseEntity<Cart>(cart, HttpStatus.CREATED);
	}
	
	@GetMapping("/{consumerId}/carts")
	public ResponseEntity<List<Cart>> getAllCarts(@PathVariable String consumerId) {
		
		List<Cart> carts = cartService.getAllCarts(consumerId);
		
		return new ResponseEntity<List<Cart>>(carts, HttpStatus.OK);
	}
	
	@GetMapping("/{consumerId}/carts/{cartId}")
	public ResponseEntity<Cart> getCart(@PathVariable String consumerId,
			@PathVariable Integer cartId) {
		
		Cart cart = cartService.getCart(cartId);
		
		return new ResponseEntity<Cart>(cart, HttpStatus.OK);
	}
	
	@DeleteMapping("/{consumerId}/carts/{cartId}/items")
	public ResponseEntity<?> removeItemsFromOneCart(@PathVariable String consumerId,
			@PathVariable Integer cartId, @RequestBody List<CartItem> itemsToRemove) 
					throws CartNotFoundException, CartItemNotFoundException {
		
		cartService.removeItems(cartId, itemsToRemove);
		
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	@DeleteMapping("/{consumerId}/carts/{cartId}")
	public ResponseEntity<?> releaseCartAndRemoveItems(@PathVariable String consumerId,
			@PathVariable Integer cartId) {
		
		cartService.releaseCartAndRemoveItems(cartId);
		
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	// --------- inventory verifiers ----------
	
	@GetMapping("/inventory/number") // the /inventory just to differentiate from /cart
	public Integer produceRandomNumber() {
		
		return new Random().nextInt();
	}
	
	@GetMapping("/inventory/items") // the /inventory just to differentiate from /cart
	public List<CartItem> getInventoryItems() {
		
		return inventoryServiceProxy.getItems();
	}
	
}

