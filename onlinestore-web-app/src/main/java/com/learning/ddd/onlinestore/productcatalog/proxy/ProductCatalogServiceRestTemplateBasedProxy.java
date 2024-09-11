package com.learning.ddd.onlinestore.productcatalog.proxy;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.learning.ddd.onlinestore.product.application.dto.GetProductsResponseDTO;
import com.learning.ddd.onlinestore.product.domain.Product;


@Component
public class ProductCatalogServiceRestTemplateBasedProxy {

	private static final String PRODUCT_CATALOG_SERVICE_URI 
		= "http://product-catalog-service/productcatalog/products/";
	
	@Autowired
	private RestTemplate productCatalogServiceRestTemplate;
	
//	@Bean(name = "productCatalogServiceRestTemplate")
//    @LoadBalanced
//    public RestTemplate restTemplate() {
//        return new RestTemplate();
//    }
	
	
	public List<Product> getAllProducts() {
		
		List<Product> allProducts = productCatalogServiceRestTemplate.exchange(
			PRODUCT_CATALOG_SERVICE_URI, 
			HttpMethod.GET,
			null,
			new ParameterizedTypeReference<GetProductsResponseDTO>() {}
		).getBody().getProducts();
		
		return allProducts;
	}

	public Product getProduct(Integer productId) {
		
		Product product = productCatalogServiceRestTemplate.exchange(
			PRODUCT_CATALOG_SERVICE_URI + productId, 
			HttpMethod.GET,
			null,
			new ParameterizedTypeReference<Product>() {}
		).getBody();
		
		return product;
	}
	
//	public Product getProduct(Integer productId) {
//		
//		Product product = productCatalogServiceRestTemplate.exchange(
//			"http://product-catalog-service/productcatalog/products/" + productId, 
//			HttpMethod.GET,
//			null,
//			new ParameterizedTypeReference<Product>() {}
//		).getBody();
//			
//		return product;
//	}
//	
//	public List<Product> searchProducts(Product exampleProduct) {
//		
//		SearchItemsRequestDTO requestDTO = new SearchItemsRequestDTO(exampleProduct);
//		
//		HttpEntity<SearchProductsRequestDTO> request = 
//				new HttpEntity<SearchProductsRequestDTO>(requestDTO);
//		
//		List<Product> searchedProducts = productCatalogServiceRestTemplate.exchange(
//			"http://product-catalog-service/productcatalog/products/searches", 
//			HttpMethod.POST,
//			request,
//			new ParameterizedTypeReference<SearchProductsResponseDTO>() {}
//		).getBody().getProducts();
//		
//		return searchedProducts;
//	}
	

}
