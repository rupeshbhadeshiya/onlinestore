package com.learning.ddd.onlinestore.order.proxy;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.learning.ddd.onlinestore.cart.domain.Cart;
import com.learning.ddd.onlinestore.cart.proxy.CartServiceRestTemplateBasedProxy;
import com.learning.ddd.onlinestore.order.application.dto.SearchOrdersRequestDTO;
import com.learning.ddd.onlinestore.order.domain.Order;
import com.learning.ddd.onlinestore.payment.domain.PaymentMethod;

@Controller
public class OrderController {
	
	private static final String CHECKOUT_JSP_NAME = "order/checkout";
	private static final String VIEW_ALL_ORDERS_JSP_NAME = "order/view-orders";
	private static final String VIEW_ORDER_DETAILS_JSP_NAME = "order/view-order-details";
	private static final String SEARCH_ORDERS_JSP_NAME = "order/search-orders";

	@Autowired
	private CartServiceRestTemplateBasedProxy cartServiceProxy;
	
	@Autowired
	private OrderServiceRestTemplateBasedProxy orderServiceProxy;
	
	
	public OrderController() {
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
    
	
	@GetMapping("/go-for-checkout")
    public String checkoutView(@RequestParam Integer cartId, Model model) {

		Cart cart = cartServiceProxy.getCart(cartId);
		model.addAttribute("cart", cart);
		
		List<PaymentMethod> paymentMethods = orderServiceProxy.getSupportedPaymentMethods();
		model.addAttribute("paymentMethods", paymentMethods);
		
		// required by ModelAttribute 'order' in checkout.jsp
		model.addAttribute("order", new Order());
		
        return CHECKOUT_JSP_NAME;			
    }
	
	// when someone clicks on link "Checkout", this method is called
   	@PostMapping("/checkout")
    public String checkout(Model model,
    		@RequestParam String cartId, 
    		@ModelAttribute("order") Order order) {

   		Cart cart = cartServiceProxy.getCart(Integer.parseInt(cartId));
   		
   		order.setConsumerId(cart.getConsumerId());
   		
   		Order createdOrder = orderServiceProxy.checkout(cart, order);
   		
   		System.out.println(
 			"--------------------- checkout() --------------------\n"
 			+ " order = " + createdOrder +
 			"\n--------------------------------------------------");
   		
        model.addAttribute("order", createdOrder);
        model.addAttribute("isOrderCreatedSuccessfully", true);
         
        return VIEW_ORDER_DETAILS_JSP_NAME;			
    }
	
    // when someone clicks on link "Orders", this method is called
  	@GetMapping("/view-orders")
    public String viewOrders(Model model) {

  		List<Order> orders = orderServiceProxy.getAllOrders();
  		
  		System.out.println(
 			"--------------------- viewOrders() --------------------\n"
 			+ " orders = " + orders +
 			"\n--------------------------------------------------");
  		
        model.addAttribute("orders", orders);
        
        return VIEW_ALL_ORDERS_JSP_NAME;			
    }
  	
    // when someone wishes to view details of an Order, this method is called
  	@GetMapping("/view-order-details")
    public String viewOrder(Model model, @RequestParam Integer orderId) {

  		Order order = orderServiceProxy.getOrder(orderId);
  		
  		System.out.println(
 			"--------------------- viewOrder() --------------------\n"
 			+ " order = " + order +
 			"\n--------------------------------------------------");
	
        model.addAttribute("order", order);
        
        return VIEW_ORDER_DETAILS_JSP_NAME;			
    } 	
  	
  	// when someone clicks on "Cancel Order", this method is called
   	@GetMapping("/cancel-order")
    public String cancelOrder(Model model, @RequestParam Integer orderId) {

   		orderServiceProxy.cancelOrder(orderId);
   		
   		List<Order> orders = orderServiceProxy.getAllOrders();
   		
        model.addAttribute("orders", orders);
         
        return VIEW_ALL_ORDERS_JSP_NAME;			
    }
   	
   	// when someone clicks on link "search-orders" then this method is called
   	@GetMapping("/search-orders")
   	public String searchOrders(Model model) {
   		
   		model.addAttribute("searchOrdersRequestDTO", new SearchOrdersRequestDTO());
   		
   		return SEARCH_ORDERS_JSP_NAME;
   	}
   	
   	// when someone submits search orders form details, this method is called
   	@PostMapping(value = "/search-orders")
   	public String searchOrders(Model model, 
   			@ModelAttribute("searchOrdersRequestDTO") SearchOrdersRequestDTO searchOrdersRequestDTO) {
   		
   		List<Order> orders = orderServiceProxy.searchOrders(searchOrdersRequestDTO);
   		
   		System.out.println(
   				
  			"---------------------- OrderController.searchOrders() --------------------\n"
   					+ "searchOrdersRequestDTO = " + searchOrdersRequestDTO
  					+ "searchedOrders = " + orders 
   	 		+ "\n--------------------------------------------------");
   		
   		if (orders.isEmpty()) {
   			model.addAttribute("noOrdersFound", true);
   			System.out.println(
   	   	  			"---------------------- OrderController.searchOrders() --------------------\n"
   	   	   					+ "noOrdersFound = " + true
   	   	   	 		+ "\n--------------------------------------------------");
   		} else {
   			model.addAttribute("numOrdersFound", orders.size());
   			System.out.println(
   	   	  			"---------------------- OrderController.searchOrders() --------------------\n"
   	   	   					+ "numOrdersFound = " + orders.size()
   	   	   	 		+ "\n--------------------------------------------------");
   		}
   		
   		model.addAttribute("orders", orders);
   		
   		return VIEW_ALL_ORDERS_JSP_NAME;
   	}
  	
}
