<%@ include file="../common/header.jspf"%>

    <div class="container">

    	<c:if test="${not empty cart}">
    	
	        <table class="table table-sm">
	            <caption>Cart Details:</caption>
	                <tr>
	                    <th>ConsumerId</th>
	                    <td>${cart.consumerId}</td>
	                </tr>
	                <tr>
	                    <th>CartId</th>
	                    <td>${cart.cartId}</td>
	                </tr>
	                <tr>
	                    <th>ItemCount</th>
	                    <td>${cart.itemCount}</td>
	                </tr>
	                <tr>
	                    <th>Total Amount</th>
	                    <td>${cart.computeAmount()}</td>
	                </tr>
	        </table>
        
	    	<c:if test="${not empty cart.items}">
		        <table class="table table-striped">
		            <thead>
		                <tr>
		                    <th>ItemId</th>
		                    <th>Category</th>
		                    <th>Sub-Category</th>
		                    <th>Name</th>
		                    <th>Quantity</th>
		                    <th>Price</th>
		                </tr>	                
		            </thead>
		            <tbody>
	                	<c:forEach items="${cart.items}" var="item">
		                    <tr>
		                        <td>${item.itemId}</td>
		                        <td>${item.category}</td>
		                        <td>${item.subCategory}</td>
		                        <td>${item.name}</td>
		                        <td>${item.quantity}</td>
		                        <td>${item.price}</td>
		                    </tr>
		                </c:forEach>
		            </tbody>
		        </table>
		        <div>
		       		<a class="btn btn-success" href="/onlinestore/go-for-checkout?cartId=${cart.cartId}">Checkout</a>	
		       		<a class="btn btn-warning" href="/onlinestore/empty-cart?cartId=${cart.cartId}">Empty Cart</a>
		       	</div>
	        </c:if>
        
        </c:if>
        
        <%@ include file="actions.jspf"%>
        
    </div>
    
<%@ include file="../common/footer.jspf"%>