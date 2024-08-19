package com.learning.ddd.onlinestore.productcatalog.domain;

import java.io.Serializable;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.learning.ddd.onlinestore.inventory.domain.Product;

//DDD: Entity
@Entity
@Table(schema="productcatalog")
//@Document(collection = "productcatalog")
public class ProductCatalogItem implements Serializable {

	private static final long serialVersionUID = -4475065436816315823L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int itemId;				// ex. 1001, unique Id to identify an item uniquely
	
	@Embedded
	private Product product;
	
//	@NotBlank(message = "Category is required")
//	private String category;		// ex. Grocery
//	
//	@NotBlank(message = "Sub-Category is required")
//	private String subCategory;		// ex. Biscuits
//	
//	@NotBlank(message = "Name is required")
//	private String name;			// ex. Parle-G
//	
//	@Positive(message = "Price should be a positive decimal number")
//	@DecimalMin(value = "1.0", message = "Price should be a positive decimal number")
//	private Double price;			// ex. 30.0 INR (price of a single item)
//	
//	@Positive(message = "Quantity should be a positive number")
//	@Min(value = 1, message = "Quantity should be a positive number")
//	private int quantity;			// ex. 5

	
	public ProductCatalogItem() {
		
		super();
		this.setProduct(new Product());
	}
	
	public ProductCatalogItem(int productId, String category, String subCategory, 
			String name, double price, int quantity) {
		
		super();
		this.setProduct(new Product(
			productId, category, subCategory, 
			name, price, quantity
		));
	}

	public ProductCatalogItem(Product product) {

		super();
		this.setProduct(new Product(product));
	}
	
	public ProductCatalogItem(ProductCatalogItem inventoryItem) {
		
		this(inventoryItem.getProduct());
		
//		this.category = inventoryItem.category;
//		this.subCategory = inventoryItem.subCategory;
//		this.name = inventoryItem.name;
//		this.price = inventoryItem.price;
//		this.quantity = inventoryItem.quantity;
	}

	public Product getProduct() {
		return product;
	}
	
	public void setProduct(Product product) {
		this.product = product;
	}
	
	public int getItemId() {
		return itemId;
	}

	public void setItemId(int itemId) {
		this.itemId = itemId;
	}

	public String getName() {
		return this.getProduct().getName();
	}
	
	public void setName(String name) {
		this.getProduct().setName(name);
	}
	
	public String getCategory() {
		return this.getProduct().getCategory();
	}

	public void setCategory(String category) {
		this.getProduct().setCategory(category);
	}

	public String getSubCategory() {
		return this.getProduct().getSubCategory();
	}

	public void setSubCategory(String subCategory) {
		this.getProduct().setSubCategory(subCategory);
	}

	public int getQuantity() {
		return this.getProduct().getQuantity();
	}

	public void setQuantity(int quantity) {
		this.getProduct().setQuantity(quantity);
	}

	public Double getPrice() {
		return this.getProduct().getPrice();
	}

	public void setPrice(Double price) {
		this.getProduct().setPrice(price);
	}
	
	//-------- specific methods : start -------------
	
	public void increaseQuantity(int quantityToIncrease) {
		this.getProduct().increaseQuantity(quantityToIncrease);
		
	}
	public void decreaseQuantity(int quantityToDecrease) {
		this.getProduct().decreaseQuantity(quantityToDecrease);
	}
	
	public double computeAmount() {
		return this.getProduct().computeAmount();
	}
	
	//-------- specific methods : end -------------
	
	@Override
	public String toString() {
		return "ProductCatalogItem [id=" + itemId + ", " + this.getProduct() + "]";
	}

	@Override
	public int hashCode() {
		return this.getProduct().hashCode();
	}

	@Override
	public boolean equals(Object other) {
		
		if (this == other)
			return true;
		if (other == null)
			return false;
		if (getClass() != other.getClass())
			return false;
		
		ProductCatalogItem otherInventoryItem = (ProductCatalogItem) other;
		
		return this.getProduct().equals(otherInventoryItem.getProduct());
	}
	
	@Override
	public ProductCatalogItem clone() throws CloneNotSupportedException {
		return new ProductCatalogItem(this);
	}
	
}
