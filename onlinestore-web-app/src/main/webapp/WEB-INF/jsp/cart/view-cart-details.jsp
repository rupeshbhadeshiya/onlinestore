<%@ include file="../common/header.jspf"%>

    <div class="container">
    
        <c:if test="${isProductShoppedSuccessfully}">
            <div class="text-success">Product removed from Cart</div>
            <br/>
        </c:if>
        <c:if test="${empty cart}">
            <div class="text-warning">No Product in Cart! Do some Shopping!</div>
            <br/>
        </c:if>

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
	                    <th>ProductCount</th>
	                    <td>${cart.getProductCount()}</td>
	                </tr>
	                <tr>
	                    <th>Total Amount</th>
	                    <td>${cart.computeAmount()}</td>
	                </tr>
	        </table>
        
	    	<c:if test="${not empty cart.getProducts()}">
		        <table class="table table-striped">
		            <thead>
		                <tr>
		                    <th>ProductId</th>
		                    <th>Category</th>
		                    <th>Sub-Category</th>
		                    <th>Name</th>
		                    <th>Quantity</th>
		                    <th>Price</th>
		                    <th></th>
		                </tr>	                
		            </thead>
		            <tbody>
	                	<c:forEach items="${cart.getProducts()}" var="product">
		                    <tr>
		                        <td>${product.productId}</td>
		                        <td>${product.category}</td>
		                        <td>${product.subCategory}</td>
		                        <td>${product.name}</td>
		                        <td>${product.quantity}</td>
		                        <td>${product.price}</td>
		                        <td><a class="btn btn-warning" href="/onlinestore/remove-product-from-cart?cartId=${cart.cartId}&productId=${product.productId}">Remove Product from Cart</a></td>
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
        
        <br/>
        <%@ include file="actions.jspf"%>
        
    </div>
    
<%@ include file="../common/footer.jspf"%>