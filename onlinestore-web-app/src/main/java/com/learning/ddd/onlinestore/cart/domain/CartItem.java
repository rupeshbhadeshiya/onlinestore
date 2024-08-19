package com.learning.ddd.onlinestore.cart.domain;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.learning.ddd.onlinestore.inventory.domain.Product;

@Entity
//@Table(schema="carts")
public class CartItem implements Serializable {

	private static final long serialVersionUID = 5472900313173264495L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int itemId;				// ex. 1001, unique Id to identify an item uniquely
	
	
	@Embedded
	private Product product;
	
//	private int itemId;				// ex. 1001, unique Id to identify an item uniquely
//	private String category;		// ex. Grocery
//	private String subCategory;		// ex. Biscuits
//	private String name;			// ex. Parle-G
//	private Double price;			// ex. 30.0 INR (price of a single item)
//	private int quantity;			// ex. 5
	
	// Ignore 
	@JsonIgnore
	// all Cascades except 'REMOVE' that's why not mentioning ALL
	@ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
	//@ManyToOne(fetch = FetchType.LAZY) // <- this created big problem!
	@JoinColumn(name = "cartId")		 // <- this created big problem!
	private Cart cart;

	
	public CartItem() {
		
		super();
		this.setProduct(new Product());
	}
	
//	public CartItem(String category, String subCategory, String name, 
//			Double price, int quantity) {
//		
//		super();
//		this.product.setCategory(category);
//		this.product.setSubCategory(subCategory);
//		this.product.setName(name);
//		this.product.setPrice(price);
//		this.product.setQuantity(quantity);
//	}

	public CartItem(Product product) {

		super();
		this.setProduct(new Product(product));
	}
	
	public CartItem(CartItem cartItem) {
		
		this(cartItem.getProduct());
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

	public Cart getCart() {
		return cart;
	}
	
	public void setCart(Cart cart) {
		this.cart = cart;
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
		
		//if (id != other.id)
		//	return false;
		
		//if (cart == null) {
		//	if (other.cart != null)
		//		return false;
		//} else if (!cart.equals(other.cart))
		//	return false;
		
		CartItem otherCartItem = (CartItem) other;
		
		return this.getProduct().equals(otherCartItem.getProduct());
	}

	@Override
	public String toString() {
		return "CartItem [id=" + itemId
				+ this.getProduct()
				+ "]";
	}
	
	
	@Override
	public CartItem clone() throws CloneNotSupportedException {
		return new CartItem(this);
	}

}
