<%@ include file="common/header.jspf"%>

<h1>Error Page</h1>
<p>Application has encountered an error. Please contact support on ...</p>
   
<!-- <div class="container"> -->
	
	  Failed URL: ${url}
	  Exception:  ${exception.message}
	  <c:forEach items="${exception.stackTrace}" var="ex">    
	      ${ex} 
	  </c:forEach>
	
<!-- </div> -->

<%@ include file="common/footer.jspf"%>