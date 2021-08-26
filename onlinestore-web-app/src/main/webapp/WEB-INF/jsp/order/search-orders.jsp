<%@ include file="../common/header.jspf"%>

   <div class="container">
    
       <form:form action="/onlinestore/search-orders" method="post" modelAttribute="searchOrdersRequestDTO">
        	<table  class="table table-striped">
	            <tr>
	            	<td><form:label path="orderNumber">Order Number: </form:label></td>
	            	<td><form:input type="text" path="orderNumber"/></td>
	            <tr>
	            <tr>
	            	<td><form:label path="transactionNumber">Transaction Number: </form:label></td>
	            	<td><form:input type="text" path="transactionNumber"/></td>
	            <tr>	            
	            <tr>
	            	<td><form:label path="purchaseDate">Purchase Date: </form:label></td>
	            	<td><form:input type="text" path="purchaseDate"/></td>
	            <tr>
	            <tr>
	            	<td><form:label path="paymentMethod">Payment Method: </form:label></td>
	            	<td><form:input type="text" path="paymentMethod"/></td>
	            <tr>
	            <tr>
	            	<td><form:label path="itemDetails">Item Details: </form:label></td>
	            	<td><form:input type="text" path="itemDetails"/></td>
	            <tr>
	            <tr>
	            	<td><form:label path="addressDetails">Address Details: </form:label></td>
	            	<td><form:input type="text" path="addressDetails"/></td>
	            <tr>
	            <tr>
	            	<td><form:label path="merchantName">Merchant Name: </form:label></td>
	            	<td><form:input type="text" path="merchantName"/></td>
	            <tr>
            	<tr>
                	<td colspan="2" align="center"><input type="submit" value="submit" class="btn btn-default" /></td>
                </tr>
            </table>
        </form:form>
        
        <%@ include file="actions.jspf"%>
        
    </div>

<%@ include file="../common/footer.jspf"%>