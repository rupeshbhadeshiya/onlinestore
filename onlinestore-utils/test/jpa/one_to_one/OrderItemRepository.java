package jpa.one_to_one;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {

	@Modifying
	@Query("delete from OrderItem item where "
			+ "item.itemId=:itemId "
			+ " OR item.category=:category"
			+ " OR item.subCategory=:subCategory"
			+ " OR item.name=:name"
			+ " OR item.price=:price"
			+ " OR item.quantity=:quantity")
	void deleteItems(@Param("itemId") Integer itemId, 
			@Param("category") String category, @Param("subCategory") String subCategory,
			@Param("name") String name, @Param("price") Double price, @Param("quantity") Integer quantity);

}
