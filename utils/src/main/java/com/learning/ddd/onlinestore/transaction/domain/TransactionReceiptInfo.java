package com.learning.ddd.onlinestore.transaction.domain;

import java.util.Date;

public class TransactionReceiptInfo {

	private String receiptNo;
	private Date purchaseDate;
	
	public TransactionReceiptInfo() {
	}
	
	public TransactionReceiptInfo(String receiptNo, Date purchaseDate) {
		super();
		this.receiptNo = receiptNo;
		this.purchaseDate = purchaseDate;
	}

	public String getReceiptNo() {
		return receiptNo;
	}

	public void setReceiptNo(String receiptNo) {
		this.receiptNo = receiptNo;
	}

	public Date getPurchaseDate() {
		return purchaseDate;
	}
	
	public void setPurchaseDate(Date purchaseDate) {
		this.purchaseDate = purchaseDate;
	}

	@Override
	public String toString() {
		return "ReceiptInfo [receiptNo=" + receiptNo + ", purchaseDate=" + purchaseDate + "]";
	}

}
