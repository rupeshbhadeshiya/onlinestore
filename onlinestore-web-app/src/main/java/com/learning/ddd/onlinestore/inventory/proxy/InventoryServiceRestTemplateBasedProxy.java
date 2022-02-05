package com.learning.ddd.onlinestore.inventory.proxy;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.learning.ddd.onlinestore.inventory.application.dto.AddItemRequestDTO;
import com.learning.ddd.onlinestore.inventory.application.dto.AddItemResponseDTO;
import com.learning.ddd.onlinestore.inventory.application.dto.GetItemsResponseDTO;
import com.learning.ddd.onlinestore.inventory.application.dto.SearchItemsRequestDTO;
import com.learning.ddd.onlinestore.inventory.application.dto.SearchItemsResponseDTO;
import com.learning.ddd.onlinestore.inventory.application.dto.UpdateItemRequestDTO;
import com.learning.ddd.onlinestore.inventory.application.dto.UpdateItemResponseDTO;
import com.learning.ddd.onlinestore.inventory.domain.InventoryItem;

@Component
public class InventoryServiceRestTemplateBasedProxy {

	@Autowired
	private RestTemplate inventoryServiceRestTemplate;
	
//	@Bean(name = "inventoryServiceRestTemplate")
//    @LoadBalanced
//    public RestTemplate restTemplate() {
//        return new RestTemplate();
//    }
	
	
	public InventoryItem addItem(InventoryItem item) {
		
		AddItemRequestDTO requestDTO = new AddItemRequestDTO(item);
		HttpEntity<AddItemRequestDTO> request = new HttpEntity<AddItemRequestDTO>(requestDTO);
		
		InventoryItem addedItem = inventoryServiceRestTemplate.exchange(
			"http://inventory-service/inventory/items", 
			HttpMethod.POST,
			request,
			new ParameterizedTypeReference<AddItemResponseDTO>() {}
		).getBody().getItem();
		
		return addedItem;
		
//		savedItems = Arrays.asList(
//			restTemplate.postForObject(
//				new URI("http://inventory-service/inventory/items"), 
//				items, 
//				InventoryItem[].class
//			)
//		);
//		return (List<Item>) responseEntity.getBody();
	}
	
	public List<InventoryItem> getAllItems() {
		
//		return new ArrayList<>();
		
		// FIXME
		
		List<InventoryItem> allItems = inventoryServiceRestTemplate.exchange(
			"http://inventory-service/inventory/items", 
			HttpMethod.GET,
			null,
			new ParameterizedTypeReference<GetItemsResponseDTO>() {}
		).getBody().getItems();
		
		return allItems;
	}
	
	public InventoryItem getItem(Integer itemId) {
		
		InventoryItem item = inventoryServiceRestTemplate.exchange(
			"http://inventory-service/inventory/items/" + itemId, 
			HttpMethod.GET,
			null,
			new ParameterizedTypeReference<InventoryItem>() {}
		).getBody();
			
		return item;
	}
	
	public List<InventoryItem> searchItems(InventoryItem exampleItem) {
		
		SearchItemsRequestDTO requestDTO = new SearchItemsRequestDTO(exampleItem);
		
		HttpEntity<SearchItemsRequestDTO> request = 
				new HttpEntity<SearchItemsRequestDTO>(requestDTO);
		
		List<InventoryItem> searchedItems = inventoryServiceRestTemplate.exchange(
			"http://inventory-service/inventory/items/searches", 
			HttpMethod.POST,
			request,
			new ParameterizedTypeReference<SearchItemsResponseDTO>() {}
		).getBody().getItems();
		
		return searchedItems;
	}
	
	public InventoryItem updateItem(InventoryItem itemToUpdate) {
		
		UpdateItemRequestDTO requestDTO = new UpdateItemRequestDTO(itemToUpdate);
		
		HttpEntity<UpdateItemRequestDTO> request = 
				new HttpEntity<UpdateItemRequestDTO>(requestDTO);
		
		UpdateItemResponseDTO responseDTO = inventoryServiceRestTemplate.exchange(
			"http://inventory-service/inventory/items/" + itemToUpdate.getItemId(), 
			HttpMethod.PUT,
			request,
			new ParameterizedTypeReference<UpdateItemResponseDTO>() {}
		).getBody();
			
		return responseDTO.getItem();
	}

	public void removeItem(Integer itemId) {
		
		inventoryServiceRestTemplate.delete("http://inventory-service/inventory/items/"+itemId);
	}

}
