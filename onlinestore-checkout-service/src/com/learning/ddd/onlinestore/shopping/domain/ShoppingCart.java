package com.learning.ddd.onlinestore.shopping.domain;

import java.util.ArrayList;
import java.util.List;

import com.learning.ddd.onlinestore.commons.domain.event.DomainEventsPublisher;
import com.learning.ddd.onlinestore.inventory.domain.Item;
import com.learning.ddd.onlinestore.shopping.domain.event.ItemAddedToCartEvent;
import com.learning.ddd.onlinestore.shopping.domain.event.ItemQuantityDecreasedInCartEvent;
import com.learning.ddd.onlinestore.shopping.domain.event.ItemQuantityIncreasedInCartEvent;
import com.learning.ddd.onlinestore.shopping.domain.event.ItemRemovedFromCartEvent;
import com.learning.ddd.onlinestore.shopping.domain.exception.ItemNotFoundInCartException;
import com.learning.ddd.onlinestore.shopping.domain.exception.ItemRemovalFromCartFailedException;

//DDD: Entity / Aggregate Root?
public class ShoppingCart {

	private List<Item> items;
	private int itemCount;
	private DomainEventsPublisher domainEventPublisher;
	
	public ShoppingCart() {
		items = new ArrayList<>();
		itemCount = 0;
	}

	public List<Item> getItems() {
		return items;
	}

	public int getItemCount() {
		return itemCount;
	}

	public Item getItem(int itemId) {
		for (Item item : items) {
			if (item.getItemId() == itemId) {
				return item;
			}
		}
		return null;
	}
	
	public void addItem(Item item) {
		items.add(item);
		itemCount += item.getQuantity();
		domainEventPublisher.publishEvent(new ItemAddedToCartEvent(item));
	}

	public void removeItem(Item item) {
		itemCount -= item.getQuantity();
		items.remove(item);
		domainEventPublisher.publishEvent(new ItemRemovedFromCartEvent(item));
	}

	public void increaseItemQuantity(int itemId, int quantityToIncrease) {
		Item item;
		if ((item = getItem(itemId)) != null) {
			item.increaseQuantity(quantityToIncrease);
			itemCount += quantityToIncrease;
			domainEventPublisher.publishEvent(new ItemQuantityIncreasedInCartEvent(itemId, quantityToIncrease));
		} else {
			throw new ItemNotFoundInCartException(itemId);
		}
	}

	public void decreaseItemQuantity(int itemId, int quantityToDecrease) {
		Item item;
		if ((item = getItem(itemId)) != null) {
			if (item.getQuantity() >= quantityToDecrease) {
				item.decreaseQuantity(quantityToDecrease);
				itemCount -= quantityToDecrease;
				domainEventPublisher.publishEvent(new ItemQuantityDecreasedInCartEvent(itemId, quantityToDecrease));
				if (item.getQuantity() == 0) {
					items.remove(item);
					domainEventPublisher.publishEvent(new ItemRemovedFromCartEvent(item));
				}
			} else {
				throw new ItemRemovalFromCartFailedException(itemId, quantityToDecrease);
			}
		} else {
			throw new ItemNotFoundInCartException(itemId);
		}
	}

	public Double computeAmount() {
		double amount = 0.0;
		for (Item item : items) {
			amount += (item.getQuantity() * item.getPrice());
		}
		return amount;
	}
	
	public void setDomainEventPublisher(DomainEventsPublisher domainEventPublisher) {
		this.domainEventPublisher = domainEventPublisher;
	}

}
