package com.learning.ddd.onlinestore.inventory.domain;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.learning.ddd.onlinestore.commons.domain.event.DomainEventsPublisher;
import com.learning.ddd.onlinestore.inventory.domain.event.ItemAddedToInventoryEvent;
import com.learning.ddd.onlinestore.inventory.domain.event.ItemRemovedFromInventoryEvent;
import com.learning.ddd.onlinestore.inventory.domain.event.ItemsAddedToInventoryEvent;
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

	//private List<Item> items;
	@Autowired
	private DomainEventsPublisher domainEventPublisher;
	
	@Autowired
	private InventoryItemRepository itemRepository;


	public Inventory() {
		//domainEventPublisher = new DummyDomainEventsPublisher();
	}
	
	
	public void setItemRepository(InventoryItemRepository itemRepository) {
		this.itemRepository = itemRepository;
	}
	public void setDomainEventPublisher(DomainEventsPublisher domainEventPublisher) {
		this.domainEventPublisher = domainEventPublisher;
	}
	
	public InventoryItem addItem(InventoryItem item) {
		
		final InventoryItem persistedItem = itemRepository.save(item);
		
		domainEventPublisher.publishEvent(new ItemAddedToInventoryEvent(item));
		
		return persistedItem;
	}
	
	public List<InventoryItem> addItems(List<InventoryItem> items) {
		
		List<InventoryItem> persistedItems = itemRepository.saveAll(items);
		
		domainEventPublisher.publishEvent(new ItemsAddedToInventoryEvent(persistedItems));
		
		return persistedItems;
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
		
		return getItems().stream()
				.filter(item ->
							item.getItemId()==exampleItem.getItemId() 
							|| item.getCategory().equals(exampleItem.getCategory())
							|| item.getSubCategory().equals(exampleItem.getSubCategory())
							|| item.getName().equals(exampleItem.getName())
							|| item.getPrice().equals(exampleItem.getPrice())
							|| item.getQuantity()==exampleItem.getQuantity()
						)
				.collect(Collectors.toList());
		
		
//		List<Item> searchedItems = new ArrayList<>();
//		
//		List<Item> items = itemRepository.findAll();
//		for (Item item : items) {
//			//System.out.println("\n\n~~~~~~searchItem(itemId): item.getItemId()="+item.getItemId()+ ", itemId="+itemId+ "\n");
//			
////			System.out.println("\n\n~~~~~~searchItems(exampleItem): item.getItemId()="+item.getItemId()+ ", exampleItem.getItemId()="+exampleItem.getItemId() + ", Outcome="+(item.getItemId() == exampleItem.getItemId()));
////			System.out.println("~~~~~~searchItems(exampleItem): item.getCategory()="+item.getCategory()+ ", exampleItem.getCategory()="+exampleItem.getCategory() + ", Outcome="+(item.getCategory().equals(exampleItem.getCategory())));
////			System.out.println("~~~~~~searchItems(exampleItem): item.getSubCategory()="+item.getSubCategory()+ ", exampleItem.getSubCategory()="+exampleItem.getSubCategory() + ", Outcome="+(item.getSubCategory().equals(exampleItem.getSubCategory())));
////			System.out.println("~~~~~~searchItems(exampleItem): item.getName()="+item.getName()+ ", exampleItem.getName()="+exampleItem.getName() + ", Outcome="+(item.getName().equals(exampleItem.getName())));
////			System.out.println("~~~~~~searchItems(exampleItem): item.getPrice()="+item.getPrice()+ ", exampleItem.getPrice()="+exampleItem.getPrice() + ", Outcome="+(item.getPrice().equals(exampleItem.getPrice())));
////			System.out.println("~~~~~~searchItems(exampleItem): item.getQuantity()="+item.getQuantity()+ ", exampleItem.getQuantity()="+exampleItem.getQuantity() + ", Outcome="+(item.getQuantity() == exampleItem.getQuantity())+ "\n");
//			
//			if ((item.getItemId() == exampleItem.getItemId()) 
//					|| item.getCategory().equals(exampleItem.getCategory())
//					|| item.getSubCategory().equals(exampleItem.getSubCategory())
//					|| item.getName().equals(exampleItem.getName())
//					|| item.getPrice().equals(exampleItem.getPrice())
//					|| (item.getQuantity()==exampleItem.getQuantity())
//				){
//				
//				searchedItems.add(item);
//			}
//		}
//		
//		return searchedItems;
		
	}
	
	public void removeItem(Integer itemId) {
		
		InventoryItem item = getItem(itemId);
		
		itemRepository.deleteById(itemId);
		
		domainEventPublisher.publishEvent(new ItemRemovedFromInventoryEvent(item));
	}

	//@Transactional
	public void removeItem(InventoryItem item) {
		
		itemRepository.delete(item);
		
		domainEventPublisher.publishEvent(new ItemRemovedFromInventoryEvent(item));
	}

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
