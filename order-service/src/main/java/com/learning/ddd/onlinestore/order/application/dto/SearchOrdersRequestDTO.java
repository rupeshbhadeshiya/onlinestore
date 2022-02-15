package com.learning.ddd.onlinestore.order.application.dto;

import java.io.Serializable;

public class SearchOrdersRequestDTO implements Serializable {

	private static final long serialVersionUID = -3197471656365920992L;

	private String orderNumber;
	private String transactionNumber;
	private String purchaseDate;
	private String paymentMethod;
	private String itemDetails;
	private String addressDetails;
	private String merchantName;

	
	public SearchOrdersRequestDTO() {
	}


	public String getOrderNumber() {
		return orderNumber;
	}


	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}


	public String getPurchaseDate() {
		return purchaseDate;
	}


	public void setPurchaseDate(String purchaseDate) {
		this.purchaseDate = purchaseDate;
	}


	public String getTransactionNumber() {
		return transactionNumber;
	}


	public void setTransactionNumber(String transactionNumber) {
		this.transactionNumber = transactionNumber;
	}


	public String getPaymentMethod() {
		return paymentMethod;
	}


	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}


	public String getItemDetails() {
		return itemDetails;
	}


	public void setItemDetails(String itemDetails) {
		this.itemDetails = itemDetails;
	}


	public String getAddressDetails() {
		return addressDetails;
	}


	public void setAddressDetails(String addressDetails) {
		this.addressDetails = addressDetails;
	}


	public String getMerchantName() {
		return merchantName;
	}


	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}


	@Override
	public String toString() {
		return "SearchOrdersRequestDTO [orderNumber=" + orderNumber 
				+ ", transactionNumber=" + transactionNumber
				+ ", purchaseDate=" + purchaseDate
				+ ", paymentMethod=" + paymentMethod 
				+ ", itemDetails=" + itemDetails 
				+ ", addressDetails=" + addressDetails 
				+ ", merchantName=" + merchantName + "]";
	}

}
