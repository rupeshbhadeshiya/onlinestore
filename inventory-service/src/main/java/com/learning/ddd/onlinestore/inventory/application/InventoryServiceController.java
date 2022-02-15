package com.learning.ddd.onlinestore.inventory.application;

import java.util.List;
import java.util.Random;

import javax.jms.JMSException;
import javax.transaction.Transactional;
import javax.validation.Valid;

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

import com.learning.ddd.onlinestore.inventory.application.dto.AddItemRequestDTO;
import com.learning.ddd.onlinestore.inventory.application.dto.AddItemResponseDTO;
import com.learning.ddd.onlinestore.inventory.application.dto.DeleteItemsRequestDTO;
import com.learning.ddd.onlinestore.inventory.application.dto.GetItemsResponseDTO;
import com.learning.ddd.onlinestore.inventory.application.dto.SearchItemsRequestDTO;
import com.learning.ddd.onlinestore.inventory.application.dto.SearchItemsResponseDTO;
import com.learning.ddd.onlinestore.inventory.application.dto.UpdateItemRequestDTO;
import com.learning.ddd.onlinestore.inventory.application.dto.UpdateItemResponseDTO;
import com.learning.ddd.onlinestore.inventory.domain.Inventory;
import com.learning.ddd.onlinestore.inventory.domain.InventoryItem;
import com.learning.ddd.onlinestore.inventory.domain.exception.ItemAlreadyExistsException;

@RestController
@RequestMapping("/inventory")
@Validated
public class InventoryServiceController { //must be in root package of project
	
	@Autowired
	private Inventory inventory;
	
