<%@ include file="../common/header.jspf"%>

   	<div class="container">

    	<c:if test="${empty items}">
            <div class="text-info">No Items available in Inventory to Shop!</div>
            <br/>
        </c:if>
    	<c:if test="${isItemShoppedSuccessfully}">
            <div class="text-success">Item added successfully to Cart!</div>
            <br/>
        </c:if>
    	
    	<c:if test="${not empty items}">
	        <table class="table table-striped">
	            <caption>Available Items for Shopping:</caption>
	            <thead>
	                <tr>
	                    <th>ItemId</th>
	                    <th>Category</th>
	                    <th>Sub-Category</th>
	                    <th>Name</th>
	                    <th>Quantity</th>
	                    <th>Price</th>
	                    <th></th>
	                </tr>
	            </thead>
	            <tbody>
	                <c:forEach items="${items}" var="item">
	                    <tr>
	                        <td>${item.itemId}</td>
	                        <td>${item.category}</td>
	                        <td>${item.subCategory}</td>
	                        <td>${item.name}</td>
	                        <td>${item.quantity}</td>
	                        <td>${item.price}</td>
	                        <td><a class="btn btn-success" href="/onlinestore/shop-item?inventoryItemId=${item.itemId}&cartId=${cartId}">Add Item to Cart</a></td>
	                    </tr>
	                </c:forEach>
	            </tbody>
	        </table>
        </c:if>
        
        <%@ include file="actions.jspf"%>
        
    </div>

<%@ include file="../common/footer.jspf"%>
