package com.learning.ddd.onlinestore.inventory_management.domain;

public enum ItemCategorySubCategory {

	GROCERY_BISCUIT ("Grocery", "Biscuit"),
	GROCERY_CHIVDA ("Grocery", "Chivda"),
	TOILETRIES_BATHINGSOAP ("Toiletries", "Bathing Soap");
	
	private String category;
	private String subCategory;

	ItemCategorySubCategory(String category, String subCategory) {
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
