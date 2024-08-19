package com.learning.ddd.onlinestore.inventory.proxy;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.learning.ddd.onlinestore.inventory.domain.InventoryItem;
import com.learning.ddd.onlinestore.inventory.domain.Product;

@Controller
public class InventoryController {
	
	private static final String WELCOME_JSP_NAME = "welcome";
	private static final String VIEW_INVENTORY_PRODUCTS_JSP_NAME = "inventory/view-products";
	private static final String SEARCH_INVENTORY_ITEMS_JSP_NAME = "inventory/search-products";
	private static final String ADD_INVENTORY_PRODUCT_JSP_NAME = "inventory/add-product";
	private static final String UPDATE_INVENTORY_PRODUCT_JSP_NAME = "inventory/update-product";

//	private final AtomicInteger atomicInteger = new AtomicInteger();

	private List<InventoryItem> items = new ArrayList<>();
	
	@Autowired
	private InventoryServiceRestTemplateBasedProxy inventoryServiceProxy;
	
	
	public InventoryController() {
		
		// for testing just Controller->JSP flow, i.e. without DB or actual business
		InventoryItem BISCUIT_ITEM = new InventoryItem(11, "Grocery", "Biscuit", "Parle-G", 10.0, 10);
		InventoryItem CHIVDA_ITEM = new InventoryItem(22, "Grocery", "Chivda", "Real Farali Chivda", 20.0, 10);
		InventoryItem BATHING_SOAP_ITEM = new InventoryItem(33, "Toiletries", "Bathing Soap", "Mysore Sandal Soap", 30.0, 5);
		InventoryItem PENCIL_ITEM = new InventoryItem(44, "Stationery", "Pencil", "Natraj Pencil", 5.0, 10);

		items.add(BISCUIT_ITEM);
		items.add(CHIVDA_ITEM);
		items.add(BATHING_SOAP_ITEM);
		items.add(PENCIL_ITEM); 
	}
	
    @GetMapping("/welcome")
    public String welcome(){
        return WELCOME_JSP_NAME;			// render welcome.jsp
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
    
    
    // this is a simple URL for anyone to surf into inventory
    @GetMapping("/inventory")
    public String inventory(Model model) {

        return VIEW_INVENTORY_PRODUCTS_JSP_NAME;		// render view-products.jsp
    }
    
    @GetMapping("/view-inventory-products")
    public String viewAvailableProducts(Model model) {

    	List<Product> products = inventoryServiceProxy.getAllAvailableProducts();
    	System.out.println(
 			"---------------------- viewInventoryProducts() --------------------\n"
 					+ "AllAvailableProductsFromInventory = " + products 
 			+ "\n--------------------------------------------------");
    	
		model.addAttribute("products", products);
		
        return VIEW_INVENTORY_PRODUCTS_JSP_NAME;		// render view-products.jsp
    }
    
    // when someone clicks on link "add-products" then this method is called
  	@GetMapping("/add-inventory-product")
    public String addItemsView(Model model) {
  		
         model.addAttribute("product", new Product());
          
         return ADD_INVENTORY_PRODUCT_JSP_NAME;			// render add-products.jsp
    }
  	
    // when someone submits inventory product form details, this method is called
 	@PostMapping(value = "/add-inventory-product")
 	public String addProduct(Model model, @ModelAttribute("product") Product product) {
 		
		System.out.println(
 			"---------------------- addProductToInventory() --------------------\n"
				+ "productToAdd = " + product
 			+ "\n--------------------------------------------------");
 		
 		Product savedProduct = null;
 		
 		try {
 			
 			savedProduct = inventoryServiceProxy.addProduct(product);
 			
		} catch (Exception e) {
			
			System.err.println("-----######-----#######----- " + e);
			model.addAttribute("anyError", true);

			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);

			model.addAttribute("errorMessage", e.getMessage());
			System.err.println("=====######~~~~~#######===== " + sw.toString());
		}
 		
 		if (savedProduct != null) {
 		
 			System.out.println(
	 			"---------------------- addProductToInventory() --------------------\n"
 				+ "savedProduct = " + savedProduct
	 			+ "\n--------------------------------------------------");
	 		
	 		model.addAttribute("isProductAddedSuccessfully", true);
	 		model.addAttribute("savedProduct", savedProduct);
	 		
 		} 
// 		else {
// 			
// 			model.addAttribute("anyError", true);
//	 		model.addAttribute("errorMessage", "There was an issue in adding Product to Inventory");
// 		}

 		List<Product> products = inventoryServiceProxy.getAllAvailableProducts();
    	System.out.println(
 			"---------------------- addProductToInventory() --------------------\n"
			+ "AllAvailableProductsFromInventory = " + products 
 			+ "\n--------------------------------------------------");
    	
    	model.addAttribute("products", products);
 		
 		return VIEW_INVENTORY_PRODUCTS_JSP_NAME;		// render view-products.jsp
 	}

