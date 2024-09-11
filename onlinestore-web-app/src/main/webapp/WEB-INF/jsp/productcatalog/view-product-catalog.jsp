<%@ include file="../common/header.jspf"%>

    <div class="container">
    
		<c:if test="${empty products}">
            <div class="text-info">No Products available in Inventory! Add some products!</div>
            <br/>
        </c:if>    
    	<c:if test="${isProductShoppedSuccessfully}">
            <div class="text-success">Product added successfully to Cart: ${savedProduct}</div>
            <br/>
          
        </c:if>
        <%-- <c:if test="${isProductUpdatedSuccessfully}">
            <div class="text-success">Product updated Successfully: ${updatedProduct}</div>
            <br/>
        </c:if>
        <c:if test="${isProductRemovedSuccessfully}">
            <div class="text-success">Product removed Successfully</div>
            <br/>
        </c:if>
        <c:if test="${noProductsFound}">
            <div class="text-info">No Products Found!</div>
            <br/>
        </c:if> --%>
        
    	<c:if test="${not empty products}">
	        <table class="table table-striped">
	            <caption>Product Catalog:</caption>
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
	                        <td><a class="btn btn-success" href="/onlinestore/shop-product?productId=${product.productId}&cartId=${cartId}">Add Product to Cart</a></td>
	                    </tr>
	                </c:forEach>
	            </tbody>
	        </table>
        </c:if>
        
        <%@ include file="actions.jspf"%>
        
    </div>
    
<%@ include file="../common/footer.jspf"%>