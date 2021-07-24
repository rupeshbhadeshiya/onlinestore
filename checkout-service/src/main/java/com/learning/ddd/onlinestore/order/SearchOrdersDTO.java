package com.learning.ddd.onlinestore.order;

import java.util.Date;

public class SearchOrdersDTO {

	private String searchText;
	private Date orderPlacedDate;
	

	public SearchOrdersDTO() {
		super();
	}
	
	public SearchOrdersDTO(String searchText, Date orderPlacedDate) {
		super();
		this.searchText = searchText;
		this.orderPlacedDate = orderPlacedDate;
	}
	
	public String getSearchText() {
		return searchText;
	}

	public void setSearchText(String searchText) {
		this.searchText = searchText;
		
	}

	public Date getOrderPlacedDate() {
		return orderPlacedDate;
	}
	
	public void setOrderPlacedDate(Date orderPlacedDate) {
		this.orderPlacedDate = orderPlacedDate;
		
	}

	@Override
	public String toString() {
		return "SearchOrdersDTO [searchText=" + searchText 
				+ ", orderPlacedDate=" + orderPlacedDate + "]";
	}
	
	

}
