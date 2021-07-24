package com.learning.ddd.onlinestore.inventory;

import java.util.List;
import java.util.Random;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.learning.ddd.onlinestore.inventory.domain.Inventory;
import com.learning.ddd.onlinestore.inventory.domain.InventoryItem;

@ComponentScan("com.learning.ddd.onlinestore")
@SpringBootApplication
@RestController
@RequestMapping("/inventory")
@EnableEurekaClient
public class InventoryServiceBootApp { //must be in root package of project
	
	@Autowired
	private Inventory inventory;
	
	public static void main(String[] args) {
		SpringApplication.run(InventoryServiceBootApp.class, args);
	}
	
	@PostMapping("/items")
	public ResponseEntity<AddItemsResponseDTO> addItems(
		@RequestBody AddItemsRequestDTO requestDTO) {
		
		List<InventoryItem> items = inventory.addItems(requestDTO.getItems());
		
		AddItemsResponseDTO responseDTO = new AddItemsResponseDTO(
			items,
			inventory.getItemsQuantitiesTotal()
		);
		
		return new ResponseEntity<AddItemsResponseDTO>(
			responseDTO, HttpStatus.CREATED
		);
	}
	
	@GetMapping("/items")
	public ResponseEntity<AddItemsResponseDTO> getAllItems() {
		
		List<InventoryItem> items = inventory.getItems();
		
		AddItemsResponseDTO responseDTO = new AddItemsResponseDTO(
			items,
			inventory.getItemsQuantitiesTotal()
		);
		
		return new ResponseEntity<AddItemsResponseDTO>(
			responseDTO, 
			HttpStatus.OK
		);
	}
	
	@GetMapping("/items/{itemId}")
	public ResponseEntity<InventoryItem> getItem(@PathVariable Integer itemId) {
		
		InventoryItem item = inventory.getItem(itemId);
		
		return new ResponseEntity<InventoryItem>(
			item, 
			HttpStatus.OK
		);
	}
	
	@PostMapping("/items/searches")
	public ResponseEntity<SearchItemsResponseDTO> searchItemsByExample(
			@RequestBody SearchItemsRequestDTO searchItemsRequestDTO) {
		
		InventoryItem exampleItem = searchItemsRequestDTO.getExampleItem();
		
		List<InventoryItem> items = inventory.searchItems(exampleItem);
		
		SearchItemsResponseDTO responseDTO = new SearchItemsResponseDTO(items);
		
		return new ResponseEntity<SearchItemsResponseDTO>(
			responseDTO, 
			HttpStatus.OK
		);
	}
	
	@DeleteMapping("/items/{itemId}")
	@Transactional
	public ResponseEntity<InventoryItem> deleteItem(@PathVariable Integer itemId) {
		
		inventory.removeItem(itemId);
		
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	@DeleteMapping("/items")
	@Transactional
	public ResponseEntity<InventoryItem> deleteItem(@RequestBody DeleteItemsRequestDTO deleteItemsRequestDTO) {
		
		InventoryItem deleteExampleItem = deleteItemsRequestDTO.getExampleItem();
		
		inventory.removeItems(deleteExampleItem);
		
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	@GetMapping("/number") // a ping kind of api to know things working fine!
	public Integer produceRandomNumber() {
		
		return new Random().nextInt();
	}
	
}
