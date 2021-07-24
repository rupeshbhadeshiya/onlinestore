package com.learning.ddd.onlinestore.microservice_app;

import java.net.URISyntaxException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.learning.ddd.onlinestore.cart.domain.Cart;
import com.learning.ddd.onlinestore.cart.domain.CartItem;
import com.learning.ddd.onlinestore.cart.proxy.CartServiceRestTemplateBasedProxy;
import com.learning.ddd.onlinestore.inventory.proxy.InventoryServiceRestTemplateBasedProxy;

// Add Items to a Cart:
//
// URL: POST http://localhost:8100/cart-service/cart/items
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

// Get Items from a Cart:  GET http://localhost:8100/cart-service/cart/items

@RestController
public class OnlineStoreMicroserviceAppController {

//	@Autowired
//	private CartService cartService;
	
	@Autowired
	private InventoryServiceRestTemplateBasedProxy inventoryServiceProxy;
	@Autowired
	private CartServiceRestTemplateBasedProxy shoppingServiceProxy;
	
	//-------- INVENTORY ----------------------
	
	@GetMapping("/onlinestore/inventory-management/items")
	public List<CartItem> getInventoryItems() {
		
		return inventoryServiceProxy.getItems();
	}
	
	//-------- SHOPPING -----------------------
	
	@PostMapping("/onlinestore/shopping/carts")
	public Cart addCart(@RequestBody List<CartItem> items) throws URISyntaxException {
		
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
