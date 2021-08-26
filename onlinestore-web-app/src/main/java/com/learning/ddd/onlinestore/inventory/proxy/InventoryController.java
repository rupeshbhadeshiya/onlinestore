package com.learning.ddd.onlinestore.inventory.proxy;

import java.util.ArrayList;
import java.util.Arrays;
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

import com.learning.ddd.onlinestore.inventory.application.dto.SearchInventoryItemDTO;
import com.learning.ddd.onlinestore.inventory.domain.InventoryItem;

@Controller
public class InventoryController {
	
	private static final String WELCOME_JSP_NAME = "welcome";
	private static final String VIEW_INVENTORY_ITEMS_JSP_NAME = "inventory/view-items";
	private static final String SEARCH_INVENTORY_ITEMS_JSP_NAME = "inventory/search-items";
	private static final String ADD_INVENTORY_ITEM_JSP_NAME = "inventory/add-item";
	private static final String UPDATE_INVENTORY_ITEM_JSP_NAME = "inventory/update-item";;

//	private final AtomicInteger atomicInteger = new AtomicInteger();

	private List<InventoryItem> items = new ArrayList<>();
	
	@Autowired
	private InventoryServiceRestTemplateBasedProxy inventoryServiceProxy;
	
	
	public InventoryController() {
		
		// for testing just Controller->JSP flow, i.e. without DB or actual business
		InventoryItem BISCUIT_ITEM = new InventoryItem("Grocery", "Biscuit", "Parle-G", 10, 10.0);
		InventoryItem CHIVDA_ITEM = new InventoryItem("Grocery", "Chivda", "Real Farali Chivda", 10, 20.0);
		InventoryItem BATHING_SOAP_ITEM = new InventoryItem("Toiletries", "Bathing Soap", "Mysore Sandal Soap", 5, 30.0);
		InventoryItem PENCIL_ITEM = new InventoryItem("Stationery", "Pencil", "Natraj Pencil", 10, 5.0);
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
    // - example... /onlinestore/view-items where /onlinestore is web context-root
    //
    //@RequestMapping("/onlinestore/inventory/view-items")
    //
    // thus, if you wish to have domainness then name resource like that way
    // example name /view-inventory-items rather than /view-items
    
    
    // this is a simple URL for anyone to surf into inventory
    @GetMapping("/inventory")
    public String inventory(Model model) {

        return VIEW_INVENTORY_ITEMS_JSP_NAME;		// render view-items.jsp
    }
    
    @GetMapping("/view-inventory-items")
    public String viewItems(Model model) {

    	List<InventoryItem> allItems = inventoryServiceProxy.getAllItems();
    	System.out.println(
 			"---------------------- updateItems() --------------------\n"
 					+ "allItems = " + allItems 
 			+ "\n--------------------------------------------------");
     		
    	
		model.addAttribute("items", allItems);
		
        return VIEW_INVENTORY_ITEMS_JSP_NAME;		// render view-items.jsp
    }
    
    // when someone clicks on link "add-items" then this method is called
  	@GetMapping("/add-inventory-item")
    public String addItemsView(Model model) {
  		
         model.addAttribute("item", new InventoryItem());
          
         return ADD_INVENTORY_ITEM_JSP_NAME;			// render add-items.jsp
    }
  	
    // when someone submits inventory item form details, this method is called
 	@PostMapping(value = "/add-inventory-item")
 	public String addItem(Model model, @ModelAttribute("item") InventoryItem item) {
 		
 		List<InventoryItem> items = inventoryServiceProxy.addItems(
			Arrays.asList(new InventoryItem[] { item })
		);
 		
 		if (!items.isEmpty()) {
 		
 			System.out.println(
	 			"---------------------- addItem() --------------------\n"
	 					+ "itemToAdd = " + item
	 					+ "savedItem = " + items.get(0)
	 			+ "\n--------------------------------------------------");
	 		
	 		model.addAttribute("isItemAddedSuccessfully", true);
	 		model.addAttribute("savedItem", items.get(0));
	 		
	 		List<InventoryItem> allItems = inventoryServiceProxy.getAllItems();
	 		System.out.println(
 	 			"---------------------- addItem() --------------------\n"
 	 					+ "allItems = " + allItems 
 	 			+ "\n--------------------------------------------------");
	 		
	    	model.addAttribute("items", allItems);
	 		
 		} else {
 			
 			model.addAttribute("anyError", true);
	 		model.addAttribute("errorMessage", "There was an issue in adding Item");
 		}
 		
 		return VIEW_INVENTORY_ITEMS_JSP_NAME;		// render view-items.jsp
 	}

 	// when someone clicks on link "search-items" then this method is called
  	@GetMapping("/search-inventory-items")
    public String searchItemsView(Model model) {
  		
         model.addAttribute("searchItemsRequestDTO", new SearchInventoryItemDTO());
          
         return SEARCH_INVENTORY_ITEMS_JSP_NAME;		// render search-items.jsp
    }
  	
  	// when someone submits search inventory items form details, this method is called
  	@PostMapping(value = "/search-inventory-items")
  	public String searchItems(Model model, 
  			@ModelAttribute("searchItemsRequestDTO") SearchInventoryItemDTO searchItemsRequestDTO) {
  		
  		List<InventoryItem> searchedItems = inventoryServiceProxy.searchItems(
  				searchItemsRequestDTO.toInventoryItem());
  		
  		System.out.println(
 			"---------------------- InventoryController.searchItems() --------------------\n"
 					+ "searchItemsRequestDTO = " + searchItemsRequestDTO
 					+ "searchedItems = " + searchedItems 
  	 		+ "\n--------------------------------------------------");
  		
  		if (searchedItems.isEmpty()) {
  			model.addAttribute("noItemsFound", true);
  		}
  		
  		model.addAttribute("items", searchedItems);
  		
  		return VIEW_INVENTORY_ITEMS_JSP_NAME;		// render view-items.jsp
  	}
  	
  	// when someone wishes to view update inventory items form, this method is called
   	@GetMapping(value = "/update-inventory-item")
   	public String updateItem(Model model, @RequestParam Integer itemId) {
   		
   		InventoryItem item = inventoryServiceProxy.getItem(itemId);
   		
   		model.addAttribute("item", item);
   		
   		return UPDATE_INVENTORY_ITEM_JSP_NAME;		// render update-inventory-item.jsp
   	}
  	
  	// when someone submits update inventory items form details, this method is called
   	@PostMapping(value = "/update-inventory-item")
   	public String updateItem(Model model, 
   			@RequestParam Integer itemId, 
   			@ModelAttribute("item") InventoryItem item) {
   		
   		item.setItemId(itemId);
   		
   		InventoryItem updatedItem = inventoryServiceProxy.updateItem(item);
   		
   		System.out.println(
   			"--------------------- updateItem() --------------------\n"
   						+ "itemToUpdate = " + item
   						+ ", updatedItem = " + updatedItem
   			+ "\n--------------------------------------------------");
   		
   		model.addAttribute("isItemUpdatedSuccessfully", true);
   		model.addAttribute("updatedItem", updatedItem);
 		
 		List<InventoryItem> allItems = inventoryServiceProxy.getAllItems();
    	System.out.println(
 			"---------------------- updateItem() --------------------\n"
 					+ "allItems = " + allItems 
 			+ "\n--------------------------------------------------");
 		
    	model.addAttribute("items", allItems);
   		
   		return VIEW_INVENTORY_ITEMS_JSP_NAME;		// render view-items.jsp
   	}

 	
 	@GetMapping("/delete-inventory-item")
	public String deleteItem(@RequestParam Integer itemId, Model model) {
		
 		inventoryServiceProxy.removeItem(itemId);
 		
 		System.out.println(
 			"---------------------- deleteItem() --------------------\n"
 					+ "itemId = " + itemId 
 			+ "\n--------------------------------------------------");
 		
 		model.addAttribute("isItemRemovedSuccessfully", true);
 		
 		List<InventoryItem> allItems = inventoryServiceProxy.getAllItems();
 		System.out.println(
 			"---------------------- deleteItem() --------------------\n"
 					+ "allItems = " + allItems 
 			+ "\n--------------------------------------------------");
 		
    	model.addAttribute("items", allItems);
 		
 		return VIEW_INVENTORY_ITEMS_JSP_NAME;		// render view-items.jsp
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
