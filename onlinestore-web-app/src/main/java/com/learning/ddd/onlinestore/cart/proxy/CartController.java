package com.learning.ddd.onlinestore.cart.proxy;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.learning.ddd.onlinestore.cart.domain.Cart;
import com.learning.ddd.onlinestore.inventory.domain.Product;
import com.learning.ddd.onlinestore.productcatalog.proxy.ProductCatalogServiceRestTemplateBasedProxy;
import com.learning.ddd.onlinestore.utils.SessionLikeInMemoryStore;

// FIXME Fix this problem: 
//			Initially web-app has cartId=0 and cart-service don't have any cart so creates a new
//			So web-app sends cartId=1, cart-service responds properly
//			Now if cart-service is shut-down, web-app still has cartId=1 so it sends it
//			when it sends cartId=1, cart-service can't find it so returns an error
// 			hence need some way by which web-app knows to send cartId=0 when cart-service restarts

@Controller
public class CartController {
	
	private static final String SHOP_PRODUCTS_JSP_NAME = "cart/shop-products";
	private static final String VIEW_CART_DETAILS_JSP_NAME = "cart/view-cart-details";
	
	private static final String CONSUMER_ID = "11";

	@Autowired
	private CartServiceRestTemplateBasedProxy cartServiceProxy;
	
	@Autowired
	private ProductCatalogServiceRestTemplateBasedProxy productCatalogServiceProxy;
	
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
    // - example... /onlinestore/view-products where /onlinestore is web context-root
    //
    //@RequestMapping("/onlinestore/inventory/view-products")
    //
    // thus, if you wish to have domainness then name resource like that way
    // example name /view-inventory-products rather than /view-products
    

//	// when someone clicks on link "add-products", this method is called
//  	@GetMapping("/shop-products-view")
//    public String shopItemsView(Model model) {
//  		
//		model.addAttribute("cartId", getCartId());
//  		
//		List<Product> products = productCatalogServiceProxy.getAllProducts();
// 		System.out.println(
// 			"---------------------- shopItemsView() --------------------\n"
//				+ "AllAvailableProducts = " + products 
//				+ "\n Total = " + products.size()
// 			+ "\n--------------------------------------------------");
//    	model.addAttribute("products", products);
//        
//        return SHOP_PRODUCTS_JSP_NAME;			
//    }
//  	
//    // when someone adds a item to a Cart, this method is called
//  	@GetMapping(value = "/shop-product")
// 	public String shopProduct(@RequestParam Integer productId, 
// 			@RequestParam Integer cartId, Model model) {
// 		
//  		Product product = productCatalogServiceProxy.getProduct(productId);
//  		
//  		Cart cart = cartServiceProxy.addProductToCart(cartId, product);
//  			
//  		System.out.println(
// 			"--------------------- shopProduct() --------------------"
//				+ "\n a Product is added to a Cart = " + product
//				+ "\n cart = " + cart + 
// 			"\n--------------------------------------------------");
// 		
// 		model.addAttribute("isProductShoppedSuccessfully", true);
// 		
// 		setCartId(cart);
// 		model.addAttribute("cartId", cart.getCartId());
// 		
// 		List<Product> products = productCatalogServiceProxy.getAllProducts();
// 		System.out.println(
// 			"---------------------- shopProduct() --------------------\n"
//				+ "AllAvailableProducts = " + products 
//				+ "\n Total = " + products.size()
// 			+ "\n--------------------------------------------------");
//    	model.addAttribute("products", products);
//        
// 		return SHOP_PRODUCTS_JSP_NAME;		
// 	}

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
// 	public String getCartInfo(Model model) {
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
 	
 	
 	@GetMapping(value = "/remove-product-from-cart")
 	public String removeProductFromCart(@RequestParam Integer cartId, 
 			@RequestParam Integer productId, Model model) {
 		
 		System.out.println(
 			"--------------------- removeProductFromCart() : before --------------------"
				+ "\n cartId = " + cartId
				+ "\n productId = " + productId + 
 			"\n--------------------------------------------------");
 	 		
 		
 		Cart updatedCart = cartServiceProxy.removeProductFromCart(
 			getConsumerId(), cartId, productId
 		);
 		
 		
 		if (updatedCart != null) {
 			
 			System.out.println(
	 			"--------------------- removeProductFromCart() : after --------------------"
 					+ "\n cartId = " + cartId
 					+ "\n productId = " + productId
 					+ "\n outcome => Cart is updated, redirecting back to Cart Details " 
 					+ "\n Updated Cart = " + updatedCart + 
	 			"\n--------------------------------------------------");
 			
 			
 		} else {
 			
 			// Cart has become empty!
 			
 			System.out.println(
	 			"--------------------- removeProductFromCart() : after --------------------"
 					+ "\n cartId = " + cartId
 					+ "\n productId = " + productId
 					+ "\n outcome => Cart is empty now! Redirecting to Shop Products!" +
	 			"\n--------------------------------------------------");
 			
 		}
 		
 		model.addAttribute("isProductRemovedSuccessfully", true);
			
		model.addAttribute("cart", updatedCart);
		
		model.addAttribute("products", updatedCart.getProducts());
 		
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