 	// when someone clicks on link "search-items" then this method is called
  	@GetMapping("/search-inventory-products")
    public String searchProductsView(Model model) {
  		
         model.addAttribute("exampleProduct", new Product());
          
         return SEARCH_INVENTORY_ITEMS_JSP_NAME;		// render search-items.jsp
    }
  	
  	// when someone submits search inventory items form details, this method is called
  	@PostMapping(value = "/search-inventory-products")
  	public String searchProducts(Model model, 
  			@ModelAttribute("exampleProduct") Product exampleProduct) {
  		
  		List<Product> searchedProducts = inventoryServiceProxy.searchProducts(exampleProduct);
  		
  		System.out.println(
 			"---------------------- InventoryController.searchProducts() --------------------\n"
 					+ "searchProductsRequestDTO = " + exampleProduct
 					+ "searchedProducts = " + searchedProducts 
  	 		+ "\n--------------------------------------------------");
  		
  		if (searchedProducts.isEmpty()) {
  			model.addAttribute("noProductsFound", true);
  		}
  		
  		model.addAttribute("products", searchedProducts);
  		
  		return VIEW_INVENTORY_PRODUCTS_JSP_NAME;		// render view-products.jsp
  	}
  	
  	// when someone wishes to view update inventory items form, this method is called
   	@GetMapping(value = "/update-inventory-product")
   	public String updateProduct(Model model, @RequestParam Integer productId) {
   		
   		Product product = inventoryServiceProxy.getProduct(productId);
   		
   		model.addAttribute("product", product);
   		
   		return UPDATE_INVENTORY_PRODUCT_JSP_NAME;		// render update-inventory-item.jsp
   	}
  	
  	// when someone submits update inventory items form details, this method is called
   	@PostMapping(value = "/update-inventory-product")
   	public String updateProduct(Model model, 
   			@RequestParam Integer productId, 
   			@ModelAttribute("product") Product product) {
   		
   		product.setProductId(productId);
   		
   		Product updatedProduct = inventoryServiceProxy.updateProduct(product);
   		
   		System.out.println(
   			"--------------------- updateProduct() --------------------\n"
   						+ "productToUpdate = " + product
   						+ ", updatedProduct = " + updatedProduct
   			+ "\n--------------------------------------------------");
   		
   		model.addAttribute("isProductUpdatedSuccessfully", true);
   		model.addAttribute("updatedProduct", updatedProduct);
 		
   		List<Product> products = inventoryServiceProxy.getAllAvailableProducts();
    	System.out.println(
 			"---------------------- updateProduct() --------------------\n"
				+ "AllAvailableProductsFromInventory = " + products 
 			+ "\n--------------------------------------------------");
    	model.addAttribute("products", products);
   		
   		return VIEW_INVENTORY_PRODUCTS_JSP_NAME;		// render view-products.jsp
   	}

 	
 	@GetMapping("/delete-inventory-product")
	public String deleteProduct(@RequestParam Integer productId, Model model) {
		
 		inventoryServiceProxy.removeProduct(productId);
 		
 		System.out.println(
 			"---------------------- deleteProduct() --------------------\n"
 				+ "productId = " + productId 
 			+ "\n--------------------------------------------------");
 		
 		model.addAttribute("isProductRemovedSuccessfully", true);
 		
 		List<Product> products = inventoryServiceProxy.getAllAvailableProducts();
 		System.out.println(
 			"---------------------- deleteProduct() --------------------\n"
				+ "AllAvailableProductsFromInventory = " + products 
 			+ "\n--------------------------------------------------");
    	model.addAttribute("products", products);
 		
 		return VIEW_INVENTORY_PRODUCTS_JSP_NAME;		// render view-products.jsp
	}
 	
 	
	// Total control - setup a model and return the view name yourself. Or
	// consider subclassing ExceptionHandlerExceptionResolver (see below).
	@ExceptionHandler(Exception.class)
	public ModelAndView handleError(HttpServletRequest req, Exception ex) {
		//logger.error("Request: " + req.getRequestURL() + " raised " + ex);
		System.err.println("Request: " + req.getRequestURL() + " raised " + ex);

		ModelAndView mav = new ModelAndView();
		mav.addObject("exception", ex);
		mav.addObject("url", req.getRequestURL());
		mav.setViewName("error");
		
		return mav;
	}
 	
 	//
 	// helper methods
 	//
 	
// 	private int newItemId() {
//		return atomicInteger.incrementAndGet();
// 	}
    
}
