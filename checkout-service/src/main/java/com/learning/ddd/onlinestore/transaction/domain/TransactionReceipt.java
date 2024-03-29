package com.learning.ddd.onlinestore.transaction.domain;

import com.learning.ddd.onlinestore.payment.domain.PaymentMethod;

public class TransactionReceipt {

	// Merchant details (name, address, phone/email)
	// Receipt/Bill no., Receipt/Bill date, 
	// Item details: each item brief, rate, total such items, total value for each set of items
	// Total details: total items, total amount
	// payment method used
	// discount given etc
	
	private TransactionStatus transactionStatus;
	private MerchantInfo merchantInfo;
	private ReceiptInfo receiptInfo;
	private ItemInfo itemInfo;
	private PaymentMethod paymentMethod;
	
	public TransactionReceipt() {
	}
	
	public TransactionReceipt(TransactionStatus transactionStatus, MerchantInfo merchantInfo, 
			ReceiptInfo receiptInfo, ItemInfo itemInfo, PaymentMethod paymentMethod) {
		super();
		this.transactionStatus = transactionStatus;
		this.merchantInfo = merchantInfo;
		this.receiptInfo = receiptInfo;
		this.itemInfo = itemInfo;
		this.paymentMethod = paymentMethod;
	}

	public TransactionStatus getTransactionStatus() {
		return transactionStatus;
	}

	public void setTransactionStatus(TransactionStatus transactionStatus) {
		this.transactionStatus = transactionStatus;
	}

	public MerchantInfo getMerchantInfo() {
		return merchantInfo;
	}

	public void setMerchantInfo(MerchantInfo merchantInfo) {
		this.merchantInfo = merchantInfo;
	}

	public ReceiptInfo getReceiptInfo() {
		return receiptInfo;
	}

	public void setReceiptInfo(ReceiptInfo receiptInfo) {
		this.receiptInfo = receiptInfo;
	}

	public ItemInfo getItemInfo() {
		return itemInfo;
	}

	public void setItemInfo(ItemInfo itemInfo) {
		this.itemInfo = itemInfo;
	}

	public PaymentMethod getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(PaymentMethod paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	@Override
	public String toString() {
		return "TransactionReceipt [transactionStatus=" + transactionStatus + ", merchantInfo=" + merchantInfo
				+ ", receiptInfo=" + receiptInfo + ", itemInfo=" + itemInfo + ", paymentMethod=" + paymentMethod + "]";
	}
	
}
