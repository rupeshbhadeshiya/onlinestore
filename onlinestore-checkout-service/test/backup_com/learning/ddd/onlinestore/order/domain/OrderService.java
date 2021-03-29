package backup_com.learning.ddd.onlinestore.order.domain;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import backup_com.learning.ddd.onlinestore.payment.domain.Address;
import backup_com.learning.ddd.onlinestore.payment.domain.PaymentMethod;

@Service
public class OrderService {

	@Autowired
	private OrderRepository orderRepository;
	
	public Order createOrder(Cart cart, PaymentMethod paymentMethod,
		Address billingAddress, Address shipingAddress
			) {
		
		Order order = new Order(
			1,
			new Date(),
			"Temp_Merchant",
			cart.getItems(),
			cart.getItemCount(),
			cart.computeAmount(),
			paymentMethod,
			billingAddress,
			shipingAddress,
			null,
			null
		);
		
		return null;
	}

}
