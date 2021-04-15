package com.learning.ddd.onlinestore.shopping.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

//DDD: Entity / Aggregate Root
@Entity
public class Cart {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int cartId;			// Unique reference to each Cart
	private String consumerId;	// Consumer currently associated with this Cart
	private int itemCount;
	
	@OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, 
				fetch = FetchType.EAGER, orphanRemoval = true)
	private List<Item> items;
	
	public Cart() {
	}
	
	public Cart(String consumerId) {
		this.consumerId = consumerId;
		this.items = new ArrayList<>();
	}
	
	// addChild(Child child) {
    //    child.setParent(this);
    //    children.add(child);
    // }
	public void addItem(Item item) {
		item.setCart(this);					// very important to set bi-directional relationship
		items.add(item);					// very important to set bi-directional relationship
		itemCount += item.getQuantity();
	}
	
	// removeChild(Child child) {
    //    children.remove(child);
    //    child.setParent(null);
    // }
	public void removeItem(Item item) {
		itemCount -= item.getQuantity();
		items.remove(item);					// very important to remove bi-directional relationship
		item.setCart(null);					// very important to remove bi-directional relationship
	}
	
	public void addItems(List<Item> items) {
		for (Item item : items) {
			addItem(item);
		}
	}

	public Double computeAmount() {
		double totalAmount = 0.0; 
		for (Item item : items) {
			totalAmount += item.getPrice() * item.getQuantity();
		}
		return totalAmount;
	}
	
	public int getCartId() {
		return cartId;
	}
	public void setCartId(int cartId) {
		this.cartId = cartId;
	}

	public String getConsumerId() {
		return consumerId;
	}
	public void setConsumerId(String consumerId) {
		this.consumerId = consumerId;
	}

	public List<Item> getItems() {
		return items;
	}
	public void setItems(List<Item> items) {
		this.items = items;
	}

	public int getItemCount() {
		return itemCount;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + cartId;
		result = prime * result + ((consumerId == null) ? 0 : consumerId.hashCode());
		result = prime * result + itemCount;
		result = prime * result + ((items == null) ? 0 : items.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Cart other = (Cart) obj;
		if (cartId != other.cartId)
			return false;
		if (consumerId == null) {
			if (other.consumerId != null)
				return false;
		} else if (!consumerId.equals(other.consumerId))
			return false;
		if (itemCount != other.itemCount)
			return false;
		if (items == null) {
			if (other.items != null)
				return false;
		} else if (!items.equals(other.items))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Cart [cartId=" + cartId + ", consumerId=" + consumerId 
					+ ", itemCount=" + itemCount
					+ ", items=" + items + "]";
	}

}
