<%@ include file="../common/header.jspf"%>

   <div class="container">
    
       <form:form action="/onlinestore/add-inventory-item" method="post" modelAttribute="item">
        	<table  class="table table-striped">
	            <tr>
	            	<td><form:label path="category">Category: </form:label></td>
	            	<td><form:input type="text" path="category"/></td>
	            <tr>
	            <tr>
	            	<td><form:label path="subCategory">Sub Category: </form:label></td>
	            	<td><form:input type="text" path="subCategory"/></td>
	            <tr>
	            <tr>
	            	<td><form:label path="name">Name: </form:label></td>
	            	<td><form:input type="text" path="name"/></td>
	            <tr>
	            <tr>
	            	<td><form:label path="quantity">Quantity: </form:label></td>
	            	<td><form:input type="text" path="quantity"/></td>
	            <tr>
	            <tr>
	            	<td><form:label path="price">Price: </form:label></td>
	            	<td><form:input type="text" path="price"/></td>
	            <tr>
            	<tr>
                	<td colspan="2" align="center"><input type="submit" value="submit" class="btn btn-default" /></td>
                </tr>
            </table>
        </form:form>
        
        <%@ include file="actions.jspf"%>
        
    </div>

<%@ include file="../common/footer.jspf"%>
