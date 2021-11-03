package com.learning.ddd.onlinestore.payment.domain;

import java.util.Date;

import org.springframework.stereotype.Component;

import com.learning.ddd.onlinestore.order.domain.MerchantInfo;
import com.learning.ddd.onlinestore.order.domain.Order;
import com.learning.ddd.onlinestore.order.domain.OrderTransaction;
import com.learning.ddd.onlinestore.order.domain.TransactionStatus;

@Component
public class DummyPaymentGateway implements PaymentGateway {
	
	@Override
	public OrderTransaction doPayment(Order order) {
		OrderTransaction onlineTxn = new OrderTransaction();
		onlineTxn.setTransactionStatus(TransactionStatus.APPROVED);
		onlineTxn.setPaymentMethod(order.getPaymentMethod());
		onlineTxn.setPurchaseDate(new Date());
		onlineTxn.setItems(order.getItems());
		onlineTxn.setTotalItems(order.getItemCount());
		onlineTxn.setTotalAmount(order.getAmount());
		onlineTxn.setMerchantInfo(
			new MerchantInfo("TestMerchant","123-456-789","test@email.com",
					"adrsLine1","adrsLine2","adrsLine3")
		);
		return onlineTxn;
	}
	
}