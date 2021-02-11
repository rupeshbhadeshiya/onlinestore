package com.learning.ddd.onlinestore.inventory.domain;

public enum ItemCategoryAndSubCategory {

	GROCERY_BISCUIT ("Grocery", "Biscuit"),
	GROCERY_CHIVDA ("Grocery", "Chivda"),
	TOILETRIES_BATHINGSOAP ("Toiletries", "Bathing Soap");
	
	private String category;
	private String subCategory;

	ItemCategoryAndSubCategory(String category, String subCategory) {
		this.category = category;
		this.subCategory = subCategory;
	}

	public String getCategory() {
		return category;
	}
	public String getSubCategory() {
		return subCategory;
	}
	
}
