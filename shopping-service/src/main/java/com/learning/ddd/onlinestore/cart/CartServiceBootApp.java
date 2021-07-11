package com.learning.ddd.onlinestore.cart;

import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.learning.ddd.onlinestore.cart.domain.Cart;
import com.learning.ddd.onlinestore.cart.domain.CartItem;
import com.learning.ddd.onlinestore.cart.domain.service.CartService;
import com.learning.ddd.onlinestore.inventory.proxy.InventoryServiceRestTemplateBasedProxy;

@ComponentScan( {"com.learning.ddd.onlinestore"} )
@SpringBootApplication//(scanBasePackages = "com.learning.ddd.onlinestore.*")
@RestController
//@RequestMapping("/cart-service")
@EnableEurekaClient
public class CartServiceBootApp {//extends SpringBootServletInitializer {

//	@Override
//	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
//		return application.sources(ShoppingServiceSpringBootApp.class);
//	}

	public static void main(String[] args) {
		SpringApplication.run(CartServiceBootApp.class, args);
	}
	
	@Autowired
	private CartService cartService;
	
	@Autowired
	private InventoryServiceRestTemplateBasedProxy inventoryServiceProxy;
	
	@PostMapping("/carts")
	public Cart addCart(@RequestBody List<CartItem> items) {
		
		Cart cart = new Cart();
		cart.setConsumerId("123");
		cart.addItems(items);
		Cart savedCart = cartService.saveCart(cart);
		return savedCart;
	}
	
	@GetMapping("/carts")
	public List<Cart> getCarts() {
		
		return cartService.getCarts("123");
	}
	
	@GetMapping("/inventory/number") // the /inventory just to differentiate from /cart
	public Integer produceRandomNumber() {
		
		return new Random().nextInt();
	}
	
	@GetMapping("/inventory/items") // the /inventory just to differentiate from /cart
	public List<CartItem> getInventoryItems() {
		
		return inventoryServiceProxy.getItems();
	}

}

