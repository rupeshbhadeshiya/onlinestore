<%@ include file="../common/header.jspf"%>

    <div class="container">
    
    	<c:if test="${isOrderCancelledSuccessfully}">
            <div class="text-success">Order cancelled successfully</div>
            <br/>
        </c:if>
        <c:if test="${empty orders}">
            <div class="text-info">No Order Found! Do some Shopping!</div>
            <br/>
        </c:if>
        <%-- <c:if test="${noOrdersFound}">
            <div class="text-info">No Order Found!</div>
            <br/>
        </c:if> --%>
        <c:if test="${numOrdersFound > 0}">
            <div class="text-success">${numOrdersFound} Order(s) Found</div>
            <br/>
        </c:if>  
        
    	<c:if test="${not empty orders}">
	        <table class="table table-striped">
	            <caption>Orders:</caption>
	            <thead>
	                <tr>
	                    <th>ConsumerId</th>
	                    <th>OrderNumber</th>
	                    <th>ItemCount</th>
	                    <th>Amount</th>
	                    <th>Payment Method</th>
	                    <th></th>
	                    <th></th>
	                </tr>
	            </thead>
	            <tbody>
	                <c:forEach items="${orders}" var="order">
	                    <tr>
	                        <td>${order.consumerId}</td>
	                        <td>${order.orderNumber}</td>
	                        <td>${order.itemCount}</td>
	                        <td>${order.amount}</td>
	                        <td>${order.paymentMethod}</td>
	                        <td><a class="btn btn-success" href="/onlinestore/view-order-details?orderId=${order.orderId}">View Details</a></td>
	                        <td><a class="btn btn-warning" href="/onlinestore/cancel-order?orderId=${order.orderId}">Cancel Order</a></td>
	                    </tr>
	                </c:forEach>
	            </tbody>
	        </table>
        </c:if>
        
        <%@ include file="actions.jspf"%>
        
    </div>
    
<%@ include file="../common/footer.jspf"%>
