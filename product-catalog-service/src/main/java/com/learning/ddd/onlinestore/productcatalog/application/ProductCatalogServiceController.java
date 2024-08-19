package com.learning.ddd.onlinestore.productcatalog.application;

import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.learning.ddd.onlinestore.inventory.application.dto.GetProductsResponseDTO;
import com.learning.ddd.onlinestore.inventory.domain.Product;
import com.learning.ddd.onlinestore.productcatalog.domain.service.ProductCatalogService;

@RestController
@RequestMapping("/productcatalog")
@Validated
public class ProductCatalogServiceController { //must be in root package of project
	
	@Autowired
	private ProductCatalogService productCatalogService;
	
	@GetMapping("/products")
	public ResponseEntity<GetProductsResponseDTO> getAllProducts() {
		
		List<Product> allProducts = productCatalogService.getAllProducts();
		
		return new ResponseEntity<GetProductsResponseDTO>(
			new GetProductsResponseDTO(allProducts), 
			HttpStatus.OK
		);
	}
	
	@GetMapping("/products/{productId}")
	public ResponseEntity<Product> getProduct(@PathVariable Integer productId) {
		
		Product product = productCatalogService.getProduct(productId);
		
		return new ResponseEntity<Product>(
			product, 
			HttpStatus.OK
		);
	}
	
	@GetMapping("/number") // a ping kind of api to know things working fine!
	public Integer produceRandomNumber() {
		
		return new Random().nextInt();
	}
	
}