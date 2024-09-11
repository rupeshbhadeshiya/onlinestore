<%@ include file="../common/header.jspf"%>

   <div class="container">
    
    	<c:if test="${param.error}">
            <div class="text-success">Invalid username and/or password.</div>
            <br/>
        </c:if>

    	<c:if test="${param.logout}">
            <div class="text-success">You have been logged out.</div>
            <br/>
        </c:if>
    
       <%-- <form:form action="/onlinestore/login" method="post" modelAttribute="onlinestoreUser"> --%>
       <form:form action="@{/login}" method="post">
        	<table  class="table table-striped">
	            <tr>
	            	<td><form:label path="userName">User Name: </form:label></td>
	            	<%-- <td><form:input type="text" path="userName"/></td> --%>
	            <tr>
	            <tr>
	            	<td><form:label path="password">Password: </form:label></td>
	            	<%-- <td><form:input type="text" path="password"/></td> --%>
	            <tr>
            	<tr>
                	<td colspan="2" align="center"><input type="submit" value="Sign In" class="btn btn-success" /></td>
                </tr>
            </table>
        </form:form>
        
        <%@ include file="actions.jspf"%>
        
    </div>

<%@ include file="../common/footer.jspf"%>
