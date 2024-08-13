package com.learning.ddd.onlinestore.inventory.domain;

public enum ItemCategorySubCategory {

	GROCERY_BISCUIT ("Grocery", "Biscuit"),
	GROCERY_CHIVDA ("Grocery", "Chivda"),
	TOILETRIES_BATHINGSOAP ("Toiletries", "Bathing Soap"),
	STATIONERY_PENCIL ("Stationery", "Pencil");
	
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
	
	public static boolean isCategoryPresent(String category) {
		
		return (
			GROCERY_BISCUIT.getCategory().equals(category) 
			|| GROCERY_CHIVDA.getCategory().equals(category)
			|| TOILETRIES_BATHINGSOAP.getCategory().equals(category)
			|| STATIONERY_PENCIL.getCategory().equals(category)
		);
	}
	
	public static boolean isSubCategoryPresent(String subCategory) {
	
		return (
			GROCERY_BISCUIT.getSubCategory().equals(subCategory) 
			|| GROCERY_CHIVDA.getSubCategory().equals(subCategory)
			|| TOILETRIES_BATHINGSOAP.getSubCategory().equals(subCategory)
			|| STATIONERY_PENCIL.getSubCategory().equals(subCategory)
		);
	}
	
}
