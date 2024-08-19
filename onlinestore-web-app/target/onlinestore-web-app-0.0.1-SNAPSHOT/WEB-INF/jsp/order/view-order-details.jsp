<%@ include file="../common/header.jspf"%>


   	<div class="container">
   	
    	<c:if test="${isOrderCreatedSuccessfully}">
            <div class="text-success">Order created Successfully</div>
            <br/>
        </c:if>  
    
    	<c:if test="${not empty order}">
    	
	        <table class="table table-striped">
	            <caption>Order Details:</caption>
	                <tr>
	                    <th>ConsumerId</th>
	                    <td>${order.consumerId}</td>
	                </tr>
	                <tr>
	                    <th>Order Number</th>
	                    <td>${order.orderNumber}</td>
	                </tr>
	                <tr>
	                    <th>Purchase Date</th>
	                    <td>${order.creationDate}</td>
	                </tr>	                
	                <tr>
	                    <th>Item Count</th>
	                    <td>${order.itemCount}</td>
	                </tr>
	                <tr>
	                    <th>Amount</th>
	                    <td>${order.amount}</td>
	                </tr>
	                <tr>
	                    <th>Payment Method</th>
	                    <td>${order.paymentMethod}</td>
	                </tr>
	                <tr>
	                    <th>Billing Address</th>
	                    <td>
					       	<table  class="table table-striped">
				        		<tr>
				                    <th>Address Type</th>
				                    <td>${order.billingAddress.addressType}</td>
				                </tr>
				        		<tr>
				                    <th>Line1</th>
				                    <td>${order.billingAddress.line1}</td>
				                </tr>
				        		<tr>
				                    <th>Line2</th>
				                    <td>${order.billingAddress.line2}</td>
				                </tr>
				        		<tr>
				                    <th>City</th>
				                    <td>${order.billingAddress.city}</td>
				                </tr>
				        		<tr>
				                    <th>State</th>
				                    <td>${order.billingAddress.state}</td>
				                </tr>
				        		<tr>
				                    <th>Postal Code</th>
				                    <td>${order.billingAddress.postalCode}</td>
				                </tr>				                				                				                				                
				        		<tr>
				                    <th>Country</th>
				                    <td>${order.billingAddress.country}</td>
				                </tr>
	                    	</table>
	                    </td>
	                </tr>
	                <tr>
	                    <th>Shipping Address</th>
	                    <td>
					       	<table  class="table table-striped">
				        		<tr>
				                    <th>Address Type</th>
				                    <td>${order.shippingAddress.addressType}</td>
				                </tr>
				        		<tr>
				                    <th>Line1</th>
				                    <td>${order.shippingAddress.line1}</td>
				                </tr>
				        		<tr>
				                    <th>Line2</th>
				                    <td>${order.shippingAddress.line2}</td>
				                </tr>
				        		<tr>
				                    <th>City</th>
				                    <td>${order.shippingAddress.city}</td>
				                </tr>
				        		<tr>
				                    <th>State</th>
				                    <td>${order.shippingAddress.state}</td>
				                </tr>
				        		<tr>
				                    <th>Postal Code</th>
				                    <td>${order.shippingAddress.postalCode}</td>
				                </tr>				                				                				                				                
				        		<tr>
				                    <th>Country</th>
				                    <td>${order.shippingAddress.country}</td>
				                </tr>
	                    	</table>
	                    </td>
	                </tr>	 
	                <tr>
	                	<th>Transaction</th>
	                	<td>
					    	<c:if test="${not empty order.transactions}">
						        <table class="table table-striped">
						            <thead>
						                <tr>
						                    <th>Transaction Number</th>
						                    <th>Date</th>
						                    <th>Transaction Status</th>
						                    <th>Payment Method</th>
						                    <th>Total Items</th>
						                    <th>Total Amount</th>
						                    <th>Merchant Info</th>
						                </tr>	                
						            </thead>
						            <tbody>
					                	<c:forEach items="${order.transactions}" var="transaction">
						                    <tr>
						                        <td>${transaction.transactionNumber}</td>
						                        <td>${transaction.purchaseDate}</td>
						                        <td>${transaction.transactionStatus}</td>
						                        <td>${transaction.paymentMethod}</td>
						                        <td>${transaction.totalItems}</td>
						                        <td>${transaction.totalAmount}</td>
						                        <td>${transaction.merchantInfo}</td>
						                    </tr>
						                </c:forEach>
						            </tbody>
						        </table>
					        </c:if>
				        </td>               
	                </tr>  	                 
	                <tr>
	                	<th>Items</th>
	                	<td>
					    	<c:if test="${not empty order.items}">
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
					                	<c:forEach items="${order.items}" var="item">
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
					        </c:if>
				        </td>               
	                </tr>  
	                <tr>
	                	<td colspan="2" align="center"><a class="btn btn-warning" href="/onlinestore/cancel-order?orderId=${order.orderId}">Cancel Order</a></td>
	                </tr>            
	        </table>
    	
    	</c:if>
    	    
    	<%@ include file="actions.jspf"%>
    	    
	</div>
	
<%@ include file="../common/footer.jspf"%>