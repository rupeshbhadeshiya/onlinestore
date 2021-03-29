package jpa.one_to_one;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class OrderService {

//	@Autowired
//	private OrderRepository orderRepository;

	public Order createOrder(Cart cart, PaymentMethod paymentMethod,
								Address billingAddress, Address shipingAddress) {
		
		Order order = new Order(
			convertToOrderItems(cart.getItems()),
			cart.getItemCount(),
//			cart.computeAmount(),
			paymentMethod,
			billingAddress,
			shipingAddress
		);
		
		return order;
	}

	private List<OrderItem> convertToOrderItems(List<CartItem> cartItems) {
		List<OrderItem> orderItems = new ArrayList<OrderItem>();
		for (CartItem cartItem : cartItems) {
			OrderItem orderItem = new OrderItem(cartItem.getCategory(), cartItem.getSubCategory(), 
				cartItem.getName(), cartItem.getQuantity(), cartItem.getPrice());
			orderItems.add(orderItem);
		}
		return orderItems;
	}

}
