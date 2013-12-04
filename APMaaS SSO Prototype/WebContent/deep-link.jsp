<%@ page import="com.compuware.apmng.sso.UserContext" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title>APMaaS for Macy's - Deep Link</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<body style="background-color: #99CCFF">
<h1>APMaaS for Macy's - Deep Link</h1>
<h2>User has been authenticated</h2>


<p>
<table>
    <tr>
        <td colspan="2"><b>Principal's Attributes</b></td>
    </tr>

    <c:forEach var="attribute"
               items="${userContext.attributes}">
        <tr>
            <td width="300"><c:out value="${attribute.key}"/></td>
            <td><c:out value="${attribute.value}"/></td>
        </tr>
    </c:forEach>
</table>
</p>

<p>
    <a href="<c:url value="/"/>">Home</a><br/>
    <a href="<c:url value="/logout"/>">Global Logout</a><br/>
</p>

</body>
</html>