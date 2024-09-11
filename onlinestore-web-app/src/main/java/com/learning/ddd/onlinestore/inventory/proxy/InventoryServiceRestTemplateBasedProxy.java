package com.learning.ddd.onlinestore.inventory.proxy;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.learning.ddd.onlinestore.product.application.dto.GetProductsResponseDTO;
import com.learning.ddd.onlinestore.product.application.dto.SingleProductRequestRequestDTO;
import com.learning.ddd.onlinestore.product.domain.Product;

@Component
public class InventoryServiceRestTemplateBasedProxy {

	private static final String INVENTORY_SERVICE_PRODUCTS_URI = 
			"http://inventory-service/inventory/products/";
	
	@Autowired
	private RestTemplate inventoryServiceRestTemplate;
	
//	@Bean(name = "inventoryServiceRestTemplate")
//    @LoadBalanced
//    public RestTemplate restTemplate() {
//        return new RestTemplate();
//    }
	
	
	public Product addProduct(Product product) {
		
		SingleProductRequestRequestDTO requestDTO = new SingleProductRequestRequestDTO(product);
		HttpEntity<SingleProductRequestRequestDTO> request = new HttpEntity<SingleProductRequestRequestDTO>(requestDTO);
		
		Product addedProduct = inventoryServiceRestTemplate.exchange(
			"http://inventory-service/inventory/products", 
			HttpMethod.POST,
			request,
			new ParameterizedTypeReference<SingleProductRequestRequestDTO>() {}
		).getBody().getProduct();
		
		return addedProduct;
	}
	
	public List<Product> getAllAvailableProducts() {
		
		List<Product> products = inventoryServiceRestTemplate.exchange(
			"http://inventory-service/inventory/products", 
			HttpMethod.GET,
			null,
			new ParameterizedTypeReference<GetProductsResponseDTO>() {}
		).getBody().getProducts();
		
		return products;
	}
	
	public Product getProduct(Integer productId) {
		
		Product product = inventoryServiceRestTemplate.exchange(
			INVENTORY_SERVICE_PRODUCTS_URI + productId, 
			HttpMethod.GET,
			null,
			new ParameterizedTypeReference<Product>() {}
		).getBody();
			
		return product;
	}
	
	public Product updateProduct(Product productToUpdate) {
		
		SingleProductRequestRequestDTO requestDTO = new SingleProductRequestRequestDTO(productToUpdate);
		
		HttpEntity<SingleProductRequestRequestDTO> request = 
				new HttpEntity<SingleProductRequestRequestDTO>(requestDTO);
		
		SingleProductRequestRequestDTO responseDTO = inventoryServiceRestTemplate.exchange(
			INVENTORY_SERVICE_PRODUCTS_URI + productToUpdate.getProductId(), 
			HttpMethod.PUT,
			request,
			new ParameterizedTypeReference<SingleProductRequestRequestDTO>() {}
		).getBody();
			
		return responseDTO.getProduct();
	}	
	
	public void removeProduct(Integer productId) {
		
		inventoryServiceRestTemplate.delete(INVENTORY_SERVICE_PRODUCTS_URI + productId);
	}
	
	
	public List<Product> searchProducts(Product exampleProduct) {
		
		SingleProductRequestRequestDTO requestDTO = 
			new SingleProductRequestRequestDTO(exampleProduct);
		
		HttpEntity<SingleProductRequestRequestDTO> request = 
			new HttpEntity<SingleProductRequestRequestDTO>(requestDTO);
		
		List<Product> searchedProducts = inventoryServiceRestTemplate.exchange(
			INVENTORY_SERVICE_PRODUCTS_URI + "/searches", 
			HttpMethod.POST,
			request,
			new ParameterizedTypeReference<GetProductsResponseDTO>() {}
		).getBody().getProducts();
		
		return searchedProducts;
	}
	
}
