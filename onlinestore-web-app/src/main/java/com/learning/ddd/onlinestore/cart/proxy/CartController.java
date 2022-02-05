package com.learning.ddd.onlinestore.cart.proxy;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.learning.ddd.onlinestore.cart.domain.Cart;
import com.learning.ddd.onlinestore.inventory.domain.InventoryItem;
import com.learning.ddd.onlinestore.inventory.proxy.InventoryServiceRestTemplateBasedProxy;
import com.learning.ddd.onlinestore.utils.SessionLikeInMemoryStore;

// FIXME Fix this problem: 
//			Initially web-app has cartId=0 and cart-service don't have any cart so creates a new
//			So web-app sends cartId=1, cart-service responds properly
//			Now if cart-service is shut-down, web-app still has cartId=1 so it sends it
//			when it sends cartId=1, cart-service can't find it so returns an error
// 			hence need some way by which web-app knows to send cartId=0 when cart-service restarts

@Controller
public class CartController {
	
	private static final String SHOP_ITEMS_JSP_NAME = "cart/shop-items";
	private static final String VIEW_CART_DETAILS_JSP_NAME = "cart/view-cart-details";
	
	private static final String CONSUMER_ID = "11";

	@Autowired
	private InventoryServiceRestTemplateBasedProxy inventoryServiceProxy;
	
	@Autowired
	private CartServiceRestTemplateBasedProxy cartServiceProxy;
	
	//private int cartId = 0;			// cartId acts as identifier to track cart request
	//private Cart cart = new Cart(); // cart in use for ongoing shopping!
	
	@Autowired
	private SessionLikeInMemoryStore inMemStore;
	
	
	public CartController() {
	}
	
	// - IMP: Below mentioned REST like URL cause problem, it lets js/css calls 
    // - assume that /onlinestore/inventory is web context-root and they prefix
    // - all their calls with it which then fails, so every URL call from
    // - web pages must be in this format "<context-root>/<html-item>
    // - example... /onlinestore/view-items where /onlinestore is web context-root
    //
    //@RequestMapping("/onlinestore/inventory/view-items")
    //
    // thus, if you wish to have domainness then name resource like that way
    // example name /view-inventory-items rather than /view-items
    

	// when someone clicks on link "add-items", this method is called
  	@GetMapping("/shop-items-view")
    public String shopItemsView(Model model) {
  		
		model.addAttribute("cartId", getCartId());
  		
  		List<InventoryItem> items = inventoryServiceProxy.getAllItems();
  		
        model.addAttribute("items", items);
        
        return SHOP_ITEMS_JSP_NAME;			
    }
  	
    // when someone adds a item to a Cart, this method is called
  	@GetMapping(value = "/shop-item")
 	public String shopItem(@RequestParam Integer inventoryItemId, 
 			@RequestParam Integer cartId, Model model) {
 		
  		InventoryItem inventoryItem = inventoryServiceProxy.getItem(inventoryItemId);
  		
  		Cart cart = cartServiceProxy.addItemToCart(cartId, inventoryItem);
  			
  		System.out.println(
 			"--------------------- shopItem() --------------------"
 					+ "\n An Item added to a Cart = " + inventoryItem
 					+ "\n cart = " + cart + 
 			"\n--------------------------------------------------");
 		
 		model.addAttribute("isItemShoppedSuccessfully", true);
 		
 		setCartId(cart);
 		model.addAttribute("cartId", cart.getCartId());
 		
 		List<InventoryItem> items = inventoryServiceProxy.getAllItems();
        model.addAttribute("items", items);
        
        System.out.println(
 			"--------------------- shopItem() --------------------"
 					+ "\n total items with inventory = " + items.size() +
 					", items with inventory = " + items + 
 			"\n--------------------------------------------------");
 		
 		return SHOP_ITEMS_JSP_NAME;		
 	}

 	@GetMapping(value = "/view-cart")	// every consumer can have at max one cart only
 	public String getCart(Model model) {
 		
  		Cart cart = cartServiceProxy.getCart(getConsumerId());
  		
  		System.out.println(
 			"--------------------- getCart() --------------------"
 					+ "\n cart = " + cart + 
 			"\n--------------------------------------------------");
 		
 		model.addAttribute("cart", cart);
 		
 		return VIEW_CART_DETAILS_JSP_NAME;		
 	}
 	
// 	@GetMapping(value = "/view-cart-details")
// 	public String getCartDetails(Model model) {
// 		
// 		Cart cart = cartServiceProxy.getCart(CONSUMER_ID);
//  		
//  		System.out.println(
// 			"--------------------- getCardDetails() --------------------"
// 					+ "\n cartId = " + cartId
// 					+ "\n cart = " + cart + 
// 			"\n--------------------------------------------------");
// 		
// 		model.addAttribute("cart", cart);
// 		
// 		return VIEW_CART_DETAILS_JSP_NAME;		
// 	}
 	
 	
 	@GetMapping(value = "/remove-item-from-cart")
 	public String removeItemFromCart(@RequestParam Integer cartId, 
 			@RequestParam Integer itemId, Model model) {
 		
 		System.out.println(
 			"--------------------- removeItemFromCart() : before --------------------"
 					+ "\n cartId = " + cartId
 					+ "\n itemId = " + itemId + 
 			"\n--------------------------------------------------");
 	 		
 		
 		Cart updatedCart = cartServiceProxy.removeItemFromCart(getConsumerId(), cartId, itemId);
 		
 		if (updatedCart != null) {
 			
 			System.out.println(
	 			"--------------------- removeItemFromCart() : after --------------------"
	 					+ "\n cartId = " + cartId
	 					+ "\n itemId = " + itemId
	 					+ "\n outcome => Cart is updated, redirecting back to Cart Details " 
	 					+ "\n Updated Cart = " + updatedCart + 
	 			"\n--------------------------------------------------");
 			
 			
 		} else {
 			
 			// Cart has become empty!
 			
 			System.out.println(
	 			"--------------------- removeItemFromCart() : after --------------------"
	 					+ "\n cartId = " + cartId
	 					+ "\n itemId = " + itemId
	 					+ "\n outcome => Cart is empty now! Redirecting to Shop Items!" +
	 			"\n--------------------------------------------------");
 			
 		}
 		
 		model.addAttribute("isItemRemovedSuccessfully", true);
			
		model.addAttribute("cart", updatedCart);
 		
 		return VIEW_CART_DETAILS_JSP_NAME;	
 	}
 	
 	@GetMapping(value = "/empty-cart")
 	public String emptyCart(@RequestParam Integer cartId, Model model) {
 		
  		cartServiceProxy.emptyCart(getConsumerId(), cartId);
  		
  		System.out.println(
 			"--------------------- emptyCart() --------------------" +
 			"\n--------------------------------------------------");
 		
  		
 		return VIEW_CART_DETAILS_JSP_NAME;	
 	}
 	
 	// helper methods
 	
 	private int getCartId() {
 		Integer cartId = (Integer) inMemStore.getAttribute("cartId");
 		if (cartId == null) {
 			return 0;
 		} else {
 			return cartId;
 		}
 	}
 		  	
	private void setCartId(Cart cart) {
		inMemStore.setAttribute("cartId", cart.getCartId());
	}
	
	private String getConsumerId() {
		String consumerId = (String) inMemStore.getAttribute("CONSUMER_ID");
		return (consumerId == null) ? CONSUMER_ID : consumerId;
	}
	
}

