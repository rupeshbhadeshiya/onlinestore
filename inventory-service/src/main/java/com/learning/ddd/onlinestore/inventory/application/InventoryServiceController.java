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

import com.learning.ddd.onlinestore.inventory.application.dto.GetProductsResponseDTO;
import com.learning.ddd.onlinestore.inventory.application.dto.SingleProductRequestRequestDTO;
import com.learning.ddd.onlinestore.inventory.application.dto.UpdateItemRequestDTO;
import com.learning.ddd.onlinestore.inventory.domain.Inventory;
import com.learning.ddd.onlinestore.inventory.domain.InventoryItem;
import com.learning.ddd.onlinestore.inventory.domain.Product;
import com.learning.ddd.onlinestore.inventory.domain.exception.ProductAlreadyExistsException;

@RestController
@RequestMapping("/inventory")
@Validated
public class InventoryServiceController { //must be in root package of project
	
	@Autowired
	private Inventory inventory;
	
	
	@PostMapping("/products")
	public ResponseEntity<SingleProductRequestRequestDTO> addProduct(
		@Valid @RequestBody SingleProductRequestRequestDTO requestDTO) throws ProductAlreadyExistsException, JMSException {
		
		Product addedProduct = inventory.addProduct(requestDTO.getProduct());
		
		return new ResponseEntity<SingleProductRequestRequestDTO>(
			new SingleProductRequestRequestDTO(addedProduct), HttpStatus.CREATED
		);

	}
	
	@GetMapping("/products/{productId}")
	public ResponseEntity<Product> getProduct(@PathVariable Integer productId) {
		
		Product product = inventory.getProduct(productId);
		
		return new ResponseEntity<Product>(
			product, 
			HttpStatus.OK
		);
	}
	
	@GetMapping("/products")
	public ResponseEntity<GetProductsResponseDTO> getAllAvailableProducts() {
		
		return new ResponseEntity<GetProductsResponseDTO>(
			new GetProductsResponseDTO(inventory.getAvailableProducts()), 
			HttpStatus.OK
		);
	}
	
	@PostMapping("/products/searches")
	public ResponseEntity<GetProductsResponseDTO> searchProductsByExample(
		@Valid @RequestBody SingleProductRequestRequestDTO requestDTO) throws ProductAlreadyExistsException, JMSException {
		
		List<Product> products = inventory.searchProductsByExample(requestDTO.getProduct());
		
		return new ResponseEntity<GetProductsResponseDTO>(
			new GetProductsResponseDTO(products), 
			HttpStatus.OK
		);

	}

	@PutMapping("/products/{productId}")
	public ResponseEntity<SingleProductRequestRequestDTO> updateProduct(
			@PathVariable Integer productId,
			@RequestBody UpdateItemRequestDTO requestDTO) {
		
		InventoryItem itemToUpdate = requestDTO.getItem();
		Product productToUpdate = inventory.updateProduct(itemToUpdate.getProduct());
		
		System.out.println(
  			"--------------------- updateItem() --------------------\n"
				+ " itemToUpdate = " + itemToUpdate
				+ " productToUpdate = " + productToUpdate
  			+ "\n--------------------------------------------------");
		
		SingleProductRequestRequestDTO responseDTO = 
			new SingleProductRequestRequestDTO(productToUpdate);
		
		return new ResponseEntity<SingleProductRequestRequestDTO>(
			responseDTO, 
			HttpStatus.OK
		);
	}
	
	@DeleteMapping("/products/{productId}")
	@Transactional
	public ResponseEntity<Product> deleteProduct(
			@PathVariable Integer productId) throws CloneNotSupportedException, JMSException {
		
		inventory.removeProduct(productId);
		
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	@GetMapping("/number") // a ping kind of api to know things working fine!
	public Integer produceRandomNumber() {
		
		return new Random().nextInt();
	}
	
}