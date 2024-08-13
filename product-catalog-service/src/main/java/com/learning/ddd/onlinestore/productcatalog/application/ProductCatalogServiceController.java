package com.learning.ddd.onlinestore.productcatalog.application;

import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.learning.ddd.onlinestore.productcatalog.application.dto.GetProductsResponseDTO;
import com.learning.ddd.onlinestore.productcatalog.domain.Product;
import com.learning.ddd.onlinestore.productcatalog.domain.repository.ProductCatalogRepository;

@RestController
@RequestMapping("/productcatalog")
@Validated
public class ProductCatalogServiceController { //must be in root package of project
	
	@Autowired
	private ProductCatalogRepository productcatalogRepository;
	
	@GetMapping("/products")
	public ResponseEntity<GetProductsResponseDTO> getAllProducts() {
		
		List<Product> products = productcatalogRepository.findAll();
		
		GetProductsResponseDTO responseDTO = new GetProductsResponseDTO(
			products, products.size()
		);
		
		return new ResponseEntity<GetProductsResponseDTO>(
			responseDTO, 
			HttpStatus.OK
		);
	}
	
	@GetMapping("/number") // a ping kind of api to know things working fine!
	public Integer produceRandomNumber() {
		
		return new Random().nextInt();
	}
	
}