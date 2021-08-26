<%@ include file="../common/header.jspf"%>

    <div class="container">
    
        <c:if test="${empty carts}">
            <div class="text-info">No Carts Found! Do some Shopping!</div>
        </c:if>
        
        <br/>
    
    	<c:if test="${not empty carts}">
	        <table class="table table-striped">
	            <caption>Carts:</caption>
	            <thead>
	                <tr>
	                    <th>ConsumerId</th>
	                    <th>CartId</th>
	                    <th>ItemCount</th>
	                    <th>Total Amount</th>
	                    <th></th>
	                    <th></th>
	                    <th></th>
	                </tr>
	            </thead>
	            <tbody>
	                <c:forEach items="${carts}" var="cart">
	                    <tr>
	                        <td>${cart.consumerId}</td>
	                        <td>${cart.cartId}</td>
	                        <td>${cart.itemCount}</td>
	                        <td>${cart.computeAmount()}</td>
	                        <td><a class="btn btn-success" href="/onlinestore/view-cart-details?cartId=${cart.cartId}">View Details</a></td>
	                        <td><a class="btn btn-success" href="/onlinestore/go-for-checkout?cartId=${cart.cartId}">Checkout</a></td>
	                        <td><a class="btn btn-warning" href="/onlinestore/empty-cart?cartId=${cart.cartId}">Empty Cart</a></td>
	                    </tr>
	                </c:forEach>
	            </tbody>
	        </table>
        </c:if>
        
        <%@ include file="actions.jspf"%>
        
    </div>
    
<%@ include file="../common/footer.jspf"%>