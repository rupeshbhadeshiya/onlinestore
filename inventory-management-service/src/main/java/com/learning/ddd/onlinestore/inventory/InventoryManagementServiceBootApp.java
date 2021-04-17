package com.learning.ddd.onlinestore.inventory;

import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.learning.ddd.onlinestore.inventory.domain.Inventory;
import com.learning.ddd.onlinestore.inventory.domain.Item;
import com.learning.ddd.onlinestore.inventory.domain.repository.ItemRepository;

@ComponentScan("com.learning.ddd.onlinestore")
@SpringBootApplication
@RestController
@RequestMapping("/inventory")
@EnableEurekaClient
public class InventoryManagementServiceBootApp { //must be in root package of project
	
	@Autowired
	private Inventory inventory;
	
//	@Autowired
//	private ItemRepository itemRepository;
	
	public static void main(String[] args) {
		SpringApplication.run(InventoryManagementServiceBootApp.class, args);
	}
	
	@GetMapping("/number") // a ping kind of api to know things working fine!
	public Integer produceRandomNumber() {
		
		return new Random().nextInt();
	}
	
	@GetMapping("/items")
	public List<Item> getItems() {
		
		return inventory.getItems();
				
//		return itemRepository.findAll();
		
//		final Item BISCUIT_ITEM = new Item(101, "Grocery", "Biscuit", "Parle-G", 10, 10.0);
//		final Item CHIVDA_ITEM = new Item(102, "Grocery", "Chivda", "Real Farali Chivda", 10, 20.0);
//		final Item BATHING_SOAP_ITEM = new Item(202, "Toiletries", "Bathing Soap", "Mysore Sandal Soap", 5, 30.0);
//		return Arrays.asList(
//			new Item[] { BISCUIT_ITEM, CHIVDA_ITEM, BATHING_SOAP_ITEM }
//		);
		
	}
	
	@PostMapping("/items")
	public List<Item> addItems(@RequestBody List<Item> items) {
		
		return inventory.addItems(items);
		
//		List<Item> addedItems = itemRepository.saveAll(items);
//		return addedItems;
	}
	
	@DeleteMapping("/items/{itemId}")
	public void deleteItem(@PathVariable Integer itemId) {

		inventory.removeItem(itemId);
	}
	
}
