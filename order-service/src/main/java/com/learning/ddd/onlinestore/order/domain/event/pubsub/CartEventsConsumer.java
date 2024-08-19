package com.learning.ddd.onlinestore.order.domain.event.pubsub;

public class CartEventsConsumer {
	
}

//@Component
//public class CartEventsConsumer extends DomainEventsConsumer {
//
//	public static final String SERVICE_COMPONENT = "==== [order-service] " + CartEventsConsumer.class.getSimpleName();
//	
//	@Autowired
//	private CartInfoRepository CartInfoRepository;
//	
//	
//	@Value("${onlinestore.cart.events.topic.name:CartEventsTopic}")
//	private String topicName;
//	
//	
//	@Override
//	protected String getCallingServiceName() {
//		return "[order-service] " + CartEventsConsumer.class.getSimpleName();
//	}
//	
//	
//	@Override
//	protected String getTopicName() {
//		return topicName;
//	}
//	
//	
//	@Override
//	protected void consumeDomainEvent(OnlinestoreDomainEvent domainEvent) throws CloneNotSupportedException {
//
//		if (domainEvent.getEventName().equals(OnlinestoreDomainEventName.PRODUCT_ADDED_TO_CART)) {
//				
//			ProductAddedToCartEvent event = (ProductAddedToCartEvent) domainEvent;
//			
//			CartInfo CartInfo = event.getCartInfo();
//			Product product = event.getProduct();
//			
//			// ... persist Cart to local DB for referring it when an Order is placed
//			// this will avoid doing http call to cart-service thereby making 
//			// order-service autonomous one
//
//			// ...
//			// here saving Cart object will ensure any newly added CartItem also gets stored in local data store
//			CartInfoRepository.save(CartInfo);
//			
//			System.out.println(
//				SERVICE_COMPONENT + " - Event: " + domainEvent.getEventName()
//				+ ", Product added to Cart = " + product
//				+ ", Created/Updated Cart to local data store, Cart = " + CartInfo
//			);
//		
//		} else if (domainEvent.getEventName().equals(OnlinestoreDomainEventName.PRODUCT_REMOVED_FROM_CART)) {
//
//			ProductRemovedFromCartEvent event = (ProductRemovedFromCartEvent) domainEvent;
//			
//			CartInfo CartInfo = event.getCartInfo();
//			Product product = event.getProduct();
//			
//			// ... persist Cart to local DB for referring it when an Order is placed
//			// this will avoid doing http call to cart-service thereby making 
//			// order-service autonomous one
//			
//			// ...
//			// here saving Cart object will ensure any removed CartItem also gets deleted from local data store
//			CartInfoRepository.save(CartInfo);
//			
//			System.out.println(
//				SERVICE_COMPONENT + " - Event: " + domainEvent.getEventName()
//				+ ", Product removed from Cart = " + product
//				+ ", Deleted relevant Cart and associated Products (CartItems) from "
//				+ "local data store, CartInfo = " + CartInfo
//			);
//		
//		} else if (domainEvent.getEventName().equals(OnlinestoreDomainEventName.CART_EMPTIED_DUE_TO_ORDER_CREATION) || 
//				domainEvent.getEventName().equals(OnlinestoreDomainEventName.CART_EMPTIED_BY_CONSUMER)) {
//			
//			CartEmptiedEvent event = (CartEmptiedEvent) domainEvent;
//			
//			Integer cartId = (Integer) event.getCartId();
//			
//			// ... delete the Cart from local data store which will also delete 
//			// associated CartItems i.e. Products in the Cart from local data store
//			
//			CartInfoRepository.deleteById(cartId);
//			
//			System.out.println(
//				SERVICE_COMPONENT + " - Event: " + domainEvent.getEventName() 
//				+ ", CartId = " + cartId
//				+ ", Deleted relevant Cart and associated Products (CartInfo) from "
//				+ "local data store"
//			);
//			
//		} // else-if
//		
//	}
//
//}
