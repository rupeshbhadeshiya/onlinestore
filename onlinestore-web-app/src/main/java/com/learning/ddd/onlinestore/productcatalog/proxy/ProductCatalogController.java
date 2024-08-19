package com.learning.ddd.onlinestore.productcatalog.proxy;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import com.learning.ddd.onlinestore.inventory.domain.Product;
import com.learning.ddd.onlinestore.productcatalog.domain.ProductCatalogItem;
import com.learning.ddd.onlinestore.utils.SessionLikeInMemoryStore;

@Controller
public class ProductCatalogController {
	
	private static final String VIEW_PRODUCT_CATALOG_JSP_NAME = "productcatalog/view-product-catalog";

//	private final AtomicInteger atomicInteger = new AtomicInteger();

	private List<ProductCatalogItem> productCatalogItems = new ArrayList<>();
	
	@Autowired
	private SessionLikeInMemoryStore inMemStore;
	
	@Autowired
	private ProductCatalogServiceRestTemplateBasedProxy productCatalogServiceRestTemplateBasedProxy;
	
	
	public ProductCatalogController() {
		
		final int BISCUIT_PRODUCT_ID = 11;
		final int BATHING_SOAP_PRODUCT_ID = 22;
		final int CHIVDA_PRODUCT_ID = 33;
		final int PENCIL_PRODUCT_ID = 44;
		
		final int BISCUIT_PRODUCT_QUANTITY = 10;
		final int BATHING_SOAP_PRODUCT_QUANTITY = 5;
		final int CHIVDA_PRODUCT_QUANTITY = 10;
		final int PENCIL_PRODUCT_QUANTITY = 10;
		
		final Product BISCUIT_PRODUCT = new Product(BISCUIT_PRODUCT_ID, "Grocery", "Biscuit", "Parle-G", 10.0, BISCUIT_PRODUCT_QUANTITY);
		final Product BATHING_SOAP_PRODUCT = new Product(BATHING_SOAP_PRODUCT_ID, "Toiletries", "Bathing Soap", "Mysore Sandal Soap", 30.0, BATHING_SOAP_PRODUCT_QUANTITY);
		Product CHIVDA_PRODUCT = new Product(CHIVDA_PRODUCT_ID, "Grocery", "Chivda", "Real Farali Chivda", 20.0, CHIVDA_PRODUCT_QUANTITY);
		Product PENCIL_PRODUCT = new Product(PENCIL_PRODUCT_ID, "Stationery", "Pencil", "Natraj Pencil", 5.0, PENCIL_PRODUCT_QUANTITY);
		
		final ProductCatalogItem BISCUIT_PRODUCT_CATALOG_ITEM = new ProductCatalogItem(BISCUIT_PRODUCT);
		final ProductCatalogItem BATHING_SOAP_PRODUCT_CATALOG_ITEM = new ProductCatalogItem(BATHING_SOAP_PRODUCT);
		ProductCatalogItem CHIVDA_PRODUCT_CATALOG_ITEM = new ProductCatalogItem(CHIVDA_PRODUCT);
		ProductCatalogItem PENCIL_PRODUCT_CATALOG_ITEM = new ProductCatalogItem(PENCIL_PRODUCT);
		
		// for testing just Controller->JSP flow, i.e. without DB or actual business
//		Product BISCUIT_PRODUCT = new Product("Grocery", "Biscuit", "Parle-G", 10.0, 10);
//		Product CHIVDA_PRODUCT = new Product("Grocery", "Chivda", "Real Farali Chivda", 20.0, 10);
//		Product BATHING_SOAP_PRODUCT = new Product("Toiletries", "Bathing Soap", "Mysore Sandal Soap", 30.0, 5);
//		Product PENCIL_PRODUC = new Product("Stationery", "Pencil", "Natraj Pencil", 5.0, 10);

		productCatalogItems.add(BISCUIT_PRODUCT_CATALOG_ITEM);
		productCatalogItems.add(CHIVDA_PRODUCT_CATALOG_ITEM);
		productCatalogItems.add(BATHING_SOAP_PRODUCT_CATALOG_ITEM);
		productCatalogItems.add(PENCIL_PRODUCT_CATALOG_ITEM); 
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
    @GetMapping("/productcatalog")
    public String productCatalog(Model model) {

    	model.addAttribute("cartId", getCartId());
    	
        return VIEW_PRODUCT_CATALOG_JSP_NAME;		// render view-product-catalog.jsp
    }
    
    @GetMapping("/view-product-catalog")
    public String viewProductCatalog(Model model) {

    	List<Product> allProducts = productCatalogServiceRestTemplateBasedProxy.getAllProducts();
    	System.out.println(
 			"---------------------- viewProductCatalog() --------------------\n"
 					+ "allProducts = " + allProducts 
 			+ "\n--------------------------------------------------");
     		
    	
		model.addAttribute("products", allProducts);
		
		model.addAttribute("cartId", getCartId());
		
        return VIEW_PRODUCT_CATALOG_JSP_NAME;		// render view-product-catalog.jsp
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
 	
	// helper methods
 	
 	private int getCartId() {
 		Integer cartId = (Integer) inMemStore.getAttribute("cartId");
 		if (cartId == null) {
 			return 0;
 		} else {
 			return cartId;
 		}
 	}
    
}
