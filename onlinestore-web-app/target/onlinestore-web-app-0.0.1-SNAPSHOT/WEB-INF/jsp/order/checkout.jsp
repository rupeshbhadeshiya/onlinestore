<%@ include file="../common/header.jspf"%>

	<!-- Cart details -->
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
        
    </div>
    
    <br/>
	
   	<div class="container">
    
       <form:form action="/onlinestore/checkout" method="post" modelAttribute="order">
 
        	<table  class="table table-striped">
        	
				<!-- Cart details -->
				<input type="hidden" name="cartId" value="${cart.cartId}"/>     	
        	
        		<!-- Billing Address -->
        		
        		<tr>
	            	<th colspan="2">Enter Billing Address:</th>
	            <tr>
        		
        		<input type="hidden" name="billingAddress.addressType" value="BILLING_ADDRESS"/>
	            
	            <tr>
	            	<td><form:label path="billingAddress.line1">Line1: </form:label></td>
	            	<td><form:input type="text" path="billingAddress.line1"/></td>
	            <tr>
	            <tr>
	            	<td><form:label path="billingAddress.line2">Line2: </form:label></td>
	            	<td><form:input type="text" path="billingAddress.line2"/></td>
	            <tr>
	            <tr>
	            	<td><form:label path="billingAddress.city">City: </form:label></td>
	            	<td><form:input type="text" path="billingAddress.city"/></td>
	            <tr>
	            <tr>
	            	<td><form:label path="billingAddress.state">State: </form:label></td>
	            	<td><form:input type="text" path="billingAddress.state"/></td>
	            <tr>
	            <tr>
	            	<td><form:label path="billingAddress.postalCode">Postal Code: </form:label></td>
	            	<td><form:input type="text" path="billingAddress.postalCode"/></td>
	            <tr>
           		<tr>
	            	<td><form:label path="billingAddress.country">Country: </form:label></td>
	            	<td><form:input type="text" path="billingAddress.country"/></td>
	            <tr>	       

	 			<!-- Shipping Address -->
        		
        		<tr>
	            	<th colspan="2">Enter Shipping Address:</th>
	            <tr>
        		
        		<input type="hidden" name="shippingAddress.addressType" value="SHIPPING_ADDRESS"/>
	            
	            <tr>
	            	<td><form:label path="shippingAddress.line1">Line1: </form:label></td>
	            	<td><form:input type="text" path="shippingAddress.line1"/></td>
	            <tr>
	            <tr>
	            	<td><form:label path="shippingAddress.line2">Line2: </form:label></td>
	            	<td><form:input type="text" path="shippingAddress.line2"/></td>
	            <tr>
	            <tr>
	            	<td><form:label path="shippingAddress.city">City: </form:label></td>
	            	<td><form:input type="text" path="shippingAddress.city"/></td>
	            <tr>
	            <tr>
	            	<td><form:label path="shippingAddress.state">State: </form:label></td>
	            	<td><form:input type="text" path="shippingAddress.state"/></td>
	            <tr>
	            <tr>
	            	<td><form:label path="shippingAddress.postalCode">Postal Code: </form:label></td>
	            	<td><form:input type="text" path="shippingAddress.postalCode"/></td>
	            <tr>
           		<tr>
	            	<td><form:label path="shippingAddress.country">Country: </form:label></td>
	            	<td><form:input type="text" path="shippingAddress.country"/></td>
	            <tr>	       
            
            	<!-- Payment Method - a drop-down list -->
            	<tr>
	            	<th colspan="1">Select Payment Method:</th>
	            	<td colspan="1"><form:select title="Payment Method: " path="paymentMethod" items="${paymentMethods}" /></td>
	            <tr>	                   
            
 				<!-- Submit button -->
            	<tr>
                	<td colspan="2" align="center"><input type="submit" value="submit" class="btn btn-default" /></td>
                </tr>
            
            </table>                          
            
        </form:form>
        
	</div>
	
	<%@ include file="actions.jspf"%>

<%@ include file="../common/footer.jspf"%>