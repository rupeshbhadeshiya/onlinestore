package com.learning.ddd.onlinestore.inventory_management.application;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.learning.ddd.onlinestore.inventory_management.domain.Item;

//@RestController
////@RequestMapping("/inventory")
//public class InventoryManagementServiceController {
//	
//	@GetMapping("/number")
//	public Integer produceRandomNumber() {
//		
//		return new Random().nextInt();
//	}
//	
//	@GetMapping("/items")
//	public List<Item> getItems() {
//		
//		final Item BISCUIT_ITEM = new Item(101, "Grocery", "Biscuit", "Parle-G", 10, 10.0);
//		final Item CHIVDA_ITEM = new Item(102, "Grocery", "Chivda", "Real Farali Chivda", 10, 20.0);
//		final Item BATHING_SOAP_ITEM = new Item(202, "Toiletries", "Bathing Soap", "Mysore Sandal Soap", 5, 30.0);
//		
//		return Arrays.asList(
//			new Item[] { BISCUIT_ITEM, CHIVDA_ITEM, BATHING_SOAP_ITEM }
//		);
//		
//	}
//	
//}
