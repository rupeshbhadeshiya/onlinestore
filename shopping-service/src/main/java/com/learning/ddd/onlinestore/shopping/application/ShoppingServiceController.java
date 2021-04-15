package com.learning.ddd.onlinestore.shopping.application;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.learning.ddd.onlinestore.inventory.proxy.InventoryManagementServiceRestTemplateBasedProxy;
import com.learning.ddd.onlinestore.shopping.domain.Cart;
import com.learning.ddd.onlinestore.shopping.domain.Item;
import com.learning.ddd.onlinestore.shopping.domain.service.CartService;

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

//@RestController
////@RequestMapping("/shopping")
//public class ShoppingServiceController {
//
//	@Autowired
//	private CartService cartService;
//	
//	@Autowired
//	private InventoryManagementServiceRestTemplateBasedProxy inventoryServiceProxy;
//	
//	@PostMapping("/carts")
//	public Cart addCart(@RequestBody List<Item> items) {
//		
//		Cart cart = new Cart();
//		cart.setConsumerId("123");
//		cart.addItems(items);
//		Cart savedCart = cartService.saveCart(cart);
//		return savedCart;
//	}
//	
//	@GetMapping("/carts")
//	public List<Cart> getCarts() {
//		
//		return cartService.getCarts("123");
//	}
//	
//	@GetMapping("/inventory/items")
//	public List<Item> getInventoryItems() {
//		
//		return inventoryServiceProxy.getItems();
//	}
//	
//}
