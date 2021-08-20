package com.learning.ddd.onlinestore.inventory.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.learning.ddd.onlinestore.domain.event.pubsub.DomainEventPublisher;
import com.learning.ddd.onlinestore.inventory.domain.event.ItemAddedToInventoryEvent;
import com.learning.ddd.onlinestore.inventory.domain.event.ItemRemovedFromInventoryEvent;
import com.learning.ddd.onlinestore.inventory.domain.event.ItemsAddedToInventoryEvent;
import com.learning.ddd.onlinestore.inventory.domain.exception.ItemsAlreadyExistsException;
import com.learning.ddd.onlinestore.inventory.domain.repository.InventoryItemRepository;

//What an Inventory can have and should do?
//1: Inventory contains lot of items, basically lot of Products
//2: Mart team can add batches of already supported items
//3: Mart team can add new set of items (if management decided to add new products)
//4: Mart team can remove existing items (say some problem found in some items or remove due to some problem)
//5: Mart team/Management may want to review Inventory (how many items in stock for each supported product?)
//6: Mart team/Management may want to search some/many specific item/product)
//			get a specific item by itemId
//			search item(s) by any combination of parameters of Item
@Component
public class Inventory {

	@Autowired
	private InventoryItemRepository itemRepository;

	@Autowired
	private DomainEventPublisher domainEventPublisher;

	
	public Inventory() {
	}
	
	
	@Transactional
	public InventoryItem addItem(InventoryItem item) {
		
		final InventoryItem persistedItem = itemRepository.save(item);
		
		domainEventPublisher.publishEvent(new ItemAddedToInventoryEvent(item));
		
		return persistedItem;
	}
	
	@Transactional
	public List<InventoryItem> addItems(List<InventoryItem> itemsToBeAdded) throws ItemsAlreadyExistsException {
		
		List<InventoryItem> items = new ArrayList<InventoryItem>();
		List<InventoryItem> alreadyExistingItems = new ArrayList<InventoryItem>();
		
		for (InventoryItem item : itemsToBeAdded) {
			Integer countByUniqueFields = itemRepository.countByUniqueFields(
					item.getCategory(), item.getSubCategory(), 
					item.getName(), item.getPrice(), item.getQuantity());
			if (countByUniqueFields > 0) {	
				// item exists in Inventory
				alreadyExistingItems.add(item);
			} else { //if (countByUniqueFields == 0)
				// new item
				items.add(item);
			}
		}
		
		if (!alreadyExistingItems.isEmpty() ) {
			throw new ItemsAlreadyExistsException(alreadyExistingItems);
		}
		
		if (!items.isEmpty() ) {
			items = itemRepository.saveAll(items);
			domainEventPublisher.publishEvent(new ItemsAddedToInventoryEvent(items));
		}
		
		return items;
	}

	public List<InventoryItem> getItems() {
		
		return itemRepository.findAll();
		
	}
	
	public InventoryItem getItem(final int itemId) {
		
		Optional<InventoryItem> itemFromDB = itemRepository.findById(itemId);
		//System.out.println("-------------> itemFromDB = " + itemFromDB);
		
		return ((itemFromDB != null) && itemFromDB.isPresent()) ? itemFromDB.get() : null;
	}
	
	// return items that matches any of field value of exampleItem
	// wild card search for String fields like category/subCategory/name
	public List<InventoryItem> searchItems(InventoryItem exampleItem) {
		
		return itemRepository.findByExample(exampleItem.getCategory(),
			exampleItem.getSubCategory(), exampleItem.getName(),
			exampleItem.getPrice(), exampleItem.getQuantity()
		);
		
//		return itemRepository.findAll(Example.of(exampleItem));
		
//		return getItems().stream()
//				.filter(item ->
//							item.getItemId()==exampleItem.getItemId() 
//							|| item.getCategory().equals(exampleItem.getCategory())
//							|| item.getSubCategory().equals(exampleItem.getSubCategory())
//							|| item.getName().equals(exampleItem.getName())
//							|| item.getPrice().equals(exampleItem.getPrice())
//							|| item.getQuantity()==exampleItem.getQuantity()
//						)
//				.collect(Collectors.toList());
		
	}
	
	@Transactional
	public InventoryItem updateItem(InventoryItem itemToUpdate) {
		
		return itemRepository.save(itemToUpdate);
	}
	
	@Transactional
	public void removeItem(Integer itemId) {
		
		InventoryItem item = getItem(itemId);
		
		itemRepository.deleteById(itemId);
		
		domainEventPublisher.publishEvent(new ItemRemovedFromInventoryEvent(item));
	}

	@Transactional
	public void removeItem(InventoryItem item) {
		
		itemRepository.delete(item);
		
		domainEventPublisher.publishEvent(new ItemRemovedFromInventoryEvent(item));
	}

	@Transactional
	public void removeItems(InventoryItem exampleItem) {
		
		itemRepository.deleteItems(
				exampleItem.getItemId(), exampleItem.getCategory(), exampleItem.getSubCategory(),
				exampleItem.getName(), exampleItem.getPrice(), exampleItem.getQuantity());
		
//		List<Item> items = getItems();
//		boolean anyItemRemoved = 
//				items.removeIf(item ->
//					item.getItemId()==exampleItem.getItemId() 
//					|| item.getCategory().equals(exampleItem.getCategory())
//					|| item.getSubCategory().equals(exampleItem.getSubCategory())
//					|| item.getName().equals(exampleItem.getName())
//					|| item.getPrice().equals(exampleItem.getPrice())
//					|| item.getQuantity()==exampleItem.getQuantity()
//				);
//		itemRepository.saveAll(items);		
//		domainEventPublisher.publishEvent(new ItemsRemovedFromInventoryEvent(exampleItem));
//		return anyItemRemoved;
	}

	public int getItemsQuantitiesTotal() {
		
		Integer allItemsQuantitiesTotal = itemRepository.calculateAllItemsQuantitiesTotal();
		return allItemsQuantitiesTotal != null ? allItemsQuantitiesTotal : 0;
	}


}
