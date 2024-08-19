<%@ include file="../common/header.jspf"%>

    <div class="container">
    
		<c:if test="${empty products}">
            <div class="text-info">No Products available in Inventory! Add some products!</div>
            <br/>
        </c:if>    
    	<%-- <c:if test="${isProductAddedSuccessfully}">
            <div class="text-success">Product Added Successfully: ${savedProduct}</div>
            <br/>
        </c:if>
        <c:if test="${isProductUpdatedSuccessfully}">
            <div class="text-success">Product Updated Successfully: ${updatedProduct}</div>
            <br/>
        </c:if>
        <c:if test="${isProductRemovedSuccessfully}">
            <div class="text-success">Product Removed Successfully</div>
            <br/>
        </c:if>
        <c:if test="${noProductsFound}">
            <div class="text-info">No Products Found!</div>
            <br/>
        </c:if> --%>
        
    	<c:if test="${not empty products}">
	        <table class="table table-striped">
	            <caption>All Products from Product Catalog:</caption>
	            <thead>
	                <tr>
	                    <th>ProductId</th>
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
	                <c:forEach items="${products}" var="product">
	                    <tr>
	                        <td>${product.productId}</td>
	                        <td>${product.category}</td>
	                        <td>${product.subCategory}</td>
	                        <td>${product.name}</td>
	                        <td>${product.quantity}</td>
	                        <td>${product.price}</td>
	                        <!-- type="button"  -->
	                        <%-- <td><a class="btn btn-success" href="/onlinestore/update-inventory-item?itemId=${item.itemId}">Update</a></td> --%>
							<!-- type="button"  -->
							<%-- <td><a class="btn btn-warning" href="/onlinestore/delete-inventory-item?itemId=${item.itemId}">Delete</a></td> --%>
	                    </tr>
	                </c:forEach>
	            </tbody>
	        </table>
        </c:if>
        
        <%@ include file="actions.jspf"%>
        
    </div>
    
<%@ include file="../common/footer.jspf"%>