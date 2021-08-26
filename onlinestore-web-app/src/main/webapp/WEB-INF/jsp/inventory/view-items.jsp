<%@ include file="../common/header.jspf"%>

    <div class="container">
    
		<c:if test="${empty items}">
            <div class="text-info">No Items available in Inventory! Add some items!</div>
            <br/>
        </c:if>    
    	<c:if test="${isItemAddedSuccessfully}">
            <div class="text-success">Item Added Successfully: ${savedItem}</div>
            <br/>
        </c:if>
        <c:if test="${isItemUpdatedSuccessfully}">
            <div class="text-success">Item Updated Successfully: ${updatedItem}</div>
            <br/>
        </c:if>
        <c:if test="${isItemRemovedSuccessfully}">
            <div class="text-success">Item Removed Successfully</div>
            <br/>
        </c:if>
        <c:if test="${noItemsFound}">
            <div class="text-info">No Items Found!</div>
            <br/>
        </c:if>
        
    	<c:if test="${not empty items}">
	        <table class="table table-striped">
	            <caption>Inventory Items:</caption>
	            <thead>
	                <tr>
	                    <th>ItemId</th>
	                    <th>Category</th>
	                    <th>Sub-Category</th>
	                    <th>Name</th>
	                    <th>Quantity</th>
	                    <th>Price</th>
	                    <th></th>
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
	                        <!-- type="button"  -->
	                        <td><a class="btn btn-success" href="/onlinestore/update-inventory-item?itemId=${item.itemId}">Update</a></td>
							<!-- type="button"  -->
							<td><a class="btn btn-warning" href="/onlinestore/delete-inventory-item?itemId=${item.itemId}">Delete</a></td>
	                    </tr>
	                </c:forEach>
	            </tbody>
	        </table>
        </c:if>
        
        <%@ include file="actions.jspf"%>
        
    </div>
    
<%@ include file="../common/footer.jspf"%>