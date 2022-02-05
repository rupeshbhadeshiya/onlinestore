package com.learning.ddd.onlinestore.inventory.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

//DDD: Entity
@Entity
@Table(schema="inventory")
//@Document(collection = "inventory")
public class InventoryItem implements Serializable {

	private static final long serialVersionUID = 2635836624711799146L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int itemId;				// ex. 1001, unique Id to identify an item uniquely
	
	@NotBlank(message = "Category is required")
	private String category;		// ex. Grocery
	
	@NotBlank(message = "Sub-Category is required")
	private String subCategory;		// ex. Biscuits
	
	@NotBlank(message = "Name is required")
	private String name;			// ex. Parle-G
	
	@Positive(message = "Price should be a positive decimal number")
	@DecimalMin(value = "1.0", message = "Price should be a positive decimal number")
	private Double price;			// ex. 30.0 INR (price of a single item)
	
	@Positive(message = "Quantity should be a positive number")
	@Min(value = 1, message = "Quantity should be a positive number")
	private int quantity;			// ex. 5

	
	public InventoryItem() {
	}
	
	public InventoryItem(String category, String subCategory, String name, 
			double price, int quantity) {
		this.category = category;
		this.subCategory = subCategory;
		this.name = name;
		this.price = price;
		this.quantity = quantity;
	}

	
	public void increaseQuantity(int quantityToIncrease) {
		this.quantity += quantityToIncrease;
		
	}
	public void decreaseQuantity(int quantityToDecrease) {
		this.quantity -= quantityToDecrease;
	}
	
	
	public int getItemId() {
		return itemId;
	}

	public void setItemId(int itemId) {
		this.itemId = itemId;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getSubCategory() {
		return subCategory;
	}

	public void setSubCategory(String subCategory) {
		this.subCategory = subCategory;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public Double getPrice() {
		return price;
	}
	
	public void setPrice(Double price) {
		this.price = price;
	}
	
	
	@Override
	public String toString() {
		return "InventoryItem [itemId=" + itemId + ", category=" + category
				+ ", subCategory=" + subCategory + ", name=" + name
				+ ", price=" + price + ", quantity=" + quantity + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((category == null) ? 0 : category.hashCode());
		result = prime * result + ((subCategory == null) ? 0 : subCategory.hashCode());
		//result = prime * result + itemId; // necessary fields except id field
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((price == null) ? 0 : price.hashCode());
		result = prime * result + quantity;
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
		
		InventoryItem other = (InventoryItem) obj;
		
		if (category == null) {
			if (other.category != null)
				return false;
		} else if (!category.equals(other.category))
			return false;
		
		if (subCategory == null) {
			if (other.subCategory != null)
				return false;
		} else if (!subCategory.equals(other.subCategory))
			return false;
		
		//if (itemId != other.itemId)	// compare fields which truly represent
		//	return false;				// an InventoryItem, itemId is not that field
		
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		
		if (price == null) {
			if (other.price != null)
				return false;
		} else if (!price.equals(other.price))
			return false;
		
		if (quantity != other.quantity)
			return false;
		
		return true;
	}
	
}
