package com.learning.ddd.onlinestore.inventory.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.learning.ddd.onlinestore.commons.domain.event.DomainEventsPublisher;
import com.learning.ddd.onlinestore.commons.domain.event.DummyDomainEventsPublisher;
import com.learning.ddd.onlinestore.inventory.domain.event.ItemAddedToInventoryEvent;
import com.learning.ddd.onlinestore.inventory.domain.event.ItemRemovedFromInventoryEvent;
import com.learning.ddd.onlinestore.inventory.domain.event.ItemsAddedToInventoryEvent;
import com.learning.ddd.onlinestore.inventory.domain.event.ItemsRemovedFromInventoryEvent;

//What an Inventory can have and should do?
//1: Inventory contains lot of items, basically lot of Products
//2: Mart team can add batches of already supported items
//3: Mart team can add new set of items (if management decided to add new products)
//4: Mart team can remove existing items (say some problem found in some items or remove due to some problem)
//5: Mart team/Management may want to review Inventory (how many items in stock for each supported product?)
//6: Mart team/Management may want to search some/many specific item/product)
//			get a specific item by itemId
//			search item(s) by any combination of parameters of Item
public class Inventory {

	private List<Item> items;
	private DomainEventsPublisher domainEventPublisher;

	public Inventory() {
		items = new ArrayList<>();
		domainEventPublisher = new DummyDomainEventsPublisher();
	}
	
	public void setDomainEventPublisher(DomainEventsPublisher domainEventPublisher) {
		this.domainEventPublisher = domainEventPublisher;
	}
	
	public List<Item> getItems() {
		return items;
	}

	public void addItem(Item item) {
		items.add(item);	
		domainEventPublisher.publishEvent(new ItemAddedToInventoryEvent(item));
	}
	
	public void addItems(List<Item> newItems) {
		items.addAll(newItems);	
		domainEventPublisher.publishEvent(new ItemsAddedToInventoryEvent(newItems));
	}

	public Item searchItem(final int itemId) {
		return items.stream()
				.filter(item -> item.getItemId() == itemId)
				.findFirst()
				.get();
	}
	
	// return items that matches any of field value of exampleItem
	// wild card search for String fields like category/subCategory/name
	public List<Item> searchItems(Item exampleItem) {
		
		return items.stream()
					.filter(item ->
								item.getItemId()==exampleItem.getItemId() 
								|| item.getCategory().equals(exampleItem.getCategory())
								|| item.getSubCategory().equals(exampleItem.getSubCategory())
								|| item.getName().equals(exampleItem.getName())
								|| item.getPrice().equals(exampleItem.getPrice())
								|| item.getQuantity()==exampleItem.getQuantity()
							)
					.collect(Collectors.toList());
	}

	public boolean removeItem(Item item) {
		final boolean isItemRemoved = items.remove(item);
		if (isItemRemoved) {
			domainEventPublisher.publishEvent(new ItemRemovedFromInventoryEvent(item));
		}
		return isItemRemoved;
	}

	public void removeItems(Item exampleItem) {
		
		final boolean areItemsRemoved = 
				items.removeIf(item ->
							item.getItemId()==exampleItem.getItemId() 
							|| item.getCategory().equals(exampleItem.getCategory())
							|| item.getSubCategory().equals(exampleItem.getSubCategory())
							|| item.getName().equals(exampleItem.getName())
							|| item.getPrice().equals(exampleItem.getPrice())
							|| item.getQuantity()==exampleItem.getQuantity()
						);
		
		if (areItemsRemoved) {
			domainEventPublisher.publishEvent(new ItemsRemovedFromInventoryEvent(exampleItem));
		}
	}

}
