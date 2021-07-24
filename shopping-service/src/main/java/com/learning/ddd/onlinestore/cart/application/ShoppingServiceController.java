package com.learning.ddd.onlinestore.cart.application;

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
//	@GetMapping("/inventory-service/items")
//	public List<Item> getInventoryItems() {
//		
//		return inventoryServiceProxy.getItems();
//	}
//	
//}
