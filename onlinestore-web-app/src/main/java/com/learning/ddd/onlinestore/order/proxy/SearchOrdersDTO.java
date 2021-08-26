package com.learning.ddd.onlinestore.order.proxy;

import java.io.Serializable;

public class SearchOrdersDTO implements Serializable {

	private static final long serialVersionUID = -4117530816685311198L;

	private String orderNumber;
	private String transactionNumber;
	private String purchaseDate;
	private String paymentMethod;
	private String merchantName;
	private String addressDetails;

	
	public SearchOrdersDTO() {
	}


	public String getOrderNumber() {
		return orderNumber;
	}


	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}


	public String getTransactionNumber() {
		return transactionNumber;
	}


	public void setTransactionNumber(String transactionNumber) {
		this.transactionNumber = transactionNumber;
	}


	public String getPurchaseDate() {
		return purchaseDate;
	}


	public void setPurchaseDate(String purchaseDate) {
		this.purchaseDate = purchaseDate;
	}


	public String getPaymentMethod() {
		return paymentMethod;
	}


	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}


	public String getMerchantName() {
		return merchantName;
	}


	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}


	public String getAddressDetails() {
		return addressDetails;
	}


	public void setAddressDetails(String addressDetails) {
		this.addressDetails = addressDetails;
	}


	@Override
	public String toString() {
		return "SearchOrdersDTO [orderNumber=" + orderNumber 
				+ ", transactionNumber=" + transactionNumber
				+ ", purchaseDate=" + purchaseDate 
				+ ", paymentMethod=" + paymentMethod 
				+ ", merchantName=" + merchantName 
				+ ", addressDetails=" + addressDetails + "]";
	}
	
	

}
