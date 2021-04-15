package com.learning.ddd.onlinestore.microservice_app;

import java.net.URISyntaxException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.learning.ddd.onlinestore.inventory.proxy.InventoryManagementServiceRestTemplateBasedProxy;
import com.learning.ddd.onlinestore.shopping.domain.Cart;
import com.learning.ddd.onlinestore.shopping.domain.Item;
import com.learning.ddd.onlinestore.shopping.domain.service.CartService;
import com.learning.ddd.onlinestore.shopping.proxy.ShoppingServiceRestTemplateBasedProxy;

// Add Items to a Cart:
//
// URL: POST http://localhost:8100/shopping-service/cart/items
// 
// Request:
// {
//		"consumerId": 123,
//		"items": [
//			{
//				"category": "Grocery",
//				"subCategory": "Biscuits",
//				"name": "Parle-G",
//				"quantity": 10,
//				"price": 10.0
//			}
//		]
// }
//

// Get Items from a Cart:  GET http://localhost:8100/shopping-service/cart/items

@RestController
public class OnlineStoreMicroserviceAppController {

	@Autowired
	private CartService cartService;
	
	@Autowired
	private InventoryManagementServiceRestTemplateBasedProxy inventoryServiceProxy;
	
	@Autowired
	private ShoppingServiceRestTemplateBasedProxy shoppingServiceProxy;
	
	//-------- INVENTORY ----------------------
	
	@GetMapping("/onlinestore/inventory-management/items")
	public List<Item> getInventoryItems() {
		
		return inventoryServiceProxy.getItems();
	}
	
	//-------- SHOPPING -----------------------
	
	@PostMapping("/onlinestore/shopping/carts")
	public Cart addCart(@RequestBody List<Item> items) throws URISyntaxException {
		
		return shoppingServiceProxy.addCart();
		
//		Cart cart = new Cart();
//		cart.setConsumerId("123");
//		cart.addItems(items);
//		Cart savedCart = cartService.saveCart(cart);
//		return savedCart;
	}
	
	@GetMapping("/onlinestore/shopping/carts")
	public List<Cart> getCarts() {
		
		return shoppingServiceProxy.getCarts();
		
		//return cartService.getCarts("123");
	}
	
	//-------- CHECKOUT ------------------------
	
	//
	
}
