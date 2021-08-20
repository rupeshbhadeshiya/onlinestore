package com.learning.ddd.onlinestore.inventory.application;

import java.util.List;
import java.util.Random;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.learning.ddd.onlinestore.inventory.application.dto.AddItemsRequestDTO;
import com.learning.ddd.onlinestore.inventory.application.dto.AddItemsResponseDTO;
import com.learning.ddd.onlinestore.inventory.application.dto.DeleteItemsRequestDTO;
import com.learning.ddd.onlinestore.inventory.application.dto.SearchItemsRequestDTO;
import com.learning.ddd.onlinestore.inventory.application.dto.SearchItemsResponseDTO;
import com.learning.ddd.onlinestore.inventory.application.dto.UpdateItemRequestDTO;
import com.learning.ddd.onlinestore.inventory.application.dto.UpdateItemResponseDTO;
import com.learning.ddd.onlinestore.inventory.domain.Inventory;
import com.learning.ddd.onlinestore.inventory.domain.InventoryItem;
import com.learning.ddd.onlinestore.inventory.domain.exception.ItemsAlreadyExistsException;

@RestController
@RequestMapping("/inventory")
@Validated
public class InventoryServiceController { //must be in root package of project
	
	@Autowired
	private Inventory inventory;
	
	
	@PostMapping("/items")
	public ResponseEntity<AddItemsResponseDTO> addItems(
		@RequestBody AddItemsRequestDTO requestDTO) throws ItemsAlreadyExistsException {
		
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
	public ResponseEntity<SearchItemsResponseDTO> searchItems(
			@RequestBody SearchItemsRequestDTO searchItemsRequestDTO) {
		
		InventoryItem exampleItem = searchItemsRequestDTO.getExampleItem();
		
		List<InventoryItem> items = inventory.searchItems(exampleItem);
		
		System.out.println(
  			"--------------------- InventoryServiceController.searchItems() --------------------\n"
  					+ " searchItemsRequestDTO = " + searchItemsRequestDTO
  					+ " items = " + items
  			+ "\n--------------------------------------------------");
		
		SearchItemsResponseDTO responseDTO = new SearchItemsResponseDTO(items);
		
		return new ResponseEntity<SearchItemsResponseDTO>(
			responseDTO, 
			HttpStatus.OK
		);
	}
	
	@PutMapping("/items/{itemId}")
	public ResponseEntity<UpdateItemResponseDTO> updateItem(
			@PathVariable Integer itemId,
			@RequestBody UpdateItemRequestDTO updateItemsRequestDTO) {
		
		InventoryItem itemToUpdate = updateItemsRequestDTO.getItem();
		
		InventoryItem updatedItem = inventory.updateItem(itemToUpdate);
		
		System.out.println(
  			"--------------------- updateItem() --------------------\n"
  							+ " itemToUpdate = " + itemToUpdate
  							+ " updatedItem = " + updatedItem
  			+ "\n--------------------------------------------------");
		
		UpdateItemResponseDTO responseDTO = new UpdateItemResponseDTO(updatedItem);
		
		return new ResponseEntity<UpdateItemResponseDTO>(
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