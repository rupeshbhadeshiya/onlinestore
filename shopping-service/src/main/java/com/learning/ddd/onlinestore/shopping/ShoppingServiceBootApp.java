package com.learning.ddd.onlinestore.shopping;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.learning.ddd.onlinestore.inventory.proxy.InventoryManagementServiceRestTemplateBasedProxy;
import com.learning.ddd.onlinestore.shopping.domain.Cart;
import com.learning.ddd.onlinestore.shopping.domain.Item;
import com.learning.ddd.onlinestore.shopping.domain.service.CartService;

@ComponentScan( {"com.learning.ddd.onlinestore"} )
@SpringBootApplication//(scanBasePackages = "com.learning.ddd.onlinestore.*")
@RestController
@RequestMapping("/shopping")
@EnableEurekaClient
public class ShoppingServiceBootApp {//extends SpringBootServletInitializer {

//	@Override
//	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
//		return application.sources(ShoppingServiceSpringBootApp.class);
//	}

	public static void main(String[] args) {
		SpringApplication.run(ShoppingServiceBootApp.class, args);
	}
	
	@Autowired
	private CartService cartService;
	
	@Autowired
	private InventoryManagementServiceRestTemplateBasedProxy inventoryServiceProxy;
	
	@PostMapping("/carts")
	public Cart addCart(@RequestBody List<Item> items) {
		
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
	
	@GetMapping("/inventory/number")
	public Integer produceRandomNumber() {
		
		return new Random().nextInt();
	}
	
	@GetMapping("/inventory/items")
	public List<Item> getInventoryItems() {
		
		return inventoryServiceProxy.getItems();
	}

}

