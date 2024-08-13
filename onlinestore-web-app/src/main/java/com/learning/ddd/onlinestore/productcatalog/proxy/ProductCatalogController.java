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

import com.learning.ddd.onlinestore.productcatalog.domain.Product;

@Controller
public class ProductCatalogController {
	
	private static final String WELCOME_JSP_NAME = "welcome";
	private static final String VIEW_PRODUCT_CATALOG_JSP_NAME = "productcatalog/view-product-catalog";

//	private final AtomicInteger atomicInteger = new AtomicInteger();

	private List<Product> products = new ArrayList<>();
	
	@Autowired
	private ProductCatalogServiceRestTemplateBasedProxy productCatalogServiceRestTemplateBasedProxy;
	
	
	public ProductCatalogController() {
		
		// for testing just Controller->JSP flow, i.e. without DB or actual business
		Product BISCUIT_PRODUCT = new Product("Grocery", "Biscuit", "Parle-G", 10.0, 10);
		Product CHIVDA_PRODUCT = new Product("Grocery", "Chivda", "Real Farali Chivda", 20.0, 10);
		Product BATHING_SOAP_PRODUCT = new Product("Toiletries", "Bathing Soap", "Mysore Sandal Soap", 30.0, 5);
		Product PENCIL_PRODUC = new Product("Stationery", "Pencil", "Natraj Pencil", 5.0, 10);

		products.add(BISCUIT_PRODUCT);
		products.add(CHIVDA_PRODUCT);
		products.add(BATHING_SOAP_PRODUCT);
		products.add(PENCIL_PRODUC); 
	}
	
    @GetMapping("/welcome")
    public String welcome(){
        return WELCOME_JSP_NAME;			// render welcome.jsp
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
    
    
    // this is a simple URL for anyone to surf into inventory
    @GetMapping("/productcatalog")
    public String productCatalog(Model model) {

        return VIEW_PRODUCT_CATALOG_JSP_NAME;		// render view-product-catalog.jsp
    }
    
    @GetMapping("/view-product-catalog")
    public String viewProductCatalog(Model model) {

    	List<Product> allProducts = productCatalogServiceRestTemplateBasedProxy.getAllProducts();
    	System.out.println(
 			"---------------------- viewProductCatalog() --------------------\n"
 					+ "allAvailableProducts = " + allProducts 
 			+ "\n--------------------------------------------------");
     		
    	
		model.addAttribute("products", allProducts);
		
        return VIEW_PRODUCT_CATALOG_JSP_NAME;		// render view-items.jsp
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
