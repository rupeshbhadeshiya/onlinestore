package com.learning.ddd.onlinestore.payment.domain;

import java.util.ArrayList;
import java.util.Date;

import com.learning.ddd.onlinestore.transaction.domain.ItemInfo;
import com.learning.ddd.onlinestore.transaction.domain.MerchantInfo;
import com.learning.ddd.onlinestore.transaction.domain.ReceiptInfo;
import com.learning.ddd.onlinestore.transaction.domain.TransactionReceipt;
import com.learning.ddd.onlinestore.transaction.domain.TransactionStatus;

public class DummyPaymentGateway extends PaymentGateway {
	
	@Override
	public TransactionReceipt doPayment(PaymentMethod paymentMethod, Double amount) {
		TransactionReceipt txnReceipt = new TransactionReceipt();
		txnReceipt.setTransactionStatus(TransactionStatus.APPROVED);
		txnReceipt.setPaymentMethod(paymentMethod);
		txnReceipt.setReceiptInfo(new ReceiptInfo("12345", new Date()));
		txnReceipt.setMerchantInfo(new MerchantInfo("TestMerchant","123-123-123","test@email.com","a1","a2","a3"));
		txnReceipt.setItemInfo(new ItemInfo(new ArrayList<>(), 0, 0.0));
		return txnReceipt;
	}
	
}