	// Using Spring validations
	@PostMapping("/items")
	public ResponseEntity<AddItemResponseDTO> addItem(
		@Valid @RequestBody AddItemRequestDTO requestDTO) throws ItemAlreadyExistsException, JMSException {
		
//		InventoryItem item = requestDTO.getItem();
//		
//		// Validation - Format/Value
//		
//		List<String> errors = new ArrayList<>();
//		
//		if (!ItemCategorySubCategory.isCategoryPresent(item.getCategory())) {
//			errors.add("Category: Missing or Invalid value");
//		}
//		
//		if (!ItemCategorySubCategory.isSubCategoryPresent(item.getSubCategory())) {
//			errors.add("SubCategory: Missing or Invalid value");
//		}
//		
//		if (item.getName() == null || item.getName().isEmpty()) {
//			errors.add("Name: Missing");
//		}
//		
//		if (item.getPrice() == null) {
//			errors.add("Price: Missing");
//		} else if (item.getPrice().isNaN()) {
//			errors.add("Price: Invalid value");
//		} else if (item.getPrice() <= 0) {
//			errors.add("Price: Invalid value");
//		}
//		
//		if (item.getQuantity() < 1 || item.getQuantity() > 100) {
//			errors.add("Quantity: Invalid value");
//		}
//		
//		if (!errors.isEmpty()) {
//			AddItemResponseDTO responseDTO = new AddItemResponseDTO();
//			responseDTO.setErrors(errors);
//			
//			// for sending body along with error, use this way
//			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//									.body(responseDTO);
//		}
//		
//		// Validation - Business
//		
//		if (inventory.searchItem(item) != null) {
//			errors.add("Item already exist");
//		}
//		
//		if (!errors.isEmpty()) {
//			AddItemResponseDTO responseDTO = new AddItemResponseDTO();
//			responseDTO.setErrors(errors);
//			
//			// for sending body along with error, use this way
//			return ResponseEntity.status(HttpStatus.CONFLICT)
//									.body(responseDTO);
//		}
		
		
		// Processing
		
		InventoryItem addedItem = inventory.addItem(requestDTO.getItem());
		
		return new ResponseEntity<AddItemResponseDTO>(
			new AddItemResponseDTO(addedItem), HttpStatus.CREATED
		);
	}
	
	
//	@ResponseStatus(HttpStatus.CONFLICT)
//	@ExceptionHandler(ItemAlreadyExistsException.class)
//	public ErrorDetails handleItemAlreadyExistsException(ItemAlreadyExistsException ex) {
//		
//		ErrorDetails errorDetails = new ErrorDetails();
//		
//		errorDetails.setTimestamp(new Date());
//		errorDetails.setStatus(HttpStatus.CONFLICT.value());
//		List<String> errors = new ArrayList<>();
//        errors.add("Item already exists");
//		errorDetails.setErrors(errors);
//
//		return errorDetails;
//	}
//	
//	@ResponseStatus(HttpStatus.BAD_REQUEST)
//	@ExceptionHandler(MethodArgumentNotValidException.class)
//	public ErrorDetails handleConstraintViolationException(MethodArgumentNotValidException ex) {
//		
//		ErrorDetails errorDetails = new ErrorDetails();
//		
//		errorDetails.setTimestamp(new Date());
//		errorDetails.setStatus(HttpStatus.BAD_REQUEST.value());
//		
//		List<String> errors = new ArrayList<>();
//	    ex.getBindingResult().getAllErrors().forEach((error) -> {
//	       // String fieldName = ((FieldError) error).getField();
//	        String errorMessage = error.getDefaultMessage();
//	        errors.add(errorMessage);
//	    });
////		ex.getConstraintViolations().forEach((constraintViolation) -> {
////			errors.add(constraintViolation.getMessage());
////		});
//		errorDetails.setErrors(errors);
//
//		return errorDetails;
//	}
	
//	Basic validations
//	
//	@PostMapping("/items")
//	public ResponseEntity<AddItemResponseDTO> addItem(
//			@RequestBody AddItemRequestDTO requestDTO) throws ItemAlreadyExistsException {
//		
//		InventoryItem item = requestDTO.getItem();
//		
//		// Validation - Format/Value
//		
//		List<String> errors = new ArrayList<>();
//		
//		if (!ItemCategorySubCategory.isCategoryPresent(item.getCategory())) {
//			errors.add("Category: Missing or Invalid value");
//		}
//		
//		if (!ItemCategorySubCategory.isSubCategoryPresent(item.getSubCategory())) {
//			errors.add("SubCategory: Missing or Invalid value");
//		}
//		
//		if (item.getName() == null || item.getName().isEmpty()) {
//			errors.add("Name: Missing");
//		}
//		
//		if (item.getPrice() == null) {
//			errors.add("Price: Missing");
//		} else if (item.getPrice().isNaN()) {
//			errors.add("Price: Invalid value");
//		} else if (item.getPrice() <= 0) {
//			errors.add("Price: Invalid value");
//		}
//		
//		if (item.getQuantity() < 1 || item.getQuantity() > 100) {
//			errors.add("Quantity: Invalid value");
//		}
//		
//		if (!errors.isEmpty()) {
//			AddItemResponseDTO responseDTO = new AddItemResponseDTO();
//			responseDTO.setErrors(errors);
//			
//			// for sending body along with error, use this way
//			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//									.body(responseDTO);
//		}
//		
//		// Validation - Business
//		
//		if (inventory.searchItem(item) != null) {
//			errors.add("Item already exist");
//		}
//		
//		if (!errors.isEmpty()) {
//			AddItemResponseDTO responseDTO = new AddItemResponseDTO();
//			responseDTO.setErrors(errors);
//			
//			// for sending body along with error, use this way
//			return ResponseEntity.status(HttpStatus.CONFLICT)
//									.body(responseDTO);
//		}
//		
//		
//		// Processing
//		
//		InventoryItem addedItem = inventory.addItem(requestDTO.getItem());
//		
//		return new ResponseEntity<AddItemResponseDTO>(
//			new AddItemResponseDTO(addedItem), HttpStatus.CREATED
//		);
//	}
	
	@GetMapping("/items")
	public ResponseEntity<GetItemsResponseDTO> getAllItems() {
		
		List<InventoryItem> items = inventory.getItems();
		
		GetItemsResponseDTO responseDTO = new GetItemsResponseDTO(
			items,
			inventory.getItemsQuantitiesTotal()
		);
		
		return new ResponseEntity<GetItemsResponseDTO>(
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
	
//	// Search Item through unique fields
//	
//	@GetMapping("/items")
//	public ResponseEntity<InventoryItem> getItem(@RequestParam String category,
//			@RequestParam String subCategory, @RequestParam String name) {
//		
//		InventoryItem item = inventory.searchItem(new InventoryItem(category, subCategory, name, 0, 0));
//		
//		return new ResponseEntity<InventoryItem>(
//			item, 
//			HttpStatus.OK
//		);
//	}
	
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
	public ResponseEntity<InventoryItem> deleteItem(@PathVariable Integer itemId) throws CloneNotSupportedException, JMSException {
		
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